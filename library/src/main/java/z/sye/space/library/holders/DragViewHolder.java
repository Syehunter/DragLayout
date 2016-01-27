package z.sye.space.library.holders;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
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
    public FrameLayout mFrameLayout;

    public DragViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mCardView = (CardView) itemView.findViewById(R.id.cardView_item);
        mFrameLayout = (FrameLayout) itemView.findViewById(R.id.fl_item);
        mTextView = (TextView) itemView.findViewById(R.id.tv_item);
        mDelete = (ImageView) itemView.findViewById(R.id.iv_item);
        mDelete.setVisibility(View.GONE);
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
    public void onLongPressMode() {
        mDelete.setVisibility(View.VISIBLE);
        Drawable longPressDrawable = mContext.getResources().getDrawable(R.drawable.border_longpress);
        if (Build.VERSION.SDK_INT >= 21) {
            mCardView.setBackgroundDrawable(longPressDrawable);
        } else {
            mFrameLayout.setBackgroundDrawable(longPressDrawable);
        }
    }

    @Override
    public void onNormalMode() {
        mDelete.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= 21) {
            mCardView.setBackgroundColor(Color.WHITE);
        } else {
            mFrameLayout.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.border_normal));
        }
    }
}
