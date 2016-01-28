package z.sye.space.library.adapters;

import android.os.Build;
import android.util.Log;
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
        int resource = 0;
        if (Build.VERSION.SDK_INT >= 21) {
            resource = R.layout.item_drag_up21;
        } else {
            resource = R.layout.item_drag;
        }
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);

        return new DragViewHolder(itemView);
    }

    @Override
    protected void onViewHolderBind(final DragViewHolder holder, int position) {
        holder.mTextView.setText(mDatas.get(holder.getAdapterPosition()));
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(this.toString(), "[CurrentRemovedPosition:] " + holder.getAdapterPosition());
                Log.d(this.toString(), "[CurrentRemovedItem:] " + mDatas.get(holder.getAdapterPosition()));
                onItemRemoved(holder.getAdapterPosition());
            }
        });
    }

}
