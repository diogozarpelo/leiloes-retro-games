package br.com.diogozarpelao.leiloesretrogames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.diogozarpelao.leiloesretrogames.ui.screens.ActiveAuctionsScreen
import br.com.diogozarpelao.leiloesretrogames.ui.screens.AddAuctionScreen
import br.com.diogozarpelao.leiloesretrogames.ui.screens.AuctionDetailsScreen
import br.com.diogozarpelao.leiloesretrogames.ui.theme.LeilõesRetroGamesTheme
import br.com.diogozarpelao.leiloesretrogames.ui.viewmodel.AuctionViewModel
import br.com.diogozarpelao.leiloesretrogames.ui.viewmodel.AuctionViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val auctionViewModel: AuctionViewModel = viewModel(
                factory = AuctionViewModelFactory(
                    (application as AuctionApplication).repository
                )
            )

            val auctions by auctionViewModel.auctions.collectAsState()

            var showAddAuctionScreen by rememberSaveable {
                mutableStateOf(false)
            }

            var selectedAuctionId by rememberSaveable {
                mutableStateOf<Long?>(null)
            }

            val selectedAuction = auctions.firstOrNull { auction ->
                auction.id == selectedAuctionId
            }

            LeilõesRetroGamesTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    when {
                        selectedAuction != null -> {
                            AuctionDetailsScreen(
                                auction = selectedAuction,
                                onBack = {
                                    selectedAuctionId = null
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        showAddAuctionScreen -> {
                            AddAuctionScreen(
                                onSave = { auction ->
                                    auctionViewModel.insert(auction)
                                    showAddAuctionScreen = false
                                },
                                onCancel = {
                                    showAddAuctionScreen = false
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        else -> {
                            ActiveAuctionsScreen(
                                auctions = auctions,
                                onAuctionClick = { auction ->
                                    selectedAuctionId = auction.id
                                },
                                onAddAuction = {
                                    showAddAuctionScreen = true
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}