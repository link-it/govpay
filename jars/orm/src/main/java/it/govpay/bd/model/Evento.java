package it.govpay.bd.model;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

import it.govpay.bd.BasicBD;
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

	public Class<?> getClassDettaglio(){
		if(this.getClassnameDettaglio() != null) {
			try {
				return Class.forName(this.getClassnameDettaglio());
			} catch (ClassNotFoundException e) {
			}
		}

		return null;
	}

	public <T> T getDettaglioObject(Class<T> tClass) throws IOException {
		if(this.getDettaglio() != null && tClass != null) {
			if(tClass.isAssignableFrom(this.getClassDettaglio())) {
				SerializationConfig serializationConfig = new SerializationConfig();
				serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
				serializationConfig.setIgnoreNullValues(true);
				IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
				return tClass.cast(deserializer.getObject(this.getDettaglio(), tClass));
			}
		}

		return null;
	}
}
