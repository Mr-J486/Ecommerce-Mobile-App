package com.example.e_commercemobapp.search.adapters

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
    private val onClick: (Category) -> Unit
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

        holder.text.text = cat.name

        // Convert Firebase name to clean lowercase text for matching
        val name = cat.name.lowercase().trim()

        // Assign icons based on category
        holder.icon.setImageResource(
            when (name) {

                "electronics" -> R.drawable.ic_cat_electronics
                "shoes" -> R.drawable.ic_cat_sports       // you chose sports icon for shoes
                "clothing" -> R.drawable.ic_cat_clothes
                "home appliances" -> R.drawable.ic_cat_home
                "accessories" -> R.drawable.ic_cat_accessories


                // DEFAULT ICON
                else -> R.drawable.ic_cat_all
            }
        )

        // Handle clicking on category
        holder.itemView.setOnClickListener {
            onClick(cat)
        }
    }
}
