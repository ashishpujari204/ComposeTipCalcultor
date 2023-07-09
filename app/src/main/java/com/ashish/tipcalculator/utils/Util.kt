package com.ashish.tipcalculator.utils

import android.util.Log


fun calculatedTip(totalBill: Double, tipPercentage: Int): Double {
    return if (totalBill > 1 && totalBill.toString().isNotEmpty())
        (totalBill * tipPercentage) / 100 else 0.0
}

fun calculatePerPerson(
    totalBill: Double,
    spiltBy: Int,
    tipPercentage: Int
): Double {
    val bill = calculatedTip(totalBill, tipPercentage) + totalBill
    return (bill / spiltBy)
}