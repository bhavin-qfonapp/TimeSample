package com.bhavin.currenttime

/**
 * @Author: Bhavin
 * @Date: 29-Apr-22
 */
class Timeresponse {
    var abbreviation: String?=null
    var client_ip: String?=null
    var datetime: String?=null
    var day_of_week: Int = 0
    var day_of_year: Int = 0
    var dst:Boolean = false
    var dst_from: String?=null
    var dst_offset: Int = 0
    var dst_until: String?=null
    var raw_offset: Int = 0
    var timezone: String?=null
    var unixtime: Int = 0
    var utc_datetime: String?=null
    var utc_offset: String?=null
    var week_number: Int = 0
}