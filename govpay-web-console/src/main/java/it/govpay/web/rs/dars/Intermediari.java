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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
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
import it.govpay.bd.anagrafica.IntermediariBD;
import it.govpay.bd.anagrafica.StazioniBD;
import it.govpay.bd.anagrafica.filters.IntermediarioFilter;
import it.govpay.bd.model.Intermediario;
import it.govpay.bd.model.Stazione;
import it.govpay.dars.model.IntermediarioExt;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;

@Path("/dars/intermediari")
public class Intermediari extends BaseRsService {

	public Intermediari() {
	}

	Logger log = LogManager.getLogger();

	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse find(
			@QueryParam(value = "operatore") String principalOperatore,
			@QueryParam(value = "offset") @DefaultValue(value="0") int offset,
			@QueryParam(value = "limit") @DefaultValue(value="25") int limit) throws GovPayException {
		initLogger("findIntermediari");
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

			IntermediariBD intermediariBD = new IntermediariBD(bd);
			IntermediarioFilter filter = intermediariBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Intermediario.model().COD_INTERMEDIARIO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			List<Intermediario> findAll = intermediariBD.findAll(filter);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(findAll);

		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca degli intermediari:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca degli intermediari:" +e.getMessage() , e);

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
		initLogger("getIntermediari");
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

			darsResponse.setResponse(getIntermediario(bd, id));
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);

		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca dell'intermediario:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca dell'intermediario:" +e.getMessage() , e);

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
	
	private IntermediarioExt getIntermediario(BasicBD bd, long id) throws Exception {
		IntermediariBD intermediariBD = new IntermediariBD(bd);
		Intermediario intermediario = intermediariBD.getIntermediario(id);
		StazioniBD stazioniBD = new StazioniBD(bd);
		List<Stazione> stazioni = stazioniBD.getStazioni(intermediario.getId());

		return new IntermediarioExt(intermediario, stazioni);
		
	}

	@PUT
	@Path("/")
	@Consumes({MediaType.APPLICATION_JSON})
	public DarsResponse update(
			@QueryParam(value = "operatore") String principalOperatore,
			IntermediarioExt intermediario) throws GovPayException {
		initLogger("updateIntermediari");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] intermediario["+intermediario+"]");

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
			
			IntermediarioExt oldIntermediario = getIntermediario(bd, intermediario.getId());
			this.checkIntermediario(intermediario, oldIntermediario);

