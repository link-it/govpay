package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.AclPost;
import it.govpay.backoffice.v1.beans.DominioProfiloIndex;
import it.govpay.backoffice.v1.beans.DominioProfiloPost;
import it.govpay.backoffice.v1.beans.Operatore;
import it.govpay.backoffice.v1.beans.OperatoreIndex;
import it.govpay.backoffice.v1.beans.OperatorePost;
import it.govpay.backoffice.v1.beans.Ruolo;
import it.govpay.backoffice.v1.beans.TipoPendenza;
import it.govpay.backoffice.v1.controllers.ApplicazioniController;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Acl;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.core.dao.anagrafica.dto.PutOperatoreDTO;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.model.TipoVersamento;
import it.govpay.model.exception.CodificaInesistenteException;

public class OperatoriConverter {

	public static PutOperatoreDTO getPutOperatoreDTO(OperatorePost operatoreRequest, String principal,	Authentication user) throws NotAuthorizedException, CodificaInesistenteException{
		PutOperatoreDTO putOperatoreDTO = new PutOperatoreDTO(user);

		it.govpay.bd.model.Operatore operatore = new it.govpay.bd.model.Operatore();
		it.govpay.bd.model.Utenza utenza = new it.govpay.bd.model.Utenza();
		utenza.setAbilitato(operatoreRequest.getAbilitato());
		utenza.setPrincipal(principal);
		utenza.setPrincipalOriginale(principal);
		utenza.setPassword(operatoreRequest.getPassword());

		if(operatoreRequest.getAcl()!=null) {
			List<Acl> aclList = new ArrayList<>();
			for(AclPost acls: operatoreRequest.getAcl()) {
				aclList.add(AclConverter.getAclUtenza(acls, utenza));
			}
			utenza.setAclPrincipal(aclList);
		}
		operatore.setUtenza(utenza);
		operatore.setNome(operatoreRequest.getRagioneSociale());

		boolean appAuthTipiPendenzaAll = false;
		boolean appAuthDominiAll = false;

		if(operatoreRequest.getTipiPendenza() != null) {
			List<String> idTipiVersamento = new ArrayList<>();

			for (String id : operatoreRequest.getTipiPendenza()) {
				if(id.equals(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR)) {

					List<String> tipiVersamentoAutorizzati = AuthorizationManager.getTipiVersamentoAutorizzati(user);

					if(tipiVersamentoAutorizzati == null)
						throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);

					if(tipiVersamentoAutorizzati.size() > 0) {
						throw AuthorizationManager.toNotAuthorizedException(user, "l'utenza non e' associata a tutti i tipi pendenza, non puo' dunque autorizzare l'operatore a tutti i tipi pendenza");
					}


					appAuthTipiPendenzaAll = true;
					idTipiVersamento.clear();
					break;
				}

				idTipiVersamento.add(id.toString());
			}

			putOperatoreDTO.setCodTipiVersamento(idTipiVersamento);
		}

		operatore.getUtenza().setAutorizzazioneTipiVersamentoStar(appAuthTipiPendenzaAll);

		if(operatoreRequest.getDomini() != null) {
			List<it.govpay.core.beans.commons.Dominio> domini = new ArrayList<>();

			if(operatoreRequest.getDomini() != null && !operatoreRequest.getDomini().isEmpty()) {
				for (Object object : operatoreRequest.getDomini()) {
					if(object instanceof String) {
						String idDominio = (String) object;
						if(idDominio.equals(ApplicazioniController.AUTORIZZA_DOMINI_STAR)) {
							List<String> dominiAutorizzati = AuthorizationManager.getDominiAutorizzati(user);

							if(dominiAutorizzati == null)
								throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);

							if(dominiAutorizzati.size() > 0) {
								throw AuthorizationManager.toNotAuthorizedException(user, "l'utenza non e' associata a tutti gli enti creditori, non puo' dunque autorizzare l'operatore a tutti gli enti creditori");
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
								throw AuthorizationManager.toNotAuthorizedException(user, "l'utenza non e' associata a tutti gli enti creditori, non puo' dunque autorizzare l'operatore a tutti gli enti creditori");
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
								throw AuthorizationManager.toNotAuthorizedException(user, "l'utenza non e' associata a tutti gli enti creditori, non puo' dunque autorizzare l'operatore a tutti gli enti creditori");
							}

							appAuthDominiAll = true;
							domini.clear();
							break;
						}
						domini.add(DominiConverter.getDominioCommons(dominioProfiloPost));
					}
				}
			}

