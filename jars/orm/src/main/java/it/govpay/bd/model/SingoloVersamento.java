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
package it.govpay.bd.model;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.bd.pagamento.RendicontazioniBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.core.beans.tracciati.Metadata;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.IbanAccredito;

public class SingoloVersamento extends it.govpay.model.SingoloVersamento{
	private static final long serialVersionUID = 1L;
	
	// Business
	
	private transient Versamento versamento;
	private transient Tributo tributo;
	private transient IbanAccredito ibanAccredito;
	private transient IbanAccredito ibanAppoggio;
	private transient List<Pagamento> pagamenti;
	private transient List<Rendicontazione> rendicontazioni;
	private transient Dominio dominio;
	private transient Metadata metadataPagoPA;
	
	
	public Tributo getTributo(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.tributo == null && this.getIdTributo() != null) {
			try {
				this.tributo = AnagraficaManager.getTributo(configWrapper, this.getIdTributo());
			} catch (NotFoundException e) {
				throw new ServiceException(MessageFormat.format("Tributo con id [{0}] non trovato.", this.getIdTributo()));
			}
		}
		return this.tributo;
	}
	
	public void setTributo(Tributo tributo){
		this.tributo = tributo;
	}

	public void setTributo(String codTributo, BDConfigWrapper configWrapper) throws ServiceException, NotFoundException {
		// se viene definito il dominio per la singola voce allora cerco tra i tributi associati ad esso
		if(this.getIdDominio() != null) {
			this.tributo = AnagraficaManager.getTributo(configWrapper, this.getIdDominio(), codTributo);
			this.setIdTributo(this.tributo.getId());
		} else {
			// caso monobeneficiario
			this.tributo = AnagraficaManager.getTributo(configWrapper, this.versamento.getIdDominio(), codTributo);
			this.setIdTributo(this.tributo.getId());
		}
	}
	
	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
		if(versamento.getId() != null)
			this.setIdVersamento(versamento.getId());
	}
	
	public Versamento getVersamentoBD(BasicBD bd) throws ServiceException {
		if(this.versamento == null && bd != null) {
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			versamentiBD.setAtomica(false); // connessione condivisa
			this.versamento = versamentiBD.getVersamento(this.getIdVersamento());
		}
		return this.versamento;
	}
	
	public Versamento getVersamento(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.versamento == null) {
			VersamentiBD versamentiBD = new VersamentiBD(configWrapper);
			this.versamento = versamentiBD.getVersamento(this.getIdVersamento());
		}
		return this.versamento;
	}
	
	public IbanAccredito getIbanAccredito(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.ibanAccredito == null && this.getIdIbanAccredito() != null) {
			try {
				this.ibanAccredito = AnagraficaManager.getIbanAccredito(configWrapper, this.getIdIbanAccredito());
			} catch (NotFoundException e) {
				throw new ServiceException(MessageFormat.format("Iban Accredito con id [{0}] non trovato.", this.getIdIbanAccredito()));
			}
		}
		
		if(this.ibanAccredito == null && this.getTributo(configWrapper) != null && this.getIdIbanAccredito() == null) {
			this.ibanAccredito = this.getTributo(configWrapper).getIbanAccredito();
		}
		
		return this.ibanAccredito;
	}
	
	public void setIbanAppoggio(IbanAccredito ibanAppoggio) {
		this.ibanAppoggio = ibanAppoggio;
		if(ibanAppoggio != null && ibanAppoggio.getId() != null)
			this.setIdIbanAppoggio(ibanAppoggio.getId());
	}
	
	public IbanAccredito getIbanAppoggio(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.ibanAppoggio == null && this.getIdIbanAppoggio() != null) {
			try {
				this.ibanAppoggio = AnagraficaManager.getIbanAccredito(configWrapper, this.getIdIbanAppoggio());
			} catch (NotFoundException e) {
				throw new ServiceException(MessageFormat.format("Iban Appoggio con id [{0}] non trovato.", this.getIdIbanAppoggio()));
			}
		}
		
		if(this.ibanAppoggio == null && this.getTributo(configWrapper) != null && this.getIdIbanAppoggio() == null) {
			this.ibanAppoggio = this.getTributo(configWrapper).getIbanAppoggio();
		}
		
		return this.ibanAppoggio;
	}
	
	public void setIbanAccredito(IbanAccredito ibanAccredito) {
		this.ibanAccredito = ibanAccredito;
		if(ibanAccredito != null && ibanAccredito.getId() != null)
			this.setIdIbanAccredito(ibanAccredito.getId());
	}
	
	public it.govpay.model.Tributo.TipoContabilita getTipoContabilita(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.getTipoContabilita() != null)
			return this.getTipoContabilita();
		else
			return this.getTributo(configWrapper).getTipoContabilita();
	}
	
	public String getCodContabilita(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.getCodContabilita() != null)
			return this.getCodContabilita();
		else
			return this.getTributo(configWrapper).getCodContabilita(); 
	}

	public List<Pagamento> getPagamenti(BasicBD bd) throws ServiceException  {
		if(this.pagamenti == null) {
			PagamentiBD pagamentiBD = new PagamentiBD(bd);
			pagamentiBD.setAtomica(false); // connessione aperta
			this.pagamenti = pagamentiBD.getPagamentiBySingoloVersamento(this.getId());
		}
		return pagamenti;
	}

	public void setPagamenti(List<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}

	public List<Rendicontazione> getRendicontazioni(BasicBD bd) throws ServiceException  {
		if(this.rendicontazioni == null) {
			RendicontazioniBD rendicontazioniBD = new RendicontazioniBD(bd);
			rendicontazioniBD.setAtomica(false); // connessione aperta
			this.rendicontazioni = rendicontazioniBD.getRendicontazioniBySingoloVersamento(this.getId());
		}
		return rendicontazioni;
	}

	public void setRendicontazioni(List<Rendicontazione> rendicontazioni) {
		this.rendicontazioni = rendicontazioni;
	}
	
	public Dominio getDominio(BDConfigWrapper configWrapper) throws ServiceException {
		if(this.dominio == null && this.getIdDominio() != null) {
			try {
				this.dominio = AnagraficaManager.getDominio(configWrapper, this.getIdDominio());
			} catch (NotFoundException e) {
			}
		} 
		return this.dominio;
	}
	
	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
		if(this.dominio != null) {
			this.setIdDominio(this.dominio.getId());
		}
	}
	
	@Override
	public String getMetadata() {
		try {
			return this._getJson(this.getMetadataPagoPA());
		} catch (IOException e) {
			return super.getMetadata();
		}
	}

	public Metadata getMetadataPagoPA() {
		if(this.metadataPagoPA == null) {
			try {
				this.metadataPagoPA = this._getFromJson(super.getMetadata(), Metadata.class);
			} catch (IOException e) {
				return null;
			}
		}
		return metadataPagoPA;
	}

	public void setMetadataPagoPA(Metadata metadataPagoPA) {
		this.metadataPagoPA = metadataPagoPA;
	}
	
	private <T> T _getFromJson(String jsonString, Class<T> tClass) throws IOException {
		if(jsonString != null) {
			try {
				SerializationConfig serializationConfig = new SerializationConfig();
				serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
				serializationConfig.setIgnoreNullValues(true);
				IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
				return tClass.cast(deserializer.getObject(jsonString, tClass));
			} catch(org.openspcoop2.utils.serialization.IOException e) {
				throw new IOException(e.getMessage(), e);
			}
		}

		return null;
	}

	private String _getJson(Object objToSerialize) throws IOException {
		try {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
			ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			return serializer.getObject(objToSerialize); 
		} catch(org.openspcoop2.utils.serialization.IOException e) {
			throw new IOException(e.getMessage(), e);
		}
	}
}

