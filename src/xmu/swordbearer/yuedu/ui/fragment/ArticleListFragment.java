package xmu.swordbearer.yuedu.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import xmu.swordbearer.yuedu.R;
import xmu.swordbearer.yuedu.bean.Article;
import xmu.swordbearer.yuedu.bean.FeedList;
import xmu.swordbearer.yuedu.bean.FeedListAdapter;
import xmu.swordbearer.yuedu.net.api.ArticleAPI;
import xmu.swordbearer.yuedu.net.api.NetHelper;
import xmu.swordbearer.yuedu.net.api.OnRequestListener;
import xmu.swordbearer.yuedu.net.api.Utils;
import xmu.swordbearer.yuedu.ui.activity.ArticleDetailsActivity;
import xmu.swordbearer.yuedu.utils.UiHelper;

/**
 * Created by SwordBearer on 13-8-21.
 */
public class ArticleListFragment extends BasePageFragment {

    private ImageButton btnRefresh;
    private ListView lvFeeds;
    private ImageButton btnSlide;

    //
    private FeedListAdapter mAdapter;
    private FeedList feedList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_article_list, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    protected void initViews(View rootView) {
        btnSlide = (ImageButton) rootView.findViewById(R.id.main_btn_slide);
        btnRefresh = (ImageButton) rootView.findViewById(R.id.main_btn_refresh);
        lvFeeds = (ListView) rootView.findViewById(R.id.main_feed_lv);

        btnSlide.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        lvFeeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article article = feedList.getFeeds().get(position);
                Intent intent = new Intent(mContext, ArticleDetailsActivity.class);
                intent.putExtra("extra_article_id", article.getId());
                startActivity(intent);
            }
        });
        //
        feedList = (FeedList) Utils.readCache(mContext, FeedList.generateCacheKey());
        if (feedList == null || feedList.getFeeds().size() == 0) {
            feedList = new FeedList();
            getFeeds();
        }
        mAdapter = new FeedListAdapter(mContext, feedList.getFeeds());
        lvFeeds.setAdapter(mAdapter);
    }

    private void getFeeds() {
        if (!NetHelper.isNetworkConnected(mContext)) {
            return;
        }
        Utils.deleteCache(mContext, FeedList.generateCacheKey());
        new Thread(new Runnable() {
            @Override
            public void run() {
                long date = System.currentTimeMillis() / 1000;
                ArticleAPI.getFeeds(date, getFeedListener);
            }
        }).start();
    }


    private OnRequestListener getFeedListener = new OnRequestListener() {
        @Override
        public void onError(Object obj) {
            handler.sendEmptyMessage(0);
        }

        @Override
        public void onFinished(Object obj) {
            JSONArray ja = (JSONArray) obj;
            if (ja.length() == 0) {//该日没有文章，不当做异常去处理
                return;
            }
            try {
                FeedList temp = FeedList.parseJSON(ja);
                feedList.appendFeeds(temp);
                Utils.saveCache(mContext, FeedList.generateCacheKey(), feedList);
                handler.sendEmptyMessage(1);
            } catch (JSONException e) {
                e.printStackTrace();
                onError(null);
            }
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mAdapter.notifyDataSetChanged();
            } else {
                UiHelper.showToast(mContext, R.string.get_data_failed);
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v == btnRefresh) {
            getFeeds();
        } else if (v == btnSlide) {
            toggleDrawer();
        }
    }
}
