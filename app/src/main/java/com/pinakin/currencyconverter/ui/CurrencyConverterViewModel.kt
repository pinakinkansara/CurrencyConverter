package com.pinakin.currencyconverter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinakin.currencyconverter.repository.CurrencyConverterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val repository: CurrencyConverterRepository
) : ViewModel() {

    private val _exchangeAmount = MutableStateFlow<String>("")

    val exchangeAmount: StateFlow<String> = _exchangeAmount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    val currencies = repository.getCurrencies().shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    val exchangeRates = repository.getExchangeRates().shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    fun convert(amount: String, fromCurrency: String, toCurrency: String) = viewModelScope.launch {

        repository.convert(amount, fromCurrency, toCurrency).collect {
            _exchangeAmount.value = "%.2f".format(it)
        }
    }
}