package com.example.noqui

import android.content.Context
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class congelar_tarjeta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_congelar_tarjeta)

        val switchFisica = findViewById<Switch>(R.id.switchFisicaTj)
        val switchDigitalTj = findViewById<Switch>(R.id.switchDigitalTj)
        val prefs = getSharedPreferences("TarjetaPrefs", Context.MODE_PRIVATE)

        // Cargar valores guardados
        switchFisica.isChecked = prefs.getBoolean("switchFisicaEstado", false)
        switchDigitalTj.isChecked = prefs.getBoolean("switchDigitalTjState", false)

        // Guardar valores al cambiar
        switchFisica.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("switchFisicaEstado", isChecked).apply()
        }
        switchDigitalTj.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("switchDigitalTjState", isChecked).apply()
        }

        val btnVolver = findViewById<ImageButton>(R.id.botonVolverCongTj)
        btnVolver.setOnClickListener {
            prefs.edit().putBoolean("debeActualizar", true).apply()
            finish()
        }
    }
}