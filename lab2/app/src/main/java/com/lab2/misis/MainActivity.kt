package com.lab2.misis

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val number_field_1 = findViewById<EditText>(R.id.number_field_1)
        val number_field_2 = findViewById<EditText>(R.id.number_field_2)
        val label_text = findViewById<TextView>(R.id.textView3)
        val result_text = findViewById<TextView>(R.id.result_text)
        val result_number = findViewById<TextView>(R.id.result_number)

        val button_calculate = findViewById<Button>(R.id.button_calculate)
        val button_reset = findViewById<Button>(R.id.reset_button)

        val spinner_actions = findViewById<Spinner>(R.id.actions_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.math_actions,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_actions.adapter = adapter
        }

        button_calculate.setOnClickListener{
            val n1 = number_field_1.text.toString().toFloat()
            val n2 = number_field_2.text.toString().toFloat()
            var res = 0f

            val selectedAction = spinner_actions.selectedItem.toString()

            if (selectedAction == "Сложение") {
                res = n1 + n2
            }
            if (selectedAction == "Вычитание") {
                res = n1 - n2
            }
            if (selectedAction == "Умножение") {
                res = n1 * n2
            }
            if (selectedAction == "Деление") {
                if (n2 != 0f) {
                    res = n1 / n2
                } else {
                    result_number.setText("Ошибка: деление на 0")
                    return@setOnClickListener
                }
            }
            result_number.setText(res.toString());
        }

        button_reset.setOnClickListener{
            number_field_1.text.clear()
            number_field_2.text.clear()
            result_number.setText("")
        }
    }
}