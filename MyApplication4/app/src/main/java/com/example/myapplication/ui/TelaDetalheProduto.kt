package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.CafeViewModelFactory
import com.example.myapplication.viewmodel.DetalheProdutoViewModel
import com.example.myapplication.viewmodel.SessaoViewModel

@Composable
fun TelaDetalheProduto(
    navController: NavController,
    id: Int,
    sessaoViewModel: SessaoViewModel,
    factory: CafeViewModelFactory
) {
    val viewModel: DetalheProdutoViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(id) { viewModel.carregar(id) }

    val p = uiState.produto
    if (p == null) {
        Text("Carregando...", Modifier.padding(16.dp))
        return
    }

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(p.emoji, fontSize = 32.sp)
        Text(p.nome, style = MaterialTheme.typography.titleLarge)
        Text(p.descricao)
        Text("Categoria: ${p.categoria}")
        Text("Preço: R$ " + String.format("%.2f", p.preco), color = MaterialTheme.colorScheme.primary)
        Text("Vendido ${uiState.vezesVendido} vez(es) nos pedidos")
        Text("Faturamento: R$ " + String.format("%.2f", uiState.faturamento), color = MaterialTheme.colorScheme.primary)
        Button(onClick = {
            sessaoViewModel.adicionarAoCarrinho(p)
            navController.popBackStack()
        }, modifier = Modifier.fillMaxWidth()) { Text("Adicionar ao carrinho") }

        Text("Editar produto", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(value = uiState.nome, onValueChange = viewModel::alterarNome, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = uiState.descricao, onValueChange = viewModel::alterarDescricao, label = { Text("Descrição") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = uiState.preco, onValueChange = viewModel::alterarPreco, label = { Text("Preço") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            viewModel.salvarAlteracoes { navController.popBackStack() }
        }, modifier = Modifier.fillMaxWidth()) { Text("Salvar alterações") }
    }
}
