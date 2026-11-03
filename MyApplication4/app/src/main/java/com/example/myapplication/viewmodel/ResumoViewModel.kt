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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

// Estado da tela
data class ResumoUiState(
    val totalPedidos: Int = 0,
    val entregues: Int = 0,
    val faturamento: Double = 0.0,
    val maisVendido: Produto? = null
)

class ResumoViewModel(
    private val pedidoRepository: PedidoRepository,
    private val produtoRepository: ProdutoRepository
) : ViewModel() {
    // Privado: só o ViewModel altera. Público: a tela só lê
    private val _uiState = MutableStateFlow(ResumoUiState())
    val uiState: StateFlow<ResumoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(pedidoRepository.buscarTodos(), produtoRepository.buscarTodos()) { pedidos, produtos ->
                val todosIds = pedidos.flatMap { it.listaIds() }
                val idMaisVendido = todosIds.maxByOrNull { id -> todosIds.count { it == id } }
                ResumoUiState(
                    totalPedidos = pedidos.size,
                    entregues = pedidos.count { it.status == "Entregue" },
                    faturamento = pedidos.sumOf { it.total },
                    maisVendido = produtos.firstOrNull { it.id == idMaisVendido }
                )
            }
                .catch { e -> Log.e("TelaResumo", "Erro ao buscar dados", e) }
                .collect { novoEstado -> _uiState.value = novoEstado }
        }
    }
}
