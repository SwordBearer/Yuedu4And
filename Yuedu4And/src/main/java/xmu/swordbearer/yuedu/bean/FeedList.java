package xmu.swordbearer.yuedu.bean;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by SwordBearer on 13-8-11.
 */
public class FeedList extends BaseBean {
    private long date;
    private List<Article> feeds = new ArrayList<Article>(0);

    public static FeedList parseJSON(JSONArray ja) throws JSONException {
        FeedList feedList = new FeedList();
        for (int i = 0; i < ja.length(); i++) {
            feedList.feeds.add(Article.parseJSON(ja.getJSONObject(i)));
        }
        return feedList;
    }

    public void appendFeeds(FeedList feedList) {
        feeds.clear();
        feeds.addAll(feedList.getFeeds());
        date = feedList.getDate();
    }

    public static String generateCacheKey() {
        Calendar calendar = Calendar.getInstance();
        String key = "yuedu_cache_feeds_" + calendar.get(Calendar.YEAR) + calendar.get(Calendar.YEAR) + calendar.get(Calendar.YEAR);
        return key;
    }

    public List<Article> getFeeds() {
        return feeds;
    }

    public long getDate() {
        return date;
    }
}
