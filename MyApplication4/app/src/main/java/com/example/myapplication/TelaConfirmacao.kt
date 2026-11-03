package com.example.myapplication

import android.util.Log
import androidx.compose.foundation.layout.*
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
fun TelaConfirmacao(navController: NavController, id: Int) {
    val context = LocalContext.current
    val pedidoDao = AppDatabase.getDatabase(context).pedidoDao()
    var pedido by remember { mutableStateOf<Pedido?>(null) }

    // Busca no banco o pedido que acabou de ser criado
    LaunchedEffect(id) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                pedido = pedidoDao.buscarPorId(id)
            } catch (e: Exception) {
                Log.e("TelaConfirmacao", "Erro ao buscar pedido", e)
            }
        }
    }

    val p = pedido
    if (p == null) {
        Text("Carregando...", Modifier.padding(16.dp))
        return
    }

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Pedido nº ${p.id} confirmado!", style = MaterialTheme.typography.titleLarge)
        Text("Cliente: ${p.cliente}")
        Text("Total: R$ " + String.format("%.2f", p.total), color = MaterialTheme.colorScheme.primary)
        Button(onClick = {
            navController.navigate(Rotas.CARDAPIO) { popUpTo(Rotas.CARDAPIO) { inclusive = true } }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Voltar ao cardápio")
        }
    }
}
