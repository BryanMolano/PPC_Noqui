package com.example.noqui

import android.app.Activity
import android.content.Intent
import android.net.Uri // Importar Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView // Importar ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts // Importar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Context
import android.util.Log

class Tu_Informacion : AppCompatActivity() {

    private lateinit var btnVolverTuInfo: ImageButton
    private lateinit var btnCambiarFoto: ImageButton
    private lateinit var btnActualizarDatos: Button
    private lateinit var imgPerfil: ImageView // Variable para la imagen de perfil

    private lateinit var etNombre: EditText
    private lateinit var btnEditNombre: ImageButton

    private lateinit var etTelefono: EditText
    private lateinit var btnEditTelefono: ImageButton

    private lateinit var etCorreo: EditText
    private lateinit var btnEditCorreo: ImageButton

    // Variable para almacenar la URI de la imagen seleccionada
    private var selectedImageUri: Uri? = null

    // Launcher para obtener una imagen de la galería
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // **INICIO DE CORRECCIÓN CRÍTICA**
            // Tomar el permiso de URI persistente. Esto es clave para que la app pueda
            // acceder a la imagen seleccionada después de que esta actividad se destruya
            // y se vuelva a abrir (incluso desde el MainActivity).
            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            try {
                contentResolver.takePersistableUriPermission(it, flags)
                selectedImageUri = it
                imgPerfil.setImageURI(it) // Mostrar la imagen seleccionada como vista previa
            } catch (e: SecurityException) {
                // Manejar error si no se puede tomar el permiso persistente
                Log.e("Tu_Informacion", "Error al tomar permiso persistente de URI: ${e.message}")
                Toast.makeText(this, "No se pudo asegurar el permiso para la imagen. Intenta otra vez.", Toast.LENGTH_LONG).show()
            }
            // **FIN DE CORRECCIÓN CRÍTICA**
        }
    }

    companion object {
        const val EXTRA_NOMBRE = "EXTRA_NOMBRE"
        const val EXTRA_TELEFONO = "EXTRA_TELEFONO"
        const val EXTRA_CORREO = "EXTRA_CORREO"
        const val EXTRA_URI_FOTO = "EXTRA_URI_FOTO" // Nueva clave para la foto
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tu_informacion)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        setupFieldStates(false)
        loadInitialData()
        setupClickListeners()
    }

    private fun initializeViews() {
        btnVolverTuInfo = findViewById(R.id.btnVolver_tuinfo)
        btnCambiarFoto = findViewById(R.id.btncambiarfoto)
        btnActualizarDatos = findViewById(R.id.btnActualizarDatos)
        imgPerfil = findViewById(R.id.imgPerfil) // Inicializar ImageView

        etNombre = findViewById(R.id.etNombreperfil)
        btnEditNombre = findViewById(R.id.btnEditNombre)

        etTelefono = findViewById(R.id.etTelefonoperfil)
        btnEditTelefono = findViewById(R.id.btnEditTelefono)

        etCorreo = findViewById(R.id.etCorreo)
        btnEditCorreo = findViewById(R.id.btnEditCorreo)
    }

    private fun loadInitialData() {
        val nombreActual = intent.getStringExtra(EXTRA_NOMBRE) ?: "Nombre no disponible"
        val telefonoActual = intent.getStringExtra(EXTRA_TELEFONO) ?: ""
        val correoActual = intent.getStringExtra(EXTRA_CORREO) ?: "Correo no disponible"
        val fotoUriString = intent.getStringExtra(EXTRA_URI_FOTO)

        etNombre.setText(nombreActual)
        etTelefono.setText(telefonoActual)
        etCorreo.setText(correoActual)

        // Si se recibe una URI de la pantalla anterior, se carga la imagen actual
        fotoUriString?.let {
            val imageUri = Uri.parse(it)
            imgPerfil.setImageURI(imageUri)
            selectedImageUri = imageUri
        }
    }

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

        // Al hacer clic en el botón de cambiar foto, se abre la galería
        btnCambiarFoto.setOnClickListener {
            pickImageLauncher.launch("image/*") // Lanza el selector de imágenes
        }

        btnEditNombre.setOnClickListener { toggleEditField(etNombre, "Escribe tu nombre completo") }
        btnEditTelefono.setOnClickListener { toggleEditField(etTelefono, "Ingresa tu número de teléfono") }
        btnEditCorreo.setOnClickListener { toggleEditField(etCorreo, "Ingresa tu correo electrónico") }

        btnActualizarDatos.setOnClickListener {
            val nuevoNombre = etNombre.text.toString().trim()
            val nuevoTelefono = etTelefono.text.toString().trim()
            val nuevoCorreo = etCorreo.text.toString().trim()

            if (nuevoNombre.isBlank()) {
                Toast.makeText(this, "El nombre no puede estar vacío.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_NOMBRE, nuevoNombre)
            resultIntent.putExtra(EXTRA_TELEFONO, nuevoTelefono)
            resultIntent.putExtra(EXTRA_CORREO, nuevoCorreo)

            // Añadir la URI de la nueva imagen al intent de resultado
            selectedImageUri?.let {
                resultIntent.putExtra(EXTRA_URI_FOTO, it.toString())
            }

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
