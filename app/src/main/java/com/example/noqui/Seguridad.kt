package com.example.noqui

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Seguridad : AppCompatActivity() {

    private lateinit var btnVolverSeguridad: ImageButton
    private lateinit var etClaveActual: EditText
    private lateinit var btnVerClaveActual: ImageButton
    private lateinit var etClaveNueva: EditText
    private lateinit var btnVerClaveNueva: ImageButton
    private lateinit var etClaveConfirmacion: EditText
    private lateinit var btnVerClaveConfirmacion: ImageButton
    private lateinit var btnCambiarContraseña: Button

    // Variable para almacenar la contraseña actual recibida
    private var actualPassword: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seguridad)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Obtener la contraseña actual de perfil_Inicio
        actualPassword = intent.getStringExtra(perfil_Inicio.EXTRA_PASSWORD) ?: perfil_Inicio.currentUserPassword

        initializeViews()
        setupListeners()
    }

    private fun initializeViews() {
        btnVolverSeguridad = findViewById(R.id.btnVolver_segu)
        etClaveActual = findViewById(R.id.etcontraseñaAct)
        btnVerClaveActual = findViewById(R.id.btncontraseñaAct)
        etClaveNueva = findViewById(R.id.etNuevaCont)
        btnVerClaveNueva = findViewById(R.id.btnNuevaCont)
        etClaveConfirmacion = findViewById(R.id.etConfirmcon)
        btnVerClaveConfirmacion = findViewById(R.id.btnConfirmcon)
        btnCambiarContraseña = findViewById(R.id.btnCambiarcontraseña)
    }

    private fun setupListeners() {
        btnVolverSeguridad.setOnClickListener {
            finish()
        }

        // Listeners para mostrar/ocultar las contraseñas
        btnVerClaveActual.setOnClickListener {
            togglePasswordVisibility(etClaveActual)
        }
        btnVerClaveNueva.setOnClickListener {
            togglePasswordVisibility(etClaveNueva)
        }
        btnVerClaveConfirmacion.setOnClickListener {
            togglePasswordVisibility(etClaveConfirmacion)
        }

        // Lógica de cambio de contraseña
        btnCambiarContraseña.setOnClickListener {
            cambiarContraseña()
        }
    }

    /** Alterna la visibilidad del texto en un campo de contraseña. */
    private fun togglePasswordVisibility(editText: EditText) {
        val currentMethod = editText.transformationMethod
        if (currentMethod == PasswordTransformationMethod.getInstance()) {
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        // Mantiene el cursor al final del texto
        editText.setSelection(editText.text.length)
    }

    /** Realiza la validación y el cambio de contraseña. */
    private fun cambiarContraseña() {
        val claveActualInput = etClaveActual.text.toString()
        val claveNuevaInput = etClaveNueva.text.toString()
        val claveConfirmacionInput = etClaveConfirmacion.text.toString()

        if (claveActualInput.isEmpty() || claveNuevaInput.isEmpty() || claveConfirmacionInput.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        // 1. Verificar la contraseña actual
        if (claveActualInput != actualPassword) {
            Toast.makeText(this, "La contraseña actual es incorrecta.", Toast.LENGTH_LONG).show()
            return
        }

        // 2. Verificar que la nueva contraseña y la confirmación coincidan
        if (claveNuevaInput != claveConfirmacionInput) {
            Toast.makeText(this, "La nueva contraseña y la confirmación no coinciden.", Toast.LENGTH_LONG).show()
            return
        }

        // 3. Opcional: Validar longitud mínima o complejidad (ejemplo: > 4 caracteres)
        if (claveNuevaInput.length < 5) {
            Toast.makeText(this, "La nueva contraseña debe tener al menos 5 caracteres.", Toast.LENGTH_LONG).show()
            return
        }

        // 4. Actualizar la variable de persistencia global
        perfil_Inicio.currentUserPassword = claveNuevaInput

        // 5. Actualizar la contraseña local para futuras validaciones
        actualPassword = claveNuevaInput

        Toast.makeText(this, "Contraseña cambiada exitosamente.", Toast.LENGTH_LONG).show()
        finish()
    }
}
