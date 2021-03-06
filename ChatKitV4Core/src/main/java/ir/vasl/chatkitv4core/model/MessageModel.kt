package ir.vasl.chatkitv4core.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageConditionStatus
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageContentType
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageOwnerType
import ir.vasl.chatkitv4core.util.helper.DateTimeHelper

@Entity(tableName = "table_messages")
data class MessageModel(

    @PrimaryKey
    @ColumnInfo(name = "id") // this id used for local unique message items
    val id: String,

    @ColumnInfo(name = "createdAt")
    val createAt: Long,

    @ColumnInfo(name = "title")
    val title: String? = "",

    @ColumnInfo(name = "subTitle")
    val subTitle: String? = "",

    @ColumnInfo(name = "avatar")
    var avatar: String? = "",

    @ColumnInfo(name = "date")
    val date: String? = "",

    @ColumnInfo(name = "messageOwnerType")
    var messageOwnerType: String? = MessageOwnerType.ALPHA.name,

    @ColumnInfo(name = "messageContentType")
    val messageContentType: String? = MessageContentType.TEXT.name,

    @ColumnInfo(name = "remoteFileUrl")
    var remoteFileUrl: String? = "",

    @ColumnInfo(name = "localFileAddress")
    var localFileAddress: String? = "",

    @ColumnInfo(name = "chatId")
    val chatId: String,

    @ColumnInfo(name = "progressPlayer")
    var progressPlayer: Int? = 0,

    @ColumnInfo(name = "progressDownloader")
    var progressDownloader: Int? = 0,

    @ColumnInfo(name = "messageConditionStatus")
    var messageConditionStatus: String? = MessageConditionStatus.IDLE.name,

    @ColumnInfo(name = "fileName")
    var fileName: String? = "",

    @ColumnInfo(name = "fileSize")
    var fileSize: String? = "",

    @ColumnInfo(name = "userId") // this id used for checking message type based on user ids
    var userId: String? = "",

    @ColumnInfo(name = "serverMessageId") // this id used for storing server side ids
    var serverMessageId: String? = "",

    ) {

    fun getHumanReadableDate(): String {
        return DateTimeHelper.getDateCurrentTimeZone(createAt)
    }

}