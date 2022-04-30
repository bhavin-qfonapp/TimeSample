package com.bhavin.currenttime
/**
 * @Author: Bhavin
 * @Date: 29-Apr-22
 */
sealed class ApiState {

    object Idle : ApiState()

    class Loading(val msg: String = "Loading") : ApiState()

    class FinishedWithLocal(val response: String?=null) : ApiState()

}