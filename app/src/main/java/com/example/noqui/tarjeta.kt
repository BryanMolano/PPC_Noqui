package com.example.noqui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.content.Intent
import android.widget.ImageButton
import android.widget.ImageView
import com.google.android.material.button.MaterialButton


class tarjeta : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tarjeta)

        val numTarj = "1111222233334444"
        val expiraTj = "11/28"
        val codigoTj = "123"
        val nomUsuario = "Nombre Apellido"

        val numTarjeta = findViewById<TextView>(R.id.numTarjeta)
        val expiraTarjeta = findViewById<TextView>(R.id.expTj)
        val codTarjeta = findViewById<TextView>(R.id.codTj)
        val usuario = findViewById<TextView>(R.id.usuarioTj)
        numTarjeta.text = numTarj
        expiraTarjeta.text = expiraTj
        codTarjeta.text = codigoTj
        usuario.text = nomUsuario

        val btnMovTj = findViewById<ImageButton>(R.id.btnImgMovTarjeta)
        val btnCongTj = findViewById<ImageButton>(R.id.btnImgCongTarjeta)
        val btnVolver = findViewById<ImageButton>(R.id.botonVolverTj1)
        val btnPregunta = findViewById<ImageButton>(R.id.btnImgPreguntaTj)
        val btnConfig = findViewById<ImageButton>(R.id.btnImgConfigTj)
        val btnPromos = findViewById<MaterialButton>(R.id.btnPromos)


        btnVolver.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        btnMovTj.setOnClickListener {
            val intent = Intent(this, tarjeta_movimientos::class.java)
            startActivity(intent)
        }
        btnCongTj.setOnClickListener {
            val intent = Intent(this, congelar_tarjeta::class.java)
            startActivity(intent)
        }
        btnPregunta.setOnClickListener {
            val intent = Intent(this, tarjeta_ayuda::class.java)
            startActivity(intent)
        }
        btnConfig.setOnClickListener {
            val intent = Intent(this, tarjeta_configuracion::class.java)
            startActivity(intent)
        }
        btnPromos.setOnClickListener {
            val intent = Intent(this, tarjeta_promos::class.java)
            startActivity(intent)
        }

        //Estado de Tarjeta Congelada
        val tarjetaCongelada = findViewById<ImageView>(R.id.tarjetaCongelada)

        val sharedPref = getSharedPreferences("TarjetaPrefs", Context.MODE_PRIVATE)
        val isDigitalOn = sharedPref.getBoolean("switchDigitalTjState", false)

        if (isDigitalOn) {
            tarjetaCongelada.visibility = ImageView.VISIBLE
        } else {
            tarjetaCongelada.visibility = ImageView.GONE
        }
        actualizarEstadoTarjeta()
    }

    override fun onResume() {
        super.onResume()
        actualizarEstadoTarjeta()
    }

    //Refresca la visibilidad del ImageView según el switch guardado
    private fun actualizarEstadoTarjeta() {
        val tarjetaCongelada = findViewById<ImageView>(R.id.tarjetaCongelada)
        val sharedPref = getSharedPreferences("TarjetaPrefs", Context.MODE_PRIVATE)
        val isDigitalOn = sharedPref.getBoolean("switchDigitalTjState", false)

        tarjetaCongelada.visibility = if (isDigitalOn) ImageView.VISIBLE
        else ImageView.GONE
    }
}