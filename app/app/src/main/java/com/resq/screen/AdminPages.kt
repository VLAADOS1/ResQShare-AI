package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.shape.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdmRew(vm: Vm, nav: NavHostController) {
    var rt by remember { mutableStateOf("") }
    var re by remember { mutableStateOf("") }
    var rd by remember { mutableStateOf("") }
    var rm by remember { mutableStateOf("meals") }
    var rg by remember { mutableStateOf("") }
    val emojis = listOf("🏅", "🌱", "🍞", "🍽️", "👐", "🤝", "♻️", "🌍", "📦", "☕", "🎟️", "🛒", "⭐", "💚")
    Scaf(vm, nav) {
        Hero(tr(vm, "Rewards"), tr(vm, "Create and manage rewards."), gAi)
        Spacer(Modifier.height(16.dp))
        Pane {
            Lbl(tr(vm, "Create reward"))
            Field(tr(vm, "Title"), rt) { rt = it.take(40) }
            Spacer(Modifier.height(10.dp))
            Field(tr(vm, "Emoji (type your own)"), re) { re = it.take(6) }
            Spacer(Modifier.height(8.dp))
            FlowRow(horizontalArrangement = spacedBy(6.dp), verticalArrangement = spacedBy(6.dp)) {
                for (e in emojis) Box(
                    Modifier.size(40.dp).clip(RoundedCornerShape(10.dp))
                        .background(if (re == e) GrnL else Bg)
                        .border(2.dp, if (re == e) Grn else Line, RoundedCornerShape(10.dp))
                        .clickable { re = e },
                    contentAlignment = Alignment.Center
                ) { Text(e, fontSize = 18.sp) }
            }
            Spacer(Modifier.height(12.dp))
            Lbl(tr(vm, "Description"))
            Field(tr(vm, "What it is and how to earn it"), rd) { rd = it.take(200) }
            Spacer(Modifier.height(12.dp))
            Lbl(tr(vm, "Goal metric"))
            Grp(vm, listOf("meals", "people", "kg", "co2", "items", "orgs"), rm) { rm = it }
            Spacer(Modifier.height(12.dp))
            Field(tr(vm, "Goal"), rg) { rg = it.filter { c -> c.isDigit() }.take(7) }
            Spacer(Modifier.height(14.dp))
            Btn(tr(vm, "Create reward")) {
                if (rt.isNotBlank()) {
                    vm.addReward(rt, re.ifBlank { "🏅" }, rd, rm, rg.toIntOrNull() ?: 1, null)
                    rt = ""; re = ""; rd = ""; rg = ""
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Lbl(tr(vm, "All rewards") + " (${vm.rewards.size})")
        for (r in vm.rewards) {
            Pane(14) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(r.emoji, fontSize = 22.sp)
                    Column(Modifier.weight(1f).padding(start = 12.dp)) {
                        Text(r.title, fontWeight = FontWeight.ExtraBold, color = Ink, fontSize = 14.sp)
                        Text("${r.metric} ≥ ${r.goal}", color = Sub, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        r.descr?.takeIf { it.isNotBlank() }?.let { Text(it, color = Sub, fontSize = 11.sp, maxLines = 2) }
                    }
                    Act(tr(vm, "Delete"), RedL, Red) { vm.delReward(r.id) }
                }
            }
            Spacer(Modifier.height(10.dp))
        }
        Spacer(Modifier.height(8.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdmDonas(vm: Vm, nav: NavHostController) {
    var st by remember { mutableStateOf("ALL") }
    val list = vm.adons
        .filter { matches(it, vm.admQ) }
        .filter { st == "ALL" || it.stat == st }
        .sortedByDescending { it.id }
    Scaf(vm, nav) {
        Hero(tr(vm, "Donations"), tr(vm, "All listings on the platform."), gHero)
        Spacer(Modifier.height(16.dp))
        Field(tr(vm, "Search"), vm.admQ) { vm.admQ = it }
        Spacer(Modifier.height(10.dp))
        FlowRow(horizontalArrangement = spacedBy(8.dp), verticalArrangement = spacedBy(8.dp)) {
            for (o in listOf("ALL", "AVAIL", "RESERVED", "DONE", "CANCEL")) {
                Pill(tr(vm, statLbl(o)), st == o) { st = o }
            }
        }
        Spacer(Modifier.height(12.dp))
        Lbl(tr(vm, "Donations") + " (${list.size})")
        if (list.isEmpty()) Empty(tr(vm, "Nothing here"))
        for (d in list) {
            Pane(14) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Cicon(emoji(d.cat), ctint(d.cat))
                    Column(Modifier.weight(1f).padding(start = 12.dp)) {
                        Text(d.title, fontWeight = FontWeight.ExtraBold, color = Ink, fontSize = 14.sp)
                        Text("#${d.id} · " + tr(vm, statLbl(d.stat)) + " · " + (d.dname ?: "—"), color = Sub, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        if (d.test) Text(tr(vm, "Test data"), color = Orng, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = spacedBy(8.dp)) {
                    if (d.acc > 0) Act(tr(vm, "Message"), BlueL, Blue) {
                        vm.openChat(d.acc, (d.dname ?: "").ifBlank { tr(vm, "Donor") }, d.id)
                        nav.navigate("chat")
                    }
                    Act(tr(vm, "Delete"), RedL, Red) { vm.delDona(d.id) }
                }
            }
            Spacer(Modifier.height(10.dp))
        }
        Spacer(Modifier.height(8.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdmAccts(vm: Vm, nav: NavHostController) {
    var role by remember { mutableStateOf("ALL") }
    val q = vm.admQ.trim().lowercase()
    val list = vm.accts
        .filter { q.isBlank() || it.name.lowercase().contains(q) || it.email.lowercase().contains(q) }
        .filter { role == "ALL" || it.role == role }
    Scaf(vm, nav) {
        Hero(tr(vm, "Accounts"), tr(vm, "Everyone on the platform."), gAi)
        Spacer(Modifier.height(16.dp))
        Field(tr(vm, "Search"), vm.admQ) { vm.admQ = it }
        Spacer(Modifier.height(10.dp))
        FlowRow(horizontalArrangement = spacedBy(8.dp), verticalArrangement = spacedBy(8.dp)) {
            for (o in listOf("ALL", "DONOR", "RECIP", "ORG")) {
                Pill(tr(vm, roleLbl(o)), role == o) { role = o }
            }
        }
        Spacer(Modifier.height(12.dp))
        Lbl(tr(vm, "Accounts") + " (${list.size})")
        for (a in list) {
            Pane(14) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(40.dp).clip(CircleShape).background(if (a.admin) PrplL else GrnL), contentAlignment = Alignment.Center) {
                        Text((a.name.ifBlank { a.email }).take(1).uppercase(), color = if (a.admin) Prpl else GrnD, fontWeight = FontWeight.ExtraBold)
                    }
                    Column(Modifier.weight(1f).padding(start = 12.dp)) {
                        Text(a.name.ifBlank { tr(vm, "User") }, fontWeight = FontWeight.ExtraBold, color = Ink, fontSize = 14.sp)
                        Text(a.email, color = Sub, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        if (a.test) Text(tr(vm, "Test data"), color = Orng, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    if (a.admin) Tag("ADMIN", PrplL, Prpl)
                    else if (a.banned) Tag(tr(vm, "Banned"), RedL, Red)
                    else Tag(tr(vm, roleLbl(a.role)), GrnL, GrnD)
                }
                Spacer(Modifier.height(10.dp))
                FlowRow(horizontalArrangement = spacedBy(8.dp), verticalArrangement = spacedBy(8.dp)) {
                    Act(tr(vm, "Message"), BlueL, Blue) {
                        vm.openChat(a.id, a.name.ifBlank { a.email }, 0)
                        nav.navigate("chat")
                    }
                    if (!a.admin) {
                        if (a.banned) Act(tr(vm, "Unban"), GrnL, GrnD) { vm.ban(a.id, false) }
                        else Act(tr(vm, "Ban"), OrngL, Orng) { vm.ban(a.id, true) }
                        Act(tr(vm, "Delete"), RedL, Red) { vm.delAcct(a.id) }
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
        }
        Spacer(Modifier.height(8.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdmRevs(vm: Vm, nav: NavHostController) {
    var star by remember { mutableStateOf(0) }
    val rs = vm.rateSum
    val list = vm.ratings.filter { star == 0 || it.stars == star }.reversed()
    Scaf(vm, nav) {
        Hero(tr(vm, "Reviews"), "${rs?.avg ?: 0.0} / 5 · ${rs?.count ?: 0} " + tr(vm, "ratings"), gAi)
        Spacer(Modifier.height(16.dp))
        FlowRow(horizontalArrangement = spacedBy(8.dp), verticalArrangement = spacedBy(8.dp)) {
            Pill(tr(vm, "All"), star == 0) { star = 0 }
            for (n in 5 downTo 1) Pill("$n★", star == n) { star = n }
        }
        Spacer(Modifier.height(12.dp))
        Lbl(tr(vm, "Reviews") + " (${list.size})")
        if (list.isEmpty()) Empty(tr(vm, "Nothing here"))
        for (r in list) {
            Pane(14) {
                Text("★".repeat(r.stars) + "☆".repeat((5 - r.stars).coerceAtLeast(0)), color = Orng, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                Spacer(Modifier.height(4.dp))
                Text(r.name.ifBlank { tr(vm, "User") }, fontWeight = FontWeight.Bold, color = Ink, fontSize = 13.sp)
                if (r.txt.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(r.txt, color = Sub, fontSize = 13.sp)
                }
            }
            Spacer(Modifier.height(10.dp))
        }
        Spacer(Modifier.height(8.dp))
    }
}

private fun statLbl(s: String): String = when (s) {
    "ALL" -> "All"
    "AVAIL" -> "Available"
    "RESERVED" -> "Reserved"
    "DONE", "PICKED" -> "Given away"
    "CANCEL", "REJ" -> "Cancelled"
    "REVIEW" -> "Under review"
    "SENT" -> "Sent"
    else -> s
}

private fun roleLbl(r: String): String = when (r) {
    "ALL" -> "All"
    "DONOR" -> "Donor"
    "RECIP" -> "Recipient"
    "ORG" -> "Organization"
    else -> r
}
