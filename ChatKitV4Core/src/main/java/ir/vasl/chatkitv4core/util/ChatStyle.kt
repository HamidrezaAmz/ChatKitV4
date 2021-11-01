package ir.vasl.chatkitv4core.util

import android.content.Context
import android.util.AttributeSet
import ir.vasl.chatkitv4core.R

class ChatStyle(context: Context, attrs: AttributeSet?) : ChatBaseStyle(context, attrs) {

    // self scope
    var selfTextLayoutId: Int = -1
    var selfVoiceLayoutId: Int = -1
    var selfVideoLayoutId: Int = -1
    var selfImageLayoutId: Int = -1

    // other scope
    var otherTextLayoutId: Int = -1
    var otherVoiceLayoutId: Int = -1

    // system scope
    var systemTextLayoutId: Int = -1

    companion object Parser {

        fun parse(context: Context, attrs: AttributeSet?): ChatStyle {
            val chatStyle = ChatStyle(context, attrs)
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChatKitV4)

            // self scope
            chatStyle.selfTextLayoutId = typedArray.getResourceId(
                R.styleable.ChatKitV4_self_text_layout_id,
                R.layout.defaul_self_text_layout
            )
            chatStyle.selfVoiceLayoutId = typedArray.getResourceId(
                R.styleable.ChatKitV4_self_voice_layout_id,
                R.layout.defaul_self_voice_layout
            )

            // other scope
            chatStyle.otherTextLayoutId = typedArray.getResourceId(
                R.styleable.ChatKitV4_other_text_layout_id,
                R.layout.defaul_other_text_layout
            )
            chatStyle.otherVoiceLayoutId = typedArray.getResourceId(
                R.styleable.ChatKitV4_other_voice_layout_id,
                R.layout.defaul_other_voice_layout
            )

            // system scope
            chatStyle.systemTextLayoutId = typedArray.getResourceId(
                R.styleable.ChatKitV4_system_text_layout_id,
                R.layout.defaul_system_text_layout
            )

            typedArray.recycle()
            return chatStyle
        }
    }

}