package xmu.swordbearer.yuedu.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import xmu.swordbearer.yuedu.R;
import xmu.swordbearer.yuedu.weather.DBManager;
import xmu.swordbearer.yuedu.weather.MyLocation;
import xmu.swordbearer.yuedu.weather.SeetingActivity;
import xmu.swordbearer.yuedu.weather.Weather;
import xmu.swordbearer.yuedu.weather.WeatherDesc;
import xmu.swordbearer.yuedu.weather.WeatherManager;

public class WeatherActivity extends Activity {

    private Button selectCityBtn, updateBtn;
    private TextView cityTextView, tempTextView, timeTextView, clothTextView;
    private TextView tomorrowTextView, houtianTextView;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Weather w = (Weather) msg.obj;
                    cityTextView.setText(w.getCity());
                    timeTextView.setText(w.getDate() + "��" + w.getWeek());
                    WeatherDesc wd = w.getWeatherDescList().get(0);
                    tempTextView.setText(wd.getTemp() + "��" + wd.getDesc() + "��" + wd.getWind());
                    clothTextView.setText("��ܰ��ʾ��" + w.getIndex_d());

                    WeatherDesc tomorrow = w.getWeatherDescList().get(1);
                    tomorrowTextView.setText("��������" + tomorrow.getTemp() + "��" + tomorrow.getDesc() + "��" + tomorrow.getWind());

                    WeatherDesc houtian = w.getWeatherDescList().get(2);
                    houtianTextView.setText("��������" + houtian.getTemp() + "��" + houtian.getDesc() + "��" + houtian.getWind());
                    break;
                default:
                    super.handleMessage(msg);
            }

        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_view);
        selectCityBtn = (Button) findViewById(R.id.weather_set_city_btn);
        selectCityBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, SeetingActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        updateBtn = (Button) findViewById(R.id.weather_update_btn);
        updateBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        cityTextView = (TextView) findViewById(R.id.weather_city_textview);
        tempTextView = (TextView) findViewById(R.id.weather_temp_textview);
        timeTextView = (TextView) findViewById(R.id.weather_time_textview);
        clothTextView = (TextView) findViewById(R.id.weather_cloth_textview);

        tomorrowTextView = (TextView) findViewById(R.id.weather_tomorrow_textview);
        houtianTextView = (TextView) findViewById(R.id.weather_thedayafter_tomorrow_textview);

        updateData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == 1) {
                    showWeather();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showWeather() {
        Runnable r = new Runnable() {
            public void run() {
                DBManager dbm = new DBManager(WeatherActivity.this);
                String code = dbm.getCityCode(MyLocation.city);
                dbm.updateCurrentCityCode(code);
                dbm.closeDB();

                WeatherManager wm = new WeatherManager();
                Weather w = wm.getWeather(code);
                if (w != null) {
                    Message msg = handler.obtainMessage(1);
                    msg.obj = w;
                    handler.sendMessage(msg);
                }
            }
        };
        new Thread(r).start();
    }

    private void updateData() {
        Runnable r = new Runnable() {
            public void run() {
                DBManager dbm = new DBManager(WeatherActivity.this);
                String code = dbm.queryCurrentCityCode();
                dbm.closeDB();

                WeatherManager wm = new WeatherManager();
                Weather w = wm.getWeather(code);
                if (w != null) {
                    Message msg = handler.obtainMessage(1);
                    msg.obj = w;
                    handler.sendMessage(msg);
                }
            }
        };
        new Thread(r).start();
    }
}
