package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.adapter.FilmAdapter
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentFilmsBinding
import ru.kpfu.itis.paramonov.androidtasks.di.ServiceLocator
import ru.kpfu.itis.paramonov.androidtasks.model.Film
import ru.kpfu.itis.paramonov.androidtasks.model.LikedFilms
import ru.kpfu.itis.paramonov.androidtasks.model.RvModel
import ru.kpfu.itis.paramonov.androidtasks.util.ParamKeys.Companion.NO_USER_ID
import ru.kpfu.itis.paramonov.androidtasks.util.ParamKeys.Companion.SHARED_PREF_USER_ID_KEY

class FilmsFragment: Fragment() {
    private var _binding: FragmentFilmsBinding? = null
    private val binding: FragmentFilmsBinding get() = _binding!!

    private var adapter: FilmAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        addFilms()
        setOnClickListeners()
    }

    private fun addFilms() {
        lifecycleScope.launch(Dispatchers.IO) {
            val rvModels: MutableList<RvModel> = ArrayList()
            val films = ServiceLocator.getDbInstance().filmDao.getAllFilms()
                .map { entity ->
                    Film.getFromEntity(entity)
                }

            val likedFilmIds = ServiceLocator.getDbInstance().filmRatingsDao.getUserLikedFilms(getUserId())
            if (likedFilmIds.isNotEmpty()) {
                rvModels.add(LikedFilms())
            }

            rvModels.addAll(films)

            withContext(Dispatchers.Main) {
                if (films.isNotEmpty()) {
                    binding.tvNoFilms.visibility = View.GONE
                    with(binding) {
                        val adapter = FilmAdapter(
                            context = requireContext(),
                            onFilmClicked = ::onFilmClicked,
                            onDeleteClicked = ::onDeleteClicked,
                            userId = getUserId(),
                            lifecycleScope = lifecycleScope
                        )
                        adapter.setItems(rvModels)
                        this@FilmsFragment.adapter = adapter

                        val gridLayoutManager = GridLayoutManager(
                            requireContext(),
                            2,
                            GridLayoutManager.VERTICAL,
                            false
                        ).apply {
                            spanSizeLookup = object : SpanSizeLookup() {
                                override fun getSpanSize(position: Int): Int {
                                    return when(rvModels[position]) {
                                        is Film -> 1
                                        is LikedFilms -> 2
                                        else -> throw RuntimeException()
                                    }
                                }
                            }
                        }

                        rvMovies.layoutManager = gridLayoutManager
                        rvMovies.adapter = adapter
                    }
                } else {
                    binding.tvNoFilms.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun getUserId(): Int {
        return ServiceLocator.getSharedPreferences().getInt(
            SHARED_PREF_USER_ID_KEY,
            NO_USER_ID
        )
    }

    private fun onFilmClicked(film: Film) {
        findNavController().navigate(
            R.id.action_filmsFragment_to_filmInfoFragment,
            bundleOf(BUNDLE_FILM_ID_KEY to film.id)
        )
    }

    private fun onDeleteClicked(fromLiked: Boolean, position: Int) {
        val film = adapter?.deleteItem(fromLiked, position)
        if (!fromLiked) {
            lifecycleScope.launch(Dispatchers.IO) {
                film?.id?.let {
                    ServiceLocator.getDbInstance().filmDao.deleteFilmById(it)
                }
            }
        } else {
            lifecycleScope.launch(Dispatchers.IO) {
                film?.id?.let {
                    ServiceLocator.getDbInstance().filmRatingsDao.updateMovieLiked(getUserId(), it, false)
                }
            }
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            fabAddFilm.setOnClickListener {
                findNavController().navigate(R.id.action_filmsFragment_to_addFilmFragment)
            }
            fabProfile.setOnClickListener {
                findNavController().navigate(R.id.action_filmsFragment_to_profileFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val BUNDLE_FILM_ID_KEY = "film_id"
    }
}