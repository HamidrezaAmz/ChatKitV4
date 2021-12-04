package ir.vasl.chatkitv4core.view.adapter.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import ir.vasl.chatkitv4core.R
import ir.vasl.chatkitv4core.model.MessageModel

class ViewHolderNotSupported(view: View) : RecyclerView.ViewHolder(view) {

    var textviewTitle: AppCompatTextView? = view.findViewById(R.id.tv_title) as AppCompatTextView?
    var textviewSubTitle: AppCompatTextView? = view.findViewById(R.id.tv_sub_title) as AppCompatTextView?

    fun bind(messageModel: MessageModel?) {
        val title = messageModel?.title
        val subTitle = messageModel?.subTitle

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

    }

}