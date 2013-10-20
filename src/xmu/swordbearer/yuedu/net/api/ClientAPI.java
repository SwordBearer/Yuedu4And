package xmu.swordbearer.yuedu.net.api;

/**
 * Created by Administrator on 13-8-12.
 */
public class ClientAPI {
	protected static final String URL_PREFIX = "http://10.0.2.2/muder/index.php/index/";
	protected static final String URL_GET_FEEDS = URL_PREFIX + "getFeeds";
	protected static final String URL_GET_ARTICLE = URL_PREFIX + "getArticle";
	protected static final String URL_DOWNLOAD_ARTICLES = URL_PREFIX
			+ "downloadArticles";
	protected static final String URL_GET_MUSICS = URL_PREFIX + "getMusics";
	public static final String URL_QINIU_PICTURE_PREFIX = "http://yuedu-picture.qiniudn.com/";
}
