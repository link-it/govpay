/**
 * 
 */
package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.backoffice.v1.beans.AclPost;
import it.govpay.backoffice.v1.beans.DominioIndex;
import it.govpay.backoffice.v1.beans.Profilo;
import it.govpay.backoffice.v1.beans.TipoEntrata;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.Utenza;
import it.govpay.core.dao.anagrafica.dto.LeggiProfiloDTOResponse;
import it.govpay.model.Acl;

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
		if(user.getAcls()!=null) {
			List<AclPost> aclLst = new ArrayList<>();
			for(Acl acl: user.getAcls()) {
				aclLst.add(AclConverter.toRsModel(acl));

			}
			profilo.setAcl(aclLst);
		}
		profilo.setNome(leggiProfilo.getNome());
		if(leggiProfilo.getDomini()!=null) {
			List<DominioIndex> dominiLst = new ArrayList<>();
			for(Dominio dominio: leggiProfilo.getDomini()) {
				dominiLst.add(DominiConverter.toRsModelIndex(dominio));
			}
			profilo.setDomini(dominiLst);
		}
		if(leggiProfilo.getTributi()!=null) {
			List<TipoEntrata> entrateLst = new ArrayList<>();
			for(Tributo tributo: leggiProfilo.getTributi()) {
				entrateLst.add(EntrateConverter.toTipoEntrataRsModel(tributo));
			}
			profilo.setEntrate(entrateLst);
		}
		return profilo;
	}

}
