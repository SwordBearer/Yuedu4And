package xmu.swordbearer.yuedu.core.music;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-19.
 */
@TargetApi(Build.VERSION_CODES.FROYO)
public class AudioFocusHelper implements AudioManager.OnAudioFocusChangeListener {
    AudioManager mAudioManager;
    MusicPlayerService musicPlayerService;

    public AudioFocusHelper(MusicPlayerService musicPlayerService) {
        mAudioManager = (AudioManager) musicPlayerService.getSystemService(Context.AUDIO_SERVICE);
        this.musicPlayerService = musicPlayerService;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (this.musicPlayerService == null)
            return;
        musicPlayerService.audioFocusChanged(focusChange);
    }

    public boolean requestFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);
    }

    public boolean abandonFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                mAudioManager.abandonAudioFocus(this);
    }
}
