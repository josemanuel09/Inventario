package edu.ucne.inventario.data.remote.dto

import edu.ucne.inventario.data.local.entities.ProovedorEntity

data class ProovedorDto(
    val proovedorId: Int?,
    val nombre: String,
    val contacto: String,
    val direccion: String
)

fun ProovedorDto.toEntity(): ProovedorEntity {
    return ProovedorEntity(
        proovedorId = proovedorId ?: 0,
        nombre = nombre,
        contacto = contacto,
        direccion = direccion
    )
}
