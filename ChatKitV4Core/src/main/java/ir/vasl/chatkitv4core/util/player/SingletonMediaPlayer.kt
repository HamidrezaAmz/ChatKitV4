package ir.vasl.chatkitv4core.util.player

import android.media.MediaPlayer
import android.media.MediaPlayer.*
import android.util.Log
import ir.vasl.chatkitv4core.model.chatkitv4enums.MediaHelperStatus
import ir.vasl.chatkitv4core.util.player.interfaces.MediaHelperCallback
import ir.vasl.chatkitv4core.util.player.model.MediaPlayerInfo
import java.io.IOException

class SingletonMediaPlayer : OnCompletionListener, OnPreparedListener, OnErrorListener {

    private var currMediaPlayer: MediaPlayer? = null
    private var currMediaHelperCallback: MediaHelperCallback? = null
    private var currMediaPlayerInfo: MediaPlayerInfo? = null

    companion object {

        private const val TAG = "SingletonMediaPlayer"

        @Volatile
        private var instance: SingletonMediaPlayer? = null

        fun getInstance(): SingletonMediaPlayer? {
            if (instance == null) {
                synchronized(SingletonMediaPlayer::class.java) {
                    if (instance == null) {
                        instance = SingletonMediaPlayer()
                    }
                }
            }
            return instance
        }
    }

    fun setMediaPlayerCallback(mediaHelperCallback: MediaHelperCallback?) {
        this.currMediaHelperCallback = mediaHelperCallback
    }

    fun setMediaPlayerInfo(mediaPlayerInfo: MediaPlayerInfo) {
        this.currMediaPlayerInfo = mediaPlayerInfo
    }

    fun getMediaPlayerInfo(): MediaPlayerInfo? {
        return currMediaPlayerInfo
    }

    fun refreshMediaPlayerInfo() {
        currMediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.PLAYER_STOPPED)
        this.currMediaPlayerInfo = null
        if (getIsPlaying())
            stopSound()
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
            currMediaPlayer?.setOnBufferingUpdateListener { mediaPlayer, i ->
                Log.i(
                    TAG,
                    "onBufferingUpdate: $i"
                )
            }
            currMediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.PLAYER_PLAYING)
        } catch (e: IOException) {
            e.printStackTrace()
            currMediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.ERROR)
        }
    }

    @Synchronized
    fun stopSound() {
        if (currMediaPlayer != null /*&& currMediaPlayer?.isPlaying == true*/) {
            // currMediaPlayer?.stop()
            // currMediaPlayer?.release()
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
    fun playRepeatedSound(localFileAddress: String?) {

        if (currMediaPlayer == null) {
            currMediaPlayer = MediaPlayer()
        } else {
            currMediaPlayer?.reset()
        }

        try {
            currMediaPlayer?.setDataSource(localFileAddress)
            currMediaPlayer?.prepare()
            currMediaPlayer?.setVolume(100f, 100f)
            currMediaPlayer?.isLooping = true
            currMediaPlayer?.start()
            currMediaPlayer?.setOnCompletionListener { mediaPlayer ->
                mediaPlayer?.reset()
                mediaPlayer?.release()
            }
            currMediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.PLAYER_PLAYING)
        } catch (e: IOException) {
            e.printStackTrace()
            currMediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.ERROR)
        }
    }

    @Synchronized
    fun getMediaPlayer(): MediaPlayer? {
        if (currMediaPlayer == null) {
            currMediaPlayer = MediaPlayer()
        }
        return currMediaPlayer
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) {
        currMediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.PLAYER_COMPLETED)
        Log.i(TAG, "onCompletion -> COMPLETED")
        refreshMediaPlayerInfo()
    }

    override fun onError(mediaPlayer: MediaPlayer, i: Int, i1: Int): Boolean {
        currMediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.ERROR)
        Log.i(TAG, "onCompletion -> ERROR")
        refreshMediaPlayerInfo()
        return false
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        currMediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.PLAYER_PREPARED)
        Log.i(TAG, "onCompletion -> PREPARED | Player started dg")
    }
}