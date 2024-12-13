package umi.app.vantech.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import umi.app.vantech.Preferences
import umi.app.vantech.createCustomTempFile
import umi.app.vantech.data.PredictionResponse
import umi.app.vantech.databinding.FragmentScanBinding
import umi.app.vantech.model.ApiViewModelFactory
import umi.app.vantech.network.ApiViewModel
import umi.app.vantech.uriToFile
import java.io.File

class ScanFragment : Fragment() {
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null
    private lateinit var currentPhotoPath: String
    private lateinit var viewModel: ApiViewModel

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val TAG = "ScanFragment"
        private const val ERROR_FILE_TOO_LARGE = "File terlalu besar untuk diupload"
        private const val ERROR_AUTH_FAILED = "Autentikasi gagal, silakan login ulang"
        private const val ERROR_NETWORK = "Kesalahan jaringan"
        private const val ERROR_UNEXPECTED = "Terjadi kesalahan"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        initializeViewModel()
        observeViewModel()
        checkPermissions()
        return binding.root
    }

    private fun initializeViewModel() {
        val preferences = Preferences(requireContext())
        val viewModelFactory = ApiViewModelFactory(preferences)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ApiViewModel::class.java)
    }

    private fun checkPermissions() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun observeViewModel() {
        viewModel.predictionResponse.observe(viewLifecycleOwner) { prediction ->
            prediction?.let { handlePredictionResult(it) }
        }

//        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
//            error?.let { handleError(it) }
//        }
    }

//    private fun handleError(error: String) {
//        Log.e(TAG, "Error from ViewModel: $error")
//        val message = when {
//            error.contains("413") -> ERROR_FILE_TOO_LARGE
//            error.contains("401") -> ERROR_AUTH_FAILED
//            error.contains("Network error") -> "$ERROR_NETWORK: $error"
//            else -> "$ERROR_UNEXPECTED: $error"
//        }
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//        hideProgress()
//    }

    private fun handlePredictionResult(prediction: PredictionResponse) {
        if (prediction.diseaseRecords.isEmpty()) {
            Toast.makeText(context, "Tidak dapat memprediksi gambar", Toast.LENGTH_SHORT).show()
            return
        }

        val firstDiseaseRecord = prediction.diseaseRecords.firstOrNull()
        if (firstDiseaseRecord != null) {
            val intent = Intent(context, IdentifikasiPenyakitActivity::class.java).apply {
                putExtra("DISEASE_RECORD", firstDiseaseRecord)
            }
            startActivity(intent)
        } else {
            Toast.makeText(context, "Tidak ada data penyakit yang valid", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            previewImage.visibility = View.GONE
            btnUpload.visibility = View.GONE
            lyPanduan.visibility = View.VISIBLE

            btnKamera.setOnClickListener { startTakePhoto() }
            btnGaleri.setOnClickListener { startGallery() }
            btnUpload.setOnClickListener {
                if (getFile == null) {
                    Toast.makeText(context, "Masukkan gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
                } else {
                    showProgress()
                    classifyImageWithApi()
                }
            }
        }
    }

    private fun classifyImageWithApi() {
        getFile?.let { file ->
            if (!file.exists() || !file.canRead()) {
                Toast.makeText(context, "Gambar tidak valid atau tidak dapat dibaca", Toast.LENGTH_SHORT).show()
                return
            }

            val mediaType = when {
                file.extension.equals("png", ignoreCase = true) -> "image/png"
                file.extension.equals("jpg", ignoreCase = true) || file.extension.equals("jpeg", ignoreCase = true) -> "image/jpeg"
                else -> {
                    Toast.makeText(context, "Hanya format PNG dan JPEG yang diperbolehkan", Toast.LENGTH_SHORT).show()
                    return
                }
            }

            val imagePart = MultipartBody.Part.createFormData(
                "image",
                file.name,
                file.asRequestBody(mediaType.toMediaTypeOrNull())
            )
            viewModel.predictImage(imagePart)
        } ?: Toast.makeText(context, "Masukkan gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
    }

    private fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)

        createCustomTempFile(requireActivity().application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "umi.app.vantech.ui",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            myFile.let { file ->
                val bitmap = BitmapFactory.decodeFile(file.path)
                prepareImageForPreview(bitmap)
            }
        }
    }

    private fun startGallery() {
        val intent = Intent().apply {
            action = ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, requireContext())
                getFile = myFile

                val inputStream = requireActivity().contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                prepareImageForPreview(bitmap)
            }
        }
    }

    private fun prepareImageForPreview(bitmap: Bitmap) {
        val dimension = Math.min(bitmap.width, bitmap.height)
        val thumbnail = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension)
        binding.imgresult.setImageBitmap(thumbnail)

        binding.previewImage.visibility = View.VISIBLE
        binding.btnUpload.visibility = View.VISIBLE
        binding.lyPanduan.visibility = View.GONE
    }
}