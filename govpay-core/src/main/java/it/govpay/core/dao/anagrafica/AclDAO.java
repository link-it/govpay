package it.govpay.core.dao.anagrafica;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.anagrafica.filters.AclFilter;
import it.govpay.bd.anagrafica.filters.ApplicazioneFilter;
import it.govpay.core.dao.anagrafica.dto.FindApplicazioniDTOResponse;
import it.govpay.core.dao.anagrafica.dto.ListaAclDTO;
import it.govpay.core.dao.anagrafica.dto.ListaAclDTOResponse;
import it.govpay.core.utils.GpThreadLocal;

public class AclDAO {

	public ListaAclDTOResponse leggiAclRuoloRegistrateSistema(ListaAclDTO listaAclDTO) throws ServiceException {
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		try {
			
			AclBD aclBD = new AclBD(bd);
			AclFilter filter = aclBD.newFilter();
			// tutte le acl registrate per ruolo
			filter.setForceRuolo(true);
			
			return new ListaAclDTOResponse(aclBD.count(filter), aclBD.findAll(filter));
			
		} finally {
			bd.closeConnection();
		}
	}
	
	public ListaAclDTOResponse leggiAclPrincipalRegistrateSistema(ListaAclDTO listaAclDTO) throws ServiceException {
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		try {
			
			AclBD aclBD = new AclBD(bd);
			AclFilter filter = aclBD.newFilter();
			// tutte le acl registrate per principal
			filter.setForcePrincipal(true);
			
			return new ListaAclDTOResponse(aclBD.count(filter), aclBD.findAll(filter));
			
		} finally {
			bd.closeConnection();
		}
	}
	
}
