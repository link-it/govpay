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
package it.govpay.core.utils.logger;

import java.text.MessageFormat;

import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

/**
 * @author Pintori Giuliano (giuliano.pintori@link.it)
 * @author  $Author: pintori $
 *
 */
public class MessaggioDiagnosticoUtils {

	private static final String ERROR_MSG_ERRORE_DURANTE_IL_SALVATAGGIO_DEL_MESSAGGIO_DIAGNOSTICO_0_LOG_DELL_OPERAZIONE_1 = "Errore durante il salvataggio del messaggio diagnostico {0} log dell''operazione: {1}";
	private static final String ERROR_MSG_ERRORE_DURANTE_IL_LOG_DELL_OPERAZIONE_0 = "Errore durante il log dell''operazione: {0}";

	private MessaggioDiagnosticoUtils () {/*static only*/}
	
	public static void log(Logger log, IContext ctx) {
		if(ctx != null) {
			try {
				ctx.getApplicationLogger().log();
			} catch (UtilsException e) {
				log.error(MessageFormat.format(ERROR_MSG_ERRORE_DURANTE_IL_LOG_DELL_OPERAZIONE_0, e.getMessage()),e);
			}
		}
	}

	public static void logMessaggioDiagnostico(Logger log, IContext ctx, String idMessaggioDiagnostico) {
		if(ctx != null) {
			try {
				ctx.getApplicationLogger().log(idMessaggioDiagnostico);
			} catch (UtilsException e) {
				log.error(MessageFormat.format(ERROR_MSG_ERRORE_DURANTE_IL_SALVATAGGIO_DEL_MESSAGGIO_DIAGNOSTICO_0_LOG_DELL_OPERAZIONE_1, idMessaggioDiagnostico,  e.getMessage()),e);
			}
		}
	}

	public static void logMessaggioDiagnostico(Logger log, IContext ctx, String idMessaggioDiagnostico, Object arg) {
		if(ctx != null) {
			try {
				ctx.getApplicationLogger().log(idMessaggioDiagnostico, arg);
			} catch (UtilsException e) {
				log.error(MessageFormat.format(ERROR_MSG_ERRORE_DURANTE_IL_SALVATAGGIO_DEL_MESSAGGIO_DIAGNOSTICO_0_LOG_DELL_OPERAZIONE_1, idMessaggioDiagnostico,  e.getMessage()),e);
			}
		}
	}

	public static void logMessaggioDiagnostico(Logger log, IContext ctx, String idMessaggioDiagnostico, String arg) {
		if(ctx != null) {
			try {
				ctx.getApplicationLogger().log(idMessaggioDiagnostico, arg);
			} catch (UtilsException e) {
				log.error(MessageFormat.format(ERROR_MSG_ERRORE_DURANTE_IL_SALVATAGGIO_DEL_MESSAGGIO_DIAGNOSTICO_0_LOG_DELL_OPERAZIONE_1, idMessaggioDiagnostico,  e.getMessage()),e);
			}
		}
	}

	public static void logMessaggioDiagnostico(Logger log, IContext ctx, String idMessaggioDiagnostico, String ... args) {
		if(ctx != null) {
			try {
				ctx.getApplicationLogger().log(idMessaggioDiagnostico, args);
			} catch (UtilsException e) {
				log.error(MessageFormat.format(ERROR_MSG_ERRORE_DURANTE_IL_SALVATAGGIO_DEL_MESSAGGIO_DIAGNOSTICO_0_LOG_DELL_OPERAZIONE_1, idMessaggioDiagnostico,  e.getMessage()),e);
			}
		}
	}
}
