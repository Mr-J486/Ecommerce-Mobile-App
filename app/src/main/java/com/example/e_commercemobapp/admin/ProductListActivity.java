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

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdminProductAdapter adapter;
    private ArrayList<AdminProduct> productList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerView = findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdminProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        Button addProductBtn = findViewById(R.id.addProductBtn);
        addProductBtn.setOnClickListener(v ->
                startActivity(new Intent(ProductListActivity.this, AddProductActivity.class))
        );

        db = FirebaseFirestore.getInstance();
        loadProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    private void loadProducts() {
        db.collection("products")
                .get()
                .addOnSuccessListener(query -> {
                    productList.clear();

                    for (QueryDocumentSnapshot doc : query) {
                        String id = doc.getId();
                        String name = doc.getString("name");
                        String description = doc.getString("description");
                        Double price = doc.getDouble("price");

                        Long qtyLong = doc.getLong("stock");
                        int qty = qtyLong != null ? qtyLong.intValue() : 0;

                        String categoryName = doc.getString("categoryName");
                        String imageUrl = doc.getString("imageUrl");

                        productList.add(new AdminProduct(id, name, description,
                                price != null ? price : 0.0,
                                qty,
                                categoryName,
                                imageUrl));
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Log.e("Firestore", "Error loading products", e)
                );
    }
}
