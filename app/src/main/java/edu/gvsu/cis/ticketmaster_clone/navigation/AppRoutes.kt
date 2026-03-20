package edu.gvsu.cis.ticketmaster_clone.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object NoteDetection : Screen("note_detection")
    object GuitarTuner : Screen("guitar_tuner")
}
