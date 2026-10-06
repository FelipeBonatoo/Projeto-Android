package com.example.myapplication

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

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
fun AppNavigation() {
    val navController = rememberNavController()
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
            composable(Rotas.LOGIN) { TelaLogin(navController) }
            composable(Rotas.CARDAPIO) { TelaCardapio(navController) }
            composable(Rotas.CARRINHO) { TelaCarrinho(navController) }
            composable(Rotas.PEDIDOS) { TelaPedidos(navController) }
            composable(Rotas.RESUMO) { TelaResumo() }
            composable(Rotas.DETALHE_PRODUTO, listOf(navArgument("id") { type = NavType.IntType })) { entrada ->
                TelaDetalheProduto(navController, entrada.arguments!!.getInt("id"))
            }
            composable(Rotas.DETALHE_PEDIDO, listOf(navArgument("id") { type = NavType.IntType })) { entrada ->
                TelaDetalhePedido(entrada.arguments!!.getInt("id"))
            }
            composable(Rotas.CONFIRMACAO, listOf(navArgument("id") { type = NavType.IntType })) { entrada ->
                TelaConfirmacao(navController, entrada.arguments!!.getInt("id"))
            }
        }
    }
}
