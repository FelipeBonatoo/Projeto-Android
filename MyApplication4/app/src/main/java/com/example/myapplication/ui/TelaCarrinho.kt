package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.SessaoViewModel

@Composable
fun TelaCarrinho(navController: NavController, sessaoViewModel: SessaoViewModel) {
    val uiState by sessaoViewModel.uiState.collectAsStateWithLifecycle()

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Total: R$ " + String.format("%.2f", uiState.total),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Button(onClick = {
            sessaoViewModel.confirmarPedido { id -> navController.navigate("confirmacao/$id") }
        }, enabled = uiState.podeConfirmar, modifier = Modifier.fillMaxWidth()) { Text("Confirmar pedido") }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(uiState.carrinho) { produto ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("${produto.emoji} ${produto.nome}", style = MaterialTheme.typography.titleMedium)
                        Text("R$ " + String.format("%.2f", produto.preco), color = MaterialTheme.colorScheme.primary)
                        TextButton(onClick = { sessaoViewModel.removerDoCarrinho(produto) }) {
                            Text("Remover", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
