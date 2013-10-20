package xmu.swordbearer.yuedu.net.api;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by SwordBearer on 13-8-12.
 */
public class Utils {
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


    public static Point getWindowSize(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("pref_app_size", Context.MODE_PRIVATE);
        Point point = new Point();
        point.x = prefs.getInt("window_size_width", 480);
        point.y = prefs.getInt("window_size_height", 1280);
        return point;
    }

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
     * @param context
     * @param key     缓存文件名
     * @param data    可以是Serializable对象
     * @return
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
                oos.close();
                fos.close();
            } catch (IOException e) {
            }
        }
        return false;
    }

    /**
     * 读取缓存文件
     *
     * @param context
     * @param key     文件名
     * @return 返回Object对象:如果返回的实体可以被反序列化，可以使用Serializable进行类型转换
     */
    public static Object readCache(Context context, String key) {
        File data = context.getFileStreamPath(key);
        if (!data.exists()) {
            return null;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
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
     * @param context
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
     * @param mContext
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
     * @return
     */
    public static boolean isSDCard() {
        return (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED));
    }
}
