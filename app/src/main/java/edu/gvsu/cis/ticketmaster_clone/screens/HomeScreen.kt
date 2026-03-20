package edu.gvsu.cis.ticketmaster_clone.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.gvsu.cis.ticketmaster_clone.ui.theme.TicketMaster_CloneTheme

@Composable
fun HomeScreen(
    onNavigateToNoteDetection: () -> Unit,
    onNavigateToGuitarTuner: () -> Unit
) {
    Scaffold(
        topBar = {
            HomeTopBar()
        }
    ) { innerPadding ->
        HomeContent(innerPadding, onNavigateToNoteDetection, onNavigateToGuitarTuner)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Black,
            titleContentColor = Color.White
        ),
        title = {
            Text(
                "Music Learning",
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}

@Composable
fun HomeContent(
    innerPadding: PaddingValues,
    onNavigateToNoteDetection: () -> Unit,
    onNavigateToGuitarTuner: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onNavigateToNoteDetection,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Note Detection")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onNavigateToGuitarTuner,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guitar Tuning")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TicketMaster_CloneTheme {
        HomeScreen(onNavigateToNoteDetection = {}, onNavigateToGuitarTuner = {})
    }
}
