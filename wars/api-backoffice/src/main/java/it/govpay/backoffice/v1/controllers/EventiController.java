package it.govpay.backoffice.v1.controllers;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.CategoriaEvento;
import it.govpay.backoffice.v1.beans.EsitoEvento;
import it.govpay.backoffice.v1.beans.Evento;
import it.govpay.backoffice.v1.beans.ListaEventi;
import it.govpay.backoffice.v1.beans.RuoloEvento;
import it.govpay.backoffice.v1.beans.converter.EventiConverter;
import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.bd.pagamento.filters.EventiFilter.VISTA;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.dao.eventi.EventiDAO;
import it.govpay.core.dao.eventi.dto.LeggiEventoDTO;
import it.govpay.core.dao.eventi.dto.LeggiEventoDTOResponse;
import it.govpay.core.dao.eventi.dto.ListaEventiDTO;
import it.govpay.core.dao.eventi.dto.ListaEventiDTOResponse;
import it.govpay.core.dao.pagamenti.PagamentiPortaleDAO;
import it.govpay.core.dao.pagamenti.PendenzeDAO;
import it.govpay.core.dao.pagamenti.RptDAO;
import it.govpay.core.dao.pagamenti.dto.ListaPagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPagamentiPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTOResponse;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;



public class EventiController extends BaseController {

	private SerializationConfig serializationConfig;

