package com.example.noqui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.app.Activity
class Movimientos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movimientos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAzulnv = findViewById<ImageButton>(R.id.btnAzulnv) //este es el boton azul de la barra de navegación
        val btnAzulba = findViewById<ImageButton>(R.id.btnAzul) //este es el boton azul de la activity del boton azul
        val btnFondoba = findViewById<ImageButton>(R.id.btnFondo)
        val azul = findViewById<View>(R.id.azul)

        val btn_main = findViewById<ImageButton>(R.id.btnInicio)


        val listaMovimientos = intent.getSerializableExtra("lista_movimientos") as? ArrayList<Movimiento> ?: arrayListOf()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerMovimientos)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val adapter = MovimientoAdapter(listaMovimientos)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()



        btnFondoba.setOnClickListener {
            azul.visibility = View.GONE
        }
        btnAzulnv.setOnClickListener {
            azul.visibility = View.VISIBLE
        }
        btnAzulba.setOnClickListener {
            azul.visibility = View.GONE
        }

        btn_main.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}