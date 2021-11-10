package ir.vasl.chatkitv4core.view.interfaces

import ir.vasl.chatkitv4core.model.MessageModel

public interface ChatKitV4ListCallback {
    fun onMessageClicked(messageModel: MessageModel?) {}
    fun onMessagePressed(messageModel: MessageModel?) {}
    fun onCopyMessageClicked() {}
    fun onResendMessageClicked() {}
    fun onDeleteMessageClicked() {}
    fun onSwipeRefresh() {}
    fun onRecordStarted() {}
    fun onRecordStopped() {}
    fun onRecordEnded() {}
    fun onFileClicked() {}
    fun onExtraOptionClicked() {}
    fun onReachedToEnd() {}
}