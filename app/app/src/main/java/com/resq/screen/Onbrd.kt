package com.resq.screen

import com.resq.*
import com.resq.R
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController

@Composable
fun Onbrd(vm: Vm, nav: NavHostController) {
    var step by rememberSaveable { mutableStateOf(0) }
    Scaf(
        vm, nav, back = false, lng = true, scroll = false,
        onBack = if (step == 1) ({ step = 0 }) else null,
        foot = if (step == 0) ({ Btn(tr(vm, "Get Started")) { step = 1 } }) else null
    ) {
        if (step == 0) Intro(vm) else Roles(vm, nav)
    }
}

@Composable
private fun ColumnScope.Intro(vm: Vm) {
    Column(
        Modifier.fillMaxWidth().weight(1f).padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier.size(130.dp).clip(RoundedCornerShape(40.dp)).background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Image(painterResource(R.drawable.logo), "ResQShare", Modifier.size(104.dp))
        }
        Spacer(Modifier.height(28.dp))
        Text("ResQShare", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
        Spacer(Modifier.height(14.dp))
        Text(
            tr(vm, "Share extra food and essentials with people nearby who need them."),
            fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = Sub,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ColumnScope.Roles(vm: Vm, nav: NavHostController) {
    Column(
        Modifier.fillMaxWidth().weight(1f).padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(tr(vm, "Let's get started"), fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
        Spacer(Modifier.height(6.dp))
        Text(tr(vm, "What would you like to do?"), fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Sub)
        Spacer(Modifier.height(26.dp))
        Big("🙋", BlueL, Blue, tr(vm, "I need help"), tr(vm, "Receive food and essentials nearby")) {
            vm.intent = "NEED"; vm.role = "RECIP"; nav.navigate("reg")
        }
        Spacer(Modifier.height(16.dp))
        Big("🤝", GrnL, GrnD, tr(vm, "I want to give"), tr(vm, "Share your surplus with people nearby")) {
            vm.intent = "GIVE"; vm.role = "DONOR"; nav.navigate("reg")
        }
        Spacer(Modifier.height(26.dp))
        OutBtn(tr(vm, "I already have an account")) { nav.navigate("login") }
    }
}

@Composable
private fun Big(em: String, bg: Color, fg: Color, ttl: String, sub: String, tap: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Card)
            .border(2.dp, Line, RoundedCornerShape(22.dp)).clickable { tap() }.padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(64.dp).clip(RoundedCornerShape(20.dp)).background(bg), contentAlignment = Alignment.Center) {
            Text(em, fontSize = 32.sp)
        }
        Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
            Text(ttl, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
            Text(sub, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Sub)
        }
        Text("›", color = fg, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
    }
}
