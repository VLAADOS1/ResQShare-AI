package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*

@Composable
fun Income(vm: Vm) {
    LaunchedEffect(Unit) { vm.load() }
    Scaf(vm, back = false) {
        Hero(tr(vm, "Incoming offers"), tr(vm, "Accept or decline donations."), gWarm)
        Spacer(Modifier.height(16.dp))
        if (vm.income.isEmpty()) {
            Empty(tr(vm, "No incoming offers"))
        } else {
            for (o in vm.income) {
                Pane {
                    Text(tr(vm, "Donation #") + "${o.dona}", fontWeight = FontWeight.Bold, color = Ink)
                    Spacer(Modifier.height(8.dp))
                    Tag(tr(vm, o.stat), BlueL, Blue)
                    Spacer(Modifier.height(10.dp))
                    Btn(tr(vm, "Accept")) { vm.yes(o.id) }
                    Spacer(Modifier.height(8.dp))
                    OutBtn(tr(vm, "Decline")) { vm.no(o.id) }
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}
