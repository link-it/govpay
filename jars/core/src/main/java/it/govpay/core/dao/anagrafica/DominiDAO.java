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
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.IbanAccreditoBD;
import it.govpay.bd.anagrafica.TipiVersamentoDominiBD;
import it.govpay.bd.anagrafica.TributiBD;
import it.govpay.bd.anagrafica.UnitaOperativeBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.anagrafica.filters.IbanAccreditoFilter;
import it.govpay.bd.anagrafica.filters.TipoVersamentoDominioFilter;
import it.govpay.bd.anagrafica.filters.TributoFilter;
import it.govpay.bd.anagrafica.filters.UnitaOperativaFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.core.dao.anagrafica.dto.FindDominiDTO;
import it.govpay.core.dao.anagrafica.dto.FindDominiDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindIbanDTO;
import it.govpay.core.dao.anagrafica.dto.FindIbanDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindTipiPendenzaDominioDTO;
import it.govpay.core.dao.anagrafica.dto.FindTipiPendenzaDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindTributiDTO;
import it.govpay.core.dao.anagrafica.dto.FindTributiDTOResponse;
import it.govpay.core.dao.anagrafica.dto.FindUnitaOperativeDTO;
import it.govpay.core.dao.anagrafica.dto.FindUnitaOperativeDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetDominioDTO;
import it.govpay.core.dao.anagrafica.dto.GetDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetIbanDTO;
import it.govpay.core.dao.anagrafica.dto.GetIbanDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetTipoPendenzaDominioDTO;
import it.govpay.core.dao.anagrafica.dto.GetTipoPendenzaDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetTributoDTO;
import it.govpay.core.dao.anagrafica.dto.GetTributoDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetUnitaOperativaDTO;
import it.govpay.core.dao.anagrafica.dto.GetUnitaOperativaDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutDominioDTO;
import it.govpay.core.dao.anagrafica.dto.PutDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutEntrataDominioDTO;
import it.govpay.core.dao.anagrafica.dto.PutEntrataDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutIbanAccreditoDTO;
import it.govpay.core.dao.anagrafica.dto.PutIbanAccreditoDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutTipoPendenzaDominioDTO;
import it.govpay.core.dao.anagrafica.dto.PutTipoPendenzaDominioDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutUnitaOperativaDTO;
import it.govpay.core.dao.anagrafica.dto.PutUnitaOperativaDTOResponse;
import it.govpay.core.dao.anagrafica.exception.DominioNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.IbanAccreditoNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.StazioneNonTrovataException;
import it.govpay.core.dao.anagrafica.exception.TipoTributoNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.TipoVersamentoNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.TributoNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.UnitaOperativaNonTrovataException;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.RequestValidationException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.model.TipoTributo;
import it.govpay.model.TipoVersamento;
import it.govpay.model.Tributo;

public class DominiDAO extends BaseDAO{

	public DominiDAO() {
		super();
	}

	public DominiDAO(boolean useCacheData) {
		super(useCacheData);
	}

