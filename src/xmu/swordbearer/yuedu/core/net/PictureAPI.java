package xmu.swordbearer.yuedu.core.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;
import java.util.Calendar;

import xmu.swordbearer.yuedu.utils.CommonUtils;
import xmu.swordbearer.yuedu.utils.MD5Util;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-25.
 */
public class PictureAPI extends ClientAPI {
    /**
     * 获取每日封面图片
     *
     * @param context  null
     * @param listener null
     */
    public static void getDailyCover(Context context, OnRequestListener listener) {
        String name = CommonUtils.calendar2Date(Calendar.getInstance()) + ".png";
        Point point = CommonUtils.getWindowSize(context);
        String str = URL_QINIU_PICTURE_PREFIX + name;
        String url = generateImageViewUrl(str, 1, point.x, point.y, "png");
        String fileName = MD5Util.MD5Encode(url) + ".png";

        String dir = Environment.getExternalStorageDirectory() + "/yuedu/cover/";
        Log.e("TEST", "SDCARD中的文件路径是  " + (dir + fileName));
        Bitmap bmp = BitmapFactory.decodeFile(dir + fileName);
        if (null == bmp) {
            bmp = NetHelper.downloadImg(url, listener);
            if (null != bmp) {
                try {
                    CommonUtils.saveBitmap2Sd(context, bmp, url, dir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            listener.onFinished(bmp);
        }
    }


    /**
     * 从七牛获取图片
     *
     * @param url      图片地址
     * @param mode     图片模式 1从中间开始裁剪 2设定宽或者高进行裁切
     * @param w        图片宽度
     * @param h        图片高度
     * @param format   图片格式
     * @param listener null
     * @return Bitmap
     */
    public static Bitmap getPicture(String url, int mode, int w, int h,
                                    String format, OnRequestListener listener) {
        String str = generateImageViewUrl(url, mode, w, h, format);
        Log.e("TEST", "准备获取的图片的尺寸是 " + w + ":" + h + " url " + url);
        return NetHelper.downloadImg(str, listener);
    }

    /**
     * 生成七牛图片的访问链接
     *
     * @param url    图片连接
     * @param mode   图片模式
     * @param w      图片宽度
     * @param h      图片高度
     * @param format 图片格式 eg:png,jpg
     * @return 符合七牛云存储的图片链接
     */
    private static String generateImageViewUrl(String url, int mode, int w,
                                               int h, String format) {
        url += "?imageView/" + mode + "/w/" + w + "/h/" + h;
        if (format != null) {
            url += "/format/" + format;
        }
        return url;
    }
}
