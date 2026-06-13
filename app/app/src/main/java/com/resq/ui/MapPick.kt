package com.resq.ui

import android.content.Context
import android.location.Geocoder
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.resq.Vm
import com.resq.tr
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon

private fun inBox(lat: Double, lng: Double): Boolean =
    lat in 24.0..49.5 && lng in -125.0..-66.5

private fun coordStr(p: GeoPoint): String = String.format("%.4f, %.4f", p.latitude, p.longitude)

private fun circle(map: MapView, center: GeoPoint, radKm: Int?) {
    map.overlays.removeAll { it is Polygon }
    if (radKm != null && radKm > 0) {
        val poly = Polygon(map)
        poly.points = Polygon.pointsAsCircle(center, radKm * 1000.0)
        poly.fillPaint.color = 0x3358CC02
        poly.outlinePaint.color = android.graphics.Color.parseColor("#58CC02")
        poly.outlinePaint.strokeWidth = 4f
        map.overlays.add(0, poly)
    }
}

private fun zoomFor(radKm: Int?): Double = when {
    radKm == null -> 15.0
    radKm <= 2 -> 14.0
    radKm <= 5 -> 12.5
    radKm <= 15 -> 11.0
    else -> 9.5
}

@Composable
fun MapBox(latInit: Double?, lngInit: Double?, mod: Modifier, tap: Boolean, usaMsg: String, radKm: Int? = null, onPick: (Double, Double, String) -> Unit) {
    AndroidView(
        modifier = mod.clipToBounds(),
        factory = { ctx ->
            val cfg = Configuration.getInstance()
            cfg.userAgentValue = ctx.packageName
            cfg.osmdroidBasePath = ctx.cacheDir
            cfg.osmdroidTileCache = java.io.File(ctx.cacheDir, "tiles")
            val map = MapView(ctx)
            map.setTileSource(TileSourceFactory.MAPNIK)
            map.setMultiTouchControls(tap)
            val start = GeoPoint(latInit ?: 40.7128, lngInit ?: -74.0060)
            map.controller.setZoom(zoomFor(radKm))
            map.controller.setCenter(start)
            circle(map, start, radKm)
            val mk = Marker(map)
            mk.position = start
            mk.isDraggable = tap
            mk.icon = pinIcon(ctx)
            mk.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            map.overlays.add(mk)
            var good = start
            if (tap) {
                mk.setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
                    override fun onMarkerDrag(m: Marker) {}
                    override fun onMarkerDragStart(m: Marker) {}
                    override fun onMarkerDragEnd(m: Marker) {
                        val prev = good
                        val p = m.position
                        circle(map, p, radKm); map.invalidate()
                        onPick(p.latitude, p.longitude, coordStr(p))
                        geo(ctx, p, usaMsg,
                            { la, ln, nm -> good = GeoPoint(la, ln); onPick(la, ln, nm) },
                            { mk.position = prev; circle(map, prev, radKm); map.invalidate(); onPick(prev.latitude, prev.longitude, coordStr(prev)) })
                    }
                })
                val recv = object : MapEventsReceiver {
                    override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                        val prev = good
                        mk.position = p
                        circle(map, p, radKm); map.invalidate()
                        onPick(p.latitude, p.longitude, coordStr(p))
                        geo(ctx, p, usaMsg,
                            { la, ln, nm -> good = GeoPoint(la, ln); onPick(la, ln, nm) },
                            { mk.position = prev; circle(map, prev, radKm); map.invalidate(); onPick(prev.latitude, prev.longitude, coordStr(prev)) })
                        return true
                    }

                    override fun longPressHelper(p: GeoPoint) = false
                }
                map.overlays.add(0, MapEventsOverlay(recv))
            }
            map.onResume()
            map
        },
        update = { map ->
            if (latInit != null && lngInit != null) {
                val p = GeoPoint(latInit, lngInit)
                map.overlays.filterIsInstance<Marker>().firstOrNull()?.position = p
                map.controller.setCenter(p)
                circle(map, p, radKm)
                map.invalidate()
            }
        }
    )
}

