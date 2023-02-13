package com.jjcr11.memorygame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.Layout
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class ScoreItemTouchHelper(
    private val list: MutableList<Score>,
    private val adapter: ScoreAdapter,
    private val context: Context,
    private val layout: View
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.RIGHT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = true

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val score = list[position]
        list.removeAt(position)
        adapter.notifyItemRemoved(position)

        Snackbar.make(layout, "Deleted", Snackbar.LENGTH_SHORT)
            .setAction("Undo") {
                adapter.restoreScore(position, score)
            }
            .show()
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return super.getSwipeEscapeVelocity(defaultValue * 5)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val density = context.resources.displayMetrics.density
        val width = context.resources.displayMetrics.widthPixels
        val height = context.resources.getDimension(R.dimen.item_score_height)

        val position = viewHolder.adapterPosition

        val topRect = if(position == 0) {
            0f
        } else {
            (position + 1) * height
        }

        val paint = Paint()
        paint.color = ContextCompat.getColor(context, R.color.imperial_red)
        c.drawRect(0f, topRect, width.toFloat(), height, paint)


        val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete)

        val top = 30 * density
        val grow = 15 * density
        val left = 10 * density

        deleteIcon?.bounds = Rect(
            left.toInt() + 1,
            viewHolder.itemView.top + top.toInt(),
            deleteIcon!!.intrinsicWidth + left.toInt() + grow.toInt(),
            viewHolder.itemView.top + deleteIcon.intrinsicHeight + top.toInt() + grow.toInt()
        )

        deleteIcon.draw(c)

        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX / 4,
            dY,
            actionState,
            isCurrentlyActive
        )
    }
}