package com.example.noqui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class ConfirmarEnvioDialog : DialogFragment() {

    companion object {
        private const val ARG_NUMERO = "numero"
        private const val ARG_PLATA = "plata"
        private const val ARG_MSG = "mensaje"

        // Método estático para crear una instancia con argumentos
        fun newInstance(numero: String, plata: String, mensaje: String): ConfirmarEnvioDialog {
            val fragment = ConfirmarEnvioDialog()
            val args = Bundle()
            args.putString(ARG_NUMERO, numero)
            args.putString(ARG_PLATA, plata)
            args.putString(ARG_MSG, mensaje)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialogTheme)
    }

    override fun onStart() {
        super.onStart()

        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        val marginDp = 40
        val density = displayMetrics.density
        val marginPx = (marginDp * density).toInt()

        val dialogWidth = screenWidth - marginPx

        dialog?.window?.setLayout(
            dialogWidth,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_dialog_confirmar_envio, container, false)
    }

    // Agregar listener para comunicar la acción
    private lateinit var listener: ConfirmacionListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ConfirmacionListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ConfirmacionListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener los datos pasados desde la actividad
        val numero = arguments?.getString(ARG_NUMERO) ?: ""
        val plata = arguments?.getString(ARG_PLATA) ?: ""
        val mensaje = arguments?.getString(ARG_MSG) ?: ""

        view.findViewById<TextView>(R.id.dialogNumero).text = numero
        view.findViewById<TextView>(R.id.dialogPlata).text = plata
        view.findViewById<TextView>(R.id.dialogMensaje).text = mensaje


        view.findViewById<Button>(R.id.btnConfirmar).setOnClickListener {

            dismiss()
        }

        // Obtener el monto
        val plataStr = arguments?.getString(ARG_PLATA) ?: "$ 0"
        val cleanMonto = plataStr.replace("$", "").replace(".", "").trim()
        val montoDouble = cleanMonto.toDoubleOrNull() ?: 0.0

        // Lógica del botón Confirmar
        view.findViewById<Button>(R.id.btnConfirmar).setOnClickListener {
            listener.onEnvioConfirmado(
                monto = montoDouble,
                numero = numero,
                mensaje = mensaje
            )
            dismiss()
        }
    }
}