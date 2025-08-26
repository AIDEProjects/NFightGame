package com.goldsprite.nfightgame.android;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

public class ScreenUtils {
	/**
	 * @param activity 隐藏Android p黑条的问题（三星A30s）
	 */
	public static void hideBlackBar(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
			lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
			activity.getWindow().setAttributes(lp);
		}
	}
}
