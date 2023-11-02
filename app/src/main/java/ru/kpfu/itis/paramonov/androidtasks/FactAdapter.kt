package ru.kpfu.itis.paramonov.androidtasks

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemCityFactBinding

class FactAdapter(
    private val onFactClicked: ((CityFact) -> Unit),
    private val onLikeClicked: ((Int, CityFact) -> Unit),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var factList = mutableListOf<CityFact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FactsViewHolder(
            binding = ItemCityFactBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onFactClicked = onFactClicked,
            onLikeClicked = onLikeClicked,
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? FactsViewHolder)?.bindItem(item = factList[position])
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            (payloads.first() as? Boolean)?.let {
                (holder as? FactsViewHolder)?.changeLikeBtnStatus(it)
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemCount(): Int = factList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<CityFact>) {
        val diff = FactsDiffUtil(oldItemsList = factList, newItemsList = list)
        val diffResult = DiffUtil.calculateDiff(diff)
        factList.clear()
        factList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateItem(position: Int, item: CityFact) {
        this.factList[position] = item
        notifyItemChanged(position, item.isLiked)
    }
}