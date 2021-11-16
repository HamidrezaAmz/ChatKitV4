package ir.vasl.chatkitv4core.util.helper

import com.huxq17.download.core.DownloadInfo
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.model.chatkitv4enums.MediaHelperStatus
import ir.vasl.chatkitv4core.util.downloadmanager.ChatKitV4DownloadManager
import ir.vasl.chatkitv4core.util.player.ChatKitV4MediaPlayer
import ir.vasl.chatkitv4core.util.player.interfaces.MediaHelperCallback
import ir.vasl.chatkitv4core.util.player.model.MediaPlayerInfo

object ChatKitV4MediaHelperV2 : MediaHelperCallback {

    private val TAG = "ChatKitV4MediaHelperV2"

    private var messageModel: MessageModel? = null
    private var chatKitV4DownloadManager = ChatKitV4DownloadManager(this)
    private var chatKitV4MediaPlayer = ChatKitV4MediaPlayer(this)
    private var mediaHelperCallback: MediaHelperCallback? = null
    private var currentMediaHelperStatus: MediaHelperStatus = MediaHelperStatus.IDLE

    override fun onMediaStateUpdated(
        mediaHelperStatus: MediaHelperStatus,
        downloadInfo: DownloadInfo?,
        mediaPlayerInfo: MediaPlayerInfo?
    ) {
        this.currentMediaHelperStatus = mediaHelperStatus
        this.mediaHelperCallback?.onMediaStateUpdated(
            mediaHelperStatus,
            downloadInfo,
            mediaPlayerInfo
        )

        // check if need to have an action inside media helper
        when (mediaHelperStatus) {
            MediaHelperStatus.DOWNLOAD_SUCCESS -> {
                play(downloadInfo)
            }
            else -> {
                // nothing yet
            }
        }
    }

    fun setMediaHelperCallback(mediaHelperCallback: MediaHelperCallback? = null) {
        this.mediaHelperCallback = mediaHelperCallback
    }

    fun submit(newMessageModel: MessageModel?, mediaHelperCallback: MediaHelperCallback? = null) {

        // check new input submit
        if (newMessageModel?.id == messageModel?.id) {
            // this submit is duplicate
            if (getIsPlaying() || getIsDownloading()) {
                terminate()
                return
            }
        } else {
            // we have a new submit, so just stop last one
            this.mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.IDLE)
        }

        this.messageModel = newMessageModel
        this.mediaHelperCallback = mediaHelperCallback

        // first terminate all ongoing actions (downloading || playing)
        terminate()
        // so now check file existence and then play this shit ;)
        // first check file existence or download it
        download()
    }

    fun getMessageModel(): MessageModel? {
        return this.messageModel
    }

    fun getIsPlaying(): Boolean {
        val isPlaying = when (currentMediaHelperStatus) {
            MediaHelperStatus.PLAYER_PREPARING -> {
                true
            }
            MediaHelperStatus.PLAYER_PREPARED -> {
                true
            }
            MediaHelperStatus.PLAYER_PLAYING -> {
                true
            }
            MediaHelperStatus.PLAYER_PROGRESS -> {
                true
            }
            else -> {
                true
            }
        }
        return chatKitV4MediaPlayer.getIsPlaying() && isPlaying
    }

    fun getIsDownloading(): Boolean {

        val isDownloading = when (currentMediaHelperStatus) {
            MediaHelperStatus.DOWNLOAD_PREPARING -> {
                true
            }
            MediaHelperStatus.DOWNLOAD_STARTED -> {
                true
            }
            MediaHelperStatus.DOWNLOAD_IN_PROGRESS -> {
                true
            }
            else -> {
                false
            }
        }
        return isDownloading
    }

    private fun terminate() {
        chatKitV4MediaPlayer.terminatePlayer()
        chatKitV4DownloadManager.terminateDownload(downloadId = messageModel?.remoteFileUrl)
        mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.TERMINATE)
    }

    private fun download() {
        mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.DOWNLOAD_PREPARING)
        val remoteFileUrl = this.messageModel?.remoteFileUrl
        if (!remoteFileUrl.isNullOrEmpty()) {
            // so lets check permission
            val listOfPermission = listOf<String>(
                PermissionHelper.PERMISSION_READ_EXTERNAL_STORAGE,
                PermissionHelper.PERMISSION_WRITE_EXTERNAL_STORAGE
            )
            // todo : permission checker should be added again
            /*
            if (!PermissionHelper.checkPermissionListIsGranted(listOfPermission)) {
                mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.ERROR)
                Log.i(TAG, "downloadAndPlay -> ERROR | checkPermissionListIsGranted : false")
                return
            }
            */
            // so lets download the file
            chatKitV4DownloadManager.setMediaHelperCallback(this)
            chatKitV4DownloadManager.startDownload(messageModel?.remoteFileUrl)
        }
    }

    private fun play(downloadInfo: DownloadInfo?) {
        mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.PLAYER_PREPARING)
        chatKitV4MediaPlayer.playSound(localFileAddress = downloadInfo?.filePath)
    }

}