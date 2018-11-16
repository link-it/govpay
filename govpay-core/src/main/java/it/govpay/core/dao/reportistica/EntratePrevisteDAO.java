package it.govpay.core.dao.reportistica;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.viste.EntratePrevisteBD;
import it.govpay.bd.viste.filters.EntrataPrevistaFilter;
import it.govpay.bd.viste.model.EntrataPrevista;
import it.govpay.core.business.reportistica.EntratePreviste;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.reportistica.dto.ListaEntratePrevisteDTO;
import it.govpay.core.dao.reportistica.dto.ListaEntratePrevisteDTO.FormatoRichiesto;
import it.govpay.core.dao.reportistica.dto.ListaEntratePrevisteDTOResponse;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.orm.VistaRiscossioni;

public class EntratePrevisteDAO extends BaseDAO{

	public ListaEntratePrevisteDTOResponse listaEntrate(ListaEntratePrevisteDTO listaEntratePrevisteDTO) 
			throws NotAuthenticatedException, NotAuthorizedException, UnprocessableEntityException, ServiceException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(listaEntratePrevisteDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);
			// Autorizzazione sui domini
			List<String> codDomini = AclEngine.getDominiAutorizzati((Utenza) listaEntratePrevisteDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA);
			if(codDomini == null) {
				throw new NotAuthorizedException("L'utenza autenticata ["+listaEntratePrevisteDTO.getUser().getPrincipal()+"] non e' autorizzata ai servizi " + Servizio.PAGAMENTI_E_PENDENZE + " per alcun dominio");
			}

			EntratePrevisteBD entrateBD = new EntratePrevisteBD(bd);
			EntrataPrevistaFilter filter = entrateBD.newFilter();
			
			if(codDomini != null && codDomini.size() > 0)
				filter.setCodDomini(codDomini);

			filter.setDataInizio(listaEntratePrevisteDTO.getDataDa());
			filter.setDataFine(listaEntratePrevisteDTO.getDataA());
			filter.setCodDominio(listaEntratePrevisteDTO.getIdDominio());
			filter.setCodApplicazione(listaEntratePrevisteDTO.getIdA2A());
			filter.setFilterSortList(listaEntratePrevisteDTO.getFieldSortList());

			long count = entrateBD.count(filter);
			List<EntrataPrevista> resList = new ArrayList<>();
			
			ListaEntratePrevisteDTOResponse listaEntratePrevisteDTOResponse = null;
			if(listaEntratePrevisteDTO.getFormato().equals(FormatoRichiesto.JSON)) {
				filter.setOffset(listaEntratePrevisteDTO.getOffset());
				filter.setLimit(listaEntratePrevisteDTO.getLimit());
				
				if(count > 0) {
					resList = entrateBD.findAll(filter);
				} 
				
				listaEntratePrevisteDTOResponse = new ListaEntratePrevisteDTOResponse(count, resList);
			} else {
				// PDF!
				int numeroMassimoEntriesProspettoRiscossione = GovpayConfig.getInstance().getNumeroMassimoEntriesProspettoRiscossione();
				if(count > numeroMassimoEntriesProspettoRiscossione)
					throw new UnprocessableEntityException("Il numero di risultati trovati supera il limite massimo serializzabile su file pdf, affinare la ricerca per ridurre il numero di risultati.");
				
				
				filter.setOffset(0);
				filter.setLimit(numeroMassimoEntriesProspettoRiscossione);
				
				List<FilterSortWrapper> listaOrdinamenti = new ArrayList<>();
				if(listaEntratePrevisteDTO.getIdDominio() == null)
					listaOrdinamenti.add(new FilterSortWrapper(VistaRiscossioni.model().COD_DOMINIO, SortOrder.ASC));
				listaOrdinamenti.add(new FilterSortWrapper(VistaRiscossioni.model().COD_FLUSSO, SortOrder.ASC));
				listaOrdinamenti.add(new FilterSortWrapper(VistaRiscossioni.model().IUV, SortOrder.ASC));
				filter.setFilterSortList(listaOrdinamenti );
				
				resList = entrateBD.findAll(filter);
				
				EntratePreviste entratePrevisteBD = new EntratePreviste(bd);
				byte[] pdfEntratePreviste = entratePrevisteBD.getReportPdfEntratePreviste(resList, listaEntratePrevisteDTO.getDataDa(), listaEntratePrevisteDTO.getDataA());
								
				listaEntratePrevisteDTOResponse = new ListaEntratePrevisteDTOResponse(count, resList);
				listaEntratePrevisteDTOResponse.setPdf(pdfEntratePreviste); 
			}
			
			 return listaEntratePrevisteDTOResponse;
			
		}finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
}
