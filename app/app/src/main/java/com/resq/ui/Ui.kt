package com.resq.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.resq.Vm
import com.resq.tr

private val r14 = RoundedCornerShape(14.dp)
private val r16 = RoundedCornerShape(16.dp)
private val r18 = RoundedCornerShape(18.dp)

@Composable
fun Scaf(
    vm: Vm,
    nav: NavHostController? = null,
    back: Boolean = true,
    lng: Boolean = false,
    scroll: Boolean = true,
    onBack: (() -> Unit)? = null,
    foot: @Composable (() -> Unit)? = null,
    body: @Composable ColumnScope.() -> Unit
) {
    val showBack = onBack != null || (back && nav != null)
    Column(Modifier.fillMaxSize().background(Bg)) {
        if (showBack || lng) {
            Column(Modifier.fillMaxWidth().background(Card)) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (showBack) {
                        Box(
                            Modifier.size(40.dp).clip(r14).background(Bg).border(2.dp, Line, r14)
                                .clickable { onBack?.invoke() ?: nav?.popBackStack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "back", tint = Ink, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    if (lng) Lng(vm)
                }
                Box(Modifier.fillMaxWidth().height(2.dp).background(Line))
            }
        }
        val inner = if (scroll) {
            Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(16.dp)
        } else {
            Modifier.weight(1f).fillMaxWidth()
        }
        Column(inner, content = body)
        if (foot != null) {
            Column(Modifier.fillMaxWidth().background(Card)) {
                Box(Modifier.fillMaxWidth().height(2.dp).background(Line))
                Column(Modifier.fillMaxWidth().padding(16.dp), content = { foot() })
            }
        }
    }
}

@Composable
fun Choice(em: String, bg: Color, ttl: String, sub: String, tap: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(r18).background(Card).border(2.dp, Line, r18)
            .clickable { tap() }.padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(56.dp).clip(r16).background(bg), contentAlignment = Alignment.Center) {
            Text(em, fontSize = 28.sp)
        }
        Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
            Text(ttl, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
            Text(sub, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Sub)
        }
        Icon(Icons.Filled.ChevronRight, null, tint = Sub, modifier = Modifier.size(24.dp))
    }
}

@Composable
fun Pane(pad: Int = 18, body: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(r18)
            .background(Card)
            .border(2.dp, Line, r18)
            .padding(pad.dp),
        content = body
    )
}

