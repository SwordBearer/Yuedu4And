package xmu.swordbearer.yuedu.core.weather;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context cxt) {
        helper = new DBHelper(cxt);
        db = helper.getWritableDatabase();
    }

    public void addCity(List<CityCode> cityList) {
        db.beginTransaction();
        String sql = "insert into city_code values(null,?,?,?)";
        try {
            for (CityCode cc : cityList) {
                db.execSQL(sql, new Object[]{cc.getProvince(), cc.getCityName(), cc.getCode()});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public String getCityCode(String cityName) {
        String sql = "select code from city_code where city=?";
        Cursor c = db.rawQuery(sql, new String[]{cityName});
        String code = "";
        if (c.moveToNext()) {
            code = c.getString(c.getColumnIndex("code"));
        }
        c.close();
        return code;
    }

    public List<String> getProvinces() {
        String sql = "select province from city_code group by province";
        Cursor c = db.rawQuery(sql, null);
        List<String> provinceList = new ArrayList<String>();
        while (c.moveToNext()) {
            String pv = c.getString(c.getColumnIndex("province"));
            provinceList.add(pv);
        }
        c.close();
        return provinceList;
    }

    public List<String> getCities(String province) {
        String sql = "select city from city_code where province=?";
        Cursor c = db.rawQuery(sql, new String[]{province});
        List<String> cityList = new ArrayList<String>();
        while (c.moveToNext()) {
            String city = c.getString(c.getColumnIndex("city"));
            cityList.add(city);
        }
        c.close();
        return cityList;
    }

    public boolean hasCityData() {
        String sql = "select count(*) from city_code";
        boolean result = false;
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToNext()) {
            int count = c.getInt(0);
            if (count > 0) result = true;
        }
        c.close();
        return result;
    }

    public String queryCurrentCityCode() {
        String curCode = "";
        String sql = "select code from cur_city";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToNext()) {
            curCode = c.getString(c.getColumnIndex("code"));
        } else {
            curCode = "101010100";
            updateCurrentCityCode(curCode);
        }
        c.close();
        return curCode;
    }

    public void updateCurrentCityCode(String code) {
        String delSql = "delete from cur_city";
        db.execSQL(delSql);

        db.beginTransaction();
        String sql = "insert into cur_city values(null,?)";
        try {
            db.execSQL(sql, new Object[]{code});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void closeDB() {
        db.close();
    }


}
