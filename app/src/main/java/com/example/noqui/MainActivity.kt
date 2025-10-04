package com.example.noqui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import android.app.Activity
import android.widget.Toast
import java.text.DecimalFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val movimientos = ArrayList<Movimiento>()
    private var dinero_disponible: Int = 1000
    private var dinero_caja_fuerte: Int = 0
    private var dinero_total: Int = dinero_disponible + dinero_caja_fuerte
    private var monto_en: Int = 0
    private var numero: String = ""
    private var dinero_visible: Boolean = true
    private lateinit var tvBienvenida: TextView // Declaración de tvBienvenida

    // Formateador para mostrar $ con separadores de miles
    private val saldoFormatter = DecimalFormat("#,###").apply {
        decimalFormatSymbols = java.text.DecimalFormatSymbols(Locale.getDefault()).apply {
            groupingSeparator = '.'
        }
    }

    private val cajaFuerteLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    dinero_disponible = data.getIntExtra("dinero_disponible", dinero_disponible)
                    dinero_caja_fuerte = data.getIntExtra("dinero_caja_fuerte", dinero_caja_fuerte)
                    dinero_total = dinero_disponible + dinero_caja_fuerte
                    actualizarSaldosUI()
                }
            }
        }

    private val serviciosLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val servicio = data.getStringExtra("movimiento_servicio") ?: ""
                    val monto = data.getIntExtra("movimiento_monto", 0)

                    movimientos.add(Movimiento(servicio, monto))

                    dinero_disponible = data.getIntExtra("nuevo_dinero", dinero_disponible)
                    dinero_total = dinero_disponible + dinero_caja_fuerte
                    actualizarSaldosUI()

                    Toast.makeText(this, "Dinero actualizado a: $dinero_disponible", Toast.LENGTH_SHORT).show()
                }
            }
        }
    // IMPORTANTE
    private val enviarLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    // Recibir el nuevo saldo de vuelta
                    val nuevoSaldoDouble = data.getDoubleExtra("nuevo_dinero_disponible", dinero_disponible.toDouble())
                    monto_en = data.getIntExtra("mo_monto", 0)
                    numero = data.getStringExtra("mo_numero") ?: ""

                    movimientos.add(Movimiento(numero, monto_en))

                    dinero_disponible = nuevoSaldoDouble.toInt()
                    dinero_total = dinero_disponible + dinero_caja_fuerte
                    actualizarSaldosUI()
                }
            }
        }

    // 💡 NUEVO LAUNCHER para PERFIL_INICIO (para recibir el nombre actualizado)
    private val perfilLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                // Extraer el nombre retornado desde perfil_Inicio
                val primerNombre = data?.getStringExtra(perfil_Inicio.EXTRA_NOMBRE_RETORNADO)
                if (primerNombre != null && primerNombre.isNotBlank()) {
                    // Actualizar el saludo de bienvenida con el nuevo nombre
                    tvBienvenida.text = "Hola, $primerNombre"
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAzulnv = findViewById<ImageButton>(R.id.btnAzulnv)
        val btnAzulba = findViewById<ImageButton>(R.id.btnAzul)
        val btnFondoba = findViewById<ImageButton>(R.id.btnFondo)
        val azul = findViewById<View>(R.id.azul)

        val btn_ojo_dinero = findViewById<ImageButton>(R.id.button_ojo_dinero)
        val btn_movimientos = findViewById<ImageButton>(R.id.btnMovimientos)
        val btn_enviar = findViewById<MaterialButton>(R.id.btn_enviar)
        val btn_servicios = findViewById<ImageButton>(R.id.btn_servicios)
        val btn_servicios_nv = findViewById<ImageButton>(R.id.btnServicios)
        val btn_tarjeta = findViewById<ImageButton>(R.id.btnTarjeta)
        val btn_envia = findViewById<ImageButton>(R.id.btnEnvia)
        val btnPerfil1 = findViewById<MaterialButton>(R.id.btnPerfil1)
        val btnPerfil2 = findViewById<ImageButton>(R.id.btnPerfil2)
        tvBienvenida = findViewById<TextView>(R.id.tvBienvenida)

        // 💡 Lógica inicial para el saludo (usa el valor persistido en perfil_Inicio)
        val primerNombre = perfil_Inicio.getFirstName(perfil_Inicio.currentUserName)
        tvBienvenida.text = "Hola, $primerNombre"

        val telefonoInicial = intent.getStringExtra(Login.EXTRA_TELEFONO_LOGIN)

        // Si se recibió un número de teléfono y el valor persistente en perfil_Inicio
        // aún es el valor por defecto ("Teléfono (opcional)"), se actualiza.
        if (telefonoInicial != null && perfil_Inicio.currentUserPhone == "Teléfono (opcional)") {
            perfil_Inicio.currentUserPhone = telefonoInicial
        }

        // Función para iniciar actividad de enviar
        val iniciarEnviar = {
            val intent = Intent(this, enviar::class.java)
            intent.putExtra("dinero_disponible", dinero_disponible.toDouble())
            enviarLauncher.launch(intent)
        }

        btn_enviar.setOnClickListener { iniciarEnviar() }
        btn_envia.setOnClickListener { iniciarEnviar() }

        btn_servicios.setOnClickListener {
            val intent = Intent(this, Servicios::class.java)
            intent.putExtra("dinero_disponible", dinero_disponible)
            serviciosLauncher.launch(intent)
        }
        btn_servicios_nv.setOnClickListener {
            val intent = Intent(this, Servicios::class.java)
            intent.putExtra("dinero_disponible", dinero_disponible)
            serviciosLauncher.launch(intent)
        }
        btn_tarjeta.setOnClickListener {
            val intent = Intent(this, tarjeta::class.java)
            startActivity(intent)
        }

        actualizarSaldosUI()

        btnFondoba.setOnClickListener { azul.visibility = View.GONE }
        btnAzulnv.setOnClickListener { azul.visibility = View.VISIBLE }
        btnAzulba.setOnClickListener { azul.visibility = View.GONE }

        btn_movimientos.setOnClickListener {
            val intent = Intent(this, Movimientos::class.java)
            intent.putExtra("lista_movimientos", movimientos)
            startActivity(intent)
        }

        val btn_caja_fuerte = findViewById<MaterialButton>(R.id.btnCajaFuerte)
        val btn_caja_fuerte2 = findViewById<ImageButton>(R.id.btnCajaFuerte2)

        btn_caja_fuerte.setOnClickListener {
            val intent = Intent(this, caja_fuerte::class.java)
            intent.putExtra("dinero_disponible", dinero_disponible)
            intent.putExtra("dinero_caja_fuerte", dinero_caja_fuerte)
            intent.putExtra("dinero_total", dinero_total)
            cajaFuerteLauncher.launch(intent)
        }
        btn_caja_fuerte2.setOnClickListener {
            val intent = Intent(this, caja_fuerte::class.java)
            intent.putExtra("dinero_disponible", dinero_disponible)
            intent.putExtra("dinero_caja_fuerte", dinero_caja_fuerte)
            intent.putExtra("dinero_total", dinero_total)
            cajaFuerteLauncher.launch(intent)
        }
        btnPerfil1.setOnClickListener {
            val intent = Intent(this, perfil_Inicio::class.java)
            perfilLauncher.launch(intent)
        }

        btnPerfil2.setOnClickListener {
            val intent = Intent(this, perfil_Inicio::class.java)
            perfilLauncher.launch(intent)
        }

        btn_ojo_dinero.setOnClickListener {
            dinero_visible = !dinero_visible
            actualizarSaldosUI()
        }


    }

    // Método para actualizar saldos en la interfaz
    private fun actualizarSaldosUI() {
        val tv_dinero_disponible = findViewById<TextView>(R.id.textview_dinero_disponible)
        val tv_dinero_total = findViewById<TextView>(R.id.textview_dinero_total)

        val disponibleFormato = saldoFormatter.format(dinero_disponible)
        val totalFormato = saldoFormatter.format(dinero_total)

        val disponibleTexto = if (dinero_visible) "$$disponibleFormato" else "*****"
        val totalTexto = if (dinero_visible) "$$totalFormato" else "*****"

        tv_dinero_disponible.text = disponibleTexto
        tv_dinero_total.text = totalTexto
    }
}
