package xmu.swordbearer.yuedu.core.offline;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.Log;

import xmu.swordbearer.yuedu.R;
import xmu.swordbearer.yuedu.core.net.OnRequestListener;
import xmu.swordbearer.yuedu.ui.activity.HomeActivity;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-24.
 */
public class OfflineService extends Service {
    public static String TAG = "OfflineService";
    public static final String ACTION_OFFLINE_START = "action_offline_start";
    public static final String ACTION_OFFLINE_END = "action_offline_end";

    public static final String PREF_OFFLINE = "pref_offline";
    public static final String PREF_OFFLINE_ON = "pref_offline_on";// false
    public static final String PREF_OFFLINE_ARTICLE = "pref_offline_article";// false
    public static final String PREF_OFFLINE_MUSIC = "pref_offline_music";
    public static final String PREF_OFFLINE_PICTURE = "pref_offline_picture";

    public static final String RESULT_ARTICLE_OK = "article_ok";
    public static final String RESULT_PICTURE_OK = "picture_ok";
    public static final String RESULT_MUSIC_OK = "music_ok";

    public static final long HOUR_IN_MILLIS = 60 * 60 * 1000;

    // picture,article,music/lyric
    // picture 下载后保存至SDcard
    // 文章 下载后保存到数据库
    // 音乐/lyric 下载后保存到sdcard

    private DownloadArticleTask articleTask;
    private DownloadMusicTask musicTask;
    private DownloadPictureTask pictureTask;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || intent.getAction() == null) {
            return START_NOT_STICKY;
        }

        String action = intent.getAction();
        if (action == null) return -1;
        if (action.equals(ACTION_OFFLINE_START)) {
            startDownload();
        } else if (action.equals(ACTION_OFFLINE_END)) {
            stopDownload();
        }
        tirggerNext();
        return START_STICKY;
    }

    private OnRequestListener offlineListener = new OnRequestListener() {
        @Override
        public void onError(Object obj) {
        }

        @Override
        public void onFinished(Object obj) {
            String result = obj.toString();
            if (result.equals(RESULT_ARTICLE_OK)) {// 文章下载完毕
                if (pictureTask == null) {
                    pictureTask = new DownloadPictureTask(OfflineService.this);
                }
                Log.e("TEST", "离线文章下载完毕，开始图片下载");
                pictureTask.onStart(this);
            } else if (result.equals(RESULT_PICTURE_OK)) {// 图片下载完毕
                if (musicTask == null) {
                    musicTask = new DownloadMusicTask(OfflineService.this);
                }
                Log.e("TEST", "离线图片下载完毕，开始音乐下载");
                musicTask.onStart(this);
            } else if (result.equals(RESULT_MUSIC_OK)) {// 音乐下载完毕
                Log.e("TEST", "离线音乐下载完毕");

                showNotification(R.string.offline_finished);
            }
        }
    };

    /**
     * 开始下载
     */
    private void startDownload() {
        Log.e(TAG, "startDownloadb 开始离线任务");
        SharedPreferences pref = getSharedPreferences(PREF_OFFLINE,
                Context.MODE_PRIVATE);
        boolean isOn = pref.getBoolean(PREF_OFFLINE_ON, true);
        if (!isOn) {
            return;
        }

        showNotification(R.string.offline_started);
        if (pref.getBoolean(PREF_OFFLINE_ARTICLE, true)) {
            articleTask = new DownloadArticleTask(this);
            articleTask.onStart(offlineListener);

            Log.e(TAG, "开始 articleTask");
            return;
        }
        if (pref.getBoolean(PREF_OFFLINE_PICTURE, false)) {
            pictureTask = new DownloadPictureTask(this);
            pictureTask.onStart(offlineListener);
            Log.e(TAG, "开始 pictureTask");
            return;
        }
        if (pref.getBoolean(PREF_OFFLINE_MUSIC, false)) {
            musicTask = new DownloadMusicTask(this);
            musicTask.onStart(offlineListener);
            Log.e(TAG, "开始 musicTask");
        }
    }

    private void showNotification(int trickerText) {
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),
                0, new Intent(getApplicationContext(), HomeActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification();
        Resources res = getResources();
        notification.tickerText = res.getString(trickerText);
        notification.icon = R.drawable.ic_download;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.setLatestEventInfo(this,
                res.getString(R.string.offline_ing), null, pi);
        startForeground(1, notification);
    }

    /**
     * 停止下载
     */
    private void stopDownload() {
        articleTask.stop();
        pictureTask.stop();
        musicTask.stop();
        articleTask = null;
        pictureTask = null;
        musicTask = null;
    }

    /**
     * 激活下一次下载任务
     */
    private void tirggerNext() {
        SharedPreferences prefs = getSharedPreferences(PREF_OFFLINE,
                Context.MODE_PRIVATE);
        boolean isOn = prefs.getBoolean(PREF_OFFLINE_ON, false);
        if (!isOn) {
            return;
        }
        long settledTime = prefs.getLong(PREF_OFFLINE, 7 * HOUR_IN_MILLIS);
        long now = System.currentTimeMillis();
        long atTimeInMillis;
        if ((settledTime - now) < 0) {// 超过了
            atTimeInMillis = 24 * HOUR_IN_MILLIS + settledTime;
        } else {
            atTimeInMillis = settledTime - now;
        }
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ACTION_OFFLINE_START);
        // 有问题
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + atTimeInMillis, pi);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
