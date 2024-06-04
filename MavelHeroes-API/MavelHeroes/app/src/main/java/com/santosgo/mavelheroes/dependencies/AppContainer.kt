package com.santosgo.mavelheroes.dependencies

import android.content.Context
import com.santosgo.mavelheroes.api.ApiService
import com.santosgo.mavelheroes.api.HeroApiConfig
import com.santosgo.mavelheroes.repositories.HeroesRepository

class AppContainer(context : Context) {

    //Creación del servicio, usando la api.
    private val heroApiService = HeroApiConfig.provideRetrofit().create(ApiService::class.java)

    //Creación del repositorio que hará uso de la API.
    val heroesRepository : HeroesRepository = HeroesRepository(heroApiService)
}
