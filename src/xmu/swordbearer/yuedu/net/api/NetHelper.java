package xmu.swordbearer.yuedu.net.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

public class NetHelper {

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!isEmpty(extraInfo)) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = 3;
                } else {
                    netType = 2;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;
        }
        return netType;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    private static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }
    //
    /***************************** HTTP POST **************************/

    /**
     * 执行HttpPost方法
     *
     * @param uri
     * @param post_params
     * @return
     * @throws org.apache.http.client.ClientProtocolException
     * @throws java.io.IOException
     */
    public static String httpPost(String uri, Map<String, Object> post_params) throws ClientProtocolException, IOException {
        String result = null;
        Log.e("TEST", "访问的网址是 " + uri);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(new UrlEncodedFormEntity(createNameValuePair(post_params), HTTP.UTF_8));
        HttpResponse response = httpClient.execute(httpPost);
        if (response != null) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try {
                    result = InputStreamTOString(entity.getContent());
                    result = result.substring(result.indexOf("{"), result.length());
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return result;
    }

    /**
     * InputStream转化成String
     *
     * @param in
     * @return
     * @throws Exception
     */
    private static String InputStreamTOString(InputStream in) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        int BUFFER_SIZE = 1024;
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);
        data = null;
        return new String(outStream.toByteArray());
    }

    /**
     * 生成post参数对
     *
     * @param map 提交的参数键值对
     * @return
     */
    private static ArrayList<NameValuePair> createNameValuePair(Map<String, Object> map) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        if (map != null)
            for (String name : map.keySet()) {
                pairs.add(new BasicNameValuePair(name, String.valueOf(map.get(name))));
            }
        return pairs;
    }

    /* ********************* HTTP GET ***************************** */
    public static Bitmap downloadImg(String uri, OnRequestListener listener) {
        InputStream is = _httpGet(uri);
//        if (is == null) {
//            if (listener != null)
//                listener.onError(null);
//            return null;
//        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        if (listener != null) {
            if (bitmap == null)
                listener.onError(null);
            else
                listener.onFinished(bitmap);
        }
        return bitmap;
    }

    public static String httpGetString(String uri) throws Exception {
        InputStream inputStream = _httpGet(uri);
        return InputStreamTOString(inputStream);
    }

    public static InputStream httpGetStream(String url) {
        return _httpGet(url);
    }

    /**
     * 使用默认的HttpClient和HttpGet去访问网址,返回 Inpustream 数据流是为了XML或者Jsoup 的解析处理
     *
     * @param uri 访问的网址，可以带有参数
     * @return返回 InputStream数据流，切记处理完成后要关闭连接
     */
    private static InputStream _httpGet(String uri) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(uri);
        InputStream inputStream = null;
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                inputStream = entity.getContent();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return inputStream;
        // shutdown...
    }
}
