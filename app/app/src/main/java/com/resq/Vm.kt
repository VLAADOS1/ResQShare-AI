package com.resq

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.resq.data.*
import kotlinx.coroutines.launch

class Vm : ViewModel() {

    var role by mutableStateOf("")
    var intent by mutableStateOf("NEED")
    var acct by mutableStateOf<Acct?>(null)
    var email by mutableStateOf("")
    var pass by mutableStateOf("")
    var pass2 by mutableStateOf("")
    var lang by mutableStateOf("EN")
    var did by mutableStateOf(0L)

    var pType by mutableStateOf("")
    var pName by mutableStateOf("")
    var pBio by mutableStateOf("")
    var pArea by mutableStateOf("")
    var pLat by mutableStateOf<Double?>(null)
    var pLng by mutableStateOf<Double?>(null)
    var pRad by mutableStateOf(5)
    var pNeeds by mutableStateOf("Food")
    var pDiet by mutableStateOf("None")
    var pAvail by mutableStateOf("Evening")
    var pUrg by mutableStateOf("MED")
    var pAcc by mutableStateOf("Food")
    var pDist by mutableStateOf(10)
    var pCap by mutableStateOf(20)

    var feed by mutableStateOf<List<Dona>>(emptyList())
    var mine by mutableStateOf<List<Dona>>(emptyList())
    var orgs by mutableStateOf<List<Org>>(emptyList())
    var reqs by mutableStateOf<List<Req>>(emptyList())
    var income by mutableStateOf<List<Offer>>(emptyList())
    var stat by mutableStateOf<Stats?>(null)
    var tip by mutableStateOf("")

    var ana by mutableStateOf<AnaOut?>(null)
    var cur by mutableStateOf<Dona?>(null)
    var mchs by mutableStateOf<List<Mch>>(emptyList())

    var busy by mutableStateOf(false)
    var err by mutableStateOf("")
    var done by mutableStateOf(false)

    var dCat by mutableStateOf("FOOD")
    var dClbl by mutableStateOf("Food")
    var dPickup by mutableStateOf(true)
    var dDeliver by mutableStateOf(false)
    var curDraft by mutableStateOf(0L)
    val checkAns = mutableStateMapOf<Int, Boolean>()
    var dQty by mutableStateOf("")
    var dStor by mutableStateOf("")
    var dCond by mutableStateOf("")
    var dPwin by mutableStateOf("")
    var dArea by mutableStateOf("")
    var dNote by mutableStateOf("")
    var dPhoto by mutableStateOf("")
    val dImgs = mutableStateListOf<String>()
    var curImgs by mutableStateOf<List<Long>>(emptyList())
    var dLat by mutableStateOf<Double?>(null)
    var dLng by mutableStateOf<Double?>(null)
    var scanning by mutableStateOf(false)
    var scanImg by mutableStateOf("")

    var mapFor by mutableStateOf("DONA")
    var sortNew by mutableStateOf(true)
    var dRad by mutableStateOf(5)
    var curDonor by mutableStateOf<Donr?>(null)
    var fSortNew by mutableStateOf(true)
    var fCats by mutableStateOf<Set<String>>(emptySet())
    var fDeliv by mutableStateOf(false)
    var fNearby by mutableStateOf(false)
    var ratings by mutableStateOf<List<Rating>>(emptyList())
    var rateSum by mutableStateOf<RateSum?>(null)
    var board by mutableStateOf("ACTIVE")
    var feedQ by mutableStateOf("")
    var admQ by mutableStateOf("")
    var myQ by mutableStateOf("")

    var accts by mutableStateOf<List<Acct>>(emptyList())
    var adons by mutableStateOf<List<Dona>>(emptyList())
    var sum by mutableStateOf<Sum?>(null)
    var rewards by mutableStateOf<List<Reward>>(emptyList())

    var thrds by mutableStateOf<List<Thrd>>(emptyList())
    var msgs by mutableStateOf<List<Msg>>(emptyList())
    var chOther by mutableStateOf(0L)
    var chName by mutableStateOf("")
    var chDona by mutableStateOf(0L)

    var drafts by mutableStateOf<List<Draft>>(emptyList())
    var reads by mutableStateOf<Map<Long, Long>>(emptyMap())