			if(intermediario != null){
				IntermediariBD intermediariBD = new IntermediariBD(bd);
				intermediariBD.updateIntermediario(intermediario);

				if(intermediario.getStazioni() != null && intermediario.getStazioni().size() > 0){
					StazioniBD stazioniBD = new StazioniBD(bd);
					for (Stazione stazione : intermediario.getStazioni()) {
						if(stazione.getId() != null && stazione.getId().longValue() > 0)
							stazioniBD.updateStazione(stazione);
						else {
							stazione.setIdIntermediario(intermediario.getId()); 
							stazioniBD.insertStazione(stazione);
						}
					}
				}
			}

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);

		} catch(ValidationException e){
			log.error("Riscontrato errore di validazione durante l'aggiornamento dell'intermediario:" +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito("Dati di input non validi: " + e.getMessage());

		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'aggiornamento dell'intermediario:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'aggioramento dell'intermediario:" +e.getMessage() , e);

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
			IntermediarioExt intermediario) throws GovPayException {
		initLogger("insertIntermediari");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] intermediario["+intermediario+"]");

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

			this.checkIntermediario(intermediario, null);
			IntermediariBD intermediariBD = new IntermediariBD(bd);
			intermediariBD.insertIntermediario(intermediario);
			Long id = intermediario.getId();

			if(intermediario.getStazioni() != null && intermediario.getStazioni().size() > 0){
				StazioniBD stazioniBD = new StazioniBD(bd);
				for (Stazione stazione : intermediario.getStazioni()) {
					stazione.setIdIntermediario(id); 
					stazioniBD.insertStazione(stazione);
				}
			}

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(id);

		} catch(ValidationException e){
			log.error("Riscontrato errore di validazione durante l'inserimento dell'intermediario:" +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito("Dati di input non validi: " + e.getMessage());
			
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'inserimento dell'intermediario:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'inserimento dell'intermediario:" +e.getMessage() , e);

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
	@Path("/{id}/stazioni")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse findStazioni(
			@QueryParam(value = "operatore") String principalOperatore,
			@PathParam("id") long id,
			@QueryParam(value = "offset") @DefaultValue(value="0") int offset,
			@QueryParam(value = "limit") @DefaultValue(value="25") int limit) throws GovPayException {
		initLogger("findStazioniByIntermediario");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] idIntermediario["+id+"] offset["+offset+"] limit["+limit+"]");

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

			IntermediariBD intermediariBD = new IntermediariBD(bd);
			Intermediario intermediario = intermediariBD.getIntermediario(id);
			StazioniBD stazioniBD = new StazioniBD(bd);
			List<Stazione> findAll = stazioniBD.getStazioni(intermediario.getId());

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(findAll);

		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca delle stazioni:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca delle stazioni:" +e.getMessage() , e);

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
	
	public void checkIntermediario(IntermediarioExt intermediario, IntermediarioExt oldIntermediario) throws ValidationException {

		if(intermediario == null) throw new ValidationException("Intermediario nullo");
		if(intermediario.getCodIntermediario() == null) throw new ValidationException("CodIntermediario nullo");
		if(intermediario.getCodIntermediario().length() != 11) throw new ValidationException("Lunghezza del CodIntermediario errata. Dovrebbe essere 11, trovato "+intermediario.getCodIntermediario().length());
		
		if(intermediario.getConnettorePdd() == null)  throw new ValidationException("Connettore nullo");
		if(intermediario.getConnettorePdd().getUrl() == null)  throw new ValidationException("URL Connettore nullo");
		
		try {
			new URL(intermediario.getConnettorePdd().getUrl());
		} catch (MalformedURLException e) {
			throw new ValidationException("URL Connettore non valida");
		}
		
		if(intermediario.getConnettorePdd().getTipoAutenticazione() == null)  throw new ValidationException("TipoAutenticazione Connettore nullo");

		switch(intermediario.getConnettorePdd().getTipoAutenticazione()) {
		case HTTPBasic:
			if(intermediario.getConnettorePdd().getHttpUser() == null)  throw new ValidationException("Connettore con TipoAutorizzazione ["+intermediario.getConnettorePdd().getTipoAutenticazione()+"] non deve avere HttpUser nullo");
			if(intermediario.getConnettorePdd().getHttpPassw() == null)  throw new ValidationException("Connettore con TipoAutorizzazione ["+intermediario.getConnettorePdd().getTipoAutenticazione()+"] non deve avere HttpPassw nullo");
			break;
		case NONE:
			break;
		case SSL:
			if(intermediario.getConnettorePdd().getSslKsType() == null)  throw new ValidationException("Connettore con TipoAutorizzazione ["+intermediario.getConnettorePdd().getTipoAutenticazione()+"] non deve avere SslKsType nullo");
			if(intermediario.getConnettorePdd().getSslKsLocation() == null)  throw new ValidationException("Connettore con TipoAutorizzazione ["+intermediario.getConnettorePdd().getTipoAutenticazione()+"] non deve avere SslKsLocation nullo");
			if(intermediario.getConnettorePdd().getSslKsPasswd() == null)  throw new ValidationException("Connettore con TipoAutorizzazione ["+intermediario.getConnettorePdd().getTipoAutenticazione()+"] non deve avere SslKsPasswd nullo");
			if(intermediario.getConnettorePdd().getSslTsType() == null)  throw new ValidationException("Connettore con TipoAutorizzazione ["+intermediario.getConnettorePdd().getTipoAutenticazione()+"] non deve avere SslTsType nullo");
			if(intermediario.getConnettorePdd().getSslTsLocation() == null)  throw new ValidationException("Connettore con TipoAutorizzazione ["+intermediario.getConnettorePdd().getTipoAutenticazione()+"] non deve avere SslTsLocation nullo");
			if(intermediario.getConnettorePdd().getSslTsPasswd() == null)  throw new ValidationException("Connettore con TipoAutorizzazione ["+intermediario.getConnettorePdd().getTipoAutenticazione()+"] non deve avere SslTsPasswd nullo");
			if(intermediario.getConnettorePdd().getSslPKeyPasswd() == null)  throw new ValidationException("Connettore con TipoAutorizzazione ["+intermediario.getConnettorePdd().getTipoAutenticazione()+"] non deve avere SslPKeyPasswd nullo");
			break;
		default:throw new ValidationException("TipoAutenticazione Connettore ["+intermediario.getConnettorePdd().getTipoAutenticazione()+"] non valido");
		
		}
		
		if(intermediario.getStazioni() != null) {
			for(Stazione stazione: intermediario.getStazioni()) {
				if(stazione.getCodStazione() == null)  throw new ValidationException("CodStazione nullo");
				if(!stazione.getCodStazione().startsWith(intermediario.getCodIntermediario() + "_"))  throw new ValidationException("CodStazione ["+stazione.getCodStazione()+"] non valido rispetto al codIntermediario ["+intermediario.getCodIntermediario()+"]");
				if(stazione.getCodStazione().length() != (intermediario.getCodIntermediario().length() + 3))  throw new ValidationException("CodStazione ["+stazione.getCodStazione()+"] non valido rispetto al codIntermediario ["+intermediario.getCodIntermediario()+"]");
				if(stazione.getPassword() == null || stazione.getPassword().isEmpty())   throw new ValidationException("Password stazione nulla");
				if(stazione.getPassword().contains(" ")) throw new ValidationException("Password stazione non valida. Carattere [ ] non ammesso");
			}
		}
		
		if(oldIntermediario != null) { //caso update
			if(!oldIntermediario.getCodIntermediario().equals(intermediario.getCodIntermediario())) throw new ValidationException("CodIntermediario non deve cambiare in update. Atteso ["+oldIntermediario.getCodIntermediario()+"] trovato ["+intermediario.getCodIntermediario()+"]");
			
			List<Stazione> stazioniOld = null;
			if(oldIntermediario.getStazioni() != null && !oldIntermediario.getStazioni().isEmpty()) {
				Stazione[] oldStazioniArray = oldIntermediario.getStazioni().toArray(new Stazione[] {});
				Arrays.sort(oldStazioniArray);
				stazioniOld = Arrays.asList(oldStazioniArray);
			}
			
			List<Stazione> stazioniNew = null;
			if(intermediario.getStazioni() != null && !intermediario.getStazioni().isEmpty()) {
				Stazione[] newStazioniArray = intermediario.getStazioni().toArray(new Stazione[] {});
				Arrays.sort(newStazioniArray);
				stazioniNew = Arrays.asList(newStazioniArray);
			}
			
			if(stazioniOld != null) {
				if(stazioniNew == null) throw new ValidationException("Nessuna stazione trovata in update");
				
				
				for(Stazione old: stazioniOld) {
					boolean found = false;
					for(Stazione newS: stazioniNew) {
						if(newS.getCodStazione().equals(old.getCodStazione())) found = true;
					}
					if(!found)  throw new ValidationException("Stazione ["+old.getCodStazione()+"] non trovata in update");
				}
			}
						
			
		}
	}
}

