package br.com.diogozarpelao.leiloesretrogames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import br.com.diogozarpelao.leiloesretrogames.ui.screens.ActiveAuctionsScreen
import br.com.diogozarpelao.leiloesretrogames.ui.theme.LeilõesRetroGamesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LeilõesRetroGamesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ActiveAuctionsScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}