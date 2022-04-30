package com.bhavin.currenttime

import io.reactivex.Observer
import io.reactivex.observers.DisposableObserver
import org.json.JSONObject
import retrofit2.HttpException
/**
 * @Author: Bhavin
 * @Date: 29-Apr-22
 */
abstract class CustomApiObserver : DisposableObserver<Timeresponse>(), Observer<Timeresponse> {

    override fun onNext(res: Timeresponse) {

    }

    override fun onError(e: Throwable) {
        if (e is HttpException) {

            val jj = e.response()?.errorBody()

            val j = JSONObject(jj?.string())
            val errorMessage = j.getString("message")

            if (e.code() == 400)
                onBadRequest(errorMessage)
            else if (e.code() == 500)
                onBadGateway(errorMessage)

        }
        else{
            val ee=when {
                e.message==null -> "Something went wrong"
                else -> e.message
            }

            onCommonError(ee!!)
        }
    }
    abstract fun onCommonError(e: String)

    abstract fun onBadGateway(e: String)

    abstract fun onBadRequest(e: String)

    override fun onComplete() {

    }

}