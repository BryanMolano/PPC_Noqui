
package com.example.noqui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.EnumMap
import java.util.Locale

class envio_realizado : AppCompatActivity() {

    private val TAG = "EnvioRealizadoQR"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_envio_realizado)

        // Obtener datos de la transacción
        val numero = intent.getStringExtra("numero") ?: "N/A"
        val monto = intent.getStringExtra("monto") ?: "0"
        val mensaje = intent.getStringExtra("mensaje") ?: "Sin mensaje"

        // Mostrar en pantalla con formato
        findViewById<TextView>(R.id.textNumero).text = numero
        findViewById<TextView>(R.id.textMonto).text = "$monto"
        findViewById<TextView>(R.id.textMensaje).text = mensaje

        // Formato de fecha
        val dateFormat =
            SimpleDateFormat("dd 'de' MMMM 'de' yyyy 'a las' hh:mm a", Locale("es", "ES"))
        val fecha = dateFormat.format(Date())
        findViewById<TextView>(R.id.textFecha).text = fecha

        // Construir datos del QR en JSON (más fácil de leer/usar)
        val qrDataString = """
            {
              "monto":"$monto",
              "numero":"$numero",
              "mensaje":"$mensaje",
              "fecha":"$fecha"
            }
        """.trimIndent()

        // Generar el QR
        val qrImageView = findViewById<ImageView>(R.id.imageViewQR)
        qrImageView.post {
            try {
                val size = qrImageView.width.coerceAtMost(qrImageView.height)
                    .takeIf { it > 0 } ?: 512 // tamaño mínimo por defecto

                val qrBitmap = generateQRCode(qrDataString, size, size)

                if (qrBitmap != null) {
                    qrImageView.setImageBitmap(qrBitmap)
                    Log.d(TAG, "QR generado con éxito: $qrDataString")
                } else {
                    Log.e(TAG, "Fallo al generar el Bitmap del QR.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "ERROR FATAL en la generación de QR: ${e.message}", e)
            }
        }

        // Botones de navegación
        findViewById<ImageView>(R.id.btnAtras2).setOnClickListener {
            finishToMain()
        }

        findViewById<Button>(R.id.btnListo).setOnClickListener {
            finishToMain()
        }
    }

    private fun finishToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
        finish()
    }

    private fun generateQRCode(content: String, width: Int, height: Int): Bitmap? {
        return try {
            val writer = QRCodeWriter()

            // Configurar hints de codificación UTF-8
            val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
            hints[EncodeHintType.CHARACTER_SET] = "UTF-8"

            val bitMatrix: BitMatrix =
                writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints)

            val bitmap = createBitmap(width, height, Bitmap.Config.RGB_565)

            val colorBlack = Color.rgb(0, 0, 0)
            val colorWhite = Color.rgb(255, 255, 255)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap[x, y] = if (bitMatrix[x, y]) colorBlack else colorWhite
                }
            }
            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "Fallo al generar QR: ${e.message}")
            null
        }
    }
}
