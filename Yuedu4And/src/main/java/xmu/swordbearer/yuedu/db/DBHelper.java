package xmu.swordbearer.yuedu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import xmu.swordbearer.yuedu.bean.Music;

/**
 * Created by SwordBearer on 13-8-17.
 */
public class DBHelper {

    private static DBHelper instance;

    private SQLiteDatabase db;
    private DBOpenHelper dbOpenHelper;

    public static final String TBL_MUSIC = "tbl_music";

    private DBHelper(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
        if (db == null) {
            db = dbOpenHelper.getWritableDatabase();
        }
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    public void close() {
        if (db != null) {
            if (db.isOpen()) {
                db.close();
            }
        }
    }

    private void open() {
        if (db != null) {
            if (!db.isOpen()) {
                db = dbOpenHelper.getWritableDatabase();
            }
        }
    }

    public int insert(String tblName, ContentValues values) {
        open();
        return (int) db.insert(tblName, null, values);
    }

    public int delete(String tblName, int id) {
        open();
        String idStr[] = {id + ""};
        return db.delete(tblName, "_id=?", idStr);
    }

    public int update(String tblName, int id, ContentValues values) {
        open();
        String idStr[] = {id + ""};
        return db.update(tblName, values, "_id=?", idStr);
    }

    public Cursor queryAll(String tblName) {
        open();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tblName, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor query(String tblName, int id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + tblName + " WHERE _id=" + id, null);
        cursor.moveToFirst();
        return cursor;
    }

    private class DBOpenHelper extends SQLiteOpenHelper {
        private static final int DB_VERSION = 1;

        public DBOpenHelper(Context context) {
            super(context, null, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String create_tbl_music = "CREATE TABLE IF NOT EXISTS "
                    + TBL_MUSIC + "("
                    + Music.MusicColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + Music.MusicColumns._TYPE + " INTEGER NOT NULL,"
                    + Music.MusicColumns._NAME + " TEXT NOT NULL,"
                    + Music.MusicColumns._DETAILS + " TEXT,"
                    + Music.MusicColumns._AUTHOR + " TEXT NOT NULL,"
                    + Music.MusicColumns._PATH + " TEXT,"
                    + Music.MusicColumns._URL + " TEXT NOT NULL,"
                    + Music.MusicColumns._LYC_URL + " TEXT" +
                    ")";
            db.execSQL(create_tbl_music);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TBL_MUSIC);
            onCreate(db);
        }
    }
}
