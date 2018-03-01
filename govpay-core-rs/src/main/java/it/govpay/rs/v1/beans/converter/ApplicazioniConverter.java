package it.govpay.rs.v1.beans.converter;

import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Utenza;
import it.govpay.core.dao.anagrafica.dto.PutApplicazioneDTO;
import it.govpay.model.Connettore;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.v1.beans.base.ApplicazionePost;
import it.govpay.rs.v1.beans.base.CodificaAvvisi;

public class ApplicazioniConverter {
	
	public static PutApplicazioneDTO getPutApplicazioneDTO(ApplicazionePost applicazionePost, String idA2A, IAutorizzato user) {
		PutApplicazioneDTO applicazioneDTO = new PutApplicazioneDTO(user);
		Applicazione applicazione = new Applicazione();
		Utenza utenza = new Utenza();
		utenza.setAbilitato(applicazionePost.isAbilitato());
		utenza.setPrincipal(applicazione.getPrincipal());
		applicazione.setUtenza(utenza);
		
		CodificaAvvisi codificaAvvisi = new CodificaAvvisi();
		codificaAvvisi.setCodificaIUV(applicazione.getCodApplicazioneIuv());
		codificaAvvisi.setRegExp(applicazione.getRegExp());
//		applicazione.setCodificaAvvisi(codificaAvvisi); //TODO
		
		applicazione.setCodApplicazione(applicazione.getCodApplicazione());
		applicazione.setConnettoreNotifica(getConnettore(applicazionePost.getServizioNotifica()));
		applicazione.setConnettoreVerifica(getConnettore(applicazionePost.getServizioVerifica()));
		applicazioneDTO.setApplicazione(applicazione);
		applicazioneDTO.setIdApplicazione(idA2A);
		return applicazioneDTO;		
	}

	/**
	 * @param connector
	 * @return
	 */
	private static Connettore getConnettore(it.govpay.rs.v1.beans.base.Connector connector) {
		// TODO Auto-generated method stub
		return new Connettore();
	}
}
