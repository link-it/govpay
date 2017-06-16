package it.govpay.web.rs.dars.base;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import it.govpay.bd.BasicBD;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.handler.IStatisticaDarsHandler;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;
import it.govpay.web.rs.dars.model.statistiche.PaginaGrafico;
import it.govpay.web.utils.Utils;

public abstract class StatisticaDarsService extends BaseDarsService {

	public StatisticaDarsService() {
		super();	
	}
	
	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse grafico( 	@Context UriInfo uriInfo) throws ConsoleException,WebApplicationException {
		String methodName = "grafico " + this.getNomeServizio(); 
		this.initLogger(methodName);

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		BasicBD bd = null;

		try{
			bd = BasicBD.newInstance(this.codOperazione);
			bd.setIdOperatore(this.getOperatoreByPrincipal(bd).getId());
			PaginaGrafico grafico = this.getDarsHandler().getGrafico(uriInfo,bd);

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(grafico);
		}catch(WebApplicationException e){
			this.log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			this.log.error("Riscontrato errore durante l'esecuzione del metodo "+methodName+":" +e.getMessage() , e);
			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.getNomeServizio()+".elenco.erroreGenerico"));
		}finally {
			this.response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		this.log.info("Richiesta "+methodName +" evasa con successo");
		return darsResponse;
	}
	
	public abstract IStatisticaDarsHandler<?> getDarsHandler();
}
