package ru.kpfu.itis.paramonov.androidtasks.ui.model

import kotlin.math.pow

enum class Function(private val formula: String, private val calculate: (Double) -> Double) {
    X("y = x", { x -> x} ),
    MINUS_X("y = -x", { x -> -x }),
    SIN_X("y = sin(x)", { x -> kotlin.math.sin(x) }),
    E_POW_X("y = e^x", { x -> Math.E.pow(x) }),
    X_SQUARED("y = x^2", {x -> x.pow(2) }),
    SIN_ONE_OVER_X("y = sin(1/x)", { x -> kotlin.math.sin(1.0 / x)}),
    TWO_TIMES_X("y = 2 * x", { x -> 2 * x }),
    SQRT_X("y = √x", { x -> kotlin.math.sqrt(x) }),
    X_SIN_X("y = x * sin(x)", { x -> x * kotlin.math.sin(x) });

    override fun toString(): String  = formula

    fun calculate(x: Double): Double {
        return calculate.invoke(x)
    }

    companion object {
        fun fromFormula(formula: String): Function {
            for (function in values()) {
                if (function.formula == formula) return function
            }
            throw RuntimeException("Not a valid formula")
        }
    }
}