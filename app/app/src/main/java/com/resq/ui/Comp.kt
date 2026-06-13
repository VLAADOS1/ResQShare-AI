package com.resq.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.resq.Vm
import com.resq.cname
import com.resq.emoji
import com.resq.tr
import com.resq.data.*
import java.io.ByteArrayOutputStream

private val r16 = RoundedCornerShape(16.dp)
private val r18 = RoundedCornerShape(18.dp)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Opts(vm: Vm, top: List<String>, more: List<String>, sel: String, on: (String) -> Unit) {
    var exp by remember { mutableStateOf(false) }
    val known = top + more
    var other by remember { mutableStateOf(if (sel.isNotBlank() && sel !in known) sel else "") }
    val show = if (exp) known else top
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        show.forEach { o -> Pill(tr(vm, o), sel == o) { other = ""; on(o) } }
        if (more.isNotEmpty()) {
            Pill(tr(vm, if (exp) "Less" else "More"), false) { exp = !exp }
        }
    }
    Spacer(Modifier.height(10.dp))
    Field(tr(vm, "Or write your own"), other) { other = it; on(it) }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Multi(vm: Vm, opts: List<String>, sel: String, on: (String) -> Unit) {
    val set = sel.split(",").map { it.trim() }.filter { it.isNotBlank() }
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        opts.forEach { o ->
            val active = o in set
            Pill(tr(vm, o), active) {
                val next = if (active) set - o else set + o
                on(next.joinToString(","))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Cats(vm: Vm) {
    val known = listOf("Food" to "FOOD", "Clothes" to "CLOTH", "Hygiene" to "HYG", "Baby" to "BABY", "School" to "SCHOOL")
    val extra = listOf(
        "Toys", "Books", "Furniture", "Electronics", "Medicine", "Household", "Shoes",
        "Water", "Diapers", "Formula", "Blankets", "Bedding", "Kitchenware", "Appliances",
        "Stationery", "Pet food", "Tools", "First aid", "Toiletries", "Winter gear", "Other"
    )
    var exp by remember { mutableStateOf(false) }
    val labels = known.map { it.first } + extra
    var custom by remember { mutableStateOf(if (vm.dClbl.isNotBlank() && vm.dClbl !in labels) vm.dClbl else "") }
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        known.forEach { (lbl, code) ->
            Pill(tr(vm, lbl), vm.dClbl == lbl) { vm.dClbl = lbl; vm.dCat = code; custom = "" }
        }
        if (exp) {
            extra.forEach { lbl ->
                Pill(tr(vm, lbl), vm.dClbl == lbl) { vm.dClbl = lbl; vm.dCat = "OTHER"; custom = "" }
            }
        }
        Pill(tr(vm, if (exp) "Less" else "More"), false) { exp = !exp }
    }
    Spacer(Modifier.height(10.dp))
    Field(tr(vm, "Or write your own"), custom) {
        custom = it
        if (it.isNotBlank()) {
            vm.dClbl = it
            vm.dCat = "OTHER"
        }
    }
}

data class Ach(val em: String, val title: String, val got: Boolean)

fun achs(s: Stats?): List<Ach> {
    val meals = s?.meals ?: 0
    val kg = s?.kg ?: 0.0
    val ppl = s?.people ?: 0
    val orgs = s?.orgs ?: 0
    val items = s?.items ?: 0
    val co2 = s?.co2 ?: 0.0
    return listOf(
        Ach("🌱", "First share", items >= 1),
        Ach("🍽", "100 meals", meals >= 100),
        Ach("♻", "50 kg saved", kg >= 50),
        Ach("🤝", "50 people", ppl >= 50),
        Ach("🏅", "5 partners", orgs >= 5),
        Ach("🌍", "100kg CO2", co2 >= 100)
    )
}

@Composable
fun Badge(vm: Vm, a: Ach) {
    Column(
        Modifier.width(98.dp).padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier.size(62.dp).clip(androidx.compose.foundation.shape.CircleShape)
                .background(if (a.got) GrnL else Bg)
                .border(2.dp, if (a.got) Grn else Line, androidx.compose.foundation.shape.CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(if (a.got) a.em else "🔒", fontSize = 26.sp)
        }
        Spacer(Modifier.height(6.dp))
        Text(
            tr(vm, a.title), fontSize = 11.sp, fontWeight = FontWeight.Bold,
            color = if (a.got) Ink else Sub, textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
fun Loc(vm: Vm, area: String, lat: Double?, lng: Double?, target: String, nav: androidx.navigation.NavHostController, radKm: Int? = null) {
    Lbl(tr(vm, "Location"))
    Text(
        if (area.isBlank()) tr(vm, "Tap the map to set a point") else area,
        color = if (area.isBlank()) Sub else Ink, fontSize = 14.sp, fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(10.dp))
    MapMini(vm, lat, lng, target, nav, radKm)
}

@Composable
fun RadSlider(vm: Vm) {
    Lbl(tr(vm, "Delivery radius (km)") + ": ${vm.dRad}")
    Text(tr(vm, "How far you are willing to deliver."), color = Sub, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    Slider(
        value = vm.dRad.toFloat(),
        onValueChange = { vm.dRad = it.toInt().coerceIn(1, 50) },
        valueRange = 1f..50f,
        colors = SliderDefaults.colors(thumbColor = Grn, activeTrackColor = Grn, inactiveTrackColor = Line)
    )
}

@Composable
fun Pic(iid: Long, mod: Modifier) {
    AsyncImage(model = Net.pic(iid), contentDescription = null, modifier = mod, contentScale = ContentScale.Crop)
}

@Composable
fun Img(id: Long, mod: Modifier) {
    AsyncImage(
        model = Net.img(id),
        contentDescription = null,
        modifier = mod,
        contentScale = ContentScale.Crop
    )
}

fun matches(d: Dona, q: String): Boolean {
    if (q.isBlank()) return true
    val s = q.trim().lowercase()
    return d.title.lowercase().contains(s) ||
        (d.clbl ?: "").lowercase().contains(s) ||
        d.qty.lowercase().contains(s) ||
        d.area.lowercase().contains(s) ||
        cname(d.cat).lowercase().contains(s)
}

@Composable
fun StatTag(vm: Vm, stat: String) {
    val (txt, bg, fg) = when (stat) {
        "RESERVED" -> Triple("Reserved", BlueL, Blue)
        "DONE", "PICKED" -> Triple("Given away", GrnL, GrnD)
        "SENT" -> Triple("Sent", BlueL, Blue)
        "REVIEW" -> Triple("Under review", OrngL, Orng)
        "CANCEL", "REJ" -> Triple("Cancelled", RedL, Red)
        else -> Triple("Available", GrnL, GrnD)
    }
    Tag(tr(vm, txt), bg, fg)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DonaCard(vm: Vm, d: Dona, body: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxWidth().clip(r18).background(Card).border(2.dp, Line, r18)) {
        Box(Modifier.fillMaxWidth().height(180.dp).background(ctint(d.cat)), contentAlignment = Alignment.Center) {
            if (d.pic) {
                Img(d.id, Modifier.fillMaxWidth().height(180.dp))
            } else {
                Text(emoji(d.cat), fontSize = 64.sp)
            }
        }
        Column(Modifier.padding(14.dp)) {
            Text(d.title, fontWeight = FontWeight.ExtraBold, color = Ink, fontSize = 17.sp)
            Text((d.clbl ?: "").ifBlank { tr(vm, cname(d.cat)) }, color = Sub, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                StatTag(vm, d.stat)
                if (d.deliv == "DELIVER" || d.deliv == "BOTH") Tag(tr(vm, "Delivery"), BlueL, Blue)
                if (d.test) Tag(tr(vm, "Test data"), OrngL, Orng)
            }
            if (d.pwin.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(d.pwin, color = Sub, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(12.dp))
            body()
        }
    }
}

private const val MAXPIC = 8

private fun decB64(s: String): Bitmap? = try {
    val by = Base64.decode(s.substringAfter(","), Base64.DEFAULT)
    BitmapFactory.decodeByteArray(by, 0, by.size)
} catch (e: Exception) {
    null
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Photo(vm: Vm) {
    val ctx = LocalContext.current
    val pick = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(MAXPIC)) { uris ->
        for (uri in uris) {
            if (vm.dImgs.size >= MAXPIC) break
            val raw = ctx.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            if (raw != null) {
                val small = shrink(raw)
                vm.dImgs.add("data:image/jpeg;base64," + Base64.encodeToString(small, Base64.NO_WRAP))
            }
        }
    }
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        vm.dImgs.forEachIndexed { i, s ->
            val bmp = remember(s) { decB64(s) }
            Box(Modifier.size(100.dp).clip(r16).background(Bg).border(2.dp, Line, r16)) {
                if (bmp != null) {
                    Image(bmp.asImageBitmap(), null, Modifier.matchParentSize().clip(r16), contentScale = ContentScale.Crop)
                }
                Box(
                    Modifier.align(Alignment.TopEnd).padding(5.dp).size(24.dp).clip(RoundedCornerShape(12.dp))
                        .background(Red).clickable { if (i < vm.dImgs.size) vm.dImgs.removeAt(i) },
                    contentAlignment = Alignment.Center
                ) { Text("✕", color = Color0, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold) }
            }
        }
        if (vm.dImgs.size < MAXPIC) {
            Column(
                Modifier.size(100.dp).clip(r16).background(Bg).border(2.dp, Line, r16)
                    .clickable { pick.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
            ) {
                Text("📷", fontSize = 28.sp)
                Text("+", color = Sub, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

private fun shrink(bytes: ByteArray): ByteArray {
    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return bytes
    val max = 1024
    val w = bmp.width
    val h = bmp.height
    val k = minOf(1f, max.toFloat() / maxOf(w, h))
    val nb = if (k < 1f) Bitmap.createScaledBitmap(bmp, (w * k).toInt(), (h * k).toInt(), true) else bmp
    val out = ByteArrayOutputStream()
    nb.compress(Bitmap.CompressFormat.JPEG, 80, out)
    return out.toByteArray()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tpick(vm: Vm) {
    var dd by remember { mutableStateOf("") }
    var tt by remember { mutableStateOf("") }
    var dlg by remember { mutableStateOf(0) }
    val today = java.time.LocalDate.now()
    val sel = remember {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                !java.time.Instant.ofEpochMilli(utcTimeMillis).atZone(java.time.ZoneOffset.UTC).toLocalDate().isBefore(today)

            override fun isSelectableYear(year: Int): Boolean = year >= today.year
        }
    }
    val ds = rememberDatePickerState(selectableDates = sel)
    val ts = rememberTimePickerState(initialHour = 18, initialMinute = 0)

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Slot(tr(vm, if (dd.isBlank()) "Pick date" else dd), Modifier.weight(1f)) { dlg = 1 }
        Slot(tr(vm, if (tt.isBlank()) "Pick time" else tt), Modifier.weight(1f)) { dlg = 2 }
    }

    if (dlg == 1) {
        DatePickerDialog(
            onDismissRequest = { dlg = 0 },
            confirmButton = {
                TextButton(onClick = {
                    ds.selectedDateMillis?.let {
                        dd = java.time.Instant.ofEpochMilli(it).atZone(java.time.ZoneId.systemDefault()).toLocalDate().toString()
                    }
                    vm.dPwin = (dd + " " + tt).trim()
                    dlg = 0
                }) { Text("OK") }
            }
        ) { DatePicker(ds) }
    }
    if (dlg == 2) {
        DatePickerDialog(
            onDismissRequest = { dlg = 0 },
            confirmButton = {
                TextButton(onClick = {
                    tt = String.format("%02d:%02d", ts.hour, ts.minute)
                    vm.dPwin = (dd + " " + tt).trim()
                    dlg = 0
                }) { Text("OK") }
            }
        ) { Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) { TimePicker(ts) } }
    }
}

@Composable
private fun Slot(txt: String, mod: Modifier, tap: () -> Unit) {
    Row(
        mod.clip(r16).background(Bg).border(2.dp, Line, r16).clickable { tap() }.padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(txt, color = Ink, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}
