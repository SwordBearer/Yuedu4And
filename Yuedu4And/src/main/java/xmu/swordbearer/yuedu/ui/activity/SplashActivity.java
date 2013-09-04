package xmu.swordbearer.yuedu.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import xmu.swordbearer.yuedu.R;
import xmu.swordbearer.yuedu.bean.Music;
import xmu.swordbearer.yuedu.db.DBManager;
import xmu.swordbearer.yuedu.music.MusicPlayerService;
import xmu.swordbearer.yuedu.net.MusicAPI;
import xmu.swordbearer.yuedu.net.NetHelper;
import xmu.swordbearer.yuedu.net.OnRequestListener;
import xmu.swordbearer.yuedu.net.QiniuAPI;
import xmu.swordbearer.yuedu.net.Utils;
import xmu.swordbearer.yuedu.utils.UiHelper;

/**
 * Created by SwordBearer on 13-8-11.
 */
public class SplashActivity extends Activity implements View.OnClickListener {
    private ImageView ivCover;
    private ImageButton btnSlide;
    private TextView tvMusic;
    private Music mMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, flag);
        setContentView(R.layout.activity_splash);
        //
        ivCover = (ImageView) findViewById(R.id.splash_iv_cover);
        tvMusic = (TextView) findViewById(R.id.splash_tv_music);
        btnSlide = (ImageButton) findViewById(R.id.splash_btn_slide);
        ivCover.setImageResource(R.drawable.default_bg);
        btnSlide.setOnClickListener(this);

        Utils.saveWindowSize(this);
        loadData();
        //测试：启动离线下载任务
//        Intent intent = new Intent(this, OfflineService.class);
//        intent.setAction(OfflineService.ACTION_OFFLINE_START);
//        startService(intent);
    }

    private void loadData() {
        if (!NetHelper.isNetworkConnected(this)) {
            UiHelper.showToast(this, R.string.not_access_network);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                MusicAPI.getDailyMusic(getMusicsListener);
            }
        }).start();
        new DailyCoverTask().execute(null);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://get cover
                    break;
                case 2://get music
                    tvMusic.setText(mMusic.details);
                    Intent intent = new Intent(SplashActivity.this, MusicPlayerService.class);
                    intent.setAction(MusicPlayerService.ACTION_START_PLAY);
                    intent.putExtra(MusicPlayerService.EXTRA_MUSIC, mMusic);
                    DBManager.addMusic(SplashActivity.this, mMusic);
                    startService(intent);
                    break;
                case 3://get music error
                    UiHelper.showToast(SplashActivity.this, R.string.get_data_failed);
                    break;
            }
        }
    };

    private OnRequestListener getMusicsListener = new OnRequestListener() {
        @Override
        public void onError(Object obj) {
            handler.obtainMessage(3).sendToTarget();
        }

        @Override
        public void onFinished(Object obj) {
            JSONArray ja = (JSONArray) obj;
            Log.e("TEST", "获得的音乐数 " + ja.length());
            if (ja.length() == 0) {
                onError(null);
            }
            try {
                mMusic = new Music(ja.getJSONObject(0));
                //save to DB
                handler.obtainMessage(2).sendToTarget();
                Log.e("TEST", "音乐获取完成 " + mMusic.url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onClick(View v) {
        if (v == btnSlide) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    private class DailyCoverTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            return QiniuAPI.getDailyCover(SplashActivity.this, null);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null)
                ivCover.setImageBitmap(bitmap);
            else {
                UiHelper.showToast(SplashActivity.this, R.string.get_data_failed);
            }
        }

    }
}
