package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController

@Composable
fun Login(vm: Vm, nav: NavHostController) {
    LaunchedEffect(vm.acct) {
        val a = vm.acct
        if (a != null) {
            val dest = when (a.role) {
                "RECIP" -> "feed"
                "ORG" -> "dash"
                else -> "home"
            }
            nav.navigate(dest) { popUpTo("onbrd") { inclusive = true } }
        }
    }
    Scaf(vm, nav, lng = true, foot = { Btn(tr(vm, "Log in")) { vm.login() } }) {
        Hero(tr(vm, "Welcome back"), tr(vm, "Log in to your account."), gHero)
        Spacer(Modifier.height(16.dp))
        Pane {
            Lbl(tr(vm, "Account"))
            Field(tr(vm, "Email"), vm.email) { vm.email = it }
            Spacer(Modifier.height(12.dp))
            Field(tr(vm, "Password"), vm.pass, pass = true) { vm.pass = it }
        }
        if (vm.err.isNotBlank()) {
            Spacer(Modifier.height(12.dp))
            Text(tr(vm, vm.err), color = Red, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(8.dp))
    }
}
