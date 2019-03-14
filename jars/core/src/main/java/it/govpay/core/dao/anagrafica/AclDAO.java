package it.govpay.core.dao.anagrafica;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.filters.AclFilter;
import it.govpay.core.dao.anagrafica.dto.DeleteAclDTO;
import it.govpay.core.dao.anagrafica.dto.ListaAclDTO;
import it.govpay.core.dao.anagrafica.dto.ListaAclDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PostAclDTO;
import it.govpay.core.dao.anagrafica.dto.PostAclDTOResponse;
import it.govpay.core.dao.anagrafica.exception.AclNonTrovatoException;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;

public class AclDAO extends BaseDAO{
	
	public AclDAO() {
		super();
	}
	
	public AclDAO(boolean useCacheData) {
		super(useCacheData);
	}
	
	public ListaAclDTOResponse leggiAclRuoloRegistratoSistema(String ruolo) throws ServiceException {
		ListaAclDTO listaAclDTO = new ListaAclDTO(null);
		listaAclDTO.setForceRuolo(true);
		listaAclDTO.setRuolo(ruolo);
		return this._listaAcl(listaAclDTO);
	}

	public ListaAclDTOResponse leggiAclRuoloRegistrateSistema() throws ServiceException {
		ListaAclDTO listaAclDTO = new ListaAclDTO(null);
		listaAclDTO.setForceRuolo(true);
		return this._listaAcl(listaAclDTO);
	}

	private ListaAclDTOResponse _listaAcl(ListaAclDTO listaAclDTO) throws ServiceException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId(), useCacheData);
			return this._listaAcl(listaAclDTO, bd);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	private ListaAclDTOResponse _listaAcl(ListaAclDTO listaAclDTO, BasicBD bd) throws ServiceException {
		AclBD aclBD = new AclBD(bd);
		AclFilter filter = aclBD.newFilter();

		if(listaAclDTO.getForceRuolo() != null)
			filter.setForceRuolo(listaAclDTO.getForceRuolo());

		if(listaAclDTO.getForcePrincipal() != null)
			filter.setForcePrincipal(listaAclDTO.getForcePrincipal());

		if(listaAclDTO.getPrincipal() != null)
			filter.setPrincipal(listaAclDTO.getPrincipal());

		if(listaAclDTO.getRuolo() != null)
			filter.setRuolo(listaAclDTO.getRuolo());

		if(listaAclDTO.getServizio() != null)
			filter.setServizio(listaAclDTO.getServizio());

		filter.setOffset(listaAclDTO.getOffset());
		filter.setLimit(listaAclDTO.getLimit());
		filter.getFilterSortList().addAll(listaAclDTO.getFieldSortList());

		return new ListaAclDTOResponse(aclBD.count(filter), aclBD.findAll(filter));
	}

	public ListaAclDTOResponse leggiAclPrincipalRegistrateSistema() throws ServiceException {
		ListaAclDTO listaAclDTO = new ListaAclDTO(null);
		listaAclDTO.setForcePrincipal(true);
		return this._listaAcl(listaAclDTO);
	}

	/**
	 * @param putDominioDTO
	 * @return
	 * @throws NotAuthenticatedException 
	 */
	public PostAclDTOResponse create(PostAclDTO postAclDTO) throws NotAuthorizedException, ServiceException, NotAuthenticatedException { 
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId(), useCacheData);
			this.autorizzaRichiesta(postAclDTO.getUser(), Servizio.ANAGRAFICA_RUOLI, Diritti.SCRITTURA,bd); 

			AclBD aclBD = new AclBD(bd);
			PostAclDTOResponse leggiAclDTOResponse = new PostAclDTOResponse();
			boolean exists = aclBD.existsAcl(postAclDTO.getAcl().getRuolo(), postAclDTO.getAcl().getIdUtenza(bd), postAclDTO.getAcl().getServizio());
			leggiAclDTOResponse.setCreated(!exists);
			if(exists) {
				try {
					aclBD.updateAcl(postAclDTO.getAcl());
				} catch (NotFoundException e) {
					throw new ServiceException(e);
				}
			} else {
				aclBD.insertAcl(postAclDTO.getAcl());
			}

			leggiAclDTOResponse.setCreated(true);
			return leggiAclDTOResponse;
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	/**
	 * @param deleteAclDTO
	 * @throws NotAuthenticatedException 
	 */
	public void deleteAcl(DeleteAclDTO deleteAclDTO) throws NotAuthorizedException, AclNonTrovatoException, ServiceException, NotAuthenticatedException { 
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId(), useCacheData);
			this.autorizzaRichiesta(deleteAclDTO.getUser(), Servizio.ANAGRAFICA_RUOLI, Diritti.SCRITTURA,bd); 

			new AclBD(bd).deleteAcl(deleteAclDTO.getIdAcl());
		} catch (NotFoundException e) {
			throw new AclNonTrovatoException(e.getMessage());
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

}
