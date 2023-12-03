package com.example.otakuizz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.otakuizz.databinding.FragmentGameBinding
import com.example.otakuizz.databinding.FragmentGameOverBinding
import com.example.otakuizz.databinding.FragmentGamewonBinding

class GamewonFragment : Fragment() {

    private var _binding : FragmentGamewonBinding? = null
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
        // Inflate the layout for this fragment
        _binding = FragmentGamewonBinding.inflate(inflater,container,false)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_gamewonFragment_to_gameFragment)
        }

        return binding.root
    }

}