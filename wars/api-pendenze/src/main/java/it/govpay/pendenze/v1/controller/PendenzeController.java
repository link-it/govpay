package it.govpay.pendenze.v1.controller;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.commons.Versamento;
import it.govpay.core.dao.pagamenti.PendenzeDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PatchPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.PutPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.PutPendenzaDTOResponse;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.model.Versamento.ModoAvvisatura;
import it.govpay.pendenze.v1.beans.FaultBean;
import it.govpay.pendenze.v1.beans.FaultBean.CategoriaEnum;
import it.govpay.pendenze.v1.beans.ListaPendenze;
import it.govpay.pendenze.v1.beans.ModalitaAvvisaturaDigitale;
import it.govpay.pendenze.v1.beans.PatchOp;
import it.govpay.pendenze.v1.beans.PatchOp.OpEnum;
import it.govpay.pendenze.v1.beans.Pendenza;
import it.govpay.pendenze.v1.beans.PendenzaCreata;
import it.govpay.pendenze.v1.beans.PendenzaIndex;
import it.govpay.pendenze.v1.beans.PendenzaPut;
import it.govpay.pendenze.v1.beans.StatoPendenza;
import it.govpay.pendenze.v1.beans.converter.PatchOpConverter;
import it.govpay.pendenze.v1.beans.converter.PendenzeConverter;



public class PendenzeController extends BaseController {

	public PendenzeController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
	}

	public Response pendenzeIdA2AIdPendenzaGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, String idPendenza) {
		String methodName = "getByIda2aIdPendenza";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try{
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPendenza(idPendenza);
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdA2A(idA2A);
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PENDENZE), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdApplicazione("idA2A", idA2A);
			validatoreId.validaIdPendenza("idPendenza", idPendenza);
			
			// filtro sull'applicazione			
			if(!AutorizzazioneUtils.getAuthenticationDetails(user).getApplicazione().getCodApplicazione().equals(idA2A)) {
				throw AuthorizationManager.toNotAuthorizedException(user);
			}

			LeggiPendenzaDTO leggiPendenzaDTO = new LeggiPendenzaDTO(user);

			leggiPendenzaDTO.setCodA2A(idA2A);
			leggiPendenzaDTO.setCodPendenza(idPendenza);

			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

			LeggiPendenzaDTOResponse leggiPendenzaDTOResponse = pendenzeDAO.leggiPendenza(leggiPendenzaDTO);

			Pendenza pendenza = PendenzeConverter.toRsModel(leggiPendenzaDTOResponse.getVersamento(), leggiPendenzaDTOResponse.getRpts());
			return this.handleResponseOk(Response.status(Status.OK).entity(pendenza.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}

	public Response pendenzeGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String dataDa, String dataA, String idDominio, String idA2A, String idDebitore, String stato, String idPagamento) {
		String transactionId = ContextThreadLocal.get().getTransactionId();
		String methodName = "pendenzeGET";
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PENDENZE), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input

			ListaPendenzeDTO listaPendenzeDTO = new ListaPendenzeDTO(user);

			listaPendenzeDTO.setLimit(risultatiPerPagina);
			listaPendenzeDTO.setPagina(pagina);
			if(stato != null) {
				StatoPendenza statoPendenza = StatoPendenza.fromValue(stato);
				if(statoPendenza != null) {
					switch(statoPendenza) {
					case ANNULLATA: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.ANNULLATA); break;
					case ESEGUITA: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.ESEGUITA); break;
					case ESEGUITA_PARZIALE: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.ESEGUITA_PARZIALE); break;
					case NON_ESEGUITA: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.NON_ESEGUITA); break;
					case SCADUTA: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.SCADUTA); break;
					}				
				} else {
					throw new ValidationException("Codifica inesistente per stato. Valore fornito [" + stato
							+ "] valori possibili " + ArrayUtils.toString(StatoPendenza.values()));
				}
			}

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			
			if(idDominio != null) {
				validatoreId.validaIdDominio("idDominio", idDominio);
				listaPendenzeDTO.setIdDominio(idDominio);
			}
			
			// filtro sull'applicazione e' ignorato, un'applicazione vede solo le sue pendenze
