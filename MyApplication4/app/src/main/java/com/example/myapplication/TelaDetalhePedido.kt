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
fun TelaDetalhePedido(id: Int) {
    val context = LocalContext.current
    val pedidoDao = AppDatabase.getDatabase(context).pedidoDao()
    val produtoDao = AppDatabase.getDatabase(context).produtoDao()

    var pedido by remember { mutableStateOf<Pedido?>(null) }
    var produtos by remember { mutableStateOf(listOf<Produto>()) }

    // Busca no banco o pedido com o id da rota, e os produtos para mostrar os itens
    LaunchedEffect(id) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                produtos = produtoDao.buscarTodos()
                pedido = pedidoDao.buscarPorId(id)
            } catch (e: Exception) {
                Log.e("TelaDetalhePedido", "Erro ao buscar pedido", e)
            }
        }
    }

    val p = pedido
    if (p == null) {
        Text("Carregando...", Modifier.padding(16.dp))
        return
    }

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Pedido nº ${p.id}", style = MaterialTheme.typography.titleLarge)
        Text("Cliente: ${p.cliente}")
        Text("Status: ${p.status}")
        // Cada id salvo no pedido é procurado na lista de produtos do banco
        p.listaIds().forEach { idItem ->
            val item = produtos.firstOrNull { it.id == idItem }
            if (item == null) {
                Text("Produto removido")
            } else {
                Text(item.nome + " - R$ " + String.format("%.2f", item.preco))
            }
        }
        Text("Total: R$ " + String.format("%.2f", p.total), color = MaterialTheme.colorScheme.primary)
        Button(onClick = {
            // UPDATE: mesmo pedido (mesmo id), só com o status trocado
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    pedidoDao.atualizar(p.copy(status = "Entregue"))
                    pedido = pedidoDao.buscarPorId(id)
                } catch (e: Exception) {
                    Log.e("TelaDetalhePedido", "Erro ao atualizar pedido", e)
                }
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("Marcar como entregue") }
    }
}
