package ir.vasl.chatkitv4core.util.downloadmanager.model

data class DownloadManagerInfo(
    var currMessageModelId: Int? = -1,
    var currLocalFileAddress: String? = null,
    var currProgress: Int? = null,
) {

}