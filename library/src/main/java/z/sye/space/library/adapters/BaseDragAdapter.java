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
import z.sye.space.library.interfaces.OnLongPressListener;

/**
 * T: Generics of Data List
 * <p/>
 * Created by Syehunter on 16/1/26.
 */
public abstract class BaseDragAdapter<T, VH extends BaseDragViewHolder> extends RecyclerView.Adapter<VH>
        implements DragItemChangeListener {

    public static final int KEEP = Integer.MIN_VALUE;

    /**
     * Tag to judge current item mode
     */
    private boolean isLongPressMode = false;

    /**
     * Item Count that can not change
     */
    private int mKeepItemCount = 1;

    protected List<T> mDatas = new ArrayList<>();

    private DragItemStartListener mDragItemStartListener;

    private OnItemClickListener mOnItemClickListener;
    /**
     * Listener for users to do with sth. outside recyclerview when enter LongPress Mode
     */
    private OnLongPressListener mOnLongPressListener;

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
    public void onBindViewHolder(final VH holder, int position) {
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemClickListener) {
                    Log.d(this.toString(), "[CurrentPosition]: " + holder.getAdapterPosition());
                    if (!isLongPressMode) {
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
        return mDatas.size();
    }

    @Override
    public void onItemRemoved(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
        Log.d(this.toString(), "[mDatas's Size]: " + mDatas.size());
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mDatas, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public void setDatas(List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void setKeepItemCount(int keepItemCount) {
        mKeepItemCount = keepItemCount;
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnLongPressListener(OnLongPressListener onLongPressListener) {
        mOnLongPressListener = onLongPressListener;
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
                        v.postDelayed(mLongPressRunnable, 800);
                        Log.d(this.toString(), "Runnable Post!");
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isLongPressMode) {
                        if (isMoved) break;
                        float distanceX = event.getX() - mDownX;
                        float distanceY = event.getY() - mDownY;
                        Log.d(this.toString(), "[Distance]: [" + distanceX + ", " + distanceY + "]");
                        if (Math.abs(event.getX() - mDownX) > 0.5 ||
                                Math.abs(event.getY() - mDownY) > 0.5) {
                            isMoved = true;
                            v.removeCallbacks(mLongPressRunnable);
                            Log.d(this.toString(), "Runnable Removed by Move!");
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d(this.toString(), "Action Up Run");
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
