package edu.ucne.inventario.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object CategoriasList : Screen()
    @Serializable
    data class Categorias(val categoriaId: Int): Screen()
    @Serializable
    data object ProovedoresList : Screen()
    @Serializable
    data class Proovedores(val proovedorId: Int): Screen()
    @Serializable
    data object ProductosList : Screen()
    @Serializable
    data class Productos(val productoId: Int): Screen()
    @Serializable
    data class DeleteProducto(val productoId: Int): Screen()
    @Serializable
    data class EditProducto(val productoId: Int): Screen()
    @Serializable
    data class DeleteProovedor(val proovedorId: Int): Screen()
    @Serializable
    data class EditProovedor(val proovedorId: Int): Screen()
    @Serializable
    data class DeleteCategoria(val categoriaId: Int): Screen()
    @Serializable
    data class EditCategoria(val categoriaId: Int): Screen()





}