	public PutDominioDTOResponse createOrUpdate(PutDominioDTO putDominioDTO) throws ServiceException,
	DominioNonTrovatoException,StazioneNonTrovataException,TipoTributoNonTrovatoException, TipoVersamentoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException, UnprocessableEntityException{
		PutDominioDTOResponse dominioDTOResponse = new PutDominioDTOResponse(); 
		DominiBD dominiBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			dominiBD = new DominiBD(configWrapper);
			
			dominiBD.setupConnection(configWrapper.getTransactionID());
			
			dominiBD.setAtomica(false); // gestione esplicita della connessione
			
			// stazione
			try {
				putDominioDTO.getDominio().setIdStazione(AnagraficaManager.getStazione(configWrapper, putDominioDTO.getCodStazione()).getId());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new UnprocessableEntityException("La stazione intermediaria "+putDominioDTO.getCodStazione()+" indicata non esiste.");
			} 

			TipiVersamentoDominiBD tvdBD = new TipiVersamentoDominiBD(dominiBD);
			tvdBD.setAtomica(false); 
			UnitaOperativeBD uoBd = new UnitaOperativeBD(dominiBD);
			uoBd.setAtomica(false); 
			DominioFilter filter = dominiBD.newFilter(false);
			filter.setCodDominio(putDominioDTO.getIdDominio());
			filter.setSearchModeEquals(true);

			// flag creazione o update
			boolean isCreate = dominiBD.count(filter) == 0;
			dominioDTOResponse.setCreated(isCreate);
			
			if(isCreate) {
				TipoTributo bolloT = null;
				// bollo telematico
				try {
					bolloT = AnagraficaManager.getTipoTributo(configWrapper, it.govpay.model.Tributo.BOLLOT);
				} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
					throw new TipoTributoNonTrovatoException(e.getMessage());
				}

				TributiBD tributiBD = new TributiBD(dominiBD);
				tributiBD.setAtomica(false); 

				Tributo tributo = new Tributo();
				tributo.setCodTributo(it.govpay.model.Tributo.BOLLOT);
				tributo.setAbilitato(false);
				tributo.setDescrizione(bolloT.getDescrizione());
				tributo.setIdTipoTributo(bolloT.getId());

				TipoVersamento libero = null;

				try {
					libero = AnagraficaManager.getTipoVersamento(configWrapper, GovpayConfig.getInstance().getCodTipoVersamentoPendenzeLibere());
				} catch(org.openspcoop2.generic_project.exception.NotFoundException e) {
					throw new TipoVersamentoNonTrovatoException(e.getMessage());
				}  
				TipoVersamentoDominio tvd = new TipoVersamentoDominio();
				tvd.setCodTipoVersamento(libero.getCodTipoVersamento());
				tvd.setIdTipoVersamento(libero.getId());
				tvd.setDescrizione(libero.getDescrizione());

				TipoVersamentoDominio tvdNonCensite = null;
				if(!GovpayConfig.getInstance().getCodTipoVersamentoPendenzeLibere().equals(GovpayConfig.getInstance().getCodTipoVersamentoPendenzeNonCensite())) {
					TipoVersamento nonCensite = null;

					try {
						nonCensite = AnagraficaManager.getTipoVersamento(configWrapper, GovpayConfig.getInstance().getCodTipoVersamentoPendenzeNonCensite());
					} catch(org.openspcoop2.generic_project.exception.NotFoundException e) {
						throw new TipoVersamentoNonTrovatoException(e.getMessage());
					}  
					tvdNonCensite = new TipoVersamentoDominio();
					tvdNonCensite.setCodTipoVersamento(nonCensite.getCodTipoVersamento());
					tvdNonCensite.setIdTipoVersamento(nonCensite.getId());
					tvdNonCensite.setDescrizione(nonCensite.getDescrizione());
				}

				TipoVersamento tipoVersamentoBolloT = null;

				try {
					tipoVersamentoBolloT = AnagraficaManager.getTipoVersamento(configWrapper, it.govpay.model.Tributo.BOLLOT);
				} catch(org.openspcoop2.generic_project.exception.NotFoundException e) {
					throw new TipoVersamentoNonTrovatoException(e.getMessage());
				}  
				TipoVersamentoDominio tvdBollo = new TipoVersamentoDominio();
				tvdBollo.setCodTipoVersamento(tipoVersamentoBolloT.getCodTipoVersamento());
				tvdBollo.setIdTipoVersamento(tipoVersamentoBolloT.getId());
				tvdBollo.setDescrizione(tipoVersamentoBolloT.getDescrizione());


				UnitaOperativa uo = new UnitaOperativa();
				uo.setAbilitato(true);
				uo.setAnagrafica(putDominioDTO.getDominio().getAnagrafica());
				uo.setCodUo(it.govpay.model.Dominio.EC);
				putDominioDTO.getDominio().getAnagrafica().setCodUnivoco(uo.getCodUo());
				try {
					dominiBD.setAutoCommit(false);
					dominiBD.insertDominio(putDominioDTO.getDominio());

					// UO EC
					uo.setIdDominio(putDominioDTO.getDominio().getId());
					uoBd.insertUnitaOperativa(uo);

					// MBT
					tributo.setIdDominio(putDominioDTO.getDominio().getId());
					tributiBD.insertTributo(tributo);

					// LIBERO
					tvd.setIdDominio(putDominioDTO.getDominio().getId());
					tvdBD.insertTipoVersamentoDominio(tvd);

					// NON CENSITE
					if(tvdNonCensite != null) {
						tvdNonCensite.setIdDominio(putDominioDTO.getDominio().getId());
						tvdBD.insertTipoVersamentoDominio(tvdNonCensite);
					}

					// TV MBT
					tvdBollo.setIdDominio(putDominioDTO.getDominio().getId());
					tvdBD.insertTipoVersamentoDominio(tvdBollo);

					dominiBD.commit();
				} catch (ServiceException e) {
					dominiBD.rollback(); 
					throw e;
				} finally {
					// ripristino l'autocommit
					dominiBD.setAutoCommit(true);	
				}
			} else {
				try {
					dominiBD.setAutoCommit(false);
					dominiBD.updateDominio(putDominioDTO.getDominio());
					UnitaOperativa uo = new UnitaOperativa();
					uo.setAbilitato(true);
					uo.setAnagrafica(putDominioDTO.getDominio().getAnagrafica());
					uo.setCodUo(it.govpay.model.Dominio.EC);
					uo.setIdDominio(putDominioDTO.getDominio().getId());
					uoBd.updateUnitaOperativa(uo);
					dominiBD.commit();
				} catch (NotFoundException | ServiceException e) {
					dominiBD.rollback(); 
					throw e;
				} finally {
					dominiBD.setAutoCommit(true);	
				}
			}
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new DominioNonTrovatoException(e.getMessage());
		} finally {
			if(dominiBD != null)
				dominiBD.closeConnection();
		}
		return dominioDTOResponse;
	}

	public FindDominiDTOResponse findDomini(FindDominiDTO listaDominiDTO) throws NotAuthorizedException, ServiceException, NotAuthenticatedException {
		DominiBD dominiBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			dominiBD = new DominiBD(configWrapper);
			DominioFilter filter = null;
			if(listaDominiDTO.isSimpleSearch()) {
				filter = dominiBD.newFilter(true);
				filter.setSimpleSearchString(listaDominiDTO.getSimpleSearch());
			} else {
				filter = dominiBD.newFilter(false);
				filter.setCodStazione(listaDominiDTO.getCodStazione());
				filter.setCodDominio(listaDominiDTO.getCodDominio());
				filter.setRagioneSociale(listaDominiDTO.getRagioneSociale());
				filter.setAbilitato(listaDominiDTO.getAbilitato());
			}
			if(listaDominiDTO.getIdDomini() != null && listaDominiDTO.getIdDomini().size() >0) {
				filter.getIdDomini().addAll(listaDominiDTO.getIdDomini());
			}			
			filter.setOffset(listaDominiDTO.getOffset());
			filter.setLimit(listaDominiDTO.getLimit());
			filter.getFilterSortList().addAll(listaDominiDTO.getFieldSortList());

			if(listaDominiDTO.getFormBackoffice() != null && listaDominiDTO.getFormBackoffice().booleanValue()) {
				// filtro per id che hanno form definite
				TipiVersamentoDominiBD tipiVersamentoDominiBD = new TipiVersamentoDominiBD(configWrapper);
				TipoVersamentoDominioFilter newFilter = tipiVersamentoDominiBD.newFilter();
				newFilter.setFormBackoffice(true);
				if(filter.getIdDomini().size() > 0)
					newFilter.setListaIdDomini(filter.getIdDomini());
				
				List<Long> idDomini = tipiVersamentoDominiBD.getIdDominiConFormDefinita(newFilter);

				if(idDomini.size() >0) {
					for (Long dominioConForm : idDomini) {
						if(!filter.getIdDomini().contains(dominioConForm))
							filter.getIdDomini().add(dominioConForm);
					}
//					filter.getIdDomini().addAll(idDomini);
				} else {
					return new FindDominiDTOResponse(0, new ArrayList<Dominio>());
				}
			}

			if(listaDominiDTO.getFormPortalePagamento() != null && listaDominiDTO.getFormPortalePagamento().booleanValue()) {
				// filtro per id che hanno form definite
				TipiVersamentoDominiBD tipiVersamentoDominiBD = new TipiVersamentoDominiBD(configWrapper);
				TipoVersamentoDominioFilter newFilter = tipiVersamentoDominiBD.newFilter();
				newFilter.setFormPortalePagamento(true);
				if(filter.getIdDomini().size() > 0)
					newFilter.setListaIdDomini(filter.getIdDomini());
				List<Long> idDomini = tipiVersamentoDominiBD.getIdDominiConFormDefinita(newFilter);

				if(idDomini.size() >0) {
					for (Long dominioConForm : idDomini) {
						if(!filter.getIdDomini().contains(dominioConForm))
							filter.getIdDomini().add(dominioConForm);
					}
//					filter.getIdDomini().addAll(idDomini);
				} else {
					return new FindDominiDTOResponse(0, new ArrayList<Dominio>());
				}
			}

			return new FindDominiDTOResponse(dominiBD.count(filter), dominiBD.findAll(filter));

		} finally {
			if(dominiBD != null)
				dominiBD.closeConnection();
		}
	}

	public byte[] getLogo(GetDominioDTO getDominioDTO) throws DominioNonTrovatoException, ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			Dominio dominio = AnagraficaManager.getDominio(configWrapper, getDominioDTO.getCodDominio());

			if(dominio.getLogo() != null && dominio.getLogo().length > 0) {
				return dominio.getLogo(); 
			}
			else
				throw new org.openspcoop2.generic_project.exception.NotFoundException();
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new DominioNonTrovatoException("Dominio " + getDominioDTO.getCodDominio() + " non censito in Anagrafica o logo non presente");
		} finally {
		}
	}

	public GetDominioDTOResponse getDominio(GetDominioDTO getDominioDTO) throws NotAuthorizedException, DominioNonTrovatoException, ServiceException, NotAuthenticatedException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			Dominio dominio = AnagraficaManager.getDominio(configWrapper, getDominioDTO.getCodDominio());
			GetDominioDTOResponse response = new GetDominioDTOResponse(dominio);
			response.setUo(dominio.getUnitaOperative(configWrapper));
			response.setIban(dominio.getIbanAccredito(configWrapper));
			response.setTributi(dominio.getTributi(configWrapper));
			response.setTipiVersamentoDominio(dominio.getTipiVersamento(configWrapper));

			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new DominioNonTrovatoException("Dominio " + getDominioDTO.getCodDominio() + " non censito in Anagrafica");
		} finally {
		}
	}

	public FindUnitaOperativeDTOResponse findUnitaOperative(FindUnitaOperativeDTO findUnitaOperativeDTO) throws NotAuthorizedException, DominioNonTrovatoException, ServiceException, NotAuthenticatedException {
		UnitaOperativeBD unitaOperativeBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			unitaOperativeBD = new UnitaOperativeBD(configWrapper);
			
			unitaOperativeBD.setupConnection(configWrapper.getTransactionID());
			
			UnitaOperativaFilter filter = null;
			if(findUnitaOperativeDTO.isSimpleSearch()) {
				filter = unitaOperativeBD.newFilter(true);
				filter.setSimpleSearchString(findUnitaOperativeDTO.getSimpleSearch());
			} else {
				filter = unitaOperativeBD.newFilter(false);
				filter.setCodIdentificativo(findUnitaOperativeDTO.getCodIdentificativo());
				filter.setRagioneSociale(findUnitaOperativeDTO.getRagioneSociale());
				filter.setSearchAbilitato(findUnitaOperativeDTO.getAbilitato());
			}
			try {
				filter.setDominioFilter(AnagraficaManager.getDominio(configWrapper, findUnitaOperativeDTO.getCodDominio()).getId());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException("Dominio " + findUnitaOperativeDTO.getCodDominio() + " non censito in Anagrafica");
			}
			filter.setOffset(findUnitaOperativeDTO.getOffset());
			filter.setLimit(findUnitaOperativeDTO.getLimit());
			filter.getFilterSortList().addAll(findUnitaOperativeDTO.getFieldSortList());

			if(findUnitaOperativeDTO.getUnitaOperative() != null) {
				List<Long> idUO = new ArrayList<>();
				for (IdUnitaOperativa uo : findUnitaOperativeDTO.getUnitaOperative()) {
					if(uo.getIdUnita() != null) {
						idUO.add(uo.getIdUnita());
					}
				}
				filter.setListaIdUo(idUO);
			}
			if(findUnitaOperativeDTO.getAssociati() != null && findUnitaOperativeDTO.getAssociati())
				filter.setExcludeEC(false);
			else 
				filter.setExcludeEC(true);

			return new FindUnitaOperativeDTOResponse(unitaOperativeBD.count(filter), unitaOperativeBD.findAll(filter));
		} finally {
			if(unitaOperativeBD != null)
				unitaOperativeBD.closeConnection();
		}
	}

	public GetUnitaOperativaDTOResponse getUnitaOperativa(GetUnitaOperativaDTO getUnitaOperativaDTO) throws NotAuthorizedException, DominioNonTrovatoException, UnitaOperativaNonTrovataException, ServiceException, NotAuthenticatedException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, getUnitaOperativaDTO.getCodDominio());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException("Dominio " + getUnitaOperativaDTO.getCodDominio() + " non censito in Anagrafica");
			}

			return new GetUnitaOperativaDTOResponse(AnagraficaManager.getUnitaOperativaByCodUnivocoUo(configWrapper, dominio.getId(), getUnitaOperativaDTO.getCodUnivocoUnitaOperativa()));
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new UnitaOperativaNonTrovataException("Unita Operativa " + getUnitaOperativaDTO.getCodUnivocoUnitaOperativa() + " non censita in Anagrafica per il Dominio " + getUnitaOperativaDTO.getCodDominio());
		} finally {
		}
	}

	public PutUnitaOperativaDTOResponse createOrUpdateUnitaOperativa(PutUnitaOperativaDTO putUnitaOperativaDTO) throws ServiceException, DominioNonTrovatoException, UnitaOperativaNonTrovataException, NotAuthorizedException, NotAuthenticatedException, UnprocessableEntityException{
		PutUnitaOperativaDTOResponse putUoDTOResponse = new PutUnitaOperativaDTOResponse();
		UnitaOperativeBD uoBd = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			try {
				// inserisco l'iddominio
				putUnitaOperativaDTO.getUo().setIdDominio(AnagraficaManager.getDominio(configWrapper, putUnitaOperativaDTO.getIdDominio()).getId());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new UnprocessableEntityException("Il dominio "+putUnitaOperativaDTO.getIdDominio()+" indicato non esiste.");
			}

			uoBd = new UnitaOperativeBD(configWrapper);
			
			uoBd.setupConnection(configWrapper.getTransactionID());
			
			uoBd.setAtomica(false); // gestione esplicita della connessione
			
			UnitaOperativaFilter filter = uoBd.newFilter(); 
			filter.setCodDominio(putUnitaOperativaDTO.getIdDominio());
			filter.setCodUo(putUnitaOperativaDTO.getIdUo());
			filter.setSearchModeEquals(true);

			// flag creazione o update
			boolean isCreate = uoBd.count(filter) == 0;
			putUoDTOResponse.setCreated(isCreate);
			if(isCreate) {
				uoBd.insertUnitaOperativa(putUnitaOperativaDTO.getUo());
			} else {
				uoBd.updateUnitaOperativa(putUnitaOperativaDTO.getUo());
			}
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new UnitaOperativaNonTrovataException(e.getMessage());
		} finally {
			if(uoBd != null)
				uoBd.closeConnection();
		}
		return putUoDTOResponse;
	}

	public FindIbanDTOResponse findIban(FindIbanDTO findIbanDTO) throws NotAuthorizedException, DominioNonTrovatoException, ServiceException, NotAuthenticatedException {
		IbanAccreditoBD ibanAccreditoBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			ibanAccreditoBD = new IbanAccreditoBD(configWrapper);
			IbanAccreditoFilter filter = null;
			if(findIbanDTO.isSimpleSearch()) {
				filter = ibanAccreditoBD.newFilter(true);
				filter.setSimpleSearchString(findIbanDTO.getSimpleSearch());
			} else {
				filter = ibanAccreditoBD.newFilter(false);
				filter.setSearchAbilitato(findIbanDTO.getAbilitato());
				filter.setCodIbanAccredito(findIbanDTO.getIban());
			}
			try {
				filter.setIdDominio(AnagraficaManager.getDominio(configWrapper, findIbanDTO.getCodDominio()).getId());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException("Dominio " + findIbanDTO.getCodDominio() + " non censito in Anagrafica");
			}
			filter.setOffset(findIbanDTO.getOffset());
			filter.setLimit(findIbanDTO.getLimit());
			filter.getFilterSortList().addAll(findIbanDTO.getFieldSortList());

			return new FindIbanDTOResponse(ibanAccreditoBD.count(filter), ibanAccreditoBD.findAll(filter));
		} finally {
			if(ibanAccreditoBD != null)
				ibanAccreditoBD.closeConnection();
		}
	}

	public GetIbanDTOResponse getIban(GetIbanDTO getIbanDTO) throws NotAuthorizedException, DominioNonTrovatoException, IbanAccreditoNonTrovatoException, ServiceException, NotAuthenticatedException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, getIbanDTO.getCodDominio());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException("Dominio " + getIbanDTO.getCodDominio() + " non censito in Anagrafica");
			}
			GetIbanDTOResponse response = new GetIbanDTOResponse(AnagraficaManager.getIbanAccredito(configWrapper, dominio.getId(), getIbanDTO.getCodIbanAccredito()));
			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new IbanAccreditoNonTrovatoException("Iban di accredito " + getIbanDTO.getCodIbanAccredito() + " non censito in Anagrafica per il Dominio " + getIbanDTO.getCodDominio());
		} finally {
		}
	}

	public PutIbanAccreditoDTOResponse createOrUpdateIbanAccredito(PutIbanAccreditoDTO putIbanAccreditoDTO) throws ServiceException, DominioNonTrovatoException, IbanAccreditoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException, UnprocessableEntityException{
		PutIbanAccreditoDTOResponse putIbanAccreditoDTOResponse = new PutIbanAccreditoDTOResponse();
		IbanAccreditoBD ibanAccreditoBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			try {
				// inserisco l'iddominio
				putIbanAccreditoDTO.getIban().setIdDominio(AnagraficaManager.getDominio(configWrapper, putIbanAccreditoDTO.getIdDominio()).getId());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new UnprocessableEntityException("Il dominio "+putIbanAccreditoDTO.getIdDominio()+" indicato non esiste.");
			}

			ibanAccreditoBD = new IbanAccreditoBD(configWrapper);
			IbanAccreditoFilter filter = ibanAccreditoBD.newFilter(); 
			filter.setCodDominio(putIbanAccreditoDTO.getIdDominio());
			filter.setCodIbanAccredito(putIbanAccreditoDTO.getIbanAccredito());
			filter.setSearchModeEquals(true);

			// flag creazione o update
			boolean isCreate = ibanAccreditoBD.count(filter) == 0;
			putIbanAccreditoDTOResponse.setCreated(isCreate);
			if(isCreate) {
				ibanAccreditoBD.insertIbanAccredito(putIbanAccreditoDTO.getIban());
			} else {
				ibanAccreditoBD.updateIbanAccredito(putIbanAccreditoDTO.getIban());
			}
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new IbanAccreditoNonTrovatoException(e.getMessage());
		} finally {
			if(ibanAccreditoBD != null)
				ibanAccreditoBD.closeConnection();
		}
		return putIbanAccreditoDTOResponse;
	}

	public FindTributiDTOResponse findTributi(FindTributiDTO findTributiDTO) throws NotAuthorizedException, DominioNonTrovatoException, ServiceException, NotAuthenticatedException {
		TributiBD tributiBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			tributiBD = new TributiBD(configWrapper);
			TributoFilter filter = null;
			if(findTributiDTO.isSimpleSearch()) {
				filter = tributiBD.newFilter(true);
				filter.setSimpleSearchString(findTributiDTO.getSimpleSearch());
			} else {
				filter = tributiBD.newFilter(false);
				filter.setCodTributo(findTributiDTO.getCodTributo());
				filter.setDescrizione(findTributiDTO.getDescrizione());
				filter.setSearchAbilitato(findTributiDTO.getAbilitato());
			}
			try {
				filter.setIdDominio(AnagraficaManager.getDominio(configWrapper, findTributiDTO.getCodDominio()).getId());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException("Dominio " + findTributiDTO.getCodDominio() + " non censito in Anagrafica");
			}
			filter.setOffset(findTributiDTO.getOffset());
			filter.setLimit(findTributiDTO.getLimit());
			filter.getFilterSortList().addAll(findTributiDTO.getFieldSortList());

			List<it.govpay.bd.model.Tributo> findAll = tributiBD.findAll(filter);

			List<GetTributoDTOResponse> lst = new ArrayList<>();
			for(it.govpay.bd.model.Tributo t: findAll) {
				lst.add(new GetTributoDTOResponse(t, t.getIbanAccredito(), t.getIbanAppoggio()));
			}

			return new FindTributiDTOResponse(tributiBD.count(filter), lst);
		} finally {
			if(tributiBD != null)
				tributiBD.closeConnection();
		}
	}

	public GetTributoDTOResponse getTributo(GetTributoDTO getTributoDTO) throws NotAuthorizedException, DominioNonTrovatoException, TributoNonTrovatoException, ServiceException, NotAuthenticatedException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, getTributoDTO.getCodDominio());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException("Dominio " + getTributoDTO.getCodDominio() + " non censito in Anagrafica");
			}
			it.govpay.bd.model.Tributo tributo = AnagraficaManager.getTributo(configWrapper, dominio.getId(), getTributoDTO.getCodTributo());
			GetTributoDTOResponse response = new GetTributoDTOResponse(tributo, tributo.getIbanAccredito(), tributo.getIbanAppoggio());
			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new TributoNonTrovatoException("Entrata " + getTributoDTO.getCodTributo() + " non censita in Anagrafica per il Dominio " + getTributoDTO.getCodDominio());
		} finally {
		}
	}

	public PutEntrataDominioDTOResponse createOrUpdateEntrataDominio(PutEntrataDominioDTO putEntrataDominioDTO) throws ServiceException, 
	DominioNonTrovatoException, TipoTributoNonTrovatoException, TributoNonTrovatoException, IbanAccreditoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException, RequestValidationException, UnprocessableEntityException{ 
		PutEntrataDominioDTOResponse putIbanAccreditoDTOResponse = new PutEntrataDominioDTOResponse();
		TributiBD tributiBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			Dominio dominio = null;
			try {
				// inserisco l'iddominio
				dominio = AnagraficaManager.getDominio(configWrapper, putEntrataDominioDTO.getIdDominio()); 
				putEntrataDominioDTO.getTributo().setIdDominio(dominio.getId());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new UnprocessableEntityException("Il dominio "+putEntrataDominioDTO.getIdDominio()+" indicato non esiste.");
			}

			TipoTributo tipoTributo = null;
			// entrata
			try {
				tipoTributo = AnagraficaManager.getTipoTributo(configWrapper, putEntrataDominioDTO.getIdTributo());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new UnprocessableEntityException("L'entrata "+putEntrataDominioDTO.getIdTributo()+" indicata non esiste.");
			}

			putEntrataDominioDTO.getTributo().setIdTipoTributo(tipoTributo.getId());

			// Iban Accredito 
			try {
				if(putEntrataDominioDTO.getIbanAccredito() != null) {
					putEntrataDominioDTO.getTributo().setIdIbanAccredito(AnagraficaManager.getIbanAccredito(configWrapper, dominio.getId(),
							putEntrataDominioDTO.getIbanAccredito()).getId()); 
				}
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new UnprocessableEntityException("L'iban accredito "+putEntrataDominioDTO.getIbanAccredito()+" indicato non esiste.");
			}

			// Iban Accredito postale
			try {
				if(putEntrataDominioDTO.getIbanAppoggio() != null) {
					putEntrataDominioDTO.getTributo().setIdIbanAppoggio(AnagraficaManager.getIbanAccredito(configWrapper, dominio.getId(),
							putEntrataDominioDTO.getIbanAppoggio()).getId()); 
				}
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new UnprocessableEntityException("L'iban appoggio "+putEntrataDominioDTO.getIbanAppoggio()+" indicato non esiste.");
			}


			tributiBD = new TributiBD(configWrapper);
			TributoFilter filter = tributiBD.newFilter(); 
			filter.setCodDominio(putEntrataDominioDTO.getIdDominio());
			filter.setCodTributo(putEntrataDominioDTO.getIdTributo());
			filter.setSearchModeEquals(true);

			// flag creazione o update
			boolean isCreate = tributiBD.count(filter) == 0;
			putIbanAccreditoDTOResponse.setCreated(isCreate);
			if(isCreate) {
				tributiBD.insertTributo(putEntrataDominioDTO.getTributo());
			} else {
				tributiBD.updateTributo(putEntrataDominioDTO.getTributo());
			}
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new TributoNonTrovatoException(e.getMessage());
		} finally {
			if(tributiBD != null)
				tributiBD.closeConnection();
		}
		return putIbanAccreditoDTOResponse;
	}


	public FindTipiPendenzaDominioDTOResponse findTipiPendenza(FindTipiPendenzaDominioDTO findTipiPendenzaDTO) throws NotAuthorizedException, DominioNonTrovatoException, ServiceException, NotAuthenticatedException {
		TipiVersamentoDominiBD tipiVersamentoDominiBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			tipiVersamentoDominiBD = new TipiVersamentoDominiBD(configWrapper);
			TipoVersamentoDominioFilter filter = tipiVersamentoDominiBD.newFilter();
			//			if(findTipiPendenzaDTO.isSimpleSearch()) {
			//				filter = tipiVersamentoDominiBD.newFilter(true);
			//				filter.setSimpleSearchString(findTipiPendenzaDTO.getSimpleSearch());
			//			} else {
			//				filter = tipiVersamentoDominiBD.newFilter(false);
			//				filter.setCodTipoVersamento(findTipiPendenzaDTO.getCodTipoVersamento());
			//				filter.setDescrizione(findTipiPendenzaDTO.getDescrizione());
			//				filter.setCodDominio(findTipiPendenzaDTO.getCodDominio());
			//			}
			try {
				filter.setIdDominio(AnagraficaManager.getDominio(configWrapper, findTipiPendenzaDTO.getCodDominio()).getId());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException("Dominio " + findTipiPendenzaDTO.getCodDominio() + " non censito in Anagrafica");
			}
			filter.setOffset(findTipiPendenzaDTO.getOffset());
			filter.setLimit(findTipiPendenzaDTO.getLimit());
			filter.getFilterSortList().addAll(findTipiPendenzaDTO.getFieldSortList());
			filter.setSearchAbilitato(findTipiPendenzaDTO.getAbilitato());
			filter.setListaIdTipiVersamento(findTipiPendenzaDTO.getIdTipiVersamento());
			filter.setFormBackoffice(findTipiPendenzaDTO.getFormBackoffice());
			filter.setFormPortalePagamento(findTipiPendenzaDTO.getFormPortalePagamento());
			filter.setTrasformazione(findTipiPendenzaDTO.getTrasformazione());
			filter.setDescrizione(findTipiPendenzaDTO.getDescrizione());

			List<it.govpay.bd.model.TipoVersamentoDominio> findAll = tipiVersamentoDominiBD.findAll(filter);

			List<GetTipoPendenzaDominioDTOResponse> lst = new ArrayList<>();
			for(it.govpay.bd.model.TipoVersamentoDominio t: findAll) {
				lst.add(new GetTipoPendenzaDominioDTOResponse(t));
			}

			return new FindTipiPendenzaDominioDTOResponse(tipiVersamentoDominiBD.count(filter), lst);
		} finally {
			if(tipiVersamentoDominiBD != null)
				tipiVersamentoDominiBD.closeConnection();
		}
	}

	public FindTipiPendenzaDominioDTOResponse findTipiPendenzaConPortalePagamento(FindTipiPendenzaDominioDTO findTipiPendenzaDTO) throws NotAuthorizedException, DominioNonTrovatoException, ServiceException, NotAuthenticatedException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			Long idDominio = null;
			try {
				idDominio = AnagraficaManager.getDominio(configWrapper, findTipiPendenzaDTO.getCodDominio()).getId();
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException("Dominio " + findTipiPendenzaDTO.getCodDominio() + " non censito in Anagrafica");
			}
			List<it.govpay.bd.model.TipoVersamentoDominio> findAllTmp = AnagraficaManager.getListaTipiVersamentoDominioConPagamentiPortaleForm(configWrapper, idDominio);

			List<it.govpay.bd.model.TipoVersamentoDominio> findAll = new ArrayList<>();
			List<Long> idTipiVersamentoAutorizzati = findTipiPendenzaDTO.getIdTipiVersamento();
			if(idTipiVersamentoAutorizzati != null && idTipiVersamentoAutorizzati.size() >0) {
				for (TipoVersamentoDominio tipoVersamentoDominio : findAll) {
					if(idTipiVersamentoAutorizzati.contains(tipoVersamentoDominio.getTipoVersamento(configWrapper).getId())) {
						findAll.add(tipoVersamentoDominio);
					}
				}
			} else {
				findAll.addAll(findAllTmp);
			}

			List<GetTipoPendenzaDominioDTOResponse> lst = new ArrayList<>();
			for(it.govpay.bd.model.TipoVersamentoDominio t: findAll) {
				lst.add(new GetTipoPendenzaDominioDTOResponse(t));
			}

			return new FindTipiPendenzaDominioDTOResponse(lst.size(), lst);
		} finally {
		}
	}

	public GetTipoPendenzaDominioDTOResponse getTipoPendenza(GetTipoPendenzaDominioDTO getTipoPendenzaDTO) throws NotAuthorizedException, DominioNonTrovatoException, TipoVersamentoNonTrovatoException, ServiceException, NotAuthenticatedException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, getTipoPendenzaDTO.getCodDominio());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException("Dominio " + getTipoPendenzaDTO.getCodDominio() + " non censito in Anagrafica");
			}
			TipoVersamentoDominio tipoVersamentoDominio = AnagraficaManager.getTipoVersamentoDominio(configWrapper, dominio.getId(), getTipoPendenzaDTO.getCodTipoVersamento());
			GetTipoPendenzaDominioDTOResponse response = new GetTipoPendenzaDominioDTOResponse(tipoVersamentoDominio);
			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new TipoVersamentoNonTrovatoException("Tipo Pendenza " + getTipoPendenzaDTO.getCodTipoVersamento() + " non censito in Anagrafica per il Dominio " + getTipoPendenzaDTO.getCodDominio());
		} finally {
		}
	}

	public PutTipoPendenzaDominioDTOResponse createOrUpdateTipoPendenzaDominio(PutTipoPendenzaDominioDTO putTipoPendenzaDominioDTO) throws ServiceException, 
	DominioNonTrovatoException, TipoVersamentoNonTrovatoException, TributoNonTrovatoException, IbanAccreditoNonTrovatoException, 
	NotAuthorizedException, NotAuthenticatedException, RequestValidationException, ValidationException, UnprocessableEntityException{ 
		PutTipoPendenzaDominioDTOResponse putTipoPendenzaDominioDTOResponse = new PutTipoPendenzaDominioDTOResponse();
		TipiVersamentoDominiBD tipiVersamentoDominiBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			try {
				// inserisco l'iddominio
				putTipoPendenzaDominioDTO.getTipoVersamentoDominio().setIdDominio(AnagraficaManager.getDominio(configWrapper, putTipoPendenzaDominioDTO.getIdDominio()).getId());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new UnprocessableEntityException("Il dominio "+putTipoPendenzaDominioDTO.getIdDominio()+" indicato non esiste.");
			}

			TipoVersamento tipoVersamento = null;
			try {
				tipoVersamento = AnagraficaManager.getTipoVersamento(configWrapper, putTipoPendenzaDominioDTO.getCodTipoVersamento());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new UnprocessableEntityException("Il tipo pendenza "+putTipoPendenzaDominioDTO.getCodTipoVersamento()+" indicato non esiste.");
			}

			putTipoPendenzaDominioDTO.getTipoVersamentoDominio().setIdTipoVersamento(tipoVersamento.getId());

			if(putTipoPendenzaDominioDTO.getTipoVersamentoDominio().getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizione() != null) {
				// validazione schema di validazione
				IJsonSchemaValidator validator = null;

				try{
					validator = ValidatorFactory.newJsonSchemaValidator(ApiName.NETWORK_NT);
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					throw new ServiceException(e);
				}
				JsonSchemaValidatorConfig config = new JsonSchemaValidatorConfig();

				try {
					validator.setSchema(putTipoPendenzaDominioDTO.getTipoVersamentoDominio().getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizione().getBytes(), config, this.log);
				} catch (ValidationException e) {
					this.log.error("Validazione tramite JSON Schema completata con errore: " + e.getMessage(), e);
					throw new ValidationException("Lo schema indicato per la validazione della pendenza portali backoffice non e' valido.", e);
				} 
			}

			if(putTipoPendenzaDominioDTO.getTipoVersamentoDominio().getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizione() != null) {
				// validazione schema di validazione
				IJsonSchemaValidator validator = null;

				try{
					validator = ValidatorFactory.newJsonSchemaValidator(ApiName.NETWORK_NT);
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					throw new ServiceException(e);
				}
				JsonSchemaValidatorConfig config = new JsonSchemaValidatorConfig();

				try {
					validator.setSchema(putTipoPendenzaDominioDTO.getTipoVersamentoDominio().getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizione().getBytes(), config, this.log);
				} catch (ValidationException e) {
					this.log.error("Validazione tramite JSON Schema completata con errore: " + e.getMessage(), e);
					throw new ValidationException("Lo schema indicato per la validazione della pendenza portali backoffice non e' valido.", e);
				} 
			}

			tipiVersamentoDominiBD = new TipiVersamentoDominiBD(configWrapper);
			TipoVersamentoDominioFilter filter = tipiVersamentoDominiBD.newFilter(); 
			filter.setCodDominio(putTipoPendenzaDominioDTO.getIdDominio());
			filter.setCodTipoVersamento(putTipoPendenzaDominioDTO.getCodTipoVersamento());
			filter.setSearchModeEquals(true);

			// flag creazione o update
			boolean isCreate = tipiVersamentoDominiBD.count(filter) == 0;
			putTipoPendenzaDominioDTOResponse.setCreated(isCreate);
			if(isCreate) {
				tipiVersamentoDominiBD.insertTipoVersamentoDominio(putTipoPendenzaDominioDTO.getTipoVersamentoDominio());
			} else {
				tipiVersamentoDominiBD.updateTipoVersamentoDominio(putTipoPendenzaDominioDTO.getTipoVersamentoDominio());
			}
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new TributoNonTrovatoException(e.getMessage());
		} finally {
			if(tipiVersamentoDominiBD != null)
				tipiVersamentoDominiBD.closeConnection();
		}
		return putTipoPendenzaDominioDTOResponse;
	}

}
