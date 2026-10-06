package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TelaPedidos(navController: NavController) {
    var cliente by remember { mutableStateOf("") }
    val marcados = remember { mutableStateListOf<Produto>() }

    LazyColumn(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Novo pedido", style = MaterialTheme.typography.titleLarge)
                OutlinedTextField(value = cliente, onValueChange = { cliente = it }, label = { Text("Cliente") }, modifier = Modifier.fillMaxWidth())
                produtos.forEach { produto ->
                    Row {
                        Checkbox(checked = produto in marcados, onCheckedChange = { marcado ->
                            if (marcado) marcados.add(produto) else marcados.remove(produto)
                        })
                        Text(produto.nome)
                    }
                }
                Button(onClick = {
                    pedidos.add(Pedido(proximoIdPedido, cliente, marcados.toList(), "Em preparo"))
                    proximoIdPedido++
                    cliente = ""
                    marcados.clear()
                }, modifier = Modifier.fillMaxWidth()) { Text("Adicionar pedido") }
            }
        }

        items(pedidos) { pedido ->
            Card(
                onClick = { navController.navigate("pedido/${pedido.id}") },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text("Pedido nº ${pedido.id} - ${pedido.cliente}", style = MaterialTheme.typography.titleMedium)
                    Text(pedido.status)
                    TextButton(onClick = { pedidos.remove(pedido) }) {
                        Text("Remover", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
