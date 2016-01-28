package z.sye.space.library.interfaces;

/**
 * Created by Syehunter on 16/1/26.
 */
public interface OnItemChangeListener<T> {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemRemoved(int position);

    void onItemInsert(int position, T data);
}
