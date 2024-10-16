package edu.ucne.inventario.data.remote.dto

data class ProductoDto (
    val productoId: Int?,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val cantidad: Int,
    val categoriaId: Int,
    val proovedorId: Int



)