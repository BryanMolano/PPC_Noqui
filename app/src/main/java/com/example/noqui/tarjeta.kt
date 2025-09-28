package com.example.noqui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.content.Intent
import android.widget.ImageButton


class tarjeta : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tarjeta)

        val numTarj = "1111222233334444"
        val expiraTj = "11/28"
        val codigoTj = "123"
        val nomUsuario = "Nombre Apellido"

        val numTarjeta = findViewById<TextView>(R.id.numTarjeta)
        val expiraTarjeta = findViewById<TextView>(R.id.expTj)
        val codTarjeta = findViewById<TextView>(R.id.codTj)
        val usuario = findViewById<TextView>(R.id.usuarioTj)
        numTarjeta.text = numTarj
        expiraTarjeta.text = expiraTj
        codTarjeta.text = codigoTj
        usuario.text = nomUsuario

        val btnMovTj = findViewById<ImageButton>(R.id.btnImgMovTarjeta)
        val btnCongTj = findViewById<ImageButton>(R.id.btnImgCongTarjeta)
        val btnVolver = findViewById<ImageButton>(R.id.botonVolverTj1)
        val btnPregunta = findViewById<ImageButton>(R.id.btnImgPreguntaTj)
        val btnConfig = findViewById<ImageButton>(R.id.btnImgConfigTj)

        btnVolver.setOnClickListener {
            finish()
        }
        btnMovTj.setOnClickListener {
            val intent = Intent(this, tarjeta_movimientos::class.java)
            startActivity(intent)
        }
        btnCongTj.setOnClickListener {
            val intent = Intent(this, congelar_tarjeta::class.java)
            startActivity(intent)
        }


        }
    }