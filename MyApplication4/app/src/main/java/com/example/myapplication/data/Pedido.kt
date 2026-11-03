package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabela de pedidos
@Entity(tableName = "pedidos")
data class Pedido(
    val cliente: String,
    val idsItens: String,
    val total: Double,
    val status: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

// Transforma "1,3,3" em [1, 3, 3]
fun Pedido.listaIds(): List<Int> = idsItens.split(",").mapNotNull { it.trim().toIntOrNull() }
