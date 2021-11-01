package ir.vasl.chatkitv4core.view.adapter.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import ir.vasl.chatkitv4core.R
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.view.interfaces.ChatKitV4ListCallback

class ViewHolderSelfTextMessage(
    private var view: View,
    private var chatKitV4ListCallback: ChatKitV4ListCallback? = null
) : RecyclerView.ViewHolder(view) {

    var textviewTitle = view.findViewById(R.id.tv_title) as AppCompatTextView?
    var textviewSubTitle = view.findViewById(R.id.tv_sub_title) as AppCompatTextView?
    var textviewDate = view.findViewById(R.id.tv_date) as AppCompatTextView?

    fun bind(messageModel: MessageModel?) {

        val title = messageModel?.title
        val subTitle = messageModel?.subTitle
        val date = messageModel?.date

        // set title
        if (title.isNullOrEmpty())
            textviewTitle?.visibility = View.GONE
        else {
            textviewTitle?.visibility = View.VISIBLE
            textviewTitle?.text = messageModel.title
        }

        // set sub title
        if (subTitle.isNullOrEmpty())
            textviewSubTitle?.visibility = View.GONE
        else {
            textviewSubTitle?.visibility = View.VISIBLE
            textviewSubTitle?.text = messageModel.subTitle
        }

        // set date
        if (date.isNullOrEmpty())
            textviewDate?.visibility = View.GONE
        else {
            textviewDate?.visibility = View.VISIBLE
            textviewDate?.text = messageModel.date
        }

        // set click listener
        itemView.setOnClickListener {
            chatKitV4ListCallback?.onMessageClicked(messageModel)
        }
        itemView.setOnLongClickListener {
            chatKitV4ListCallback?.onMessagePressed(messageModel)
            true
        }

    }

}