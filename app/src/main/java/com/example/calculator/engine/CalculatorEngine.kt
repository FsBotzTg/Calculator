package com.example.calculator.engine

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

enum class AngleUnit {
    DEG, RAD
}

sealed class EvalResult {
    data class Success(val value: Double, val formatted: String) : EvalResult()
    data class Error(val message: String) : EvalResult()
}

object CalculatorEngine {

    private const val MAX_PRECISION_DIGITS = 12

    /**
     * Evaluates a mathematical expression string given an angle unit (DEG or RAD).
     * Returns an EvalResult (Success or Error).
     */
    fun evaluate(expression: String, angleUnit: AngleUnit = AngleUnit.DEG, formatNumbers: Boolean = true): EvalResult {
        val cleanExpr = cleanExpression(expression)
        if (cleanExpr.isBlank()) {
            return EvalResult.Success(0.0, "0")
        }

        return try {
            val tokens = tokenize(cleanExpr)
            if (tokens.isEmpty()) {
                return EvalResult.Success(0.0, "0")
            }
            val rpn = shuntingYard(tokens)
            val rawValue = evaluateRpn(rpn, angleUnit)

            if (rawValue.isNaN()) {
                EvalResult.Error("Invalid expression")
            } else if (rawValue.isInfinite()) {
                if (rawValue > 0) EvalResult.Error("Value exceeds limit")
                else EvalResult.Error("Value exceeds limit")
            } else {
                val formatted = formatNumber(rawValue, formatNumbers)
                EvalResult.Success(rawValue, formatted)
            }
        } catch (e: ArithmeticException) {
            EvalResult.Error(e.message ?: "Arithmetic error")
        } catch (e: IllegalArgumentException) {
            EvalResult.Error(e.message ?: "Invalid expression")
        } catch (e: Exception) {
            EvalResult.Error("Invalid expression")
        }
    }

    /**
     * Formats a double number with clean decimal formatting and thousands separators.
     */
    fun formatNumber(value: Double, formatThousands: Boolean = true): String {
        if (value.isNaN()) return "Error"
        if (value.isInfinite()) return if (value > 0) "Infinity" else "-Infinity"

        // Handle exact zero
        if (abs(value) < 1e-12) return "0"

        // Check if value is very large or very small -> use scientific notation
        val absVal = abs(value)
        if ((absVal >= 1e11 || absVal < 1e-6) && absVal != 0.0) {
            val df = DecimalFormat("0.######E0", DecimalFormatSymbols(Locale.US))
            return df.format(value)
        }

        // Round to avoid floating point representation noise like 0.30000000000000004
        val bd = BigDecimal(value.toString()).setScale(10, RoundingMode.HALF_UP).stripTrailingZeros()
        val plainStr = bd.toPlainString()

        if (!formatThousands) {
            return plainStr
        }

        val parts = plainStr.split(".")
        val intPart = parts[0]
        val decPart = if (parts.size > 1) parts[1] else null

        val symbols = DecimalFormatSymbols(Locale.US)
        symbols.groupingSeparator = ','
        val intFormatter = DecimalFormat("#,##0", symbols)

        val formattedInt = try {
            intFormatter.format(intPart.toLong())
        } catch (e: Exception) {
            intPart
        }

        return if (decPart != null) {
            "$formattedInt.$decPart"
        } else {
            formattedInt
        }
    }

    private fun cleanExpression(expr: String): String {
        return expr
            .replace("×", "*")
            .replace("÷", "/")
            .replace("−", "-")
            .replace("π", "PI")
            .replace("e", "E_CONST")
            .replace("sin⁻¹", "asin")
            .replace("cos⁻¹", "acos")
            .replace("tan⁻¹", "atan")
            .replace("√", "sqrt")
            .replace(" ", "")
    }

    private enum class TokenType {
        NUMBER, OPERATOR, FUNCTION, PAREN_LEFT, PAREN_RIGHT
    }

    private data class Token(val type: TokenType, val value: String, val precedence: Int = 0, val rightAssociative: Boolean = false)

