package ir.vasl.chatkitv4core.repository.local.converter

import androidx.room.TypeConverter
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageOwnerType

class MessageTypeConverter {

    @TypeConverter
    fun toMessageType(value: String) = enumValueOf<MessageOwnerType>(value)

    @TypeConverter
    fun fromMessageType(value: MessageOwnerType) = value.name

}