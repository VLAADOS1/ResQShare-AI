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
import androidx.navigation.NavHostController

@Composable
fun ReqNew(vm: Vm, nav: NavHostController) {
    if (vm.done) {
        Scaf(vm, nav, foot = { Btn(tr(vm, "My requests")) { vm.done = false; nav.navigate("reqnew") { popUpTo("reqnew") } } }) {
            Hero(tr(vm, "Request sent"), "", gHero)
            Spacer(Modifier.height(16.dp))
            Pane {
                Text("✅", fontSize = 40.sp)
                Spacer(Modifier.height(10.dp))
                Text(tr(vm, "Your request is now visible to donors near you. They can message you to offer help."), color = Ink, fontWeight = FontWeight.SemiBold)
            }
        }
        return
    }
    if (vm.rRev) {
        Scaf(
            vm, nav, onBack = { vm.rRev = false },
            foot = { Btn(tr(vm, "Publish request")) { if (!vm.busy) vm.ask() } }
        ) {
            Hero(tr(vm, "Review your request"), tr(vm, "AI wrote this announcement."), gAi)
            Spacer(Modifier.height(16.dp))
            Pane {
                Lbl(tr(vm, "Announcement"))
                Text(tr(vm, "Edit the text before you publish."), color = Sub, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Spacer(Modifier.height(12.dp))
                Field(tr(vm, "Announcement"), vm.rPub, lines = 4) { vm.rPub = it }
            }
            Spacer(Modifier.height(8.dp))
        }
        return
    }
    Scaf(vm, nav, foot = { Btn(if (vm.busy) tr(vm, "Generating...") else tr(vm, "Continue")) { if (!vm.busy) vm.draftReq() } }) {
        Hero(tr(vm, "Create request"), tr(vm, "Tell us what you need."), gAi)
        Spacer(Modifier.height(16.dp))
        Pane {
            Lbl(tr(vm, "Category"))
            Gcat(vm, vm.rCat) { vm.rCat = it }
            Spacer(Modifier.height(14.dp))
            Field(tr(vm, "What do you need"), vm.rDesc) { vm.rDesc = it }
            Spacer(Modifier.height(14.dp))
            Field(tr(vm, "Quantity / family size"), vm.rQty) { vm.rQty = it }
            Spacer(Modifier.height(14.dp))
            Lbl(tr(vm, "Urgency"))
            Grp(vm, listOf("LOW", "MED", "HIGH"), vm.rUrg) { vm.rUrg = it }
            Spacer(Modifier.height(14.dp))
            Lbl(tr(vm, "When are you free to receive"))
            Grp(vm, listOf("Morning", "Afternoon", "Evening", "Weekend"), vm.rAvail) { vm.rAvail = it }
        }
        Spacer(Modifier.height(8.dp))
    }
}
