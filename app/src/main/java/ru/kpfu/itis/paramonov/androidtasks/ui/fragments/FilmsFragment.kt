package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
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
import ru.kpfu.itis.paramonov.androidtasks.data.db.entity.FilmEntity
import ru.kpfu.itis.paramonov.androidtasks.data.db.entity.FilmRatingEntity
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentFilmsBinding
import ru.kpfu.itis.paramonov.androidtasks.di.ServiceLocator
import ru.kpfu.itis.paramonov.androidtasks.model.Film
import ru.kpfu.itis.paramonov.androidtasks.model.LikedFilms
import ru.kpfu.itis.paramonov.androidtasks.model.RvModel
import ru.kpfu.itis.paramonov.androidtasks.util.ParamKeys.Companion.NO_USER_ID
import ru.kpfu.itis.paramonov.androidtasks.util.ParamKeys.Companion.SHARED_PREF_USER_ID_KEY
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

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
        setSpinner()
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
                .sortedByDescending {film ->
                    getReleaseDateSelectorOldToNew(film)
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

    private fun setSpinner() {
        with(binding) {
            val options = arrayOf("Sort by release date (descending)",
                "Sort by release date (ascending)",
                "Sort by your rating (descending)",
                "Sort by your rating (ascending)")

            spinnerSorting.adapter = getAdapter(options)
            spinnerSorting.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                    val films = adapter?.getFilms()
                    when(pos) {
                        0 -> {
                            val sorted = films?.sortedByDescending {
                                getReleaseDateSelectorOldToNew(it)
                            }
                            sorted?.let {
                                adapter?.setFilms(it)}
                        }
                        1 -> {
                            val sorted = films?.sortedBy {
                                getReleaseDateSelectorOldToNew(it)
                            }
                            sorted?.let {adapter?.setFilms(it)}
                        }
                        2 -> {
                            lifecycleScope.launch(Dispatchers.IO) {
                                val filmRatings = ArrayList<Film>()
                                films?.let {
                                    for (film: Film in it) {
                                        film.id?.let {
                                            val ratingInfo =
                                                ServiceLocator.getDbInstance().filmRatingsDao.getUserFilmRating(getUserId(), it)
                                            if(ratingInfo != null) {
                                                ratingInfo.filmId?.let {
                                                    val filmEntity =
                                                        ServiceLocator.getDbInstance().filmDao.getFilmById(
                                                            ratingInfo.filmId
                                                        )
                                                    filmEntity?.let {
                                                        val newFilm = Film.getFromEntity(filmEntity)
                                                        if (ratingInfo.rating != null) {
                                                            newFilm.setRating(ratingInfo.rating)
                                                        } else newFilm.setRating(0)
                                                        filmRatings.add(newFilm)
                                                    }
                                                }
                                            } else {
                                                val newFilm = Film(film.id, film.title, film.description, film.releaseDate, film.posterUrl)
                                                newFilm.setRating(0)
                                                filmRatings.add(newFilm)
                                            }
                                        }
                                    }
                                    withContext(Dispatchers.Main) {
                                        filmRatings.let { adapter?.setFilms(filmRatings.sortedByDescending {film ->
                                            film.getRating()
                                        }) }
                                    }
                                }
                            }
                        }
                        3 -> lifecycleScope.launch(Dispatchers.IO) {
                            val filmRatings = ArrayList<Film>()
                            films?.let {
                                for (film: Film in it) {
                                    film.id?.let {
                                        val ratingInfo =
                                            ServiceLocator.getDbInstance().filmRatingsDao.getUserFilmRating(getUserId(), it)
                                        if(ratingInfo != null) {
                                            ratingInfo.filmId?.let {
                                                val filmEntity =
                                                    ServiceLocator.getDbInstance().filmDao.getFilmById(
                                                        ratingInfo.filmId
                                                    )
                                                filmEntity?.let {
                                                    val newFilm = Film.getFromEntity(filmEntity)
                                                    if (ratingInfo.rating != null) {
                                                        newFilm.setRating(ratingInfo.rating)
                                                    } else newFilm.setRating(0)
                                                    filmRatings.add(newFilm)
                                                }
                                            }
                                        } else {
                                            val newFilm = Film(film.id, film.title, film.description, film.releaseDate, film.posterUrl)
                                            newFilm.setRating(0)
                                            filmRatings.add(newFilm)
                                        }
                                    }
                                }
                                withContext(Dispatchers.Main) {
                                    filmRatings.let { adapter?.setFilms(filmRatings.sortedBy {film ->
                                        film.getRating()
                                    }) }
                                }
                            }
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
        }
    }

    private fun getAdapter(array : Array<String>): ArrayAdapter<String>{
        return ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            array
        ).apply {
            setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item)
        }
    }

    private fun getReleaseDateSelectorOldToNew(film: Film): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val date = LocalDate.parse(film.releaseDate)
            return date.atStartOfDay().year
        } else {
            val date = SimpleDateFormat(RELEASE_DATE_FORMAT, Locale.UK).parse(film.releaseDate)
            val calendar = Calendar.getInstance()
            calendar.time = date
            return calendar.get(Calendar.YEAR)
        }
    }

    private fun addUserRating(film: Film, ratings: MutableList<Film>) {
        var ratingInfo: FilmRatingEntity? = null
        var res = 0
        lifecycleScope.launch(Dispatchers.IO) {
            film.id?.let {
                ratingInfo =
                    ServiceLocator.getDbInstance().filmRatingsDao.getUserFilmRating(getUserId(), it)
                ratingInfo?.rating?.let {rating ->
                    withContext(Dispatchers.Main) {
                        res = rating
                    }
                }
            }
        }

        val newFilm: Film = Film(film.id, film.title, film.description, film.releaseDate, film.posterUrl)
        newFilm.setRating(res)
        newFilm.let { ratings.add(it) }
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
        private const val RELEASE_DATE_FORMAT = "yyyy-MM-dd"
    }
}