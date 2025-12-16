package com.example.e_commercemobapp.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobapp.R
import com.google.firebase.firestore.FirebaseFirestore

class CategoryListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter
    private val categoryList = ArrayList<Category>()
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)

        recyclerView = findViewById(R.id.categoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CategoryAdapter(categoryList)
        recyclerView.adapter = adapter

        val addBtn: Button = findViewById(R.id.addCategoryBtn)
        addBtn.setOnClickListener {
            val intent = Intent(this, AddCategoryActivity::class.java)
            startActivity(intent)
        }

        db = FirebaseFirestore.getInstance()

        loadCategories()
    }

    override fun onResume() {
        super.onResume()
        loadCategories() // refresh when returning
    }

    private fun loadCategories() {
        db.collection("categories")
            .get()
            .addOnSuccessListener { querySnapshot ->
                categoryList.clear()

                for (doc in querySnapshot) {
                    val id = doc.id
                    val name = doc.getString("name") ?: ""
                    val description = doc.getString("description") ?: ""

                    categoryList.add(
                        Category(id, name, description)
                    )
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error loading categories", e)
            }
    }
}
