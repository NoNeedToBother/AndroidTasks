package ru.kpfu.itis.paramonov.androidtasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentFactGalleryBinding

class FactGalleryFragment : Fragment() {
    private var _binding: FragmentFactGalleryBinding? = null
    private val binding: FragmentFactGalleryBinding get() = _binding!!

    private var factAdapter: FactAdapter? = null

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
        factAdapter = FactAdapter(
            onFactClicked = ::onFactClicked,
            onLikeClicked = ::onLikeClicked
        )


        with(binding) {
            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvCityFacts.layoutManager = layoutManager
            rvCityFacts.adapter = factAdapter

            arguments?.getInt(ParamsKey.FACT_COUNT_PARAM)?.let {
                val facts = CityFactsRepository.getFactsList(it)
                factAdapter?.setItems(CityFactsRepository.getFactsList(it))
                Toast.makeText(context, facts[0].content, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onFactClicked(fact : CityFact) {}

    private fun onLikeClicked(position : Int, fact : CityFact) {
        factAdapter?.updateItem(position, fact)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        factAdapter = null
    }


    companion object {
        const val FACT_GALLERY_FRAGMENT_TAG = "FACT_GALLERY_FRAGMENT"

        fun newInstance(factCount : Int) = FactGalleryFragment().apply{
            arguments = bundleOf(ParamsKey.FACT_COUNT_PARAM to factCount)
        }
    }
}