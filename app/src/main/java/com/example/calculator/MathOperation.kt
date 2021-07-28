package com.example.calculator

abstract class MathOperation {
    abstract val symbol: String
    abstract fun calculate(firstOperand: Double, secondOperand: Double): Double
}

class Add : MathOperation() {
    override val symbol = "+"
    override fun calculate(firstOperand: Double, secondOperand: Double): Double {
        return firstOperand + secondOperand
    }

}

class Subtract : MathOperation() {
    override val symbol = "-"
    override fun calculate(firstOperand: Double, secondOperand: Double): Double {
        return firstOperand - secondOperand
    }
}

class Multiply : MathOperation() {
    override val symbol = "*"
    override fun calculate(firstOperand: Double, secondOperand: Double): Double {
        return firstOperand * secondOperand
    }
}

class Divide : MathOperation() {
    override val symbol = "/"
    override fun calculate(firstOperand: Double, secondOperand: Double): Double {
        return firstOperand / secondOperand
    }
}