@Composable
fun Hero(ttl: String, sub: String, g: Brush = gHero, body: @Composable ColumnScope.() -> Unit = {}) {
    Column(Modifier.fillMaxWidth()) {
        Text(ttl, color = Ink, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(6.dp))
        Box(Modifier.size(46.dp, 6.dp).clip(RoundedCornerShape(3.dp)).background(g))
        if (sub.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(sub, color = Sub, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
        body()
    }
}

@Composable
fun Btn(txt: String, fill: Color = Grn, mod: Modifier = Modifier, tap: () -> Unit) {
    val src = remember { MutableInteractionSource() }
    Box(mod.fillMaxWidth().clip(r16).background(dk(fill))) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
                .clip(r16)
                .background(fill)
                .clickable(interactionSource = src, indication = null) { tap() }
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                txt.uppercase(), color = Color0, fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp, letterSpacing = 0.6.sp, textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun OutBtn(txt: String, mod: Modifier = Modifier, tap: () -> Unit) {
    val src = remember { MutableInteractionSource() }
    Box(mod.fillMaxWidth().clip(r16).background(Line)) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
                .clip(r16)
                .background(Card)
                .border(2.dp, Line, r16)
                .clickable(interactionSource = src, indication = null) { tap() }
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                txt.uppercase(), color = GrnD, fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp, letterSpacing = 0.6.sp, textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun Pill(txt: String, on: Boolean, tap: () -> Unit) {
    Text(
        txt,
        color = if (on) GrnD else Sub,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(r14)
            .background(if (on) GrnL else Card)
            .border(2.dp, if (on) Grn else Line, r14)
            .clickable { tap() }
            .padding(horizontal = 15.dp, vertical = 9.dp)
    )
}

@Composable
fun Tag(txt: String, bg: Color, fg: Color) {
    Text(
        txt,
        color = fg,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(bg).padding(horizontal = 11.dp, vertical = 6.dp)
    )
}

@Composable
fun Safe(vm: Vm, s: String) {
    val (bg, fg, lb) = badge(s)
    Row(
        Modifier.clip(RoundedCornerShape(10.dp)).background(bg).padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(7.dp).clip(CircleShape).background(fg))
        Text("  " + tr(vm, lb), color = fg, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

private fun badge(s: String): Triple<Color, Color, String> = when (s) {
    "SAFE" -> Triple(GrnL, GrnD, "Safe")
    "SOON" -> Triple(OrngL, Orng, "Donate soon")
    "ORGONLY" -> Triple(BlueL, Blue, "Organization-only")
    "REVIEW" -> Triple(OrngL, Orng, "Needs review")
    "UNSAFE" -> Triple(RedL, Red, "Not safe")
    "RECYCLE" -> Triple(Line, Sub, "Recycle")
    else -> Triple(GrnL, GrnD, s)
}

@Composable
fun Field(lab: String, v: String, pass: Boolean = false, lines: Int = 1, on: (String) -> Unit) {
    OutlinedTextField(
        value = v,
        onValueChange = on,
        label = { Text(lab) },
        shape = r16,
        singleLine = lines <= 1,
        minLines = lines,
        visualTransformation = if (pass) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Card,
            unfocusedContainerColor = Card,
            focusedIndicatorColor = Grn,
            unfocusedIndicatorColor = Line,
            focusedLabelColor = Grn,
            unfocusedLabelColor = Sub
        )
    )
}

@Composable
fun Tile(big: String, lab: String, fg: Color, mod: Modifier = Modifier) {
    Column(
        mod.clip(r18).background(Card).border(2.dp, Line, r18).padding(16.dp)
    ) {
        Text(big, color = fg, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        Text(lab, color = Sub, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun Cicon(em: String, bg: Color, mod: Modifier = Modifier) {
    Box(mod.size(48.dp).clip(r16).background(bg), contentAlignment = Alignment.Center) {
        Text(em, fontSize = 23.sp)
    }
}

@Composable
fun Prog(v: Float, c: Color) {
    Box(Modifier.fillMaxWidth().height(16.dp).clip(RoundedCornerShape(8.dp)).background(Line)) {
        Box(Modifier.fillMaxWidth(v.coerceIn(0f, 1f)).height(16.dp).clip(RoundedCornerShape(8.dp)).background(c))
    }
}

@Composable
fun Empty(txt: String) {
    Column(
        Modifier.fillMaxWidth().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.size(72.dp).clip(CircleShape).background(GrnL), contentAlignment = Alignment.Center) {
            Text("🦉", fontSize = 34.sp)
        }
        Spacer(Modifier.height(12.dp))
        Text(txt, color = Sub, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun Skel() {
    Column(
        Modifier.fillMaxWidth().padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = Grn, strokeWidth = 5.dp, modifier = Modifier.size(52.dp))
        Spacer(Modifier.height(16.dp))
        Text("⏳", fontSize = 24.sp)
    }
}

@Composable
fun Nbtn(ic: ImageVector, lab: String, on: Boolean, tap: () -> Unit) {
    val src = remember { MutableInteractionSource() }
    Column(
        Modifier.clickable(interactionSource = src, indication = null) { tap() }.padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier.clip(r14).background(if (on) GrnL else Color(0x00000000)).padding(horizontal = 16.dp, vertical = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(ic, lab, tint = if (on) GrnD else Sub, modifier = Modifier.size(23.dp))
        }
        Spacer(Modifier.height(2.dp))
        Text(lab, color = if (on) GrnD else Sub, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun Lng(vm: Vm) {
    Row(
        Modifier.clip(r16).background(Card).border(2.dp, Line, r16).padding(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Seg("EN", vm.lang == "EN") { if (vm.lang == "IT") vm.togl() }
        Seg("IT", vm.lang == "IT") { if (vm.lang == "EN") vm.togl() }
    }
}

@Composable
private fun Seg(t: String, on: Boolean, tap: () -> Unit) {
    Text(
        t,
        color = if (on) Color0 else Sub,
        fontSize = 13.sp,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(if (on) Grn else Color0).clickable { tap() }.padding(horizontal = 13.dp, vertical = 6.dp)
    )
}

@Composable
fun Dot(c: Color) {
    Box(Modifier.size(12.dp).clip(CircleShape).background(c))
}

@Composable
fun Lbl(t: String) {
    Text(t, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Ink, modifier = Modifier.padding(bottom = 8.dp))
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Grp(vm: Vm, opts: List<String>, sel: String, on: (String) -> Unit) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        opts.forEach { o -> Pill(tr(vm, o), sel == o) { on(o) } }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Gcat(vm: Vm, sel: String, on: (String) -> Unit) {
    val cs = listOf("FOOD" to "Food", "CLOTH" to "Clothes", "HYG" to "Hygiene", "BABY" to "Baby", "SCHOOL" to "School", "OTHER" to "Other")
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        cs.forEach { p -> Pill(tr(vm, p.second), sel == p.first) { on(p.first) } }
    }
}
