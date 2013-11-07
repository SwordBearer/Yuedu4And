package xmu.swordbearer.yuedu.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import xmu.swordbearer.yuedu.R;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-12.
 */
public class UiUtils {
    public static void showToast(Context ctx, int msgId) {
        Toast toast = new Toast(ctx);
        LayoutInflater inflater = LayoutInflater.from(ctx);
        TextView tv = (TextView) inflater.inflate(R.layout.custom_toast, null, false);
        tv.setText(msgId);
        toast.setView(tv);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }
}