	public EventiController(String nomeServizio,Logger log) {
		super(nomeServizio,log);

		this.serializationConfig = new SerializationConfig();
		this.serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
		this.serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormat());
	}

	public Response findEventi(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String idDominio, String iuv, String idA2A, String idPendenza,
			String idPagamento, String esito, String dataDa, String dataA, 
			String categoria, String tipoEvento, String sottotipoEvento, String componente, String ruolo, Boolean messaggi) {
		String methodName = "findEventi";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			boolean autorizza = false;
			try {
				// autorizzazione sulla API
				this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.GIORNALE_DEGLI_EVENTI), Arrays.asList(Diritti.LETTURA));
			}catch (NotAuthorizedException e) {
				autorizza = true;
			}

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			if(idDominio != null)
				validatoreId.validaIdDominio("idDominio", idDominio);
			if(idA2A != null)
				validatoreId.validaIdApplicazione("idA2A", idA2A);

			// Parametri - > DTO Input

			ListaEventiDTO listaEventiDTO = new ListaEventiDTO(user);

			listaEventiDTO.setLimit(risultatiPerPagina);
			listaEventiDTO.setPagina(pagina);
			listaEventiDTO.setIdDominio(idDominio);
			listaEventiDTO.setIuv(iuv);
			listaEventiDTO.setIdA2A(idA2A);
			listaEventiDTO.setIdPendenza(idPendenza);
			listaEventiDTO.setIdPagamento(idPagamento);
			listaEventiDTO.setMessaggi(messaggi);

			if(esito != null) {
				EsitoEvento esitoEvento = EsitoEvento.fromValue(esito);
				if(esitoEvento != null) {
					switch (esitoEvento) {
					case FAIL:
						listaEventiDTO.setEsito(it.govpay.model.Evento.EsitoEvento.FAIL);
						break;
					case KO:
						listaEventiDTO.setEsito(it.govpay.model.Evento.EsitoEvento.KO);
						break;
					case OK:
						listaEventiDTO.setEsito(it.govpay.model.Evento.EsitoEvento.OK);
						break;
					}
				}
			}

			if(ruolo != null) {
				RuoloEvento ruoloEvento = RuoloEvento.fromValue(ruolo);
				if(ruoloEvento != null) {
					switch (ruoloEvento) {
					case CLIENT:
						listaEventiDTO.setRuolo(it.govpay.model.Evento.RuoloEvento.CLIENT);
						break;
					case SERVER:
						listaEventiDTO.setRuolo(it.govpay.model.Evento.RuoloEvento.SERVER);
						break;
					}
				}
			}

			if(categoria != null) {
				CategoriaEvento categoriaEvento = CategoriaEvento.fromValue(categoria);
				if(categoriaEvento != null) {
					switch (categoriaEvento) {
					case INTERFACCIA:
						listaEventiDTO.setCategoriaEvento(it.govpay.model.Evento.CategoriaEvento.INTERFACCIA);
						break;
					case INTERNO:
						listaEventiDTO.setCategoriaEvento(it.govpay.model.Evento.CategoriaEvento.INTERNO);
						break;
					case UTENTE:
						listaEventiDTO.setCategoriaEvento(it.govpay.model.Evento.CategoriaEvento.UTENTE);
						break;
					}
				}
			}

			listaEventiDTO.setComponente(componente); 
			listaEventiDTO.setTipoEvento(tipoEvento); 
			listaEventiDTO.setSottotipoEvento(sottotipoEvento);


			if(dataDa!=null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataDa);
				listaEventiDTO.setDataDa(dataDaDate);
			}

			if(dataA!=null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataA);
				listaEventiDTO.setDataA(dataADate);
			}

			boolean autorizzato = true;
			if(idA2A != null && idPendenza != null) {
				listaEventiDTO.setVista(VISTA.VERSAMENTI);

				if(autorizza) {
					//check autorizzazione per la pendenza scelta
					ListaPendenzeDTO listaPendenzeDTO = new ListaPendenzeDTO(user);
					listaPendenzeDTO.setIdA2A(idA2A);
					listaPendenzeDTO.setIdPendenza(idPendenza);

					// Autorizzazione sulle UO
					List<IdUnitaOperativa> idUnitaOperative = AuthorizationManager.getUoAutorizzate(user);
					if(idUnitaOperative == null) {
						throw AuthorizationManager.toNotAuthorizedExceptionNessunaUOAutorizzata(user);
					}
					listaPendenzeDTO.setUnitaOperative(idUnitaOperative);
					// autorizzazione sui tipi pendenza
					List<Long> idTipiVersamento = AuthorizationManager.getIdTipiVersamentoAutorizzati(user);
					if(idTipiVersamento == null) {
						throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);
					}
					listaPendenzeDTO.setIdTipiVersamento(idTipiVersamento);

					PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

					ListaPendenzeDTOResponse listaPendenzeDTOResponse = pendenzeDAO.countPendenze(listaPendenzeDTO);

					if(listaPendenzeDTOResponse.getTotalResults() == 0)
						autorizzato = false;
				}
			} else if(idDominio != null && iuv != null) {
				listaEventiDTO.setVista(VISTA.RPT);

				if(autorizza) {
					ListaRptDTO listaRptDTO = new ListaRptDTO(user);
					listaRptDTO.setIdDominio(idDominio);
					listaRptDTO.setIuv(iuv);

					// Autorizzazione sui domini
					List<String> domini = AuthorizationManager.getDominiAutorizzati(user);
					if(domini == null) {
						throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
					}
					listaRptDTO.setCodDomini(domini);

					RptDAO rptDAO = new RptDAO();
					ListaRptDTOResponse listaRptDTOResponse = rptDAO.countRpt(listaRptDTO);

					if(listaRptDTOResponse.getTotalResults() == 0)
						autorizzato = false;
				}
			} else if(idPagamento != null) {
				listaEventiDTO.setVista(VISTA.PAGAMENTI);
				
				if(autorizza) {
					
					ListaPagamentiPortaleDTO listaPagamentiPortaleDTO = new ListaPagamentiPortaleDTO(user);
					listaPagamentiPortaleDTO.setIdSessione(idPagamento);
					
					PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO();
					ListaPagamentiPortaleDTOResponse pagamentoPortaleDTOResponse = pagamentiPortaleDAO.countPagamentiPortale(listaPagamentiPortaleDTO);
					
					if(pagamentoPortaleDTOResponse.getTotalResults() == 0)
						autorizzato = false;
				}
			}

			List<String> domini = null;
			// Autorizzazione sui domini
			domini = AuthorizationManager.getDominiAutorizzati(user);
			//			if(domini == null) {
			//				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
			//			}
			listaEventiDTO.setCodDomini(domini);

			EventiDAO pspDAO = new EventiDAO();

			// CHIAMATA AL DAO

			ListaEventiDTOResponse listaEventiDTOResponse = (domini != null && autorizzato) ?  pspDAO.listaEventi(listaEventiDTO) : new ListaEventiDTOResponse(0, new ArrayList<>());

			// CONVERT TO JSON DELLA RISPOSTA

			List<Evento> results = new ArrayList<>();
			for(it.govpay.bd.model.Evento evento: listaEventiDTOResponse.getResults()) {
				results.add(EventiConverter.toRsModel(evento));
			}

			ListaEventi response = new ListaEventi(results, this.getServicePath(uriInfo),
					listaEventiDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null,this.serializationConfig)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}




	public Response getEvento(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String id) {
		String methodName = "getEvento";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			boolean autorizza = false;
			try {
				// autorizzazione sulla API
				this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.GIORNALE_DEGLI_EVENTI), Arrays.asList(Diritti.LETTURA));
			}catch (NotAuthorizedException e) {
				autorizza = true;
			}
			Long idLong = null; 
			try {
				idLong = Long.parseLong(id);
			}catch(Exception e) {
				throw new ValidationException("Il formato dell'id evento passato non e' valido");
			}

			// Parametri - > DTO Input
			LeggiEventoDTO leggiEventoDTO = new LeggiEventoDTO(user, idLong);

			// INIT DAO
			EventiDAO eventiDAO = new EventiDAO();

			// CHIAMATA AL DAO
			LeggiEventoDTOResponse leggiEventoDTOResponse = eventiDAO.leggiEvento(leggiEventoDTO);
			
			boolean autorizzato = true;
			if(autorizza) {
				it.govpay.bd.model.Evento evento = leggiEventoDTOResponse.getEvento();
				String idA2A = evento.getCodApplicazione();
				String idPendenza = evento.getCodVersamentoEnte();
				String idDominio = evento.getCodDominio();
				String iuv = evento.getCodDominio();
				String idPagamento = evento.getIdSessione();
				
				if(idA2A != null && idPendenza != null) {

					if(autorizza) {
						//check autorizzazione per la pendenza scelta
						ListaPendenzeDTO listaPendenzeDTO = new ListaPendenzeDTO(user);
						listaPendenzeDTO.setIdA2A(idA2A);
						listaPendenzeDTO.setIdPendenza(idPendenza);
						
						// Autorizzazione sulle UO
						List<IdUnitaOperativa> idUnitaOperative = AuthorizationManager.getUoAutorizzate(user);
						if(idUnitaOperative == null) {
							throw AuthorizationManager.toNotAuthorizedExceptionNessunaUOAutorizzata(user);
						}
						listaPendenzeDTO.setUnitaOperative(idUnitaOperative);
						// autorizzazione sui tipi pendenza
						List<Long> idTipiVersamento = AuthorizationManager.getIdTipiVersamentoAutorizzati(user);
						if(idTipiVersamento == null) {
							throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);
						}
						listaPendenzeDTO.setIdTipiVersamento(idTipiVersamento);

						PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

						ListaPendenzeDTOResponse listaPendenzeDTOResponse = pendenzeDAO.countPendenze(listaPendenzeDTO);

						if(listaPendenzeDTOResponse.getTotalResults() == 0)
							autorizzato = false;
					}
				} else if(idDominio != null && iuv != null) {

					if(autorizza) {
						ListaRptDTO listaRptDTO = new ListaRptDTO(user);
						listaRptDTO.setIdDominio(idDominio);
						listaRptDTO.setIuv(iuv);

						// Autorizzazione sui domini
						List<String> domini = AuthorizationManager.getDominiAutorizzati(user);
						if(domini == null) {
							throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
						}
						listaRptDTO.setCodDomini(domini);

						RptDAO rptDAO = new RptDAO();
						ListaRptDTOResponse listaRptDTOResponse = rptDAO.countRpt(listaRptDTO);

						if(listaRptDTOResponse.getTotalResults() == 0)
							autorizzato = false;
					}
				} else if(idPagamento != null) {
					
					if(autorizza) {
						
						ListaPagamentiPortaleDTO listaPagamentiPortaleDTO = new ListaPagamentiPortaleDTO(user);
						listaPagamentiPortaleDTO.setIdSessione(idPagamento);
						
						PagamentiPortaleDAO pagamentiPortaleDAO = new PagamentiPortaleDAO();
						ListaPagamentiPortaleDTOResponse pagamentoPortaleDTOResponse = pagamentiPortaleDAO.countPagamentiPortale(listaPagamentiPortaleDTO);
						
						if(pagamentoPortaleDTOResponse.getTotalResults() == 0)
							autorizzato = false;
					}
				}
			}
			
			if(!autorizzato)
				throw AuthorizationManager.toNotAuthorizedException(user);

			Evento response = EventiConverter.toRsModel(leggiEventoDTOResponse.getEvento()); 
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null,this.serializationConfig)).type(MediaType.APPLICATION_JSON),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}


}


