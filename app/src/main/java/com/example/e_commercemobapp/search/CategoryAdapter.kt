package com.example.e_commercemobapp.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobapp.R
import com.example.e_commercemobapp.model.Category

class CategoryAdapter(
    private val categories: List<Category>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.categoryIcon)
        val text: TextView = view.findViewById(R.id.categoryText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val cat = categories[position]
        holder.icon.setImageResource(cat.iconRes)
        holder.text.text = cat.name

        holder.itemView.setOnClickListener {
            onClick(cat.name)
        }
    }
}