package edu.ucne.inventario.data.remote.interfaces

import edu.ucne.inventario.data.remote.dto.CategoriaDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CategoriaApi {
    @GET("/api/Categorias")
    suspend fun getCategorias(): List<CategoriaDto>
    @GET("/api/Categorias/{id}")
    suspend fun getCategoriaById(@Path("id") id: Int): CategoriaDto
    @POST("/api/Categorias")
    suspend fun saveCategoria(@Body categoria: CategoriaDto): CategoriaDto
    @DELETE("/api/Categorias/{id}")
    suspend fun deleteCategoria(@Path("id") id: Int): Response<Void?>


}