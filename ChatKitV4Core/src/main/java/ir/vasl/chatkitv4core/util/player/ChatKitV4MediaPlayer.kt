package ir.vasl.chatkitv4core.util.player

import android.media.MediaPlayer
import android.media.MediaPlayer.*
import android.os.Handler
import android.os.Looper
import android.util.Log
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageConditionStatus
import ir.vasl.chatkitv4core.util.PublicValue
import ir.vasl.chatkitv4core.util.helper.FileHelper
import ir.vasl.chatkitv4core.util.player.interfaces.MediaHelperCallback
import java.io.IOException

class ChatKitV4MediaPlayer(
    private var mediaHelperCallback: MediaHelperCallback? = null
) : OnCompletionListener, OnPreparedListener, OnErrorListener, OnBufferingUpdateListener {

    private val TAG = "ChatKitV4MediaPlayer"
    private var currMediaPlayer: MediaPlayer? = null
    private var currMessageModel: MessageModel? = null
    private val updateSeekBarHandler: Handler = Handler()
    private val updateSeekBar: Runnable = object : Runnable {

        override fun run() {
            val totalDuration = currMediaPlayer?.duration ?: 1
            val currentDuration = currMediaPlayer?.currentPosition ?: 1

            if (currentDuration > 0) {
                val progress = currentDuration * 100 / totalDuration
                currMessageModel?.let {
                    it.progressPlayer = progress
                    mediaHelperCallback?.onMediaStatePlayerUpdated(
                        messageModel = it,
                        messageConditionStatus = MessageConditionStatus.PLAYER_IN_PROGRESS,
                        progress = progress
                    )
                }
            }

            // Call this thread again after 1000 milliseconds => ~ 1000/60fps
            updateSeekBarHandler.postDelayed(this, PublicValue.CHATKIR_V4_MEDIA_PLAYER_RUNNER_DELAY)
        }
    }

    fun terminatePlayer() {
        Log.i(TAG, "ChatKitV4MediaPlayer | terminatePlayer: ")

        stopMediaProgressTracker()
        stopSound()
    }

    fun getIsPlaying(): Boolean {
        return currMediaPlayer?.isPlaying ?: false
    }

    @Synchronized
    fun playSound(messageModel: MessageModel) {
        Log.i(TAG, "ChatKitV4MediaPlayer | playSound: ")

        // check if we have playing in progress, reset it before new player start
        currMessageModel?.let {
            if (messageModel != currMessageModel) {
                it.progressPlayer = PublicValue.CHATKIR_V4_MEDIA_PLAYER_START_VALUE // reset player
                mediaHelperCallback?.onMediaStatePlayerUpdated(
                    it,
                    messageConditionStatus = MessageConditionStatus.PLAYER_FINISHED
                )
            }
            stopSound()
        }

        // impl player reference
        if (currMediaPlayer == null) {
            currMediaPlayer = MediaPlayer()
        } else {
            stopSound()
        }

        // update message model
        this.currMessageModel = messageModel

        val localFile = currMessageModel?.localFileAddress
        if (localFile.isNullOrEmpty() || FileHelper.checkFileExists(localFile).not()) {
            // need to download file
            mediaHelperCallback?.onMediaStatePlayerUpdated(
                messageModel = currMessageModel ?: return,
                messageConditionStatus = MessageConditionStatus.DOWNLOAD_NEEDED
            )
            return
        }

        try {
            currMediaPlayer?.setDataSource(currMessageModel?.localFileAddress)
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
            mediaHelperCallback?.onMediaStatePlayerUpdated(
                messageModel = currMessageModel!!,
                messageConditionStatus = MessageConditionStatus.PLAYER_FAILED
            )
        }
    }

    @Synchronized
    fun stopSound() {
        Log.i(TAG, "ChatKitV4MediaPlayer | stopSound: ")

        // update ui
        currMessageModel?.let {
            it.progressPlayer = PublicValue.CHATKIR_V4_MEDIA_PLAYER_START_VALUE // reset player
            mediaHelperCallback?.onMediaStatePlayerUpdated(
                it,
                messageConditionStatus = MessageConditionStatus.PLAYER_FINISHED
            )
        }

        // stop player
        currMediaPlayer?.let {
            currMediaPlayer?.stop()
            currMediaPlayer?.release()
            currMediaPlayer = null
        }

        // clear message model
        currMessageModel = null
    }

    @Synchronized
    fun pauseSound() {
        Log.i(TAG, "ChatKitV4MediaPlayer | pauseSound: ")

        if (currMediaPlayer != null)
            currMediaPlayer?.pause()
    }

    @Synchronized
    fun restartSound() {
        Log.i(TAG, "ChatKitV4MediaPlayer | restartSound: ")

        if (currMediaPlayer != null) {
            currMediaPlayer?.start()
        }
    }

    @Synchronized
    fun getMediaPlayer(): MediaPlayer? {
        Log.i(TAG, "ChatKitV4MediaPlayer | getMediaPlayer: ")

        if (currMediaPlayer == null)
            currMediaPlayer = MediaPlayer()
        return currMediaPlayer
    }

    fun runMediaProgressTracker() {
        Log.i(TAG, "ChatKitV4MediaPlayer | runMediaProgressTracker: ")

        updateSeekBarHandler.postDelayed(updateSeekBar, 0);
    }

    fun stopMediaProgressTracker() {
        Log.i(TAG, "ChatKitV4MediaPlayer | stopMediaProgressTracker: ")

        // add delay on remove | this will fix lag issue!!! :|
        Handler(Looper.getMainLooper()).postDelayed({
            updateSeekBarHandler.removeCallbacks(updateSeekBar);
        }, PublicValue.CHATKIR_V4_MEDIA_PLAYER_REMOVER_DELAY)
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) {
        Log.i(TAG, "ChatKitV4MediaPlayer | onCompletion: ")
        currMessageModel?.let {
            it.progressPlayer = PublicValue.CHATKIR_V4_MEDIA_PLAYER_START_VALUE // reset player
            mediaHelperCallback?.onMediaStatePlayerUpdated(
                it,
                messageConditionStatus = MessageConditionStatus.PLAYER_FINISHED
            )
        }
        stopMediaProgressTracker()
    }

    override fun onError(mediaPlayer: MediaPlayer, i: Int, i1: Int): Boolean {
        Log.i(TAG, "ChatKitV4MediaPlayer | onError: ")
        currMessageModel?.let {
            mediaHelperCallback?.onMediaStatePlayerUpdated(
                it,
                messageConditionStatus = MessageConditionStatus.PLAYER_FAILED
            )
        }
        stopMediaProgressTracker()
        return false
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        Log.i(TAG, "ChatKitV4MediaPlayer | onPrepared: ")
    }

    override fun onBufferingUpdate(p0: MediaPlayer?, percent: Int) {
        Log.i(TAG, "ChatKitV4MediaPlayer | onBufferingUpdate: percent -> $percent")
    }
}