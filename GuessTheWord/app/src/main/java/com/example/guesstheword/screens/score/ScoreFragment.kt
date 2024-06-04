/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.guesstheword.screens.score

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guesstheword.R
import com.example.guesstheword.adapters.GameAdapter
import com.example.guesstheword.databinding.ScoreFragmentBinding
import com.example.guesstheword.datamodel.Game
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


/**
 * Fragment where the final score is shown, after the game is over
 */
class ScoreFragment : Fragment() {

    private lateinit var binding : ScoreFragmentBinding

    private val args : ScoreFragmentArgs by navArgs()

    private val scoreVM : ScoreViewModel by viewModels<ScoreViewModel> { ScoreViewModel.Factory }

    private lateinit var animAppearCV : Animation

    private lateinit var animAppearList : Animation

    private lateinit var animDisappearCV : Animation

    private lateinit var gameAdapter: GameAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class.
        binding = ScoreFragmentBinding.inflate(inflater,container,false)

        setViews()
        setListeners()
        setAnimations()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListeners() {
        binding.playAgainButton.setOnClickListener {
            findNavController().navigate(ScoreFragmentDirections.actionScoreFragmentToGameFragment())
        }
        binding.btnSave.setOnClickListener {
            validateName()
            hideKeyboard()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun validateName() {
        val name = binding.etName.text.toString()
        if(name.isNotBlank())
            scoreVM.saveGame(binding.etName.text.toString())
        else
            Snackbar.make(requireView(),getString(R.string.no_name),Snackbar.LENGTH_SHORT).show()
    }

    private fun setViews() {
        //Inicializa las Views
        binding.tvFinalScore.text = args.score.toString()
        binding.tvStreak.text = args.streak.toString()
        args.rightWords.forEach {
            binding.tvRightWords.text = "${binding.tvRightWords.text} \n${it}"
        }

        args.wrongWords.forEach {
            binding.tvWrongWords.text = "${binding.tvWrongWords.text} \n${it}"
        }
        //Guarda en el ViewModel
        scoreVM.setFinalScore(args.score)
        scoreVM.setStreak(args.streak)
        scoreVM.setRightWords(binding.tvRightWords.text.toString())
        scoreVM.setWrongWords(binding.tvWrongWords.text.toString())
    }

    private fun setAnimations() {
        animAppearCV = AnimationUtils.loadAnimation(requireContext(), R.anim.view_appear)
        animAppearList = AnimationUtils.loadAnimation(requireContext(), R.anim.view_appear)
        animDisappearCV = AnimationUtils.loadAnimation(requireContext(), R.anim.view_disappear)

        animAppearCV.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.cvGame.isVisible = true
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })
        animDisappearCV.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.cvGame.isVisible = false
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCollectors()
    }

    private fun initRecyclerView(games : List<Game>) {
        gameAdapter = GameAdapter(games)
        binding.rvGameList.adapter = gameAdapter
        binding.rvGameList.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)

    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                scoreVM.uiState.collect { gameState ->
                    if(!gameState.isLoading) {
                        initRecyclerView(gameState.games)
                    }
                    else {
                        binding.cvGame.startAnimation(animAppearCV)
                        binding.rvGameList.startAnimation(animAppearList)
                    }
                    if(gameState.savedGame) {
                        binding.cvGame.startAnimation(animDisappearCV)
                        binding.cvGame.isVisible = false
                    }
                    gameState.message?.let {
                        Snackbar.make(requireView(),gameState.message,Snackbar.LENGTH_SHORT).show()
                        scoreVM.messageShown()
                    }
                }
            }
        }
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}
