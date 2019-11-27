package it.govpay.bd.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import it.govpay.bd.model.eventi.DatiPagoPA;
import it.govpay.bd.model.eventi.DettaglioRichiesta;
import it.govpay.bd.model.eventi.DettaglioRisposta;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.IncassiBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.core.utils.SimpleDateFormatUtils;

public class Evento extends it.govpay.model.Evento{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public enum TipoEventoCooperazione {
		nodoInviaRPT,
		nodoInviaCarrelloRPT, 
		nodoChiediStatoRPT, 
		paaInviaRT, 
		nodoChiediCopiaRT, 
		paaVerificaRPT, 
		paaAttivaRPT,
		nodoInviaRichiestaStorno,
		paaInviaEsitoStorno,
		nodoInviaAvvisoDigitale;
	}

	public static final String COMPONENTE_COOPERAZIONE = "FESP";
	public static final String NDP = "NodoDeiPagamentiSPC";
	
	

	public Evento() {
		super();
	}

	private DettaglioRichiesta dettaglioRichiesta;
	private DettaglioRisposta dettaglioRisposta;
	private DatiPagoPA datiPagoPA;
	
	// Business
	private transient Fr fr;
	private transient Tracciato tracciato;
	private transient Incasso incasso;
	
	public Incasso getIncasso(BasicBD bd) throws ServiceException {
		if(this.getIdIncasso() != null && bd != null) {
			if(this.incasso == null) {
				IncassiBD incassiBD = new IncassiBD(bd);
				this.incasso = incassiBD.getIncasso(this.getIdIncasso());
			}
		}
		return this.incasso;
	}

	public void setIncasso(Incasso incasso) {
		this.incasso = incasso;
		this.setIdIncasso(incasso.getId());
	}
	
	public Fr getFr(BasicBD bd) throws ServiceException {
		if(this.getIdFr() != null &&  this.fr == null && bd != null) {
			FrBD frBD = new FrBD(bd);
			this.fr = frBD.getFr(this.getIdFr());
		}
		return this.fr;
	}
	
	public void setFr(Fr fr) {
		this.fr = fr;
	}
	
	public Tracciato getTracciato(BasicBD bd) throws ServiceException{
		if(this.getIdTracciato() != null &&  this.tracciato == null && bd != null) {
			TracciatiBD frBD = new TracciatiBD(bd);
			try {
				this.tracciato = frBD.getTracciato(this.getIdTracciato());
			} catch (NotFoundException e) {	}
		}
		return tracciato;
	}

	public void setTracciato(Tracciato tracciato) {
		this.tracciato = tracciato;
	}

	public DettaglioRichiesta getDettaglioRichiesta() {
		if(this.dettaglioRichiesta == null) {
			try {
				this.dettaglioRichiesta = this.getDettaglioObject(this.getParametriRichiesta(), DettaglioRichiesta.class);
			}catch (IOException e) {
			}
		}

		return dettaglioRichiesta;
	}

	public void setDettaglioRichiesta(DettaglioRichiesta dettaglioRichiesta) {
		this.dettaglioRichiesta = dettaglioRichiesta;
	}

	public DettaglioRisposta getDettaglioRisposta() {
		if(this.dettaglioRisposta == null) {
			try {
				this.dettaglioRisposta = this.getDettaglioObject(this.getParametriRisposta(), DettaglioRisposta.class);
			}catch (IOException e) {
			}
		}

		return dettaglioRisposta;
	}

	public void setDettaglioRisposta(DettaglioRisposta dettaglioRisposta) {
		this.dettaglioRisposta = dettaglioRisposta;
	}

	public DatiPagoPA getPagoPA() {
		if(this.datiPagoPA == null) {
			try {
				this.datiPagoPA = this.getDettaglioObject(this.getDatiPagoPA(), DatiPagoPA.class);
			}catch (IOException e) {
			}
		}

		return datiPagoPA;
	}

	public void setDatiPagoPA(DatiPagoPA datiPagoPA) {
		this.datiPagoPA = datiPagoPA;
	}

	public <T> T getDettaglioObject(String json, Class<T> tClass) throws IOException {
		if(json != null && tClass != null) {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi());
			serializationConfig.setIgnoreNullValues(true);
			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			return tClass.cast(deserializer.getObject(json, tClass));
		}

		return null;
	}

	public String getDettaglioAsString(Object obj) throws IOException {
		if(obj != null) {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi());
			ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			return serializer.getObject(obj); 
		}
		return null;
	}

	public <T> T getDettaglioObject(byte [] objBytes, Class<T> tClass) throws IOException {
		if(objBytes != null && tClass != null) {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi());
			serializationConfig.setIgnoreNullValues(true);
			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			try (ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);){
				return tClass.cast(deserializer.readObject(bais, tClass));
			} catch (java.io.IOException e) {
				throw new IOException(e);
			}  
		}

		return null;
	}

	public byte [] getDettaglioAsBytes(Object obj) throws IOException {
		if(obj != null) {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi());
			ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			try(ByteArrayOutputStream baos = new ByteArrayOutputStream();){
				serializer.writeObject(obj, baos);
				return baos.toByteArray();
			} catch (java.io.IOException e) {
				throw new IOException(e);
			}  
		}
		return null;
	}
	
	
}
