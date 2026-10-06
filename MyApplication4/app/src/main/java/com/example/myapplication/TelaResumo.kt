package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TelaResumo() {
    val todosItens = pedidos.flatMap { it.itens }
    val entregues = pedidos.count { it.status == "Entregue" }
    val faturamento = todosItens.sumOf { it.preco }
    val maisVendido = todosItens.maxByOrNull { item -> todosItens.count { it.id == item.id } }

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Resumo do dia", style = MaterialTheme.typography.titleLarge)

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(Modifier.padding(12.dp)) {
                Text("Pedidos", style = MaterialTheme.typography.titleMedium)
                Text("${pedidos.size} no total, $entregues entregue(s)")
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(Modifier.padding(12.dp)) {
                Text("Faturamento", style = MaterialTheme.typography.titleMedium)
                Text("R$ " + String.format("%.2f", faturamento), color = MaterialTheme.colorScheme.primary)
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
