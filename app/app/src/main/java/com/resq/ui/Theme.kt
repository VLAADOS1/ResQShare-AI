package com.resq.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.dp
import com.resq.R

val Round = FontFamily(
    Font(R.font.nunito, FontWeight.Normal, variationSettings = FontVariation.Settings(FontVariation.weight(450))),
    Font(R.font.nunito, FontWeight.Medium, variationSettings = FontVariation.Settings(FontVariation.weight(600))),
    Font(R.font.nunito, FontWeight.SemiBold, variationSettings = FontVariation.Settings(FontVariation.weight(700))),
    Font(R.font.nunito, FontWeight.Bold, variationSettings = FontVariation.Settings(FontVariation.weight(800))),
    Font(R.font.nunito, FontWeight.ExtraBold, variationSettings = FontVariation.Settings(FontVariation.weight(900)))
)

private val scheme = lightColorScheme(
    primary = Grn,
    onPrimary = Color0,
    secondary = Orng,
    background = Bg,
    surface = Card,
    onBackground = Ink,
    onSurface = Ink,
    error = Red
)

private val shapes = Shapes(
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(22.dp)
)

@Composable
fun Theme(body: @Composable () -> Unit) {
    MaterialTheme(colorScheme = scheme, shapes = shapes) {
        CompositionLocalProvider(
            LocalTextStyle provides TextStyle(fontFamily = Round, color = Ink),
            content = body
        )
    }
}
