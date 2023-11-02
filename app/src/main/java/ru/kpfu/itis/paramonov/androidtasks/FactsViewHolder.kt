package ru.kpfu.itis.paramonov.androidtasks

import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemCityFactBinding
import androidx.recyclerview.widget.RecyclerView

class FactsViewHolder(
    private val binding: ItemCityFactBinding,
    private val onFactClicked: ((CityFact) -> Unit),
    private val onLikeClicked: ((Int, CityFact) -> Unit),
) : RecyclerView.ViewHolder(binding.root) {

    private var item: CityFact? = null

    init {
        binding.root.setOnClickListener {
            this.item?.let(onFactClicked)
        }
        binding.ivLikeBtn.setOnClickListener {
            this.item?.let {
                val data = it.copy(isLiked = !it.isLiked)
                onLikeClicked(adapterPosition, data)
            }
        }
    }

    fun bindItem(item: CityFact) {
        this.item = item
        with(binding) {
            tvFactTitle.text = item.title
            item.content?.let { tvFactContent.text = it }
            with(ivFactCityImg) {
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