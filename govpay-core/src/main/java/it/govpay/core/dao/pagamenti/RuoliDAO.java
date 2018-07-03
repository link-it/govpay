package it.govpay.core.dao.pagamenti;

import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.filters.AclFilter;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRuoloDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRuoloDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRuoliDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRuoliDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PutRuoloDTO;
import it.govpay.core.dao.pagamenti.dto.PutRuoloDTOResponse;
import it.govpay.core.dao.pagamenti.dto.RuoloPatchDTO;
import it.govpay.core.dao.pagamenti.exception.PagamentoPortaleNonTrovatoException;
import it.govpay.core.dao.pagamenti.exception.RicevutaNonTrovataException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
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
			this.autorizzaRichiesta(leggiRuoliDTO.getUser(), Servizio.ANAGRAFICA_RUOLI, Diritti.LETTURA, leggiRuoliDTO.getRuolo(), null, bd);

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
			this.autorizzaRichiesta(listaRuoliDTO.getUser(), Servizio.ANAGRAFICA_RUOLI, Diritti.LETTURA, bd);
	
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

	public PutRuoloDTOResponse createOrUpdate(PutRuoloDTO listaRuoliDTO) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(listaRuoliDTO.getUser(), Servizio.ANAGRAFICA_RUOLI, Diritti.SCRITTURA, bd);
	
			AclBD aclBD = new AclBD(bd);
	
			for(Acl acl: listaRuoliDTO.getAcls()) {
				acl.setRuolo(listaRuoliDTO.getIdRuolo());
				
				if(aclBD.existsAcl(acl.getRuolo(), null, acl.getServizio())) {
					aclBD.updateAcl(acl);
				} else {
					aclBD.insertAcl(acl);
				}
			}
			
			return new PutRuoloDTOResponse();

			
		} catch (NotFoundException e) {
			throw new ServiceException(e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}

	}
	
	public LeggiRuoloDTOResponse patch(RuoloPatchDTO patchDTO) throws ServiceException,PagamentoPortaleNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{
//		LeggiRuoloDTOResponse leggiPagamentoPortaleDTOResponse = new LeggiRuoloDTOResponse();
//		
//		BasicBD bd = null;
//
//		try {
//			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
//			this.autorizzaRichiesta(patchDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA,bd);
//
//			PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(bd);
//			PagamentoPortale pagamentoPortale = pagamentiPortaleBD.getPagamentoFromCodSessione(patchDTO.getIdSessione());
//
//			for(Versamento versamento: pagamentoPortale.getVersamenti(bd)) {
//				versamento.getDominio(bd);
//				versamento.getSingoliVersamenti(bd);
//			}
//			if(pagamentoPortale.getMultiBeneficiario() != null) {
//				// controllo che il dominio sia autorizzato
//				this.autorizzaRichiesta(patchDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, pagamentoPortale.getMultiBeneficiario(), null, bd);
//			}
//			leggiPagamentoPortaleDTOResponse.setPagamento(pagamentoPortale); 
//
//			PendenzeDAO pendenzeDao = new PendenzeDAO();
//			ListaPendenzeDTO listaPendenzaDTO = new ListaPendenzeDTO(patchDTO.getUser());
//			listaPendenzaDTO.setIdPagamento(patchDTO.getIdSessione());
//			ListaPendenzeDTOResponse listaPendenze = pendenzeDao.listaPendenze(listaPendenzaDTO, bd);
//			leggiPagamentoPortaleDTOResponse.setListaPendenze(listaPendenze.getResults());
//
//			RptDAO rptDao = new RptDAO(); 
//			ListaRptDTO listaRptDTO = new ListaRptDTO(patchDTO.getUser());
//			listaRptDTO.setIdPagamento(pagamentoPortale.getIdSessione());
//			ListaRptDTOResponse listaRpt = rptDao.listaRpt(listaRptDTO, bd);
//			leggiPagamentoPortaleDTOResponse.setListaRpp(listaRpt.getResults());
//
//			for(PatchOp op: patchDTO.getOp()) {
//				
//				if("note".equals(op.getPath())) {
//					switch(op.getOp()) {
//					case ADD: Nota nota = new Nota();
//						nota.setAutore(patchDTO.getUser().getPrincipal());
//						nota.setData(new Date());
//						nota.setTesto((String)op.getValue());
//						pagamentoPortale.getNote().add(nota);
//						break;
//					default: throw new ServiceException("Op '"+op.getOp()+"' non valida per il path '"+op.getPath()+"'");
//					}
//				} else if("verificato".equals(op.getPath())) {
//					switch(op.getOp()) {
//					case REPLACE: 
//						pagamentoPortale.setAck((Boolean)op.getValue()); 
//						break;
//					default: throw new ServiceException("Op '"+op.getOp()+"' non valida per il path '"+op.getPath()+"'");
//					}
//					
//				} else {
//					throw new ServiceException("Path '"+op.getPath()+"' non valido");
//				}
//			}
//			
//			pagamentiPortaleBD.updatePagamento(pagamentoPortale, false);
//			
//			
//			
//			return leggiPagamentoPortaleDTOResponse;
//		}catch(NotFoundException e) {
//			throw new PagamentoPortaleNonTrovatoException("Non esiste un pagamento associato all'ID ["+patchDTO.getIdSessione()+"]");
//		}finally {
//			if(bd != null)
//				bd.closeConnection();
//		}
		
		return null; //TODO ruoli
	}

}
