package xmu.swordbearer.yuedu.weather;

public class CityCode {
    private String province;
    private String cityName;
    private String code;

    public CityCode(String prov, String name, String code) {
        this.province = prov;
        this.cityName = name;
        this.code = code;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
