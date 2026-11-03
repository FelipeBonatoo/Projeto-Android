package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.PedidoRepository
import com.example.myapplication.data.repository.ProdutoRepository

// Cria os ViewModels passando o repository
class CafeViewModelFactory(
    private val produtoRepository: ProdutoRepository,
    private val pedidoRepository: PedidoRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel: ViewModel = when {
            modelClass.isAssignableFrom(CardapioViewModel::class.java) ->
                CardapioViewModel(produtoRepository)

            modelClass.isAssignableFrom(DetalheProdutoViewModel::class.java) ->
                DetalheProdutoViewModel(produtoRepository, pedidoRepository)

            modelClass.isAssignableFrom(PedidosViewModel::class.java) ->
                PedidosViewModel(pedidoRepository)

            modelClass.isAssignableFrom(DetalhePedidoViewModel::class.java) ->
                DetalhePedidoViewModel(pedidoRepository, produtoRepository)

            modelClass.isAssignableFrom(ResumoViewModel::class.java) ->
                ResumoViewModel(pedidoRepository, produtoRepository)

            modelClass.isAssignableFrom(SessaoViewModel::class.java) ->
                SessaoViewModel(pedidoRepository)

            else -> throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
        }
        return viewModel as T
    }
}
