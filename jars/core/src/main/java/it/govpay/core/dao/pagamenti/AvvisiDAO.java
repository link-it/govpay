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

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Documento;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.DocumentiBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.business.model.PrintAvvisoDocumentoDTO;
import it.govpay.core.business.model.PrintAvvisoVersamentoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetDocumentoAvvisiDTO;
import it.govpay.core.dao.anagrafica.dto.GetDocumentoAvvisiDTOResponse;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.exception.DocumentoNonTrovatoException;
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.IuvUtils;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class AvvisiDAO extends BaseDAO{

	public GetAvvisoDTOResponse getAvviso(GetAvvisoDTO getAvvisoDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException {
		VersamentiBD versamentiBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			versamentiBD = new VersamentiBD(configWrapper);
			return this.getAvviso(getAvvisoDTO, versamentiBD, configWrapper);
		} finally {
			if(versamentiBD != null)
				versamentiBD.closeConnection();
		}
	}
	
	public GetAvvisoDTOResponse getAvviso(GetAvvisoDTO getAvvisoDTO, VersamentiBD versamentiBD, BDConfigWrapper configWrapper) throws ServiceException, PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException {
		Versamento versamento = null;
		try {
			if(ContextThreadLocal.get() != null) {
				((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setCodDominio(getAvvisoDTO.getCodDominio());
				((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIuv(getAvvisoDTO.getIuv());
			}
			
			if(getAvvisoDTO.getNumeroAvviso() != null)
				versamento = versamentiBD.getVersamentoByDominioIuv(AnagraficaManager.getDominio(configWrapper, getAvvisoDTO.getCodDominio()).getId(), IuvUtils.toIuv(getAvvisoDTO.getNumeroAvviso()));
			else if(getAvvisoDTO.getIuv() != null)
				versamento = versamentiBD.getVersamentoByDominioIuv(AnagraficaManager.getDominio(configWrapper, getAvvisoDTO.getCodDominio()).getId(), getAvvisoDTO.getIuv());
			else 
				throw new PendenzaNonTrovataException("Nessuna pendenza trovata");

			Dominio dominio = versamento.getDominio(configWrapper);

			// controllo eventuali accessi anonimi al servizio di lettura avviso
			GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(getAvvisoDTO.getUser());
			if(details.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO)) {
				// utenza anonima non puo' scaricare i PDF.
//				if(getAvvisoDTO.getFormato().equals(FormatoAvviso.PDF))
//					throw AuthorizationManager.toNotAuthorizedException(getAvvisoDTO.getUser());
				
				this.checkCFDebitoreVersamento(getAvvisoDTO.getUser(), getAvvisoDTO.getCfDebitore(), versamento.getAnagraficaDebitore().getCodUnivoco());
			}

			GetAvvisoDTOResponse response = new GetAvvisoDTOResponse();
			
			String pdfFileName = null;
			if(versamento.getDocumento(configWrapper) != null) {
				pdfFileName = versamento.getDominio(configWrapper).getCodDominio() + "_DOC_" + versamento.getDocumento(configWrapper).getCodDocumento() + ".pdf";
			} else {
				pdfFileName = versamento.getDominio(configWrapper).getCodDominio() + "_" + versamento.getNumeroAvviso() + ".pdf";	
			}
			
			response.setFilenameAvviso(pdfFileName);
			switch(getAvvisoDTO.getFormato()) {
			case PDF:
				it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento();
				PrintAvvisoVersamentoDTO printAvvisoDTO = new PrintAvvisoVersamentoDTO();
				printAvvisoDTO.setCodDominio(versamento.getDominio(configWrapper).getCodDominio());
				printAvvisoDTO.setIuv(versamento.getIuvVersamento());
				printAvvisoDTO.setVersamento(versamento); 
				printAvvisoDTO.setSalvaSuDB(false);
				printAvvisoDTO.setLinguaSecondaria(getAvvisoDTO.getLinguaSecondaria());
				PrintAvvisoDTOResponse printAvvisoDTOResponse = avvisoBD.printAvvisoVersamento(printAvvisoDTO);
				response.setApplicazione(versamento.getApplicazione(configWrapper));
				response.setVersamento(versamento);
				response.setAvvisoPdf(printAvvisoDTOResponse.getAvviso().getPdf());
				break;
			case JSON:
			default:
				it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamento, versamento.getApplicazione(configWrapper), dominio);

				response.setApplicazione(versamento.getApplicazione(configWrapper));
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
		}  
	}
	
	public GetDocumentoAvvisiDTOResponse getDocumento(GetDocumentoAvvisiDTO getAvvisoDTO) throws ServiceException, DocumentoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException, ValidationException, UnprocessableEntityException {
		DocumentiBD documentiBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			documentiBD = new DocumentiBD(configWrapper);
			
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setCodDominio(getAvvisoDTO.getCodDominio());

			Dominio dominio = AnagraficaManager.getDominio(configWrapper, getAvvisoDTO.getCodDominio());
			Applicazione applicazione = AnagraficaManager.getApplicazione(configWrapper, getAvvisoDTO.getCodApplicazione());
			
			Documento documento =  documentiBD.getDocumentoByApplicazioneDominioIdentificativo(applicazione.getId(), dominio.getId(), getAvvisoDTO.getNumeroDocumento());

			GetDocumentoAvvisiDTOResponse response = new GetDocumentoAvvisiDTOResponse();
			String pdfFileName = dominio.getCodDominio() + "_" + documento.getCodDocumento() + ".pdf";
			response.setFilenameDocumento(pdfFileName);
			switch(getAvvisoDTO.getFormato()) {
			case PDF:
				it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento();
				PrintAvvisoDocumentoDTO printAvvisoDTO = new PrintAvvisoDocumentoDTO();
				printAvvisoDTO.setDocumento(documento);
				printAvvisoDTO.setSalvaSuDB(false);
				printAvvisoDTO.setLinguaSecondaria(getAvvisoDTO.getLinguaSecondaria()); 
				printAvvisoDTO.setNumeriAvviso(getAvvisoDTO.getNumeriAvviso());
				PrintAvvisoDTOResponse printAvvisoDTOResponse = avvisoBD.printAvvisoDocumento(printAvvisoDTO);
				response.setDocumento(documento);
				response.setDominio(dominio);
				response.setApplicazione(applicazione);
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
			if(documentiBD != null)
				documentiBD.closeConnection();
		}
	}
	
	public GetAvvisoDTOResponse checkDisponibilitaAvviso(GetAvvisoDTO getAvvisoDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException, ValidationException {
		VersamentiBD versamentiBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			versamentiBD = new VersamentiBD(configWrapper);
			return this.checkDisponibilitaAvviso(getAvvisoDTO, versamentiBD);
		}finally {
			if(versamentiBD != null)
				versamentiBD.closeConnection();
		}
	}

	public GetAvvisoDTOResponse checkDisponibilitaAvviso(GetAvvisoDTO getAvvisoDTO, VersamentiBD versamentiBD) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException, ValidationException {
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
