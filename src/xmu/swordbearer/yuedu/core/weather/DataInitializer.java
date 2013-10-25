package xmu.swordbearer.yuedu.core.weather;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataInitializer {
    private DBManager dbManager;
    private Context cxt;

    public DataInitializer(Context cxt) {
        this.cxt = cxt;
    }

    public void initCityData() {
        dbManager = new DBManager(cxt);
        if (!dbManager.hasCityData()) {
            moveFileDataToDB();
        }
        dbManager.closeDB();
    }

    private void moveFileDataToDB() {
        try {
            InputStream is = cxt.getAssets().open("city_code.json.txt");
            InputStreamReader ir = new InputStreamReader(is, "gb2312");
            BufferedReader br = new BufferedReader(ir);
            String line = "";
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            List<CityCode> cityList = parseJson(builder.toString());
            dbManager.addCity(cityList);
        } catch (Exception e) {
        }
    }

    private List<CityCode> parseJson(String json) throws JSONException {
        List<CityCode> list = new ArrayList<CityCode>();
        JSONObject allObj = new JSONObject(json);
        JSONArray cityCodeArray = allObj.getJSONArray("�й�����");
        for (int i = 0; i < cityCodeArray.length(); ++i) {
            JSONObject obj = cityCodeArray.getJSONObject(i);
            String province = obj.getString("ʡ");
            JSONArray cityArray = obj.getJSONArray("��");
            for (int j = 0; j < cityArray.length(); ++j) {
                JSONObject jobj = cityArray.getJSONObject(j);
                String cityname = jobj.getString("����");
                String code = jobj.getString("����");
                list.add(new CityCode(province, cityname, code));
                // Log.d("DataInit", cityname);
            }
        }
        return list;
    }
}
