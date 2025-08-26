package com.goldsprite.nfightgame.android;

import android.os.Bundle;
import android.view.KeyEvent;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import com.goldsprite.nfightgame.GdxLauncher;

public class AndroidGdxLauncher extends AndroidApplication {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLaunchOptions();
		UncaughtExceptionActivity.setUncaughtExceptionHandler(this, AndroidGdxLauncher.class);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useImmersiveMode = true;
		initialize(
			new GdxLauncher()
		, cfg);

//		//设置退出事件监听器
//		ScreenManager.exitGame.add(() -> {
//			finish();
//			android.os.Process.killProcess(android.os.Process.myPid());
//		});
	}

	private void initLaunchOptions() {
		ScreenUtils.hideBlackBar(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//用于多活动堆栈时按下返回直接回到桌面
			//moveTaskToBack(true);
			return true;//返回true以阻止本次操作
		}
		return super.onKeyDown(keyCode, event);
	}


}
