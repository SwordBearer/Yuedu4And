package xmu.swordbearer.yuedu.core.net;

import android.util.Log;

import org.json.JSONObject;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-18.
 */
public class MusicAPI extends ClientAPI {
    /**
     * 获取每日音乐列表
     *
     * @param listener null
     */
    public static void getDailyMusic(OnRequestListener listener) {
        try {
            String result = NetHelper.httpPost(URL_GET_MUSICS, null);
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
}
