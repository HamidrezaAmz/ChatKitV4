package ir.vasl.chatkitv4core.util

import java.net.URI

class temp {

    companion object {

        val TEMP_URL = "https://datyar.vaslapp.com/public/api/v1/file/download/WhatsApp%20Image%202022-01-23%20at%2012.20.23%20PM.jpeg?provider=LOCAL&access=PRIVATE&parentId=e10adc39-49ba-39ab-be56-e057f20f883e&bucketName=21d1c422-1916-3847-ac16-dd7c7c32acc8&fileName=8c55fe82-aa50-4e2d-9dbb-1fb3bc02ec66.jpeg&mimeType=image%252Fjpeg&fileSize=40434"

        // val TEMP_URL = "www.google.com/temp.png"

        @JvmStatic
        fun main(args: Array<String>) {
            println("readUrlParams: " + getUrlWithExtension(TEMP_URL))
        }

        fun getUrlWithExtension(remoteFileUrl: String): String {
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

        fun getFileExtension(remoteFileUrl: String): String {
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
}