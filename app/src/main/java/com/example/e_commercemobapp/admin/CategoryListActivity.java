package com.example.e_commercemobapp.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commercemobapp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CategoryListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdminCategoryAdapter adapter;
    private ArrayList<AdminCategory> categoryList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        recyclerView = findViewById(R.id.categoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdminCategoryAdapter(categoryList);
        recyclerView.setAdapter(adapter);

        Button addBtn = findViewById(R.id.addCategoryBtn);

        // Navigate to Add AdminCategory screen
        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CategoryListActivity.this, AddCategoryActivity.class);
            startActivity(intent);
        });

        db = FirebaseFirestore.getInstance();

        loadCategories();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCategories();  // Auto-refresh when returning
    }

    private void loadCategories() {
        db.collection("categories")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    categoryList.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                        String id = doc.getId();
                        String name = doc.getString("name");

                        Object descObj = doc.get("description");
                        String description = descObj != null ? descObj.toString() : "";

                        AdminCategory category = new AdminCategory(id, name, description);
                        categoryList.add(category);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Log.e("Firestore", "Error loading categories", e)
                );
    }
}
