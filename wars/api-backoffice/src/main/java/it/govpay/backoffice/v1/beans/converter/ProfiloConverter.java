package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.beans.AclPost;
import it.govpay.backoffice.v1.beans.DominioProfiloIndex;
import it.govpay.backoffice.v1.beans.Profilo;
import it.govpay.backoffice.v1.beans.TipoPendenza;
import it.govpay.bd.model.Acl;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.model.UtenzaOperatore;
import it.govpay.core.dao.anagrafica.dto.LeggiProfiloDTOResponse;
import it.govpay.model.TipoVersamento;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 12 giu 2018 $
 * 
 */
public class ProfiloConverter {

	/**
	 * @param user
	 * @return
	 * @throws ServiceException 
	 */
	public static Profilo getProfilo(LeggiProfiloDTOResponse leggiProfilo) throws ServiceException {
		Profilo profilo = new Profilo();
		
		Utenza user = leggiProfilo.getUtente();
		List<Acl> aclsProfilo = user.getAclsProfilo();
		if(aclsProfilo!=null) {
			List<AclPost> aclLst = new ArrayList<>();
			for(Acl acl: aclsProfilo) { 
				AclPost aclRsModel = AclConverter.toRsModel(acl);
				if(aclRsModel != null)
					aclLst.add(aclRsModel);

			}
			profilo.setAcl(aclLst);
		}
		profilo.setNome(leggiProfilo.getNome());
		
		switch(user.getTipoUtenza()) {
		case ANONIMO:
		case APPLICAZIONE:
		case CITTADINO:
			break;
		case OPERATORE:
			profilo.setNome(((UtenzaOperatore) user).getNome());
			break;
		}
		
		if(leggiProfilo.getDomini()!=null) {
			List<DominioProfiloIndex> dominiLst = new ArrayList<>();
			for(Dominio dominio: leggiProfilo.getDomini()) {
				dominiLst.add(DominiConverter.toRsModelProfiloIndex(dominio));
			}
			profilo.setDomini(dominiLst);
		}
		if(leggiProfilo.getTipiVersamento()!=null) {
			List<TipoPendenza> tipiPendenzaLst = new ArrayList<>();
			for(TipoVersamento tributo: leggiProfilo.getTipiVersamento()) {
				tipiPendenzaLst.add(TipiPendenzaConverter.toTipoPendenzaRsModel(tributo));
			}
			profilo.setTipiPendenza(tipiPendenzaLst);
		}
		
		return profilo;
	}

}
