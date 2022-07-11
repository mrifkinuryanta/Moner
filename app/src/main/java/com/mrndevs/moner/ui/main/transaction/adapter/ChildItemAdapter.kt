package com.mrndevs.moner.ui.main.transaction.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mrndevs.moner.R
import com.mrndevs.moner.data.room.model.RecordEntity
import com.mrndevs.moner.databinding.ItemRecordBinding
import com.mrndevs.moner.enum.EnumRecordType
import com.mrndevs.moner.utils.Utils

class ChildItemAdapter(private val itemClick: (RecordEntity) -> Unit) :
    ListAdapter<RecordEntity, ChildItemAdapter.ViewHolder>(ChildItemAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val movie = getItem(position)
        holder.bindView(movie)
    }

    inner class ViewHolder(
        private val binding: ItemRecordBinding,
        private val itemClick: (RecordEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: RecordEntity) {
            with(item) {
                itemView.setOnClickListener {
                    itemClick(this)
                }
                if (recordType == EnumRecordType.EXPENSE.type) {
                    binding.ivItemRecord.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.root.context,
                            R.drawable.ic_money_out_24
                        )
                    )
                    binding.tvItemTotal.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.red
                        )
                    )
                } else {
                    binding.ivItemRecord.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.root.context,
                            R.drawable.ic_money_in_24
                        )
                    )
                    binding.tvItemTotal.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.green
                        )
                    )
                }

                binding.tvItemTitle.text = category
                binding.tvItemDesc.text = note.orEmpty()
                    .ifEmpty { binding.root.context.getString(R.string.text_placeholder_note_empty) }
                binding.tvItemTotal.text = Utils.currencyFormatter(amount.toString())
            }
        }
    }

    companion object DiffCallback :
        DiffUtil.ItemCallback<RecordEntity>() {
        override fun areItemsTheSame(oldItem: RecordEntity, newItem: RecordEntity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: RecordEntity, newItem: RecordEntity): Boolean {
            return oldItem.id == newItem.id
        }
    }
}