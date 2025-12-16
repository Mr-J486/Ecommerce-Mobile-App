package com.example.e_commercemobapp.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commercemobapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddCategoryActivity extends AppCompatActivity {

    private EditText categoryNameInput, categoryDescriptionInput;
    private Button saveCategoryBtn;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        // UI elements
        categoryNameInput = findViewById(R.id.categoryNameInput);
        categoryDescriptionInput = findViewById(R.id.categoryDescriptionInput);
        saveCategoryBtn = findViewById(R.id.saveCategoryBtn);

        // Firebase
        db = FirebaseFirestore.getInstance();

        saveCategoryBtn.setOnClickListener(v -> saveCategory());
    }

    private void saveCategory() {
        String name = categoryNameInput.getText().toString().trim();
        String description = categoryDescriptionInput.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "AdminCategory name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> category = new HashMap<>();
        category.put("name", name);
        category.put("description", description);

        db.collection("categories")
                .add(category)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "AdminCategory added!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}
