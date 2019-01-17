/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.bd.model.converter;

import java.util.Arrays;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

import it.govpay.bd.model.Evento;
import it.govpay.bd.model.eventi.EventoCooperazione;
import it.govpay.bd.model.eventi.EventoIntegrazione;
import it.govpay.bd.model.eventi.EventoNota;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Evento.CategoriaEvento;
import it.govpay.orm.IdPagamentoPortale;
import it.govpay.orm.IdVersamento;

public class EventoConverter {

	public static it.govpay.orm.Evento toVO(Evento dto) {
		it.govpay.orm.Evento vo = new it.govpay.orm.Evento();
		vo.setCategoriaEvento(dto.getCategoriaEvento().getCodifica());
		vo.setCcp(dto.getCcp());
		vo.setCodDominio(dto.getCodDominio());
		vo.setData(dto.getData());
		vo.setDettaglio(dto.getDettaglio());
		vo.setClassnameDettaglio(dto.getClassnameDettaglio());
		vo.setIntervallo(dto.getIntervallo() != null ? dto.getIntervallo() : 0l); 
		vo.setId(dto.getId());
		vo.setIuv(dto.getIuv());
		vo.setTipoEvento(dto.getTipoEvento());
		vo.setSottotipoEvento(dto.getSottotipoEvento());
		if(dto.getIdVersamento() != null && dto.getIdVersamento() > 0) {
			IdVersamento idVersamento = new IdVersamento();
			idVersamento.setId(dto.getIdVersamento());
			vo.setIdVersamento(idVersamento );
		}
		
		if(dto.getIdPagamentoPortale() != null && dto.getIdPagamentoPortale() > 0) {
			IdPagamentoPortale idPagamentoPortale = new IdPagamentoPortale();
			idPagamentoPortale.setId(dto.getIdPagamentoPortale());
			vo.setIdPagamentoPortale(idPagamentoPortale);
		}
		/*
		 		vo.setCategoriaEvento(dto.getCategoriaEvento().getCodifica());
		vo.setCcp(dto.getCcp());
		vo.setCodCanale(dto.getCodCanale());
		vo.setCodDominio(dto.getCodDominio());
		vo.setCodPsp(dto.getCodPsp());
		vo.setCodStazione(dto.getCodStazione());
		vo.setComponente(dto.getComponente());
		vo.setData1(dto.getDataRichiesta());
		vo.setData2(dto.getDataRisposta());
		vo.setErogatore(dto.getErogatore());
		vo.setEsito(dto.getEsito());
		vo.setFruitore(dto.getFruitore());
		vo.setId(dto.getId());
		vo.setIuv(dto.getIuv());
		vo.setParametri1(dto.getAltriParametriRichiesta());
		vo.setParametri2(dto.getAltriParametriRisposta());
		vo.setTipoEvento(dto.getTipoEvento().toString());
		if(dto.getTipoVersamento() != null)
			vo.setTipoVersamento(dto.getTipoVersamento().getCodifica());
		 * */
		
		
		return vo;
	}
	
	
	
