package com.example.noqui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Tu_Informacion : AppCompatActivity() {

    private lateinit var btnVolverTuInfo: ImageButton
    private lateinit var btnCambiarFoto: ImageButton
    private lateinit var btnActualizarDatos: Button

    private lateinit var etNombre: EditText
    private lateinit var btnEditNombre: ImageButton

    private lateinit var etTelefono: EditText
    private lateinit var btnEditTelefono: ImageButton

    private lateinit var etCorreo: EditText
    private lateinit var btnEditCorreo: ImageButton

    // Claves para el Intent de retorno
    companion object {
        const val EXTRA_NOMBRE = "EXTRA_NOMBRE"
        const val EXTRA_TELEFONO = "EXTRA_TELEFONO"
        const val EXTRA_CORREO = "EXTRA_CORREO"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tu_informacion)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupFieldStates(false) // Deshabilita los campos al inicio
        loadInitialData() // Carga datos recibidos
        setupClickListeners()
    }

    private fun initializeViews() {
        // Botones y campos
        btnVolverTuInfo = findViewById(R.id.btnVolver_tuinfo)
        btnCambiarFoto = findViewById(R.id.btncambiarfoto)
        btnActualizarDatos = findViewById(R.id.btnActualizarDatos)

        etNombre = findViewById(R.id.etNombreperfil)
        btnEditNombre = findViewById(R.id.btnEditNombre)

        etTelefono = findViewById(R.id.etTelefonoperfil)
        btnEditTelefono = findViewById(R.id.btnEditTelefono)

        etCorreo = findViewById(R.id.etCorreo)
        btnEditCorreo = findViewById(R.id.btnEditCorreo)
    }

    /** Carga los datos recibidos desde la actividad anterior. */
    private fun loadInitialData() {
        val nombreActual = intent.getStringExtra(EXTRA_NOMBRE) ?: "Nombre no disponible"
        val telefonoActual = intent.getStringExtra(EXTRA_TELEFONO) ?: ""
        val correoActual = intent.getStringExtra(EXTRA_CORREO) ?: "Correo no disponible"

        etNombre.setText(nombreActual)
        etTelefono.setText(telefonoActual)
        etCorreo.setText(correoActual)
    }

    /** Configura el estado inicial de habilitación de los campos de texto. */
    private fun setupFieldStates(isEnabled: Boolean) {
        etNombre.isEnabled = isEnabled
        etTelefono.isEnabled = isEnabled
        etCorreo.isEnabled = isEnabled
    }

    private fun setupClickListeners() {
        btnVolverTuInfo.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        // Botones de Edición Individual
        btnEditNombre.setOnClickListener {
            toggleEditField(etNombre, "Escribe tu nombre completo")
        }
        btnEditTelefono.setOnClickListener {
            toggleEditField(etTelefono, "Ingresa tu número de teléfono")
        }
        btnEditCorreo.setOnClickListener {
            toggleEditField(etCorreo, "Ingresa tu correo electrónico")
        }

        // Botón Actualizar Datos
        btnActualizarDatos.setOnClickListener {
            val nuevoNombre = etNombre.text.toString().trim()
            val nuevoTelefono = etTelefono.text.toString().trim()
            val nuevoCorreo = etCorreo.text.toString().trim()

            if (nuevoNombre.isBlank()) {
                Toast.makeText(this, "El nombre no puede estar vacío.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Prepara y envía los resultados a la actividad de origen (perfil_Inicio)
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_NOMBRE, nuevoNombre)
            resultIntent.putExtra(EXTRA_TELEFONO, nuevoTelefono)
            resultIntent.putExtra(EXTRA_CORREO, nuevoCorreo)

            setResult(Activity.RESULT_OK, resultIntent)
            Toast.makeText(this, "Datos actualizados.", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    /** Alterna el estado de edición de un campo EditText, usando el valor anterior como hint. */
    private fun toggleEditField(editText: EditText, hintText: String) {
        val newState = !editText.isEnabled
        editText.isEnabled = newState

        if (newState) {
            // Habilita edición: mueve el texto actual al hint y limpia el campo.
            val currentText = editText.text.toString()
            editText.setText("")
            editText.hint = currentText.ifBlank { hintText }
            editText.requestFocus()
            Toast.makeText(this, "Edición habilitada", Toast.LENGTH_SHORT).show()
        } else {
            // Deshabilita edición: si está vacío, restaura el valor del hint.
            if (editText.text.isBlank() && editText.hint != null) {
                editText.setText(editText.hint)
            }
            editText.hint = null // Limpia el hint
            Toast.makeText(this, "Edición deshabilitada", Toast.LENGTH_SHORT).show()
        }
    }
}
