package com.pinakin.currencyconverter.ui.converter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.pinakin.currencyconverter.R
import com.pinakin.currencyconverter.models.ExchangeRate

class ExchangeRateAdapter(
    var exchangeRates: List<ExchangeRate> = emptyList()
) : RecyclerView.Adapter<ExchangeRateAdapter.ViewHolder>() {

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val textRate: MaterialTextView = item.findViewById(R.id.txt_exchange_rate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_exchange_rate, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exchangeRate = exchangeRates[position]

        val rate = "${exchangeRate.code}: ${exchangeRate.rate}"
        holder.textRate.text = rate
    }

    override fun getItemCount(): Int {
        return exchangeRates.size
    }
}