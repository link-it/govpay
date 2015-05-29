/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.ejb.core.utils;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;

public class GovPayLogger implements Logger {

	public enum Loggers {

		PSP_UPD("govpay_psp"),
		PAY_REQ("govpay_pay_req"),
		PAY_RSP("govpay_pay_rsp"),
		GDE("govpay_gde");
		
		private String loggerName;
		private Loggers(String loggerName) {
			this.loggerName = loggerName;
		}
		
		public String getLoggerName() {
			return loggerName;
		}
	}
	
	private String prefix;
	private Logger log;
	
	public GovPayLogger(Logger log, String identificativoCreditore, String iuv) {
		this.log = log;
		this.prefix = "[" + identificativoCreditore + "][" + iuv + "] ";
	}
	
	public GovPayLogger(Logger log, String uuid) {
		this.log = log;
		this.prefix = "[" + uuid + "] ";
	}

	@Override
	public void catching(Level level, Throwable t) {
		log.catching(level, t);
	}

	@Override
	public void catching(Throwable t) {
		log.catching(t);
		
	}

	@Override
	public void debug(Marker marker, Message msg) {
		log.debug(marker, msg);
	}

	@Override
	public void debug(Marker marker, Message msg, Throwable t) {
		log.debug(marker, msg, t);
		
	}

	@Override
	public void debug(Marker marker, Object message) {
		log.debug(marker, message);
	}

	@Override
	public void debug(Marker marker, Object message, Throwable t) {
		log.debug(marker, message, t);
	}

	@Override
	public void debug(Marker marker, String message) {
		log.debug(marker, message);
	}

	@Override
	public void debug(Marker marker, String message, Object... params) {
		log.debug(marker, message, params);
	}

	@Override
	public void debug(Marker marker, String message, Throwable t) {
		log.debug(marker, message, t);
		
	}

	@Override
	public void debug(Message msg) {
		log.debug(msg);
		
	}

	@Override
	public void debug(Message msg, Throwable t) {
		log.debug(msg, t);
	}

	@Override
	public void debug(Object message) {
		log.debug(message);
	}

	@Override
	public void debug(Object message, Throwable t) {
		log.debug(message, t);
	}

	@Override
	public void debug(String message) {
		log.debug(prefix + message);
	}

	@Override
	public void debug(String message, Object... params) {
		log.debug(message, params);
	}

	@Override
	public void debug(String message, Throwable t) {
		log.debug(message, t);
	}

