package com.example.myapplication.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.myapplication.viewmodel.CafeViewModelFactory
import com.example.myapplication.viewmodel.SessaoViewModel

object Rotas {
    const val LOGIN = "login"
    const val CARDAPIO = "cardapio"
    const val CARRINHO = "carrinho"
    const val PEDIDOS = "pedidos"
    const val RESUMO = "resumo"
    const val DETALHE_PRODUTO = "produto/{id}"
    const val DETALHE_PEDIDO = "pedido/{id}"
    const val CONFIRMACAO = "confirmacao/{id}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(factory: CafeViewModelFactory) {
    val navController = rememberNavController()
    // Criado fora do NavHost para o carrinho ser o mesmo em todas as telas
    val sessaoViewModel: SessaoViewModel = viewModel(factory = factory)
    val entradaAtual by navController.currentBackStackEntryAsState()
    val rotaAtual = entradaAtual?.destination?.route

    Scaffold(
        topBar = {
            if (rotaAtual == Rotas.DETALHE_PRODUTO || rotaAtual == Rotas.DETALHE_PEDIDO) {
                TopAppBar(
                    title = { Text("Detalhes") },
                    navigationIcon = {
                        Button(onClick = { navController.popBackStack() }) { Text("Voltar") }
                    }
                )
            }
        },
        bottomBar = {
            if (rotaAtual != Rotas.LOGIN) {
                NavigationBar {
                    NavigationBarItem(
                        selected = rotaAtual == Rotas.CARDAPIO,
                        onClick = { navController.navigate(Rotas.CARDAPIO) },
                        icon = { Text("☕") },
                        label = { Text("Cardápio") }
                    )
                    NavigationBarItem(
                        selected = rotaAtual == Rotas.CARRINHO,
                        onClick = { navController.navigate(Rotas.CARRINHO) },
                        icon = { Text("🛒") },
                        label = { Text("Carrinho") }
                    )
                    NavigationBarItem(
                        selected = rotaAtual == Rotas.PEDIDOS,
                        onClick = { navController.navigate(Rotas.PEDIDOS) },
                        icon = { Text("📋") },
                        label = { Text("Pedidos") }
                    )
                    NavigationBarItem(
                        selected = rotaAtual == Rotas.RESUMO,
                        onClick = { navController.navigate(Rotas.RESUMO) },
                        icon = { Text("📊") },
                        label = { Text("Resumo") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Rotas.LOGIN, modifier = Modifier.padding(innerPadding)) {
            composable(Rotas.LOGIN) { TelaLogin(navController, sessaoViewModel) }
            composable(Rotas.CARDAPIO) { TelaCardapio(navController, sessaoViewModel, factory) }
            composable(Rotas.CARRINHO) { TelaCarrinho(navController, sessaoViewModel) }
            composable(Rotas.PEDIDOS) { TelaPedidos(navController, factory) }
            composable(Rotas.RESUMO) { TelaResumo(factory) }
            composable(Rotas.DETALHE_PRODUTO, listOf(navArgument("id") { type = NavType.IntType })) { entrada ->
                TelaDetalheProduto(navController, entrada.arguments!!.getInt("id"), sessaoViewModel, factory)
            }
            composable(Rotas.DETALHE_PEDIDO, listOf(navArgument("id") { type = NavType.IntType })) { entrada ->
                TelaDetalhePedido(entrada.arguments!!.getInt("id"), factory)
            }
            composable(Rotas.CONFIRMACAO, listOf(navArgument("id") { type = NavType.IntType })) { entrada ->
                TelaConfirmacao(navController, entrada.arguments!!.getInt("id"), factory)
            }
        }
    }
}
