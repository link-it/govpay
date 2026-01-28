/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils.trasformazioni;

import java.text.MessageFormat;
import java.util.List;

import org.openspcoop2.utils.regexp.RegExpException;
import org.openspcoop2.utils.regexp.RegExpNotFoundException;
import org.openspcoop2.utils.regexp.RegularExpressionEngine;
import org.slf4j.Logger;

import it.govpay.core.utils.LogUtils;
import it.govpay.core.utils.trasformazioni.exception.TrasformazioneException;

public class URLRegExpExtractor {

	private static final String ERROR_MSG_ESTRAZIONE_0_FALLITA_1 = "Estrazione ''{0}'' fallita: {1}";
	private static final String DEBUG_MSG_ESTRAZIONE_0_NON_HA_TROVATO_RISULTATI_1 = "Estrazione ''{0}'' non ha trovato risultati: {1}";

	private Logger log;

	private String url;

	public URLRegExpExtractor(String url, Logger log) {
		this.url = url;
		this.log = log;
	}

	public boolean match(String pattern) throws TrasformazioneException {
		String v = read(pattern);
		return v!=null && !"".equals(v);
	}

	public String read(String pattern) throws TrasformazioneException {
		String valore = null;
		try {
			valore = RegularExpressionEngine.getStringMatchPattern(this.url, pattern);
		}
		catch(RegExpNotFoundException e){
			LogUtils.logDebug(log, MessageFormat.format(DEBUG_MSG_ESTRAZIONE_0_NON_HA_TROVATO_RISULTATI_1, pattern, e.getMessage()),e);
		}
		catch(RegExpException e){
			throw new TrasformazioneException(e.getMessage(),e);
		}
		catch(Exception e){
			throw new TrasformazioneException(MessageFormat.format(ERROR_MSG_ESTRAZIONE_0_FALLITA_1, pattern, e.getMessage()),e);
		}
		return valore;
	}

	public List<String> readList(String pattern) throws TrasformazioneException {
		List<String> valore = null;
		try {
			valore = RegularExpressionEngine.getAllStringMatchPattern(this.url, pattern);
		}
		catch(RegExpNotFoundException e){
			LogUtils.logDebug(log, MessageFormat.format(DEBUG_MSG_ESTRAZIONE_0_NON_HA_TROVATO_RISULTATI_1, pattern, e.getMessage()),e);
		}
		catch(RegExpException e){
			throw new TrasformazioneException(e.getMessage(),e);
		}
		catch(Exception e){
			throw new TrasformazioneException(MessageFormat.format(ERROR_MSG_ESTRAZIONE_0_FALLITA_1, pattern, e.getMessage()),e);
		}
		return valore;
	}



	public boolean found(String pattern) throws TrasformazioneException {
		String v = find(pattern);
		return v!=null && !"".equals(v);
	}

	public String find(String pattern) throws TrasformazioneException {
		String valore = null;
		try {
			valore = RegularExpressionEngine.getStringFindPattern(this.url, pattern);
		}
		catch(RegExpNotFoundException e){
			LogUtils.logDebug(log, MessageFormat.format(DEBUG_MSG_ESTRAZIONE_0_NON_HA_TROVATO_RISULTATI_1, pattern, e.getMessage()),e);
		}
		catch(RegExpException e){
			throw new TrasformazioneException(e.getMessage(),e);
		}
		catch(Exception e){
			throw new TrasformazioneException(MessageFormat.format(ERROR_MSG_ESTRAZIONE_0_FALLITA_1, pattern, e.getMessage()),e);
		}
		return valore;
	}

	public List<String> findAll(String pattern) throws TrasformazioneException {
		List<String> valore = null;
		try {
			valore = RegularExpressionEngine.getAllStringFindPattern(this.url, pattern);
		}
		catch(RegExpNotFoundException e){
			LogUtils.logDebug(log, MessageFormat.format(DEBUG_MSG_ESTRAZIONE_0_NON_HA_TROVATO_RISULTATI_1, pattern, e.getMessage()),e);
		}
		catch(RegExpException e){
			throw new TrasformazioneException(e.getMessage(),e);
		}
		catch(Exception e){
			throw new TrasformazioneException(MessageFormat.format(ERROR_MSG_ESTRAZIONE_0_FALLITA_1, pattern, e.getMessage()),e);
		}
		return valore;
	}
}
