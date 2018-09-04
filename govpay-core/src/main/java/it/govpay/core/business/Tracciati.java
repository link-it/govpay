package it.govpay.core.business;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Operazione;
import it.govpay.bd.model.OperazioneAnnullamento;
import it.govpay.bd.model.OperazioneCaricamento;
import it.govpay.bd.model.Tracciato;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.OperazioniBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.OperazioneFilter;
import it.govpay.core.dao.pagamenti.dto.ElaboraTracciatoDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiOperazioneDTOResponse;
import it.govpay.core.rs.v1.beans.base.AnnullamentoPendenza;
import it.govpay.core.rs.v1.beans.base.DettaglioTracciatoPendenzeEsito;
import it.govpay.core.rs.v1.beans.base.EsitoOperazionePendenza;
import it.govpay.core.rs.v1.beans.base.PendenzaPost;
import it.govpay.core.rs.v1.beans.base.TracciatoPendenzePost;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.tracciati.CostantiCaricamento;
import it.govpay.core.utils.tracciati.operazioni.AnnullamentoRequest;
import it.govpay.core.utils.tracciati.operazioni.AnnullamentoResponse;
import it.govpay.core.utils.tracciati.operazioni.CaricamentoRequest;
import it.govpay.core.utils.tracciati.operazioni.CaricamentoResponse;
import it.govpay.core.utils.tracciati.operazioni.OperazioneFactory;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;
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

			TracciatoPendenzePost tracciatoPendenzeRequest = (TracciatoPendenzePost) TracciatoPendenzePost.parse(new String(tracciato.getRawRichiesta()), TracciatoPendenzePost.class);

			List<PendenzaPost> inserimenti = tracciatoPendenzeRequest.getInserimenti();
			List<AnnullamentoPendenza> annullamenti = tracciatoPendenzeRequest.getAnnullamenti();

			if(beanDati.getStepElaborazione().equals(StatoTracciatoType.NUOVO.getValue())) {
				log.debug("Cambio stato del tracciato da NUOVO a IN_CARICAMENTO");
				beanDati.setStepElaborazione(StatoTracciatoType.IN_CARICAMENTO.getValue());
				beanDati.setLineaElaborazioneAdd(0);
				beanDati.setLineaElaborazioneDel(0);
				beanDati.setNumAddTotali(inserimenti != null ? inserimenti.size() : 0);
				beanDati.setNumDelTotali(annullamenti != null ? annullamenti.size() : 0);
				tracciato.setBeanDati(serializer.getObject(beanDati));
				tracciatiBD.update(tracciato);
				this.commit();
			}

			OperazioniBD operazioniBD = new OperazioniBD(this);
			OperazioneFactory factory = new OperazioneFactory();
			// eseguo operazioni add
			long numLinea = beanDati.getLineaElaborazioneAdd();

			log.debug("Elaboro le operazioni di caricamento del tracciato saltando le prime " + numLinea + " linee");
			for(long linea = numLinea; linea < beanDati.getNumAddTotali() ; linea ++) {
				PendenzaPost pendenzaPost = inserimenti.get((int) linea);

				it.govpay.core.dao.commons.Versamento versamentoToAdd = it.govpay.core.utils.tracciati.VersamentoUtils.getVersamentoFromPendenza(pendenzaPost);
				
				// inserisco l'identificativo del dominio
				versamentoToAdd.setCodDominio(tracciato.getCodDominio());

				CaricamentoRequest request = new CaricamentoRequest();
				request.setCodApplicazione(pendenzaPost.getIdA2A());
				request.setCodVersamentoEnte(pendenzaPost.getIdPendenza());
				request.setVersamento(versamentoToAdd);
				request.setLinea(linea + 1);
				request.setOperatore(tracciato.getOperatore(this));
				
				CaricamentoResponse caricamentoResponse = factory.caricaVersamento(request, this);

				this.setAutoCommit(false);

				Operazione operazione = new Operazione();
				operazione.setCodVersamentoEnte(versamentoToAdd.getCodVersamentoEnte());
				String jsonPendenza = pendenzaPost.toJSON(null);
				operazione.setDatiRichiesta(jsonPendenza.getBytes());
				operazione.setDatiRisposta(caricamentoResponse.getEsitoOperazionePendenza().toJSON(null).getBytes());
				operazione.setStato(caricamentoResponse.getStato());
				if(caricamentoResponse.getDescrizioneEsito() != null)
					operazione.setDettaglioEsito(caricamentoResponse.getDescrizioneEsito().length() > 255 ? caricamentoResponse.getDescrizioneEsito().substring(0, 255) : caricamentoResponse.getDescrizioneEsito());
				try {
					operazione.setIdApplicazione(AnagraficaManager.getApplicazione(this, caricamentoResponse.getIdA2A()).getId());
				} catch(Exception e) {
					// CodApplicazione non censito in anagrafica.
				}
				operazione.setIdTracciato(tracciato.getId());
				operazione.setLineaElaborazione(linea + 1);
				operazione.setTipoOperazione(TipoOperazioneType.ADD);
				operazione.setCodDominio(tracciato.getCodDominio());
				operazioniBD.insertOperazione(operazione);

				if(operazione.getStato().equals(StatoOperazioneType.ESEGUITO_OK)) {
					beanDati.setNumAddOk(beanDati.getNumAddOk()+1);
				} else {
					if(!caricamentoResponse.getEsito().equals(CostantiCaricamento.EMPTY.toString()))
						beanDati.setNumAddKo(beanDati.getNumAddKo()+1);
				}				
				beanDati.setLineaElaborazioneAdd(beanDati.getLineaElaborazioneAdd()+1);	
				log.debug("Inserimento Pendenza Numero ["+ numLinea + "] elaborata con esito [" +operazione.getStato() + "]: " + operazione.getDettaglioEsito() + " Raw: [" + jsonPendenza + "]");
				beanDati.setDataUltimoAggiornamento(new Date());

				tracciatiBD.updateBeanDati(tracciato, serializer.getObject(beanDati));
				this.commit();

				BatchManager.aggiornaEsecuzione(this, Operazioni.batch_tracciati);

			}

			// eseguo operazioni del
			numLinea = beanDati.getLineaElaborazioneDel();
			
			log.debug("Elaboro le operazioni di annullamento del tracciato saltando le prime " + numLinea + " linee");
			for(long linea = numLinea; linea < beanDati.getNumDelTotali() ; linea ++) {
				AnnullamentoPendenza annullamento = annullamenti.get((int) linea);
				AnnullamentoRequest request = new AnnullamentoRequest();
				request.setCodApplicazione(annullamento.getIdA2A());
				request.setCodVersamentoEnte(annullamento.getIdPendenza());
				request.setMotivoAnnullamento(annullamento.getMotivoAnnullamento());
				request.setLinea(beanDati.getNumAddTotali() + linea + 1);
				request.setOperatore(tracciato.getOperatore(this));

				AnnullamentoResponse annullamentoResponse = factory.annullaVersamento(request, this);

				this.setAutoCommit(false);

				Operazione operazione = new Operazione();
				operazione.setCodVersamentoEnte(request.getCodVersamentoEnte());
				String jsonPendenza = annullamento.toJSON(null);
				operazione.setDatiRichiesta(jsonPendenza.getBytes());
				operazione.setDatiRisposta(annullamentoResponse.getEsitoOperazionePendenza().toJSON(null).getBytes());
				operazione.setStato(annullamentoResponse.getStato());
				if(annullamentoResponse.getDescrizioneEsito() != null)
					operazione.setDettaglioEsito(annullamentoResponse.getDescrizioneEsito().length() > 255 ? annullamentoResponse.getDescrizioneEsito().substring(0, 255) : annullamentoResponse.getDescrizioneEsito());
				try {
					operazione.setIdApplicazione(AnagraficaManager.getApplicazione(this, annullamentoResponse.getIdA2A()).getId());
				} catch(Exception e) {
					// CodApplicazione non censito in anagrafica.
				}
				operazione.setIdTracciato(tracciato.getId());
				// proseguo il conteggio delle linee sommandole a quelle delle operazioni di ADD
				operazione.setLineaElaborazione(beanDati.getNumAddTotali() + linea + 1);
				operazione.setTipoOperazione(TipoOperazioneType.DEL);
				operazione.setCodDominio(tracciato.getCodDominio());
				operazioniBD.insertOperazione(operazione);

				if(operazione.getStato().equals(StatoOperazioneType.ESEGUITO_OK)) {
					beanDati.setNumDelOk(beanDati.getNumDelOk()+1);
				} else {
					if(!annullamentoResponse.getEsito().equals(CostantiCaricamento.EMPTY.toString()))
						beanDati.setNumDelKo(beanDati.getNumDelKo()+1);
				}				
				beanDati.setLineaElaborazioneDel(beanDati.getLineaElaborazioneDel()+1);	
				log.debug("Annullamento Pendenza Numero ["+ numLinea + "] elaborata con esito [" +operazione.getStato() + "]: " + operazione.getDettaglioEsito() + " Raw: [" + jsonPendenza + "]");
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
			List<FilterSortWrapper> fsl = new ArrayList<FilterSortWrapper>();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setSortOrder(SortOrder.ASC);
			fsw.setField(it.govpay.orm.Operazione.model().LINEA_ELABORAZIONE); 
			fsl.add(fsw );
			filter.setFilterSortList(fsl );

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			DettaglioTracciatoPendenzeEsito esitoElaborazioneTracciato = new DettaglioTracciatoPendenzeEsito();
			esitoElaborazioneTracciato.setIdTracciato(tracciato.getFileNameRichiesta()); 
			List<EsitoOperazionePendenza> esitiAnnullamenti = new ArrayList<>();
			List<EsitoOperazionePendenza> esitiInserimenti = new ArrayList<>();
			
			while(true) {
				// Ciclo finche' non mi ritorna meno record del limit. Altrimenti esco perche' ho finito
				List<Operazione> findAll = operazioniBD.findAll(filter);
				for(Operazione operazione : findAll) {
					switch (operazione.getTipoOperazione()) {
					case ADD:
						esitiInserimenti.add(EsitoOperazionePendenza.parse(new String(operazione.getDatiRisposta())));
						break;
					case DEL:
						esitiAnnullamenti.add(EsitoOperazionePendenza.parse(new String(operazione.getDatiRisposta())));
						break;
					case INC:
					case N_V:
					default:
						break;
					}
				}
				if(findAll.size() == 500) {
					filter.setOffset(filter.getOffset() + 500);
				} else {
					break;
				}
			}

			esitoElaborazioneTracciato.setAnnullamenti(esitiAnnullamenti);
			esitoElaborazioneTracciato.setInserimenti(esitiInserimenti);

			if((beanDati.getNumAddKo() + beanDati.getNumDelKo()) > 0) {
				beanDati.setStepElaborazione(StatoTracciatoType.CARICAMENTO_KO.getValue());
			} else {
				beanDati.setStepElaborazione(StatoTracciatoType.CARICAMENTO_OK.getValue());
			}

			tracciato.setRawEsito(esitoElaborazioneTracciato.toJSON(null).getBytes());
			try {baos.flush();} catch(Exception e){}
			try {baos.close();} catch(Exception e){}
			
			tracciato.setFileNameEsito("esito_" + tracciato.getFileNameRichiesta()); 

			if((beanDati.getNumAddKo() + beanDati.getNumDelKo()) == (beanDati.getNumAddTotali() + beanDati.getNumDelTotali()))
				tracciato.setStato(STATO_ELABORAZIONE.SCARTATO);
			else 
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

	public LeggiOperazioneDTOResponse fillOperazione(Operazione operazione) throws ServiceException {
		LeggiOperazioneDTOResponse leggiOperazioneDTOResponse = new LeggiOperazioneDTOResponse();

		switch (operazione.getTipoOperazione()) {
		case ADD:
			VersamentiBD versamentiBD = new VersamentiBD(this);
			OperazioneCaricamento operazioneCaricamento = new OperazioneCaricamento(operazione);
			try {
				Versamento versamento = versamentiBD.getVersamento(operazione.getIdApplicazione(), operazione.getCodVersamentoEnte());
				versamento.getSingoliVersamenti(this);
				versamento.getDominio(this);
				versamento.getUo(this);
				versamento.getApplicazione(this);
				versamento.getIuv(this);
				operazioneCaricamento.setVersamento(versamento);
				
				operazioneCaricamento.getApplicazione(this);
				operazioneCaricamento.getDominio(this);
			}catch(NotFoundException e) {
				// do nothing
			}

			leggiOperazioneDTOResponse.setOperazione(operazioneCaricamento);
			break;
		case DEL:
			OperazioneAnnullamento operazioneAnnullamento = new OperazioneAnnullamento(operazione);
			try {
				AnnullamentoPendenza annullamentoP = AnnullamentoPendenza.parse(new String(operazione.getDatiRichiesta()));
				operazioneAnnullamento.setMotivoAnnullamento(annullamentoP.getMotivoAnnullamento());
				
				operazioneAnnullamento.getApplicazione(this);
				try {
					operazioneAnnullamento.getDominio(this);
				} catch (NotFoundException e1) {
				}
			}catch(ValidationException e){

			}
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
