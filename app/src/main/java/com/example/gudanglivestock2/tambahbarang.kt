package com.example.gudanglivestock2

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gudanglivestock2.databinding.ActivityStokbarangBinding
import com.example.gudanglivestock2.databinding.ActivityTambahbarangBinding
import com.google.firebase.firestore.FirebaseFirestore

class tambahbarang : AppCompatActivity() {
    private lateinit var binding: ActivityTambahbarangBinding
    private lateinit var spinner: Spinner
    private val daftarGudang = listOf("Silakan Pilih Gudang", "Gudang Depo Kereta", "Gudang PUK Medan", "Gudang PUS Tebing", "Gudang PUK Kisaran", "Gudang PUS Rantau")
    private lateinit var db: FirebaseFirestore
    private val daftarpilihan = listOf("Silakan Pilih Satuan", "Buah", "Pasang", "Liter")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahbarangBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()



        val adaptergudang = ArrayAdapter(this, android.R.layout.simple_spinner_item, daftarGudang)
        adaptergudang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnertambahbarang.adapter = adaptergudang

        val adapterpilihan = ArrayAdapter(this, android.R.layout.simple_spinner_item, daftarpilihan)
        adapterpilihan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerpilihsatuan.adapter = adapterpilihan




        binding.submit.setOnClickListener {
            tambahBarangKeFirestore()
        }










        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



    }

    private fun tambahBarangKeFirestore() {
        val gudangTerpilih = binding.spinnertambahbarang.selectedItem.toString()
        val pilihan = binding.spinnerpilihsatuan.selectedItem.toString()
        val namaBarang = binding.namabarang.text.toString().trim()
        val jumlahBarang = binding.jumlahbarang.text.toString().trim()

        if (namaBarang.isEmpty()) {
            binding.namabarang.error = "Nama barang harus diisi"
            binding.namabarang.requestFocus()
            return
        }


        if(jumlahBarang.isEmpty()) {
            binding.jumlahbarang.error = "Jumlah barang harus diisi"
            binding.jumlahbarang.requestFocus()
            return
        }

        if (gudangTerpilih == "Silakan Pilih Gudang") {
            Toast.makeText(this, "Pilih gudang terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }
        if (pilihan == "Pilih Satuan") {
            Toast.makeText(this, "Pilih satuan terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        val barang = hashMapOf(
            "namabarang" to namaBarang,
            "jumlah" to jumlahBarang,
            "satuan" to pilihan
        )

        val namaDocument = namaBarang
        db.collection("gudang").document(gudangTerpilih).collection("Barang").document(namaDocument).set(barang)
            .addOnSuccessListener {
                Toast.makeText(this, "Barang berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                binding.namabarang.text.clear()
                binding.jumlahbarang.text.clear()
                binding.spinnertambahbarang.setSelection(0)
                binding.spinnerpilihsatuan.setSelection(0)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal menambahkan barang: ${e.message}", Toast.LENGTH_SHORT).show()
            }





    }
}