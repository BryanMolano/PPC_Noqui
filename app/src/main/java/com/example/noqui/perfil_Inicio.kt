package com.example.noqui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import android.widget.TextView


class perfil_Inicio : AppCompatActivity() {
    private var numeroTelefonoUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_inicio)

        // Referencias a las vistas dentro del onCreate
        val btnVolverPerfil = findViewById<ImageButton>(R.id.btnVolverperfil)
        val btnInfoPerfil = findViewById<MaterialCardView>(R.id.btnInfoPerfil)
        val btnSeguridadPerfil = findViewById<MaterialCardView>(R.id.btnSeguridad)
        val tvTelefonoperfil = findViewById<TextView>(R.id.tvTelefonoperfil) // Referencia al TextView del teléfono
        numeroTelefonoUsuario = intent.getStringExtra("numero_telefono")
        if (numeroTelefonoUsuario != null) {
            tvTelefonoperfil.text = numeroTelefonoUsuario
        }

        // Botón volver → regresa al MainActivity
        btnVolverPerfil.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Botón información → abre Activity "Tu Información"
        btnInfoPerfil.setOnClickListener {
            val intent = Intent(this, Tu_Informacion::class.java)
            if (numeroTelefonoUsuario != null) {
                intent.putExtra("numero_telefono", numeroTelefonoUsuario)
            }
            startActivity(intent)
        }

        // Botón seguridad → abre Activity "Seguridad"
        btnSeguridadPerfil.setOnClickListener {
            val intent = Intent(this, Seguridad::class.java)
            startActivity(intent)
        }
    }
}
