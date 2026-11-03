package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Pedido
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
data class DetalhePedidoUiState(
    val pedido: Pedido? = null,
    // null = produto removido
    val itens: List<Produto?> = emptyList()
)

// Usado também na tela de Confirmação
class DetalhePedidoViewModel(
    private val pedidoRepository: PedidoRepository,
    private val produtoRepository: ProdutoRepository
) : ViewModel() {
    // Privado: só o ViewModel altera. Público: a tela só lê
    private val _uiState = MutableStateFlow(DetalhePedidoUiState())
    val uiState: StateFlow<DetalhePedidoUiState> = _uiState.asStateFlow()

    private var idCarregado: Int? = null

    fun carregar(id: Int) {
        if (idCarregado == id) return
        idCarregado = id

        viewModelScope.launch {
            val pedido = try {
                pedidoRepository.buscarPorId(id)
            } catch (e: Exception) {
                Log.e("TelaDetalhePedido", "Erro ao buscar pedido", e)
                null
            } ?: return@launch

            produtoRepository.buscarTodos()
                .catch { e -> Log.e("TelaDetalhePedido", "Erro ao buscar produtos", e) }
                .collect { produtos ->
                    _uiState.update { estado ->
                        val atual = estado.pedido ?: pedido
                        estado.copy(
                            pedido = atual,
                            itens = atual.listaIds().map { idItem -> produtos.firstOrNull { it.id == idItem } }
                        )
                    }
                }
        }
    }

    fun marcarComoEntregue() {
        val p = _uiState.value.pedido ?: return
        viewModelScope.launch {
            try {
                pedidoRepository.atualizar(p.copy(status = "Entregue"))
                val atualizado = pedidoRepository.buscarPorId(p.id)
                _uiState.update { it.copy(pedido = atualizado) }
            } catch (e: Exception) {
                Log.e("TelaDetalhePedido", "Erro ao atualizar pedido", e)
            }
        }
    }
}
