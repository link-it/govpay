package it.govpay.backoffice.v1.beans.converter;

import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.backoffice.v1.beans.AnnullamentoPendenza;
import it.govpay.backoffice.v1.beans.DettaglioTracciatoPendenzeEsito;
import it.govpay.backoffice.v1.beans.EsitoOperazionePendenza;
import it.govpay.backoffice.v1.beans.OperazionePendenza;
import it.govpay.backoffice.v1.beans.PendenzaPost;
import it.govpay.backoffice.v1.beans.StatoOperazionePendenza;
import it.govpay.backoffice.v1.beans.StatoTracciatoPendenza;
import it.govpay.backoffice.v1.beans.TipoOperazionePendenza;
import it.govpay.backoffice.v1.beans.Tracciato;
import it.govpay.backoffice.v1.beans.TracciatoPendenze;
import it.govpay.backoffice.v1.beans.TracciatoPendenzeEsito;
import it.govpay.backoffice.v1.beans.TracciatoPendenzeIndex;
import it.govpay.backoffice.v1.beans.TracciatoPendenzePost;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.OperazioneAnnullamento;
import it.govpay.bd.model.OperazioneCaricamento;
import it.govpay.bd.model.Versamento;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.orm.constants.StatoTracciatoType;

public class TracciatiConverter {


	public static Tracciato toRsModel(it.govpay.bd.model.Tracciato tracciato) throws ServiceException {
		Tracciato rsModel = new Tracciato();

		rsModel.setId(BigDecimal.valueOf(tracciato.getId()));
		rsModel.setCodDominio(tracciato.getCodDominio());
		rsModel.setTipo(tracciato.getTipo().toString());
		rsModel.setStato(tracciato.getStato().toString());
		rsModel.setDescrizioneStato(tracciato.getDescrizioneStato());
		rsModel.setDataCaricamento(tracciato.getDataCaricamento());
		rsModel.setDataCompletamento(tracciato.getDataCompletamento());
		rsModel.setBeanDati(tracciato.getBeanDati());
		rsModel.setFilenameRichiesta(tracciato.getFileNameRichiesta());
		rsModel.setFilenameEsito(tracciato.getFileNameEsito());

		return rsModel;
	}
	
	public static TracciatoPendenze toTracciatoPendenzeRsModel(it.govpay.bd.model.Tracciato tracciato) throws ServiceException {
		TracciatoPendenze rsModel = new TracciatoPendenze();

		rsModel.setId(BigDecimal.valueOf(tracciato.getId()));
		rsModel.setDataOraCaricamento(tracciato.getDataCaricamento());
		rsModel.setNomeFile(tracciato.getFileNameRichiesta());
		rsModel.setDescrizioneStato(tracciato.getDescrizioneStato());
		
		it.govpay.core.beans.tracciati.TracciatoPendenza beanDati = leggiBeanDati(tracciato.getBeanDati());	
		
		if(tracciato.getOperatore(null) != null)
			rsModel.setOperatoreMittente(tracciato.getOperatore(null).getNome());
		rsModel.setNumeroOperazioniEseguite(BigDecimal.valueOf(beanDati.getNumAddOk() + beanDati.getNumDelOk()));
		rsModel.setNumeroOperazioniFallite(BigDecimal.valueOf(beanDati.getNumAddKo() + beanDati.getNumDelKo()));
		rsModel.setNumeroOperazioniTotali(BigDecimal.valueOf(beanDati.getNumAddTotali() + beanDati.getNumDelTotali()));
		rsModel.setDataOraUltimoAggiornamento(beanDati.getDataUltimoAggiornamento() != null ? beanDati.getDataUltimoAggiornamento() : tracciato.getDataCompletamento());
		rsModel.setStampaAvvisi(beanDati.isStampaAvvisi());
		rsModel.setNumeroAvvisiTotali(BigDecimal.valueOf(beanDati.getNumStampeTotali()));
		rsModel.setNumeroAvvisiStampati(BigDecimal.valueOf(beanDati.getNumStampeOk()));
		rsModel.setNumeroAvvisiFalliti(BigDecimal.valueOf(beanDati.getNumStampeKo()));
		
		StatoTracciatoType statoTracciato = StatoTracciatoType.valueOf(beanDati.getStepElaborazione());
		
		if(tracciato.getStato() != null) {
			switch(tracciato.getStato()){
			case COMPLETATO:
				if(statoTracciato.equals(StatoTracciatoType.CARICAMENTO_OK))
					rsModel.setStato(StatoTracciatoPendenza.ESEGUITO);
				else
					rsModel.setStato(StatoTracciatoPendenza.ESEGUITO_CON_ERRORI);
				break;
			case ELABORAZIONE:
				if(statoTracciato.equals(StatoTracciatoType.NUOVO))
					rsModel.setStato(StatoTracciatoPendenza.IN_ATTESA);
				else
					rsModel.setStato(StatoTracciatoPendenza.IN_ELABORAZIONE);
				break;
			case SCARTATO:
				rsModel.setStato(StatoTracciatoPendenza.SCARTATO);
				break;
			case IN_STAMPA:
				rsModel.setStato(StatoTracciatoPendenza.ELABORAZIONE_STAMPA);
				break;
			}
		}
		
		try {
			if(tracciato.getRawRichiesta() == null) {
				
			} else {
				rsModel.setContenuto(TracciatoPendenzePost.parse(new String(tracciato.getRawRichiesta())));
			}
		}catch(Exception e) {}

		return rsModel;
	}
	
