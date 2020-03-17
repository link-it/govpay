package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.mail.BodyPart;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.mime.MimeMultipart;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.Avviso;
import it.govpay.backoffice.v1.beans.DettaglioTracciatoPendenzeEsito;
import it.govpay.backoffice.v1.beans.EsitoOperazionePendenza;
import it.govpay.backoffice.v1.beans.FaultBean;
import it.govpay.backoffice.v1.beans.FaultBean.CategoriaEnum;
import it.govpay.backoffice.v1.beans.ListaOperazioniPendenza;
import it.govpay.backoffice.v1.beans.ListaPendenze;
import it.govpay.backoffice.v1.beans.ListaTracciatiPendenza;
import it.govpay.backoffice.v1.beans.ModalitaAvvisaturaDigitale;
import it.govpay.backoffice.v1.beans.OperazionePendenza;
import it.govpay.backoffice.v1.beans.PatchOp;
import it.govpay.backoffice.v1.beans.PatchOp.OpEnum;
import it.govpay.backoffice.v1.beans.Pendenza;
import it.govpay.backoffice.v1.beans.PendenzaCreata;
import it.govpay.backoffice.v1.beans.PendenzaIndex;
import it.govpay.backoffice.v1.beans.PendenzaPost;
import it.govpay.backoffice.v1.beans.PendenzaPut;
import it.govpay.backoffice.v1.beans.StatoOperazionePendenza;
import it.govpay.backoffice.v1.beans.StatoPendenza;
import it.govpay.backoffice.v1.beans.StatoTracciatoPendenza;
import it.govpay.backoffice.v1.beans.TracciatoPendenze;
import it.govpay.backoffice.v1.beans.TracciatoPendenzeEsito;
import it.govpay.backoffice.v1.beans.TracciatoPendenzeIndex;
import it.govpay.backoffice.v1.beans.TracciatoPendenzePost;
import it.govpay.backoffice.v1.beans.converter.PatchOpConverter;
import it.govpay.backoffice.v1.beans.converter.PendenzeConverter;
import it.govpay.backoffice.v1.beans.converter.TracciatiConverter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operazione;
import it.govpay.bd.model.Tracciato;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.eventi.DatiPagoPA;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.commons.Versamento;
import it.govpay.core.dao.commons.exception.NonTrovataException;
import it.govpay.core.dao.pagamenti.PendenzeDAO;
import it.govpay.core.dao.pagamenti.TracciatiDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiTracciatoDTO;
import it.govpay.core.dao.pagamenti.dto.ListaOperazioniTracciatoDTO;
import it.govpay.core.dao.pagamenti.dto.ListaOperazioniTracciatoDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaTracciatiDTO;
import it.govpay.core.dao.pagamenti.dto.ListaTracciatiDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PatchPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.PostTracciatoDTO;
import it.govpay.core.dao.pagamenti.dto.PostTracciatoDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PutPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.PutPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Operazione.StatoOperazioneType;
import it.govpay.model.Operazione.TipoOperazioneType;
import it.govpay.model.TipoVersamento;
import it.govpay.model.Tracciato.FORMATO_TRACCIATO;
import it.govpay.model.Tracciato.STATO_ELABORAZIONE;
import it.govpay.model.Tracciato.TIPO_TRACCIATO;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.model.Versamento.ModoAvvisatura;

public class PendenzeController extends BaseController {

	private SerializationConfig serializationConfig  = null;

	public PendenzeController(String nomeServizio,Logger log) {
		super(nomeServizio,log);

		this.serializationConfig = new SerializationConfig();
		this.serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
		this.serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
	}

