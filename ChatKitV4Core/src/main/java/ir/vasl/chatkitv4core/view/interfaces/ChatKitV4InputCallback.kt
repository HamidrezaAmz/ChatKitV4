package ir.vasl.chatkitv4core.view.interfaces

interface ChatKitV4InputCallback {
    fun onSubmitNewMessage(message: String) {}
    fun onRecorderStart() {}
    fun onRecorderCancel() {}
    fun onRecorderFinish(recordTime: Long, limitReached: Boolean) {}
    fun onRecorderLessThanSecond() {}
    fun onRecorderNeedPermission() {}
    fun onAttachmentClicked() {}
    fun onAttachmentIconClicked() {}
}