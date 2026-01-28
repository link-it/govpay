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
package it.govpay.rs.eventi;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.ext.logging.event.LogEvent;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedWriter;
import org.apache.cxf.message.Message;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.IContext;

import it.govpay.core.beans.EventoContext;
import it.govpay.core.beans.EventoContext.Componente;
import it.govpay.core.dao.configurazione.ConfigurazioneDAO;
import it.govpay.core.dao.configurazione.dto.LeggiConfigurazioneDTO;
import it.govpay.core.dao.configurazione.dto.LeggiConfigurazioneDTOResponse;
import it.govpay.core.utils.client.HttpMethod;
import it.govpay.core.utils.eventi.EventiUtils;
import it.govpay.model.configurazione.GdeEvento;
import it.govpay.model.configurazione.GdeInterfaccia;
import it.govpay.model.configurazione.Giornale;

public class GiornaleEventiUtilities {

	private GiornaleEventiUtilities() {}

	public static GdeInterfaccia getConfigurazioneGiornaleEventi (IContext context, ConfigurazioneDAO configurazioneDAO, GiornaleEventiConfig giornaleEventiConfig) throws ServiceException, it.govpay.core.exceptions.IOException {
		LeggiConfigurazioneDTO leggiConfigurazioneDTO = new LeggiConfigurazioneDTO(context.getAuthentication());
		LeggiConfigurazioneDTOResponse configurazione = configurazioneDAO.getConfigurazione(leggiConfigurazioneDTO);
		Giornale giornale = configurazione.getConfigurazione().getGiornale();

		return EventiUtils.getConfigurazioneComponente(giornaleEventiConfig.getApiNameEnum(), giornale);
	}

    public static String safeGet(Message message, String key) {
        if (message == null || !message.containsKey(key)) {
            return null;
        }
        Object value = message.get(key);
        return (value instanceof String) ? value.toString() : null;
    }

	public static boolean dumpEvento(GdeEvento evento, Integer responseCode) {
		return EventiUtils.dumpEvento(evento, responseCode);
	}

	public static boolean logEvento(GdeEvento evento, Integer responseCode) {
		return EventiUtils.logEvento(evento, responseCode);
	}

	public static boolean dumpEvento(GdeEvento evento, EventoContext.Esito esito) {
		return EventiUtils.dumpEvento(evento, esito);
	}

	public static boolean logEvento(GdeEvento evento, EventoContext.Esito esito) {
		return EventiUtils.logEvento(evento, esito);
	}

	public static HttpMethod getHttpMethod(String httpMethod) {
		return EventiUtils.getHttpMethod(httpMethod);
	}

	public static boolean isRequestLettura(HttpMethod httpMethod, Componente componente, String operazione) {
		return EventiUtils.isRequestLettura(httpMethod,componente,operazione);
	}

	public static boolean isRequestScrittura(HttpMethod httpMethod, Componente componente, String operazione) {
		return EventiUtils.isRequestScrittura(httpMethod,componente,operazione);
	}


	public static  void addContent(Message message, final LogEvent event, GiornaleEventiConfig config) {
		try {
			CachedOutputStream cos = message.getContent(CachedOutputStream.class);
			if (cos != null) {
				handleOutputStream(event, message, cos, config);
			} else {
				CachedWriter writer = message.getContent(CachedWriter.class);
				if (writer != null) {
					handleWriter(event, writer, config);
				}
			}
		} catch (IOException e) {
			throw new Fault(e);
		}
	}

	public static  void handleOutputStream(final LogEvent event, Message message, CachedOutputStream cos, GiornaleEventiConfig config) throws IOException {
		String encoding = (String) message.get(Message.ENCODING);
		if (StringUtils.isEmpty(encoding)) {
			encoding = StandardCharsets.UTF_8.name();
		}
		StringBuilder payload = new StringBuilder();
		cos.writeCacheTo(payload, encoding, config.getLimit());
		cos.close();
		event.setPayload(payload.toString());
		boolean isTruncated = cos.size() > config.getLimit() && config.getLimit() != -1;
		event.setTruncated(isTruncated);
		event.setFullContentFile(cos.getTempFile());
	}

	public static  void handleWriter(final LogEvent event, CachedWriter writer, GiornaleEventiConfig config) throws IOException {
		boolean isTruncated = writer.size() > config.getLimit() && config.getLimit() != -1;
		StringBuilder payload = new StringBuilder();
		writer.writeCacheTo(payload, config.getLimit());
		event.setPayload(payload.toString());
		event.setTruncated(isTruncated);
		event.setFullContentFile(writer.getTempFile());
	}

}
