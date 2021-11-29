package ir.vasl.chatkitv4core.view.adapter.viewholder

import android.view.View
import android.widget.ProgressBar
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.huxq17.download.core.DownloadInfo
import ir.vasl.chatkitv4core.R
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageConditionStatus
import ir.vasl.chatkitv4core.util.helper.MediaMetadataRetrieverHelper
import ir.vasl.chatkitv4core.util.player.interfaces.MediaHelperCallback
import ir.vasl.chatkitv4core.util.player.model.MediaPlayerInfo
import ir.vasl.chatkitv4core.view.interfaces.ChatKitV4ListAdapterCallback

class ViewHolderAlphaVoiceMessage(
    private var view: View,
    private var chatKitV4ListAdapterCallback: ChatKitV4ListAdapterCallback? = null
) : RecyclerView.ViewHolder(view), MediaHelperCallback {

    private var messageModel: MessageModel? = null
    private var tvTitle = view.findViewById(R.id.tv_title) as AppCompatTextView?
    private var tvSubTitle = view.findViewById(R.id.tv_sub_title) as AppCompatTextView?
    private var tvTimer = view.findViewById(R.id.tv_timer) as AppCompatTextView?
    private var tvDate = view.findViewById(R.id.tv_date) as AppCompatTextView?
    private var ibPlayPause = view.findViewById(R.id.ib_play_pause) as AppCompatImageButton?
    private var pbDownload = view.findViewById(R.id.pb_download) as ProgressBar?
    private var sbVoice = view.findViewById(R.id.sb_voice) as SeekBar?
    private var pbUpload = view.findViewById(R.id.pb_upload) as ProgressBar?
    private var ibDownload = view.findViewById(R.id.ib_retry) as AppCompatImageButton?

    init {
        sbVoice?.isEnabled = false
    }

    fun bind(messageModel: MessageModel?) {
        this.messageModel = messageModel

        val title = messageModel?.title
        val subTitle = messageModel?.subTitle
        val date = messageModel?.date
        val progressPlayer = messageModel?.progressPlayer

        // set title
        if (title.isNullOrEmpty()) {
            tvTitle?.visibility = View.GONE
        } else {
            tvTitle?.visibility = View.VISIBLE
            tvTitle?.text = title
        }

        // set sub title
        if (subTitle.isNullOrEmpty()) {
            tvSubTitle?.visibility = View.GONE
        } else {
            tvSubTitle?.visibility = View.VISIBLE
            tvSubTitle?.text = subTitle
        }

        // set date
        if (date.isNullOrEmpty()) {
            tvDate?.visibility = View.GONE
        } else {
            tvDate?.visibility = View.VISIBLE
            tvDate?.text = date
        }

        // set progress player
        sbVoice?.progress = progressPlayer ?: 0

        // set-sync message condition status
        syncMessageConditionWithUI(messageModel)

        // set click listener
        ibPlayPause?.setOnClickListener {

            when (messageModel?.messageConditionStatus) {
                // if the player is playing sth, so stop it but if the player is on any condition,
                // try to play and launch player ...
                MessageConditionStatus.PLAYER_STARTED.name, MessageConditionStatus.PLAYER_IN_PROGRESS.name -> {
                    messageModel.messageConditionStatus =
                        MessageConditionStatus.PLAYER_STOPPED.name
                }
                else -> {
                    messageModel?.messageConditionStatus =
                        MessageConditionStatus.PLAYER_STARTED.name
                }
            }

            syncMessageConditionWithUI(messageModel)

            // save changes into database
            chatKitV4ListAdapterCallback?.onPlayPauseClicked(messageModel)
        }

        ibDownload?.setOnClickListener {
            messageModel?.messageConditionStatus = MessageConditionStatus.DOWNLOAD_STARTED.name
            syncMessageConditionWithUI(messageModel)
            // save changes into database
            chatKitV4ListAdapterCallback?.onDownloadFileClicked(messageModel)
        }

        pbDownload?.setOnClickListener {
            messageModel?.messageConditionStatus = MessageConditionStatus.IDLE.name
            syncMessageConditionWithUI(messageModel)
            // save changes into database
            chatKitV4ListAdapterCallback?.onPlayPauseClicked(messageModel)
        }

        pbUpload?.setOnClickListener {
            messageModel?.messageConditionStatus = MessageConditionStatus.IDLE.name
            syncMessageConditionWithUI(messageModel)
            // save changes into database
            chatKitV4ListAdapterCallback?.onPlayPauseClicked(messageModel)
        }

    }

    private fun syncMessageConditionWithUI(messageModel: MessageModel?) {
        when (messageModel?.messageConditionStatus ?: MessageConditionStatus.IDLE.name) {

            // global
            MessageConditionStatus.UNDEFINED.name -> {
                showDownloadProgress(false)
                showUploadProgress(false)
                showMediaPlayer(false)
                syncMediaPlayerStateWithUI(false)
                showDownload(true)
            }
            MessageConditionStatus.IDLE.name -> {
                showDownloadProgress(false)
                showUploadProgress(false)
                showMediaPlayer(false)
                syncMediaPlayerStateWithUI(false)
                showDownload(true)
            }

            // uploader
            MessageConditionStatus.UPLOAD_IN_PROGRESS.name -> {
                showDownloadProgress(false)
                showUploadProgress(true)
                showMediaPlayer(false)
                syncMediaPlayerStateWithUI(false)
                showDownload(false)
            }
            MessageConditionStatus.UPLOAD_FAILED.name -> {
                showDownloadProgress(false)
                showUploadProgress(false)
                showMediaPlayer(true)
                syncMediaPlayerStateWithUI(true)
                showDownload(false)

                // make retry view visible
                // todo : IMPL retry scenario
            }
            MessageConditionStatus.UPLOAD_SUCCEED.name -> {
                showDownloadProgress(false)
                showUploadProgress(false)
                showMediaPlayer(true)
                syncMediaPlayerStateWithUI(true)
                showDownload(false)
            }

            // downloader
            MessageConditionStatus.DOWNLOAD_IN_PROGRESS.name -> {
                showDownloadProgress(true)
                showUploadProgress(false)
                showMediaPlayer(false)
                syncMediaPlayerStateWithUI(false)
                showDownload(false)
            }
            MessageConditionStatus.DOWNLOAD_FAILED.name -> {
                showDownloadProgress(false)
                showUploadProgress(false)
                showMediaPlayer(false)
                syncMediaPlayerStateWithUI(false)
                showDownload(true)
            }
            MessageConditionStatus.DOWNLOAD_NEEDED.name -> {
                showDownloadProgress(false)
                showUploadProgress(false)
                showMediaPlayer(false)
                syncMediaPlayerStateWithUI(false)
                showDownload(true)
            }
            MessageConditionStatus.DOWNLOAD_SUCCEED.name -> {
                showDownloadProgress(false)
                showUploadProgress(false)
                showMediaPlayer(true)
                syncMediaPlayerStateWithUI(true)
                showDownload(false)
            }
            MessageConditionStatus.DOWNLOAD_STOPPED.name -> {
                showDownloadProgress(false)
                showUploadProgress(false)
                showMediaPlayer(false)
                syncMediaPlayerStateWithUI(false)
                showDownload(true)
            }

            // player
            MessageConditionStatus.PLAYER_STARTED.name -> {
                showDownloadProgress(false)
                showUploadProgress(false)
                showMediaPlayer(true)
                syncMediaPlayerStateWithUI(false)
                showDownload(false)
            }
            MessageConditionStatus.PLAYER_IN_PROGRESS.name -> {
                showDownloadProgress(false)
                showUploadProgress(false)
                showMediaPlayer(true)
                syncMediaPlayerStateWithUI(false)
                showDownload(false)
            }
            MessageConditionStatus.PLAYER_PAUSED.name -> {
                showDownloadProgress(false)
                showUploadProgress(false)
                showMediaPlayer(true)
                syncMediaPlayerStateWithUI(true)
                showDownload(false)
            }
            MessageConditionStatus.PLAYER_STOPPED.name -> {
                showDownloadProgress(false)
                showUploadProgress(false)
                showMediaPlayer(true)
                syncMediaPlayerStateWithUI(true)
                showDownload(false)
            }
            MessageConditionStatus.PLAYER_FAILED.name -> {
                showDownloadProgress(false)
                showUploadProgress(false)
                showMediaPlayer(true)
                syncMediaPlayerStateWithUI(true)
                showDownload(false)
            }
            MessageConditionStatus.PLAYER_FINISHED.name -> {
                showDownloadProgress(false)
                showUploadProgress(false)
                showMediaPlayer(true)
                syncMediaPlayerStateWithUI(true)
                showDownload(false)
            }

            else -> {
                showDownloadProgress(false)
                showUploadProgress(false)
                showMediaPlayer(true)
                syncMediaPlayerStateWithUI(true)
            }
        }
    }

    private fun syncMediaPlayerStateWithUI(showPlayIcon: Boolean = true) {
        if (showPlayIcon) {
            ibPlayPause?.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        } else {
            ibPlayPause?.setImageResource(R.drawable.ic_baseline_pause_24)
        }
    }

    private fun showMediaPlayer(showMediaPlayer: Boolean = true) {
        if (showMediaPlayer) {
            ibPlayPause?.visibility = View.VISIBLE
        } else {
            ibPlayPause?.visibility = View.INVISIBLE
        }
    }

    private fun showDownloadProgress(showDownloadProgress: Boolean = true) {
        if (showDownloadProgress) {
            pbDownload?.visibility = View.VISIBLE
        } else {
            pbDownload?.visibility = View.GONE
        }
    }

    private fun showUploadProgress(showUploadProgress: Boolean = true) {
        if (showUploadProgress) {
            pbUpload?.visibility = View.VISIBLE
        } else {
            pbUpload?.visibility = View.GONE
        }
    }

    private fun showDownload(showRetry: Boolean) {
        if (showRetry) {
            ibDownload?.visibility = View.VISIBLE
        } else {
            ibDownload?.visibility = View.GONE
        }
    }

    private fun resetSeekbar() {
        sbVoice?.progress = 0
    }

    private fun setPlayerProgress(progress: Int) {
        sbVoice?.progress = progress
    }

    private fun setPlayerInfo(downloadInfo: DownloadInfo?) {
        tvTimer?.text = MediaMetadataRetrieverHelper.getFileDuration(downloadInfo?.filePath)
    }

    private fun setPlayerInfo(mediaPlayerInfo: MediaPlayerInfo?) {
        tvTimer?.text =
            MediaMetadataRetrieverHelper.getFileDuration(mediaPlayerInfo?.currLocalFileAddress)
    }

}