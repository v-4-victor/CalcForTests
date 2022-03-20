package com.example.calcfortests

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlin.math.sqrt

class MainActivity : Activity() {
    private enum class Operation {
        ADD, SUB, MULT, DIV, NONE
    }

    private var txtCalcDisplay: TextView? = null
    private var txtCalcOperator: TextView? = null
    private var operation: Operation? = null
    private var decimals = false
    private var resetDisplay = false
    private var performOperation = false
    private var value = 0.0
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtCalcDisplay = findViewById<View>(R.id.txt_calc_display) as TextView
        txtCalcOperator = findViewById<View>(R.id.txt_calc_operator) as TextView
        operation = Operation.NONE
    }

    fun onDigitPressed(v: View) {
        if (resetDisplay) {
            txtCalcDisplay?.text = null
            resetDisplay = false
        }
        txtCalcOperator?.text = null
        if (decimals || !only0IsDisplayed()) txtCalcDisplay!!.append((v as Button).text)
        if (operation != Operation.NONE) performOperation = true
    }

    fun onOperatorPressed(v: View) {
        if (performOperation) {
            performOperation()
            performOperation = false
        }
        when (v.id) {
            R.id.btn_op_divide -> {
                operation = Operation.DIV
                txtCalcOperator!!.text = "/"
            }
            R.id.btn_op_multiply -> {
                operation = Operation.MULT
                txtCalcOperator!!.text = "x"
            }
            R.id.btn_op_subtract -> {
                operation = Operation.SUB
                txtCalcOperator!!.text = "–"
            }
            R.id.btn_op_add -> {
                operation = Operation.ADD
                txtCalcOperator!!.text = "+"
            }
            R.id.btn_op_equals -> {
            }
            else -> throw RuntimeException("Unsupported operation.")
        }
        resetDisplay = true
        value = displayValue
    }

    fun onSpecialPressed(v: View) {
        when (v.id) {
            R.id.btn_spec_sqroot -> {
                val value = displayValue
                val sqrt = sqrt(value)
                txtCalcDisplay!!.text = sqrt.toString()
            }
            R.id.btn_spec_pi -> {
                resetDisplay = false
                txtCalcOperator?.text = null
                txtCalcDisplay!!.text = Math.PI.toString()
                if (operation != Operation.NONE) performOperation = true
                return
            }
            R.id.btn_spec_percent -> {
                val value = displayValue
                val percent = value / 100.0f
                txtCalcDisplay!!.text = percent.toString()
            }
            R.id.btn_spec_comma -> {
                if (!decimals) {
                    val text = if (displayIsEmpty()) "0." else "."
                    txtCalcDisplay!!.append(text)
                    decimals = true
                }
            }
            R.id.btn_spec_clear -> {
                value = 0.0
                decimals = false
                operation = Operation.NONE
                txtCalcDisplay?.text = null
                txtCalcOperator?.text = null
            }
        }
        resetDisplay = false
        performOperation = false
    }

    private fun performOperation() {
        val display = displayValue
        value = when (operation) {
            Operation.DIV -> value / display
            Operation.MULT -> value * display
            Operation.SUB -> value - display
            Operation.ADD -> value + display
            Operation.NONE -> return
            else -> throw RuntimeException("Unsupported operation.")
        }
        txtCalcOperator!!.text = null
        txtCalcDisplay!!.text = value.toString()
    }

    private fun only0IsDisplayed(): Boolean {
        val text = txtCalcDisplay!!.text
        return text.length == 1 && text[0] == '0'
    }

    private fun displayIsEmpty(): Boolean {
        return txtCalcDisplay!!.text.isEmpty()
    }

    private val displayValue: Double
         get() {
            val display = txtCalcDisplay!!.text.toString()
            return if (display.isEmpty()) 0.02 else display.toDouble()
        }
}
