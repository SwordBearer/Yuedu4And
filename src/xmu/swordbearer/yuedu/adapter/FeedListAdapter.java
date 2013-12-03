package xmu.swordbearer.yuedu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xmu.swordbearer.yuedu.R;
import xmu.swordbearer.yuedu.bean.Article;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-12.
 */
public class FeedListAdapter extends BaseListAdapter {
    public FeedListAdapter(Context context, List<?> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listitem_feed, null, false);
            assert convertView != null;
            holder.tvTitle = (TextView) convertView.findViewById(R.id.listitem_feed_title);
//            holder.tvTime = (TextView) convertView.findViewById(R.id.listitem_feed_info);
            holder.tvOutline = (TextView) convertView.findViewById(R.id.listitem_feed_outline);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Article article = (Article) mDataList.get(position);
        holder.tvTitle.setText(article.getTitle());
        holder.tvOutline.setText(article.getOutline());
        return convertView;
    }

    private class ViewHolder {
        TextView tvTitle, tvOutline;
    }
}
