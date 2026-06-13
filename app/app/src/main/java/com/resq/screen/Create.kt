package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController

@Composable
fun Create(vm: Vm, nav: NavHostController) {
    var step by rememberSaveable { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        if (vm.dLat == null && vm.pLat != null) {
            vm.dLat = vm.pLat; vm.dLng = vm.pLng; vm.dArea = vm.pArea
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            val has = vm.dQty.isNotBlank() || vm.dNote.isNotBlank() || vm.dImgs.isNotEmpty() ||
                vm.dPwin.isNotBlank() || vm.dCond.isNotBlank()
            if (has) vm.saveDraft()
        }
    }
    val steps: List<@Composable () -> Unit> = listOf(
        { SPhoto(vm) },
        { SCat(vm) },
        { SDetails(vm) },
        { SDeliv(vm) },
        { SLoc(vm, nav) }
    )
    val last = steps.size - 1
    val cur = step.coerceIn(0, last)
    Scaf(
        vm, nav,
        onBack = if (cur > 0) ({ vm.err = ""; step = cur - 1 }) else null,
        foot = {
            if (vm.err.isNotBlank()) {
                Text(tr(vm, vm.err), color = Red, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
            }
            if (cur < last) {
                val block = cur == 0 && vm.scanning
                Btn(tr(vm, "Next"), if (block) Line else Grn) {
                    if (!block) {
                        val e = stepErr(vm, cur)
                        if (e.isNotBlank()) vm.err = e else { vm.err = ""; step = cur + 1 }
                    }
                }
            } else {
                Btn(tr(vm, "Analyze with AI")) {
                    val bad = vm.badDona()
                    if (bad.isNotBlank()) vm.err = bad else { vm.err = ""; vm.analyze(); nav.navigate("result") }
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

private fun stepErr(vm: Vm, step: Int): String = when (step) {
    0 -> if (vm.dImgs.isEmpty()) "Add at least one photo" else ""
    1 -> if (vm.dClbl.isBlank()) "Pick a category" else ""
    2 -> if (vm.dQty.isBlank()) "Enter a quantity" else if (vm.dNote.isBlank()) "Add info for the AI" else ""
    3 -> if (!vm.dPickup && !vm.dDeliver) "Choose pickup or delivery" else if (vm.dPwin.isBlank()) "Set a time" else ""
    else -> ""
}

@Composable
private fun SPhoto(vm: Vm) {
    Hero(tr(vm, "Add photos"), tr(vm, "At least one photo is required. You can add several."), gHero)
    Spacer(Modifier.height(16.dp))
    Photo(vm)
    val first = vm.dImgs.firstOrNull()
    LaunchedEffect(first) {
        if (first != null && first != vm.scanImg && !vm.scanning) vm.scanPhoto(first)
    }
    if (vm.scanning) {
        Spacer(Modifier.height(16.dp))
        Pane {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(Modifier.size(22.dp), color = Grn, strokeWidth = 3.dp)
                Text("  " + tr(vm, "AI is analyzing the photo..."), color = Ink, fontWeight = FontWeight.Bold)
            }
        }
    } else if (vm.scanImg.isNotBlank() && first != null) {
        Spacer(Modifier.height(16.dp))
        Pane {
            Text("✨ " + tr(vm, "Done! We filled in the details — just look them over."), color = GrnD, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SCat(vm: Vm) {
    Hero(tr(vm, "What is it?"), tr(vm, "Pick a category."), gHero)
    Spacer(Modifier.height(16.dp))
    Pane {
        Lbl(tr(vm, "Category"))
        Cats(vm)
    }
}

@Composable
private fun SDetails(vm: Vm) {
    Hero(tr(vm, "Details"), tr(vm, "Tell us more about it."), gHero)
    Spacer(Modifier.height(16.dp))
    Pane {
        Field(tr(vm, "Quantity"), vm.dQty) { vm.dQty = it.take(40) }
        Spacer(Modifier.height(12.dp))
        Field(tr(vm, "Condition"), vm.dCond) { vm.dCond = it.take(60) }
        Spacer(Modifier.height(12.dp))
        Lbl(tr(vm, "Info for AI"))
        Field(tr(vm, "Describe the item so the AI can write the listing"), vm.dNote) { vm.dNote = it.take(300) }
    }
}

@Composable
private fun SDeliv(vm: Vm) {
    Hero(tr(vm, "How will it reach people?"), tr(vm, "Choose one or both."), gHero)
    Spacer(Modifier.height(16.dp))
    Pane {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Pill(tr(vm, "Pickup"), vm.dPickup) { vm.dPickup = !vm.dPickup }
            Pill(tr(vm, "I can deliver"), vm.dDeliver) { vm.dDeliver = !vm.dDeliver }
        }
        Spacer(Modifier.height(14.dp))
        Lbl(if (vm.dDeliver && !vm.dPickup) tr(vm, "Delivery time window") else tr(vm, "Pickup time"))
        Tpick(vm)
    }
}

@Composable
private fun SLoc(vm: Vm, nav: NavHostController) {
    Hero(tr(vm, "Location"), tr(vm, "Where can it be collected?"), gHero)
    Spacer(Modifier.height(16.dp))
    Pane {
        Loc(vm, vm.dArea, vm.dLat, vm.dLng, "DONA", nav, if (vm.dDeliver) vm.dRad else null)
        if (vm.dDeliver) {
            Spacer(Modifier.height(14.dp))
            RadSlider(vm)
        }
    }
}
