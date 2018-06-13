package it.govpay.rs.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Tributo;
import it.govpay.core.dao.anagrafica.dto.PutOperatoreDTO;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.rs.v1.beans.base.AclPost;
import it.govpay.core.rs.v1.beans.base.DominioIndex;
import it.govpay.core.rs.v1.beans.base.Operatore;
import it.govpay.core.rs.v1.beans.base.OperatorePost;
import it.govpay.core.rs.v1.beans.base.TipoEntrata;
import it.govpay.model.Acl;
import it.govpay.model.IAutorizzato;

public class OperatoriConverter {

	public static PutOperatoreDTO getPutOperatoreDTO(OperatorePost operatoreRequest, String principal,	IAutorizzato user) throws NotAuthorizedException, ServiceException{
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
		
		putOperatoreDTO.setIdDomini(operatoreRequest.getDomini());
		putOperatoreDTO.setIdTributi(operatoreRequest.getEntrate());
		
		putOperatoreDTO.setPrincipal(principal);
		putOperatoreDTO.setOperatore(operatore);
		
		return putOperatoreDTO;
	}
	 
	
	public static Operatore toRsModel(it.govpay.bd.model.Operatore operatore) throws ServiceException {
		Operatore rsModel = new Operatore();
		rsModel.abilitato(operatore.getUtenza().isAbilitato())
		.principal(operatore.getUtenza().getPrincipalOriginale())
		.ragioneSociale(operatore.getNome());
		
		
		if(operatore.getUtenza().getDomini(null) != null) {
			List<DominioIndex> idDomini = new ArrayList<DominioIndex>();
			for (Dominio dominio : operatore.getUtenza().getDomini(null)) {
				idDomini.add(DominiConverter.toRsModelIndex(dominio));
			}
			rsModel.setDomini(idDomini);
		}
		
		if(operatore.getUtenza().getTributi(null) != null) {
			List<TipoEntrata> idTributi = new ArrayList<TipoEntrata>();
			for (Tributo tributo : operatore.getUtenza().getTributi(null)) {
				TipoEntrata tEI = new TipoEntrata();
				tEI.setIdEntrata(tributo.getCodTributo());
				tEI.setDescrizione(tributo.getDescrizione());
				idTributi.add(tEI);
			}
			rsModel.setEntrate(idTributi);
		}
		
		
		return rsModel;
	}
}

