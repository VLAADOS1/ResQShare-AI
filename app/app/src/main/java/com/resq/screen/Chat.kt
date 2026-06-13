package com.resq.screen

import com.resq.*
import com.resq.data.*
import com.resq.ui.*

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
fun Chat(vm: Vm, nav: NavHostController) {
    var txt by remember { mutableStateOf("") }
    val list = rememberLazyListState()
    LaunchedEffect(vm.chOther) {
        vm.loadChat()
        while (true) {
            delay(3000)
            vm.loadChat()
        }
    }
    LaunchedEffect(vm.msgs.size) {
        if (vm.msgs.isNotEmpty()) list.animateScrollToItem(vm.msgs.size - 1)
    }
    Scaf(
        vm, nav, scroll = false,
        foot = {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = txt, onValueChange = { txt = it },
                    placeholder = { Text(tr(vm, "Type a message")) },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Card, unfocusedContainerColor = Card,
                        focusedIndicatorColor = Grn, unfocusedIndicatorColor = Line
                    )
                )
                Spacer(Modifier.size(10.dp))
                Box(
                    Modifier.size(52.dp).clip(CircleShape).background(Grn)
                        .clickable { vm.sendMsg(txt); txt = "" },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, "send", tint = Color0, modifier = Modifier.size(22.dp))
                }
            }
        }
    ) {
        Column(Modifier.fillMaxSize()) {
            Text(
                vm.chName, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Ink,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            if (vm.msgs.isEmpty()) {
                Empty(tr(vm, "Say hello"))
            } else {
                val me = vm.acct?.id ?: 0
                LazyColumn(
                    state = list,
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(vm.msgs) { _, m ->
                        val mine = m.frm == me
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = if (mine) Arrangement.End else Arrangement.Start
                        ) {
                            Text(
                                m.body,
                                color = if (mine) Color0 else Ink,
                                fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (mine) Grn else Line)
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
