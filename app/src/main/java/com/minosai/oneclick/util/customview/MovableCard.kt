package com.minosai.oneclick.util.customview

import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.cardview.widget.CardView
import android.util.AttributeSet

@CoordinatorLayout.DefaultBehavior(MoveUpwardBehavior::class)
class MovableCardView : CardView {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
}