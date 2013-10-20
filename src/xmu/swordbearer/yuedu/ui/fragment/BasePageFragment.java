package xmu.swordbearer.yuedu.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import xmu.swordbearer.yuedu.ui.activity.HomeActivity;

/**
 * Created by SwordBearer on 13-8-21.
 */
public abstract class BasePageFragment extends Fragment implements
        View.OnClickListener {
    protected Context mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    protected abstract void initViews(View rootView);

    protected void toggleDrawer() {
        Log.e("TEST", "发送drawer 通知");
        Intent intent = new Intent(HomeActivity.ACTION_TOGGLE_DRAWER);
        getActivity().sendBroadcast(intent);
    }
}