    var rCat by mutableStateOf("FOOD")
    var rDesc by mutableStateOf("")
    var rQty by mutableStateOf("")
    var rUrg by mutableStateOf("MED")
    var rRad by mutableStateOf(5)
    var rAvail by mutableStateOf("Evening")
    var rRestr by mutableStateOf("")
    var rPub by mutableStateOf("")
    var rRev by mutableStateOf(false)

    var myStat by mutableStateOf<Stats?>(null)
    var credit by mutableStateOf<Gain?>(null)
    var creditTo by mutableStateOf("")

    private val api = Net.api
    private var lastNotif = System.currentTimeMillis()

    init {
        lang = Store.lang()
        if (lang == "RU") lang = "IT"
        Store.sess()?.let { s ->
            acct = s.acct
            role = s.role
            did = s.did
            pArea = s.pArea
            pLat = s.pLat
            pLng = s.pLng
            pRad = s.pRad
        }
        drafts = Store.drafts()
        reads = Store.reads().mapKeys { it.key.toLong() }
        poll()
    }

    private fun poll() {
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(10000)
                val a = acct ?: continue
                try {
                    val ts = api.threads(a.id)
                    for (t in ts) {
                        if (t.lastFrm != a.id && t.ts > lastNotif && t.other != chOther) {
                            Notif.show(t.oname.ifBlank { "New message" }, t.last)
                        }
                    }
                    lastNotif = maxOf(lastNotif, ts.maxOfOrNull { it.ts } ?: 0L)
                    thrds = ts
                } catch (e: Exception) {
                }
            }
        }
    }

    private fun saveSess() {
        val a = acct ?: return
        Store.saveSess(Sess(a, role, did, lang, pArea, pLat, pLng, pRad))
    }

    fun setRad(n: Int) {
        pRad = n
        if (acct != null) saveSess()
    }

    fun logout() {
        Store.saveSess(null)
        acct = null
        role = ""
        did = 0
    }

    fun delivStr(): String = if (dPickup && dDeliver) "BOTH" else if (dDeliver) "DELIVER" else "PICKUP"

    fun newDona() {
        ana = null
        cur = null
        mchs = emptyList()
        curDraft = 0
        dCat = "FOOD"; dClbl = "Food"; dQty = ""; dCond = ""; dStor = ""; dPwin = ""
        dNote = ""; dPhoto = ""; dImgs.clear(); dArea = ""; dLat = null; dLng = null
        dPickup = true; dDeliver = false; dRad = 5
        scanning = false; scanImg = ""
        checkAns.clear()
    }

    fun saveDraft() {
        val id = if (curDraft > 0) curDraft else System.currentTimeMillis()
        curDraft = id
        val d = Draft(id, ana, dCat, dClbl, dQty, dCond, dStor, dPwin, dArea, dLat, dLng, dNote, dImgs.toList(), delivStr())
        drafts = listOf(d) + drafts.filter { it.id != id }.take(19)
        Store.saveDrafts(drafts)
    }

    fun openDraft(d: Draft) {
        ana = d.ana
        curDraft = d.id
        dCat = d.cat; dClbl = d.clbl; dQty = d.qty; dCond = d.cond; dStor = d.stor
        dPwin = d.pwin; dArea = d.area; dLat = d.lat; dLng = d.lng
        dNote = d.note; dImgs.clear(); dImgs.addAll(d.imgs)
        dPickup = d.deliv != "DELIVER"
        dDeliver = d.deliv == "DELIVER" || d.deliv == "BOTH"
        checkAns.clear()
    }

    fun delDraft(d: Draft) {
        drafts = drafts.filter { it.id != d.id }
        Store.saveDrafts(drafts)
    }

    fun pick(r: String) {
        role = r
    }

    fun togl() {
        lang = if (lang == "EN") "IT" else "EN"
        Store.saveLang(lang)
        if (acct != null) saveSess()
    }

    fun badReg(): String {
        val e = email.trim()
        if (!e.matches(Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))) return "Enter a valid email"
        if (pass.length < 6) return "Password must be at least 6 characters"
        if (pass != pass2) return "Passwords don't match"
        return ""
    }

    fun loadAdmin() = go {
        accts = api.adminAccts()
        adons = api.adminDonas()
        sum = api.adminSum()
        rewards = api.rewards()
        ratings = api.ratings()
        rateSum = api.rateSum()
    }

    fun delDona(id: Long) = go {
        api.adminDelDona(id)
        adons = api.adminDonas()
        sum = api.adminSum()
    }

    fun ban(id: Long, v: Boolean) = go {
        api.adminBan(id, v)
        accts = api.adminAccts()
    }

    fun delAcct(id: Long) = go {
        api.adminDelAcct(id)
        accts = api.adminAccts()
        sum = api.adminSum()
    }

    fun reg() = go {
        val bad = badReg()
        if (bad.isNotBlank()) {
            err = bad
            return@go
        }
        val nm = if (pName.isNotBlank()) pName else pType
        val a = api.reg(RegIn(email.trim(), pass, role, nm, pArea))
        acct = a
        when (role) {
            "DONOR" -> {
                val d = api.donr(Donr(acc = a.id, type = pType, name = pName, area = pArea, lat = pLat, lng = pLng, dtype = dCat, pref = "pickup", bio = pBio))
                did = d.id
                api.link(a.id, d.id)
            }
            "RECIP" -> {
                val r = api.recp(Recp(acc = a.id, atype = pType, area = pArea, lat = pLat, lng = pLng, radius = pRad, needs = pNeeds, diet = pDiet, avail = pAvail, urg = pUrg))
                api.link(a.id, r.id)
            }
            "ORG" -> {
                val o = api.addOrg(Org(acc = a.id, otype = pType, name = pName, area = pArea, lat = pLat, lng = pLng, accept = pAcc, dist = pDist, cap = pCap))
                api.link(a.id, o.id)
            }
        }
        saveSess()
        load()
    }

    fun login() = go {
        val a = api.login(LoginIn(email.trim(), pass))
        acct = a
        role = a.role
        if (a.role == "DONOR") {
            did = a.prof ?: 0
        }
        saveSess()
        load()
    }

    fun statVal(m: String): Double = valOf(stat, m)

    fun myVal(m: String): Double = if (myStat != null) valOf(myStat, m) else valOf(stat, m)

    private fun valOf(s: Stats?, m: String): Double {
        if (s == null) return 0.0
        return when (m) {
            "kg" -> s.kg
            "co2" -> s.co2
            "people" -> s.people.toDouble()
            "items" -> s.items.toDouble()
            "orgs" -> s.orgs.toDouble()
            else -> s.meals.toDouble()
        }
    }

    fun addReward(t: String, e: String, d: String?, m: String, g: Int, gift: String?) = go {
        api.adminAddReward(RewardIn(t, e, d?.ifBlank { null }, m, g, gift?.ifBlank { null }))
        rewards = api.rewards()
    }

    fun delReward(id: Long) = go {
        api.adminDelReward(id)
        rewards = api.rewards()
    }

    fun load() = go {
        stat = api.stats()
        rewards = api.rewards()
        when (role) {
            "RECIP" -> feed = api.feed()
            "DONOR" -> {
                mine = api.donas()
                orgs = api.orgs()
                tip = api.demand(dArea, lang)
                acct?.let { myStat = api.myStats(it.id) }
            }
            "ORG" -> income = api.income(1)
        }
    }

    fun feedAt(c: String?) = go { feed = api.feed(c) }

    fun scanPhoto(img: String) {
        viewModelScope.launch {
            scanning = true
            try {
                val s = api.scan(ScanIn(img, lang))
                val map = mapOf("FOOD" to "Food", "CLOTH" to "Clothes", "HYG" to "Hygiene", "BABY" to "Baby", "SCHOOL" to "School")
                if (s.cat in map) {
                    dClbl = map[s.cat]!!; dCat = s.cat
                } else if (s.clbl.isNotBlank()) {
                    dClbl = s.clbl; dCat = "OTHER"
                }
                if (s.qty.isNotBlank()) dQty = s.qty
                if (s.cond.isNotBlank()) dCond = s.cond
                if (s.note.isNotBlank() && dNote.isBlank()) dNote = s.note
            } catch (e: Exception) {
            } finally {
                scanImg = img
                scanning = false
            }
        }
    }

    fun analyze() = go {
        ana = api.ana(
            AnaIn(
                cat = dCat, qty = dQty, store = dStor, cond = dCond, pwin = dPwin,
                area = dArea, notes = dNote, photo = dPhoto, clbl = dClbl,
                img = dImgs.firstOrNull(), lang = lang, lat = dLat, lng = dLng
            )
        )
        saveDraft()
    }

    fun badDona(): String {
        if (dQty.isBlank()) return "Enter a quantity"
        if (dLat == null) return "Set a location on the map"
        if (dPwin.isBlank()) return "Set a time"
        return ""
    }

    private fun buildChk(): String {
        val a = ana ?: return ""
        return a.checks.mapIndexed { i, c ->
            val v = when (checkAns[i]) {
                true -> "Y"
                false -> "N"
                else -> "?"
            }
            "$c ::: $v"
        }.joinToString("\n")
    }

    private suspend fun doSave(): Dona? {
        val a = ana ?: return null
        val ac = acct
        val d = api.add(
            DonaIn(
                donor = if (did > 0) did else 1, acc = ac?.id ?: 0,
                dname = ac?.name ?: pName, lat = dLat, lng = dLng, img = dImgs.firstOrNull(), imgs = dImgs.toList(),
                title = a.title, descr = a.descr, cat = dCat, clbl = dClbl,
                qty = dQty, store = dStor, cond = dCond, pwin = dPwin,
                area = dArea, notes = dNote, photo = dPhoto,
                safe = a.safe, risk = a.risk, expl = a.expl,
                chk = buildChk(), deliv = delivStr(),
                rad = if (dDeliver) dRad else null
            )
        )
        cur = d
        if (curDraft > 0) {
            drafts = drafts.filter { it.id != curDraft }
            Store.saveDrafts(drafts)
            curDraft = 0
        }
        mine = api.donas()
        return d
    }

    fun save() = go { doSave() }

    fun find() = go {
        val a = ana ?: return@go
        val ac = acct
        cur = null
        mchs = emptyList()
        mchs = api.matchPreview(
            DonaIn(
                donor = if (did > 0) did else 1, acc = ac?.id ?: 0,
                dname = ac?.name ?: pName, lat = dLat, lng = dLng,
                title = a.title, descr = a.descr, cat = dCat, clbl = dClbl,
                qty = dQty, store = dStor, cond = dCond, pwin = dPwin,
                area = dArea, notes = dNote, photo = dPhoto,
                safe = a.safe, risk = a.risk, expl = a.expl,
                chk = buildChk(), deliv = delivStr(), rad = if (dDeliver) dRad else null
            ),
            lang
        )
    }

    fun open(id: Long) = go {
        val d = api.dona(id)
        cur = d
        curImgs = try {
            api.imgs(id)
        } catch (e: Exception) {
            emptyList()
        }
        mchs = api.match(id, lang)
        curDonor = try {
            if (d.donor > 0) api.donorInfo(d.donor) else null
        } catch (e: Exception) {
            null
        }
    }

    fun rate(stars: Int, txt: String) = go {
        val a = acct
        api.rate(Rating(acc = a?.id ?: 0, name = a?.name ?: "", stars = stars, txt = txt.trim()))
        done = true
    }

    fun loadRatings() = go {
        ratings = api.ratings()
        rateSum = api.rateSum()
    }

    fun saveName(n: String) = go {
        val a = acct ?: return@go
        if (n.isBlank()) return@go
        acct = api.setName(a.id, n.trim())
        saveSess()
    }

    fun saveBio(b: String) = go {
        if (did <= 0) return@go
        api.setBio(did, b.trim())
    }

    fun send(m: Mch) = go {
        val me = acct ?: return@go
        val d = cur ?: doSave() ?: return@go
        api.offer(OffIn(d.id, m.tgt, m.ttype))
        if (m.acc > 0) {
            val body = if (lang == "IT") "Ciao! Vorrei offrirti: ${d.title}" else "Hi! I'd like to offer: ${d.title}"
            api.msg(MsgIn(d.id, me.id, m.acc, me.name, m.name, body))
            openChat(m.acc, m.name, d.id)
        }
    }

    fun move(id: Long, s: String) = go {
        cur = api.move(id, s)
        mine = api.donas()
        stat = api.stats()
    }

    var lastGain by mutableStateOf<Gain?>(null)

    fun done(id: Long) = go {
        lastGain = null
        val r = api.done(id)
        cur = r.dona
        lastGain = r.gain
        mine = api.donas()
        stat = api.stats()
    }

    fun yes(id: Long) = go {
        api.accept(id)
        income = api.income(1)
        stat = api.stats()
    }

    fun no(id: Long) = go {
        api.deny(id)
        income = api.income(1)
    }

    fun newReq() {
        rDesc = ""; rQty = ""; rRestr = ""; rPub = ""; rRev = false; done = false
    }

    fun draftReq() = go {
        val a = acct
        val d = api.reqDraft(
            ReqIn(null, rCat, rDesc, rQty, rUrg, rRad, rAvail, rRestr,
                acc = a?.id ?: 0, name = a?.name ?: "", lat = pLat, lng = pLng, area = pArea, lang = lang)
        )
        rPub = d.pub
        rRev = true
    }

    fun ask() = go {
        val a = acct
        api.addReq(
            ReqIn(null, rCat, rDesc, rQty, rUrg, rRad, rAvail, rRestr,
                acc = a?.id ?: 0, name = a?.name ?: "", lat = pLat, lng = pLng, area = pArea,
                pub = rPub, lang = lang)
        )
        reqs = api.reqs()
        rRev = false
        done = true
    }

    fun loadReq() = go { reqs = api.reqs() }

    fun reqStat(id: Long, v: String) = go {
        api.reqStat(id, v)
        reqs = api.reqs()
    }

    fun fulfil(id: Long, acc: Long, name: String) = go {
        credit = api.reqFulfil(id, acc)
        creditTo = name
        reqs = api.reqs()
    }

    fun loadThreads() = go {
        val a = acct?.id ?: return@go
        thrds = api.threads(a)
    }

    fun openChat(other: Long, oname: String, dona: Long) {
        chOther = other
        chName = oname
        chDona = dona
        markRead(other)
        loadChat()
    }

    fun markRead(other: Long) {
        val m = reads + (other to System.currentTimeMillis())
        reads = m
        Store.saveReads(m.mapKeys { it.key.toString() })
    }

    fun loadChat() = go {
        val a = acct?.id ?: return@go
        msgs = api.conv(a, chOther, if (chDona > 0) chDona else null)
    }

    fun sendMsg(text: String) = go {
        val a = acct ?: return@go
        if (text.isBlank()) return@go
        api.msg(MsgIn(chDona, a.id, chOther, a.name, chName, text.trim()))
        msgs = api.conv(a.id, chOther, if (chDona > 0) chDona else null)
    }

    fun clear() {
        ana = null
        cur = null
        mchs = emptyList()
        done = false
        dQty = ""; dStor = ""; dCond = ""; dPwin = ""; dNote = ""; dPhoto = ""
        dImgs.clear(); dLat = null; dLng = null; dPickup = true; dDeliver = false
        scanning = false; scanImg = ""
        checkAns.clear()
    }

    private fun go(b: suspend () -> Unit) {
        viewModelScope.launch {
            busy = true
            err = ""
            try {
                b()
            } catch (e: retrofit2.HttpException) {
                err = srvErr(e)
            } catch (e: java.io.IOException) {
                err = "Can't reach the server"
            } catch (e: Exception) {
                err = "Something went wrong"
            } finally {
                busy = false
            }
        }
    }

    private fun srvErr(e: retrofit2.HttpException): String {
        return try {
            val body = e.response()?.errorBody()?.string().orEmpty()
            val m = Regex("\"error\"\\s*:\\s*\"([^\"]*)\"").find(body)
            val msg = m?.groupValues?.get(1)
            if (!msg.isNullOrBlank()) msg else "Request failed (${e.code()})"
        } catch (x: Exception) {
            "Request failed (${e.code()})"
        }
    }
}
