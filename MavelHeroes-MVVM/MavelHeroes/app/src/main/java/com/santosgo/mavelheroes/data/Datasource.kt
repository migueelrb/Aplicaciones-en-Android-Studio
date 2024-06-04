/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.santosgo.mavelheroes.data

object Datasource {

    private var heroList = mutableListOf<Hero>()

    fun getHeroList(): MutableList<Hero> {
        heroList = mutableListOf<Hero>(
            Hero("Capitán América",
            10,
            4,
            "capitan_america",
            "Líder militar experto y un gran estratega. Su escudo de Vibranium es prácticamente indestructible."),
            Hero("Iron Man",
                6,
                8,
                "iron_man",
                "Vengador blindado. Tony Stark es un genio que diseá soluciones de alta tecnología, como su armadura."
            ),
            Hero("Hulk",
                9,
                7,
                "hulk",
                "Prefiere utilizar su inmenso poder para aplastar a las fuerzas del mal. ¡Es uno de los superhéroes más fuertes!"
            ),
            Hero("Viuda negra",
                5,
                5,
                "viuda_negra",
                "Superespía y una experta en artes marciales. Tiene una agilidad y una capacidad atlética excepcionales."
            ),
            Hero("Thor",
                9,
                3,
                "thor",
                "¡Dios del Trueno! Controla el trueno y utiliza su martillo Mjolnir para proteger su hogar, Asgardia, y la Tierra."
            ),
            Hero("Capitana Marvel",
                10,
                4,
                "capitana_marvel",
                "Una de la superheroínas más poderosas. Puede volar y lanza ráfagas de energía de sus manos."
            ),
            Hero("Spiderman",
                6,
                6,
                "spiderman",
                "Tu vecino y amigo siempre está atento para protegerte. Es un chico muy pegajoso."
            ),
            Hero("Star Lord",
                4,
                7,
                "star_lord",
                "Viaja por el cosmos en su nave espacial, la Milano, en busca de fortuna. Es el líder de los Guardianes de la Galaxia."
            ),
            Hero("Gamora",
                6,
                4,
                "gamora",
                "Es una espadachina hábil y una guerrera famosa. Hija del villano Titán Thanos, aunque tiene su corazoncito."
            ),
            Hero("Doctor Strange",
            3,
            8,
            "doctor_strange",
            "Desarrolló sus poderes mediante antiguas técnicas asiáticas. Utiliza ilusiones, hechizos y engaños. ¡Puede abrir portales multidimensionales!"
            )
        )
        return heroList
    }

    fun getHero(name : String) : Hero? {
        var hero: Hero? = null
        for(h in heroList) {
            if (h.name == name) {
                hero = h
                break
            }
        }
        return hero
    }

}