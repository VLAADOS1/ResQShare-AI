package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController

@Composable
fun MyReqs(vm: Vm, nav: NavHostController) {
    LaunchedEffect(Unit) { vm.loadReq(); vm.loadThreads() }
    val me = vm.acct?.id ?: 0L
    val mine = vm.reqs.filter { it.acc == me && it.stat != "DONE" && it.stat != "CANCEL" }.sortedByDescending { it.id }
    var pick by remember { mutableStateOf<Req?>(null) }
    pick?.let { Picker(vm, it) { pick = null } }
    vm.credit?.let { Done(vm, it) { vm.credit = null } }
    Scaf(
        vm, nav, back = false,
        foot = { Btn(tr(vm, "Create request")) { vm.newReq(); nav.navigate("reqform") } }
    ) {
        Hero(tr(vm, "My requests"), tr(vm, "What you are asking for."), gAi)
        Spacer(Modifier.height(16.dp))
        if (vm.busy && vm.reqs.isEmpty()) {
            Skel()
        } else if (mine.isEmpty()) {
            Empty(tr(vm, "You have no active requests"))
        } else {
            for (r in mine) {
                Pane {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Cicon(emoji(r.cat), ctint(r.cat))
                        Column(Modifier.weight(1f).padding(start = 12.dp)) {
                            Text(tr(vm, cname(r.cat)), fontWeight = FontWeight.ExtraBold, color = Ink)
                            Tag(tr(vm, "Active"), GrnL, GrnD)
                        }
                    }
                    val txt = r.pub.ifBlank { r.descr }
                    if (txt.isNotBlank()) {
                        Spacer(Modifier.height(8.dp))
                        Text(txt, color = Sub, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = spacedBy(10.dp)) {
                        Btn(tr(vm, "Fulfilled"), GrnD, Modifier.weight(1f)) { pick = r }
                        OutBtn(tr(vm, "Cancel request"), Modifier.weight(1f)) { vm.reqStat(r.id, "CANCEL") }
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun Picker(vm: Vm, r: Req, onClose: () -> Unit) {
    val mates = vm.thrds.distinctBy { it.other }.filter { it.other > 0 }
    AlertDialog(
        onDismissRequest = onClose,
        containerColor = Card,
        confirmButton = {
            Text(
                tr(vm, "Just close"), color = Sub, fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.clickable { vm.reqStat(r.id, "DONE"); onClose() }.padding(12.dp)
            )
        },
        title = { Text(tr(vm, "Who fulfilled this request?"), fontWeight = FontWeight.ExtraBold, color = Ink) },
        text = {
            Column {
                if (mates.isEmpty()) {
                    Text(tr(vm, "No conversations yet. Close without crediting."), color = Sub, fontWeight = FontWeight.SemiBold)
                } else {
                    Text(tr(vm, "They get the AI-credited impact for it."), color = Sub, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Spacer(Modifier.height(12.dp))
                    for (t in mates) {
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Bg)
                                .border(2.dp, Line, RoundedCornerShape(16.dp))
                                .clickable { vm.fulfil(r.id, t.other, t.oname); onClose() }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Cicon("🙂", GrnL)
                            Text(
                                t.oname.ifBlank { tr(vm, "Donor") }, color = Ink,
                                fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }
    )
}

@Composable
private fun Done(vm: Vm, g: Gain, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        containerColor = Card,
        confirmButton = {
            Text(
                tr(vm, "Close"), color = Grn, fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.clickable { onClose() }.padding(12.dp)
            )
        },
        icon = { Text("🎉", fontSize = 40.sp) },
        title = { Text(tr(vm, "Thank you!"), fontWeight = FontWeight.ExtraBold, color = Ink) },
        text = {
            Column {
                Text(
                    tr(vm, "You credited") + " " + vm.creditTo + ".",
                    color = Ink, fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(12.dp))
                if (g.meals > 0) Text("🍽  ${g.meals} " + tr(vm, "meals provided"), color = Ink, fontWeight = FontWeight.Bold)
                if (g.kg > 0) Text("♻  ${g.kg} kg " + tr(vm, "rescued"), color = Ink, fontWeight = FontWeight.Bold)
                if (g.people > 0) Text("🧡  ${g.people} " + tr(vm, "people helped"), color = Ink, fontWeight = FontWeight.Bold)
            }
        }
    )
}
