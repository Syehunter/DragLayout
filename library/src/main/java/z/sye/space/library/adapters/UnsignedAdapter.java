package z.sye.space.library.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import z.sye.space.library.R;
import z.sye.space.library.holders.DragViewHolder;
import z.sye.space.pinyin.PinyinUtils;

/**
 * Created by Syehunter on 16/1/28.
 */
public class UnsignedAdapter extends BaseUnsignedAdapter<String, DragViewHolder> {

    @Override
    protected void onViewHolderBind(DragViewHolder holder, int position) {
        holder.onNormalMode();
        holder.mTextView.setText(mDatas.get(holder.getAdapterPosition()));
    }

    @Override
    public DragViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resource;
        if (Build.VERSION.SDK_INT >= 21) {
            resource = R.layout.item_drag_up21;
        } else {
            resource = R.layout.item_drag;
        }
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new DragViewHolder(itemView);
    }

    /**
     * Add item, data should be 汉字(HanZi)
     *      This will sort by Pinyin
     * @param data
     */
    public void addHanZiItem(String data) {
        int insertPosition = getInsertPosition(mDatas, data);
        onItemInsert(insertPosition, data);
    }

    /**
     * Get the insertPosition, sort by 汉字(HanZi) 拼音(Pinyin)
     * @param datas
     * @param data
     * @return
     */
    private Integer getInsertPosition(List<String> datas, String data) {
        int insertPosition = datas.size();
        String pinyinData = PinyinUtils.hanziToPinyin(data);
        String pinyinTemp;
        HanZiComparator comparator = new HanZiComparator();
        for (int i = 0 ; i < datas.size() ; i++) {
            pinyinTemp = PinyinUtils.hanziToPinyin(datas.get(i));
            if (comparator.compare(pinyinTemp, pinyinData) > 0) {
                insertPosition = i;
                break;
            }
        }
        return insertPosition;
    }

    /**
     * All datas should be 汉字(HanZi)
     * @param datas
     */
    public void setHanZiDatas(List<String> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        Collections.sort(mDatas, new HanZiComparator());
        notifyDataSetChanged();
    }

    class HanZiComparator implements Comparator<String> {

        @Override
        public int compare(String lhs, String rhs) {
            return PinyinUtils.hanziToPinyin(lhs).compareTo(PinyinUtils.hanziToPinyin(rhs));
        }
    }

}
