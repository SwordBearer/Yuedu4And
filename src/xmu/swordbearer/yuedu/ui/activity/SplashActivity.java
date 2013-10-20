package xmu.swordbearer.yuedu.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import xmu.swordbearer.yuedu.R;
import xmu.swordbearer.yuedu.net.api.Utils;

/**
 * Created by SwordBearer on 13-8-11.
 */
public class SplashActivity extends Activity implements View.OnClickListener {
    private ImageView ivCover;
    private ImageButton btnSlide;
    private TextView tvMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //
        ivCover = (ImageView) findViewById(R.id.splash_iv_cover);
        tvMusic = (TextView) findViewById(R.id.splash_tv_music);
        btnSlide = (ImageButton) findViewById(R.id.splash_btn_slide);
        ivCover.setImageResource(R.drawable.default_bg);
        btnSlide.setOnClickListener(this);

        Utils.saveWindowSize(this);
        showDailyCover();
    }

    /**
     * 从数据库获取最新的封面图片
     */
    private void showDailyCover() {
    }


    @Override
    public void onClick(View v) {
        if (v == btnSlide) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }
}
