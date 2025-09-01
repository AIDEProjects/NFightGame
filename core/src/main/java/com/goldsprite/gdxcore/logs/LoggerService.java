package com.goldsprite.gdxcore.logs;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;

public class LoggerService {
	private static final Logger logger = new Logger(LoggerService.class.getName(), Logger.DEBUG);
	private Array<String> logMessages;

	public LoggerService() {
		logMessages = new Array<>();
	}

	public void log(String message, Object... args) {
		message = String.format(message, args);
		logger.setLevel(Logger.NONE);
		logger.info(message);
		logMessages.add("NONE: " + message);
	}

	public void logInfo(String message, Object... args) {
		message = String.format(message, args);
		logger.setLevel(Logger.INFO);
		logger.info(message);
		logMessages.add("INFO: " + message);
	}

	public void logDebug(String message, Object... args) {
		message = String.format(message, args);
		logger.setLevel(Logger.DEBUG);
		logger.debug(message);
		logMessages.add("DEBUG: " + message);
	}

	public void logError(String message, Object... args) {
		message = String.format(message, args);
		logger.setLevel(Logger.ERROR);
		logger.error(message);
		logMessages.add("ERROR: " + message);
	}

	// 获取日志消息
	public Array<String> getLogMessages() {
		return logMessages;
	}
}
