package com.bhavin.currenttime

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @Author: Bhavin
 * @Date: 29-Apr-22
 */


class TimeViewModel() : BaseDisposablesViewModel() {
    var timeLivedata = MutableLiveData<ApiState>(ApiState.Idle)
    fun setIdleState() = timeLivedata.postValue(ApiState.Idle)
    val dateTimeFormatINeed = "yyyy-MM-dd HH:mm:ss"
    val dateTimeFormatServer = "yyyy-MM-dd'T'HH:mm:ss"

    fun getTimeData() {
        timeLivedata.postValue(ApiState.Loading())
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES).build()

        val retrofit: Retrofit =
            Retrofit.Builder().baseUrl("https://worldtimeapi.org/api/timezone/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()

        val myApi = retrofit.create(Api::class.java)

        myCompositeDisposable.add(
            myApi.getTimeOfYourZone()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : CustomApiObserver(), Disposable {
                    override fun onNext(res: Timeresponse) {
                        super.onNext(res)
                        serverTime(res)
                    }

                    override fun onCommonError(e: String) {
                        onErrorResonse()
                    }

                    override fun onBadGateway(e: String) {
                        onErrorResonse()
                    }

                    override fun onBadRequest(e: String) {
                        onErrorResonse()
                    }
                })
        )

    }

    private fun onErrorResonse() {
        val date = Date()
        val df: DateFormat = SimpleDateFormat(dateTimeFormatINeed)
        df.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        val f = df.format(date)
        return timeLivedata.postValue(ApiState.FinishedWithLocal(f))
    }

    private fun serverTime(timeresponse: Timeresponse?) {
        var finalDate: String?
        timeresponse?.let {
            when {
                it.datetime.isNullOrBlank() -> {
                    onErrorResonse()
                }
                else -> {
                    it.datetime?.let { dt ->
                        finalDate = formatDate(dt, dateTimeFormatServer, dateTimeFormatINeed)
                        return timeLivedata.postValue(ApiState.FinishedWithLocal(finalDate))
                    }
                }
            }
        }
    }

    private fun formatDate(dateToFormat: String, inputFormat: String?, outputFormat: String?): String? {
        try {
            return SimpleDateFormat(outputFormat).format(SimpleDateFormat(inputFormat).parse(dateToFormat.removeSuffix("+05:30")))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }
}