	public static TracciatoPendenzeIndex toTracciatoPendenzeRsModelIndex(it.govpay.bd.model.Tracciato tracciato) throws ServiceException {
		TracciatoPendenzeIndex rsModel = new TracciatoPendenzeIndex();

		rsModel.setId(BigDecimal.valueOf(tracciato.getId()));
		rsModel.setDataOraCaricamento(tracciato.getDataCaricamento());
		rsModel.setNomeFile(tracciato.getFileNameRichiesta());
		rsModel.setDescrizioneStato(tracciato.getDescrizioneStato());
		
		it.govpay.core.beans.tracciati.TracciatoPendenza beanDati = leggiBeanDati(tracciato.getBeanDati());
		
		if(tracciato.getOperatore(null) != null)
			rsModel.setOperatoreMittente(tracciato.getOperatore(null).getNome());
		rsModel.setNumeroOperazioniEseguite(BigDecimal.valueOf(beanDati.getNumAddOk() + beanDati.getNumDelOk()));
		rsModel.setNumeroOperazioniFallite(BigDecimal.valueOf(beanDati.getNumAddKo() + beanDati.getNumDelKo()));
		rsModel.setNumeroOperazioniTotali(BigDecimal.valueOf(beanDati.getNumAddTotali() + beanDati.getNumDelTotali()));
		rsModel.setDataOraUltimoAggiornamento(beanDati.getDataUltimoAggiornamento() != null ? beanDati.getDataUltimoAggiornamento() : tracciato.getDataCompletamento());
		rsModel.setStampaAvvisi(beanDati.isStampaAvvisi());
		rsModel.setNumeroAvvisiTotali(BigDecimal.valueOf(beanDati.getNumStampeTotali()));
		rsModel.setNumeroAvvisiStampati(BigDecimal.valueOf(beanDati.getNumStampeOk()));
		rsModel.setNumeroAvvisiFalliti(BigDecimal.valueOf(beanDati.getNumStampeKo()));
		
		StatoTracciatoType statoTracciato = StatoTracciatoType.valueOf(beanDati.getStepElaborazione());
		
		if(tracciato.getStato() != null) {
			switch(tracciato.getStato()){
			case COMPLETATO:
				if(statoTracciato.equals(StatoTracciatoType.CARICAMENTO_OK))
					rsModel.setStato(StatoTracciatoPendenza.ESEGUITO);
				else
					rsModel.setStato(StatoTracciatoPendenza.ESEGUITO_CON_ERRORI);
				break;
			case ELABORAZIONE:
				if(statoTracciato.equals(StatoTracciatoType.NUOVO))
					rsModel.setStato(StatoTracciatoPendenza.IN_ATTESA);
				else
					rsModel.setStato(StatoTracciatoPendenza.IN_ELABORAZIONE);
				break;
			case IN_STAMPA:
				rsModel.setStato(StatoTracciatoPendenza.ELABORAZIONE_STAMPA);
				break;
			case SCARTATO:
				rsModel.setStato(StatoTracciatoPendenza.SCARTATO);
				break;
			}
		}
		
		return rsModel;
	}
	
