package xmu.swordbearer.yuedu.weather;

public class WeatherDesc {
    private String temp;
    private String desc;
    private String wind;
    private String windLevel;

    public WeatherDesc(String temp, String desc, String wind, String windLevel) {
        this.temp = temp;
        this.desc = desc;
        this.wind = wind;
        this.windLevel = windLevel;
    }

    public String getTemp() {
        return temp;
    }


    public String getDesc() {
        return desc;
    }

    public String getWind() {
        return wind;
    }

    public String getWindLevel() {
        return windLevel;
    }
}
