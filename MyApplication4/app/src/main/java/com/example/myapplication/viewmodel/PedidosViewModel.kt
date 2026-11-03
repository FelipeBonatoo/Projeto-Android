package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Pedido
import com.example.myapplication.data.repository.PedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado da tela
data class PedidosUiState(
    val pedidos: List<Pedido> = emptyList()
)

class PedidosViewModel(private val pedidoRepository: PedidoRepository) : ViewModel() {
    // Privado: só o ViewModel altera. Público: a tela só lê
    private val _uiState = MutableStateFlow(PedidosUiState())
    val uiState: StateFlow<PedidosUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            pedidoRepository.buscarTodos()
                .catch { e -> Log.e("TelaPedidos", "Erro ao buscar pedidos", e) }
                .collect { lista ->
                    _uiState.update { it.copy(pedidos = lista) }
                }
        }
    }

    fun removerPedido(pedido: Pedido) {
        viewModelScope.launch {
            try {
                pedidoRepository.deletar(pedido)
            } catch (e: Exception) {
                Log.e("TelaPedidos", "Erro ao deletar pedido", e)
            }
        }
    }
}
