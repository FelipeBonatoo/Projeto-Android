package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.CafeViewModelFactory
import com.example.myapplication.viewmodel.CardapioViewModel
import com.example.myapplication.viewmodel.SessaoViewModel

@Composable
fun TelaCardapio(
    navController: NavController,
    sessaoViewModel: SessaoViewModel,
    factory: CafeViewModelFactory
) {
    val viewModel: CardapioViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var menuEmojiAberto by remember { mutableStateOf(false) }
    var menuAberto by remember { mutableStateOf(false) }

    LazyColumn(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    sessaoViewModel.sair()
                    navController.navigate(Rotas.LOGIN) { popUpTo(navController.graph.id) { inclusive = true } }
                }) { Text("Sair") }

                Text("Novo produto", style = MaterialTheme.typography.titleLarge)
                OutlinedTextField(value = uiState.nome, onValueChange = viewModel::alterarNome, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = uiState.descricao, onValueChange = viewModel::alterarDescricao, label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = uiState.preco, onValueChange = viewModel::alterarPreco, label = { Text("Preço") }, modifier = Modifier.fillMaxWidth())

                Box {
                    Button(onClick = { menuEmojiAberto = true }) { Text("Emoji: ${uiState.emoji}") }
                    DropdownMenu(expanded = menuEmojiAberto, onDismissRequest = { menuEmojiAberto = false }) {
                        listOf("☕", "🥛", "🧀", "🍫", "🥐", "🍵").forEach { opcao ->
                            DropdownMenuItem(text = { Text(opcao) }, onClick = { viewModel.alterarEmoji(opcao); menuEmojiAberto = false })
                        }
                    }
                }

                Box {
                    Button(onClick = { menuAberto = true }) { Text("Categoria: ${uiState.categoria}") }
                    DropdownMenu(expanded = menuAberto, onDismissRequest = { menuAberto = false }) {
                        listOf("Cafés", "Chás", "Salgados", "Doces").forEach { opcao ->
                            DropdownMenuItem(text = { Text(opcao) }, onClick = { viewModel.alterarCategoria(opcao); menuAberto = false })
                        }
                    }
                }

                Button(onClick = { viewModel.adicionarProduto() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Adicionar produto")
                }
            }
        }

        items(uiState.produtos.chunked(3)) { linha ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                linha.forEach { produto ->
                    Card(
                        onClick = { navController.navigate("produto/${produto.id}") },
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(Modifier.fillMaxWidth().padding(bottom = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            TextButton(onClick = { viewModel.removerProduto(produto) }, modifier = Modifier.align(Alignment.End)) {
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
