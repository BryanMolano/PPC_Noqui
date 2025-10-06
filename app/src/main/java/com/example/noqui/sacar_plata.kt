package com.example.noqui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class sacar_plata : AppCompatActivity() {
    private lateinit var txtcodigo: TextView
    private lateinit var Contador: TextView
    private var codigoActual: Int = 0
    private var mostrarCodigo = false
    private lateinit var timer: CountDownTimer
    private var dineroDisponible: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sacar_plata)

        txtcodigo = findViewById<TextView>(R.id.codigoSacarPlata)
        val btnVerCodigo = findViewById<ImageButton>(R.id.btnVerCodigo)
        Contador = findViewById<TextView>(R.id.tiempoCodigo)
        val btnVolver = findViewById<ImageButton>(R.id.botonVolverSacarPlat)
        val cantidad = findViewById<EditText>(R.id.CantidadRetiro)
        val btnRetirar = findViewById<Button>(R.id.btnRetirar)

        dineroDisponible = intent.getDoubleExtra("dinero_disponible", 0.0)

        btnVolver.setOnClickListener {
            finish()
        }

        // Generar el primer código
        generarNuevoCodigo()

        // Mostrar / ocultar el código
        btnVerCodigo.setOnClickListener {
            mostrarCodigo = !mostrarCodigo
            actualizarTextoCodigo()
        }

        // Iniciar temporizador de 30 segundos
        iniciarTemporizador()

        btnRetirar.setOnClickListener {
            val cantidadTexto = cantidad.text.toString()
            if (cantidadTexto.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese una cantidad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val monto = cantidadTexto.toDoubleOrNull()
            if (monto == null || monto <= 0) {
                Toast.makeText(this, "Ingrese un monto válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (monto > dineroDisponible) {
                Toast.makeText(this, "Saldo insuficiente", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val nuevoSaldo = dineroDisponible - monto
            val resultIntent = Intent().apply {
                putExtra("nuevo_dinero_disponible", nuevoSaldo)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun iniciarTemporizador() {
        val tiempoTotal = 30 * 1000L // medio minuto = 30 segundos

        timer = object : CountDownTimer(tiempoTotal, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val segundos = millisUntilFinished / 1000
                val tiempoFormateado = String.format("00:%02d", segundos)
                Contador.text = tiempoFormateado
            }

            override fun onFinish() {
                generarNuevoCodigo()
                iniciarTemporizador() // reiniciar ciclo automáticamente
            }
        }
        timer.start()
    }

    private fun generarNuevoCodigo() {
        codigoActual = Random.nextInt(10000, 99999)
        actualizarTextoCodigo()
    }

    private fun actualizarTextoCodigo() {
        txtcodigo.text = if (mostrarCodigo) codigoActual.toString() else "*****"
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::timer.isInitialized) timer.cancel()
    }
}