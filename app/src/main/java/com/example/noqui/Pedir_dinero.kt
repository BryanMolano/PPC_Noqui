package com.example.noqui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView // Importar TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat // Importar DecimalFormat
import java.util.Locale // Importar Locale

class Pedir_dinero : AppCompatActivity() {

    // Clave para comunicar a la Activity Notificacion qué pestaña debe mostrar
    companion object {
        const val MOSTRAR_EN_ESPERA_KEY = "MOSTRAR_EN_ESPERA"
    }

    // Formateador para mostrar $ con separadores de miles (ej: 1.000.000)
    private val saldoFormatter = DecimalFormat("###,###").apply {
        decimalFormatSymbols = java.text.DecimalFormatSymbols(Locale.getDefault()).apply {
            groupingSeparator = '.'
        }
    }

    // Vistas y Variables
    private lateinit var btnVolver: ImageButton
    private lateinit var btnSiguiente: ImageButton
    private lateinit var btnToggleSaldo: ImageButton
    private lateinit var layoutSaldo: LinearLayout
    private lateinit var edtNumero: EditText
    private lateinit var edtMonto: EditText
    private lateinit var edtMensaje: EditText

    // ** CAMBIO: Declarar los TextViews según los IDs de tu XML **
    private lateinit var tvDisponible: TextView
    private lateinit var tvTotal: TextView

    private var esSaldoVisible = false
    private val CODIGO_SELECCIONAR_CONTACTO = 1001
    private var saldoDisponible: Int = 0 // Variable para almacenar el saldo de MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedir_dinero)

        // Recibir el saldo de MainActivity
        saldoDisponible = intent.getIntExtra("dinero_disponible", 0)

        // Inicializar vistas
        btnVolver = findViewById(R.id.btnVolver_pide)
        btnSiguiente = findViewById(R.id.btnSiguepide)
        btnToggleSaldo = findViewById(R.id.btnToggleSaldo)
        layoutSaldo = findViewById(R.id.layoutSaldo)
        edtNumero = findViewById(R.id.edit_numeropide)
        edtMonto = findViewById(R.id.editPlatapide) // Inicializar el EditText del monto
        edtMensaje = findViewById(R.id.editMsgpide) // Inicializar el EditText del mensaje


        tvDisponible = findViewById(R.id.txtDisponible)
        tvTotal = findViewById(R.id.txtTotal)

        // Muestra el saldo recibido formateado
        // Se asume que el saldo disponible es el mismo que el saldo total para esta vista.
        val saldoFormateado = saldoFormatter.format(saldoDisponible)
        tvDisponible.text = "Disponible: $$saldoFormateado"
        tvTotal.text = "Total: $$saldoFormateado"


        // Evento volver
        btnVolver.setOnClickListener { finish() }

        // Evento continuar: llama a la función para guardar la solicitud
        btnSiguiente.setOnClickListener {
            crearSolicitudEnEspera()
        }

        btnToggleSaldo.setOnClickListener {
            esSaldoVisible = !esSaldoVisible

            val transicion = android.transition.AutoTransition()
            transicion.duration = 300
            android.transition.TransitionManager.beginDelayedTransition(
                findViewById(R.id.main), // Usamos el ID del ConstraintLayout principal
                transicion
            )

            layoutSaldo.visibility = if (esSaldoVisible) View.VISIBLE else View.GONE
            // Cambia el ícono del toggle si tienes los drawables ic_arrow_up y ic_arrow_down
            // btnToggleSaldo.setImageResource(if (esSaldoVisible) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)
        }

        // Evento para abrir contactos al tocar el ícono de drawableEnd
        edtNumero.setOnTouchListener { v, evento ->
            if (evento.action == MotionEvent.ACTION_UP) {
                val indiceDrawableDerecho = 2 // posición derecha
                val drawable = edtNumero.compoundDrawables[indiceDrawableDerecho]
                if (drawable != null && evento.rawX >= (edtNumero.right - drawable.bounds.width())) {
                    v.performClick()
                    abrirContactos()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    /**
     * Captura los datos del formulario, valida, y lanza la actividad Notificacion
     * indicando que muestre la pestaña "En Espera".
     */
    private fun crearSolicitudEnEspera() {
        val contacto = edtNumero.text.toString().trim()
        val montoStr = edtMonto.text.toString().trim()
        // El mensaje es opcional, se usa el trim() para guardar vacío si solo tiene espacios
        val mensaje = edtMensaje.text.toString().trim()

        if (contacto.isEmpty() || montoStr.isEmpty()) {
            Toast.makeText(this, "Debe ingresar contacto y monto.", Toast.LENGTH_SHORT).show()
            return
        }

        val monto = montoStr.toDoubleOrNull()
        if (monto == null || monto <= 0) {
            Toast.makeText(this, "Monto inválido.", Toast.LENGTH_SHORT).show()
            return
        }

        // 1. CREAR Y GUARDAR LA SOLICITUD EN LA LISTA 'solicitudesEnEspera'
        val nuevaPeticion = PeticionDinero(
            contacto = contacto,
            monto = monto,
            mensaje = mensaje
        )
        // **CORRECCIÓN: Se agrega la nueva solicitud al Singleton de datos.**
        AlmacenDatos.solicitudesEnEspera.add(0, nuevaPeticion) // Agregar al inicio para que aparezca primero

        Toast.makeText(this, "Solicitud a $contacto enviada.", Toast.LENGTH_LONG).show()


        // 2. Lanzar la Activity Notificacion con la bandera para mostrar "En Espera"
        val intent = Intent(this, Notificacion::class.java)
        intent.putExtra(MOSTRAR_EN_ESPERA_KEY, true)
        startActivity(intent)

        // 3. Cerrar la actividad actual (Pedir Dinero)
        finish()
    }


    private fun abrirContactos() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        startActivityForResult(intent, CODIGO_SELECCIONAR_CONTACTO)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODIGO_SELECCIONAR_CONTACTO && resultCode == Activity.RESULT_OK) {
            val uriContacto: Uri? = data?.data
            val proyeccion = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME)

            contentResolver.query(uriContacto!!, proyeccion, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val indiceNumero = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val indiceNombre = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME)

                    val numero = cursor.getString(indiceNumero)
                    // Intentamos obtener el nombre
                    val nombre = if (indiceNombre != -1) cursor.getString(indiceNombre) else numero

                    // autocompleta con el nombre/número
                    edtNumero.setText(nombre)
                }
            }
        }
    }
}
