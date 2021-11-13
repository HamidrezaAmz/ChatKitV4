package ir.vasl.chatkitv4core.util.helper

import android.content.Context
import android.util.AttributeSet
import ir.vasl.chatkitv4core.R

object TypedArrayReaderHelper {

    fun read(context: Context, attrs: AttributeSet, targetAttr: Int): String? {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ChatKitV4)
        return styledAttributes.getString(targetAttr)
    }

}