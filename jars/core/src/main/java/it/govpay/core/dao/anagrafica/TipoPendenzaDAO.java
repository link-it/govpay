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

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.IJsonSchemaValidator;
import org.openspcoop2.utils.json.JsonSchemaValidatorConfig;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.json.ValidatorFactory;
import org.openspcoop2.utils.json.JsonValidatorAPI.ApiName;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.TipiVersamentoBD;
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
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);
			TipiVersamentoBD intermediariBD = new TipiVersamentoBD(bd);
			TipoVersamentoFilter filter = intermediariBD.newFilter(false);
			filter.setCodTipoVersamento(putTipoPendenzaDTO.getCodTipoVersamento());
			
			if(putTipoPendenzaDTO.getTipoVersamento().getValidazioneDefinizioneDefault() != null) {
				// validazione schema di validazione
				IJsonSchemaValidator validator = null;
	
				try{
					validator = ValidatorFactory.newJsonSchemaValidator(ApiName.NETWORK_NT);
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					throw new ServiceException(e);
				}
				JsonSchemaValidatorConfig config = new JsonSchemaValidatorConfig();
	
				try {
					validator.setSchema(putTipoPendenzaDTO.getTipoVersamento().getValidazioneDefinizioneDefault().getBytes(), config, this.log);
				} catch (ValidationException e) {
					this.log.error("Validazione tramite JSON Schema completata con errore: " + e.getMessage(), e);
					throw new ValidationException("Lo schema indicato per la validazione non e' valido.", e);
				} 
			}

			// flag creazione o update
			boolean isCreate = intermediariBD.count(filter) == 0;
			intermediarioDTOResponse.setCreated(isCreate);
			if(isCreate) {
				intermediariBD.insertTipoVersamento(putTipoPendenzaDTO.getTipoVersamento());
			} else {
				intermediariBD.updateTipoVersamento(putTipoPendenzaDTO.getTipoVersamento());
			}
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new TipoVersamentoNonTrovatoException(e.getMessage());
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return intermediarioDTOResponse;
	}

	public FindTipiPendenzaDTOResponse findTipiPendenza(FindTipiPendenzaDTO findTipiPendenzaDTO) throws NotAuthorizedException, ServiceException, NotAuthenticatedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			TipiVersamentoBD stazioneBD = new TipiVersamentoBD(bd);
			TipoVersamentoFilter filter = null;
			if(findTipiPendenzaDTO.isSimpleSearch()) {
				filter = stazioneBD.newFilter(true);
				filter.setSimpleSearchString(findTipiPendenzaDTO.getSimpleSearch());
			} else {
				filter = stazioneBD.newFilter(false);
			}

			filter.setOffset(findTipiPendenzaDTO.getOffset());
			filter.setLimit(findTipiPendenzaDTO.getLimit());
			filter.getFilterSortList().addAll(findTipiPendenzaDTO.getFieldSortList());
			filter.setSearchAbilitato(findTipiPendenzaDTO.getAbilitato());
			if(findTipiPendenzaDTO.getTipo() != null)
				filter.setTipo(findTipiPendenzaDTO.getTipo().getCodifica());
			filter.setListaIdTipiVersamento(findTipiPendenzaDTO.getIdTipiVersamento());
			filter.setForm(findTipiPendenzaDTO.getForm());
			filter.setCodTipoVersamento(findTipiPendenzaDTO.getCodTipoVersamento());
			filter.setDescrizione(findTipiPendenzaDTO.getDescrizione());
			filter.setTrasformazione(findTipiPendenzaDTO.getTrasformazione());

			return new FindTipiPendenzaDTOResponse(stazioneBD.count(filter), stazioneBD.findAll(filter));
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public GetTipoPendenzaDTOResponse getTipoPendenza(GetTipoPendenzaDTO getTipoPendenzaDTO) throws NotAuthorizedException, TipoVersamentoNonTrovatoException, ServiceException, NotAuthenticatedException {
		GetTipoPendenzaDTOResponse response = null;
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);
			response = new GetTipoPendenzaDTOResponse(AnagraficaManager.getTipoVersamento(bd, getTipoPendenzaDTO.getCodTipoVersamento()));
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new TipoVersamentoNonTrovatoException("TipoPendenza " + getTipoPendenzaDTO.getCodTipoVersamento() + " non censita in Anagrafica");
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return response;
	}

}