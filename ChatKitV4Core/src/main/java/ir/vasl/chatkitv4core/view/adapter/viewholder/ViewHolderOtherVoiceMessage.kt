package ir.vasl.chatkitv4core.view.adapter.viewholder

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.huxq17.download.core.DownloadInfo
import ir.vasl.chatkitv4core.R
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.model.chatkitv4enums.MediaHelperStatus
import ir.vasl.chatkitv4core.util.helper.ChatKitV4MediaHelperV2
import ir.vasl.chatkitv4core.util.helper.MediaMetadataRetrieverHelper
import ir.vasl.chatkitv4core.util.player.interfaces.MediaHelperCallback
import ir.vasl.chatkitv4core.util.player.model.MediaPlayerInfo
import ir.vasl.chatkitv4core.view.interfaces.ChatKitV4ListCallback

class ViewHolderOtherVoiceMessage(
    private var view: View,
    private var chatKitV4ListCallback: ChatKitV4ListCallback? = null
) : RecyclerView.ViewHolder(view), MediaHelperCallback {

    private val TAG = "ViewHolderSelfVoiceMsg"
    private var messageModel: MessageModel? = null

    private var currMediaHelperStatus: MediaHelperStatus = MediaHelperStatus.IDLE

    var textviewTitle = view.findViewById(R.id.tv_title) as AppCompatTextView?
    var textviewTimer = view.findViewById(R.id.tv_timer) as AppCompatTextView?
    var textviewDate = view.findViewById(R.id.tv_date) as AppCompatTextView?
    var imageButtonPlayPause = view.findViewById(R.id.ib_play_pause) as AppCompatImageButton?
    var progressBarDownload = view.findViewById(R.id.pb_download) as ProgressBar?
    var seekbarVoice = view.findViewById(R.id.sb_voice) as SeekBar?

    init {
        seekbarVoice?.isEnabled = false
    }

    fun bind(messageModel: MessageModel?) {
        this.messageModel = messageModel

        val title = messageModel?.title
        val date = messageModel?.date

        // set title
        if (title.isNullOrEmpty())
            textviewTitle?.visibility = View.GONE
        else {
            textviewTitle?.visibility = View.VISIBLE
            textviewTitle?.text = messageModel.title
        }

        // set date
        if (date.isNullOrEmpty())
            textviewDate?.visibility = View.GONE
        else {
            textviewDate?.visibility = View.VISIBLE
            textviewDate?.text = messageModel.date
        }

        // set click listener
        imageButtonPlayPause?.setOnClickListener {
            ChatKitV4MediaHelperV2.submit(messageModel, this)
        }
        itemView.setOnLongClickListener {
            chatKitV4ListCallback?.onMessagePressed(messageModel)
            false
        }

        bindMediaHelper()
    }

    override fun onMediaStateUpdated(
        mediaHelperStatus: MediaHelperStatus,
        downloadInfo: DownloadInfo?,
        mediaPlayerInfo: MediaPlayerInfo?
    ) {
        this.currMediaHelperStatus = mediaHelperStatus
        when (mediaHelperStatus) {
            MediaHelperStatus.IDLE -> {
                // here we are just idle
                showPlayIcon()
                showProgress(false)
                resetSeekbar()
                Log.i(TAG, "onMediaStateUpdated: IDLE")
            }
            MediaHelperStatus.ERROR -> {
                // here we are just idle
                showPlayIcon()
                showProgress(false)
                Log.i(TAG, "onMediaStateUpdated: ERROR")
            }
            MediaHelperStatus.TERMINATE -> {
                // we have terminated all actions and progresses
                showPlayIcon()
                showProgress(false)
                resetSeekbar()
                Log.i(TAG, "onMediaStateUpdated: TERMINATE")
            }

            MediaHelperStatus.DOWNLOAD_PREPARING -> {
                showPlayIcon(false)
                showProgress()
                Log.i(TAG, "onMediaStateUpdated: DOWNLOAD_PREPARING")
            }
            MediaHelperStatus.DOWNLOAD_STARTED -> {
                showPlayIcon(false)
                showProgress()
                Log.i(TAG, "onMediaStateUpdated: DOWNLOAD_STARTED")
            }
            MediaHelperStatus.DOWNLOAD_STOPPED -> {
                showPlayIcon()
                showProgress(false)
                Log.i(TAG, "onMediaStateUpdated: DOWNLOAD_STOPPED")
            }
            MediaHelperStatus.DOWNLOAD_IN_PROGRESS -> {
                showPlayIcon(false)
                showProgress()
                Log.i(TAG, "onMediaStateUpdated: DOWNLOAD_IN_PROGRESS")
            }
            MediaHelperStatus.DOWNLOAD_SUCCESS -> {
                showPlayIcon(false)
                showProgress(false)
                Log.i(TAG, "onMediaStateUpdated: DOWNLOAD_SUCCESS")
            }

            MediaHelperStatus.PLAYER_STOPPED -> {
                // stop player and sync view with this status
                showPlayIcon()
                showProgress(false)
                Log.i(TAG, "onMediaStateUpdated: PLAYER_STOPPED")
            }
            MediaHelperStatus.PLAYER_PREPARING -> {
                showPlayIcon(false)
                showProgress(false)
                Log.i(TAG, "onMediaStateUpdated: PLAYER_PREPARING")
            }
            MediaHelperStatus.PLAYER_PREPARED -> {
                showPlayIcon(false)
                showProgress(false)
                Log.i(TAG, "onMediaStateUpdated: PLAYER_PREPARED")
            }
            MediaHelperStatus.PLAYER_PLAYING -> {
                showPlayIcon(false)
                showProgress(false)
                Log.i(TAG, "onMediaStateUpdated: PLAYER_PLAYING")
            }
            MediaHelperStatus.PLAYER_PROGRESS -> {
                setSeekbarProgress(mediaPlayerInfo?.currProgress ?: 0)
                showProgress(false)
            }
            MediaHelperStatus.PLAYER_ERROR -> {
                showPlayIcon()
                showProgress(false)
                Log.i(TAG, "onMediaStateUpdated: PLAYER_ERROR")
            }
            MediaHelperStatus.PLAYER_COMPLETED -> {
                showPlayIcon()
                showProgress(false)
                resetSeekbar()
            }
            else -> {
                // any thing
                showPlayIcon()
                showProgress(false)
                resetSeekbar()
            }
        }
    }

    private fun showPlayIcon(showPlayIcon: Boolean = true) {
        if (showPlayIcon) {
            imageButtonPlayPause?.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        } else {
            imageButtonPlayPause?.setImageResource(R.drawable.ic_baseline_pause_24)
        }
    }

    private fun showProgress(showProgress: Boolean = true) {
        if (showProgress) {
            progressBarDownload?.visibility = View.VISIBLE
        } else {
            progressBarDownload?.visibility = View.GONE
        }
    }

    private fun resetSeekbar() {
        seekbarVoice?.progress = 0
    }

    private fun setSeekbarProgress(progress: Int) {
        seekbarVoice?.progress = progress
    }

    private fun setPlayerInfo(downloadInfo: DownloadInfo?) {
        textviewTimer?.text = MediaMetadataRetrieverHelper.getFileDuration(downloadInfo?.filePath)
    }

    private fun setPlayerInfo(mediaPlayerInfo: MediaPlayerInfo?) {
        textviewTimer?.text =
            MediaMetadataRetrieverHelper.getFileDuration(mediaPlayerInfo?.currLocalFileAddress)
    }

    private fun bindMediaHelper() {
        val targetMessageModel = ChatKitV4MediaHelperV2.getMessageModel()
        if (messageModel?.id == targetMessageModel?.id) {
            // sync listeners
            ChatKitV4MediaHelperV2.setMediaHelperCallback(this)
            // sync UI
            when {
                ChatKitV4MediaHelperV2.getIsPlaying() -> {
                    onMediaStateUpdated(MediaHelperStatus.PLAYER_PLAYING)
                }
                ChatKitV4MediaHelperV2.getIsDownloading() -> {
                    onMediaStateUpdated(MediaHelperStatus.DOWNLOAD_PREPARING)
                }
                else -> {
                    onMediaStateUpdated(MediaHelperStatus.IDLE)
                }
            }
        } else {
            // reset/IDLE UI
            onMediaStateUpdated(MediaHelperStatus.IDLE)
        }
    }

}