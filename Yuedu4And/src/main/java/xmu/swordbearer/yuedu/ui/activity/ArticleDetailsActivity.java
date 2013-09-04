package xmu.swordbearer.yuedu.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import xmu.swordbearer.yuedu.R;
import xmu.swordbearer.yuedu.bean.Article;
import xmu.swordbearer.yuedu.net.ArticleAPI;
import xmu.swordbearer.yuedu.net.Utils;
import xmu.swordbearer.yuedu.net.NetHelper;
import xmu.swordbearer.yuedu.net.OnRequestListener;
import xmu.swordbearer.yuedu.utils.UiHelper;

/**
 * Created by SwordBearer on 13-8-12.
 */
public class ArticleDetailsActivity extends Activity {
    private WebView webView;
    private ImageButton btnBack, btnRefresh;
    private Article mArticle = new Article();
    private long articleId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        initViews();
    }

    public void initViews() {
        Intent i = getIntent();
        articleId = i.getLongExtra("extra_article_id", -1);
        if (articleId == -1) {
            UiHelper.showToast(this, R.string.get_data_failed);
            finish();
            return;
        }
        btnBack = (ImageButton) findViewById(R.id.article_details_back);
        btnRefresh = (ImageButton) findViewById(R.id.article_details_refresh);
        webView = (WebView) findViewById(R.id.article_details_webview);

        mArticle.setId(articleId);
        mArticle = (Article) Utils.readCache(this, mArticle.generateCacheKey());
        if (mArticle == null) {
            getArticleDetails();
        } else {
            showArticleDetails();
        }
    }

    private void getArticleDetails() {
//        progressBar.setVisibility(View.VISIBLE);
        if (!NetHelper.isNetworkConnected(this)) {
//            progressBar.setVisibility(View.INVISIBLE);
            UiHelper.showToast(this, R.string.not_access_network);
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                ArticleAPI.getArticle(articleId, getArticleListener);
            }
        }).start();
    }

    private void showArticleDetails() {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("<html><body style='padding:0px 4px;'><div style='padding-left:10px;border-left:6px #CD0000 solid;'><div style='font-size:20px;font-weight:bold'>");
        sBuffer.append(mArticle.getTitle() + "</div>");
        String birth = mArticle.getBirth().substring(0, 16);
        sBuffer.append("<div style='color:#828282;font-size:12px;'>" + birth + "</div></div>");
        sBuffer.append("<div style='text-indent: 2em;'>");
        sBuffer.append(mArticle.getContent());
        sBuffer.append("</div></body></html>");
        webView.loadDataWithBaseURL(null, sBuffer.toString(), "text/html", "UTF-8", null);
//        progressBar.setVisibility(View.INVISIBLE);

    }

    private OnRequestListener getArticleListener = new OnRequestListener() {
        @Override
        public void onError(Object obj) {
            handler.sendEmptyMessage(0);
        }

        @Override
        public void onFinished(Object obj) {
            JSONObject response = (JSONObject) obj;
            try {
                mArticle = Article.parseJSON(response);
                Utils.saveCache(ArticleDetailsActivity.this, mArticle.generateCacheKey(), mArticle);
                handler.sendEmptyMessage(1);
            } catch (JSONException e) {
                onError(null);
                Log.e("ArticleDetails", "转化Article 错误");
            }

        }

    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    showArticleDetails();
                    break;
                case 0:
                    UiHelper.showToast(ArticleDetailsActivity.this, R.string.get_data_failed);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };
}
