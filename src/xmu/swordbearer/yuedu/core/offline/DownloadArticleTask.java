package xmu.swordbearer.yuedu.core.offline;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import xmu.swordbearer.yuedu.core.net.ArticleAPI;
import xmu.swordbearer.yuedu.core.net.OnRequestListener;
import xmu.swordbearer.yuedu.db.DBManager;
import xmu.swordbearer.yuedu.db.bean.Article;
import xmu.swordbearer.yuedu.db.bean.FeedList;
import xmu.swordbearer.yuedu.utils.CommonUtils;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-14.
 */
public class DownloadArticleTask extends BaseDownloadTask {
    private String TAG = "DownloadArticleTask";

    protected DownloadArticleTask(Context mContext) {
        super(mContext);
    }

    @Override
    public void onStart(final OnRequestListener listener) {
        Log.e(TAG, "启动离线任务");
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray jaFeeds = ArticleAPI.downloadFeeds();
                    FeedList feedList = FeedList.parseJSON(jaFeeds);
                    CommonUtils.saveCache(mContext, FeedList.CACHE_KEY,
                            feedList);
                    JSONArray jaArticles = ArticleAPI.downloadArticles();
                    Article temp;
                    for (int i = 0; i < jaArticles.length(); i++) {
                        temp = Article.parseJSON(jaArticles.getJSONObject(i));
                        DBManager.addArticle(mContext, temp);
                    }
                    Log.e(TAG, "离线下载的文章有 " + jaArticles.length());
                    listener.onFinished(OfflineService.RESULT_ARTICLE_OK);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
