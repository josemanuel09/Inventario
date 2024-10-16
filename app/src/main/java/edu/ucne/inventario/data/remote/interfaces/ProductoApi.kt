package edu.ucne.inventario.data.remote.interfaces

import edu.ucne.inventario.data.remote.dto.ProductoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductoApi {
    @GET("/api/Productos")
    suspend fun getProductos(): List<ProductoDto>
    @GET("/api/Productos/{id}")
    suspend fun getProductoById(@Path("id") id: Int): ProductoDto
    @POST("/api/Productos")
    suspend fun saveProducto(@Body producto: ProductoDto): ProductoDto
    @DELETE ("/api/Productos/{id}")
    suspend fun deleteProducto(@Path("id") id: Int): Response<Void?>

}