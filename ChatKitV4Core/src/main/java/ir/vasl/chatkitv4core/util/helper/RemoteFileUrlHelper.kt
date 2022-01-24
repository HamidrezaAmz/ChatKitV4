package ir.vasl.chatkitv4core.util.helper

import android.net.Uri

object RemoteFileUrlHelper {

    fun cleanAndValidateUrl(remoteFileUrl: String?): String? {
        val mimeType = getMimeFromUrl(remoteFileUrl)
        val extension = getExtensionByMimeType(mimeType)

        if (extension.isNullOrEmpty().not())
            return remoteFileUrl + extension

        return remoteFileUrl
    }

    fun getMimeFromUrl(url: String?): String {

        val uri = Uri.parse(url)
        val param = uri.getQueryParameter("mimeType") ?: return ""

        return if (param.startsWith("image")) {
            "image"
        } else if (param.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            "document"
        } else if (param.startsWith("application/msword")) {
            "document"
        } else if (param.startsWith("application/pdf")) {
            "pdf"
        } else if (param.startsWith("audio")) {
            "mp3"
        } else {
            ""
        }
    }

    fun getExtensionByMimeType(mimetype: String): String {

        return if (mimetype.startsWith("image")) {
            ".jpg"
        } else if (mimetype.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            ".docx"
        } else if (mimetype.startsWith("application/msword")) {
            ".doc"
        } else if (mimetype.startsWith("application/pdf")) {
            ".pdf"
        } else if (mimetype.startsWith("audio")) {
            ".mp3"
        } else {
            ""
        }
    }

}