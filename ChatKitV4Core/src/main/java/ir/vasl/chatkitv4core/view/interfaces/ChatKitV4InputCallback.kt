package ir.vasl.chatkitv4core.view.interfaces

public interface ChatKitV4InputCallback {
    fun onAttachmentClicked() {}
    fun onSubmitNewMessage(message: String) {}
    fun onRecorderStart() {}
    fun onRecorderCancel() {}
    fun onRecorderFinish(recordTime: Long, limitReached: Boolean) {}
    fun onRecorderLessThanSecond() {}
    fun onRecorderNeedPermission() {}
}