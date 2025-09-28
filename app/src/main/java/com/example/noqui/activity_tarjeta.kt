package com.example.noqui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.content.Intent
import android.widget.ImageButton


class activity_tarjeta : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tarjeta)

        val numTarj = "1111222233334444"
        val expiraTj = "11/28"
        val codigoTj = "123"

        val numTarjeta = findViewById<TextView>(R.id.numTarjeta)
        val expiraTarjeta = findViewById<TextView>(R.id.expTj)
        val codTarjeta = findViewById<TextView>(R.id.codTj)
        numTarjeta.text = numTarj
        expiraTarjeta.text = expiraTj
        codTarjeta.text = codigoTj

        val btnMovTj = findViewById<ImageButton>(R.id.btnImgMovTarjeta)
        val btnCongTj = findViewById<ImageButton>(R.id.btnImgCongTarjeta)

        btnMovTj.setOnClickListener {
            val intent = Intent(this, activity_tarjeta_movimientos::class.java)
            startActivity(intent)
        }
        btnCongTj.setOnClickListener {
            val intent = Intent(this, activity_congelar_tarjeta::class.java)
            startActivity(intent)
        }


        }
    }