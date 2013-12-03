package xmu.swordbearer.yuedu.core.offline;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import xmu.swordbearer.yuedu.R;
import xmu.swordbearer.yuedu.bean.Music;
import xmu.swordbearer.yuedu.core.app.YueduApp;
import xmu.swordbearer.yuedu.core.net.MusicAPI;
import xmu.swordbearer.yuedu.core.net.OnRequestListener;
import xmu.swordbearer.yuedu.db.DBManager;
import xmu.swordbearer.yuedu.utils.CommonUtils;
import xmu.swordbearer.yuedu.utils.UiUtils;


/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-14.
 */
public class DownloadMusicTask extends BaseDownloadTask {

    protected DownloadMusicTask(Context mContext) {
        super(mContext);
    }

    @Override
    public void onStart(final OnRequestListener listener) {
        if (!CommonUtils.isSDCard()) {
            UiUtils.showToast(mContext, R.string.not_access_sdcard);
            return;
        }
        final OnRequestListener getMusicsListener = new OnRequestListener() {
            @Override
            public void onError(Object obj) {
            }

            @Override
            public void onFinished(Object obj) {
                JSONArray ja = (JSONArray) obj;
                Log.e("TEST", "获得的音乐数 " + ja.length());
                if (ja.length() == 0) {
                    onError(null);
                }
                try {
                    Music mMusic = new Music(ja.getJSONObject(0));
//                    mMusic.setType(MusicPlayerService.SOURCE_TYPE_FILE);
                    DBManager.addMusic(mContext, mMusic);
                    YueduApp.downloadMusic(mMusic);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MusicAPI.getDailyMusic(getMusicsListener);
            }
        });
        thread.start();
    }

}
