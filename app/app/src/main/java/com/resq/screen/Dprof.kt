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

@Composable
fun Dprof(vm: Vm, nav: NavHostController) {
    Scaf(
        vm, nav,
        foot = { Btn(tr(vm, "Save and continue")) { vm.reg(); nav.navigate("home") } }
    ) {
        Hero(tr(vm, "Set up your profile"), tr(vm, "I want to donate"), gHero)
        Spacer(Modifier.height(16.dp))
        Pane {
            Lbl(tr(vm, "Account"))
            Field(tr(vm, "Email"), vm.email) { vm.email = it }
            Spacer(Modifier.height(12.dp))
            Field(tr(vm, "Password"), vm.pass, pass = true) { vm.pass = it }
        }
        Spacer(Modifier.height(16.dp))
        Pane {
            Lbl(tr(vm, "Donor type"))
            Opts(
                vm,
                listOf("Restaurant", "Cafe", "Grocery store", "Bakery", "Individual"),
                listOf("School", "University", "Office", "Event organizer", "Hotel", "Canteen", "Supermarket", "Farm"),
                vm.pType
            ) { vm.pType = it }
            Spacer(Modifier.height(14.dp))
            Field(tr(vm, "Display name"), vm.pName) { vm.pName = it }
            Spacer(Modifier.height(14.dp))
            Lbl(tr(vm, "About you"))
            Field(tr(vm, "Tell recipients who you are (e.g. a small bakery)"), vm.pBio) { vm.pBio = it.take(300) }
            Spacer(Modifier.height(14.dp))
            Loc(vm, vm.pArea, vm.pLat, vm.pLng, "PROF", nav)
            Spacer(Modifier.height(14.dp))
            Lbl(tr(vm, "Category"))
            Gcat(vm, vm.dCat) { vm.dCat = it }
        }
        Spacer(Modifier.height(8.dp))
    }
}
