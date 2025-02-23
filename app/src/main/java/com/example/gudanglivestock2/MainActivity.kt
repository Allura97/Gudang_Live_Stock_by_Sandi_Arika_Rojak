package com.example.gudanglivestock2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gudanglivestock2.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.tombollupapassword.setOnClickListener {
            val intent = Intent (this,lupapassword::class.java)
            startActivity(intent)
        }




        binding.register.setOnClickListener {
            val Intent = Intent(this, register::class.java)
            startActivity(Intent)
        }
        auth = FirebaseAuth.getInstance()
        binding.login.setOnClickListener{
            val email = binding.email1.text.toString()
            val password = binding.password.text.toString()

            if (email.isEmpty()){
                binding.email1.error = "Email Harus Diisi"
                binding.email1.requestFocus()
                return@setOnClickListener
            }


            if (password.isEmpty()) {
                binding.password.error = "Password Harus Diisi"
                binding.password.requestFocus()
                return@setOnClickListener

            }


            LoginFirebase(email, password)



        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun LoginFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { it: Task<AuthResult> ->
                if (it.isSuccessful) {
                    Toast.makeText(this, "Selamat Datang $email", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, dashboard::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    Toast.makeText(this, "Username / Password Salah", Toast.LENGTH_SHORT).show()
                    val exceptionMessage = when (it.exception) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            "Password salah, coba lagi."
                        }
                        is FirebaseAuthInvalidUserException -> {
                            "Email tidak terdaftar. Silakan daftar terlebih dahulu."
                        }
                        is FirebaseAuthUserCollisionException -> {
                            "Akun dengan email ini sudah ada."
                        }
                        is FirebaseNetworkException -> {
                            "Tidak ada koneksi internet. Periksa koneksi Anda dan coba lagi."
                        }
                        else -> {
                            "Login gagal karena kesalahan yang tidak diketahui."
                        }
                    }
                    Toast.makeText(this, exceptionMessage, Toast.LENGTH_SHORT).show()
                }

            }

    }


}