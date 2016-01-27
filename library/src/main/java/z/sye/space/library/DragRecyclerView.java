package z.sye.space.library;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import z.sye.space.library.adapters.BaseDragAdapter;
import z.sye.space.library.adapters.DragAdapter;
import z.sye.space.library.interfaces.DragItemStartListener;
import z.sye.space.library.interfaces.DragItemTouchHelperCallBack;
import z.sye.space.library.interfaces.OnItemClickListener;
import z.sye.space.library.interfaces.OnLongPressListener;

/**
 * Created by Syehunter on 16/1/26.
 */
public class DragRecyclerView extends RecyclerView implements DragItemStartListener {

    private BaseDragAdapter mAdapter;
    private DragGridLayoutManager mManager;
    private List mDatas;
    private ItemTouchHelper mItemTouchHelper;
    private OnItemClickListener mOnItemClickListener;
    private OnLongPressListener mOnLongPressListener;
    private int mKeepItemCount = 1;

    public DragRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DragRecyclerView adapter(BaseDragAdapter adapter) {
        mAdapter = adapter;
        return this;
    }

    public DragRecyclerView grid(DragGridLayoutManager manager) {
        mManager = manager;
        return this;
    }

    public DragRecyclerView datas(List datas) {
        if (null == mDatas) mDatas = new ArrayList();
        mDatas.clear();
        mDatas.addAll(datas);
        return this;
    }

    public DragRecyclerView onItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        return this;
    }

    public DragRecyclerView onLongPressListener(OnLongPressListener onLongPressListener) {
        mOnLongPressListener = onLongPressListener;
        return this;
    }

    public DragRecyclerView keepItemCount(int keepItemCount) {
        mKeepItemCount = keepItemCount;
        return this;
    }

    public void build() {
        if (null == mAdapter) {
            mAdapter = new DragAdapter(this);
        }
        this.setAdapter(mAdapter);

        if (null == mManager) {
            mManager = new DragGridLayoutManager(getContext(), 4);
        }
        this.setLayoutManager(mManager);

        if (null != mDatas) {
            mAdapter.setDatas(mDatas);
        }

        if (null != mOnItemClickListener) {
            mAdapter.setOnItemClickListener(mOnItemClickListener);
        }

        if (null != mOnLongPressListener) {
            mAdapter.setOnLongPressListener(mOnLongPressListener);
        }

        mAdapter.setKeepItemCount(mKeepItemCount);

        DragItemTouchHelperCallBack callBack = new DragItemTouchHelperCallBack(mAdapter, mKeepItemCount);
        mItemTouchHelper = new ItemTouchHelper(callBack);
        mItemTouchHelper.attachToRecyclerView(this);
    }

    @Override
    public void onDragStart(ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    public void quitLongPressMode() {
        if (null != mAdapter) {
            mAdapter.quitLongPressMode();
        }
    }

}
