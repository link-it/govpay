package it.govpay.core.dao.anagrafica;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.filters.AclFilter;
import it.govpay.bd.model.Acl;
import it.govpay.core.dao.anagrafica.dto.LeggiRuoloDTO;
import it.govpay.core.dao.anagrafica.dto.LeggiRuoloDTOResponse;
import it.govpay.core.dao.anagrafica.dto.ListaRuoliDTO;
import it.govpay.core.dao.anagrafica.dto.ListaRuoliDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PatchRuoloDTO;
import it.govpay.core.dao.anagrafica.dto.PatchRuoloDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutRuoloDTO;
import it.govpay.core.dao.anagrafica.dto.PutRuoloDTOResponse;
import it.govpay.core.dao.anagrafica.utils.UtenzaPatchUtils;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.model.PatchOp;
import it.govpay.orm.ACL;

public class RuoliDAO extends BaseDAO{

	public RuoliDAO() {
		super();
	}

	public RuoliDAO(boolean useCacheData) {
		super(useCacheData);
	}

	public LeggiRuoloDTOResponse leggiRuoli(LeggiRuoloDTO leggiRuoliDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException{
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		AclBD aclBD = null;
		LeggiRuoloDTOResponse response = null;
		try {
			aclBD = new AclBD(configWrapper);
			AclFilter filter = aclBD.newFilter();
			filter.setRuolo(leggiRuoliDTO.getRuolo());
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(ACL.model().RUOLO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.addFilterSort(fsw);
			long count= aclBD.count(filter); 

			List<Acl> lst = aclBD.findAll(filter); 
			response = new LeggiRuoloDTOResponse(count, lst);
			
		} finally {
			if(aclBD != null)
				aclBD.closeConnection();
		}
		return response;
	}

	public ListaRuoliDTOResponse listaRuoli(ListaRuoliDTO listaRuoliDTO) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		AclBD aclBD = null;

		try {
			aclBD = new AclBD(configWrapper);
			AclFilter filter = aclBD.newFilter();
	
			filter.setOffset(listaRuoliDTO.getOffset());
			filter.setLimit(listaRuoliDTO.getLimit());
			filter.setForceRuolo(true);
			filter.getFilterSortList().addAll(listaRuoliDTO.getFieldSortList());
			filter.setEseguiCountConLimit(listaRuoliDTO.isEseguiCountConLimit());
			
			Long count = null;
			
			if(listaRuoliDTO.isEseguiCount()) {
				 count = aclBD.count(filter);
			}
			
			List<String> resList = new ArrayList<>();
			if(listaRuoliDTO.isEseguiFindAll()) {
				resList = aclBD.findAllRuoli(filter);
				
				if(listaRuoliDTO.getLimit() == null && !listaRuoliDTO.isEseguiCount()) {
					count = (long) resList.size();
				}
			}
	
			return new ListaRuoliDTOResponse(count, resList);
			
		} finally {
			if(aclBD != null)
				aclBD.closeConnection();
		}

	}

	public PutRuoloDTOResponse createOrUpdate(PutRuoloDTO listaRuoliDTO) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		PutRuoloDTOResponse response = new PutRuoloDTOResponse();
		AclBD aclBD = null;

		try {
			aclBD = new AclBD(configWrapper);
			AclFilter filter = aclBD.newFilter();
			
			filter.setForceRuolo(true);
			filter.setRuolo(listaRuoliDTO.getIdRuolo());
			long count = aclBD.countRuoli(filter);
			
			if(count > 0) {
				// cancello le acl collegate al ruolo e le inserisco
				response.setCreated(false);
				
				// cancello le vecchie acl
				List<Acl> lst = aclBD.findAll(filter); 
				for(Acl acl: lst) {
					aclBD.deleteAcl(acl);
				}
			}
			
			// inserimento nuove acl
			for(Acl acl: listaRuoliDTO.getAcls()) {
				acl.setRuolo(listaRuoliDTO.getIdRuolo());
				
				if(aclBD.existsAcl(acl.getRuolo(), null, acl.getServizio())) {
					aclBD.updateAcl(acl);
				} else {
					aclBD.insertAcl(acl);
				}
			}
			
			return response;
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(aclBD != null)
				aclBD.closeConnection();
		}

	}
	
	public PatchRuoloDTOResponse patch(PatchRuoloDTO patchDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException{
		PatchRuoloDTOResponse patchRuoloDTOResponse = new PatchRuoloDTOResponse();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		AclBD aclBD = null;

		try {
			aclBD = new AclBD(configWrapper);
			AclFilter filter = aclBD.newFilter();
			filter.setRuolo(patchDTO.getIdRuolo());
			List<Acl> lst = aclBD.findAll(filter); 
			
			for(PatchOp op: patchDTO.getOp()) {
				UtenzaPatchUtils.patchRuolo(op, patchDTO.getIdRuolo(), lst, configWrapper);
			}

			return patchRuoloDTOResponse;
		}catch(Exception e) {
			throw new NotAuthorizedException("Operazione non autorizzata");
		}finally {
			if(aclBD != null)
				aclBD.closeConnection();
		}
		
	}

}
