package xmu.swordbearer.yuedu.core.music;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import xmu.swordbearer.yuedu.bean.Music;
import xmu.swordbearer.yuedu.ui.activity.HomeActivity;
import xmu.swordbearer.yuedu.utils.CommonUtils;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-17.
 */
public class MusicPlayerService extends Service implements
        MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener {
    private static final String TAG = "MusicPlayerService";
    private MediaPlayer mMediaPlayer;

    private TelephonyManager mTelephonyManager;
    private int initialCallState;
    private WifiManager.WifiLock mWifiLock;
    private AudioFocusHelper mAudioFocusHelper;
    private Notification notification;//通知栏

    private MediaSourceType mSourceType;//音乐文件的来源

    //
    private Timer mTimer;
    private TimerTask mTimerTask;


    public static final String ACTION_MUSIC_START_PLAY = "action_music_start_play";
    public static final String ACTION_MUSIC_TOGGLE_PLAY = "action_music_toggle_play";
    public static final String ACTION_MUSIC_PAUSE = "action_music_pause";
    public static final String ACTION_MUSIC_STOP = "action_music_stop";
    public static final String ACTION_MUSIC_SEEK_TO = "action_music_seekto";

    public static final String EXTRA_MUSIC = "extra_music";
    public static final String EXTRA_SEEK_PERCENT = "extra_seek_percent";


    public enum MediaSourceType {
        MEDIA_TYPE_FILE, MEDIA_TYPE_NET
    }

    private PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state != TelephonyManager.CALL_STATE_IDLE
                    && state != initialCallState) {
                stopSelf();
            }
        }
    };

    @Override
    public void onCreate() {
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager
                .listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        mWifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "my_wifilock");
        mWifiLock.setReferenceCounted(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAudioFocusHelper = new AudioFocusHelper(this);
        } else {
            mAudioFocusHelper = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_NOT_STICKY;
        }
        String action = intent.getAction();
        if (action == null) {
            return START_NOT_STICKY;
        }
        if (action.equals(ACTION_MUSIC_PAUSE)) {
            pause();
        }
        if (action.equals(ACTION_MUSIC_TOGGLE_PLAY)) {
            toggle();
        } else if (action.equals(ACTION_MUSIC_START_PLAY)) {// play
            Log.e(TAG, "MusicPlayerService 播放音乐开始");
            Music music = (Music) intent.getSerializableExtra(EXTRA_MUSIC);
            if (music == null || music.getUrl() == null) {
                return START_NOT_STICKY;
            }
            preparePlayer(music);
            initialCallState = mTelephonyManager.getCallState();
        } else if (action.equals(ACTION_MUSIC_SEEK_TO)) {
            float percent = intent.getFloatExtra(EXTRA_SEEK_PERCENT, -1);
            if (percent < 0)
                return START_NOT_STICKY;
            seekTo(percent);
        }
        return START_STICKY;
    }

    /**
     * 初始化MediaPlayer的参数
     *
     * @param music Music
     */
    private void preparePlayer(final Music music) {
        Uri uri = null;
        String path = music.getPath();
        if (path != null) {
            File file = new File(path);
            if (CommonUtils.isSDCard() && file.exists()) {
                uri = Uri.parse(path);
                this.mSourceType = MediaSourceType.MEDIA_TYPE_FILE;
            }
        }
        if (uri == null) {
            uri = Uri.parse(java.net.URLEncoder.encode(music.getUrl()));
        }
        stop();

        mMediaPlayer = new MediaPlayer();
        //如果是网络流的话，保持wifi连接
        if (mSourceType == MediaSourceType.MEDIA_TYPE_NET) {
            mWifiLock.acquire();
        }
        mMediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer
                .setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        start();
                        showNotification(music);
                        sendAction(ACTION_MUSIC_START_PLAY, music);
                    }
                });

        mMediaPlayer.setOnBufferingUpdateListener(this);
        try {
            mMediaPlayer.reset();
            Log.e("TEST", "播放的路径是" + uri);
            mMediaPlayer.setDataSource(this, uri);

            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (am.getStreamVolume(AudioManager.STREAM_MUSIC) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.prepareAsync();
            }
        } catch (IOException e) {
            stop();
        }
    }

    private void showNotification(Music music) {
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),
                0, new Intent(getApplicationContext(), HomeActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification = new Notification();
        notification.tickerText = music.getName();
        notification.icon = android.R.drawable.ic_media_play;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.setLatestEventInfo(this, "乐读正在播放:", "Playing: "
                + music.getName(), pi);
        Log.e(TAG, "显示通知栏");
        startForeground(1, notification);
    }

    /**
     * 停止播放
     */
    private void stop() {
        if (mMediaPlayer == null)
            return;
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
        mWifiLock.release();
        if (mAudioFocusHelper != null)
            mAudioFocusHelper.abandonFocus();
        stopTimer();
        sendAction(ACTION_MUSIC_STOP, null);
    }

    /**
     * 暂停播放
     */
    private void pause() {
        if (mMediaPlayer == null)
            return;
        mMediaPlayer.pause();
        stopTimer();
        sendAction(ACTION_MUSIC_PAUSE, null);
    }

    /**
     * 开始播放
     */
    private void start() {
        if (mMediaPlayer == null)
            return;
        mMediaPlayer.start();
        if (mAudioFocusHelper != null)
            mAudioFocusHelper.requestFocus();
        Log.e(TAG, "开始播放音乐 " + mMediaPlayer.getDuration());
        startTimer();
    }

    /**
     * 跳到某个位置开始播放
     *
     * @param percent 播放百分比
     */
    private void seekTo(float percent) {
        if (mMediaPlayer == null)
            return;
        int pos = (int) (percent * mMediaPlayer.getDuration());
        mMediaPlayer.seekTo(pos);
    }

    /**
     * 切换 播放/暂停 状态
     */
    private void toggle() {
        if (mMediaPlayer == null)
            return;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            stopTimer();
        } else {
            mMediaPlayer.start();
            startTimer();
        }
    }

    /**
     * 对外通知播放情况
     *
     * @param action String
     * @param music  Music
     */
    private void sendAction(String action, Music music) {
        Intent intent = new Intent(action);
        if (music != null) {
            intent.putExtra(EXTRA_MUSIC, music);
        }
        sendBroadcast(intent);
        Log.e("TEST", "MUSIC_service 发送广播 " + action);
    }

    /**
     * 通知外部刷新界面
     */
    private void updateUi() {
        Log.e("TEST", "正在播放   " + mMediaPlayer.getCurrentPosition());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 当播放停止后，释放资源
     */
    @Override
    public void onDestroy() {
        stop();
        stopForeground(true);
        super.onDestroy();
    }

    /**
     * 手机声音被其他程序占用时
     *
     * @param focusChange
     */
    public void audioFocusChanged(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                start();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                stop();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (mMediaPlayer == null)
                    return;
                if (mMediaPlayer.isPlaying())
                    mMediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e("TEST", "出错了 艹 " + what);
        stop();

        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        // Log.e(TAG, " 缓存进度 " + percent);
    }


    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    updateUi();
                }
            };
        }
        mTimer.schedule(mTimerTask, 1000, 1000);
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }


}
