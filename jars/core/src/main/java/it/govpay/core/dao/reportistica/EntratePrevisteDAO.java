/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.core.dao.reportistica;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.viste.EntratePrevisteBD;
import it.govpay.bd.viste.filters.EntrataPrevistaFilter;
import it.govpay.bd.viste.model.EntrataPrevista;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.business.reportistica.EntratePreviste;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.reportistica.dto.ListaEntratePrevisteDTO;
import it.govpay.core.dao.reportistica.dto.ListaEntratePrevisteDTO.FormatoRichiesto;
import it.govpay.core.dao.reportistica.dto.ListaEntratePrevisteDTOResponse;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.RequestParamException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.orm.VistaRiscossioni;

public class EntratePrevisteDAO extends BaseDAO{

	public ListaEntratePrevisteDTOResponse listaEntrate(ListaEntratePrevisteDTO listaEntratePrevisteDTO) 
			throws NotAuthenticatedException, NotAuthorizedException, RequestParamException, ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		EntratePrevisteBD entrateBD = null;

		try {
			// Autorizzazione sui domini
			List<String> codDomini = AuthorizationManager.getDominiAutorizzati(listaEntratePrevisteDTO.getUser());
			if(codDomini == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(listaEntratePrevisteDTO.getUser());
			}

			entrateBD = new EntratePrevisteBD(configWrapper);
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
					throw new RequestParamException("Il numero di risultati trovati supera il limite massimo serializzabile su file pdf, affinare la ricerca per ridurre il numero di risultati.");
				
				
				filter.setOffset(0);
				filter.setLimit(numeroMassimoEntriesProspettoRiscossione);
				
				List<FilterSortWrapper> listaOrdinamenti = new ArrayList<>();
				if(listaEntratePrevisteDTO.getIdDominio() == null)
					listaOrdinamenti.add(new FilterSortWrapper(VistaRiscossioni.model().COD_DOMINIO, SortOrder.ASC));
				listaOrdinamenti.add(new FilterSortWrapper(VistaRiscossioni.model().COD_FLUSSO, SortOrder.ASC));
				listaOrdinamenti.add(new FilterSortWrapper(VistaRiscossioni.model().IUV, SortOrder.ASC));
				filter.setFilterSortList(listaOrdinamenti );
				
				resList = entrateBD.findAll(filter);
				
				EntratePreviste entratePrevisteBD = new EntratePreviste();
				byte[] pdfEntratePreviste = entratePrevisteBD.getReportPdfEntratePreviste(resList, listaEntratePrevisteDTO.getDataDa(), listaEntratePrevisteDTO.getDataA());
								
				listaEntratePrevisteDTOResponse = new ListaEntratePrevisteDTOResponse(count, resList);
				listaEntratePrevisteDTOResponse.setPdf(pdfEntratePreviste); 
			}
			
			 return listaEntratePrevisteDTOResponse;
			
		}finally {
			if(entrateBD != null)
				entrateBD.closeConnection();
		}
	}
}
