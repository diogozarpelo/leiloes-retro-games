package br.com.diogozarpelao.leiloesretrogames.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.diogozarpelao.leiloesretrogames.data.repository.AuctionRepository
import br.com.diogozarpelao.leiloesretrogames.model.Auction
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuctionViewModel(
    private val repository: AuctionRepository
) : ViewModel() {

    val auctions: StateFlow<List<Auction>> = repository.auctions.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun insert(
        auction: Auction,
        onInserted: (Long) -> Unit = {}
    ) {
        viewModelScope.launch {
            val id = repository.insert(auction)
            onInserted(id)
        }
    }

    fun update(auction: Auction) {
        viewModelScope.launch {
            repository.update(auction)
        }
    }

    fun delete(auction: Auction) {
        viewModelScope.launch {
            repository.delete(auction)
        }
    }
}