package ir.vasl.chatkitv4core.util.helper

import android.Manifest
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED

object PermissionHelper {

    val PERMISSION_WRITE_EXTERNAL_STORAGE: String = Manifest.permission.WRITE_EXTERNAL_STORAGE
    val PERMISSION_READ_EXTERNAL_STORAGE: String = Manifest.permission.READ_EXTERNAL_STORAGE
    val PERMISSION_RECORDE_AUDIO: String = Manifest.permission.RECORD_AUDIO

    fun checkPermissionIsGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PERMISSION_GRANTED
    }

    fun checkPermissionListIsGranted(context: Context, permissionList: List<String>): Boolean {
        var isPermissionListGranted = true
        for (permission in permissionList) {
            if (!checkPermissionIsGranted(context, permission)) {
                isPermissionListGranted = false
            }
        }
        return isPermissionListGranted
    }

}