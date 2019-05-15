package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.AclPost;
import it.govpay.backoffice.v1.beans.DominioIndex;
import it.govpay.backoffice.v1.beans.Operatore;
import it.govpay.backoffice.v1.beans.OperatorePost;
import it.govpay.backoffice.v1.beans.TipoPendenza;
import it.govpay.backoffice.v1.controllers.ApplicazioniController;
import it.govpay.bd.model.Acl;
import it.govpay.bd.model.Dominio;
import it.govpay.core.dao.anagrafica.dto.PutOperatoreDTO;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.model.TipoVersamento;

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
				aclList.add(AclConverter.getAclUtenza(acls, utenza));
			}
			utenza.setAclPrincipal(aclList);
		}
		operatore.setUtenza(utenza);
		operatore.setNome(operatoreRequest.getRagioneSociale()); 
		
		boolean appAuthTipiPendenzaAll = false;
		boolean appAuthDominiAll = false;
		
		if(operatoreRequest.getTipiPendenza() != null) {
			List<String> idTipiVersamento = new ArrayList<>();
						
			for (String id : operatoreRequest.getTipiPendenza()) {
				if(id.equals(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR)) {
					appAuthTipiPendenzaAll = true;
					idTipiVersamento.clear();
					break;
				} 

				idTipiVersamento.add(id.toString());
			}
			
			putOperatoreDTO.setCodTipiVersamento(idTipiVersamento);
		}
		
		operatore.getUtenza().setAutorizzazioneTipiVersamentoStar(appAuthTipiPendenzaAll);
		
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
			
			putOperatoreDTO.setCodDomini(idDomini);
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

		List<TipoPendenza> idTipiPendenza = new ArrayList<>();
		List<TipoVersamento> tipiVersamento = operatore.getUtenza().getTipiVersamento(null);
		if(tipiVersamento == null)
			tipiVersamento = new ArrayList<>();
		
		if(operatore.getUtenza().isAutorizzazioneTipiVersamentoStar()) {
			TipoPendenza tPI = new TipoPendenza();
			tPI.setIdTipoPendenza(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR);
			tPI.setDescrizione(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR_LABEL);
			idTipiPendenza.add(tPI);
		} else {
			for (TipoVersamento tipoVersamento : tipiVersamento) {
				TipoPendenza tPI = new TipoPendenza();
				tPI.setIdTipoPendenza(tipoVersamento.getCodTipoVersamento());
				tPI.setDescrizione(tipoVersamento.getDescrizione());
				idTipiPendenza.add(tPI);
			}
		}
		
		rsModel.setTipiPendenza(idTipiPendenza);
		
		if(operatore.getUtenza().getAcls()!=null) {
			List<AclPost> aclList = new ArrayList<>();
			
			for(Acl acl: operatore.getUtenza().getAcls()) {
				AclPost aclRsModel = AclConverter.toRsModel(acl);
				if(aclRsModel != null)
					aclList.add(aclRsModel);
			}
			
			rsModel.setAcl(aclList);
		}
		

		
		return rsModel;
	}
}

