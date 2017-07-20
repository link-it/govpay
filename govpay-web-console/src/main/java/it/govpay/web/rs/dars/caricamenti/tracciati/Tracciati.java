package it.govpay.web.rs.dars.caricamenti.tracciati;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import it.govpay.bd.BasicBD;
import it.govpay.model.Acl.Servizio;
import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.handler.IDarsHandler;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.Elemento;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.utils.Utils;

@Path("/dars/caricamenti/tracciati")
public class Tracciati extends DarsService {

	public Tracciati() {
		super();
	}
	
	@Override
	public String getNomeServizio() {
		return "caricamentoTracciati";
	}

	@Override
	public IDarsHandler<?> getDarsHandler() {
		return new TracciatiHandler(this.log, this);
	}
	
	@Override
	public String getPathServizio() {
		return "/dars/caricamenti/tracciati";// + this.getNomeServizio();
	}
	
	@Override
	public Servizio getFunzionalita() {
		return Servizio.Gestione_Pagamenti;
	}
	
	@GET
	@Path("/{id}/refresh") 
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse refresh(
			@PathParam("id") long id,
			@Context UriInfo uriInfo) throws ConsoleException,WebApplicationException {
		String methodName = "Refresh " + this.getNomeServizio() + ".id [" + id + "]"; 
		this.initLogger(methodName);

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			bd = BasicBD.newInstance(this.codOperazione);
			bd.setIdOperatore(this.getOperatoreByPrincipal(bd).getId());

			Elemento elemento = ((TracciatiHandler) this.getDarsHandler()).getElemento(id,uriInfo,bd);

			darsResponse.setResponse(elemento);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		} catch(WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			this.log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.getNomeServizio()+".refresh.erroreGenerico"));
		}finally {
			this.response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		this.log.info("Richiesta evasa con successo");
		return darsResponse;
	}
}
