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
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;
import it.govpay.bd.pagamento.VersamentoFilter;
import it.govpay.dars.bd.PagamentiBD;
import it.govpay.dars.model.ListaPagamentiEntry;
import it.govpay.dars.model.Pagamento;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;

@Path("/dars/pagamenti")
public class Pagamenti extends BaseRsService {

	//private static boolean ENABLE_CHECK_AUTHORIZZAZIONE_OPERATORE = true; // false solo per debug
	Logger log = LogManager.getLogger();

	@GET
	@Path("/")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse listaEntry(
			@QueryParam(value = "operatore") String principalOperatore,
			@QueryParam(value = "statoPagamento") String statoPagamento,
			@QueryParam(value = "statoRendicontazione") String statoRendicontazione,
			@QueryParam(value = "codEnte") String codEnte,
			@QueryParam(value = "iuv") String iuv,
			@QueryParam(value = "offset") @DefaultValue(value="0") int offset,
			@QueryParam(value = "limit") @DefaultValue(value="25") int limit) throws GovPayException {
		initLogger("pagamenti");
		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}

			PagamentiBD versamentiBD = new PagamentiBD(bd);
			VersamentoFilter filter = versamentiBD.newFilter();

			Operatore operatore = this.getOperatoreByPrincipal(bd);

			filter.setOffset(offset);
			filter.setLimit(limit);
			filter.setStatoPagamento(statoPagamento);
			filter.setStatoRendicontazione(statoRendicontazione); 
			filter.setIuv(iuv);
			filter.setCodEnte(codEnte);

			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Versamento.model().DATA_ORA_ULTIMO_AGGIORNAMENTO);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);

			List<ListaPagamentiEntry> findAll = null;

			// Se l'utente non e' admin puo'vedere solo gli enti che sono associati a lui.
			if(!ProfiloOperatore.ADMIN.equals(operatore.getProfilo())){
				if(operatore.getIdEnti() == null || operatore.getIdEnti().isEmpty() 
						|| operatore.getIdApplicazioni() == null || operatore.getIdApplicazioni().isEmpty())
					findAll = new ArrayList<ListaPagamentiEntry>();
				else {
					filter.setIdEnti(operatore.getIdEnti());
					filter.setIdApplicazioni(operatore.getIdApplicazioni());
				}
			}

			if(findAll == null)
				findAll = versamentiBD.findAll(filter);

			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			
			darsResponse.setResponse(findAll);

		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca dei pagamenti:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'esecuzione della ricerca dei pagamenti:" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();


			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		return darsResponse;
	}

	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse versamento(
			@QueryParam(value = "operatore") String principalOperatore,
			@PathParam("id") long id) throws GovPayException {
		initLogger("pagamenti");
		BasicBD bd = null;
		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);

		try {
			try {
				bd = BasicBD.getInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			PagamentiBD pagamentiBD = new PagamentiBD(bd);



			Pagamento pagamento = pagamentiBD.get(id);
			//if(ENABLE_CHECK_AUTHORIZZAZIONE_OPERATORE) {
			Operatore operatore = this.getOperatoreByPrincipal(bd);

			boolean authorized = false;
			if(ProfiloOperatore.ADMIN.equals(operatore.getProfilo())) { //Admin vede tutto
				authorized = true;
			} else {
				if(operatore.getIdEnti() != null) {
					for(Long idEnte: operatore.getIdEnti()) {
						if(idEnte.longValue() == pagamento.getVersamento().getIdEnte()) {
							authorized = true;
							break;
						}
					}
				}

				if(operatore.getIdApplicazioni() != null && !authorized) {
					for(Long idApplicazione: operatore.getIdApplicazioni()) {
						if(idApplicazione.longValue() == pagamento.getVersamento().getIdApplicazione()) {
							authorized = true;
							break;
						}
					}
				}
			}

			if(!authorized) {
				throw new WebApplicationException(getUnauthorizedResponse());
			}			
			//}

			darsResponse.setResponse(pagamento);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'esecuzione del metodo versamento:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'esecuzione del metodo versamento:" +e.getMessage() , e);

			if(bd != null) 
				bd.rollback();


			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		return darsResponse;
	}
}