    private fun tokenize(expr: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var i = 0
        var expectUnary = true

        while (i < expr.length) {
            val c = expr[i]

            when {
                c.isDigit() || c == '.' -> {
                    val sb = StringBuilder()
                    while (i < expr.length && (expr[i].isDigit() || expr[i] == '.')) {
                        sb.append(expr[i])
                        i++
                    }
                    tokens.add(Token(TokenType.NUMBER, sb.toString()))
                    expectUnary = false
                }
                c == '+' || c == '-' -> {
                    if (expectUnary && c == '-') {
                        // Check if previous token allows unary minus or if it's start
                        tokens.add(Token(TokenType.OPERATOR, "u-", precedence = 4, rightAssociative = true))
                        i++
                    } else if (expectUnary && c == '+') {
                        // ignore unary plus
                        i++
                    } else {
                        val prec = 1
                        tokens.add(Token(TokenType.OPERATOR, c.toString(), precedence = prec))
                        i++
                        expectUnary = true
                    }
                }
                c == '*' || c == '/' || c == '%' -> {
                    val prec = 2
                    tokens.add(Token(TokenType.OPERATOR, c.toString(), precedence = prec))
                    i++
                    expectUnary = true
                }
                c == '^' -> {
                    tokens.add(Token(TokenType.OPERATOR, "^", precedence = 3, rightAssociative = true))
                    i++
                    expectUnary = true
                }
                c == '!' -> {
                    tokens.add(Token(TokenType.OPERATOR, "!", precedence = 4))
                    i++
                    expectUnary = false
                }
                c == '(' -> {
                    tokens.add(Token(TokenType.PAREN_LEFT, "("))
                    i++
                    expectUnary = true
                }
                c == ')' -> {
                    tokens.add(Token(TokenType.PAREN_RIGHT, ")"))
                    i++
                    expectUnary = false
                }
                expr.startsWith("PI", i) -> {
                    tokens.add(Token(TokenType.NUMBER, Math.PI.toString()))
                    i += 2
                    expectUnary = false
                }
                expr.startsWith("E_CONST", i) -> {
                    tokens.add(Token(TokenType.NUMBER, Math.E.toString()))
                    i += 7
                    expectUnary = false
                }
                expr.startsWith("asin", i) -> {
                    tokens.add(Token(TokenType.FUNCTION, "asin"))
                    i += 4
                    expectUnary = true
                }
                expr.startsWith("acos", i) -> {
                    tokens.add(Token(TokenType.FUNCTION, "acos"))
                    i += 4
                    expectUnary = true
                }
                expr.startsWith("atan", i) -> {
                    tokens.add(Token(TokenType.FUNCTION, "atan"))
                    i += 4
                    expectUnary = true
                }
                expr.startsWith("sin", i) -> {
                    tokens.add(Token(TokenType.FUNCTION, "sin"))
                    i += 3
                    expectUnary = true
                }
                expr.startsWith("cos", i) -> {
                    tokens.add(Token(TokenType.FUNCTION, "cos"))
                    i += 3
                    expectUnary = true
                }
                expr.startsWith("tan", i) -> {
                    tokens.add(Token(TokenType.FUNCTION, "tan"))
                    i += 3
                    expectUnary = true
                }
                expr.startsWith("log", i) -> {
                    tokens.add(Token(TokenType.FUNCTION, "log"))
                    i += 3
                    expectUnary = true
                }
                expr.startsWith("ln", i) -> {
                    tokens.add(Token(TokenType.FUNCTION, "ln"))
                    i += 2
                    expectUnary = true
                }
                expr.startsWith("sqrt", i) -> {
                    tokens.add(Token(TokenType.FUNCTION, "sqrt"))
                    i += 4
                    expectUnary = true
                }
                else -> {
                    i++ // Skip unknown
                }
            }
        }
        return tokens
    }

    private fun shuntingYard(tokens: List<Token>): List<Token> {
        val output = mutableListOf<Token>()
        val operatorStack = mutableListOf<Token>()

        for (token in tokens) {
            when (token.type) {
                TokenType.NUMBER -> output.add(token)
                TokenType.FUNCTION -> operatorStack.add(token)
                TokenType.OPERATOR -> {
                    while (operatorStack.isNotEmpty()) {
                        val top = operatorStack.last()
                        if (top.type == TokenType.OPERATOR &&
                            ((!token.rightAssociative && token.precedence <= top.precedence) ||
                                    (token.rightAssociative && token.precedence < top.precedence))
                        ) {
                            output.add(operatorStack.removeAt(operatorStack.lastIndex))
                        } else if (top.type == TokenType.FUNCTION) {
                            output.add(operatorStack.removeAt(operatorStack.lastIndex))
                        } else {
                            break
                        }
                    }
                    operatorStack.add(token)
                }
                TokenType.PAREN_LEFT -> operatorStack.add(token)
                TokenType.PAREN_RIGHT -> {
                    var foundParen = false
                    while (operatorStack.isNotEmpty()) {
                        val top = operatorStack.removeAt(operatorStack.lastIndex)
                        if (top.type == TokenType.PAREN_LEFT) {
                            foundParen = true
                            break
                        } else {
                            output.add(top)
                        }
                    }
                    if (!foundParen) {
                        throw IllegalArgumentException("Mismatched parentheses")
                    }
                    if (operatorStack.isNotEmpty() && operatorStack.last().type == TokenType.FUNCTION) {
                        output.add(operatorStack.removeAt(operatorStack.lastIndex))
                    }
                }
            }
        }

        while (operatorStack.isNotEmpty()) {
            val top = operatorStack.removeAt(operatorStack.lastIndex)
            if (top.type == TokenType.PAREN_LEFT || top.type == TokenType.PAREN_RIGHT) {
                throw IllegalArgumentException("Mismatched parentheses")
            }
            output.add(top)
        }

        return output
    }

