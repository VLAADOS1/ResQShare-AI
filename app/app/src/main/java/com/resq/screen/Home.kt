package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Home(vm: Vm, nav: NavHostController) {
    LaunchedEffect(Unit) { vm.load() }
    val archSt = setOf("DONE", "PICKED", "CANCEL", "REJ")
    fun grp(d: Dona) = if (d.stat in archSt) "ARCHIVE" else if (d.stat == "RESERVED") "RESERVED" else "ACTIVE"
    val nA = vm.mine.count { grp(it) == "ACTIVE" }
    val nR = vm.mine.count { grp(it) == "RESERVED" }
    val nX = vm.mine.count { grp(it) == "ARCHIVE" }
    val cur = vm.mine.filter { grp(it) == vm.board && matches(it, vm.myQ) }
    val sorted = if (vm.sortNew) cur.sortedByDescending { it.id } else cur.sortedBy { it.id }
    Scaf(
        vm, nav, back = false, scroll = false,
        foot = { Btn(tr(vm, "Create new donation")) { vm.newDona(); nav.navigate("create") } }
    ) {
        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                val nm = vm.acct?.name?.takeIf { it.isNotBlank() }
                Hero(if (nm != null) tr(vm, "Hi") + ", $nm" else tr(vm, "Hi there"), tr(vm, "Share what you no longer need."), gHero)
            }
            item {
                val st = vm.stat
                Row {
                    Tile("${st?.meals ?: 0}", tr(vm, "Meals"), Grn, Modifier.weight(1f))
                    Spacer(Modifier.width(10.dp))
                    Tile("${st?.people ?: 0}", tr(vm, "People"), Orng, Modifier.weight(1f))
                    Spacer(Modifier.width(10.dp))
                    Tile("${st?.orgs ?: 0}", tr(vm, "Partners"), Blue, Modifier.weight(1f))
                }
            }
            if (vm.drafts.isNotEmpty()) {
                    item { Lbl(tr(vm, "Drafts")) }
                items(vm.drafts, key = { it.id }) { d ->
                    Pane {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Cicon(emoji(d.cat), ctint(d.cat))
                            Column(Modifier.weight(1f).padding(start = 12.dp)) {
                                Text((d.ana?.title ?: "").ifBlank { d.clbl.ifBlank { tr(vm, "Untitled draft") } }, fontWeight = FontWeight.ExtraBold, color = Ink)
                                Text(tr(vm, "Draft"), color = Sub, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Text(
                                tr(vm, "Delete"), color = Red, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp,
                                modifier = Modifier.clickable { vm.delDraft(d) }.padding(8.dp)
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                        OutBtn(tr(vm, "Resume")) { vm.openDraft(d); nav.navigate(if (d.ana == null) "create" else "result") }
                    }
                }
            }
            item { Lbl(tr(vm, "My donations")) }
            item {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Sort(tr(vm, "Active") + " ($nA)", vm.board == "ACTIVE") { vm.board = "ACTIVE" }
                    Sort(tr(vm, "Reserved") + " ($nR)", vm.board == "RESERVED") { vm.board = "RESERVED" }
                    Sort(tr(vm, "Archive") + " ($nX)", vm.board == "ARCHIVE") { vm.board = "ARCHIVE" }
                }
            }
            item { Field(tr(vm, "Search"), vm.myQ) { vm.myQ = it } }
            if (cur.size > 1) {
                item {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Spacer(Modifier.weight(1f))
                        Sort(tr(vm, "Newest"), vm.sortNew) { vm.sortNew = true }
                        Spacer(Modifier.width(6.dp))
                        Sort(tr(vm, "Oldest"), !vm.sortNew) { vm.sortNew = false }
                    }
                }
            }
            if (vm.busy && cur.isEmpty()) {
                item { Skel() }
            } else if (cur.isEmpty()) {
                item { Empty(tr(vm, "Nothing here")) }
            } else {
                items(sorted, key = { it.id }) { d ->
                    DonaCard(vm, d) {
                        OutBtn(tr(vm, "View details")) { vm.open(d.id); nav.navigate("detail") }
                    }
                }
            }
        }
    }
}

@Composable
private fun Sort(txt: String, on: Boolean, tap: () -> Unit) {
    Text(
        txt,
        color = if (on) GrnD else Sub, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (on) GrnL else Card)
            .border(2.dp, if (on) Grn else Line, RoundedCornerShape(10.dp))
            .clickable { tap() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}
