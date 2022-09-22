package it.govpay.pagopa.v2.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoNonValidoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.gde.GdeInvoker;
import it.govpay.gde.v1.model.NuovoEvento;
import it.govpay.gde.v1.model.NuovoEvento.EsitoEnum;
import it.govpay.pagopa.v2.beans.Connettore;
import it.govpay.pagopa.v2.client.exception.ClientException;
import it.govpay.pagopa.v2.client.verifica.IVerificaClient;
import it.govpay.pagopa.v2.client.verifica.impl.VerificaClient;
import it.govpay.pagopa.v2.entity.ApplicazioneEntity;
import it.govpay.pagopa.v2.entity.VersamentoEntity;
import it.govpay.pagopa.v2.entity.VersamentoEntity.StatoVersamento;
import it.govpay.pagopa.v2.entity.VersamentoEntity.TipologiaTipoVersamento;
import it.govpay.pagopa.v2.utils.DateUtils;
import it.govpay.pagopa.v2.utils.VersamentoUtils;

@Component
public class Versamento {

	@Value("${it.govpay.context.aggiornamentoValiditaMandatorio}")
	private boolean aggiornamentoValiditaMandatorio;
	
	@Autowired
	private Applicazione applicazioneBusiness;
	
//	@Autowired
	private GdeInvoker gdeInvoker;

	private static Logger log = LoggerFactory.getLogger(Versamento.class);

