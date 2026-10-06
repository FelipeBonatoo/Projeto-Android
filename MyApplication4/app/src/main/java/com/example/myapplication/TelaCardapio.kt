package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TelaCardapio(navController: NavController) {
    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("☕") }
    var categoria by remember { mutableStateOf("Cafés") }
    var menuEmojiAberto by remember { mutableStateOf(false) }
    var menuAberto by remember { mutableStateOf(false) }

    LazyColumn(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    nomeUsuario = ""
                    carrinho.clear()
                    navController.navigate(Rotas.LOGIN) { popUpTo(navController.graph.id) { inclusive = true } }
                }) { Text("Sair") }

                Text("Novo produto", style = MaterialTheme.typography.titleLarge)
                OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = preco, onValueChange = { preco = it }, label = { Text("Preço") }, modifier = Modifier.fillMaxWidth())

                Box {
                    Button(onClick = { menuEmojiAberto = true }) { Text("Emoji: $emoji") }
                    DropdownMenu(expanded = menuEmojiAberto, onDismissRequest = { menuEmojiAberto = false }) {
                        listOf("☕", "🥛", "🧀", "🍫", "🥐", "🍵").forEach { opcao ->
                            DropdownMenuItem(text = { Text(opcao) }, onClick = { emoji = opcao; menuEmojiAberto = false })
                        }
                    }
                }

                Box {
                    Button(onClick = { menuAberto = true }) { Text("Categoria: $categoria") }
                    DropdownMenu(expanded = menuAberto, onDismissRequest = { menuAberto = false }) {
                        listOf("Cafés", "Chás", "Salgados", "Doces").forEach { opcao ->
                            DropdownMenuItem(text = { Text(opcao) }, onClick = { categoria = opcao; menuAberto = false })
                        }
                    }
                }

                Button(onClick = {
                    val valor = preco.replace(',', '.').toDoubleOrNull() ?: 0.0
                    produtos.add(Produto(proximoIdProduto, emoji, nome, descricao, valor, categoria))
                    proximoIdProduto++
                    nome = ""
                    descricao = ""
                    preco = ""
                }, modifier = Modifier.fillMaxWidth()) { Text("Adicionar produto") }
            }
        }

        items(produtos.chunked(3)) { linha ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                linha.forEach { produto ->
                    Card(
                        onClick = { navController.navigate("produto/${produto.id}") },
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(Modifier.fillMaxWidth().padding(bottom = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            TextButton(onClick = { produtos.remove(produto) }, modifier = Modifier.align(Alignment.End)) {
                                Text("✕", color = MaterialTheme.colorScheme.error)
                            }
                            Text(produto.emoji, fontSize = 32.sp, modifier = Modifier.padding(top = 8.dp))
                            Text(produto.nome, style = MaterialTheme.typography.titleMedium)
                            Text("R$ " + String.format("%.2f", produto.preco), color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}
