package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity: cada objeto Pedido vira uma linha da tabela "pedidos" no banco
@Entity(tableName = "pedidos")
data class Pedido(
    val cliente: String,
    // O total fica salvo à parte para não mudar se o preço do produto for editado depois.
    val idsItens: String,
    val total: Double,
    val status: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

fun Pedido.listaIds(): List<Int> = idsItens.split(",").mapNotNull { it.trim().toIntOrNull() }
