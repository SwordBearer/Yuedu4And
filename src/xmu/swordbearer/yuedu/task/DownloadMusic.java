package xmu.swordbearer.yuedu.task;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import xmu.swordbearer.yuedu.bean.Music;

/**
 * Created by Administrator on 13-12-7.
 */

public class DownloadMusic extends AsyncTask<String, Integer, Void> {
    //    private String _uri;
//    private String _filePath;
//    private String _fileName;
//    private int _progress;
    private Music mContext;
    private Music _music;

    public DownloadMusic(Context mContext, Music music) {
        this.mContext = mContext;
        this._music = music;
    }

    @Override
    protected Void doInBackground(String... strings) {
        // take CPU lock to prevent CPU from going off if the user presses the power button during download
//        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//                getClass().getName());
//        wl.acquire();
        try {
            String url = strings[0];
//            InputStream inStream = NetHelper.httpGetStream(url);
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(uri);
            InputStream inputStream = null;
            long fileLength = 0;//file size
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    inputStream = entity.getContent();
                    fileLength = entity.getContentLength();
                    this._music.setPath(fileLength);
                    //
                    File file = new File(this._filePath);
                    file.mkdirs();
                    FileOutputStream fos = new FileOutputStream(file);

                    byte[] buff = new byte[1024];
                    int ln = 0;
                    while ((ln = inputStream.read(buff)) != -1) {
                        this._progress += ln;

                        fos.write(buff, 0, ln);
                    }
                    fos.close();
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
//        super.onProgressUpdate(values);
        this._music.setDownProgress(values[0]);
        //
        //update
    }
}
