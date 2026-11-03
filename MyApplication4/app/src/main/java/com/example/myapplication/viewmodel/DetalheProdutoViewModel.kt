package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Produto
import com.example.myapplication.data.listaIds
import com.example.myapplication.data.repository.PedidoRepository
import com.example.myapplication.data.repository.ProdutoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado da tela
data class DetalheProdutoUiState(
    val produto: Produto? = null,
    val vezesVendido: Int = 0,
    val faturamento: Double = 0.0,
    val nome: String = "",
    val descricao: String = "",
    val preco: String = ""
)

class DetalheProdutoViewModel(
    private val produtoRepository: ProdutoRepository,
    private val pedidoRepository: PedidoRepository
) : ViewModel() {
    // Privado: só o ViewModel altera. Público: a tela só lê
    private val _uiState = MutableStateFlow(DetalheProdutoUiState())
    val uiState: StateFlow<DetalheProdutoUiState> = _uiState.asStateFlow()

    private var idCarregado: Int? = null

    fun carregar(id: Int) {
        if (idCarregado == id) return
        idCarregado = id

        viewModelScope.launch {
            val encontrado = try {
                produtoRepository.buscarPorId(id)
            } catch (e: Exception) {
                Log.e("TelaDetalheProduto", "Erro ao buscar produto", e)
                null
            } ?: return@launch

            _uiState.update {
                it.copy(nome = encontrado.nome, descricao = encontrado.descricao, preco = encontrado.preco.toString())
            }

            pedidoRepository.buscarTodos()
                .catch { e -> Log.e("TelaDetalheProduto", "Erro ao buscar pedidos", e) }
                .collect { pedidos ->
                    val vezes = pedidos.sumOf { pedido -> pedido.listaIds().count { it == id } }
                    _uiState.update {
                        it.copy(produto = encontrado, vezesVendido = vezes, faturamento = vezes * encontrado.preco)
                    }
                }
        }
    }

    fun alterarNome(valor: String) = _uiState.update { it.copy(nome = valor) }
    fun alterarDescricao(valor: String) = _uiState.update { it.copy(descricao = valor) }
    fun alterarPreco(valor: String) = _uiState.update { it.copy(preco = valor) }

    fun salvarAlteracoes(aoSalvar: () -> Unit) {
        val estado = _uiState.value
        val p = estado.produto ?: return
        val valor = estado.preco.replace(',', '.').toDoubleOrNull() ?: p.preco
        val editado = p.copy(nome = estado.nome, descricao = estado.descricao, preco = valor)
        viewModelScope.launch {
            try {
                produtoRepository.atualizar(editado)
            } catch (e: Exception) {
                Log.e("TelaDetalheProduto", "Erro ao atualizar produto", e)
                return@launch
            }
            aoSalvar()
        }
    }
}
