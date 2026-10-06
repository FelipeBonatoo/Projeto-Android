package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TelaDetalhePedido(id: Int) {
    val pedido = pedidos.firstOrNull { it.id == id } ?: return

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Pedido nº ${pedido.id}", style = MaterialTheme.typography.titleLarge)
        Text("Cliente: ${pedido.cliente}")
        Text("Status: ${pedido.status}")
        pedido.itens.forEach { item ->
            Text(item.nome + " - R$ " + String.format("%.2f", item.preco))
        }
        Text("Total: R$ " + String.format("%.2f", pedido.itens.sumOf { it.preco }), color = MaterialTheme.colorScheme.primary)
        Button(onClick = {
            val i = pedidos.indexOfFirst { it.id == id }
            pedidos[i] = pedidos[i].copy(status = "Entregue")
        }, modifier = Modifier.fillMaxWidth()) { Text("Marcar como entregue") }
    }
}
