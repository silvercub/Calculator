package com.example.calculator

abstract class MathOperation(val firstOperand: Float, val secondOperand: Float) {
    abstract val symbol: String
    abstract fun calculate(firstOperand: Float, secondOperand: Float): Float
}

class Add(private val x: Float, private val y: Float) : MathOperation(x, y) {
    override val symbol = "+"
    override fun calculate(firstOperand: Float, secondOperand: Float): Float {
        return firstOperand + secondOperand
    }

}

class Subtract(private val x: Float, private val y: Float) : MathOperation(x, y) {
    override val symbol = "-"
    override fun calculate(firstOperand: Float, secondOperand: Float): Float {
        return firstOperand - secondOperand
    }
}

class Multiply(private val x: Float, private val y: Float) : MathOperation(x, y) {
    override val symbol = "*"
    override fun calculate(firstOperand: Float, secondOperand: Float): Float {
        return firstOperand * secondOperand
    }
}

class Divide(private val x: Float, private val y: Float) : MathOperation(x, y) {
    override val symbol = "/"
    override fun calculate(firstOperand: Float, secondOperand: Float): Float {
        return firstOperand / secondOperand
    }
}