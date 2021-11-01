package ir.vasl.chatkitv4core.util.downloadmanager

import android.util.Log
import com.huxq17.download.Pump
import com.huxq17.download.core.DownloadListener
import ir.vasl.chatkitv4core.model.chatkitv4enums.MediaHelperStatus
import ir.vasl.chatkitv4core.util.PublicValue
import ir.vasl.chatkitv4core.util.player.interfaces.MediaHelperCallback
import okhttp3.Request

class ChatKitV4DownloadManager(
    private var newMediaHelperCallback: MediaHelperCallback? = null
) {

    private val TAG = "ChatKitV4DownloadMng"
    private var mediaHelperCallback: MediaHelperCallback? = null

    init {
        this.mediaHelperCallback = newMediaHelperCallback
    }

    fun setMediaHelperCallback(newMediaHelperCallback: MediaHelperCallback? = null) {
        this.mediaHelperCallback = newMediaHelperCallback
    }

    fun startDownload(remoteFileUrl: String?) {

        if (remoteFileUrl.isNullOrEmpty()) {
            mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.DOWNLOAD_FAILED)
            Log.i(TAG, "startDownload -> DOWNLOAD_FAILED | remoteFileUrl is null")
            return
        }

        mediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.DOWNLOAD_STARTED)

        Pump.newRequest(remoteFileUrl)
            .setId(remoteFileUrl) // we pass remoteFileUrl for unique file id for download manager
            .setRequestBuilder(Request.Builder())
            .tag(PublicValue.SAMPLE_FILE_TAG)
            .listener(object : DownloadListener() {

                override fun onProgress(progress: Int) {
                    mediaHelperCallback?.onMediaStateUpdated(
                        mediaHelperStatus = MediaHelperStatus.DOWNLOAD_IN_PROGRESS,
                        downloadInfo = downloadInfo
                    )
                }

                override fun onSuccess() {
                    mediaHelperCallback?.onMediaStateUpdated(
                        mediaHelperStatus = MediaHelperStatus.DOWNLOAD_SUCCESS,
                        downloadInfo = downloadInfo
                    )
                }

                override fun onFailed() {
                    mediaHelperCallback?.onMediaStateUpdated(
                        mediaHelperStatus = MediaHelperStatus.DOWNLOAD_FAILED,
                        downloadInfo = downloadInfo
                    )
                }
            })
            .submit()
    }

    fun terminateDownload(downloadId: String?) {
        if (!downloadId.isNullOrEmpty()) {
            Pump.stop(downloadId)
            mediaHelperCallback?.onMediaStateUpdated(mediaHelperStatus = MediaHelperStatus.DOWNLOAD_STOPPED)
            mediaHelperCallback = null // to fix Pump bug! onProgress call after terminate
        }
    }

}