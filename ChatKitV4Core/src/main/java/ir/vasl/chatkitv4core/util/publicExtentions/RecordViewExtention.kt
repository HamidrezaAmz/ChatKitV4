package ir.vasl.chatkitv4core.util.publicExtentions

import android.graphics.Typeface
import android.widget.TextView
import com.devlomi.record_view.RecordView

fun RecordView.setTypeface(typeface: Typeface?) {
    typeface?.let { customTypeface ->
        getSlideTextView().let { textView ->
            textView.typeface = customTypeface
            this.invalidate()
        }
        getTimerTextView().let { textView ->
            textView.typeface = customTypeface
            this.invalidate()
        }
    }
}

fun RecordView.getSlideTextView(): TextView {
    return RecordView::class.java.getDeclaredField("slideToCancel").let {
        it.isAccessible = true
        val value = it.get(this)
        return@let value as TextView
    }
}

fun RecordView.getTimerTextView(): TextView {
    return RecordView::class.java.getDeclaredField("counterTime").let {
        it.isAccessible = true
        val value = it.get(this)
        return@let value as TextView
    }
}
