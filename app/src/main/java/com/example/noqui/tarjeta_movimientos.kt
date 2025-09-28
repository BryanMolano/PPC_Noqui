package com.example.noqui

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class tarjeta_movimientos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tarjeta_movimientos)

        val btnVolver = findViewById<ImageButton>(R.id.botonVolverMovmTj)

        btnVolver.setOnClickListener {
            finish()
        }
    }
}