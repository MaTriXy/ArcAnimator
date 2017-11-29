package io.codetail.animation.arcanimator

import android.graphics.PointF
import java.util.*

internal class ArcMetric {

    var mStartPoint = PointF()

    var mEndPoint = PointF()

    var mMidPoint = PointF()

    var mAxisPoint = arrayOfNulls<PointF>(2)

    var mZeroPoint = PointF()

    //SEGMENTS. This Segments create virtual triangle except mZeroStartSegment

    var mStartEndSegment: Float = 0.toFloat()

    var mRadius: Float = 0.toFloat()

    var mMidAxisSegment: Float = 0.toFloat()

    var mZeroStartSegment: Float = 0.toFloat()

    //DEGREES.

    var mAnimationDegree: Float = 0.toFloat()

    var mSideDegree: Float = 0.toFloat()

    var mZeroStartDegree: Float = 0.toFloat()

    /**
     * Return evaluated start degree
     *
     * @return the start degree
     */
    var startDegree: Float = 0.toFloat()

    /**
     * Return evaluated end degree
     *
     * @return the end degree
     */
    var endDegree: Float = 0.toFloat()

    //Side of animation
    lateinit var mSide: Side

    val axisPoint: PointF
        get() = mAxisPoint[mSide.value] ?: PointF(0.0f, 0.0f)

    private fun createAxisVariables() {
        for (i in mAxisPoint.indices)
            mAxisPoint[i] = PointF()
    }


    private fun calcStartEndSeg() {
        mStartEndSegment = Math.sqrt(Math.pow((mStartPoint.x - mEndPoint.x).toDouble(), 2.0) + Math.pow((mStartPoint.y - mEndPoint.y).toDouble(), 2.0)).toFloat()

    }

    private fun calcRadius() {
        mSideDegree = (180 - mAnimationDegree) / 2
        mRadius = mStartEndSegment / Utils.sin(mAnimationDegree.toDouble()) * Utils.sin(mSideDegree.toDouble())
    }

    private fun calcMidAxisSeg() {
        mMidAxisSegment = mRadius * Utils.sin(mSideDegree.toDouble())
    }

    private fun calcMidPoint() {
        mMidPoint.x = mStartPoint.x + mStartEndSegment / 2 * (mEndPoint.x - mStartPoint.x) / mStartEndSegment
        mMidPoint.y = mStartPoint.y + mStartEndSegment / 2 * (mEndPoint.y - mStartPoint.y) / mStartEndSegment
    }

    private fun calcAxisPoints() {
        if (mStartPoint.y > mEndPoint.y || mStartPoint.y == mEndPoint.y) {
            mAxisPoint[0]!!.x = mMidPoint.x + mMidAxisSegment * (mEndPoint.y - mStartPoint.y) / mStartEndSegment
            mAxisPoint[0]!!.y = mMidPoint.y - mMidAxisSegment * (mEndPoint.x - mStartPoint.x) / mStartEndSegment

            mAxisPoint[1]!!.x = mMidPoint.x - mMidAxisSegment * (mEndPoint.y - mStartPoint.y) / mStartEndSegment
            mAxisPoint[1]!!.y = mMidPoint.y + mMidAxisSegment * (mEndPoint.x - mStartPoint.x) / mStartEndSegment
        } else {
            mAxisPoint[0]!!.x = mMidPoint.x - mMidAxisSegment * (mEndPoint.y - mStartPoint.y) / mStartEndSegment
            mAxisPoint[0]!!.y = mMidPoint.y + mMidAxisSegment * (mEndPoint.x - mStartPoint.x) / mStartEndSegment

            mAxisPoint[1]!!.x = mMidPoint.x + mMidAxisSegment * (mEndPoint.y - mStartPoint.y) / mStartEndSegment
            mAxisPoint[1]!!.y = mMidPoint.y - mMidAxisSegment * (mEndPoint.x - mStartPoint.x) / mStartEndSegment
        }
    }

    private fun calcZeroPoint() {
        when (mSide) {
            Side.RIGHT -> {
                mZeroPoint.x = mAxisPoint[Side.RIGHT.value]!!.x + mRadius
                mZeroPoint.y = mAxisPoint[Side.RIGHT.value]!!.y
            }
            Side.LEFT -> {
                mZeroPoint.x = mAxisPoint[Side.LEFT.value]!!.x - mRadius
                mZeroPoint.y = mAxisPoint[Side.LEFT.value]!!.y
            }
        }
    }

