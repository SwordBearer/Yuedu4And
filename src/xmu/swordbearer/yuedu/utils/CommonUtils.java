package xmu.swordbearer.yuedu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-12.
 */
public class CommonUtils {
    /**
     * 保存窗口尺寸
     *
     * @param activity Activity
     */
    public static void saveWindowSize(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        Log.e("Tag", "屏幕尺寸是 " + width + ":" + height);
        SharedPreferences prefs = activity.getSharedPreferences("pref_app_size", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("window_size_width", width);
        editor.putInt("window_size_height", height);
        editor.commit();
    }

    /**
     * 获取窗口尺寸
     *
     * @param context Context
     * @return Point
     */
    public static Point getWindowSize(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("pref_app_size", Context.MODE_PRIVATE);
        Point point = new Point();
        point.x = prefs.getInt("window_size_width", 480);
        point.y = prefs.getInt("window_size_height", 1280);
        return point;
    }

    /**
     * 保存音乐面板的尺寸
     *
     * @param context Context
     * @param view    View
     */
    public static void saveDashboardSize(Context context, View view) {
        int width = view.getWidth();
        int height = view.getHeight();
        SharedPreferences prefs = context.getSharedPreferences("pref_app_size", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("musicdashboard_size_width", width);
        editor.putInt("musicdashboard_size_height", height);
        editor.commit();
        Log.e("MusicDashboard", "View的尺寸是 " + view.getWidth() + ":" + view.getHeight() + "-----" + view.getMeasuredWidth() + ":" + view.getMeasuredHeight());
    }

    /**
     * 获取音乐面板的尺寸
     *
     * @param context Context
     * @return Point
     */
    public static Point getDashboardSize(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("pref_app_size", Context.MODE_PRIVATE);
        Point point = new Point();
        point.x = prefs.getInt("musicdashboard_size_width", 260);
        point.y = prefs.getInt("musicdashboard_size_height", 200);
        return point;
    }

    /* **********date**************** */
    public static String calendar2Date(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(calendar.getTime());
    }


    /* **********cache**************** */

    /**
     * 保存缓存文件：建议缓存的数据是可序列化的，便于读取时进行反序列化操作
     *
     * @param context Context
     * @param key     缓存文件名
     * @param data    必须是Serializable对象
     * @return boolean
     */
    public static boolean saveCache(Context context, String key, Object data) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        File file = context.getFileStreamPath(key);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = context.openFileOutput(key, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null)
                    oos.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                //
            }
        }
        return false;
    }

    /**
     * 读取缓存文件
     *
     * @param context Context
     * @param key     文件名
     * @return 返回Object对象:如果返回的实体可以被反序列化，可以使用Serializable进行类型转换
     */
    public static Object readCache(Context context, String key) {
        File data = context.getFileStreamPath(key);
        if (!data.exists()) {
            return null;
        }
        FileInputStream fis;
        ObjectInputStream ois;
        try {
            fis = context.openFileInput(key);
            ois = new ObjectInputStream(fis);
            try {
                return ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除缓存
     *
     * @param context Context
     * @param key     缓存文件名
     */
    public static void deleteCache(Context context, String key) {
        File cache = context.getFileStreamPath(key);
        if (cache.exists()) {
            cache.delete();
        }
    }

    /**
     * 清除缓存
     *
     * @param mContext Context
     */
    public static boolean clearCache(Context mContext) {
        File file = mContext.getFilesDir();
        if (file != null && file.exists() && file.isDirectory()) {
            for (File item : file.listFiles()) {
                item.delete();
            }
            file.delete();
        }
        return true;
    }

    /**
     * 检测手机是否有SDCard
     *
     * @return boolean
     */
    public static boolean isSDCard() {
        return (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED));
    }

    /**
     * 将图片流保存到SD卡中
     *
     * @param context Context
     * @param bmp     Bitmap
     * @param url     String
     * @param dir     String
     * @return String
     * @throws IOException
     */
    public static String saveBitmap2Sd(Context context, Bitmap bmp, String url, String dir) throws IOException {
        String fileName = MD5Util.MD5Encode(url) + ".png";
        File fileDir = new File(dir);
        Log.e("TEST", "要存储的文件目录是*******  " + dir);
        fileDir.mkdirs();
        String filePath = dir + fileName;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        InputStream inStream = new ByteArrayInputStream(baos.toByteArray());
        return saveStream2SD(context, inStream, filePath);
    }

    /**
     * 将文件流保存到SD卡中
     *
     * @param context     Context
     * @param inputStream InputStream
     * @param filePath    String
     * @return String
     * @throws IOException
     */
    private static String saveStream2SD(Context context, InputStream inputStream, String filePath) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File file = new File(filePath);
        Log.e("TEST", "要存储的文件路径是 " + filePath + "  fileExists " + file.exists());
        if (!file.exists()) {
            file.createNewFile();
        }
        try {
            bis = new BufferedInputStream(inputStream);
            bos = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[2048];
            int length;
            while ((length = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
                buffer = new byte[2048];
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e2) {
                //
            }
        }
        return filePath;
    }

    /**
     * 从缓存中读取图片
     *
     * @param mContext Context
     * @param url      String
     * @return Bitmap
     */
    public static Bitmap getBitmapFromCache(Context mContext, String url) {
        Bitmap bmp;
        String fileName = MD5Util.MD5Encode(url);
        FileInputStream fis = null;
        try {
            fis = mContext.openFileInput(fileName);
            bmp = BitmapFactory.decodeStream(fis);
            return bmp;
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    //
                }
            }
        }
    }

    /**
     * 将Bitmap流保存到缓存中
     *
     * @param mContext    Context
     * @param url         String
     * @param inputStream InputStream
     * @return String
     */
    public static String saveBitmap2Cache(Context mContext, String url, InputStream inputStream) {
        String fileName = MD5Util.MD5Encode(url);// 加密后的文件名
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(inputStream);
            bos = new BufferedOutputStream(mContext.openFileOutput(fileName,
                    Context.MODE_PRIVATE));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e2) {
                //
            }
        }
        return mContext.getFilesDir() + "/" + fileName;
    }

}
