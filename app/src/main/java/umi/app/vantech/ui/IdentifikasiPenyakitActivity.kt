package umi.app.vantech.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import umi.app.vantech.databinding.ActivityIdentifikasiPenyakitBinding

class IdentifikasiPenyakitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIdentifikasiPenyakitBinding
    private var progr = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdentifikasiPenyakitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val namaPenyakit  = intent.getStringExtra("NAMA_PENYAKIT")
        val namaLain = intent.getStringExtra("NAMA_LAIN")
        val acurasi = intent.getStringExtra("AKURASI")
        val gejala = intent.getStringExtra("GEJALA")
        val solusi = intent.getStringExtra("SOLUSI")


        binding.apply {

            tvNamaPenyakit.text = namaPenyakit
            tvNamaLainPenyakit.text = namaLain
            tvGejala.text = gejala
            tvSolusi.text = solusi


            btnBack.setOnClickListener {
                startActivity(Intent(this@IdentifikasiPenyakitActivity, MainActivity::class.java))
                finish()
            }
        }


    }
}