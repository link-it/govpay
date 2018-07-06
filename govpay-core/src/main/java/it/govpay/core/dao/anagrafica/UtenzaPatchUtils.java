/**
 * 
 */
package it.govpay.core.dao.anagrafica;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Utenza;
import it.govpay.core.rs.v1.beans.base.PatchOp;
import it.govpay.model.Acl;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 03 lug 2018 $
 * 
 */
public class UtenzaPatchUtils {

	public static Utenza patchUtenza(PatchOp op, Utenza utenza, BasicBD bd) throws ServiceException, NotFoundException {

		if("/acl".equals(op.getPath())) {
				LinkedHashMap<?,?> map = (LinkedHashMap<?,?>) op.getValue();
				Acl acl = new Acl();
				acl.setServizio(Servizio.valueOf((String) map.get("servizio")));
				List<?> lstAuth = (List<?>) map.get("autorizzazioni");
				List<Diritti> listaDiritti = acl.getListaDiritti() != null ? acl.getListaDiritti() : new ArrayList<>(); 
				for(Object obj: lstAuth) {
					Diritti diritto = Diritti.valueOf((String)obj);
					if(!listaDiritti.contains(diritto))
						listaDiritti.add(diritto);
				}
				acl.setListaDiritti(listaDiritti);

				switch(op.getOp()) {
				
				case ADD: utenza.getAcls().add(acl); 
					break;
				case DELETE: utenza.getAcls().remove(acl);
					break;
				default: throw new ServiceException("Path '"+op.getPath()+"' non valido");
				}
				
			} else if("/domini".equals(op.getPath())) {
				String dominio = (String) op.getValue();
				
				Long idDominio = AnagraficaManager.getDominio(bd, dominio).getId();
				switch(op.getOp()) {
					case ADD: utenza.getIdDomini().add(idDominio); 
						break;
					case DELETE: utenza.getIdDomini().remove(idDominio);
						break;
					default: throw new ServiceException("Path '"+op.getPath()+"' non valido");
				}
				
				utenza.setDomini(null);
				utenza.getDomini(bd);

			} else if("/entrate".equals(op.getPath())) {
				String tributo = (String) op.getValue();
				Long idTributo = AnagraficaManager.getTipoTributo(bd, tributo).getId();
				switch(op.getOp()) {
					case ADD: utenza.getIdTributi().add(idTributo); 
						break;
					case DELETE: utenza.getIdTributi().remove(idTributo);
						break;
					default: throw new ServiceException("Path '"+op.getPath()+"' non valido");
				}
				
				utenza.setTributi(null);
				utenza.getTributi(bd);

			} else {
				throw new ServiceException("Path '"+op.getPath()+"' non valido");
			}

		return utenza;
	}

}
