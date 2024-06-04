package com.santosgo.mavelheroes.ui.herolist

import android.annotation.SuppressLint
import android.content.res.Configuration
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.santosgo.mavelheroes.R
import com.santosgo.mavelheroes.adapter.HeroAdapter
import com.santosgo.mavelheroes.data.Hero
import com.santosgo.mavelheroes.databinding.FragmentHeroListBinding
import kotlinx.coroutines.launch


class HeroListFragment : Fragment() {

    private var _binding : FragmentHeroListBinding? = null
    val binding
        get() = _binding!!

    private val heroListVM by viewModels<HeroListVM> { HeroListVM.Factory }

    private lateinit var heroAdapter : HeroAdapter

    // Guarda el estado de la lista de héroes
    private var heroListState : MutableList<Hero> = mutableListOf()


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
        _binding = FragmentHeroListBinding.inflate(inflater,container,false)

        //texto inicial
        binding.textView.text = getString(R.string.hero_list)

        return binding.root
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecView()

        setCollectors()

    }

    private fun initRecView() {
        heroAdapter = HeroAdapter(
            _heroList = mutableListOf(),
            onClickHero = { heroName -> selectHero(heroName)},
            onClickDelete = {  pos -> confirmDeleteHero(pos)}
        )
        binding.rvHeroes.adapter = heroAdapter
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            binding.rvHeroes.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        else {
            binding.textView.visibility = View.GONE
            binding.rvHeroes.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                heroListVM.uiState.collect { heroState ->
                    if(!heroState.isLoading) {
                        binding.pbLoading.isVisible = false
                        heroAdapter.setHeroList(heroState.heroList)
                        heroAdapter.notifyDataSetChanged()
                    }else {
                        binding.pbLoading.isVisible = true
                    }

                }
            }
        }
    }

    private fun selectHero(heroId: String) {
        val action = HeroListFragmentDirections.actionHeroListFragmentToHeroDetailsFragment(heroId)
        findNavController().navigate(action)
    }

    //Dialog de confirmación del borrado.
    private fun confirmDeleteHero(pos : Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete))
            .setMessage(resources.getString(R.string.support_confirm_delete,heroAdapter.heroList[pos].name))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                // Respond to positive button press
                heroListVM.deleteHero(pos)
            }
            .show()
    }
}