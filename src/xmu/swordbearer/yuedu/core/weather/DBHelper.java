package xmu.swordbearer.yuedu.core.weather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String WEATHER_DB = "weather.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context cxt) {
        super(cxt, WEATHER_DB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists city_code(city_num integer primary key autoincrement,province varchar,city varchar,code varchar)";
        db.execSQL(sql);
        sql = "create table if not exists cur_city(city_num integer primary key autoincrement, code varchar)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "alter table city_code add column other string";
        db.execSQL(sql);
    }

}
