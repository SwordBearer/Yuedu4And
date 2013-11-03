package xmu.swordbearer.yuedu.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import xmu.swordbearer.yuedu.R;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-21.
 */
public class SettingsFragment extends BasePageFragment {
    private ImageButton btnSlide;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_settings, container, false);
        initViews(root);
        return root;
    }

    @Override
    protected void initViews(View rootView) {
        btnSlide = (ImageButton) rootView.findViewById(R.id.settings_btn_slide);

        btnSlide.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSlide) {
            toggleDrawer();
        }
    }
}

