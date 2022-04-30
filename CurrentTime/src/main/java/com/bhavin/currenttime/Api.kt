package com.bhavin.currenttime

import io.reactivex.Observable
import retrofit2.http.GET
/**
 * @Author: Bhavin
 * @Date: 29-Apr-22
 */
interface Api {
    @GET("Asia/Kolkata")
    fun getTimeOfYourZone(): Observable<Timeresponse>
}