package ir.vasl.chatkitv4core.view.adapter.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import ir.vasl.chatkitv4core.R

class ViewHolderNotSupported(view: View) : RecyclerView.ViewHolder(view) {

    var textviewTitle: AppCompatTextView? = view.findViewById(R.id.tv_title) as AppCompatTextView?

}