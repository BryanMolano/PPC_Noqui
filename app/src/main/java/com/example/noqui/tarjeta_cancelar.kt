package com.example.noqui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class tarjeta_cancelar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tarjeta_cancelar)

        val btnNo = findViewById<Button>(R.id.btnNo)
        btnNo.setOnClickListener {
            finish()
        }
        val btnSi = findViewById<Button>(R.id.btnSi)
        btnSi.setOnClickListener {
            val prefs = getSharedPreferences("TarjetaPrefs", MODE_PRIVATE)
            prefs.edit().putBoolean("tarjeta_cancelada", true).apply()

            val intent = Intent(this, tarjeta_sacarTJ::class.java)
            startActivity(intent)
            finish()
        }
        val btnFondo = findViewById<ImageButton>(R.id.btnFondo)
        btnFondo.setOnClickListener {
            finish()
        }
    }
}