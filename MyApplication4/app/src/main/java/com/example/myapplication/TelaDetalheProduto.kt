package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TelaDetalheProduto(navController: NavController, id: Int) {
    val produto = produtos.firstOrNull { it.id == id } ?: return

    // COMPLEXIDADE EXTRA: cruza as duas listas para contar quantas vezes
    // este produto aparece nos pedidos e quanto ele ja faturou
    val vezes = pedidos.sumOf { pedido -> pedido.itens.count { it.id == produto.id } }
    val faturamento = vezes * produto.preco

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(produto.emoji, fontSize = 32.sp)
        Text(produto.nome, style = MaterialTheme.typography.titleLarge)
        Text(produto.descricao)
        Text("Categoria: ${produto.categoria}")
        Text("Preço: R$ " + String.format("%.2f", produto.preco), color = MaterialTheme.colorScheme.primary)
        Text("Vendido $vezes vez(es) nos pedidos")
        Text("Faturamento: R$ " + String.format("%.2f", faturamento), color = MaterialTheme.colorScheme.primary)
        Button(onClick = {
            carrinho.add(produto)
            navController.popBackStack()
        }, modifier = Modifier.fillMaxWidth()) { Text("Adicionar ao carrinho") }
    }
}
