package xmu.swordbearer.yuedu.db.bean;

import android.database.Cursor;
import android.provider.BaseColumns;

import org.json.JSONException;
import org.json.JSONObject;

import xmu.swordbearer.yuedu.core.music.MusicPlayerService;

/**
 * Created by SwordBearer on 13-8-17.
 */
public class Music extends BaseBean {
    public int id = -1;
    public int type;//0 1 下载 收藏
    public String name;//名称
    public String details;//详细
    public String author;//作者
    public String path;//保存路径
    public String url;//网络存放地址
    public String lyc_url;//歌词的地址
    public int cloudId;//云端ID


    public Music() {

    }

    public Music(Cursor cursor) throws Exception {
        id = cursor.getInt(0);
        type = cursor.getInt(1);
        name = cursor.getString(2);
        details = cursor.getString(3);
        author = cursor.getString(4);
        path = cursor.getString(5);
        url = cursor.getString(6);
        lyc_url = cursor.getString(7);
        cloudId = cursor.getInt(8);
    }

    public Music(JSONObject jo) throws JSONException {
        this.type = MusicPlayerService.SOURCE_TYPE_STREAM;
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
}
