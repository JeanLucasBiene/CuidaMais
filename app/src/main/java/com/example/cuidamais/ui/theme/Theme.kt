package com.example.cuidamais.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


// paleta propria do app, verde azulado em vez do roxo padrao
private val esquemaCores = lightColorScheme(
    primary = Color(0xFF2E7D6B),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD6EFE7),
    onPrimaryContainer = Color(0xFF0F3D33),
    background = Color(0xFFF6FAF8),
    surface = Color.White,
    surfaceVariant = Color(0xFFE9F1EE)
)


private val formas = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)


@Composable
fun CuidaMaisTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = esquemaCores,
        shapes = formas,
        content = content
    )
}