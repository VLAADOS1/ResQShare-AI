package com.resq.ui

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

val Color0 = Color(0xFFFFFFFF)
val Grn = Color(0xFF58CC02)
val GrnD = Color(0xFF58A700)
val GrnL = Color(0xFFD7FFB8)
val Orng = Color(0xFFFF9600)
val OrngL = Color(0xFFFFEFD4)
val Red = Color(0xFFFF4B4B)
val RedL = Color(0xFFFFDFE0)
val Blue = Color(0xFF1CB0F6)
val BlueL = Color(0xFFDDF4FF)
val Prpl = Color(0xFFCE82FF)
val PrplL = Color(0xFFF3E6FF)
val Bg = Color(0xFFFFFFFF)
val Card = Color(0xFFFFFFFF)
val Ink = Color(0xFF3C3C3C)
val Sub = Color(0xFF888888)
val Line = Color(0xFFE5E5E5)

val gHero: Brush = SolidColor(Grn)
val gWarm: Brush = SolidColor(Orng)
val gAi: Brush = SolidColor(Prpl)

fun dk(c: Color): Color = Color(c.red * 0.74f, c.green * 0.74f, c.blue * 0.74f, c.alpha)

fun ctint(c: String): Color = when (c) {
    "FOOD" -> GrnL
    "CLOTH" -> BlueL
    "HYG" -> PrplL
    "BABY" -> OrngL
    "SCHOOL" -> BlueL
    else -> Line
}
