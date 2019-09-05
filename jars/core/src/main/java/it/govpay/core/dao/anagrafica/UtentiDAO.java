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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.springframework.security.core.Authentication;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.OperatoriBD;
import it.govpay.bd.anagrafica.TipiVersamentoBD;
import it.govpay.bd.anagrafica.UtenzeBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.anagrafica.filters.OperatoreFilter;
import it.govpay.bd.anagrafica.filters.TipoVersamentoFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.anagrafica.dto.DeleteOperatoreDTO;
import it.govpay.core.dao.anagrafica.dto.FindOperatoriDTO;
import it.govpay.core.dao.anagrafica.dto.FindOperatoriDTOResponse;
import it.govpay.core.dao.anagrafica.dto.LeggiOperatoreDTO;
import it.govpay.core.dao.anagrafica.dto.LeggiOperatoreDTOResponse;
import it.govpay.core.dao.anagrafica.dto.LeggiProfiloDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutOperatoreDTO;
import it.govpay.core.dao.anagrafica.dto.PutOperatoreDTOResponse;
import it.govpay.core.dao.anagrafica.exception.DominioNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.OperatoreNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.TipoVersamentoNonTrovatoException;
import it.govpay.core.dao.anagrafica.utils.UtenzaPatchUtils;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.commons.Dominio.Uo;
import it.govpay.core.dao.pagamenti.dto.OperatorePatchDTO;
import it.govpay.core.dao.pagamenti.exception.PagamentoPortaleNonTrovatoException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.model.IdUnitaOperativa;
import it.govpay.model.PatchOp;


public class UtentiDAO extends BaseDAO{

	public UtentiDAO() {
		super();
	}

	public UtentiDAO(boolean useCacheData) {
		super(useCacheData);
	}

	public LeggiProfiloDTOResponse getProfilo(Authentication authentication) throws NotAuthenticatedException, ServiceException, NotAuthorizedException {
		BasicBD bd = null;
		LeggiProfiloDTOResponse response = new LeggiProfiloDTOResponse();
		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(authentication);
			response.setNome(userDetails.getIdentificativo());
			response.setUtente(userDetails.getUtenza());
			if(userDetails.getUtenza().isAutorizzazioneDominiStar()) {
				DominiBD dominiBD = new DominiBD(bd);
				DominioFilter newFilter = dominiBD.newFilter();
				List<Dominio> findAll = dominiBD.findAll(newFilter);

//				List<it.govpay.core.dao.commons.Dominio> domini = new ArrayList<>();
//
//				for (Dominio dominio : findAll) {
//					it.govpay.core.dao.commons.Dominio dominioCommons = new it.govpay.core.dao.commons.Dominio();
//
//					dominioCommons.setCodDominio(dominio.getCodDominio());
//					dominioCommons.setId(dominio.getId());
//					dominioCommons.setRagioneSociale(dominio.getRagioneSociale());
//
//					List<UnitaOperativa> unitaOperative = dominio.getUnitaOperative(bd);
//					List<Uo> uoList = new ArrayList<>();
//					for (UnitaOperativa unitaOperativa : unitaOperative) {
//						Uo uo = new Uo();
//						uo.setCodUo(unitaOperativa.getCodUo());
//						uo.setId(unitaOperativa.getId());
//						uo.setRagioneSociale(unitaOperativa.getAnagrafica().getRagioneSociale());
//						uoList.add(uo );
//					}
//
//					dominioCommons.setUo(uoList );
//					domini.add(dominioCommons );
//				}

				response.setDomini(findAll );
			} else {
				List<it.govpay.bd.model.IdUnitaOperativa> dominiUo = userDetails.getUtenza().getDominiUo(bd);
				List<Dominio> domini = new ArrayList<Dominio>();
				try {
					domini = convertIdUnitaOperativeToDomini(bd, dominiUo);
				} catch (NotFoundException e) {
				}

				response.setDomini(domini);
			}
			if(userDetails.getUtenza().isAutorizzazioneTipiVersamentoStar()) {
				TipiVersamentoBD tipiVersamentoBD = new TipiVersamentoBD(bd);
				TipoVersamentoFilter newFilter = tipiVersamentoBD.newFilter();
				response.setTipiVersamento(tipiVersamentoBD.findAll(newFilter));
			} else {
				response.setTipiVersamento(userDetails.getUtenza().getTipiVersamento(bd));
			}

		} finally {
			if(bd != null)
				bd.closeConnection();
		}

