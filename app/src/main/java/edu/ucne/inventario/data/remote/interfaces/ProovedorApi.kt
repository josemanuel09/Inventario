package edu.ucne.inventario.data.remote.interfaces

import edu.ucne.inventario.data.remote.dto.ProovedorDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProovedorApi {
    @GET("/api/Proovedores")
    suspend fun getProovedores(): List<ProovedorDto>
    @GET("/api/Proovedores/{id}")
    suspend fun getProovedorById(@Path("id") id: Int): ProovedorDto
    @POST("/api/Proovedores")
    suspend fun saveProovedor(@Body proovedor: ProovedorDto): ProovedorDto
    @DELETE("/api/Proovedores/{id}")
    suspend fun deleteProovedor(@Path("id") id: Int): Response<Void?>


}