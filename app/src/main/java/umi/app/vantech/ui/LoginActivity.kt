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
import umi.app.vantech.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var pref : Preferences
    private lateinit var mDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDatabase = FirebaseDatabase.getInstance().getReference("users")
        pref = Preferences(this)

        binding.apply {
            btnLogin.setOnClickListener {
                login()
            }
            tvKlikDisini.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }

    }

    private fun login() {
        binding.apply {
            val username = edtUsername.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()){
                Toast.makeText(applicationContext, "Silahkan isi username dan password",Toast.LENGTH_LONG).show()
                return
            }

            mDatabase.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        var isValidPassword = false
                        var userId = ""

                        for (snap in snapshot.children){
                            val user = snap.getValue(User::class.java)
                            if (user != null && user.password == password){
                                isValidPassword = true
                                userId = snap.key!!
                            }
                        }

                        if (isValidPassword){
                            pref.setValues("STATUS_LOGIN", "true")
                            pref.setValues("USERNAME", username)
                            pref.setValues("USER_ID", userId)

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()

                        }else{
                            Toast.makeText(applicationContext,"Password anda salah", Toast.LENGTH_LONG).show()
                        }

                    }else{
                        Toast.makeText(applicationContext,"Username anda salah", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                   //
                }

            })
        }

    }
}