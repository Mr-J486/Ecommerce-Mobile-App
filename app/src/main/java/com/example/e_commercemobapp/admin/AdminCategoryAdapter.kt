package com.example.e_commercemobapp.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobapp.R
import com.example.e_commercemobapp.model.Category

class AdminCategoryAdapter(
    private val categories: MutableList<Category>,
    private val onClick: (Category) -> Unit
) : RecyclerView.Adapter<AdminCategoryAdapter.AdminCategoryViewHolder>() {

    inner class AdminCategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.categoryNameText)
        val desc: TextView = view.findViewById(R.id.categoryDescriptionText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminCategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_item_category, parent, false)
        return AdminCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminCategoryViewHolder, position: Int) {
        val cat = categories[position]

        holder.name.text = cat.name
        holder.desc.text = cat.description

        holder.itemView.setOnClickListener {
            onClick(cat)
        }
    }

    override fun getItemCount(): Int = categories.size
}
