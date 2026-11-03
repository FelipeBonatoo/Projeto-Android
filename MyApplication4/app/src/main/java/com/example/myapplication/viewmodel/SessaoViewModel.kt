package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Pedido
import com.example.myapplication.data.Produto
import com.example.myapplication.data.repository.PedidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Nome do usuário e carrinho (antes ficavam no Dados.kt)
data class SessaoUiState(
    val nomeUsuario: String = "",
    val carrinho: List<Produto> = emptyList()
) {
    val total: Double get() = carrinho.sumOf { it.preco }
    val podeEntrar: Boolean get() = nomeUsuario.trim().length >= 3
    val podeConfirmar: Boolean get() = carrinho.isNotEmpty() && nomeUsuario.isNotBlank()
}

class SessaoViewModel(private val pedidoRepository: PedidoRepository) : ViewModel() {
    // Privado: só o ViewModel altera. Público: a tela só lê
    private val _uiState = MutableStateFlow(SessaoUiState())
    val uiState: StateFlow<SessaoUiState> = _uiState.asStateFlow()

    fun alterarNome(valor: String) = _uiState.update { it.copy(nomeUsuario = valor) }

    fun adicionarAoCarrinho(produto: Produto) =
        _uiState.update { it.copy(carrinho = it.carrinho + produto) }

    fun removerDoCarrinho(produto: Produto) =
        _uiState.update { it.copy(carrinho = it.carrinho - produto) }

    fun sair() = _uiState.update { SessaoUiState() }

    fun confirmarPedido(aoConfirmar: (Int) -> Unit) {
        val estado = _uiState.value
        val novo = Pedido(
            cliente = estado.nomeUsuario,
            idsItens = estado.carrinho.joinToString(",") { it.id.toString() },
            total = estado.total,
            status = "Em preparo"
        )
        viewModelScope.launch {
            val id = try {
                pedidoRepository.inserir(novo)
            } catch (e: Exception) {
                Log.e("TelaCarrinho", "Erro ao confirmar pedido", e)
                return@launch
            }
            _uiState.update { it.copy(carrinho = emptyList()) }
            aoConfirmar(id.toInt())
        }
    }
}
