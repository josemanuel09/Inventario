package edu.ucne.inventario.data.repository

import android.util.Log
import edu.ucne.inventario.data.local.dao.ProductoDao
import edu.ucne.inventario.data.local.entities.ProductoEntity
import edu.ucne.inventario.data.remote.RemoteDataSource
import edu.ucne.inventario.data.remote.Resources
import edu.ucne.inventario.data.remote.dto.ProductoDto
import edu.ucne.inventario.data.remote.dto.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class ProductoRepository @Inject constructor(
    private val data: RemoteDataSource,
    private val productoDao: ProductoDao
) {

    fun getProductos(): Flow<Resources<List<ProductoEntity>>> = flow {
        try {
            emit(Resources.Loading())
            val productosApi = data.getProductos()
            val productosRoom = productosApi.map { it.toEntity() }
            productosRoom.forEach { productoDao.save(it) }

            emit(Resources.Success(productosRoom))
        } catch (e: HttpException) {
            emit(Resources.Error(e.message ?: "Error HTTP DE INTERNET"))
        } catch (e: Exception) {
            val productosRoom = productoDao.getProductos().firstOrNull()
            if (productosRoom.isNullOrEmpty()) {
                emit(Resources.Error(e.message ?: "Error DESCONOCIDO"))
            } else {
                emit(Resources.Success(productosRoom))
            }
        }
    }

    fun getProductoById(id: Int): Flow<Resources<ProductoEntity>> = flow {
        try {
            emit(Resources.Loading())
            val producto = productoDao.getProducto(id)
            emit(Resources.Success(producto!!))
        } catch (e: HttpException) {
            emit(Resources.Error("ERROR DE INTERNET ${e.message()}"))
        } catch (e: Exception) {
            Log.e("Producto Repository", "getProducto: ${e.message}")
            emit(Resources.Error(e.message ?: "Error DESCONOCIDO"))
        }
    }

    fun saveProducto(producto: ProductoDto): Flow<Resources<ProductoEntity>> = flow {
        try {
            emit(Resources.Loading())
            val productoApi = data.saveProducto(producto)
            val productoRoom = productoApi.toEntity()
            productoDao.save(productoRoom)
            emit(Resources.Success(productoRoom))
        } catch (e: HttpException) {
            emit(Resources.Error("Error de conexión: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resources.Error("Error: ${e.message}"))
        }
    }

    suspend fun deleteProducto(id: Int): Resources<Unit> {
        return try {
            data.deleteProducto(id)
            val productoRoom = productoDao.getProducto(id)
            if (productoRoom != null) {
                productoDao.deleteProducto(productoRoom)
            }
            Resources.Success(Unit)
        } catch (e: HttpException) {
            Resources.Error("Error de conexión: ${e.message()}")
        } catch (e: Exception) {
            val productoRoom = productoDao.getProducto(id)
            if (productoRoom != null) {
                productoDao.deleteProducto(productoRoom)
            }
            Resources.Success(Unit)
        }
    }
}
