package com.resq.data

data class Dona(
    val id: Long = 0,
    val donor: Long = 0,
    val acc: Long = 0,
    val dname: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val pic: Boolean = false,
    val title: String = "",
    val descr: String = "",
    val cat: String = "",
    val clbl: String? = null,
    val qty: String = "",
    val store: String = "",
    val cond: String = "",
    val pwin: String = "",
    val area: String = "",
    val notes: String = "",
    val photo: String = "",
    val stat: String = "",
    val risk: Int = 0,
    val safe: String = "",
    val expl: String = "",
    val chk: String? = null,
    val deliv: String? = null,
    val rad: Int? = null,
    val test: Boolean = false
)

data class Mch(
    val tgt: Long = 0,
    val acc: Long = 0,
    val ttype: String = "",
    val name: String = "",
    val dist: Double? = null,
    val score: Int = 0,
    val reason: String? = null,
    val kind: String? = null,
    val area: String? = null,
    val need: String? = null,
    val accepts: Boolean = false,
    val urgent: Boolean = false,
    val test: Boolean = false
)

data class Stats(
    val id: Long = 0,
    val kg: Double = 0.0,
    val meals: Int = 0,
    val co2: Double = 0.0,
    val items: Int = 0,
    val people: Int = 0,
    val orgs: Int = 0
)

data class AnaIn(
    val cat: String,
    val qty: String,
    val store: String,
    val cond: String,
    val pwin: String,
    val area: String,
    val notes: String,
    val photo: String = "",
    val clbl: String = "",
    val img: String? = null,
    val lang: String = "EN",
    val lat: Double? = null,
    val lng: Double? = null
)

data class ScanIn(
    val img: String,
    val lang: String = "EN"
)

data class ScanOut(
    val item: String = "",
    val clbl: String = "",
    val cat: String = "OTHER",
    val qty: String = "",
    val cond: String = "",
    val note: String = ""
)

data class AnaOut(
    val item: String = "",
    val title: String = "",
    val descr: String = "",
    val cat: String = "",
    val safe: String = "",
    val risk: Int = 0,
    val expl: String = "",
    val checks: List<String> = emptyList(),
    val quests: List<String> = emptyList(),
    val recs: List<String> = emptyList()
)

data class DonaIn(
    val donor: Long,
    val acc: Long = 0,
    val dname: String = "",
    val lat: Double? = null,
    val lng: Double? = null,
    val img: String? = null,
    val title: String,
    val descr: String,
    val cat: String,
    val clbl: String = "",
    val qty: String,
    val store: String,
    val cond: String,
    val pwin: String,
    val area: String,
    val notes: String,
    val photo: String,
    val safe: String,
    val risk: Int,
    val expl: String,
    val chk: String = "",
    val deliv: String = "PICKUP",
    val rad: Int? = null,
    val test: Boolean = false,
    val imgs: List<String> = emptyList()
)

data class OffIn(
    val dona: Long,
    val tgt: Long,
    val ttype: String
)

data class Offer(
    val id: Long = 0,
    val dona: Long = 0,
    val tgt: Long = 0,
    val ttype: String = "",
    val stat: String = ""
)

data class Org(
    val id: Long = 0,
    val acc: Long = 0,
    val otype: String = "",
    val name: String = "",
    val area: String = "",
    val lat: Double? = null,
    val lng: Double? = null,
    val accept: String = "",
    val dist: Int = 0,
    val cap: Int = 0
)

data class Req(
    val id: Long = 0,
    val recip: Long = 0,
    val acc: Long = 0,
    val name: String = "",
    val lat: Double? = null,
    val lng: Double? = null,
    val area: String = "",
    val test: Boolean = false,
    val stat: String = "OPEN",
    val cat: String = "",
    val descr: String = "",
    val qty: String = "",
    val urg: String = "",
    val radius: Int = 0,
    val avail: String = "",
    val restr: String = "",
    val pub: String = ""
)

data class ReqIn(
    val recip: Long?,
    val cat: String,
    val descr: String,
    val qty: String,
    val urg: String,
    val radius: Int,
    val avail: String,
    val restr: String,
    val acc: Long = 0,
    val name: String = "",
    val lat: Double? = null,
    val lng: Double? = null,
    val area: String = "",
    val pub: String = "",
    val lang: String = "EN"
)

data class ReqDraft(
    val pub: String = ""
)

data class LoginIn(
    val email: String,
    val pass: String
)

data class RegIn(
    val email: String,
    val pass: String,
    val role: String,
    val name: String,
    val area: String
)

data class Acct(
    val id: Long = 0,
    val email: String = "",
    val role: String = "",
    val name: String = "",
    val area: String = "",
    val prof: Long? = null,
    val admin: Boolean = false,
    val banned: Boolean = false,
    val test: Boolean = false
)

data class Sum(
    val accts: Long = 0,
    val donas: Long = 0,
    val donors: Long = 0,
    val recips: Long = 0,
    val orgs: Long = 0
)

data class Reward(
    val id: Long = 0,
    val title: String = "",
    val emoji: String = "🏅",
    val descr: String? = null,
    val metric: String = "meals",
    val goal: Int = 1,
    val gift: String? = null
)

data class RewardIn(
    val title: String,
    val emoji: String,
    val descr: String?,
    val metric: String,
    val goal: Int,
    val gift: String?
)

data class Donr(
    val id: Long = 0,
    val acc: Long = 0,
    val type: String? = "",
    val name: String? = "",
    val area: String? = "",
    val lat: Double? = null,
    val lng: Double? = null,
    val dtype: String = "FOOD",
    val pref: String = "",
    val bio: String? = ""
)

data class Rating(
    val id: Long = 0,
    val acc: Long = 0,
    val name: String = "",
    val stars: Int = 5,
    val txt: String = ""
)

data class RateSum(
    val avg: Double = 0.0,
    val count: Int = 0
)

data class Gain(
    val kg: Double = 0.0,
    val meals: Int = 0,
    val co2: Double = 0.0,
    val people: Int = 0
)

data class MoveOut(
    val dona: Dona = Dona(),
    val gain: Gain? = null
)

data class Recp(
    val id: Long = 0,
    val acc: Long = 0,
    val atype: String = "",
    val area: String = "",
    val lat: Double? = null,
    val lng: Double? = null,
    val radius: Int = 5,
    val needs: String = "",
    val diet: String = "",
    val avail: String = "",
    val deliv: Boolean = false,
    val urg: String = "MED"
)

data class MsgIn(
    val dona: Long,
    val frm: Long,
    val to: Long,
    val nameFrm: String,
    val nameTo: String,
    val body: String
)

data class Msg(
    val id: Long = 0,
    val dona: Long = 0,
    val frm: Long = 0,
    val to: Long = 0,
    val nameFrm: String = "",
    val nameTo: String = "",
    val body: String = "",
    val ts: Long = 0
)

data class Thrd(
    val other: Long = 0,
    val oname: String = "",
    val dona: Long = 0,
    val last: String = "",
    val lastFrm: Long = 0,
    val ts: Long = 0
)
