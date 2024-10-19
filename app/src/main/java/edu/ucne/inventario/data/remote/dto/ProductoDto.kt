package edu.ucne.inventario.data.remote.dto

import edu.ucne.inventario.data.local.entities.ProductoEntity

data class ProductoDto (
    val productoId: Int?,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val cantidad: Int,
    val categoriaId: Int,
    val proovedorId: Int



)
fun ProductoDto.toEntity(): ProductoEntity {
    return ProductoEntity(
        productoId = productoId ?: 0,
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        cantidad = cantidad,
        categoriaId = categoriaId,
        proovedorId = proovedorId
    )

}