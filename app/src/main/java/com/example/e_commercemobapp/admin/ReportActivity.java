package com.example.e_commercemobapp.admin;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commercemobapp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    private TextView reportSelectedDate;
    private RecyclerView reportRecyclerView;
    private ReportAdapter reportAdapter;
    private final ArrayList<Report> reportList = new ArrayList<>();

    private FirebaseFirestore db;

    // yyyy-MM-dd (used to match string date in Firestore)
    private String selectedDatePrefix;

    // 🔥 Cache to avoid repeated Firestore reads
    private final Map<String, String> userEmailCache = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        reportSelectedDate = findViewById(R.id.reportSelectedDate);
        reportRecyclerView = findViewById(R.id.reportRecyclerView);
        Button reportPickDateBtn = findViewById(R.id.reportPickDateBtn);

        reportRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportAdapter = new ReportAdapter(reportList);
        reportRecyclerView.setAdapter(reportAdapter);

        db = FirebaseFirestore.getInstance();

        reportPickDateBtn.setOnClickListener(v -> openDatePicker());
    }

    // -------------------------------
    // Date Picker
    // -------------------------------
    private void openDatePicker() {
        Calendar cal = Calendar.getInstance();

        new DatePickerDialog(
                this,
                (view, year, month, day) -> {

                    selectedDatePrefix =
                            year + "-" +
                                    String.format("%02d", month + 1) + "-" +
                                    String.format("%02d", day);

                    reportSelectedDate.setText(selectedDatePrefix);
                    loadOrders();

                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    // -------------------------------
    // Load Orders (ONE ROW = ONE ORDER)
    // -------------------------------
    private void loadOrders() {

        db.collection("orders")
                .get()
                .addOnSuccessListener(snapshot -> {

                    reportList.clear();

                    for (QueryDocumentSnapshot doc : snapshot) {

                        String dateStr = doc.getString("date");
                        if (dateStr == null || !dateStr.startsWith(selectedDatePrefix))
                            continue;

                        String userId = doc.getString("userId");
                        Double total = doc.getDouble("total");

                        List<Map<String, Object>> items =
                                (List<Map<String, Object>>) doc.get("items");

                        if (items == null || items.isEmpty())
                            continue;

                        // Build items summary
                        StringBuilder itemsSummary = new StringBuilder();

                        for (Map<String, Object> item : items) {
                            String name = (String) item.get("productName");
                            int qty = ((Long) item.get("quantity")).intValue();
                            double price = ((Number) item.get("price")).doubleValue();

                            itemsSummary.append("• ")
                                    .append(name)
                                    .append(" x")
                                    .append(qty)
                                    .append(" ($")
                                    .append(price)
                                    .append(")\n");
                        }

                        // Resolve user email safely
                        resolveUserEmail(userId, userEmail -> {

                            reportList.add(
                                    new Report(
                                            userEmail,
                                            itemsSummary.toString().trim(),
                                            total != null ? total : 0,
                                            dateStr.substring(0, 10)
                                    )
                            );

                            reportAdapter.notifyDataSetChanged();
                        });
                    }
                });
    }

    // -------------------------------
    // Resolve User Email (WITH CACHE)
    // -------------------------------
    private void resolveUserEmail(String userId, OnUserResolved callback) {

        if (userId == null) {
            callback.onResolved("Unknown User");
            return;
        }

        if (userEmailCache.containsKey(userId)) {
            callback.onResolved(userEmailCache.get(userId));
            return;
        }

        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    String email = doc.exists() && doc.getString("email") != null
                            ? doc.getString("email")
                            : "Unknown User";

                    userEmailCache.put(userId, email);
                    callback.onResolved(email);
                })
                .addOnFailureListener(e -> callback.onResolved("Unknown User"));
    }

    // -------------------------------
    // Callback Interface
    // -------------------------------
    private interface OnUserResolved {
        void onResolved(String value);
    }
}
