package com.example.calculator.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calculator.MathOperation
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

private const val FIRST_OPERAND = 1
private const val SECOND_OPERAND = 2
private const val TEN_DOUBLE = 10.0
private const val ZERO_LONG: Long = 0


class CalculatorViewModel : ViewModel() {
    private var _wholeFirstOperand: Long = 0
    private var _wholesSecondOperand: Long = 0
    private var _fractionalFirstOperand: Long = 0
    private var _fractionalSecondOperand: Long = 0
    private var _mathOperation = MutableLiveData<MathOperation?>()
    private val _calculationDisplay = MutableLiveData<String>()
    val calculationDisplay: LiveData<String>
        get() = _calculationDisplay
    private var _decimalModeFirstOperand: Boolean = false
    private var _decimalModeSecondOperand: Boolean = false

    init {
        reset()
    }

    private fun updateDisplay() {
        val totalFirstOperand = getTotalOperand(
            _wholeFirstOperand,
            _fractionalFirstOperand, _decimalModeFirstOperand
        ).toString()
        _calculationDisplay.value = when (getCurrentOperand()) {
            FIRST_OPERAND -> totalFirstOperand
            else -> totalFirstOperand + _mathOperation.value!!.symbol +
                    getTotalOperand(
                        _wholesSecondOperand,
                        _fractionalFirstOperand,
                        _decimalModeSecondOperand
                    )
        }
    }

    /*
     * Add the new button press on to the end of the appropriate either the whole or fractional part
     * of the appropriate operand
     */
    fun setOrExtendOperand(buttonPress: Int) {
        when (getCurrentOperand()) {
            FIRST_OPERAND -> {
                if (_decimalModeFirstOperand) {
                    _fractionalFirstOperand =
                        setValueIfZeroOtherwiseExtend(_fractionalFirstOperand, buttonPress)
                } else {
                    _wholeFirstOperand =
                        setValueIfZeroOtherwiseExtend(_wholeFirstOperand, buttonPress)
                }
            }
            else -> {
                if (_decimalModeSecondOperand) {
                    _fractionalSecondOperand =
                        setValueIfZeroOtherwiseExtend(_fractionalFirstOperand, buttonPress)
                } else {
                    _wholesSecondOperand =
                        setValueIfZeroOtherwiseExtend(_wholesSecondOperand, buttonPress)
                }
            }
        }
        updateDisplay()
    }

    /*
     * Helper function to put the number in the next available column
     *  to 'append' the new number pressed to the previous one
     */
    private fun extendByPowerOfTen(originalValue: Long, buttonPress: Int): Int =
        TEN_DOUBLE.pow(originalValue.length()).toInt() * buttonPress


    private fun getCurrentOperand(): Int {
        return when (_mathOperation.value) {
            null -> FIRST_OPERAND
            else -> SECOND_OPERAND
        }
    }

    private fun setValueIfZeroOtherwiseExtend(operand: Long, newVal: Int): Long {
        return if (operand == ZERO_LONG) {
            newVal.toLong()
        } else {
            return operand + extendByPowerOfTen(operand, newVal)
        }
    }

    private fun getTotalOperand(
        wholeOperand: Long,
        decimalOperand: Long, decimalFlag: Boolean
    ): Float {
        return if (decimalFlag) {
            wholeOperand.toFloat()
        } else {
            // add the non-decimal(whole part)
            // and convert the decimal part by dividing it by 10 times the length
            wholeOperand + (decimalOperand.toFloat() / (decimalOperand.length() * 10))
        }
    }

    /*
     * Reset all values used to store data for calculator to their initial values.
     */
    fun reset() {
        _wholeFirstOperand = 0
        _wholesSecondOperand = 0
        _fractionalFirstOperand = 0
        _fractionalSecondOperand = 0
        _mathOperation.value = null
        _decimalModeFirstOperand = false
        _decimalModeSecondOperand = false
        updateDisplay()
    }

    /**
     * converts an integer to its length ignoring it's equal sign.
     * source: https://stackoverflow.com/questions/42950812/count-number-of-digits-in-kotlin
     */
    private fun Long.length() = when (this) {
        ZERO_LONG -> 1
        else -> log10(abs(toDouble())).toInt() + 1
    }
}