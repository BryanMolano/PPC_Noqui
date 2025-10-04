package com.example.noqui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView

class perfil_Inicio : AppCompatActivity() {

    // Variables de estado para persistencia en memoria y constantes de retorno.
    companion object {
        const val EXTRA_NOMBRE_RETORNADO = "EXTRA_NOMBRE_RETORNADO"
        // Clave para enviar la contraseña a Seguridad
        const val EXTRA_PASSWORD = "EXTRA_PASSWORD"

        var currentUserName: String = "Nombre de Usuario"
        var currentUserPhone: String = "Teléfono (opcional)"
        var currentUserEmail: String = "Correo (opcional)"
        // **Nueva variable para almacenar la contraseña del Login**
        var currentUserPassword: String = "12345" // Contraseña temporal por defecto/ejemplo

        /** Obtiene solo el primer nombre de la cadena completa. */
        fun getFirstName(fullName: String): String {
            return fullName.split(" ").firstOrNull() ?: fullName
        }
    }

    private lateinit var btnVolverperfil: ImageButton
    private lateinit var btnInfoPerfil: MaterialCardView
    private lateinit var btnSeguridad: MaterialCardView
    private lateinit var imgPerfil: ImageView

    private lateinit var tvNombreperfil: TextView
    private lateinit var tvTelefonomostrar: TextView
    private lateinit var tvCorreomostrar: TextView

    private var dataUpdatedThisSession: Boolean = false // Bandera de actualización

    // Registro del Launcher para Tu_Informacion
    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data

            // Extraer y actualizar los datos persistentes
            val nuevoNombre = data?.getStringExtra(Tu_Informacion.EXTRA_NOMBRE)
            val nuevoTelefono = data?.getStringExtra(Tu_Informacion.EXTRA_TELEFONO)
            val nuevoCorreo = data?.getStringExtra(Tu_Informacion.EXTRA_CORREO)

            if (nuevoNombre != null) {
                currentUserName = nuevoNombre
                dataUpdatedThisSession = true
            }
            if (nuevoTelefono != null) {
                currentUserPhone = nuevoTelefono
            }
            if (nuevoCorreo != null) {
                currentUserEmail = nuevoCorreo
            }

            updateUI() // Reflejar el nuevo estado en la UI
            Toast.makeText(this, "Perfil actualizado con éxito.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil_inicio)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        updateUI() // Carga los valores persistentes en la UI
        setupClickListeners()
    }

    private fun initializeViews() {
        btnVolverperfil = findViewById(R.id.btnVolverperfil)
        btnInfoPerfil = findViewById(R.id.btnInfoPerfil)
        btnSeguridad = findViewById(R.id.btnSeguridad)
        imgPerfil = findViewById(R.id.imgPerfil)

        tvNombreperfil = findViewById(R.id.tvNombreperfil)
        tvTelefonomostrar = findViewById(R.id.tvTelefonoperfil)
        // tvCorreomostrar = findViewById(R.id.tvCorreomostrar)
    }

    /** Asigna las variables de estado persistentes a los TextViews. */
    private fun updateUI() {
        tvNombreperfil.text = currentUserName
        tvTelefonomostrar.text = currentUserPhone
        // tvCorreomostrar.text = currentUserEmail
    }

    private fun setupClickListeners() {
        // Botón Volver (Devuelve el nombre actualizado a MainActivity si hubo cambios)
        btnVolverperfil.setOnClickListener {
            if (dataUpdatedThisSession) {
                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_NOMBRE_RETORNADO, getFirstName(currentUserName))
                setResult(Activity.RESULT_OK, resultIntent)
            } else {
                setResult(Activity.RESULT_CANCELED)
            }
            finish()
        }

        // Inicia Tu_Informacion
        btnInfoPerfil.setOnClickListener {
            val intent = Intent(this, Tu_Informacion::class.java)

            intent.putExtra(Tu_Informacion.EXTRA_NOMBRE, currentUserName)
            intent.putExtra(Tu_Informacion.EXTRA_TELEFONO, currentUserPhone)
            intent.putExtra(Tu_Informacion.EXTRA_CORREO, currentUserEmail)

            activityResultLauncher.launch(intent)
        }

        // Inicia Seguridad, enviando la contraseña actual.
        btnSeguridad.setOnClickListener {
            val intent = Intent(this, Seguridad::class.java)
            // Se envía la contraseña actual para su verificación
            intent.putExtra(EXTRA_PASSWORD, currentUserPassword)
            startActivity(intent)
        }
    }
}
