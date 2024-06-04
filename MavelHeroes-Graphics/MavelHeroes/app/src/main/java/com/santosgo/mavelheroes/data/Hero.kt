package com.santosgo.mavelheroes.data

import android.os.Parcel
import android.os.Parcelable

//Clase con los objetos del RecyclerView.
data class Hero(
    val id : String,
    val name : String,
    val power : Int,
    val intelligence : Int,
    val photo : String,
    val description : String
)