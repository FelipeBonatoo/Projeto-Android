package com.example.myapplication

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TelaResumo() {
    val context = LocalContext.current
    val pedidoDao = AppDatabase.getDatabase(context).pedidoDao()
    val produtoDao = AppDatabase.getDatabase(context).produtoDao()

    var pedidos by remember { mutableStateOf(listOf<Pedido>()) }
    var produtos by remember { mutableStateOf(listOf<Produto>()) }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                pedidos = pedidoDao.buscarTodos()
                produtos = produtoDao.buscarTodos()
            } catch (e: Exception) {
                Log.e("TelaResumo", "Erro ao buscar dados", e)
            }
        }
    }

    // Junta os ids de todos os pedidos numa lista só, ex.: [1, 3, 3, 2, 3]
    val todosIds = pedidos.flatMap { it.listaIds() }
    val entregues = pedidos.count { it.status == "Entregue" }
    val faturamento = pedidos.sumOf { it.total }
    // O id que mais se repete é o do produto mais vendido
    val idMaisVendido = todosIds.maxByOrNull { id -> todosIds.count { it == id } }
    val maisVendido = produtos.firstOrNull { it.id == idMaisVendido }

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
