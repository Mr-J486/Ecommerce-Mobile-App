package com.example.e_commercemobapp.admin;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commercemobapp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {

    private EditText nameInput, descriptionInput, priceInput, stockInput, barcodeInput, imageUrlInput;
    private Spinner categorySpinner;
    private Button saveProductBtn;

    private FirebaseFirestore db;

    private final ArrayList<String> categoryNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        nameInput = findViewById(R.id.productNameInput);
        descriptionInput = findViewById(R.id.productDescriptionInput);
        priceInput = findViewById(R.id.productPriceInput);
        stockInput = findViewById(R.id.productStockInput);
        barcodeInput = findViewById(R.id.productBarcodeInput);
        imageUrlInput = findViewById(R.id.productImageUrlInput);
        categorySpinner = findViewById(R.id.categorySpinner);
        saveProductBtn = findViewById(R.id.saveProductBtn);

        db = FirebaseFirestore.getInstance();

        loadCategories();

        saveProductBtn.setOnClickListener(v -> saveProduct());
    }

    // 🔥 Load categories from Firestore
    private void loadCategories() {
        db.collection("categories")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    categoryNames.clear();

                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String name = doc.getString("name");
                        if (name != null) {
                            categoryNames.add(name);
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            this,
                            android.R.layout.simple_spinner_item,
                            categoryNames
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(adapter);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load categories", Toast.LENGTH_SHORT).show()
                );
    }

    // 🔥 Save product
    private void saveProduct() {
        String name = nameInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();
        String priceStr = priceInput.getText().toString().trim();
        String stockStr = stockInput.getText().toString().trim();
        String barcode = barcodeInput.getText().toString().trim();
        String imageUrl = imageUrlInput.getText().toString().trim();
        String categoryName = categorySpinner.getSelectedItem().toString();

        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty() || barcode.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        int stock;

        try {
            price = Double.parseDouble(priceStr);
            stock = Integer.parseInt(stockStr);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid price or stock", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> product = new HashMap<>();
        product.put("name", name);
        product.put("description", description);
        product.put("price", price);
        product.put("stock", stock);
        product.put("barcode", barcode);
        product.put("imageUrl", imageUrl);
        product.put("categoryName", categoryName);

        db.collection("products")
                .add(product)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Firestore error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}
