package com.example.noqui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView


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


        val editPlata = findViewById<EditText>(R.id.editPlata)
        val editNumero = findViewById<EditText>(R.id.edit_numero)
        val editMsg = findViewById<EditText>(R.id.editMsg)
        val btnSigue = findViewById<ImageButton>(R.id.btnSigue)
        val btnAtras = findViewById<ImageView>(R.id.btnAtras)


        btnAtras.setOnClickListener {
            finish()
        }


        //Conversion del campo plata 1000 -> 1.000
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
        // Obtener referencias a los campos y al botón
        btnSigue.setOnClickListener {
            // Obtener los valores de los campos
            val numero = editNumero.text.toString().trim()
            val plata = editPlata.text.toString().trim()
            val mensaje = editMsg.text.toString().trim()

            // Validación básica
            if (numero.isEmpty()) {
                editNumero.error = "Ingresa un número"
                return@setOnClickListener
            }
            if (plata.replace("$", "").replace(".", "").trim().isEmpty()) {
                editPlata.error = "Ingresa una cantidad"
                return@setOnClickListener
            }

            // Crear y mostrar el Fragmento de Diálogo
            val dialogoConfirmacion = ConfirmarEnvioDialog.newInstance(
                numero = numero,
                plata = plata,
                mensaje = if (mensaje.isEmpty()) "Sin mensaje" else mensaje
            )
            dialogoConfirmacion.show(supportFragmentManager, "ConfirmarEnvioTag")
        }


    }

}