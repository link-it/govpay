package it.govpay.pagopa.v2.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import it.govpay.pagopa.v2.beans.Connettore;
import it.govpay.pagopa.v2.entity.ApplicazioneEntity;
import it.govpay.pagopa.v2.entity.VersamentoEntity;
import it.govpay.pagopa.v2.entity.VersamentoEntity.StatoVersamento;
import it.govpay.pagopa.v2.entity.VersamentoEntity.TipologiaTipoVersamento;
import it.govpay.pagopa.v2.exception.GovPayException;
import it.govpay.pagopa.v2.exception.VersamentoAnnullatoException;
import it.govpay.pagopa.v2.exception.VersamentoDuplicatoException;
import it.govpay.pagopa.v2.exception.VersamentoNonValidoException;
import it.govpay.pagopa.v2.exception.VersamentoScadutoException;
import it.govpay.pagopa.v2.exception.VersamentoSconosciutoException;
import it.govpay.pagopa.v2.utils.DateUtils;

@Component
public class Versamento {

	@Value("${it.govpay.context.aggiornamentoValiditaMandatorio}")
	private boolean aggiornamentoValiditaMandatorio;
	
	@Autowired
	private Applicazione applicazioneBusiness;

	private static Logger log = LoggerFactory.getLogger(Versamento.class);

	public VersamentoEntity aggiornaVersamento(VersamentoEntity versamento) throws VersamentoScadutoException, VersamentoAnnullatoException, VersamentoDuplicatoException, 
	VersamentoSconosciutoException, GovPayException, VersamentoNonValidoException {
		// Se il versamento non e' in attesa, non aggiorno un bel niente
		if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO))
			return versamento;

		ApplicazioneEntity applicazione = versamento.getApplicazione();
		// Controllo se la data di scadenza e' indicata ed e' decorsa
		if(versamento.getDataScadenza() != null && DateUtils.isDataDecorsa(versamento.getDataScadenza())) {
			throw new VersamentoScadutoException(applicazione.getCodApplicazione(), versamento.getCodVersamentoEnte(), "-", "-", "-", "-", versamento.getDataScadenza());
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
//					} catch (ClientException e) {
//						// Errore nella chiamata all'ente. Controllo se e' mandatoria o uso quel che ho
//						if(this.aggiornamentoValiditaMandatorio) { 
//							log.error("Rilevata eccezione durante il processo di aggiornamento della pendenza, la proprieta' aggiornamentoValiditaMandatorio == true aggiornamento terminato con errore: " + e.getMessage(),e);
//							throw new VersamentoScadutoException(applicazione.getCodApplicazione(), codVersamentoEnte, bundlekeyD, debitoreD, dominioD, iuvD, versamento.getDataScadenza());
//						}
//
//						log.debug("Rilevata eccezione durante il processo di aggiornamento della pendenza, la proprieta' aggiornamentoValiditaMandatorio == false quindi verra' utilizzata la pendenza originale. Errore: " + e.getMessage(),e);
					} catch (VersamentoSconosciutoException e) {
						// Versamento sconosciuto all'ente (bug dell'ente?). Controllo se e' mandatoria o uso quel che ho
						if(this.aggiornamentoValiditaMandatorio) { 
							log.error("Rilevata eccezione durante il processo di aggiornamento della pendenza, la proprieta' aggiornamentoValiditaMandatorio == true aggiornamento terminato con errore: " + e.getMessage(),e);
							throw new VersamentoScadutoException(applicazione.getCodApplicazione(), codVersamentoEnte, bundlekeyD, debitoreD, dominioD, iuvD, versamento.getDataScadenza());
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
					throw new VersamentoScadutoException(applicazione.getCodApplicazione(), codVersamentoEnte, bundlekeyD, debitoreD, dominioD, iuvD, versamento.getDataScadenza());
			} else {
				// versamento valido
			} 
		}
		return versamento;
	}

	public VersamentoEntity acquisisciVersamento(ApplicazioneEntity applicazione, String codVersamentoEnte,
			String bundlekey, String debitore, String codDominio, String iuv, TipologiaTipoVersamento tipo) throws VersamentoScadutoException, VersamentoAnnullatoException, VersamentoDuplicatoException, 
	VersamentoSconosciutoException, GovPayException, VersamentoNonValidoException  {
		// TODO Auto-generated method stub
		return null;
	}
}
