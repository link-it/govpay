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
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Ente;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;
import it.govpay.dars.bd.DominiBD;
import it.govpay.dars.bd.EntiBD;
import it.govpay.dars.model.DominioExt;
import it.govpay.dars.model.ListaDominiEntry;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.utils.DominiUtils;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;

@Path("/dars/domini")
public class Domini extends BaseRsService{

	Logger log = LogManager.getLogger();

	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse find(
			@QueryParam(value = "operatore") String principalOperatore,
			@QueryParam(value = "codStazione") String codStazione,
			@QueryParam(value = "offset") @DefaultValue(value="0") int offset,
			@QueryParam(value = "limit") @DefaultValue(value="25") int limit) throws GovPayException {
		initLogger("findDomini");

		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] codStazione["+codStazione+"] offset["+offset+"] limit["+limit+"]");
		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(ThreadContext.get("op"));

		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}

			Operatore operatore = this.getOperatoreByPrincipal(bd);

			DominiBD dominiBD = new DominiBD(bd);
			DominioFilter filter = dominiBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			filter.setCodStazione(codStazione);

			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);



			// Se l'utente e' operatore  puo' vedere solo i domini per cui e' associato
			List<ListaDominiEntry> findAllListaEntries = null; 

			if(!ProfiloOperatore.ADMIN.equals(operatore.getProfilo())){
				EntiBD entiBD = new EntiBD(bd);
				List<Long> listaIdDominiByIdEnti = entiBD.getListaIdDominiByIdEnti(operatore.getIdEnti());

				if(listaIdDominiByIdEnti == null || listaIdDominiByIdEnti.isEmpty())
					findAllListaEntries = new ArrayList<ListaDominiEntry>();
				else 
					filter.setIdDomini(listaIdDominiByIdEnti); 
			} 

			if(findAllListaEntries == null)
				findAllListaEntries = 	dominiBD.findAllListaEntries(filter);

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(findAllListaEntries);
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca dei domini:" +e.getMessage() , e);
			throw e;
		}  catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca dei domini:" +e.getMessage() , e);

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
		initLogger("getDominio");
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

			DominiBD dominiBD = new DominiBD(bd);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(dominiBD.getDominio(id));

		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca del dominio:" +e.getMessage() , e);
			throw e;
		}catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca del dominio:" +e.getMessage() , e);

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
			DominioExt dominio) throws GovPayException {
		initLogger("updateDomini");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] dominio["+dominio+"]");

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

			DominiBD dominiBD = new DominiBD(bd);
			dominiBD.updateDominioExt(dominio);
			DominiUtils.updateTabellaControparti(bd, dominio.getId());
			DominiUtils.updateContiAccredito(bd, dominio.getId());
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);//TODO discriminare tra ESEGUITA e NONESEGUITA

		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante 'aggiornamento del dominio:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'aggiornamento della dominio:" +e.getMessage() , e);

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
			DominioExt dominio) throws GovPayException {
		initLogger("createDomini");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] dominio["+dominio+"]");

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

			DominiBD dominiBD = new DominiBD(bd);
			dominiBD.insertDominioExt(dominio);
			DominiUtils.updateTabellaControparti(bd, dominio.getId());
			DominiUtils.updateContiAccredito(bd, dominio.getId());
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(dominio.getId());
		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'inserimento del dominio:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'inserimento della dominio:" +e.getMessage() , e);

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



	/*** IBAN ACCREDITO **/

	@GET
	@Path("/{id}/ibanaccredito")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse findIbanaccreditoDominio(
			@PathParam("id") long idDominio,
			@QueryParam(value = "operatore") String principalOperatore,
			@QueryParam(value = "offset") @DefaultValue(value="0") int offset,
			@QueryParam(value = "limit") @DefaultValue(value="25") int limit) throws GovPayException {
		initLogger("getIbanAccreditoByDominio");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] offset["+offset+"] limit["+limit+"]");

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(ThreadContext.get("op"));//this.codOperazione);

		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}

			this.checkOperatoreAdmin(bd);

			DominiBD dominiBD = new DominiBD(bd);

			darsResponse.setResponse(dominiBD.getIbanAccreditoByIdDominio(idDominio));

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		} catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca dei domini:" +e.getMessage() , e);

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
	@Path("/ibanaccredito")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse findIbanaccredito(
			@QueryParam(value = "operatore") String principalOperatore,
			@QueryParam(value = "codDominio") String codDominio,
			@QueryParam(value = "codEnte") String codEnte,
			@QueryParam(value = "idEnte") long idEnte,
			@QueryParam(value = "offset") @DefaultValue(value="0") int offset,
			@QueryParam(value = "limit") @DefaultValue(value="25") int limit) throws GovPayException {
		initLogger("findIbanAccredito");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"] codDominio["+codDominio+"] codEnte["+codEnte+"] idEnte["+idEnte+"]  offset["+offset+"] limit["+limit+"]");

		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(ThreadContext.get("op"));

		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}

			this.checkOperatoreAdmin(bd);

			DominiBD dominiBD = new DominiBD(bd);
			Dominio dominio = null;

			if(codEnte != null){
				Ente ente = AnagraficaManager.getEnte(bd, codEnte);

				dominio = dominiBD.getDominio(ente.getIdDominio());
			} else if(idEnte > 0){
				Ente ente = AnagraficaManager.getEnte(bd, idEnte);

				dominio = dominiBD.getDominio(ente.getIdDominio());
			} else {
				if(codDominio != null)
					dominio = AnagraficaManager.getDominio(bd, codDominio);

			}
			if(dominio != null)
				darsResponse.setResponse(dominiBD.getIbanAccreditoByIdDominio(dominio.getId()));
			else 
				darsResponse.setResponse(new ArrayList<IbanAccredito>());
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		} catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca dei domini:" +e.getMessage() , e);

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
