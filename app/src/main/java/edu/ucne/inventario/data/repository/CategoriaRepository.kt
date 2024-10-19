package edu.ucne.inventario.data.repository

import android.util.Log
import edu.ucne.inventario.data.local.dao.CategoriaDao
import edu.ucne.inventario.data.local.entities.CategoriaEntity
import edu.ucne.inventario.data.remote.RemoteDataSource
import edu.ucne.inventario.data.remote.Resources
import edu.ucne.inventario.data.remote.dto.CategoriaDto
import edu.ucne.inventario.data.remote.dto.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class CategoriaRepository @Inject constructor(
    private val data: RemoteDataSource,
    private val categoriaDao: CategoriaDao
) {
    fun getCategorias(): Flow<Resources<List<CategoriaEntity>>> = flow {
        try {
            emit(Resources.Loading())
            val categoriaApi = data.getCategorias()
            val categoriaRoom = categoriaApi.map { it.toEntity() }
            categoriaRoom.forEach { categoriaDao.save(it) }

            emit(Resources.Success(categoriaRoom))
        } catch (e: HttpException) {
            emit(Resources.Error(e.message ?: "Error HTTP DE INTERNET"))
        } catch (e: Exception) {
            val categoriaRoom = categoriaDao.getCategorias().firstOrNull()
            if (categoriaRoom.isNullOrEmpty()) {
                emit(Resources.Error(e.message ?: "Error DESCONOCIDO"))
            } else {
                emit(Resources.Success(categoriaRoom))
            }
        }
    }


    fun getCategoriaById(id: Int): Flow<Resources<CategoriaEntity>> = flow {
        try{
            emit(Resources.Loading())
            val categoria = categoriaDao.getCategoria(id)
            emit(Resources.Success(categoria!!))
        }catch (e: HttpException){
            emit(Resources.Error("ERROR DE INTERNET ${e.message}"))
        }catch (e: Exception){
            Log.e("Categoria Repository", "getCategoria: ${e.message}")
            emit(Resources.Error(e.message ?: "Error DESCONOCIDO"))
        }
    }
    fun saveCategoria(categoria: CategoriaDto): Flow<Resources<CategoriaEntity>> = flow {
        try{
            emit(Resources.Loading())
            val categoriaApi = data.saveCategoria(categoria)
            val categoriaRoom = categoriaApi.toEntity()
            categoriaDao.save(categoriaRoom)
            emit(Resources.Success(categoriaRoom))
        }catch (e: HttpException){
            emit(Resources.Error("Error de conexión: ${e.message()}"))
        }catch (e: Exception) {
            emit(Resources.Error("Error: ${e.message}"))
        }
    }

    suspend fun deleteCategoria(id: Int): Resources<Unit> {
       return try{
           data.deleteCategoria(id)
           val categoriaRoom = categoriaDao.getCategoria(id)
           if (categoriaRoom != null){
               categoriaDao.deleteCategoria(categoriaRoom)
           }
           Resources.Success(Unit)
        }catch (e: HttpException){
           Resources.Error("Error de conexión: ${e.message()}")
       }catch (e: Exception){
           val categoriaRoom = categoriaDao.getCategoria(id)
           if (categoriaRoom != null){
               categoriaDao.deleteCategoria(categoriaRoom)
           }
           Resources.Success(Unit)
       }
    }

}