	public static it.govpay.core.beans.tracciati.TracciatoPendenza leggiBeanDati(String beanDatiS) throws ServiceException{ 
		it.govpay.core.beans.tracciati.TracciatoPendenza beanDati = null;
		SerializationConfig config = new SerializationConfig();
		config.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
		config.setIgnoreNullValues(true);
		IDeserializer deserializer;
		try {
			deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, config);
			beanDati = (it.govpay.core.beans.tracciati.TracciatoPendenza) deserializer.getObject(beanDatiS, it.govpay.core.beans.tracciati.TracciatoPendenza.class);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		
		return beanDati;
	}
	
	public static TracciatoPendenzeEsito toTracciatoPendenzeEsitoRsModel(it.govpay.bd.model.Tracciato tracciato) throws ServiceException {
		TracciatoPendenzeEsito rsModel = new TracciatoPendenzeEsito();

		rsModel.setId(BigDecimal.valueOf(tracciato.getId()));
		rsModel.setDataOraCaricamento(tracciato.getDataCaricamento());
		rsModel.setNomeFile(tracciato.getFileNameEsito());
		rsModel.setDescrizioneStato(tracciato.getDescrizioneStato());
		
		it.govpay.core.beans.tracciati.TracciatoPendenza beanDati = leggiBeanDati(tracciato.getBeanDati());		
		if(tracciato.getOperatore(null) != null)
			rsModel.setOperatoreMittente(tracciato.getOperatore(null).getNome());
		rsModel.setNumeroOperazioniEseguite(BigDecimal.valueOf(beanDati.getNumAddOk() + beanDati.getNumDelOk()));
		rsModel.setNumeroOperazioniFallite(BigDecimal.valueOf(beanDati.getNumAddKo() + beanDati.getNumDelKo()));
		rsModel.setNumeroOperazioniTotali(BigDecimal.valueOf(beanDati.getNumAddTotali() + beanDati.getNumDelTotali()));
		rsModel.setDataOraUltimoAggiornamento(beanDati.getDataUltimoAggiornamento() != null ? beanDati.getDataUltimoAggiornamento() : tracciato.getDataCompletamento());
		rsModel.setStampaAvvisi(beanDati.isStampaAvvisi());
		rsModel.setNumeroAvvisiTotali(BigDecimal.valueOf(beanDati.getNumStampeTotali()));
		rsModel.setNumeroAvvisiStampati(BigDecimal.valueOf(beanDati.getNumStampeOk()));
		rsModel.setNumeroAvvisiFalliti(BigDecimal.valueOf(beanDati.getNumStampeKo()));
		
		StatoTracciatoType statoTracciato = StatoTracciatoType.valueOf(beanDati.getStepElaborazione());
		
		if(tracciato.getStato() != null) {
			switch(tracciato.getStato()){
			case COMPLETATO:
				if(statoTracciato.equals(StatoTracciatoType.CARICAMENTO_OK))
					rsModel.setStato(StatoTracciatoPendenza.ESEGUITO);
				else
					rsModel.setStato(StatoTracciatoPendenza.ESEGUITO_CON_ERRORI);
				break;
			case ELABORAZIONE:
				if(statoTracciato.equals(StatoTracciatoType.NUOVO))
					rsModel.setStato(StatoTracciatoPendenza.IN_ATTESA);
				else
					rsModel.setStato(StatoTracciatoPendenza.IN_ELABORAZIONE);
				break;
			case IN_STAMPA:
				rsModel.setStato(StatoTracciatoPendenza.ELABORAZIONE_STAMPA);
				break;
			case SCARTATO:
				rsModel.setStato(StatoTracciatoPendenza.SCARTATO);
				break;
			}
		}
		
		try {
			if(tracciato.getRawEsito() == null) 
				rsModel.setEsito(new DettaglioTracciatoPendenzeEsito());
			else
				rsModel.setEsito(DettaglioTracciatoPendenzeEsito.parse(new String(tracciato.getRawEsito())));
		} catch(Exception e) {
			throw new ServiceException(e);
		}

		return rsModel;
	}
	
