package ru.kpfu.itis.paramonov.androidtasks.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemFilmBinding
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemLikedFilmsBinding
import ru.kpfu.itis.paramonov.androidtasks.model.LikedFilms
import ru.kpfu.itis.paramonov.androidtasks.model.Film
import ru.kpfu.itis.paramonov.androidtasks.model.RvModel
import ru.kpfu.itis.paramonov.androidtasks.ui.holders.FilmItem
import ru.kpfu.itis.paramonov.androidtasks.ui.holders.LikedFilmsItem
import java.lang.RuntimeException

class FilmAdapter(
    private val context: Context,
    private val onFilmClicked: (Film) -> Unit,
    private val onDeleteClicked: (Boolean, Int) -> Unit,
    private val userId: Int,
    private val lifecycleScope: LifecycleCoroutineScope
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var filmList = mutableListOf<RvModel>()

    override fun getItemViewType(position: Int): Int {
        return when(filmList[position]) {
            is Film -> R.layout.item_film
            is LikedFilms -> R.layout.item_liked_films
            else -> throw RuntimeException()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.item_film -> FilmItem(
                context = context,
                binding = ItemFilmBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false),
                onFilmClicked = onFilmClicked,
                onDeleteClicked = onDeleteClicked
            )
            R.layout.item_liked_films -> LikedFilmsItem(
                context = context,
                binding = ItemLikedFilmsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                lifecycleScope = lifecycleScope,
                onFilmClicked = onFilmClicked,
                onDeleteClicked = onDeleteClicked,
                userId = userId
            )
            else -> throw RuntimeException()
        }
    }

    override fun getItemCount(): Int {
        return filmList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is FilmItem -> holder.onBind(filmList[position] as Film)
            is LikedFilmsItem -> holder.onBind()
        }
    }

    fun setItems(list: List<RvModel>) {
        filmList.clear()
        filmList.addAll(list)
    }

    fun deleteItem(position: Int): Film {
        val res = filmList.removeAt(position) as Film
        notifyItemRemoved(position)
        return res
    }
}