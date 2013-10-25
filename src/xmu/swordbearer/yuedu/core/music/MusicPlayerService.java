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

import xmu.swordbearer.yuedu.db.bean.Music;
import xmu.swordbearer.yuedu.ui.activity.HomeActivity;
import xmu.swordbearer.yuedu.utils.CommonUtils;

/**
 * 在后台播放音乐 Created by SwordBearer on 13-8-17.
 */
public class MusicPlayerService extends Service implements
        MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener {
    private static final String TAG = "MusicPlayerService";
    private MediaPlayer mMediaPlayer;

    private TelephonyManager mTelephonyManager;
    private int initialCallState;
    private WifiManager.WifiLock mWifiLock;
    private AudioFocusHelper mAudioFocusHelper;
    private Notification notification;
    private MusicPlayerTimerTask timerTask;

    private int mSourceType;

    public static final String ACTION_MUSIC_START_PLAY = "action_music_start_play";
    public static final String ACTION_MUSIC_TOGGLE_PLAY = "action_music_toggle_play";
    public static final String ACTION_MUSIC_PAUSE = "action_music_pause";
    public static final String ACTION_MUSIC_STOP = "action_music_stop";
    public static final String ACTION_MUSIC_SEEK_TO = "action_music_seekto";

    public static final String EXTRA_MUSIC = "extra_music";
    public static final String EXTRA_SEEK_PERCENT = "extra_seek_percent";

    public static final int SOURCE_TYPE_FILE = 0;
    public static final int SOURCE_TYPE_STREAM = 1;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAudioFocusHelper = new AudioFocusHelper(this);
        } else {
            mAudioFocusHelper = null;
        }
        timerTask = new MusicPlayerTimerTask();
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
            if (music == null || music.url == null) {
                return START_NOT_STICKY;
            }
            play(music);
            initialCallState = mTelephonyManager.getCallState();
        } else if (action.equals(ACTION_MUSIC_SEEK_TO)) {
            float percent = intent.getFloatExtra(EXTRA_SEEK_PERCENT, -1);
            if (percent < 0)
                return START_NOT_STICKY;
            seekTo(percent);
        }
        return START_STICKY;
    }

    private void play(final Music music) {
        Uri uri = null;
        if (music.path != null) {
            File file = new File(music.path);
            if (CommonUtils.isSDCard() && file.exists()) {
                uri = Uri.parse(music.path);
                this.mSourceType = SOURCE_TYPE_FILE;
            }
        }
        if (uri == null) {
            uri = Uri.parse(music.url);
            this.mSourceType = SOURCE_TYPE_STREAM;
        }
        stop();

        mMediaPlayer = new MediaPlayer();
        if (mSourceType == SOURCE_TYPE_STREAM) {
            mWifiLock.acquire();
        }
        mMediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer
                .setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        start();
                        showNotification(music);
                        sendAction(ACTION_MUSIC_START_PLAY, music);
                    }
                });
        try {
            mMediaPlayer.reset();
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
        notification.tickerText = music.name;
        notification.icon = android.R.drawable.ic_media_play;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.setLatestEventInfo(this, "乐读正在播放:", "Playing: "
                + music.name, pi);
        Log.e(TAG, "显示通知栏");
        startForeground(1, notification);
    }

    private void stop() {
        if (mMediaPlayer == null)
            return;
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
        mWifiLock.release();
        if (mAudioFocusHelper != null)
            mAudioFocusHelper.requestFocus();
        timerTask.stop();
        sendAction(ACTION_MUSIC_STOP, null);
    }

    private void pause() {
        if (mMediaPlayer == null)
            return;
        mMediaPlayer.pause();
        timerTask.pause();
        sendAction(ACTION_MUSIC_PAUSE, null);
    }

    private void start() {
        if (mMediaPlayer == null)
            return;
        mMediaPlayer.start();
        if (mAudioFocusHelper != null)
            mAudioFocusHelper.requestFocus();
        Log.e(TAG, "开始播放音乐 " + mMediaPlayer.getDuration());
        timerTask.start();
    }

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
            timerTask.pause();
//            timerTask.stop();
        } else {
            mMediaPlayer.start();
//            timerTask.start();
            timerTask.run();
        }
    }

    private void sendAction(String action, Music music) {
        Intent intent = new Intent(action);
        if (music != null) {
            intent.putExtra(EXTRA_MUSIC, music);
        }
        sendBroadcast(intent);
        Log.e("TEST", "MUSIC_service 发送广播 " + action);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        stop();
        stopForeground(true);
        super.onDestroy();
    }

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
        stop();
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        // Log.e(TAG, " 缓存进度 " + percent);
    }

    private class MusicPlayerTimerTask extends TimerTask {
        private Timer mTimer;

        public MusicPlayerTimerTask() {
            this.mTimer = new Timer();
        }

        public void start() {
            Log.e(TAG, "MusicPlayerTimerTask 启动 ");
            mTimer.purge();
            mTimer.schedule(this, 1000);
        }

        public void pause() {
            try {
                mTimer.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void stop() {
            mTimer.cancel();
        }

        @Override
        public void run() {
            Log.e(TAG, "播放进度 " + mMediaPlayer.getCurrentPosition());
        }
    }

}
