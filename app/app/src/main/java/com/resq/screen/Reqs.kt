package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController

@Composable
fun Reqs(vm: Vm) {
    LaunchedEffect(Unit) { vm.loadReq() }
    Scaf(vm, back = false) {
        Hero(tr(vm, "Your matches"), tr(vm, "Requests you've submitted."), gHero)
        Spacer(Modifier.height(16.dp))
        if (vm.reqs.isEmpty()) {
            Empty(tr(vm, "No requests yet"))
        } else {
            for (r in vm.reqs) {
                Pane {
                    Row(horizontalArrangement = spacedBy(8.dp)) {
                        Tag(tr(vm, cname(r.cat)), GrnL, GrnD)
                        Tag(tr(vm, r.urg), OrngL, Orng)
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(r.pub, color = Ink)
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}
