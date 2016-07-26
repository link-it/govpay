package it.govpay.web.rs.converter;

import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.servizi.commons.Anagrafica;
import it.govpay.servizi.commons.Versamento;
import it.govpay.servizi.commons.Versamento.SingoloVersamento;

public class VersamentoConverter {
	
	
	public static Versamento toVersamentoCommons(it.govpay.web.rs.model.Versamento request, 
				Applicazione applicazioneAutenticata, boolean aggiornabile, BasicBD bd) throws ServiceException, GovPayException {
		
		
		String codiceCreditore = request.getCodiceCreditore();
		String codLotto =  request.getIdentificativoDebito();
		
		String codiceTributo = request.getCodiceTributo();
		String codiceFiscaleContribuente = request.getCodiceFiscaleContribuente();
		String anagraficaContribuente = request.getAnagraficaContribuente();
		
		
		return toVersamentoCommons(request, applicazioneAutenticata, codLotto,  codiceCreditore, codiceTributo, codiceFiscaleContribuente, anagraficaContribuente, aggiornabile, bd);
	}

	public static Versamento toVersamentoCommons(it.govpay.web.rs.model.Versamento versamento, Applicazione applicazioneAutenticata,
			String codLotto, 
			String codiceCreditore, String codiceTributo,
			String codiceFiscaleContribuente, String anagraficaContribuente,
			boolean aggiornabile, BasicBD bd) throws ServiceException, GovPayException {
		Versamento model = new Versamento();
		model.setAggiornabile(aggiornabile);

		if(versamento.getCausale() != null) {
			model.setCausale(versamento.getCausale());
		}
		
		model.setCodDebito(codLotto);
		model.setBundlekey(versamento.getBundleKey()); 
				
		Anagrafica anagraficaDebitore = new Anagrafica();
		anagraficaDebitore.setCodUnivoco(codiceFiscaleContribuente);
		anagraficaDebitore.setRagioneSociale(anagraficaContribuente); 
		model.setDebitore(anagraficaDebitore);

		model.setCodVersamentoEnte(versamento.getIdentificativoVersamento());
		model.setDataScadenza(versamento.getDataScadenza());
		model.setCodApplicazione(applicazioneAutenticata.getCodApplicazione());
		model.setCodDominio(codiceCreditore);
		model.setCodUnitaOperativa(Dominio.EC);

		BigDecimal importoVersamentoAsBigDecimal  = new BigDecimal(versamento.getImporto().doubleValue());
		model.setImportoTotale(importoVersamentoAsBigDecimal);

		model.getSingoloVersamento().add(toSingoloVersamentoCommons(model, versamento,codiceTributo, bd));

		return model;
	}

	public static SingoloVersamento toSingoloVersamentoCommons(Versamento versamentoModel, it.govpay.web.rs.model.Versamento versamentoRs, String codiceTributo, BasicBD bd) throws ServiceException, GovPayException {
		SingoloVersamento model = new SingoloVersamento();
		model.setCodSingoloVersamentoEnte(versamentoModel.getCodVersamentoEnte());
		BigDecimal importoSingoloVersamentoAsBigDecimal  = new BigDecimal(versamentoRs.getImporto().doubleValue());
		model.setImporto(importoSingoloVersamentoAsBigDecimal);
		model.setNote(versamentoRs.getNote());
		model.setCodTributo(codiceTributo);

		return model;
	}
}
