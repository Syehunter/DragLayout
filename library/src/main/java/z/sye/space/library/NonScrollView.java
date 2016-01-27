package z.sye.space.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Syehunter on 16/1/27.
 */
public class NonScrollView extends ScrollView {

    private int downX, downY;

    public NonScrollView(Context context) {
        super(context);
    }

    public NonScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > 10) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }
}
