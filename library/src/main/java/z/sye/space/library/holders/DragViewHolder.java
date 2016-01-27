package z.sye.space.library.holders;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import z.sye.space.library.R;

/**
 * Created by Syehunter on 16/1/26.
 */
public class DragViewHolder extends BaseDragViewHolder {

    private Context mContext;
    public TextView mTextView;
    public CardView mCardView;
    public ImageView mDelete;

    public DragViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mCardView = (CardView) itemView.findViewById(R.id.cardView_item);
        mTextView = (TextView) itemView.findViewById(R.id.tv_item);
        mDelete = (ImageView) itemView.findViewById(R.id.iv_item);
    }

    @Override
    public void onDrag() {
        mTextView.setTextColor(Color.RED);
    }

    @Override
    public void onDragFinished() {
        mTextView.setTextColor(Color.BLACK);
    }

    @Override
    public void onLongPressState() {
        mDelete.setVisibility(View.VISIBLE);
        mCardView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.border_cardview));
    }

    @Override
    public void onNormalState() {
        mDelete.setVisibility(View.GONE);
        mCardView.setBackgroundColor(Color.WHITE);
    }
}
