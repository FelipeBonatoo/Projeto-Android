package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.CafeViewModelFactory
import com.example.myapplication.viewmodel.DetalhePedidoViewModel

@Composable
fun TelaDetalhePedido(id: Int, factory: CafeViewModelFactory) {
    val viewModel: DetalhePedidoViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(id) { viewModel.carregar(id) }

    val p = uiState.pedido
    if (p == null) {
        Text("Carregando...", Modifier.padding(16.dp))
        return
    }

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Pedido nº ${p.id}", style = MaterialTheme.typography.titleLarge)
        Text("Cliente: ${p.cliente}")
        Text("Status: ${p.status}")
        uiState.itens.forEach { item ->
            if (item == null) {
                Text("Produto removido")
            } else {
                Text(item.nome + " - R$ " + String.format("%.2f", item.preco))
            }
        }
        Text("Total: R$ " + String.format("%.2f", p.total), color = MaterialTheme.colorScheme.primary)
        Button(onClick = { viewModel.marcarComoEntregue() }, modifier = Modifier.fillMaxWidth()) {
            Text("Marcar como entregue")
        }
    }
}
