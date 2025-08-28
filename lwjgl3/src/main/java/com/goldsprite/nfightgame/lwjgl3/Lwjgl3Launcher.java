package com.goldsprite.nfightgame.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import com.goldsprite.nfightgame.GdxLauncher;

/**
 * Launches the desktop (LWJGL3) application.
 */
public class Lwjgl3Launcher {
	public static final float WORLD_WIDTH = 960+200, WORLD_HEIGHT = 540;

	public static void main(String[] args) {
		if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
		createApplication();
	}

	private static Lwjgl3Application createApplication() {
		return new Lwjgl3Application(new GdxLauncher(), getDefaultConfiguration());
	}

	private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
		Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		configuration.setTitle("NFightGame");
		//// Vsync limits the frames per second to what your hardware can display, and helps eliminate
		//// screen tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
		// 不使用垂直同步以避免窗口模式卡顿
		configuration.useVsync(false);
		//// Limits FPS to the refresh rate of the currently active monitor, plus 1 to try to match fractional
		//// refresh rates. The Vsync setting above should limit the actual FPS to match the monitor.
		// 如果不限制帧率注释即可
		configuration.setForegroundFPS(1000);
		// configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
		//// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
		//// useful for testing performance, but can also be very stressful to some hardware.
		//// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.
//		configuration.setWindowedMode(192*6, 108*6);
		configuration.setWindowedMode((int) WORLD_WIDTH, (int) WORLD_HEIGHT);
		//// You can change these files; they are in lwjgl3/src/main/resources/ .
		configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
		return configuration;
	}
}
