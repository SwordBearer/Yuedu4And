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
import xmu.swordbearer.yuedu.core.net.ArticleAPI;
import xmu.swordbearer.yuedu.core.net.NetHelper;
import xmu.swordbearer.yuedu.core.net.OnRequestListener;
import xmu.swordbearer.yuedu.db.DBManager;
import xmu.swordbearer.yuedu.utils.CalendarUtil;
import xmu.swordbearer.yuedu.utils.UiUtils;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-21.
 */
public class ArticleDetailsActivity extends Activity {
    private WebView webView;
    private ImageButton btnBack, btnRefresh;
    private Article mArticle = null;
    private int articleId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        initViews();
    }

    public void initViews() {
        Intent i = getIntent();
        articleId = i.getIntExtra("extra_article_id", -1);
        if (articleId == -1) {
            UiUtils.showToast(this, R.string.get_data_failed);
            finish();
            return;
        }
        btnBack = (ImageButton) findViewById(R.id.article_details_back);
        btnRefresh = (ImageButton) findViewById(R.id.article_details_refresh);
        webView = (WebView) findViewById(R.id.article_details_webview);

        mArticle = DBManager.getArticleByCloudId(this, articleId);
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
            UiUtils.showToast(this, R.string.not_access_network);
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
        String birth = CalendarUtil.timeStamp2Date(mArticle.getBirth(), "yyyy-MM-dd HH:mm");
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
                DBManager.addArticle(ArticleDetailsActivity.this, mArticle);
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
                    UiUtils.showToast(ArticleDetailsActivity.this, R.string.get_data_failed);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };
}
