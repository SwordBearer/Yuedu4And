package xmu.swordbearer.yuedu.offline;

import android.content.Context;

import xmu.swordbearer.yuedu.net.api.OnRequestListener;

/**
 * Created by Administrator on 13-8-14.
 */
public abstract class BaseDownloadTask {
    protected Context mContext;
    protected Thread thread;

    public BaseDownloadTask(Context mContext) {
        this.mContext = mContext;
    }

    public void stop() {
        if (!thread.isInterrupted()) {
            thread.interrupt();
        }
    }

    public abstract void onStart(OnRequestListener listener);
}
