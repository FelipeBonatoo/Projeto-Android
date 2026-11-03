package com.example.myapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = CafeClaro,
    onPrimary = OnCafeClaro,
    primaryContainer = CafeContainer,
    onPrimaryContainer = Leite,
    secondary = CarameloClaro,
    onSecondary = OnCafeClaro,
    background = FundoEscuro,
    onBackground = TextoClaro,
    surface = SuperficieEscura,
    onSurface = TextoClaro,
    surfaceVariant = CardEscuro,
    onSurfaceVariant = TextoCardEscuro,
    outline = ContornoEscuro,
    error = ErroClaro,
    secondaryContainer = CafeContainer,
    onSecondaryContainer = Leite,
    surfaceContainer = BarraEscura
)

private val LightColorScheme = lightColorScheme(
    primary = Cafe,
    onPrimary = Branco,
    primaryContainer = Leite,
    onPrimaryContainer = CafeEscuro,
    secondary = Caramelo,
    onSecondary = Branco,
    background = Creme,
    onBackground = Texto,
    surface = Branco,
    onSurface = Texto,
    surfaceVariant = CremeCard,
    onSurfaceVariant = TextoCard,
    outline = Contorno,
    error = Erro,
    secondaryContainer = Leite,
    onSecondaryContainer = CafeEscuro,
    surfaceContainer = Barra
)

// dynamicColor foi removido de proposito: no Android 12+ ele trocava a nossa
// paleta marrom pelas cores do papel de parede do celular.
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
