package xmu.swordbearer.yuedu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xmu.swordbearer.yuedu.R;
import xmu.swordbearer.yuedu.bean.Music;

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
            holder.tvAuthor = (TextView) convertView.findViewById(R.id.listitem_music_author);
            holder.ivDetails = (ImageView) convertView.findViewById(R.id.listitem_music_details);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Music music = (Music) mDataList.get(position);
        holder.tvName.setText(music.getName());
        holder.tvAuthor.setText(music.getAuthor());
        holder.ivDetails.setOnClickListener(new View.OnClickListener() {
            public void onClick(android.view.View view) {
            }
        });
        //如果文件不存在
        if (!music.isDownloaded()) {
            //显示下载按钮
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tvName, tvAuthor;
        ImageView ivDetails;
    }
}
