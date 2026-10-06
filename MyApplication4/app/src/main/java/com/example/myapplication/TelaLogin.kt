package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TelaLogin(navController: NavController) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("CaféPositivo", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = nomeUsuario, onValueChange = { nomeUsuario = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            navController.navigate(Rotas.CARDAPIO) { popUpTo(Rotas.LOGIN) { inclusive = true } }
        }, modifier = Modifier.fillMaxWidth()) { Text("Entrar") }
    }
}
