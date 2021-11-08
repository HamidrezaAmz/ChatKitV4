package ir.vasl.chatkitv4.utils

import android.os.Build

class BuildUtils {

    companion object {
        
        fun isAtLeast24Api(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        }

        fun isAtLeast17Api(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
        }
    }

}