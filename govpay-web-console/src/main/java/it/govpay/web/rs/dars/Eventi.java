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
/*
 * GovPay - Porta di Accesso al Nodo dei Eventi SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
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
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;
import it.govpay.bd.registro.EventoFilter;
import it.govpay.dars.bd.EntiBD;
import it.govpay.dars.bd.EventiBD;
import it.govpay.dars.model.ListaEventiEntry;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;

@Path("/dars/eventi")
public class Eventi extends BaseRsService {
	
	Logger log = LogManager.getLogger();
	
	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse listaEntry(
			@QueryParam(value = "operatore") String principalOperatore,
			@QueryParam(value = "esito") String esito,
			@QueryParam(value = "codDominio") @DefaultValue(value="") String codDominio ,
			@QueryParam(value = "categoria") String categoria,
			@QueryParam(value = "tipo") String tipo,
			@QueryParam(value = "sottotipo") String sottotipo,
			@QueryParam(value = "iuv") String iuv,
			@QueryParam(value = "offset") @DefaultValue(value="0") int offset,
			@QueryParam(value = "limit") @DefaultValue(value="25") int limit) throws GovPayException {
		initLogger("listaEntryEventi");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] esito["+esito+"] codDominio["+codDominio+"] categoria["+categoria+"]"
				+ " tipo["+tipo+"] sottotipo["+sottotipo+"] iuv["+iuv+"] offset["+offset+"] limit["+limit+"]");

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			
			Operatore operatore = getOperatoreByPrincipal(bd);
			
			EventiBD eventiBD = new EventiBD(bd);
			EventoFilter filter = eventiBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			filter.setEsito(esito);
			filter.setIuv(iuv);
			filter.setCategoria(categoria);
			filter.setTipo(tipo);
			filter.setSottotipo(sottotipo); 
			filter.setCodDominio(codDominio);
			
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Evento.model().DATA_ORA_EVENTO);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			List<ListaEventiEntry> findAll = null;

			// Se l'utente non e' admin puo'vedere solo gli enti che sono associati a lui.
			if(!ProfiloOperatore.ADMIN.equals(operatore.getProfilo())){
				EntiBD entiBD = new EntiBD(bd);
				List<String> listaIdDominiByIdEnti = entiBD.getListaCodDominiByIdEnti(operatore.getIdEnti());
				
				if(listaIdDominiByIdEnti  == null || listaIdDominiByIdEnti.isEmpty()) 
					findAll = new ArrayList<ListaEventiEntry>();
				else {
					filter.setCodDominiAbilitati(listaIdDominiByIdEnti);
				}
			}

			if(findAll == null)
				findAll = eventiBD.findAll(filter);
			
			
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(findAll);

		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca degli eventi:" +e.getMessage() , e);
			throw e;
		}catch (Exception e) {
			log.error("Riscontrato errore durante l'esecuzione della ricerca degli eventi:" +e.getMessage() , e);
			
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
	public DarsResponse evento(
			@QueryParam(value = "operatore") String principalOperatore,
			@PathParam("id") long id) throws GovPayException {
		initLogger("getEventi");
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
			EventiBD eventiBD = new EventiBD(bd);
			
			
			Evento evento = eventiBD.get(id);
			
			Operatore operatore = this.getOperatoreByPrincipal(bd);
			EntiBD entiBD = new EntiBD(bd);
			List<String> listaIdDominiByIdEnti = entiBD.getListaCodDominiByIdEnti(operatore.getIdEnti());

			boolean authorized = false;
			if(ProfiloOperatore.ADMIN.equals(operatore.getProfilo())) { //Admin vede tutto
				authorized = true;
			} else {
				if(listaIdDominiByIdEnti != null) {
					for(String codDominio: listaIdDominiByIdEnti) {
						if(codDominio.equals(evento.getCodDominio())) {
							authorized = true;
							break;
						}
					}
				}
			}

			if(!authorized) {
				throw new WebApplicationException(getUnauthorizedResponse());
			}	
			
			darsResponse.setResponse(evento);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca dell'evento:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'esecuzione del metodo evento:" +e.getMessage() , e);
			
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
}

