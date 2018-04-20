package com.hhdb.csadmin.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogDefault {

	protected Logger logger;

	public static LogDefault getDefaultLogger(String logName) {
		LogDefault logDefault = new LogDefault();
		logDefault.logger = LogManager.getLogger(logName);
		return logDefault;
	}

	public void error(Exception e, String format, Object... args) {
		if (format != null) {
			logger.error(String.format(format, args));
		}
		error(e);
	}
	
	public void error(Exception e) {
		if (e != null) {
			logger.error(e.getMessage());
			StackTraceElement[] se = e.getStackTrace();
			for (int i = 0; i < se.length; i++) {
				logger.error(se[i].toString());
			}
		}
	}

	public void errorExit(Exception e, String format, Object... args) {
		error(e, format, args);
		System.exit(-1);
	}

	public void debug(String format, Object... args) {
		logger.debug(String.format(format, args));
	}
	
	public void debug(String format) {
		logger.debug(format);
	}

	public void info(String format, Object... args) {
		logger.info(String.format(format, args));
	}

}