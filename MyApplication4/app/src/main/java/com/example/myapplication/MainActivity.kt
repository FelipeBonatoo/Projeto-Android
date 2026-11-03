package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.repository.PedidoRepository
import com.example.myapplication.data.repository.ProdutoRepository
import com.example.myapplication.ui.AppNavigation
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.CafeViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Banco -> Repository -> Factory
        val database = AppDatabase.getDatabase(applicationContext)
        val factory = CafeViewModelFactory(
            produtoRepository = ProdutoRepository(database.produtoDao()),
            pedidoRepository = PedidoRepository(database.pedidoDao())
        )

        setContent {
            MyApplicationTheme {
                AppNavigation(factory)
            }
        }
    }
}
