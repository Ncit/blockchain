package com.acv.blockchain.stories.main

import androidx.recyclerview.widget.RecyclerView
import com.acv.blockchain.databinding.TransactionLayoutBinding
import com.acv.blockchain.network.models.TransactionModel

class TransactionHolder (private val binding: TransactionLayoutBinding,): RecyclerView.ViewHolder(binding.root) {

    fun bind(model: TransactionModel, position: Int) {
        binding.apply {
            positionValue.text = "$position"
            transactionId.text = model.x.hash
            transactionTime.text = "${model.x.time}"
        }
    }
}