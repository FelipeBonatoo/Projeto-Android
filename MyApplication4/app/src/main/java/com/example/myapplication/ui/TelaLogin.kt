package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.myapplication.viewmodel.SessaoViewModel

@Composable
fun TelaLogin(navController: NavController, sessaoViewModel: SessaoViewModel) {
    val uiState by sessaoViewModel.uiState.collectAsStateWithLifecycle()

    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("CaféPositivo", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = uiState.nomeUsuario, onValueChange = sessaoViewModel::alterarNome, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            navController.navigate(Rotas.CARDAPIO) { popUpTo(Rotas.LOGIN) { inclusive = true } }
        }, enabled = uiState.podeEntrar, modifier = Modifier.fillMaxWidth()) { Text("Entrar") }
    }
}