	public VersamentoEntity aggiornaVersamento(VersamentoEntity versamento) throws VersamentoScadutoException, VersamentoAnnullatoException, VersamentoDuplicatoException, 
	VersamentoSconosciutoException, GovPayException, VersamentoNonValidoException {
		// Se il versamento non e' in attesa, non aggiorno un bel niente
		if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO))
			return versamento;

		ApplicazioneEntity applicazione = versamento.getApplicazione();
		// Controllo se la data di scadenza e' indicata ed e' decorsa
		if(versamento.getDataScadenza() != null && DateUtils.isDataDecorsa(versamento.getDataScadenza())) {
			throw new VersamentoScadutoException(applicazione.getCodApplicazione(), versamento.getCodVersamentoEnte(), "-", "-", "-", "-", DateUtils.fromLocalDateTime(versamento.getDataScadenza()));
		}else {
			if(versamento.getDataValidita() != null && DateUtils.isDataDecorsa(versamento.getDataValidita())) {
				String codVersamentoEnte = versamento.getCodVersamentoEnte();
				String bundlekey = versamento.getCodBundlekey();
				String debitore = versamento.getDebitoreIdentificativo();
				String codDominio = versamento.getDominio().getCodDominio(); 
				String iuv = null;


				String codVersamentoEnteD = codVersamentoEnte != null ? codVersamentoEnte : "-";
				String bundlekeyD = bundlekey != null ? bundlekey : "-";
				String debitoreD = debitore != null ? debitore : "-";
				String dominioD = codDominio != null ? codDominio : "-";
				String iuvD = iuv != null ? iuv : "-";

				log.info("Decorsa data validita' del versamento [Versamento:{1} BundleKey:{2} Debitore:{3} Dominio:{4} Iuv:{5}] dall'applicativo gestore [Applicazione:{0}] necessaria verifica.", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);

				Connettore connettoreApplicazione = applicazioneBusiness.getConnettoreApplicazione(applicazione);
				if(connettoreApplicazione != null) {
					TipologiaTipoVersamento tipo = versamento.getTipo();
					try {
						versamento = acquisisciVersamento(applicazione, codVersamentoEnte, bundlekey, debitore, codDominio, iuv, tipo);
					} catch (ClientException e) {
						// Errore nella chiamata all'ente. Controllo se e' mandatoria o uso quel che ho
						if(this.aggiornamentoValiditaMandatorio) { 
							log.error("Rilevata eccezione durante il processo di aggiornamento della pendenza, la proprieta' aggiornamentoValiditaMandatorio == true aggiornamento terminato con errore: " + e.getMessage(),e);
							throw new VersamentoScadutoException(applicazione.getCodApplicazione(), codVersamentoEnte, bundlekeyD, debitoreD, dominioD, iuvD, DateUtils.fromLocalDateTime(versamento.getDataScadenza()));
						}

						log.debug("Rilevata eccezione durante il processo di aggiornamento della pendenza, la proprieta' aggiornamentoValiditaMandatorio == false quindi verra' utilizzata la pendenza originale. Errore: " + e.getMessage(),e);
					} catch (VersamentoSconosciutoException e) {
						// Versamento sconosciuto all'ente (bug dell'ente?). Controllo se e' mandatoria o uso quel che ho
						if(this.aggiornamentoValiditaMandatorio) { 
							log.error("Rilevata eccezione durante il processo di aggiornamento della pendenza, la proprieta' aggiornamentoValiditaMandatorio == true aggiornamento terminato con errore: " + e.getMessage(),e);
							throw new VersamentoScadutoException(applicazione.getCodApplicazione(), codVersamentoEnte, bundlekeyD, debitoreD, dominioD, iuvD, DateUtils.fromLocalDateTime(versamento.getDataScadenza()));
						}						
						log.debug("Rilevata eccezione durante il processo di aggiornamento della pendenza, la proprieta' aggiornamentoValiditaMandatorio == false quindi verra' utilizzata la pendenza originale. Errore: " + e.getMessage(),e);
					} catch (VersamentoNonValidoException e) {
						// Versamento non valido per errori di validazione, se e' mandatorio l'aggiornamento rilancio l'eccezione altrimenti uso quello che ho
						if(this.aggiornamentoValiditaMandatorio) {
							log.error("Rilevata eccezione durante il processo di aggiornamento della pendenza, la proprieta' aggiornamentoValiditaMandatorio == true aggiornamento terminato con errore: " + e.getMessage(),e);
							throw e;
						}

						log.debug("Rilevata eccezione durante il processo di aggiornamento della pendenza, la proprieta' aggiornamentoValiditaMandatorio == false quindi verra' utilizzata la pendenza originale. Errore: " + e.getMessage(),e);
					} catch (GovPayException e) {
						// Versamento non aggiornato per errori interni, se e' mandatorio l'aggiornamento rilancio l'eccezione altrimenti uso quello che ho
						if(this.aggiornamentoValiditaMandatorio) {
							log.error("Rilevata eccezione durante il processo di aggiornamento della pendenza, la proprieta' aggiornamentoValiditaMandatorio == true aggiornamento terminato con errore: " + e.getMessage(),e);
							throw e;
						}

						log.debug("Rilevata eccezione durante il processo di aggiornamento della pendenza, la proprieta' aggiornamentoValiditaMandatorio == false quindi verra' utilizzata la pendenza originale. Errore: " + e.getMessage(),e);
					}
				} else if(this.aggiornamentoValiditaMandatorio) 
					// connettore verifica non definito, versamento non aggiornabile
					throw new VersamentoScadutoException(applicazione.getCodApplicazione(), codVersamentoEnte, bundlekeyD, debitoreD, dominioD, iuvD, DateUtils.fromLocalDateTime(versamento.getDataScadenza()));
			} else {
				// versamento valido
			} 
		}
		return versamento;
	}

	public VersamentoEntity acquisisciVersamento(ApplicazioneEntity applicazione, String codVersamentoEnte, String bundlekey, String debitore, String dominio, String iuv, TipologiaTipoVersamento tipo) throws VersamentoScadutoException, VersamentoAnnullatoException, VersamentoDuplicatoException, 
	VersamentoSconosciutoException, GovPayException, VersamentoNonValidoException, ClientException  {
		String codVersamentoEnteD = codVersamentoEnte != null ? codVersamentoEnte : "-";
		String bundlekeyD = bundlekey != null ? bundlekey : "-";
		String debitoreD = debitore != null ? debitore : "-";
		String dominioD = dominio != null ? dominio : "-";
		String iuvD = iuv != null ? iuv : "-";

		log.debug("Verifica del versamento [Versamento:{} BundleKey:{} Debitore:{} Dominio:{} Iuv:{}] dall'applicativo gestore [Applicazione:{}] in corso...", codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD,applicazione.getCodApplicazione());
		
		if(applicazione.getCodConnettoreIntegrazione() == null) {
			log.debug("Servizio di verifica non configurato per l'applicazione {}. Pagamento sconosciuto.", applicazione.getCodApplicazione());
			throw new VersamentoSconosciutoException();
		}
		NuovoEvento eventoCtx = new NuovoEvento();
		IVerificaClient verificaClient = new VerificaClient(applicazione);
		// salvataggio id Rpt/ versamento/ pagamento
		eventoCtx.setIdDominio(dominio); 
		eventoCtx.setIuv(iuv);
		eventoCtx.setIdA2A(applicazione.getCodApplicazione());
		eventoCtx.setIdPendenza(codVersamentoEnte);
		VersamentoEntity versamento = null;
		try {
			try {
				versamento = verificaClient.invoke(codVersamentoEnte, bundlekey, debitore, dominio, iuv);
				log.info("Verificato versamento [Versamento:{} BundleKey:{} Debitore:{} Dominio:{} Iuv:{}] dall'applicativo gestore [Applicazione:{}]: versamento da pagare.", codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD, applicazione.getCodApplicazione());

				eventoCtx.setEsito(EsitoEnum.OK);
			} catch (ClientException e){
				log.error("Errore durante la verifica del versamento [Versamento:{} BundleKey:{} Debitore:{} Dominio:{} Iuv:{}] dall'applicativo gestore [Applicazione:{}]: {}", codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD, applicazione.getCodApplicazione(), e.getMessage());
				eventoCtx.setSottotipoEsito(e.getResponseCode() + "");
				eventoCtx.setEsito(EsitoEnum.FAIL);
				eventoCtx.setDettaglioEsito(e.getMessage());
//				eventoCtx.setException(e);
				throw e;
			} catch (VersamentoScadutoException e) {
				log.info("Verificato versamento [Versamento:{} BundleKey:{} Debitore:{} Dominio:{} Iuv:{}] dall'applicativo gestore [Applicazione:{}]: versamento scaduto.", codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD, applicazione.getCodApplicazione());
				eventoCtx.setSottotipoEsito("Pendenza Scaduta");
				eventoCtx.setEsito(EsitoEnum.KO);
				eventoCtx.setDettaglioEsito(e.getMessage());
//				eventoCtx.setException(e);
				throw e;
			} catch (VersamentoAnnullatoException e) {
				log.info("Verificato versamento [Versamento:{} BundleKey:{} Debitore:{} Dominio:{} Iuv:{}] dall'applicativo gestore [Applicazione:{}]: versamento annullato.", codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD, applicazione.getCodApplicazione());
				eventoCtx.setSottotipoEsito("Pendenza Annullata");
				eventoCtx.setEsito(EsitoEnum.KO);
				eventoCtx.setDettaglioEsito(e.getMessage());
//				eventoCtx.setException(e);
				throw e;
			} catch (VersamentoDuplicatoException e) {
				log.info("Verificato versamento [Versamento:{} BundleKey:{} Debitore:{} Dominio:{} Iuv:{}] dall'applicativo gestore [Applicazione:{}]: versamento duplicato.", codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD, applicazione.getCodApplicazione());
				eventoCtx.setSottotipoEsito("Pendenza Duplicata");
				eventoCtx.setEsito(EsitoEnum.KO);
				eventoCtx.setDettaglioEsito(e.getMessage());
//				eventoCtx.setException(e);
				throw e;
			} catch (VersamentoSconosciutoException e) {
				log.info("Verificato versamento [Versamento:{} BundleKey:{} Debitore:{} Dominio:{} Iuv:{}] dall'applicativo gestore [Applicazione:{}]: versamento sconosciuto.", codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD, applicazione.getCodApplicazione());
				eventoCtx.setSottotipoEsito("Pendenza Sconosciuta");
				eventoCtx.setEsito(EsitoEnum.KO);
				eventoCtx.setDettaglioEsito(e.getMessage());
//				eventoCtx.setException(e);
				throw e;
			} catch (VersamentoNonValidoException e) {
				log.error("Errore durante la verifica del versamento [Versamento:{} BundleKey:{} Debitore:{} Dominio:{} Iuv:{}] dall'applicativo gestore [Applicazione:{}]: {}", codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD, applicazione.getCodApplicazione(), e.getMessage());
				eventoCtx.setSottotipoEsito("Pendenza non valida");
				eventoCtx.setEsito(EsitoEnum.KO);
				eventoCtx.setDettaglioEsito(e.getMessage());
//				eventoCtx.setException(e);
				throw e;
			} 

//			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento();
			boolean generaIuv = VersamentoUtils.generaIUV(versamento);
			versamento.setTipo(tipo);
//			versamentoBusiness.caricaVersamento(versamento, generaIuv, true, false, null, null);
		}finally {
//			if(eventoCtx.isRegistraEvento()) {
//				GdeUtils.salvaEvento(eventoCtx);
//			}
//			this.gdeInvoker.salvaEvento(eventoCtx);
		}
		return versamento;
	}
	
	
}
