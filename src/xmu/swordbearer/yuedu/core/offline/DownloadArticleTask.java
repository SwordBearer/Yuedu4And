package xmu.swordbearer.yuedu.core.offline;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xmu.swordbearer.yuedu.core.net.ArticleAPI;
import xmu.swordbearer.yuedu.core.net.OnRequestListener;
import xmu.swordbearer.yuedu.db.bean.Article;
import xmu.swordbearer.yuedu.db.bean.FeedList;
import xmu.swordbearer.yuedu.utils.CommonUtils;

/**
 * Created by SwordBearer on 13-8-14.
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
					CommonUtils.saveCache(mContext, FeedList.generateCacheKey(),
                            feedList);
					JSONArray jaArticles = ArticleAPI.downloadArticles();
					List<Article> articleList = new ArrayList<Article>();
					Article temp;
					for (int i = 0; i < jaArticles.length(); i++) {
						temp = Article.parseJSON(jaArticles.getJSONObject(i));
						articleList.add(temp);
						CommonUtils.saveCache(mContext, temp.generateCacheKey(), temp);
					}
					Log.e(TAG, "离线下载的文章有 " + articleList.size());
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
