package z.sye.space.library;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.List;

import z.sye.space.library.adapters.BaseRecyclerAdapter;
import z.sye.space.library.adapters.BaseUnsignedAdapter;
import z.sye.space.library.adapters.UnsignedAdapter;
import z.sye.space.library.interfaces.OnItemRemovedListener;

/**
 * Created by Syehunter on 16/1/28.
 */
public class UnsignedRecyclerView extends RecyclerView {

    private BaseUnsignedAdapter mAdapter;
    private DragGridLayoutManager mManager;
    private List mDatas;
    private OnItemRemovedListener mOnItemRemovedListener;

    private boolean isHanZi = false;

    public UnsignedRecyclerView(Context context) {
        super(context);
    }

    public UnsignedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UnsignedRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public UnsignedRecyclerView adapter(BaseUnsignedAdapter adapter) {
        mAdapter = adapter;
        return this;
    }

    public UnsignedRecyclerView manager(DragGridLayoutManager manager) {
        mManager = manager;
        return this;
    }

    public UnsignedRecyclerView datas(List datas) {
        isHanZi = false;
        mDatas = datas;
        return this;
    }

    public UnsignedRecyclerView hanZiDatas(List datas) {
        isHanZi = true;
        mDatas = datas;
        return this;
    }

    public UnsignedRecyclerView onItemRemoved(OnItemRemovedListener listener) {
        mOnItemRemovedListener = listener;
        return this;
    }

    public void build() {
        if (null == mAdapter) {
            mAdapter = new UnsignedAdapter();
        }
        this.setAdapter(mAdapter);

        if (null == mManager) {
            mManager = new DragGridLayoutManager(getContext(), 4);
        }
        this.setLayoutManager(mManager);

        if (null != mDatas) {
            if (isHanZi && mAdapter instanceof UnsignedAdapter)
                ((UnsignedAdapter) mAdapter).setHanZiDatas(mDatas);
            else
                mAdapter.setDatas(mDatas);
        }

        if (null != mOnItemRemovedListener) {
            mAdapter.setOnItemRemovedListener(mOnItemRemovedListener);
        }
    }

    public void addItem(Object data) {
        if (null != mAdapter) {
            if (mAdapter instanceof UnsignedAdapter && data instanceof String) {
                ((UnsignedAdapter) mAdapter).addHanZiItem((String) data);
            } else {
                mAdapter.addItem(data);
            }
        }
    }

    public void addItem(int insertPosition, Object data) {
        mAdapter.addItem(insertPosition, data);
    }

    public List getDatas() {
        if (getAdapter() instanceof BaseRecyclerAdapter) {
            return ((BaseRecyclerAdapter) getAdapter()).getDatas();
        }
        return mDatas;
    }
}
