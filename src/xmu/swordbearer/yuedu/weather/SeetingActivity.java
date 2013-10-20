package xmu.swordbearer.yuedu.weather;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xmu.swordbearer.yuedu.R;


public class SeetingActivity extends Activity {
    private GridView gridView;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, String>> dataList;
    private int curType = 1;// 1:province; 2:city;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    initProvince();
                    break;
                case 2:
                    initCity();
                    break;
                case 3:// success
                    setResult(1);
                    finish();
                    break;
                case 4:// fail
                    setResult(0);
                    finish();
                default:
                    super.handleMessage(msg);
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_province);
        gridView = (GridView) findViewById(R.id.weather_province_gridview);
        init();
    }

    private void init() {
        Runnable r = new Runnable() {
            public void run() {
                DataInitializer cityDataInit = new DataInitializer(SeetingActivity.this);
                cityDataInit.initCityData();

                Message msg = handler.obtainMessage(1);
                handler.sendMessage(msg);
            }
        };
        new Thread(r).start();
    }

    private void initProvince() {
        DBManager dm = new DBManager(SeetingActivity.this);
        dataList = new ArrayList<HashMap<String, String>>();
        List<String> provinceList = dm.getProvinces();
        dm.closeDB();

        for (int i = 0; i < provinceList.size(); ++i) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("province", provinceList.get(i));
            dataList.add(map);
            Log.e("TEST", map.get("province"));
        }

        adapter = new SimpleAdapter(this, dataList, R.layout.weather_province_item, new String[]{"province"}, new int[]{R.id.weather_province_item_textview});
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                curType = 2;
                HashMap<String, String> item = (HashMap<String, String>) arg0.getItemAtPosition(arg2);
                MyLocation.province = item.get("province");
                Message msg = handler.obtainMessage(2);
                handler.sendMessage(msg);
            }
        });
    }

    private void initCity() {

        DBManager dm = new DBManager(SeetingActivity.this);
        dataList = new ArrayList<HashMap<String, String>>();
        List<String> cityList = dm.getCities(MyLocation.province);
        dm.closeDB();

        for (int i = 0; i < cityList.size(); ++i) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("city", cityList.get(i));
            dataList.add(map);
        }

        adapter = new SimpleAdapter(this, dataList, R.layout.weather_province_item, new String[]{"city"}, new int[]{R.id.weather_province_item_textview});
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                curType = 1;
                HashMap<String, String> item = (HashMap<String, String>) arg0.getItemAtPosition(arg2);
                MyLocation.city = item.get("city");

                Message msg = handler.obtainMessage(3);
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (curType == 1) {
                Message msg = handler.obtainMessage(4);
                handler.sendMessage(msg);
            } else if (curType == 2) {
                curType = 1;
                Message msg = handler.obtainMessage(1);
                handler.sendMessage(msg);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
