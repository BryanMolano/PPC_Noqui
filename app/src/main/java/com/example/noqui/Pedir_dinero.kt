package com.example.noqui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

class Pedir_dinero : AppCompatActivity() {

    // Clave para comunicar a la Activity Notificacion qué pestaña debe mostrar
    companion object {
        const val MOSTRAR_EN_ESPERA_KEY = "MOSTRAR_EN_ESPERA"
        private const val CODIGO_SELECCIONAR_CONTACTO = 1001
        // Proyección concisa de columnas para la consulta de contactos
        private val PROYECCION_CONTACTOS = arrayOf(
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME
        )
    }

    // Formateador para mostrar el monto con formato de Peso Colombiano: $ 10.000 (sin decimales)
    private val formateadorMoneda: NumberFormat = DecimalFormat("¤ #,##0", DecimalFormatSymbols(Locale("es", "CO")).apply {
        groupingSeparator = '.' // Separador de miles: punto
        currencySymbol = "$" // Símbolo de moneda: $
    }).apply {
        maximumFractionDigits = 0
    }

    // Formateador para mostrar el saldo con separadores de miles (sin símbolo de moneda en el TextView)
    private val saldoFormatter = DecimalFormat("#,###", DecimalFormatSymbols(Locale("es", "CO")).apply {
        groupingSeparator = '.'
    })

    // Vistas y Variables (se mantiene lateinit para inicialización en onCreate)
    private lateinit var btnVolver: ImageButton
    private lateinit var btnSiguiente: ImageButton
    private lateinit var btnToggleSaldo: ImageButton
    private lateinit var layoutSaldo: LinearLayout
    private lateinit var edtNumero: EditText
    private lateinit var edtMonto: EditText // Campo de monto
    private lateinit var edtMensaje: EditText
    private lateinit var tvDisponible: TextView
    private lateinit var tvTotal: TextView

