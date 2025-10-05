package com.example.noqui

import java.util.UUID

/**
 * Clase de datos para representar una solicitud de dinero.
 *
 * @param id Identificador único de la petición.
 * @param contacto Nombre o número de la persona a la que se le pide.
 * @param monto Cantidad de dinero solicitada.
 * @param mensaje Mensaje adjunto a la solicitud.
 * @param fecha Marca de tiempo de la solicitud.
 */
data class PeticionDinero(
    val id: String = UUID.randomUUID().toString(),
    val contacto: String,
    val monto: Double,
    val mensaje: String,
    val fecha: Long = System.currentTimeMillis() // Para ordenar
)

/**
 * Objeto Singleton para el almacenamiento de datos en memoria
 * Las listas se mantienen vivas mientras la aplicación esté activa.
 */
object AlmacenDatos {
    // Lista para las notificaciones de dinero pedido (En Espera)
    val solicitudesEnEspera = mutableListOf<PeticionDinero>()

    // Lista para las notificaciones de dinero recibido (Recibidas)
    // Se agrega un ejemplo inicial.
    val solicitudesRecibidas = mutableListOf<PeticionDinero>(
        PeticionDinero(
            contacto = "Javier López",
            monto = 50000.00,
            mensaje = "Pago por almuerzo",
        ),
        PeticionDinero(
            contacto = "María González",
            monto = 25000.00,
            mensaje = "Pago por libros",
        )
    )
}


