package com.example.calculator.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calculator.MathOperation
import kotlin.math.abs
import kotlin.math.log10

private const val FIRST_OPERAND = 1
private const val SECOND_OPERAND = 2
private const val FIRST_OPERAND_WITH_DECIMAL_MODE = 3
private const val SECOND_OPERAND_WITH_DECIMAL_MODE = 4
private const val TEN = 10
private const val ZERO_LONG: Long = 0


class CalculatorViewModel : ViewModel() {
    private var _wholeFirstOperand: Long = 0
    private var _wholesSecondOperand: Long = 0
    private var _fractionalFirstOperand: Long = 0
    private var _fractionalSecondOperand: Long = 0
    private var _mathOperation: MathOperation? = null
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
            else -> totalFirstOperand + _mathOperation?.symbol +
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
    fun setOrExtendOperand(buttonPress: Int) =
        applyToAppropriateOperand(buttonPress, this::setValueIfZeroOtherwiseExtend)

    fun deleteLastEntry() =
        applyToAppropriateOperand(this::backspace)

    private fun backspace(originalValue: Long): Long {
        return originalValue / TEN
    }

    /*
     * Helper function to put the number in the next available column
     *  to 'append' the new number pressed to the previous one
     */
    private fun extendByOneTensPlace(originalValue: Long): Long =
        TEN * originalValue

    private fun getCurrentOperand(): Int {
        return when (_mathOperation) {
            null -> FIRST_OPERAND
            else -> SECOND_OPERAND
        }
    }

    private fun setValueIfZeroOtherwiseExtend(operand: Long, newVal: Int): Long {
        return if (operand == ZERO_LONG) {
            newVal.toLong()
        } else {
            return newVal + extendByOneTensPlace(operand)
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
        _mathOperation = null
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

    private fun applyToAppropriateOperand(buttonPress: Int, changeOperand: (Long, Int) -> Long) {
        when (getCurrentOperand()) {
            FIRST_OPERAND -> {
                if (_decimalModeFirstOperand) {
                    _fractionalFirstOperand =
                        changeOperand(_fractionalFirstOperand, buttonPress)
                } else {
                    _wholeFirstOperand =
                        changeOperand(_wholeFirstOperand, buttonPress)
                }
            }
            else -> {
                if (_decimalModeSecondOperand) {
                    _fractionalSecondOperand =
                        changeOperand(_fractionalFirstOperand, buttonPress)
                } else {
                    _wholesSecondOperand =
                        changeOperand(_wholesSecondOperand, buttonPress)
                }
            }
        }
        updateDisplay()
    }

    private fun applyToAppropriateOperand(changeOperand: (Long) -> Long) {
        when (getCurrentOperand()) {
            FIRST_OPERAND -> {
                if (_decimalModeFirstOperand) {
                    _fractionalFirstOperand =
                        changeOperand(_fractionalFirstOperand)
                } else {
                    _wholeFirstOperand =
                        changeOperand(_wholeFirstOperand)
                }
            }
            else -> {
                if (_decimalModeSecondOperand) {
                    _fractionalSecondOperand =
                        changeOperand(_fractionalFirstOperand)
                } else {
                    _wholesSecondOperand =
                        changeOperand(_wholesSecondOperand)
                }
            }
        }
        updateDisplay()
    }
}