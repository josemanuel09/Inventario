package edu.ucne.inventario.data.repository

import edu.ucne.inventario.data.remote.dto.ProovedorDto
import edu.ucne.inventario.data.remote.interfaces.ProovedorApi
import retrofit2.Response
import javax.inject.Inject

class ProovedorRepository @Inject constructor(
    private val proovedorApi: ProovedorApi
) {
    suspend fun getProovedores() = proovedorApi.getProovedores()
    suspend fun getProovedorById(id: Int) = proovedorApi.getProovedorById(id)
    suspend fun saveProovedor(proovedor: ProovedorDto) = proovedorApi.saveProovedor(proovedor)
    suspend fun deleteProovedor(id: Int): Response<Void?> = proovedorApi.deleteProovedor(id)
}