package xmu.swordbearer.yuedu.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;

import xmu.swordbearer.yuedu.R;
import xmu.swordbearer.yuedu.music.MusicPlayerService;
import xmu.swordbearer.yuedu.net.Utils;

/**
 * Created by SwordBearer on 13-8-17.
 */
public class MusicDashboard extends Fragment implements View.OnClickListener {
    private ImageButton btnPrev, btnNext, btnPlay;
    private SeekBar seekBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_music_dashboard, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        btnPrev = (ImageButton) rootView.findViewById(R.id.dashboard_btn_pre);
        btnPlay = (ImageButton) rootView.findViewById(R.id.dashboard_btn_play);
        seekBar = (SeekBar) rootView.findViewById(R.id.dashboard_seekbar);
        btnNext = (ImageButton) rootView.findViewById(R.id.dashboard_btn_next);

        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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

    private void updateView() {

    }

    private void togglePlay() {
        Log.e("TEST", "SeekBar单击事件");
        Intent pauseMusic = new Intent(getActivity(), MusicPlayerService.class);
        pauseMusic.setAction(MusicPlayerService.ACTION_TOGGLE_PLAY);
        getActivity().startService(pauseMusic);
        Utils.saveDashboardSize(getActivity(), getView());
    }

    private class MusicPlayerBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }
}
