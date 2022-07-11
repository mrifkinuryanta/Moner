package com.mrndevs.moner.ui.recordform.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mrndevs.moner.databinding.ItemCategoryBottomSheetBinding

class CategoryAdapter(private val itemClick: (String) -> Unit) :
    ListAdapter<String, CategoryAdapter.CategoryViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBottomSheetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

    inner class CategoryViewHolder(
        private val binding: ItemCategoryBottomSheetBinding,
        val itemClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: String) {
            with(item) {
                itemView.setOnClickListener { itemClick(this) }
                binding.tvTitleCategory.text = this
            }
        }
    }

    companion object DiffCallback :
        DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }
    }
}