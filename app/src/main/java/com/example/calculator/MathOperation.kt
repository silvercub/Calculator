package com.example.calculator

abstract class MathOperation(val firstOperand: Double, val secondOperand: Double) {
    abstract val symbol: String
    abstract fun calculate(firstOperand: Double, secondOperand: Double): Double
}

class Add(private val x: Double, private val y: Double) : MathOperation(x, y) {
    override val symbol = "+"
    override fun calculate(firstOperand: Double, secondOperand: Double): Double {
        return firstOperand + secondOperand
    }

}

class Subtract(private val x: Double, private val y: Double) : MathOperation(x, y) {
    override val symbol = "-"
    override fun calculate(firstOperand: Double, secondOperand: Double): Double {
        return firstOperand - secondOperand
    }
}

class Multiply(private val x: Double, private val y: Double) : MathOperation(x, y) {
    override val symbol = "*"
    override fun calculate(firstOperand: Double, secondOperand: Double): Double {
        return firstOperand * secondOperand
    }
}

class Divide(private val x: Double, private val y: Double) : MathOperation(x, y) {
    override val symbol = "/"
    override fun calculate(firstOperand: Double, secondOperand: Double): Double {
        return firstOperand / secondOperand
    }
}