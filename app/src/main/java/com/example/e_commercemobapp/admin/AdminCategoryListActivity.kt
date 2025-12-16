package com.example.e_commercemobapp.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobapp.R
import com.example.e_commercemobapp.model.Category
import com.google.firebase.firestore.FirebaseFirestore
import android.view.View


class AdminCategoryListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminCategoryAdapter
    private val categories = ArrayList<Category>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)

        recyclerView = findViewById(R.id.categoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = AdminCategoryAdapter(categories) { selectedCat ->
            val intent = Intent(this, AdminEditCategoryActivity::class.java)
            intent.putExtra("id", selectedCat.id)
            intent.putExtra("name", selectedCat.name)
            intent.putExtra("description", selectedCat.description)
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        findViewById<View>(R.id.addCategoryBtn).setOnClickListener {
            startActivity(Intent(this, AdminAddCategoryActivity::class.java))
        }

        loadCategories()
    }

    override fun onResume() {
        super.onResume()
        loadCategories()
    }

    private fun loadCategories() {
        db.collection("categories")
            .get()
            .addOnSuccessListener { snap ->
                categories.clear()
                for (doc in snap) {
                    categories.add(
                        Category(
                            id = doc.id,
                            name = doc.getString("name") ?: "",
                            description = doc.getString("description") ?: ""
                        )
                    )
                }
                adapter.notifyDataSetChanged()
            }
    }
}
