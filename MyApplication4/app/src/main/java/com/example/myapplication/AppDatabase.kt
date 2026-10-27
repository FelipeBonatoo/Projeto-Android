package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// @Database: diz quais tabelas (entities) existem no banco e a versão dele
@Database(entities = [Produto::class, Pedido::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // O Room implementa essas funções e devolve os DAOs prontos
    abstract fun produtoDao(): ProdutoDao
    abstract fun pedidoDao(): PedidoDao

    // Singleton: o app inteiro usa uma única instância do banco
    companion object {
        // @Volatile: toda thread enxerga o valor mais atual de INSTANCE
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Se já existe, devolve. Se não, cria uma vez só.
            // synchronized: impede duas threads de criarem o banco ao mesmo tempo
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cafe_database" // nome do arquivo do banco no celular
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
