package xmu.swordbearer.yuedu.core.net;

/**
 * Created by Administrator on 13-8-12.
 */
public class ClientAPI {

    public static final int FLAG_REFRESH = 1;
    public static final int FLAG_MORE = 2;

    protected static final String URL_PREFIX = "http://muder.sinaapp.com/index.php/index/";
    protected static final String URL_GET_FEEDS = URL_PREFIX + "getFeeds";
    protected static final String URL_GET_ARTICLE = URL_PREFIX + "getArticle";
    protected static final String URL_DOWNLOAD_ARTICLES = URL_PREFIX
            + "downloadArticles";
    protected static final String URL_GET_MUSICS = URL_PREFIX + "getMusics";
    public static final String URL_QINIU_PICTURE_PREFIX = "http://yuedu-picture.qiniudn.com/";

    public static final String SD_PATH_ROOT = "yuedu/";
    public static final String SD_PATH_COVER = SD_PATH_ROOT + "cover";
    public static final String SD_PATH_MUSIC = SD_PATH_ROOT + "music";


}
