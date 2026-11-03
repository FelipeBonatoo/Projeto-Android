package com.example.myapplication.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {
    @Insert
    suspend fun inserir(produto: Produto)

    // Flow: a lista atualiza sozinha quando a tabela muda
    @Query("SELECT * FROM produtos")
    fun buscarTodos(): Flow<List<Produto>>

    @Update
    suspend fun atualizar(produto: Produto)

    @Delete
    suspend fun deletar(produto: Produto)

    @Query("SELECT * FROM produtos WHERE id = :id")
    suspend fun buscarPorId(id: Int): Produto?
}
