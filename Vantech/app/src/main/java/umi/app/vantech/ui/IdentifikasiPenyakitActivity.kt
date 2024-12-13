package umi.app.vantech.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import umi.app.vantech.data.Disease
import umi.app.vantech.data.DiseaseRecord
import umi.app.vantech.databinding.ActivityIdentifikasiPenyakitBinding
import umi.app.vantech.network.ApiHelper

class IdentifikasiPenyakitActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIdentifikasiPenyakitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdentifikasiPenyakitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the DiseaseRecord object passed via intent
        val diseaseRecord = intent.getParcelableExtra<DiseaseRecord>("DISEASE_RECORD")

        // Check if the diseaseRecord is not null and proceed
        if (diseaseRecord != null) {
            updateUI(diseaseRecord)
        } else {
            finish() // Close the activity
        }

        // Back button listener
        binding.btnBack.setOnClickListener {
            finish() // Close the activity
        }
    }

    // Function to update the UI with the received disease record
    private fun updateUI(diseaseRecord: DiseaseRecord) {
        val disease = diseaseRecord.disease

        // Update the UI components with the disease details
        binding.tvNamaPenyakit.text = disease.name
        binding.tvGejala.text = disease.symptoms.joinToString("\n• ", prefix = "• ")
        binding.tvSolusi.text = disease.prevention.joinToString("\n• ", prefix = "• ")

        // Fetch additional disease details by its ID from the API
        getDiseaseById(disease.id)
    }

    // Function to call the API and fetch disease details by its ID
    private fun getDiseaseById(id: Int) {
        val apiService = ApiHelper.createApiService()

        apiService.getRecordById(id).enqueue(object : Callback<DiseaseRecord> {
            override fun onResponse(call: Call<DiseaseRecord>, response: Response<DiseaseRecord>) {
                if (response.isSuccessful) {
                    val diseaseRecord = response.body()
                    if (diseaseRecord != null) {
                        updateUI(diseaseRecord)
                    }
                }
            }

            override fun onFailure(call: Call<DiseaseRecord>, t: Throwable) {
                // Handle failure silently
            }
        })
    }
}
