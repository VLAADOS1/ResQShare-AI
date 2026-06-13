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
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController

@Composable
fun Admin(vm: Vm, nav: NavHostController) {
    LaunchedEffect(Unit) { vm.loadAdmin() }
    val s = vm.sum
    val rs = vm.rateSum
    Scaf(vm, nav) {
        Hero(tr(vm, "Admin panel"), tr(vm, "Manage the platform"), gAi)
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            Tile("${s?.accts ?: 0}", tr(vm, "Accounts"), Prpl, Modifier.weight(1f))
            Spacer(Modifier.width(10.dp))
            Tile("${s?.donas ?: 0}", tr(vm, "Donations"), Grn, Modifier.weight(1f))
        }
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth()) {
            Tile("${s?.donors ?: 0}", tr(vm, "Donors"), Orng, Modifier.weight(1f))
            Spacer(Modifier.width(10.dp))
            Tile("${s?.recips ?: 0}", tr(vm, "Recipients"), Blue, Modifier.weight(1f))
            Spacer(Modifier.width(10.dp))
            Tile("${s?.orgs ?: 0}", tr(vm, "Orgs"), GrnD, Modifier.weight(1f))
        }
        Spacer(Modifier.height(20.dp))

        Pane {
            Hrow("👤", tr(vm, "Accounts"), "${vm.accts.size}") { nav.navigate("admaccts") }
            Hrow("📦", tr(vm, "Donations"), "${vm.adons.size}") { nav.navigate("admdonas") }
            Hrow("🏅", tr(vm, "Rewards"), "${vm.rewards.size}") { nav.navigate("admrew") }
            Hrow("⭐", tr(vm, "Reviews"), "${rs?.avg ?: 0.0} · ${rs?.count ?: 0}") { nav.navigate("admrevs") }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun Hrow(em: String, title: String, info: String, tap: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable { tap() }.padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(em, fontSize = 20.sp)
        Text("  $title", color = Ink, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f).padding(start = 6.dp))
        Text(info, color = Sub, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Text("  ›", color = Sub, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun Act(txt: String, bg: Color, fg: Color, tap: () -> Unit) {
    Text(
        txt,
        color = fg, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(bg).clickable { tap() }.padding(horizontal = 12.dp, vertical = 7.dp)
    )
}
