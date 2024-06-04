package com.example.guesstheword.utils

import android.Manifest
import android.content.res.Resources
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.guesstheword.R
import com.example.guesstheword.databinding.FragmentCameraBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

class CameraFragment : Fragment() {

    private lateinit var binding : FragmentCameraBinding

    //variable para las capturas de imágenes.
    private lateinit var imageCapture : ImageCapture

    //Flujo de estado para colectar desde el fragmento padre que contiene a este y ver si se otorgan los permisos.
    private val _grantedPermissionState : MutableStateFlow<Boolean> = MutableStateFlow(true)
    val grantedPermissionState : StateFlow<Boolean> = _grantedPermissionState.asStateFlow()

    //nombre de la foto tomada.
    private var _photoName :String? = null
    val photoName get() = _photoName

    //variable para la petición de permisos. Se inicializa en alguno de los métodos onCreate.
    private val cameraPermissionLauncher by lazy {
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            _grantedPermissionState.value = isGranted
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater,container,false)

        //Lanzamiento de la petición de permiso del uso de la cámara.
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)

        //configura la cámara para la captura de imágenes.
        setImageCapture()

        //evento del botón de tomar captura.
        setListeners()

        return binding.root
    }

    private fun setListeners() {
        //al pulsar el botón de foto se alterna entre la imagen de toma de foto y volver a la cámara.
        //Hace invisible la previsualización de la cámara si se realiza la foto, dejando visible el ivPicture.
        binding.fabTakePict.setOnClickListener {
            if(binding.viewFinderPict.isVisible) {
                captureImage()
                binding.viewFinderPict.isVisible = false
                binding.fabTakePict.setImageResource(R.drawable.ic_camera)
            } else {
                binding.viewFinderPict.isVisible = true
                binding.fabTakePict.setImageResource(R.drawable.ic_take_pict)
            }
        }
    }

    //configura la cámara.
    fun setImageCapture() {
        // Configruación de la captura de imagen con su builder.
        imageCapture = ImageCapture.Builder().apply {
            setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        }.build()


        // Configura la cámara y lo enlaza a su viewFinder para su previsualización.
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        val executor = ContextCompat.getMainExecutor(requireContext())

        cameraProviderFuture.addListener({
            // Aquí se produce en enlace entre la previsualización y el elemento.
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinderPict.surfaceProvider)
            }

            // Selecciona la cámara frontal por defecto.
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // desvincula cualquier instancia anterior.
                cameraProvider.unbindAll()

                // Enlaza la cámara a las posibles capturas.
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

            } catch (e: Exception) {
                Log.e("MyError", "Binding failed", e)
            }
        }, executor)
    }

    //Toma una foto y devuelve el nombre de esta.
    fun captureImage() {
        // Crear un archivo de salida para guardar la imagen capturada
        _photoName = "photo_${System.currentTimeMillis()}.jpg"
        val photoFile = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            _photoName!!
        )

        // Configurar el objeto de captura para que guarde la imagen en el archivo de salida
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Capturar la imagen y guardarla en el archivo de salida
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // Mostrar un mensaje indicando que la imagen se ha guardado correctamente
                    Toast.makeText(requireContext(), "Image saved: ${photoFile.absolutePath}", Toast.LENGTH_LONG).show()
                    //Log.d("camera","Image saved: ${photoFile.absolutePath}")
                    //carga la imagen en un ImageView que se encuentra en el fragmento creado para mostrar la captura realizada.
                    Glide.with(requireContext())
                        .load(File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),_photoName!!))
                        .override(Resources.getSystem().displayMetrics.widthPixels/2)
                        .circleCrop()
                        .into(binding.ivPicture)
                }

                override fun onError(exception: ImageCaptureException) {
                    // Mostrar un mensaje de error si la imagen no se ha podido guardar y vuelve el nombre de foto a null.
                    Toast.makeText(requireContext(), "Error saving image: ${exception.message}", Toast.LENGTH_LONG).show()
                    _photoName = null
                }
            }
        )
    }
}