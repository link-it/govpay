package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.AclPost;
import it.govpay.backoffice.v1.beans.DominioIndex;
import it.govpay.backoffice.v1.beans.Operatore;
import it.govpay.backoffice.v1.beans.OperatorePost;
import it.govpay.backoffice.v1.beans.TipoEntrata;
import it.govpay.backoffice.v1.controllers.ApplicazioniController;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Tributo;
import it.govpay.core.dao.anagrafica.dto.PutOperatoreDTO;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.model.Acl;

public class OperatoriConverter {

	public static PutOperatoreDTO getPutOperatoreDTO(OperatorePost operatoreRequest, String principal,	Authentication user) throws NotAuthorizedException, ServiceException{
		PutOperatoreDTO putOperatoreDTO = new PutOperatoreDTO(user);
		
		it.govpay.bd.model.Operatore operatore = new it.govpay.bd.model.Operatore();
		it.govpay.bd.model.Utenza utenza = new it.govpay.bd.model.Utenza();
		utenza.setAbilitato(operatoreRequest.isAbilitato());
		utenza.setPrincipal(principal);
		utenza.setPrincipalOriginale(principal);
		
		if(operatoreRequest.getAcl()!=null) {
			List<Acl> aclList = new ArrayList<>();
			for(AclPost acls: operatoreRequest.getAcl()) {
				aclList.add(AclConverter.getPostAclDTO(acls, user).getAcl());
			}
			utenza.setAclPrincipal(aclList);
		}
		operatore.setUtenza(utenza);
		operatore.setNome(operatoreRequest.getRagioneSociale()); 
		
		boolean appAuthEntrateAll = false;
		boolean appAuthDominiAll = false;
		
		if(operatoreRequest.getEntrate() != null) {
			List<String> idTributi = new ArrayList<>();
						
			for (String id : operatoreRequest.getEntrate()) {
				if(id.equals(ApplicazioniController.AUTORIZZA_TRIBUTI_STAR)) {
					appAuthEntrateAll = true;
					idTributi.clear();
					break;
				} 

				idTributi.add(id.toString());
			}
			
			putOperatoreDTO.setIdTributi(idTributi);
		}
		
		operatore.getUtenza().setAutorizzazioneTributiStar(appAuthEntrateAll);
		
		if(operatoreRequest.getDomini() != null) {
			List<String> idDomini = new ArrayList<>();
			
			for (String id : operatoreRequest.getDomini()) {
				if(id.equals(ApplicazioniController.AUTORIZZA_DOMINI_STAR)) {
					appAuthDominiAll = true;
					idDomini.clear();
					break;
				}
				idDomini.add(id);
			}
			
			putOperatoreDTO.setIdDomini(idDomini);
		}
		operatore.getUtenza().setAutorizzazioneDominiStar(appAuthDominiAll);
		
		putOperatoreDTO.setPrincipal(principal);
		putOperatoreDTO.setOperatore(operatore);
		
		return putOperatoreDTO;
	}
	 
	
	public static Operatore toRsModel(it.govpay.bd.model.Operatore operatore) throws ServiceException {
		Operatore rsModel = new Operatore();
		rsModel.abilitato(operatore.getUtenza().isAbilitato())
		.principal(operatore.getUtenza().getPrincipalOriginale())
		.ragioneSociale(operatore.getNome());
		
		
		
		List<DominioIndex> idDomini = new ArrayList<>();
		if(operatore.getUtenza().isAutorizzazioneDominiStar()) {
			DominioIndex tuttiDomini = new DominioIndex();
			tuttiDomini.setIdDominio(ApplicazioniController.AUTORIZZA_DOMINI_STAR);
			tuttiDomini.setRagioneSociale(ApplicazioniController.AUTORIZZA_DOMINI_STAR_LABEL);
			idDomini.add(tuttiDomini);
		} else if(operatore.getUtenza().getDomini(null) != null) {
			for (Dominio dominio : operatore.getUtenza().getDomini(null)) {
				idDomini.add(DominiConverter.toRsModelIndex(dominio));
			}
		}
		
		rsModel.setDomini(idDomini);

		List<TipoEntrata> idTributi = new ArrayList<>();
		List<Tributo> tributi = operatore.getUtenza().getTributi(null);
		if(tributi == null)
			tributi = new ArrayList<>();
		
		if(operatore.getUtenza().isAutorizzazioneTributiStar()) {
			TipoEntrata tEI = new TipoEntrata();
			tEI.setIdEntrata(ApplicazioniController.AUTORIZZA_TRIBUTI_STAR);
			tEI.setDescrizione(ApplicazioniController.AUTORIZZA_TRIBUTI_STAR_LABEL);
			idTributi.add(tEI);
		} else {
			for (Tributo tributo : tributi) {
				TipoEntrata tEI = new TipoEntrata();
				tEI.setIdEntrata(tributo.getCodTributo());
				tEI.setDescrizione(tributo.getDescrizione());
				idTributi.add(tEI);
			}
		}
		
		if(operatore.getUtenza().getAcls()!=null) {
			List<AclPost> aclList = new ArrayList<>();
			
			for(Acl acl: operatore.getUtenza().getAcls()) {
				aclList.add(AclConverter.toRsModel(acl));
			}
			
			rsModel.setAcl(aclList);
		}
		

		
		return rsModel;
	}
}

