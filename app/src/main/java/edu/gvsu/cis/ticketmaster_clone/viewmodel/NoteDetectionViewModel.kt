package edu.gvsu.cis.ticketmaster_clone.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.paramsen.noise.Noise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

data class NoteResult(
    val note: String,
    val frequency: Float,
    val isDetected: Boolean
)

@SuppressLint("MissingPermission")
class NoteDetectorViewModel(app: Application) : AndroidViewModel(app) {

    private val RATE_HZ = 44100
    private val SAMPLE_SIZE = 4096

    private val _noteResult = MutableStateFlow(NoteResult("?", 0f, false))
    val noteResult: StateFlow<NoteResult> = _noteResult

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording

    private var recordingJob: Job? = null

    val exoPlayer: ExoPlayer = ExoPlayer.Builder(app).build().apply {
        repeatMode = Player.REPEAT_MODE_ONE
    }

    private val _isMetronomeOn = MutableStateFlow(false)
    val isMetronomeOn: StateFlow<Boolean> = _isMetronomeOn

    private val _bpm = MutableStateFlow(80f)
    val bpm: StateFlow<Float> = _bpm

    private val noteNames = listOf("A","Bb","B","C","Db","D","Eb","E","F","Gb","G","Ab")


    fun startRecording() {
        if (_isRecording.value) return
        _isRecording.value = true
        recordingJob = viewModelScope.launch(Dispatchers.IO) {
            runRecordingLoop()
        }
    }

    fun stopRecording() {
        recordingJob?.cancel()
        recordingJob = null
        _isRecording.value = false
        _noteResult.value = NoteResult("?", 0f, false)
    }


    fun startMetronome(clickSoundUri: String) {
        val mediaItem = MediaItem.fromUri(clickSoundUri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
        _isMetronomeOn.value = true
    }

    fun stopMetronome() {
        exoPlayer.stop()
        _isMetronomeOn.value = false
    }

    fun setBpm(newBpm: Float) {
        _bpm.value = newBpm
        val speedFactor = newBpm / 80f
        exoPlayer.setPlaybackSpeed(speedFactor)
    }


    @SuppressLint("MissingPermission")
    private suspend fun runRecordingLoop() {
        val fft = Noise.real(SAMPLE_SIZE)

        val minBuf = AudioRecord.getMinBufferSize(
            RATE_HZ,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        val recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            RATE_HZ,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            minBuf
        )

        recorder.startRecording()

        val shortBuf = ShortArray(SAMPLE_SIZE)
        val floatBuf = FloatArray(SAMPLE_SIZE)
        var read = 0

        try {
            while (coroutineContext.isActive) {
                val numRead = recorder.read(shortBuf, read, SAMPLE_SIZE - read)
                if (numRead < 0) break
                read += numRead

                if (read >= SAMPLE_SIZE) {
                    for (i in shortBuf.indices) floatBuf[i] = shortBuf[i].toFloat()

                    val fftOut = fft.fft(floatBuf, FloatArray(SAMPLE_SIZE + 2))


                    var maxMag = 0.0
                    var maxIndex = 0
                    for (k in 1 until SAMPLE_SIZE / 2) {
                        val re = fftOut[2 * k]
                        val im = fftOut[2 * k + 1]
                        val mag = Math.sqrt((re * re + im * im).toDouble())
                        if (mag > maxMag) {
                            maxMag = mag
                            maxIndex = k
                        }
                    }


                    val normalizedMag = maxMag / ((1 shl 15) * Math.sqrt(2.0))
                    val frequency = if (normalizedMag > 10)
                        maxIndex * RATE_HZ.toFloat() / SAMPLE_SIZE
                    else 0f

                    if (frequency > 0f) {
                        _noteResult.value = NoteResult(
                            note = frequencyToNoteName(frequency),
                            frequency = frequency,
                            isDetected = true
                        )
                    } else {
                        _noteResult.value = NoteResult("...", 0f, false)
                    }

                    read = 0
                }
            }
        } finally {
            recorder.stop()
            recorder.release()
        }
    }


    private fun frequencyToNoteName(freq: Float): String {

        var semitones = Math.round(
            (12.0 * Math.log(freq.toDouble() / 440.0) / Math.log(2.0)).toFloat()
        )

        semitones = ((semitones % 12) + 12) % 12
        return noteNames[semitones]
    }


    override fun onCleared() {
        super.onCleared()
        stopRecording()
        exoPlayer.release()
    }
}
