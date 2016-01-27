package z.sye.space.library;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.recyclerview.BuildConfig;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Override GirdLayoutManager for setting Width or Height to wrap_content
 * Doesn't suit for using SpanSizeLook
 * Reference to [https://github.com/serso/android-linear-layout-manager] with some modifications
 *
 * Created by Syehunter on 16/1/26.
 */
public class DragGridLayoutManager extends GridLayoutManager {

    private static boolean canMakeInsetsDirty = true;
    private static Field insetsDirtyField = null;

    private static final int CHILD_WIDTH = 0;
    private static final int CHILD_HEIGHT = 1;
    private static final int DEFAULT_CHILD_SIZE = 100;

    private final int[] childDimensions = new int[2];
    private final RecyclerView view = null;

    private int childSize = DEFAULT_CHILD_SIZE;
    private int overScrollMode = ViewCompat.OVER_SCROLL_ALWAYS;
    private final Rect tmpRect = new Rect();

    public DragGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public DragGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public DragGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                          int widthSpec, int heightSpec) {
        final int widthMode = View.MeasureSpec.getMode(widthSpec);
        final int heightMode = View.MeasureSpec.getMode(heightSpec);

        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        final int heightSize = View.MeasureSpec.getSize(heightSpec);

        final boolean hasWidthSize = widthMode != View.MeasureSpec.UNSPECIFIED;
        final boolean hasHeightSize = heightMode != View.MeasureSpec.UNSPECIFIED;

        final boolean exactWidth = widthMode == View.MeasureSpec.EXACTLY;
        final boolean exactHeight = heightMode == View.MeasureSpec.EXACTLY;

        final int unspecified = makeUnspecifiedSpec();

        if (exactWidth && exactHeight) {
            // in case of exact calculations for both dimensions let's use default "onMeasure" implementation
            super.onMeasure(recycler, state, widthSpec, heightSpec);
            return;
        }

        final boolean vertical = getOrientation() == VERTICAL;

        initChildDimensions(widthSize, heightSize, vertical);

        int width = 0;
        int height = 0;

        // it's possible to get scrap views in recycler which are bound to old (invalid) adapter entities. This
        // happens because their invalidation happens after "onMeasure" method. As a workaround let's clear the
        // recycler now (it should not cause any performance issues while scrolling as "onMeasure" is never
        // called whiles scrolling)
        recycler.clear();

        final int stateItemCount = state.getItemCount();
        final int adapterItemCount = getItemCount();
        // adapter always contains actual data while state might contain old data (f.e. data before the animation is
        // done). As we want to measure the view with actual data we must use data from the adapter and not from  the
        // state
        for (int i = 0; i < adapterItemCount; i++) {
            if (vertical) {
                if (i < stateItemCount) {
                    // we should not exceed state count, otherwise we'll get IndexOutOfBoundsException. For such items
                    // we will use previously calculated dimensions
                    measureChild(recycler, i, widthSize, unspecified, childDimensions);
                } else {
                    logMeasureWarning(i);
                }
                if (i % getSpanCount() == 0) {
                    height += childDimensions[CHILD_HEIGHT];
                }
                if (i == 0) {
                    width = childDimensions[CHILD_WIDTH];
                }
                if (hasHeightSize && height >= heightSize) {
                    break;
                }
            } else {
                if (i < stateItemCount) {
                    // we should not exceed state count, otherwise we'll get IndexOutOfBoundsException. For such items
                    // we will use previously calculated dimensions
                    measureChild(recycler, i, unspecified, heightSize, childDimensions);
                } else {
                    logMeasureWarning(i);
                }
                if (i % getSpanCount() == 0) {
                    width += childDimensions[CHILD_WIDTH];
                }
                if (i == 0) {
                    height = childDimensions[CHILD_HEIGHT];
                }
                if (hasWidthSize && width >= widthSize) {
                    break;
                }
            }
        }

        if (exactWidth) {
            width = widthSize;
        } else {
            width += getPaddingLeft() + getPaddingRight();
            if (hasWidthSize) {
                width = Math.min(width, widthSize);
            }
        }

        if (exactHeight) {
            height = heightSize;
        } else {
            height += getPaddingTop() + getPaddingBottom();
            if (hasHeightSize) {
                height = Math.min(height, heightSize);
            }
        }

        setMeasuredDimension(width, height);

        if (view != null && overScrollMode == ViewCompat.OVER_SCROLL_IF_CONTENT_SCROLLS) {
            final boolean fit = (vertical && (!hasHeightSize || height < heightSize))
                    || (!vertical && (!hasWidthSize || width < widthSize));

            ViewCompat.setOverScrollMode(view, fit ? ViewCompat.OVER_SCROLL_NEVER : ViewCompat.OVER_SCROLL_ALWAYS);
        }
    }

    public static int makeUnspecifiedSpec() {
        return View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    }

    private void initChildDimensions(int width, int height, boolean vertical) {
        if (childDimensions[CHILD_WIDTH] != 0 || childDimensions[CHILD_HEIGHT] != 0) {
            // already initialized, skipping
            return;
        }
        if (vertical) {
            childDimensions[CHILD_WIDTH] = width;
            childDimensions[CHILD_HEIGHT] = childSize;
        } else {
            childDimensions[CHILD_WIDTH] = childSize;
            childDimensions[CHILD_HEIGHT] = height;
        }
    }

    private void measureChild(RecyclerView.Recycler recycler, int position, int widthSize, int heightSize, int[] dimensions) {
        final View child;
        try {
            child = recycler.getViewForPosition(position);
        } catch (IndexOutOfBoundsException e) {
            if (BuildConfig.DEBUG) {
                Log.w("LinearLayoutManager", "LinearLayoutManager doesn't work well with animations. Consider switching them off", e);
            }
            return;
        }

        final RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) child.getLayoutParams();

        final int hPadding = getPaddingLeft() + getPaddingRight();
        final int vPadding = getPaddingTop() + getPaddingBottom();

        final int hMargin = p.leftMargin + p.rightMargin;
        final int vMargin = p.topMargin + p.bottomMargin;

        // we must make insets dirty in order calculateItemDecorationsForChild to work
        makeInsetsDirty(p);
        // this method should be called before any getXxxDecorationXxx() methods
        calculateItemDecorationsForChild(child, tmpRect);

        final int hDecoration = getRightDecorationWidth(child) + getLeftDecorationWidth(child);
        final int vDecoration = getTopDecorationHeight(child) + getBottomDecorationHeight(child);

        final int childWidthSpec = getChildMeasureSpec(widthSize, hPadding + hMargin + hDecoration, p.width, canScrollHorizontally());
        final int childHeightSpec = getChildMeasureSpec(heightSize, vPadding + vMargin + vDecoration, p.height, canScrollVertically());

        child.measure(childWidthSpec, childHeightSpec);

        dimensions[CHILD_WIDTH] = getDecoratedMeasuredWidth(child) + p.leftMargin + p.rightMargin;
        dimensions[CHILD_HEIGHT] = getDecoratedMeasuredHeight(child) + p.bottomMargin + p.topMargin;

        // as view is recycled let's not keep old measured values
        makeInsetsDirty(p);
        recycler.recycleView(child);
    }

    private static void makeInsetsDirty(RecyclerView.LayoutParams p) {
        if (!canMakeInsetsDirty) {
            return;
        }
        try {
            if (insetsDirtyField == null) {
                insetsDirtyField = RecyclerView.LayoutParams.class.getDeclaredField("mInsetsDirty");
                insetsDirtyField.setAccessible(true);
            }
            insetsDirtyField.set(p, true);
        } catch (NoSuchFieldException e) {
            onMakeInsertDirtyFailed();
        } catch (IllegalAccessException e) {
            onMakeInsertDirtyFailed();
        }
    }

    private static void onMakeInsertDirtyFailed() {
        canMakeInsetsDirty = false;
        if (BuildConfig.DEBUG) {
            Log.w("LinearLayoutManager", "Can't make LayoutParams insets dirty, decorations measurements might be incorrect");
        }
    }

    private void logMeasureWarning(int child) {
        if (BuildConfig.DEBUG) {
            Log.w("LinearLayoutManager", "Can't measure child #" + child + ", previously used dimensions will be reused." +
                    "To remove this message either use #setChildSize() method or don't run RecyclerView animations");
        }
    }

    @Override
    public void onItemsRemoved(RecyclerView recyclerView, int positionStart, int itemCount) {
        super.onItemsRemoved(recyclerView, positionStart, itemCount);
    }
}
