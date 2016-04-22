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
package it.govpay.web.rs.dars.anagrafica.iban;

import java.io.ByteArrayOutputStream;
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
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.IbanAccreditoBD;
import it.govpay.bd.anagrafica.filters.IbanAccreditoFilter;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IbanAccredito;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
import it.govpay.web.rs.dars.anagrafica.domini.Domini;
import it.govpay.web.rs.dars.anagrafica.iban.input.IdNegozio;
import it.govpay.web.rs.dars.anagrafica.iban.input.IdSellerBank;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.base.CheckButton;
import it.govpay.web.rs.dars.model.input.base.InputNumber;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.utils.Utils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class IbanHandler extends BaseDarsHandler<IbanAccredito> implements IDarsHandler<IbanAccredito> {

	private static Map<String, ParamField<?>> infoCreazioneMap = null;
	private String codDominio = null;

	public IbanHandler(Logger log, BaseDarsService darsService) {
		super(log,darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{
			log.info("Esecuzione " + methodName + " in corso..."); 
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			Domini dominiDars = new Domini();
			String codDominioId = Utils.getInstance().getMessageFromResourceBundle(dominiDars.getNomeServizio()+ ".codDominio.id");
			this.codDominio = this.getParameter(uriInfo, codDominioId, String.class);
			URI esportazione = null;
			URI cancellazione = null;

			IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(bd);
			IbanAccreditoFilter filter = ibanAccreditoBD.newFilter();
			filter.setCodDominio(this.codDominio);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.IbanAccredito.model().COD_IBAN);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);


			long count = ibanAccreditoBD.count(filter);

			Elenco elenco = new Elenco(this.titoloServizio, this.getInfoRicerca(uriInfo, bd),
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = uriInfo.getBaseUriBuilder().path(this.pathServizio).path("{id}");

			List<IbanAccredito> findAll = ibanAccreditoBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (IbanAccredito entry : findAll) {
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
		return null;
	}

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI creazione = this.getUriCreazione(uriInfo, bd);
		InfoForm infoCreazione = new InfoForm(creazione);

		String ibanAccreditoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String codIbanId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codIban.id");
		String codIbanAppoggioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codIbanAppoggio.id");
		String codBicAccreditoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codBicAccredito.id");
		String codBicAppoggioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codBicAppoggio.id");
		String idNegozioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idNegozio.id");
		String idSellerBankId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idSellerBank.id");
		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String attivatoObepId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".attivatoObep.id");
		String postaleId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".postale.id");
		//Domini dominiDars = new Domini();
		String codDominioId = Utils.getInstance().getMessageFromResourceBundle("domini.codDominio.id"); //(intermediariDars.getNomeServizio()+ ".codDominio.id");

		if(infoCreazioneMap == null){
			initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoCreazione.getSezioneRoot();
		InputNumber idIbanAccredito = (InputNumber) infoCreazioneMap.get(ibanAccreditoId);
		idIbanAccredito.setDefaultValue(null);
		sezioneRoot.addField(idIbanAccredito);

		InputText codDominio = (InputText) infoCreazioneMap.get(codDominioId);
		codDominio.setDefaultValue(this.codDominio);
		sezioneRoot.addField(codDominio);

		InputText codIban = (InputText) infoCreazioneMap.get(codIbanId);
		codIban.setDefaultValue(null);
		codIban.setEditable(true);     
		sezioneRoot.addField(codIban);

		InputText codIbanAppoggio = (InputText) infoCreazioneMap.get(codIbanAppoggioId);
		codIbanAppoggio.setDefaultValue(null);
		sezioneRoot.addField(codIbanAppoggio);
		
		InputText codBicAccredito = (InputText) infoCreazioneMap.get(codBicAccreditoId);
		codBicAccredito.setDefaultValue(null);
		sezioneRoot.addField(codBicAccredito);
		
		InputText codBicAppoggio = (InputText) infoCreazioneMap.get(codBicAppoggioId);
		codBicAppoggio.setDefaultValue(null);
		sezioneRoot.addField(codBicAppoggio);
		
		IdNegozio idNegozio = (IdNegozio) infoCreazioneMap.get(idNegozioId);
		idNegozio.setDefaultValue(null);
		sezioneRoot.addField(idNegozio);
		
		IdSellerBank idSellerBank = (IdSellerBank) infoCreazioneMap.get(idSellerBankId);
		idSellerBank.setDefaultValue(null);
		sezioneRoot.addField(idSellerBank);
		
		CheckButton abilitato = (CheckButton) infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(true); 
		sezioneRoot.addField(abilitato);
		
		CheckButton attivatoObep = (CheckButton) infoCreazioneMap.get(attivatoObepId);
		attivatoObep.setDefaultValue(false); 
		sezioneRoot.addField(attivatoObep);
		
		CheckButton postale = (CheckButton) infoCreazioneMap.get(postaleId);
		postale.setDefaultValue(false); 
		sezioneRoot.addField(postale);

		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoCreazioneMap == null){
			infoCreazioneMap = new HashMap<String, ParamField<?>>();

			String ibanAccreditoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
			String codIbanId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codIban.id");
			String codIbanAppoggioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codIbanAppoggio.id");
			String codBicAccreditoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codBicAccredito.id");
			String codBicAppoggioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codBicAppoggio.id");
			String idNegozioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idNegozio.id");
			String idSellerBankId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idSellerBank.id");
			String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			String attivatoObepId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".attivatoObep.id");
			String postaleId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".postale.id");
			//Domini dominiDars = new Domini();
			String codDominioId = Utils.getInstance().getMessageFromResourceBundle("domini.codDominio.id"); //(intermediariDars.getNomeServizio()+ ".codDominio.id");

			// id 
			InputNumber id = new InputNumber(ibanAccreditoId, null, null, true, true, false, 1, 20);
			infoCreazioneMap.put(ibanAccreditoId, id);

			// codIntermediario
			InputText codDominio = new InputText(codDominioId, "", null, true, true, false, 1, 255);
			infoCreazioneMap.put(codDominioId, codDominio);

			// codIban
			String codIbanLabel =  Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codIban.label");
			InputText codIban = new InputText(codIbanId, codIbanLabel, null, true, false, true, 1, 255);
			infoCreazioneMap.put(codIbanId, codIban);

			// codIbanAppoggio
			String codIbanAppoggioLabel =  Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codIbanAppoggio.label");
			InputText codIbanAppoggio = new InputText(codIbanAppoggioId, codIbanAppoggioLabel, null, false, false, true, 1, 255);
			infoCreazioneMap.put(codIbanAppoggioId, codIbanAppoggio);

			// codBicAccredito
			String codBicAccreditoLabel =  Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codBicAccredito.label");
			InputText codBicAccredito = new InputText(codBicAccreditoId, codBicAccreditoLabel, null, false, false, true, 1, 255);
			codBicAccredito.setAvanzata(true);
			infoCreazioneMap.put(codBicAccreditoId, codBicAccredito);

			// codBicAppoggio
			String codBicAppoggioLabel =  Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codBicAppoggio.label");
			InputText codBicAppoggio = new InputText(codBicAppoggioId, codBicAppoggioLabel, null, false, false, true, 1, 255);
			codBicAppoggio.setAvanzata(true); 
			infoCreazioneMap.put(codBicAppoggioId, codBicAppoggio);
			
			// attivatoObep
			String attivatoObepLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".attivatoObep.label");
			CheckButton attivatoObep = new CheckButton(attivatoObepId, attivatoObepLabel, null, false, false, true);
			infoCreazioneMap.put(attivatoObepId, attivatoObep);

			List<RawParamValue> attivatoObepValues = new ArrayList<RawParamValue>();
			attivatoObepValues.add(new RawParamValue(attivatoObepId, "false")); 
			// idNegozio
			String idNegozioLabel =  Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idNegozio.label");
			URI idNegozioRefreshUri = this.getUriField(uriInfo, bd, idNegozioId); 
			
			IdNegozio idNegozio = new IdNegozio(this.nomeServizio, idNegozioId, idNegozioLabel, 1, 255, idNegozioRefreshUri , attivatoObepValues); 
			idNegozio.addDependencyField(attivatoObep);
			idNegozio.init(attivatoObepValues);
			infoCreazioneMap.put(idNegozioId,idNegozio);
			
			// idSellerBank
			String idSellerBankLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idSellerBank.label");
			URI idSellerBankRefreshUri = this.getUriField(uriInfo, bd, idSellerBankId); 
			IdSellerBank idSellerBank = new IdSellerBank(this.nomeServizio, idSellerBankId, idSellerBankLabel, 1, 255, idSellerBankRefreshUri , attivatoObepValues);
			idSellerBank.addDependencyField(attivatoObep);
			idSellerBank.init(attivatoObepValues);
			infoCreazioneMap.put(idSellerBankId, idSellerBank);

			// abilitato
			String abilitatoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label");
			CheckButton abiliato = new CheckButton(abilitatoId, abilitatoLabel, null, false, false, true);
			infoCreazioneMap.put(abilitatoId, abiliato);

	

			// postale
			String postaleLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".postale.label");
			CheckButton postale = new CheckButton(postaleId, postaleLabel, null, false, false, true);
			infoCreazioneMap.put(postaleId, postale);


		}
	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, IbanAccredito entry) throws ConsoleException {
		URI modifica = this.getUriModifica(uriInfo, bd);
		InfoForm infoModifica = new InfoForm(modifica);

		String ibanAccreditoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String codIbanId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codIban.id");
		String codIbanAppoggioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codIbanAppoggio.id");
		String codBicAccreditoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codBicAccredito.id");
		String codBicAppoggioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codBicAppoggio.id");
		String idNegozioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idNegozio.id");
		String idSellerBankId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idSellerBank.id");
		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String attivatoObepId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".attivatoObep.id");
		String postaleId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".postale.id");
		//Domini dominiDars = new Domini();
		String codDominioId = Utils.getInstance().getMessageFromResourceBundle("domini.codDominio.id"); //(intermediariDars.getNomeServizio()+ ".codDominio.id");

		if(infoCreazioneMap == null){
			initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoModifica.getSezioneRoot();
		InputNumber idIbanAccredito = (InputNumber) infoCreazioneMap.get(ibanAccreditoId);
		idIbanAccredito.setDefaultValue(entry.getId());
		sezioneRoot.addField(idIbanAccredito);

		DominiBD dominiBD = new DominiBD(bd);
		Dominio dominio = null;
		try {
			dominio = dominiBD.getDominio(entry.getIdDominio());
		} catch (Exception e) {
			throw new ConsoleException(e);
		}
		
		InputText codDominio = (InputText) infoCreazioneMap.get(codDominioId);
		codDominio.setDefaultValue(dominio.getCodDominio()); 
		sezioneRoot.addField(codDominio);

		InputText codIban = (InputText) infoCreazioneMap.get(codIbanId);
		codIban.setDefaultValue(entry.getCodIban());
		codIban.setEditable(false);     
		sezioneRoot.addField(codIban);

		InputText codIbanAppoggio = (InputText) infoCreazioneMap.get(codIbanAppoggioId);
		codIbanAppoggio.setDefaultValue(entry.getCodIbanAppoggio());
		sezioneRoot.addField(codIbanAppoggio);
		
		InputText codBicAccredito = (InputText) infoCreazioneMap.get(codBicAccreditoId);
		codBicAccredito.setDefaultValue(entry.getCodBicAccredito());
		sezioneRoot.addField(codBicAccredito);
		
		InputText codBicAppoggio = (InputText) infoCreazioneMap.get(codBicAppoggioId);
		codBicAppoggio.setDefaultValue(entry.getCodBicAppoggio());
		sezioneRoot.addField(codBicAppoggio);
		
		IdNegozio idNegozio = (IdNegozio) infoCreazioneMap.get(idNegozioId);
		idNegozio.setDefaultValue(entry.getIdNegozio());
		sezioneRoot.addField(idNegozio);
		
		IdSellerBank idSellerBank = (IdSellerBank) infoCreazioneMap.get(idSellerBankId);
		idSellerBank.setDefaultValue(entry.getIdSellerBank());
		sezioneRoot.addField(idSellerBank);
		
		CheckButton abilitato = (CheckButton) infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(entry.isAbilitato()); 
		sezioneRoot.addField(abilitato);
		
		CheckButton attivatoObep = (CheckButton) infoCreazioneMap.get(attivatoObepId);
		attivatoObep.setDefaultValue(entry.isAttivatoObep()); 
		sezioneRoot.addField(attivatoObep);
		
		CheckButton postale = (CheckButton) infoCreazioneMap.get(postaleId);
		postale.setDefaultValue(entry.isPostale()); 
		sezioneRoot.addField(postale);
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
			IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(bd);
			IbanAccredito ibanAccredito = ibanAccreditoBD.getIbanAccredito(id);
			
			DominiBD dominiBD = new DominiBD(bd);
			Dominio dominio = dominiBD.getDominio(ibanAccredito.getIdDominio());

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,ibanAccredito);
			URI cancellazione = null;
			URI esportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(ibanAccredito), esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			// dati dele dettaglio
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codIban.label"), ibanAccredito.getCodIban());
			//Domini dominiDars = new Domini();
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle("domini.codDominio.label"), dominio.getCodDominio());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codIbanAppoggio.label"), ibanAccredito.getCodIbanAppoggio(),true);
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codBicAccredito.label"), ibanAccredito.getCodBicAccredito(),true);
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codBicAppoggio.label"), ibanAccredito.getCodBicAppoggio(),true);
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idNegozio.label"), ibanAccredito.getIdNegozio(),true);
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idSellerBank.label"), ibanAccredito.getIdSellerBank(),true);
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getSiNoAsLabel(ibanAccredito.isAbilitato()));
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".attivatoObep.label"), Utils.getSiNoAsLabel(ibanAccredito.isAttivatoObep()));
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".postale.label"), Utils.getSiNoAsLabel(ibanAccredito.isPostale()));
			

			log.info("Esecuzione " + methodName + " completata.");

			return dettaglio;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public Dettaglio insert(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException ,ValidationException,DuplicatedEntryException {
		String methodName = "Insert " + this.titoloServizio;

		try{
			log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			IbanAccredito entry = this.creaEntry(is, uriInfo, bd);

			this.checkEntry(entry, null);

			IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(bd);
			DominiBD dominiBD = new DominiBD(bd);

			try{
				Dominio dominio = dominiBD.getDominio(this.codDominio);
				entry.setIdDominio(dominio.getId());
			}catch(NotFoundException e){}

			try{
				ibanAccreditoBD.getIbanAccredito(entry.getCodIban());
				String msg = Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".oggettoEsistente", entry.getCodIban());
				throw new DuplicatedEntryException(msg);
			}catch(NotFoundException e){}

			ibanAccreditoBD.insertIbanAccredito(entry); 

			log.info("Esecuzione " + methodName + " completata.");

			return this.getDettaglio(entry.getId(), uriInfo, bd);
		}catch(DuplicatedEntryException e){
			throw e;
		}catch(ValidationException e){
			throw e;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public IbanAccredito creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException {
		String methodName = "creaEntry " + this.titoloServizio;
		IbanAccredito entry = null;
		try{
			log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			JsonConfig jsonConfig = new JsonConfig();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();

			JSONObject jsonObjectStazione = JSONObject.fromObject( baos.toString() );  
			jsonConfig.setRootClass(IbanAccredito.class);
			entry = (IbanAccredito) JSONObject.toBean( jsonObjectStazione, jsonConfig );

			jsonObjectStazione = JSONObject.fromObject( baos.toString() );  
			jsonConfig.setRootClass(Dominio.class);
			Dominio dominio = (Dominio) JSONObject.toBean( jsonObjectStazione, jsonConfig );
			this.codDominio = dominio.getCodDominio();

			log.info("Esecuzione " + methodName + " completata.");
			return entry;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}
	@Override
	public void checkEntry(IbanAccredito entry, IbanAccredito oldEntry) throws ValidationException {
		if(entry.getCodIban() == null)  throw new ValidationException("Codice Iban Accredito nullo");

		if(entry.getCodIban().length() != 27)
			throw new ValidationException("La lunghezza dell'Iban di accredito deve essere di 27 caratteri, trovati " + entry.getCodIban().length() + ".");
		
		// aggiungere pattern validazione IBAN
		
		// update
		if(oldEntry != null){
			if(!entry.getCodIban().equals(oldEntry.getCodIban())) throw new ValidationException("Non e' consentito modificare l'Iban Accredito");
		}
		
	}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException {
		String methodName = "Update " + this.titoloServizio;

		try{
			log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			IbanAccredito entry = this.creaEntry(is, uriInfo, bd);

			IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(bd);
			DominiBD dominiBD = new DominiBD(bd);

			IbanAccredito oldEntry = ibanAccreditoBD.getIbanAccredito(entry.getCodIban());

			this.checkEntry(entry, oldEntry); 

			try{
				Dominio dominio = dominiBD.getDominio(this.codDominio);
				entry.setIdDominio(dominio.getId());
			}catch(NotFoundException e){}

			ibanAccreditoBD.updateIbanAccredito(entry); 

			log.info("Esecuzione " + methodName + " completata.");
			return this.getDettaglio(entry.getId(), uriInfo, bd);
		}catch(ValidationException e){
			throw e;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws ConsoleException {
	}


	@Override
	public String getTitolo(IbanAccredito entry) {
		return entry.getCodIban();
	}

	@Override
	public String getSottotitolo(IbanAccredito entry) {
		StringBuilder sb = new StringBuilder();

		sb.append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label")).append(": ").append(Utils.getSiNoAsLabel(entry.isAbilitato()));
		sb.append(", ").append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".attivatoObep.label")).append(": ").append(Utils.getSiNoAsLabel(entry.isAttivatoObep()));
		sb.append(", ").append(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".postale.label")).append(": ").append(Utils.getSiNoAsLabel(entry.isPostale()));

		return Utils.getAbilitatoAsLabel(entry.isAbilitato()); 
	}



}
