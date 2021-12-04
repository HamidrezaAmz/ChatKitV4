package ir.vasl.chatkitv4core.util.generator

import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageContentType
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageOwnerType
import ir.vasl.chatkitv4core.util.PublicValue

class SampleChatDataGenerator {

    companion object {

        fun refreshIncreasingId() {
            SampleRandomIdGenerator.refreshIncreasingId()
        }

        fun getMessageList(chatId: String): ArrayList<MessageModel> {
            val messageList = ArrayList<MessageModel>()
            for (i in 1..50) {
                messageList.add(getMessage(chatId, i))
            }
            return messageList
        }

        fun getMessage(
            chatId: String,
            order: Int = 0
        ): MessageModel {
            return MessageModel(
                id = getStrId(),
                createAt = System.currentTimeMillis(),
                title = getTitle(),
                subTitle = getSubTitle(),
                date = getDate(),
                messageOwnerType = if (order % 2 == 0) MessageOwnerType.ALPHA.name else MessageOwnerType.BETA.name,
                messageContentType = MessageContentType.TEXT.name,
                chatId = chatId
            )
        }

        fun getMessage(
            chatId: String,
            messageOwnerType: MessageOwnerType = MessageOwnerType.ALPHA,
            messageContentType: MessageContentType = MessageContentType.TEXT
        ): MessageModel {
            return MessageModel(
                id = getStrId(),
                createAt = System.currentTimeMillis(),
                title = getTitle(),
                subTitle = getSubTitle(),
                date = getDate(),
                messageOwnerType = messageOwnerType.name,
                messageContentType = messageContentType.name,
                remoteFileUrl = if (messageContentType == MessageContentType.VOICE) PublicValue.SAMPLE_URL_MP3 else "",
                chatId = chatId
            )
        }

        fun getMessageNotSupported(
            chatId: String,
            messageOwnerType: MessageOwnerType = MessageOwnerType.ALPHA,
            messageContentType: MessageContentType = MessageContentType.TEXT,
        ): MessageModel {
            return MessageModel(
                id = getStrId(),
                createAt = System.currentTimeMillis(),
                subTitle = "پیام دریافتی پشتیبانی نمی شود",
                date = getDate(),
                messageOwnerType = messageOwnerType.name,
                messageContentType = messageContentType.name,
                chatId = chatId
            )
        }

        private fun getTitle(): String {
            return SampleRandomTextGenerator.getRandomShortTextFa()
        }

        private fun getSubTitle(): String {
            return SampleRandomTextGenerator.getRandomLongTextFa()
        }

        private fun getId(): Int {
            return SampleRandomIdGenerator.getRandomIntId()
        }

        private fun getStrId(): String {
            return SampleRandomIdGenerator.getRandomStrId()
        }

        private fun getIncreasingId(): Int {
            return SampleRandomIdGenerator.getIncreasingId()
        }

        private fun getDate(): String {
            return "12:00 ق.ض"
        }

    }
}