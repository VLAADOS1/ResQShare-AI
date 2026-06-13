package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.foundation.shape.*
import androidx.compose.ui.unit.*
import androidx.compose.material3.Text
import androidx.navigation.NavHostController

@Composable
fun Detail(vm: Vm, nav: NavHostController) {
    val d = vm.cur
    if (d == null) {
        Scaf(vm, nav) { Empty(tr(vm, "Nothing here")) }
        return
    }
    Scaf(vm, nav) {
        Hero(d.title, (d.clbl ?: "").ifBlank { tr(vm, cname(d.cat)) }, gHero)
        Spacer(Modifier.height(16.dp))

        val pics = vm.curImgs
        if (pics.size > 1) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(pics) { iid -> Pic(iid, Modifier.size(240.dp, 200.dp).clip(RoundedCornerShape(18.dp))) }
            }
            Spacer(Modifier.height(12.dp))
        } else if (pics.size == 1) {
            Pic(pics[0], Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(18.dp)))
            Spacer(Modifier.height(12.dp))
        } else if (d.pic) {
            Img(d.id, Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(18.dp)))
            Spacer(Modifier.height(12.dp))
        }

        Pane {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatTag(vm, d.stat)
                if (d.deliv == "DELIVER" || d.deliv == "BOTH") Tag(tr(vm, "Delivery"), BlueL, Blue)
            }
            Spacer(Modifier.height(8.dp))
            Text(d.descr, color = Ink)
            Spacer(Modifier.height(8.dp))
            Text(tr(vm, "Quantity") + ": ${d.qty}", color = Sub)
            if (d.pwin.isNotBlank()) {
                val lbl = if (d.deliv == "DELIVER") tr(vm, "Delivery time window") else tr(vm, "Pickup time")
                Text("$lbl: ${d.pwin}", color = Sub)
            }
            if (d.area.isNotBlank()) Text(tr(vm, "Location") + ": ${d.area}", color = Sub)
            d.rad?.let { Text(tr(vm, "Delivery radius (km)") + ": $it", color = Sub) }
        }
        Spacer(Modifier.height(12.dp))

        val dn = vm.curDonor
        val dType = dn?.type ?: ""
        val dBio = dn?.bio ?: ""
        val dArea = dn?.area ?: ""
        val dName = dn?.name ?: ""
        if (dn != null && (dType.isNotBlank() || dBio.isNotBlank())) {
            Pane {
                Lbl(tr(vm, "About the donor"))
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Text("🏠", fontSize = 20.sp)
                    Column(Modifier.padding(start = 10.dp)) {
                        Text(dName.ifBlank { d.dname ?: tr(vm, "Donor") }, fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold, color = Ink)
                        if (dType.isNotBlank()) Text(tr(vm, dType), color = Sub, fontSize = 13.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                    }
                }
                if (dBio.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(dBio, color = Ink, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                }
                if (dArea.isNotBlank()) {
                    Spacer(Modifier.height(6.dp))
                    Text(tr(vm, "Area") + ": $dArea", color = Sub, fontSize = 13.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        val checks = (d.chk ?: "").split("\n").filter { it.contains(" ::: ") }
        if (checks.isNotEmpty()) {
            Pane {
                Lbl(tr(vm, "Safety checklist"))
                for (line in checks) {
                    val q = line.substringBefore(" ::: ")
                    val v = line.substringAfter(" ::: ")
                    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Text(if (v == "Y") "✅" else if (v == "N") "❌" else "▫️")
                        Text("  $q", color = Ink, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        if (vm.role == "DONOR") {
            val archived = d.stat == "DONE" || d.stat == "PICKED" || d.stat == "CANCEL" || d.stat == "REJ"
            Pane {
                Lbl(tr(vm, "Update status"))
                if (archived) {
                    Text(tr(vm, "This listing is in your archive."), color = Sub, fontSize = 12.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                    Spacer(Modifier.height(12.dp))
                    Btn(tr(vm, "Restore to active"), Blue) { vm.move(d.id, "AVAIL"); nav.popBackStack() }
                } else {
                    Text(tr(vm, "Reserved keeps it active. Given away and Cancelled move it to your archive."), color = Sub, fontSize = 12.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                    Spacer(Modifier.height(12.dp))
                    if (d.stat == "RESERVED") {
                        Btn(tr(vm, "Cancel reservation"), Orng) { vm.move(d.id, "AVAIL") }
                    } else {
                        Btn(tr(vm, "Mark reserved"), Blue) { vm.move(d.id, "RESERVED") }
                    }
                    Spacer(Modifier.height(10.dp))
                    Btn(tr(vm, "Mark given away"), GrnD) { vm.done(d.id); nav.navigate("celebrate") }
                    Spacer(Modifier.height(10.dp))
                    Btn(tr(vm, "Mark cancelled"), Red) { vm.move(d.id, "CANCEL"); nav.popBackStack() }
                }
            }
        } else if (d.acc > 0) {
            Btn(tr(vm, "Message donor")) {
                vm.openChat(d.acc, (d.dname ?: "").ifBlank { tr(vm, "Donor") }, d.id)
                nav.navigate("chat")
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}
