package com.example.noqui

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class sacar_plata : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sacar_plata)

        val btnVolver = findViewById<ImageButton>(R.id.botonVolverSacarPlat)

        btnVolver.setOnClickListener {
            finish()
        }

        val Contador = findViewById<TextView>(R.id.tiempoCodigo)

        val tiempoTotal = 30 * 60 * 1000L  // 30 minutos

        val timer = object : CountDownTimer(tiempoTotal, 1000) { // cada segundo
            override fun onTick(millisUntilFinished: Long) {
                val minutos = millisUntilFinished / 1000 / 60
                val segundos = (millisUntilFinished / 1000) % 60
                val tiempoFormateado = String.format("%02d:%02d", minutos, segundos)
                Contador.text = tiempoFormateado
            }

            override fun onFinish() {
                Contador.text = "00:00"
            }
        }
        timer.start()
    }
}