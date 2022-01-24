package ir.vasl.chatkitv4core.view.adapter.viewholder

import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import ir.vasl.chatkitv4core.R
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageConditionStatus
import ir.vasl.chatkitv4core.util.helper.FileHelper
import ir.vasl.chatkitv4core.util.player.interfaces.MediaHelperCallback
import ir.vasl.chatkitv4core.view.interfaces.ChatKitV4ListAdapterCallback

class ViewHolderAlphaDocumentMessage(
    private var view: View,
    private var chatKitV4ListAdapterCallback: ChatKitV4ListAdapterCallback? = null
) : RecyclerView.ViewHolder(view), MediaHelperCallback {

    private var messageModel: MessageModel? = null
    private var tvTitle = view.findViewById(R.id.tv_title) as AppCompatTextView?
    private var tvSubTitle = view.findViewById(R.id.tv_sub_title) as AppCompatTextView?
    private var tvDate = view.findViewById(R.id.tv_date) as AppCompatTextView?
    private var progressBar = view.findViewById(R.id.progressBar) as ProgressBar?

    fun bind(messageModel: MessageModel?) {
        this.messageModel = messageModel

        val title = messageModel?.title
        val fileName = messageModel?.fileName
        val fileSize = messageModel?.fileSize
        val date = messageModel?.date

        // set title
        if (fileName.isNullOrEmpty()) {
            tvTitle?.visibility = View.GONE
        } else {
            tvTitle?.visibility = View.VISIBLE
            tvTitle?.text = fileName
        }

        // set sub title
        if (fileSize.isNullOrEmpty()) {
            tvSubTitle?.visibility = View.GONE
        } else {
            tvSubTitle?.visibility = View.VISIBLE
            tvSubTitle?.text = fileSize
        }

        // set date
        if (date.isNullOrEmpty()) {
            tvDate?.visibility = View.GONE
        } else {
            tvDate?.visibility = View.VISIBLE
            tvDate?.text = date
        }

        // set-sync message condition status
        syncMessageConditionWithUI(messageModel)

        // set click listener
        itemView.setOnClickListener {
            // callback click
            chatKitV4ListAdapterCallback?.onMessageClicked(messageModel)

            when {
                letsUpload(messageModel) -> {
                    messageModel?.messageConditionStatus = MessageConditionStatus.UPLOAD_IN_PROGRESS.name
                    chatKitV4ListAdapterCallback?.onUploadFileClicked(messageModel)
                }
                letsDownload(messageModel) -> {
                    messageModel?.messageConditionStatus = MessageConditionStatus.DOWNLOAD_STARTED.name
                    chatKitV4ListAdapterCallback?.onDownloadFileClicked(messageModel)
                }
                letsPreview(messageModel) -> {
                    messageModel?.messageConditionStatus = MessageConditionStatus.IDLE.name
                    chatKitV4ListAdapterCallback?.onPreviewFileClicked(messageModel)
                }
                letsDownloadAgain(messageModel) -> {
                    messageModel?.messageConditionStatus = MessageConditionStatus.DOWNLOAD_STARTED.name
                    chatKitV4ListAdapterCallback?.onDownloadFileClicked(messageModel)
                }
            }

            syncMessageConditionWithUI(messageModel)
            // save changes into database
            chatKitV4ListAdapterCallback?.onMessageModelChanged(messageModel)
        }
    }

    private fun syncMessageConditionWithUI(messageModel: MessageModel?) {
        when (messageModel?.messageConditionStatus ?: MessageConditionStatus.IDLE.name) {
            MessageConditionStatus.UNDEFINED.name -> {
                showUploadProgressBar(false)
            }
            MessageConditionStatus.IDLE.name -> {
                showUploadProgressBar(false)
            }

            MessageConditionStatus.UPLOAD_FAILED.name -> {
                showUploadProgressBar(false)
            }
            MessageConditionStatus.UPLOAD_IN_PROGRESS.name -> {
                showUploadProgressBar()
            }
            MessageConditionStatus.UPLOAD_STOPPED.name -> {
                showUploadProgressBar(false)
            }
            MessageConditionStatus.UPLOAD_SUCCEED.name -> {
                showUploadProgressBar(false)
            }

            MessageConditionStatus.DOWNLOAD_FAILED.name -> {
                showDownloadProgressBar(show = false)
            }
            MessageConditionStatus.DOWNLOAD_IN_PROGRESS.name -> {
                showDownloadProgressBar()
            }
            MessageConditionStatus.DOWNLOAD_STOPPED.name -> {
                showDownloadProgressBar(show = false)
            }
            MessageConditionStatus.DOWNLOAD_SUCCEED.name -> {
                showDownloadProgressBar(show = false)
            }
        }
    }

    private fun showUploadProgressBar(show: Boolean = true) {
        progressBar?.isIndeterminate = true
        if (show)
            progressBar?.visibility = View.VISIBLE
        else
            progressBar?.visibility = View.GONE
    }

    private fun showDownloadProgressBar(show: Boolean = true, progress: Int = 0) {
        progressBar?.isIndeterminate = false
        progressBar?.progress = progress
        if (show)
            progressBar?.visibility = View.VISIBLE
        else
            progressBar?.visibility = View.GONE
    }

    private fun letsUpload(messageModel: MessageModel?): Boolean {
        return (messageModel?.localFileAddress.isNullOrEmpty().not() && messageModel?.remoteFileUrl.isNullOrEmpty())
    }

    private fun letsDownload(messageModel: MessageModel?): Boolean {
        return (messageModel?.localFileAddress.isNullOrEmpty() && messageModel?.remoteFileUrl.isNullOrEmpty().not())
    }

    private fun letsPreview(messageModel: MessageModel?): Boolean {

        val localFileAddress = messageModel?.localFileAddress ?: ""

        val resultEmpty = (messageModel?.localFileAddress.isNullOrEmpty().not() && messageModel?.remoteFileUrl.isNullOrEmpty().not())
        val resultExist = FileHelper.isValidFile(localFileAddress)

        return resultEmpty && resultExist
    }

    private fun letsDownloadAgain(messageModel: MessageModel?): Boolean {
        return true
    }

}