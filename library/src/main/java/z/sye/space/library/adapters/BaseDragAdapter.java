package z.sye.space.library.adapters;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import z.sye.space.library.holders.BaseDragViewHolder;
import z.sye.space.library.interfaces.DragItemStartListener;
import z.sye.space.library.interfaces.OnItemClickListener;
import z.sye.space.library.interfaces.OnItemRemovedListener;
import z.sye.space.library.interfaces.OnLongPressListener;

/**
 * T: Generics of Data List
 * <p/>
 * Created by Syehunter on 16/1/26.
 */
public abstract class BaseDragAdapter<T, VH extends BaseDragViewHolder> extends BaseRecyclerAdapter<T, VH> {

    public static final int KEEP = Integer.MIN_VALUE;

    /**
     * Tag to judge current item mode
     */
    private boolean isLongPressMode = false;
    /**
     * Item Count that can not change
     */
    private int mKeepItemCount = 1;

    private DragItemStartListener mDragItemStartListener;
    private OnItemClickListener mOnItemClickListener;
    /**
     * Listener for users to do with views outside RecyclerView when enter LongPress Mode
     */
    private OnLongPressListener mOnLongPressListener;
    private OnItemRemovedListener<T> mOnItemRemovedListener;

    public BaseDragAdapter(DragItemStartListener listener) {
        mDragItemStartListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position <= mKeepItemCount - 1) {
            return KEEP;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onViewHolderBind(holder, holder.getAdapterPosition());

        if (holder.getItemViewType() != KEEP) {
            //change Background for different state
            if (isLongPressMode) {
                holder.onLongPressMode();
            } else {
                holder.onNormalMode();
            }
        }

        holder.itemView.setOnTouchListener(
                new OnItemTouchListener(holder, mDragItemStartListener));
    }

    protected abstract void onViewHolderBind(VH holder, int position);

    @Override
    public void onItemRemoved(int position) {
        if (null != mOnItemRemovedListener) {
            mOnItemRemovedListener.onItemRemoved(position, mDatas.get(position));
        }
        mDatas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDatas.size());
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        moveDataInList(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    private void moveDataInList(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }
        T item = mDatas.get(fromPosition);
        mDatas.remove(item);
        mDatas.add(toPosition, item);
    }

    @Override
    public void onItemInsert(int position, T data) {
        //auto put the new item at the end
        mDatas.add(data);
        notifyItemInserted(mDatas.size() - 1);
        notifyItemRangeChanged(mDatas.size() - 1, mDatas.size());
    }

    public void setKeepItemCount(int keepItemCount) {
        mKeepItemCount = keepItemCount;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnLongPressListener(OnLongPressListener onLongPressListener) {
        mOnLongPressListener = onLongPressListener;
    }

    public void setOnItemRemovedListener(OnItemRemovedListener<T> onItemRemovedListener) {
        mOnItemRemovedListener = onItemRemovedListener;
    }

    /**
     * Change LongPressMode to Normal
     */
    public void quitLongPressMode() {
        if (isLongPressMode) {
            isLongPressMode = false;
            notifyDataSetChanged();
        }
    }

    /**
     * @return if current mode is LongPressMode or not
     */
    public boolean getLongPressMode() {
        return isLongPressMode;
    }

    private class OnItemTouchListener implements View.OnTouchListener {

        /**
         * Distance to judge whether ACTION_MOVE act on or not
         */
        private float mDownY;
        private float mDownX;
        private boolean isMoved = false;
        private BaseDragViewHolder mViewHolder;
        private DragItemStartListener mDragItemStartListener;

        private Runnable mLongPressRunnable = new Runnable() {
            @Override
            public void run() {
                isLongPressMode = true;
                notifyDataSetChanged();
                mDragItemStartListener.onDragStart(mViewHolder);
                if (null != mOnLongPressListener) {
                    mOnLongPressListener.onLongPress();
                }
            }
        };

        public OnItemTouchListener(BaseDragViewHolder holder, DragItemStartListener listener) {
            mViewHolder = holder;
            mDragItemStartListener = listener;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (isLongPressMode) {
                        //if current item state is longPressMode, start dragging immediately
                        mDragItemStartListener.onDragStart(mViewHolder);
                    } else {
                        mDownX = event.getX();
                        mDownY = event.getY();
                        isMoved = false;
                        v.postDelayed(mLongPressRunnable, ViewConfiguration.getLongPressTimeout());
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isLongPressMode) {
                        if (isMoved) break;
                        if (Math.abs(event.getX() - mDownX) > 5 ||
                                Math.abs(event.getY() - mDownY) > 5) {
                            isMoved = true;
                            v.removeCallbacks(mLongPressRunnable);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (!isLongPressMode) {
                        v.removeCallbacks(mLongPressRunnable);
                        if (null != mOnItemClickListener) {
                            mOnItemClickListener.onItemClick(mViewHolder, mViewHolder.getAdapterPosition());
                        }
                    }
                    break;
            }
            return true;
        }
    }
}
