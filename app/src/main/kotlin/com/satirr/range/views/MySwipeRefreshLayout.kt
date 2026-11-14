package com.satirr.range.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.color.MaterialColors
import com.qtalk.recyclerviewfastscroller.RecyclerViewFastScroller
import com.satirr.commons.extensions.getProperBackgroundColor
import com.satirr.commons.extensions.getProperPrimaryColor
import com.satirr.commons.extensions.isDynamicTheme

class MySwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : SwipeRefreshLayout(context, attrs) {

    init {
        updateColors()
    }

    fun updateColors() {
        val primaryColor = context.getProperPrimaryColor()
        val surfaceColor = if (context.isDynamicTheme()) {
            MaterialColors.getColor(
                context,
                com.google.android.material.R.attr.colorSurfaceContainerHigh,
                context.getProperBackgroundColor()
            )
        } else {
            context.getProperBackgroundColor()
        }
        setColorSchemeColors(primaryColor)
        setProgressBackgroundColorSchemeColor(surfaceColor)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (!isEnabled) {
            return false
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun canChildScrollUp(): Boolean {
        val directChild = getChildAt(0)
        return when (directChild) {
            is RecyclerViewFastScroller -> {
                val innerRecyclerView = directChild.getChildAt(0)
                innerRecyclerView?.canScrollVertically(-1) == true
            }
            else -> super.canChildScrollUp()
        }
    }
}
