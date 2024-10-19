package edu.ucne.inventario.data.repository

import android.util.Log
import edu.ucne.inventario.data.local.dao.ProovedorDao
import edu.ucne.inventario.data.local.entities.ProovedorEntity
import edu.ucne.inventario.data.remote.RemoteDataSource
import edu.ucne.inventario.data.remote.Resources
import edu.ucne.inventario.data.remote.dto.ProovedorDto
import edu.ucne.inventario.data.remote.dto.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class ProovedorRepository @Inject constructor(
    private val data: RemoteDataSource,
    private val proovedorDao: ProovedorDao
) {

    fun getProovedores(): Flow<Resources<List<ProovedorEntity>>> = flow {
        try {
            emit(Resources.Loading())
            val proovedorApi = data.getProovedores()
            val proovedorRoom = proovedorApi.map { it.toEntity() }
            proovedorRoom.forEach { proovedorDao.save(it) }

            emit(Resources.Success(proovedorRoom))
        } catch (e: HttpException) {
            emit(Resources.Error(e.message ?: "Error HTTP DE INTERNET"))
        } catch (e: Exception) {
            val proovedorRoom = proovedorDao.getProovedores().firstOrNull()
            if (proovedorRoom.isNullOrEmpty()) {
                emit(Resources.Error(e.message ?: "Error DESCONOCIDO"))
            } else {
                emit(Resources.Success(proovedorRoom))
            }
        }
    }

    fun getProovedorById(id: Int): Flow<Resources<ProovedorEntity>> = flow {
        try {
            emit(Resources.Loading())
            val proovedor = proovedorDao.getProovedor(id)
            emit(Resources.Success(proovedor!!))
        } catch (e: HttpException) {
            emit(Resources.Error("ERROR DE INTERNET ${e.message()}"))
        } catch (e: Exception) {
            Log.e("Proovedor Repository", "getProovedor: ${e.message}")
            emit(Resources.Error(e.message ?: "Error DESCONOCIDO"))
        }
    }

    fun saveProovedor(proovedor: ProovedorDto): Flow<Resources<ProovedorEntity>> = flow {
        try {
            emit(Resources.Loading())
            val proovedorApi = data.saveProovedor(proovedor)
            val proovedorRoom = proovedorApi.toEntity()
            proovedorDao.save(proovedorRoom)
            emit(Resources.Success(proovedorRoom))
        } catch (e: HttpException) {
            emit(Resources.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resources.Error("Error: ${e.message}"))
        }
    }

    suspend fun deleteProovedor(id: Int): Resources<Unit> {
        return try {
            data.deleteProovedor(id)
            val proovedorRoom = proovedorDao.getProovedor(id)
            if (proovedorRoom != null) {
                proovedorDao.deleteProovedor(proovedorRoom)
            }
            Resources.Success(Unit)
        } catch (e: HttpException) {
            Resources.Error("Error de conexión: ${e.message()}")
        } catch (e: Exception) {
            val proovedorRoom = proovedorDao.getProovedor(id)
            if (proovedorRoom != null) {
                proovedorDao.deleteProovedor(proovedorRoom)
            }
            Resources.Success(Unit)
        }
    }
}
