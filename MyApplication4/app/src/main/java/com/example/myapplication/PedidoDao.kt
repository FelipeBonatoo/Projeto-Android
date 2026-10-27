package com.example.myapplication

import androidx.room.*

// @Dao: interface com as operações da tabela "pedidos"
@Dao
interface PedidoDao {

    // Retorna Long: é o id que o banco gerou para o pedido novo.
    // Usamos esse id para abrir a tela de confirmação.
    @Insert
    suspend fun inserir(pedido: Pedido): Long

    @Query("SELECT * FROM pedidos")
    suspend fun buscarTodos(): List<Pedido>

    @Update
    suspend fun atualizar(pedido: Pedido)

    @Delete
    suspend fun deletar(pedido: Pedido)

    // Consulta filtrada: traz só o pedido com aquele id (ou null se não existir)
    @Query("SELECT * FROM pedidos WHERE id = :id")
    suspend fun buscarPorId(id: Int): Pedido?
}
