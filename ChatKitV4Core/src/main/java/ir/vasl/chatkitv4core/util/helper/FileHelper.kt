package ir.vasl.chatkitv4core.util.helper

import java.io.File

object FileHelper {

    fun checkFileExists(localFileAddress: String): Boolean {
        return File(localFileAddress).exists()
    }

    fun isValidFile(localFileAddress: String): Boolean {
        val file = File(localFileAddress)

        val isNotEmpty = localFileAddress.isEmpty().not()
        val isFile = file.isFile
        val exists = file.exists()
        val hasLength = file.length() > 0

        return isNotEmpty && isFile && exists && hasLength
    }
}