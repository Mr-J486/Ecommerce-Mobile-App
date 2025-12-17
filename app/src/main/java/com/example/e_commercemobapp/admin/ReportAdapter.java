package com.example.e_commercemobapp.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commercemobapp.R;

import java.util.List;

public class ReportAdapter
        extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private final List<Report> reportList;

    public ReportAdapter(List<Report> reportList) {
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ReportViewHolder holder, int position) {

        Report report = reportList.get(position);

        holder.user.setText("User: " + report.getUserEmail());
        holder.items.setText(report.getItemsSummary());
        holder.total.setText("Total: $" + report.getTotal());
        holder.date.setText(report.getDate());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {

        TextView user, items, total, date;

        ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.reportUser);
            items = itemView.findViewById(R.id.reportItems);
            total = itemView.findViewById(R.id.reportTotal);
            date = itemView.findViewById(R.id.reportDate);
        }
    }
}
