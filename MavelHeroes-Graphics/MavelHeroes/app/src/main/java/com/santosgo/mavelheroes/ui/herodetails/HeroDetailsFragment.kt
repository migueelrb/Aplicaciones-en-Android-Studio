package com.santosgo.mavelheroes.ui.herodetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.santosgo.mavelheroes.data.FullHero
import com.santosgo.mavelheroes.databinding.FragmentHeroDetailsBinding
import kotlinx.coroutines.launch


class HeroDetailsFragment : Fragment() {

    companion object {
        const val DRAWABLE = "drawable"
    }

    private var _binding : FragmentHeroDetailsBinding? = null
    private val binding
        get() = _binding!!

    val args: HeroDetailsFragmentArgs by navArgs()

    private val heroDetailsVM by viewModels<HeroDetailsVM> { HeroDetailsVM.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHeroDetailsBinding.inflate(inflater,container,false)

        heroDetailsVM.setHero(args.idHero)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCollectors()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                heroDetailsVM.uiState.collect { heroState ->
                    if(!heroState.isLoading) {
                        binding.pbLoadingDetails!!.isVisible = false
                        heroState.hero?.let {
                            binding.tvName.text = it.name
                            Glide.with(requireContext())
                                .load(it.photo)
                                .circleCrop()
                                .into(binding.ivPhoto)
                            binding.rbPower.rating = it.power.toFloat()/FullHero.SCALE
                            binding.rbIntelligence.rating = it.intelligence.toFloat()/FullHero.SCALE
                            binding.tvDesc.text = it.description
                        }
                    } else {
                        binding.pbLoadingDetails!!.isVisible = true
                    }
                }
            }
        }
    }

}