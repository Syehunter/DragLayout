package z.sye.space.library.interfaces;

/**
 * Created by Syehunter on 16/1/26.
 */
public interface DragItemChangeListener {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemRemoved(int position);
}
