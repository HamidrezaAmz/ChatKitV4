package ir.vasl.chatkitv4core.util.player.model

data class MediaPlayerInfo(
    var currMessageModelId: Int? = -1,
    var currLocalFileAddress: String? = null,
    var currDuration: String? = null,
    var currProgress: Int? = 0,
    var currPlayerState: String? = null
) {

    fun refresh() {
        currMessageModelId = -1
        currLocalFileAddress = null
        currDuration = null
        currProgress = 0
        currPlayerState = null
    }
}
