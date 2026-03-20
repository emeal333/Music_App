package edu.gvsu.cis.ticketmaster_clone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.gvsu.cis.ticketmaster_clone.navigation.Screen
import edu.gvsu.cis.ticketmaster_clone.screens.GuitarTunerScreen
import edu.gvsu.cis.ticketmaster_clone.screens.HomeScreen
import edu.gvsu.cis.ticketmaster_clone.screens.NoteDetectionScreen
import edu.gvsu.cis.ticketmaster_clone.ui.theme.TicketMaster_CloneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicketMaster_CloneTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToNoteDetection = {
                    navController.navigate(Screen.NoteDetection.route)
                },
                onNavigateToGuitarTuner = {
                    navController.navigate(Screen.GuitarTuner.route)
                }
            )
        }
        composable(Screen.NoteDetection.route) {
            NoteDetectionScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.GuitarTuner.route) {
            GuitarTunerScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
