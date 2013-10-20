package xmu.swordbearer.yuedu.offline;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.Calendar;

import xmu.swordbearer.yuedu.net.api.OnRequestListener;
import xmu.swordbearer.yuedu.net.api.QiniuAPI;
import xmu.swordbearer.yuedu.net.api.Utils;

/**
 * Created by SwordBearer on 13-8-24.
 */

/**
 * 专门下载图片
 */
public class DownloadPictureTask extends BaseDownloadTask {

    public DownloadPictureTask(Context mContext) {
        super(mContext);
    }

    @Override
    public void onStart(final OnRequestListener listener) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                QiniuAPI.getDailyCover(mContext, new OnRequestListener() {
                    @Override
                    public void onError(Object obj) {
                    }

                    @Override
                    public void onFinished(Object obj) {
                        if (obj == null) {
                            onError(null);
                        }
                        Bitmap bmpBg = (Bitmap) obj;
                        String name = Utils.calendar2Date(Calendar
                                .getInstance()) + ".png";
                        Utils.saveCache(mContext, name, bmpBg);
                        listener.onFinished(OfflineService.RESULT_PICTURE_OK);
                    }
                });
            }
        });
        thread.start();
    }
}
