package ir.vasl.chatkitv4.utils

import android.app.Activity
import android.content.Intent
import ir.vasl.chatkitv4.utils.PublicValue.PICKFILE_RESULT_CODE

object PublicFunction {

    fun openFileChooser(activity: Activity) {
        var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
        chooseFile.type = "*/*"
        chooseFile = Intent.createChooser(chooseFile, "Choose a file")
        activity.startActivityForResult(chooseFile, PICKFILE_RESULT_CODE)
    }

    fun openFileChooserV2(activity: Activity) {
       
    }

}