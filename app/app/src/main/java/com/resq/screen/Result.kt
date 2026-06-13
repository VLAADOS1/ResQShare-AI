package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.compose.material3.Text
import androidx.navigation.NavHostController

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Result(vm: Vm, nav: NavHostController) {
    val a = vm.ana
    if (vm.busy || a == null) {
        Scaf(vm, nav) {
            Hero(tr(vm, "AI-generated listing"), tr(vm, "AI is checking item type, safety, and best matches..."), gAi)
            Spacer(Modifier.height(16.dp))
            Skel()
        }
        return
    }
    val answered = a.checks.indices.all { vm.checkAns[it] != null }
    Scaf(
        vm, nav,
        foot = {
            if (!answered) {
                Text(tr(vm, "Answer the checklist to continue."), color = Red, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(Modifier.height(10.dp))
            }
            Btn(tr(vm, "Find matches"), if (answered) Grn else Line) {
                if (answered) { vm.find(); nav.navigate("match") { popUpTo("home") } }
            }
            Spacer(Modifier.height(10.dp))
            OutBtn(tr(vm, "Post listing")) { if (answered) { vm.save(); nav.navigate("home") { popUpTo("home") { inclusive = true } } } }
        }
    ) {
        Hero(a.item, tr(vm, "AI-generated listing"), gAi)
        Spacer(Modifier.height(16.dp))

        Pane {
            Lbl(tr(vm, "AI-generated listing"))
            Text(tr(vm, "Edit the text before you publish."), color = Sub, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Spacer(Modifier.height(12.dp))
            Field(tr(vm, "Title"), a.title) { vm.ana = a.copy(title = it) }
            Spacer(Modifier.height(12.dp))
            Field(tr(vm, "Description"), a.descr, lines = 3) { vm.ana = a.copy(descr = it) }
        }
        Spacer(Modifier.height(12.dp))

        Pane {
            Lbl(tr(vm, "Safety checklist"))
            Text(tr(vm, "Confirm each point before sharing."), color = Sub, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            a.checks.forEachIndexed { i, c ->
                Spacer(Modifier.height(14.dp))
                Text(c, color = Ink, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    YN(tr(vm, "Yes"), vm.checkAns[i] == true, Grn, GrnL, Modifier.weight(1f)) { vm.checkAns[i] = true }
                    YN(tr(vm, "No"), vm.checkAns[i] == false, Red, RedL, Modifier.weight(1f)) { vm.checkAns[i] = false }
                }
            }
        }
        Spacer(Modifier.height(12.dp))

        Pane {
            Lbl(tr(vm, "Recommended recipients"))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (r in a.recs) Tag(r, GrnL, GrnD)
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun YN(txt: String, on: Boolean, fg: Color, bgOn: Color, mod: Modifier, tap: () -> Unit) {
    Box(
        mod.clip(RoundedCornerShape(14.dp))
            .background(if (on) bgOn else Card)
            .border(2.dp, if (on) fg else Line, RoundedCornerShape(14.dp))
            .clickable { tap() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(txt, color = if (on) fg else Sub, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
    }
}
