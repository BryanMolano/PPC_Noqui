package com.example.noqui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.text.Html

class Notificacion : AppCompatActivity() {
    private lateinit var btnVolver: ImageButton
    private lateinit var btnRecibidos: Button
    private lateinit var btnEnEspera: Button
    private lateinit var listaNotificaciones: RecyclerView

    private lateinit var adaptador: AdaptadorPeticiones
    private var esRecibidosActivo = true // Estado inicial: Recibidos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificacion)

        // Si el Intent tiene la clave, significa que venimos de una nueva peticion.
        if (intent.getBooleanExtra(Pedir_dinero.MOSTRAR_EN_ESPERA_KEY, false)) {
            // Establecer el estado inicial a "En Espera" (false)
            esRecibidosActivo = false
        }

        // Inicializar vistas (IDs del activity_notificacion.xml)
        btnVolver = findViewById(R.id.btnVolver_pide2)
        btnRecibidos = findViewById(R.id.btnRecibidos)
        btnEnEspera = findViewById(R.id.btnEnEspera)
        listaNotificaciones = findViewById(R.id.recyclerViewNotificaciones)

        // Configurar RecyclerView
        adaptador = AdaptadorPeticiones()
        listaNotificaciones.layoutManager = LinearLayoutManager(this)
        listaNotificaciones.adapter = adaptador

        // Eventos de los botones de pestaña
        btnRecibidos.setOnClickListener { mostrarRecibidos() }
        btnEnEspera.setOnClickListener { mostrarEnEspera() }
        btnVolver.setOnClickListener { finish() }

        // La carga inicial y refresco se gestiona en onResume()
    }

    /**
     * onResume() se llama cada vez que la actividad vuelve al primer plano,
     * asegurando que la lista se refresque después de usar Pedir_dinero.
     */
    override fun onResume() {
        super.onResume()
        // Cargar los datos de la pestaña que estuviera activa
        if (esRecibidosActivo) {
            mostrarRecibidos(forzarCarga = true)
        } else {
            mostrarEnEspera(forzarCarga = true)
        }
    }

    /** Muestra las peticiones recibidas y actualiza el estado de los botones */
    private fun mostrarRecibidos(forzarCarga: Boolean = false) {
        if (!esRecibidosActivo || forzarCarga) {
            esRecibidosActivo = true
            // Carga la lista del Singleton AlmacenDatos
            adaptador.actualizarLista(AlmacenDatos.solicitudesRecibidas, true)
            actualizarEstiloBotones()
        }
    }

    /** Muestra las peticiones en espera y actualiza el estado de los botones */
    private fun mostrarEnEspera(forzarCarga: Boolean = false) {
        if (esRecibidosActivo || forzarCarga) {
            esRecibidosActivo = false
            // Carga la lista del Singleton AlmacenDatos
            adaptador.actualizarLista(AlmacenDatos.solicitudesEnEspera, false)
            actualizarEstiloBotones()
        }
    }

    /** Actualiza el estilo visual de los botones de pestaña */
    private fun actualizarEstiloBotones() {
        val colorActivo = ContextCompat.getColor(this, android.R.color.white)
        val colorInactivo = 0xFFA9A9A9.toInt() // Gris claro

        // Estilos para Recibidos
        btnRecibidos.setTextColor(if (esRecibidosActivo) colorActivo else colorInactivo)
        btnRecibidos.alpha = if (esRecibidosActivo) 1.0f else 0.5f

        // Estilos para En Espera
        btnEnEspera.setTextColor(if (!esRecibidosActivo) colorActivo else colorInactivo)
        btnEnEspera.alpha = if (!esRecibidosActivo) 1.0f else 0.5f
    }

    // --- ADAPTER DEL RECYCLERVIEW (ADAPTADOR PERSONALIZADO) ---
    inner class AdaptadorPeticiones : RecyclerView.Adapter<AdaptadorPeticiones.VistaPeticionHolder>() {

        private var peticiones: List<PeticionDinero> = emptyList()
        private var esRecibido: Boolean = false
        // Formato de moneda (Colombia)
        private val formatoNumero: NumberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        private val formatoFecha = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

        fun actualizarLista(nuevaLista: List<PeticionDinero>, esRecibido: Boolean) {
            this.peticiones = nuevaLista
            this.esRecibido = esRecibido
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VistaPeticionHolder {
            // Usa el layout item_peticion_notificacion.xml
            val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_peticion_notificacion, parent, false)
            return VistaPeticionHolder(vista)
        }

        override fun onBindViewHolder(holder: VistaPeticionHolder, position: Int) {
            val peticion = peticiones[position]
            holder.enlazar(peticion, esRecibido)
        }

        override fun getItemCount(): Int = peticiones.size

        inner class VistaPeticionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            // Referencias a los IDs del item_peticion_notificacion.xml
            private val tvTitulo: TextView = itemView.findViewById(R.id.tvTituloPeticion)
            private val tvMonto: TextView = itemView.findViewById(R.id.tvMontoPeticion)
            private val tvMensaje: TextView = itemView.findViewById(R.id.tvMensajePeticion)
            private val tvFecha: TextView = itemView.findViewById(R.id.tvFechaPeticion)

            fun enlazar(peticion: PeticionDinero, esRecibido: Boolean) {
                if (esRecibido) {
                    tvTitulo.text = "¡Recibiste ${formatoNumero.format(peticion.monto)}!"

                    // Aplicar negrilla al nombre del contacto en la línea "De:"
                    val textoMonto = "De: <b>${peticion.contacto}</b>"
                    tvMonto.text = Html.fromHtml(textoMonto, Html.FROM_HTML_MODE_COMPACT)

                    // Color verde (Recibido)
                    tvMonto.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_green_light))
                } else {
                    // Aplicar negrilla al nombre del contacto en el título de la solicitud
                    val textoTitulo = "Solicitud a <b>${peticion.contacto}</b>"
                    tvTitulo.text = Html.fromHtml(textoTitulo, Html.FROM_HTML_MODE_COMPACT)

                    tvMonto.text = "Monto: ${formatoNumero.format(peticion.monto)}"
                    // Color naranja (En Espera)
                    tvMonto.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.holo_orange_light))
                }

                tvMensaje.text = peticion.mensaje
                tvFecha.text = formatoFecha.format(Date(peticion.fecha))
            }
        }
    }
}
