package xmu.swordbearer.yuedu.core.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import xmu.swordbearer.yuedu.bean.Music;
import xmu.swordbearer.yuedu.core.music.MusicPlayerService;
import xmu.swordbearer.yuedu.core.net.NetHelper;

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

    /**
     * 开始下载音乐
     *
     * @param music
     * @return 音乐的保存路径
     */
    public static String downloadMusic(Music music) {
//        HttpClient client = new DefaultHttpClient();
//        HttpGet httpGet = new HttpGet(music.getUrl());
        String path = music.getPath();
        File file = new File(path);
        file.mkdirs();
        try {
//            HttpResponse response = client.execute(httpGet);
//            InputStream in = response.getEntity().getContent();
            InputStream in = NetHelper.httpGetStream(music.getUrl());
            FileOutputStream out = new FileOutputStream(file);
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
//            client.getConnectionManager().shutdown();
        }
        Log.e("TEST", "音乐下载成功 " + file.length());
        return path;
    }
}
