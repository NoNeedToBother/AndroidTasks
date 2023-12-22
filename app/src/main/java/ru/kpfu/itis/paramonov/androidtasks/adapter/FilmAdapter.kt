package ru.kpfu.itis.paramonov.androidtasks.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemFilmBinding
import ru.kpfu.itis.paramonov.androidtasks.model.Film
import ru.kpfu.itis.paramonov.androidtasks.ui.holders.FilmItem
import java.lang.RuntimeException

class FilmAdapter(
    private val context: Context,
    private val onFilmClicked: (Film) -> Unit,
    private val onDeleteClicked: (Int) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var filmList = mutableListOf<Film>()

    override fun getItemViewType(position: Int): Int {
        return when(filmList[position]) {
            is Film -> R.layout.item_film
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
            else -> throw RuntimeException()
        }
    }

    override fun getItemCount(): Int {
        return filmList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is FilmItem -> holder.onBind(filmList[position])
        }
    }

    fun setItems(list: List<Film>) {
        filmList.clear()
        filmList.addAll(list)
    }

    fun deleteItem(position: Int): Film {
        val res = filmList.removeAt(position)
        Log.i("BD", res.title)
        notifyItemRemoved(position)
        return res
    }
}