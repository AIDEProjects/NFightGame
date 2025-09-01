package com.goldsprite.gdxcore.logs;

import com.badlogic.gdx.utils.Logger;

public class Log {

	public static void log(Object msg, Object... args) {
		log("main", msg==null?"null":msg.toString(), args);
	}
	public static void log(String tag, String msg, Object... args) {
		Logger logger = new Logger(tag, Logger.DEBUG);
		logger.info(String.format(msg, args));
	}

}
