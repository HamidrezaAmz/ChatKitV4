package ir.vasl.chatkitv4core.util.downloadmanager

import android.util.Log
import com.huxq17.download.Pump
import com.huxq17.download.core.DownloadListener
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageConditionStatus
import ir.vasl.chatkitv4core.util.PublicValue
import ir.vasl.chatkitv4core.util.player.interfaces.MediaHelperCallback
import okhttp3.Request

class ChatKitV4DownloadManager(
    private var mediaHelperCallback: MediaHelperCallback? = null
) {

    private val TAG = "ChatKitV4DownloadManage"

    init {
        // this is not possible but, this shit is listening to download scenario! :))
        val downloadObserver: DownloadListener = object : DownloadListener() {

            override fun onProgress(progress: Int) {
                Log.i(
                    TAG,
                    "onProgress: ${downloadInfo.progress} % | downloadId: ${downloadInfo.id}"
                )
                mediaHelperCallback?.onMediaStateDownloaderUpdated(
                    messageId = downloadInfo.id,
                    messageConditionStatus = MessageConditionStatus.DOWNLOAD_IN_PROGRESS,
                    downloadInfo = downloadInfo
                )
            }

            override fun onSuccess() {
                Log.i(TAG, "onSuccess: ${downloadInfo.filePath}")
                mediaHelperCallback?.onMediaStateDownloaderUpdated(
                    messageId = downloadInfo.id,
                    messageConditionStatus = MessageConditionStatus.DOWNLOAD_SUCCEED,
                    downloadInfo = downloadInfo
                )
            }

            override fun onFailed() {
                Log.i(TAG, "onFailed: ${downloadInfo.filePath}")
                mediaHelperCallback?.onMediaStateDownloaderUpdated(
                    messageId = downloadInfo.id,
                    messageConditionStatus = MessageConditionStatus.DOWNLOAD_FAILED,
                    downloadInfo = downloadInfo
                )
            }
        }
        downloadObserver.enable()
    }

    fun submitNewDownloadRequest(messageModel: MessageModel) {
        Pump.newRequest(messageModel.remoteFileUrl)
            .setId(messageModel.id)
            .setRequestBuilder(Request.Builder())
            .tag(PublicValue.SAMPLE_FILE_TAG)
            .submit()
    }

    fun stopDownload(messageModel: MessageModel?) {
        Pump.stop(messageModel?.id)
    }

}