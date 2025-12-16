//package com.example.e_commercemobapp.search
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.e_commercemobapp.R
//import com.example.e_commercemobapp.model.Category
//
//class CategoryAdapter(
//    private val categories: List<Category>,
//    private val onClick: (String) -> Unit
//) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
//
//    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val icon: ImageView = view.findViewById(R.id.categoryIcon)
//        val text: TextView = view.findViewById(R.id.categoryText)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_category, parent, false)
//        return CategoryViewHolder(view)
//    }
//
//    override fun getItemCount(): Int = categories.size
//
//    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
//        val cat = categories[position]
//        holder.icon.setImageResource(cat.iconRes)
//        holder.text.text = cat.name
//
//        holder.itemView.setOnClickListener {
//            onClick(cat.name)
//        }
//    }
//}


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

        holder.icon.setImageResource(
            when (cat.name.lowercase()) {
                "electronics" -> R.drawable.ic_cat_electronics
                "sports" -> R.drawable.ic_cat_sports
                "clothes" -> R.drawable.ic_cat_clothes
                "food" -> R.drawable.ic_cat_food
                else -> R.drawable.ic_cat_all
            }
        )

        holder.itemView.setOnClickListener {
            onClick(cat)
        }
    }
}
