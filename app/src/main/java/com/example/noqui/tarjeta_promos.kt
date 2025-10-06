package com.example.noqui

import android.app.Activity
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class tarjeta_promos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tarjeta_promos)

        val btnVolver = findViewById<ImageButton>(R.id.botonVolverTj2)
        btnVolver.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

    }
}