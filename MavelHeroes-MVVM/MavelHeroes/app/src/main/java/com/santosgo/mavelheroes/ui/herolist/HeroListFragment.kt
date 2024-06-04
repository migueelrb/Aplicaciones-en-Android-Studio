package com.santosgo.mavelheroes.ui.herolist

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.santosgo.mavelheroes.R
import com.santosgo.mavelheroes.adapter.HeroAdapter
import com.santosgo.mavelheroes.data.Datasource
import com.santosgo.mavelheroes.data.Hero
import com.santosgo.mavelheroes.databinding.FragmentHeroListBinding


class HeroListFragment : Fragment() {

    private var _binding : FragmentHeroListBinding? = null
    val binding
        get() = _binding!!

    private var listaHeroes = Datasource.getHeroList()
    private lateinit var heroAdapter : HeroAdapter

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

    private fun initRecView() {
        heroAdapter = HeroAdapter(listaHeroes,
            onClickHero = { heroName -> selectHero(heroName)},
            onClickDelete = {  pos -> confirmDeleteHero(pos)},
            onClickImage = { pos, hero -> clonHero(pos,hero)}
        )
        binding.rvHeroes.adapter = heroAdapter
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            binding.rvHeroes.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        else {
            binding.textView.visibility = View.GONE
            binding.rvHeroes.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }


    }

    private fun selectHero(heroName: String) {
        val action = HeroListFragmentDirections.actionHeroListFragmentToHeroDetailsFragment(heroName)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecView()

    }
    //Dialog de confirmación del borrado.
    private fun confirmDeleteHero(pos : Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete))
            .setMessage(resources.getString(R.string.support_confirm_delete,listaHeroes[pos].name))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                // Respond to positive button press
                deleteHero(pos)
            }
            .show()
    }

    //borra un heroe de la lista y notifica al adapter.
    private fun deleteHero(pos : Int) {
        listaHeroes.removeAt(pos)
        heroAdapter.notifyItemRemoved(pos)
    }


    //clona un heroe de la lista en la posición indicada y notifica al adapter.
    private fun clonHero(pos: Int, hero : Hero) {
        listaHeroes.add(pos,hero)
        heroAdapter.notifyItemInserted(pos)
    }

}