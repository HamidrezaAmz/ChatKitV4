package ir.vasl.chatkitv4core.util.helper

import android.Manifest

object PermissionHelper {

    val PERMISSION_WRITE_EXTERNAL_STORAGE: String = Manifest.permission.WRITE_EXTERNAL_STORAGE
    val PERMISSION_READ_EXTERNAL_STORAGE: String = Manifest.permission.READ_EXTERNAL_STORAGE

    fun checkPermissionIsGranted(permission: String): Boolean {
        return true
    }

    fun checkPermissionListIsGranted(permissionList: List<String>): Boolean {
        var isPermissionListGranted = true
        for (permission in permissionList) {
            if (!checkPermissionIsGranted(permission)) {
                isPermissionListGranted = false
            }
        }
        return isPermissionListGranted
    }

}