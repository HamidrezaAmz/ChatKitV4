package ir.vasl.chatkitv4core.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import ir.vasl.chatkitv4core.databinding.LayoutChatkitV4Binding
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.view.interfaces.ChatKitV4InputCallback
import ir.vasl.chatkitv4core.view.interfaces.ChatKitV4ListCallback
import ir.vasl.chatkitv4core.viewmodel.ChatKitV4ViewModel

class ChatKitV4 @kotlin.jvm.JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {

    private var binding = LayoutChatkitV4Binding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
        initializeChatKitV4()
        initializeViews()
    }

    private fun initializeChatKitV4() {
    }

    private fun initializeViews() {
    }

    suspend fun initializeChatKitV4ViewModel(inputChatKitV4ViewModel: ChatKitV4ViewModel) {
        binding.chatKitV4List.initializeChatKitV4ListViewModel(inputChatKitV4ViewModel)
    }

    fun initializeChatKitV4ListCallback(chatKitV4ListCallback: ChatKitV4ListCallback) {
        binding.chatKitV4List.initializeChatKitV4ListCallback(chatKitV4ListCallback)
    }

    fun initializeChatKitV4InputCallback(chatKitV4InputCallback: ChatKitV4InputCallback) {
        binding.chatKitV4Input.initializeChatKitV4InputCallback(chatKitV4InputCallback)
    }

    fun addNewMessageIntoChatKitV4(messageModel: MessageModel) {
        binding.chatKitV4List.addMessageModelIntoDatabase(messageModel)
    }

    fun addNewMessageListIntoChatKitV4(messageModelList: List<MessageModel>) {
        binding.chatKitV4List.addMessageModelListIntoDatabase(messageModelList)
    }

    fun clearAll() {
        binding.chatKitV4List.clearAll()
    }

    fun moveToStartOfChatKitV4(delayTime: Long = 500) {
        binding.chatKitV4List.moveToStartOfChatKitV4(delayTime)
    }

    fun moveToEndOfChatKitV4(delayTime: Long = 500) {
        binding.chatKitV4List.moveToEndOfChatKitV4(delayTime)
    }

    fun goToNextPage() {

    }

    fun goToPreviousPage() {

    }

    fun showHintView() {

    }

    fun hideHintView() {

    }

    fun blockChatKitV4Input() {
        binding.chatKitV4Input.blockChatKitV4Input()
    }

    fun unBlockChatKitV4Input() {
        binding.chatKitV4Input.unBlockChatKitV4Input()
    }

}