/**
 * 
 */
package it.govpay.rs.v1.beans.ragioneria.converter;

import java.util.ArrayList;
import java.util.List;

import it.govpay.core.rs.v1.beans.ragioneria.AclPost;
import it.govpay.core.rs.v1.beans.ragioneria.Profilo;
import it.govpay.model.Acl;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.v1.beans.ragioneria.converter.AclConverter;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 11 giu 2018 $
 * 
 */
public class ProfiliConverter {

	/**
	 * @param user
	 * @return
	 */
	public static Profilo toRsModel(IAutorizzato user) {
		Profilo profilo = new Profilo();
		List<AclPost> acls = new ArrayList<>();
		for(Acl acl: user.getAcls()) {
			acls.add(AclConverter.toRsModel(acl));
		}
		profilo.setAcl(acls);
//		profilo.setDomini(DominiConverter.toRsIndexModel(user.get	));
//		profilo.set
		return profilo;
	}

	
}
