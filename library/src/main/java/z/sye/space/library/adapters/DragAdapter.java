package z.sye.space.library.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import z.sye.space.library.R;
import z.sye.space.library.holders.DragViewHolder;
import z.sye.space.library.interfaces.DragItemStartListener;

/**
 * Created by Syehunter on 16/1/26.
 */
public class DragAdapter extends BaseDragAdapter<String, DragViewHolder> {

    public DragAdapter(DragItemStartListener listener) {
        super(listener);
    }

    @Override
    public DragViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drag, parent, false);
        return new DragViewHolder(itemView);
    }

    @Override
    protected void onViewHolderBind(final DragViewHolder holder, int position) {
        holder.mTextView.setText(mItemList.get(position));
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemRemoved(holder.getAdapterPosition());
            }
        });
    }
}
