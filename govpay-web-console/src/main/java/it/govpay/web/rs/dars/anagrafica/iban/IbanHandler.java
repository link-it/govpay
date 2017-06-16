/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.IbanAccreditoBD;
import it.govpay.bd.anagrafica.filters.IbanAccreditoFilter;
import it.govpay.model.Dominio;
import it.govpay.model.IbanAccredito;
import it.govpay.web.rs.dars.anagrafica.iban.input.IdNegozio;
import it.govpay.web.rs.dars.anagrafica.iban.input.IdSellerBank;
import it.govpay.web.rs.dars.base.DarsHandler;
import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.exception.DeleteException;
import it.govpay.web.rs.dars.exception.DuplicatedEntryException;
import it.govpay.web.rs.dars.exception.ExportException;
import it.govpay.web.rs.dars.exception.ValidationException;
import it.govpay.web.rs.dars.handler.IDarsHandler;
import it.govpay.web.rs.dars.model.Dettaglio;
import it.govpay.web.rs.dars.model.Elenco;
import it.govpay.web.rs.dars.model.InfoForm;
import it.govpay.web.rs.dars.model.InfoForm.Sezione;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.Voce;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.RefreshableParamField;
import it.govpay.web.rs.dars.model.input.base.CheckButton;
import it.govpay.web.rs.dars.model.input.base.InputNumber;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.utils.Utils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class IbanHandler extends DarsHandler<IbanAccredito> implements IDarsHandler<IbanAccredito> {

	public static final String patternIBAN = "[a-zA-Z]{2,2}[0-9]{2,2}[a-zA-Z0-9]{1,30}"; 
	public static final String patternBIC = "[A-Z]{6,6}[A-Z2-9][A-NP-Z0-9]([A-Z0-9]{3,3}){0,1}";
	private Long idDominio = null;

	public IbanHandler(Logger log, DarsService darsService) {
		super(log,darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{
			this.log.info("Esecuzione " + methodName + " in corso..."); 
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio+ ".idDominio.id");
			this.idDominio = this.getParameter(uriInfo, idDominioId, Long.class);
			URI esportazione = null;

			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);
			IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(bd);
			IbanAccreditoFilter filter = ibanAccreditoBD.newFilter();
			filter.setIdDominio(this.idDominio);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.IbanAccredito.model().COD_IBAN);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);
			filter.setOffset(offset);
			filter.setLimit(limit);


			long count = ibanAccreditoBD.count(filter);
			
			Map<String, String> params = new HashMap<String, String>();
			params.put(idDominioId, this.idDominio + "");


			Elenco elenco = new Elenco(this.titoloServizio, this.getInfoRicerca(uriInfo, bd, params),
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, this.getInfoCancellazione(uriInfo, bd)); 

			List<IbanAccredito> findAll = ibanAccreditoBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (IbanAccredito entry : findAll) {
					elenco.getElenco().add(this.getElemento(entry, entry.getId(), this.pathServizio,bd));
				}
			}

			this.log.info("Esecuzione " + methodName + " completata.");

			return elenco;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String,String> parameters) throws ConsoleException {
		URI ricerca =  this.getUriRicerca(uriInfo, bd, parameters);
		InfoForm infoRicerca = new InfoForm(ricerca);
		return infoRicerca;
	}

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI creazione = this.getUriCreazione(uriInfo, bd);
		InfoForm infoCreazione = new InfoForm(creazione,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.titolo"));

		String ibanAccreditoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String codIbanId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIban.id");
		String codIbanAppoggioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIbanAppoggio.id");
		String codBicAccreditoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codBicAccredito.id");
		String codBicAppoggioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codBicAppoggio.id");
		String idNegozioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idNegozio.id");
		String idSellerBankId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idSellerBank.id");
		String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String attivatoObepId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".attivatoObep.id");
		String postaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".postale.id");
		String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");

		if(this.infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoCreazione.getSezioneRoot();
		InputNumber idIbanAccredito = (InputNumber) this.infoCreazioneMap.get(ibanAccreditoId);
		idIbanAccredito.setDefaultValue(null);
		sezioneRoot.addField(idIbanAccredito);

		InputNumber idDominio = (InputNumber) this.infoCreazioneMap.get(idDominioId);
		idDominio.setDefaultValue(this.idDominio);
		sezioneRoot.addField(idDominio);

		InputText codIban = (InputText) this.infoCreazioneMap.get(codIbanId);
		codIban.setDefaultValue(null);
		codIban.setEditable(true);     
		sezioneRoot.addField(codIban);

		InputText codIbanAppoggio = (InputText) this.infoCreazioneMap.get(codIbanAppoggioId);
		codIbanAppoggio.setDefaultValue(null);
		sezioneRoot.addField(codIbanAppoggio);

		InputText codBicAccredito = (InputText) this.infoCreazioneMap.get(codBicAccreditoId);
		codBicAccredito.setDefaultValue(null);
		sezioneRoot.addField(codBicAccredito);

		InputText codBicAppoggio = (InputText) this.infoCreazioneMap.get(codBicAppoggioId);
		codBicAppoggio.setDefaultValue(null);
		sezioneRoot.addField(codBicAppoggio);

		IdNegozio idNegozio = (IdNegozio) this.infoCreazioneMap.get(idNegozioId);
		idNegozio.setDefaultValue(null);
		sezioneRoot.addField(idNegozio);

		IdSellerBank idSellerBank = (IdSellerBank) this.infoCreazioneMap.get(idSellerBankId);
		idSellerBank.setDefaultValue(null);
		sezioneRoot.addField(idSellerBank);

		CheckButton abilitato = (CheckButton) this.infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(true); 
		sezioneRoot.addField(abilitato);

		CheckButton attivatoObep = (CheckButton) this.infoCreazioneMap.get(attivatoObepId);
		attivatoObep.setDefaultValue(false); 
		sezioneRoot.addField(attivatoObep);

		CheckButton postale = (CheckButton) this.infoCreazioneMap.get(postaleId);
		postale.setDefaultValue(false); 
		sezioneRoot.addField(postale);

		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(this.infoCreazioneMap == null){
			this.infoCreazioneMap = new HashMap<String, ParamField<?>>();

			String ibanAccreditoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
			String codIbanId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIban.id");
			String codIbanAppoggioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIbanAppoggio.id");
			String codBicAccreditoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codBicAccredito.id");
			String codBicAppoggioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codBicAppoggio.id");
			String idNegozioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idNegozio.id");
			String idSellerBankId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idSellerBank.id");
			String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
			String attivatoObepId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".attivatoObep.id");
			String postaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".postale.id");
			String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");

			// id 
			InputNumber id = new InputNumber(ibanAccreditoId, null, null, true, true, false, 1, 20);
			this.infoCreazioneMap.put(ibanAccreditoId, id);

			// idDominio
			InputNumber idDominio = new InputNumber(idDominioId, null, null, true, true, false, 1, 255);
			this.infoCreazioneMap.put(idDominioId, idDominio);

			// codIban
			String codIbanLabel =  Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIban.label");
			InputText codIban = new InputText(codIbanId, codIbanLabel, null, true, false, true, 5, 34);
			codIban.setValidation(patternIBAN, Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIban.errorMessage"));
			codIban.setSuggestion(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIban.suggestion"));
			this.infoCreazioneMap.put(codIbanId, codIban);

			// codIbanAppoggio
			String codIbanAppoggioLabel =  Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIbanAppoggio.label");
			InputText codIbanAppoggio = new InputText(codIbanAppoggioId, codIbanAppoggioLabel, null, false, false, true, 5, 34);
			codIbanAppoggio.setValidation(patternIBAN, Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIbanAppoggio.errorMessage"));
			codIbanAppoggio.setAvanzata(true);
			//codIbanAppoggio.setSuggestion(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIbanAppoggio.suggestion"));
			this.infoCreazioneMap.put(codIbanAppoggioId, codIbanAppoggio);

			// codBicAccredito
			String codBicAccreditoLabel =  Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codBicAccredito.label");
			InputText codBicAccredito = new InputText(codBicAccreditoId, codBicAccreditoLabel, null, false, false, true, 8, 11);
			codBicAccredito.setValidation(patternBIC, Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codBicAccredito.errorMessage"));
			//codBicAccredito.setSuggestion(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codBicAccredito.suggestion"));
			this.infoCreazioneMap.put(codBicAccreditoId, codBicAccredito);

			// codBicAppoggio
			String codBicAppoggioLabel =  Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codBicAppoggio.label");
			InputText codBicAppoggio = new InputText(codBicAppoggioId, codBicAppoggioLabel, null, false, false, true, 8, 11);
			codBicAppoggio.setAvanzata(true); 
			codBicAppoggio.setValidation(patternBIC, Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codBicAppoggio.errorMessage"));
			//codBicAppoggio.setSuggestion(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codBicAppoggio.suggestion"));
			this.infoCreazioneMap.put(codBicAppoggioId, codBicAppoggio);

			// attivatoObep
			String attivatoObepLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".attivatoObep.label");
			CheckButton attivatoObep = new CheckButton(attivatoObepId, attivatoObepLabel, null, false, false, true);
			this.infoCreazioneMap.put(attivatoObepId, attivatoObep);

			List<RawParamValue> attivatoObepValues = new ArrayList<RawParamValue>();
			attivatoObepValues.add(new RawParamValue(attivatoObepId, "false")); 
			// idNegozio
			String idNegozioLabel =  Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idNegozio.label");
			URI idNegozioRefreshUri = this.getUriField(uriInfo, bd, idNegozioId); 

			IdNegozio idNegozio = new IdNegozio(this.nomeServizio, idNegozioId, idNegozioLabel, 1, 255, idNegozioRefreshUri , attivatoObepValues,this.getLanguage()); 
			idNegozio.addDependencyField(attivatoObep);
			idNegozio.init(attivatoObepValues,this.getLanguage());
			this.infoCreazioneMap.put(idNegozioId,idNegozio);

			// idSellerBank
			String idSellerBankLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idSellerBank.label");
			URI idSellerBankRefreshUri = this.getUriField(uriInfo, bd, idSellerBankId); 
			IdSellerBank idSellerBank = new IdSellerBank(this.nomeServizio, idSellerBankId, idSellerBankLabel, 1, 255, idSellerBankRefreshUri , attivatoObepValues,this.getLanguage());
			idSellerBank.addDependencyField(attivatoObep);
			idSellerBank.init(attivatoObepValues,this.getLanguage());
			this.infoCreazioneMap.put(idSellerBankId, idSellerBank);

			// abilitato
			String abilitatoLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label");
			CheckButton abiliato = new CheckButton(abilitatoId, abilitatoLabel, null, false, false, true);
			this.infoCreazioneMap.put(abilitatoId, abiliato);

			// postale
			String postaleLabel = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".postale.label");
			CheckButton postale = new CheckButton(postaleId, postaleLabel, null, false, false, true);
			this.infoCreazioneMap.put(postaleId, postale);


		}
	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, IbanAccredito entry) throws ConsoleException {
		URI modifica = this.getUriModifica(uriInfo, bd);
		InfoForm infoModifica = new InfoForm(modifica,Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modifica.titolo"));

		String ibanAccreditoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String codIbanId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIban.id");
		String codIbanAppoggioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIbanAppoggio.id");
		String codBicAccreditoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codBicAccredito.id");
		String codBicAppoggioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codBicAppoggio.id");
		String idNegozioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idNegozio.id");
		String idSellerBankId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idSellerBank.id");
		String abilitatoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String attivatoObepId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".attivatoObep.id");
		String postaleId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".postale.id");
		String idDominioId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");

		if(this.infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoModifica.getSezioneRoot();
		InputNumber idIbanAccredito = (InputNumber) this.infoCreazioneMap.get(ibanAccreditoId);
		idIbanAccredito.setDefaultValue(entry.getId());
		sezioneRoot.addField(idIbanAccredito);

		InputNumber idDominio = (InputNumber) this.infoCreazioneMap.get(idDominioId);
		idDominio.setDefaultValue(entry.getIdDominio()); 
		sezioneRoot.addField(idDominio);

		InputText codIban = (InputText) this.infoCreazioneMap.get(codIbanId);
		codIban.setDefaultValue(entry.getCodIban());
		codIban.setEditable(false);     
		sezioneRoot.addField(codIban);

		InputText codIbanAppoggio = (InputText) this.infoCreazioneMap.get(codIbanAppoggioId);
		codIbanAppoggio.setDefaultValue(entry.getCodIbanAppoggio());
		sezioneRoot.addField(codIbanAppoggio);

		InputText codBicAccredito = (InputText) this.infoCreazioneMap.get(codBicAccreditoId);
		codBicAccredito.setDefaultValue(entry.getCodBicAccredito());
		sezioneRoot.addField(codBicAccredito);

		InputText codBicAppoggio = (InputText) this.infoCreazioneMap.get(codBicAppoggioId);
		codBicAppoggio.setDefaultValue(entry.getCodBicAppoggio());
		sezioneRoot.addField(codBicAppoggio);

		IdNegozio idNegozio = (IdNegozio) this.infoCreazioneMap.get(idNegozioId);
		idNegozio.setDefaultValue(entry.getIdNegozio());
		sezioneRoot.addField(idNegozio);

		IdSellerBank idSellerBank = (IdSellerBank) this.infoCreazioneMap.get(idSellerBankId);
		idSellerBank.setDefaultValue(entry.getIdSellerBank());
		sezioneRoot.addField(idSellerBank);

		CheckButton abilitato = (CheckButton) this.infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(entry.isAbilitato()); 
		sezioneRoot.addField(abilitato);

		CheckButton attivatoObep = (CheckButton) this.infoCreazioneMap.get(attivatoObepId);
		attivatoObep.setDefaultValue(entry.isAttivatoObep()); 
		sezioneRoot.addField(attivatoObep);

		CheckButton postale = (CheckButton) this.infoCreazioneMap.get(postaleId);
		postale.setDefaultValue(entry.isPostale()); 
		sezioneRoot.addField(postale);
		return infoModifica;
	}
	
	@Override
	public InfoForm getInfoCancellazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException { return null;}
	
	@Override
	public InfoForm getInfoCancellazioneDettaglio(UriInfo uriInfo, BasicBD bd, IbanAccredito entry) throws ConsoleException {
		return null;
	}

	@Override
	public Object getField(UriInfo uriInfo,List<RawParamValue>values, String fieldId,BasicBD bd) throws ConsoleException {
		this.log.debug("Richiesto field ["+fieldId+"]");
		try{
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);
			
			if(this.infoCreazioneMap == null){
				this.initInfoCreazione(uriInfo, bd);
			}

			if(this.infoCreazioneMap.containsKey(fieldId)){
				RefreshableParamField<?> paramField = (RefreshableParamField<?>) this.infoCreazioneMap.get(fieldId);

				paramField.aggiornaParametro(values,bd,this.getLanguage());

				return paramField;

			}

			this.log.debug("Field ["+fieldId+"] non presente.");

		}catch(Exception e){
			throw new ConsoleException(e);
		}
		return null;
	}
	
	@Override
	public Object getSearchField(UriInfo uriInfo, List<RawParamValue> values, String fieldId, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public Dettaglio getDettaglio(long id, UriInfo uriInfo, BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "dettaglio " + this.titoloServizio + "."+ id;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			// recupero oggetto
			IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(bd);
			IbanAccredito ibanAccredito = ibanAccreditoBD.getIbanAccredito(id);

			DominiBD dominiBD = new DominiBD(bd);
			Dominio dominio = dominiBD.getDominio(ibanAccredito.getIdDominio());

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,ibanAccredito);
			InfoForm infoCancellazione = this.getInfoCancellazioneDettaglio(uriInfo, bd, ibanAccredito);
			URI esportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(ibanAccredito,bd), esportazione, infoCancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			// dati dele dettaglio
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIban.label"), ibanAccredito.getCodIban());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("domini.codDominio.label"), dominio.getCodDominio());
			if(StringUtils.isNotEmpty(ibanAccredito.getCodIbanAppoggio())) {
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codIbanAppoggio.label"), ibanAccredito.getCodIbanAppoggio(),true);
			}
			if(StringUtils.isNotEmpty(ibanAccredito.getCodBicAccredito())) {
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codBicAccredito.label"), ibanAccredito.getCodBicAccredito(),true);
			}
			if(StringUtils.isNotEmpty(ibanAccredito.getCodBicAppoggio())) {
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codBicAppoggio.label"), ibanAccredito.getCodBicAppoggio(),true);
			}
			if(StringUtils.isNotEmpty(ibanAccredito.getIdNegozio())) {
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idNegozio.label"), ibanAccredito.getIdNegozio(),true);
			}
			if(StringUtils.isNotEmpty(ibanAccredito.getIdSellerBank())) {
				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".idSellerBank.label"), ibanAccredito.getIdSellerBank(),true);
			}
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getSiNoAsLabel(ibanAccredito.isAbilitato()));
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".attivatoObep.label"), Utils.getSiNoAsLabel(ibanAccredito.isAttivatoObep()));
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".postale.label"), Utils.getSiNoAsLabel(ibanAccredito.isPostale()));


			this.log.info("Esecuzione " + methodName + " completata.");

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
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			IbanAccredito entry = this.creaEntry(is, uriInfo, bd);

			this.checkEntry(entry, null);

			IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(bd);
			try{
				ibanAccreditoBD.getIbanAccredito(entry.getIdDominio(),entry.getCodIban());
				String msg = Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".oggettoEsistente", entry.getCodIban());
				throw new DuplicatedEntryException(msg);
			}catch(NotFoundException e){}

			ibanAccreditoBD.insertIbanAccredito(entry); 

			this.log.info("Esecuzione " + methodName + " completata.");

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
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			JsonConfig jsonConfig = new JsonConfig();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();

			JSONObject jsonObjectIBAN = JSONObject.fromObject( baos.toString() );  
			jsonConfig.setRootClass(IbanAccredito.class);
			entry = (IbanAccredito) JSONObject.toBean( jsonObjectIBAN, jsonConfig );

			this.log.info("Esecuzione " + methodName + " completata.");
			return entry;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}
	@Override
	public void checkEntry(IbanAccredito entry, IbanAccredito oldEntry) throws ValidationException {
		if(entry.getCodIban() == null) {
			throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreIbanAccreditoObbligatorio"));
		}

		if(entry.getCodIban().length() < 5 || entry.getCodIban().length() > 34) {
			throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".creazione.lunghezzaIbanAccreditoErrata",entry.getCodIban().length()));
		}

		Pattern ibanPattern= Pattern.compile(patternIBAN);
		Matcher matcher = ibanPattern.matcher(entry.getCodIban());

		if(!matcher.matches()) {
			throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.formatoIbanAccreditoErrato"));
		} 

		if(StringUtils.isNotEmpty(entry.getCodIbanAppoggio())){
			if(entry.getCodIbanAppoggio().length() < 5 || entry.getCodIbanAppoggio().length() > 34) {
				throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".creazione.lunghezzaIbanAppoggioErrata", entry.getCodIbanAppoggio().length()));
			}

			matcher = ibanPattern.matcher(entry.getCodIbanAppoggio());
			if(!matcher.matches()) {
				throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.formatoIbanAppoggioErrato"));
			}
		}
		
		// validazione dei bic
		Pattern bicPattern= Pattern.compile(patternBIC); //[A-Z]{6,6}[A-Z2-9][A-NP-Z0-9]([A-Z0-9]{3,3}){0,1}
		
		if(StringUtils.isNotEmpty(entry.getCodBicAccredito())){
			if(entry.getCodBicAccredito().length() < 8 || entry.getCodBicAccredito().length() > 11) {
				throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".creazione.lunghezzaBicAccreditoErrata", entry.getCodBicAccredito().length()));
			}

			Matcher bicMtcher = bicPattern.matcher(entry.getCodBicAccredito());
			if(!bicMtcher.matches()) {
				throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.formatoBicAccreditoErrato"));
			}
		}
		
		if(StringUtils.isNotEmpty(entry.getCodBicAppoggio())){
			if(entry.getCodBicAppoggio().length() < 8 || entry.getCodBicAppoggio().length() > 11) {
				throw new ValidationException(
						Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".creazione.lunghezzaBicAppoggioErrata", entry.getCodBicAppoggio().length()));
			}

			Matcher bicMtcher = bicPattern.matcher(entry.getCodBicAppoggio());
			if(!bicMtcher.matches()) {
				throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".creazione.formatoBicAppoggioErrato"));
			}
		}

		// update
		if(oldEntry != null){
			if(!entry.getCodIban().equals(oldEntry.getCodIban())) {
				throw new ValidationException(Utils.getInstance(this.getLanguage()).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".aggiornamento.erroreIbanAccreditoNonCoincide",oldEntry.getCodIban(),entry.getCodIban()));
			}
		}

	}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException {
		String methodName = "Update " + this.titoloServizio;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			IbanAccredito entry = this.creaEntry(is, uriInfo, bd);

			IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(bd);
			IbanAccredito oldEntry = ibanAccreditoBD.getIbanAccredito(entry.getIdDominio(),entry.getCodIban());

			this.checkEntry(entry, oldEntry); 

			ibanAccreditoBD.updateIbanAccredito(entry); 

			this.log.info("Esecuzione " + methodName + " completata.");
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
	public Elenco delete(List<Long> idsToDelete, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, DeleteException {	return null; 	}

	@Override
	public String getTitolo(IbanAccredito entry, BasicBD bd) {
		return entry.getCodIban();
	}

	@Override
	public String getSottotitolo(IbanAccredito entry, BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		sb.append(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label")).append(": ").append(Utils.getSiNoAsLabel(entry.isAbilitato()));
		sb.append(", ").append(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".attivatoObep.label")).append(": ").append(Utils.getSiNoAsLabel(entry.isAttivatoObep()));
		sb.append(", ").append(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".postale.label")).append(": ").append(Utils.getSiNoAsLabel(entry.isPostale()));

		return Utils.getAbilitatoAsLabel(entry.isAbilitato()); 
	}
	
	@Override
	public Map<String, Voce<String>> getVoci(IbanAccredito entry, BasicBD bd) throws ConsoleException { return null; }

	@Override
	public String esporta(List<Long> idsToExport, List<RawParamValue> rawValues, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException,ExportException {
		return null;
	}
	
	@Override
	public String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)	throws WebApplicationException, ConsoleException,ExportException {
		return null;
	}

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}
