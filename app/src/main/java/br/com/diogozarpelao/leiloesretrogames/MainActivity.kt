package br.com.diogozarpelao.leiloesretrogames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import br.com.diogozarpelao.leiloesretrogames.ui.theme.LeilõesRetroGamesTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val notificationScheduler =
            AuctionNotificationScheduler(this)

        setContent {
            LeilõesRetroGamesTheme {
                LeiloesRetroGamesApp(
                    application =
                        application as AuctionApplication,
                    notificationScheduler =
                        notificationScheduler
                )
            }
        }
    }
}