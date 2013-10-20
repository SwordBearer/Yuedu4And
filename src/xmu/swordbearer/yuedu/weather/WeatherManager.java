package xmu.swordbearer.yuedu.weather;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WeatherManager {
    private static final String STR_URL = "http://m.weather.com.cn/data/%s.html";

    public WeatherManager() {
    }

    public Weather getWeather(String cityCode) {
        String realAddr = String.format(STR_URL, cityCode);
        String json = queryWeather(realAddr);
        Weather w = parseJson(json);
        return w;
    }

    private String queryWeather(String realAddr) {
        try {
            URL url = new URL(realAddr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStreamReader input = new InputStreamReader(conn.getInputStream(), "utf-8");
            BufferedReader bufReader = new BufferedReader(input);
            StringBuilder builder = new StringBuilder();
            String line = "";
            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
            }
            conn.disconnect();
            return builder.toString();
        } catch (Exception e) {
            Log.e("queryWeather", "queryWeather()::" + e.toString());
        }
        return null;
    }

    private Weather parseJson(String json) {
        try {
            JSONObject allObj = new JSONObject(json);
            JSONObject obj = allObj.getJSONObject("weatherinfo");
            String city = obj.getString("city");
            String date = obj.getString("date_y");
            String week = obj.getString("week");
            String index = obj.getString("index");
            String index_d = obj.getString("index_d");

            List<WeatherDesc> weatherDesc = new ArrayList<WeatherDesc>();
            for (int i = 1; i <= 6; ++i) {
                String tmp = obj.getString("temp" + i);
                String desc = obj.getString("weather" + i);
                String wind = obj.getString("wind" + i);
                String windLevel = obj.getString("fl" + i);
                weatherDesc.add(new WeatherDesc(tmp, desc, wind, windLevel));
            }
            return new Weather(city, date, week, weatherDesc, index, index_d);
        } catch (Exception e) {
            Log.e("parseJson", "parseJson()::" + e.toString());
        }
        return null;
    }
}
