package com.example.e_commercemobapp.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commercemobapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditCategoryActivity extends AppCompatActivity {

    private EditText editCategoryName, editCategoryDescription;
    private Button updateCategoryBtn, deleteCategoryBtn;

    private FirebaseFirestore db;
    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        editCategoryName = findViewById(R.id.editCategoryName);
        editCategoryDescription = findViewById(R.id.editCategoryDescription);
        updateCategoryBtn = findViewById(R.id.updateCategoryBtn);
        deleteCategoryBtn = findViewById(R.id.deleteCategoryBtn);

        db = FirebaseFirestore.getInstance();

        // Receive data from adapter
        categoryId = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");

        editCategoryName.setText(name);
        editCategoryDescription.setText(description);

        updateCategoryBtn.setOnClickListener(v -> updateCategory());
        deleteCategoryBtn.setOnClickListener(v -> deleteCategory());
    }

    private void updateCategory() {
        String newName = editCategoryName.getText().toString().trim();
        String newDesc = editCategoryDescription.getText().toString().trim();

        db.collection("categories")
                .document(categoryId)
                .update("name", newName, "description", newDesc)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Category updated!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    private void deleteCategory() {
        db.collection("categories")
                .document(categoryId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Category deleted!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Delete error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}
