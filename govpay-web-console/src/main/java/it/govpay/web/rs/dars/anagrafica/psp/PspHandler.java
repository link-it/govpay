/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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
package it.govpay.web.rs.dars.anagrafica.psp;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.filters.PspFilter;
import it.govpay.bd.model.Psp;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.utils.Utils;

public class PspHandler extends BaseDarsHandler<it.govpay.bd.model.Psp> implements IDarsHandler<it.govpay.bd.model.Psp>{

	public PspHandler(Logger log, BaseDarsService darsService) {
		super(log,darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo,BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		
		try{		
			Integer offset = this.getOffset(uriInfo);
//			Integer limit = this.getLimit(uriInfo);
			URI esportazione = null;
			URI cancellazione = null;

			log.info("Esecuzione " + methodName + " in corso..."); 
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			it.govpay.bd.anagrafica.PspBD pspBD = new it.govpay.bd.anagrafica.PspBD(bd);
			PspFilter filter = pspBD.newFilter();
			filter.setOffset(offset);
//			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Psp.model().COD_PSP);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			
			long count = pspBD.count(filter);

			Elenco elenco = new Elenco(this.titoloServizio, this.getInfoRicerca(uriInfo, bd),
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = uriInfo.getBaseUriBuilder().path(this.pathServizio).path("{id}");

			List<it.govpay.bd.model.Psp> findAll = pspBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (it.govpay.bd.model.Psp entry : findAll) {
					elenco.getElenco().add(this.getElemento(entry, entry.getId(), uriDettaglioBuilder));
				}
			}

			log.info("Esecuzione " + methodName + " completata.");

			return elenco;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		// TODO Auto-generated method stub
		return null;
	}

	 

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		InfoForm infoCreazione = null; 
		return infoCreazione;
	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, it.govpay.bd.model.Psp entry) throws ConsoleException {
		InfoForm infoModifica = null; 
		return infoModifica;
	}

	@Override
	public Object getField(UriInfo uriInfo,List<RawParamValue>values, String fieldId,BasicBD bd) throws ConsoleException {
		return null;
	}

	@Override
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "dettaglio " + this.titoloServizio + "."+ id;
		try{
			log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			// recupero oggetto
			it.govpay.bd.anagrafica.PspBD pspBD = new it.govpay.bd.anagrafica.PspBD(bd);
			it.govpay.bd.model.Psp psp = pspBD.getPsp(id);

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,psp);
			URI cancellazione = null;
			URI esportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(psp), esportazione, cancellazione, infoModifica);
			
			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			// dati del psp
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codPsp.label"), psp.getCodPsp());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".ragioneSociale.label"), psp.getRagioneSociale());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codFlusso.label"), psp.getCodFlusso());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".urlInfo.label"), psp.getUrlInfo());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getAbilitatoAsLabel(psp.isAbilitato()));
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".bolloGestito.label"), Utils.getAbilitatoAsLabel(psp.isBolloGestito()));
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".stornoGestito.label"), Utils.getAbilitatoAsLabel(psp.isStornoGestito()));
			
			// Elementi correlati
			String etichettaCanali = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".elementoCorrelato.canali.titolo");
			String codPspId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codPsp.id");
			
			Canali canaliDars = new Canali();
			UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path(canaliDars.getPathServizio()).queryParam(codPspId, psp.getCodPsp());
			dettaglio.addElementoCorrelato(etichettaCanali, uriBuilder.build());
			
			log.info("Esecuzione " + methodName + " completata.");

			return dettaglio;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}
	
	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException,ValidationException,DuplicatedEntryException {
		return null;
	}
	
	@Override
	public it.govpay.bd.model.Psp creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		return null;
	}
	
	@Override
	public void checkEntry(Psp entry, Psp oldEntry) throws ValidationException {}
	
	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException {
		return null;
	}
	
	@Override
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		// operazione non prevista
	}

	@Override
	public String getTitolo(it.govpay.bd.model.Psp entry) {
		StringBuilder sb = new StringBuilder();

		sb.append(entry.getRagioneSociale());
		sb.append(" (").append(entry.getCodPsp()).append(")");
		return sb.toString();
	}

	@Override
	public String getSottotitolo(it.govpay.bd.model.Psp entry) {
		StringBuilder sb = new StringBuilder();

		sb.append(Utils.getAbilitatoAsLabel(entry.isAbilitato()));
		sb.append(", Bollo ").append(Utils.getAbilitatoAsLabel(entry.isBolloGestito()));
		sb.append(", Storno ").append(Utils.getAbilitatoAsLabel(entry.isStornoGestito()));

		return sb.toString();
	}


}
