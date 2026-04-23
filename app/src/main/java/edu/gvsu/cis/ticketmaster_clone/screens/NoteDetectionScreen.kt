package edu.gvsu.cis.ticketmaster_clone.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import kotlin.math.roundToInt


fun noteDrawable(note: String): Int? = when (note) {
    "A"  -> R.drawable.a
    "E"  -> R.drawable.guitar_chords
    else -> null
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun NoteDetectionScreen(
    onNavigateBack: () -> Unit,
    vm: NoteDetectorViewModel = viewModel()
) {
    val context = LocalContext.current
    val micPermission = rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)

    val isRecording by vm.isRecording.collectAsState()
    val noteResult by vm.noteResult.collectAsState()
    val isMetronomeOn by vm.isMetronomeOn.collectAsState()
    val bpm by vm.bpm.collectAsState()

    val clickUri = "android.resource://${context.packageName}/raw/metronome"

    val noteLog = remember { mutableStateListOf<String>() }

    LaunchedEffect(noteResult) {
        if (noteResult.isDetected) {
            if (noteLog.isEmpty() || noteLog.first() != noteResult.note) {
                noteLog.add(0, noteResult.note)
                if (noteLog.size > 20) noteLog.removeAt(noteLog.lastIndex)
            }
        }
    }

    val recordBtnColor by animateColorAsState(
        targetValue = if (isRecording) Color(0xFFE53935) else Color.White,
        animationSpec = tween(300),
        label = "recordColor"
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
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
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
                        text = "Note Detection",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(if (noteResult.isDetected) Color(0xFFE3F2FD) else Color.White)
                        .clickable {
                            if (!micPermission.status.isGranted) {
                                micPermission.launchPermissionRequest()
                            } else if (isRecording) {
                                vm.stopRecording()
                            } else {
                                vm.startRecording()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (!isRecording && !noteResult.isDetected) {
                            Image(
                                painter = painterResource(id = R.drawable.microphone3),
                                contentDescription = "Microphone",
                                modifier = Modifier.size(80.dp)
                            )
                        } else {
                            Text(
                                text = noteResult.note,
                                fontSize = if (noteResult.note.length <= 2) 72.sp else 40.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (noteResult.isDetected) Color(0xFF1565C0) else Color.Gray
                            )
                            if (noteResult.isDetected) {
                                Text(
                                    text = "${"%.1f".format(noteResult.frequency)} Hz",
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (!micPermission.status.isGranted) {
                            micPermission.launchPermissionRequest()
                        } else if (isRecording) {
                            vm.stopRecording()
                        } else {
                            vm.startRecording()
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = recordBtnColor,
                        contentColor = if (isRecording) Color.White else Color(0xFF1F1A02)
                    )
                ) {
                    Text(if (isRecording) "Stop Recording" else "Start Recording")
                }


                Text(
                    text = when {
                        !micPermission.status.isGranted -> "Microphone permission needed"
                        isRecording && noteResult.isDetected -> "Note detected!"
                        isRecording -> "Listening... sing or play a note"
                        else -> "Press microphone to start"
                    },
                    fontSize = 15.sp,
                    color = Color.White,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.White, 
                            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                        )
                        .padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Metronome", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("${bpm.roundToInt()} BPM", fontSize = 22.sp, fontWeight = FontWeight.Bold)

                        Slider(
                            value = bpm,
                            onValueChange = { vm.setBpm(it) },
                            valueRange = 40f..200f,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                if (isMetronomeOn) vm.stopMetronome()
                                else vm.startMetronome(clickUri)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isMetronomeOn) Color(0xFFE53935) else Color(0xFF43A047)
                            )
                        ) {
                            Text(text = if (isMetronomeOn) "⏹ Stop Metronome" else "▶ Start Metronome")
                        }

                        //if (!isMetronomeOn) {
                            //Text(
                                //text = "Requires res/raw/metronome.mp3",
                               // fontSize = 11.sp,
                                //color = Color.Gray
                            //)
                        //}

                        HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Detected Notes",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            if (noteLog.isNotEmpty()) {
                                TextButton(onClick = { noteLog.clear() }) {
                                    Text("Clear", color = Color.Gray, fontSize = 13.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (noteLog.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No notes detected yet.\nStart recording and play or sing a note!",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                noteLog.forEachIndexed { index, note ->
                                    DetectedNoteRow(
                                        note = note,
                                        isNewest = index == 0
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DetectedNoteRow(note: String, isNewest: Boolean) {
    val drawableRes = noteDrawable(note)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isNewest) Color(0xFFE3F2FD) else Color(0xFFF5F5F5),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (drawableRes != null) {
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = "Chord $note"
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color(0xFF90A4AE), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = note,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (note.length == 1) 22.sp else 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = "Note detected: $note",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = if (isNewest) FontWeight.Bold else FontWeight.Normal,
                color = if (isNewest) Color(0xFF1565C0) else Color.Black
            )
            if (isNewest) {
                Text(
                    text = "most recent",
                    fontSize = 12.sp,
                    color = Color(0xFF1565C0)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteScreenPreview() {
    TicketMaster_CloneTheme {
        NoteDetectionScreen(onNavigateBack = {})
    }
}
