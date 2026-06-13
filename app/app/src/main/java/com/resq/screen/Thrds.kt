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
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController

@Composable
fun Thrds(vm: Vm, nav: NavHostController) {
    LaunchedEffect(Unit) { vm.loadThreads() }
    Scaf(vm, nav, back = false) {
        Hero(tr(vm, "Messages"), tr(vm, "Your conversations."), gHero)
        Spacer(Modifier.height(16.dp))
        if (vm.thrds.isEmpty()) {
            Empty(tr(vm, "No messages yet"))
        } else {
            val me = vm.acct?.id ?: 0L
            for (t in vm.thrds) {
                val unread = t.lastFrm != me && t.ts > (vm.reads[t.other] ?: 0L)
                Row2(t.oname.ifBlank { tr(vm, "User") }, t.last, unread) {
                    vm.openChat(t.other, t.oname, t.dona)
                    nav.navigate("chat")
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun Row2(name: String, last: String, unread: Boolean, tap: () -> Unit) {
    androidx.compose.foundation.layout.Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(Card)
            .border(2.dp, if (unread) Grn else Line, RoundedCornerShape(18.dp)).clickable { tap() }.padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(48.dp).clip(CircleShape).background(GrnL), contentAlignment = Alignment.Center) {
            Text(name.take(1).uppercase(), color = GrnD, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
        }
        Column(Modifier.weight(1f).padding(start = 12.dp)) {
            Text(name, color = Ink, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            Text(last, color = if (unread) Ink else Sub, fontSize = 13.sp, fontWeight = if (unread) FontWeight.Bold else FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        if (unread) {
            Box(Modifier.size(12.dp).clip(CircleShape).background(Grn))
        }
    }
}
