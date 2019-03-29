package it.govpay.pendenze.v1.controller;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

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
import it.govpay.core.utils.GovpayConfig;

import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.IContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
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
import it.govpay.pendenze.v1.beans.converter.PatchOpConverter;
import it.govpay.pendenze.v1.beans.converter.PendenzeConverter;



public class PendenzeController extends BaseController {

     public PendenzeController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_BACKOFFICE_OPEN_API_FILE_NAME);
     }
     
     public Response pendenzeIdA2AIdPendenzaGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, String idPendenza) {
		String methodName = "getByIda2aIdPendenza";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdApplicazione("idA2A", idA2A);
			
			LeggiPendenzaDTO leggiPendenzaDTO = new LeggiPendenzaDTO(user);
			
			leggiPendenzaDTO.setCodA2A(idA2A);
			leggiPendenzaDTO.setCodPendenza(idPendenza);
			
			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 
			
			LeggiPendenzaDTOResponse leggiPendenzaDTOResponse = pendenzeDAO.leggiPendenza(leggiPendenzaDTO);

			Pendenza pendenza = PendenzeConverter.toRsModel(leggiPendenzaDTOResponse.getVersamentoIncasso(), leggiPendenzaDTOResponse.getRpts());
			return this.handleResponseOk(Response.status(Status.OK).entity(pendenza.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }
    
    public Response pendenzeGET(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, String idDominio, String idA2A, String idDebitore, String stato, String idPagamento) {
    	IContext ctx = null;
    	String transactionId = null;
		ByteArrayOutputStream baos= null;
		String methodName = "pendenzeGET";
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input
			
			ListaPendenzeDTO listaPendenzeDTO = new ListaPendenzeDTO(user);
			
			listaPendenzeDTO.setPagina(pagina);
			listaPendenzeDTO.setLimit(risultatiPerPagina);
			listaPendenzeDTO.setStato(stato);
			
			if(idDominio != null)
				listaPendenzeDTO.setIdDominio(idDominio);
			if(idA2A != null)
				listaPendenzeDTO.setIdA2A(idA2A);
			if(idDebitore != null)
				listaPendenzeDTO.setIdDebitore(idDebitore);
			
			if(idPagamento != null)
				listaPendenzeDTO.setIdPagamento(idPagamento);
			
			if(ordinamento != null)
				listaPendenzeDTO.setOrderBy(ordinamento);
			// INIT DAO
			
			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 
			
			// CHIAMATA AL DAO
			
			ListaPendenzeDTOResponse listaPendenzeDTOResponse = pendenzeDAO.listaPendenze(listaPendenzeDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<PendenzaIndex> results = new ArrayList<>();
			for(LeggiPendenzaDTOResponse ricevutaDTOResponse: listaPendenzeDTOResponse.getResults()) {
				PendenzaIndex rsModel = PendenzeConverter.toRsIndexModel(ricevutaDTOResponse.getVersamentoIncasso());
				results.add(rsModel);
			}
			
			ListaPendenze response = new ListaPendenze(results, this.getServicePath(uriInfo),
					listaPendenzeDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(campi), 200);
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }

    @SuppressWarnings("unchecked")
	public Response pendenzeIdA2AIdPendenzaPATCH(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, String idPendenza, java.io.InputStream is) {
    	String methodName = "pendenzeIdA2AIdPendenzaPATCH";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			// salvo il json ricevuto
			IOUtils.copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdApplicazione("idA2A", idA2A);
			
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
			
			pendenzeDAO.patch(patchPendenzaDTO);
			
			LeggiPendenzaDTO leggiPendenzaDTO = new LeggiPendenzaDTO(user);
			
			leggiPendenzaDTO.setCodA2A(idA2A);
			leggiPendenzaDTO.setCodPendenza(idPendenza);
			
			LeggiPendenzaDTOResponse leggiPendenzaDTOResponse = pendenzeDAO.leggiPendenza(leggiPendenzaDTO);

			Pendenza pendenza = PendenzeConverter.toRsModel(leggiPendenzaDTOResponse.getVersamentoIncasso(), leggiPendenzaDTOResponse.getRpts());
			return this.handleResponseOk(Response.status(Status.OK).entity(pendenza.toJSON(null)),transactionId).build();
		} catch(GovPayException e) {
			this.log.error("Errore durante il processo di pagamento", e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.OPERAZIONE);
			respKo.setCodice(e.getCodEsito().name());
			respKo.setDescrizione(e.getDescrizioneEsito());
			respKo.setDettaglio(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo.toJSON(null), 500);
			}catch(Exception e1) {
				this.log.error("Errore durante il log della risposta", e1);
			}
			return this.handleResponseOk(Response.status(Status.INTERNAL_SERVER_ERROR).entity(this.getRespJson(respKo)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }



    public Response pendenzeIdA2AIdPendenzaPUT(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String idA2A, String idPendenza, java.io.InputStream is, Boolean stampaAvviso, Boolean avvisaturaDigitale, ModalitaAvvisaturaDigitale modalitaAvvisaturaDigitale) {
    	String methodName = "pendenzeIdA2AIdPendenzaPUT";  
		IContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			baos = new ByteArrayOutputStream();
			// salvo il json ricevuto
			IOUtils.copy(is, baos);
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdApplicazione("idA2A", idA2A);
			
			String jsonRequest = baos.toString();
			PendenzaPut pendenzaPost= JSONSerializable.parse(jsonRequest, PendenzaPut.class);
			pendenzaPost.validate();
			
			ValidatorFactory vf = ValidatorFactory.newInstance();
			vf.getValidator("idPendenza", idPendenza).notNull().minLength(1).maxLength(35);
			vf.getValidator("idA2A", idA2A).minLength(1).maxLength(35);
			
			Versamento versamento = PendenzeConverter.getVersamentoFromPendenza(pendenzaPost, idA2A, idPendenza);
			
			
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
			pc.setNumeroAvviso(createOrUpdate.getVersamento().getNumeroAvviso());
			pc.pdf(createOrUpdate.getPdf());
			Status responseStatus = createOrUpdate.isCreated() ?  Status.CREATED : Status.OK;
			this.logResponse(uriInfo, httpHeaders, methodName, pc.toJSON(null), responseStatus.getStatusCode());
			this.log.info(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus).entity(pc.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ctx);
		}
    }
	
}


