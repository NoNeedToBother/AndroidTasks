package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.data.db.entity.FilmEntity
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentAddFilmBinding
import ru.kpfu.itis.paramonov.androidtasks.di.ServiceLocator
import ru.kpfu.itis.paramonov.androidtasks.model.Film

class AddFilmFragment: Fragment() {
    private var _binding: FragmentAddFilmBinding? = null
    private val binding: FragmentAddFilmBinding get() = _binding!!

    private var correctReleaseDate = false
    private var correctPosterUrl = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddFilmBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setOnTextChangedListeners()
        setOnFocusChangedListeners()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            btnSave.setOnClickListener {
                if (!(correctReleaseDate && correctPosterUrl)) {
                    showToast(R.string.incorrect_info)
                    return@setOnClickListener
                }

                val title = etTitle.text.toString()
                val date = etReleaseDate.text.toString()
                if (!checkFilm(title, date)) {
                    showToast(R.string.film_already_exists)
                    return@setOnClickListener
                }

                val posterUrl = etPosterUrl.text.toString()
                val description = etDescription.text.toString()
                val film = Film(
                    title = title,
                    description = description,
                    releaseDate = date,
                    posterUrl = posterUrl
                )

                lifecycleScope.launch(Dispatchers.IO) {
                    ServiceLocator.getDbInstance().filmDao.saveFilm(FilmEntity.getEntity(film))
                }

                findNavController().navigate(R.id.action_addFilmFragment_to_filmsFragment)
            }
        }
    }

    private fun checkFilm(title: String, date: String): Boolean {
        var res = true
        lifecycleScope.launch(Dispatchers.IO) {
            val film = ServiceLocator.getDbInstance().filmDao.getFilmByTitleAndDate(title, date)
            if (film != null) res = false
        }
        return res
    }

    private fun setOnTextChangedListeners() {
        with(binding) {
            etReleaseDate.addTextChangedListener(getTextWatcher {
                if (!RELEASE_DATE_REGEX.toRegex().matches(it.toString())) {
                    correctReleaseDate = false
                    etReleaseDate.error = getString(R.string.date_format_error)
                } else {
                    correctReleaseDate = true
                    etReleaseDate.error = null
                }
            })

            etPosterUrl.addTextChangedListener(getTextWatcher {
                if (!Patterns.WEB_URL.matcher(it.toString()).matches()) {
                    correctPosterUrl = false
                    etPosterUrl.error = getString(R.string.url_error)
                } else {
                    correctPosterUrl = true
                    etPosterUrl.error = null
                }
            })
        }
    }

    private fun setOnFocusChangedListeners() {
        with(binding) {
            etPosterUrl.setOnFocusChangeListener { _,_ ->
                val url = etPosterUrl.text.toString()
                Glide.with(requireActivity())
                    .load(url)
                    .error(R.drawable.placeholder)
                    .into(ivFilmPoster)
            }
        }
    }

    private fun getTextWatcher(afterTextChanged: (editable: Editable) -> Unit) = object :
        TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            p0?.let {
                afterTextChanged.invoke(it)
            }
        }
    }

    private fun showToast(messageId: Int) {
        Toast.makeText(context, messageId, Toast.LENGTH_LONG).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val RELEASE_DATE_REGEX = """^(19|20)\d\d-(0[1-9]|1[012])-([12][0-9]|3[01]|0[1-9])$"""
    }
}