package ir.vasl.chatkitv4core.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageConditionStatus
import ir.vasl.chatkitv4core.repository.local.ChatKitV4Database
import ir.vasl.chatkitv4core.util.PublicValue.CHATKIR_V4_PAGE_SIZE
import kotlinx.coroutines.flow.Flow

class ChatKitV4Repository(
    private val context: Context,
    private val chatId: String
) {

    private var chatKitV4Database: ChatKitV4Database = ChatKitV4Database.getDatabase(context)

    fun getMessageListFromDatabase(): Flow<PagingData<MessageModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = CHATKIR_V4_PAGE_SIZE,
                maxSize = CHATKIR_V4_PAGE_SIZE + (CHATKIR_V4_PAGE_SIZE * 2),
                enablePlaceholders = false
            ),
            pagingSourceFactory = { chatKitV4Database.getMessageDao().getMessageList(chatId) }
        ).flow
    }

    fun getMessageListFromNetwork() {}

    fun getMessageListFromMediator() {}

    fun addMessageIntoDatabase(messageModel: MessageModel) {
        chatKitV4Database.getMessageDao().insert(messageModel)
    }

    fun addMessageListIntoDatabase(messageModelList: List<MessageModel>) {
        chatKitV4Database.getMessageDao().insertAll(messageModelList)
    }

    fun updateMessageModel(messageModel: MessageModel) {
        chatKitV4Database.getMessageDao().update(messageModel)
    }

    fun updateMessageCondition(messageId: String, messageConditionStatus: MessageConditionStatus) {
        chatKitV4Database.getMessageDao().update(messageId, messageConditionStatus.name)
    }

    fun clearAll() {
        chatKitV4Database.getMessageDao().clearAll()
        chatKitV4Database.getRemoteKeysDao().clearAll()
    }

}