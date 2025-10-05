package com.example.noqui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class tarjeta_sacarTJ : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tarjeta_sacar_tj)

        val btnVolver = findViewById<ImageButton>(R.id.botonVolverTj)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val btnAyuda = findViewById<ImageButton>(R.id.btnImgPreguntaTj2)
        btnAyuda.setOnClickListener {
            val intent = Intent(this, tarjeta_ayuda::class.java)
            startActivity(intent)
        }
        val btnSacarTj = findViewById<ImageButton>(R.id.btnSacarTj)
        btnSacarTj.setOnClickListener {
            // Simula “sacar” una nueva tarjeta
            val prefs = getSharedPreferences("TarjetaPrefs", MODE_PRIVATE)
            prefs.edit().putBoolean("tarjeta_cancelada", false).apply()

            val intent = Intent(this, tarjeta::class.java)
            startActivity(intent)
            finish()
        }
    }
}