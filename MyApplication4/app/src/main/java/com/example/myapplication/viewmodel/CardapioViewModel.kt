package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Produto
import com.example.myapplication.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado da tela
data class CardapioUiState(
    val produtos: List<Produto> = emptyList(),
    val nome: String = "",
    val descricao: String = "",
    val preco: String = "",
    val emoji: String = "☕",
    val categoria: String = "Cafés"
)

class CardapioViewModel(private val produtoRepository: ProdutoRepository) : ViewModel() {
    // Privado: só o ViewModel altera. Público: a tela só lê
    private val _uiState = MutableStateFlow(CardapioUiState())
    val uiState: StateFlow<CardapioUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            produtoRepository.buscarTodos()
                .catch { e -> Log.e("TelaCardapio", "Erro ao buscar produtos", e) }
                .collect { lista ->
                    _uiState.update { it.copy(produtos = lista) }
                }
        }
    }

    fun alterarNome(valor: String) = _uiState.update { it.copy(nome = valor) }
    fun alterarDescricao(valor: String) = _uiState.update { it.copy(descricao = valor) }
    fun alterarPreco(valor: String) = _uiState.update { it.copy(preco = valor) }
    fun alterarEmoji(valor: String) = _uiState.update { it.copy(emoji = valor) }
    fun alterarCategoria(valor: String) = _uiState.update { it.copy(categoria = valor) }

    fun adicionarProduto() {
        val estado = _uiState.value
        val valor = estado.preco.replace(',', '.').toDoubleOrNull() ?: 0.0
        val novo = Produto(estado.emoji, estado.nome, estado.descricao, valor, estado.categoria)
        _uiState.update { it.copy(nome = "", descricao = "", preco = "") }
        viewModelScope.launch {
            try {
                produtoRepository.inserir(novo)
            } catch (e: Exception) {
                Log.e("TelaCardapio", "Erro ao inserir produto", e)
            }
        }
    }

    fun removerProduto(produto: Produto) {
        viewModelScope.launch {
            try {
                produtoRepository.deletar(produto)
            } catch (e: Exception) {
                Log.e("TelaCardapio", "Erro ao deletar produto", e)
            }
        }
    }
}
