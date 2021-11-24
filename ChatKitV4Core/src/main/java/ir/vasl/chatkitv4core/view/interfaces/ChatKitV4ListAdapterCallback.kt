package ir.vasl.chatkitv4core.view.interfaces

import ir.vasl.chatkitv4core.model.MessageModel

public interface ChatKitV4ListAdapterCallback {
    fun onMessageClicked(messageModel: MessageModel?) {}
    fun onMessagePressed(messageModel: MessageModel?) {}
    fun onPlayPauseClicked(messageModel: MessageModel?) {}
    fun onFileClicked(messageModel: MessageModel?) {}
    fun onDownloadFileClicked(messageModel: MessageModel?) {}
}