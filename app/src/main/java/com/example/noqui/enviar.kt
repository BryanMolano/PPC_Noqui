package com.example.noqui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


class enviar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_enviar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Conversion del campo plata 1000 -> 1.000
        val editPlata = findViewById<EditText>(R.id.editPlata)

        editPlata.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    editPlata.removeTextChangedListener(this)

                    //quitar todo lo que no sean dígitos
                    val cleanString = s.toString().replace("[^\\d]".toRegex(), "")

                    if (cleanString.isNotEmpty()) {
                        val parsed = cleanString.toLong()
                        // Formato con puntos de miles
                        val formatted = "$ " + "%,d".format(parsed).replace(",", ".")
                        current = formatted
                        editPlata.setText(formatted)
                        editPlata.setSelection(formatted.length)
                    } else {
                        current = ""
                        editPlata.setText("")
                    }

                    editPlata.addTextChangedListener(this)
                }
            }
        })

    }
}