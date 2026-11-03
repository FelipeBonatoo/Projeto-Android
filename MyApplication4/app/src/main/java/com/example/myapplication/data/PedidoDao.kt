package com.example.myapplication.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {
    @Insert
    suspend fun inserir(pedido: Pedido): Long

    // Flow: a lista atualiza sozinha quando a tabela muda
    @Query("SELECT * FROM pedidos")
    fun buscarTodos(): Flow<List<Pedido>>

    @Update
    suspend fun atualizar(pedido: Pedido)

    @Delete
    suspend fun deletar(pedido: Pedido)

    @Query("SELECT * FROM pedidos WHERE id = :id")
    suspend fun buscarPorId(id: Int): Pedido?
}
