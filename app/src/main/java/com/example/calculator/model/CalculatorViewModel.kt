package com.example.calculator.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calculator.*
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

private const val FIRST_OPERAND = 1
private const val SECOND_OPERAND = 2
private const val TEN = 10
private const val ZERO_LONG: Long = 0
private const val NEGATIVE_ONE = -1


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
    private val _previousCalcResult: Double = 0.0

    init {
        reset()
    }

    /*
     * Update the calculator display text with either the current operation that's being entered
     * or the calculation result.
     */
    private fun updateDisplay() {
        val totalFirstOperand = getTotalFirstOperand().toString()
        _calculationDisplay.value = when (getCurrentOperand()) {
            FIRST_OPERAND -> totalFirstOperand
            else -> totalFirstOperand + _mathOperation?.symbol +
                    getTotalSecondOperand().toString()
        }
    }

    /*
     * Update the calculator display text with either the current operation that's being entered
     * or the calculation result.
     */
    private fun getTotalFirstOperand(): Double = getTotalOperand(
        _wholeFirstOperand,
        _fractionalFirstOperand, _decimalModeFirstOperand
    )

    /*
     * Helper method to return the operand(number portion of the calculation)
     * that is second in the math operation
     */
    private fun getTotalSecondOperand(): Double = getTotalOperand(
        _wholesSecondOperand,
        _fractionalFirstOperand,
        _decimalModeSecondOperand
    )

    fun calculate() {
        // TODO: implement calculation function(https://florianz.atlassian.net/browse/CAL-14?atlOrigin=eyJpIjoiOWUzNDk4NzBiN2VlNGE2YWI4ODIzNTM1YjgyNGQ5ZmEiLCJwIjoiaiJ9)
    }

    /*
     * Add the new button press on to the end of the appropriate either the whole or fractional part
     * of the appropriate operand
     */
    fun setOrExtendOperand(buttonPress: Int) =
        applyToAppropriateOperand(buttonPress, this::setValueIfZeroOtherwiseExtend)

    /*
     * clear the last entry from the calculator
     */
    fun deleteLastEntry() =
        applyToAppropriateOperand(this::backspace)

    /*
     * Helper function to "erase" an integer from and input integer by truncating the last value
     */
    private fun backspace(originalValue: Long): Long {
        return originalValue / TEN
    }

    /*
     * Helper function to put the number in the next available column
     *  to 'append' the new number pressed to the previous one
     */
    private fun extendByOneTensPlace(originalValue: Long): Long =
        TEN * originalValue

    /*
     * Determine the currently active operand. If there is no math operation set then the first
     * operand is still active. Otherwise the second operator is active.
     */
    private fun getCurrentOperand(): Int {
        return when (_mathOperation) {
            null -> FIRST_OPERAND
            else -> SECOND_OPERAND
        }
    }

    /*
     * If the operand is 0 replace the value with the input. If there is already input then
     * add the new numeric input to the tens column and shift the previous number to the left.
     */
    private fun setValueIfZeroOtherwiseExtend(operand: Long, newVal: Int): Long {
        return if (operand == ZERO_LONG) {
            newVal.toLong()
        } else {
            return newVal + extendByOneTensPlace(operand)
        }
    }

    /*
     * Get the entire operand by combining the whole and fractional part of the operand.
     */
    private fun getTotalOperand(
        wholeOperand: Long,
        decimalOperand: Long, decimalFlag: Boolean
    ): Double {
        return if (!decimalFlag) {
            wholeOperand.toDouble()
        } else {
            // add the non-decimal(whole part)
            // and convert the decimal part by dividing it
            // by 10 to the power of the decimal parts length
            wholeOperand + decimalOperand / (TEN.toDouble().pow(decimalOperand.length()))
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

    /*
     * converts an integer to its length ignoring it's equal sign.
     * source: https://stackoverflow.com/questions/42950812/count-number-of-digits-in-kotlin
     */
    private fun Long.length() = when (this) {
        ZERO_LONG -> 1
        else -> log10(abs(toDouble())).toInt() + 1
    }

    /*
     * Helper function to modify the active operand.
     */
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

    /*
     * Determine the active operand and set decimal mode. When decimal mode is active all numeric
     * input will be added to the decimal part of the operand.
     */
    fun setDecimalMode() {
        when (getCurrentOperand()) {
            FIRST_OPERAND -> _decimalModeFirstOperand = true
            else -> _decimalModeSecondOperand = true
        }
    }

    /*
     * Helper function to modify the active operand.
     */
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

    /*
     * Invert the active operand.
     */
    fun invertOperator() {
        when (getCurrentOperand()) {
            FIRST_OPERAND -> _wholeFirstOperand * NEGATIVE_ONE
            else -> _wholesSecondOperand * NEGATIVE_ONE

        }
    }

    /*
     * Set the active operand to addition.
     */
    fun setAdditionOperator() {
        _mathOperation = Add()
    }

    /*
     * Set the active operand to subtraction.
     */
    fun setSubtractionOperator() {
        _mathOperation = Subtract()
    }

    /*
     * Set the active operand to multiply.
     */
    fun setMultiplicationOperator() {
        _mathOperation = Multiply()
    }

    /*
     * set the active operand to division.
     */
    fun setDivisionOperator() {
        _mathOperation = Divide()
    }
}