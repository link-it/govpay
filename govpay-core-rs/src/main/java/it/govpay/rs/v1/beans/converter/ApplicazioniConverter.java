package it.govpay.rs.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Tributo;
import it.govpay.core.dao.anagrafica.dto.PutApplicazioneDTO;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.rs.v1.beans.base.AclPost;
import it.govpay.core.rs.v1.beans.base.Applicazione;
import it.govpay.core.rs.v1.beans.base.ApplicazionePost;
import it.govpay.core.rs.v1.beans.base.CodificaAvvisi;
import it.govpay.core.rs.v1.beans.base.DominioIndex;
import it.govpay.core.rs.v1.beans.base.TipoEntrata;
import it.govpay.model.Acl;
import it.govpay.model.IAutorizzato;
import it.govpay.model.Rpt.FirmaRichiesta;
import it.govpay.rs.v1.controllers.base.ApplicazioniController;

public class ApplicazioniConverter {
	
	public static PutApplicazioneDTO getPutApplicazioneDTO(ApplicazionePost applicazionePost, String idA2A, IAutorizzato user) throws ServiceException,NotAuthorizedException {
		PutApplicazioneDTO applicazioneDTO = new PutApplicazioneDTO(user);
		it.govpay.bd.model.Applicazione applicazione = new it.govpay.bd.model.Applicazione();
		it.govpay.bd.model.Utenza utenza = new it.govpay.bd.model.Utenza();
		utenza.setAbilitato(applicazionePost.isAbilitato());
		utenza.setPrincipal(applicazionePost.getPrincipal());
		utenza.setPrincipalOriginale(applicazionePost.getPrincipal()); 
		applicazione.setUtenza(utenza);
		applicazioneDTO.setIdUtenza(applicazionePost.getPrincipal());
		
		applicazioneDTO.setIdDomini(applicazionePost.getDomini());
		
		applicazione.setTrusted(false);
		if(applicazionePost.getEntrate() != null) {
			List<String> idTributi = new ArrayList<>();
						
			for (String id : applicazionePost.getEntrate()) {
				if(id.equals(ApplicazioniController.AUTODETERMINAZIONE_TRIBUTI_VALUE)) {
					idTributi.clear();
					applicazione.setTrusted(true);
					break;
				}
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
		rsModel.setPrincipal(applicazione.getUtenza().getPrincipalOriginale());
		
		if(applicazione.getConnettoreNotifica()!=null)
			rsModel.setServizioNotifica(ConnettoriConverter.toRsModel(applicazione.getConnettoreNotifica()));
		
		if(applicazione.getConnettoreVerifica()!=null)
			rsModel.setServizioVerifica(ConnettoriConverter.toRsModel(applicazione.getConnettoreVerifica()));
		
		
		if(applicazione.getUtenza().getDomini(null) != null) {
			List<DominioIndex> idDomini = new ArrayList<>();
			for (Dominio dominio : applicazione.getUtenza().getDomini(null)) {
				idDomini.add(DominiConverter.toRsModelIndex(dominio));
			}
			rsModel.setDomini(idDomini);
		}

		List<TipoEntrata> idTributi = new ArrayList<>();
		List<Tributo> tributi = applicazione.getUtenza().getTributi(null);
		if(tributi == null)
			tributi = new ArrayList<>();
		
		if(applicazione.isTrusted() && tributi.size() == 0) {
			TipoEntrata tEI = new TipoEntrata();
			tEI.setIdEntrata(ApplicazioniController.AUTODETERMINAZIONE_TRIBUTI_VALUE);
			tEI.setDescrizione(ApplicazioniController.AUTODETERMINAZIONE_TRIBUTI_LABEL);
			idTributi.add(tEI);
		} else {
			for (Tributo tributo : tributi) {
				TipoEntrata tEI = new TipoEntrata();
				tEI.setIdEntrata(tributo.getCodTributo());
				tEI.setDescrizione(tributo.getDescrizione());
				idTributi.add(tEI);
			}
		}
		
		if(applicazione.getUtenza().getAcls()!=null) {
			List<AclPost> aclList = new ArrayList<>();
			
			for(Acl acl: applicazione.getUtenza().getAcls()) {
				aclList.add(AclConverter.toRsModel(acl));
			}
			
			rsModel.setAcl(aclList);
		}
		
		rsModel.setEntrate(idTributi);
		
		return rsModel;
	}
}
