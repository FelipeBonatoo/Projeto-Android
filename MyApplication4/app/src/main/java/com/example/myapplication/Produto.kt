package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity: cada objeto Produto vira uma linha da tabela "produtos" no banco
@Entity(tableName = "produtos")
data class Produto(
    val emoji: String,
    val nome: String,
    val descricao: String,
    val preco: Double,
    val categoria: String,
    // @PrimaryKey(autoGenerate = true): o próprio Room gera o id (1, 2, 3...).
    // Fica por último com valor 0 para criar um Produto sem informar o id.
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
