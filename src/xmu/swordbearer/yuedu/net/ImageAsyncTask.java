package xmu.swordbearer.yuedu.net;

import xmu.swordbearer.yuedu.R;
import xmu.swordbearer.yuedu.bean.Image;
import xmu.swordbearer.yuedu.net.api.QiniuAPI;
import xmu.swordbearer.yuedu.utils.UiHelper;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
	private ImageView imgView;
	private Context context;
	private Image img;

	public ImageAsyncTask(Context context, ImageView iv, Image img) {
		this.context = context;
		this.imgView = iv;
		this.img = img;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		return QiniuAPI.getPicture(img.url, img.mode, img.width, img.height,
				img.format, null);
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (bitmap != null) {
			Log.e("TEST",
					"得到的图片尺寸是 " + bitmap.getWidth() + ":" + bitmap.getHeight());
			imgView.setImageBitmap(bitmap);
		} else {
			UiHelper.showToast(context, R.string.get_data_failed);
		}
	}
}
