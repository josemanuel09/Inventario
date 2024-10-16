package edu.ucne.inventario.data.repository

import edu.ucne.inventario.data.remote.dto.ProductoDto
import edu.ucne.inventario.data.remote.interfaces.ProductoApi
import retrofit2.Response
import javax.inject.Inject

class ProductoRepository @Inject constructor(
    private val productoApi: ProductoApi
) {
    suspend fun getProductos() = productoApi.getProductos()
    suspend fun getProductoById(id: Int) = productoApi.getProductoById(id)
    suspend fun saveProducto(producto: ProductoDto) = productoApi.saveProducto(producto)
    suspend fun deleteProducto(id: Int) : Response<Void?> = productoApi.deleteProducto(id)

}