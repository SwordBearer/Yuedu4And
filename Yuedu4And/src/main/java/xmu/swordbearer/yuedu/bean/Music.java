package xmu.swordbearer.yuedu.bean;

import android.database.Cursor;
import android.provider.BaseColumns;

import org.json.JSONException;
import org.json.JSONObject;

import xmu.swordbearer.yuedu.music.MusicPlayerService;

/**
 * Created by SwordBearer on 13-8-17.
 */
public class Music extends BaseBean {
    public int id = -1;
    public int type;//0 1 下载 收藏
    public String name;
    public String details;
    public String author;
    public String path;
    public String url;
    public String lyc_url;


    public Music() {

    }

    public Music(Cursor cursor) {
        id = cursor.getInt(0);
        type = cursor.getInt(1);
        name = cursor.getString(2);
        details = cursor.getString(3);
        author = cursor.getString(4);
        path = cursor.getString(5);
        url = cursor.getString(6);
        lyc_url = cursor.getString(7);
    }

    public Music(JSONObject jo) throws JSONException {
        this.id = jo.getInt("id");
        this.type = MusicPlayerService.SOURCE_TYPE_STREAM;
        this.name = jo.getString("name");
        this.author = jo.getString("author");
        this.details = jo.getString("details");
        this.url = jo.getString("url");
        this.lyc_url = jo.getString("lyc_url");
    }

    public static class MusicColumns implements BaseColumns {
        public static final String _TYPE = "type";
        public static final String _NAME = "name";
        public static final String _DETAILS = "details";
        public static final String _AUTHOR = "author";
        public static final String _PATH = "path";
        public static final String _URL = "url";
        public static final String _LYC_URL = "lyc_url";
    }
}