	public static Evento toDTO(it.govpay.orm.Evento vo) throws ServiceException{
		Evento dto = new Evento();
		if(vo.getCategoriaEvento() != null)
		dto.setCategoriaEvento(CategoriaEvento.toEnum(vo.getCategoriaEvento()));
		dto.setCcp(vo.getCcp());
		dto.setCodDominio(vo.getCodDominio());
		dto.setData(vo.getData());
		dto.setDettaglio(vo.getDettaglio());
		dto.setClassnameDettaglio(vo.getClassnameDettaglio());
		dto.setId(vo.getId());
		dto.setIuv(vo.getIuv());
		dto.setTipoEvento(vo.getTipoEvento());
		dto.setSottotipoEvento(vo.getSottotipoEvento());
		dto.setIntervallo(vo.getIntervallo()); 
		
		if(vo.getIdVersamento() != null)
			dto.setIdVersamento(vo.getIdVersamento().getId());
		
		if(vo.getIdPagamentoPortale() != null)
			dto.setIdPagamentoPortale(vo.getIdPagamentoPortale().getId());
		
		/*
		 if(vo.getCategoriaEvento() != null)
		dto.setCategoriaEvento(CategoriaEvento.toEnum(vo.getCategoriaEvento()));
		dto.setCcp(vo.getCcp());
		dto.setCodCanale(vo.getCodCanale());
		dto.setCodDominio(vo.getCodDominio());
		dto.setCodPsp(vo.getCodPsp());
		dto.setCodStazione(vo.getCodStazione());
		dto.setComponente(vo.getComponente());
		dto.setDataRichiesta(vo.getData1());
		dto.setDataRisposta(vo.getData2());
		dto.setErogatore(vo.getErogatore());
		dto.setEsito(vo.getEsito());
		dto.setFruitore(vo.getFruitore());
		dto.setId(vo.getId());
		dto.setIuv(vo.getIuv());
		dto.setAltriParametriRichiesta(vo.getParametri1());
		dto.setAltriParametriRisposta(vo.getParametri2());
		if(vo.getTipoEvento() != null)
			dto.setTipoEvento(TipoEvento.valueOf(vo.getTipoEvento()));
		if(vo.getTipoVersamento() != null)
			dto.setTipoVersamento(TipoVersamento.toEnum(vo.getTipoVersamento()));
		 * */
		
		return dto;
	}

	public static Evento fromEventoCooperazioneToEvento(EventoCooperazione eventoCooperazione) throws IOException {
		Evento evento = new Evento();
		
		evento.setCategoriaEvento(eventoCooperazione.getCategoriaEvento());
		evento.setCcp(eventoCooperazione.getCcp());
		evento.setCodDominio(eventoCooperazione.getCodDominio());
		evento.setData(eventoCooperazione.getDataRichiesta());
		evento.setClassnameDettaglio(EventoCooperazione.class.getName());
		evento.setDettaglio(getDettaglioEventoCooperazione(eventoCooperazione)); 
		evento.setIdPagamentoPortale(eventoCooperazione.getIdPagamentoPortale()); 
		evento.setIdVersamento(eventoCooperazione.getIdVersamento());
		if(eventoCooperazione.getDataRisposta() != null) {
			if(eventoCooperazione.getDataRichiesta() != null) {
				evento.setIntervallo(eventoCooperazione.getDataRisposta().getTime() - eventoCooperazione.getDataRichiesta().getTime());
			} else {
				evento.setIntervallo(0l);
			}
		} else {
			evento.setIntervallo(0l);
		}
		evento.setIuv(eventoCooperazione.getIuv());
		evento.setSottotipoEvento(eventoCooperazione.getSottotipoEvento());
		if(eventoCooperazione.getTipoEvento() != null)
			evento.setTipoEvento(eventoCooperazione.getTipoEvento().toString());
		
		
		return evento;
	}
	
	public static Evento fromEventointegrazioneToEvento(EventoIntegrazione eventointegrazione) throws IOException {
		Evento evento = new Evento();
		
		evento.setCategoriaEvento(eventointegrazione.getCategoriaEvento());
		evento.setCcp(eventointegrazione.getCcp());
		evento.setCodDominio(eventointegrazione.getCodDominio());
		evento.setData(eventointegrazione.getDataRichiesta());
		evento.setClassnameDettaglio(EventoIntegrazione.class.getName());
		evento.setDettaglio(getDettaglioEventoIntegrazione(eventointegrazione)); 
		evento.setIdPagamentoPortale(eventointegrazione.getIdPagamentoPortale()); 
		evento.setIdVersamento(eventointegrazione.getIdVersamento());
		if(eventointegrazione.getDataRisposta() != null) {
			if(eventointegrazione.getDataRichiesta() != null) {
				evento.setIntervallo(eventointegrazione.getDataRisposta().getTime() - eventointegrazione.getDataRichiesta().getTime());
			} else {
				evento.setIntervallo(0l);
			}
		} else {
			evento.setIntervallo(0l);
		}
		evento.setIuv(eventointegrazione.getIuv());
		evento.setSottotipoEvento(eventointegrazione.getSottotipoEvento());
		if(eventointegrazione.getTipoEvento() != null)
			evento.setTipoEvento(eventointegrazione.getTipoEvento().toString());
		
		return evento;
	}
	
