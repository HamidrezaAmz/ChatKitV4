package ir.vasl.chatkitv4core.util.player.interfaces

import com.huxq17.download.core.DownloadInfo
import ir.vasl.chatkitv4core.model.chatkitv4enums.MediaHelperStatus
import ir.vasl.chatkitv4core.util.player.model.MediaPlayerInfo

interface MediaHelperCallback {

    fun onError(errorMessage: String) {}
    fun onMediaStateUpdated(
        mediaHelperStatus: MediaHelperStatus,
        downloadInfo: DownloadInfo? = null,
        mediaPlayerInfo: MediaPlayerInfo? = null
    ) {
    }

    fun onPlayerStarted() {}
    fun onPlayerCompletion() {}
    fun onPlayerError() {}
    fun onPlayerPrepared() {}

    fun onDownloadStarted() {}
    fun onDownloadError(downloadInfo: DownloadInfo) {}
    fun onDownloadCompletion(downloadInfo: DownloadInfo) {}
    fun onDownloadCanceled() {}
    fun onDownloadProgress(progress: Int, downloadInfo: DownloadInfo) {}

}