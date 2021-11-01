package ir.vasl.chatkitv4core.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.repository.ChatKitV4Repository

class ChatKitV4ViewModel constructor(
    application: Application,
    chatId: String
) : ViewModel() {

    var repository: ChatKitV4Repository = ChatKitV4Repository(application, chatId)

    val messageListFromDatabase = repository.getMessageListFromDatabase().cachedIn(viewModelScope)

    fun addMessageIntoDatabase(messageModel: MessageModel) {
        repository.addMessageIntoDatabase(messageModel)
    }

    fun addMessageListIntoDatabase(messageModelList: List<MessageModel>) {
        repository.addMessageListIntoDatabase(messageModelList)
    }

    fun clearAll() {
        repository.clearAll()
    }

}