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
fun Dash(vm: Vm, nav: NavHostController) {
    LaunchedEffect(Unit) { vm.load() }
    Scaf(vm, nav, back = false) {
        Hero(tr(vm, "Organization dashboard"), tr(vm, "Offers, needs and priorities."), gWarm)
        Spacer(Modifier.height(16.dp))

        Lbl(tr(vm, "Incoming offers"))
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

        Spacer(Modifier.height(12.dp))
        Lbl(tr(vm, "Needs overview"))
        Row {
            Tile("20", tr(vm, "Meals needed"), Grn, Modifier.weight(1f))
            Spacer(Modifier.width(10.dp))
            Tile("8", tr(vm, "Hygiene kits"), Orng, Modifier.weight(1f))
            Spacer(Modifier.width(10.dp))
            Tile("12", tr(vm, "Clothes"), Blue, Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))
        Lbl(tr(vm, "High priority"))
        Pane {
            Text(tr(vm, "3 food donations expire within 2 hours"), color = Ink)
            Spacer(Modifier.height(6.dp))
            Text(tr(vm, "High demand for baby items in your area"), color = Sub)
        }
    }
}
