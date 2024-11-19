/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
			log.debug(msg, e);
		}
	}

	public static void logDebug(Logger log, String msg, Object ... params) {
		if(log != null) {
			log.debug(msg, params);
		}
	}

	public static void logInfoException(Logger log, String msg, Exception e) {
		if(log != null) {
			log.info(msg, e);
		}
	}

	public static void logInfo(Logger log, String msg, Object ... params) {
		if(log != null) {
			log.info(msg, params);
		}
	}

	public static void logWarnException(Logger log, String msg, Exception e) {
		if(log != null) {
			log.warn(msg, e);
		}
	}

	public static void logWarn(Logger log, String msg, Object ... params) {
		if(log != null) {
			log.warn(msg, params);
		}
	}
	
	public static void logError(Logger log, String msg) {
		if(log != null) {
			log.error(msg);
		}
	}

	public static void logError(Logger log, String msg, Exception e) {
		if(log != null) {
			log.error(msg, e);
		}
	}

	public static void logTrace(Logger log, String msg, Object ... params) {
		if(log != null) {
			log.trace(msg, params);
		}
	}

}
