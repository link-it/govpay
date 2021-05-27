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
package it.govpay.core.dao.anagrafica;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.IJsonSchemaValidator;
import org.openspcoop2.utils.json.JsonSchemaValidatorConfig;
import org.openspcoop2.utils.json.JsonValidatorAPI.ApiName;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.json.ValidatorFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.TipiVersamentoBD;
import it.govpay.bd.anagrafica.TipiVersamentoDominiBD;
import it.govpay.bd.anagrafica.filters.TipoVersamentoFilter;
import it.govpay.core.dao.anagrafica.dto.FindTipiPendenzaDTO;
import it.govpay.core.dao.anagrafica.dto.FindTipiPendenzaDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetTipoPendenzaDTO;
import it.govpay.core.dao.anagrafica.dto.GetTipoPendenzaDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutTipoPendenzaDTO;
import it.govpay.core.dao.anagrafica.dto.PutTipoPendenzaDTOResponse;
import it.govpay.core.dao.anagrafica.exception.TipoVersamentoNonTrovatoException;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.model.TipoVersamento;

public class TipoPendenzaDAO extends BaseDAO{
	
	public TipoPendenzaDAO() {
		super();
	}

	public TipoPendenzaDAO(boolean useCacheData) {
		super(useCacheData);
	}

	public PutTipoPendenzaDTOResponse createOrUpdateTipoPendenza(PutTipoPendenzaDTO putTipoPendenzaDTO) throws ServiceException,
		TipoVersamentoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException, ValidationException{
		PutTipoPendenzaDTOResponse intermediarioDTOResponse = new PutTipoPendenzaDTOResponse();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		TipiVersamentoBD tipiVerwsamentoBD = null;

		try {
			tipiVerwsamentoBD = new TipiVersamentoBD(configWrapper);
			TipoVersamentoFilter filter = tipiVerwsamentoBD.newFilter(false);
			filter.setCodTipoVersamento(putTipoPendenzaDTO.getCodTipoVersamento());
			filter.setSearchModeEquals(true); 
			
			if(putTipoPendenzaDTO.getTipoVersamento().getCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault() != null) {
				// validazione schema di validazione
				IJsonSchemaValidator validator = null;
	
				try{
					validator = ValidatorFactory.newJsonSchemaValidator(ApiName.NETWORK_NT);
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					throw new ServiceException(e);
				}
				JsonSchemaValidatorConfig config = new JsonSchemaValidatorConfig();
	
				try {
					validator.setSchema(putTipoPendenzaDTO.getTipoVersamento().getCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault().getBytes(), config, this.log);
				} catch (ValidationException e) {
					this.log.error("Validazione tramite JSON Schema completata con errore: " + e.getMessage(), e);
					throw new ValidationException("Lo schema indicato per la validazione della pendenza portali backoffice non e' valido.", e);
				} 
			}
			
			if(putTipoPendenzaDTO.getTipoVersamento().getCaricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault() != null) {
				// validazione schema di validazione
				IJsonSchemaValidator validator = null;
	
				try{
					validator = ValidatorFactory.newJsonSchemaValidator(ApiName.NETWORK_NT);
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					throw new ServiceException(e);
				}
				JsonSchemaValidatorConfig config = new JsonSchemaValidatorConfig();
	
				try {
					validator.setSchema(putTipoPendenzaDTO.getTipoVersamento().getCaricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault().getBytes(), config, this.log);
				} catch (ValidationException e) {
					this.log.error("Validazione tramite JSON Schema completata con errore: " + e.getMessage(), e);
					throw new ValidationException("Lo schema indicato per la validazione della pendenza portali pagamento non e' valido.", e);
				} 
			}

			// flag creazione o update
			boolean isCreate = tipiVerwsamentoBD.count(filter) == 0;
			intermediarioDTOResponse.setCreated(isCreate);
			if(isCreate) {
				// possono creare i tipi pendenza solo gli utenti che hanno autorizzazione su tutti i tipi pendenza (lista idtipoversamento non null e vuota)
				if(putTipoPendenzaDTO.getIdTipiVersamento() == null || putTipoPendenzaDTO.getIdTipiVersamento().size() > 0) {
					throw new NotAuthorizedException("L'utenza non possiede i diritti per creare nuovi Tipi Pendenza");
				}
				
				tipiVerwsamentoBD.insertTipoVersamento(putTipoPendenzaDTO.getTipoVersamento());
			} else {
				// possono modificare i tipi pendenza solo gli utenti che hanno autorizzazione su tutti i tipi pendenza o sullo specifico tipo
				if(putTipoPendenzaDTO.getCodTipiVersamento() == null) {
					throw new NotAuthorizedException("L'utenza non possiede i diritti per modificare il Tipo Pendenza");
				}
				
				if(putTipoPendenzaDTO.getCodTipiVersamento().size() > 0) {
					if(!putTipoPendenzaDTO.getCodTipiVersamento().contains(putTipoPendenzaDTO.getCodTipoVersamento())) {
						throw new NotAuthorizedException("L'utenza non possiede i diritti per modificare il Tipo Pendenza");
					}
				}
				
				tipiVerwsamentoBD.updateTipoVersamento(putTipoPendenzaDTO.getTipoVersamento());
			}
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new TipoVersamentoNonTrovatoException(e.getMessage());
		} finally {
			if(tipiVerwsamentoBD != null)
				tipiVerwsamentoBD.closeConnection();
		}
		return intermediarioDTOResponse;
	}