@Composable
fun MapMini(vm: Vm, lat: Double?, lng: Double?, target: String, nav: NavHostController, radKm: Int? = null) {
    Box(Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(18.dp)).border(2.dp, Line, RoundedCornerShape(18.dp))) {
        MapBox(lat, lng, Modifier.fillMaxSize(), false, "", radKm) { _, _, _ -> }
        Box(
            Modifier.matchParentSize().clickable { vm.mapFor = target; nav.navigate("mapfull") },
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                tr(vm, "Tap to open the map"),
                color = Color0, fontSize = 13.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().background(dk(Ink)).padding(8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun MapFull(vm: Vm, nav: NavHostController) {
    val ctx = androidx.compose.ui.platform.LocalContext.current
    val isDona = vm.mapFor == "DONA"
    val lat = if (isDona) vm.dLat else vm.pLat
    val lng = if (isDona) vm.dLng else vm.pLng
    val area = if (isDona) vm.dArea else vm.pArea
    val set: (Double, Double, String) -> Unit = { la, ln, nm ->
        if (isDona) {
            vm.dLat = la; vm.dLng = ln; vm.dArea = nm
        } else {
            vm.pLat = la; vm.pLng = ln; vm.pArea = nm
        }
    }
    val msg = tr(vm, "Your point must be within the USA")
    val perm = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
    ) { ok -> if (ok) locate(ctx, msg, set) }
    Scaf(vm, nav, scroll = false, foot = {
        OutBtn(tr(vm, "Detect automatically")) {
            if (granted(ctx)) locate(ctx, msg, set)
            else perm.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        Spacer(Modifier.height(10.dp))
        Btn(tr(vm, "Done")) { nav.popBackStack() }
    }) {
        Column(Modifier.fillMaxSize()) {
            Text(
                if (area.isBlank()) tr(vm, "Tap the map to set a point") else area,
                color = if (area.isBlank()) Sub else Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            val rad = if (isDona && vm.dDeliver) vm.dRad else null
            MapBox(lat, lng, Modifier.fillMaxWidth().weight(1f), true, tr(vm, "Your point must be within the USA"), rad, set)
            if (rad != null) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    RadSlider(vm)
                }
            }
        }
    }
}

private fun granted(ctx: Context): Boolean =
    androidx.core.content.ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
        android.content.pm.PackageManager.PERMISSION_GRANTED

private fun locate(ctx: Context, usaMsg: String, onPick: (Double, Double, String) -> Unit) {
    try {
        val lm = ctx.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        val loc = lm.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER)
            ?: lm.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER)
            ?: lm.getLastKnownLocation(android.location.LocationManager.PASSIVE_PROVIDER)
        if (loc != null) {
            geo(ctx, GeoPoint(loc.latitude, loc.longitude), usaMsg, onPick, {})
        }
    } catch (e: SecurityException) {
    }
}

private fun pinIcon(ctx: Context): android.graphics.drawable.Drawable {
    val s = 88
    val bmp = android.graphics.Bitmap.createBitmap(s, s, android.graphics.Bitmap.Config.ARGB_8888)
    val cv = android.graphics.Canvas(bmp)
    val p = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG)
    val cx = s / 2f
    val cy = s / 2f
    p.color = 0x33000000
    cv.drawCircle(cx, cy + 4f, 30f, p)
    p.color = android.graphics.Color.WHITE
    cv.drawCircle(cx, cy, 30f, p)
    p.color = android.graphics.Color.parseColor("#58CC02")
    cv.drawCircle(cx, cy, 24f, p)
    p.color = android.graphics.Color.WHITE
    cv.drawCircle(cx, cy, 9f, p)
    return android.graphics.drawable.BitmapDrawable(ctx.resources, bmp)
}

private fun geo(ctx: Context, p: GeoPoint, usaMsg: String, ok: (Double, Double, String) -> Unit, rej: () -> Unit) {
    Thread {
        var country: String? = null
        val name = try {
            val a = Geocoder(ctx).getFromLocation(p.latitude, p.longitude, 1)?.firstOrNull()
            country = a?.countryCode
            val line = a?.getAddressLine(0)
            if (line != null && line.isNotBlank()) {
                line
            } else {
                val parts = listOfNotNull(a?.thoroughfare, a?.subLocality, a?.locality, a?.adminArea)
                if (parts.isNotEmpty()) parts.joinToString(", ")
                else String.format("%.5f, %.5f", p.latitude, p.longitude)
            }
        } catch (e: Exception) {
            String.format("%.5f, %.5f", p.latitude, p.longitude)
        }
        val us = country == "US" || (country == null && inBox(p.latitude, p.longitude))
        Handler(Looper.getMainLooper()).post {
            if (us) {
                ok(p.latitude, p.longitude, name)
            } else {
                android.widget.Toast.makeText(ctx, usaMsg, android.widget.Toast.LENGTH_SHORT).show()
                rej()
            }
        }
    }.start()
}
