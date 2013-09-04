package xmu.swordbearer.yuedu.offline;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by SwordBearer on 13-8-14.
 */
public class OfflineBroadcast extends BroadcastReceiver {
    private String TAG = "DownloadArticleTask";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(OfflineService.RESULT_MUSIC_OK)) {
            Log.e(TAG, "收到任务完成通知");
        } else if (action.equals(OfflineService.ACTION_OFFLINE_START)) {
            Log.e(TAG, "时间到，开始执行离线任务....");
            Intent startOffline = new Intent(OfflineService.ACTION_OFFLINE_START);
            context.startService(startOffline);
        } else if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            //重新计算离线任务时间
        }
    }
}
