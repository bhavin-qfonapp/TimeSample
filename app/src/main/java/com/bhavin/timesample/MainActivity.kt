package com.bhavin.timesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import com.bhavin.currenttime.ApiState
import com.bhavin.currenttime.TimeViewModel
import com.bhavin.currenttime.Timeresponse

class MainActivity : AppCompatActivity() {

    private val timeViewModel: TimeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        timeViewModel.timeLivedata.observe(this){
            when (it) {
                is ApiState.FinishedWithLocal -> {
                    getTimee(null,timeinMillisecond=it.response)
                    timeViewModel.setIdleState()
                }
                is ApiState.Loading -> {
                    Log.e("Loading->",it.msg)
                }
                else -> return@observe
            }
        }

        timeViewModel.getTimeData()

    }

    private fun getTimee(response: Timeresponse?,timeinMillisecond:String?) {
        response?.let {

            it.datetime?.let {dateT->

                val curentSystem=System.currentTimeMillis().toString()

                val unixt=it.unixtime
                val ppp=unixt.toString()+"\n"+curentSystem
                findViewById<TextView>(R.id.time).text = ppp
            }
        }

        timeinMillisecond?.let {
            findViewById<TextView>(R.id.time).text = it
        }
    }


}