package it.govpay.core.dao.pagamenti;

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.filters.AclFilter;
import it.govpay.bd.model.Utenza;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRuoloDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRuoloDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRuoliDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRuoliDTOResponse;
import it.govpay.core.dao.pagamenti.exception.RicevutaNonTrovataException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;

public class RuoliDAO extends BaseDAO{

	public RuoliDAO() {
	}

	public LeggiRuoloDTOResponse leggiRuoli(LeggiRuoloDTO leggiRuoliDTO) throws ServiceException,RicevutaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{

		BasicBD bd = null;
		LeggiRuoloDTOResponse response = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			// controllo che il dominio sia autorizzato
			this.autorizzaRichiesta(leggiRuoliDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, leggiRuoliDTO.getRuolo(), null, bd);

			AclBD aclBD = new AclBD(bd);
			AclFilter filter = aclBD.newFilter();
			filter.setRuolo(leggiRuoliDTO.getRuolo());
			long count= aclBD.count(filter); 

			List<Acl> lst = aclBD.findAll(filter); 
			response = new LeggiRuoloDTOResponse(count, lst);
			
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return response;
	}

	public ListaRuoliDTOResponse listaRuoli(ListaRuoliDTO listaRuoliDTO) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			List<String> listaDominiFiltro;
			this.autorizzaRichiesta(listaRuoliDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);
	
			// Autorizzazione sui domini
			listaDominiFiltro = AclEngine.getDominiAutorizzati((Utenza) listaRuoliDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA);
			if(listaDominiFiltro == null) {
				throw new NotAuthorizedException("L'utenza autenticata ["+listaRuoliDTO.getUser().getPrincipal()+"] non e' autorizzata ai servizi " + Servizio.PAGAMENTI_E_PENDENZE + " per alcun dominio");
			}
	
			AclBD rptBD = new AclBD(bd);
			AclFilter filter = rptBD.newFilter();
	
			filter.setOffset(listaRuoliDTO.getOffset());
			filter.setLimit(listaRuoliDTO.getLimit());
	
			long count = rptBD.countRuoli(filter);
	
			List<String> resList = null;
			if(count > 0) {
				resList = rptBD.findAllRuoli(filter);
	
			} 
	
			return new ListaRuoliDTOResponse(count, resList);
			
		} finally {
			if(bd != null)
				bd.closeConnection();
		}

	}
}
