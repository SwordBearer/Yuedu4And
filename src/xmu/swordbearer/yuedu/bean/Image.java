package xmu.swordbearer.yuedu.bean;

public class Image {
    public String url;
    public int width;
    public int height;
    public int mode = 1;
    public String format = "png";
    public long size;

    public Image(String url, int w, int h) {
        this.url = url;
        this.width = w;
        this.height = h;
    }

    public Image(String url, int width, int height, int mode, String format) {
        this.url = url;
        this.width = width;
        this.height = height;
        this.mode = mode;
        this.format = format;
    }

}
