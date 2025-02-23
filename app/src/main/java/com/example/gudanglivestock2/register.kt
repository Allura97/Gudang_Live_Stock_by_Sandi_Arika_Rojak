package com.example.gudanglivestock2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gudanglivestock2.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class register : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.register2.setOnClickListener {
            val nama = binding.nama.text.toString()
            val nipp = binding.nipp.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()



            if (nama.isEmpty()) {
                binding.nama.error = "Nama Harus Diisi"
                binding.nama.requestFocus()
                return@setOnClickListener
            }



            if (nipp.isEmpty()) {
                binding.nipp.error = "Nipp Harus Diisi"
                binding.nipp.requestFocus()
                return@setOnClickListener
            }




            if (email.isEmpty()) {
                binding.email.error = "Email harus diisi"
                binding.email.requestFocus()
                return@setOnClickListener

            }



            if (password.isEmpty()) {
                binding.password.error = "Pasword Harus Diisi"
                binding.email.requestFocus()
                return@setOnClickListener


            }


            if (password.length < 8) {
                binding.password.error = "Minimal 8 Karakter"
                binding.password.requestFocus()
                return@setOnClickListener
            }

            RegisterFirebase (email,password,nama,nipp)


        }












        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }






    }

    private fun RegisterFirebase(email: String, password: String, nama: String, nipp: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registrasi sukses
                    val user = auth.currentUser

                    // Update profile dengan nama pengguna
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(nama)
                        .build()




                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                val userData = hashMapOf(
                                    "uid" to user.uid,
                                    "nama" to nama,
                                    "nipp" to nipp,
                                    "email" to email
                                )

                                db.collection("users").document(nipp)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        Log.d("RegisterActivity", "User profile updated and data stored in Firestore.")
                                        Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("RegisterActivity", "Error saving user data", e)
                                        Toast.makeText(this, "Gagal menyimpan data: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }





                                Log.d("RegisterActivity", "User profile updated.")
                                Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                                // Navigasi ke halaman berikutnya atau lakukan hal lain
                                val intent = Intent(this,MainActivity::class.java)
                                startActivity(intent)
                            }

                        }
                }
                else {
                    // Jika registrasi gagal, tampilkan pesan
                    Toast.makeText(this, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()


                }
            }

    }
}