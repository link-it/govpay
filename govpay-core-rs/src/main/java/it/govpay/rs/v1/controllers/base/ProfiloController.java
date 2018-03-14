package it.govpay.rs.v1.controllers.base;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;

import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.core.rs.v1.beans.FaultBean;
import it.govpay.core.rs.v1.beans.ListaAcl;
import it.govpay.core.rs.v1.beans.base.FaultBean.CategoriaEnum;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.v1.beans.converter.AclConverter;

public class ProfiloController extends it.govpay.rs.BaseController {

     public ProfiloController(String nomeServizio,Logger log) {
		super(nomeServizio,log);
     }



    public Response profiloGET(IAutorizzato user, UriInfo uriInfo, HttpHeaders httpHeaders , Integer pagina, Integer risultatiPerPagina) {
    	String methodName = "profiloGET";  
		GpContext ctx = null;
		ByteArrayOutputStream baos= null;
		this.log.info("Esecuzione " + methodName + " in corso..."); 
		try{
			baos = new ByteArrayOutputStream();
			this.logRequest(uriInfo, httpHeaders, methodName, baos);
			
			ctx =  GpThreadLocal.get();
			
			// Parametri - > DTO Input

			// INIT DAO
			
			UtentiDAO utentiDAO = new UtentiDAO();
			
			// CHIAMATA AL DAO
			
			utentiDAO.populateUser(user);
			
			// CONVERT TO JSON DELLA RISPOSTA
			
			List<it.govpay.core.rs.v1.beans.ACL> results = new ArrayList<it.govpay.core.rs.v1.beans.ACL>();
			for(Acl acl: user.getAcls()) {
				results.add(AclConverter.toRsModel(acl));
			}

			ListaAcl response = new ListaAcl(results, uriInfo.getRequestUri(),
					results.size(), 1, results.size());

			this.logResponse(uriInfo, httpHeaders, methodName, response.toJSON(null), 200);
			this.log.info("Esecuzione " + methodName + " completata."); 
			return Response.status(Status.OK).entity(response.toJSON(null)).build();
			
		}catch (Exception e) {
			log.error("Errore interno durante la ricerca dei Ruoli: " + e.getMessage(), e);
			FaultBean respKo = new FaultBean();
			respKo.setCategoria(CategoriaEnum.INTERNO);
			respKo.setCodice("");
			respKo.setDescrizione(e.getMessage());
			try {
				this.logResponse(uriInfo, httpHeaders, methodName, respKo.toJSON(null), 500);
			}catch(Exception e1) {
				log.error("Errore durante il log della risposta", e1);
			}
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(respKo.toJSON(null)).build();
		} finally {
			if(ctx != null) ctx.log();
		}
    }


}


