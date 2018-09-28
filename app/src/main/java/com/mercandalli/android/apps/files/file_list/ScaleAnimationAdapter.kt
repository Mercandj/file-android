package com.mercandalli.android.apps.files.file_list

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.os.Build
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator

/**
 * [RecyclerView] adapter with animation.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class ScaleAnimationAdapter @JvmOverloads constructor(
        /**
         * The [RecyclerView] with animated [ViewHolder].
         */
        private val recyclerView: RecyclerView,
        /**
         * The [RecyclerView.Adapter] with animated [ViewHolder].
         */
        private val mAdapter: RecyclerView.Adapter<ViewHolder>,
        /**
         * The number of [ViewHolder] items per line.
         */
        private val mViewHolderPerLine: Int = 1,
        private val mFrom: Float = 0.0f) : RecyclerView.Adapter<ViewHolder>(), Animator.AnimatorListener {

    /**
     * The animation duration for each [RecyclerView].
     */
    private var mDuration = 220

    /**
     * The animation offset duration between each [RecyclerView].
     */
    private var mOffsetDuration = 50

    /**
     * The animation [Interpolator].
     */
    private var mInterpolator: Interpolator = AccelerateDecelerateInterpolator()

    private var mLastPosition = -1
    private val mIsFirstOnly = false
    @get:Synchronized
    var counter: Int = 0
        private set
    private var mAnimsInitialized: Boolean = false

    private var mNoAnimatedPosition: NoAnimatedPosition? = null

    init {
        setHasStableIds(true)
        mAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                mLastPosition = -1
                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                notifyItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                notifyItemRangeChanged(positionStart, itemCount, payload)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                notifyItemRangeRemoved(positionStart, itemCount)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                notifyDataSetChanged()
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return this.mAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        this.mAdapter.onBindViewHolder(holder, position)
        if (this.mIsFirstOnly && position <= this.mLastPosition) {
            clear(holder.itemView)
        } else if (mNoAnimatedPosition == null || mNoAnimatedPosition!!.isAnimatedItem(position)) {
            val animators = this.getAnimators(holder.itemView)
            if (!mAnimsInitialized) {
                for (anim in animators) {
                    anim.duration = this.mDuration.toLong()
                    anim.interpolator = this.mInterpolator
                    increaseCounter()

                    if (mViewHolderPerLine > 1 && position >= mViewHolderPerLine) {
                        anim.startDelay = (mOffsetDuration * (position - mViewHolderPerLine / 2)).toLong()
                    } else {
                        anim.startDelay = (mOffsetDuration * position).toLong()
                    }
                    anim.addListener(this)
                    anim.start()
                }
            } else {
                holder.itemView.scaleX = 1f
                holder.itemView.scaleY = 1f
            }

            this.mLastPosition = position
        } else {
            holder.itemView.scaleX = 1f
            holder.itemView.scaleY = 1f
            this.mLastPosition = position
        }
    }

    override fun getItemId(position: Int): Long {
        return mAdapter.getItemId(position)
    }

    override fun onAnimationStart(animation: Animator) {

    }

    override fun onAnimationEnd(animation: Animator) {
        if (decreaseCounter()) {
            mAnimsInitialized = true
        }
    }

    override fun onAnimationCancel(animation: Animator) {

    }

    override fun onAnimationRepeat(animation: Animator) {

    }

    @Synchronized
    fun increaseCounter() {
        counter++
    }

    override fun getItemCount(): Int {
        return this.mAdapter.itemCount
    }

    fun setDuration(duration: Int) {
        this.mDuration = duration
    }

    fun setOffsetDuration(mOffsetDuration: Int) {
        this.mOffsetDuration = mOffsetDuration
    }

    fun setInterpolator(interpolator: Interpolator) {
        this.mInterpolator = interpolator
    }

    fun setStartPosition(start: Int) {
        this.mLastPosition = start
    }

    override fun getItemViewType(position: Int): Int {
        return this.mAdapter.getItemViewType(position)
    }

    @Synchronized
    private fun decreaseCounter(): Boolean {
        counter--
        return counter == 0
    }

    private fun getAnimators(view: View): Array<Animator> {
        view.scaleX = 0f
        view.scaleY = 0f
        return arrayOf(ObjectAnimator.ofFloat(view, "scaleX", this.mFrom, 1.0f), ObjectAnimator.ofFloat(view, "scaleY", this.mFrom, 1.0f))
    }

    private fun clear(v: View) {
        ViewCompat.setAlpha(v, 1.0f)
        ViewCompat.setScaleY(v, 1.0f)
        ViewCompat.setScaleX(v, 1.0f)
        v.pivotY = (v.measuredHeight / 2).toFloat()
        ViewCompat.setPivotX(v, (v.measuredWidth / 2).toFloat())
        ViewCompat.animate(v).interpolator = null
    }

    fun setNoAnimatedPosition(noAnimatedPosition: NoAnimatedPosition?) {
        mNoAnimatedPosition = noAnimatedPosition
    }

    fun reset() {
        mLastPosition = -1
        mAnimsInitialized = false
    }

    interface NoAnimatedPosition {

        /**
         * The the item not animated.
         *
         * @param position The position to check.
         * @return True if the item is animated.
         */
        fun isAnimatedItem(position: Int): Boolean
    }
}
