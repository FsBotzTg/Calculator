package com.example

import com.example.calculator.engine.AngleUnit
import com.example.calculator.engine.CalculatorEngine
import com.example.calculator.engine.EvalResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculatorEngineTest {

    @Test
    fun testBasicArithmetic() {
        val res1 = CalculatorEngine.evaluate("125 × 48")
        assertTrue(res1 is EvalResult.Success)
        assertEquals("6,000", (res1 as EvalResult.Success).formatted)

        val res2 = CalculatorEngine.evaluate("10 + 5 × 2")
        assertTrue(res2 is EvalResult.Success)
        assertEquals("20", (res2 as EvalResult.Success).formatted)

        val res3 = CalculatorEngine.evaluate("(10 + 5) × 2")
        assertTrue(res3 is EvalResult.Success)
        assertEquals("30", (res3 as EvalResult.Success).formatted)
    }

    @Test
    fun testDecimalsAndNegatives() {
        val res1 = CalculatorEngine.evaluate("12.5 − 3.2")
        assertTrue(res1 is EvalResult.Success)
        assertEquals("9.3", (res1 as EvalResult.Success).formatted)

        val res2 = CalculatorEngine.evaluate("-15 + 5")
        assertTrue(res2 is EvalResult.Success)
        assertEquals("-10", (res2 as EvalResult.Success).formatted)
    }

    @Test
    fun testScientificTrigonometry() {
        val sinDeg = CalculatorEngine.evaluate("sin(90)", AngleUnit.DEG)
        assertTrue(sinDeg is EvalResult.Success)
        assertEquals("1", (sinDeg as EvalResult.Success).formatted)

        val cosDeg = CalculatorEngine.evaluate("cos(0)", AngleUnit.DEG)
        assertTrue(cosDeg is EvalResult.Success)
        assertEquals("1", (cosDeg as EvalResult.Success).formatted)
    }

    @Test
    fun testDivisionByZero() {
        val res = CalculatorEngine.evaluate("10 ÷ 0")
        assertTrue(res is EvalResult.Error)
        assertEquals("Cannot divide by zero", (res as EvalResult.Error).message)
    }

    @Test
    fun testInvalidExpression() {
        val res = CalculatorEngine.evaluate("10 + × 5")
        assertTrue(res is EvalResult.Error)
    }

    @Test
    fun testFactorialAndLog() {
        val fact = CalculatorEngine.evaluate("5!")
        assertTrue(fact is EvalResult.Success)
        assertEquals("120", (fact as EvalResult.Success).formatted)

        val logVal = CalculatorEngine.evaluate("log(100)")
        assertTrue(logVal is EvalResult.Success)
        assertEquals("2", (logVal as EvalResult.Success).formatted)
    }
}