//			if(idA2A != null) {
//				validatoreId.validaIdApplicazione("idA2A", idA2A);
//				listaPendenzeDTO.setIdA2A(idA2A);
//			}
			// filtro sull'applicazione			
			listaPendenzeDTO.setIdA2A(AutorizzazioneUtils.getAuthenticationDetails(user).getApplicazione().getCodApplicazione()); 
			
			if(idDebitore != null)
				listaPendenzeDTO.setIdDebitore(idDebitore);

			if(idPagamento != null)
				listaPendenzeDTO.setIdPagamento(idPagamento);

			if(ordinamento != null)
				listaPendenzeDTO.setOrderBy(ordinamento);
			
			if(dataDa!=null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataDa);
				listaPendenzeDTO.setDataDa(dataDaDate);
			}
				
			
			if(dataA!=null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataA);
				listaPendenzeDTO.setDataA(dataADate);
			}

			// Autorizzazione sui domini
			List<Long> idDomini = AuthorizationManager.getIdDominiAutorizzati(user);
			if(idDomini == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
			}
			listaPendenzeDTO.setIdDomini(idDomini);
			// autorizzazione sui tipi pendenza
			List<Long> idTipiVersamento = AuthorizationManager.getIdTipiVersamentoAutorizzati(user);
			if(idTipiVersamento == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);
			}
			listaPendenzeDTO.setIdTipiVersamento(idTipiVersamento);

			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

			// CHIAMATA AL DAO

			ListaPendenzeDTOResponse listaPendenzeDTOResponse = pendenzeDAO.listaPendenze(listaPendenzeDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			List<PendenzaIndex> results = new ArrayList<>();
			for(LeggiPendenzaDTOResponse ricevutaDTOResponse: listaPendenzeDTOResponse.getResults()) {
				PendenzaIndex rsModel = PendenzeConverter.toRsIndexModel(ricevutaDTOResponse.getVersamento());
				results.add(rsModel);
			}

			ListaPendenze response = new ListaPendenze(results, this.getServicePath(uriInfo),
					listaPendenzeDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}

	@SuppressWarnings("unchecked")
	public Response pendenzeIdA2AIdPendenzaPATCH(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, String idPendenza, java.io.InputStream is) {
		String methodName = "pendenzeIdA2AIdPendenzaPATCH";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream();){
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPendenza(idPendenza);
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdA2A(idA2A);
			
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PENDENZE), Arrays.asList(Diritti.SCRITTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdApplicazione("idA2A", idA2A);
			validatoreId.validaIdPendenza("idPendenza", idPendenza);
			
			// filtro sull'applicazione			
			if(!AutorizzazioneUtils.getAuthenticationDetails(user).getApplicazione().getCodApplicazione().equals(idA2A)) {
				throw new GovPayException(EsitoOperazione.APP_002, AutorizzazioneUtils.getAuthenticationDetails(user).getApplicazione().getCodApplicazione(), idA2A);
			}

			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

			PatchPendenzaDTO patchPendenzaDTO = new PatchPendenzaDTO(user);
			patchPendenzaDTO.setIdA2a(idA2A);
			patchPendenzaDTO.setIdPendenza(idPendenza);

			String jsonRequest = baos.toString();

			List<PatchOp> lstOp = new ArrayList<>();

			try {
				List<java.util.LinkedHashMap<?,?>> lst = JSONSerializable.parse(jsonRequest, List.class);
				for(java.util.LinkedHashMap<?,?> map: lst) {
					PatchOp op = new PatchOp();
					String opText = (String) map.get("op");
					OpEnum opFromValue = OpEnum.fromValue(opText);

					if(StringUtils.isNotEmpty(opText) && opFromValue == null)
						throw new ValidationException("Il campo op non e' valido.");

					op.setOp(opFromValue);
					op.setPath((String) map.get("path"));
					op.setValue(map.get("value"));
					op.validate();
					lstOp.add(op);
				}
			} catch (ServiceException e) {
				lstOp = JSONSerializable.parse(jsonRequest, List.class);
			}

			patchPendenzaDTO.setOp(PatchOpConverter.toModel(lstOp));

			LeggiPendenzaDTOResponse leggiPendenzaDTOResponse = pendenzeDAO.patch(patchPendenzaDTO);

			Pendenza pendenza = PendenzeConverter.toRsModel(leggiPendenzaDTOResponse.getVersamento(), leggiPendenzaDTOResponse.getRpts());
			return this.handleResponseOk(Response.status(Status.OK).entity(pendenza.toJSON(null)),transactionId).build();
		} catch(GovPayException e) {
			this.log.error("Errore durante il processo di pagamento", e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.OPERAZIONE);
			respKo.setCodice(e.getCodEsito().name());
			respKo.setDescrizione(e.getDescrizioneEsito());
			respKo.setDettaglio(e.getMessage());
			return this.handleResponseOk(Response.status(Status.INTERNAL_SERVER_ERROR).entity(this.getRespJson(respKo)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}



	public Response pendenzeIdA2AIdPendenzaPUT(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, String idPendenza, java.io.InputStream is, Boolean stampaAvviso, Boolean avvisaturaDigitale, ModalitaAvvisaturaDigitale modalitaAvvisaturaDigitale) {
		String methodName = "pendenzeIdA2AIdPendenzaPUT";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream();){
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPendenza(idPendenza);
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdA2A(idA2A);
			
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PENDENZE), Arrays.asList(Diritti.SCRITTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdApplicazione("idA2A", idA2A);
			validatoreId.validaIdPendenza("idPendenza", idPendenza);
			
			// filtro sull'applicazione			
			if(!AutorizzazioneUtils.getAuthenticationDetails(user).getApplicazione().getCodApplicazione().equals(idA2A)) {
				throw new GovPayException(EsitoOperazione.APP_002, AutorizzazioneUtils.getAuthenticationDetails(user).getApplicazione().getCodApplicazione(), idA2A);
			}

			String jsonRequest = baos.toString();
			PendenzaPut pendenzaPost= JSONSerializable.parse(jsonRequest, PendenzaPut.class);
			pendenzaPost.validate();

			Versamento versamento = PendenzeConverter.getVersamentoFromPendenza(pendenzaPost, idA2A, idPendenza);
			
			PutPendenzaDTO putVersamentoDTO = new PutPendenzaDTO(user);
			putVersamentoDTO.setVersamento(versamento);
			putVersamentoDTO.setStampaAvviso(stampaAvviso);
			putVersamentoDTO.setAvvisaturaDigitale(avvisaturaDigitale);
			ModoAvvisatura avvisaturaModalita = null;
			if(modalitaAvvisaturaDigitale != null) {
				avvisaturaModalita = modalitaAvvisaturaDigitale.equals(ModalitaAvvisaturaDigitale.ASINCRONA) ? ModoAvvisatura.ASICNRONA : ModoAvvisatura.SINCRONA;
			}

			putVersamentoDTO.setAvvisaturaModalita(avvisaturaModalita);
			
			String codDominio = versamento.getCodDominio();
			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(putVersamentoDTO.getUser(), codDominio)) {
				throw AuthorizationManager.toNotAuthorizedException(putVersamentoDTO.getUser(), codDominio);
			}

			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

			PutPendenzaDTOResponse createOrUpdate = pendenzeDAO.createOrUpdate(putVersamentoDTO);
			
			PendenzaCreata pc = new PendenzaCreata();
			pc.setIdDominio(createOrUpdate.getDominio().getCodDominio());
			pc.setNumeroAvviso(createOrUpdate.getVersamento().getNumeroAvviso());
			pc.pdf(createOrUpdate.getPdf());
			Status responseStatus = createOrUpdate.isCreated() ?  Status.CREATED : Status.OK;
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus).entity(pc.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}

}


