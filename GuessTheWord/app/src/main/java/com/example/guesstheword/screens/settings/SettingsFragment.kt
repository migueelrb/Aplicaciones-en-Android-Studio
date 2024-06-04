package com.example.guesstheword.screens.settings

import android.content.res.Resources
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.guesstheword.R
import com.example.guesstheword.databinding.FragmentSettingsBinding
import com.example.guesstheword.datamodel.UserPreferences
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.io.File


class SettingsFragment : Fragment() {

    private lateinit var binding : FragmentSettingsBinding

    private val settingsVM : SettingsVM by viewModels<SettingsVM> {  SettingsVM.Factory }

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
        binding = FragmentSettingsBinding.inflate(inflater,container,false)

        setListeners()

        return binding.root
    }

    private fun setListeners() {
        binding.btnBackSettings.setOnClickListener {
            validateName(binding.etUserName.text.toString())
        }
        binding.ivPictUser.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToPhotoFragment(binding.etUserName.text.toString(),setLevelInt()))
        }
        binding.ivDelPictProfile.setOnClickListener {
            settingsVM.delPicture()
        }
    }

    private fun validateName(name: String) {
        if(name.isBlank())
            Snackbar.make(requireView(),getString(R.string.name_is_empty),Snackbar.LENGTH_SHORT).show()
        else {
            settingsVM.saveSettings(name,setLevelInt())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCollectors()

    }

    private fun setLevelInt(): Int {
        return when {
            binding.rbLow.isChecked -> UserPreferences.LOW
            binding.rbMedium.isChecked -> UserPreferences.MEDIUM
            else -> UserPreferences.HIGH
        }
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                //inicialización de campos.
                settingsVM.uiState.collect { userSettings ->
                    binding.etUserName.setText(userSettings.name)
                    Glide.with(requireContext())
                            .load(
                                if(userSettings.photo != "")
                                    File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),userSettings.photo)
                                else
                                    R.drawable.avatar
                            )
                            .override(Resources.getSystem().displayMetrics.widthPixels/2)
                            .circleCrop()
                            .into(binding.ivPictUser)
                    when (userSettings.level) {
                        UserPreferences.LOW -> binding.rbLow.isChecked = true
                        UserPreferences.MEDIUM -> binding.rbMedium.isChecked = true
                        UserPreferences.HIGH -> binding.rbHard.isChecked = true
                    }
                    //si se guardan los cambios, vuelve al fragmento principal.
                    if(userSettings.savedSettings) {
                        Snackbar.make(requireView(),R.string.saved_settings,Snackbar.LENGTH_SHORT).show()
                        settingsVM.savedSettingsCompleted()
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }
}