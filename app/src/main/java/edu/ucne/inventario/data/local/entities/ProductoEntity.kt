package edu.ucne.inventario.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Productos",
    foreignKeys = [
        ForeignKey(
        entity = CategoriaEntity::class,
        parentColumns = ["categoriaId"],
        childColumns = ["categoriaId"]
        ),
        ForeignKey(
            entity = ProovedorEntity::class,
            parentColumns = ["proovedorId"],
            childColumns = ["proovedorId"]
        )
    ]
)

 data class ProductoEntity(
     @PrimaryKey
     val productoId: Int,
     val nombre: String,
     val descripcion: String,
     val precio: Double,
     val cantidad: Int,
     val categoriaId: Int,
     val proovedorId: Int



 )