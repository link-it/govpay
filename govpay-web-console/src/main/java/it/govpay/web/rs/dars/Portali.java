/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.web.rs.dars;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.filters.ApplicazioneFilter;
import it.govpay.bd.anagrafica.filters.PortaleFilter;
import it.govpay.dars.bd.ApplicazioniBD;
import it.govpay.dars.bd.PortaliBD;
import it.govpay.dars.model.ListaApplicazioniEntry;
import it.govpay.dars.model.ListaPortaliEntry;
import it.govpay.dars.model.PortaleExt;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;

@Path("/dars/portali")
public class Portali extends BaseRsService {

	public Portali() {
	}

	Logger log = LogManager.getLogger();

	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse find(
			@QueryParam(value = "operatore") String principalOperatore,
			@QueryParam(value = "offset") @DefaultValue(value="0") int offset,
			@QueryParam(value = "limit") @DefaultValue(value="25") int limit) throws GovPayException {
		initLogger("findPortali");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] offset["+offset+"] limit["+limit+"]");

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		
		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			
			this.checkOperatoreAdmin(bd);
			PortaliBD portaliBD = new PortaliBD(bd);
			PortaleFilter filter = portaliBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Portale.model().COD_PORTALE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			List<ListaPortaliEntry> findAll = portaliBD.findAll(filter);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(findAll);
			
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca delle applicazioni:" +e.getMessage() , e);
			throw e;
		}  catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca delle applicazioni:" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();
			
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}

	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse get(
			@QueryParam(value = "operatore") String principalOperatore,
			@PathParam("id") long id) throws GovPayException {
		initLogger("getPortali");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] id["+id+"]");

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		
		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			this.checkOperatoreAdmin(bd);

			PortaliBD portaliBD = new PortaliBD(bd);
			PortaleExt portaleExt = portaliBD.getPortale(id);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(portaleExt);
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca dell'applicazione:" +e.getMessage() , e);
			throw e;
		}  catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca dell'applicazione:" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}

	@PUT
	@Path("/")
	@Consumes({MediaType.APPLICATION_JSON})
	public DarsResponse update(
			@QueryParam(value = "operatore") String principalOperatore,
			PortaleExt portale) throws GovPayException {
		initLogger("updatePortali");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] portale["+portale+"]");
		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		
		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			
			this.checkOperatoreAdmin(bd);

			PortaliBD portaliBD = new PortaliBD(bd);
			portaliBD.updatePortale(portale);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);//TODO discriminare tra ESEGUITA e NONESEGUITA

		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'aggiornamento del portale:" +e.getMessage() , e);
			throw e;
		}  catch (Exception e) {
			log.error("Riscontrato errore durante l'aggioramento del portale:" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}

	@POST
	@Path("/")
	@Consumes({MediaType.APPLICATION_JSON})
	public DarsResponse insert(
			@QueryParam(value = "operatore") String principalOperatore,
			PortaleExt portale) throws GovPayException {
		initLogger("insertPortale");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] portale["+portale+"]");

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		
		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			
			this.checkOperatoreAdmin(bd);

			PortaliBD portaliBD = new PortaliBD(bd);
			portaliBD.insertPortale(portale);
			Long id = portale.getPortale().getId();
			
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(id);

		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'inserimento dell'applicazione:" +e.getMessage() , e);
			throw e;
		}  catch (Exception e) {
			log.error("Riscontrato errore durante l'inserimento dell'applicazione:" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();
			
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}
	
	@GET
	@Path("/{id}/applicazioni")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse findTributi(
			@PathParam("id") long idPortale,
			@QueryParam(value = "operatore") String principalOperatore,
			@QueryParam(value = "escludeOwnedApp") boolean escludiApplicazioniGiaAssociate,
			@QueryParam(value = "offset") @DefaultValue(value="0") int offset,
			@QueryParam(value = "limit") @DefaultValue(value="25") int limit) throws GovPayException {
		initLogger("findTributi");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] idPortale["+idPortale+"]  offset["+offset+"] limit["+limit+"]");
		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		
		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			
			this.checkOperatoreAdmin(bd);

			ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
			List<ListaApplicazioniEntry> findAll  = new ArrayList<ListaApplicazioniEntry>();
			ApplicazioneFilter filter = applicazioniBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Applicazione.model().COD_APPLICAZIONE);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			
			List<Long> idApplicazioni = new ArrayList<Long>();
			
			if(idPortale > 0 ){
				PortaliBD portaliBD = new PortaliBD(bd);
				PortaleExt portaleExt = portaliBD.getPortale(idPortale);
				idApplicazioni = portaleExt.getPortale().getIdApplicazioni();
				if(escludiApplicazioniGiaAssociate)
					findAll = applicazioniBD.findAllApplicazioniNonInLista(filter,idApplicazioni);
				else 
					findAll = applicazioniBD.findAll(filter,idApplicazioni);
			
			}else {
				findAll = applicazioniBD.findAll(filter);
			}

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(findAll);

		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca dei tributi:" +e.getMessage() , e);
			throw e;
		}  catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca dei tributi:" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();
			//			if(e instanceof GovPayException)
			//				throw (GovPayException) e;

			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}

		log.info("Richiesta evasa con successo");
		return darsResponse;
	}
	
}