	@Override
	public void entry() {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public void entry(Object... params) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void error(Marker marker, Message msg) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void error(Marker marker, Message msg, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void error(Marker marker, Object message) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void error(Marker marker, Object message, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void error(Marker marker, String message) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void error(Marker marker, String message, Object... params) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void error(Marker marker, String message, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void error(Message msg) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void error(Message msg, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void error(Object message) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void error(Object message, Throwable t) {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public void error(String message) {
		log.error(prefix + message);
	}

	@Override
	public void error(String message, Object... params) {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public void error(String message, Throwable t) {
		log.error(prefix + message, t);
	}

	@Override
	public void exit() {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public <R> R exit(R result) {
		return log.exit(result);
	}

	@Override
	public void fatal(Marker marker, Message msg) {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public void fatal(Marker marker, Message msg, Throwable t) {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public void fatal(Marker marker, Object message) {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public void fatal(Marker marker, Object message, Throwable t) {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public void fatal(Marker marker, String message) {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public void fatal(Marker marker, String message, Object... params) {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public void fatal(Marker marker, String message, Throwable t) {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public void fatal(Message msg) {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public void fatal(Message msg, Throwable t) {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public void fatal(Object message) {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public void fatal(Object message, Throwable t) {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public void fatal(String message) {
		log.fatal(prefix + message);
	}

	@Override
	public void fatal(String message, Object... params) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void fatal(String message, Throwable t) {
		log.fatal(prefix + message, t);
	}

	@Override
	public Level getLevel() {
		return log.getLevel();
	}

	@Override
	public MessageFactory getMessageFactory() {
		return log.getMessageFactory();
	}

	@Override
	public String getName() {
		return log.getName();
	}

	@Override
	public void info(Marker marker, Message msg) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void info(Marker marker, Message msg, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void info(Marker marker, Object message) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void info(Marker marker, Object message, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void info(Marker marker, String message) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void info(Marker marker, String message, Object... params) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void info(Marker marker, String message, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void info(Message msg) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void info(Message msg, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void info(Object message) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void info(Object message, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void info(String message) {
		log.info(prefix + message);
	}

	@Override
	public void info(String message, Object... params) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void info(String message, Throwable t) {
		log.fatal(prefix + message, t);
	}

	@Override
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	@Override
	public boolean isDebugEnabled(Marker marker) {
		return log.isDebugEnabled(marker);
	}

	@Override
	public boolean isEnabled(Level level) {
		return log.isEnabled(level);
	}

	@Override
	public boolean isEnabled(Level level, Marker marker) {
		return log.isEnabled(level, marker);
	}

	@Override
	public boolean isErrorEnabled() {
		return log.isErrorEnabled();
	}

	@Override
	public boolean isErrorEnabled(Marker marker) {
		return log.isErrorEnabled(marker);
	}

	@Override
	public boolean isFatalEnabled() {
		return log.isFatalEnabled();
	}

	@Override
	public boolean isFatalEnabled(Marker marker) {
		return log.isFatalEnabled(marker);
	}

	@Override
	public boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}

	@Override
	public boolean isInfoEnabled(Marker marker) {
		return log.isInfoEnabled(marker);
	}

	@Override
	public boolean isTraceEnabled() {
		return log.isTraceEnabled();
	}

	@Override
	public boolean isTraceEnabled(Marker marker) {
		return log.isTraceEnabled(marker);
	}

	@Override
	public boolean isWarnEnabled() {
		return log.isWarnEnabled();
	}

	@Override
	public boolean isWarnEnabled(Marker marker) {
		return log.isWarnEnabled(marker);
	}

	@Override
	public void log(Level level, Marker marker, Message msg) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void log(Level level, Marker marker, Message msg, Throwable t) {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public void log(Level level, Marker marker, Object message) {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public void log(Level level, Marker marker, Object message, Throwable t) {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public void log(Level level, Marker marker, String message) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void log(Level level, Marker marker, String message,
			Object... params) {
		throw new RuntimeException("Non implementato");
	}

	@Override
	public void log(Level level, Marker marker, String message, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void log(Level level, Message msg) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void log(Level level, Message msg, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void log(Level level, Object message) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void log(Level level, Object message, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void log(Level level, String message) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void log(Level level, String message, Object... params) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void log(Level level, String message, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void printf(Level level, Marker marker, String format,
			Object... params) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void printf(Level level, String format, Object... params) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public <T extends Throwable> T throwing(Level level, T t) {
		return log.throwing(level, t);
	}

	@Override
	public <T extends Throwable> T throwing(T t) {
		return log.throwing(t);
	}

	@Override
	public void trace(Marker marker, Message msg) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void trace(Marker marker, Message msg, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void trace(Marker marker, Object message) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void trace(Marker marker, Object message, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void trace(Marker marker, String message) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void trace(Marker marker, String message, Object... params) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void trace(Marker marker, String message, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void trace(Message msg) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void trace(Message msg, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void trace(Object message) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void trace(Object message, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void trace(String message) {
		log.trace(prefix + message);
	}

	@Override
	public void trace(String message, Object... params) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void trace(String message, Throwable t) {
		log.trace(prefix + message, t);
	}

	@Override
	public void warn(Marker marker, Message msg) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void warn(Marker marker, Message msg, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void warn(Marker marker, Object message) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void warn(Marker marker, Object message, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void warn(Marker marker, String message) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void warn(Marker marker, String message, Object... params) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void warn(Marker marker, String message, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void warn(Message msg) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void warn(Message msg, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void warn(Object message) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void warn(Object message, Throwable t) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void warn(String message) {
		log.warn(prefix + message);
	}

	@Override
	public void warn(String message, Object... params) {
		throw new RuntimeException("Non implementato");
		
	}

	@Override
	public void warn(String message, Throwable t) {
		log.warn(prefix + message, t);
	}
	

}
