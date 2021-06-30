package com.pinakin.currencyconverter.ui.converter

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pinakin.currencyconverter.R
import com.pinakin.currencyconverter.ui.CurrencyConverterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CurrencyConverterFragment : Fragment(R.layout.fragment_currency_converter) {

    private lateinit var tipSourceCurrencySelector: TextInputLayout
    private lateinit var tipSourceAmount: TextInputLayout
    private lateinit var edtSourceCurrencySelector: MaterialAutoCompleteTextView
    private lateinit var edtSourceAmount: TextInputEditText
    private lateinit var tipDestCurrencySelector: TextInputLayout
    private lateinit var tipDestAmount: TextInputLayout
    private lateinit var edtDestCurrencySelector: MaterialAutoCompleteTextView
    private lateinit var edtDestAmount: TextInputEditText
    private lateinit var recConversationRate: RecyclerView
    private lateinit var adapter: ExchangeRateAdapter
    private lateinit var btnConvert: MaterialButton
    private val viewModel: CurrencyConverterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tipSourceCurrencySelector = view.findViewById(R.id.tip_currency_selector)
        tipSourceAmount = view.findViewById(R.id.tip_amount)

        tipDestCurrencySelector = view.findViewById(R.id.tip_dest_currency_selector)
        tipDestAmount = view.findViewById(R.id.tip_dest_amount)

        edtSourceCurrencySelector = view.findViewById(R.id.edt_currency_selector)
        edtSourceAmount = view.findViewById(R.id.edt_amount)

        edtDestCurrencySelector = view.findViewById(R.id.edt_dest_currency_selector)
        edtDestAmount = view.findViewById(R.id.edt_dest_amount)

        recConversationRate = view.findViewById(R.id.rec_conversations)
        adapter = ExchangeRateAdapter()
        recConversationRate.layoutManager = LinearLayoutManager(requireContext())
        recConversationRate.adapter = adapter


        btnConvert = view.findViewById(R.id.btn_convert)

        btnConvert.setOnClickListener {
            convert()
        }

        collectCurrenciesFlow()

        collectExchangeRatesFlow()

        collectExchangedAmountFlow()
    }

    private fun collectCurrenciesFlow() {
        viewModel.currencies.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                setCurrenciesAdapters(it)
            }.launchIn(lifecycleScope)
    }

    private fun setCurrenciesAdapters(currencies: List<String>) {
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, currencies)

        (tipSourceCurrencySelector.editText as? MaterialAutoCompleteTextView)?.setAdapter(adapter)
        edtSourceCurrencySelector.setText("JPY", false)

        (tipDestCurrencySelector.editText as? MaterialAutoCompleteTextView)?.setAdapter(
            adapter
        )
        edtDestCurrencySelector.setText("INR", false)
    }

    private fun collectExchangeRatesFlow() {
        viewModel.exchangeRates.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                adapter.exchangeRates = it
                adapter.notifyDataSetChanged()
            }.launchIn(lifecycleScope)
    }

    private fun collectExchangedAmountFlow() {
        viewModel.exchangeAmount.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                edtDestAmount.setText(it)
            }.launchIn(lifecycleScope)
    }

    private fun convert() {
        val fromCurrency = edtSourceCurrencySelector.text.toString()
        val toCurrency = edtDestCurrencySelector.text.toString()
        val amount = edtSourceAmount.text.toString()

        when {
            amount.isEmpty() -> {
                edtSourceAmount.error = "Please enter valid amount"
                edtDestAmount.text?.clear()
                edtSourceAmount.requestFocus()
            }
            fromCurrency.isEmpty() -> {
                tipSourceCurrencySelector.error = "Select currency"
                edtDestAmount.text?.clear()
            }
            toCurrency.isEmpty() -> {
                tipDestCurrencySelector.error = "Select currency"
                edtDestAmount.text?.clear()
            }
            else -> {
                viewModel.convert(amount, fromCurrency, toCurrency)
            }
        }

    }
}