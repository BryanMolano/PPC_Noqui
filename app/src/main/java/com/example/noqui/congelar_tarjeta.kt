package com.example.noqui

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class congelar_tarjeta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_congelar_tarjeta)

        val btnVolver = findViewById<ImageButton>(R.id.botonVolverCongTj)

        btnVolver.setOnClickListener {
            finish()
        }
    }
}