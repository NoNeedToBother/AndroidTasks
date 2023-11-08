package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.os.Build
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeTransform
import android.transition.Transition
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentFactBinding
import ru.kpfu.itis.paramonov.androidtasks.model.CityFact
import ru.kpfu.itis.paramonov.androidtasks.util.ParamsKey

class FactFragment : Fragment() {
    private var _binding: FragmentFactBinding? = null
    private val binding: FragmentFactBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = getTransition()
        sharedElementReturnTransition = getTransition()
    }

    private fun getTransition() : Transition {
        return TransitionSet()
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addTransition(ChangeBounds())
            .addTransition(ChangeTransform())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFactBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        init()
        (view.parent as? ViewGroup?)?.doOnPreDraw {
            startPostponedEnterTransition()
        }

    }

    private fun init() {
        val city = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(ParamsKey.FACT_ID_PARAM, CityFact::class.java)
        } else {
            arguments?.getSerializable(ParamsKey.FACT_ID_PARAM)
        }

        with(binding) {
            arguments?.getString(ParamsKey.TRANSITION_NAME_PARAM)?.let {
                ViewCompat.setTransitionName(ivFactImg, it)
            }
            with(city as CityFact) {
                when(this.city) {
                    "Moscow" -> ivFactImg.setImageResource(R.drawable.img_moscow)
                    "London" -> ivFactImg.setImageResource(R.drawable.img_london)
                    "New York" -> ivFactImg.setImageResource(R.drawable.img_new_york)
                    "Paris" -> ivFactImg.setImageResource(R.drawable.img_paris)
                    "Tokio" -> ivFactImg.setImageResource(R.drawable.img_tokio)
                    "Sydney" -> ivFactImg.setImageResource(R.drawable.img_sydney)
                    "Madrid" -> ivFactImg.setImageResource(R.drawable.img_madrid)
                }

                tvFactTitle.text = this.title
                tvFactContent.text = this.content
            }
        }
    }

    companion object {
        const val FACT_FRAGMENT_TAG = "FACT_FRAGMENT"

        fun newInstance(fact : CityFact, transitionName : String) = FactFragment().apply{
            arguments = bundleOf(ParamsKey.FACT_ID_PARAM to fact, ParamsKey.TRANSITION_NAME_PARAM to transitionName)
        }
    }
}