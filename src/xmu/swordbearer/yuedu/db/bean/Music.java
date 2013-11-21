package xmu.swordbearer.yuedu.db.bean;

import android.database.Cursor;
import android.provider.BaseColumns;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-17.
 */
public class Music extends BaseBean {
    private int id = -1;
    private int type;//0 1 下载 收藏 deprecated
    private String name;//名称
    private String details;//详细
    private String author;//作者
    private String path;//保存路径
    private String url;//网络存放地址
    private String lyc_url;//歌词的地址
    private int cloudId;//云端ID


    public Music() {

    }

    public Music(Cursor cursor) throws Exception {
        id = cursor.getInt(0);
//        type = cursor.getInt(1);
        name = cursor.getString(2);
        details = cursor.getString(3);
        author = cursor.getString(4);
        path = cursor.getString(5);
        url = cursor.getString(6);
        lyc_url = cursor.getString(7);
        cloudId = cursor.getInt(8);
    }

    public Music(JSONObject jo) throws JSONException {
//        this.type = MusicPlayerService.SOURCE_TYPE_STREAM;
        this.name = jo.getString("name");
        this.author = jo.getString("author");
        this.details = jo.getString("details");
        this.url = jo.getString("url");
        this.lyc_url = jo.getString("lyc_url");
        this.cloudId = jo.getInt("id");
    }


    public static class MusicColumns implements BaseColumns {
        public static final String _TYPE = "type";
        public static final String _NAME = "name";
        public static final String _DETAILS = "details";
        public static final String _AUTHOR = "author";
        public static final String _PATH = "path";
        public static final String _URL = "url";
        public static final String _LYC_URL = "lyc_url";
        public static final String _CLOUD_ID = "cloudId";
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getAuthor() {
        return author;
    }

    public String getPath() {
        return path;
    }

    public String getUrl() {
        return url;
    }

    public String getLyc_url() {
        return lyc_url;
    }

    public int getCloudId() {
        return cloudId;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setType(int type) {
        this.type = type;
    }

}
