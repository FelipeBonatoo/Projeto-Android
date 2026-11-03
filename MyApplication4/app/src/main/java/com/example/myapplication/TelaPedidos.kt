package com.example.myapplication

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TelaPedidos(navController: NavController) {
    val context = LocalContext.current
    val pedidoDao = AppDatabase.getDatabase(context).pedidoDao()

    var pedidos by remember { mutableStateOf(listOf<Pedido>()) }

    // Ao abrir a tela: busca os pedidos no banco para mostrar a lista.
    // Pedido novo só nasce pelo Carrinho; aqui a aba só lista, abre o detalhe e remove.
    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                pedidos = pedidoDao.buscarTodos()
            } catch (e: Exception) {
                Log.e("TelaPedidos", "Erro ao buscar pedidos", e)
            }
        }
    }

    LazyColumn(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text("Pedidos", style = MaterialTheme.typography.titleLarge) }

        items(pedidos) { pedido ->
            Card(
                onClick = { navController.navigate("pedido/${pedido.id}") },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text("Pedido nº ${pedido.id} - ${pedido.cliente}", style = MaterialTheme.typography.titleMedium)
                    Text(pedido.status)
                    TextButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                pedidoDao.deletar(pedido)
                                pedidos = pedidoDao.buscarTodos()
                            } catch (e: Exception) {
                                Log.e("TelaPedidos", "Erro ao deletar pedido", e)
                            }
                        }
                    }) {
                        Text("Remover", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
