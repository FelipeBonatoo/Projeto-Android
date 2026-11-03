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
import kotlinx.coroutines.withContext

@Composable
fun TelaCarrinho(navController: NavController) {
    val context = LocalContext.current
    val pedidoDao = AppDatabase.getDatabase(context).pedidoDao()

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Total: R$ " + String.format("%.2f", carrinho.sumOf { it.preco }),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        // enabled: só libera com itens no carrinho e com cliente (impede pedido vazio ou sem nome)
        Button(onClick = {
            val novo = Pedido(
                cliente = nomeUsuario,
                idsItens = carrinho.joinToString(",") { it.id.toString() },
                total = carrinho.sumOf { it.preco },
                status = "Em preparo"
            )
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // inserir() devolve o id gerado pelo banco
                    val id = pedidoDao.inserir(novo)
                    // Mexer na tela (limpar o carrinho e navegar) só pode ser feito na
                    // thread principal (Main). Por isso saímos da IO com withContext.
                    withContext(Dispatchers.Main) {
                        carrinho.clear()
                        navController.navigate("confirmacao/$id")
                    }
                } catch (e: Exception) {
                    Log.e("TelaCarrinho", "Erro ao confirmar pedido", e)
                }
            }
        }, enabled = carrinho.isNotEmpty() && nomeUsuario.isNotBlank(), modifier = Modifier.fillMaxWidth()) { Text("Confirmar pedido") }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(carrinho) { produto ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("${produto.emoji} ${produto.nome}", style = MaterialTheme.typography.titleMedium)
                        Text("R$ " + String.format("%.2f", produto.preco), color = MaterialTheme.colorScheme.primary)
                        TextButton(onClick = { carrinho.remove(produto) }) {
                            Text("Remover", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
