package com.resq

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.resq.screen.*
import com.resq.ui.*

private val tabs = setOf("feed", "home", "dash", "impact", "profil", "reqnew", "reqs", "income", "map", "msgs", "needs")

@Composable
fun Nav(vm: Vm) {
    val nav = rememberNavController()
    val ent by nav.currentBackStackEntryAsState()
    val route = ent?.destination?.route ?: "onbrd"
    val start = if (vm.acct != null) {
        when (vm.role) {
            "RECIP" -> "feed"
            "ORG" -> "dash"
            else -> "home"
        }
    } else {
        "onbrd"
    }
    val bars = safeBars()
    Box(
        Modifier.fillMaxSize().background(Bg)
            .padding(top = bars.calculateTopPadding(), bottom = bars.calculateBottomPadding())
    ) {
    Scaffold(
        containerColor = Bg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = { if (route in tabs) Bar(vm, route, nav) }
    ) { pad ->
        NavHost(nav, start, Modifier.padding(pad)) {
            composable("onbrd") { Onbrd(vm, nav) }
            composable("login") { Login(vm, nav) }
            composable("reg") { Reg(vm, nav) }
            composable("rprof") { Rprof(vm, nav) }
            composable("dprof") { Dprof(vm, nav) }
            composable("oprof") { Oprof(vm, nav) }
            composable("feed") { Feed(vm, nav) }
            composable("reqnew") { MyReqs(vm, nav) }
            composable("reqform") { ReqNew(vm, nav) }
            composable("reqs") { Reqs(vm) }
            composable("home") { Home(vm, nav) }
            composable("create") { Create(vm, nav) }
            composable("result") { Result(vm, nav) }
            composable("match") { Match(vm, nav) }
            composable("detail") { Detail(vm, nav) }
            composable("dash") { Dash(vm, nav) }
            composable("income") { Income(vm) }
            composable("map") { MapScr(vm) }
            composable("impact") { Impact(vm) }
            composable("profil") { Profil(vm, nav) }
            composable("msgs") { Thrds(vm, nav) }
            composable("chat") { Chat(vm, nav) }
            composable("mapfull") { MapFull(vm, nav) }
            composable("admin") { Admin(vm, nav) }
            composable("admrew") { AdmRew(vm, nav) }
            composable("admdonas") { AdmDonas(vm, nav) }
            composable("admaccts") { AdmAccts(vm, nav) }
            composable("admrevs") { AdmRevs(vm, nav) }
            composable("celebrate") { Celebrate(vm, nav) }
            composable("needs") { Needs(vm, nav) }
            composable("faq") { Faq(vm, nav) }
            composable("about") { About(vm, nav) }
            composable("terms") { Terms(vm, nav) }
            composable("privacy") { Privacy(vm, nav) }
            composable("editp") { EditProf(vm, nav) }
            composable("places") { Places(vm, nav) }
            composable("rate") { Rate(vm, nav) }
        }
    }
    }
}

@Composable
private fun Bar(vm: Vm, route: String, nav: NavHostController) {
    val items = when (vm.role) {
        "RECIP" -> listOf(
            Tab("feed", "Home", Icons.Filled.Home),
            Tab("reqnew", "Request", Icons.Filled.Add),
            Tab("msgs", "Messages", Icons.AutoMirrored.Filled.Chat),
            Tab("profil", "Profile", Icons.Filled.Person)
        )
        "ORG" -> listOf(
            Tab("dash", "Dashboard", Icons.Filled.Dashboard),
            Tab("income", "Incoming", Icons.Filled.Inbox),
            Tab("map", "Map", Icons.Filled.Place),
            Tab("impact", "Impact", Icons.Filled.Insights),
            Tab("profil", "Profile", Icons.Filled.Person)
        )
        else -> listOf(
            Tab("home", "Home", Icons.Filled.Home),
            Tab("needs", "Requests", Icons.Filled.FavoriteBorder),
            Tab("msgs", "Messages", Icons.AutoMirrored.Filled.Chat),
            Tab("impact", "Impact", Icons.Filled.Insights),
            Tab("profil", "Profile", Icons.Filled.Person)
        )
    }
    Column(Modifier.fillMaxWidth().background(Card)) {
        Box(Modifier.fillMaxWidth().height(2.dp).background(Line))
        Row(
            Modifier.fillMaxWidth().padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEach { t ->
                Nbtn(t.ic, tr(vm, t.lab), route == t.go) {
                    if (route != t.go) {
                        nav.navigate(t.go) { launchSingleTop = true }
                    }
                }
            }
        }
    }
}

@Composable
private fun safeBars(): PaddingValues {
    val d = LocalDensity.current
    val ctx = LocalContext.current
    val navLive = WindowInsets.navigationBars.getBottom(d)
    val statLive = WindowInsets.statusBars.getTop(d)
    val nav = if (navLive > 0) navLive else dimen(ctx, "navigation_bar_height")
    val stat = if (statLive > 0) statLive else dimen(ctx, "status_bar_height")
    return with(d) { PaddingValues(top = stat.toDp(), bottom = nav.toDp()) }
}

private fun dimen(ctx: android.content.Context, name: String): Int {
    val id = ctx.resources.getIdentifier(name, "dimen", "android")
    return if (id > 0) ctx.resources.getDimensionPixelSize(id) else 0
}

private data class Tab(val go: String, val lab: String, val ic: ImageVector)
