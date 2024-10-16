package edu.ucne.inventario.data.repository

import edu.ucne.inventario.data.remote.dto.CategoriaDto
import edu.ucne.inventario.data.remote.interfaces.CategoriaApi
import retrofit2.Response
import javax.inject.Inject

class CategoriaRepository @Inject constructor(
    private val categoriaApi: CategoriaApi
) {
    suspend fun getCategorias() = categoriaApi.getCategorias()
    suspend fun getCategoriaById(id: Int) = categoriaApi.getCategoriaById(id)
    suspend fun saveCategoria(categoria: CategoriaDto) = categoriaApi.saveCategoria(categoria)
    suspend fun deleteCategoria(id: Int): Response<Void?> = categoriaApi.deleteCategoria(id)

}