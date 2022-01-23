package ir.vasl.chatkitv4core.util.helper

import java.net.URI

object RemoteFileUrlHelper {

    fun cleanAndValidateUrl(remoteFileUrl: String?): String? {
        return remoteFileUrl?.let { getUrlWithExtension(it) }
    }

    private fun getUrlWithExtension(remoteFileUrl: String): String {
        return try {
            val uri = URI(remoteFileUrl)
            val path = uri.path
            val fileExtension = getFileExtension(path)
            if (fileExtension.isNullOrEmpty().not())
                remoteFileUrl + "." + fileExtension
            else
                remoteFileUrl
        } catch (e: Exception) {
            remoteFileUrl
        }
    }

    private fun getFileExtension(remoteFileUrl: String): String {
        try {
            val emptyExt = ""
            if (remoteFileUrl.contains(".")) {
                val sub = remoteFileUrl.substring(remoteFileUrl.lastIndexOf('.') + 1)
                if (sub.isNullOrEmpty())
                    return emptyExt
                return if (sub.contains("?"))
                    sub.substring(0, sub.indexOf('?'))
                else
                    sub
            }
            return emptyExt
        } catch (e: Exception) {
            return ""
        }
    }


}