package it.govpay.rs.v1.controllers.pagamenti;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.core.rs.v1.beans.pagamenti.ListaAcl;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.v1.beans.pagamenti.converter.AclConverter;

public class ProfiloController extends it.govpay.rs.BaseController {

     public ProfiloController(String nomeServizio,Logger log) {
		super(nomeServizio,log, GovpayConfig.GOVPAY_PAGAMENTI_OPEN_API_FILE);
     }



    public Response profiloGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina, String campi) {
    	String methodName = "profiloGET";  
		GpContext ctx = null;
		String transactionId = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			transactionId = ctx.getTransactionId();
			
			// Parametri - > DTO Input

			// INIT DAO
			
			UtentiDAO utentiDAO = new UtentiDAO();
			
			// CHIAMATA AL DAO
			
			utentiDAO.populateUser(user);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.core.rs.v1.beans.pagamenti.Acl> results = new ArrayList<it.govpay.core.rs.v1.beans.pagamenti.Acl>();
			for(Acl acl: user.getAcls()) {
				results.add(AclConverter.toRsModel(acl));
			}

			ListaAcl response = new ListaAcl(results, this.getServicePath(uriInfo),
					results.size(), 1, results.size());

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSONArray(campi), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response.toJSONArray(campi)),transactionId).build();
			
		}catch (Exception e) {
			return handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			if(ctx != null) ctx.log();
		}
    }


}


