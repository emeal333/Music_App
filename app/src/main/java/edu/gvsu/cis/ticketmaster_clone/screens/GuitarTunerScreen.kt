package edu.gvsu.cis.ticketmaster_clone.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import edu.gvsu.cis.ticketmaster_clone.R
import edu.gvsu.cis.ticketmaster_clone.ui.theme.TicketMaster_CloneTheme
import edu.gvsu.cis.ticketmaster_clone.viewmodel.NoteDetectorViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun GuitarTunerScreen(
    onNavigateBack: () -> Unit,
    vm: NoteDetectorViewModel = viewModel()
) {
    val micPermission = rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)
    val isRecording by vm.isRecording.collectAsState()
    val noteResult by vm.noteResult.collectAsState()

    var activeNote by remember { mutableStateOf<String?>(null) }

    val targetFrequencies = mapOf(
        "E" to 82.41f,
        "A" to 110.00f,
        "D" to 146.83f,
        "G" to 196.00f,
        "B" to 246.94f,
        "e" to 329.63f
    )

    Scaffold(
        topBar = {
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
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF1F1A02)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.flower_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Guitar Tuner",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(if (isRecording) Color(0xFFE3F2FD) else Color.White)
                        .clickable {
                            if (!micPermission.status.isGranted) {
                                micPermission.launchPermissionRequest()
                            } else if (isRecording) {
                                vm.stopRecording()
                                activeNote = null
                            } else {
                                vm.startRecording()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isRecording && noteResult.isDetected) {
                        Text(
                            text = noteResult.note,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1565C0)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.microphone3),
                            contentDescription = "Microphone",
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }

                val strings = listOf("E", "A", "D", "G", "B", "e")
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    strings.forEach { note ->
                        val isActive = activeNote == note && isRecording
                        val targetFreq = targetFrequencies[note] ?: 0f

                        val buttonText = if (isActive) {
                            when {
                                !noteResult.isDetected -> "Listening..."
                                noteResult.frequency < targetFreq - 2f -> "Higher"
                                noteResult.frequency > targetFreq + 2f -> "Lower"
                                else -> "Successfully tuned"
                            }
                        } else {
                            "Record for: $note"
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    if (!micPermission.status.isGranted) {
                                        micPermission.launchPermissionRequest()
                                    } else {
                                        if (activeNote == note) {
                                            vm.stopRecording()
                                            activeNote = null
                                        } else {
                                            vm.stopRecording()
                                            vm.startRecording()
                                            activeNote = note
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .weight(2f)
                                    .padding(start = 10.dp, end = 8.dp)
                                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(10.dp)),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isActive) Color(0xFF43A047) else Color(0xFFEEEAC2),
                                    contentColor = if (isActive) Color.White else Color(0xFF1F1A02)
                                )
                            ) {
                                Text(
                                    text = buttonText,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.tuning_peg),
                                    contentDescription = null,
                                )

                                Text(
                                    text = "$note",
                                    color = Color.Black,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TunerScreenPreview() {
    TicketMaster_CloneTheme {
        GuitarTunerScreen(onNavigateBack = {})
    }
}
