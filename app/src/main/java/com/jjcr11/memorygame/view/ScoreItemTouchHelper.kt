package com.jjcr11.memorygame.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jjcr11.memorygame.R
import com.jjcr11.memorygame.model.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScoreItemTouchHelper(
    private val adapter: ScoreAdapter,
    private val context: Context,
    private val layout: View,
    private val lifecycleOwner: LifecycleOwner
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
        val score = adapter.deleteScore(position)
        lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            AppDatabase.getDatabase(context).dao().removeScore(score)
        }
        Snackbar.make(layout, "Deleted", Snackbar.LENGTH_SHORT)
            .setAction("Undo") {
                adapter.restoreScore(position, score)
                lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    AppDatabase.getDatabase(context).dao().addScore(score)
                }
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

        val paint = Paint()
        paint.color = ContextCompat.getColor(context, R.color.imperial_red)
        c.drawRect(
            0f,
            0f,
            width.toFloat(),
            height * (position + 1),
            paint
        )

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