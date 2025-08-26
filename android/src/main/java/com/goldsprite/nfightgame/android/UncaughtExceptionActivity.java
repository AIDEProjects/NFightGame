package com.goldsprite.nfightgame.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;

public class UncaughtExceptionActivity extends Activity {
	public static final String launchMode = "launchMode";
	public static final int launchMode_normal = 0;
	public static final int launchMode_crash = 1;
	public static UncaughtExceptionActivity instance;
	private static Class<?> targetClazz;

	public static void exceptionDaialog(String title, String inMsg, AlertDialog.OnDismissListener callback) {
		instance.runOnUiThread(() -> {
			String msg = inMsg;
			Dialog dialog = new Dialog(instance);
			dialog.setTitle(title);

			String c = new String(new char[120]).replace('\0', 'x');
			msg = c + "\n" + msg + new String(new char[10]).replace('\0', '\n');

			//java代码布局
			//manualLayout(dialog, msg);

			//xml资源布局
			dialog.setContentView(R.layout.dialog_exception_layout);
			TextView tv = dialog.findViewById(R.id.dialog_exception_errMsg);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				msg = formatColorString(msg, "#FF0000");
				tv.setText(Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT));
			} else {
				tv.setText(msg);
			}

			if (callback != null)
				dialog.setOnDismissListener(callback);
			dialog.show();
		});
	}

	private static void manualLayout(Dialog dialog, String msg) {
		ScrollView sv = new ScrollView(instance);
		HorizontalScrollView hsv = new HorizontalScrollView(instance);
		TextView tv = new TextView(instance);
		tv.setTextIsSelectable(true);
		//tv.setTypeface(Typeface.MONOSPACE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			msg = formatColorString(msg, "#FF0000");
			tv.setText(Html.fromHtml(msg, Html.FROM_HTML_MODE_COMPACT));
		} else {
			tv.setText(msg);
		}
		hsv.addView(tv);
		sv.addView(hsv);

		dialog.setContentView(sv);
	}

	public static <T extends Activity> void setUncaughtExceptionHandler(T activity, Class<? extends Activity> clazz) {
		targetClazz = clazz;
		Thread.setDefaultUncaughtExceptionHandler(
			new Thread.UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(Thread t, Throwable e) {
					activity.runOnUiThread(() -> {
						String title = "全局未捕获异常: " + e.getMessage();
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw));
						String msg =
							"Thread: " + t
								+ "\nStackTrace: " + sw;
						Intent i = new Intent(activity, UncaughtExceptionActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						i.putExtra(launchMode, launchMode_crash);
						i.putExtra("title", title);
						i.putExtra("msg", msg);
						activity.startActivity(i);
						android.os.Process.killProcess(android.os.Process.myPid());
					});
				}
			}
		);
	}

	public static String formatColorString(String str, String hexColor) {
		str = str.replace("\n", "<br>");
		str = String.format("<font color='%s'>%s</font>", hexColor, str);
		return str;
	}

	@Override
	public void onCreate(Bundle savedBundleState) {
		super.onCreate(savedBundleState);
		instance = this;

		RelativeLayout layout = new RelativeLayout(this);
		layout.setBackgroundColor(Color.BLACK);
		layout.setGravity(Gravity.CENTER);
		setContentView(layout);

		Intent launchIntent = getIntent();
		if (launchIntent.getIntExtra(launchMode, launchMode_normal) == launchMode_crash) {
			String title = launchIntent.getStringExtra("title");
			String msg = launchIntent.getStringExtra("msg");
			exceptionDaialog(title, msg, (dislog) -> {
				launchGame();
			});
		}
	}

	private void launchGame() {
		//targetClazz = AndroidGdxLauncher.class;
		Intent i = new Intent(instance, targetClazz);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		instance.startActivity(i);
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}

