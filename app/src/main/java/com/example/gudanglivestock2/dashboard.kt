package com.example.gudanglivestock2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gudanglivestock2.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        getUserData ()

        showWelcomeMessage()

        binding.tombolstokgudang.setOnClickListener{
            val intent = Intent (this, stokbarang::class.java)
            startActivity(intent)
        }

        binding.tomboltambahbarang.setOnClickListener{
            val intent = Intent (this, tambahbarang::class.java)
            startActivity(intent)
        }










        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.logout.setOnClickListener{
            auth.signOut()
            Toast.makeText(this, "Anda telah Logout", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()


        }










    }

    private fun showWelcomeMessage() {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)

        val message = when {
            hourOfDay in 0..11 -> "Selamat Pagi"
            hourOfDay in 12..17 -> "Selamat Siang"
            else -> "Selamat Malam"
        }
        binding.selamatdatang.text = message
    }

    private fun getUserData() {
        val user = auth.currentUser
        if (user != null) {
            val email = user.email // Ambil email pengguna dari FirebaseAuth

            if (email != null) {
                // Cari NIPP berdasarkan email
                db.collection("users").whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val document = documents.documents[0]
                            val nipp = document.getString("nipp") // Ambil NIPP dari Firestore

                            if (nipp != null) {
                                // Setelah dapat NIPP, ambil data berdasarkan NIPP sebagai document ID
                                db.collection("users").document(nipp)
                                    .get()
                                    .addOnSuccessListener { userDocument ->
                                        if (userDocument.exists()) {
                                            val nama = userDocument.getString("nama") // Ambil nama
                                            val nippUser = userDocument.getString("nipp") // Ambil NIPP

                                            // Tampilkan di TextView menggunakan binding
                                            binding.user.text = nama ?: "Nama tidak tersedia"
                                            binding.tvnipp.text = nippUser ?: "NIPP tidak tersedia"
                                        } else {
                                            binding.user.text = "Data tidak ditemukan"
                                            binding.tvnipp.text = "Data tidak ditemukan"
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        binding.user.text = "Error: ${exception.message}"
                                        binding.tvnipp.text = "Error: ${exception.message}"
                                    }
                            } else {
                                binding.user.text = "Nama tidak ditemukan"
                                binding.tvnipp.text = "NIPP tidak ditemukan"
                            }
                        } else {
                            binding.user.text = "Data pengguna tidak ditemukan"
                            binding.tvnipp.text = "Data pengguna tidak ditemukan"
                        }
                    }
                    .addOnFailureListener { exception ->
                        binding.user.text = "Error: ${exception.message}"
                        binding.tvnipp.text = "Error: ${exception.message}"
                    }
            } else {
                binding.user.text = "Email tidak tersedia"
                binding.tvnipp.text = "Email tidak tersedia"
            }
        } else {
            binding.user.text = "Pengguna tidak terautentikasi"
            binding.tvnipp.text = "Pengguna tidak terautentikasi"
        }
    }







}