    private var esSaldoVisible = false
    private var saldoDisponible: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedir_dinero)

        // Recibir el saldo de MainActivity
        saldoDisponible = intent.getIntExtra("dinero_disponible", 0)

        // Inicialización concisa de vistas y configuración inicial usando 'with'
        with(this) {
            btnVolver = findViewById(R.id.btnVolver_pide)
            btnSiguiente = findViewById(R.id.btnSiguepide)
            btnToggleSaldo = findViewById(R.id.btnToggleSaldo)
            layoutSaldo = findViewById(R.id.layoutSaldo)
            edtNumero = findViewById(R.id.edit_numeropide)
            edtMonto = findViewById(R.id.editPlatapide)
            edtMensaje = findViewById(R.id.editMsgpide)
            tvDisponible = findViewById(R.id.txtDisponible)
            tvTotal = findViewById(R.id.txtTotal)
        }

        // Muestra el saldo recibido formateado
        val saldoFormateado = saldoFormatter.format(saldoDisponible)
        tvDisponible.text = "Disponible: $$saldoFormateado"
        tvTotal.text = "Total: $$saldoFormateado"

        // Aplicar el TextWatcher para el formateo en tiempo real
        edtMonto.addTextChangedListener(FormateadorMontoTexto(edtMonto, formateadorMoneda))

        // Evento volver
        btnVolver.setOnClickListener { finish() }

        // Evento continuar: llama a la función para guardar la solicitud
        btnSiguiente.setOnClickListener { crearSolicitudEnEspera() }

        // Toggle del saldo (ocultar/mostrar)
        btnToggleSaldo.setOnClickListener {
            esSaldoVisible = !esSaldoVisible

            // Uso de la clase Transition para una animación suave de la visibilidad
            val transicion = android.transition.AutoTransition()
            transicion.duration = 300
            android.transition.TransitionManager.beginDelayedTransition(
                findViewById(R.id.main), // Usamos el ID del ConstraintLayout principal
                transicion
            )
            layoutSaldo.visibility = if (esSaldoVisible) View.VISIBLE else View.GONE
        }

        // Evento para abrir contactos al tocar el ícono de drawableEnd
        edtNumero.setOnTouchListener { v, evento ->
            if (evento.action == MotionEvent.ACTION_UP) {
                val indiceDrawableDerecho = 2 // posición derecha
                val drawable = edtNumero.compoundDrawables[indiceDrawableDerecho]

                // Se simplifica la condición de toque en el drawable
                if (drawable != null && evento.rawX >= (edtNumero.right - drawable.bounds.width())) {
                    v.performClick()
                    abrirContactos()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    // --- CLASE PARA EL FORMATO AUTOMÁTICO DEL MONTO ---
    /**
     * Clase que formatea automáticamente el texto de un EditText a formato de moneda ($ 10.000).
     */
    private inner class FormateadorMontoTexto(
        private val campoMonto: EditText,
        private val formato: NumberFormat
    ) : TextWatcher {

        private var previeneCambio = false // Bandera para evitar recursividad

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(editable: Editable) {
            if (previeneCambio) return

            previeneCambio = true

            // Obtiene solo los dígitos del texto de forma concisa
            val textoLimpio = editable.toString().replace(Regex("[^\\d]"), "")

            if (textoLimpio.isEmpty()) {
                campoMonto.setText("")
            } else {
                // Convertir a Long y formatear en un solo bloque
                textoLimpio.toLongOrNull()?.let { montoEntero ->
                    val textoFormateado = formato.format(montoEntero)
                    campoMonto.setText(textoFormateado)
                    campoMonto.setSelection(textoFormateado.length) // Mueve el cursor al final
                } ?: campoMonto.setText("") // Si falla la conversión (ej: número demasiado grande), limpiamos
            }
            previeneCambio = false
        }
    }


    /**
     * Captura los datos del formulario, valida el monto, guarda la solicitud
     * y lanza la actividad Notificacion.
     */
    private fun crearSolicitudEnEspera() {
        val contacto = edtNumero.text.toString().trim()
        val montoStr = edtMonto.text.toString().trim()
        val mensaje = edtMensaje.text.toString().trim()

        // 1. VALIDACIÓN Y EXTRACCIÓN DEL MONTO LIMPIO
        val textoMontoLimpio = montoStr.replace(Regex("[^\\d]"), "")
        val monto = textoMontoLimpio.toDoubleOrNull()

        if (contacto.isEmpty() || montoStr.isEmpty()) {
            Toast.makeText(this, "Debe ingresar contacto y monto.", Toast.LENGTH_SHORT).show()
            return
        }

        // Validación de que el monto es numérico y positivo.
        if (monto == null || monto <= 0) {
            Toast.makeText(this, "Monto inválido. Debe ser un valor numérico mayor a cero.", Toast.LENGTH_LONG).show()
            return
        }

        // 2. CREAR Y GUARDAR LA SOLICITUD
        val nuevaPeticion = PeticionDinero(
            contacto = contacto,
            monto = monto,
            mensaje = mensaje
        )
        // Guardar la solicitud al inicio de la lista "En Espera"
        AlmacenDatos.solicitudesEnEspera.add(0, nuevaPeticion)

        Toast.makeText(this, "Solicitud a $contacto enviada.", Toast.LENGTH_LONG).show()


        // 3. Lanzar la Activity Notificacion
        val intent = Intent(this, Notificacion::class.java).apply {
            putExtra(MOSTRAR_EN_ESPERA_KEY, true)
        }
        startActivity(intent)

        // 4. Cerrar la actividad actual
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
            // Uso de 'let' para un manejo de nulos conciso y seguro del URI de contacto.
            data?.data?.let { uriContacto ->

                // Consulta al ContentResolver usando la constante y 'use' para cierre automático.
                contentResolver.query(uriContacto, PROYECCION_CONTACTOS, null, null, null)?.use { cursor ->

                    if (cursor.moveToFirst()) {
                        val indiceNumero = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val indiceNombre = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Identity.DISPLAY_NAME)

                        val numero = cursor.getString(indiceNumero)
                        // Lógica concisa: si el índice de nombre es válido, usa el nombre; de lo contrario, usa el número.
                        val nombre = if (indiceNombre != -1) cursor.getString(indiceNombre) else numero

                        // autocompleta con el nombre/número
                        edtNumero.setText(nombre)
                    }
                }
            }
        }
    }
}
