package z.sye.space.library.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import z.sye.space.library.holders.BaseDragViewHolder;
import z.sye.space.library.interfaces.DragItemStartListener;
import z.sye.space.library.interfaces.DragItemChangeListener;
import z.sye.space.library.interfaces.OnItemClickListener;

/**
 * T: Generics of Data List
 * <p/>
 * Created by Syehunter on 16/1/26.
 */
public abstract class BaseDragAdapter<T, VH extends BaseDragViewHolder> extends RecyclerView.Adapter<VH>
        implements DragItemChangeListener {

    /**
     * Tag to judge current item state
     */
    private boolean isLongPressState = false;

    protected List<T> mItemList = new ArrayList<>();

    private DragItemStartListener mDragItemStartListener;

    private OnItemClickListener mOnItemClickListener;

    public BaseDragAdapter(DragItemStartListener listener) {
        mDragItemStartListener = listener;
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        //change Background for different state
        if (isLongPressState) {
            holder.onLongPressState();
        } else {
            holder.onNormalState();
        }
        holder.itemView.setOnTouchListener(
                new OnItemTouchListener(holder, mDragItemStartListener));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemClickListener) {
                    Log.d(this.toString(), "[CurrentPosition]: " + holder.getAdapterPosition());
                    if (!isLongPressState) {
                        mOnItemClickListener.onItemClick(holder, holder.getAdapterPosition());
                    }
                }
            }
        });
        onViewHolderBind(holder, position);
    }

    protected abstract void onViewHolderBind(VH holder, int position);

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public void onItemRemoved(int position) {
        mItemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mItemList.size());
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItemList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public void setItemList(List<T> itemList) {
        mItemList.clear();
        mItemList.addAll(itemList);
        notifyDataSetChanged();
    }

    public List<T> getItemList() {
        return mItemList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * Change LongPressState to Normal
     */
    public void quitLongPressState() {
        if (isLongPressState) {
            isLongPressState = false;
            notifyDataSetChanged();
        }
    }

    private class OnItemTouchListener implements View.OnTouchListener {

        /**
         * Distance to judge whether ACTION_MOVE act on or not
         */
        private static final int TOUCH_SLOP = 15;
        private float mDownY;
        private float mDownX;
        private boolean isMoved = false;
        private BaseDragViewHolder mViewHolder;
        private DragItemStartListener mDragItemStartListener;

        private Runnable mLongPressRunnable = new Runnable() {
            @Override
            public void run() {
                isLongPressState = true;
                notifyDataSetChanged();
                mDragItemStartListener.onDragStart(mViewHolder);
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
                    if (isLongPressState) {
                        //if current item state is longPressState, start dragging immediately
                        mDragItemStartListener.onDragStart(mViewHolder);
                    } else {
                        mDownX = event.getX();
                        mDownY = event.getY();
                        isMoved = false;
                        v.postDelayed(mLongPressRunnable, ViewConfiguration.getLongPressTimeout());
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isLongPressState) {
                        if (isMoved) break;
                        if (Math.abs(event.getX() - mDownX) > TOUCH_SLOP ||
                                Math.abs(event.getY() - mDownY) > TOUCH_SLOP) {
                            isMoved = true;
                            v.removeCallbacks(mLongPressRunnable);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (!isLongPressState)
                        v.removeCallbacks(mLongPressRunnable);
                    break;
            }
            return false;
        }
    }
}
