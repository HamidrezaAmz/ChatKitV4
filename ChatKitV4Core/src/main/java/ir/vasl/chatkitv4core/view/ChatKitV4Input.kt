package ir.vasl.chatkitv4core.view

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import com.devlomi.record_view.OnRecordListener
import com.devlomi.record_view.RecordPermissionHandler
import com.devlomi.record_view.RecordView
import ir.vasl.chatkitv4core.R
import ir.vasl.chatkitv4core.databinding.LayoutChatkitV4InputBinding
import ir.vasl.chatkitv4core.util.helper.PermissionHelper
import ir.vasl.chatkitv4core.util.helper.PermissionHelper.PERMISSION_RECORDE_AUDIO
import ir.vasl.chatkitv4core.util.helper.PermissionHelper.PERMISSION_WRITE_EXTERNAL_STORAGE
import ir.vasl.chatkitv4core.util.publicExtentions.setTypeface
import ir.vasl.chatkitv4core.view.interfaces.ChatKitV4InputCallback

class ChatKitV4Input @kotlin.jvm.JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var binding = LayoutChatkitV4InputBinding.inflate(LayoutInflater.from(context))

    private lateinit var chatKitV4InputCallback: ChatKitV4InputCallback

    private var showVoiceRecorderIcon: Boolean = true
    private var showAttachmentIcon: Boolean = true

    init {
        addView(binding.root)
        initializeRecordView()
        initializeListeners()
    }

    fun initializeChatKitV4InputCallback(chatKitV4InputCallback: ChatKitV4InputCallback) {
        this.chatKitV4InputCallback = chatKitV4InputCallback
    }

    private fun initializeRecordView() {
        binding.recordButton.setRecordView(binding.recordView)
        binding.recordView.setTypeface(ResourcesCompat.getFont(context, R.font.app_font))
        binding.recordView.setOnRecordListener(object : OnRecordListener {

            override fun onStart() {
                showAttachmentIcon(false)
                showUserInputHint(false)
                if (::chatKitV4InputCallback.isInitialized)
                    chatKitV4InputCallback.onRecorderStart()
            }

            override fun onCancel() {
                showUserInputHint()
                showAttachmentIcon()
                if (::chatKitV4InputCallback.isInitialized)
                    chatKitV4InputCallback.onRecorderCancel()
            }

            override fun onFinish(recordTime: Long, limitReached: Boolean) {
                showAttachmentIcon()
                showUserInputHint()
                if (::chatKitV4InputCallback.isInitialized)
                    chatKitV4InputCallback.onRecorderFinish(recordTime, limitReached)
            }

            override fun onLessThanSecond() {
                showAttachmentIcon()
                showUserInputHint()
                if (::chatKitV4InputCallback.isInitialized)
                    chatKitV4InputCallback.onRecorderLessThanSecond()
            }
        })
        binding.recordView.setRecordPermissionHandler(object : RecordPermissionHandler {
            override fun isPermissionGranted(): Boolean {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    return true;
                }

                if (PermissionHelper.checkPermissionListIsGranted(
                        context,
                        listOf(PERMISSION_WRITE_EXTERNAL_STORAGE, PERMISSION_RECORDE_AUDIO)
                    )
                ) {
                    return true
                }

                if (::chatKitV4InputCallback.isInitialized)
                    chatKitV4InputCallback.onRecorderNeedPermission()
                return false
            }
        })
    }

    private fun initializeListeners() {
        binding.recordButton.setOnClickListener {
            if (::chatKitV4InputCallback.isInitialized) {
                val message = binding.etInputMessage.text.toString()
                if (!message.isNullOrEmpty())
                    chatKitV4InputCallback.onSubmitNewMessage(message)
            }
            binding.etInputMessage.text?.clear()
        }
        binding.ibAttachment.setOnClickListener {
            if (::chatKitV4InputCallback.isInitialized) {
                chatKitV4InputCallback.onAttachmentClicked()
                chatKitV4InputCallback.onAttachmentIconClicked()
            }
        }
        binding.etInputMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                if (text.toString().isNotEmpty()) {
                    showVoiceRecorderIcon(false)
                } else {
                    showVoiceRecorderIcon(true)
                }
            }
        })
    }

    private fun showUserInputHint(showUserInputHint: Boolean = true) {
        if (showUserInputHint) {
            binding.etInputMessage.hint = context.getString(R.string.title_chatkitv4_input_edittext_hint)
        } else {
            binding.etInputMessage.hint = null
        }
    }

    private fun showVoiceRecorderIcon(enableVoiceRecorder: Boolean = true) {
        if (enableVoiceRecorder && showVoiceRecorderIcon) {
            binding.recordButton.setImageResource(R.drawable.recv_ic_mic_white)
            binding.recordButton.isListenForRecord = true
        } else {
            binding.recordButton.setImageResource(R.drawable.ic_baseline_send_24)
            binding.recordButton.isListenForRecord = false
        }
    }

    fun setVoiceRecorderEnable(enableVoiceRecorder: Boolean) {
        this.showVoiceRecorderIcon = enableVoiceRecorder
        showVoiceRecorderIcon(enableVoiceRecorder)
    }

    fun blockChatKitV4Input() {
        binding.constraintLayoutUserBlocker.visibility = VISIBLE
    }

    fun unBlockChatKitV4Input() {
        binding.constraintLayoutUserBlocker.visibility = GONE
    }

    fun getRecordView(): RecordView {
        return binding.recordView
    }

    private fun showAttachmentIcon(visible: Boolean = true) {
        if (visible && showAttachmentIcon)
            binding.ibAttachment.visibility = VISIBLE
        else
            binding.ibAttachment.visibility = GONE
    }

    fun setAttachmentEnable(enableAttachment: Boolean) {
        this.showAttachmentIcon = enableAttachment
        showAttachmentIcon(enableAttachment)
    }

}