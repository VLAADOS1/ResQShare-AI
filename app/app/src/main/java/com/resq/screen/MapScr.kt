package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*

@Composable
fun MapScr(vm: Vm) {
    Scaf(vm, back = false) {
        Hero(tr(vm, "Map"), tr(vm, "Nearby offers and organizations."), gHero)
        Spacer(Modifier.height(16.dp))
        Pane {
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(Line, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Dot(Grn); Text("  " + tr(vm, "Fresh bread - safe"), color = Ink)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Dot(Orng); Text("  " + tr(vm, "Sandwiches - urgent pickup"), color = Ink)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Dot(Red); Text("  " + tr(vm, "Prepared meal - needs review"), color = Ink)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Dot(Blue); Text("  " + tr(vm, "Hope Shelter"), color = Ink)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Pane {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Dot(Grn); Text("  " + tr(vm, "Safe"), color = Sub)
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Dot(Orng); Text("  " + tr(vm, "Urgent"), color = Sub)
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Dot(Red); Text("  " + tr(vm, "Review"), color = Sub)
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Dot(Blue); Text("  " + tr(vm, "Organization"), color = Sub)
            }
        }
    }
}
