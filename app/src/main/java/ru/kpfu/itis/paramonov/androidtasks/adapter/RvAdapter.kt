package ru.kpfu.itis.paramonov.androidtasks.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.adapter.diffutil.FactsDiffUtil
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemBsdBtnBinding
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemCityFactBinding
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemDateBinding
import ru.kpfu.itis.paramonov.androidtasks.model.BsdButton
import ru.kpfu.itis.paramonov.androidtasks.model.CityFact
import ru.kpfu.itis.paramonov.androidtasks.model.Date
import ru.kpfu.itis.paramonov.androidtasks.model.Model
import ru.kpfu.itis.paramonov.androidtasks.ui.holders.BottomSheetDisplayBtnItem
import ru.kpfu.itis.paramonov.androidtasks.ui.holders.DateItem
import ru.kpfu.itis.paramonov.androidtasks.ui.holders.FactItem
import java.lang.RuntimeException

class RvAdapter(
    private val onBsdButtonClicked: () -> Unit,
    private val onFactClicked: ((CityFact) -> Unit),
    private val onLikeClicked: ((Int, CityFact) -> Unit),
    private val onDeleteClicked: (Int, CityFact) -> Unit,
    private val enableDeleteButton: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var factList = mutableListOf<Model>()

    override fun getItemViewType(position: Int): Int {
        return when (factList[position]) {
            is BsdButton -> R.layout.item_bsd_btn
            is CityFact -> R.layout.item_city_fact
            is Date -> R.layout.item_date
            else -> throw RuntimeException()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.item_city_fact -> FactItem(
                binding = ItemCityFactBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onFactClicked = onFactClicked,
                onLikeClicked = onLikeClicked,
                onDeleteClicked = onDeleteClicked,
                enableDeleteButton = enableDeleteButton
            )

            R.layout.item_bsd_btn -> BottomSheetDisplayBtnItem(
                binding = ItemBsdBtnBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onBtnClicked = onBsdButtonClicked
            )

            R.layout.item_date -> DateItem(
                binding = ItemDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            else -> throw RuntimeException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is FactItem -> (holder as? FactItem)?.bindItem(item = factList[position] as CityFact)
            is DateItem -> (holder as? DateItem)?.bindItem()
            is BottomSheetDisplayBtnItem -> (holder as? BottomSheetDisplayBtnItem)?.bindItem()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            (payloads.first() as? Boolean)?.let {
                (holder as? FactItem)?.changeLikeBtnStatus(it)
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemCount(): Int = factList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<Model>) {
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

    fun addItem(position: Int, item: CityFact) {
        factList.add(position, item)
        for (pos in position .. factList.size) {
            notifyItemChanged(pos)
        }
    }

    fun deleteItem(position: Int) : CityFact{
        val fact = factList.removeAt(position)
        notifyItemRemoved(position)
        return fact as CityFact
    }
}