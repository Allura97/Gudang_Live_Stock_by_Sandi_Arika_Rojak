package com.example.gudanglivestock2

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gudanglivestock2.databinding.ActivityStokbarangBinding
import com.google.firebase.firestore.FirebaseFirestore

class stokbarang : AppCompatActivity() {

    private lateinit var binding: ActivityStokbarangBinding
    private lateinit var spinner: Spinner
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterBarang: BarangAdapter
    private val daftarGudang = listOf("Silakan Pilih Gudang", "Gudang Depo Kereta", "Gudang PUK Medan", "Gudang PUS Tebing", "Gudang PUK Kisaran", "Gudang PUS Rantau")
    private lateinit var db: FirebaseFirestore

    private var barangList = mutableListOf<Barang>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStokbarangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        spinner = binding.spinnerpilihgudang
        recyclerView = binding.recyclerpilihgudang
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapterBarang = BarangAdapter(barangList)
        recyclerView.adapter = adapterBarang

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, daftarGudang)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedGudang = daftarGudang[position]
                ambilDataDariFirestore(selectedGudang)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun ambilDataDariFirestore(namaGudang: String) {
        db.collection("gudang").document(namaGudang).collection("Barang")
            .get()
            .addOnSuccessListener { result ->
                barangList.clear() // Kosongkan list sebelum menambahkan data baru
                barangList.addAll(result.toObjects(Barang::class.java))
                adapterBarang.notifyDataSetChanged() // Perbarui tampilan RecyclerView
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}