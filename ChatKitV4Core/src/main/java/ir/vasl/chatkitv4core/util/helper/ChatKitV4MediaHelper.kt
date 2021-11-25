package ir.vasl.chatkitv4core.util.helper

import android.util.Log
import com.huxq17.download.core.DownloadInfo
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageConditionStatus
import ir.vasl.chatkitv4core.util.downloadmanager.ChatKitV4DownloadManager
import ir.vasl.chatkitv4core.util.player.ChatKitV4MediaPlayer
import ir.vasl.chatkitv4core.util.player.interfaces.MediaHelperCallback

class ChatKitV4MediaHelper(
    private val mediaHelperCallback: MediaHelperCallback? = null
) : MediaHelperCallback {

    private val TAG = "ChatKitV4MediaHelperV2"

    private var chatKitV4DownloadManager = ChatKitV4DownloadManager(this)
    private var chatKitV4MediaPlayer = ChatKitV4MediaPlayer(this)
    private var messageModelQueue = HashMap<String, MessageModel>()

    fun downloadFile(messageModel: MessageModel) {
        Log.i(TAG, "downloadFile: newMessageModelId -> ${messageModel.id}")

        // we have a new guest
        this.messageModelQueue[messageModel.id] = messageModel

        if (messageModel.remoteFileUrl.isEmpty().not())
            chatKitV4DownloadManager.submitNewDownloadRequest(messageModel)
        else {
            Log.i(TAG, "downloadFile: remoteFileUrl is empty!")
            mediaHelperCallback?.onMediaStateUpdated(
                messageModel = messageModel,
                messageConditionStatus = MessageConditionStatus.DOWNLOAD_FAILED
            )
        }
    }

    fun stopDownloadFile(messageModel: MessageModel) {
        chatKitV4DownloadManager.stopDownload(messageModel)
    }

    fun playVoice(messageModel: MessageModel) {
        // we have a new guest
        this.messageModelQueue[messageModel.id] = messageModel
        chatKitV4MediaPlayer.playSound(messageModel)
    }

    fun stopVoice() {
        chatKitV4MediaPlayer.terminatePlayer()
    }

    override fun onMediaStateDownloaderUpdated(
        messageId: String,
        messageConditionStatus: MessageConditionStatus,
        downloadInfo: DownloadInfo?,
        progress: Int?
    ) {
        // get target model
        val messageModel = messageModelQueue[messageId]
        messageModel?.let { validMessageModel ->
            // update filePath ;)
            if (messageConditionStatus == MessageConditionStatus.DOWNLOAD_SUCCEED)
                validMessageModel.localFileAddress = downloadInfo?.filePath ?: ""
            // update status
            validMessageModel.messageConditionStatus = messageConditionStatus.name
            // callback all changes to save!
            mediaHelperCallback?.onMediaStateUpdated(validMessageModel, messageConditionStatus)
        }
    }

    override fun onMediaStatePlayerUpdated(
        messageModel: MessageModel,
        messageConditionStatus: MessageConditionStatus,
        progress: Int?
    ) {
        // update status
        messageModel.messageConditionStatus = messageConditionStatus.name
        // callback all changes to save!
        mediaHelperCallback?.onMediaStateUpdated(
            messageModel = messageModel,
            messageConditionStatus = messageConditionStatus,
            progressPlayer = progress
        )
    }


}