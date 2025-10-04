package com.example.noqui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editTelefono = findViewById<EditText>(R.id.editTelefono)
        val editClave = findViewById<EditText>(R.id.editClave)
        val botonIniciar = findViewById<ImageButton>(R.id.botonIniciar)
        val botonVerClave = findViewById<ImageButton>(R.id.botonVerClave)
        val botonVolver = findViewById<ImageButton>(R.id.botonVolver)
        var claveVisible = false
435345
        botonVolver.setOnClickListener {
            finishAffinity()
        }
        botonVerClave.setOnClickListener {
            claveVisible = !claveVisible

            if (claveVisible) {
                editClave.transformationMethod = HideReturnsTransformationMethod.getInstance()
                botonVerClave.contentDescription = "Ocultar contraseña"
            } else {
                editClave.transformationMethod = PasswordTransformationMethod.getInstance()
                botonVerClave.contentDescription = "Mostrar contraseña"
            }
            editClave.setSelection(editClave.text?.length ?: 0)
        }

        botonIniciar.setOnClickListener {
            val telefono = editTelefono.text.toString().trim()
            val clave = editClave.text.toString().trim()

            if (telefono.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Usuario Ingresado Correctamente", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("numero_telefono", telefono)
                startActivity(intent)
                finish()
            }
        }
    }
}
