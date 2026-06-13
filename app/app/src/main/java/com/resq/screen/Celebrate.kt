package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController

@Composable
fun Celebrate(vm: Vm, nav: NavHostController) {
    val g = vm.lastGain
    var shown by remember { mutableStateOf(false) }
    LaunchedEffect(g) { if (g != null) shown = true }
    val scale by animateFloatAsState(
        if (shown) 1f else 0.4f,
        spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow), label = "scale"
    )
    val alpha by animateFloatAsState(if (shown) 1f else 0f, tween(500), label = "alpha")
    val rise by animateDpAsState(if (shown) 0.dp else 24.dp, tween(500), label = "rise")
    Scaf(
        vm, nav, back = false,
        foot = { Btn(tr(vm, "Done")) { nav.navigate("home") { popUpTo("home") } } }
    ) {
        if (vm.busy && g == null) {
            Spacer(Modifier.height(60.dp))
            Skel()
            return@Scaf
        }
        Spacer(Modifier.height(40.dp))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Box(
                Modifier.size(130.dp).scale(scale).clip(CircleShape).background(GrnL),
                contentAlignment = Alignment.Center
            ) { Text("🎉", fontSize = 64.sp) }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            tr(vm, "Thank you for sharing!"), color = Ink, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().alpha(alpha)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            tr(vm, "Here is the impact you just made:"), color = Sub, fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().alpha(alpha)
        )
        Spacer(Modifier.height(20.dp))
        if (g != null) {
            Box(Modifier.alpha(alpha).offset(y = rise)) {
                Pane {
                    if (g.meals > 0) Gain("🍽️", "+${g.meals}", tr(vm, "meals provided"))
                    Gain("♻️", "+${num(g.kg)} kg", tr(vm, "kg rescued"))
                    Gain("🌍", "+${num(g.co2)} kg", tr(vm, "kg CO2 avoided"))
                    Gain("🤝", "+${g.people}", tr(vm, "people helped"))
                }
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun Gain(emoji: String, value: String, label: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(emoji, fontSize = 24.sp)
        Text("  $value", color = GrnD, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(start = 8.dp))
        Spacer(Modifier.weight(1f))
        Text(label, color = Sub, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

private fun num(v: Double): String = if (v % 1.0 == 0.0) v.toInt().toString() else String.format("%.1f", v)
