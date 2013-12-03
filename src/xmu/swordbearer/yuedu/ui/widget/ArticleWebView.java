package xmu.swordbearer.yuedu.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-14.
 */
public class ArticleWebView extends WebView {
    public ArticleWebView(Context context) {
        super(context);
        init(context);
    }

    public ArticleWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ArticleWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
    }
}
