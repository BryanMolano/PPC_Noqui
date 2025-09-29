package com.example.noqui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class Servicios : AppCompatActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_servicios)

        val dinero_disponible_de_main=intent.getIntExtra("dinero_disponible",0)

        val btn_regresar = findViewById<ImageButton>(R.id.btn_regresar)
        val btn_celular_movistar = findViewById<ImageButton>(R.id.btn_celular_movistar)
        val btn_celular_claro = findViewById<ImageButton>(R.id.btn_celular_claro)
        val btn_celular_wom = findViewById<ImageButton>(R.id.btn_celular_wom)
        val btn_internet_claro = findViewById<ImageButton>(R.id.btn_internet_claro)
        val btn_internet_movistar = findViewById<ImageButton>(R.id.btn_internet_movistar)
        val btn_internet_wom = findViewById<ImageButton>(R.id.btn_internet_wom)
        val btn_servicios_1 = findViewById<ImageButton>(R.id.btn_servicios_1)
        val btn_servicios_2 = findViewById<ImageButton>(R.id.btn_servicios_2)
        val btn_servicios_3 = findViewById<ImageButton>(R.id.btn_servicios_3)
        val btn_servicios_4 = findViewById<ImageButton>(R.id.btn_servicios_4)

        btn_regresar.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }



        val pagar_launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data = result.data
                if(data!=null){
                    val nuevo_dinero=data.getIntExtra("nuevo_dinero", dinero_disponible_de_main)

                    val resultIntent= Intent()
                    resultIntent.putExtra("nuevo_dinero", nuevo_dinero)
                    setResult(Activity.RESULT_OK, resultIntent)
                    //finish()
                }
            }
        }

        btn_celular_movistar.setOnClickListener {
            abrirPagoCelular("movistar", dinero_disponible_de_main,pagar_launcher)

        }
        btn_celular_claro.setOnClickListener {
            abrirPagoCelular("claro", dinero_disponible_de_main, pagar_launcher)

        }
        btn_celular_wom.setOnClickListener {
            abrirPagoCelular("wom", dinero_disponible_de_main, pagar_launcher)

        }








        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun abrirPagoCelular(servicio: String, dinero_disponible: Int, pagarLauncher: androidx.activity.result.ActivityResultLauncher<Intent>){
        val intent = Intent(this, Pagar_factura_celular::class.java)
        intent.putExtra("dinero_disponible", dinero_disponible)
        intent.putExtra("servicio", servicio)
        pagarLauncher.launch(intent)
    }




}