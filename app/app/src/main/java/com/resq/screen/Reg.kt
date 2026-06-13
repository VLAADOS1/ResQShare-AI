package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController

@Composable
fun Reg(vm: Vm, nav: NavHostController) {
    var step by rememberSaveable { mutableStateOf(0) }
    LaunchedEffect(vm.acct) {
        if (vm.acct != null) {
            val dest = when (vm.role) {
                "RECIP" -> "feed"
                "ORG" -> "dash"
                else -> "home"
            }
            nav.navigate(dest) { popUpTo("onbrd") { inclusive = true } }
        }
    }
    val steps: List<@Composable () -> Unit> = buildList {
        add { Who(vm) }
        when (vm.role) {
            "ORG" -> add { Accept(vm) }
            "DONOR" -> add { Cat(vm) }
            else -> add { Need(vm) }
        }
        add { Where(vm, nav) }
        add { Acc(vm, nav) }
    }
    val last = steps.size - 1
    val cur = step.coerceIn(0, last)
    Scaf(
        vm, nav, lng = true,
        onBack = if (cur > 0) ({ vm.err = ""; step = cur - 1 }) else null,
        foot = {
            if (vm.err.isNotBlank()) {
                Text(tr(vm, vm.err), color = Red, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
            }
            if (cur < last) {
                Btn(tr(vm, "Next")) { vm.err = ""; step = cur + 1 }
            } else {
                Btn(tr(vm, "Create account")) {
                    val bad = vm.badReg()
                    if (bad.isNotBlank()) vm.err = bad else vm.reg()
                }
            }
        }
    ) {
        Prog((cur + 1f) / steps.size, Grn)
        Spacer(Modifier.height(6.dp))
        Text(tr(vm, "Step") + " ${cur + 1}/${steps.size}", color = Sub, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(14.dp))
        steps[cur]()
    }
}

@Composable
private fun Who(vm: Vm) {
    if (vm.intent == "GIVE") {
        Hero(tr(vm, "About you"), tr(vm, "What kind of donor are you?"), gHero)
        Spacer(Modifier.height(16.dp))
        Pane {
            Lbl(tr(vm, "Donor type"))
            Opts(vm, dTop, dMore, vm.pType) { vm.pType = it }
            Spacer(Modifier.height(14.dp))
            Field(tr(vm, "Display name"), vm.pName) { vm.pName = it.take(60) }
        }
    } else {
        Hero(tr(vm, "Who are you?"), tr(vm, "A person or an organization?"), gHero)
        Spacer(Modifier.height(16.dp))
        Pane {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Pill(tr(vm, "A person"), vm.role != "ORG") { vm.role = "RECIP"; vm.pType = "" }
                Pill(tr(vm, "An organization"), vm.role == "ORG") { vm.role = "ORG"; vm.pType = "" }
            }
            Spacer(Modifier.height(16.dp))
            if (vm.role == "ORG") {
                Lbl(tr(vm, "Organization type"))
                Opts(vm, oTop, oMore, vm.pType) { vm.pType = it }
                Spacer(Modifier.height(14.dp))
                Field(tr(vm, "Organization name"), vm.pName) { vm.pName = it.take(60) }
            } else {
                Lbl(tr(vm, "I am a"))
                Opts(vm, pTop, pMore, vm.pType) { vm.pType = it }
            }
        }
    }
}

@Composable
private fun Need(vm: Vm) {
    Hero(tr(vm, "What do you need?"), tr(vm, "Pick everything that applies."), gHero)
    Spacer(Modifier.height(16.dp))
    Pane {
        Lbl(tr(vm, "I need"))
        Multi(vm, needsAll, vm.pNeeds) { vm.pNeeds = it }
        Spacer(Modifier.height(16.dp))
        Lbl(tr(vm, "How urgent is it?"))
        Grp(vm, listOf("LOW", "MED", "HIGH"), vm.pUrg) { vm.pUrg = it }
    }
}

@Composable
private fun Accept(vm: Vm) {
    Hero(tr(vm, "What can you accept?"), tr(vm, "Pick everything that applies."), gWarm)
    Spacer(Modifier.height(16.dp))
    Pane {
        Lbl(tr(vm, "We accept"))
        Multi(vm, needsAll, vm.pAcc) { vm.pAcc = it }
    }
}

@Composable
private fun Cat(vm: Vm) {
    Hero(tr(vm, "What do you usually share?"), tr(vm, "Pick everything that applies."), gHero)
    Spacer(Modifier.height(16.dp))
    Pane {
        Lbl(tr(vm, "I share"))
        Multi(vm, needsAll, vm.pNeeds) { vm.pNeeds = it }
    }
}

@Composable
private fun Where(vm: Vm, nav: NavHostController) {
    Hero(tr(vm, "Where are you?"), tr(vm, "Set your location on the map."), gHero)
    Spacer(Modifier.height(16.dp))
    Pane {
        Loc(vm, vm.pArea, vm.pLat, vm.pLng, "PROF", nav)
    }
}

@Composable
private fun Acc(vm: Vm, nav: NavHostController) {
    Hero(tr(vm, "Create your account"), tr(vm, "You can log in with these later."), gAi)
    Spacer(Modifier.height(16.dp))
    Pane {
        Lbl(tr(vm, "Account"))
        Field(tr(vm, "Email"), vm.email) { vm.email = it.trim().take(60) }
        Spacer(Modifier.height(12.dp))
        Field(tr(vm, "Password"), vm.pass, pass = true) { vm.pass = it.take(40) }
        Spacer(Modifier.height(12.dp))
        Field(tr(vm, "Confirm password"), vm.pass2, pass = true) { vm.pass2 = it.take(40) }
    }
    Spacer(Modifier.height(14.dp))
    OutBtn(tr(vm, "I already have an account")) { nav.navigate("login") }
}
