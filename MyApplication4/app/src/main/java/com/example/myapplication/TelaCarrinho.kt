package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TelaCarrinho(navController: NavController) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Total: R$ " + String.format("%.2f", carrinho.sumOf { it.preco }),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Button(onClick = {
            val id = proximoIdPedido
            proximoIdPedido++
            pedidos.add(Pedido(id, nomeUsuario, carrinho.toList(), "Em preparo"))
            carrinho.clear()
            navController.navigate("confirmacao/$id")
        }, modifier = Modifier.fillMaxWidth()) { Text("Confirmar pedido") }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(carrinho) { produto ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("${produto.emoji} ${produto.nome}", style = MaterialTheme.typography.titleMedium)
                        Text("R$ " + String.format("%.2f", produto.preco), color = MaterialTheme.colorScheme.primary)
                        TextButton(onClick = { carrinho.remove(produto) }) {
                            Text("Remover", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
