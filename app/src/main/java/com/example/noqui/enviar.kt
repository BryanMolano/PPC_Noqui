package com.example.noqui

import android.app.Activity
import android.content.Intent
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
import android.widget.Toast

class enviar : AppCompatActivity(), ConfirmacionListener {

    private var dineroDisponible: Double = 0.0
    private lateinit var editPlata: EditText
    private lateinit var editNumero: EditText
    private lateinit var editMsg: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_enviar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dineroDisponible = intent.getDoubleExtra("dinero_disponible", 0.0)


        editPlata = findViewById(R.id.editPlata)
        editNumero = findViewById(R.id.edit_numero)
        editMsg = findViewById(R.id.editMsg)
        val btnSigue = findViewById<ImageButton>(R.id.btnSigue)
        val btnAtras = findViewById<ImageView>(R.id.btnAtras)


        btnAtras.setOnClickListener {
            finish()
        }


        // Conversion del campo plata 1000 -> 1.000
        editPlata.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    editPlata.removeTextChangedListener(this)

                    // quitar todo lo que no sean dígitos
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
            // Validación de la cantidad de plata
            val cleanPlata = plata.replace("$", "").replace(".", "").trim()
            if (cleanPlata.isEmpty()) {
                editPlata.error = "Ingresa una cantidad"
                return@setOnClickListener
            }

            // Crear y mostrar el Fragmento de Diálogo
            val dialogoConfirmacion = ConfirmarEnvioDialog.newInstance(
                numero = numero,
                plata = plata,
                mensaje = mensaje.ifEmpty { "Sin mensaje" }
            )
            dialogoConfirmacion.show(supportFragmentManager, "ConfirmarEnvioTag")
        }
    }
    // Implementación del método de la interfaz ConfirmacionListener
    override fun onEnvioConfirmado(monto: Double, numero: String, mensaje: String) {

        // Validar si el saldo es suficiente antes de proceder
        if (monto > dineroDisponible) {
            Toast.makeText(this, "Saldo insuficiente para realizar el envío.", Toast.LENGTH_LONG).show()
            return
        }

        // Actualizar el Saldo y enviar resultado a MainActivity
        val nuevoSaldo = dineroDisponible - monto

        // Crea el Intent de resultado para que MainActivity actualice el saldo
        val resultIntent = Intent().apply {
            putExtra("nuevo_dinero_disponible", nuevoSaldo)
        }
        setResult(Activity.RESULT_OK, resultIntent)


        val intentEnvio = Intent(this, envio_realizado::class.java).apply {
            putExtra("numero", numero)
            // Se usa el texto formateado (ej: "$ 1.000") para mostrar
            putExtra("monto", editPlata.text.toString())
            putExtra("mensaje", mensaje)
        }
        startActivity(intentEnvio)

        finish()
    }
}