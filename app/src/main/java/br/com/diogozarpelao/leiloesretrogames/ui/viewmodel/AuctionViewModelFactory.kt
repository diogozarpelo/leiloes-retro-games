package br.com.diogozarpelao.leiloesretrogames.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.diogozarpelao.leiloesretrogames.data.repository.AuctionRepository

class AuctionViewModelFactory(
    private val repository: AuctionRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuctionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuctionViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}