package com.example.noqui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import android.view.View

class MainActivity : AppCompatActivity() {

    private var dinero_disponible: Int = 100000
    private var dinero_caja_fuerte: Int = 0
    private var dinero_total: Int = dinero_disponible + dinero_caja_fuerte

    private var dinero_visible: Boolean = true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAzulnv = findViewById<ImageButton>(R.id.btnAzulnv) //este es el boton azul de la barra de navegación
        val btnAzulba = findViewById<ImageButton>(R.id.btnAzul) //este es el boton azul de la activity del boton azul
        val btnFondoba = findViewById<ImageButton>(R.id.btnFondo)
        val azul = findViewById<View>(R.id.azul)

        val tv_dinero_disponible = findViewById<TextView>(R.id.textview_dinero_disponible)
        val tv_dinero_total = findViewById<TextView>(R.id.textview_dinero_total)
        val btn_ojo_dinero = findViewById<ImageButton>(R.id.button_ojo_dinero)
        val btn_movimientos = findViewById<ImageButton>(R.id.btnMovimientos)

        btnFondoba.setOnClickListener {
            azul.visibility = View.GONE
        }
        btnAzulnv.setOnClickListener {
            azul.visibility = View.VISIBLE
        }
        btnAzulba.setOnClickListener {
            azul.visibility = View.GONE
        }

        btn_movimientos.setOnClickListener {
            val intent = Intent(this, Movimientos::class.java)
            startActivity(intent)
            finish()
        }
        val btn_caja_fuerte = findViewById<MaterialButton>(R.id.btnCajaFuerte)
        btn_caja_fuerte.setOnClickListener {
            val intent = Intent(this, caja_fuerte::class.java)
            startActivity(intent)
            finish()
        }

        tv_dinero_disponible.text = "$".plus(dinero_disponible.toString())
        tv_dinero_total.text = "$".plus(dinero_total.toString())

        btn_movimientos.setOnClickListener {
            val intent = Intent(this, Movimientos::class.java)
            startActivity(intent)
            finish()
        }

        btn_ojo_dinero.setOnClickListener {
            if(dinero_visible){
                tv_dinero_disponible.text="*****"
                tv_dinero_total.text="*****"
                dinero_visible=false
            }else{
                tv_dinero_disponible.text="$".plus(dinero_disponible.toString())
                tv_dinero_total.text="$".plus(dinero_total.toString())
                dinero_visible=true
            }


        }






    }
}