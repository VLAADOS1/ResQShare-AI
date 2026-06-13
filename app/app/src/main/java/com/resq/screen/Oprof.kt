package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Oprof(vm: Vm, nav: NavHostController) {
    Scaf(
        vm, nav,
        foot = { Btn(tr(vm, "Save and continue")) { vm.reg(); nav.navigate("dash") } }
    ) {
        Hero(tr(vm, "Set up your profile"), tr(vm, "I represent an organization"), gWarm)
        Spacer(Modifier.height(16.dp))
        Pane {
            Lbl(tr(vm, "Account"))
            Field(tr(vm, "Email"), vm.email) { vm.email = it }
            Spacer(Modifier.height(12.dp))
            Field(tr(vm, "Password"), vm.pass, pass = true) { vm.pass = it }
        }
        Spacer(Modifier.height(16.dp))
        Pane {
            Lbl(tr(vm, "Organization type"))
            Opts(
                vm,
                listOf("Shelter", "Food bank", "Community center", "Volunteer group"),
                listOf("School", "Student pantry", "Church", "Charity", "Hospital", "Library"),
                vm.pType
            ) { vm.pType = it }
            Spacer(Modifier.height(14.dp))
            Field(tr(vm, "Organization name"), vm.pName) { vm.pName = it }
            Spacer(Modifier.height(14.dp))
            Loc(vm, vm.pArea, vm.pLat, vm.pLng, "PROF", nav)
            Spacer(Modifier.height(14.dp))
            Lbl(tr(vm, "What can you accept"))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val cs = listOf("food" to "Food", "cloth" to "Clothes", "hyg" to "Hygiene", "baby" to "Baby", "school" to "School")
                cs.forEach { p -> Pill(tr(vm, p.second), vm.pAcc == p.first) { vm.pAcc = p.first } }
            }
            Spacer(Modifier.height(14.dp))
            Lbl(tr(vm, "Max pickup distance (km)"))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (n in listOf(5, 10, 15, 20)) {
                    Pill("$n", vm.pDist == n) { vm.pDist = n }
                }
            }
            Spacer(Modifier.height(14.dp))
            Lbl(tr(vm, "Daily capacity (meals)"))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (n in listOf(10, 20, 30, 50)) {
                    Pill("$n", vm.pCap == n) { vm.pCap = n }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}
