package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.CafeViewModelFactory
import com.example.myapplication.viewmodel.ResumoViewModel

@Composable
fun TelaResumo(factory: CafeViewModelFactory) {
    val viewModel: ResumoViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val maisVendido = uiState.maisVendido

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Resumo do dia", style = MaterialTheme.typography.titleLarge)

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(Modifier.padding(12.dp)) {
                Text("Pedidos", style = MaterialTheme.typography.titleMedium)
                Text("${uiState.totalPedidos} no total, ${uiState.entregues} entregue(s)")
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(Modifier.padding(12.dp)) {
                Text("Faturamento", style = MaterialTheme.typography.titleMedium)
                Text("R$ " + String.format("%.2f", uiState.faturamento), color = MaterialTheme.colorScheme.primary)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(Modifier.padding(12.dp)) {
                Text("Mais vendido", style = MaterialTheme.typography.titleMedium)
                Text(if (maisVendido == null) "-" else "${maisVendido.emoji} ${maisVendido.nome}")
            }
        }
    }
}