		return response;
	}

	public static List<Dominio> convertIdUnitaOperativeToDomini(BasicBD bd, List<it.govpay.bd.model.IdUnitaOperativa> dominiUo) throws ServiceException, NotFoundException {
		List<Dominio> domini = new ArrayList<>();

		Map<String, List<it.govpay.bd.model.IdUnitaOperativa>> mapUO = new HashMap<String, List<it.govpay.bd.model.IdUnitaOperativa>>();
		for (it.govpay.bd.model.IdUnitaOperativa idUnita : dominiUo) {
			String key = idUnita.getCodDominio() != null ? idUnita.getCodDominio() : "_NULL_";

			List<it.govpay.bd.model.IdUnitaOperativa> remove = mapUO.remove(key);

			if(remove == null)
				remove = new ArrayList<>();
			
			remove.add(idUnita);

			mapUO.put(key, remove);
		}

		for (String codDominio : mapUO.keySet()) {
			
			if(!"_NULL_".equals(codDominio)) {
				List<UnitaOperativa> uoList = new ArrayList<>();
				Dominio dominioCommons = AnagraficaManager.getDominio(bd, codDominio);
				
				for (it.govpay.bd.model.IdUnitaOperativa idUnitaOperativa : mapUO.get(codDominio)) {
					if(idUnitaOperativa.getCodUO() != null)
						uoList.add(AnagraficaManager.getUnitaOperativa(bd, dominioCommons.getId(), idUnitaOperativa.getCodUO()));
				}	
				dominioCommons.setUnitaOperative(uoList);
				domini.add(dominioCommons);
			} else { // dovrebbe essere una lista max da 1 elemento
				domini.add(new Dominio());
			}
		}
		return domini;
	}

	public static List<it.govpay.core.dao.commons.Dominio> convertIdUnitaOperativeToDomini(List<it.govpay.bd.model.IdUnitaOperativa> dominiUo) {
		List<it.govpay.core.dao.commons.Dominio> domini = new ArrayList<>();

		Map<String, List<it.govpay.bd.model.IdUnitaOperativa>> mapUO = new HashMap<String, List<it.govpay.bd.model.IdUnitaOperativa>>();
		for (it.govpay.bd.model.IdUnitaOperativa idUnita : dominiUo) {
			String key = idUnita.getCodDominio() != null ? idUnita.getCodDominio() : "_NULL_";

			List<it.govpay.bd.model.IdUnitaOperativa> remove = mapUO.remove(key);

			if(remove == null)
				remove = new ArrayList<>();
			
			remove.add(idUnita);

			mapUO.put(key, remove);
		}

		for (String codDominio : mapUO.keySet()) {
			it.govpay.core.dao.commons.Dominio dominioCommons = new it.govpay.core.dao.commons.Dominio();
			if(!"_NULL_".equals(codDominio)) {
				List<Uo> uoList = new ArrayList<>();
				
				boolean first = true;
				for (it.govpay.bd.model.IdUnitaOperativa idUnitaOperativa : mapUO.get(codDominio)) {
					
					if(first) {
						dominioCommons.setCodDominio(idUnitaOperativa.getCodDominio());
						dominioCommons.setId(idUnitaOperativa.getIdDominio());
						dominioCommons.setRagioneSociale(idUnitaOperativa.getRagioneSociale());
						first = false;
					}
					
					Uo uo = new Uo();
					uo.setCodUo(idUnitaOperativa.getCodUO());
					uo.setId(idUnitaOperativa.getIdUnita());
					uo.setRagioneSociale(idUnitaOperativa.getRagioneSocialeUO());
					uoList.add(uo );
				}	
				dominioCommons.setUo(uoList);
			} else { // dovrebbe essere una lista max da 1 elemento

			}
			domini.add(dominioCommons);
		}
		return domini;
	}
	
	public static it.govpay.core.dao.commons.Dominio convertIdUnitaOperativeToDomini(List<it.govpay.bd.model.IdUnitaOperativa> dominiUo, String codDominio) {
		Map<String, List<it.govpay.bd.model.IdUnitaOperativa>> mapUO = new HashMap<String, List<it.govpay.bd.model.IdUnitaOperativa>>();
		for (it.govpay.bd.model.IdUnitaOperativa idUnita : dominiUo) {
			String key = idUnita.getCodDominio() != null ? idUnita.getCodDominio() : "_NULL_";

			List<it.govpay.bd.model.IdUnitaOperativa> remove = mapUO.remove(key);

			if(remove == null)
				remove = new ArrayList<>();
			
			remove.add(idUnita);

			mapUO.put(key, remove);
		}
		
		String key = codDominio != null ? codDominio : "_NULL_";

		it.govpay.core.dao.commons.Dominio dominioCommons = new it.govpay.core.dao.commons.Dominio();
		
		if(mapUO.containsKey(key)) {
			dominioCommons = new it.govpay.core.dao.commons.Dominio();
			
			if(!"_NULL_".equals(key)) {
				List<Uo> uoList = new ArrayList<>();
				
				
				boolean first = true;
				for (it.govpay.bd.model.IdUnitaOperativa idUnitaOperativa : mapUO.get(key)) {
					
					if(first) {
						dominioCommons.setCodDominio(idUnitaOperativa.getCodDominio());
						dominioCommons.setId(idUnitaOperativa.getIdDominio());
						dominioCommons.setRagioneSociale(idUnitaOperativa.getRagioneSociale());
						first = false;
					}
					
					Uo uo = new Uo();
					uo.setCodUo(idUnitaOperativa.getCodUO());
					uo.setId(idUnitaOperativa.getIdUnita());
					uo.setRagioneSociale(idUnitaOperativa.getRagioneSocialeUO());
					uoList.add(uo );
				}	
				dominioCommons.setUo(uoList);
			}
		}
		
		return dominioCommons;
	}
	
	public LeggiOperatoreDTOResponse getOperatore(LeggiOperatoreDTO leggiOperatore) throws NotAuthenticatedException, ServiceException, OperatoreNonTrovatoException, NotAuthorizedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);
			OperatoriBD applicazioniBD = new OperatoriBD(bd);

			Operatore operatore = applicazioniBD.getOperatore(leggiOperatore.getPrincipal());
			LeggiOperatoreDTOResponse response = new LeggiOperatoreDTOResponse();
			response.setOperatore(operatore);
			return response;
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e3) {
			throw new OperatoreNonTrovatoException("Operatore " + leggiOperatore.getPrincipal() + " non censito in Anagrafica");
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public FindOperatoriDTOResponse findOperatori(FindOperatoriDTO listaOperatoriDTO) throws NotAuthorizedException, ServiceException, NotAuthenticatedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			OperatoriBD applicazioniBD = new OperatoriBD(bd);
			OperatoreFilter filter = null;
			if(listaOperatoriDTO.isSimpleSearch()) {
				filter = applicazioniBD.newFilter(true);
				filter.setSimpleSearchString(listaOperatoriDTO.getSimpleSearch());
			} else {
				filter = applicazioniBD.newFilter(false);

			}

			filter.setSearchAbilitato(listaOperatoriDTO.getAbilitato());
			filter.setOffset(listaOperatoriDTO.getOffset());
			filter.setLimit(listaOperatoriDTO.getLimit());
			filter.getFilterSortList().addAll(listaOperatoriDTO.getFieldSortList());

			return new FindOperatoriDTOResponse(applicazioniBD.count(filter), applicazioniBD.findAll(filter));

		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public PutOperatoreDTOResponse createOrUpdate(PutOperatoreDTO putOperatoreDTO) throws ServiceException, OperatoreNonTrovatoException,TipoVersamentoNonTrovatoException, DominioNonTrovatoException, NotAuthorizedException, NotAuthenticatedException, UnprocessableEntityException {
		PutOperatoreDTOResponse operatoreDTOResponse = new PutOperatoreDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);
			OperatoriBD operatoriBD = new OperatoriBD(bd);
			UtenzeBD utenzeBD = new UtenzeBD(bd);
			OperatoreFilter filter = operatoriBD.newFilter(false);
			filter.setPrincipal(putOperatoreDTO.getPrincipal());
			filter.setSearchModeEquals(true); // ricerca esatta del principal che sto inserendo

			// flag creazione o update
			boolean isCreate = operatoriBD.count(filter) == 0;
			operatoreDTOResponse.setCreated(isCreate);

			if(putOperatoreDTO.getDomini() != null) {
				List<IdUnitaOperativa> idDomini = new ArrayList<>();
				for (it.govpay.core.dao.commons.Dominio dominioCommons : putOperatoreDTO.getDomini()) {


					String codDominio = dominioCommons.getCodDominio();
					if(codDominio != null) {
						try {
							Long idDominio = AnagraficaManager.getDominio(bd, codDominio).getId();

							if(dominioCommons.getUo() != null && !dominioCommons.getUo().isEmpty()) {

								for (Uo uo : dominioCommons.getUo()) {		
									IdUnitaOperativa idUo = new IdUnitaOperativa();
									idUo.setIdDominio(idDominio);

									UnitaOperativa unitaOperativa = AnagraficaManager.getUnitaOperativa(bd, idDominio, uo.getCodUo());
									idUo.setIdUnita(unitaOperativa.getId());
									idDomini.add(idUo);
								}

							} else {
								IdUnitaOperativa idUo = new IdUnitaOperativa();
								idUo.setIdDominio(idDominio);
								idDomini.add(idUo);
							}
						} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
							throw new DominioNonTrovatoException("Il dominio ["+codDominio+"] non e' censito nel sistema", e);
						}

					} else { // caso null/null 
						idDomini.add(new IdUnitaOperativa());
					}
				}

				putOperatoreDTO.getOperatore().getUtenza().setIdDominiUo(idDomini );
			}

			if(putOperatoreDTO.getCodTipiVersamento() != null) {
				List<Long> idTipiVersamento = new ArrayList<>();
				for (String codTipoVersamento : putOperatoreDTO.getCodTipiVersamento()) {
					try {
						idTipiVersamento.add(AnagraficaManager.getTipoVersamento(bd, codTipoVersamento).getId());
					} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
						throw new TipoVersamentoNonTrovatoException("Il tipo pendenza ["+codTipoVersamento+"] non e' censito nel sistema", e);
					}
				}

				putOperatoreDTO.getOperatore().getUtenza().setIdTipiVersamento(idTipiVersamento);
			}


			if(isCreate) {
				// controllo che il principal scelto non sia gia' utilizzato
				if(utenzeBD.existsByPrincipalOriginale(putOperatoreDTO.getPrincipal()))
					throw new UnprocessableEntityException("Impossibile aggiungere l'operatore ["+putOperatoreDTO.getOperatore().getNome() +"], il principal scelto non e' disponibile.");		

				operatoriBD.insertOperatore(putOperatoreDTO.getOperatore());
			} else {
				putOperatoreDTO.getOperatore().setIdUtenza(AnagraficaManager.getUtenza(bd, putOperatoreDTO.getOperatore().getUtenza().getPrincipal()).getId());
				operatoriBD.updateOperatore(putOperatoreDTO.getOperatore());
			}
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new OperatoreNonTrovatoException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return operatoreDTOResponse;
	}

	/**
	 * @param deleteOperatoreDTO
	 * @throws NotAuthenticatedException 
	 */
	public void deleteOperatore(DeleteOperatoreDTO deleteOperatoreDTO) throws NotAuthorizedException, OperatoreNonTrovatoException, ServiceException, NotAuthenticatedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);
			new OperatoriBD(bd).deleteOperatore(deleteOperatoreDTO.getPrincipal());
		} catch (NotFoundException e) {
			throw new OperatoreNonTrovatoException(e.getMessage());
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}


	public LeggiOperatoreDTOResponse patch(OperatorePatchDTO patchDTO) throws ServiceException,PagamentoPortaleNonTrovatoException, NotAuthorizedException, NotAuthenticatedException, ValidationException{
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			OperatoriBD operatoriBD = new OperatoriBD(bd);
			Operatore operatore = operatoriBD.getOperatore(patchDTO.getIdOperatore());
			LeggiOperatoreDTOResponse leggiOperatoreDTOResponse = new LeggiOperatoreDTOResponse();
			for(PatchOp op: patchDTO.getOp()) {
				UtenzaPatchUtils.patchUtenza(op, operatore.getUtenza(), bd);
			}

			//operatoriBD.updateOperatore(operatore);

			AnagraficaManager.removeFromCache(operatore);
			AnagraficaManager.removeFromCache(operatore.getUtenza()); 

			operatore = operatoriBD.getOperatore(patchDTO.getIdOperatore());
			leggiOperatoreDTOResponse.setOperatore(operatore);

			return leggiOperatoreDTOResponse;
		}catch(NotFoundException e) {
			throw new PagamentoPortaleNonTrovatoException("Non esiste un operatore associato al principal ["+patchDTO.getIdOperatore()+"]");
		}finally {
			if(bd != null)
				bd.closeConnection();
		}

	}


}



