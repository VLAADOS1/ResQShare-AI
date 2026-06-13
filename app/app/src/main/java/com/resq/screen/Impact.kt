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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Impact(vm: Vm) {
    LaunchedEffect(Unit) { vm.load() }
    val s = vm.stat
    val rw = vm.rewards
    val done = rw.count { vm.myVal(it.metric) >= it.goal }
    val gifts = rw.filter { it.gift != null && vm.myVal(it.metric) >= it.goal }
    var sel by remember { mutableStateOf<Reward?>(null) }
    sel?.let { RewardDlg(vm, it) { sel = null } }
    Scaf(vm, back = false) {
        Hero(tr(vm, "Impact dashboard"), tr(vm, "What we've achieved together."), gHero)
        Spacer(Modifier.height(16.dp))
        Row {
            Tile("${s?.kg ?: 0.0} kg", tr(vm, "Food rescued"), Grn, Modifier.weight(1f))
            Spacer(Modifier.width(10.dp))
            Tile("${s?.meals ?: 0}", tr(vm, "Meals provided"), Orng, Modifier.weight(1f))
        }
        Spacer(Modifier.height(10.dp))
        Row {
            Tile("${s?.co2 ?: 0.0} kg", tr(vm, "CO2 avoided"), Blue, Modifier.weight(1f))
            Spacer(Modifier.width(10.dp))
            Tile("${s?.items ?: 0}", tr(vm, "Items reused"), GrnD, Modifier.weight(1f))
        }
        Spacer(Modifier.height(10.dp))
        Row {
            Tile("${s?.people ?: 0}", tr(vm, "People helped"), Orng, Modifier.weight(1f))
            Spacer(Modifier.width(10.dp))
            Tile("${s?.orgs ?: 0}", tr(vm, "Organizations"), Blue, Modifier.weight(1f))
        }
        Spacer(Modifier.height(16.dp))

        Pane {
            Lbl(tr(vm, "Your badges") + "  $done/${rw.size}")
            Prog(if (rw.isEmpty()) 0f else done / rw.size.toFloat(), Grn)
            Spacer(Modifier.height(6.dp))
            Text(tr(vm, "Tap a badge to see how to unlock it."), color = Sub, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(14.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (r in rw) Box(Modifier.clickable { sel = r }) {
                    Badge(vm, Ach(r.emoji, tr(vm, r.title), vm.myVal(r.metric) >= r.goal))
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        if (gifts.isNotEmpty()) {
            Pane {
                Lbl(tr(vm, "Your gifts"))
                for (g in gifts) {
                    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                        Text(g.emoji, fontSize = 20.sp)
                        Text("  " + (g.gift ?: ""), color = Ink, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        Pane {
            Lbl(tr(vm, "How it works"))
            Step(tr(vm, "Every completed donation counts toward your badges."))
            Step(tr(vm, "Help more people to unlock higher rewards."))
            Step(tr(vm, "Organizations multiply your impact across the city."))
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun Step(t: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
        Text("•  ", color = Grn, fontWeight = FontWeight.ExtraBold)
        Text(t, color = Ink, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun RewardDlg(vm: Vm, r: Reward, onClose: () -> Unit) {
    val cur = vm.myVal(r.metric)
    val got = cur >= r.goal
    val prog = if (r.goal <= 0) 1f else (cur / r.goal).toFloat().coerceIn(0f, 1f)
    val left = (r.goal - cur).coerceAtLeast(0.0)
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onClose,
        containerColor = Card,
        confirmButton = {
            Text(
                tr(vm, "Close"), color = Grn, fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.clickable { onClose() }.padding(12.dp)
            )
        },
        icon = { Text(if (got) r.emoji else "🔒", fontSize = 40.sp) },
        title = { Text(tr(vm, r.title), fontWeight = FontWeight.ExtraBold, color = Ink) },
        text = {
            Column {
                val d = r.descr
                if (!d.isNullOrBlank()) {
                    Text(tr(vm, d), color = Sub, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(14.dp))
                }
                Lbl(tr(vm, "Requirement"))
                Text(tr(vm, "Reach") + " ${r.goal} " + metricLbl(vm, r.metric), color = Ink, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                Prog(prog, if (got) Grn else Orng)
                Text(num(cur) + " / ${r.goal}", color = Sub, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                val g = r.gift
                if (!g.isNullOrBlank()) {
                    Spacer(Modifier.height(14.dp))
                    Lbl(tr(vm, "Gift"))
                    Text("🎁  " + tr(vm, g), color = Ink, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(14.dp))
                if (got) {
                    Tag(tr(vm, "Unlocked"), GrnL, GrnD)
                } else {
                    Text(num(left) + " " + metricLbl(vm, r.metric) + " " + tr(vm, "to go"), color = Orng, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                }
            }
        }
    )
}

private fun metricLbl(vm: Vm, m: String): String = tr(
    vm, when (m) {
        "people" -> "people helped"
        "kg" -> "kg rescued"
        "co2" -> "kg CO2 avoided"
        "items" -> "items reused"
        "orgs" -> "partner organizations"
        else -> "meals provided"
    }
)

private fun num(v: Double): String = if (v % 1.0 == 0.0) v.toInt().toString() else String.format("%.1f", v)
