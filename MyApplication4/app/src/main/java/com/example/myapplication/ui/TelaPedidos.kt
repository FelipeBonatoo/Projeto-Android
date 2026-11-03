package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.CafeViewModelFactory
import com.example.myapplication.viewmodel.PedidosViewModel

@Composable
fun TelaPedidos(navController: NavController, factory: CafeViewModelFactory) {
    val viewModel: PedidosViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text("Pedidos", style = MaterialTheme.typography.titleLarge) }

        items(uiState.pedidos) { pedido ->
            Card(
                onClick = { navController.navigate("pedido/${pedido.id}") },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text("Pedido nº ${pedido.id} - ${pedido.cliente}", style = MaterialTheme.typography.titleMedium)
                    Text(pedido.status)
                    TextButton(onClick = { viewModel.removerPedido(pedido) }) {
                        Text("Remover", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
