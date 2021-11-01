package ir.vasl.chatkitv4core.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.vasl.chatkitv4core.viewmodel.ChatKitV4ViewModel

class ChatKitV4ViewModelFactory(
    private val application: Application,
    private val chatId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ChatKitV4ViewModel::class.java)) {
            ChatKitV4ViewModel(application, chatId) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found!")
        }
    }

}