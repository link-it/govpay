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
package it.govpay.core.dao.pagamenti;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.logger.beans.Property;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import it.gov.pagopa.checkout.model.CartRequest;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Canale;
import it.govpay.bd.model.Configurazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.PagamentoPortale.CODICE_STATO;
import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Stazione;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.PagamentiPortaleBD;
import it.govpay.bd.pagamento.filters.PagamentoPortaleFilter;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.EventoContext;
import it.govpay.core.beans.EventoContext.Componente;
import it.govpay.core.beans.GpAvviaTransazionePagamentoResponse;
import it.govpay.core.beans.GpResponse;
import it.govpay.core.beans.Mittente;
import it.govpay.core.beans.tracciati.PendenzaPost;
import it.govpay.core.dao.anagrafica.utils.UtenzaPatchUtils;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.eventi.EventiDAO;
import it.govpay.core.dao.eventi.dto.ListaEventiDTO;
import it.govpay.core.dao.eventi.dto.ListaEventiDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPagamentiPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO.RefVersamentoAvviso;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO.RefVersamentoModello4;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO.RefVersamentoPendenza;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PagamentoPatchDTO;
import it.govpay.core.dao.pagamenti.exception.PagamentoPortaleNonTrovatoException;
import it.govpay.core.exceptions.EcException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.GovPayException.FaultBean;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.CheckoutUtils;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.LogUtils;
import it.govpay.core.utils.SeveritaProperties;
import it.govpay.core.utils.TracciatiConverter;
import it.govpay.core.utils.UrlUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.client.CheckoutClient;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.client.exception.ClientInitializeException;
import it.govpay.core.utils.tracciati.validator.PendenzaPostValidator;
import it.govpay.model.Anagrafica;
import it.govpay.model.PatchOp;
import it.govpay.model.Stazione.Versione;
import it.govpay.model.TipoVersamento;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.model.Versamento.TipologiaTipoVersamento;
import it.govpay.model.configurazione.Giornale;
import it.govpay.model.exception.CodificaInesistenteException;
import it.govpay.orm.IdVersamento;

public class PagamentiPortaleDAO extends BaseDAO {

	public static final String PATH_NOTA = PendenzeDAO.PATH_NOTA;
	public static final String PATH_VERIFICATO = "/verificato";

	public PagamentiPortaleDAO() {
		super();
	}

