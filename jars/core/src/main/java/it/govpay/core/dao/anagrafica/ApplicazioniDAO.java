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

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.crypt.Password;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
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
		ApplicazioniBD applicazioniBD = null;
		try {
			applicazioniBD = new ApplicazioniBD(ContextThreadLocal.get().getTransactionId(), useCacheData);
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
			filter.setCodApplicazione(listaApplicazioniDTO.getCodApplicazione());
			filter.setPrincipalOriginale(listaApplicazioniDTO.getPrincipal());
			filter.setEseguiCountConLimit(listaApplicazioniDTO.isEseguiCountConLimit());
			
			Long count = null;
			
			if(listaApplicazioniDTO.isEseguiCount()) {
				 count = applicazioniBD.count(filter);
			}
			
			List<Applicazione> findAll = new ArrayList<>();
			if(listaApplicazioniDTO.isEseguiFindAll()) {
				findAll = applicazioniBD.findAll(filter);
				
				if(listaApplicazioniDTO.getLimit() == null && !listaApplicazioniDTO.isEseguiCount()) {
					count = (long) findAll.size();
				}
			}
			
			return new FindApplicazioniDTOResponse(count, findAll);

		} finally {
			if(applicazioniBD != null) 
				applicazioniBD.closeConnection();
		}
	}

	public GetApplicazioneDTOResponse getApplicazione(GetApplicazioneDTO getApplicazioneDTO) throws NotAuthorizedException, ApplicazioneNonTrovataException, ServiceException, NotAuthenticatedException {
		try {
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
			return new GetApplicazioneDTOResponse(AnagraficaManager.getApplicazione(configWrapper, getApplicazioneDTO.getCodApplicazione()));
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new ApplicazioneNonTrovataException("Applicazione " + getApplicazioneDTO.getCodApplicazione() + " non censita in Anagrafica");
		} finally {
		}
	}


	public PutApplicazioneDTOResponse createOrUpdate(PutApplicazioneDTO putApplicazioneDTO) throws ServiceException,
	ApplicazioneNonTrovataException, NotAuthorizedException, NotAuthenticatedException, UnprocessableEntityException, TipoVersamentoNonTrovatoException, DominioNonTrovatoException, UnitaOperativaNonTrovataException, RuoloNonTrovatoException {  
		PutApplicazioneDTOResponse applicazioneDTOResponse = new PutApplicazioneDTOResponse();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		it.govpay.bd.anagrafica.ApplicazioniBD applicazioniBD = null;
		try {
			applicazioniBD = new it.govpay.bd.anagrafica.ApplicazioniBD(configWrapper);
			
			applicazioniBD.setupConnection(configWrapper.getTransactionID());
			
			applicazioniBD.setAtomica(false); // gestione esplicita della connessione
			
			ApplicazioneFilter filter = applicazioniBD.newFilter(false);
			filter.setCodApplicazione(putApplicazioneDTO.getIdApplicazione());
			filter.setSearchModeEquals(true);

			if(putApplicazioneDTO.getDomini() != null) {
				List<IdUnitaOperativa> idDomini = new ArrayList<>();
				for (Dominio dominioCommons : putApplicazioneDTO.getDomini()) {
					String codDominio = dominioCommons.getCodDominio();
					if(codDominio != null) {
						try {
							Long idDominio = AnagraficaManager.getDominio(configWrapper, codDominio).getId();
							
							if(dominioCommons.getUo() != null && !dominioCommons.getUo().isEmpty()) {
								
								for (Uo uo : dominioCommons.getUo()) {		
									IdUnitaOperativa idUo = new IdUnitaOperativa();
									idUo.setIdDominio(idDominio);
									
									try {
										UnitaOperativa unitaOperativa = AnagraficaManager.getUnitaOperativa(configWrapper, idDominio, uo.getCodUo());
										idUo.setIdUnita(unitaOperativa.getId());
										idDomini.add(idUo);
									} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
										throw new UnprocessableEntityException("L'unita' operativa "+uo.getCodUo()+" indicata non esiste.");
									}
								}
								
							} else {
								IdUnitaOperativa idUo = new IdUnitaOperativa();
								idUo.setIdDominio(idDominio);
								idDomini.add(idUo);
							}
						} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
							throw new UnprocessableEntityException("Il dominio "+codDominio+" indicato non esiste.");
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
						idTipiVersamento.add(AnagraficaManager.getTipoVersamento(configWrapper, codTipoVersamento).getId());
					} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
						throw new UnprocessableEntityException("Il tipo pendenza "+codTipoVersamento+" indicato non esiste.");
					}
				}

				putApplicazioneDTO.getApplicazione().getUtenza().setIdTipiVersamento(idTipiVersamento);
			}
			
			if(putApplicazioneDTO.getApplicazione().getUtenza().getRuoli() != null && putApplicazioneDTO.getApplicazione().getUtenza().getRuoli().size() > 0) {
				AclBD aclBD = new AclBD(applicazioniBD);
				
				aclBD.setAtomica(false); // gestione esplicita della connessione
				
				AclFilter aclFilter = aclBD.newFilter();
				
				for (String idRuolo : putApplicazioneDTO.getApplicazione().getUtenza().getRuoli()) {
					aclFilter.setRuolo(idRuolo);
					long count= aclBD.count(aclFilter); 
					
					if(count <= 0) {
						throw new UnprocessableEntityException("Il ruolo "+idRuolo+" indicato non esiste.");
					}
				}
			}

			// controllo aggioramento password: 
			// se la stringa ricevuta e' vuota non la aggiorno, altrimenti la cambio
			if(StringUtils.isNotEmpty(putApplicazioneDTO.getApplicazione().getUtenza().getPassword())) {
				// cifratura dalla nuova password 
				Password password = new Password();
				String pwdTmp = putApplicazioneDTO.getApplicazione().getUtenza().getPassword();
				String cryptPwd = password.cryptPw(pwdTmp);
				
				log.debug("Cifratura Password ["+pwdTmp+"] > ["+cryptPwd+"]");
				putApplicazioneDTO.getApplicazione().getUtenza().setPassword(cryptPwd);
			}

			// flag creazione o update
			boolean isCreate = applicazioniBD.count(filter) == 0;
			applicazioneDTOResponse.setCreated(isCreate);
			
			UtenzeBD utenzeBD = new UtenzeBD(applicazioniBD);
			
			utenzeBD.setAtomica(false); // gestione esplicita della connessione
			
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
				
				// se non ho ricevuto una password imposto la vecchia
				if(StringUtils.isEmpty(putApplicazioneDTO.getApplicazione().getUtenza().getPassword())) {
					putApplicazioneDTO.getApplicazione().getUtenza().setPassword(applicazioneOld.getUtenza().getPassword());
				}
				
				applicazioniBD.updateApplicazione(putApplicazioneDTO.getApplicazione());
			}
		} catch (org.openspcoop2.generic_project.exception.NotFoundException e) {
			throw new ApplicazioneNonTrovataException(e.getMessage());
		} finally {
			if(applicazioniBD != null) 
				applicazioniBD.closeConnection();
		}
		return applicazioneDTOResponse;
	}

	public GetApplicazioneDTOResponse patch(ApplicazionePatchDTO patchDTO) throws ServiceException,ApplicazioneNonTrovataException, NotAuthorizedException, NotAuthenticatedException, ValidationException{
		it.govpay.bd.anagrafica.ApplicazioniBD applicazioniBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			applicazioniBD = new it.govpay.bd.anagrafica.ApplicazioniBD(configWrapper);

			Applicazione applicazione = applicazioniBD.getApplicazione(patchDTO.getCodApplicazione());

			GetApplicazioneDTOResponse getApplicazioneDTOResponse = new GetApplicazioneDTOResponse(applicazione);

			for(PatchOp op: patchDTO.getOp()) {
				UtenzaPatchUtils.patchUtenza(op, getApplicazioneDTOResponse.getApplicazione().getUtenza(), configWrapper);
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
			if(applicazioniBD != null) 
				applicazioniBD.closeConnection();
		}

	}


}
