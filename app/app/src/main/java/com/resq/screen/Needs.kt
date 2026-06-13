package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

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

private fun rkm(vm: Vm, r: Req): Double? {
    val ula = vm.pLat; val uln = vm.pLng; val rla = r.lat; val rln = r.lng
    if (ula == null || uln == null || rla == null || rln == null) return null
    val dLa = Math.toRadians(rla - ula)
    val dLn = Math.toRadians(rln - uln)
    val a = sin(dLa / 2).pow(2) + cos(Math.toRadians(ula)) * cos(Math.toRadians(rla)) * sin(dLn / 2).pow(2)
    return Math.round(6371.0 * 2 * atan2(sqrt(a), sqrt(1 - a)) * 10) / 10.0
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Needs(vm: Vm, nav: NavHostController) {
    LaunchedEffect(Unit) { vm.loadReq() }
    val list = vm.reqs
        .filter { !it.test }
        .filter { it.stat != "CANCEL" && it.stat != "DONE" }
        .filter { (rkm(vm, it) ?: 0.0) <= 60.0 }
        .sortedByDescending { it.id }
    Scaf(vm, nav, back = false, scroll = false) {
        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = spacedBy(12.dp)
        ) {
            item { Hero(tr(vm, "Requests near you"), tr(vm, "See what people need and offer help."), gHero) }
            if (vm.busy && vm.reqs.isEmpty()) {
                item { Skel() }
            } else if (list.isEmpty()) {
                item { Empty(tr(vm, "No requests nearby")) }
            } else {
                items(list, key = { it.id }) { r ->
                    Pane {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Cicon(emoji(r.cat), ctint(r.cat))
                            Column(Modifier.weight(1f).padding(start = 12.dp)) {
                                Text(r.name.ifBlank { tr(vm, "Recipient") }, fontWeight = FontWeight.ExtraBold, color = Ink)
                                Text(tr(vm, cname(r.cat)), color = Sub, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                            if (r.test) Tag(tr(vm, "Test data"), OrngL, Orng)
                        }
                        Spacer(Modifier.height(10.dp))
                        val txt = r.pub.ifBlank { r.descr }
                        if (txt.isNotBlank()) Text(txt, color = Ink)
                        Spacer(Modifier.height(8.dp))
                        FlowRow(horizontalArrangement = spacedBy(8.dp), verticalArrangement = spacedBy(8.dp)) {
                            if (r.qty.isNotBlank()) Tag(r.qty, GrnL, GrnD)
                            val km = rkm(vm, r)
                            if (km != null) Tag("$km " + tr(vm, "km away"), BlueL, Blue)
                            if (r.urg == "HIGH") Tag(tr(vm, "urgent"), RedL, Red)
                            if (r.area.isNotBlank()) Tag(r.area, GrnL, GrnD)
                        }
                        Spacer(Modifier.height(10.dp))
                        if (r.acc > 0) {
                            Btn(tr(vm, "Message")) {
                                vm.openChat(r.acc, r.name.ifBlank { tr(vm, "Recipient") }, 0)
                                nav.navigate("chat")
                            }
                        }
                    }
                }
            }
        }
    }
}
