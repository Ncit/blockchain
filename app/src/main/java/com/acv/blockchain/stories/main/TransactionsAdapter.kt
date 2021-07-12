package com.acv.blockchain.stories.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acv.blockchain.databinding.TransactionLayoutBinding
import com.acv.blockchain.network.models.TransactionModel
import javax.inject.Inject

class TransactionsAdapter @Inject constructor() : RecyclerView.Adapter<TransactionHolder>() {

    private var transactions = mutableListOf<TransactionModel>()

    fun add(transactionModel: TransactionModel) {
        transactions.add(transactionModel)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: TransactionLayoutBinding = TransactionLayoutBinding.inflate(
            inflater, parent, false
        )
        return TransactionHolder(binding)
    }

    override fun getItemCount(): Int = transactions.size

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        holder.bind(transactions[position],position)
    }

    fun clear() {
        transactions = mutableListOf()
        notifyDataSetChanged()
    }
}