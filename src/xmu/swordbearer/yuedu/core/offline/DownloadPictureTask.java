package xmu.swordbearer.yuedu.core.offline;

import android.content.Context;

import xmu.swordbearer.yuedu.R;
import xmu.swordbearer.yuedu.core.net.OnRequestListener;
import xmu.swordbearer.yuedu.core.net.PictureAPI;
import xmu.swordbearer.yuedu.utils.CommonUtils;
import xmu.swordbearer.yuedu.utils.UiUtils;

/**
 * @author SwordBearer  e-mail :ranxiedao@163.com
 *         Created by SwordBearer on 13-8-24.
 */

/**
 * 专门下载图片
 */
public class DownloadPictureTask extends BaseDownloadTask {

    public DownloadPictureTask(Context mContext) {
        super(mContext);
    }

    public void onStart(final OnRequestListener listener) {
        if (!CommonUtils.isSDCard()) {
            UiUtils.showToast(mContext, R.string.not_access_sdcard);
            return;
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                PictureAPI.getDailyCover(mContext, listener);
            }
        });
        thread.start();
    }
}