	public static EventoIntegrazione toEventoIntegrazione(Evento evento) throws IOException {
		EventoIntegrazione eventointegrazione = evento.getDettaglioObject(EventoIntegrazione.class);
		return eventointegrazione;
	}
	
	public static EventoCooperazione toEventoCooperazione(Evento evento) throws IOException {
		EventoCooperazione eventoCooperazione = evento.getDettaglioObject(EventoCooperazione.class);
		
//		EventoCooperazione eventoCooperazione = new EventoCooperazione();
		
//		eventoCooperazione.setCategoriaEvento(evento.getCategoriaEvento());
//		eventoCooperazione.setCcp(evento.getCcp());
//		eventoCooperazione.setCodDominio(evento.getCodDominio());
//		eventoCooperazione.setDataRichiesta(evento.getData());
//		if(evento.getIntervallo() != null) {
//			if(evento.getData() != null) {
//				eventoCooperazione.setDataRisposta(new Date(evento.getData().getTime() + evento.getIntervallo()));
//			} else {
//				eventoCooperazione.setDataRisposta(null);
//			}
//		} else {
//			eventoCooperazione.setDataRisposta(null);
//		}
//		eventoCooperazione.setIuv(evento.getIuv());
//		eventoCooperazione.setSottotipoEvento(evento.getSottotipoEvento());
//		if(evento.getTipoEvento() != null)
//			eventoCooperazione.setTipoEvento(TipoEvento.valueOf(evento.getTipoEvento()));
		
//		EventoCooperazione eventoCooperazione = evento.toEventoCooperazione();
		
		return eventoCooperazione;
	}
	
	public static Evento fromEventoNotaToEvento(EventoNota eventoNota) throws IOException {
		Evento evento = new Evento();
		
		evento.setCategoriaEvento(eventoNota.getCategoriaEvento());
		evento.setCcp(eventoNota.getCcp());
		evento.setCodDominio(eventoNota.getCodDominio());
		evento.setData(eventoNota.getData());
		evento.setClassnameDettaglio(EventoNota.class.getName());
		evento.setDettaglio(getDettaglioEventoNota(eventoNota)); 
		evento.setIdPagamentoPortale(eventoNota.getIdPagamentoPortale()); 
		evento.setIdVersamento(eventoNota.getIdVersamento());
		evento.setIuv(eventoNota.getIuv());
		evento.setSottotipoEvento(eventoNota.getSottotipoEvento());
		if(eventoNota.getTipoEvento() != null)
			evento.setTipoEvento(eventoNota.getTipoEvento().toString());
		
		return evento;
	}
	
	public static EventoNota toEventoNota(Evento evento) throws IOException {
		EventoNota eventoNota = evento.getDettaglioObject(EventoNota.class);
		return eventoNota;
	}
	
	public static String getDettaglioEventoIntegrazione(EventoIntegrazione eventoIntegrazione) throws IOException {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
		ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
		return serializer.getObject(eventoIntegrazione); 
	}
	
	public static String getDettaglioEventoCooperazione(EventoCooperazione eventoCooperazione) throws IOException {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
		ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
		return serializer.getObject(eventoCooperazione); 
	}

	public static String getDettaglioEventoNota(EventoNota eventoNota) throws IOException {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
		ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
		return serializer.getObject(eventoNota); 
	}
}
