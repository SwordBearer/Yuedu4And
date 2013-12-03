package xmu.swordbearer.yuedu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import xmu.swordbearer.yuedu.bean.Article;
import xmu.swordbearer.yuedu.bean.Music;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-21.
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
        DBHelper dbHelper = DBHelper.getInstance(context);
        Cursor tmp = dbHelper.queryByColumn(DBHelper.TBL_MUSIC, Music.MusicColumns._CLOUD_ID, music.getCloudId());
        if (tmp.moveToFirst()) {//已经存在该音乐
            dbHelper.close();
            Log.e("TEST", "已经存在该音乐了");
            return 1;
        }
        ContentValues values = new ContentValues();
        values.put(Music.MusicColumns._TYPE, music.getType());
        values.put(Music.MusicColumns._NAME, music.getName());
        values.put(Music.MusicColumns._DETAILS, music.getDetails());
        values.put(Music.MusicColumns._AUTHOR, music.getAuthor());
        values.put(Music.MusicColumns._PATH, music.getPath());
        values.put(Music.MusicColumns._URL, music.getUrl());
        values.put(Music.MusicColumns._LYC_URL, music.getLyc_url());
        values.put(Music.MusicColumns._CLOUD_ID, music.getCloudId());

        int result = dbHelper.insert(DBHelper.TBL_MUSIC, values);

        Log.e("TEST", "DBManager---------addMusic 保存音乐 " + result + " ************** " + music.getUrl());

        dbHelper.close();
        return result;
    }

    public static int deleteMusic(Context context, int id) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        int result = dbHelper.delete(DBHelper.TBL_MUSIC, id);
        dbHelper.close();
        return result;
    }

    /**
     * ******************************article*************************
     */

    /**
     * 获取某一篇文章的详细内容
     *
     * @param context Context
     * @param id      int
     * @return Article
     */
    public static Article getArticle(Context context, int id) {
        Article article = null;
        DBHelper dbHelper = DBHelper.getInstance(context);
        Cursor cursor = dbHelper.query(DBHelper.TBL_ARTICLE, id);
        if (cursor.moveToFirst()) {
            article = new Article(cursor);
        }
        cursor.close();
        return article;
    }

    public static Article getArticleByCloudId(Context context, int cloudId) {
        Article article = null;
        DBHelper dbHelper = DBHelper.getInstance(context);
        Cursor cursor = dbHelper.queryByColumn(DBHelper.TBL_ARTICLE, Article.ArticleColumns._CLOUD_ID, cloudId);
        if (cursor.moveToFirst()) {
            article = new Article(cursor);
        }
        cursor.close();
        return article;
    }

    /**
     * 添加文章
     *
     * @param context  Context
     * @param mArticle Article
     */
    public static int addArticle(Context context, Article mArticle) {
        ContentValues values = new ContentValues();
//        values.put(Article.ArticleColumns._DB_ID, mArticle.dbId);
        values.put(Article.ArticleColumns._CLOUD_ID, mArticle.getCloudId());// cloudId
        values.put(Article.ArticleColumns._TITLE, mArticle.getTitle());
        values.put(Article.ArticleColumns._AUTHOR, mArticle.getAuthor());
        values.put(Article.ArticleColumns._SOURCE, mArticle.getSource());
        values.put(Article.ArticleColumns._OUTLINE, mArticle.getOutline());
        values.put(Article.ArticleColumns._BIRTH, mArticle.getBirth());
        values.put(Article.ArticleColumns._CONTENT, mArticle.getContent());

        DBHelper dbHelper = DBHelper.getInstance(context);
        int dbId = dbHelper.insert(DBHelper.TBL_ARTICLE, values);
        dbHelper.close();
        return dbId;
    }
}