    private fun evaluateRpn(rpn: List<Token>, angleUnit: AngleUnit): Double {
        val stack = mutableListOf<Double>()

        for (token in rpn) {
            when (token.type) {
                TokenType.NUMBER -> {
                    stack.add(token.value.toDouble())
                }
                TokenType.OPERATOR -> {
                    when (token.value) {
                        "u-" -> {
                            if (stack.isEmpty()) throw IllegalArgumentException("Invalid expression")
                            val a = stack.removeAt(stack.lastIndex)
                            stack.add(-a)
                        }
                        "!" -> {
                            if (stack.isEmpty()) throw IllegalArgumentException("Invalid expression")
                            val a = stack.removeAt(stack.lastIndex)
                            stack.add(factorial(a))
                        }
                        "%" -> {
                            if (stack.isEmpty()) throw IllegalArgumentException("Invalid expression")
                            val a = stack.removeAt(stack.lastIndex)
                            stack.add(a / 100.0)
                        }
                        "+", "-", "*", "/", "^" -> {
                            if (stack.size < 2) throw IllegalArgumentException("Invalid expression")
                            val b = stack.removeAt(stack.lastIndex)
                            val a = stack.removeAt(stack.lastIndex)
                            val result = when (token.value) {
                                "+" -> a + b
                                "-" -> a - b
                                "*" -> a * b
                                "/" -> {
                                    if (abs(b) < 1e-15) {
                                        throw ArithmeticException("Cannot divide by zero")
                                    }
                                    a / b
                                }
                                "^" -> a.pow(b)
                                else -> 0.0
                            }
                            stack.add(result)
                        }
                    }
                }
                TokenType.FUNCTION -> {
                    if (stack.isEmpty()) throw IllegalArgumentException("Invalid expression")
                    val a = stack.removeAt(stack.lastIndex)
                    val result = when (token.value) {
                        "sin" -> {
                            val rad = if (angleUnit == AngleUnit.DEG) Math.toRadians(a) else a
                            sin(rad)
                        }
                        "cos" -> {
                            val rad = if (angleUnit == AngleUnit.DEG) Math.toRadians(a) else a
                            cos(rad)
                        }
                        "tan" -> {
                            val rad = if (angleUnit == AngleUnit.DEG) Math.toRadians(a) else a
                            if (abs(cos(rad)) < 1e-14) throw ArithmeticException("Tangent undefined")
                            tan(rad)
                        }
                        "asin" -> {
                            if (a < -1.0 || a > 1.0) throw ArithmeticException("Domain error")
                            val rad = asin(a)
                            if (angleUnit == AngleUnit.DEG) Math.toDegrees(rad) else rad
                        }
                        "acos" -> {
                            if (a < -1.0 || a > 1.0) throw ArithmeticException("Domain error")
                            val rad = acos(a)
                            if (angleUnit == AngleUnit.DEG) Math.toDegrees(rad) else rad
                        }
                        "atan" -> {
                            val rad = atan(a)
                            if (angleUnit == AngleUnit.DEG) Math.toDegrees(rad) else rad
                        }
                        "log" -> {
                            if (a <= 0) throw ArithmeticException("Domain error")
                            log10(a)
                        }
                        "ln" -> {
                            if (a <= 0) throw ArithmeticException("Domain error")
                            ln(a)
                        }
                        "sqrt" -> {
                            if (a < 0) throw ArithmeticException("Domain error")
                            sqrt(a)
                        }
                        else -> 0.0
                    }
                    stack.add(result)
                }
                else -> {}
            }
        }

        if (stack.size != 1) {
            throw IllegalArgumentException("Invalid expression")
        }

        return stack[0]
    }

    private fun factorial(n: Double): Double {
        if (n < 0 || n != Math.floor(n)) {
            throw ArithmeticException("Factorial undefined for non-integers")
        }
        if (n > 170) {
            throw ArithmeticException("Factorial overflow")
        }
        var res = 1.0
        for (i in 2..n.toInt()) {
            res *= i
        }
        return res
    }
}
