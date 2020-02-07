package it.govpay.backoffice.v1.controllers;

import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openspcoop2.utils.json.ValidationException;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.backoffice.v1.beans.ListaOperatori;
import it.govpay.backoffice.v1.beans.Operatore;
import it.govpay.backoffice.v1.beans.OperatorePost;
import it.govpay.backoffice.v1.beans.PatchOp;
import it.govpay.backoffice.v1.beans.PatchOp.OpEnum;
import it.govpay.backoffice.v1.beans.converter.OperatoriConverter;
import it.govpay.backoffice.v1.beans.converter.PatchOpConverter;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.core.dao.anagrafica.dto.FindOperatoriDTO;
import it.govpay.core.dao.anagrafica.dto.FindOperatoriDTOResponse;
import it.govpay.core.dao.anagrafica.dto.LeggiOperatoreDTO;
import it.govpay.core.dao.anagrafica.dto.LeggiOperatoreDTOResponse;
import it.govpay.core.dao.anagrafica.dto.PutOperatoreDTO;
import it.govpay.core.dao.anagrafica.dto.PutOperatoreDTOResponse;
import it.govpay.core.dao.anagrafica.exception.DominioNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.TipoVersamentoNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.UnitaOperativaNonTrovataException;
import it.govpay.core.dao.pagamenti.dto.OperatorePatchDTO;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;

public class OperatoriController extends BaseController {

     public OperatoriController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }

    public Response addOperatore(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String principal, java.io.InputStream is) {
    	String methodName = "addOperatore";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_RUOLI), Arrays.asList(Diritti.SCRITTURA));
			
			String jsonRequest = baos.toString();
			OperatorePost operatoreRequest= JSONSerializable.parse(jsonRequest, OperatorePost.class);
			
			if(principal != null)
				principal =  URLDecoder.decode(principal, StandardCharsets.UTF_8.toString());
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdOperatore("principal", principal);
			
			operatoreRequest.validate();
			
			PutOperatoreDTO putOperatoreDTO = OperatoriConverter.getPutOperatoreDTO(operatoreRequest, principal, user); 
			
			UtentiDAO operatoriDAO = new UtentiDAO(false);
			
			PutOperatoreDTOResponse putOperatoreDTOResponse = null;
			try {
				putOperatoreDTOResponse = operatoriDAO.createOrUpdate(putOperatoreDTO);
			} catch(DominioNonTrovatoException | TipoVersamentoNonTrovatoException | UnitaOperativaNonTrovataException e) {
				throw new UnprocessableEntityException(e.getDetails());
			}
			
			Status responseStatus = putOperatoreDTOResponse.isCreated() ?  Status.CREATED : Status.OK;
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(responseStatus),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
    }



    public Response getOperatore(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , String principal) {
    	String methodName = "getOperatore";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_RUOLI), Arrays.asList(Diritti.LETTURA));
			
			if(principal != null)
				principal =  URLDecoder.decode(principal, StandardCharsets.UTF_8.toString());
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdOperatore("principal", principal);
			
			// Parametri - > DTO Input
			
			LeggiOperatoreDTO leggiOperatoreDTO = new LeggiOperatoreDTO(user);
			leggiOperatoreDTO.setPrincipal(principal);
			
			// INIT DAO
			
			UtentiDAO operatoriDAO = new UtentiDAO(false);
			
			// CHIAMATA AL DAO
			
			LeggiOperatoreDTOResponse getOperatoreDTOResponse = operatoriDAO.getOperatore(leggiOperatoreDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			Operatore response = OperatoriConverter.toRsModel(getOperatoreDTOResponse.getOperatore());
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
    }



    @SuppressWarnings("unchecked")
	public Response updateOperatore(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , java.io.InputStream is, String principal) {
    	String methodName = "updateOperatore";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try(ByteArrayOutputStream baos= new ByteArrayOutputStream();){
			// salvo il json ricevuto
			IOUtils.copy(is, baos);
			
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_RUOLI), Arrays.asList(Diritti.SCRITTURA));

			if(principal != null)
				principal =  URLDecoder.decode(principal, StandardCharsets.UTF_8.toString());
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdOperatore("principal", principal);
			
			String jsonRequest = baos.toString();

			OperatorePatchDTO operatorePatchDTO = new OperatorePatchDTO(user);
			operatorePatchDTO.setIdOperatore(principal);
			
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
			} catch (Exception e) {
				lstOp = JSONSerializable.parse(jsonRequest, List.class);
			}
			
			operatorePatchDTO.setOp(PatchOpConverter.toModel(lstOp));

			UtentiDAO utentiDAO = new UtentiDAO(false);
			
			LeggiOperatoreDTOResponse pagamentoPortaleDTOResponse = utentiDAO.patch(operatorePatchDTO);
			
			Operatore response = OperatoriConverter.toRsModel(pagamentoPortaleDTOResponse.getOperatore());

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(null)),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
    }



    public Response findOperatori(Authentication user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String ordinamento, String campi, Boolean abilitato) {
    	String methodName = "findOperatori";  
		String transactionId = this.context.getTransactionId();
		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 
		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.OPERATORE, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.ANAGRAFICA_RUOLI), Arrays.asList(Diritti.LETTURA));
			
			// Parametri - > DTO Input
			
			FindOperatoriDTO listaDominiDTO = new FindOperatoriDTO(user);
			
			listaDominiDTO.setLimit(risultatiPerPagina);
			listaDominiDTO.setPagina(pagina);
			listaDominiDTO.setOrderBy(ordinamento);
			listaDominiDTO.setAbilitato(abilitato);
			listaDominiDTO.setOrderBy(ordinamento); 
			
			// INIT DAO
			
			UtentiDAO operatoriDAO = new UtentiDAO(false);
			
			// CHIAMATA AL DAO
			
			FindOperatoriDTOResponse listaOperatoriDTOResponse = operatoriDAO.findOperatori(listaDominiDTO);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.backoffice.v1.beans.OperatoreIndex> results = new ArrayList<>();
			for(it.govpay.bd.model.Operatore operatore: listaOperatoriDTOResponse.getResults()) {
				results.add(OperatoriConverter.toRsModelIndex(operatore));
			}
			
			ListaOperatori response = new ListaOperatori(results, this.getServicePath(uriInfo),
					listaOperatoriDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			
			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSON(campi)),transactionId).build();
			
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(this.context);
		}
    }
}
