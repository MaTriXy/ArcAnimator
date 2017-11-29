package io.codetail.animation.arcanimator

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Interpolator

import java.lang.ref.WeakReference

class ArcAnimator private constructor(internal var mArcMetric: ArcMetric, target: View) : Animator() {
    internal var mTarget: WeakReference<View>
    internal var mAnimator: WeakReference<ValueAnimator>
    internal var mValue: Float = 0.toFloat()

    internal var degree: Float
        get() = mValue
        set(degree) {
            mValue = degree
            val clipView = mTarget.get()
            val x = mArcMetric.axisPoint.x +
                    mArcMetric.mRadius * Utils.cos(degree.toDouble())
            val y = mArcMetric.axisPoint.y -
                    mArcMetric.mRadius * Utils.sin(degree.toDouble())
            if (clipView != null) {
                clipView.x = x - (clipView.width / 2)
                clipView.y = y - (clipView.height / 2)
            }
        }


    init {
        mTarget = WeakReference(target)

        mAnimator = WeakReference(
                ValueAnimator.ofFloat(mArcMetric.startDegree, mArcMetric.endDegree)
        )
        mAnimator.get()?.addUpdateListener(
                { animation -> degree = animation.animatedValue as Float })
    }

    override fun getStartDelay(): Long {
        val a = mAnimator.get()
        return a?.duration ?: 0
    }

    override fun setStartDelay(startDelay: Long) {
        val a = mAnimator.get()
        if (a != null) a.startDelay = startDelay
    }

    override fun setDuration(duration: Long): ArcAnimator {
        val a = mAnimator.get()
        if (a != null) a.duration = duration
        return this
    }

    override fun getDuration(): Long {
        val a = mAnimator.get()
        return a?.duration ?: 0
    }

    override fun setInterpolator(timeInterpolator: TimeInterpolator) {
        val a = mAnimator.get()
        if (a != null) a.interpolator = timeInterpolator
    }

    fun setInterpolator(value: Interpolator) {
        val a = mAnimator.get()
        if (a != null) a.interpolator = value
    }

    override fun start() {
        super.start()
        val a = mAnimator.get()
        a?.start()
    }

    override fun end() {
        super.end()
        val a = mAnimator.get()
        a?.end()
    }

    override fun cancel() {
        super.cancel()
        val a = mAnimator.get()
        a?.cancel()
    }

    override fun addListener(listener: Animator.AnimatorListener) {
        val a = mAnimator.get()
        a?.addListener(listener)
    }

    override fun setupEndValues() {
        super.setupEndValues()
        val a = mAnimator.get()
        a?.setupEndValues()
    }

    override fun setupStartValues() {
        super.setupStartValues()
        val a = mAnimator.get()
        a?.setupStartValues()
    }

    override fun isRunning(): Boolean {
        val a = mAnimator.get()
        return a != null && a.isRunning
    }

    override fun toString(): String {
        return mArcMetric.toString()
    }

    companion object {

        fun createArcAnimator(clipView: View, nestView: View,
                              degree: Float, side: Side): ArcAnimator {

            return createArcAnimator(clipView, Utils.centerX(nestView), Utils.centerY(nestView),
                    degree, side)
        }

        fun createArcAnimator(clipView: View, endX: Float, endY: Float,
                              degree: Float, side: Side): ArcAnimator {

            val arcMetric = ArcMetric.evaluate(Utils.centerX(clipView), Utils.centerY(clipView),
                    endX, endY, degree, side)
            return ArcAnimator(arcMetric, clipView)
        }
    }
}
