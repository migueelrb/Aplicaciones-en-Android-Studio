package com.example.otakuizz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.otakuizz.databinding.FragmentTitleBinding

class TitleFragment : Fragment() {

    private var _binding : FragmentTitleBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTitleBinding.inflate(layoutInflater,container,false)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
        
        binding.btnPlay.setOnClickListener {
            findNavController().navigate(R.id.action_titleFragment_to_gameFragment)
        }

        return binding.root
    }

}
