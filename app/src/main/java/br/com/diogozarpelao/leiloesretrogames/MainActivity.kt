package br.com.diogozarpelao.leiloesretrogames

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.diogozarpelao.leiloesretrogames.model.AuctionStatus
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

        val notificationScheduler =
            AuctionNotificationScheduler(this)

        setContent {
            val auctionViewModel: AuctionViewModel = viewModel(
                factory = AuctionViewModelFactory(
                    (application as AuctionApplication).repository
                )
            )

            val notificationPermissionLauncher =
                rememberLauncherForActivityResult(
                    contract =
                        ActivityResultContracts.RequestPermission()
                ) {}

            LaunchedEffect(Unit) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermissionLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                }
            }

            val auctions by auctionViewModel.auctions.collectAsState()

            var showAddAuctionScreen by rememberSaveable {
                mutableStateOf(false)
            }

            var selectedAuctionId by rememberSaveable {
                mutableStateOf<Long?>(null)
            }

            var editingAuctionId by rememberSaveable {
                mutableStateOf<Long?>(null)
            }

            val selectedAuction = auctions.firstOrNull { auction ->
                auction.id == selectedAuctionId
            }

            val auctionBeingEdited = auctions.firstOrNull { auction ->
                auction.id == editingAuctionId
            }

            LeilõesRetroGamesTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    when {
                        auctionBeingEdited != null -> {
                            AddAuctionScreen(
                                auctionToEdit = auctionBeingEdited,
                                onSave = { updatedAuction ->
                                    auctionViewModel.update(updatedAuction)

                                    notificationScheduler.cancel(
                                        updatedAuction.id
                                    )

                                    notificationScheduler.schedule(
                                        updatedAuction
                                    )

                                    editingAuctionId = null
                                    selectedAuctionId = updatedAuction.id
                                },
                                onCancel = {
                                    editingAuctionId = null
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        selectedAuction != null -> {
                            AuctionDetailsScreen(
                                auction = selectedAuction,
                                onBack = {
                                    selectedAuctionId = null
                                },
                                onEdit = {
                                    editingAuctionId = selectedAuction.id
                                },
                                onStatusChange = { newStatus ->
                                    auctionViewModel.update(
                                        selectedAuction.copy(
                                            status = newStatus,
                                            finalPriceInCents =
                                                if (
                                                    newStatus ==
                                                    AuctionStatus.NOT_WON
                                                ) {
                                                    null
                                                } else {
                                                    selectedAuction
                                                        .finalPriceInCents
                                                }
                                        )
                                    )
                                },
                                onFinalPriceChange = { finalPrice ->
                                    auctionViewModel.update(
                                        selectedAuction.copy(
                                            finalPriceInCents = finalPrice,
                                            status = if (
                                                selectedAuction.status ==
                                                AuctionStatus.WON_PAID
                                            ) {
                                                AuctionStatus.WON_PAID
                                            } else {
                                                AuctionStatus
                                                    .WON_PENDING_PAYMENT
                                            }
                                        )
                                    )
                                },
                                onDelete = {
                                    notificationScheduler.cancel(
                                        selectedAuction.id
                                    )

                                    auctionViewModel.delete(
                                        selectedAuction
                                    )

                                    selectedAuctionId = null
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        showAddAuctionScreen -> {
                            AddAuctionScreen(
                                onSave = { auction ->
                                    auctionViewModel.insert(
                                        auction
                                    ) { generatedId ->
                                        notificationScheduler.schedule(
                                            auction.copy(
                                                id = generatedId
                                            )
                                        )
                                    }

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