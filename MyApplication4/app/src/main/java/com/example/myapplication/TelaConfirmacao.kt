package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TelaConfirmacao(navController: NavController, id: Int) {
    val pedido = pedidos.firstOrNull { it.id == id } ?: return

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Pedido nº ${pedido.id} confirmado!", style = MaterialTheme.typography.titleLarge)
        Text("Cliente: ${pedido.cliente}")
        Text("Total: R$ " + String.format("%.2f", pedido.itens.sumOf { it.preco }), color = MaterialTheme.colorScheme.primary)
        Button(onClick = { navController.navigate(Rotas.CARDAPIO) }, modifier = Modifier.fillMaxWidth()) {
            Text("Voltar ao cardápio")
        }
    }
}
