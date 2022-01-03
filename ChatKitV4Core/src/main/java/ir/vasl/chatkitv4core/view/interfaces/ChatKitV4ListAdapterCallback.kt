package ir.vasl.chatkitv4core.view.interfaces

import ir.vasl.chatkitv4core.model.MessageModel

interface ChatKitV4ListAdapterCallback {
    fun onMessageClicked(messageModel: MessageModel?) {}
    fun onMessagePressed(messageModel: MessageModel?) {}
    fun onPlayPauseClicked(messageModel: MessageModel?) {}
    fun onFileClicked(messageModel: MessageModel?) {}
    fun onMessageModelChanged(messageModel: MessageModel?) {}
    fun onDownloadFileClicked(messageModel: MessageModel?) {}
    fun onUploadFileClicked(messageModel: MessageModel?) {}
    fun onPreviewFileClicked(messageModel: MessageModel?) {}
}