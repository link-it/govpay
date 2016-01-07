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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Disponibilita;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Ente;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Operatore.ProfiloOperatore;
import it.govpay.bd.model.Periodo;
import it.govpay.dars.bd.DominiBD;
import it.govpay.dars.bd.EntiBD;
import it.govpay.dars.model.DominioExt;
import it.govpay.dars.model.ListaDominiEntry;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.utils.DominiUtils;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.exception.ValidationException;
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
				bd = BasicBD.newInstance();
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
			if(bd != null) bd.rollback();
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
			return darsResponse;
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
				bd = BasicBD.newInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			this.checkOperatoreAdmin(bd);
			DominiBD dominiBD = new DominiBD(bd);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(dominiBD.getDominioExt(id));
		} catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la ricerca del dominio:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca del dominio:" +e.getMessage() , e);
			if(bd != null) bd.rollback();
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
			return darsResponse;
		} finally {
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
				bd = BasicBD.newInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			this.checkOperatoreAdmin(bd);
			DominiBD dominiBD = new DominiBD(bd);
			DominioExt oldDominio = dominiBD.getDominioExt(dominio.getId());
			this.checkDominio(dominio, oldDominio);
			dominiBD.updateDominioExt(dominio);
			DominiUtils.updateTabellaControparti(bd, dominio.getId());
			DominiUtils.updateContiAccredito(bd, dominio.getId());
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		} catch(ValidationException e) {
			log.error("Riscontrato errore di validazione durante l'aggiornamento del dominio:" +e.getMessage());
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito("Dati di input non validi: " + e.getMessage());
			return darsResponse;
		} catch(WebApplicationException e) {
			log.error("Riscontrato errore di autorizzazione durante 'aggiornamento del dominio:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'aggiornamento della dominio:" +e.getMessage() , e);
			if(bd != null) bd.rollback();
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
			return darsResponse;
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
				bd = BasicBD.newInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			this.checkOperatoreAdmin(bd);
			this.checkDominio(dominio, null);
			DominiBD dominiBD = new DominiBD(bd);
			try {
				dominiBD.getDominio(dominio.getCodDominio());
				throw new ValidationException("Il dominio con id [" + dominio.getCodDominio() + "] e' gia' presente in anagrafica.");
			} catch (NotFoundException e) {
				// Ok non esiste gia'. posso inserire
			}
			dominiBD.insertDominioExt(dominio);
			DominiUtils.updateTabellaControparti(bd, dominio.getId());
			DominiUtils.updateContiAccredito(bd, dominio.getId());
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
			darsResponse.setResponse(dominio.getId());
		} catch(ValidationException e){
			log.error("Riscontrato errore di validazione durante l'inserimento del dominio:" +e.getMessage());
			darsResponse.setEsitoOperazione(EsitoOperazione.NONESEGUITA);
			darsResponse.setDettaglioEsito("Dati di input non validi: " + e.getMessage());
			return darsResponse;
		} catch(WebApplicationException e) {
			log.error("Riscontrato errore di autorizzazione durante l'inserimento del dominio:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'inserimento della dominio:" +e.getMessage() , e);
			if(bd != null) bd.rollback();
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
			return darsResponse;
		} finally {
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
				bd = BasicBD.newInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			this.checkOperatoreAdmin(bd);
			DominiBD dominiBD = new DominiBD(bd);
			darsResponse.setResponse(dominiBD.getIbanAccreditoByIdDominio(idDominio));
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		} catch (Exception e) {
			log.error("Riscontrato errore durante la ricerca dei domini:" +e.getMessage() , e);
			if(bd != null)  bd.rollback();
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString());
			return darsResponse;
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
				bd = BasicBD.newInstance();
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
			return darsResponse;
		}finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd != null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}

	private void checkDominio(DominioExt dominio, DominioExt oldDominio) throws ValidationException {

		if(dominio == null || dominio.getCodDominio() == null || dominio.getCodDominio().length() != 11) throw new ValidationException("Lunghezza del IdDominio errata. Richieste 11 cifre, trovate "+dominio.getCodDominio().length());
		try { 
			Long.parseLong(dominio.getCodDominio());
		} catch (NumberFormatException e) {
			throw new ValidationException("Formato IdDominio errato. Richieste 11 cifre, trovato "+dominio.getCodDominio());
		}

		if(dominio.getRagioneSociale() == null || dominio.getRagioneSociale().isEmpty()) throw new ValidationException("Il campo Ragione Sociale deve essere valorizzato.");

		if(dominio.getGln() != null && !dominio.getGln().isEmpty()) {
			if(dominio.getGln().length() != 13) throw new ValidationException("Lunghezza Global Location Number errata. Richieste 13 cifre, trovate "+dominio.getGln().length());
			try { 
				Long.parseLong(dominio.getGln());
			} catch (NumberFormatException e) {
				throw new ValidationException("Formato Global Location Number errato. Richieste 11 cifre, trovato "+dominio.getGln());
			}
		}
	
		if(dominio.getIdStazione() == 0) throw new ValidationException("Il campo Stazione deve essere valorizzato");

		if(dominio.getIbanAccredito() != null) {
			List<String> ibans = new ArrayList<String>();
			for(IbanAccredito iban : dominio.getIbanAccredito()) {
				if(iban.getCodIban() == null) throw new ValidationException("Il campo Iban deve essere valorizzato.");
				if(iban.getCodIban().length() != 27) throw new ValidationException("La lunghezza dell'Iban di accredito deve essere di 27 caratteri, trovati " + iban.getCodIban().length() + ".");
				if(ibans.contains(iban.getCodIban())) throw new ValidationException("L'iban [" + iban.getCodIban() + "] e' duplicato.");
				ibans.add(iban.getCodIban());
			}
		}
		checkDisponibilita(dominio.getDisponibilita());
		checkDisponibilita(dominio.getIndisponibilita());
		
		if(oldDominio != null) {
			if(!dominio.getCodDominio().equals(oldDominio.getCodDominio())) throw new ValidationException("Non e' consentito modificare l'IdDominio");
			if(dominio.getIdStazione() != oldDominio.getIdStazione()) throw new ValidationException("Non e' consentito modificare la Stazione");
			
			List<IbanAccredito> ibanOld = null;
			if(oldDominio.getIbanAccredito() != null && !oldDominio.getIbanAccredito().isEmpty()) {
				IbanAccredito[] oldIbanArray = oldDominio.getIbanAccredito().toArray(new IbanAccredito[] {});
				Arrays.sort(oldIbanArray);
				ibanOld = Arrays.asList(oldIbanArray);
			}
			
			List<IbanAccredito> ibanNew = null;
			if(dominio.getIbanAccredito() != null && !dominio.getIbanAccredito().isEmpty()) {
				IbanAccredito[] newIbanArray = dominio.getIbanAccredito().toArray(new IbanAccredito[] {});
				Arrays.sort(newIbanArray);
				ibanNew = Arrays.asList(newIbanArray);
			}
			
			if(ibanOld != null) {
				if(ibanNew == null) throw new ValidationException("Nessun Iban trovato in update");
				for(IbanAccredito old: ibanOld) {
					boolean found = false;
					for(IbanAccredito newI: ibanNew) {
						if(newI.getCodIban().equals(old.getCodIban())) found = true;
					}
					if(!found)  throw new ValidationException("Iban Accredito ["+old.getCodIban()+"] non trovato in update");
				}
			}
		}
	}

	private void checkDisponibilita(List<Disponibilita> disponibilita) throws ValidationException {
		if(disponibilita == null) return;
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		for(Disponibilita d : disponibilita) {
			if(d.getTipoDisponibilita() == null) throw new ValidationException("E' obbligatorio valorizzare il campo Tipo Disponibilita di ogni periodo");
			if(d.getTipoPeriodo() == null) throw new ValidationException("E' obbligatorio valorizzare il campo Tipo Periodo di ogni periodo");

			switch (d.getTipoPeriodo()) {
			case ANNUALE:
				if(d.getGiorno() == null || d.getGiorno().matches("(0[123456789]|[12]\\d|3[01])-(0\\d|1[012])")) throw new ValidationException("In periodo di tipo Annuale, il campo Giorno deve essere valorizzato [dd-mm]");
				break;
			case GIORNALIERA:
				if(d.getGiorno() != null && !d.getGiorno().isEmpty()) throw new ValidationException("In periodo di tipo Giornaliero, il campo Giorno non deve essere valorizzato");
				break;
			case MENSILE:
				if(d.getGiorno() == null || !d.getGiorno().matches("0[123456789]|[12]\\d|3[01]")) throw new ValidationException("In periodo di tipo Mensile, il campo Giorno deve essere valorizzato [01,31]");
				break;
			case SETTIMANALE:
				if(d.getGiorno() == null || !d.getGiorno().matches("lunedi|martedi|mercoledi|giovedi|venerdi|sabato|domenica")) throw new ValidationException("In periodo di tipo Settimanale, il campo Giorno deve essere valorizzato [Lunedi|Martedi|...]");
				break;
			}
			
			for(Periodo p : d.getFasceOrarieLst()) {
				Date da, a = null;
				try {
					da = format.parse(p.getDa()); 
				} catch (ParseException e) {
					throw new ValidationException("L'orario [" + p.getDa() + "] non e' corretto. Formato atteso [hh:mm:ss].");
				}
				try {
					a = format.parse(p.getA());
				} catch (ParseException e) {
					throw new ValidationException("L'orario [" + p.getA() + "] non e' corretto. Formato atteso [hh:mm:ss].");
				}
				if(da.after(a)) throw new ValidationException("L'orario di partenza di un periodo deve essere successivo a quello di terminazione.");
			}
		}
	}
}
