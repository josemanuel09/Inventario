package edu.ucne.inventario.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.inventario.data.local.entities.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Upsert
    suspend fun save(productoEntity: ProductoEntity)
    @Query("""
        SELECT * FROM Productos
        WHERE productoId = :productoId
        LIMIT 1
    """)
    suspend fun getProducto(productoId: Int): ProductoEntity?

    @Delete
    suspend fun deleteProducto(productoEntity: ProductoEntity)
    @Query("SELECT * FROM Productos")
    fun getProductos(): Flow<List<ProductoEntity>>


}