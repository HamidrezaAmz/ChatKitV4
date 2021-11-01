package ir.vasl.chatkitv4core.view.interfaces

interface ChatKitV4InputCallback {
    fun onAttachmentClicked() {}
    fun onSubmitNewMessage(message: String) {}
}