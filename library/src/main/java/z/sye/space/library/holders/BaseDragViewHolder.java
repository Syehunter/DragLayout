package z.sye.space.library.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Syehunter on 16/1/26.
 */
public abstract class BaseDragViewHolder extends RecyclerView.ViewHolder {

    public BaseDragViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * Make your actions with itemView when the item is dragging
     */
    public abstract void onDrag();

    /**
     * Recover the itemView when the dragging finished
     */
    public abstract void onDragFinished();

    public abstract void onLongPressMode();

    public abstract void onNormalMode();
}
