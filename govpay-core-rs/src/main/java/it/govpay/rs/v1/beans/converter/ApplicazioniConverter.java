package it.govpay.rs.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.dao.anagrafica.dto.PutApplicazioneDTO;
import it.govpay.core.rs.v1.beans.Applicazione;
import it.govpay.core.rs.v1.beans.base.ApplicazionePost;
import it.govpay.core.rs.v1.beans.base.CodificaAvvisi;
import it.govpay.model.IAutorizzato;
import it.govpay.model.Rpt.FirmaRichiesta;

public class ApplicazioniConverter {
	
	public static PutApplicazioneDTO getPutApplicazioneDTO(ApplicazionePost applicazionePost, String idA2A, IAutorizzato user) throws ServiceException {
		PutApplicazioneDTO applicazioneDTO = new PutApplicazioneDTO(user);
		it.govpay.bd.model.Applicazione applicazione = new it.govpay.bd.model.Applicazione();
		it.govpay.bd.model.Utenza utenza = new it.govpay.bd.model.Utenza();
		utenza.setAbilitato(applicazionePost.isAbilitato());
		utenza.setPrincipal(applicazionePost.getPrincipal());
		applicazione.setUtenza(utenza);
		applicazioneDTO.setIdUtenza(applicazionePost.getPrincipal());
		
		// TODO controllare tipi generati
		if(applicazionePost.getDomini() != null) {
			List<String> idDomini = new ArrayList<>();
			for (Object id : applicazionePost.getDomini()) {
				idDomini.add(id.toString());
			}
			applicazioneDTO.setIdDomini(idDomini);
		}
		
		// TODO controllare tipi generati
		if(applicazionePost.getEntrate() != null) {
			List<String> idTributi = new ArrayList<>();
			for (Object id : applicazionePost.getEntrate()) {
				idTributi.add(id.toString());
			}
			
			applicazioneDTO.setIdTributi(idTributi);
		}
		
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

	public static Applicazione toRsModel(it.govpay.bd.model.Applicazione applicazione) throws ServiceException {
		Applicazione rsModel = new Applicazione();
		rsModel.setAbilitato(applicazione.getUtenza().isAbilitato());
		
		CodificaAvvisi codificaAvvisi = new CodificaAvvisi();
		codificaAvvisi.setCodificaIuv(applicazione.getCodApplicazioneIuv());
		codificaAvvisi.setRegExpIuv(applicazione.getRegExp());
		codificaAvvisi.setGenerazioneIuvInterna(applicazione.isAutoIuv());
		rsModel.setCodificaAvvisi(codificaAvvisi);
		
		rsModel.setIdA2A(applicazione.getCodApplicazione());
		rsModel.setPrincipal(applicazione.getUtenza().getPrincipal());
		rsModel.setServizioNotifica(ConnettoriConverter.toRsModel(applicazione.getConnettoreNotifica()));
		rsModel.setServizioVerifica(ConnettoriConverter.toRsModel(applicazione.getConnettoreVerifica()));
		
		return rsModel;
	}
}
