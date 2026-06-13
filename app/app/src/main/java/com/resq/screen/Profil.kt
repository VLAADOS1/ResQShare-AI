package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController

@Composable
fun Profil(vm: Vm, nav: NavHostController) {
    var notif by remember { mutableStateOf(true) }
    var sound by remember { mutableStateOf(true) }
    val a = vm.acct
    Scaf(
        vm, nav, back = false,
        foot = { Btn(tr(vm, "Log out"), Red) { vm.logout(); nav.navigate("onbrd") { popUpTo(0) } } }
    ) {
        Hero(tr(vm, "Profile"), a?.name ?: tr(vm, "Your account"), gAi)
        Spacer(Modifier.height(16.dp))

        Pane {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(56.dp).clip(CircleShape).background(GrnL), contentAlignment = Alignment.Center) {
                    Text((a?.name ?: "U").take(1).uppercase(), color = GrnD, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                }
                Column(Modifier.padding(start = 14.dp)) {
                    Text(a?.name ?: tr(vm, "Guest"), fontWeight = FontWeight.ExtraBold, color = Ink, fontSize = 18.sp)
                    Text(a?.email ?: "", color = Sub, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Tag(tr(vm, "Role") + ": ${vm.role}", GrnL, GrnD)
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        if (a?.admin == true) {
            Btn(tr(vm, "Admin panel"), Prpl) { vm.loadAdmin(); nav.navigate("admin") }
            Spacer(Modifier.height(16.dp))
        }

        Pane {
            Lbl(tr(vm, "Preferences"))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(tr(vm, "Language"), color = Ink, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Lng(vm)
            }
            Tog(tr(vm, "Notifications"), notif) { notif = it }
            Tog(tr(vm, "Sound"), sound) { sound = it }
        }
        Spacer(Modifier.height(16.dp))

        Pane {
            Lbl(tr(vm, "Account"))
            Srow("✏️", tr(vm, "Edit profile")) { nav.navigate("editp") }
            Srow("📍", tr(vm, "My location")) { nav.navigate("places") }
        }
        Spacer(Modifier.height(16.dp))

        Pane {
            Lbl(tr(vm, "Support"))
            Srow("⭐", tr(vm, "Rate the app")) { nav.navigate("rate") }
            Srow("ℹ️", tr(vm, "About ResQShare")) { nav.navigate("about") }
            Srow("📄", tr(vm, "Terms & policies")) { nav.navigate("terms") }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun Tog(title: String, on: Boolean, set: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = Ink, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Switch(
            checked = on, onCheckedChange = set,
            colors = SwitchDefaults.colors(checkedThumbColor = Color0, checkedTrackColor = Grn, uncheckedTrackColor = Line)
        )
    }
}

@Composable
private fun Srow(em: String, title: String, tap: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable { tap() }.padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(em, fontSize = 18.sp)
        Text("  $title", color = Ink, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f).padding(start = 6.dp))
        Text("›", color = Sub, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}
