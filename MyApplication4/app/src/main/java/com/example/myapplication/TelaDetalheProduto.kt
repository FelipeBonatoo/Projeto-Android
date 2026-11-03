package com.example.myapplication

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun TelaDetalheProduto(navController: NavController, id: Int) {
    val context = LocalContext.current
    val produtoDao = AppDatabase.getDatabase(context).produtoDao()
    val pedidoDao = AppDatabase.getDatabase(context).pedidoDao()

    // Começa null: enquanto o banco não responde, a tela mostra "Carregando..."
    var produto by remember { mutableStateOf<Produto?>(null) }
    var pedidos by remember { mutableStateOf(listOf<Pedido>()) }

    // Campos da edição (Update), preenchidos com os dados que vierem do banco
    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }

    // LaunchedEffect(id): busca no banco o produto com o id que veio na navegação
    LaunchedEffect(id) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val encontrado = produtoDao.buscarPorId(id)
                pedidos = pedidoDao.buscarTodos()
                if (encontrado != null) {
                    nome = encontrado.nome
                    descricao = encontrado.descricao
                    preco = encontrado.preco.toString()
                }
                produto = encontrado
            } catch (e: Exception) {
                Log.e("TelaDetalheProduto", "Erro ao buscar produto", e)
            }
        }
    }

    val p = produto
    if (p == null) {
        Text("Carregando...", Modifier.padding(16.dp))
        return
    }

    // COMPLEXIDADE EXTRA: cruza produtos e pedidos para contar quantas vezes
    // este produto aparece nos pedidos e quanto ele ja faturou
    val vezes = pedidos.sumOf { pedido -> pedido.listaIds().count { it == p.id } }
    val faturamento = vezes * p.preco

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(p.emoji, fontSize = 32.sp)
        Text(p.nome, style = MaterialTheme.typography.titleLarge)
        Text(p.descricao)
        Text("Categoria: ${p.categoria}")
        Text("Preço: R$ " + String.format("%.2f", p.preco), color = MaterialTheme.colorScheme.primary)
        Text("Vendido $vezes vez(es) nos pedidos")
        Text("Faturamento: R$ " + String.format("%.2f", faturamento), color = MaterialTheme.colorScheme.primary)
        Button(onClick = {
            carrinho.add(p)
            navController.popBackStack()
        }, modifier = Modifier.fillMaxWidth()) { Text("Adicionar ao carrinho") }

        Text("Editar produto", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = descricao, onValueChange = { descricao = it }, label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = preco, onValueChange = { preco = it }, label = { Text("Preço") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            val valor = preco.replace(',', '.').toDoubleOrNull() ?: p.preco
            // copy() mantém o mesmo id, então o @Update sabe qual linha alterar
            val editado = p.copy(nome = nome, descricao = descricao, preco = valor)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    produtoDao.atualizar(editado)
                    // Navegação só pode rodar na thread principal (Main), não na IO
                    withContext(Dispatchers.Main) { navController.popBackStack() }
                } catch (e: Exception) {
                    Log.e("TelaDetalheProduto", "Erro ao atualizar produto", e)
                }
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("Salvar alterações") }
    }
}
