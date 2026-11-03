package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabela de produtos
@Entity(tableName = "produtos")
data class Produto(
    val emoji: String,
    val nome: String,
    val descricao: String,
    val preco: Double,
    val categoria: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
