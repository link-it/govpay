package it.govpay.core.business;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotAuthorizedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Operazione;
import it.govpay.bd.model.OperazioneAnnullamento;
import it.govpay.bd.model.OperazioneCaricamento;
import it.govpay.bd.pagamento.OperazioniBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.filters.OperazioneFilter;
import it.govpay.core.dao.pagamenti.dto.ElaboraTracciatoDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiOperazioneDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiOperazioneDTOResponse;
import it.govpay.core.utils.CSVUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.tracciati.CostantiCaricamento;
import it.govpay.core.utils.tracciati.operazioni.AbstractOperazioneRequest;
import it.govpay.core.utils.tracciati.operazioni.AbstractOperazioneResponse;
import it.govpay.core.utils.tracciati.operazioni.AnnullamentoRequest;
import it.govpay.core.utils.tracciati.operazioni.CaricamentoRequest;
import it.govpay.core.utils.tracciati.operazioni.CaricamentoResponse;
import it.govpay.core.utils.tracciati.operazioni.OperazioneFactory;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.bd.model.Tracciato;
import it.govpay.model.Tracciato.STATO_ELABORAZIONE;
import it.govpay.orm.constants.StatoTracciatoType;

public class Tracciati extends BasicBD {

	private static Logger log = LoggerWrapperFactory.getLogger(Tracciati.class);

	public Tracciati(BasicBD basicBD) {
		super(basicBD);
	}

	public void elaboraTracciato(ElaboraTracciatoDTO elaboraTracciatoDTO) throws ServiceException {

		boolean wasAutocommit = this.isAutoCommit();

		if(this.isAutoCommit()) {
			this.setAutoCommit(false);
		}

		TracciatiBD tracciatiBD = new TracciatiBD(this);
		Tracciato tracciato = elaboraTracciatoDTO.getTracciato();
		

		log.info("Avvio elaborazione tracciato [" + tracciato.getId() +"]");

		try {
			SerializationConfig config = new SerializationConfig();
			config.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
			config.setIgnoreNullValues(true);
			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, config);
			ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, config);
			
			it.govpay.core.beans.tracciati.Pendenza beanDati = (it.govpay.core.beans.tracciati.Pendenza) deserializer.getObject(tracciato.getBeanDati(), it.govpay.core.beans.tracciati.Pendenza.class);
			
			long numLinea = beanDati.getLineaElaborazione();
			
			log.debug("Leggo il tracciato saltando le prime " + numLinea + " linee");
			
			List<byte[]> lst = CSVUtils.splitCSV(tracciato.getRawRichiesta(), numLinea);
			log.debug("Lette " + lst.size() + " linee");
			

			OperazioniBD operazioniBD = new OperazioniBD(this);
			
			OperazioneFactory factory = new OperazioneFactory();
			
			for(byte[] linea: lst) {
				
				numLinea = numLinea + 1 ;
				
				// Elaboro l'operazione
				
				AbstractOperazioneRequest request = factory.acquisisci(linea, tracciato.getId(), numLinea);
				AbstractOperazioneResponse response = factory.eseguiOperazione(request, tracciato, this);

				this.setAutoCommit(false);
				
				Operazione operazione = new Operazione();
				operazione.setCodVersamentoEnte(request.getCodVersamentoEnte());
				operazione.setDatiRichiesta(linea);
				operazione.setDatiRisposta(response.getDati());
				operazione.setStato(response.getStato());
				if(response.getDescrizioneEsito() != null)
					operazione.setDettaglioEsito(response.getDescrizioneEsito().length() > 255 ? response.getDescrizioneEsito().substring(0, 255) : response.getDescrizioneEsito());
				try {
					operazione.setIdApplicazione(AnagraficaManager.getApplicazione(this, request.getCodApplicazione()).getId());
				} catch(Exception e) {
					// CodApplicazione non censito in anagrafica.
				}
				operazione.setIdTracciato(request.getIdTracciato());
				operazione.setLineaElaborazione(request.getLinea());
				operazione.setTipoOperazione(request.getTipoOperazione());
				operazioniBD.insertOperazione(operazione);
				
				if(operazione.getStato().equals(StatoOperazioneType.ESEGUITO_OK)) {
					beanDati.setNumOperazioniOk(beanDati.getNumOperazioniOk()+1);
				} else {
					if(!response.getEsito().equals(CostantiCaricamento.EMPTY.toString()))
						beanDati.setNumOperazioniKo(beanDati.getNumOperazioniKo()+1);
				}				
				beanDati.setLineaElaborazione(beanDati.getLineaElaborazione()+1);	
				log.debug("Linea ["+ numLinea + "] elaborata con esito [" +operazione.getStato() + "]: " + operazione.getDettaglioEsito() + " Raw: [" + new String(linea) + "]");
				beanDati.setDataUltimoAggiornamento(new Date());
				
				tracciatiBD.updateBeanDati(tracciato, serializer.getObject(beanDati));
				this.commit();
				
				BatchManager.aggiornaEsecuzione(this, Operazioni.batch_tracciati);
			}
			
			
			// Elaborazione completata. Processamento tracciato di esito
			
