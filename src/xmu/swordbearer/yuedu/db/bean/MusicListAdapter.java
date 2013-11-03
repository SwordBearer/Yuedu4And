package xmu.swordbearer.yuedu.db.bean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xmu.swordbearer.yuedu.R;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-1-03.
 */
public class MusicListAdapter extends BaseListAdapter {
    public MusicListAdapter(Context context, List<?> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listitem_music, null, false);
            assert convertView != null;
            holder.tvName = (TextView) convertView.findViewById(R.id.listitem_music_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Music music = (Music) mDataList.get(position);
        holder.tvName.setText(music.name);
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
    }
}
