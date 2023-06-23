package com.manoffocus.mfdistricts

import com.google.android.gms.maps.model.LatLng

fun main(){
    val coorsRaw = "-3.6950111,40.419066900000004,0.0 -3.701877599999999,40.4200471,0.0 -3.7059116,40.4202758,0.0 -3.7118983,40.4243923,0.0 -3.7139153,40.423330500000006,0.0 -3.712842499999999,40.42140299999999,0.0 -3.7133574000000005,40.4090031,0.0 -3.7116838000000003,40.4068463,0.0 -3.7080789,40.4117152,0.0 -3.7064052000000003,40.4147867,0.0 -3.7037444000000006,40.4142639,0.0 -3.6991739,40.4179887,0.0 -3.6950111,40.419066900000004,0.0"
    val coors = coorsRaw.split(",").toList().windowed(2, 2).map { it.reversed() }
    val cleanCoors = coors.map {
        LatLng(it[0].toDouble(), it[1].replace("0.0", "").trim().toDouble())
    }
    println(cleanCoors)
}