package ir.vasl.chatkitv4core.util.helper

import java.io.File

object FileHelper {

    fun checkFileExists(localFileAddress: String): Boolean {
        return File(localFileAddress).exists()
    }
}