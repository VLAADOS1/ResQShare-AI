package com.resq.data

import retrofit2.http.*

interface Api {

    @POST("reg")
    suspend fun reg(@Body b: RegIn): Acct

    @POST("login")
    suspend fun login(@Body b: LoginIn): Acct

    @PUT("acct/{id}/prof")
    suspend fun link(@Path("id") id: Long, @Query("val") prof: Long): Acct

    @POST("donor")
    suspend fun donr(@Body b: Donr): Donr

    @POST("recip")
    suspend fun recp(@Body b: Recp): Recp

    @POST("org")
    suspend fun addOrg(@Body b: Org): Org

    @GET("donor/{id}")
    suspend fun donorInfo(@Path("id") id: Long): Donr

    @PUT("donor/{id}/bio")
    suspend fun setBio(@Path("id") id: Long, @Query("val") v: String): Donr

    @PUT("acct/{id}/name")
    suspend fun setName(@Path("id") id: Long, @Query("val") v: String): Acct

    @POST("ana")
    suspend fun ana(@Body b: AnaIn): AnaOut

    @POST("scan")
    suspend fun scan(@Body b: ScanIn): ScanOut

    @POST("dona")
    suspend fun add(@Body b: DonaIn): Dona

    @GET("dona")
    suspend fun donas(@Query("donor") d: Long? = null): List<Dona>

    @GET("dona/{id}")
    suspend fun dona(@Path("id") id: Long): Dona

    @GET("dona/{id}/imgs")
    suspend fun imgs(@Path("id") id: Long): List<Long>

    @GET("dona/{id}/match")
    suspend fun match(@Path("id") id: Long, @Query("lang") lang: String? = null): List<Mch>

    @POST("match")
    suspend fun matchPreview(@Body b: DonaIn, @Query("lang") lang: String? = null): List<Mch>

    @PUT("dona/{id}/stat")
    suspend fun move(@Path("id") id: Long, @Query("val") v: String): Dona

    @PUT("dona/{id}/done")
    suspend fun done(@Path("id") id: Long): MoveOut

    @GET("feed")
    suspend fun feed(@Query("cat") c: String? = null): List<Dona>

    @GET("org")
    suspend fun orgs(): List<Org>

    @GET("org/{id}/income")
    suspend fun income(@Path("id") id: Long): List<Offer>

    @POST("offer")
    suspend fun offer(@Body b: OffIn): Offer

    @PUT("offer/{id}/accept")
    suspend fun accept(@Path("id") id: Long): Offer

    @PUT("offer/{id}/decline")
    suspend fun deny(@Path("id") id: Long): Offer

    @POST("req")
    suspend fun addReq(@Body b: ReqIn): Req

    @GET("req")
    suspend fun reqs(): List<Req>

    @PUT("req/{id}/stat")
    suspend fun reqStat(@Path("id") id: Long, @Query("val") v: String): Req

    @POST("req/draft")
    suspend fun reqDraft(@Body b: ReqIn): ReqDraft

    @PUT("req/{id}/fulfil")
    suspend fun reqFulfil(@Path("id") id: Long, @Query("acc") acc: Long): Gain

    @GET("stats")
    suspend fun stats(): Stats

    @GET("stats/{acc}")
    suspend fun myStats(@Path("acc") acc: Long): Stats

    @GET("demand")
    suspend fun demand(@Query("area") a: String? = null, @Query("lang") l: String? = null): String

    @POST("msg")
    suspend fun msg(@Body b: MsgIn): Msg

    @GET("msg")
    suspend fun conv(@Query("acc") a: Long, @Query("other") o: Long, @Query("dona") d: Long? = null): List<Msg>

    @GET("threads")
    suspend fun threads(@Query("acc") a: Long): List<Thrd>

    @GET("admin/accts")
    suspend fun adminAccts(): List<Acct>

    @GET("admin/donas")
    suspend fun adminDonas(): List<Dona>

    @GET("admin/sum")
    suspend fun adminSum(): Sum

    @DELETE("admin/dona/{id}")
    suspend fun adminDelDona(@Path("id") id: Long): Map<String, Boolean>

    @PUT("admin/acct/{id}/ban")
    suspend fun adminBan(@Path("id") id: Long, @Query("val") v: Boolean): Acct

    @DELETE("admin/acct/{id}")
    suspend fun adminDelAcct(@Path("id") id: Long): Map<String, Boolean>

    @GET("rewards")
    suspend fun rewards(): List<Reward>

    @POST("admin/reward")
    suspend fun adminAddReward(@Body b: RewardIn): Reward

    @DELETE("admin/reward/{id}")
    suspend fun adminDelReward(@Path("id") id: Long): Map<String, Boolean>

    @POST("rate")
    suspend fun rate(@Body b: Rating): Rating

    @GET("rate")
    suspend fun ratings(): List<Rating>

    @GET("rate/sum")
    suspend fun rateSum(): RateSum
}
