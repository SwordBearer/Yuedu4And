package xmu.swordbearer.yuedu.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by SwordBearer on 13-8-25.
 */
public class QiniuAPI extends ClientAPI {
    public static Bitmap getDailyCover(Context context, OnRequestListener listener) {
        Point point = Utils.getWindowSize(context);
        String name = Utils.calendar2Date(Calendar.getInstance()) + ".png";
        String url = URL_QINIU_PICTURE_PREFIX + name;
        url = generateImageViewUrl(url, 1, point.x, point.y, null);
        Log.e("TEST", "图片的网址是 " + url);
        return NetHelper.downloadImg(url, listener);
    }

    public static void getPicture(String url, int mode, int w, int h, String format, OnRequestListener listener) {
        url = generateImageViewUrl(url, mode, w, h, format);
        NetHelper.downloadImg(url, listener);
    }

    private static String generateImageViewUrl(String url, int mode, int w, int h, String format) {
        url += "?imageView/" + mode + "/w/" + w + "/h/" + h;
        if (format != null) {
            url += "/format/" + format;
        }
        return url;
    }
}
