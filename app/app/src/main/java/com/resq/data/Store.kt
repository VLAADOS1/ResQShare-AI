package com.resq.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

data class Sess(
    val acct: Acct,
    val role: String,
    val did: Long,
    val lang: String,
    val pArea: String,
    val pLat: Double?,
    val pLng: Double?,
    val pRad: Int = 5
)

data class Draft(
    val id: Long,
    val ana: AnaOut?,
    val cat: String,
    val clbl: String,
    val qty: String,
    val cond: String,
    val stor: String,
    val pwin: String,
    val area: String,
    val lat: Double?,
    val lng: Double?,
    val note: String,
    val imgs: List<String> = emptyList(),
    val deliv: String
)

object Store {

    private var sp: SharedPreferences? = null
    private val gson = Gson()

    fun init(ctx: Context) {
        sp = ctx.getSharedPreferences("resq", Context.MODE_PRIVATE)
    }

    fun saveSess(s: Sess?) {
        val e = sp?.edit() ?: return
        if (s == null) e.remove("sess") else e.putString("sess", gson.toJson(s))
        e.apply()
    }

    fun sess(): Sess? {
        val j = sp?.getString("sess", null) ?: return null
        return try {
            gson.fromJson(j, Sess::class.java)
        } catch (x: Exception) {
            null
        }
    }

    fun saveLang(l: String) {
        sp?.edit()?.putString("lang", l)?.apply()
    }

    fun lang(): String = sp?.getString("lang", "EN") ?: "EN"

    fun saveDrafts(d: List<Draft>) {
        sp?.edit()?.putString("drafts", gson.toJson(d))?.apply()
    }

    fun drafts(): List<Draft> {
        val j = sp?.getString("drafts", null) ?: return emptyList()
        return try {
            gson.fromJson(j, Array<Draft>::class.java).toList()
        } catch (x: Exception) {
            emptyList()
        }
    }

    fun saveReads(m: Map<String, Long>) {
        sp?.edit()?.putString("reads", gson.toJson(m))?.apply()
    }

    fun reads(): Map<String, Long> {
        val j = sp?.getString("reads", null) ?: return emptyMap()
        return try {
            gson.fromJson(j, object : com.google.gson.reflect.TypeToken<Map<String, Long>>() {}.type)
        } catch (x: Exception) {
            emptyMap()
        }
    }
}
