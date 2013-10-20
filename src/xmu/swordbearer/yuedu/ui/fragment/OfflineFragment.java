package xmu.swordbearer.yuedu.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xmu.swordbearer.yuedu.R;

/**
 * Created by SwordBearer on 13-8-21.
 */
public class OfflineFragment extends BasePageFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_offline, container, false);
        initViews(root);
        return root;
    }

    @Override
    protected void initViews(View rootView) {

    }

    @Override
    public void onClick(View v) {

    }
}