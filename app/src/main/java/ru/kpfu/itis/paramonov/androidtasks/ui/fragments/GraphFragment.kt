package ru.kpfu.itis.paramonov.androidtasks.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorListener
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentGraphBinding
import ru.kpfu.itis.paramonov.androidtasks.ui.model.Function
import kotlin.math.sin

class GraphFragment: Fragment(R.layout.fragment_graph) {

    private val binding: FragmentGraphBinding by viewBinding(FragmentGraphBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.gvGraph.provideValues(
            getFunctionValuePairs { x ->
                sin(x)
            }
        )
        setHintViewColors()
        setOnClickListeners()
        setOnTextChangedListeners()
        initFunctionSpinner()
    }

    private fun initFunctionSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(), R.array.formulas, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerFormulas.adapter = adapter
        }
        binding.spinnerFormulas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val formula = parent.getItemAtPosition(pos) as String
                val function = Function.fromFormula(formula)
                val values = getFunctionValuePairs { x -> function.calculate(x)}
                binding.gvGraph.provideValues(values)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun setOnTextChangedListeners() {
        with(binding) {
            etDotSize.addTextChangedListener {
                it?.let {
                    if (it.isNotEmpty()) {
                        val value = it.toString().toFloat()
                        if (value >= 0) gvGraph.dotSize = value
                        else etDotSize.error = getString(R.string.more_than_zero)
                    }
                }
            }
            etLineSize.addTextChangedListener {
                it?.let {
                    if (it.isNotEmpty()) {
                        val value = it.toString().toFloat()
                        if (value >= 0) gvGraph.graphStrokeWidth = value
                        else etLineSize.error = getString(R.string.more_than_zero)
                    }
                }
            }
            etLabelsX.addTextChangedListener {
                it?.let {
                    if (it.isNotEmpty()) {
                        val value = it.toString().toInt()
                        if (value >= 1) gvGraph.labelXAmount = value
                        else etLabelsX.error = getString(R.string.more_than_one)
                    }
                }
            }
            etLabelsY.addTextChangedListener {
                it?.let {
                    if (it.isNotEmpty()) {
                        val value = it.toString().toInt()
                        if (value >= 1) gvGraph.labelYAmount = value
                        else etLabelsY.error = getString(R.string.more_than_one)
                    }
                }
            }
        }
    }

    private fun setHintViewColors() {
        with(binding) {
            viewDotColor.setBackgroundColor(gvGraph.dotColor)
            viewFillColor.setBackgroundColor(gvGraph.graphFillColor)
            viewGradientColor.setBackgroundColor(gvGraph.graphGradientColor)
            viewGraphLineColor.setBackgroundColor(gvGraph.graphStrokeColor)
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            viewDotColor.setOnClickListener {
                showColorPickerDialog {
                    gvGraph.dotColor = it
                    viewDotColor.setBackgroundColor(it)
                }
            }
            viewFillColor.setOnClickListener {
                showColorPickerDialog {
                    gvGraph.graphFillColor = it
                    viewFillColor.setBackgroundColor(it)
                }
            }
            viewGraphLineColor.setOnClickListener {
                showColorPickerDialog {
                    gvGraph.graphStrokeColor = it
                    viewGraphLineColor.setBackgroundColor(it)
                }
            }
            viewGradientColor.setOnClickListener {
                showColorPickerDialog {
                    gvGraph.graphGradientColor = it
                    viewGradientColor.setBackgroundColor(it)
                }
            }
            cbGradient.setOnClickListener {
                gvGraph.gradient = cbGradient.isChecked
            }
            btnClear.setOnClickListener {
                gvGraph.clear()
            }
            btnAddDot.setOnClickListener {
                val x = etX.text.toString()
                val y = etY.text.toString()
                if (x.isNotEmpty() && y.isNotEmpty()) {
                    gvGraph.addValues(x.toDouble() to y.toDouble())
                }
            }
        }
    }

    private fun showColorPickerDialog(onColorChosen: (Int) -> Unit) {
        ColorPickerDialog.Builder(requireContext())
            .setTitle(R.string.pick_color)
            .setPositiveButton(getString(R.string.save), object : ColorListener {
                override fun onColorSelected(color: Int, fromUser: Boolean) {
                    onColorChosen.invoke(color)
                }
            })
            .attachAlphaSlideBar(false)
            .show()
    }

    private fun getFunctionValuePairs(function: (Double) -> Double): List<Pair<Double, Double>> {
        val result = mutableListOf<Pair<Double, Double>>()
        for (i in LEFT_VALUE_EDGE * FACTOR .. RIGHT_VALUE_EDGE * FACTOR) {
            val x = i.toDouble() / LEFT_VALUE_EDGE
            val y = function.invoke(x)
            result.add(x to y)
        }
        return result
    }

    companion object {
        fun newInstance(): GraphFragment = GraphFragment()

        private const val LEFT_VALUE_EDGE = -5
        private const val RIGHT_VALUE_EDGE = 5
        private const val FACTOR = 10

        const val GRAPH_FRAGMENT_TAG = "GRAPH_FRAGMENT"
    }
}