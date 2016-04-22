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
package it.govpay.web.rs.dars.monitoraggio.versamenti;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.filters.DominioFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.Utils;

public class VersamentiHandler extends BaseDarsHandler<Versamento> implements IDarsHandler<Versamento>{

	private static Map<String, ParamField<?>> infoRicercaMap = null;

	public VersamentiHandler(Logger log, BaseDarsService darsService) { 
		super(log, darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita agli utenti registrati
			this.darsService.getOperatoreByPrincipal(bd);

		//			Integer offset = this.getOffset(uriInfo);
			//			Integer limit = this.getLimit(uriInfo);
			URI esportazione = null;
			URI cancellazione = null;

			log.info("Esecuzione " + methodName + " in corso..."); 

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			VersamentoFilter filter = versamentiBD.newFilter();
			//			filter.setOffset(offset);
			//			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Versamento.model().DATA_CREAZIONE);
			fsw.setSortOrder(SortOrder.DESC);
			filter.getFilterSortList().add(fsw);
			
			String cfDebitoreId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".cfDebitore.id");
			String cfVersanteId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".cfVersante.id");
			String idVersamentoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idVersamento.id");
			String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String iuvId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");
			 
			// imposto i filtri
			
			long count = versamentiBD.count(filter);

			Elenco elenco = new Elenco(this.titoloServizio, this.getInfoRicerca(uriInfo, bd),
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = uriInfo.getBaseUriBuilder().path(this.pathServizio).path("{id}");

			List<Versamento> findAll = versamentiBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (Versamento entry : findAll) {
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

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI ricerca = this.getUriRicerca(uriInfo, bd);
		InfoForm infoRicerca = new InfoForm(ricerca);

		String cfDebitoreId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".cfDebitore.id");
		String cfVersanteId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".cfVersante.id");
		String idVersamentoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idVersamento.id");
		String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
		String iuvId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");

		if(infoRicercaMap == null){
			initInfoRicerca(uriInfo, bd);
		}

		Sezione sezioneRoot = infoRicerca.getSezioneRoot();
		
		InputText cfDebitore = (InputText) infoRicercaMap.get(cfDebitoreId);
		cfDebitore.setDefaultValue(null);
		sezioneRoot.addField(cfDebitore);
		
		InputText cfVersante = (InputText) infoRicercaMap.get(cfVersanteId);
		cfVersante.setDefaultValue(null);
		sezioneRoot.addField(cfVersante);
		
		InputText idVersamento = (InputText) infoRicercaMap.get(idVersamentoId);
		idVersamento.setDefaultValue(null);
		sezioneRoot.addField(idVersamento);
		
		InputText iuv = (InputText) infoRicercaMap.get(iuvId);
		iuv.setDefaultValue(null);
		sezioneRoot.addField(iuv);

		// idDominio
		List<Voce<Long>> domini = new ArrayList<Voce<Long>>();

		DominiBD dominiBD = new DominiBD(bd);
		DominioFilter filter;
		try {
			filter = dominiBD.newFilter();
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Dominio.model().COD_DOMINIO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			List<Dominio> findAll = dominiBD.findAll(filter );

			domini.add(new Voce<Long>(Utils.getInstance().getMessageFromResourceBundle("commons.label.qualsiasi"), -1L));
			if(findAll != null && findAll.size() > 0){
				for (Dominio dominio : findAll) {
					domini.add(new Voce<Long>(dominio.getCodDominio(), dominio.getId()));  
				}
			}
		} catch (ServiceException e) {
			throw new ConsoleException(e);
		}
		SelectList<Long> idDominio = (SelectList<Long>) infoRicercaMap.get(idDominioId);
		idDominio.setDefaultValue(-1L);
		idDominio.setValues(domini); 
		sezioneRoot.addField(idDominio);

		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoRicercaMap == null){
			infoRicercaMap = new HashMap<String, ParamField<?>>();

			String cfDebitoreId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".cfDebitore.id");
			String cfVersanteId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".cfVersante.id");
			String idVersamentoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idVersamento.id");
			String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String iuvId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.id");

			// cfDebitore
			String cfDebitoreLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".cfDebitore.label");
			InputText cfDebitore = new InputText(cfDebitoreId, cfDebitoreLabel, null, false, false, true, 1, 255);
			infoRicercaMap.put(cfDebitoreId, cfDebitore);

			// cfVersante
			String cfVersanteLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".cfVersante.label");
			InputText cfVersante = new InputText(cfVersanteId, cfVersanteLabel, null, false, false, true, 1, 255);
			infoRicercaMap.put(cfVersanteId, cfVersante);

			// Id Versamento
			String idVersamentoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idVersamento.label");
			InputText idVersamento = new InputText(idVersamentoId, idVersamentoLabel, null, false, false, true, 1, 255);
			infoRicercaMap.put(idVersamentoId, idVersamento);

			// iuv
			String iuvLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".iuv.label");
			InputText iuv = new InputText(iuvId, iuvLabel, null, false, false, true, 1, 255);
			infoRicercaMap.put(iuvId, iuv);			

			List<Voce<Long>> domini = new ArrayList<Voce<Long>>();
			// idDominio
			String idDominioLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.label");
			SelectList<Long> idDominio = new SelectList<Long>(idDominioId, idDominioLabel, null, false, false, true, domini);
			infoRicercaMap.put(idDominioId, idDominio);

		}
	}

	@Override
	public Object getField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitolo(Versamento entry) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSottotitolo(Versamento entry) {
		// TODO Auto-generated method stub
		return null;
	} 

	/* Creazione/Update non previsti**/

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException { return null; }

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Versamento entry) throws ConsoleException { return null; }

	@Override
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {	}

	@Override
	public Versamento creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException { return null; }

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException, DuplicatedEntryException { return null; }

	@Override
	public void checkEntry(Versamento entry, Versamento oldEntry) throws ValidationException { }

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException { return null; }
}
