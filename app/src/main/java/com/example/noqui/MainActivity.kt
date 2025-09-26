package com.example.noqui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.widget.ImageButton
import android.widget.TextView

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
        val tv_dinero_disponible = findViewById<TextView>(R.id.textview_dinero_disponible)
        val tv_dinero_total = findViewById<TextView>(R.id.textview_dinero_total)
        val btn_ojo_dinero = findViewById<ImageButton>(R.id.button_ojo_dinero)

        tv_dinero_disponible.text = "$".plus(dinero_disponible.toString())
        tv_dinero_total.text = "$".plus(dinero_total.toString())

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