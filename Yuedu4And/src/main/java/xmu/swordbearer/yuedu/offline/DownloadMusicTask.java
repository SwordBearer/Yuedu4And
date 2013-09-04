package xmu.swordbearer.yuedu.offline;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import xmu.swordbearer.yuedu.R;
import xmu.swordbearer.yuedu.bean.Music;
import xmu.swordbearer.yuedu.db.DBManager;
import xmu.swordbearer.yuedu.music.MusicPlayerService;
import xmu.swordbearer.yuedu.net.MusicAPI;
import xmu.swordbearer.yuedu.net.OnRequestListener;
import xmu.swordbearer.yuedu.net.Utils;
import xmu.swordbearer.yuedu.utils.UiHelper;

/**
 * Created by SwordBearer on 13-8-14.
 */
public class DownloadMusicTask extends BaseDownloadTask {

    protected DownloadMusicTask(Context mContext) {
        super(mContext);
    }

    @Override
    public void onStart(final OnRequestListener listener) {
        if (!Utils.isSDCard()) {
            UiHelper.showToast(mContext, R.string.not_access_sdcard);
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
                    String url = mMusic.url;
                    String extend = url.substring(mMusic.url.lastIndexOf("."), url.length());
                    String dir = Environment.getExternalStorageDirectory().getAbsolutePath() +
                            File.separator + "yuedu/offline/";
                    File file = new File(dir);
                    file.mkdirs();
                    mMusic.path = dir + mMusic.name +
                            extend;
                    mMusic.type = MusicPlayerService.SOURCE_TYPE_FILE;
                    DBManager.addMusic(mContext, mMusic);
                    Log.e("TST", "保存的音乐的文件路径是 " + mMusic.path);
                    downloadMusic(mMusic.url, mMusic.path);
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

    private String downloadMusic(String url, String path) {
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        File file = new File(path);
        try {
            HttpResponse response = client.execute(httpGet);

            InputStream in = response.getEntity().getContent();

            FileOutputStream out = new FileOutputStream(new File(path));
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            in.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        Log.e("TEST", "音乐下载成功 " + file.length());
        return path;
    }
}
