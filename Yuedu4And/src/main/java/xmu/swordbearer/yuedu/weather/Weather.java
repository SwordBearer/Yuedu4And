package xmu.swordbearer.yuedu.weather;

import java.util.List;

public class Weather {
    // json�ṹΪ��http://m.weather.com.cn/data/101010100.html
    private String city;
    private String date;
    private String week;
    private List<WeatherDesc> weatherDescList;

    // ���촩��ָ��
    private String index;
    private String index_d;

    public Weather(String city, String date, String week, List<WeatherDesc> weatherDescList, String index, String index_x) {
        this.city = city;
        this.date = date;
        this.week = week;
        this.weatherDescList = weatherDescList;
        this.index = index;
        this.index_d = index_x;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public List<WeatherDesc> getWeatherDescList() {
        return weatherDescList;
    }

    public void setWeatherDescList(List<WeatherDesc> weatherDescList) {
        this.weatherDescList = weatherDescList;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getIndex_d() {
        return index_d;
    }

    public void setIndex_d(String index_d) {
        this.index_d = index_d;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n���У�" + city);
        builder.append("\nʱ�䣺" + date);
        builder.append("\n�����ǣ�" + week);
        WeatherDesc today = weatherDescList.get(0);
        builder.append("\n��������:" + today.getTemp());
        builder.append("\n��ܰ��ʾ��" + today.getDesc());
        builder.append("\n���٣�" + today.getWind() + "��" + today.getWindLevel());
        builder.append("\n����ָ��" + index_d);
        return builder.toString();
    }

}
