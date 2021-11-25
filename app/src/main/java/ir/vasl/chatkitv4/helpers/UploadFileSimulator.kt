package ir.vasl.chatkitv4.helpers

import android.os.Handler
import android.os.Looper
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageConditionStatus
import ir.vasl.chatkitv4core.view.ChatKitV4

object UploadFileSimulator {

    fun startUploadFile(chatKitV4: ChatKitV4, messageModel: MessageModel) {
        Handler(Looper.getMainLooper()).postDelayed({
            messageModel.messageConditionStatus = MessageConditionStatus.UPLOAD_SUCCEED.name
            chatKitV4.updateMessageModel(messageModel)
        }, 3000)
    }

}