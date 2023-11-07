package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.kpfu.itis.paramonov.androidtasks.MainActivity
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.util.CityFactsRepository
import ru.kpfu.itis.paramonov.androidtasks.adapter.RvAdapter
import ru.kpfu.itis.paramonov.androidtasks.util.ParamsKey
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentFactGalleryBinding
import ru.kpfu.itis.paramonov.androidtasks.model.CityFact

class FactGalleryFragment : Fragment() {
    private var _binding: FragmentFactGalleryBinding? = null
    private val binding: FragmentFactGalleryBinding get() = _binding!!

    private var rvAdapter: RvAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFactGalleryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        rvAdapter = RvAdapter(
            onBsdButtonClicked = ::onBsdButtonClicked,
            onFactClicked = ::onFactClicked,
            onLikeClicked = ::onLikeClicked
        )


        with(binding) {
            arguments?.getInt(ParamsKey.FACT_COUNT_PARAM)?.let {
                if (it == 0) {
                    tvNoFacts.visibility = TextView.VISIBLE
                    return
                }

                val layoutManager : LayoutManager

                if (it <= 12) {
                    rvAdapter?.let {adapter ->
                        ItemTouchHelper(getItemTouchHelperSwipeCallback(adapter)).attachToRecyclerView(rvCityFacts) }
                    layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                } else {
                    layoutManager = GridLayoutManager(context, 2).apply {
                        spanSizeLookup = object : SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                return if (position == 1 || (position - 1) % 9 == 0) 2
                                else 1
                            }
                        }
                    }
                }
                rvCityFacts.layoutManager = layoutManager
                rvCityFacts.adapter = rvAdapter
                rvAdapter?.setItems(CityFactsRepository.getFactsList(it))
            }
        }
    }

    private fun getItemTouchHelperSwipeCallback(rvAdapter: RvAdapter) = object: ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return makeMovementFlags(0, ItemTouchHelper.LEFT)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.adapterPosition
            val fact = rvAdapter.deleteItem(pos)
            onFactDeleted(pos, fact, rvAdapter)
        }

        override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
            return 0.5f
        }
    }

    private fun onFactDeleted(position: Int, fact : CityFact, adapter: RvAdapter) {
        Snackbar.make(binding.root, getString(R.string.fact_deleted), Snackbar.LENGTH_SHORT).apply {
            setAction(getString(R.string.undo_delete)) {
                adapter.addItem(position, fact)
            }
            show()
        }
    }

    private fun onFactClicked(fact : CityFact) {
        (requireActivity() as MainActivity).goToScreen(
            FactFragment.newInstance(fact),
            FactFragment.FACT_FRAGMENT_TAG,
            true
        )
    }

    private fun onLikeClicked(position : Int, fact : CityFact) {
        rvAdapter?.updateItem(position, fact)
    }

    private fun onBsdButtonClicked() {
        AddFactsBsdFragment {
            updateItems()
        }.show(parentFragmentManager, AddFactsBsdFragment.ADD_FACTS_BSD_FRAGMENT_TAG)
    }

    private fun updateItems() {
        rvAdapter?.setItems(CityFactsRepository.getFactsList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        rvAdapter = null
    }


    companion object {
        const val FACT_GALLERY_FRAGMENT_TAG = "FACT_GALLERY_FRAGMENT"

        fun newInstance(factCount : Int) = FactGalleryFragment().apply{
            arguments = bundleOf(ParamsKey.FACT_COUNT_PARAM to factCount)
        }
    }
}