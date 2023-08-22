package com.android.mediproject.core.common.dialog

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable.INFINITE
import com.android.mediproject.core.common.R
import com.android.mediproject.core.common.uiutil.dpToPx

class ProgressIndicator(context: Context, textMessage: String?) : ConstraintLayout(context) {

    private val lottie = LottieAnimationView(context).apply {
        setAnimation(R.raw.bluedottedprogress)
        repeatCount = INFINITE
        id = R.id.progressBar
    }

    private val textView = TextView(context).also { textView ->
        textMessage?.apply {
            textView.text = this
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            textView.setTextColor(Color.DKGRAY)
        } ?: run { textView.visibility = GONE }

        textView.gravity = Gravity.CENTER_HORIZONTAL
        textView.id = R.id.progressText
    }

    init {
        background = ContextCompat.getDrawable(context, R.drawable.progress_background)

        addView(lottie, LayoutParams(LayoutParams.WRAP_CONTENT, dpToPx(context, 31)).apply {
            topToTop = LayoutParams.PARENT_ID
            startToStart = LayoutParams.PARENT_ID
            endToEnd = LayoutParams.PARENT_ID
        })
        addView(textView, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            topToBottom = lottie.id
            topMargin = dpToPx(context, 16)
            startToStart = LayoutParams.PARENT_ID
            endToEnd = LayoutParams.PARENT_ID
        })

        lottie.playAnimation()
    }
}