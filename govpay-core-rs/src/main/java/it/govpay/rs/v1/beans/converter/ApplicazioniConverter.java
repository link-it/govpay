package it.govpay.rs.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Utenza;
import it.govpay.core.dao.anagrafica.dto.PutApplicazioneDTO;
import it.govpay.model.IAutorizzato;
import it.govpay.model.Rpt.FirmaRichiesta;
import it.govpay.rs.v1.beans.base.ApplicazionePost;
import it.govpay.rs.v1.beans.base.CodificaAvvisi;

public class ApplicazioniConverter {
	
	public static PutApplicazioneDTO getPutApplicazioneDTO(ApplicazionePost applicazionePost, String idA2A, IAutorizzato user) throws ServiceException {
		PutApplicazioneDTO applicazioneDTO = new PutApplicazioneDTO(user);
		Applicazione applicazione = new Applicazione();
		Utenza utenza = new Utenza();
		utenza.setAbilitato(applicazionePost.isAbilitato());
		utenza.setPrincipal(applicazionePost.getPrincipal());
		applicazione.setUtenza(utenza);
		applicazioneDTO.setIdUtenza(applicazionePost.getPrincipal());
		
		CodificaAvvisi codificaAvvisi = new CodificaAvvisi();
		codificaAvvisi.setCodificaIuv(applicazione.getCodApplicazioneIuv());
		codificaAvvisi.setRegExpIuv(applicazione.getRegExp());
		codificaAvvisi.setGenerazioneIuvInterna(applicazione.isAutoIuv());
		
		applicazione.setCodApplicazioneIuv(applicazionePost.getCodificaAvvisi().getCodificaIuv());
		applicazione.setRegExp(applicazionePost.getCodificaAvvisi().getRegExpIuv());
		applicazione.setAutoIuv(applicazionePost.getCodificaAvvisi().isGenerazioneIuvInterna());
		applicazione.setCodApplicazione(idA2A);
		applicazione.setFirmaRichiesta(FirmaRichiesta.NESSUNA);
		applicazione.setConnettoreNotifica(ConnettoriConverter.getConnettore(applicazionePost.getServizioNotifica()));
		applicazione.setConnettoreVerifica(ConnettoriConverter.getConnettore(applicazionePost.getServizioVerifica()));
		applicazioneDTO.setApplicazione(applicazione);
		applicazioneDTO.setIdApplicazione(idA2A);
		return applicazioneDTO;		
	}

}
