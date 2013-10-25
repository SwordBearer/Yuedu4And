package xmu.swordbearer.yuedu.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import xmu.swordbearer.yuedu.R;
import xmu.swordbearer.yuedu.core.net.OnRequestListener;
import xmu.swordbearer.yuedu.core.offline.DownloadPictureTask;
import xmu.swordbearer.yuedu.utils.CommonUtils;

/**
 * Created by SwordBearer on 13-8-11.
 */
public class SplashActivity extends Activity implements View.OnClickListener {
    private ImageView ivCover;
    private ImageButton btnSlide;
    private TextView tvMusic;

    private static final int RESULT_GET_COVER_OK = 0x31;
    private static final int RESULT_GET_COVER_ERRO = 0x32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //
        ivCover = (ImageView) findViewById(R.id.splash_iv_cover);
        tvMusic = (TextView) findViewById(R.id.splash_tv_music);
        btnSlide = (ImageButton) findViewById(R.id.splash_btn_slide);
        btnSlide.setOnClickListener(this);

        showDailyCover();
        CommonUtils.saveWindowSize(this);
    }

    /**
     * 从数据库获取最新的封面图片
     */
    private void showDailyCover() {
        Log.e("TEST", "开始获取封面");
        // 下载封面
        new DownloadPictureTask(this).onStart(new OnRequestListener() {
            @Override
            public void onError(Object obj) {
            }

            @Override
            public void onFinished(Object obj) {
                if (null != obj) {
                    Message msg = handler.obtainMessage();
                    msg.obj = obj;
                    msg.what = RESULT_GET_COVER_OK;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == RESULT_GET_COVER_OK) {
                Bitmap bmp = (Bitmap) msg.obj;
                ivCover.setImageBitmap(bmp);
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
}
