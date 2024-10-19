package edu.ucne.inventario.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.inventario.data.local.dao.CategoriaDao
import edu.ucne.inventario.data.local.dao.ProductoDao
import edu.ucne.inventario.data.local.dao.ProovedorDao
import edu.ucne.inventario.data.local.entities.CategoriaEntity
import edu.ucne.inventario.data.local.entities.ProductoEntity
import edu.ucne.inventario.data.local.entities.ProovedorEntity

@Database(
    entities = [
        ProductoEntity::class,
        CategoriaEntity::class,
        ProovedorEntity::class
    ],
    version = 2,
    exportSchema = false

)

abstract class InventarioDb: RoomDatabase() {
    abstract val productoDao: ProductoDao
    abstract val categoriaDao: CategoriaDao
    abstract val proovedorDao: ProovedorDao

}