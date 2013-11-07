package xmu.swordbearer.yuedu.core.offline;

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
import xmu.swordbearer.yuedu.core.music.MusicPlayerService;
import xmu.swordbearer.yuedu.core.net.MusicAPI;
import xmu.swordbearer.yuedu.core.net.OnRequestListener;
import xmu.swordbearer.yuedu.db.DBManager;
import xmu.swordbearer.yuedu.db.bean.Music;
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
                    String url = mMusic.getUrl();
                    String extend = url.substring(mMusic.getUrl().lastIndexOf("."), url.length());
                    String dir = Environment.getExternalStorageDirectory().getAbsolutePath() +
                            File.separator + "yuedu/offline/";
                    File file = new File(dir);
                    file.mkdirs();
                    String path = dir + mMusic.getName() +
                            extend;
                    mMusic.setPath(path);
                    mMusic.setType(MusicPlayerService.SOURCE_TYPE_FILE);
                    DBManager.addMusic(mContext, mMusic);
                    Log.e("TST", "保存的音乐的文件路径是 " + path);
                    downloadMusic(url, path);
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
            int len;
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
