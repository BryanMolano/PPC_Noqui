package com.example.noqui

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageButton
import android.widget.TextView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sacar_plata)

        txtcodigo = findViewById<TextView>(R.id.codigoSacarPlata)
        val btnVerCodigo = findViewById<ImageButton>(R.id.btnVerCodigo)
        Contador = findViewById<TextView>(R.id.tiempoCodigo)
        val btnVolver = findViewById<ImageButton>(R.id.botonVolverSacarPlat)

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