package ir.vasl.chatkitv4core.util.helper

import android.util.Log
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.model.chatkitv4enums.MediaHelperStatus
import ir.vasl.chatkitv4core.util.downloadmanager.ChatKitV4DownloadManager
import ir.vasl.chatkitv4core.util.downloadmanager.SingletonDownloadManager
import ir.vasl.chatkitv4core.util.downloadmanager.model.DownloadManagerInfo
import ir.vasl.chatkitv4core.util.player.ChatKitV4MediaPlayer
import ir.vasl.chatkitv4core.util.player.SingletonMediaPlayer
import ir.vasl.chatkitv4core.util.player.interfaces.MediaHelperCallback

class ChatKitV4MediaHelper(
    private val mediaHelperCallback: MediaHelperCallback? = null
) : MediaHelperCallback {

    private val TAG = "ChatKitV4MediaHelper"
    private var messageModel: MessageModel? = null
    private var chatKitV4DownloadManager: ChatKitV4DownloadManager = ChatKitV4DownloadManager(this)
    private var chatKitV4MediaPlayer: ChatKitV4MediaPlayer = ChatKitV4MediaPlayer(this)

    fun submit(messageModel: MessageModel?) {
        this.messageModel = messageModel
        // check download manger info and status
        val downloadManagerInfo = SingletonDownloadManager.getInstance()?.getDownloadManagerInfo()
        if (downloadManagerInfo != null) {
            // oh! we have a downloading file
            if (messageModel?.id == downloadManagerInfo.currMessageModelId) {
                // stop and cancel current downloading file
                SingletonDownloadManager.getInstance()?.refreshDownloadManagerInfo()
                mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.PLAYER_STOPPED)
                Log.i(TAG, "submit: -> STOP | we have sth in our download manger")
            } else {
                // stop last downloading item and then start new one

            }
        } else if (SingletonMediaPlayer.getInstance()?.getIsPlaying() == true) {
            // check if we are playing the voice and then stop or download target file ;)
            // check if selected voice is playing or not
            val mediaPlayerInfo = SingletonMediaPlayer.getInstance()?.getMediaPlayerInfo()
            if (messageModel?.id == mediaPlayerInfo?.currMessageModelId) {
                // so submitted item is current playing ite, so stop it
                SingletonMediaPlayer.getInstance()?.refreshMediaPlayerInfo()
                mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.PLAYER_STOPPED)
                Log.i(TAG, "submit: -> STOP")
            } else {
                // so we have a new file we have to play it after stopping last one
                SingletonMediaPlayer.getInstance()?.refreshMediaPlayerInfo()
                mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.PLAYER_STOPPED)
                Log.i(TAG, "submit: -> STOP")
                // now play the new one
                mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.IDLE)
                Log.i(TAG, "submit: -> IDLE")
                downloadAndPlay(messageModel)
            }
        } else {
            // file is not playing, so check existence and play the voice
            mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.IDLE)
            Log.i(TAG, "submit: -> IDLE")
            downloadAndPlay(messageModel)
        }
    }

    fun downloadAndPlay(messageModel: MessageModel?) {

        mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.DOWNLOAD_PREPARING)
        Log.i(TAG, "downloadAndPlay: -> PREPARING")

        if (messageModel == null) {
            SingletonMediaPlayer.getInstance()?.refreshMediaPlayerInfo()
            mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.ERROR)
            Log.i(TAG, "downloadAndPlay -> ERROR | message model is null")
            return
        }

        val remoteFileUrl = this.messageModel?.remoteFileUrl
        if (!remoteFileUrl.isNullOrEmpty()) {
            // so lets check permission
            val listOfPermission = listOf<String>(
                PermissionHelper.PERMISSION_READ_EXTERNAL_STORAGE,
                PermissionHelper.PERMISSION_WRITE_EXTERNAL_STORAGE
            )
            // todo : permission should be add again...
            /*
            if (!PermissionHelper.checkPermissionListIsGranted(listOfPermission)) {
                mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.ERROR)
                Log.i(TAG, "downloadAndPlay -> ERROR | checkPermissionListIsGranted : false")
                return
            }
            */
            // so lets download the file
            downloadFile()
        } else {
            // What the hell...!
            mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.ERROR)
            Log.i(TAG, "submit -> ERROR | We do not have any thing to do with")
        }
    }

    private fun downloadFile() {
        mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.DOWNLOAD_IN_PROGRESS)
        Log.i(TAG, "downloadFile -> DOWNLOADING : Add file into queue")

        val downloadManagerInfo = DownloadManagerInfo(
            this.messageModel?.id,
            this.messageModel?.remoteFileUrl
        )
        SingletonDownloadManager.getInstance()?.setMediaHelperCallback(mediaHelperCallback)
        SingletonDownloadManager.getInstance()?.setDownloadManagerInfo(downloadManagerInfo)
        SingletonDownloadManager.getInstance()?.startDownloadFile(
            remoteFileUrl = messageModel?.remoteFileUrl ?: "what the file!"
        )
    }

}