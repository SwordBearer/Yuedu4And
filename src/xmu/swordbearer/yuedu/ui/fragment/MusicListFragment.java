package xmu.swordbearer.yuedu.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import xmu.swordbearer.yuedu.R;
import xmu.swordbearer.yuedu.core.app.YueduApp;
import xmu.swordbearer.yuedu.db.DBManager;
import xmu.swordbearer.yuedu.db.bean.Music;
import xmu.swordbearer.yuedu.db.bean.MusicListAdapter;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-21.
 */
public class MusicListFragment extends BasePageFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ImageButton btnRefresh;
    private ListView lvMusics;
    private ImageButton btnSlide;
    //
    private MusicListAdapter mAdapter;
    private List<Music> musicList = new ArrayList<Music>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_music_list, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    protected void initViews(View rootView) {
        btnSlide = (ImageButton) rootView.findViewById(R.id.music_btn_slide);
        btnRefresh = (ImageButton) rootView.findViewById(R.id.music_btn_refresh);
        lvMusics = (ListView) rootView.findViewById(R.id.music_lv);

        btnSlide.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        lvMusics.setOnItemClickListener(this);

        showMusics();
    }

    private void showMusics() {
        musicList = DBManager.getMusic(mContext);
        mAdapter = new MusicListAdapter(mContext, musicList);
        lvMusics.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSlide) {
            toggleDrawer();
        } else if (v == btnRefresh) {
            showMusics();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Music m = musicList.get(i);
        if (null != m) {
            YueduApp.startPlay(getActivity(), m);
        }
    }
}
