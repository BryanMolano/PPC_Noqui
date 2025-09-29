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
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var dinero_disponible: Int = 1000
    private var dinero_caja_fuerte: Int = 0
    private var dinero_total: Int = dinero_disponible + dinero_caja_fuerte

    private var dinero_visible: Boolean = true

    private val cajaFuerteLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    dinero_disponible = data.getIntExtra("dinero_disponible", dinero_disponible)
                    dinero_caja_fuerte = data.getIntExtra("dinero_caja_fuerte", dinero_caja_fuerte)
                    dinero_total = dinero_disponible + dinero_caja_fuerte

                    findViewById<TextView>(R.id.textview_dinero_disponible).text = "$$dinero_disponible"
                    findViewById<TextView>(R.id.textview_dinero_total).text = "$$dinero_total"
                }
            }
        }
    val servicios_launcher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        if(result.resultCode== Activity.RESULT_OK){
            val data=result.data
            if(data!=null){
                dinero_disponible=data.getIntExtra("nuevo_dinero",dinero_disponible)
                dinero_total =dinero_disponible+dinero_caja_fuerte
                findViewById<TextView>(R.id.textview_dinero_disponible).text = "$$dinero_disponible"
                findViewById<TextView>(R.id.textview_dinero_total).text = "$$dinero_total"

                Toast.makeText(this, "Dinero actualizado a: $dinero_disponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAzulnv = findViewById<ImageButton>(R.id.btnAzulnv)
        val btnAzulba = findViewById<ImageButton>(R.id.btnAzul)
        val btnFondoba = findViewById<ImageButton>(R.id.btnFondo)
        val azul = findViewById<View>(R.id.azul)

        val tv_dinero_disponible = findViewById<TextView>(R.id.textview_dinero_disponible)
        val tv_dinero_total = findViewById<TextView>(R.id.textview_dinero_total)
        val btn_ojo_dinero = findViewById<ImageButton>(R.id.button_ojo_dinero)
        val btn_movimientos = findViewById<ImageButton>(R.id.btnMovimientos)
        val btn_enviar = findViewById<MaterialButton>(R.id.btn_enviar)
        val btn_servicios= findViewById<ImageButton>(R.id.btn_servicios)
        val btn_tarjeta = findViewById<ImageButton>(R.id.btnTarjeta)
        val btn_envia = findViewById<ImageButton>(R.id.btnEnvia)

        btn_enviar.setOnClickListener {
            val intent = Intent(this, enviar::class.java)
            startActivity(intent)
        }
        btn_servicios.setOnClickListener {
            val intent = Intent(this, Servicios::class.java)
            intent.putExtra("dinero_disponible", dinero_disponible)
            servicios_launcher.launch(intent)
        }
        btn_tarjeta.setOnClickListener {
            val intent = Intent(this, tarjeta::class.java)
            startActivity(intent)
        }
        btn_envia.setOnClickListener {
            val intent = Intent(this, enviar::class.java)
            startActivity(intent)
        }



        tv_dinero_disponible.text = "$$dinero_disponible"
        tv_dinero_total.text = "$$dinero_total"

        btnFondoba.setOnClickListener { azul.visibility = View.GONE }
        btnAzulnv.setOnClickListener { azul.visibility = View.VISIBLE }
        btnAzulba.setOnClickListener { azul.visibility = View.GONE }

        btn_movimientos.setOnClickListener {
            val intent = Intent(this, Movimientos::class.java)
            startActivity(intent)
        }

        val btn_caja_fuerte = findViewById<MaterialButton>(R.id.btnCajaFuerte)

        val btn_caja_fuerte2 = findViewById<ImageButton>(R.id.btnCajaFuerte2)
        btn_caja_fuerte.setOnClickListener {
            val intent = Intent(this, caja_fuerte::class.java)
            intent.putExtra("dinero_disponible", dinero_disponible)
            intent.putExtra("dinero_caja_fuerte", dinero_caja_fuerte)
            intent.putExtra("dinero_total", dinero_total)
            cajaFuerteLauncher.launch(intent)
        }
        btn_caja_fuerte2.setOnClickListener {
            val intent = Intent(this, caja_fuerte::class.java)
            intent.putExtra("dinero_disponible", dinero_disponible)
            intent.putExtra("dinero_caja_fuerte", dinero_caja_fuerte)
            intent.putExtra("dinero_total", dinero_total)
            cajaFuerteLauncher.launch(intent)
        }

        btn_ojo_dinero.setOnClickListener {
            if (dinero_visible) {
                tv_dinero_disponible.text = "*****"
                tv_dinero_total.text = "*****"
                dinero_visible = false
            } else {
                tv_dinero_disponible.text = "$$dinero_disponible"
                tv_dinero_total.text = "$$dinero_total"
                dinero_visible = true
            }
        }

    }










}