	public Response getPendenza(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, String idPendenza, boolean addInfoIncasso) {
		String methodName = "getPendenza";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName));  
		try{
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPendenza(idPendenza);
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdA2A(idA2A);
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));

			LeggiPendenzaDTO leggiPendenzaDTO = new LeggiPendenzaDTO(user);

			leggiPendenzaDTO.setInfoIncasso(addInfoIncasso);
			leggiPendenzaDTO.setCodA2A(idA2A);
			leggiPendenzaDTO.setCodPendenza(idPendenza);

			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

			LeggiPendenzaDTOResponse ricevutaDTOResponse = pendenzeDAO.leggiPendenza(leggiPendenzaDTO);

			Dominio dominio = ricevutaDTOResponse.getDominio();
			TipoVersamento tipoVersamento = ricevutaDTOResponse.getTipoVersamento();
			UnitaOperativa unitaOperativa = ricevutaDTOResponse.getUnitaOperativa();

			// controllo che il dominio, uo e tipo versamento siano autorizzati
			if(!AuthorizationManager.isTipoVersamentoUOAuthorized(leggiPendenzaDTO.getUser(), dominio.getCodDominio(), unitaOperativa.getCodUo(), tipoVersamento.getCodTipoVersamento())) {
				throw AuthorizationManager.toNotAuthorizedException(leggiPendenzaDTO.getUser(), dominio.getCodDominio(), unitaOperativa.getCodUo(), tipoVersamento.getCodTipoVersamento());
			}

			Pendenza pendenza =	PendenzeConverter.toRsModel(ricevutaDTOResponse);
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(pendenza.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}

	public Response findPendenze(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String idDominio, String idA2A, String idDebitore, String stato, String idPagamento, String idPendenza, String dataDa, String dataA, String idTipoPendenza, String direzione, String divisione, String iuv, Boolean mostraSpontaneiNonPagati) {
		String methodName = "findPendenze";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		try{
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input
			ListaPendenzeDTO listaPendenzeDTO = new ListaPendenzeDTO(user);

			listaPendenzeDTO.setLimit(risultatiPerPagina);
			listaPendenzeDTO.setPagina(pagina);
			
			if(stato != null) {
				StatoPendenza statoPendenza = StatoPendenza.fromValue(stato);
				if(statoPendenza != null) {
					switch(statoPendenza) {
					case ANNULLATA: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.ANNULLATA); break;
					case ANOMALA: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.ANOMALA); break;
					case ESEGUITA: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.ESEGUITA); break;
					case ESEGUITA_PARZIALE: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.ESEGUITA_PARZIALE); break;
					case NON_ESEGUITA: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.NON_ESEGUITA); break;
					case SCADUTA: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.SCADUTA); break;
					case INCASSATA:  listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.INCASSATA); break;
					}				
				} else {
					throw new ValidationException("Codifica inesistente per stato. Valore fornito [" + stato
							+ "] valori possibili " + ArrayUtils.toString(StatoPendenza.values()));
				}
			}

			if(idDominio != null)
				listaPendenzeDTO.setIdDominio(idDominio);
			if(idA2A != null)
				listaPendenzeDTO.setIdA2A(idA2A);
			if(idDebitore != null)
				listaPendenzeDTO.setIdDebitore(idDebitore);

			if(idPagamento != null)
				listaPendenzeDTO.setIdPagamento(idPagamento);

			if(idPendenza != null)
				listaPendenzeDTO.setIdPendenza(idPendenza);

			if(ordinamento != null)
				listaPendenzeDTO.setOrderBy(ordinamento);

			if(dataDa!=null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataDa, "dataDa");
				listaPendenzeDTO.setDataDa(dataDaDate);
			}

			if(dataA!=null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataA, "dataA");
				listaPendenzeDTO.setDataA(dataADate);
			}

			if(idTipoPendenza != null)
				listaPendenzeDTO.setIdTipoVersamento(idTipoPendenza);
			listaPendenzeDTO.setDirezione(direzione);
			listaPendenzeDTO.setDivisione(divisione);
			listaPendenzeDTO.setIuvONumAvviso(iuv);
			listaPendenzeDTO.setMostraSpontaneiNonPagati(mostraSpontaneiNonPagati);

			// Autorizzazione sulle UO
			List<IdUnitaOperativa> uoAutorizzate = AuthorizationManager.getUoAutorizzate(user);
			if(uoAutorizzate == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunaUOAutorizzata(user);
			}
			listaPendenzeDTO.setUnitaOperative(uoAutorizzate);

			// Autorizzazione sui domini
			//			List<Long> idDomini = AuthorizationManager.getIdDominiAutorizzati(user);
			//			if(idDomini == null) {
			//				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
			//			}
			//			listaPendenzeDTO.setIdDomini(idDomini);

			// autorizzazione sui tipi pendenza
			List<Long> idTipiVersamento = AuthorizationManager.getIdTipiVersamentoAutorizzati(user);
			if(idTipiVersamento == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);
			}
			listaPendenzeDTO.setIdTipiVersamento(idTipiVersamento);

			// INIT DAO

			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

			// CHIAMATA AL DAO

			ListaPendenzeDTOResponse listaPendenzeDTOResponse = pendenzeDAO.listaPendenze(listaPendenzeDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			List<PendenzaIndex> results = new ArrayList<>();
			for(LeggiPendenzaDTOResponse ricevutaDTOResponse: listaPendenzeDTOResponse.getResults()) {
				results.add(PendenzeConverter.toRsModelIndex(ricevutaDTOResponse.getVersamento()));
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
	public Response updatePendenza(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, String idPendenza, java.io.InputStream is, boolean addInfoIncasso) {
		String methodName = "updatePendenza";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPendenza(idPendenza);
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdA2A(idA2A);
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.SCRITTURA));

			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

			PatchPendenzaDTO patchPendenzaDTO = new PatchPendenzaDTO(user);
			patchPendenzaDTO.setIdA2a(idA2A);
			patchPendenzaDTO.setIdPendenza(idPendenza);
			patchPendenzaDTO.setInfoIncasso(addInfoIncasso);

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
				//				PatchOp op = PatchOp.parse(jsonRequest);
				//				op.validate();
				//				lstOp.add(op);
			}

			patchPendenzaDTO.setOp(PatchOpConverter.toModel(lstOp));

			LeggiPendenzaDTOResponse ricevutaDTOResponse = pendenzeDAO.patch(patchPendenzaDTO);

			Pendenza pendenza =	PendenzeConverter.toRsModel(ricevutaDTOResponse);
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
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

	public Response addPendenza(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, String idPendenza, java.io.InputStream is, Boolean stampaAvviso, Boolean avvisaturaDigitale, ModalitaAvvisaturaDigitale modalitaAvvisaturaDigitale) {
		String methodName = "addPendenza";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPendenza(idPendenza);
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdA2A(idA2A);
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.SCRITTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdApplicazione("idA2A", idA2A);
			validatoreId.validaIdPendenza("idPendenza", idPendenza);

			String jsonRequest = baos.toString();
			PendenzaPut pendenzaPost= JSONSerializable.parse(jsonRequest, PendenzaPut.class);
			pendenzaPost.validate();

			Versamento versamento = PendenzeConverter.getVersamentoFromPendenza(pendenzaPost, idA2A, idPendenza);

			// controllo che il dominio, uo e tipo versamento siano autorizzati
			if(!AuthorizationManager.isTipoVersamentoUOAuthorized(user, versamento.getCodDominio(), versamento.getCodUnitaOperativa(), versamento.getCodTipoVersamento())) {
				throw AuthorizationManager.toNotAuthorizedException(user, versamento.getCodDominio(), versamento.getCodUnitaOperativa(), versamento.getCodTipoVersamento());
			}

			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

			PutPendenzaDTO putVersamentoDTO = new PutPendenzaDTO(user);
			putVersamentoDTO.setVersamento(versamento);
			putVersamentoDTO.setStampaAvviso(stampaAvviso);
			putVersamentoDTO.setAvvisaturaDigitale(avvisaturaDigitale);
			ModoAvvisatura avvisaturaModalita = null;
			if(modalitaAvvisaturaDigitale != null) {
				avvisaturaModalita = modalitaAvvisaturaDigitale.equals(ModalitaAvvisaturaDigitale.ASINCRONA) ? ModoAvvisatura.ASICNRONA : ModoAvvisatura.SINCRONA;
			}

			putVersamentoDTO.setAvvisaturaModalita(avvisaturaModalita);

			PutPendenzaDTOResponse createOrUpdate = pendenzeDAO.createOrUpdate(putVersamentoDTO);

			PendenzaCreata pc = new PendenzaCreata();
			pc.setIdDominio(createOrUpdate.getDominio().getCodDominio());
			if(createOrUpdate.getUo()!= null && !it.govpay.model.Dominio.EC.equals(createOrUpdate.getUo().getCodUo()))
				pc.setIdUnitaOperativa(createOrUpdate.getUo().getCodUo());
			pc.setNumeroAvviso(createOrUpdate.getVersamento().getNumeroAvviso());
			pc.pdf(createOrUpdate.getPdf());
			Status responseStatus = createOrUpdate.isCreated() ?  Status.CREATED : Status.OK;
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus).entity(pc.toJSON(null)),transactionId).build();
		} catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}

	public Response addPendenza(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, Boolean stampaAvviso, Boolean avvisaturaDigitale, ModalitaAvvisaturaDigitale modalitaAvvisaturaDigitale) {
		String methodName = "addPendenza";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.SCRITTURA));

			String jsonRequest = baos.toString();
			PendenzaPost pendenzaPost= JSONSerializable.parse(jsonRequest, PendenzaPost.class);

			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPendenza(pendenzaPost.getIdPendenza());
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdA2A(pendenzaPost.getIdA2A());

			pendenzaPost.validate();

			Versamento versamento = PendenzeConverter.getVersamentoFromPendenza(pendenzaPost);

			// controllo che il dominio, uo e tipo versamento siano autorizzati
			if(!AuthorizationManager.isTipoVersamentoUOAuthorized(user, versamento.getCodDominio(), versamento.getCodUnitaOperativa(), versamento.getCodTipoVersamento())) {
				throw AuthorizationManager.toNotAuthorizedException(user, versamento.getCodDominio(), versamento.getCodUnitaOperativa(), versamento.getCodTipoVersamento());
			}

			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

			PutPendenzaDTO putVersamentoDTO = new PutPendenzaDTO(user);
			putVersamentoDTO.setVersamento(versamento);
			putVersamentoDTO.setStampaAvviso(stampaAvviso);
			putVersamentoDTO.setAvvisaturaDigitale(avvisaturaDigitale);
			ModoAvvisatura avvisaturaModalita = null;
			if(modalitaAvvisaturaDigitale != null) {
				avvisaturaModalita = modalitaAvvisaturaDigitale.equals(ModalitaAvvisaturaDigitale.ASINCRONA) ? ModoAvvisatura.ASICNRONA : ModoAvvisatura.SINCRONA;
			}

			putVersamentoDTO.setAvvisaturaModalita(avvisaturaModalita);

			PutPendenzaDTOResponse createOrUpdate = pendenzeDAO.createOrUpdate(putVersamentoDTO);

			PendenzaCreata pc = new PendenzaCreata();
			pc.setIdDominio(createOrUpdate.getDominio().getCodDominio());
			if(createOrUpdate.getUo()!= null && !it.govpay.model.Dominio.EC.equals(createOrUpdate.getUo().getCodUo()))
				pc.setIdUnitaOperativa(createOrUpdate.getUo().getCodUo());
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



	public Response addPendenzaPOST(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idDominio, String idTipoPendenza, java.io.InputStream is, String idUnitaOperativa, Boolean stampaAvviso, Boolean avvisaturaDigitale, ModalitaAvvisaturaDigitale modalitaAvvisaturaDigitale) {
		String methodName = "addPendenzaPOST";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){

			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setCodDominio(idDominio);
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.SCRITTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			validatoreId.validaIdTipoVersamento("idTipoPendenza", idTipoPendenza);
			if(idUnitaOperativa != null)
				validatoreId.validaIdUO("idUnitaOperativa", idUnitaOperativa);

			// controllo che il dominio e tipo versamento siano autorizzati
			if(idUnitaOperativa != null) {
				if(!AuthorizationManager.isTipoVersamentoUOAuthorized(user, idDominio, idUnitaOperativa, idTipoPendenza)) {
					throw AuthorizationManager.toNotAuthorizedException(user, idDominio, idUnitaOperativa, idTipoPendenza);
				}
			} else {
				if(!AuthorizationManager.isTipoVersamentoDominioAuthorized(user, idDominio, idTipoPendenza)) {
					throw AuthorizationManager.toNotAuthorizedException(user, idDominio, idTipoPendenza);
				}
			}

			// salvo il json ricevuto
			IOUtils.copy(is, baos);	
			String jsonRequest = baos.toString();

			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

			PutPendenzaDTO putVersamentoDTO = new PutPendenzaDTO(user);
			putVersamentoDTO.setStampaAvviso(stampaAvviso);
			putVersamentoDTO.setAvvisaturaDigitale(avvisaturaDigitale);
			ModoAvvisatura avvisaturaModalita = null;
			if(modalitaAvvisaturaDigitale != null) {
				avvisaturaModalita = modalitaAvvisaturaDigitale.equals(ModalitaAvvisaturaDigitale.ASINCRONA) ? ModoAvvisatura.ASICNRONA : ModoAvvisatura.SINCRONA;
			}
			putVersamentoDTO.setAvvisaturaModalita(avvisaturaModalita);
			putVersamentoDTO.setCustomReq(jsonRequest);
			putVersamentoDTO.setCodDominio(idDominio);
			putVersamentoDTO.setCodTipoVersamento(idTipoPendenza);
			putVersamentoDTO.setCodUo(idUnitaOperativa);
			putVersamentoDTO.setHeaders(this.getHeaders(getRequest()));
			putVersamentoDTO.setPathParameters(uriInfo.getPathParameters());
			putVersamentoDTO.setQueryParameters(uriInfo.getQueryParameters());

			PutPendenzaDTOResponse createOrUpdate = pendenzeDAO.createOrUpdateCustom(putVersamentoDTO);

			PendenzaCreata pc = new PendenzaCreata();
			pc.setIdDominio(createOrUpdate.getDominio().getCodDominio());
			pc.setNumeroAvviso(createOrUpdate.getVersamento().getNumeroAvviso());
			pc.pdf(createOrUpdate.getPdf());
			pc.setIdA2A(createOrUpdate.getVersamento().getApplicazione(null).getCodApplicazione());
			pc.setIdPendenza(createOrUpdate.getVersamento().getCodVersamentoEnte());
			if(createOrUpdate.getUo()!= null && !it.govpay.model.Dominio.EC.equals(createOrUpdate.getUo().getCodUo()))
				pc.setIdUnitaOperativa(createOrUpdate.getUo().getCodUo());
			Status responseStatus = createOrUpdate.isCreated() ?  Status.CREATED : Status.OK;
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus).entity(pc.toJSON(null)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}


	public Response addTracciatoPendenze(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is) {
		String methodName = "addTracciatoPendenze";  
		String transactionId = ContextThreadLocal.get().getTransactionId();

		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){

			String contentTypeBody = null;
			if(httpHeaders.getRequestHeaders().containsKey("Content-Type")) {
				contentTypeBody = httpHeaders.getRequestHeaders().get("Content-Type").get(0);
			}

			this.log.debug(MessageFormat.format("Content-Type della richiesta: {0}.", contentTypeBody));


			String fileName = null;
			InputStream fileInputStream = null;
			try{
				// controllo se sono in una richiesta multipart
				if(contentTypeBody != null && contentTypeBody.startsWith("multipart")) {
					javax.mail.internet.ContentType cType = new javax.mail.internet.ContentType(contentTypeBody);
					this.log.debug(MessageFormat.format("Content-Type Boundary: [{0}]", cType.getParameter("boundary")));

					MimeMultipart mimeMultipart = new MimeMultipart(is,contentTypeBody);

					for(int i = 0 ; i < mimeMultipart.countBodyParts() ;  i ++) {
						BodyPart bodyPart = mimeMultipart.getBodyPart(i);
						fileName = getBodyPartFileName(bodyPart);

						if(fileName != null) {
							fileInputStream = bodyPart.getInputStream();
							break;
						}
					}

					if(fileInputStream != null) {
						IOUtils.copy(fileInputStream, baos);
					}
				}
			}catch(Exception e) {
				this.log.error(e.getMessage(),e);
			}

			if(fileInputStream == null) {
				// salvo il json ricevuto
				IOUtils.copy(is, baos);
			}

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.SCRITTURA));

			String jsonRequest = baos.toString();
			TracciatoPendenzePost tracciatoPendenzeRequest = JSONSerializable.parse(jsonRequest, TracciatoPendenzePost.class);

			tracciatoPendenzeRequest.validate();

			TracciatiDAO tracciatiDAO = new TracciatiDAO();

			PostTracciatoDTO postTracciatoDTO = new PostTracciatoDTO(user);

			postTracciatoDTO.setIdDominio(tracciatoPendenzeRequest.getIdDominio());
			postTracciatoDTO.setNomeFile(tracciatoPendenzeRequest.getIdTracciato());
			postTracciatoDTO.setAvvisaturaDigitale(tracciatoPendenzeRequest.AvvisaturaDigitale());
			if(tracciatoPendenzeRequest.getModalitaAvvisaturaDigitale() != null) {
				ModoAvvisatura modoAvvisatura = tracciatoPendenzeRequest.getModalitaAvvisaturaDigitale().equals(ModalitaAvvisaturaDigitale.ASINCRONA) ? ModoAvvisatura.ASICNRONA : ModoAvvisatura.SINCRONA;
				postTracciatoDTO.setAvvisaturaModalita(modoAvvisatura );
			}
			postTracciatoDTO.setContenuto(baos.toByteArray());
			postTracciatoDTO.setFormato(FORMATO_TRACCIATO.JSON);

			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(user);
			Operatore operatore = userDetails.getOperatore();
			postTracciatoDTO.setOperatore(operatore);

			PostTracciatoDTOResponse postTracciatoDTOResponse = tracciatiDAO.create(postTracciatoDTO);
			
			GpContext ctx = (GpContext) ContextThreadLocal.get().getApplicationContext();
			if(ctx.getEventoCtx().getDatiPagoPA() == null) {
				ctx.getEventoCtx().setDatiPagoPA(new DatiPagoPA());
			}
			ctx.getEventoCtx().getDatiPagoPA().setIdTracciato(postTracciatoDTOResponse.getTracciato().getId());
			ctx.getEventoCtx().setIdTracciato(postTracciatoDTOResponse.getTracciato().getId());

			TracciatoPendenzeIndex rsModel = TracciatiConverter.toTracciatoPendenzeRsModelIndex(postTracciatoDTOResponse.getTracciato());

			Status responseStatus = Status.CREATED;
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).entity(rsModel.toJSON(null,this.serializationConfig)).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}
	
	public Response addTracciatoPendenze(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, String idDominio, Boolean avvisaturaDigitale, ModalitaAvvisaturaDigitale modalitaAvvisaturaDigitale) {
		return _addTracciatoPendenze(user, uriInfo, httpHeaders, is, idDominio, null, avvisaturaDigitale, modalitaAvvisaturaDigitale, false);
	}
	
	public Response addTracciatoPendenze(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, String idDominio, String idTipoPendenza, Boolean avvisaturaDigitale, ModalitaAvvisaturaDigitale modalitaAvvisaturaDigitale) {
		return _addTracciatoPendenze(user, uriInfo, httpHeaders, is, idDominio, idTipoPendenza, avvisaturaDigitale, modalitaAvvisaturaDigitale, true);
	}

	private Response _addTracciatoPendenze(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, String idDominio, String idTipoPendenza,
			Boolean avvisaturaDigitale, ModalitaAvvisaturaDigitale modalitaAvvisaturaDigitale, boolean checkTipoPendenza) {
		String methodName = "addTracciatoPendenze";  
		String transactionId = ContextThreadLocal.get().getTransactionId();

		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){

			String contentTypeBody = null;
			if(httpHeaders.getRequestHeaders().containsKey("Content-Type")) {
				contentTypeBody = httpHeaders.getRequestHeaders().get("Content-Type").get(0);
			}

			this.log.debug(MessageFormat.format("Content-Type della richiesta: {0}.", contentTypeBody));


			String fileName = null;
			InputStream fileInputStream = null;
			try{
				// controllo se sono in una richiesta multipart
				if(contentTypeBody != null && contentTypeBody.startsWith("multipart")) {
					javax.mail.internet.ContentType cType = new javax.mail.internet.ContentType(contentTypeBody);
					this.log.debug(MessageFormat.format("Content-Type Boundary: [{0}]", cType.getParameter("boundary")));

					MimeMultipart mimeMultipart = new MimeMultipart(is,contentTypeBody);

					for(int i = 0 ; i < mimeMultipart.countBodyParts() ;  i ++) {
						BodyPart bodyPart = mimeMultipart.getBodyPart(i);
						fileName = getBodyPartFileName(bodyPart);

						if(fileName != null) {
							fileInputStream = bodyPart.getInputStream();
							break;
						}
					}

					if(fileInputStream != null) {
						IOUtils.copy(fileInputStream, baos);
					}
				}
			}catch(Exception e) {
				this.log.error(e.getMessage(),e);
			}
			
			if(httpHeaders.getRequestHeaders().containsKey("X-GOVPAY-FILENAME")) {
				fileName = httpHeaders.getRequestHeaders().get("X-GOVPAY-FILENAME").get(0);
			}

			if(fileInputStream == null) {
				// salvo il file ricevuto
				IOUtils.copy(is, baos);
			}

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.SCRITTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);
			if(checkTipoPendenza)
				validatoreId.validaIdTipoVersamento("idTipoPendenza", idTipoPendenza);

			// controllo che il dominio e tipo versamento siano autorizzati
			if(idTipoPendenza != null && !AuthorizationManager.isTipoVersamentoDominioAuthorized(user, idDominio, idTipoPendenza)) {
				throw AuthorizationManager.toNotAuthorizedException(user, idDominio, idTipoPendenza);
			} else if(idTipoPendenza == null && !AuthorizationManager.isDominioAuthorized(user, idDominio)) {
				throw AuthorizationManager.toNotAuthorizedException(user, idDominio, null);
			}

			TracciatiDAO tracciatiDAO = new TracciatiDAO();

			PostTracciatoDTO postTracciatoDTO = new PostTracciatoDTO(user);

			postTracciatoDTO.setIdDominio(idDominio);
			postTracciatoDTO.setIdTipoPendenza(idTipoPendenza);
			postTracciatoDTO.setNomeFile(fileName);
			if(postTracciatoDTO.getNomeFile() == null)
				if(idTipoPendenza != null)
					postTracciatoDTO.setNomeFile(idDominio + "_" + idTipoPendenza);
				else
					postTracciatoDTO.setNomeFile(idDominio);
			postTracciatoDTO.setAvvisaturaDigitale(avvisaturaDigitale);
			if(modalitaAvvisaturaDigitale != null) {
				ModoAvvisatura modoAvvisatura = modalitaAvvisaturaDigitale.equals(ModalitaAvvisaturaDigitale.ASINCRONA) ? ModoAvvisatura.ASICNRONA : ModoAvvisatura.SINCRONA;
				postTracciatoDTO.setAvvisaturaModalita(modoAvvisatura);
			}
			postTracciatoDTO.setContenuto(baos.size() > 0 ? baos.toByteArray() : null);
			postTracciatoDTO.setFormato(FORMATO_TRACCIATO.CSV);

			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(user);
			Operatore operatore = userDetails.getOperatore();
			postTracciatoDTO.setOperatore(operatore);

			PostTracciatoDTOResponse postTracciatoDTOResponse = tracciatiDAO.create(postTracciatoDTO);
			
			GpContext ctx = (GpContext) ContextThreadLocal.get().getApplicationContext();
			if(ctx.getEventoCtx().getDatiPagoPA() == null) {
				ctx.getEventoCtx().setDatiPagoPA(new DatiPagoPA());
			}
			ctx.getEventoCtx().getDatiPagoPA().setIdTracciato(postTracciatoDTOResponse.getTracciato().getId());
			ctx.getEventoCtx().setIdTracciato(postTracciatoDTOResponse.getTracciato().getId());

			TracciatoPendenzeIndex rsModel = TracciatiConverter.toTracciatoPendenzeRsModelIndex(postTracciatoDTOResponse.getTracciato());

			Status responseStatus = Status.CREATED;
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).entity(rsModel.toJSON(null,this.serializationConfig)).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}



	public Response findTracciatiPendenze(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String idDominio, StatoTracciatoPendenza stato) {
		String methodName = "findTracciatiPendenze";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		try{
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input

			ListaTracciatiDTO listaTracciatiDTO = new ListaTracciatiDTO(user);

			listaTracciatiDTO.setLimit(risultatiPerPagina);
			listaTracciatiDTO.setPagina(pagina);
			if(stato != null)
				listaTracciatiDTO.setStatoTracciatoPendenza(it.govpay.model.StatoTracciatoPendenza.fromValue(stato.name()));
			List<TIPO_TRACCIATO> tipoTracciato = new ArrayList<>();
			tipoTracciato.add(TIPO_TRACCIATO.PENDENZA);
			listaTracciatiDTO.setTipoTracciato(tipoTracciato);
			listaTracciatiDTO.setIdDominio(idDominio);

			// Autorizzazione sui domini
			List<String> domini = AuthorizationManager.getDominiAutorizzati(user);
			if(domini == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
			}
			listaTracciatiDTO.setCodDomini(domini);

			// INIT DAO

			TracciatiDAO tracciatiDAO = new TracciatiDAO(); 

			// CHIAMATA AL DAO

			ListaTracciatiDTOResponse listaTracciatiDTOResponse = tracciatiDAO.listaTracciati(listaTracciatiDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			List<TracciatoPendenzeIndex> results = new ArrayList<>();
			for(Tracciato tracciato: listaTracciatiDTOResponse.getResults()) {
				TracciatoPendenzeIndex rsModel = TracciatiConverter.toTracciatoPendenzeRsModelIndex(tracciato);
				results.add(rsModel);
			}

			ListaTracciatiPendenza response = new ListaTracciatiPendenza(results, this.getServicePath(uriInfo),
					listaTracciatiDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null,this.serializationConfig)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}



	public Response getEsitoTracciatoPendenze(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer id) {
		String methodName = "getEsitoTracciatoPendenze";  
		String transactionId = ContextThreadLocal.get().getTransactionId();

		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));

			LeggiTracciatoDTO leggiTracciatoDTO = new LeggiTracciatoDTO(user);
			leggiTracciatoDTO.setId((long) id);

			// Autorizzazione sui domini
			List<Long> idDomini = AuthorizationManager.getIdDominiAutorizzati(user);
			if(idDomini == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
			}

			TracciatiDAO tracciatiDAO = new TracciatiDAO();
			Tracciato tracciato = tracciatiDAO.leggiTracciato(leggiTracciatoDTO);

			// check dominio
			if(!AuthorizationManager.isDominioAuthorized(leggiTracciatoDTO.getUser(), tracciato.getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(leggiTracciatoDTO.getUser(), tracciato.getCodDominio(),null);
			}

			if(tracciato.getStato().equals(STATO_ELABORAZIONE.ELABORAZIONE))
				throw new NonTrovataException("Tracciato di Esito non presente: elaborazione ancora in corso");
			
			if(tracciato.getStato().equals(STATO_ELABORAZIONE.SCARTATO))
				throw new NonTrovataException("Tracciato di Esito non presente: tracciato scartato");


			String resFileName = tracciato.getFileNameEsito();
			switch (tracciato.getFormato()) {
			case CSV:
				if(!resFileName.endsWith(".csv"))
					resFileName = resFileName.concat(".csv");

				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).type("text/csv").entity(tracciato.getRawEsito()).header("content-disposition", "attachment; filename=\""+resFileName+"\""),transactionId).build();
			case JSON:
				TracciatoPendenzeEsito rsModel = TracciatiConverter.toTracciatoPendenzeEsitoRsModel(tracciato);

				if(!resFileName.endsWith(".json"))
					resFileName = resFileName.concat(".json");

				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(rsModel.toJSON(null,this.serializationConfig)).header("content-disposition", "attachment; filename=\""+resFileName+"\""),transactionId).build();
			case XML:
			default:
				throw new ValidationException("Formato non disponibile");
			}
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}



	public Response getTracciatoPendenze(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer id) {
		String methodName = "getTracciatoPendenze";  
		String transactionId = ContextThreadLocal.get().getTransactionId();

		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));

			LeggiTracciatoDTO leggiTracciatoDTO = new LeggiTracciatoDTO(user);
			leggiTracciatoDTO.setId((long) id);

			List<Long> idDomini = AuthorizationManager.getIdDominiAutorizzati(user);
			if(idDomini == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
			}

			TracciatiDAO tracciatiDAO = new TracciatiDAO();
			Tracciato tracciato = tracciatiDAO.leggiTracciato(leggiTracciatoDTO);

			// check dominio
			if(!AuthorizationManager.isDominioAuthorized(leggiTracciatoDTO.getUser(), tracciato.getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(leggiTracciatoDTO.getUser(), tracciato.getCodDominio(),null);
			}

			TracciatoPendenze rsModel = TracciatiConverter.toTracciatoPendenzeRsModel(tracciato);
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(rsModel.toJSON(null,this.serializationConfig)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}



	public Response findOperazioniTracciatoPendenze(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer id, Integer pagina, Integer risultatiPerPagina) {
		String transactionId = ContextThreadLocal.get().getTransactionId();
		String methodName = "findOperazioniTracciatoPendenze";
		try{
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName));

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));

			// Parametri - > DTO Input

			ListaOperazioniTracciatoDTO listaOperazioniTracciatoDTO = new ListaOperazioniTracciatoDTO(user);

			listaOperazioniTracciatoDTO.setLimit(risultatiPerPagina);
			listaOperazioniTracciatoDTO.setPagina(pagina);
			listaOperazioniTracciatoDTO.setIdTracciato((long) id);

			List<Long> idDomini = AuthorizationManager.getIdDominiAutorizzati(user);
			if(idDomini == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
			}

			// INIT DAO

			TracciatiDAO tracciatiDAO = new TracciatiDAO(); 

			// CHIAMATA AL DAO

			ListaOperazioniTracciatoDTOResponse listaTracciatiDTOResponse = tracciatiDAO.listaOperazioniTracciatoPendenza(listaOperazioniTracciatoDTO);

			// CONVERT TO JSON DELLA RISPOSTA

			List<OperazionePendenza> results = new ArrayList<>();
			for(Operazione operazione: listaTracciatiDTOResponse.getResults()) {
				OperazionePendenza rsModel = TracciatiConverter.toOperazioneTracciatoPendenzaRsModel(operazione);
				results.add(rsModel);
			}

			ListaOperazioniPendenza response = new ListaOperazioniPendenza(results, this.getServicePath(uriInfo),
					listaTracciatiDTOResponse.getTotalResults(), pagina, risultatiPerPagina);

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null,this.serializationConfig)),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}

	public Response getRichiestaTracciatoPendenze(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer id) {
		String methodName = "getRichiestaTracciatoPendenze";  
		String transactionId = ContextThreadLocal.get().getTransactionId();

		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));

			LeggiTracciatoDTO leggiTracciatoDTO = new LeggiTracciatoDTO(user);
			leggiTracciatoDTO.setId((long) id);

			// Autorizzazione sui domini
			List<Long> idDomini = AuthorizationManager.getIdDominiAutorizzati(user);
			if(idDomini == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
			}

			TracciatiDAO tracciatiDAO = new TracciatiDAO();
			Tracciato tracciato = tracciatiDAO.leggiTracciato(leggiTracciatoDTO);

			// check dominio
			if(!AuthorizationManager.isDominioAuthorized(leggiTracciatoDTO.getUser(), tracciato.getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(leggiTracciatoDTO.getUser(), tracciato.getCodDominio(),null);
			}

			String reqFileName = tracciato.getFileNameRichiesta();
			switch (tracciato.getFormato()) {
			case CSV:
				if(!reqFileName.endsWith(".csv"))
					reqFileName = reqFileName.concat(".csv");

				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).type("text/csv").entity(tracciato.getRawRichiesta()).header("content-disposition", "attachment; filename=\""+reqFileName+"\""),transactionId).build();
			case JSON:
				if(!reqFileName.endsWith(".json"))
					reqFileName = reqFileName.concat(".json");

				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(new String(tracciato.getRawRichiesta())).header("content-disposition", "attachment; filename=\""+reqFileName+"\""),transactionId).build();
			case XML:
			default:
				throw new ValidationException("Formato non disponibile");
			}
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
	}


	public Response getStampeTracciatoPendenze(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer id) {
		String methodName = "getStampeTracciatoPendenze";  
		String transactionId = ContextThreadLocal.get().getTransactionId();

		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.PENDENZE), Arrays.asList(Diritti.LETTURA));

			LeggiTracciatoDTO leggiTracciatoDTO = new LeggiTracciatoDTO(user);
			leggiTracciatoDTO.setId((long) id);

			List<Long> idDomini = AuthorizationManager.getIdDominiAutorizzati(user);
			if(idDomini == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(user);
			}

			TracciatiDAO tracciatiDAO = new TracciatiDAO();
			Tracciato tracciato = tracciatiDAO.leggiTracciato(leggiTracciatoDTO);

			if(tracciato.getStato().equals(STATO_ELABORAZIONE.ELABORAZIONE))
				throw new NonTrovataException("Stampe avvisi non disponibili per il tracciato: elaborazione ancora in corso");
			
			if(tracciato.getStato().equals(STATO_ELABORAZIONE.SCARTATO))
				throw new NonTrovataException("Stampe avvisi non disponibili per il tracciato: tracciato scartato");

			// check dominio
			if(!AuthorizationManager.isDominioAuthorized(leggiTracciatoDTO.getUser(), tracciato.getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(leggiTracciatoDTO.getUser(), tracciato.getCodDominio(),null);
			}

			String zipFileName = (tracciato.getFileNameRichiesta().contains(".") ? tracciato.getFileNameRichiesta().substring(0, tracciato.getFileNameRichiesta().lastIndexOf(".")) : tracciato.getFileNameRichiesta()) + ".zip";

			ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
			ZipOutputStream zos = new ZipOutputStream(baos1);
			switch (tracciato.getFormato()) {
			case CSV:
				this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
				this.popolaZipTracciatoCSV(user, tracciato, zos);
				break;
			case JSON:
				TracciatoPendenzeEsito rsModel = TracciatiConverter.toTracciatoPendenzeEsitoRsModel(tracciato);
				DettaglioTracciatoPendenzeEsito esito = rsModel.getEsito();
				List<EsitoOperazionePendenza> inserimenti = esito.getInserimenti();
				this.popolaZip(user, inserimenti, zos);
				break;
			case XML:
			default:
				throw new ValidationException("Formato non disponibile");
			}

			zos.flush();
			zos.close();
			byte[] b = baos1.toByteArray();
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).type(MediaType.APPLICATION_OCTET_STREAM).entity(b).header("content-disposition", "attachment; filename=\""+zipFileName+"\""),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}

	}

	private void popolaZip(Authentication user, List<EsitoOperazionePendenza> inserimenti, ZipOutputStream zos)
			throws ServiceException, PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException,
			IOException {
		boolean addError = true;
		
		// Tengo traccia degli avvisi inseriti nello zip per tenere solo l'ultima versione.
		Set<String> numeriAvviso = new HashSet<String>();
		
		if(inserimenti != null && !inserimenti.isEmpty()) {
			PendenzeDAO pendenzeDAO = new PendenzeDAO();
			for (EsitoOperazionePendenza esitoOperazionePendenza : inserimenti) {
				if(esitoOperazionePendenza.getStato().equals(StatoOperazionePendenza.ESEGUITO) && esitoOperazionePendenza.getEsito().equals("ADD_OK")) { 
					addError = false; // ho trovato almeno un avviso da stampare

					LeggiPendenzaDTO leggiPendenzaDTO = new LeggiPendenzaDTO(user);

					String idDominio = null;
					String numeroAvviso = null;
					try {
						Avviso avviso = (Avviso) esitoOperazionePendenza.getDati();
						idDominio = avviso.getIdDominio();
						numeroAvviso = avviso.getNumeroAvviso();
					} catch (Exception e) {
						java.util.LinkedHashMap<?,?> map = (LinkedHashMap<?, ?>) esitoOperazionePendenza.getDati();
						idDominio =(String)map.get("idDominio");
						numeroAvviso =(String)map.get("numeroAvviso");
					}
					
					// Non tutte le pendenze caricate hanno il numero avviso
					// In questo caso posso saltare alla successiva.
					// Se lo hanno, controllo che non sia oggetto di una precedente generazione
					if(numeroAvviso == null || numeriAvviso.contains(idDominio + numeroAvviso)) continue;
					
					numeriAvviso.add(idDominio + numeroAvviso);

					leggiPendenzaDTO.setIdDominio(idDominio);
					leggiPendenzaDTO.setNumeroAvviso(numeroAvviso);
					LeggiPendenzaDTOResponse leggiPendenzaDTOResponse = pendenzeDAO.leggiAvvisoPagamento(leggiPendenzaDTO);

					String pdfFileName = idDominio + "_" + numeroAvviso + ".pdf"; 
					ZipEntry tracciatoOutputEntry = new ZipEntry(pdfFileName);
					zos.putNextEntry(tracciatoOutputEntry);
					zos.write(leggiPendenzaDTOResponse.getAvvisoPdf());
					zos.flush();
					zos.closeEntry();
				}
			}
		} 

		if(addError){
			ZipEntry tracciatoOutputEntry = new ZipEntry("errore.txt");
			zos.putNextEntry(tracciatoOutputEntry);
			zos.write("Attenzione: non sono presenti inserimenti andati a buon fine nel tracciato selezionato.".getBytes());
			zos.flush();
			zos.closeEntry();
		}
	}

	private void popolaZipTracciatoCSV(Authentication user, Tracciato tracciato, ZipOutputStream zos)
			throws ServiceException, PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException,
			IOException, ValidationException {
		boolean addError = true;
		TracciatiDAO tracciatiDAO = new TracciatiDAO();
		PendenzeDAO pendenzeDAO = new PendenzeDAO();

		ListaOperazioniTracciatoDTO listaOperazioniTracciatoDTO = new ListaOperazioniTracciatoDTO(user);

		int pagina = 1;
		listaOperazioniTracciatoDTO.setLimit(50);
		listaOperazioniTracciatoDTO.setPagina(pagina);
		listaOperazioniTracciatoDTO.setIdTracciato(tracciato.getId());
		listaOperazioniTracciatoDTO.setStato(StatoOperazioneType.ESEGUITO_OK);
		listaOperazioniTracciatoDTO.setTipo(TipoOperazioneType.ADD);

		ListaOperazioniTracciatoDTOResponse listaTracciatiDTOResponse = tracciatiDAO.listaOperazioniTracciatoPendenza(listaOperazioniTracciatoDTO);

		// Tengo traccia degli avvisi inseriti nello zip per tenere solo l'ultima versione.
		Set<String> numeriAvviso = new HashSet<String>();
		
		if(listaTracciatiDTOResponse.getTotalResults() > 0) {
			do {
				for (Operazione operazione : listaTracciatiDTOResponse.getResults()) {

					EsitoOperazionePendenza esitoOperazionePendenza = EsitoOperazionePendenza.parse(new String(operazione.getDatiRisposta()));

					if(esitoOperazionePendenza.getStato().equals(StatoOperazionePendenza.ESEGUITO) && esitoOperazionePendenza.getEsito().equals("ADD_OK")) { 
						addError = false; // ho trovato almeno un avviso da stampare

						LeggiPendenzaDTO leggiPendenzaDTO = new LeggiPendenzaDTO(user);

						String idDominio = null;
						String numeroAvviso = null;
						try {
							Avviso avviso = (Avviso) esitoOperazionePendenza.getDati();
							idDominio = avviso.getIdDominio();
							numeroAvviso = avviso.getNumeroAvviso();
						} catch (Exception e) {
							java.util.LinkedHashMap<?,?> map = (LinkedHashMap<?, ?>) esitoOperazionePendenza.getDati();
							idDominio =(String)map.get("idDominio");
							numeroAvviso =(String)map.get("numeroAvviso");
						}

						// Non tutte le pendenze caricate hanno il numero avviso
						// In questo caso posso saltare alla successiva.
						// Se lo hanno, controllo che non sia oggetto di una precedente generazione
						if(numeroAvviso == null || numeriAvviso.contains(idDominio + numeroAvviso)) continue;
						
						numeriAvviso.add(idDominio + numeroAvviso);
						
						leggiPendenzaDTO.setIdDominio(idDominio);
						leggiPendenzaDTO.setNumeroAvviso(numeroAvviso);
						LeggiPendenzaDTOResponse leggiPendenzaDTOResponse = pendenzeDAO.leggiAvvisoPagamento(leggiPendenzaDTO);

						String pdfFileName = idDominio + "_" + numeroAvviso + ".pdf"; 
						ZipEntry tracciatoOutputEntry = new ZipEntry(pdfFileName );
						zos.putNextEntry(tracciatoOutputEntry);
						zos.write(leggiPendenzaDTOResponse.getAvvisoPdf());
						zos.flush();
						zos.closeEntry();
					}
				}

				pagina ++;
				listaOperazioniTracciatoDTO.setPagina(pagina);
				listaTracciatiDTOResponse = tracciatiDAO.listaOperazioniTracciatoPendenza(listaOperazioniTracciatoDTO);

			}while(!listaTracciatiDTOResponse.getResults().isEmpty());
		}

		if(addError){
			ZipEntry tracciatoOutputEntry = new ZipEntry("errore.txt");
			zos.putNextEntry(tracciatoOutputEntry);
			zos.write("Attenzione: non sono presenti inserimenti andati a buon fine nel tracciato selezionato.".getBytes());
			zos.flush();
			zos.closeEntry();
		}
	}

	private String getBodyPartFileName (BodyPart bodyPart) throws Exception{
		String partName =  null;
		String[] headers = bodyPart.getHeader(BaseController.PARAMETRO_CONTENT_DISPOSITION);
		if(headers != null && headers.length > 0){
			String header = headers[0];

			// in due parti perche il suffisso con solo " imbrogliava il controllo
			int prefixIndex = header.indexOf(BaseController.PREFIX_FILENAME);
			if(prefixIndex > -1){
				partName = header.substring(prefixIndex + BaseController.PREFIX_FILENAME.length());

				int suffixIndex = partName.indexOf(BaseController.SUFFIX_FILENAME);
				partName = partName.substring(0,suffixIndex);
			}
		}

		return partName;
	}

}


