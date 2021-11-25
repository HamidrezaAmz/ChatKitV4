package ir.vasl.chatkitv4core.util.player.interfaces

import com.huxq17.download.core.DownloadInfo
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageConditionStatus

interface MediaHelperCallback {

    fun onError(errorMessage: String) {}

    fun onPlayerStarted() {}
    fun onPlayerCompletion() {}
    fun onPlayerError() {}
    fun onPlayerPrepared() {}

    fun onDownloadStarted() {}
    fun onDownloadError(downloadInfo: DownloadInfo) {}
    fun onDownloadCompletion(downloadInfo: DownloadInfo) {}
    fun onDownloadCanceled() {}
    fun onDownloadProgress(progress: Int, downloadInfo: DownloadInfo) {}

    fun onMediaStateUpdated(
        messageModel: MessageModel,
        messageConditionStatus: MessageConditionStatus,
        progressPlayer: Int? = -1,
        progressDownloader: Int? = -1
    ) {
    }

    fun onMediaStatePlayerUpdated(
        messageModel: MessageModel,
        messageConditionStatus: MessageConditionStatus,
        progress: Int? = -1
    ) {
    }

    fun onMediaStateDownloaderUpdated(
        messageId: String,
        messageConditionStatus: MessageConditionStatus,
        downloadInfo: DownloadInfo?,
        progress: Int? = -1
    ) {
    }

}