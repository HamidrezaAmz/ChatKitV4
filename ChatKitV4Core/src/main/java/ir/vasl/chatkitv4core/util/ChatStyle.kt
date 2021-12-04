package ir.vasl.chatkitv4core.util

import android.content.Context
import android.util.AttributeSet
import ir.vasl.chatkitv4core.R

class ChatStyle(context: Context, attrs: AttributeSet?) : ChatBaseStyle(context, attrs) {

    // alpha scope
    var alphaTextLayoutId: Int = -1
    var alphaVoiceLayoutId: Int = -1
    var alphaVideoLayoutId: Int = -1
    var alphaImageLayoutId: Int = -1

    // beta scope
    var betaTextLayoutId: Int = -1
    var betaVoiceLayoutId: Int = -1

    // system scope
    var systemTextLayoutId: Int = -1

    // chatkit scope
    var notSupportedLayoutId: Int = -1

    companion object Parser {

        fun parse(context: Context, attrs: AttributeSet?): ChatStyle {
            val chatStyle = ChatStyle(context, attrs)
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChatKitV4)

            // alpha scope
            chatStyle.alphaTextLayoutId = typedArray.getResourceId(
                R.styleable.ChatKitV4_alpha_text_layout_id,
                R.layout.defaul_alpha_text_layout
            )
            chatStyle.alphaVoiceLayoutId = typedArray.getResourceId(
                R.styleable.ChatKitV4_alpha_voice_layout_id,
                R.layout.defaul_alpha_voice_layout
            )

            // beta scope
            chatStyle.betaTextLayoutId = typedArray.getResourceId(
                R.styleable.ChatKitV4_beta_text_layout_id,
                R.layout.defaul_beta_text_layout
            )
            chatStyle.betaVoiceLayoutId = typedArray.getResourceId(
                R.styleable.ChatKitV4_beta_voice_layout_id,
                R.layout.defaul_beta_voice_layout
            )

            // system scope
            chatStyle.systemTextLayoutId = typedArray.getResourceId(
                R.styleable.ChatKitV4_system_text_layout_id,
                R.layout.defaul_system_text_layout
            )

            // chatkit scope
            chatStyle.notSupportedLayoutId = typedArray.getResourceId(
                R.styleable.ChatKitV4_not_supported_layout_id,
                R.layout.defaul_not_supported_layout
            )

            typedArray.recycle()
            return chatStyle
        }
    }

}