package xmu.swordbearer.yuedu.net.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SwordBearer on 13-8-12.
 */
public class ArticleAPI extends ClientAPI {
    /**
     * 获取订阅内容
     *
     * @param date     日期 20130812
     * @param listener
     */
    public static void getFeeds(long date, OnRequestListener listener) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("date", date);
        try {
            String result = NetHelper.httpPost(URL_GET_FEEDS, params);
            Log.e("TEST", "获取的数据是 " + result);
            JSONObject jo = new JSONObject(result);
            int status = jo.getInt("status");
            if (status != 1) {
                listener.onError(null);
            } else {
                listener.onFinished(jo.getJSONArray("data"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            listener.onError(null);
        }
    }

    public static void getArticle(long id, OnRequestListener listener) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        try {
            String result = NetHelper.httpPost(URL_GET_ARTICLE, params);
            Log.e("TEST", "获取的数据是 " + result);
            if (result == null) {
                listener.onError(null);
                return;
            }
            JSONObject jo = new JSONObject(result);
            int status = jo.getInt("status");
            if (status != 1) {
                listener.onError(null);
            } else {
                listener.onFinished(jo.getJSONObject("data"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            listener.onError(null);
        }
    }

    public static JSONArray downloadFeeds() throws IOException, JSONException {
        String result = NetHelper.httpPost(URL_GET_FEEDS, null);
        JSONObject jo = new JSONObject(result);
        int status = jo.getInt("status");
        if (status == 1) {
            return jo.getJSONArray("data");
        }
        return null;
    }

    public static JSONArray downloadArticles() throws IOException, JSONException {
        String result = NetHelper.httpPost(URL_DOWNLOAD_ARTICLES, null);
        JSONObject jo = new JSONObject(result);
        int status = jo.getInt("status");
        if (status == 1) {
            return jo.getJSONArray("data");
        }
        return null;
    }
}
