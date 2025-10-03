package com.example.noqui

interface ConfirmacionListener {
    fun onEnvioConfirmado(monto: Double, numero: String, mensaje: String)
}