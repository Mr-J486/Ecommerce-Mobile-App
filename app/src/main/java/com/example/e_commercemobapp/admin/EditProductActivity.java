package com.example.e_commercemobapp.admin;

import android.os.Bundle;
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

public class EditProductActivity extends AppCompatActivity {

    private EditText nameInput, descriptionInput, priceInput, stockInput, barcodeInput, imageUrlInput;
    private Spinner categorySpinner;
    private Button updateBtn, deleteBtn;

    private FirebaseFirestore db;
    private String productId;

    private final ArrayList<String> categoryNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        productId = getIntent().getStringExtra("productId");
        if (productId == null) {
            finish();
            return;
        }

        nameInput = findViewById(R.id.productNameInput);
        descriptionInput = findViewById(R.id.productDescriptionInput);
        priceInput = findViewById(R.id.productPriceInput);
        stockInput = findViewById(R.id.productStockInput);
        barcodeInput = findViewById(R.id.productBarcodeInput);
        imageUrlInput = findViewById(R.id.productImageUrlInput);
        categorySpinner = findViewById(R.id.categorySpinner);

        updateBtn = findViewById(R.id.updateProductBtn);
        deleteBtn = findViewById(R.id.deleteProductBtn);

        db = FirebaseFirestore.getInstance();

        loadCategories();
        loadProduct();

        updateBtn.setOnClickListener(v -> updateProduct());
        deleteBtn.setOnClickListener(v -> deleteProduct());
    }

    private void loadCategories() {
        db.collection("categories")
                .get()
                .addOnSuccessListener(snapshot -> {
                    categoryNames.clear();
                    for (QueryDocumentSnapshot doc : snapshot) {
                        String name = doc.getString("name");
                        if (name != null) categoryNames.add(name);
                    }
                    categorySpinner.setAdapter(
                            new android.widget.ArrayAdapter<>(
                                    this,
                                    android.R.layout.simple_spinner_item,
                                    categoryNames
                            )
                    );
                });
    }

    private void loadProduct() {
        db.collection("products").document(productId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) return;

                    nameInput.setText(doc.getString("name"));
                    descriptionInput.setText(doc.getString("description"));
                    priceInput.setText(String.valueOf(doc.getDouble("price")));
                    stockInput.setText(String.valueOf(doc.getLong("stock")));
                    barcodeInput.setText(doc.getString("barcode"));
                    imageUrlInput.setText(doc.getString("imageUrl"));

                    String category = doc.getString("categoryName");
                    if (category != null && categoryNames.contains(category)) {
                        categorySpinner.setSelection(categoryNames.indexOf(category));
                    }
                });
    }

    private void updateProduct() {
        Map<String, Object> product = new HashMap<>();
        product.put("name", nameInput.getText().toString().trim());
        product.put("description", descriptionInput.getText().toString().trim());
        product.put("price", Double.parseDouble(priceInput.getText().toString()));
        product.put("stock", Integer.parseInt(stockInput.getText().toString()));
        product.put("barcode", barcodeInput.getText().toString().trim());
        product.put("imageUrl", imageUrlInput.getText().toString().trim());
        product.put("categoryName", categorySpinner.getSelectedItem().toString());

        db.collection("products").document(productId)
                .update(product)
                .addOnSuccessListener(v -> {
                    Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void deleteProduct() {
        db.collection("products").document(productId)
                .delete()
                .addOnSuccessListener(v -> {
                    Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}
