package z.sye.space.library.adapters;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import z.sye.space.library.interfaces.OnItemChangeListener;

/**
 * Created by Syehunter on 16/1/28.
 */
public abstract class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements OnItemChangeListener<T> {

    protected List<T> mDatas = new ArrayList<>();

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setDatas(List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mDatas;
    }
}
