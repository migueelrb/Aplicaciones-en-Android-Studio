package com.santosgo.mavelheroes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.santosgo.mavelheroes.R
import com.santosgo.mavelheroes.data.FullHero
import com.santosgo.mavelheroes.data.Hero
import com.santosgo.mavelheroes.databinding.HeroItemBinding

//Clase que implementa el adaptador (Adapter), por lo que debe implementar los métodos.
class HeroAdapter(
    private var _heroList : MutableList<Hero>,
    private val onClickHero: (String) -> Unit,
    private val onClickDelete: (Int) -> Unit
) : RecyclerView.Adapter<HeroAdapter.HeroViewHolder>() {

    companion object {
        const val DRAWABLE = "drawable"
        const val CLON = "Clon"
    }

    val heroList get() = _heroList

    class HeroViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        private val binding = HeroItemBinding.bind(view)

        //función para enlazar datos de cada elemento con su interfaz. Aquí se incluyen los listeners.
        fun bind(
            hero: Hero,
            onClickHero: (String) -> Unit,
            onClickDelete: (Int) -> Unit
        ) {
            binding.tvName.text = hero.name
            binding.rbIntelligence.rating = hero.intelligence.toFloat()/FullHero.SCALE
            binding.rbPower.rating = hero.power.toFloat()/FullHero.SCALE
            //tomar fotos de drawable...
            val context = binding.ivPhoto.context
            //val idPhoto = context.resources.getIdentifier("iron_man", DRAWABLE,context.packageName)
            Glide.with(context)
                .load(hero.photo)
                .into(binding.ivPhoto)

            binding.root.setOnClickListener {
                onClickHero(hero.id)
            }

            //borrado de un elemento de la lista con función lambda.
            binding.ivDelHero.setOnClickListener {
                onClickDelete(adapterPosition)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HeroViewHolder(layoutInflater.inflate(R.layout.hero_item,parent,false))
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val hero = heroList[position]
        holder.bind(hero, onClickHero, onClickDelete)

    }

    override fun getItemCount(): Int {
        return heroList.size
    }

    fun setHeroList(heroes : List<Hero>) {
        _heroList = heroes.toMutableList()
    }

    fun deleteHero(pos : Int) {
        _heroList.removeAt(pos)
    }

}