package edu.ucne.inventario.data.remote.dto

import edu.ucne.inventario.data.local.entities.CategoriaEntity

data class CategoriaDto(
    val categoriaId: Int?,
    val nombre: String,
    val descripcion: String

)

fun CategoriaDto.toEntity(): CategoriaEntity{
    return CategoriaEntity(
        categoriaId = categoriaId ?: 0,
        nombre = nombre,
        descripcion = descripcion
    )

}
