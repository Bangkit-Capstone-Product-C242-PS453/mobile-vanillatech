package umi.app.vantech.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import org.tensorflow.lite.DataType
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
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import umi.app.vantech.CommonUtils
import umi.app.vantech.PlantClas
import umi.app.vantech.PlantClasses
import umi.app.vantech.createCustomTempFile
import umi.app.vantech.data.PenyakitRepository
import umi.app.vantech.databinding.FragmentScanBinding
import umi.app.vantech.ml.BestFloat32
import umi.app.vantech.ml.PlantDisease
import umi.app.vantech.uriToFile
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder


class ScanFragment : Fragment() {
    private var _binding : FragmentScanBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null
    private lateinit var currentPhotoPath: String
    private val imageSize = 224
    var result : String? = null
    var acurasi : String? = null
    var namaPenyakit : String? = null
    var namaLain : String? = null
    var gejala : String? = null
    var solusi : String? = null
    private var progressDialog : Dialog? = null


    companion object{
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS){
            if (!allPermissionsGranted()){
                Toast.makeText(
                    context,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun showProgress(){
        hideProgress()
        progressDialog = CommonUtils.showLoadingDialog(requireContext());
    }

    private fun hideProgress(){
        progressDialog?.let { if(it.isShowing)it.cancel() }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScanBinding.inflate(inflater,container,false)
        val view = binding.root
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            previewImage.visibility = View.GONE
            btnUpload.visibility = View.GONE
            lyPanduan.visibility = View.VISIBLE

            btnKamera.setOnClickListener { startTakePhoto() }

            btnGaleri.setOnClickListener { startGallery() }

            binding.btnUpload.setOnClickListener {
                if (getFile == null) {
                    Toast.makeText(context, "Masukkan gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
                } else {

                    showProgress()

                    val handler = Handler()
                    handler.postDelayed(object : Runnable {
                        override fun run() {
                            hideProgress()
                            val intent = Intent(context, IdentifikasiPenyakitActivity::class.java)
                            intent.putExtra("NAMA_PENYAKIT", namaPenyakit)
                            intent.putExtra("NAMA_LAIN", namaLain)
                            intent.putExtra("AKURASI", acurasi)
                            intent.putExtra("GEJALA", gejala)
                            intent.putExtra("SOLUSI", solusi)
                            intent.putExtra("IMAGE_PATH_KEY", getFile?.absolutePath)
                            startActivity(intent)
                            requireActivity().finish()
//                            }
                        }
                    }, 3000)

                }

            }

        }
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
                val dimension = Math.min(bitmap.width, bitmap.height)
                val thumbnail = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension)
                binding.imgresult.setImageBitmap(thumbnail)

                val scaledBitmap = Bitmap.createScaledBitmap(thumbnail, imageSize, imageSize, false)
                classifyImage(scaledBitmap)
                binding.previewImage.visibility = View.VISIBLE
                binding.btnUpload.visibility = View.VISIBLE
                binding.lyPanduan.visibility = View.GONE

            }
        }

    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
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
                val dimension = Math.min(bitmap.width, bitmap.height)
                val thumbnail = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension)
                binding.imgresult.setImageBitmap(thumbnail)

                val scaledBitmap = Bitmap.createScaledBitmap(thumbnail, imageSize, imageSize, false)
                classifyImage(scaledBitmap)

                binding.previewImage.visibility = View.VISIBLE
                binding.btnUpload.visibility = View.VISIBLE
                binding.lyPanduan.visibility = View.GONE


            }
        }
    }


    private fun classifyImage(image: Bitmap) {
        try {
            val model = PlantDisease.newInstance(requireActivity().applicationContext)

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())

            val intValues = IntArray(imageSize * imageSize)
            image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)

            var pixel = 0
            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val `val` = intValues[pixel++]
                    byteBuffer.putFloat(((`val` shr 16) and 0xFF) * (1.0f / 127.5f))
                    byteBuffer.putFloat(((`val` shr 8) and 0xFF) * (1.0f / 127.5f))
                    byteBuffer.putFloat((`val` and 0xFF) * (1.0f / 127.5f))
                }
            }

            inputFeature0.loadBuffer(byteBuffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            val confidences = outputFeature0.floatArray

            var maxPos = 0
            var maxConfidence = 0.0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }

            val classes = PlantClas.classes


            result = classes[maxPos]

            val penyakitDetail = PenyakitRepository.getDetailPenyakitByName(result!!)

            if (penyakitDetail != null) {
                namaPenyakit = penyakitDetail.namaPenyakit
                namaLain = penyakitDetail.namaLain
                gejala = penyakitDetail.gejala
                solusi = penyakitDetail.solusi
                acurasi = String.format("%.1f%%", maxConfidence * 100)

            }

            model.close()

        } catch (e: IOException) {
            // Handle the exception
        }
    }


}