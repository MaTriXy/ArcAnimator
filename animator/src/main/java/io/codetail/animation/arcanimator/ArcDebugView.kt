package io.codetail.animation.arcanimator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View

class ArcDebugView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var mArcMetric: ArcMetric? = null

    private val mPaintFill = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintStroke = Paint(Paint.ANTI_ALIAS_FLAG)

    fun drawArcAnimator(arcAnimator: ArcAnimator) {
        mArcMetric = arcAnimator.mArcMetric
        invalidate()
    }

    init {
        mPaintStroke.style = Paint.Style.STROKE
        mPaintStroke.strokeWidth = dpToPx(1).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mArcMetric != null) {
            drawElements(canvas)
        }
    }

    private fun drawElements(canvas: Canvas) {
        drawLines(canvas)
        drawCircles(canvas)
        drawPoints(canvas)
    }

    private fun drawPoints(canvas: Canvas) {
        mPaintFill.color = STARTPOINT
        canvas.drawCircle(mArcMetric!!.mStartPoint.x,
                mArcMetric!!.mStartPoint.y, dpToPx(2).toFloat(), mPaintFill)
        mPaintFill.color = ENDPOINT
        canvas.drawCircle(mArcMetric!!.mEndPoint.x,
                mArcMetric!!.mEndPoint.y, dpToPx(2).toFloat(), mPaintFill)
        mPaintFill.color = MIDPOINT
        canvas.drawCircle(mArcMetric!!.mMidPoint.x,
                mArcMetric!!.mMidPoint.y, dpToPx(2).toFloat(), mPaintFill)
        mPaintFill.color = AXISPOINT
        canvas.drawCircle(mArcMetric!!.mAxisPoint[Side.RIGHT.value]!!.x,
                mArcMetric!!.mAxisPoint[Side.RIGHT.value]!!.y,
                dpToPx(3).toFloat(), mPaintFill)
        mPaintFill.color = AXISPOINT1
        canvas.drawCircle(mArcMetric!!.mAxisPoint[Side.LEFT.value]!!.x,
                mArcMetric!!.mAxisPoint[Side.LEFT.value]!!.y,
                dpToPx(3).toFloat(), mPaintFill)
        mPaintFill.color = ABSOLUTEPOINT
        canvas.drawCircle(mArcMetric!!.mZeroPoint.x,
                mArcMetric!!.mZeroPoint.y, dpToPx(2).toFloat(), mPaintFill)
    }

    private fun drawLines(canvas: Canvas) {
        mPaintStroke.color = PURPLE
        canvas.drawLine(mArcMetric!!.mStartPoint.x, mArcMetric!!.mStartPoint.y,
                mArcMetric!!.mEndPoint.x, mArcMetric!!.mEndPoint.y, mPaintStroke)
        canvas.drawLine(mArcMetric!!.mStartPoint.x, mArcMetric!!.mStartPoint.y,
                mArcMetric!!.mAxisPoint[mArcMetric!!.mSide.value]!!.x,
                mArcMetric!!.mAxisPoint[mArcMetric!!.mSide.value]!!.y, mPaintStroke)
        canvas.drawLine(mArcMetric!!.mEndPoint.x, mArcMetric!!.mEndPoint.y,
                mArcMetric!!.mAxisPoint[mArcMetric!!.mSide.value]!!.x,
                mArcMetric!!.mAxisPoint[mArcMetric!!.mSide.value]!!.y, mPaintStroke)
    }

    private fun drawCircles(canvas: Canvas) {
        mPaintStroke.color = BLUE
        canvas.drawCircle(mArcMetric!!.mStartPoint.x,
                mArcMetric!!.mStartPoint.y, mArcMetric!!.mRadius, mPaintStroke)
        canvas.drawCircle(mArcMetric!!.mEndPoint.x,
                mArcMetric!!.mEndPoint.y, mArcMetric!!.mRadius, mPaintStroke)
        mPaintStroke.color = LIGHTGREEN
        canvas.drawCircle(mArcMetric!!.mAxisPoint[mArcMetric!!.mSide.value]!!.x,
                mArcMetric!!.mAxisPoint[mArcMetric!!.mSide.value]!!.y,
                mArcMetric!!.mRadius, mPaintStroke)
    }

    fun dpToPx(dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    companion object {

        private val BLUE = -0xde690d
        private val PURPLE = -0x63d850
        private val LIGHTGREEN = -0x9b22e9

        private val STARTPOINT = -0xbbcca// red
        private val ENDPOINT = -0x6800//orange
        private val MIDPOINT = -0x86aab8//brown
        private val AXISPOINT = -0xb350b0//green
        private val AXISPOINT1 = -0x63d850//purple
        private val ABSOLUTEPOINT = -0xbdbdbe//grey
    }
}