			OperazioneFilter filter = operazioniBD.newFilter();
			filter.setIdTracciato(tracciato.getId());
			filter.setLimit(500);
			filter.setOffset(0);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			while(true) {
				
				// Ciclo finche' non mi ritorna meno record del limit. Altrimenti esco perche' ho finito
				
				List<Operazione> findAll = operazioniBD.findAll(filter);
				for(Operazione operazione : findAll) {
					baos.write(operazione.getDatiRisposta());
					baos.write("\n".getBytes());
				}
				if(findAll.size() == 500) {
					filter.setOffset(filter.getOffset() + 500);
				} else {
					break;
				}
				
			}

			if(beanDati.getNumOperazioniKo() > 0) {
				beanDati.setStepElaborazione(StatoTracciatoType.CARICAMENTO_KO.getValue());
			} else {
				beanDati.setStepElaborazione(StatoTracciatoType.CARICAMENTO_OK.getValue());
			}
			
			tracciato.setRawEsito(baos.toByteArray());
			try {baos.flush();} catch(Exception e){}
			try {baos.close();} catch(Exception e){}
			
			
			tracciato.setStato(STATO_ELABORAZIONE.COMPLETATO);
			tracciato.setDataCompletamento(new Date());
			tracciato.setBeanDati(serializer.getObject(beanDati));
			tracciatiBD.update(tracciato);
			
			
			if(!isAutoCommit()) this.commit();
			log.info("Elaborazione tracciato ["+tracciato.getId()+"] terminata: " + tracciato.getStato());
		} catch(Throwable e) {
			log.error("Errore durante l'elaborazione del tracciato ["+tracciato.getId()+"]: " + e.getMessage(), e);
			if(!isAutoCommit()) this.rollback();
		} finally {
			this.setAutoCommit(wasAutocommit);
		}
	}
	
	public LeggiOperazioneDTOResponse leggiOperazione(LeggiOperazioneDTO leggiOperazioneDTO) throws NotAuthorizedException, ServiceException {
		OperazioniBD operazioniBD = new OperazioniBD(this);
		Operazione operazione = operazioniBD.getOperazione(leggiOperazioneDTO.getId());

		LeggiOperazioneDTOResponse leggiOperazioneDTOResponse = fillOperazione(operazione);

		return leggiOperazioneDTOResponse;
}

	public LeggiOperazioneDTOResponse fillOperazione(Operazione operazione) throws ServiceException {
		LeggiOperazioneDTOResponse leggiOperazioneDTOResponse = new LeggiOperazioneDTOResponse();
		OperazioneFactory factory = new OperazioneFactory();
		
		switch (operazione.getTipoOperazione()) {
		case ADD:
			CaricamentoRequest caricamentoRequest = (CaricamentoRequest) factory.parseLineaOperazioneRequest(operazione.getDatiRichiesta());
			AbstractOperazioneResponse abstractOperazioneResponse = factory.parseLineaOperazioneResponse(operazione.getTipoOperazione(), operazione.getDatiRisposta());
			CaricamentoResponse caricamentoResponse = (abstractOperazioneResponse instanceof CaricamentoResponse) ?  (CaricamentoResponse) abstractOperazioneResponse : null;

			OperazioneCaricamento operazioneCaricamento = new OperazioneCaricamento(operazione);

			operazioneCaricamento.setAnagraficaDebitore(caricamentoRequest.getAnagraficaDebitore());
			operazioneCaricamento.setBundleKey(caricamentoRequest.getBundleKey());
			operazioneCaricamento.setCausale(caricamentoRequest.getCausale());
			operazioneCaricamento.setCfDebitore(caricamentoRequest.getCfDebitore());
			operazioneCaricamento.setCodDominio(caricamentoRequest.getCodDominio());
			operazioneCaricamento.setCodTributo(caricamentoRequest.getCodTributo());
			operazioneCaricamento.setIdDebito(caricamentoRequest.getIdDebito());
			operazioneCaricamento.setImporto(caricamentoRequest.getImporto());
			operazioneCaricamento.setNote(caricamentoRequest.getNote());
			operazioneCaricamento.setScadenza(caricamentoRequest.getScadenza());

			if(caricamentoResponse != null) {
				operazioneCaricamento.setIuv(caricamentoResponse.getIuv());
				operazioneCaricamento.setBarCode(caricamentoResponse.getBarCode());
				operazioneCaricamento.setQrCode(caricamentoResponse.getQrCode());
			}

			leggiOperazioneDTOResponse.setOperazione(operazioneCaricamento);
			break;
		case DEL:
			AnnullamentoRequest annullamentoRequest = (AnnullamentoRequest) factory.parseLineaOperazioneRequest(operazione.getDatiRichiesta());
			OperazioneAnnullamento operazioneAnnullamento = new OperazioneAnnullamento(operazione);
			operazioneAnnullamento.setMotivoAnnullamento(annullamentoRequest.getMotivoAnnullamento());
			leggiOperazioneDTOResponse.setOperazione(operazioneAnnullamento);
			break;
		case N_V:
		default:
			leggiOperazioneDTOResponse.setOperazione(operazione);
			break;
		}
		
		return leggiOperazioneDTOResponse;
	}
}
