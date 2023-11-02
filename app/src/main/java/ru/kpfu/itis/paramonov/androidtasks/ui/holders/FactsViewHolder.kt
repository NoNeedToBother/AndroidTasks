package ru.kpfu.itis.paramonov.androidtasks.ui.holders

import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemCityFactBinding
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.androidtasks.model.CityFact
import ru.kpfu.itis.paramonov.androidtasks.R

class FactsViewHolder(
    private val binding: ItemCityFactBinding,
    private val onFactClicked: ((CityFact) -> Unit),
    private val onLikeClicked: ((Int, CityFact) -> Unit),
) : RecyclerView.ViewHolder(binding.root) {

    private var item: CityFact? = null

    init {
        binding.root.setOnClickListener {
            this.item?.let{onFactClicked(it)}
        }
        binding.ivLikeBtn.setOnClickListener {
            this.item?.let {
                it.isLiked = !it.isLiked
                onLikeClicked(adapterPosition, it)
            }
        }
    }

    fun bindItem(item: CityFact) {
        this.item = item
        with(binding) {
            tvFactItemTitle.text = item.title
            item.content.let { tvFactItemContent.text = it }
            with(ivFactItemImg) {
                when(item.city) {
                    "Moscow" -> setImageResource(R.drawable.img_moscow)
                    "London" -> setImageResource(R.drawable.img_london)
                    "New York" -> setImageResource(R.drawable.img_new_york)
                    "Paris" -> setImageResource(R.drawable.img_paris)
                    "Tokio" -> setImageResource(R.drawable.img_tokio)
                    "Sydney" -> setImageResource(R.drawable.img_sydney)
                    "Madrid" -> setImageResource(R.drawable.img_madrid)
                }
            }
            changeLikeBtnStatus(isChecked = item.isLiked)
        }
    }

    fun changeLikeBtnStatus(isChecked: Boolean) {
        val likeDrawable = if (isChecked) R.drawable.like_pressed else R.drawable.like
        binding.ivLikeBtn.setImageResource(likeDrawable)
    }
}