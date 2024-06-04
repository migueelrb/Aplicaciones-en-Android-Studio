package com.example.guesstheword.screens.photo

import android.os.Bundle
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
import androidx.navigation.fragment.navArgs
import com.example.guesstheword.R
import com.example.guesstheword.databinding.FragmentPhotoBinding
import com.example.guesstheword.utils.CameraFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class PhotoFragment : Fragment() {

    private lateinit var binding : FragmentPhotoBinding

    private val photoVM : PhotoVM by viewModels<PhotoVM> { PhotoVM.Factory }

    //name y level que llegan desde el fragmento de Settings.
    private val args : PhotoFragmentArgs by navArgs<PhotoFragmentArgs>()

    //Inserción de la declaración del fragmento de cámara para acceder a sus métodos.
    private val cameraFragment : CameraFragment by lazy { childFragmentManager.findFragmentById(R.id.fragmentCamera) as CameraFragment }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPhotoBinding.inflate(inflater,container,false)

        //guardamos en VM name y level.
        photoVM.setNameAndLevel(args.name,args.level)

        //Listeners de botones.
        setListeners()

        return binding.root
    }

    private fun setListeners() {
        //botón para realizar el guardado de la imagen si se ha realizado alguna foto.
        binding.btnSavePict.setOnClickListener {
            //si no es nulo guarda la configuración en datastore y levanta bandera savedPhoto.
            cameraFragment.photoName?.let {
                photoVM.savePhoto(it)
            } ?: Snackbar.make(requireView(),R.string.take_picture,Snackbar.LENGTH_SHORT).show()
        }
        //botón de volver. No se guarda nada.
        binding.btnBackFromPhoto.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCollectors()
    }

    private fun setCollectors() {
        //observa si se ha levantado la bandera de foto guardada y vuelve al fragmento de settings.
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoVM.uiState.collect { photoState ->
                    if(photoState.savedPhoto)
                        findNavController().popBackStack()
                }
            }
        }
        //observa si se ha concedido el permiso de uso de cámara en el fragmento de cámara.
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                //comprueba los permisos de la cámara para salir en caso de que se denieguen.
                cameraFragment.grantedPermissionState.collect { grantedPermission ->
                    if(!grantedPermission) {
                        Snackbar.make(requireView(), R.string.grant_permissions, Snackbar.LENGTH_SHORT)
                            .show()
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }
}