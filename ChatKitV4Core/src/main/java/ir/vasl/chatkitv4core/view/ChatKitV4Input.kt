package ir.vasl.chatkitv4core.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import ir.vasl.chatkitv4core.databinding.LayoutChatkitV4InputBinding
import ir.vasl.chatkitv4core.view.interfaces.ChatKitV4InputCallback

class ChatKitV4Input @kotlin.jvm.JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var binding = LayoutChatkitV4InputBinding.inflate(LayoutInflater.from(context))

    private lateinit var chatKitV4InputCallback: ChatKitV4InputCallback

    init {
        addView(binding.root)
        initializeListeners()
    }

    fun initializeChatKitV4InputCallback(chatKitV4InputCallback: ChatKitV4InputCallback) {
        this.chatKitV4InputCallback = chatKitV4InputCallback
    }

    private fun initializeListeners() {
        binding.btnSendMessage.setOnClickListener {
            if (::chatKitV4InputCallback.isInitialized) {
                val message = binding.etInputMessage.text.toString()
                if (!message.isNullOrEmpty())
                    chatKitV4InputCallback.onSubmitNewMessage(message)
            }
            binding.etInputMessage.text?.clear()
        }
    }

}