	public FindTipiPendenzaDTOResponse findTipiPendenza(FindTipiPendenzaDTO findTipiPendenzaDTO) throws NotAuthorizedException, ServiceException, NotAuthenticatedException, UnprocessableEntityException {
		TipiVersamentoBD tipiVersamentoBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			tipiVersamentoBD = new TipiVersamentoBD(configWrapper);
			TipoVersamentoFilter filter = null;
			if(findTipiPendenzaDTO.isSimpleSearch()) {
				filter = tipiVersamentoBD.newFilter(true);
				filter.setSimpleSearchString(findTipiPendenzaDTO.getSimpleSearch());
			} else {
				filter = tipiVersamentoBD.newFilter(false);
			}

			filter.setOffset(findTipiPendenzaDTO.getOffset());
			filter.setLimit(findTipiPendenzaDTO.getLimit());
			filter.getFilterSortList().addAll(findTipiPendenzaDTO.getFieldSortList());
			filter.setSearchAbilitato(findTipiPendenzaDTO.getAbilitato());
			filter.setListaIdTipiVersamento(findTipiPendenzaDTO.getIdTipiVersamento());
			filter.setFormBackoffice(findTipiPendenzaDTO.getFormBackoffice());
			filter.setFormPortalePagamento(findTipiPendenzaDTO.getFormPortalePagamento());
			filter.setCodTipoVersamento(findTipiPendenzaDTO.getCodTipoVersamento());
			filter.setDescrizione(findTipiPendenzaDTO.getDescrizione());
			filter.setTrasformazione(findTipiPendenzaDTO.getTrasformazione());
			filter.setEseguiCountConLimit(findTipiPendenzaDTO.isEseguiCountConLimit());
			
			if(findTipiPendenzaDTO.getNonAssociati() != null) {
				Long idDominio = null;
				try {
					idDominio = AnagraficaManager.getDominio(configWrapper, findTipiPendenzaDTO.getNonAssociati()).getId();
				} catch(NotFoundException e) {
					throw new UnprocessableEntityException("Impossibile ricercare i TipiPendenza non associati al Dominio ["+findTipiPendenzaDTO.getNonAssociati() +"]: non e' censito nel sistema.");	
				}
				
				TipiVersamentoDominiBD tipiVersamentoDominiBD = new TipiVersamentoDominiBD(configWrapper);
				List<Long> idTipiTributiDefinitiPerDominio = tipiVersamentoDominiBD.getIdTipiVersamentoDefinitiPerDominio(idDominio);
				filter.setListaIdTipiVersamentoDaEscludere(idTipiTributiDefinitiPerDominio);
			}
			
			if(findTipiPendenzaDTO.getCodDominio() != null) {
				Long idDominio = null;
				try {
					idDominio = AnagraficaManager.getDominio(configWrapper, findTipiPendenzaDTO.getCodDominio()).getId();
				} catch(NotFoundException e) {
					throw new UnprocessableEntityException("Impossibile ricercare i TipiPendenza associati al Dominio ["+findTipiPendenzaDTO.getCodDominio() +"]: non e' censito nel sistema.");	
				}
				
				TipiVersamentoDominiBD tipiVersamentoDominiBD = new TipiVersamentoDominiBD(configWrapper);
				List<Long> idTipiTributiDefinitiPerDominio = tipiVersamentoDominiBD.getIdTipiVersamentoDefinitiPerDominio(idDominio);
				filter.setListaIdTipiVersamentoDaIncludere(idTipiTributiDefinitiPerDominio);
			}
			
			Long count = null;
			
			if(findTipiPendenzaDTO.isEseguiCount()) {
				 count = tipiVersamentoBD.count(filter);
			}
			
			List<TipoVersamento> findAll = new ArrayList<>();
			if(findTipiPendenzaDTO.isEseguiFindAll()) {
				findAll = tipiVersamentoBD.findAll(filter);
				
				if(findTipiPendenzaDTO.getLimit() == null && !findTipiPendenzaDTO.isEseguiCount()) {
					count = (long) findAll.size();
				}
			}

			
			return new FindTipiPendenzaDTOResponse(count, findAll);
		} finally {
			if(tipiVersamentoBD != null)
				tipiVersamentoBD.closeConnection();
		}
	}

	public GetTipoPendenzaDTOResponse getTipoPendenza(GetTipoPendenzaDTO getTipoPendenzaDTO) throws NotAuthorizedException, TipoVersamentoNonTrovatoException, ServiceException, NotAuthenticatedException {
		GetTipoPendenzaDTOResponse response = null;
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
			response = new GetTipoPendenzaDTOResponse(AnagraficaManager.getTipoVersamento(configWrapper, getTipoPendenzaDTO.getCodTipoVersamento()));
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new TipoVersamentoNonTrovatoException("TipoPendenza " + getTipoPendenzaDTO.getCodTipoVersamento() + " non censita in Anagrafica");
		} 
		return response;
	}

}