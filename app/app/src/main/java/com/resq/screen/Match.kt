package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.compose.material3.Text
import androidx.navigation.NavHostController

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Match(vm: Vm, nav: NavHostController) {
    Scaf(vm, nav) {
        Hero(tr(vm, "Top matches"), tr(vm, "Best places for your donation."), gHero)
        Spacer(Modifier.height(16.dp))

        if (vm.busy && vm.mchs.isEmpty()) {
            Skel()
        } else if (vm.mchs.isEmpty()) {
            Empty(tr(vm, "No matches yet"))
        }
        for (m in vm.mchs) {
            Pane {
                Text(m.name, fontWeight = FontWeight.Bold, color = Ink)
                Spacer(Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Tag("${m.score}% " + tr(vm, "match"), GrnL, GrnD)
                    val km = m.dist
                    if (km != null) Tag("$km " + tr(vm, "km away"), BlueL, Blue)
                    Tag(tr(vm, if (m.ttype == "ORG") "Organization" else "Recipient"), GrnL, GrnD)
                    if (m.test) Tag(tr(vm, "Test data"), OrngL, Orng)
                }
                Spacer(Modifier.height(8.dp))
                val why = if (!m.reason.isNullOrBlank()) m.reason else buildList {
                    m.kind?.takeIf { it.isNotBlank() }?.let { add(tr(vm, it)) }
                    m.area?.takeIf { it.isNotBlank() }?.let { add(it) }
                    if (m.ttype == "RECIP") {
                        m.need?.takeIf { it.isNotBlank() }?.let { add(tr(vm, "needs") + " " + tr(vm, it)) }
                        if (m.urgent) add(tr(vm, "urgent"))
                    } else {
                        add(if (m.accepts) tr(vm, "accepts this category") else tr(vm, "general intake"))
                    }
                }.joinToString(" · ")
                Text(why, color = Sub, fontSize = 13.sp)
                Spacer(Modifier.height(8.dp))
                Btn(tr(vm, "Send offer")) { vm.send(m); nav.navigate("chat") }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}
