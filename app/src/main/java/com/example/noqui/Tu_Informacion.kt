package com.example.noqui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.EditText
import android.widget.ImageButton

class Tu_Informacion : AppCompatActivity() {
    private var numeroTelefonoUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tu_informacion)

        val btnVolver_tuinfo = findViewById<ImageButton>(R.id.btnVolver_tuinfo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnVolver_tuinfo.setOnClickListener {
            val intent = Intent(this, perfil_Inicio::class.java)
            startActivity(intent)
            finish()
        }
    }
}