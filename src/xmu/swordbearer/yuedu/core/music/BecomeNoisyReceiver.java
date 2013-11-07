package xmu.swordbearer.yuedu.core.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-20.
 */
public class BecomeNoisyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (null == action) return;
        if (action.equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
            Log.e("TET", "耳机插孔发生变化，发送暂停播放通知");
            Intent pauseMusic = new Intent(context, MusicPlayerService.class);
            pauseMusic.setAction(MusicPlayerService.ACTION_MUSIC_PAUSE);
            context.startService(pauseMusic);
        }
    }
}