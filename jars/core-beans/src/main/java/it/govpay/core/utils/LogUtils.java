/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.core.utils;

import org.slf4j.Logger;

/**
 * @author Pintori Giuliano (giuliano.pintori@link.it)
 * @author  $Author: pintori $
 *
 */
public class LogUtils {
	
	private LogUtils() {}
	
	public static void logDebugException(Logger log, String msg, Exception e) {
		if(log != null) {
			msg = sanitizeForLog(msg);
			
			if(e != null) {
				log.debug(msg, e);
			} else {
				log.debug(msg);
			}
		}
	}

	public static void logDebug(Logger log, String msg, Object ... params) {
		if(log != null) {
			msg = sanitizeForLog(msg);
			
			if(params != null && params.length > 0) {
				sanitizeForLog(params);
				
				log.debug(msg, params);
			} else {
				log.debug(msg);
			}
		}
	}

	public static void logInfoException(Logger log, String msg, Exception e) {
		if(log != null) {
			msg = sanitizeForLog(msg);
			
			if(e != null) {
				log.info(msg, e);
			} else {
				log.info(msg);
			}
		}
	}

	public static void logInfo(Logger log, String msg, Object ... params) {
		if(log != null) {
			msg = sanitizeForLog(msg);
			
			if(params != null && params.length > 0) {
				sanitizeForLog(params);
				
				log.info(msg, params);
			} else {
				log.info(msg);
			}
		}
	}

	public static void logWarnException(Logger log, String msg, Exception e) {
		if(log != null) {
			msg = sanitizeForLog(msg);
			
			if(e != null) {
				log.warn(msg, e);
			} else {
				log.warn(msg);
			}
		}
	}

	public static void logWarn(Logger log, String msg, Object ... params) {
		if(log != null) {
			msg = sanitizeForLog(msg);
			
			if(params != null && params.length > 0) {
				sanitizeForLog(params);
				
				log.warn(msg, params);
			} else {
				log.warn(msg);
			}
		}
	}
	
	public static void logError(Logger log, String msg) {
		if(log != null) {
			msg = sanitizeForLog(msg);
			
			log.error(msg);
		}
	}

	public static void logError(Logger log, String msg, Exception e) {
		if(log != null) {
			msg = sanitizeForLog(msg);
			
			if(e != null) {
				log.error(msg, e);
			} else {
				log.error(msg);
			}
		}
	}
	
	public static void logError(Logger log, String msg, Object ... params) {
		if(log != null) {
			msg = sanitizeForLog(msg);
			
			if(params != null && params.length > 0) {
				sanitizeForLog(params);
				
				log.error(msg, params);
			} else {
				log.error(msg);
			}
		}
	}

	public static void logTrace(Logger log, String msg, Object ... params) {
		if(log != null) {
			msg = sanitizeForLog(msg);
			
			if(params != null && params.length > 0) {
				sanitizeForLog(params);
				
				log.trace(msg, params);
			} else {
				log.trace(msg);
			}
		}
	}

	private static void sanitizeForLog(Object... params) {
		for (int i = 0; i < params.length; i++) {
			if (params[i] instanceof String data) {
				params[i] = sanitizeForLog(data);
			}
		}
	}
	
	private static String sanitizeForLog(String data) {
		if (data != null) {
			return data.replaceAll("[\n\r]", "_");
		} else {
			return null;
		}
	}
}