			putOperatoreDTO.setDomini(domini);
		}
		operatore.getUtenza().setAutorizzazioneDominiStar(appAuthDominiAll);

		if(operatoreRequest.getRuoli() != null ) {
			operatore.getUtenza().setRuoli(operatoreRequest.getRuoli());
		}

		putOperatoreDTO.setPrincipal(principal);
		putOperatoreDTO.setOperatore(operatore);

		return putOperatoreDTO;
	}

	public static OperatoreIndex toRsModelIndex(it.govpay.bd.model.Operatore operatore) {
		OperatoreIndex rsModel = new OperatoreIndex();
		rsModel.abilitato(operatore.getUtenza().isAbilitato())
		.principal(operatore.getUtenza().getPrincipalOriginale())
		.ragioneSociale(operatore.getNome());

		return rsModel;
	}


	public static Operatore toRsModel(it.govpay.bd.model.Operatore operatore) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		Operatore rsModel = new Operatore();
		rsModel.abilitato(operatore.getUtenza().isAbilitato())
		.principal(operatore.getUtenza().getPrincipalOriginale())
		.ragioneSociale(operatore.getNome());

		rsModel.setPassword(StringUtils.isNotEmpty(operatore.getUtenza().getPassword()));

		List<DominioProfiloIndex> idDomini = new ArrayList<>();
		if(operatore.getUtenza().isAutorizzazioneDominiStar()) {
			DominioProfiloIndex tuttiDomini = new DominioProfiloIndex();
			tuttiDomini.setIdDominio(ApplicazioniController.AUTORIZZA_DOMINI_STAR);
			tuttiDomini.setRagioneSociale(ApplicazioniController.AUTORIZZA_DOMINI_STAR_LABEL);
			idDomini.add(tuttiDomini);
		} else if(operatore.getUtenza().getDominiUo() != null) {
			List<it.govpay.core.beans.commons.Dominio> domini = UtentiDAO.convertIdUnitaOperativeToDomini(operatore.getUtenza().getDominiUo());

			for (it.govpay.core.beans.commons.Dominio dominio : domini) {
				idDomini.add(DominiConverter.toRsModelProfiloIndex(dominio));
			}
		}

		rsModel.setDomini(idDomini);

		List<TipoPendenza> idTipiPendenza = new ArrayList<>();
		List<TipoVersamento> tipiVersamento = operatore.getUtenza().getTipiVersamento(configWrapper);
		if(tipiVersamento == null)
			tipiVersamento = new ArrayList<>();

		if(operatore.getUtenza().isAutorizzazioneTipiVersamentoStar()) {
			TipoPendenza tPI = new TipoPendenza();
			tPI.setIdTipoPendenza(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR);
			tPI.setDescrizione(ApplicazioniController.AUTORIZZA_TIPI_PENDENZA_STAR_LABEL);
			idTipiPendenza.add(tPI);
		} else {
			for (TipoVersamento tipoVersamento : tipiVersamento) {
				TipoPendenza tPI = new TipoPendenza();
				tPI.setIdTipoPendenza(tipoVersamento.getCodTipoVersamento());
				tPI.setDescrizione(tipoVersamento.getDescrizione());
				idTipiPendenza.add(tPI);
			}
		}

		rsModel.setTipiPendenza(idTipiPendenza);

		List<Acl> acls = operatore.getUtenza().getAclsNoRuoli();
		if(acls!=null) {
			List<AclPost> aclList = new ArrayList<>();

			for(Acl acl: acls) {
				AclPost aclRsModel = AclConverter.toRsModel(acl);
				if(aclRsModel != null)
					aclList.add(aclRsModel);
			}

			rsModel.setAcl(aclList);
		}

		if(operatore.getUtenza().getRuoli() != null && operatore.getUtenza().getRuoli().size() > 0) {
			List<Ruolo> ruoli = new ArrayList<>();

			for (String idRuolo : operatore.getUtenza().getRuoli()) {
				ruoli.add(RuoliConverter.toRsModel(idRuolo, operatore.getUtenza().getRuoliUtenza().get(idRuolo)));
			}

			rsModel.setRuoli(ruoli);
		}

		return rsModel;
	}
}

