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
package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.AclPost;
import it.govpay.backoffice.v1.beans.Applicazione;
import it.govpay.backoffice.v1.beans.ApplicazioneIndex;
import it.govpay.backoffice.v1.beans.ApplicazionePost;
import it.govpay.backoffice.v1.beans.CodificaAvvisi;
import it.govpay.backoffice.v1.beans.DominioProfiloIndex;
import it.govpay.backoffice.v1.beans.DominioProfiloPost;
import it.govpay.backoffice.v1.beans.Ruolo;
import it.govpay.backoffice.v1.beans.TipoPendenza;
import it.govpay.backoffice.v1.controllers.ApplicazioniController;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Acl;
import it.govpay.bd.model.UtenzaApplicazione;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.core.dao.anagrafica.dto.PutApplicazioneDTO;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Rpt.FirmaRichiesta;
import it.govpay.model.TipoVersamento;
import it.govpay.model.exception.CodificaInesistenteException;

public class ApplicazioniConverter {

	public static PutApplicazioneDTO getPutApplicazioneDTO(ApplicazionePost applicazionePost, String idA2A, Authentication user) throws NotAuthorizedException, CodificaInesistenteException {
		PutApplicazioneDTO applicazioneDTO = new PutApplicazioneDTO(user);
		it.govpay.bd.model.Applicazione applicazione = new it.govpay.bd.model.Applicazione();
		it.govpay.bd.model.Utenza utenza = new it.govpay.bd.model.Utenza();
		utenza.setAbilitato(applicazionePost.getAbilitato());
		utenza.setPrincipal(applicazionePost.getPrincipal());
		utenza.setPrincipalOriginale(applicazionePost.getPrincipal());
		utenza.setPassword(applicazionePost.getPassword());
		applicazione.setUtenza(new UtenzaApplicazione(utenza, idA2A));
		applicazioneDTO.setIdUtenza(applicazionePost.getPrincipal());

		boolean appTrusted = false;
		boolean appAuthTipiPendenzaAll = false;
		boolean appAuthDominiAll = false;

		if(applicazionePost.getTipiPendenza() != null) {
			List<String> idTipiVersamento = new ArrayList<>();

			for (String id : applicazionePost.getTipiPendenza()) {
				if(id.equals(ApplicazioniController.AUTODETERMINAZIONE_TIPI_PENDENZA_VALUE)) {
					appTrusted = true;
				} else if(id.equals(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR)) {
					appAuthTipiPendenzaAll = true;
				} else{
					idTipiVersamento.add(id);
				}
			}

			if(appAuthTipiPendenzaAll || appTrusted) {
				List<String> tipiVersamentoAutorizzati = AuthorizationManager.getTipiVersamentoAutorizzati(user);

				if(tipiVersamentoAutorizzati == null)
					throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);

				if(tipiVersamentoAutorizzati.size() > 0) {
					throw AuthorizationManager.toNotAuthorizedException(user, "l'utenza non e' associata a tutti i tipi pendenza, non puo' dunque autorizzare l'applicazione a tutti i tipi pendenza o abilitare l'autodeterminazione dei tipi pendenza");
				}

				applicazioneDTO.setCodTipiVersamento(new ArrayList<>());
			} else {
				applicazioneDTO.setCodTipiVersamento(idTipiVersamento);
			}
		}

		applicazione.setTrusted(appTrusted);
		applicazione.getUtenza().setAutorizzazioneTipiVersamentoStar(appAuthTipiPendenzaAll);

