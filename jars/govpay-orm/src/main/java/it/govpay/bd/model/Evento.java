package it.govpay.bd.model;

import java.util.Arrays;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.eventi.EventoCooperazione;
import it.govpay.bd.model.eventi.EventoGenerico;
import it.govpay.bd.model.eventi.EventoNota;
import it.govpay.bd.pagamento.PagamentiPortaleBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.utils.SimpleDateFormatUtils;

public class Evento extends it.govpay.model.Evento{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Evento() {
		super();
	}

	// Business

	private transient Versamento versamento;
	private transient PagamentoPortale pagamentoPortale;

	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
		if(versamento != null && versamento.getId() != null)
			this.setIdVersamento(versamento.getId());
	}

	public Versamento getVersamento(BasicBD bd) throws ServiceException {
		if(this.versamento == null && this.getIdVersamento() != null && bd != null) {
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			this.versamento = versamentiBD.getVersamento(this.getIdVersamento());
		}
		return this.versamento;
	}

	public PagamentoPortale getPagamentoPortale(BasicBD bd) throws ServiceException {
		if(this.pagamentoPortale == null && this.getIdPagamentoPortale() != null && bd != null) {
			PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(bd);
			try {
				this.pagamentoPortale = pagamentiPortaleBD.getPagamento(this.getIdPagamentoPortale());
			}catch (NotFoundException e) {
			}
		}
		return pagamentoPortale;
	}

	public void setPagamentoPortale(PagamentoPortale pagamentoPortale) {
		this.pagamentoPortale = pagamentoPortale;
		if(pagamentoPortale != null && pagamentoPortale.getId() != null)
			this.setIdPagamentoPortale(pagamentoPortale.getId());
	}

	public EventoGenerico toEventoGenerico() throws IOException {
		if(this.getDettaglio() != null) {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
			serializationConfig.setIgnoreNullValues(true);
			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			return (EventoGenerico) deserializer.getObject(this.getDettaglio(), EventoGenerico.class);
		}

		return null;
	}

	public String getDettaglioEventoGenerico(EventoGenerico eventoGenerico) throws IOException {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
		ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
		return serializer.getObject(eventoGenerico); 
	}

	public EventoCooperazione toEventoCooperazione() throws IOException {
		if(this.getDettaglio() != null) {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
			serializationConfig.setIgnoreNullValues(true);
			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			return (EventoCooperazione) deserializer.getObject(this.getDettaglio(), EventoCooperazione.class);
		}

		return null;
	}
	
	public String getDettaglioEventoCooperazione(EventoCooperazione eventoCooperazione) throws IOException {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
		ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
		return serializer.getObject(eventoCooperazione); 
	}
	
	public EventoNota toEventoNota() throws IOException {
		if(this.getDettaglio() != null) {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
			serializationConfig.setIgnoreNullValues(true);
			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			return (EventoNota) deserializer.getObject(this.getDettaglio(), EventoNota.class);
		}

		return null;
	}
	
	public String getDettaglioEventoNota(EventoNota eventoNota) throws IOException {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
		ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
		return serializer.getObject(eventoNota); 
	}
}
