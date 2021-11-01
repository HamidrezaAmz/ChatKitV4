package ir.vasl.chatkitv4core.util.player

import android.media.MediaPlayer
import android.media.MediaPlayer.*
import android.os.Handler
import android.util.Log
import ir.vasl.chatkitv4core.model.chatkitv4enums.MediaHelperStatus
import ir.vasl.chatkitv4core.util.PublicValue
import ir.vasl.chatkitv4core.util.player.interfaces.MediaHelperCallback
import ir.vasl.chatkitv4core.util.player.model.MediaPlayerInfo
import java.io.IOException

class ChatKitV4MediaPlayer(
    private var mediaHelperCallback: MediaHelperCallback? = null
) : OnCompletionListener, OnPreparedListener, OnErrorListener, OnBufferingUpdateListener {

    private val TAG = "ChatKitV4MediaPlayer"
    private var currMediaPlayer: MediaPlayer? = null
    private val updateSeekBarHandler: Handler = Handler()
    private val updateSeekBar: Runnable = object : Runnable {

        override fun run() {
            val totalDuration = currMediaPlayer?.duration ?: 1
            val currentDuration = currMediaPlayer?.currentPosition ?: 1

            if (currentDuration > 0) {
                val progress = currentDuration * 100 / totalDuration
                mediaHelperCallback?.onMediaStateUpdated(
                    mediaHelperStatus = MediaHelperStatus.PLAYER_PROGRESS,
                    mediaPlayerInfo = MediaPlayerInfo(currProgress = progress)
                )
            }

            // Call this thread again after 1000 milliseconds => ~ 1000/60fps
            updateSeekBarHandler.postDelayed(this, PublicValue.CHATKIR_V4_MEDIA_PLAYER_RUNNER_DELAY)
        }
    }

    fun terminatePlayer() {
        stopSound()
        stopMediaProgressTracker()
    }

    fun getIsPlaying(): Boolean {
        return currMediaPlayer?.isPlaying ?: false
    }

    /**
     * @param localFileAddress if sound name is "sound.mp3" then pass fileName as "sound" only.
     */
    @Synchronized
    fun playSound(localFileAddress: String?) {
        if (currMediaPlayer == null) {
            currMediaPlayer = MediaPlayer()
        } else {
            stopSound()
        }
        try {
            currMediaPlayer?.setDataSource(localFileAddress)
            currMediaPlayer?.prepare()
            currMediaPlayer?.setVolume(100f, 100f)
            currMediaPlayer?.isLooping = false
            currMediaPlayer?.start()
            currMediaPlayer?.setOnCompletionListener(this)
            currMediaPlayer?.setOnErrorListener(this)
            currMediaPlayer?.setOnPreparedListener(this)
            currMediaPlayer?.setOnBufferingUpdateListener(this)

            runMediaProgressTracker()

        } catch (e: IOException) {
            e.printStackTrace()
            mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.PLAYER_ERROR)
        }
    }

    @Synchronized
    fun stopSound() {
        mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.PLAYER_STOPPED)
        if (currMediaPlayer != null) {
            currMediaPlayer?.reset()
        }
    }

    @Synchronized
    fun pauseSound() {
        if (currMediaPlayer != null) {
            currMediaPlayer?.pause()
        }
    }

    @Synchronized
    fun restartSound() {
        if (currMediaPlayer != null) {
            currMediaPlayer?.start()
        }
    }

    @Synchronized
    fun getMediaPlayer(): MediaPlayer? {
        if (currMediaPlayer == null) {
            currMediaPlayer = MediaPlayer()
        }
        return currMediaPlayer
    }

    fun runMediaProgressTracker() {
        updateSeekBarHandler.postDelayed(updateSeekBar, 0);
    }

    fun stopMediaProgressTracker() {
        updateSeekBarHandler.removeCallbacks(updateSeekBar);
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) {
        mediaHelperCallback?.onMediaStateUpdated(
            mediaHelperStatus = MediaHelperStatus.PLAYER_PROGRESS,
            mediaPlayerInfo = MediaPlayerInfo(currProgress = 100)
        )
        mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.PLAYER_COMPLETED)
        terminatePlayer()
    }

    override fun onError(mediaPlayer: MediaPlayer, i: Int, i1: Int): Boolean {
        mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.PLAYER_ERROR)
        terminatePlayer()
        return false
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.PLAYER_PREPARED)
        mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.PLAYER_PLAYING)
    }

    override fun onBufferingUpdate(p0: MediaPlayer?, percent: Int) {
        Log.i(TAG, "onBufferingUpdate: percent -> $percent")
    }
}