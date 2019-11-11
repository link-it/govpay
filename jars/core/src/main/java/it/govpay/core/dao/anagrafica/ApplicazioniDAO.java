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
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.anagrafica.UtenzeBD;
import it.govpay.bd.anagrafica.filters.AclFilter;
import it.govpay.bd.anagrafica.filters.ApplicazioneFilter;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.core.dao.anagrafica.dto.FindApplicazioniDTO;
import it.govpay.core.dao.anagrafica.dto.FindApplicazioniDTOResponse;
import it.govpay.core.dao.anagrafica.dto.GetApplicazioneDTO;
import it.govpay.core.dao.anagrafica.dto.GetApplicazioneDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutApplicazioneDTO;
import it.govpay.core.dao.anagrafica.dto.PutApplicazioneDTOResponse;
import it.govpay.core.dao.anagrafica.exception.ApplicazioneNonTrovataException;
import it.govpay.core.dao.anagrafica.exception.DominioNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.RuoloNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.TipoVersamentoNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.UnitaOperativaNonTrovataException;
import it.govpay.core.dao.anagrafica.utils.UtenzaPatchUtils;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.commons.Dominio;
import it.govpay.core.dao.commons.Dominio.Uo;
import it.govpay.core.dao.pagamenti.dto.ApplicazionePatchDTO;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.model.IdUnitaOperativa;
import it.govpay.model.PatchOp;

public class ApplicazioniDAO extends BaseDAO {

	public ApplicazioniDAO() {
		super();
	}

	public ApplicazioniDAO(boolean useCacheData) {
		super(useCacheData);
	}

	public FindApplicazioniDTOResponse findApplicazioni(FindApplicazioniDTO listaApplicazioniDTO) throws NotAuthorizedException, ServiceException, NotAuthenticatedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
			ApplicazioneFilter filter = null;
			if(listaApplicazioniDTO.isSimpleSearch()) {
				filter = applicazioniBD.newFilter(true);
				filter.setSimpleSearchString(listaApplicazioniDTO.getSimpleSearch());
			} else {
				filter = applicazioniBD.newFilter(false);
				filter.setSearchAbilitato(listaApplicazioniDTO.getAbilitato());
			}
			filter.setOffset(listaApplicazioniDTO.getOffset());
			filter.setLimit(listaApplicazioniDTO.getLimit());
			filter.getFilterSortList().addAll(listaApplicazioniDTO.getFieldSortList());

