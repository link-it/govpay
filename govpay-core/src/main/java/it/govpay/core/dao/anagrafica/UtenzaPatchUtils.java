/**
 * 
 */
package it.govpay.core.dao.anagrafica;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.exception.ValidationException;

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

	public static Utenza patchUtenza(PatchOp op, Utenza utenza, BasicBD bd) throws ServiceException, NotFoundException, ValidationException {

		if("/acl".equals(op.getPath())) {
			LinkedHashMap<?,?> map = (LinkedHashMap<?,?>) op.getValue();
			Acl acl = new Acl();
			try {
				if(map.get("servizio") == null) throw new ValidationException("ACL non valida: atteso campo `servizio`");
				if(!(map.get("servizio") instanceof String)) throw new ValidationException("ACL non valida: attesa stringa come valore di `servizio`");
				acl.setServizio(Servizio.toEnum((String) map.get("servizio")));
			} catch (ServiceException se) {
				throw new ValidationException("ACL non valida: servizio `" + map.get("servizio") + "` non gestito.");
			}
			
			if(map.get("autorizzazioni") == null) throw new ValidationException("ACL non valida: atteso campo `autorizzazioni`");
			if(!(map.get("servizio") instanceof List<?>)) throw new ValidationException("ACL non valida: attesa lista di stringhe nel campo `servizio`");

			List<?> lstAuth = (List<?>) map.get("autorizzazioni");
			Set<Diritti> listaDiritti = new HashSet<>();
			
			for(Object obj: lstAuth) {
				try {
					if(!(obj instanceof String)) throw new ValidationException("ACL non valida: attesa lista di stringhe nel campo `servizio`");
					Diritti diritto = Diritti.toEnum((String)obj);
					if(!listaDiritti.contains(diritto))
						listaDiritti.add(diritto);
				} catch (ServiceException se) {
					throw new ValidationException("ACL non valida: servizio `" + map.get("servizio") + "` non gestito.");
				}
			}
			acl.setListaDiritti(listaDiritti);

			switch(op.getOp()) {
			case ADD: 
				boolean update = false;
				for(Acl aclPresente : utenza.getAcls()) {
					if(aclPresente.getServizio().equals(acl.getServizio())) {
						aclPresente.getListaDiritti().addAll(acl.getListaDiritti());
						update = true;
					}
				}
				if(!update)
					utenza.getAcls().add(acl); 
			break;
			case DELETE: 
				for(Acl aclPresente : utenza.getAcls()) {
					if(aclPresente.getServizio().equals(acl.getServizio())) {
						utenza.getAcls().remove(aclPresente);
					}
				}
			break;
			default: throw new ValidationException("Op '"+op.getOp().name()+"' non valido per il path '"+op.getPath()+"'");
			}

		} else if("/domini".equals(op.getPath())) {
			if(!(op.getValue() instanceof String)) throw new ValidationException("Value non valido per il path '"+op.getPath()+"'");
			String dominio = (String) op.getValue();

			try {
				AnagraficaManager.getDominio(bd, dominio).getId();
			} catch (NotFoundException e) {
				throw new ValidationException("Value non valido per il path '"+op.getPath()+"'");
			}
			Long idDominio = AnagraficaManager.getDominio(bd, dominio).getId();
			switch(op.getOp()) {
			case ADD: utenza.getIdDomini().add(idDominio); 
			break;
			case DELETE: utenza.getIdDomini().remove(idDominio);
			break;
			default: throw new ValidationException("Op '"+op.getOp().name()+"' non valido per il path '"+op.getPath()+"'");
			}

			utenza.setDomini(null);
			utenza.getDomini(bd);

		} else if("/entrate".equals(op.getPath())) {
			if(!(op.getValue() instanceof String)) throw new ValidationException("Value non valido per il path '"+op.getPath()+"'");
			String tributo = (String) op.getValue();
			try {
				AnagraficaManager.getTipoTributo(bd, tributo).getId();
			} catch (NotFoundException e) {
				throw new ValidationException("Value non valido per il path '"+op.getPath()+"'");
			}
			Long idTributo = AnagraficaManager.getTipoTributo(bd, tributo).getId();
			switch(op.getOp()) {
			case ADD: utenza.getIdTributi().add(idTributo); 
			break;
			case DELETE: utenza.getIdTributi().remove(idTributo);
			break;
			default: throw new ValidationException("Op '"+op.getOp().name()+"' non valido per il path '"+op.getPath()+"'");
			}

			utenza.setTributi(null);
			utenza.getTributi(bd);

		} else {
			throw new ValidationException("Path '"+op.getPath()+"' non valido");
		}

		return utenza;
	}

}
