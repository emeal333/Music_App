package edu.gvsu.cis.ticketmaster_clone.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.gvsu.cis.ticketmaster_clone.R
import edu.gvsu.cis.ticketmaster_clone.ui.theme.TicketMaster_CloneTheme

@Composable
fun HomeScreen(
    onNavigateToNoteDetection: () -> Unit,
    onNavigateToGuitarTuner: () -> Unit
) {
    Scaffold(
        topBar = {
            HomeTopBar()
        },
        bottomBar = {
            HomeBottomButtons(onNavigateToNoteDetection, onNavigateToGuitarTuner)
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.flower_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Welcome to Music Learning",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White.copy(alpha = 0.5f),
            titleContentColor = Color(0xFF1F1A02)
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
fun HomeBottomButtons(
    onNavigateToNoteDetection: () -> Unit,
    onNavigateToGuitarTuner: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onNavigateToNoteDetection,
            modifier = Modifier
                .weight(1f)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEEEAC2),
                contentColor = Color(0xFF1F1A02)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp, // Using shadow modifier for better visual control
                pressedElevation = 2.dp
            )
        ) {
            Text("Note Detection")
        }
        
        Button(
            onClick = onNavigateToGuitarTuner,
            modifier = Modifier
                .weight(1f)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEEEAC2),
                contentColor = Color(0xFF1F1A02)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 2.dp
            )
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
