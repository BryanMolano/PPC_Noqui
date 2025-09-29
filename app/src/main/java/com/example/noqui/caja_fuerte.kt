package com.example.noqui

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.widget.ImageButton
import android.app.Activity
import android.content.Intent
import android.widget.ImageView
import android.widget.Toast


class caja_fuerte : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_caja_fuerte)
        val intent = intent

        var dinero_disponible = intent.getIntExtra("dinero_disponible", 0)
        var dinero_caja_fuerte = intent.getIntExtra("dinero_caja_fuerte", 0)
        var dinero_total = intent.getIntExtra("dinero_total", 0)

        val tv_dinero_caja_fuerte = findViewById<TextView>(R.id.tv_dinero_caja_fuerte)
        val tv_dinero_a_meter_caja_fuerte = findViewById<TextView>(R.id.tv_dinero_a_meter_caja_fuerte)

        tv_dinero_caja_fuerte.text = "$".plus(dinero_caja_fuerte.toString())
        var dinero_a_meter: Int = 0
        tv_dinero_a_meter_caja_fuerte.text = "$".plus(dinero_a_meter.toString())

        val btn_sumar_caja_fuerte = findViewById<ImageButton>(R.id.btn_sumar_caja_fuerte)
        val btn_restar_caja_fuerte = findViewById<ImageButton>(R.id.btn_restar_caja_fuerte)

        val btn_aceptar_caja_fuerte = findViewById<ImageButton>(R.id.caja_fuerte_btn_aceptar)

        val btnAtras = findViewById<ImageView>(R.id.btnAtras)


        btnAtras.setOnClickListener {
            finish()
        }


        btn_restar_caja_fuerte.setOnClickListener{
            if( dinero_caja_fuerte+dinero_a_meter>0) {

                dinero_a_meter -= 1000
                tv_dinero_a_meter_caja_fuerte.text = "$".plus(dinero_a_meter.toString())
            }else{
                Toast.makeText(this, "Dinero Insuficiente", Toast.LENGTH_SHORT).show()
                tv_dinero_a_meter_caja_fuerte.text = "$".plus(dinero_a_meter.toString())

            }

        }
        btn_sumar_caja_fuerte.setOnClickListener{

            if(dinero_a_meter>=dinero_disponible){
                Toast.makeText(this, "Dinero Insuficiente", Toast.LENGTH_SHORT).show()
                tv_dinero_a_meter_caja_fuerte.text = "$".plus(dinero_a_meter.toString())
            }else{
                dinero_a_meter+=1000
                tv_dinero_a_meter_caja_fuerte.text = "$".plus(dinero_a_meter.toString())
            }

        }
        btn_aceptar_caja_fuerte.setOnClickListener {
            if (dinero_a_meter > 0) {
                dinero_disponible -= dinero_a_meter
                dinero_caja_fuerte += dinero_a_meter
            } else if (dinero_a_meter < 0) {
                dinero_disponible -= dinero_a_meter
                dinero_caja_fuerte += dinero_a_meter
            }

            val resultIntent = Intent()
            resultIntent.putExtra("dinero_disponible", dinero_disponible)
            resultIntent.putExtra("dinero_caja_fuerte", dinero_caja_fuerte)
            setResult(Activity.RESULT_OK, resultIntent)

            finish()
        }













        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}