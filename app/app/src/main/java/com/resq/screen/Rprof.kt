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
fun Rprof(vm: Vm, nav: NavHostController) {
    Scaf(
        vm, nav,
        foot = { Btn(tr(vm, "Save and continue")) { vm.reg(); nav.navigate("feed") } }
    ) {
        Hero(tr(vm, "Set up your profile"), tr(vm, "I need support"), gAi)
        Spacer(Modifier.height(16.dp))
        Pane {
            Lbl(tr(vm, "Account"))
            Field(tr(vm, "Email"), vm.email) { vm.email = it }
            Spacer(Modifier.height(12.dp))
            Field(tr(vm, "Password"), vm.pass, pass = true) { vm.pass = it }
        }
        Spacer(Modifier.height(16.dp))
        Pane {
            Lbl(tr(vm, "Account type"))
            Opts(
                vm,
                listOf("Individual", "Family", "Student", "Elderly person"),
                listOf("Nonprofit", "Refugee", "Veteran", "Single parent"),
                vm.pType
            ) { vm.pType = it }
            Spacer(Modifier.height(14.dp))
            Lbl(tr(vm, "Your name"))
            Field(tr(vm, "Your name"), vm.pName) { vm.pName = it.take(40) }
            Spacer(Modifier.height(14.dp))
            Loc(vm, vm.pArea, vm.pLat, vm.pLng, "PROF", nav)
            Spacer(Modifier.height(14.dp))
            Lbl(tr(vm, "Max pickup radius (km)"))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                for (n in listOf(1, 3, 5, 10, 15, 25)) {
                    Pill("$n", vm.pRad == n) { vm.pRad = n }
                }
            }
            Spacer(Modifier.height(14.dp))
            Lbl(tr(vm, "What do you need"))
            Grp(vm, listOf("Food", "Clothes", "Hygiene", "Baby", "School", "Other"), vm.pNeeds) { vm.pNeeds = it }
            Spacer(Modifier.height(14.dp))
            Lbl(tr(vm, "Dietary restrictions"))
            Grp(vm, listOf("None", "Vegetarian", "Halal", "Kosher", "Gluten-free", "No pork"), vm.pDiet) { vm.pDiet = it }
            Spacer(Modifier.height(14.dp))
            Lbl(tr(vm, "Availability"))
            Grp(vm, listOf("Morning", "Afternoon", "Evening", "Weekend"), vm.pAvail) { vm.pAvail = it }
            Spacer(Modifier.height(14.dp))
            Lbl(tr(vm, "Urgency"))
            Grp(vm, listOf("LOW", "MED", "HIGH"), vm.pUrg) { vm.pUrg = it }
        }
        Spacer(Modifier.height(8.dp))
    }
}
