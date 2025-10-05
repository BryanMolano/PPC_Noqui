package com.example.noqui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class tarjeta_configuracion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tarjeta_configuracion)

        val btnVolver = findViewById<ImageButton>(R.id.botonVolverConfigTj)
        btnVolver.setOnClickListener {
            finish()
        }
        val btnCongelar = findViewById<ImageButton>(R.id.btnImgCongelar)
        btnCongelar.setOnClickListener {
            val intent = Intent(this, congelar_tarjeta::class.java)
            startActivity(intent)
        }
        val btnCancelar = findViewById<ImageButton>(R.id.btnImgCancelar)
        btnCancelar.setOnClickListener {
            val intent = Intent(this, tarjeta_cancelar::class.java)
            startActivity(intent)
        }

    }
}