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

import it.govpay.bd.BasicBD;
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
	DominioNonTrovatoException,StazioneNonTrovataException,TipoTributoNonTrovatoException, TipoVersamentoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{
		PutDominioDTOResponse dominioDTOResponse = new PutDominioDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);
			// stazione
			try {
				putDominioDTO.getDominio().setIdStazione(AnagraficaManager.getStazione(bd, putDominioDTO.getCodStazione()).getId());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new StazioneNonTrovataException(e.getMessage());
			} 

			TipiVersamentoDominiBD tvdBD = new TipiVersamentoDominiBD(bd);
			UnitaOperativeBD uoBd = new UnitaOperativeBD(bd);
			DominiBD dominiBD = new DominiBD(bd);
			DominioFilter filter = dominiBD.newFilter(false);
			filter.setCodDominio(putDominioDTO.getIdDominio());

			// flag creazione o update
			boolean isCreate = dominiBD.count(filter) == 0;
			dominioDTOResponse.setCreated(isCreate);
			if(isCreate) {
				TipoTributo bolloT = null;
				// bollo telematico
				try {
					bolloT = AnagraficaManager.getTipoTributo(bd, it.govpay.model.Tributo.BOLLOT);
				} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
					throw new TipoTributoNonTrovatoException(e.getMessage());
				}

				TributiBD tributiBD = new TributiBD(bd);

				Tributo tributo = new Tributo();
				tributo.setCodTributo(it.govpay.model.Tributo.BOLLOT);
				tributo.setAbilitato(false);
				tributo.setDescrizione(bolloT.getDescrizione());
				tributo.setIdTipoTributo(bolloT.getId());
				
				TipoVersamento libero = null;
				
				try {
					libero = AnagraficaManager.getTipoVersamento(bd, GovpayConfig.getInstance().getCodTipoVersamentoPendenzeLibere());
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
						nonCensite = AnagraficaManager.getTipoVersamento(bd, GovpayConfig.getInstance().getCodTipoVersamentoPendenzeNonCensite());
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
					tipoVersamentoBolloT = AnagraficaManager.getTipoVersamento(bd, it.govpay.model.Tributo.BOLLOT);
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
					bd.setAutoCommit(false);
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
					
					bd.commit();
				} catch (ServiceException e) {
					bd.rollback(); 
					throw e;
				} finally {
					// ripristino l'autocommit
					bd.setAutoCommit(true);	
				}
			} else {
				try {
					bd.setAutoCommit(false);
					dominiBD.updateDominio(putDominioDTO.getDominio());
					UnitaOperativa uo = new UnitaOperativa();
					uo.setAbilitato(true);
					uo.setAnagrafica(putDominioDTO.getDominio().getAnagrafica());
					uo.setCodUo(it.govpay.model.Dominio.EC);
					uo.setIdDominio(putDominioDTO.getDominio().getId());
					uoBd.updateUnitaOperativa(uo);
					bd.commit();
				} catch (NotFoundException | ServiceException e) {
					bd.rollback(); 
					throw e;
				} finally {
					bd.setAutoCommit(true);	
				}
			}
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new DominioNonTrovatoException(e.getMessage());
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return dominioDTOResponse;
	}

	public FindDominiDTOResponse findDomini(FindDominiDTO listaDominiDTO) throws NotAuthorizedException, ServiceException, NotAuthenticatedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			DominiBD dominiBD = new DominiBD(bd);
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
			filter.setIdDomini(listaDominiDTO.getIdDomini());
			filter.setOffset(listaDominiDTO.getOffset());
			filter.setLimit(listaDominiDTO.getLimit());
			filter.getFilterSortList().addAll(listaDominiDTO.getFieldSortList());

			return new FindDominiDTOResponse(dominiBD.count(filter), dominiBD.findAll(filter));

		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public byte[] getLogo(GetDominioDTO getDominioDTO) throws DominioNonTrovatoException, ServiceException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			Dominio dominio = AnagraficaManager.getDominio(bd, getDominioDTO.getCodDominio());
			
			if(dominio.getLogo() != null && dominio.getLogo().length > 0) {
				return dominio.getLogo(); 
			}
			else
				throw new org.openspcoop2.generic_project.exception.NotFoundException();
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new DominioNonTrovatoException("Dominio " + getDominioDTO.getCodDominio() + " non censito in Anagrafica o logo non presente");
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public GetDominioDTOResponse getDominio(GetDominioDTO getDominioDTO) throws NotAuthorizedException, DominioNonTrovatoException, ServiceException, NotAuthenticatedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			Dominio dominio = AnagraficaManager.getDominio(bd, getDominioDTO.getCodDominio());
			GetDominioDTOResponse response = new GetDominioDTOResponse(dominio);
			response.setUo(dominio.getUnitaOperative(bd));
			response.setIban(dominio.getIbanAccredito(bd));
			response.setTributi(dominio.getTributi(bd));
			response.setTipiVersamentoDominio(dominio.getTipiVersamento(bd));

			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new DominioNonTrovatoException("Dominio " + getDominioDTO.getCodDominio() + " non censito in Anagrafica");
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public FindUnitaOperativeDTOResponse findUnitaOperative(FindUnitaOperativeDTO findUnitaOperativeDTO) throws NotAuthorizedException, DominioNonTrovatoException, ServiceException, NotAuthenticatedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			UnitaOperativeBD unitaOperativeBD = new UnitaOperativeBD(bd);
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
				filter.setDominioFilter(AnagraficaManager.getDominio(bd, findUnitaOperativeDTO.getCodDominio()).getId());
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
			if(bd != null)
				bd.closeConnection();
		}
	}

	public GetUnitaOperativaDTOResponse getUnitaOperativa(GetUnitaOperativaDTO getUnitaOperativaDTO) throws NotAuthorizedException, DominioNonTrovatoException, UnitaOperativaNonTrovataException, ServiceException, NotAuthenticatedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(bd, getUnitaOperativaDTO.getCodDominio());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException("Dominio " + getUnitaOperativaDTO.getCodDominio() + " non censito in Anagrafica");
			}

			return new GetUnitaOperativaDTOResponse(AnagraficaManager.getUnitaOperativaByCodUnivocoUo(bd, dominio.getId(), getUnitaOperativaDTO.getCodUnivocoUnitaOperativa()));
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new UnitaOperativaNonTrovataException("Unita Operativa " + getUnitaOperativaDTO.getCodUnivocoUnitaOperativa() + " non censito in Anagrafica per il dominio " + getUnitaOperativaDTO.getCodDominio());
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public PutUnitaOperativaDTOResponse createOrUpdateUnitaOperativa(PutUnitaOperativaDTO putUnitaOperativaDTO) throws ServiceException, DominioNonTrovatoException, UnitaOperativaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		PutUnitaOperativaDTOResponse putUoDTOResponse = new PutUnitaOperativaDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			try {
				// inserisco l'iddominio
				putUnitaOperativaDTO.getUo().setIdDominio(AnagraficaManager.getDominio(bd, putUnitaOperativaDTO.getIdDominio()).getId());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException(e.getMessage());
			}

			UnitaOperativeBD uoBd = new UnitaOperativeBD(bd);
			UnitaOperativaFilter filter = uoBd.newFilter(); 
			filter.setCodDominio(putUnitaOperativaDTO.getIdDominio());
			filter.setCodUo(putUnitaOperativaDTO.getIdUo());

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
			if(bd != null)
				bd.closeConnection();
		}
		return putUoDTOResponse;
	}

	public FindIbanDTOResponse findIban(FindIbanDTO findIbanDTO) throws NotAuthorizedException, DominioNonTrovatoException, ServiceException, NotAuthenticatedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(bd);
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
				filter.setIdDominio(AnagraficaManager.getDominio(bd, findIbanDTO.getCodDominio()).getId());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException("Dominio " + findIbanDTO.getCodDominio() + " non censito in Anagrafica");
			}
			filter.setOffset(findIbanDTO.getOffset());
			filter.setLimit(findIbanDTO.getLimit());
			filter.getFilterSortList().addAll(findIbanDTO.getFieldSortList());

			return new FindIbanDTOResponse(ibanAccreditoBD.count(filter), ibanAccreditoBD.findAll(filter));
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public GetIbanDTOResponse getIban(GetIbanDTO getIbanDTO) throws NotAuthorizedException, DominioNonTrovatoException, IbanAccreditoNonTrovatoException, ServiceException, NotAuthenticatedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(bd, getIbanDTO.getCodDominio());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException("Dominio " + getIbanDTO.getCodDominio() + " non censito in Anagrafica");
			}
			GetIbanDTOResponse response = new GetIbanDTOResponse(AnagraficaManager.getIbanAccredito(bd, dominio.getId(), getIbanDTO.getCodIbanAccredito()));
			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new IbanAccreditoNonTrovatoException("Iban di accredito " + getIbanDTO.getCodIbanAccredito() + " non censito in Anagrafica per il dominio " + getIbanDTO.getCodDominio());
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public PutIbanAccreditoDTOResponse createOrUpdateIbanAccredito(PutIbanAccreditoDTO putIbanAccreditoDTO) throws ServiceException, DominioNonTrovatoException, IbanAccreditoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{
		PutIbanAccreditoDTOResponse putIbanAccreditoDTOResponse = new PutIbanAccreditoDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			try {
				// inserisco l'iddominio
				putIbanAccreditoDTO.getIban().setIdDominio(AnagraficaManager.getDominio(bd, putIbanAccreditoDTO.getIdDominio()).getId());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException(e.getMessage());
			}

			IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(bd);
			IbanAccreditoFilter filter = ibanAccreditoBD.newFilter(); 
			filter.setCodDominio(putIbanAccreditoDTO.getIdDominio());
			filter.setCodIbanAccredito(putIbanAccreditoDTO.getIbanAccredito());

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
			if(bd != null)
				bd.closeConnection();
		}
		return putIbanAccreditoDTOResponse;
	}

	public FindTributiDTOResponse findTributi(FindTributiDTO findTributiDTO) throws NotAuthorizedException, DominioNonTrovatoException, ServiceException, NotAuthenticatedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			TributiBD ibanAccreditoBD = new TributiBD(bd);
			TributoFilter filter = null;
			if(findTributiDTO.isSimpleSearch()) {
				filter = ibanAccreditoBD.newFilter(true);
				filter.setSimpleSearchString(findTributiDTO.getSimpleSearch());
			} else {
				filter = ibanAccreditoBD.newFilter(false);
				filter.setCodTributo(findTributiDTO.getCodTributo());
				filter.setDescrizione(findTributiDTO.getDescrizione());
				filter.setSearchAbilitato(findTributiDTO.getAbilitato());
			}
			try {
				filter.setIdDominio(AnagraficaManager.getDominio(bd, findTributiDTO.getCodDominio()).getId());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException("Dominio " + findTributiDTO.getCodDominio() + " non censito in Anagrafica");
			}
			filter.setOffset(findTributiDTO.getOffset());
			filter.setLimit(findTributiDTO.getLimit());
			filter.getFilterSortList().addAll(findTributiDTO.getFieldSortList());

			List<it.govpay.bd.model.Tributo> findAll = ibanAccreditoBD.findAll(filter);

			List<GetTributoDTOResponse> lst = new ArrayList<>();
			for(it.govpay.bd.model.Tributo t: findAll) {
				lst.add(new GetTributoDTOResponse(t, t.getIbanAccredito(), t.getIbanAppoggio()));
			}

			return new FindTributiDTOResponse(ibanAccreditoBD.count(filter), lst);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public GetTributoDTOResponse getTributo(GetTributoDTO getTributoDTO) throws NotAuthorizedException, DominioNonTrovatoException, TributoNonTrovatoException, ServiceException, NotAuthenticatedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(bd, getTributoDTO.getCodDominio());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException("Dominio " + getTributoDTO.getCodDominio() + " non censito in Anagrafica");
			}
			it.govpay.bd.model.Tributo tributo = AnagraficaManager.getTributo(bd, dominio.getId(), getTributoDTO.getCodTributo());
			GetTributoDTOResponse response = new GetTributoDTOResponse(tributo, tributo.getIbanAccredito(), tributo.getIbanAppoggio());
			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new TributoNonTrovatoException("Tributo " + getTributoDTO.getCodTributo() + " non censito in Anagrafica per il dominio " + getTributoDTO.getCodDominio());
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public PutEntrataDominioDTOResponse createOrUpdateEntrataDominio(PutEntrataDominioDTO putEntrataDominioDTO) throws ServiceException, 
	DominioNonTrovatoException, TipoTributoNonTrovatoException, TributoNonTrovatoException, IbanAccreditoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException, RequestValidationException{ 
		PutEntrataDominioDTOResponse putIbanAccreditoDTOResponse = new PutEntrataDominioDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);
			try {
				// inserisco l'iddominio
				putEntrataDominioDTO.getTributo().setIdDominio(AnagraficaManager.getDominio(bd, putEntrataDominioDTO.getIdDominio()).getId());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException(e.getMessage());
			}

			TipoTributo tipoTributo = null;
			// bollo telematico
			try {
				tipoTributo = AnagraficaManager.getTipoTributo(bd, putEntrataDominioDTO.getIdTributo());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new TipoTributoNonTrovatoException(e.getMessage());
			}

			putEntrataDominioDTO.getTributo().setIdTipoTributo(tipoTributo.getId());

			// Iban Accredito 
			try {
				if(putEntrataDominioDTO.getIbanAccredito() != null) {
					putEntrataDominioDTO.getTributo().setIdIbanAccredito(AnagraficaManager.getIbanAccredito(bd, AnagraficaManager.getDominio(bd, putEntrataDominioDTO.getIdDominio()).getId(),
							putEntrataDominioDTO.getIbanAccredito()).getId()); 
				}
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new RequestValidationException("Iban accredito ["+putEntrataDominioDTO.getIbanAccredito()+"] non censito");
			}

			// Iban Accredito postale
			try {
				if(putEntrataDominioDTO.getIbanAppoggio() != null) {
					putEntrataDominioDTO.getTributo().setIdIbanAppoggio(AnagraficaManager.getIbanAccredito(bd, AnagraficaManager.getDominio(bd, putEntrataDominioDTO.getIdDominio()).getId(),
							putEntrataDominioDTO.getIbanAppoggio()).getId()); 
				}
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new RequestValidationException("Iban appoggio ["+putEntrataDominioDTO.getIbanAppoggio()+"] non censito");
			}


			TributiBD tributiBD = new TributiBD(bd);
			TributoFilter filter = tributiBD.newFilter(); 
			filter.setCodDominio(putEntrataDominioDTO.getIdDominio());
			filter.setCodTributo(putEntrataDominioDTO.getIdTributo());

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
			if(bd != null)
				bd.closeConnection();
		}
		return putIbanAccreditoDTOResponse;
	}
	
	
	public FindTipiPendenzaDominioDTOResponse findTipiPendenza(FindTipiPendenzaDominioDTO findTipiPendenzaDTO) throws NotAuthorizedException, DominioNonTrovatoException, ServiceException, NotAuthenticatedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			TipiVersamentoDominiBD tipiVersamentoDominiBD = new TipiVersamentoDominiBD(bd);
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
				filter.setIdDominio(AnagraficaManager.getDominio(bd, findTipiPendenzaDTO.getCodDominio()).getId());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException("Dominio " + findTipiPendenzaDTO.getCodDominio() + " non censito in Anagrafica");
			}
			filter.setOffset(findTipiPendenzaDTO.getOffset());
			filter.setLimit(findTipiPendenzaDTO.getLimit());
			filter.getFilterSortList().addAll(findTipiPendenzaDTO.getFieldSortList());
			filter.setSearchAbilitato(findTipiPendenzaDTO.getAbilitato());
			if(findTipiPendenzaDTO.getTipo() != null)
				filter.setTipo(findTipiPendenzaDTO.getTipo().getCodifica());
			filter.setListaIdTipiVersamento(findTipiPendenzaDTO.getIdTipiVersamento());
			filter.setForm(findTipiPendenzaDTO.getForm());
			filter.setTrasformazione(findTipiPendenzaDTO.getTrasformazione());

			List<it.govpay.bd.model.TipoVersamentoDominio> findAll = tipiVersamentoDominiBD.findAll(filter);

			List<GetTipoPendenzaDominioDTOResponse> lst = new ArrayList<>();
			for(it.govpay.bd.model.TipoVersamentoDominio t: findAll) {
				lst.add(new GetTipoPendenzaDominioDTOResponse(t));
			}

			return new FindTipiPendenzaDominioDTOResponse(tipiVersamentoDominiBD.count(filter), lst);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public GetTipoPendenzaDominioDTOResponse getTipoPendenza(GetTipoPendenzaDominioDTO getTipoPendenzaDTO) throws NotAuthorizedException, DominioNonTrovatoException, TipoVersamentoNonTrovatoException, ServiceException, NotAuthenticatedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			Dominio dominio = null;
			try {
				dominio = AnagraficaManager.getDominio(bd, getTipoPendenzaDTO.getCodDominio());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException("Dominio " + getTipoPendenzaDTO.getCodDominio() + " non censito in Anagrafica");
			}
			TipoVersamentoDominio tipoVersamentoDominio = AnagraficaManager.getTipoVersamentoDominio(bd, dominio.getId(), getTipoPendenzaDTO.getCodTipoVersamento());
			GetTipoPendenzaDominioDTOResponse response = new GetTipoPendenzaDominioDTOResponse(tipoVersamentoDominio);
			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new TipoVersamentoNonTrovatoException("Tipo Pendenza " + getTipoPendenzaDTO.getCodTipoVersamento() + " non censito in Anagrafica per il dominio " + getTipoPendenzaDTO.getCodDominio());
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public PutTipoPendenzaDominioDTOResponse createOrUpdateTipoPendenzaDominio(PutTipoPendenzaDominioDTO putTipoPendenzaDominioDTO) throws ServiceException, 
	DominioNonTrovatoException, TipoVersamentoNonTrovatoException, TributoNonTrovatoException, IbanAccreditoNonTrovatoException, 
		NotAuthorizedException, NotAuthenticatedException, RequestValidationException, ValidationException{ 
		PutTipoPendenzaDominioDTOResponse putTipoPendenzaDominioDTOResponse = new PutTipoPendenzaDominioDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);
			try {
				// inserisco l'iddominio
				putTipoPendenzaDominioDTO.getTipoVersamentoDominio().setIdDominio(AnagraficaManager.getDominio(bd, putTipoPendenzaDominioDTO.getIdDominio()).getId());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new DominioNonTrovatoException(e.getMessage());
			}

			TipoVersamento tipoVersamento = null;
			try {
				tipoVersamento = AnagraficaManager.getTipoVersamento(bd, putTipoPendenzaDominioDTO.getCodTipoVersamento());
			} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
				throw new TipoVersamentoNonTrovatoException(e.getMessage());
			}

			putTipoPendenzaDominioDTO.getTipoVersamentoDominio().setIdTipoVersamento(tipoVersamento.getId());
			
			if(putTipoPendenzaDominioDTO.getTipoVersamentoDominio().getValidazioneDefinizione() != null) {
				// validazione schema di validazione
				IJsonSchemaValidator validator = null;
	
				try{
					validator = ValidatorFactory.newJsonSchemaValidator(ApiName.NETWORK_NT);
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					throw new ServiceException(e);
				}
				JsonSchemaValidatorConfig config = new JsonSchemaValidatorConfig();
	
				try {
					validator.setSchema(putTipoPendenzaDominioDTO.getTipoVersamentoDominio().getValidazioneDefinizione().getBytes(), config, this.log);
				} catch (ValidationException e) {
					this.log.error("Validazione tramite JSON Schema completata con errore: " + e.getMessage(), e);
					throw new ValidationException("Lo schema indicato per la validazione non e' valido.", e);
				} 
			}

			TipiVersamentoDominiBD tipiVersamentoDominiBD = new TipiVersamentoDominiBD(bd);
			TipoVersamentoDominioFilter filter = tipiVersamentoDominiBD.newFilter(); 
			filter.setCodDominio(putTipoPendenzaDominioDTO.getIdDominio());
			filter.setCodTipoVersamento(putTipoPendenzaDominioDTO.getCodTipoVersamento());

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
			if(bd != null)
				bd.closeConnection();
		}
		return putTipoPendenzaDominioDTOResponse;
	}

}
