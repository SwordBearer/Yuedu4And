package xmu.swordbearer.yuedu.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import xmu.swordbearer.yuedu.R;
import xmu.swordbearer.yuedu.core.music.MusicPlayerService;
import xmu.swordbearer.yuedu.core.net.MusicAPI;
import xmu.swordbearer.yuedu.core.net.NetHelper;
import xmu.swordbearer.yuedu.core.net.OnRequestListener;
import xmu.swordbearer.yuedu.db.DBManager;
import xmu.swordbearer.yuedu.db.bean.Music;
import xmu.swordbearer.yuedu.utils.UiUtils;

/**
 * Created by SwordBearer on 13-8-17.
 */
public class MusicDashboard extends Fragment implements View.OnClickListener {
    private ImageButton btnPrev, btnNext, btnPlay;
    private SeekBar seekBar;
    private TextView tvName;

    private MusicDashBoardBroadcast broadcast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_music_dashboard,
                container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        btnPrev = (ImageButton) rootView.findViewById(R.id.dashboard_btn_pre);
        btnPlay = (ImageButton) rootView.findViewById(R.id.dashboard_btn_play);
        seekBar = (SeekBar) rootView.findViewById(R.id.dashboard_seekbar);
        btnNext = (ImageButton) rootView.findViewById(R.id.dashboard_btn_next);
        tvName = (TextView) rootView.findViewById(R.id.dashboard_music_name);

        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        loadData();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            UiUtils.showToast(getActivity(),
                    R.string.get_data_failed);
        }
    };

    private void loadData() {
        if (!NetHelper.isNetworkConnected(getActivity())) {
            UiUtils.showToast(getActivity(), R.string.not_access_network);
            return;
        }
        //读取音乐
        final OnRequestListener getMusicsListener = new OnRequestListener() {
            @Override
            public void onError(Object obj) {
                handler.obtainMessage(0).sendToTarget();
                startPlayMusic();//即使保存也要播放最新的音乐
            }

            @Override
            public void onFinished(Object obj) {
                JSONArray ja = (JSONArray) obj;
                Log.e("TEST", "获得的音乐数 " + ja.length());
                //没有获得音乐数据，不当做异常处理，播放以前的音乐，防止某一天没有音乐
                if (ja.length() > 0) {
                    try {
                        Music mMusic = new Music(ja.getJSONObject(0));
                        DBManager.addMusic(getActivity(), mMusic);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onError(null);
                        Log.e("TEST", "getDailyMusic 解析JSON出错了 " + obj.toString());
                    }
                }
                startPlayMusic();
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                MusicAPI.getDailyMusic(getMusicsListener);
            }
        }).start();
    }

    /**
     * 开始播放音乐
     */
    private void startPlayMusic() {
        //从数据库中读取音乐播放
        //播放最新的音乐，如果没有音乐文件，则直接播放
        //如果不允许非wifi情况播放音乐，则就播放其他已经下载音乐
        Log.e("TEST", "startPlayMusic 开始播放啦");
        List<Music> musicList = DBManager.getMusic(getActivity());
        if (musicList.size() == 0) return;
        Log.d("FUCK", "总音乐数 " + musicList.size());
        Music music = musicList.get(0);//获取最新的一条音乐
        Log.e("TEST", "dashboard---------BEFORE startService   " + musicList.size());
        Intent startPlay = new Intent(getActivity(), MusicPlayerService.class);
        startPlay.putExtra(MusicPlayerService.EXTRA_MUSIC, music);
        startPlay.setAction(MusicPlayerService.ACTION_MUSIC_START_PLAY);
        getActivity().startService(startPlay);
        Log.e("TEST", "dashboard---------AFTER startService");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.registerBroadcast();
    }

    @Override
    public void onDestroy() {
        unRegisterBroadcast();
        super.onDestroy();
    }

    /**
     * 注册广播，用于接收MediaPlayer的播放状态
     */
    private void registerBroadcast() {
        if (broadcast == null) {
            broadcast = new MusicDashBoardBroadcast();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicPlayerService.ACTION_MUSIC_START_PLAY);
        getActivity().registerReceiver(broadcast, filter);
        Log.e("TEST", "注册了广播");
    }

    //
    private void unRegisterBroadcast() {
        if (broadcast != null)
            getActivity().unregisterReceiver(broadcast);
    }

    private void playNext() {
    }

    private void playPrev() {
    }

    private void seekTo() {

    }

    @Override
    public void onClick(View v) {
        if (v == btnPrev) {
        } else if (v == btnNext) {
        } else if (v == btnPlay) {
            togglePlay();
        }
    }

    private void updateView(Music music) {
        tvName.setText(music.name + "-" + music.author);
    }

    private void togglePlay() {
        Log.e("TEST", "SeekBar单击事件");
        Intent pauseMusic = new Intent(getActivity(), MusicPlayerService.class);
        pauseMusic.setAction(MusicPlayerService.ACTION_MUSIC_TOGGLE_PLAY);
        getActivity().startService(pauseMusic);
    }

    public class MusicDashBoardBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("TEST", "MusicDashBoardBroadcast 收到广播");
            if (intent == null || intent.getAction() == null) {
                return;
            }
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            if (action.equals(MusicPlayerService.ACTION_MUSIC_START_PLAY)) {
                Music music = (Music) intent
                        .getSerializableExtra(MusicPlayerService.EXTRA_MUSIC);
                if (music == null) {
                    return;
                }
                Log.e("TEST-----", "=================更新播放界面");
                updateView(music);
            }
        }
    }
}
