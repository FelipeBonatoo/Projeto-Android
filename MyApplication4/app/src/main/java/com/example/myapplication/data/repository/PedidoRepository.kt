package com.example.myapplication.data.repository

import com.example.myapplication.data.Pedido
import com.example.myapplication.data.PedidoDao
import kotlinx.coroutines.flow.Flow

// Só o repository fala com o DAO
class PedidoRepository(private val pedidoDao: PedidoDao) {
    fun buscarTodos(): Flow<List<Pedido>> = pedidoDao.buscarTodos()

    suspend fun buscarPorId(id: Int): Pedido? = pedidoDao.buscarPorId(id)

    suspend fun inserir(pedido: Pedido): Long = pedidoDao.inserir(pedido)

    suspend fun atualizar(pedido: Pedido) = pedidoDao.atualizar(pedido)

    suspend fun deletar(pedido: Pedido) = pedidoDao.deletar(pedido)
}
