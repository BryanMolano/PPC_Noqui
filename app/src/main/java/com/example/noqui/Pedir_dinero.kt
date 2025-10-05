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
import androidx.appcompat.app.AppCompatActivity

class Pedir_dinero : AppCompatActivity() {

    private lateinit var btnVolver: ImageButton
    private lateinit var btnSiguiente: ImageButton
    private lateinit var btnToggleSaldo: ImageButton
    private lateinit var layoutSaldo: LinearLayout
    private lateinit var edtNumero: EditText

    private var saldoVisible = false
    private val PICK_CONTACT = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedir_dinero)

        btnVolver = findViewById(R.id.btnVolver_pide)
        btnSiguiente = findViewById(R.id.btnSiguepide)
        btnToggleSaldo = findViewById(R.id.btnToggleSaldo)
        layoutSaldo = findViewById(R.id.layoutSaldo)
        edtNumero = findViewById(R.id.edit_numeropide)

        // Evento volver
        btnVolver.setOnClickListener { finish() }

        // Evento continuar
        btnSiguiente.setOnClickListener {
            // Aquí tu lógica de envío
        }

        btnToggleSaldo.setOnClickListener {
            saldoVisible = !saldoVisible

            val transition = android.transition.AutoTransition()
            transition.duration = 300
            android.transition.TransitionManager.beginDelayedTransition(
                findViewById(R.id.contenedorPrincipal),
                transition
            )

            layoutSaldo.visibility = if (saldoVisible) View.VISIBLE else View.GONE
            btnToggleSaldo.setImageResource(
                if (saldoVisible) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
            )
        }

        // Evento: tocar el ícono del EditText (drawableEnd)
        edtNumero.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2 // posición derecha
                if (event.rawX >= (edtNumero.right - edtNumero.compoundDrawables[drawableEnd].bounds.width())) {
                    v.performClick()
                    abrirContactos()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun abrirContactos() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        startActivityForResult(intent, PICK_CONTACT)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_CONTACT && resultCode == Activity.RESULT_OK) {
            val contactUri: Uri? = data?.data
            val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)

            contentResolver.query(contactUri!!, projection, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val number = cursor.getString(numberIndex)
                    edtNumero.setText(number) // autocompleta con el número
                }
            }
        }
    }
}
