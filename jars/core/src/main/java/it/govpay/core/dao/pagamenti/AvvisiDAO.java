/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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
package it.govpay.core.dao.pagamenti;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Documento;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.DocumentiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.business.model.PrintAvvisoDocumentoDTO;
import it.govpay.core.business.model.PrintAvvisoVersamentoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO.FormatoAvviso;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetDocumentoAvvisiDTO;
import it.govpay.core.dao.anagrafica.dto.GetDocumentoAvvisiDTOResponse;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.exception.DocumentoNonTrovatoException;
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.IuvUtils;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class AvvisiDAO extends BaseDAO{

	public GetAvvisoDTOResponse getAvviso(GetAvvisoDTO getAvvisoDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException {
		BasicBD bd = null;
		Versamento versamento = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
			
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setCodDominio(getAvvisoDTO.getCodDominio());
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIuv(getAvvisoDTO.getIuv());

			if(getAvvisoDTO.getNumeroAvviso() != null)
				versamento = versamentiBD.getVersamentoByDominioIuv(AnagraficaManager.getDominio(bd, getAvvisoDTO.getCodDominio()).getId(), IuvUtils.toIuv(getAvvisoDTO.getNumeroAvviso()));
			else if(getAvvisoDTO.getIuv() != null)
				versamento = versamentiBD.getVersamentoByDominioIuv(AnagraficaManager.getDominio(bd, getAvvisoDTO.getCodDominio()).getId(), getAvvisoDTO.getIuv());
			else 
				throw new PendenzaNonTrovataException("Nessuna pendenza trovata");

			Dominio dominio = versamento.getDominio(versamentiBD);

			// controllo eventuali accessi anonimi al servizio di lettura avviso
			GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(getAvvisoDTO.getUser());
			if(details.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO)) {
				// utenza anonima non puo' scaricare i PDF.
				if(getAvvisoDTO.getFormato().equals(FormatoAvviso.PDF))
					throw AuthorizationManager.toNotAuthorizedException(getAvvisoDTO.getUser());
				
				this.checkCFDebitoreVersamento(getAvvisoDTO.getUser(), getAvvisoDTO.getCfDebitore(), versamento.getAnagraficaDebitore().getCodUnivoco());
			}

			GetAvvisoDTOResponse response = new GetAvvisoDTOResponse();
			String pdfFileName = versamento.getDominio(bd).getCodDominio() + "_" + versamento.getNumeroAvviso() + ".pdf";
			response.setFilenameAvviso(pdfFileName);
			switch(getAvvisoDTO.getFormato()) {
			case PDF:
				it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento(bd);
				PrintAvvisoVersamentoDTO printAvvisoDTO = new PrintAvvisoVersamentoDTO();
				printAvvisoDTO.setCodDominio(versamento.getDominio(bd).getCodDominio());
				printAvvisoDTO.setIuv(versamento.getIuvVersamento());
				printAvvisoDTO.setVersamento(versamento); 
				PrintAvvisoDTOResponse printAvvisoDTOResponse = avvisoBD.printAvvisoVersamento(printAvvisoDTO);
				response.setApplicazione(versamento.getApplicazione(versamentiBD));
				response.setVersamento(versamento);
				response.setAvvisoPdf(printAvvisoDTOResponse.getAvviso().getPdf());
				break;
			case JSON:
			default:
				it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamento, versamento.getApplicazione(bd), dominio);

				response.setApplicazione(versamento.getApplicazione(versamentiBD));
				response.setVersamento(versamento);
				response.setDominio(dominio);
				response.setBarCode(new String(iuvGenerato.getBarCode()));
				response.setQrCode(new String(iuvGenerato.getQrCode())); 
				break;

			}

			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new PendenzaNonTrovataException("Nessuna pendenza trovata");
		} catch (ValidationException e) {
			throw new PendenzaNonTrovataException("Nessuna pendenza trovata, sintassi del numero avviso non conforme alle specifiche.");
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
	
	public GetDocumentoAvvisiDTOResponse getDocumento(GetDocumentoAvvisiDTO getAvvisoDTO) throws ServiceException, DocumentoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException, ValidationException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
			
			DocumentiBD documentiBD = new DocumentiBD(bd);
			
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setCodDominio(getAvvisoDTO.getCodDominio());
			//((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIuv(getAvvisoDTO.getIuv());

			Dominio dominio = AnagraficaManager.getDominio(bd, getAvvisoDTO.getCodDominio());
			
			Documento documento =  documentiBD.getDocumentoByDominioIdentificativo(dominio.getId(), getAvvisoDTO.getNumeroDocumento());

			GetDocumentoAvvisiDTOResponse response = new GetDocumentoAvvisiDTOResponse();
			String pdfFileName = dominio.getCodDominio() + "_" + documento.getCodDocumento() + ".pdf";
			response.setFilenameDocumento(pdfFileName);
			switch(getAvvisoDTO.getFormato()) {
			case PDF:
				it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento(bd);
				PrintAvvisoDocumentoDTO printAvvisoDTO = new PrintAvvisoDocumentoDTO();
				printAvvisoDTO.setDocumento(documento);
				PrintAvvisoDTOResponse printAvvisoDTOResponse = avvisoBD.printAvvisoDocumento(printAvvisoDTO);
				response.setDocumento(documento);
				response.setDominio(dominio);
				response.setApplicazione(documento.getApplicazione(documentiBD));
				response.setDocumentoPdf(printAvvisoDTOResponse.getAvviso().getPdf());
				break;
			case JSON:
			default:
				throw new ValidationException("Il formato ["+getAvvisoDTO.getFormato()+"] richiesto per il documento non e' previsto.");
			}

			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new DocumentoNonTrovatoException("Nessun documento trovato");
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
	
	public GetAvvisoDTOResponse checkDisponibilitaAvviso(GetAvvisoDTO getAvvisoDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException, ValidationException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
			return this.checkDisponibilitaAvviso(getAvvisoDTO, bd);
		}finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public GetAvvisoDTOResponse checkDisponibilitaAvviso(GetAvvisoDTO getAvvisoDTO, BasicBD bd) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException, ValidationException {
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		
		if(ContextThreadLocal.get() != null) {
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setCodDominio(getAvvisoDTO.getCodDominio());
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIuv(getAvvisoDTO.getIuv());
		}
		
		VersamentoFilter filter = versamentiBD.newFilter();
		filter.setCodDominio(getAvvisoDTO.getCodDominio());
		filter.setIdSessione(getAvvisoDTO.getIdentificativoCreazionePendenza());
		if(getAvvisoDTO.getNumeroAvviso() != null)
			filter.setIuv(IuvUtils.toIuv(getAvvisoDTO.getNumeroAvviso()));
		else if(getAvvisoDTO.getIuv() != null)
			filter.setIuv(getAvvisoDTO.getIuv());
		else 
			throw new PendenzaNonTrovataException("Nessuna pendenza trovata");
		long count = versamentiBD.count(filter);
		GetAvvisoDTOResponse response = new GetAvvisoDTOResponse();
		response.setFound(count > 0);

		return response;
	}
}
