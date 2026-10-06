package com.example.myapplication

import androidx.compose.runtime.*

data class Produto(val id: Int, val emoji: String, val nome: String, val descricao: String, val preco: Double, val categoria: String)

data class Pedido(val id: Int, val cliente: String, val itens: List<Produto>, val status: String)

val produtos = mutableStateListOf(
    Produto(1, "☕", "Espresso", "Curto e encorpado", 6.0, "Cafés"),
    Produto(2, "🥛", "Latte", "Café com bastante leite vaporizado", 10.0, "Cafés"),
    Produto(3, "🧀", "Pão de queijo", "Porção com 3 unidades", 7.5, "Salgados"),
    Produto(4, "🍫", "Mocha", "Café com chocolate e leite", 12.0, "Cafés"),
    Produto(5, "🥐", "Croissant", "Massa folhada amanteigada", 9.0, "Salgados"),
    Produto(6, "🍵", "Chá", "Chá verde ou de camomila", 5.0, "Chás")
)

val pedidos = mutableStateListOf(
    Pedido(1, "Ana", listOf(produtos[0], produtos[2]), "Em preparo")
)

val carrinho = mutableStateListOf<Produto>()

var nomeUsuario by mutableStateOf("")

var proximoIdProduto = 7
var proximoIdPedido = 2
