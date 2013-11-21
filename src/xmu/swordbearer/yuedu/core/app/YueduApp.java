package xmu.swordbearer.yuedu.core.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import xmu.swordbearer.yuedu.core.music.MusicPlayerService;
import xmu.swordbearer.yuedu.db.bean.Music;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-11.
 */
public class YueduApp extends Application {

    public static void startOfflineTask() {
//        Intent intent = new Intent(this, OfflineService.class);
//        intent.setAction(OfflineService.ACTION_OFFLINE_START);
//        startService(intent);
    }

    /**
     * 通知MusicPlayerService播放指定的音乐
     *
     * @param context
     * @param music
     */
    public static void startPlay(Context context, Music music) {
        Intent startPlay = new Intent(context, MusicPlayerService.class);
        startPlay.putExtra(MusicPlayerService.EXTRA_MUSIC, music);
        startPlay.setAction(MusicPlayerService.ACTION_MUSIC_START_PLAY);
        context.startService(startPlay);
    }


    public static String downloadMusic(String url, String path) {
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
