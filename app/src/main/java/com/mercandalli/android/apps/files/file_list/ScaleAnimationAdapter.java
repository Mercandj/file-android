package com.mercandalli.android.apps.files.file_list;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * {@link RecyclerView} adapter with animation.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScaleAnimationAdapter extends RecyclerView.Adapter<ViewHolder> implements
        Animator.AnimatorListener {

    /**
     * The {@link RecyclerView.Adapter} with animated {@link ViewHolder}.
     */
    @NonNull
    private final RecyclerView.Adapter mAdapter;

    /**
     * The {@link RecyclerView} with animated {@link ViewHolder}.
     */
    @NonNull
    private final RecyclerView mRecyclerView;

    /**
     * The animation duration for each {@link RecyclerView}.
     */
    private int mDuration = 220;

    /**
     * The animation offset duration between each {@link RecyclerView}.
     */
    private int mOffsetDuration = 50;

    /**
     * The animation {@link Interpolator}.
     */
    @NonNull
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    /**
     * The number of {@link ViewHolder} items per line.
     */
    private int mViewHolderPerLine;

    private int mLastPosition = -1;
    private boolean mIsFirstOnly = false;
    private int mCounter;
    private boolean mAnimsInitialized;
    private final float mFrom;

    @Nullable
    private NoAnimatedPosition mNoAnimatedPosition;

    public ScaleAnimationAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        this(recyclerView, adapter, 1, 0.0F);
    }

    public ScaleAnimationAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter, int cardPerLine) {
        this(recyclerView, adapter, cardPerLine, 0.0F);
    }

    public ScaleAnimationAdapter(
            @NonNull final RecyclerView recyclerView,
            @NonNull final RecyclerView.Adapter adapter,
            int cardPerLine,
            float from) {
        setHasStableIds(true);
        mRecyclerView = recyclerView;
        mAdapter = adapter;
        mViewHolderPerLine = cardPerLine;
        mFrom = from;
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mLastPosition = -1;
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                notifyItemRangeChanged(positionStart, itemCount, payload);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                notifyItemRangeRemoved(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return this.mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        this.mAdapter.onBindViewHolder(holder, position);
        if (this.mIsFirstOnly && position <= this.mLastPosition) {
            clear(holder.itemView);
        } else if (mNoAnimatedPosition == null || mNoAnimatedPosition.isAnimatedItem(position)) {
            final Animator[] animators = this.getAnimators(holder.itemView);
            if (!mAnimsInitialized) {
                for (Animator anim : animators) {
                    anim.setDuration((long) this.mDuration);
                    anim.setInterpolator(this.mInterpolator);
                    increaseCounter();

                    if (mViewHolderPerLine > 1 && position >= mViewHolderPerLine) {
                        anim.setStartDelay(mOffsetDuration * (position - mViewHolderPerLine / 2));
                    } else {
                        anim.setStartDelay(mOffsetDuration * position);
                    }
                    anim.addListener(this);
                    anim.start();
                }
            } else {
                holder.itemView.setScaleX(1);
                holder.itemView.setScaleY(1);
            }

            this.mLastPosition = position;
        } else {
            holder.itemView.setScaleX(1);
            holder.itemView.setScaleY(1);
            this.mLastPosition = position;
        }
    }

    @Override
    public long getItemId(int position) {
        return mAdapter.getItemId(position);
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (decreaseCounter()) {
            mAnimsInitialized = true;
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    public synchronized int getCounter() {
        return mCounter;
    }

    public synchronized void increaseCounter() {
        mCounter++;
    }

    public int getItemCount() {
        return this.mAdapter.getItemCount();
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public void setOffsetDuration(int mOffsetDuration) {
        this.mOffsetDuration = mOffsetDuration;
    }

    public void setInterpolator(@NonNull final Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    public void setStartPosition(int start) {
        this.mLastPosition = start;
    }

    private RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public int getItemViewType(int position) {
        return this.mAdapter.getItemViewType(position);
    }

    private synchronized boolean decreaseCounter() {
        mCounter--;
        return mCounter == 0;
    }

    private Animator[] getAnimators(View view) {
        view.setScaleX(0);
        view.setScaleY(0);
        return new Animator[]{ObjectAnimator.ofFloat(view, "scaleX", this.mFrom, 1.0F), ObjectAnimator.ofFloat(view, "scaleY", this.mFrom, 1.0F)};
    }

    private static void clear(@NonNull final View v) {
        ViewCompat.setAlpha(v, 1.0F);
        ViewCompat.setScaleY(v, 1.0F);
        ViewCompat.setScaleX(v, 1.0F);
        v.setPivotY((float) (v.getMeasuredHeight() / 2));
        ViewCompat.setPivotX(v, (float) (v.getMeasuredWidth() / 2));
        ViewCompat.animate(v).setInterpolator(null);
    }

    public void setNoAnimatedPosition(@Nullable final NoAnimatedPosition noAnimatedPosition) {
        mNoAnimatedPosition = noAnimatedPosition;
    }

    public void reset() {
        mLastPosition = -1;
        mAnimsInitialized = false;
    }

    public interface NoAnimatedPosition {

        /**
         * The the item not animated.
         *
         * @param position The position to check.
         * @return True if the item is animated.
         */
        boolean isAnimatedItem(int position);
    }
}
