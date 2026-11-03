package com.example.myapplication.data.repository

import com.example.myapplication.data.Produto
import com.example.myapplication.data.ProdutoDao
import kotlinx.coroutines.flow.Flow

// Só o repository fala com o DAO
class ProdutoRepository(private val produtoDao: ProdutoDao) {
    fun buscarTodos(): Flow<List<Produto>> = produtoDao.buscarTodos()

    suspend fun buscarPorId(id: Int): Produto? = produtoDao.buscarPorId(id)

    suspend fun inserir(produto: Produto) = produtoDao.inserir(produto)

    suspend fun atualizar(produto: Produto) = produtoDao.atualizar(produto)

    suspend fun deletar(produto: Produto) = produtoDao.deletar(produto)
}
