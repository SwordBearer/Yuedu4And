package xmu.swordbearer.yuedu.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import xmu.swordbearer.yuedu.R;
import xmu.swordbearer.yuedu.core.net.ArticleAPI;
import xmu.swordbearer.yuedu.core.net.ClientAPI;
import xmu.swordbearer.yuedu.core.net.NetHelper;
import xmu.swordbearer.yuedu.core.net.OnRequestListener;
import xmu.swordbearer.yuedu.db.bean.Article;
import xmu.swordbearer.yuedu.db.bean.FeedList;
import xmu.swordbearer.yuedu.db.bean.FeedListAdapter;
import xmu.swordbearer.yuedu.ui.activity.ArticleDetailsActivity;
import xmu.swordbearer.yuedu.utils.CommonUtils;
import xmu.swordbearer.yuedu.utils.UiUtils;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-21.
 */
public class ArticleListFragment extends BasePageFragment implements AdapterView.OnItemClickListener {

    private ImageButton btnFavorite;
    private ListView lvFeeds;
    private ImageButton btnSlide;

    private int lastFeedPos = 0;

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
        btnFavorite = (ImageButton) rootView.findViewById(R.id.main_btn_favorite);
        lvFeeds = (ListView) rootView.findViewById(R.id.main_feed_lv);

        btnSlide.setOnClickListener(this);
        btnFavorite.setOnClickListener(this);
        lvFeeds.setOnItemClickListener(this);
        //缓存
        feedList = (FeedList) CommonUtils.readCache(mContext, FeedList.CACHE_KEY);
        if (feedList == null || feedList.getFeeds().size() == 0) {
            feedList = new FeedList();
            refresh();
        }
        mAdapter = new FeedListAdapter(mContext, feedList.getFeeds());
        lvFeeds.setAdapter(mAdapter);
    }

    /**
     * 下拉刷新
     */
    private void refresh() {
        if (!NetHelper.isNetworkConnected(mContext)) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                long firstId = 0;
                if (feedList != null) {
                    firstId = feedList.getFirstId();
                }
                ArticleAPI.getFeeds(ClientAPI.FLAG_REFRESH, firstId, onRefreshListener);
            }
        }).start();
    }

    /**
     * 点击加载更多
     */
    private void getMore() {
        if (!NetHelper.isNetworkConnected(mContext)) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                long lastId = 0;
                if (feedList != null) {
                    lastId = feedList.getLastId();
                }
                ArticleAPI.getFeeds(ClientAPI.FLAG_MORE, lastId, onGetMoreListener);
            }
        }).start();
    }

    private OnRequestListener onRefreshListener = new OnRequestListener() {
        @Override
        public void onError(Object obj) {
            handler.sendEmptyMessage(0);
        }

        @Override
        public void onFinished(Object obj) {
            JSONArray ja = (JSONArray) obj;
            try {
                //将最新的数据放到最前面
                int newCount = feedList.prepend(ja);
                handler.sendEmptyMessage(1);
            } catch (JSONException e) {
                e.printStackTrace();
                onError(null);
            }
        }
    };

    private OnRequestListener onGetMoreListener = new OnRequestListener() {
        @Override
        public void onError(Object obj) {
            handler.sendEmptyMessage(0);
        }

        @Override
        public void onFinished(Object obj) {
            JSONArray ja = (JSONArray) obj;
            try {
                int newCount = feedList.append(ja);
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
                UiUtils.showToast(mContext, R.string.get_data_failed);
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v == btnFavorite) {
            refresh();
        } else if (v == btnSlide) {
            toggleDrawer();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Article article = feedList.getFeeds().get(position);
        Intent intent = new Intent(mContext, ArticleDetailsActivity.class);
        intent.putExtra("extra_article_id", article.getCloudId());
        startActivity(intent);
    }
}
