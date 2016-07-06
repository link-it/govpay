package it.govpay.web.rs.converter;

import java.math.BigDecimal;
import java.util.Date;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Anagrafica;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.Versamento.CausaleSemplice;
import it.govpay.bd.model.Versamento.StatoVersamento;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.servizi.commons.EsitoOperazione;

public class VersamentoConverter {
	
	
	public static Versamento toVersamentoModel(it.govpay.web.rs.model.Versamento request, 
				Applicazione applicazioneAutenticata, boolean aggiornabile, BasicBD bd) throws ServiceException, GovPayException {
		
		
		String codiceCreditore = request.getCodiceCreditore();
		String codLotto =  request.getIdentificativoDebito();
		String codiceVersamentoLotto = null;
		
		String codiceTributo = request.getCodiceTributo();
		String codiceFiscaleContribuente = request.getCodiceFiscaleContribuente();
		String anagraficaContribuente = request.getAnagraficaContribuente();
		
		
		return toVersamentoModel(request, applicazioneAutenticata, codLotto, codiceVersamentoLotto, codiceCreditore, codiceTributo, codiceFiscaleContribuente, anagraficaContribuente, aggiornabile, bd);
	}

	public static Versamento toVersamentoModel(it.govpay.web.rs.model.Versamento versamento, Applicazione applicazioneAutenticata,
			String codLotto, String codVersamentoLotto, 
			  String codiceCreditore, String codiceTributo,
			String codiceFiscaleContribuente, String anagraficaContribuente,
			boolean aggiornabile, BasicBD bd) throws ServiceException, GovPayException {
		Versamento model = new Versamento();
		model.setAggiornabile(aggiornabile);

		if(versamento.getCausale() != null) {
			CausaleSemplice causale = model.new CausaleSemplice();
			causale.setCausale(versamento.getCausale());
			model.setCausaleVersamento(causale);
		}
		
		model.setCodLotto(codLotto);
		model.setCodVersamentoLotto(codVersamentoLotto);
		model.setCodBundlekey(versamento.getBundleKey()); 
				
		Anagrafica anagraficaDebitore = new Anagrafica();
		anagraficaDebitore.setCodUnivoco(codiceFiscaleContribuente);
		anagraficaDebitore.setRagioneSociale(anagraficaContribuente); 
		model.setAnagraficaDebitore(anagraficaDebitore);

		model.setCodVersamentoEnte(versamento.getIdentificativoVersamento());
		model.setDataCreazione(new Date());
		model.setDataScadenza(versamento.getDataScadenza());
		model.setDataUltimoAggiornamento(new Date());
		model.setDescrizioneStato(null);
		model.setId(null);
		try {
			model.setApplicazione(applicazioneAutenticata.getCodApplicazione(), bd);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.APP_000, applicazioneAutenticata.getCodApplicazione());
		}

		Dominio dominio = null;
		try {
			dominio = AnagraficaManager.getDominio(bd, codiceCreditore);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.DOM_000, codiceCreditore);
		}

		String codUnitaOperativa = Dominio.EC;
		try {
			model.setUo(dominio.getId(), codUnitaOperativa, bd);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.UOP_000, codUnitaOperativa , codiceCreditore);
		}

		BigDecimal importoVersamentoAsBigDecimal  = new BigDecimal(versamento.getImporto().doubleValue());
		model.setImportoTotale(importoVersamentoAsBigDecimal);
		model.setStatoVersamento(StatoVersamento.NON_ESEGUITO);

		model.addSingoloVersamento(toSingoloVersamentoModel(model, versamento,codiceTributo, bd));

		return model;
	}

	public static SingoloVersamento toSingoloVersamentoModel(Versamento versamentoModel, it.govpay.web.rs.model.Versamento versamentoRs, String codiceTributo, BasicBD bd) throws ServiceException, GovPayException {
		SingoloVersamento model = new SingoloVersamento();
		model.setVersamento(versamentoModel);
		model.setCodSingoloVersamentoEnte(versamentoModel.getCodVersamentoEnte());
		model.setId(null);
		model.setIdVersamento(0);
		BigDecimal importoSingoloVersamentoAsBigDecimal  = new BigDecimal(versamentoRs.getImporto().doubleValue());
		model.setImportoSingoloVersamento(importoSingoloVersamentoAsBigDecimal);
		model.setStatoSingoloVersamento(StatoSingoloVersamento.NON_ESEGUITO);
		model.setNote(versamentoRs.getNote()); 

		try {
			model.setTributo(codiceTributo, bd);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.TRB_000, versamentoModel.getUo(bd).getDominio(bd).getCodDominio(), codiceTributo);
		}

		if(!versamentoModel.getApplicazione(bd).isTrusted() && !versamentoModel.getApplicazione(bd).getIdTributi().contains(model.getIdTributo())) {
			throw new GovPayException(EsitoOperazione.VER_022, versamentoModel.getUo(bd).getDominio(bd).getCodDominio(), codiceTributo);
		}

		return model;
	}
}
