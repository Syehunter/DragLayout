package z.sye.space.library.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import z.sye.space.library.interfaces.OnItemRemovedListener;

/**
 * Created by Syehunter on 16/1/28.
 */
public abstract class BaseUnsignedAdapter<T, VH extends RecyclerView.ViewHolder>
        extends BaseRecyclerAdapter<T, VH> {

    private OnItemRemovedListener mOnItemRemovedListener;

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        onViewHolderBind(holder, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemRemovedListener)
                    mOnItemRemovedListener.onItemRemoved(
                            holder.getAdapterPosition(),
                            mDatas.get(holder.getAdapterPosition()));
                onItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    protected abstract void onViewHolderBind(VH holder, int position);

    public void setOnItemRemovedListener(OnItemRemovedListener onItemRemovedListener) {
        mOnItemRemovedListener = onItemRemovedListener;
    }

    @Override
    public void onItemRemoved(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //never be used
        return false;
    }

    @Override
    public void onItemInsert(int position, T data) {
        mDatas.add(position, data);
        notifyItemInserted(position);
    }

    /**
     * Add item at the end
     * @param data
     */
    public void addItem(T data) {
        onItemInsert(mDatas.size(), data);
    }

    /**
     * Add item with position
     * @param insertPosition
     * @param data
     */
    public void addItem(int insertPosition, T data) {
        onItemInsert(insertPosition, data);
    }

}
