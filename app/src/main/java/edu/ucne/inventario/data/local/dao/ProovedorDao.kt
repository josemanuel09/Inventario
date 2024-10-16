package edu.ucne.inventario.data.local.dao

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.inventario.data.local.entities.ProovedorEntity
import kotlinx.coroutines.flow.Flow

interface ProovedorDao {
    @Upsert
    suspend fun save(proovedorEntity: ProovedorEntity)
    @Query("""SELECT * FROM Proovedores
            WHERE proovedorId = :proovedorId
            LIMIT 1
    """)
    suspend fun getProovedor(proovedorId: Int): ProovedorEntity?
    @Delete
    suspend fun deleteProovedor(proovedorEntity: ProovedorEntity)
    @Query("SELECT * FROM Proovedores")
    fun getProovedores(): Flow<List<ProovedorEntity>>


}