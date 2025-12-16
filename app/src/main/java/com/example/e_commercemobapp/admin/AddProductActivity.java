package com.example.e_commercemobapp.admin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commercemobapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 100;

    private EditText productNameInput, productDescriptionInput, productPriceInput, productStockInput;
    private Button saveProductBtn, selectImageBtn;
    private ImageView productImagePreview;

    private Uri imageUri = null;
    private FirebaseFirestore db;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);

        FirebaseAuth.getInstance().signInAnonymously();

        productNameInput = findViewById(R.id.productNameInput);
        productDescriptionInput = findViewById(R.id.productDescriptionInput);
        productPriceInput = findViewById(R.id.productPriceInput);
        productStockInput = findViewById(R.id.productStockInput);
        saveProductBtn = findViewById(R.id.saveProductBtn);
        selectImageBtn = findViewById(R.id.selectImageBtn);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference().child("product_images");

        selectImageBtn.setOnClickListener(v -> openGallery());

        saveProductBtn.setOnClickListener(v -> saveProduct());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            productImagePreview.setImageURI(imageUri);
        }
    }

    private void saveProduct() {
        String name = productNameInput.getText().toString().trim();
        String description = productDescriptionInput.getText().toString().trim();
        String priceStr = productPriceInput.getText().toString().trim();
        String stockStr = productStockInput.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        int stock = Integer.parseInt(stockStr);

        if (imageUri == null) {
            // No image → save directly with null URL
            saveProductToFirestore(name, description, price, stock, null);
        } else {
            uploadImageAndSave(name, description, price, stock);
        }
    }

    private void uploadImageAndSave(String name, String description, double price, int stock) {
        try {
            InputStream stream = getContentResolver().openInputStream(imageUri);

            String fileName = "prod_" + System.currentTimeMillis() + ".jpg";
            StorageReference fileRef = storageRef.child(fileName);

            UploadTask uploadTask = fileRef.putStream(stream);

            uploadTask.addOnSuccessListener(taskSnapshot ->
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        saveProductToFirestore(name, description, price, stock, uri.toString());
                    })
            ).addOnFailureListener(e -> {
                Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });

        } catch (Exception e) {
            Toast.makeText(this, "Failed to read image", Toast.LENGTH_LONG).show();
        }
    }

    private void saveProductToFirestore(String name, String description, double price, int stock, String imageUrl) {
        Map<String, Object> product = new HashMap<>();

        product.put("name", name);
        product.put("description", description);
        product.put("price", price);
        product.put("stock", stock);

        if (imageUrl != null) {
            product.put("imageUrl", imageUrl);
        }

        db.collection("products")
                .add(product)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(this, "Product added!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Firestore error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}
