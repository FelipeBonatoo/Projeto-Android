package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity: cada objeto Pedido vira uma linha da tabela "pedidos" no banco
@Entity(tableName = "pedidos")
data class Pedido(
    val cliente: String,
    // O Room só sabe salvar tipos simples (texto, número...). Uma List<Produto> precisaria
    // de um TypeConverter, então guardamos só os ids dos produtos num texto: "1,3,3".
    // O total fica salvo à parte para não mudar se o preço do produto for editado depois.
    val idsItens: String,
    val total: Double,
    val status: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)

// Transforma o texto "1,3,3" de volta em uma lista de ids [1, 3, 3]
fun Pedido.listaIds(): List<Int> = idsItens.split(",").mapNotNull { it.trim().toIntOrNull() }