		if(applicazionePost.getDomini() != null) {
			List<it.govpay.core.beans.commons.Dominio> domini = new ArrayList<>();

			if(applicazionePost.getDomini() != null && !applicazionePost.getDomini().isEmpty()) {
				for (Object object : applicazionePost.getDomini()) {
					if(object instanceof String) {
						String idDominio = (String) object;
						if(idDominio.equals(ApplicazioniController.AUTORIZZA_DOMINI_STAR)) {
							List<String> dominiAutorizzati = AuthorizationManager.getDominiAutorizzati(user);

							if(dominiAutorizzati == null)
								throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);

							if(dominiAutorizzati.size() > 0) {
								throw AuthorizationManager.toNotAuthorizedException(user, "l'utenza non e' associata a tutti gli enti creditori, non puo' dunque autorizzare l'applicazione a tutti gli enti creditori");
							}

							appAuthDominiAll = true;
							domini.clear();
							break;
						}
						domini.add(DominiConverter.getDominioCommons(idDominio));
					} else if(object instanceof DominioProfiloPost) {
						DominioProfiloPost dominioProfiloPost = (DominioProfiloPost) object;
						if(dominioProfiloPost.getIdDominio().equals(ApplicazioniController.AUTORIZZA_DOMINI_STAR)) {
							List<String> dominiAutorizzati = AuthorizationManager.getDominiAutorizzati(user);

							if(dominiAutorizzati == null)
								throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);

							if(dominiAutorizzati.size() > 0) {
								throw AuthorizationManager.toNotAuthorizedException(user, "l'utenza non e' associata a tutti gli enti creditori, non puo' dunque autorizzare l'applicazione a tutti gli enti creditori");
							}

							appAuthDominiAll = true;
							domini.clear();
							break;
						}
						domini.add(DominiConverter.getDominioCommons(dominioProfiloPost));
					} else if(object instanceof java.util.LinkedHashMap) {
						java.util.LinkedHashMap<?,?> map = (LinkedHashMap<?,?>) object;

						DominioProfiloPost dominioProfiloPost = new DominioProfiloPost();
						if(map.containsKey("idDominio"))
							dominioProfiloPost.setIdDominio((String) map.get("idDominio"));
						if(map.containsKey("unitaOperative")) {
							@SuppressWarnings("unchecked")
							List<String> unitaOperative = (List<String>) map.get("unitaOperative");
							dominioProfiloPost.setUnitaOperative(unitaOperative);
						}

						if(dominioProfiloPost.getIdDominio() != null && dominioProfiloPost.getIdDominio().equals(ApplicazioniController.AUTORIZZA_DOMINI_STAR)) {
							List<String> dominiAutorizzati = AuthorizationManager.getDominiAutorizzati(user);

							if(dominiAutorizzati == null)
								throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);

							if(dominiAutorizzati.size() > 0) {
								throw AuthorizationManager.toNotAuthorizedException(user, "l'utenza non e' associata a tutti gli enti creditori, non puo' dunque autorizzare l'applicazione a tutti gli enti creditori");
							}

							appAuthDominiAll = true;
							domini.clear();
							break;
						}
						domini.add(DominiConverter.getDominioCommons(dominioProfiloPost));
					}
				}
			}

			applicazioneDTO.setDomini(domini);
		}
		applicazione.getUtenza().setAutorizzazioneDominiStar(appAuthDominiAll);

		if(applicazionePost.getCodificaAvvisi() != null) {
			applicazione.setCodApplicazioneIuv(applicazionePost.getCodificaAvvisi().getCodificaIuv());
			applicazione.setRegExp(applicazionePost.getCodificaAvvisi().getRegExpIuv());
			applicazione.setAutoIuv(applicazionePost.getCodificaAvvisi().isGenerazioneIuvInterna());
		}

		applicazione.setCodApplicazione(idA2A);
		applicazione.setFirmaRichiesta(FirmaRichiesta.NESSUNA);

		if(applicazionePost.getServizioIntegrazione() != null)
			applicazione.setConnettoreIntegrazione(ConnettoriConverter.getConnettore(applicazionePost.getServizioIntegrazione()));

		applicazioneDTO.setApplicazione(applicazione);
		applicazioneDTO.setIdApplicazione(idA2A);

		// ACL Backoffice
		List<Acl> aclPrincipal = new ArrayList<>();
		if(applicazionePost.getAcl()!=null) {
			for(AclPost aclPost: applicazionePost.getAcl()) {
				Acl acl = AclConverter.getAclUtenza(aclPost, user);
				acl.setUtenza(applicazione.getUtenza());
				aclPrincipal.add(acl);
			}
		}

		if(applicazionePost.getApiPagamenti()) {
			Acl acl = AclConverter.getAclAPI(Servizio.API_PAGAMENTI, user);
			acl.setUtenza(applicazione.getUtenza());
			aclPrincipal.add(acl);
		}

		if(applicazionePost.getApiPendenze()) {
			Acl acl = AclConverter.getAclAPI(Servizio.API_PENDENZE, user);
			acl.setUtenza(applicazione.getUtenza());
			aclPrincipal.add(acl);
		}

		if(applicazionePost.getApiRagioneria()) {
			Acl acl = AclConverter.getAclAPI(Servizio.API_RAGIONERIA, user);
			acl.setUtenza(applicazione.getUtenza());
			aclPrincipal.add(acl);
		}


		applicazione.getUtenza().setAclPrincipal(aclPrincipal);

		if(applicazionePost.getRuoli() != null ) {
			applicazione.getUtenza().setRuoli(applicazionePost.getRuoli());
		}

		return applicazioneDTO;
	}

	public static Applicazione toRsModel(it.govpay.bd.model.Applicazione applicazione) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		Applicazione rsModel = new Applicazione();
		rsModel.setAbilitato(applicazione.getUtenza().isAbilitato());
		rsModel.setPassword(StringUtils.isNotEmpty(applicazione.getUtenza().getPassword()));

		if(!(StringUtils.isEmpty(applicazione.getRegExp()) && StringUtils.isEmpty(applicazione.getCodApplicazioneIuv()))) {
			CodificaAvvisi codificaAvvisi = new CodificaAvvisi();
			codificaAvvisi.setCodificaIuv(applicazione.getCodApplicazioneIuv());
			codificaAvvisi.setRegExpIuv(applicazione.getRegExp());
			codificaAvvisi.setGenerazioneIuvInterna(applicazione.isAutoIuv());
			rsModel.setCodificaAvvisi(codificaAvvisi);
		} else {
			if(applicazione.isAutoIuv()) {
				CodificaAvvisi codificaAvvisi = new CodificaAvvisi();
				codificaAvvisi.setGenerazioneIuvInterna(applicazione.isAutoIuv());
				rsModel.setCodificaAvvisi(codificaAvvisi);
			}
		}

		rsModel.setIdA2A(applicazione.getCodApplicazione());
		rsModel.setPrincipal(applicazione.getUtenza().getPrincipalOriginale());

		if(applicazione.getConnettoreIntegrazione()!=null)
			rsModel.setServizioIntegrazione(ConnettoriConverter.toRsModel(applicazione.getConnettoreIntegrazione()));


		List<DominioProfiloIndex> idDomini = new ArrayList<>();
		if(applicazione.getUtenza().isAutorizzazioneDominiStar()) {
			DominioProfiloIndex tuttiDomini = new DominioProfiloIndex();
			tuttiDomini.setIdDominio(ApplicazioniController.AUTORIZZA_DOMINI_STAR);
			tuttiDomini.setRagioneSociale(ApplicazioniController.AUTORIZZA_DOMINI_STAR_LABEL);
			idDomini.add(tuttiDomini);
		} else if(applicazione.getUtenza().getDominiUo() != null) {
			List<it.govpay.core.beans.commons.Dominio> domini = UtentiDAO.convertIdUnitaOperativeToDomini(applicazione.getUtenza().getDominiUo());

			for (it.govpay.core.beans.commons.Dominio dominio : domini) {
				idDomini.add(DominiConverter.toRsModelProfiloIndex(dominio));
			}
		}

		rsModel.setDomini(idDomini);

		List<TipoPendenza> idTipiPendenza = new ArrayList<>();
		List<TipoVersamento> tipiVersamento = applicazione.getUtenza().getTipiVersamento(configWrapper);
		if(tipiVersamento == null)
			tipiVersamento = new ArrayList<>();

		if(applicazione.isTrusted()) {
			TipoPendenza tPI = new TipoPendenza();
			tPI.setIdTipoPendenza(ApplicazioniController.AUTODETERMINAZIONE_TIPI_PENDENZA_VALUE);
			tPI.setDescrizione(ApplicazioniController.AUTODETERMINAZIONE_TIPI_PENDENZA_LABEL);
			idTipiPendenza.add(tPI);
		}

		if(applicazione.getUtenza().isAutorizzazioneTipiVersamentoStar()) {
			TipoPendenza tPI = new TipoPendenza();
			tPI.setIdTipoPendenza(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR);
			tPI.setDescrizione(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR_LABEL);
			idTipiPendenza.add(tPI);
		}

		if(!applicazione.isTrusted() && !applicazione.getUtenza().isAutorizzazioneTipiVersamentoStar()) {
			for (TipoVersamento tipoVersamento : tipiVersamento) {
				TipoPendenza tPI = new TipoPendenza();
				tPI.setIdTipoPendenza(tipoVersamento.getCodTipoVersamento());
				tPI.setDescrizione(tipoVersamento.getDescrizione());
				idTipiPendenza.add(tPI);
			}
		}

		rsModel.setTipiPendenza(idTipiPendenza);

		rsModel.apiPagamenti(false);
		rsModel.apiPendenze(false);
		rsModel.apiRagioneria(false);

		List<Acl> acls = applicazione.getUtenza().getAclsNoRuoli();
		if(acls!=null) {
			List<AclPost> aclList = new ArrayList<>();

			for(Acl acl: acls) {
				AclPost aclRsModel = AclConverter.toRsModel(acl);
				if(aclRsModel != null)
					aclList.add(aclRsModel);
			}

			rsModel.setAcl(aclList);

			// ACL sulle API
			for(Acl acl: acls) {
				if(acl.getServizio() != null && acl.getServizio().equals(Servizio.API_PAGAMENTI)) {
					rsModel.apiPagamenti(true);
					break;
				}
			}

			for(Acl acl: acls) {
				if(acl.getServizio() != null && acl.getServizio().equals(Servizio.API_PENDENZE)) {
					rsModel.apiPendenze(true);
					break;
				}
			}

			for(Acl acl: acls) {
				if(acl.getServizio() != null && acl.getServizio().equals(Servizio.API_RAGIONERIA)) {
					rsModel.apiRagioneria(true);
					break;
				}
			}
		}

		if(applicazione.getUtenza().getRuoli() != null && applicazione.getUtenza().getRuoli().size() > 0) {
			List<Ruolo> ruoli = new ArrayList<>();

			for (String idRuolo : applicazione.getUtenza().getRuoli()) {
				ruoli.add(RuoliConverter.toRsModel(idRuolo, applicazione.getUtenza().getRuoliUtenza().get(idRuolo)));
			}

			rsModel.setRuoli(ruoli);
		}

		return rsModel;
	}

	public static ApplicazioneIndex toRsModelIndex(it.govpay.bd.model.Applicazione applicazione) throws ServiceException {
		ApplicazioneIndex rsModel = new ApplicazioneIndex();
		rsModel.setAbilitato(applicazione.getUtenza().isAbilitato());

		rsModel.setIdA2A(applicazione.getCodApplicazione());
		rsModel.setPrincipal(applicazione.getUtenza().getPrincipalOriginale());

		return rsModel;
	}
}
