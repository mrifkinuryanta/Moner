package com.mrndevs.moner.ui.main.transaction.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mrndevs.moner.data.model.Parent
import com.mrndevs.moner.data.room.model.RecordEntity
import com.mrndevs.moner.databinding.ItemParentBinding
import com.mrndevs.moner.utils.Constant
import com.mrndevs.moner.utils.Utils

class TransactionAdapter(
    private val onClickItemChild: (RecordEntity) -> Unit
) :
    ListAdapter<Parent, TransactionAdapter.TransactionViewModel>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewModel {
        val binding = ItemParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewModel(binding, onClickItemChild)
    }

    override fun onBindViewHolder(holder: TransactionViewModel, position: Int) {
        holder.bindView(getItem(position))
    }

    inner class TransactionViewModel(
        private val binding: ItemParentBinding,
        private val onClickItemChild: (RecordEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: Parent) {
            with(item) {
                binding.parentItemTitle.text = Utils.dateFormatter(
                    parentItemTitle,
                    Constant.MONTH_PATTERN_DB,
                    Constant.MONTH_PATTERN
                )
                val layoutManager = LinearLayoutManager(
                    binding.root.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )

                val childItemAdapter = ChildItemAdapter {
                    onClickItemChild(it)
                }

                childItemAdapter.submitList(childItemList)
                binding.childRecyclerView.apply {
                    this.layoutManager = layoutManager
                    adapter = childItemAdapter
                    setRecycledViewPool(RecyclerView.RecycledViewPool())
                }
            }
        }
    }

    companion object DiffCallback :
        DiffUtil.ItemCallback<Parent>() {
        override fun areItemsTheSame(oldItem: Parent, newItem: Parent): Boolean {
            return oldItem.parentItemTitle == newItem.parentItemTitle
        }

        override fun areContentsTheSame(oldItem: Parent, newItem: Parent): Boolean {
            return oldItem == newItem
        }
    }
}