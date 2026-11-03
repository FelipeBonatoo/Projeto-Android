package com.example.myapplication

import androidx.room.*

// @Dao: interface com as operações da tabela "produtos".
// O Room escreve o código de cada função sozinho (via KSP).
// suspend fun: a função roda dentro de uma coroutine, sem travar a tela.
@Dao
interface ProdutoDao {

    @Insert
    suspend fun inserir(produto: Produto)

    @Query("SELECT * FROM produtos")
    suspend fun buscarTodos(): List<Produto>

    @Update
    suspend fun atualizar(produto: Produto)

    @Delete
    suspend fun deletar(produto: Produto)

    // Consulta filtrada: traz só o produto com aquele id (ou null se não existir)
    @Query("SELECT * FROM produtos WHERE id = :id")
    suspend fun buscarPorId(id: Int): Produto?
}
