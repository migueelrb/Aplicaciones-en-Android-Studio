package com.santosgo.mavelheroes.dependencies

import android.content.Context

class AppContainer(context : Context) {

    //Definición de la api de Retrofit2.



    companion object {
        const val USER_TOKEN = ""
        const val BASE_URL = "https://superheroapi.com/api/$USER_TOKEN"
    }

}
