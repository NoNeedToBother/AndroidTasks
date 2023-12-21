package ru.kpfu.itis.paramonov.androidtasks.ui.holders

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemFilmBinding
import ru.kpfu.itis.paramonov.androidtasks.model.Film

class FilmItem(
    private val context: Context,
    private val binding: ItemFilmBinding,
    private val onFilmClicked: (Film) -> Unit,
    private val onDeleteClicked: (Film) -> Unit
): RecyclerView.ViewHolder(binding.root){
    private var item: Film? = null

    init {
        with(binding) {
            root.setOnClickListener {
                item?.let {
                    onFilmClicked(it)
                }
            }

            ivDeleteBtn.setOnLongClickListener {
                item?.let {
                    onDeleteClicked(it)
                }
                true
            }
        }
    }

    fun onBind(item: Film) {
        this.item = item

        with(binding) {
            Glide.with(context)
                .load(item.posterUrl)
                .error(R.drawable.placeholder)
                .into(ivFactItemImg)

            tvFilmItemTitle.text = item.title
            tvFilmItemDesc.text = item.description
            tvFilmItemReleaseDate.text = item.releaseDate
        }
    }
}