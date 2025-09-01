package com.goldsprite.gdxcore.logs;

import com.badlogic.gdx.utils.Logger;

public class Log {

	public static void log(String tag, String msg, Object... args) {
		Logger logger = new Logger(tag);
		logger.info(String.format(msg, args));
	}

}
