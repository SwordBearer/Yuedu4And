package xmu.swordbearer.yuedu.db.bean;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-21.
 */
public class FeedList extends BaseBean {
    public static String CACHE_KEY = "yuedu_cache_feeds";
    private List<Article> feeds = new ArrayList<Article>(0);

    public static FeedList parseJSON(JSONArray ja) throws JSONException {
        FeedList feedList = new FeedList();
        for (int i = 0; i < ja.length(); i++) {
            feedList.feeds.add(Article.parseJSON(ja.getJSONObject(i)));
        }
        return feedList;
    }

    /**
     * 在尾部追加数据
     *
     * @param ja JsonArray
     * @return List
     */
    public int append(JSONArray ja) throws JSONException {
        FeedList tempList = FeedList.parseJSON(ja);
        feeds.addAll(tempList.getFeeds());
        return ja.length();
    }

    /**
     * 在头部添加新的数据
     *
     * @param ja JSONArray
     * @return List
     */
    public int prepend(JSONArray ja) throws JSONException {
        FeedList tempList = FeedList.parseJSON(ja);
        feeds.addAll(0, tempList.getFeeds());
        return ja.length();
    }

    public List<Article> getFeeds() {
        return feeds;
    }

    public int getCount() {
        return feeds.size();
    }

    /**
     * 获取第一条数据的ID
     *
     * @return long
     */
    public long getFirstId() {
        long id = 0;
        if (feeds.size() > 0) {
            id = feeds.get(0).getId();
        }
        return id;
    }

    /**
     * 获取最后一条数据的ID
     *
     * @return long
     */
    public long getLastId() {
        long id = 0;
        if (feeds.size() > 0) {
            id = feeds.get(feeds.size() - 1).getId();
        }
        return id;
    }
}
