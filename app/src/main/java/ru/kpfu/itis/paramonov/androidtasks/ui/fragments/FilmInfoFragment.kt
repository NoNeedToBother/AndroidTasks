package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.data.db.entity.FilmRatingEntity
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentFilmInfoBinding
import ru.kpfu.itis.paramonov.androidtasks.di.ServiceLocator
import ru.kpfu.itis.paramonov.androidtasks.model.Film
import ru.kpfu.itis.paramonov.androidtasks.util.ParamKeys.Companion.NO_USER_ID
import ru.kpfu.itis.paramonov.androidtasks.util.ParamKeys.Companion.SHARED_PREF_USER_ID_KEY

class FilmInfoFragment: Fragment() {
    private var _binding: FragmentFilmInfoBinding? = null
    private val binding: FragmentFilmInfoBinding get() = _binding!!

    private var filmId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setFilm()
    }

    private fun setFilm() {
        val filmId = arguments?.getInt(FilmsFragment.BUNDLE_FILM_ID_KEY)
        this.filmId = filmId
        filmId?.let {
            lifecycleScope.launch(Dispatchers.IO) {
                ServiceLocator.getDbInstance().filmDao.getFilmById(filmId)?.let {
                    val film = Film.getFromEntity(it)
                    withContext(Dispatchers.Main) {
                        setFilmInfo(film)
                    }
                }
            }
        }
    }

    private fun setFilmInfo(film: Film) {
        with(binding) {
            Glide.with(requireContext())
                .load(film.posterUrl)
                .error(R.drawable.placeholder)
                .into(ivFilmPoster)

            tvFilmTitle.text = film.title
            tvFilmReleaseDate.text = getString(R.string.standard_release_date, film.releaseDate)
            tvFilmDescription.text = film.description
            setRating()
        }
    }

    private fun setRating() {
        val userId = ServiceLocator.getSharedPreferences().getInt(SHARED_PREF_USER_ID_KEY, NO_USER_ID)
        val filmId = filmId
        binding.like.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                filmId?.let { onLikeClicked(userId, it) }
            }
        }

        binding.seekbarRating.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                lifecycleScope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.Main) {
                        filmId?.let { onRatingUpdated(userId, filmId, progress)}
                    }
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        lifecycleScope.launch(Dispatchers.IO) {
            filmId?.let {
                setAverageRating(
                    ServiceLocator.getDbInstance().filmRatingsDao.getFilmRatings(filmId)
                )
                ServiceLocator.getDbInstance().filmRatingsDao.getFilmRating(userId, it)?.let {rating ->
                    rating.rating?.let {
                        withContext(Dispatchers.Main){
                            binding.seekbarRating.progress = it
                            binding.tvFilmRating.text = getString(R.string.standard_film_rating, it)

                            binding.seekbarRating.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                                override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                                    onRatingUpdated(userId, filmId, progress)
                                }

                                override fun onStartTrackingTouch(p0: SeekBar?) {
                                }

                                override fun onStopTrackingTouch(p0: SeekBar?) {
                                }
                            })
                        }
                    }
                    rating.liked?.let {
                        withContext(Dispatchers.Main) {
                            if (it) binding.like.setImageResource(R.drawable.like_pressed)
                            else binding.like.setImageResource(R.drawable.like)
                        }
                    }
                }
            }
        }
    }

    private fun onLikeClicked(userId: Int, filmId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val filmRating = ServiceLocator.getDbInstance().filmRatingsDao.getFilmRating(userId, filmId)
            var isLiked = false

            if (filmRating != null) {
                if (filmRating.liked != null) {
                    isLiked = !filmRating.liked
                    ServiceLocator.getDbInstance().filmRatingsDao.updateMovieLiked(
                        userId = userId,
                        filmId = filmId,
                        isLiked = isLiked
                    )
                }
                else {
                    isLiked = true
                    ServiceLocator.getDbInstance().filmRatingsDao.addFilmRating(
                        FilmRatingEntity.getEntity(
                            userId = userId,
                            filmId = filmId,
                            rating = null,
                            isLiked = true
                        )
                    )
                }
            } else {
                isLiked = true
                ServiceLocator.getDbInstance().filmRatingsDao.addFilmRating(
                    FilmRatingEntity.getEntity(
                        userId = userId,
                        filmId = filmId,
                        rating = null,
                        isLiked = true
                    )
                )
            }

            withContext(Dispatchers.Main) {
                if (isLiked) binding.like.setImageResource(R.drawable.like_pressed)
                else binding.like.setImageResource(R.drawable.like)
            }
        }
    }

    private fun onRatingUpdated(userId: Int, filmId: Int, rating: Int) {
        with(binding) {
            tvFilmRating.text = getString(R.string.standard_film_rating, rating)
            lifecycleScope.launch(Dispatchers.IO) {
                val filmRating = ServiceLocator.getDbInstance().filmRatingsDao.getFilmRating(userId, filmId)
                if (filmRating == null) {
                    ServiceLocator.getDbInstance().filmRatingsDao.addFilmRating(
                        FilmRatingEntity.getEntity(
                            userId = userId,
                            filmId = filmId,
                            rating = rating,
                            isLiked = null
                        )
                    )
                } else {
                    ServiceLocator.getDbInstance().filmRatingsDao.updateMovieRating(
                        userId,
                        filmId,
                        rating
                    )
                    val ratings =
                        ServiceLocator.getDbInstance().filmRatingsDao.getFilmRatings(filmId)
                    setAverageRating(ratings)
                }
            }
        }
    }

    private fun setAverageRating(ratings: List<Int?>) {
        lifecycleScope.launch(Dispatchers.IO) {
            var sum = 0
            var amount = 0
            for (rating: Int? in ratings) {
                rating?.let {
                    sum += rating
                    amount++
                }
            }
            withContext(Dispatchers.Main) {
                if (amount == 0) {
                    binding.tvAvgFilmRating.text = getString(R.string.standard_avg_rating, "0.0")
                } else {
                    binding.tvAvgFilmRating.text = getString(
                        R.string.standard_avg_rating,
                        (sum.toDouble()/amount).toString()
                        //String.format("%3.f", sum.toDouble() / amount)
                    )
                }
            }
        }
    }
}