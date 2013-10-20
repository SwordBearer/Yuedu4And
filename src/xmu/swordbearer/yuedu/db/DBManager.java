package xmu.swordbearer.yuedu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import xmu.swordbearer.yuedu.bean.Music;

/**
 * Created by SwordBearer on 13-8-17.
 */
public class DBManager {
    public static List<Music> getMusic(Context context) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        Cursor cursor = dbHelper.queryAll(DBHelper.TBL_MUSIC, "desc");
        List<Music> musics = new ArrayList<Music>();
        try {
            while (cursor.moveToNext()) {
                musics.add(new Music(cursor));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FUCK", "getMusic failure " + e.getMessage());
        }
        cursor.close();
        Log.e("TEST", "DBManager---------getMusic 音乐总数为 " + musics.size());
        return musics;
    }

    public static int addMusic(Context context, Music music) {
        ContentValues values = new ContentValues();
        values.put(Music.MusicColumns._TYPE, music.type);
        values.put(Music.MusicColumns._NAME, music.name);
        values.put(Music.MusicColumns._DETAILS, music.details);
        values.put(Music.MusicColumns._AUTHOR, music.author);
        values.put(Music.MusicColumns._PATH, music.path);
        values.put(Music.MusicColumns._URL, music.url);
        values.put(Music.MusicColumns._LYC_URL, music.lyc_url);

        DBHelper dbHelper = DBHelper.getInstance(context);
        int result = dbHelper.insert(DBHelper.TBL_MUSIC, values);

        Log.e("TEST", "DBManager---------addMusic 保存音乐 " + result + " ************** " + music.url);

        dbHelper.close();
        return result;
    }
}
