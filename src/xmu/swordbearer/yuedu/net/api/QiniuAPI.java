package xmu.swordbearer.yuedu.net.api;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;

/**
 * Created by SwordBearer on 13-8-25.
 */
public class QiniuAPI extends ClientAPI {
	public static void getDailyCover(Context context, OnRequestListener listener) {
		Point point = Utils.getWindowSize(context);
		String name = Utils.calendar2Date(Calendar.getInstance()) + ".png";
		String url = URL_QINIU_PICTURE_PREFIX + name;
		getPicture(url, 1, point.x, point.y, "png", listener);
	}

	public static Bitmap getPicture(String url, int mode, int w, int h,
			String format, OnRequestListener listener) {
		url = generateImageViewUrl(url, mode, w, h, format);
		Log.e("TEST", "准备获取的图片的尺寸是 " + w + ":" + h);
		return NetHelper.downloadImg(url, listener);
	}

	private static String generateImageViewUrl(String url, int mode, int w,
			int h, String format) {
		url += "?imageView/" + mode + "/w/" + w + "/h/" + h;
		if (format != null) {
			url += "/format/" + format;
		}
		return url;
	}
}
