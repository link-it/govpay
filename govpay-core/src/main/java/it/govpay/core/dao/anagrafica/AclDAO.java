package it.govpay.core.dao.anagrafica;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.filters.AclFilter;
import it.govpay.core.dao.anagrafica.dto.LeggiAclDTO;
import it.govpay.core.dao.anagrafica.dto.LeggiAclDTOResponse;
import it.govpay.core.dao.anagrafica.dto.ListaAclDTO;
import it.govpay.core.dao.anagrafica.dto.ListaAclDTOResponse;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpThreadLocal;

public class AclDAO {

	public ListaAclDTOResponse leggiAclRuoloRegistrateSistema() throws ServiceException {
		ListaAclDTO listaAclDTO = new ListaAclDTO(null);
		listaAclDTO.setForceRuolo(true);
		return this.listaAcl(listaAclDTO);
	}
	
	public ListaAclDTOResponse listaAcl(ListaAclDTO listaAclDTO) throws ServiceException {
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		try {
			
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
			
			return new ListaAclDTOResponse(aclBD.count(filter), aclBD.findAll(filter));
			
		} finally {
			bd.closeConnection();
		}
	}
	
	public ListaAclDTOResponse leggiAclPrincipalRegistrateSistema() throws ServiceException {
		ListaAclDTO listaAclDTO = new ListaAclDTO(null);
		listaAclDTO.setForcePrincipal(true);
		return this.listaAcl(listaAclDTO);
	}
	
	public LeggiAclDTOResponse leggiAcl(LeggiAclDTO leggiAclDTO) throws NotAuthorizedException, NotFoundException, ServiceException {
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		
//		Set<String> applicazioni = AclEngine.getApplicazioniAutorizzati(getApplicazioneDTO.getUser(), Servizio.Anagrafica_PagoPa);
//		
//		if(applicazioni != null && !applicazioni.contains(getApplicazioneDTO.getCodApplicazione())) {
//			throw new NotAuthorizedException("L'utente autenticato non e' autorizzato in lettura ai servizi " + Servizio.Anagrafica_PagoPa + " per l'applicazione " + getApplicazioneDTO.getCodApplicazione());
//		}

		try {
			AclBD aclBD = new AclBD(bd);
			LeggiAclDTOResponse leggiAclDTOResponse = new LeggiAclDTOResponse();
			leggiAclDTOResponse.setAcl(aclBD.getAcl(leggiAclDTO.getIdAcl()));
			return leggiAclDTOResponse;
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} finally {
			bd.closeConnection();
		}
	}
	
}