	public PagamentiPortaleDTOResponse inserisciPagamenti(PagamentiPortaleDTO pagamentiPortaleDTO) 
			throws IOException, CodificaInesistenteException, GovPayException, NotAuthorizedException, ServiceException, UtilsException, ValidationException, EcException, UnprocessableEntityException { 
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		PagamentiPortaleDTOResponse response  = new PagamentiPortaleDTOResponse();
		GpAvviaTransazionePagamentoResponse transazioneResponse = new GpAvviaTransazionePagamentoResponse();
		Logger log = LoggerWrapperFactory.getLogger(WebControllerDAO.class);

		IContext ctx = ContextThreadLocal.get();

		if(ctx == null)
			throw new GovPayException(EsitoOperazione.INTERNAL, "Inizializzazione contesto fallita.");

		GpContext appContext = (GpContext) ctx.getApplicationContext();

		if(ctx == null || appContext==null || appContext.getPagamentoCtx() == null || appContext.getRequest()==null)
			throw new GovPayException(EsitoOperazione.INTERNAL, "Inizializzazione contesto fallita.");

		PagamentiPortaleBD pagamentiPortaleBD = null;
		((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPagamento(pagamentiPortaleDTO.getIdSessione());
		try {
			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(pagamentiPortaleDTO.getUser());
			List<Versamento> versamenti = new ArrayList<>();

			// Aggiungo il codSessionePortale al PaymentContext
			appContext.getPagamentoCtx().setCodSessionePortale(pagamentiPortaleDTO.getIdSessionePortale());
			appContext.getRequest().addGenericProperty(new Property("codSessionePortale", pagamentiPortaleDTO.getIdSessionePortale() != null ? pagamentiPortaleDTO.getIdSessionePortale() : "--Non fornito--"));

			ctx.getApplicationLogger().log("ws.ricevutaRichiesta");
			ctx.getApplicationLogger().log("ws.autorizzazione");

			String nome = null;
			List<IdVersamento> idVersamento = new ArrayList<>();
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento();
			StringBuilder sbNomeVersamenti = new StringBuilder();
			List<String> listaMultibeneficiari = new ArrayList<>();
			Anagrafica versanteModel = VersamentoUtils.toAnagraficaModel(pagamentiPortaleDTO.getVersante());
			// 1. Lista Id_versamento
			for(int i = 0; i < pagamentiPortaleDTO.getPendenzeOrPendenzeRef().size(); i++) {
				Object v = pagamentiPortaleDTO.getPendenzeOrPendenzeRef().get(i);
				Versamento versamentoModel = null;
				if(v instanceof it.govpay.core.beans.commons.Versamento) {
					it.govpay.core.beans.commons.Versamento versamento = (it.govpay.core.beans.commons.Versamento) v;
					ctx.getApplicationLogger().log("rpt.acquisizioneVersamento", versamento.getCodApplicazione(), versamento.getCodVersamentoEnte());
					versamentoModel = versamentoBusiness.chiediVersamento(versamento);
					versamentoModel.setTipo(TipologiaTipoVersamento.SPONTANEO);

					// se l'utenza che ha caricato la pendenza inline e' un cittadino sono necessari dei controlli supplementari.
					if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {

						// se il tributo non puo' essere pagato da terzi allora debitore e versante (se presente) devono coincidere con chi sta effettuando il pagamento.
						if(!versamentoModel.getTipoVersamentoDominio(configWrapper).isPagaTerzi()) {
							if(!versamento.getDebitore().getCodUnivoco().equals(userDetails.getIdentificativo()))
								throw new GovPayException(EsitoOperazione.CIT_003, userDetails.getIdentificativo(),versamentoModel.getApplicazione(configWrapper).getCodApplicazione(), versamentoModel.getCodVersamentoEnte(),versamento.getDebitore().getCodUnivoco());

							if(versanteModel != null && !versanteModel.getCodUnivoco().equals(userDetails.getIdentificativo()))
								throw new GovPayException(EsitoOperazione.CIT_004, userDetails.getIdentificativo(),versamentoModel.getApplicazione(configWrapper).getCodApplicazione(), versamentoModel.getCodVersamentoEnte(),versanteModel.getCodUnivoco());
						}

					}
				}  else if(v instanceof RefVersamentoAvviso) {
					String idDominio = ((RefVersamentoAvviso)v).getIdDominio();
					String cfToCheck = ((RefVersamentoAvviso)v).getIdDebitore();
					try {
						Dominio dominio = AnagraficaManager.getDominio(configWrapper, idDominio);

						if(!dominio.isAbilitato())
							throw new GovPayException(EsitoOperazione.DOM_001, dominio.getCodDominio());

						versamentoModel = versamentoBusiness.chiediVersamento((RefVersamentoAvviso)v);

						// controllo che l'utenza anonima possa effettuare il pagamento dell'avviso	
						if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO)) {
							this.checkCFDebitoreVersamento(pagamentiPortaleDTO.getUser(), cfToCheck, versamentoModel.getAnagraficaDebitore().getCodUnivoco());
						}

					}catch(NotFoundException e) {
						throw new GovPayException("Il pagamento non puo' essere avviato poiche' uno dei versamenti risulta associato ad un dominio non disponibile [Dominio:"+idDominio+"].", EsitoOperazione.DOM_000, idDominio);
					}
				}  else if(v instanceof RefVersamentoPendenza) {
					// controllo se le pendenze richieste siano a disposizione in sessione altrimenti assumo che siano dei dovuti gia' caricati
					if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO) || userDetails.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO)) {
						String idA2A = ((RefVersamentoPendenza)v).getIdA2A();
						String idPendenza = ((RefVersamentoPendenza)v).getIdPendenza();

						if(pagamentiPortaleDTO.getListaPendenzeDaSessione() != null && pagamentiPortaleDTO.getListaPendenzeDaSessione().containsKey((idA2A+idPendenza))) {
							ctx.getApplicationLogger().log("rpt.acquisizioneVersamento", idA2A, idPendenza);
							versamentoModel = pagamentiPortaleDTO.getListaPendenzeDaSessione().get((idA2A+idPendenza));
							versamentoModel.setTipo(TipologiaTipoVersamento.SPONTANEO);

							// se l'utenza che ha caricato la pendenza inline e' un cittadino sono necessari dei controlli supplementari.
							if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
								// se il tributo non puo' essere pagato da terzi allora debitore e versante (se presente) devono coincidere con chi sta effettuando il pagamento.
								if(!versamentoModel.getTipoVersamentoDominio(configWrapper).isPagaTerzi()) {
									if(!versamentoModel.getAnagraficaDebitore().getCodUnivoco().equals(userDetails.getIdentificativo()))
										throw new GovPayException(EsitoOperazione.CIT_003, userDetails.getIdentificativo(),versamentoModel.getApplicazione(configWrapper).getCodApplicazione(), versamentoModel.getCodVersamentoEnte(),versamentoModel.getAnagraficaDebitore().getCodUnivoco());

									if(versanteModel != null && !versanteModel.getCodUnivoco().equals(userDetails.getIdentificativo()))
										throw new GovPayException(EsitoOperazione.CIT_004, userDetails.getIdentificativo(),versamentoModel.getApplicazione(configWrapper).getCodApplicazione(), versamentoModel.getCodVersamentoEnte(),versanteModel.getCodUnivoco());
								}

							}
							log.debug("RefVersamentoPendenza [idA2A:{}, idPendenza: {}] letto dalla lista identificativi in sessione", idA2A, idPendenza);
						} else {
							versamentoModel = versamentoBusiness.chiediVersamento((RefVersamentoPendenza)v);
						}
					} else {
						// applicazioni
						versamentoModel = versamentoBusiness.chiediVersamento((RefVersamentoPendenza)v);
					}
				} else if(v instanceof RefVersamentoModello4) {
					String idDominio = ((RefVersamentoModello4)v).getIdDominio();
					String idTipoVersamento = ((RefVersamentoModello4)v).getIdTipoPendenza();
					String dati = ((RefVersamentoModello4)v).getDati();

					Dominio dominio = null;
					try {
						dominio = AnagraficaManager.getDominio(configWrapper, idDominio);
					} catch (NotFoundException e1) {
						throw new GovPayException("Il pagamento non puo' essere avviato poiche' uno dei versamenti risulta associato ad un dominio non disponibile [Dominio:"+idDominio+"].", EsitoOperazione.DOM_000, idDominio);
					}

					if(!dominio.isAbilitato())
						throw new GovPayException(EsitoOperazione.DOM_001, dominio.getCodDominio());
					// lettura della configurazione TipoVersamentoDominio
					TipoVersamentoDominio tipoVersamentoDominio = null;
					try {
						tipoVersamentoDominio = AnagraficaManager.getTipoVersamentoDominio(configWrapper, dominio.getId(), idTipoVersamento);
					} catch (NotFoundException e1) {
						throw new GovPayException("Il pagamento non puo' essere avviato poiche' uno dei versamenti risulta associato ad un tipo pendenza ["+idTipoVersamento+"] non disponibilte per il dominio ["+idDominio+"].", EsitoOperazione.TVD_000, idDominio, idTipoVersamento);
					}

					VersamentoUtils.validazioneInputVersamentoModello4(this.log, dati, tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoValidazioneDefinizione());

					MultivaluedMap<String, String> queryParameters = pagamentiPortaleDTO.getQueryParameters(); 
					MultivaluedMap<String, String> pathParameters = pagamentiPortaleDTO.getPathParameters();
					Map<String, String> headers = pagamentiPortaleDTO.getHeaders();

					String idUO = null;
					UnitaOperativa uo = null;
					boolean trasformazione = false;
					String trasformazioneDefinizione = tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizione();
					String trasformazioneTipo = tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoTrasformazioneTipo();
					if(trasformazioneDefinizione != null && trasformazioneTipo != null) {
						dati = VersamentoUtils.trasformazioneInputVersamentoModello4(log, dominio, idTipoVersamento, trasformazioneTipo, uo, dati, queryParameters, pathParameters, headers, trasformazioneDefinizione);  
						trasformazione = true;
					}

					String codApplicazione = tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoCodApplicazione();
					if(codApplicazione != null) {
						versamentoModel = VersamentoUtils.inoltroInputVersamentoModello4(log, idDominio, idTipoVersamento, idUO, codApplicazione, dati);
					} else {
						try {
							PendenzaPost pendenzaPost = PendenzaPost.parse(dati);

							// imposto i dati idDominio, idTipoVersamento e idUnitaOperativa fornite nella URL di richiesta, sovrascrivendo eventuali valori impostati dalla trasformazione.
							pendenzaPost.setIdDominio(idDominio);
							pendenzaPost.setIdTipoPendenza(idTipoVersamento);
							pendenzaPost.setIdUnitaOperativa(idUO);

							new PendenzaPostValidator(pendenzaPost).validate();
							it.govpay.core.beans.commons.Versamento versamentoCommons = TracciatiConverter.getVersamentoFromPendenza(pendenzaPost);
							((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPendenza(versamentoCommons.getCodVersamentoEnte());
							((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdA2A(versamentoCommons.getCodApplicazione());
							versamentoModel = versamentoBusiness.chiediVersamento(versamentoCommons);
						}catch(ValidationException | IOException e) {
							if(trasformazione) { // se la pendenza generata dalla trasformazione non e' valida restituisco errore interno
								throw new GovPayException(EsitoOperazione.VAL_003, e.getMessage());
							} else {
								throw e;
							}
						}
					}
					versamentoModel.setTipo(TipologiaTipoVersamento.SPONTANEO);
				}

				if(versamentoModel != null) {

					Dominio dominio = versamentoModel.getDominio(configWrapper); 
					UnitaOperativa uo = versamentoModel.getUo(configWrapper); 
					TipoVersamento tipoVersamento = versamentoModel.getTipoVersamento(configWrapper); 
					log.debug("Verifica autorizzazione utenza [{}], tipo [{}] al pagamento del versamento [Id: {}, IdA2A: {}] per il dominio [{}], UO [{}], tipoPendenza [{}]...",
					          userDetails.getIdentificativo(),
					          userDetails.getTipoUtenza(),
					          versamentoModel.getCodVersamentoEnte(),
					          versamentoModel.getApplicazione(configWrapper).getCodApplicazione(),
					          dominio.getCodDominio(),
					          uo.getCodUo(),
					          tipoVersamento.getCodTipoVersamento());

					if(!AuthorizationManager.isTipoVersamentoUOAuthorized(userDetails.getUtenza(), dominio.getCodDominio(), uo.getCodUo(), tipoVersamento.getCodTipoVersamento())) {
						log.warn("Non autorizzato utenza [{}], tipo [{}] al pagamento del versamento [Id: {}, IdA2A: {}] per il dominio [{}], UO [{}], tipoPendenza [{}]",
						          userDetails.getIdentificativo(),
						          userDetails.getTipoUtenza(),
						          versamentoModel.getCodVersamentoEnte(),
						          versamentoModel.getApplicazione(configWrapper).getCodApplicazione(),
						          dominio.getCodDominio(),
						          uo.getCodUo(),
						          tipoVersamento.getCodTipoVersamento());
						throw new GovPayException(EsitoOperazione.APP_003, userDetails.getIdentificativo(), versamentoModel.getApplicazione(configWrapper).getCodApplicazione(), versamentoModel.getCodVersamentoEnte());
					}

					log.debug("Autorizzato utenza [{}], tipo [{}] al pagamento del versamento [Id: {}, IdA2A: {}] per il dominio [{}], UO [{}], tipoPendenza [{}]",
					          userDetails.getIdentificativo(),
					          userDetails.getTipoUtenza(),
					          versamentoModel.getCodVersamentoEnte(),
					          versamentoModel.getApplicazione(configWrapper).getCodApplicazione(),
					          dominio.getCodDominio(),
					          uo.getCodUo(),
					          tipoVersamento.getCodTipoVersamento());


					if(!uo.isAbilitato()) {
						throw new GovPayException("Il pagamento non puo' essere avviato poiche' uno dei versamenti risulta associato ad una unita' operativa disabilitata [Uo:"+uo.getCodUo()+"].", EsitoOperazione.UOP_001, uo.getCodUo());
					}

					if(!dominio.isAbilitato()) {
						throw new GovPayException("Il pagamento non puo' essere avviato poiche' uno dei versamenti risulta associato ad un dominio disabilitato [Dominio:"+dominio.getCodDominio()+"].", EsitoOperazione.DOM_001, dominio.getCodDominio());
					}


					if(i == 0) {
						// la prima pendenza da il nome al pagamento, eventualmente si appende il numero di pendenze ulteriori
						if(versamentoModel.getNome()!=null) {
							sbNomeVersamenti.append(versamentoModel.getNome());
						} else {
							try {
								sbNomeVersamenti.append(versamentoModel.getCausaleVersamento().getSimple());
							} catch (UnsupportedEncodingException e) {
								//donothing
							}						
						}
						if(pagamentiPortaleDTO.getPendenzeOrPendenzeRef().size() > 1) {
							int numeroAltre = pagamentiPortaleDTO.getPendenzeOrPendenzeRef().size() -1;
							if(numeroAltre == 1)
								sbNomeVersamenti.append(" ed un'altra pendenza.");
							else 
								sbNomeVersamenti.append(" ed altre "+ numeroAltre +" pendenze.");
						}
					}

					versamenti.add(versamentoModel);

					// colleziono i domini inserendo solo se non presente in lista
					if(!listaMultibeneficiari.contains(dominio.getCodDominio()))
						listaMultibeneficiari.add(dominio.getCodDominio());
				}
			}

			// Controllo se sono nel caso d'uso Pagamento Modello 1 SANP 3.1.0 cioe' se la stazione impostata per il dominio e' V2
			Stazione stazione = null;
			Dominio dominio = null;
			for (Versamento vTmp : versamenti) {
				Dominio dominioTmp = vTmp.getDominio(configWrapper);
				if(dominio == null)	{
					dominio = dominioTmp;
				}

				if(stazione == null) {
					stazione = dominioTmp.getStazione();
				} else {
					if(stazione.getId().compareTo(dominioTmp.getStazione().getId()) != 0) {
						throw new GovPayException(EsitoOperazione.PAG_000);
					}
				}
			}

			// il checkout si puo' utilizzare solo se la funzionalita' e' abilitata e la stazione e' V2
			if(GovpayConfig.getInstance().isCheckoutEnabled() && stazione != null ) {
				// check versione
				Versione versioneStazione = stazione.getVersione();
				if(versioneStazione.equals(Versione.V2)) {

					String codStazione = stazione.getCodStazione();
					log.debug("La stazione utilizzata [{}] ha versione [{}], invocazione verso il Checkout PagoPA...", codStazione, versioneStazione);
					String email = null;

					// urlritorno obbligatoria
					if(StringUtils.isEmpty(pagamentiPortaleDTO.getUrlRitorno())) {
						throw new ValidationException("Il campo urlRitorno non deve essere vuoto.");
					}
					
					// nella modalita' V1 le pendenze venivano aggiornate prima dell'invio RPT, qui forzo la generazione del numero avviso per quelle che non sono ancora inserite
					it.govpay.core.business.Versamento versamentiBusiness = new it.govpay.core.business.Versamento();
					log.debug("Controllo esistenza del numero avviso per tutte le pendenze in corso...");
					for(Versamento versamento : versamenti) {
						// Aggiorno tutti i versamenti che mi sono stati passati
						String codVersamentoEnte = versamento.getCodVersamentoEnte();
						String codApplicazione = versamento.getApplicazione(configWrapper).getCodApplicazione();
						boolean create = versamento.getId() == null;
						String msg = create ? "caricamento" : "aggiornamento";
						if(!create) {
							// lettura dei singoli versamenti
							versamento.getSingoliVersamenti(configWrapper);
						}
						log.debug("Pendenza [idA2A:{}, idPendenza: {}] {} in corso...", codApplicazione, codVersamentoEnte, msg);
						versamentiBusiness.caricaVersamento(versamento, true, true, false, null, pagamentiPortaleBD);
						log.debug("Pendenza [idA2A:{}, idPendenza: {}] {} e generazione del numero avviso (se previsto) completati: NAV: {}", codApplicazione, codVersamentoEnte, msg, versamento.getNumeroAvviso());
					}
					log.debug("Controllo esistenza del numero avviso per tutte le pendenze completato.");

					response.setId("0");
					response.setIdSessione("0");
					response.setIdSessionePsp("0");
					// indico se restituire un redirect o un json
					response.setRedirect(GovpayConfig.getInstance().isCheckoutResponseSendRedirectEnabled()); 

					String checkoutBaseUrl = GovpayConfig.getInstance().getCheckoutBaseURL();
					log.debug("Url Base Checkout: {}", checkoutBaseUrl);
					String operationId = appContext.setupAppIOClient(CheckoutClient.SWAGGER_OPERATION_POST_CARTS_OPERATION_ID, checkoutBaseUrl);

					// Esecuizione della chiamata verso PagoPA
					try {
						CartRequest cartRequest = CheckoutUtils.createCartRequest(log, configWrapper, pagamentiPortaleDTO.getUrlRitorno(), versamenti, pagamentiPortaleDTO.getCodiceConvenzione(), email);

						Configurazione configurazione = new it.govpay.core.business.Configurazione().getConfigurazione();
						Giornale giornale = configurazione.getGiornale();
						CheckoutClient checkoutClient = new CheckoutClient(CheckoutClient.SWAGGER_OPERATION_POST_CARTS_OPERATION_ID, checkoutBaseUrl, operationId, giornale, new EventoContext(Componente.API_PAGOPA));
						String location = checkoutClient.inviaCartRequest(cartRequest);

						log.debug("Stazione [{}] versione [{}], invocazione verso il Checkout PagoPA completata, ricevuta URL redirect [{}]", codStazione, versioneStazione, location);
						response.setLocation(location);
						response.setRedirectUrl(location);
					} catch (UnsupportedEncodingException e) {
						LogUtils.logError(log, "Errore nella decodifica della causale Versamento: " + e.getMessage(), e);
						throw new GovPayException(e);
					} catch (ClientException e) {
						LogUtils.logError(log, "Errore durante la spedizione della richiesta verso il Checkoout PagoPA: " + e.getMessage(), e);
						throw new GovPayException(e);
					} catch (ClientInitializeException e) {
						LogUtils.logError(log, "Errore durante la creazione del client per la spedizione della richiesta verso il Checkoout PagoPA: " + e.getMessage(), e);
						throw new GovPayException(e);
					}

					return response;
				}
			}

			// 5. somma degli importi delle pendenze
			BigDecimal sommaImporti = BigDecimal.ZERO;
			for (Versamento vTmp : versamenti) {
				sommaImporti = sommaImporti.add(vTmp.getImportoTotale());
			}

			nome = sbNomeVersamenti.length() > 255 ? (sbNomeVersamenti.substring(0, 252) + "...") : sbNomeVersamenti.toString();
			STATO stato = null;
			CODICE_STATO codiceStato = null;
			String redirectUrl = null;
			String idSessionePsp = null;
			String pspRedirect = null;
			String codCanale = null;		
			String codPsp = null;
			String tipoVersamento = null;
			String descrizioneStato = null;

			PagamentoPortale pagamentoPortale = new PagamentoPortale();
			pagamentoPortale.setPrincipal(userDetails.getIdentificativo()); 
			pagamentoPortale.setTipoUtenza(userDetails.getTipoUtenza());
			pagamentoPortale.setIdSessione(pagamentiPortaleDTO.getIdSessione());
			pagamentoPortale.setIdSessionePortale(pagamentiPortaleDTO.getIdSessionePortale());
			pagamentoPortale.setJsonRequest(pagamentiPortaleDTO.getJsonRichiesta());
			if(pagamentiPortaleDTO.getUrlRitorno() != null)
				pagamentoPortale.setUrlRitorno(UrlUtils.addParameter(pagamentiPortaleDTO.getUrlRitorno() , "idPagamento",pagamentiPortaleDTO.getIdSessione()));
			pagamentoPortale.setDataRichiesta(new Date());
			pagamentoPortale.setNome(nome);
			pagamentoPortale.setImporto(sommaImporti); 
			if(versanteModel != null)
				pagamentoPortale.setVersanteIdentificativo(versanteModel.getCodUnivoco());

			if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.APPLICAZIONE)) {
				pagamentoPortale.setIdApplicazione(userDetails.getApplicazione().getId());
			}

			// gestione multibeneficiari
			// se ho solo un dominio all'interno della lista allora vuol dire che i tutti pagamenti riferiscono lo stesso dominio
			// lascio null se il numero di domini e' > 1
			if(listaMultibeneficiari.size() == 1) {
				pagamentoPortale.setMultiBeneficiario(listaMultibeneficiari.get(0)); 
			}

			pagamentoPortale.setStato(STATO.IN_CORSO);
			pagamentoPortale.setCodiceStato(CODICE_STATO.PAGAMENTO_IN_CORSO_AL_PSP);
			pagamentoPortale.setTipo(1); //Pagamento iniziativa ente
			pagamentiPortaleBD = new PagamentiPortaleBD(configWrapper);
			pagamentiPortaleBD.insertPagamento(pagamentoPortale);

			// procedo al pagamento
			it.govpay.core.business.Rpt rptBD = new it.govpay.core.business.Rpt();
			List<Rpt> rpts = null;

			// sessione di pagamento non in corso
			codiceStato = CODICE_STATO.PAGAMENTO_IN_CORSO_AL_PSP;
			stato = STATO.IN_CORSO;

			try {
				Canale canale = Canale.canaleUniversale;

				if(pagamentiPortaleDTO.getIdentificativoPSP() != null && pagamentiPortaleDTO.getIdentificativoIntermediarioPSP() != null && pagamentiPortaleDTO.getIdentificativoCanale() != null) {
					canale = new Canale(pagamentiPortaleDTO.getIdentificativoIntermediarioPSP(), pagamentiPortaleDTO.getIdentificativoPSP(), pagamentiPortaleDTO.getIdentificativoCanale(), pagamentiPortaleDTO.getTipoVersamento(), null);
				}

				rpts = rptBD.avviaTransazione(versamenti, pagamentiPortaleDTO.getUser(), canale, pagamentiPortaleDTO.getIbanAddebito(), versanteModel, pagamentiPortaleDTO.getAutenticazioneSoggetto(),
						pagamentiPortaleDTO.getUrlRitorno(), true, pagamentoPortale, pagamentiPortaleDTO.getCodiceConvenzione());
				Rpt rpt = rpts.get(0);

				GpAvviaTransazionePagamentoResponse.RifTransazione rifTransazione = new GpAvviaTransazionePagamentoResponse.RifTransazione();
				rifTransazione.setCcp(rpt.getCcp());
				Versamento versamento = rpt.getVersamento();
				Applicazione applicazione = versamento.getApplicazione(configWrapper); 
				rifTransazione.setCodApplicazione(applicazione.getCodApplicazione());
				rifTransazione.setCodDominio(rpt.getCodDominio());
				rifTransazione.setCodVersamentoEnte(versamento.getCodVersamentoEnte());
				rifTransazione.setIuv(rpt.getIuv());
				transazioneResponse.getRifTransazione().add(rifTransazione);

				// se ho un redirect 			
				if(rpt.getPspRedirectURL() != null) {
					codiceStato = CODICE_STATO.PAGAMENTO_IN_CORSO_AL_PSP;
					stato = STATO.IN_CORSO;
					idSessionePsp = rpt.getCodSessione();
					if(pagamentoPortale.getUrlRitorno()!= null)
						pagamentoPortale.setUrlRitorno(UrlUtils.addParameter(pagamentoPortale.getUrlRitorno() , "idSession", idSessionePsp));
					pspRedirect = rpt.getPspRedirectURL(); 
					response.setRedirectUrl(pspRedirect);
					response.setIdSessione(idSessionePsp); 
				} else {
					stato = STATO.IN_CORSO;
					codiceStato = CODICE_STATO.PAGAMENTO_IN_ATTESA_DI_ESITO;
					response.setRedirectUrl(pagamentiPortaleDTO.getUrlRitorno());
				}

				transazioneResponse.setPspSessionId(idSessionePsp);
				transazioneResponse.setUrlRedirect(redirectUrl);
				transazioneResponse.setCodEsito(EsitoOperazione.OK.toString());
				transazioneResponse.setDescrizioneEsito("Operazione completata con successo");
				transazioneResponse.setMittente(Mittente.GOV_PAY);

				response.setIdCarrelloRpt(rpt.getIdTransazioneRpt());
			}catch(GovPayException e) {
				transazioneResponse = (GpAvviaTransazionePagamentoResponse) this.getWsResponse(e, transazioneResponse, "ws.ricevutaRichiestaKo", log);
				for(Versamento versamentoModel: versamenti) {
					if(versamentoModel.getId() != null) {
						IdVersamento idV = new IdVersamento();
						idV.setCodVersamentoEnte(versamentoModel.getCodVersamentoEnte());
						idV.setId(versamentoModel.getId());
						idVersamento.add(idV);
					}
				}

				pagamentoPortale.setIdVersamento(idVersamento); 
				pagamentoPortale.setCodiceStato(CODICE_STATO.PAGAMENTO_FALLITO);
				pagamentoPortale.setStato(STATO.FALLITO);
				pagamentoPortale.setDescrizioneStato(e.getMessage());
				pagamentoPortale.setAck(false);
				this.impostaSeveritaErrore(pagamentoPortale, e);
				pagamentiPortaleBD.updatePagamento(pagamentoPortale, true, false);

				throw new GovPayException(e, pagamentoPortale);
			} catch (Exception e) {
				transazioneResponse = (GpAvviaTransazionePagamentoResponse) this.getWsResponse(new GovPayException(e), transazioneResponse, "ws.ricevutaRichiestaKo", log);
				for(Versamento versamentoModel: versamenti) {
					if(versamentoModel.getId() != null) {
						IdVersamento idV = new IdVersamento();
						idV.setCodVersamentoEnte(versamentoModel.getCodVersamentoEnte());
						idV.setId(versamentoModel.getId());
						idVersamento.add(idV);
					}
				}
				pagamentoPortale.setIdVersamento(idVersamento); 
				pagamentoPortale.setCodiceStato(CODICE_STATO.PAGAMENTO_FALLITO);
				pagamentoPortale.setStato(STATO.FALLITO);
				pagamentoPortale.setDescrizioneStato(e.getMessage());
				pagamentoPortale.setAck(false);
				this.impostaSeveritaErrore(pagamentoPortale, e);
				pagamentiPortaleBD.updatePagamento(pagamentoPortale, true, false);
				throw e;
			}

			for(Versamento versamentoModel: versamenti) {
				if(versamentoModel.getId() != null) {
					IdVersamento idV = new IdVersamento();
					idV.setCodVersamentoEnte(versamentoModel.getCodVersamentoEnte());
					idV.setId(versamentoModel.getId());
					idVersamento.add(idV);
				}
			}
			pagamentoPortale.setIdVersamento(idVersamento); 
			pagamentoPortale.setIdSessionePsp(idSessionePsp);
			pagamentoPortale.setPspRedirectUrl(pspRedirect);
			pagamentoPortale.setCodiceStato(codiceStato);
			pagamentoPortale.setStato(stato);
			pagamentoPortale.setDescrizioneStato(descrizioneStato);
			pagamentoPortale.setCodPsp(codPsp);
			pagamentoPortale.setTipoVersamento(tipoVersamento);
			pagamentoPortale.setCodCanale(codCanale); 
			pagamentiPortaleBD.updatePagamento(pagamentoPortale, true, false); //inserisce anche i versamenti
			response.setId(pagamentoPortale.getIdSessione());
			response.setIdSessionePsp(pagamentoPortale.getIdSessionePsp());

			return response;
		}finally {
			if(ctx != null) {
				GpContext.setResult(ctx.getApplicationContext().getTransaction(), transazioneResponse);
			}
			if(pagamentiPortaleBD != null)
				pagamentiPortaleBD.closeConnection();
		}
	}

	private void impostaSeveritaErrore(PagamentoPortale pagamentoPortale, Exception e) {
		if(e instanceof GovPayException) {
			pagamentoPortale.setSeverita(SeveritaProperties.getInstance().getSeverita(((GovPayException) e).getCodEsito()));
		} else {
			pagamentoPortale.setSeverita(SeveritaProperties.getInstance().getSeverita(new GovPayException(e).getCodEsito()));
		}

	}

	public LeggiPagamentoPortaleDTOResponse leggiPagamentoPortale(LeggiPagamentoPortaleDTO leggiPagamentoPortaleDTO) throws ServiceException,PagamentoPortaleNonTrovatoException, ValidationException{
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		LeggiPagamentoPortaleDTOResponse leggiPagamentoPortaleDTOResponse = new LeggiPagamentoPortaleDTOResponse();
		PagamentiPortaleBD pagamentiPortaleBD = null;

		try {
			GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(leggiPagamentoPortaleDTO.getUser());

			pagamentiPortaleBD = new PagamentiPortaleBD(configWrapper);

			pagamentiPortaleBD.setupConnection(configWrapper.getTransactionID());

			pagamentiPortaleBD.setAtomica(false);

			PagamentoPortale pagamentoPortale = null;
			if(leggiPagamentoPortaleDTO.getId() != null) { 
				((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPagamento(leggiPagamentoPortaleDTO.getId());
				pagamentoPortale = pagamentiPortaleBD.getPagamentoFromCodSessione(leggiPagamentoPortaleDTO.getId());
			}else {
				pagamentoPortale = pagamentiPortaleBD.getPagamentoFromCodSessionePsp(leggiPagamentoPortaleDTO.getIdSessionePsp());
				((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPagamento(pagamentoPortale.getIdSessione());
			}



			pagamentoPortale.getApplicazione(configWrapper);

			List<Versamento> versamenti = pagamentoPortale.getVersamenti(pagamentiPortaleBD);

			if(versamenti != null && !versamenti.isEmpty()) {
				for(Versamento versamento: versamenti) {
					versamento.getDominio(configWrapper);
					versamento.getSingoliVersamenti(pagamentiPortaleBD);
				}
			}
			leggiPagamentoPortaleDTOResponse.setPagamento(pagamentoPortale); 

			if(leggiPagamentoPortaleDTO.isRisolviLink()) {
				PendenzeDAO pendenzeDao = new PendenzeDAO();
				ListaPendenzeDTO listaPendenzaDTO = new ListaPendenzeDTO(leggiPagamentoPortaleDTO.getUser());
				listaPendenzaDTO.setIdPagamento(pagamentoPortale.getIdSessione());
				listaPendenzaDTO.setIdDomini(leggiPagamentoPortaleDTO.getIdDomini());
				listaPendenzaDTO.setIdTipiVersamento(leggiPagamentoPortaleDTO.getIdTipiVersamento());
				it.govpay.bd.viste.VersamentiBD versamentiBD = new it.govpay.bd.viste.VersamentiBD(pagamentiPortaleBD);
				versamentiBD.setAtomica(false);
				ListaPendenzeDTOResponse listaPendenze = pendenzeDao.listaPendenze(listaPendenzaDTO, versamentiBD);
				leggiPagamentoPortaleDTOResponse.setListaPendenze(listaPendenze.getResults());

				RptDAO rptDao = new RptDAO(); 
				ListaRptDTO listaRptDTO = new ListaRptDTO(leggiPagamentoPortaleDTO.getUser());
				listaRptDTO.setIdPagamento(pagamentoPortale.getIdSessione());
				listaRptDTO.setIdDomini(leggiPagamentoPortaleDTO.getIdDomini());
				listaRptDTO.setIdTipiVersamento(leggiPagamentoPortaleDTO.getIdTipiVersamento());
				it.govpay.bd.viste.RptBD rptBD = new it.govpay.bd.viste.RptBD(pagamentiPortaleBD);
				rptBD.setAtomica(false);
				ListaRptDTOResponse listaRpt = rptDao.listaRpt(listaRptDTO, rptBD);
				leggiPagamentoPortaleDTOResponse.setListaRpp(listaRpt.getResults());
			}

			// giornale degli eventi disponibile solo per le utenze autenticate
			if(!details.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO) && !details.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
				EventiDAO eventiDAO = new EventiDAO();
				ListaEventiDTO listaEventiDTO = new ListaEventiDTO(leggiPagamentoPortaleDTO.getUser());
				listaEventiDTO.setIdPagamento(pagamentoPortale.getIdSessione());
				ListaEventiDTOResponse listaEventi = eventiDAO.listaEventi(listaEventiDTO);
				leggiPagamentoPortaleDTOResponse.setEventi(listaEventi.getResults());
			}

			return leggiPagamentoPortaleDTOResponse;
		}catch(NotFoundException e) {
			throw new PagamentoPortaleNonTrovatoException("Non esiste un pagamento associato all'ID ["+leggiPagamentoPortaleDTO.getId()+"]");
		}finally {
			if(pagamentiPortaleBD != null)
				pagamentiPortaleBD.closeConnection();
		}
	}

	public ListaPagamentiPortaleDTOResponse countPagamentiPortale(ListaPagamentiPortaleDTO listaPagamentiPortaleDTO) throws ServiceException, NotFoundException { 
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		PagamentiPortaleBD pagamentiPortaleBD = null;

		try {
			pagamentiPortaleBD = new PagamentiPortaleBD(configWrapper);
			PagamentoPortaleFilter filter = pagamentiPortaleBD.newFilter();

			filter.setIdDomini(listaPagamentiPortaleDTO.getIdDomini());
			filter.setIdTipiVersamento(listaPagamentiPortaleDTO.getIdTipiVersamento());		
			filter.setEseguiCountConLimit(listaPagamentiPortaleDTO.isEseguiCountConLimit());

			if(listaPagamentiPortaleDTO.getUnitaOperative() != null) {
				filter.setIdUo(listaPagamentiPortaleDTO.getUnitaOperative());
			}

			filter.setOffset(listaPagamentiPortaleDTO.getOffset());
			filter.setLimit(listaPagamentiPortaleDTO.getLimit());
			filter.setDataInizio(listaPagamentiPortaleDTO.getDataDa());
			filter.setDataFine(listaPagamentiPortaleDTO.getDataA());
			filter.setAck(listaPagamentiPortaleDTO.getVerificato());
			filter.setIdSessionePortale(listaPagamentiPortaleDTO.getIdSessionePortale()); 
			filter.setIdSessionePsp(listaPagamentiPortaleDTO.getIdSessionePsp());
			filter.setIdSessione(listaPagamentiPortaleDTO.getIdSessione());
			filter.setStato(listaPagamentiPortaleDTO.getStato());
			filter.setVersante(listaPagamentiPortaleDTO.getVersante());
			filter.setFilterSortList(listaPagamentiPortaleDTO.getFieldSortList());
			filter.setCfCittadino(listaPagamentiPortaleDTO.getCfCittadino()); 
			if(StringUtils.isNotBlank(listaPagamentiPortaleDTO.getCodApplicazione())) {
				Applicazione applicazione = AnagraficaManager.getApplicazione(configWrapper, listaPagamentiPortaleDTO.getCodApplicazione());
				filter.setIdApplicazione(applicazione.getId());
			}
			filter.setSeveritaDa(listaPagamentiPortaleDTO.getSeveritaDa());
			filter.setSeveritaA(listaPagamentiPortaleDTO.getSeveritaA());

			long count = pagamentiPortaleBD.count(filter);
			return new ListaPagamentiPortaleDTOResponse(count, new ArrayList<>());
		}finally {
			pagamentiPortaleBD.closeConnection();
		}
	}

	public ListaPagamentiPortaleDTOResponse listaPagamentiPortale(ListaPagamentiPortaleDTO listaPagamentiPortaleDTO) throws ServiceException, NotFoundException, ValidationException { 
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		PagamentiPortaleBD pagamentiPortaleBD = null;

		try {
			pagamentiPortaleBD = new PagamentiPortaleBD(configWrapper);
			PagamentoPortaleFilter filter = pagamentiPortaleBD.newFilter();

			filter.setIdDomini(listaPagamentiPortaleDTO.getIdDomini());
			filter.setIdTipiVersamento(listaPagamentiPortaleDTO.getIdTipiVersamento());
			filter.setEseguiCountConLimit(listaPagamentiPortaleDTO.isEseguiCountConLimit());

			if(listaPagamentiPortaleDTO.getUnitaOperative() != null) {
				filter.setIdUo(listaPagamentiPortaleDTO.getUnitaOperative());
			}

			filter.setOffset(listaPagamentiPortaleDTO.getOffset());
			filter.setLimit(listaPagamentiPortaleDTO.getLimit());
			filter.setDataInizio(listaPagamentiPortaleDTO.getDataDa());
			filter.setDataFine(listaPagamentiPortaleDTO.getDataA());
			filter.setAck(listaPagamentiPortaleDTO.getVerificato());
			filter.setIdSessionePortale(listaPagamentiPortaleDTO.getIdSessionePortale()); 
			filter.setIdSessionePsp(listaPagamentiPortaleDTO.getIdSessionePsp());
			filter.setIdSessione(listaPagamentiPortaleDTO.getIdSessione());
			filter.setStato(listaPagamentiPortaleDTO.getStato());
			filter.setVersante(listaPagamentiPortaleDTO.getVersante());
			filter.setFilterSortList(listaPagamentiPortaleDTO.getFieldSortList());
			filter.setCfCittadino(listaPagamentiPortaleDTO.getCfCittadino()); 
			filter.setIdDebitore(listaPagamentiPortaleDTO.getIdDebitore());
			if(listaPagamentiPortaleDTO.getIuv() != null) {
				if(listaPagamentiPortaleDTO.getIuv().length() == 18) {
					filter.setIuv(IuvUtils.toIuv(listaPagamentiPortaleDTO.getIuv()));
				} else {
					filter.setIuv(listaPagamentiPortaleDTO.getIuv());
				}
			}
			filter.setCodDominio(listaPagamentiPortaleDTO.getIdDominio() );
			filter.setCodApplicazione(listaPagamentiPortaleDTO.getIdA2A());
			filter.setCodVersamento(listaPagamentiPortaleDTO.getIdPendenza());

			if(StringUtils.isNotBlank(listaPagamentiPortaleDTO.getCodApplicazione())) {
				Applicazione applicazione = AnagraficaManager.getApplicazione(configWrapper, listaPagamentiPortaleDTO.getCodApplicazione());
				filter.setIdApplicazione(applicazione.getId());
			}

			filter.setSeveritaDa(listaPagamentiPortaleDTO.getSeveritaDa());
			filter.setSeveritaA(listaPagamentiPortaleDTO.getSeveritaA());

			Long count = null;

			if(listaPagamentiPortaleDTO.isEseguiCount()) {
				count = pagamentiPortaleBD.count(filter);
			}

			if(listaPagamentiPortaleDTO.isEseguiFindAll()) {
				List<LeggiPagamentoPortaleDTOResponse> lst = new ArrayList<>();
				List<PagamentoPortale> findAll = pagamentiPortaleBD.findAll(filter);

				for (PagamentoPortale pagamentoPortale : findAll) {
					LeggiPagamentoPortaleDTOResponse dto = new LeggiPagamentoPortaleDTOResponse();
					dto.setPagamento(pagamentoPortale);
					lst.add(dto);
				}

				return new ListaPagamentiPortaleDTOResponse(count, lst);
			} else {
				return new ListaPagamentiPortaleDTOResponse(count, new ArrayList<>());
			}
		}finally {
			pagamentiPortaleBD.closeConnection();
		}
	}

	public LeggiPagamentoPortaleDTOResponse patch(PagamentoPatchDTO patchDTO) 
			throws ServiceException, PagamentoPortaleNonTrovatoException, ValidationException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		LeggiPagamentoPortaleDTOResponse leggiPagamentoPortaleDTOResponse = new LeggiPagamentoPortaleDTOResponse();

		PagamentiPortaleBD pagamentiPortaleBD = null;

		try {
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPagamento(patchDTO.getIdSessione());
			pagamentiPortaleBD = new PagamentiPortaleBD(configWrapper);

			pagamentiPortaleBD.setupConnection(configWrapper.getTransactionID());

			pagamentiPortaleBD.setAtomica(false);

			PagamentoPortale pagamentoPortale = pagamentiPortaleBD.getPagamentoFromCodSessione(patchDTO.getIdSessione());

			for(Versamento versamento: pagamentoPortale.getVersamenti(pagamentiPortaleBD)) {
				versamento.getDominio(configWrapper);
				versamento.getSingoliVersamenti(pagamentiPortaleBD);
				versamento.getTipoVersamentoDominio(configWrapper);
				versamento.getTipoVersamento(configWrapper);
			}
			leggiPagamentoPortaleDTOResponse.setPagamento(pagamentoPortale); 

			PendenzeDAO pendenzeDao = new PendenzeDAO();
			ListaPendenzeDTO listaPendenzaDTO = new ListaPendenzeDTO(patchDTO.getUser());
			listaPendenzaDTO.setIdPagamento(patchDTO.getIdSessione());
			listaPendenzaDTO.setIdDomini(patchDTO.getIdDomini());
			listaPendenzaDTO.setIdTipiVersamento(patchDTO.getIdTipiVersamento());
			it.govpay.bd.viste.VersamentiBD versamentiBD = new it.govpay.bd.viste.VersamentiBD(pagamentiPortaleBD);
			versamentiBD.setAtomica(false);
			ListaPendenzeDTOResponse listaPendenze = pendenzeDao.listaPendenze(listaPendenzaDTO, versamentiBD);
			leggiPagamentoPortaleDTOResponse.setListaPendenze(listaPendenze.getResults());

			RptDAO rptDao = new RptDAO(); 
			ListaRptDTO listaRptDTO = new ListaRptDTO(patchDTO.getUser());
			listaRptDTO.setIdPagamento(pagamentoPortale.getIdSessione());
			listaRptDTO.setIdDomini(patchDTO.getIdDomini());
			listaRptDTO.setIdTipiVersamento(patchDTO.getIdTipiVersamento());
			it.govpay.bd.viste.RptBD rptBD = new it.govpay.bd.viste.RptBD(pagamentiPortaleBD);
			rptBD.setAtomica(false);
			ListaRptDTOResponse listaRpt = rptDao.listaRpt(listaRptDTO, rptBD);
			leggiPagamentoPortaleDTOResponse.setListaRpp(listaRpt.getResults());

			Boolean ack = null;
			for(PatchOp op: patchDTO.getOp()) {

				if(PATH_NOTA.equals(op.getPath())) {
					switch(op.getOp()) {
					case ADD: 

						LinkedHashMap<?,?> map = (LinkedHashMap<?,?>) op.getValue();
						StringBuilder sb = new StringBuilder();
						sb.append((String)map.get(UtenzaPatchUtils.OGGETTO_NOTA_KEY));

						if((String)map.get(UtenzaPatchUtils.TESTO_NOTA_KEY) != null) {
							sb.append(": ").append((String)map.get(UtenzaPatchUtils.TESTO_NOTA_KEY) );
						}

						String descrizione = sb.toString();
						pagamentoPortale.setDescrizioneStato(descrizione);
						break;
					default: throw new ServiceException("Op '"+op.getOp()+"' non valida per il path '"+op.getPath()+"'");
					}
				} else if(PATH_VERIFICATO.equals(op.getPath())) {
					switch(op.getOp()) {
					case REPLACE: 
						ack = (Boolean)op.getValue();
						pagamentoPortale.setAck(ack); 
						break;
					default: throw new ServiceException("Op '"+op.getOp()+"' non valida per il path '"+op.getPath()+"'");
					}

				} else {
					throw new ServiceException("Path '"+op.getPath()+"' non valido");
				}
			}

			pagamentiPortaleBD.updateStatoPagamento(pagamentoPortale.getId(), null, pagamentoPortale.getDescrizioneStato(), ack); 
			return leggiPagamentoPortaleDTOResponse;
		}catch(NotFoundException e) {
			throw new PagamentoPortaleNonTrovatoException("Non esiste un pagamento associato all'ID ["+patchDTO.getIdSessione()+"]");
		}finally {
			if(pagamentiPortaleBD != null)
				pagamentiPortaleBD.closeConnection();
		}
	}

	public GpResponse getWsResponse(GovPayException e, GpResponse response, String codMsgDiagnostico, Logger log) {
		FaultBean faultBean = e.getFaultBean();
		if(faultBean == null) {
			response.setMittente(Mittente.GOV_PAY);
			response.setCodEsito(e.getCodEsito() != null ? e.getCodEsito().toString() : "");
			response.setDescrizioneEsito(e.getDescrizioneEsito() != null ? e.getDescrizioneEsito() : "");
			response.setDettaglioEsito(e.getMessage());
			switch (e.getCodEsito()) {
			case INTERNAL:
				log.error("[" + e.getCodEsito() + "] " + e.getMessage() + (e.getCausa() != null ? "\n" + e.getCausa() : ""), e);
				break;
			default:
				LogUtils.logWarn(log, "[" + e.getCodEsito() + "] " + e.getMessage() +  (e.getCausa() != null ? "\n" + e.getCausa() : ""));
				break;
			}

			try {
				ContextThreadLocal.get().getApplicationLogger().log(codMsgDiagnostico, response.getCodEsito(), response.getDescrizioneEsito(), response.getDettaglioEsito());
			} catch (UtilsException e1) {
				LoggerWrapperFactory.getLogger(getClass()).error("Errore durante la scrittura dell'esito operazione: "+ e1.getMessage(),e1);
			}

		} else {
			if(faultBean.getId().contains("NodoDeiPagamentiSPC")) 
				response.setMittente(Mittente.NODO_DEI_PAGAMENTI_SPC);
			else 
				response.setMittente(Mittente.PSP);
			response.setCodEsito(faultBean.getFaultCode() != null ? faultBean.getFaultCode() : "");
			response.setDescrizioneEsito(faultBean.getFaultString() != null ? faultBean.getFaultString() : "");
			response.setDettaglioEsito(faultBean.getDescription());
		}
		return response;
	}
}
