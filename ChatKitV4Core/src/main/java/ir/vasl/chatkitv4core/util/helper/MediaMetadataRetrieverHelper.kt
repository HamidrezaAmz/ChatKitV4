package ir.vasl.chatkitv4core.util.helper

import android.media.MediaMetadataRetriever
import java.util.concurrent.TimeUnit

object MediaMetadataRetrieverHelper {

    fun getFileDuration(mediaPath: String? = ""): String {

        if (mediaPath.isNullOrEmpty()) {
            return "--:--"
        }

        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(mediaPath)
        val duration =
            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0
        mmr.release()

        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(duration),
            TimeUnit.MILLISECONDS.toSeconds(duration) -
                    TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(duration)
                    )
        );
    }

}