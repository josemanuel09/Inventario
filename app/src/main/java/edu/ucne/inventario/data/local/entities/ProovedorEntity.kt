package edu.ucne.inventario.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Proovedores")
data class ProovedorEntity(
    @PrimaryKey
    val proovedorId: Int,
    val nombre: String,
    val contacto: String,
    val direccion: String
)
