package umi.app.vantech.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import umi.app.vantech.Preferences
import umi.app.vantech.data.User
import umi.app.vantech.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var mDatabase : DatabaseReference
    private lateinit var pref : Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDatabase = FirebaseDatabase.getInstance().getReference("users")
        pref = Preferences(this)

        binding.btnRegister.setOnClickListener {
            saveData()
        }

    }

    private fun saveData() {
        binding.apply {
            val namaLengkap = edtNamaLengkap.text.toString().trim()
            val alamat = edtAlamat.text.toString().trim()
            val noTelp = edtTelp.text.toString().trim()
            val username = edtUsername.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val konfirPassword = edtKonfirmasiPassword.text.toString().trim()

            if (namaLengkap.isEmpty() || alamat.isEmpty() || noTelp.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()||konfirPassword.isEmpty()){
                Toast.makeText(applicationContext, "Lengkapi data terlebih dahulu", Toast.LENGTH_LONG).show()
                return
            }

            if (password != konfirPassword){
                Toast.makeText(applicationContext, "Password tidak sama", Toast.LENGTH_LONG).show()
                return
            }

            mDatabase.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        Toast.makeText(applicationContext,"Username sudah digunakan", Toast.LENGTH_LONG).show()
                    }else{
                        val id = mDatabase.push().key
                        val userData = User(id, username, password, namaLengkap, alamat, noTelp, email)

                        if (id !== null){
                            mDatabase.child(id).setValue(userData).addOnCompleteListener {
                                Toast.makeText(applicationContext, "Registrasi berhasil", Toast.LENGTH_LONG).show()
                                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                finish()
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    //
                }

            })

        }
    }
}