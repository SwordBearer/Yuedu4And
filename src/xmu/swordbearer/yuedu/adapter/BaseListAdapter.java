package xmu.swordbearer.yuedu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by SwordBearer on 13-8-12.
 */
public abstract class BaseListAdapter extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected List<?> mDataList;

    public BaseListAdapter(Context context, List<?> data) {
        this.mDataList = data;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
