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
    private var dinero_disponible: Int = 5000

    val celular_launcher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        if(result.resultCode== Activity.RESULT_OK){
            val data=result.data
            if(data!=null){
                dinero_disponible=data.getIntExtra("nuevo_dinero",dinero_disponible)
                Toast.makeText(this, "Dinero actualizado a: $dinero_disponible", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_servicios)

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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_celular_movistar.setOnClickListener {
            abrirPagoCelular("movistar", dinero_disponible)
        }
        btn_celular_claro.setOnClickListener {
            abrirPagoCelular("claro", dinero_disponible)

        }
        btn_celular_wom.setOnClickListener {
            abrirPagoCelular("wom", dinero_disponible)

        }








        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun abrirPagoCelular(servicio: String, dinero_disponible: Int){
        val intent = Intent(this, Pagar_factura_celular::class.java)
        intent.putExtra("dinero_dispnible", dinero_disponible)
        intent.putExtra("servicio", servicio)
        celular_launcher.launch(intent)
    }




}