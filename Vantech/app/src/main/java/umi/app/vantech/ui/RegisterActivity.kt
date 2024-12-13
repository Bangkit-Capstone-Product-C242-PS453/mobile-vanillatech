package umi.app.vantech.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import umi.app.vantech.data.RegisterRequest
import umi.app.vantech.data.RegisterResponse
import umi.app.vantech.databinding.ActivityRegisterBinding
import umi.app.vantech.network.ApiHelper

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        binding.apply {
            val namaLengkap = edtNamaLengkap.text.toString().trim()
            val alamat = edtAlamat.text.toString().trim()

            val username = edtUsername.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()


            if (namaLengkap.isEmpty() || alamat.isEmpty()  || username.isEmpty() || email.isEmpty() || password.isEmpty() ) {
                Toast.makeText(applicationContext, "Lengkapi data terlebih dahulu", Toast.LENGTH_LONG).show()
                return
            }



            val request = RegisterRequest(username = username, email = email, password = password)
            registerUser(request)
        }
    }

    private fun registerUser(request: RegisterRequest) {
        val apiService = ApiHelper.createApiService()
        apiService.register(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    Toast.makeText(
                        applicationContext,
                        "Registrasi berhasil: ${registerResponse?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Registrasi gagal: ${response.errorBody()?.string()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
