package ru.kpfu.itis.paramonov.androidtasks.ui.holders

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.androidtasks.adapter.FilmAdapter
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemLikedFilmsBinding
import ru.kpfu.itis.paramonov.androidtasks.di.ServiceLocator
import ru.kpfu.itis.paramonov.androidtasks.model.Film
import ru.kpfu.itis.paramonov.androidtasks.model.RvModel

class LikedFilmsItem(
    private val binding: ItemLikedFilmsBinding,
    private val onFilmClicked: (Film) -> Unit,
    private val onDeleteClicked: (Boolean, Int) -> Unit,
    private val context: Context,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val userId: Int
) : RecyclerView.ViewHolder(binding.root) {

    private var filmAdapter: FilmAdapter? = null
    private var filmList = mutableListOf<Film>()

    fun onBind() {
        filmList.clear()
        lifecycleScope.launch(Dispatchers.IO) {
            val liked = ServiceLocator.getDbInstance().filmRatingsDao.getUserLikedFilms(userId)

            filmList.addAll(ServiceLocator.getDbInstance().filmDao.getFilmsById(liked)
                .map { entity -> Film.getFromEntity(entity) }
                .map {film -> film.setFromLiked()
                })

            withContext(Dispatchers.Main) {

                with(binding) {
                    filmAdapter = FilmAdapter(
                        context = context,
                        onFilmClicked = onFilmClicked,
                        onDeleteClicked = onDeleteClicked,
                        userId = userId,
                        lifecycleScope = lifecycleScope
                    )
                    filmAdapter?.setItems(filmList)

                    val layoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                    rvLiked.layoutManager = layoutManager
                    rvLiked.adapter = filmAdapter
                }
            }
        }
    }

    fun removeFilm(pos: Int): Film {
        val res = filmList.removeAt(pos)
        filmAdapter?.notifyItemRemoved(pos)
        return res
    }

    fun notifyFilmDeleted(filmId: Int) {
        val pos = filmList.indexOfFirst { film -> film.id == filmId }
        if (pos != -1) {
            filmList.removeAt(pos)
            filmAdapter?.notifyItemRemoved(pos)
        }
    }

    fun getItemCount(): Int {
        return filmList.size
    }
}