package com.example.myapplication

import androidx.compose.runtime.*

// Produtos e pedidos agora ficam no banco (Room). Aqui só sobra o que é temporário:
// o carrinho (ainda não virou pedido) e o nome de quem entrou no app.

val carrinho = mutableStateListOf<Produto>()

var nomeUsuario by mutableStateOf("")
