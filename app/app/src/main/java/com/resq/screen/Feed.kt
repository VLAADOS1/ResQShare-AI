package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import kotlin.math.*

private val cats = listOf("Food" to "FOOD", "Clothes" to "CLOTH", "Hygiene" to "HYG", "Baby" to "BABY", "School" to "SCHOOL", "Other" to "OTHER")

private fun dist(la1: Double, ln1: Double, la2: Double, ln2: Double): Double {
    val r = 6371.0
    val dLa = Math.toRadians(la2 - la1)
    val dLn = Math.toRadians(ln2 - ln1)
    val a = sin(dLa / 2).pow(2) + cos(Math.toRadians(la1)) * cos(Math.toRadians(la2)) * sin(dLn / 2).pow(2)
    return r * 2 * atan2(sqrt(a), sqrt(1 - a))
}

private fun near(vm: Vm, d: Dona): Boolean {
    val ula = vm.pLat; val uln = vm.pLng; val dla = d.lat; val dln = d.lng
    if (ula == null || uln == null || dla == null || dln == null) return true
    return dist(ula, uln, dla, dln) <= vm.pRad
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Feed(vm: Vm, nav: NavHostController) {
    LaunchedEffect(Unit) { vm.load() }
    var more by remember { mutableStateOf(false) }
    val list = vm.feed
        .filter { matches(it, vm.feedQ) }
        .filter { vm.fCats.isEmpty() || it.cat in vm.fCats }
        .filter { !vm.fDeliv || it.deliv == "DELIVER" || it.deliv == "BOTH" }
        .filter { !vm.fNearby || near(vm, it) }
        .let { if (vm.fSortNew) it.sortedByDescending { d -> d.id } else it.sortedBy { d -> d.id } }
    Scaf(vm, nav, back = false, scroll = false) {
        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = spacedBy(12.dp)
        ) {
            item { Hero(tr(vm, "You might need this"), tr(vm, "Browse and request what you need."), gHero) }
            item { Field(tr(vm, "Search"), vm.feedQ) { vm.feedQ = it } }
            item {
                FlowRow(horizontalArrangement = spacedBy(8.dp), verticalArrangement = spacedBy(8.dp)) {
                    Pill(tr(vm, "All"), vm.fCats.isEmpty()) { vm.fCats = emptySet() }
                    for (c in cats) {
                        Pill(tr(vm, c.first), c.second in vm.fCats) {
                            vm.fCats = if (c.second in vm.fCats) vm.fCats - c.second else vm.fCats + c.second
                        }
                    }
                    Pill(tr(vm, "More filters"), more) { more = !more }
                }
            }
            if (more) {
                item {
                    Pane {
                        Lbl(tr(vm, "Sort"))
                        Row(horizontalArrangement = spacedBy(8.dp)) {
                            Pill(tr(vm, "Newest"), vm.fSortNew) { vm.fSortNew = true }
                            Pill(tr(vm, "Oldest"), !vm.fSortNew) { vm.fSortNew = false }
                        }
                        Spacer(Modifier.height(12.dp))
                        Lbl(tr(vm, "Options"))
                        FlowRow(horizontalArrangement = spacedBy(8.dp), verticalArrangement = spacedBy(8.dp)) {
                            Pill(tr(vm, "Delivery available"), vm.fDeliv) { vm.fDeliv = !vm.fDeliv }
                            Pill(tr(vm, "Within my radius") + " (${vm.pRad}km)", vm.fNearby) { vm.fNearby = !vm.fNearby }
                        }
                        if (vm.fCats.isNotEmpty() || vm.fDeliv || vm.fNearby || !vm.fSortNew) {
                            Spacer(Modifier.height(12.dp))
                            Text(
                                tr(vm, "Reset filters"), color = Red, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp,
                                modifier = Modifier.clickable {
                                    vm.fCats = emptySet(); vm.fDeliv = false; vm.fNearby = false; vm.fSortNew = true
                                }
                            )
                        }
                    }
                }
            }
            item {
                Text(
                    "${list.size} " + tr(vm, "offers"), color = Sub, fontSize = 13.sp, fontWeight = FontWeight.Bold
                )
            }
            if (vm.busy && vm.feed.isEmpty()) {
                item { Skel() }
            } else if (list.isEmpty()) {
                item { Empty(tr(vm, "No offers right now")) }
            } else {
                items(list, key = { it.id }) { d ->
                    DonaCard(vm, d) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = spacedBy(10.dp)) {
                            OutBtn(tr(vm, "Details"), Modifier.weight(1f)) { vm.open(d.id); nav.navigate("detail") }
                            if (d.acc > 0) {
                                Btn(tr(vm, "Message"), Grn, Modifier.weight(1f)) {
                                    vm.openChat(d.acc, (d.dname ?: "").ifBlank { tr(vm, "Donor") }, d.id)
                                    nav.navigate("chat")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