			return new FindApplicazioniDTOResponse(applicazioniBD.count(filter), applicazioniBD.findAll(filter));

		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public GetApplicazioneDTOResponse getApplicazione(GetApplicazioneDTO getApplicazioneDTO) throws NotAuthorizedException, ApplicazioneNonTrovataException, ServiceException, NotAuthenticatedException {
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);
			return new GetApplicazioneDTOResponse(AnagraficaManager.getApplicazione(bd, getApplicazioneDTO.getCodApplicazione()));
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new ApplicazioneNonTrovataException("Applicazione " + getApplicazioneDTO.getCodApplicazione() + " non censita in Anagrafica");
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}


	public PutApplicazioneDTOResponse createOrUpdate(PutApplicazioneDTO putApplicazioneDTO) throws ServiceException,
	ApplicazioneNonTrovataException, NotAuthorizedException, NotAuthenticatedException, UnprocessableEntityException, TipoVersamentoNonTrovatoException, DominioNonTrovatoException, UnitaOperativaNonTrovataException, RuoloNonTrovatoException {  
		PutApplicazioneDTOResponse applicazioneDTOResponse = new PutApplicazioneDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);

			ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
			UtenzeBD utenzeBD = new UtenzeBD(bd);
			ApplicazioneFilter filter = applicazioniBD.newFilter(false);
			filter.setCodApplicazione(putApplicazioneDTO.getIdApplicazione());

			if(putApplicazioneDTO.getDomini() != null) {
				List<IdUnitaOperativa> idDomini = new ArrayList<>();
				for (Dominio dominioCommons : putApplicazioneDTO.getDomini()) {
					String codDominio = dominioCommons.getCodDominio();
					if(codDominio != null) {
						try {
							Long idDominio = AnagraficaManager.getDominio(bd, codDominio).getId();
							
							if(dominioCommons.getUo() != null && !dominioCommons.getUo().isEmpty()) {
								
								for (Uo uo : dominioCommons.getUo()) {		
									IdUnitaOperativa idUo = new IdUnitaOperativa();
									idUo.setIdDominio(idDominio);
									
									try {
										UnitaOperativa unitaOperativa = AnagraficaManager.getUnitaOperativa(bd, idDominio, uo.getCodUo());
										idUo.setIdUnita(unitaOperativa.getId());
										idDomini.add(idUo);
									} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
										throw new UnitaOperativaNonTrovataException("L'unita' operativa ["+ uo.getCodUo()+"] non e' censita nel sistema", e);
									}
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

				putApplicazioneDTO.getApplicazione().getUtenza().setIdDominiUo(idDomini );
			}

			if(putApplicazioneDTO.getCodTipiVersamento() != null) {
				List<Long> idTipiVersamento = new ArrayList<>();
				for (String codTipoVersamento : putApplicazioneDTO.getCodTipiVersamento()) {
					try {
						idTipiVersamento.add(AnagraficaManager.getTipoVersamento(bd, codTipoVersamento).getId());
					} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
						throw new TipoVersamentoNonTrovatoException("Il tipo pendenza ["+codTipoVersamento+"] non e' censito nel sistema", e);
					}
				}

				putApplicazioneDTO.getApplicazione().getUtenza().setIdTipiVersamento(idTipiVersamento);
			}
			
			if(putApplicazioneDTO.getApplicazione().getUtenza().getRuoli() != null && putApplicazioneDTO.getApplicazione().getUtenza().getRuoli().size() > 0) {
				AclBD aclBD = new AclBD(bd);
				AclFilter aclFilter = aclBD.newFilter();
				
				for (String idRuolo : putApplicazioneDTO.getApplicazione().getUtenza().getRuoli()) {
					aclFilter.setRuolo(idRuolo);
					long count= aclBD.count(aclFilter); 
					
					if(count <= 0) {
						throw new RuoloNonTrovatoException("Il ruolo ["+idRuolo+"] non e' censito nel sistema");
					}
				}
			}


			// flag creazione o update
			boolean isCreate = applicazioniBD.count(filter) == 0;
			applicazioneDTOResponse.setCreated(isCreate);
			if(isCreate) {
				// controllo che il principal scelto non sia gia' utilizzato
				if(utenzeBD.existsByPrincipalOriginale(putApplicazioneDTO.getApplicazione().getPrincipal()))
					throw new UnprocessableEntityException("Impossibile aggiungere l'Applicazione ["+putApplicazioneDTO.getIdApplicazione()+"], il Principal indicato non e' disponibile.");			

				applicazioniBD.insertApplicazione(putApplicazioneDTO.getApplicazione());
			} else {
				// prelevo la vecchia utenza
				Applicazione applicazioneOld = applicazioniBD.getApplicazione(putApplicazioneDTO.getIdApplicazione());

				// confronto con il principal originale perche' e' quello che ho ricevuto dal servizio
				if(!applicazioneOld.getUtenza().getPrincipalOriginale().equals(putApplicazioneDTO.getApplicazione().getPrincipal())) {
					// se ho cambiato il principal controllo che sia disponibile
					if(utenzeBD.existsByPrincipalOriginale(putApplicazioneDTO.getApplicazione().getPrincipal()))
						throw new UnprocessableEntityException("Impossibile modificare l'Applicazione ["+putApplicazioneDTO.getIdApplicazione()+"], il Principal indicato non e' disponibile.");	
				}

				applicazioniBD.updateApplicazione(putApplicazioneDTO.getApplicazione());
			}
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new ApplicazioneNonTrovataException(e.getMessage());
		} finally {
			if(bd != null) 
				bd.closeConnection();
		}
		return applicazioneDTOResponse;
	}

	public GetApplicazioneDTOResponse patch(ApplicazionePatchDTO patchDTO) throws ServiceException,ApplicazioneNonTrovataException, NotAuthorizedException, NotAuthenticatedException, ValidationException{
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId(), useCacheData);
			ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);

			Applicazione applicazione = applicazioniBD.getApplicazione(patchDTO.getCodApplicazione());

			GetApplicazioneDTOResponse getApplicazioneDTOResponse = new GetApplicazioneDTOResponse(applicazione);

			for(PatchOp op: patchDTO.getOp()) {
				UtenzaPatchUtils.patchUtenza(op, getApplicazioneDTOResponse.getApplicazione().getUtenza(), bd);
			}

			//applicazioniBD.updateApplicazione(getApplicazioneDTOResponse.getApplicazione());

			AnagraficaManager.removeFromCache(getApplicazioneDTOResponse.getApplicazione());
			AnagraficaManager.removeFromCache(getApplicazioneDTOResponse.getApplicazione().getUtenza()); 

			applicazione = applicazioniBD.getApplicazione(patchDTO.getCodApplicazione());
			getApplicazioneDTOResponse.setApplicazione(applicazione);

			return getApplicazioneDTOResponse;
		}catch(NotFoundException e) {
			throw new ApplicazioneNonTrovataException("Non esiste un'applicazione associata all'ID ["+patchDTO.getCodApplicazione()+"]");
		}finally {
			if(bd != null)
				bd.closeConnection();
		}

	}


}
