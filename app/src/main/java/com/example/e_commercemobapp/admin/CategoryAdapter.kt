package com.example.e_commercemobapp.admin

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commercemobapp.admin.CategoryAdapter.CategoryViewHolder
import com.example.e_commercemobapp.R

class CategoryAdapter(private val categoryList: MutableList<Category>) :
    RecyclerView.Adapter<CategoryViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.admin_item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList.get(position)

        holder.nameText.setText(category.name)
        holder.descriptionText.setText(category.description)

        holder.itemView.setOnClickListener(View.OnClickListener { v: View? ->
            val intent: Intent = Intent(v!!.getContext(), EditCategoryActivity::class.java)
            intent.putExtra("id", category.id)
            intent.putExtra("name", category.name)
            intent.putExtra("description", category.description)
            v.getContext().startActivity(intent)
        })
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

     class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameText: TextView
        var descriptionText: TextView

        init {
            nameText = itemView.findViewById<TextView?>(R.id.categoryNameText)
            descriptionText = itemView.findViewById<TextView?>(R.id.categoryDescriptionText)
        }
    }
}