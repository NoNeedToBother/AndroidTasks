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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.adapter.FilmAdapter
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentFilmsBinding
import ru.kpfu.itis.paramonov.androidtasks.di.ServiceLocator
import ru.kpfu.itis.paramonov.androidtasks.model.Film

class FilmsFragment: Fragment() {
    private var _binding: FragmentFilmsBinding? = null
    private val binding: FragmentFilmsBinding get() = _binding!!

    private val filmDao = ServiceLocator.getDbInstance().filmDao

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
            val films = filmDao.getAllFilms()
                .map {entity ->
                    Film.getFromEntity(entity)
                }

            withContext(Dispatchers.Main) {
                binding.tvNoFilms.visibility = View.GONE
                if (films.isNotEmpty()) {
                    with(binding) {
                        val adapter = FilmAdapter(
                            context = requireContext(),
                            onFilmClicked = ::onFilmClicked,
                            onDeleteClicked = ::onDeleteClicked
                        )
                        adapter.setItems(films)
                        this@FilmsFragment.adapter = adapter

                        val gridLayoutManager = GridLayoutManager(
                            requireContext(),
                            2,
                            GridLayoutManager.VERTICAL,
                            false
                        )

                        rvMovies.layoutManager = gridLayoutManager
                        rvMovies.adapter = adapter
                    }
                } else {
                    binding.tvNoFilms.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun onFilmClicked(film: Film) {
        findNavController().navigate(
            R.id.action_filmsFragment_to_filmInfoFragment,
            bundleOf(BUNDLE_FILM_ID_KEY to film.id)
        )
    }

    private fun onDeleteClicked(position: Int) {
        val film = adapter?.deleteItem(position)
        lifecycleScope.launch(Dispatchers.IO) {
            film?.id?.let {
                ServiceLocator.getDbInstance().filmDao.deleteFilmById(it)
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