	public static OperazionePendenza toOperazioneTracciatoPendenzaRsModel(it.govpay.bd.model.Operazione operazione) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		OperazionePendenza rsModel = new OperazionePendenza();
		
		Applicazione applicazione = operazione.getApplicazione(configWrapper); 
		if(applicazione != null)
			rsModel.setApplicazione(applicazione.getCodApplicazione());
		rsModel.setNumero(BigDecimal.valueOf(operazione.getLineaElaborazione()));  
		
		switch (operazione.getStato()) {
		case ESEGUITO_KO:
			rsModel.setStato(StatoOperazionePendenza.SCARTATO);
			break;
		case ESEGUITO_OK:
			rsModel.setStato(StatoOperazionePendenza.ESEGUITO);
			break;
		case NON_VALIDO:
			rsModel.setStato(StatoOperazionePendenza.NON_VALIDO);
			break;
		}
		
		EsitoOperazionePendenza risposta = null;
		
		try {
			risposta = EsitoOperazionePendenza.parse(new String(operazione.getDatiRisposta())); 
		} catch(Exception e) {
			
		}
		
		PendenzaPost pendenzaPost = null;
		AnnullamentoPendenza annullamentoPendenza = null;
		try {
			pendenzaPost = PendenzaPost.parse(new String(operazione.getDatiRichiesta()));
			rsModel.setRichiesta(pendenzaPost); 
		} catch(Exception e) {
			try {
				annullamentoPendenza = AnnullamentoPendenza.parse(new String(operazione.getDatiRichiesta()));
				rsModel.setRichiesta(annullamentoPendenza); 
			} catch(Exception e1) {
			}
		}
		
		switch (operazione.getTipoOperazione()) {
		case ADD:
			TracciatiConverter.popolaOperazioneAdd(operazione, rsModel);
			
			break;
		case DEL:
			TracciatiConverter.popolaOperazioneDel(operazione, rsModel);
			break;
		
		case N_V:
			rsModel.setTipoOperazione(TipoOperazionePendenza.NON_VALIDA);
			break;
		case INC:
		default:
			rsModel.setTipoOperazione(TipoOperazionePendenza.NON_VALIDA);
			break;
		}
		
		rsModel.setRisposta(risposta);

		return rsModel;
	}

	private static void popolaOperazioneDel(it.govpay.bd.model.Operazione operazione, OperazionePendenza rsModel)
			throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		rsModel.setTipoOperazione(TipoOperazionePendenza.DEL);
		rsModel.setIdentificativoPendenza(operazione.getCodVersamentoEnte());
		
		OperazioneAnnullamento opAnnullamento = (OperazioneAnnullamento) operazione;
		rsModel.setDescrizioneStato(opAnnullamento.getMotivoAnnullamento());
		
		try {
			Dominio dominio = opAnnullamento.getDominio(configWrapper);
			if(dominio != null)
				rsModel.setEnteCreditore(DominiConverter.toRsModelIndex(dominio));
		} catch (NotFoundException e) {
		}
	}

	private static void popolaOperazioneAdd(it.govpay.bd.model.Operazione operazione, OperazionePendenza rsModel)
			throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		rsModel.setTipoOperazione(TipoOperazionePendenza.ADD);
		rsModel.setIdentificativoPendenza(operazione.getCodVersamentoEnte());
		
		OperazioneCaricamento opCaricamento = (OperazioneCaricamento) operazione;
		rsModel.setDescrizioneStato(opCaricamento.getDettaglioEsito()); 
		
		try {
			Dominio dominio = opCaricamento.getDominio(configWrapper); 
			if(dominio != null)
				rsModel.setEnteCreditore(DominiConverter.toRsModelIndex(dominio));
		} catch (NotFoundException e) {
		}
		
		Versamento versamento = opCaricamento.getVersamento();
		
		if(versamento != null) {
			rsModel.setSoggettoPagatore(AnagraficaConverter.toSoggettoRsModel(versamento.getAnagraficaDebitore()));
			rsModel.setNumeroAvviso(versamento.getNumeroAvviso()); 
		}
	}
}