    private fun calcDegrees() {
        mZeroStartSegment = Math.sqrt(Math.pow((mZeroPoint.x - mStartPoint.x).toDouble(), 2.0) + Math.pow((mZeroPoint.y - mStartPoint.y).toDouble(), 2.0)).toFloat()
        mZeroStartDegree = Utils.acos((2 * Math.pow(mRadius.toDouble(), 2.0) - Math.pow(mZeroStartSegment.toDouble(), 2.0)) / (2 * Math.pow(mRadius.toDouble(), 2.0)))
        when (mSide) {
            Side.RIGHT -> if (mStartPoint.y <= mZeroPoint.y) {
                if (mStartPoint.y > mEndPoint.y || mStartPoint.y == mEndPoint.y && mStartPoint.x > mEndPoint.x) {
                    startDegree = mZeroStartDegree
                    endDegree = startDegree + mAnimationDegree
                } else {
                    startDegree = mZeroStartDegree
                    endDegree = startDegree - mAnimationDegree
                }
            } else if (mStartPoint.y >= mZeroPoint.y) {
                if (mStartPoint.y < mEndPoint.y || mStartPoint.y == mEndPoint.y && mStartPoint.x > mEndPoint.x) {
                    startDegree = 0 - mZeroStartDegree
                    endDegree = startDegree - mAnimationDegree
                } else {
                    startDegree = 0 - mZeroStartDegree
                    endDegree = startDegree + mAnimationDegree
                }
            }
            Side.LEFT -> if (mStartPoint.y <= mZeroPoint.y) {
                if (mStartPoint.y > mEndPoint.y || mStartPoint.y == mEndPoint.y && mStartPoint.x < mEndPoint.x) {
                    startDegree = 180 - mZeroStartDegree
                    endDegree = startDegree - mAnimationDegree
                } else {
                    startDegree = 180 - mZeroStartDegree
                    endDegree = startDegree + mAnimationDegree
                }
            } else if (mStartPoint.y >= mZeroPoint.y) {
                if (mStartPoint.y < mEndPoint.y || mStartPoint.y == mEndPoint.y && mStartPoint.x < mEndPoint.x) {
                    startDegree = 180 + mZeroStartDegree
                    endDegree = startDegree + mAnimationDegree
                } else {
                    startDegree = 180 + mZeroStartDegree
                    endDegree = startDegree - mAnimationDegree
                }
            }
        }
    }

    fun setDegree(degree: Float) {
        var degree = degree
        degree = Math.abs(degree)
        if (degree > 180)
            setDegree(degree % 180)
        else if (degree == 180f)
            setDegree(degree - 1)
        else if (degree < 30)
            setDegree(30f)
        else
            this.mAnimationDegree = degree
    }

    override fun toString(): String {
        return "ArcMetric{" +
                "\nmStartPoint=" + mStartPoint +
                "\n mEndPoint=" + mEndPoint +
                "\n mMidPoint=" + mMidPoint +
                "\n mAxisPoint=" + Arrays.toString(mAxisPoint) +
                "\n mZeroPoint=" + mZeroPoint +
                "\n mStartEndSegment=" + mStartEndSegment +
                "\n mRadius=" + mRadius +
                "\n mMidAxisSegment=" + mMidAxisSegment +
                "\n mZeroStartSegment=" + mZeroStartSegment +
                "\n mAnimationDegree=" + mAnimationDegree +
                "\n mSideDegree=" + mSideDegree +
                "\n mZeroStartDegree=" + mZeroStartDegree +
                "\n mStartDegree=" + startDegree +
                "\n mEndDegree=" + endDegree +
                "\n mSide=" + mSide +
                '}'
    }

    companion object {

        /**
         * Create new [ArcMetric] instance and do all calculations below
         * and finally return ready to use object
         */
        fun evaluate(startX: Float, startY: Float,
                     endX: Float, endY: Float,
                     degree: Float, side: Side): ArcMetric {
            //TODO return ready to use object with have done computations
            val arcMetric = ArcMetric()
            arcMetric.mStartPoint.set(startX, startY)
            arcMetric.mEndPoint.set(endX, endY)
            arcMetric.setDegree(degree)
            arcMetric.mSide = side
            arcMetric.createAxisVariables()

            arcMetric.calcStartEndSeg()
            arcMetric.calcRadius()
            arcMetric.calcMidAxisSeg()
            arcMetric.calcMidPoint()
            arcMetric.calcAxisPoints()
            arcMetric.calcZeroPoint()
            arcMetric.calcDegrees()

            return arcMetric
        }
    }
}
