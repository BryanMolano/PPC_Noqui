package com.example.noqui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class tarjeta_ayuda : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tarjeta_ayuda)

        val btnVolver = findViewById<ImageButton>(R.id.botonVolverAyudaTj)

        btnVolver.setOnClickListener {
            finish()
        }

        val btnchat = findViewById<ImageButton>(R.id.btnImgChat)
        btnchat.setOnClickListener {
            val urlchat = "https://www.whatsapp.com/?lang=es"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlchat))
            startActivity(intent)
        }
        val btnevaluar = findViewById<ImageButton>(R.id.btnImgEvalua)
        btnevaluar.setOnClickListener {
            val urlevalua = "https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_CO&pli=1"
            val intentev = Intent(Intent.ACTION_VIEW, Uri.parse(urlevalua))
            startActivity(intentev)
        }
        val soporte = findViewById<TextView>(R.id.txlinkSoporte)
        soporte.setOnClickListener {
            val urlsoporte = "https://ayuda.nequi.com.co/hc/es"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlsoporte))
            startActivity(intent)
        }
    }
}