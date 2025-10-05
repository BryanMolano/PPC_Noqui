package com.example.noqui

import android.app.Activity
import android.content.Intent
import android.net.Uri // Importar Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView

class perfil_Inicio : AppCompatActivity() {

    companion object {
        const val EXTRA_NOMBRE_RETORNADO = "EXTRA_NOMBRE_RETORNADO"
        const val EXTRA_PASSWORD = "EXTRA_PASSWORD"

        var currentUserName: String = "Nombre de Usuario"
        var currentUserPhone: String = ""
        var currentUserEmail: String = ""
        var currentUserPassword: String = "12345"
        var currentUserImageUri: String? = null // Variable para la URI de la foto

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

    private var dataUpdatedThisSession: Boolean = false

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data

            val nuevoNombre = data?.getStringExtra(Tu_Informacion.EXTRA_NOMBRE)
            val nuevoTelefono = data?.getStringExtra(Tu_Informacion.EXTRA_TELEFONO)
            val nuevoCorreo = data?.getStringExtra(Tu_Informacion.EXTRA_CORREO)
            val nuevaFotoUri = data?.getStringExtra(Tu_Informacion.EXTRA_URI_FOTO) // Recibir la URI

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
            if (nuevaFotoUri != null) { // Si se recibió una nueva foto
                currentUserImageUri = nuevaFotoUri
                dataUpdatedThisSession = true
            }

            updateUI()
            Toast.makeText(this, "Perfil actualizado con éxito.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_inicio)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        updateUI()
        setupClickListeners()
    }

    private fun initializeViews() {
        btnVolverperfil = findViewById(R.id.btnVolverperfil)
        btnInfoPerfil = findViewById(R.id.btnInfoPerfil)
        btnSeguridad = findViewById(R.id.btnSeguridad)
        imgPerfil = findViewById(R.id.imgPerfil)

        tvNombreperfil = findViewById(R.id.tvNombreperfil)
        tvTelefonomostrar = findViewById(R.id.tvTelefonoperfil)
    }

    private fun updateUI() {
        tvNombreperfil.text = currentUserName
        tvTelefonomostrar.text = currentUserPhone

        // Actualizar la imagen de perfil si hay una URI guardada
        currentUserImageUri?.let {
            imgPerfil.setImageURI(Uri.parse(it))
        }
    }

    private fun setupClickListeners() {
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

        btnInfoPerfil.setOnClickListener {
            val intent = Intent(this, Tu_Informacion::class.java)

            intent.putExtra(Tu_Informacion.EXTRA_NOMBRE, currentUserName)
            intent.putExtra(Tu_Informacion.EXTRA_TELEFONO, currentUserPhone)
            intent.putExtra(Tu_Informacion.EXTRA_CORREO, currentUserEmail)
            // Enviar la URI de la foto actual a la pantalla de edición
            currentUserImageUri?.let {
                intent.putExtra(Tu_Informacion.EXTRA_URI_FOTO, it)
            }

            activityResultLauncher.launch(intent)
        }

        btnSeguridad.setOnClickListener {
            val intent = Intent(this, Seguridad::class.java)
            intent.putExtra(EXTRA_PASSWORD, currentUserPassword)
            startActivity(intent)
        }
    }
}