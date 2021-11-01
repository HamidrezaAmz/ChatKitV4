package ir.vasl.chatkitv4core.util.downloadmanager

import android.util.Log
import com.huxq17.download.Pump
import com.huxq17.download.core.DownloadListener
import ir.vasl.chatkitv4core.model.chatkitv4enums.MediaHelperStatus
import ir.vasl.chatkitv4core.util.PublicValue
import ir.vasl.chatkitv4core.util.downloadmanager.model.DownloadManagerInfo
import ir.vasl.chatkitv4core.util.player.interfaces.MediaHelperCallback
import okhttp3.Request

class SingletonDownloadManager {

    private var currMediaHelperCallback: MediaHelperCallback? = null
    private var currDownloadManagerInfo: DownloadManagerInfo? = null

    companion object {

        private const val TAG = "SingletonDownloadManager"

        @Volatile
        private var instance: SingletonDownloadManager? = null

        fun getInstance(): SingletonDownloadManager? {
            if (instance == null) {
                synchronized(SingletonDownloadManager::class.java) {
                    if (instance == null) {
                        instance = SingletonDownloadManager()
                    }
                }
            }
            return instance
        }
    }

    fun setMediaHelperCallback(mediaHelperCallback: MediaHelperCallback?) {
        this.currMediaHelperCallback = mediaHelperCallback
    }

    fun setDownloadManagerInfo(downloadManagerInfo: DownloadManagerInfo?) {
        this.currDownloadManagerInfo = downloadManagerInfo
    }

    fun getDownloadManagerInfo(): DownloadManagerInfo? {
        return currDownloadManagerInfo
    }

    fun refreshDownloadManagerInfo() {
        val downloadItemId = currDownloadManagerInfo?.currLocalFileAddress
        downloadItemId.let {
            Pump.stop(it) // we use local file as id
        }
        // currMediaHelperCallback = null
        currDownloadManagerInfo = null
    }

    fun getWeHaveDownloadingFile(): Boolean {
        return currDownloadManagerInfo != null
    }

    fun startDownloadFile(
        remoteFileUrl: String,
    ) {
        Pump.newRequest(remoteFileUrl)
            .setRequestBuilder(Request.Builder())
            .tag(PublicValue.SAMPLE_FILE_TAG)
            .listener(object : DownloadListener() {

                override fun onProgress(progress: Int) {
                    currMediaHelperCallback?.onDownloadProgress(progress, downloadInfo)
                    currMediaHelperCallback?.onMediaStateUpdated(MediaHelperStatus.DOWNLOAD_IN_PROGRESS)
                    Log.i(TAG, "downloadFile -> DOWNLOADING | progress : $progress")
                }

                override fun onSuccess() {
                    refreshDownloadManagerInfo()
                    currMediaHelperCallback?.onMediaStateUpdated(mediaHelperStatus = MediaHelperStatus.DOWNLOAD_SUCCESS)
                    Log.i(TAG, "downloadFile -> DOWNLOADED")
                    currMediaHelperCallback?.onMediaStateUpdated(
                        mediaHelperStatus = MediaHelperStatus.PLAYER_PLAYING,
                        downloadInfo = downloadInfo
                    )
                    Log.i(TAG, "downloadFile -> PLAY")
                }

                override fun onFailed() {
                    refreshDownloadManagerInfo()
                    currMediaHelperCallback?.onMediaStateUpdated(
                        mediaHelperStatus = MediaHelperStatus.ERROR,
                        downloadInfo = downloadInfo
                    )
                    Log.i(TAG, "downloadFile -> ERROR | errorCode : ${downloadInfo.errorCode}")
                }
            })
            .submit()
    }

}