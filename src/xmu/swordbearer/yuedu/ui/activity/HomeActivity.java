package xmu.swordbearer.yuedu.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import xmu.swordbearer.yuedu.R;
import xmu.swordbearer.yuedu.ui.fragment.ArticleListFragment;
import xmu.swordbearer.yuedu.ui.fragment.BasePageFragment;
import xmu.swordbearer.yuedu.ui.fragment.MusicListFragment;
import xmu.swordbearer.yuedu.ui.fragment.OfflineFragment;
import xmu.swordbearer.yuedu.ui.fragment.SettingsFragment;


/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-21.
 */
public class HomeActivity extends FragmentActivity implements
        ListView.OnItemClickListener {
    public static final String ACTION_TOGGLE_DRAWER = "action_toggle_drawer";

    private String[] navItems;
    private View leftNav;
    private DrawerLayout drawer;
    private ListView leftNavLv;
    private BasePageFragment fragArticleList, fragMusicList, fragOffline,
            fragSettings;

    private DrawerBroadCastReceiver drawerReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navItems = getResources().getStringArray(R.array.nav_items);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftNav = findViewById(R.id.home_left_drawer);
        leftNavLv = (ListView) findViewById(R.id.home_drawer_lv);
        leftNavLv.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, navItems));
        int[] icons = {R.drawable.left_nav_icon_home,
                R.drawable.left_nav_icon_music,
                R.drawable.left_nav_icon_offline,
                R.drawable.left_nav_icon_settings};
        leftNavLv.setAdapter(new NavListAdapter(this, icons, navItems));
        leftNavLv.setOnItemClickListener(this);
        drawer.openDrawer(leftNav);

        switchPage(0);
    }


    private void toggoleDrawer() {
        if (drawer.isDrawerOpen(leftNav)) {
            drawer.closeDrawer(leftNav);
        } else {
            drawer.openDrawer(leftNav);
        }
    }

    private void switchPage(int position) {
        BasePageFragment fragment = null;
        switch (position) {
            case 0:
                if (fragArticleList == null) {
                    fragArticleList = new ArticleListFragment();
                    Log.e("TEST", "新建 fragArticleList");
                }
                fragment = fragArticleList;
                break;
            case 1:
                if (fragMusicList == null)
                    fragMusicList = new MusicListFragment();
                fragment = fragMusicList;
                break;
            case 2:
                if (fragOffline == null)
                    fragOffline = new OfflineFragment();
                fragment = fragOffline;
                break;
            case 3:
                if (fragSettings == null) {
                    fragSettings = new SettingsFragment();
                }
                fragment = fragSettings;
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.home_content_frame, fragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (drawerReceiver == null)
            drawerReceiver = new DrawerBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_TOGGLE_DRAWER);
        registerReceiver(drawerReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(drawerReceiver);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        switchPage(position);
        leftNavLv.setItemChecked(position, true);
        drawer.closeDrawer(leftNav);
    }

    private class NavListAdapter extends BaseAdapter {
        private String[] navItems;
        private int[] navIcons;
        private LayoutInflater inflater;

        public NavListAdapter(Context context, int[] icons, String[] items) {
            this.navIcons = icons;
            this.navItems = items;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return navItems.length;
        }

        @Override
        public Object getItem(int position) {
            return navIcons[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.listitem_left_nav, null,
                    false);
            ImageView iv = (ImageView) convertView
                    .findViewById(R.id.left_nav_listitem_iv);
            TextView tv = (TextView) convertView
                    .findViewById(R.id.left_nav_listitem_tv);
            iv.setImageResource(navIcons[position]);
            tv.setText(navItems[position]);
            return convertView;
        }
    }

    public class DrawerBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("TEST", "收到drawer 通知");
            if (intent == null)
                return;
            String action = intent.getAction();
            if (action == null) return;
            if (action.equals(ACTION_TOGGLE_DRAWER)) {
                toggoleDrawer();
            }
        }
    }
}
