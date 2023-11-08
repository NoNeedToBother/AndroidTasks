package ru.kpfu.itis.paramonov.androidtasks.ui.holders

import android.annotation.SuppressLint
import android.view.View
import android.view.View.OnLongClickListener
import androidx.core.view.ViewCompat
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemCityFactBinding
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.androidtasks.model.CityFact
import ru.kpfu.itis.paramonov.androidtasks.R

class FactItem(
    private val binding: ItemCityFactBinding,
    private val onFactClicked: ((View, CityFact) -> Unit),
    private val onLikeClicked: ((Int, CityFact) -> Unit),
    private val onDeleteClicked: (Int, CityFact) -> Unit,
    private var enableDeleteButton: Boolean
) : RecyclerView.ViewHolder(binding.root) {

    private var item: CityFact? = null


    init {
        if (enableDeleteButton) {
            binding.root.setOnLongClickListener(getOnLongClickListener())
        }
        binding.root.setOnClickListener {
            this.item?.let{onFactClicked(binding.ivFactItemImg, it)}
        }
        binding.ivLikeBtn.setOnClickListener {
            this.item?.let {
                it.isLiked = !it.isLiked
                onLikeClicked(adapterPosition, it)
            }
        }
    }

    private fun getOnLongClickListener() = OnLongClickListener {
        with(binding.ivDeleteBtn) {
            when (this.visibility) {
                View.VISIBLE -> this.visibility = View.GONE
                View.GONE -> {
                    this.setOnClickListener {
                        item?.let { onDeleteClicked(adapterPosition, it) }
                    }
                    this.visibility = View.VISIBLE
                }
                View.INVISIBLE -> this.visibility = View.VISIBLE
            }
        }
        true
    }

    fun bindItem(item: CityFact) {
        this.item = item
        with(binding) {
            ViewCompat.setTransitionName(ivFactItemImg, "fact_${item.id}")
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