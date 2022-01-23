package ir.vasl.chatkitv4core.repository.local.messages

import androidx.paging.PagingSource
import androidx.room.*
import ir.vasl.chatkitv4core.model.MessageModel

@Dao
interface MessageDao {

    @Query("SELECT * FROM table_messages WHERE chatId =:chatId ORDER BY createdAt ASC")
    fun getMessageList(chatId: String): PagingSource<Int, MessageModel>

    @Query("SELECT * FROM table_messages WHERE chatId =:chatId AND id =:messageId")
    fun getMessageByMessageId(chatId: String, messageId: Int): MessageModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movieEntity: MessageModel)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(movieEntityList: List<MessageModel>)

    @Delete
    suspend fun clear(movieEntity: MessageModel)

    @Query("DELETE FROM table_messages")
    fun clearAll()

    @Update
    fun update(movieEntity: MessageModel)

    @Query("UPDATE table_messages SET messageConditionStatus=:conditionStatus WHERE id = :messageId")
    fun update(messageId: String, conditionStatus: String)

}