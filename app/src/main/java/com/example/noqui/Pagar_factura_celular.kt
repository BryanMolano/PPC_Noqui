package com.example.noqui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageButton
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


class Pagar_factura_celular : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pagar_factura_celular)

        val btn_volver = findViewById<ImageButton>(R.id.btn_volver)
        val btn_continuar = findViewById<ImageButton>(R.id.btn_continuar)
        val edit_dinero = findViewById<EditText>(R.id.edit_dinero)
        val edit_numero = findViewById<EditText>(R.id.edit_numero)
        val txtview_factura_celular = findViewById<TextView>(R.id.txtview_factura_celular)

        var dineroDisponible = intent.getIntExtra("dinero_disponible",0)
        val servicio = intent.getStringExtra("servicio" )?:"Movistar"

        //txtview_factura_celular.text = "Pagar Factura de celular $servicio"

        btn_volver.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        btn_continuar.setOnClickListener {

            if (edit_dinero.text.toString().isEmpty() || edit_numero.text.toString().isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            val monto = edit_dinero.text.toString().toIntOrNull()?:0
            if(monto<=0){
                Toast.makeText(this, "Monto Invalido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(dineroDisponible>=monto) {
                val movimiento = Movimiento(servicio, -monto) // este es la variable del registro

                val nuevo_dinero = dineroDisponible - monto
                val resultIntent = Intent()
                resultIntent.putExtra("nuevo_dinero", nuevo_dinero)

                resultIntent.putExtra("movimiento_servicio",movimiento.servicio)
                resultIntent.putExtra("movimiento_monto",movimiento.monto)

                setResult(Activity.RESULT_OK, resultIntent)
                Toast.makeText(this, "Pago Completo", Toast.LENGTH_SHORT).show()
                finish()
                return@setOnClickListener
            }else{
                    Toast.makeText(this, "Monto Invalido", Toast.LENGTH_SHORT).show()

                }
        }

        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}