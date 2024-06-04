package com.santosgo.mavelheroes.ui.herolist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.santosgo.mavelheroes.data.Hero
import com.santosgo.mavelheroes.dependencies.MarvelHeroes
import com.santosgo.mavelheroes.repositories.HeroesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Math.random
import kotlin.random.Random

class HeroListVM(
    private val heroesRepository: HeroesRepository
) : ViewModel() {

    private val _uiState : MutableStateFlow<HeroListUiState> = MutableStateFlow(HeroListUiState())
    val uiState : StateFlow<HeroListUiState> = _uiState.asStateFlow()

    init {
        getSomeRandHeroes(NUM_HEROES)
    }

    private fun getSomeRandHeroes(numHeroes: Int) {
        viewModelScope.launch {
            val myHeroResp = heroesRepository.getSomeRandHeroes(NUM_HEROES)
            if(myHeroResp.isSuccessful) {
                val myHeroes = myHeroResp.body()
                _uiState.update { currentSate ->
                    currentSate.copy(
                        isLoading = false,
                        heroList = myHeroes?.let { it.toList() } ?: emptyList<Hero>()
                    )
                }
            } else {
                //error en la respuesta...
            }
        }
    }

    fun deleteHero(pos: Int) {
        Log.d("heroapi", "posicion: $pos")
        viewModelScope.launch {
            _uiState.update { currentState ->
                val delHeroList = currentState.heroList.toMutableList()
                delHeroList.removeAt(pos)
                currentState.copy(
                    heroList = delHeroList
                )
            }
        }
    }

    companion object {

        const val NUM_HEROES = 10

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return HeroListVM(
                    (application as MarvelHeroes).appContainer.heroesRepository
                ) as T
            }
        }
    }
}