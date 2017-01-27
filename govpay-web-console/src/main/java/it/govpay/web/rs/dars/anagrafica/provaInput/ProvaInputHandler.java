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
package it.govpay.web.rs.dars.anagrafica.provaInput;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
//import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.anagrafica.IbanAccreditoBD;
import it.govpay.bd.anagrafica.TipiTributoBD;
import it.govpay.bd.anagrafica.TributiBD;
import it.govpay.bd.anagrafica.filters.IbanAccreditoFilter;
import it.govpay.bd.anagrafica.filters.TipoTributoFilter;
import it.govpay.bd.anagrafica.filters.TributoFilter;
import it.govpay.bd.model.Tributo;
import it.govpay.model.IbanAccredito;
import it.govpay.model.TipoTributo;
import it.govpay.model.Tributo.TipoContabilta;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.BaseDarsHandler;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.IDarsHandler;
//import it.govpay.web.rs.dars.anagrafica.tributi.input.CodContabilita;
//import it.govpay.web.rs.dars.anagrafica.tributi.input.CodificaTributoInIuv;
//import it.govpay.web.rs.dars.anagrafica.tributi.input.TipoContabilita;
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
import it.govpay.web.rs.dars.model.input.RefreshableParamField;
import it.govpay.web.rs.dars.model.input.base.CheckButton;
import it.govpay.web.rs.dars.model.input.base.InputFile;
import it.govpay.web.rs.dars.model.input.base.InputNumber;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.rs.dars.model.input.base.InputTextArea;
import it.govpay.web.rs.dars.model.input.base.SelectList;
import it.govpay.web.utils.Utils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class ProvaInputHandler extends BaseDarsHandler<Tributo> implements IDarsHandler<Tributo>{

	private static Map<String, ParamField<?>> infoCreazioneMap = null;
	private static Map<String, ParamField<?>> infoRicercaMap = null;
//	private Long idDominio = null;

	public ProvaInputHandler(Logger log, BaseDarsService darsService) {
		super(log,darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo,BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;
		try{	
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			Integer offset = this.getOffset(uriInfo);
			Integer limit = this.getLimit(uriInfo);
			URI esportazione = null;
			URI cancellazione = null;

			boolean visualizzaRicerca = true;
			this.log.info("Esecuzione " + methodName + " in corso..."); 

			TributiBD tributiBD = new TributiBD(bd);
			TributoFilter filter = tributiBD.newFilter();
			filter.setOffset(offset);
			filter.setLimit(limit);
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.Tributo.model().TIPO_TRIBUTO.COD_TRIBUTO);
			fsw.setSortOrder(SortOrder.ASC);
			filter.getFilterSortList().add(fsw);

//			String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio+ ".idDominio.id");
//			this.idDominio = this.getParameter(uriInfo, idDominioId, Long.class);
//
//			filter.setIdDominio(this.idDominio);

			String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");
			String codTributo = this.getParameter(uriInfo, codTributoId, String.class);
			filter.setCodTributo(codTributo); 

			long count = tributiBD.count(filter);

			// visualizza la ricerca solo se i risultati sono > del limit
			visualizzaRicerca = visualizzaRicerca && this.visualizzaRicerca(count, limit);

			InfoForm infoRicerca = visualizzaRicerca ? this.getInfoRicerca(uriInfo, bd) : null;

			Elenco elenco = new Elenco(this.titoloServizio, infoRicerca, this.getInfoCreazione(uriInfo, bd), count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}");

			List<Tributo> findAll = tributiBD.findAll(filter);

			if(findAll != null && findAll.size() > 0){
				for (Tributo entry : findAll) {
					elenco.getElenco().add(this.getElemento(entry, entry.getId(), uriDettaglioBuilder,bd));
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
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		//String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
		URI ricerca =  null;
		try{
			ricerca =  this.getUriRicerca(uriInfo, bd);// BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).queryParam(idDominioId, this.idDominio).build();
		}catch(Exception e ){
			throw new ConsoleException(e);
		}
		InfoForm infoRicerca = new InfoForm(ricerca);

		String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");

		if(infoRicercaMap == null){
			this.initInfoRicerca(uriInfo, bd);
		}

		Sezione sezioneRoot = infoRicerca.getSezioneRoot();

		InputText codTributo = (InputText) infoRicercaMap.get(codTributoId);
		codTributo.setDefaultValue(null);
		sezioneRoot.addField(codTributo);
		return infoRicerca;
	}

	private void initInfoRicerca(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoRicercaMap == null){
			infoRicercaMap = new HashMap<String, ParamField<?>>();

			String codTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.id");

			// codTributo
			String codTributoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.label");
			InputText codTributo = new InputText(codTributoId, codTributoLabel, null, false, false, true, 1, 255);
			infoRicercaMap.put(codTributoId, codTributo);

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		URI creazione = this.getUriCreazione(uriInfo, bd);
		InfoForm infoCreazione = new InfoForm(creazione,Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".creazione.titolo"));

		String tributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String idTipoTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idTipoTributo.id");
		String fileProvaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".fileProva.id");
		String risultatoUploadId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".risultatoUpload.id");
		String contenutoFileId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".contenutoFile.id");
		
		if(infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);

		}

		Sezione sezioneRoot = infoCreazione.getSezioneRoot();

		InputNumber idITributo = (InputNumber) infoCreazioneMap.get(tributoId);
		idITributo.setDefaultValue(null);
		sezioneRoot.addField(idITributo);

		SelectList<Long> idTipoTributo  = (SelectList<Long>) infoCreazioneMap.get(idTipoTributoId);
		List<Voce<Long>> idTipoTributoValues = new ArrayList<Voce<Long>>();

		try{
			// 1. prelevo i tipi tributi gia' definiti per il dominio

			List<Long> listaIdTipiTributoDaEscludere = new ArrayList<Long>(); // tributiBD.getIdTipiTributiDefinitiPerDominio(this.idDominio);

			TipiTributoBD tipiTributoBD = new TipiTributoBD(bd);
			TipoTributoFilter filterTipiTributi = tipiTributoBD.newFilter();
			filterTipiTributi.setListaIdTributiDaEscludere(listaIdTipiTributoDaEscludere );
			FilterSortWrapper fsw = new FilterSortWrapper();
			fsw.setField(it.govpay.orm.TipoTributo.model().DESCRIZIONE);
			fsw.setSortOrder(SortOrder.ASC);
			filterTipiTributi.getFilterSortList().add(fsw);
			List<it.govpay.model.TipoTributo> findAll = tipiTributoBD.findAll(filterTipiTributi);
			if(findAll != null && findAll.size() > 0){
				for (it.govpay.model.TipoTributo tipoTributo : findAll) {
					String label = Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".idTipoTributo.label.form", tipoTributo.getDescrizione(),tipoTributo.getCodTributo());
					idTipoTributoValues.add(new Voce<Long>(label, tipoTributo.getId()));
				}
			}
		}catch(Exception e){
			throw new ConsoleException(e);
		}
		idTipoTributo.setEditable(true);
		idTipoTributo.setHidden(false);
		idTipoTributo.setValues(idTipoTributoValues);
		idTipoTributo.setDefaultValue(null);
		sezioneRoot.addField(idTipoTributo);
		
		InputFile fileProva = (InputFile) infoCreazioneMap.get(fileProvaId);
		fileProva.setDefaultValue(null);
		sezioneRoot.addField(fileProva);
		
		InputText risultatoUpload = (InputText) infoCreazioneMap.get(risultatoUploadId);
		risultatoUpload.setDefaultValue(null);
		sezioneRoot.addField(risultatoUpload);
		
		InputTextArea contenutoFile = (InputTextArea) infoCreazioneMap.get(contenutoFileId);
		contenutoFile.setDefaultValue(null);
		sezioneRoot.addField(contenutoFile);


		return infoCreazione;
	}

	private void initInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException{
		if(infoCreazioneMap == null){
			infoCreazioneMap = new HashMap<String, ParamField<?>>();

			String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
			String idIbanAccreditoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idIbanAccredito.id");
			String tributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
			String idTipoTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idTipoTributo.id");
			String risultatoUploadId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".risultatoUpload.id");
			String contenutoFileId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".contenutoFile.id");
			
			// id 
			InputNumber id = new InputNumber(tributoId, null, null, true, true, false, 1, 20);
			infoCreazioneMap.put(tributoId, id);

			// tipoTributo
			String idTipoTributoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idTipoTributo.label");
			List<Voce<Long>> idTipoTributoValues = new ArrayList<Voce<Long>>();
			SelectList<Long> idTipoTributo = new SelectList<Long>(idTipoTributoId, idTipoTributoLabel, null, true, false, true, idTipoTributoValues );
			idTipoTributo.setSuggestion(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idTipoTributo.suggestion"));
			infoCreazioneMap.put(idTipoTributoId, idTipoTributo);

			// idDominio
			InputNumber idDominio = new InputNumber(idDominioId, null, null, true, true, false, 1, 255);
			infoCreazioneMap.put(idDominioId, idDominio);

			String idIbanAccreditoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idIbanAccredito.label");
			List<Voce<Long>> ibanValues = new ArrayList<Voce<Long>>();
			SelectList<Long> idIbanAccredito = new SelectList<Long>(idIbanAccreditoId, idIbanAccreditoLabel, null, true, false, true, ibanValues );
			infoCreazioneMap.put(idIbanAccreditoId, idIbanAccredito);

			try {
				String fileProvaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".fileProva.id");
				String fileProvaLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".fileProva.label");

				List<String> mt = new ArrayList<String>();
				//mt.add(".*");
				UriBuilder uriUploadBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("upload");

				Long dimensioneMassimaFileTracciato = 1000000000L;
				int numeroFileTracciato = 1;

				InputFile fileProva = new InputFile(fileProvaId,fileProvaLabel, true, false, true, mt , dimensioneMassimaFileTracciato ,numeroFileTracciato,uriUploadBuilder.build());
				fileProva.setSuggestion(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".fileProva.suggestion"));
				infoCreazioneMap.put(fileProvaId, fileProva);
			} catch(Exception e ){
				throw new ConsoleException(e); 
			}
			
			// risultatoUpload
			String risultatoUploadLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".risultatoUpload.label");
			InputText risultatoUpload = new InputText(risultatoUploadId, risultatoUploadLabel, null, false, false, true, 1, 255);
			infoCreazioneMap.put(risultatoUploadId, risultatoUpload);

			// contenuto file
			String contenutoFileLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".contenutoFile.label");
			InputTextArea contenutoFile = new InputTextArea(contenutoFileId, contenutoFileLabel, null, false, false, true, 1, 50000, 10, 200);
			infoCreazioneMap.put(contenutoFileId, contenutoFile);
		}


	}

	@SuppressWarnings("unchecked")
	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, Tributo entry) throws ConsoleException {
		URI modifica = this.getUriModifica(uriInfo, bd);
		InfoForm infoModifica = new InfoForm(modifica,Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".modifica.titolo"));

		String idDominioId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idDominio.id");
		String idIbanAccreditoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idIbanAccredito.id");
		String abilitatoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.id");
		String tributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".id.id");
		String tipoContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.id");
		String codContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codContabilita.id");
		String idTipoTributoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idTipoTributo.id");
		String codificaTributoInIuvId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codificaTributoInIuv.id");

		if(infoCreazioneMap == null){
			this.initInfoCreazione(uriInfo, bd);
		}

		Sezione sezioneRoot = infoModifica.getSezioneRoot();
		InputNumber idTributo = (InputNumber) infoCreazioneMap.get(tributoId);
		idTributo.setDefaultValue(entry.getId());
		sezioneRoot.addField(idTributo);

		InputNumber idTipoTributo = new InputNumber(idTipoTributoId, null, null, true, true, false, 1, 255);
		idTipoTributo.setDefaultValue(entry.getIdTipoTributo());
		sezioneRoot.addField(idTipoTributo);

		InputNumber idDominio = (InputNumber) infoCreazioneMap.get(idDominioId);
		idDominio.setDefaultValue(entry.getIdDominio());
		sezioneRoot.addField(idDominio);

		SelectList<Long> idIbanAccredito  = (SelectList<Long>) infoCreazioneMap.get(idIbanAccreditoId);
		List<Voce<Long>> listaIban = new ArrayList<Voce<Long>>();

		if(!entry.getCodTributo().equals(Tributo.BOLLOT)){
			try{
				DominiBD dominiBD = new DominiBD(bd);
				IbanAccreditoBD ibanAccreditoBD = new IbanAccreditoBD(bd);
				IbanAccreditoFilter filterIban = ibanAccreditoBD.newFilter();
				FilterSortWrapper fsw = new FilterSortWrapper();
				fsw.setField(it.govpay.orm.IbanAccredito.model().COD_IBAN);
				fsw.setSortOrder(SortOrder.ASC);
				filterIban.getFilterSortList().add(fsw);
				filterIban.setCodDominio(dominiBD.getDominio(entry.getIdDominio()).getCodDominio());   
				List<it.govpay.model.IbanAccredito> findAll = ibanAccreditoBD.findAll(filterIban);

				if(findAll != null && findAll.size() > 0){
					for (it.govpay.model.IbanAccredito ib : findAll) {
						listaIban.add(new Voce<Long>(ib.getCodIban(), ib.getId()));  
					}
				}

			}catch(Exception e){
				throw new ConsoleException(e);
			}
			idIbanAccredito.setEditable(true);
			idIbanAccredito.setHidden(false);
			idIbanAccredito.setRequired(true);
		} else {
			idIbanAccredito.setEditable(false);
			idIbanAccredito.setHidden(true);
			idIbanAccredito.setRequired(false);
		}

		idIbanAccredito.setValues(listaIban);
		idIbanAccredito.setDefaultValue(entry.getIdIbanAccredito());
		sezioneRoot.addField(idIbanAccredito);

		// prelevo le versioni statiche per l'update
		SelectList<String> tipoContabilita = (SelectList<String>) infoCreazioneMap.get(tipoContabilitaId+"_update");

		TipoContabilta tipoContabilitaCustom = entry.getTipoContabilitaCustom();
		TipoContabilta tipoContabilitaDefault = entry.getTipoContabilitaDefault();

		List<Voce<String>> lst = new ArrayList<Voce<String>>();
		if(tipoContabilitaDefault != null){
			switch(tipoContabilitaDefault){
			case ALTRO:
				lst.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.altro.default"), TipoContabilta.ALTRO.getCodifica() + "_p"));
				break;
			case SIOPE:
				lst.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.siope.default"), TipoContabilta.SIOPE.getCodifica() + "_p"));
				break;
			case SPECIALE:
				lst.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.speciale.default"), TipoContabilta.SPECIALE.getCodifica() + "_p"));
				break;
			case CAPITOLO:
			default:
				lst.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.capitolo.default"), TipoContabilta.CAPITOLO.getCodifica() + "_p"));
				break;
			}
		}
		
		lst.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.capitolo"), TipoContabilta.CAPITOLO.getCodifica()));
		lst.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.speciale"), TipoContabilta.SPECIALE.getCodifica()));
		lst.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.siope"), TipoContabilta.SIOPE.getCodifica()));
		lst.add(new Voce<String>(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.altro"), TipoContabilta.ALTRO.getCodifica()));
		

		if(tipoContabilitaCustom == null)
			tipoContabilita.setDefaultValue(tipoContabilitaDefault.getCodifica() + "_p");
		else
			tipoContabilita.setDefaultValue(tipoContabilitaCustom.getCodifica());

		tipoContabilita.setValues(lst); 
		sezioneRoot.addField(tipoContabilita);

		InputText codContabilita = (InputText) infoCreazioneMap.get(codContabilitaId+"_update");
		String codContabilitaLabel = StringUtils.isNotEmpty(entry.getCodContabilitaDefault()) ?	Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".codContabilita.label.default.form",entry.getCodContabilitaDefault()) :	Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codContabilita.label");
		codContabilita.setLabel(codContabilitaLabel);
		codContabilita.setDefaultValue(entry.getCodContabilitaCustom());
		sezioneRoot.addField(codContabilita);

		InputText codificaTributoInIuv = (InputText) infoCreazioneMap.get(codificaTributoInIuvId+"_update");
		String codificaTributoInIuvLabel = StringUtils.isNotEmpty(entry.getCodTributoIuvDefault()) ? Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".codificaTributoInIuv.label.default.form",entry.getCodTributoIuvDefault()) : Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codificaTributoInIuv.label");

		codificaTributoInIuv.setLabel(codificaTributoInIuvLabel);
		codificaTributoInIuv.setDefaultValue(entry.getCodTributoIuvCustom());
		sezioneRoot.addField(codificaTributoInIuv);

		CheckButton abilitato = (CheckButton) infoCreazioneMap.get(abilitatoId);
		abilitato.setDefaultValue(entry.isAbilitato()); 
		sezioneRoot.addField(abilitato);

		return infoModifica;
	}

	@Override
	public Object getField(UriInfo uriInfo,List<RawParamValue>values, String fieldId,BasicBD bd) throws WebApplicationException,ConsoleException {
		this.log.debug("Richiesto field ["+fieldId+"]");
		try{
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			if(infoCreazioneMap == null){
				this.initInfoCreazione(uriInfo, bd);
			}

			if(infoCreazioneMap.containsKey(fieldId)){
				RefreshableParamField<?> paramField = (RefreshableParamField<?>) infoCreazioneMap.get(fieldId);
				paramField.aggiornaParametro(values,bd);
				return paramField;
			}

			this.log.debug("Field ["+fieldId+"] non presente.");

		}catch(Exception e){
			throw new ConsoleException(e);
		}
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
			TributiBD tributiBD = new TributiBD(bd);
			Tributo tributo = tributiBD.getTributo(id);

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,tributo);
			URI cancellazione = null;
			URI esportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(tributo,bd), esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot(); 

			// dati del tributo
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codTributo.label"), tributo.getCodTributo());
			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".descrizione.label"), tributo.getDescrizione());

			if(tributo.getIdIbanAccredito() != null){
				IbanAccredito ibanAccredito = tributo.getIbanAccredito(bd); 
				root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".idIbanAccredito.label"), ibanAccredito.getCodIban());
			}

			TipoContabilta tipoContabilita = tributo.getTipoContabilita() != null ? tributo.getTipoContabilita() : TipoContabilta.CAPITOLO;
			String tipoContabilitaValue = null;
			switch (tipoContabilita) {
			case ALTRO:
				tipoContabilitaValue = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.altro");
				break;
			case SPECIALE:
				tipoContabilitaValue = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.speciale");
				break;
			case SIOPE:
				tipoContabilitaValue = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.siope");
				break;
			case CAPITOLO:
			default:
				tipoContabilitaValue = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.capitolo");				
				break;
			}

			String tipoContabilitaLabel = !tributo.isTipoContabilitaCustom() ? Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.label.default") : Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.label.custom");
			root.addVoce(tipoContabilitaLabel, tipoContabilitaValue);

			String codContabilitaLabel = !tributo.isCodContabilitaCustom() ? Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codContabilita.label.default") : Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codContabilita.label.custom");
			root.addVoce(codContabilitaLabel, tributo.getCodContabilita());

			if(StringUtils.isNotEmpty(tributo.getCodTributoIuv())){
				String codificaTributoInIuvLabel = !tributo.isCodTributoIuvCustom() ? Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codificaTributoInIuv.label.default") : Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codificaTributoInIuv.label.custom");
				root.addVoce(codificaTributoInIuvLabel, tributo.getCodTributoIuv(),true);
			}

			root.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getSiNoAsLabel(tributo.isAbilitato()));

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
			throws WebApplicationException, ConsoleException,ValidationException,DuplicatedEntryException {
		String methodName = "Insert " + this.titoloServizio;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

//			Tributo entry = this.creaEntry(is, uriInfo, bd);
//
//			this.checkEntry(entry, null);
//
//			TributiBD tributiBD = new TributiBD(bd);
//
//			try{
//				tributiBD.getTributo(entry.getIdDominio(),entry.getCodTributo());
//				String msg = Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".oggettoEsistente", entry.getCodTributo());
//				throw new DuplicatedEntryException(msg);
//			}catch(NotFoundException e){}
//
//			tributiBD.insertTributo(entry); 

			this.log.info("Esecuzione " + methodName + " completata.");

			return this.getDettaglio(8l, uriInfo, bd);
//		}catch(DuplicatedEntryException e){
//			throw e;
//		}catch(ValidationException e){
//			throw e;
		}
		catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public Tributo creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		String methodName = "creaEntry " + this.titoloServizio;
		Tributo entry = null;
		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			JsonConfig jsonConfig = new JsonConfig();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copy(is, baos);

			baos.flush();
			baos.close();

			JSONObject jsonObject = JSONObject.fromObject( baos.toString() ); 

			String tipoContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.id");
			String codContabilitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codContabilita.id");
			String codificaTributoInIuvId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".codificaTributoInIuv.id");

			String tipocontabilitaS = jsonObject.getString(tipoContabilitaId);
			jsonObject.remove(tipoContabilitaId);

			String codContabilitaS =  jsonObject.getString(codContabilitaId);
			jsonObject.remove(codContabilitaId);

			String codificaTributoInIuvS =  jsonObject.getString(codificaTributoInIuvId);
			jsonObject.remove(codificaTributoInIuvId);

			jsonConfig.setRootClass(Tributo.class);
			entry = (Tributo) JSONObject.toBean( jsonObject, jsonConfig );

			TipiTributoBD tributiBD = new TipiTributoBD(bd);
			TipoTributo t = tributiBD.getTipoTributo(entry.getIdTipoTributo());
			entry.setCodTributo(t.getCodTributo());
			entry.setDescrizione(t.getDescrizione());

			// imposto i valori custom solo se sono valorizzati correttamente. 
			if(StringUtils.isNotEmpty(tipocontabilitaS) && !tipocontabilitaS.endsWith("_p")){
				TipoContabilta tipoContabilita =  TipoContabilta.toEnum(tipocontabilitaS);
				entry.setTipoContabilitaCustom(tipoContabilita);
			} 

			if(StringUtils.isNotBlank(codContabilitaS))
				entry.setCodContabilitaCustom(codContabilitaS);

			if(StringUtils.isNotBlank(codificaTributoInIuvS))
				entry.setCodTributoIuvCustom(codificaTributoInIuvS); 

			this.log.info("Esecuzione " + methodName + " completata.");
			return entry;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public void checkEntry(Tributo entry, Tributo oldEntry) throws ValidationException {
		if(entry == null || entry.getIdTipoTributo() == 0 ) throw new ValidationException(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreTipoObbligatorio"));
		if(entry == null || entry.getIdDominio() == 0) throw new ValidationException(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreDominioObbligatorio"));
		if(!entry.getCodTributo().equals(Tributo.BOLLOT) && entry.getIdIbanAccredito() == null ) throw new ValidationException(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreIbanAccreditoObbligatorio"));
		//		if(entry == null || entry.getTipoContabilita() == null) throw new ValidationException(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreTipoContabilitaObbligatorio"));
		//		if(entry == null || entry.getCodContabilita() == null || entry.getCodContabilita().isEmpty()) throw new ValidationException(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreCodContabilitaObbligatorio"));
		if(entry.getCodContabilitaCustom() != null && StringUtils.contains(entry.getCodContabilitaCustom()," ")) throw new ValidationException(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".creazione.erroreCodContabilitaNoSpazi"));
		
		if(oldEntry != null) {
			if(entry.getIdTipoTributo() != oldEntry.getIdTipoTributo()) throw new ValidationException(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".aggiornamento.erroreTipoModificato"));
			if(entry.getIdDominio() != oldEntry.getIdDominio()) throw new ValidationException(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".aggiornamento.erroreDominioModificato"));
		}
	}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException {
		String methodName = "Update " + this.titoloServizio;

		try{
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

//			Tributo entry = this.creaEntry(is, uriInfo, bd);
//
//			TributiBD tributiBD = new TributiBD(bd);
//			Tributo oldEntry = tributiBD.getTributo(entry.getIdDominio(),entry.getCodTributo());
//
//			this.checkEntry(entry, oldEntry);
//
//			tributiBD.updateTributo(entry); 

			this.log.info("Esecuzione " + methodName + " completata.");
			return this.getDettaglio(8l, uriInfo, bd);
//		}catch(ValidationException e){
//			throw e;
		}catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
	}

	@Override
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		// operazione non prevista
	}

	@Override
	public String getTitolo(Tributo entry, BasicBD bd) {
		StringBuilder sb = new StringBuilder();
		sb.append(Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.titolo", entry.getDescrizione()));
		return sb.toString();
	}

	@Override
	public String getSottotitolo(Tributo entry, BasicBD bd) throws ConsoleException {
		StringBuilder sb = new StringBuilder();

		TipoContabilta tipoContabilita = entry.getTipoContabilitaDefault() != null ? entry.getTipoContabilitaDefault() : TipoContabilta.CAPITOLO;
		String tipoContabilitaValue = null;
		switch (tipoContabilita) {
		case ALTRO:
			tipoContabilitaValue = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.altro");
			break;
		case SPECIALE:
			tipoContabilitaValue = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.speciale");
			break;
		case SIOPE:
			tipoContabilitaValue = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.siope");
			break;
		case CAPITOLO:
		default:
			tipoContabilitaValue = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".tipoContabilita.capitolo");				
			break;
		}

		String codContabilitaValue = StringUtils.isNotEmpty(entry.getCodContabilitaDefault()) ? entry.getCodContabilitaDefault() : "--";

		sb.append(Utils.getInstance().getMessageWithParamsFromResourceBundle(this.nomeServizio + ".label.sottotitolo",entry.getCodTributo(), tipoContabilitaValue,codContabilitaValue,Utils.getAbilitatoAsLabel(entry.isAbilitato())));

		return sb.toString();
	}

	@Override
	public List<String> getValori(Tributo entry, BasicBD bd) throws ConsoleException {
		return null;
	}

	@Override
	public String esporta(List<Long> idsToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)
			throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public String esporta(Long idToExport, UriInfo uriInfo, BasicBD bd, ZipOutputStream zout)	throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { 
		String methodName = "Simulazione caricamento file per il servizio " + this.titoloServizio;
		this.log.info("Esecuzione " + methodName + " in corso...");

		String risultatoUploadId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".risultatoUpload.id");
		String contenutoFileId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + ".contenutoFile.id");
		Map<String, List<InputPart>> formParts = input.getFormDataMap();

		JSONArray res = new JSONArray();
		List<InputPart> inPart = formParts.get("file");
		try{

			for (InputPart inputPart : inPart) {

				// Retrieve headers, read the Content-Disposition header to obtain the original name of the file
				MultivaluedMap<String, String> headers = inputPart.getHeaders();

				// Handle the body of that part with an InputStream
				InputStream is = inputPart.getBody(InputStream.class,null);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 	Utils.copy(is, baos);
				
				String ct = headers.getFirst("Content-Type");
				
				String fileName = Utils.getFileName(headers);
				
				String val = "Ricevuto file: [" + fileName + "], Content-Type: ["+ct+"], Dimensione: ["+baos.size()+" byte].";
				
//				File f = new File("/tmp/fileTemporaneo");
				
//				if(f.exists())
//					f.delete();
//				
//				if(!f.exists())
//					f.createNewFile();
				
//				FileOutputStream fos = new FileOutputStream(f);
				
//				Utils.copy(is, fos);
				
//				fos.close();

				Voce<String> voce = new Voce<String>(risultatoUploadId, val);

				res.add(voce);
				
				Voce<String> voce2 = new Voce<String>(contenutoFileId, baos.toString());

				res.add(voce2);
			}
			this.log.info("Esecuzione " + methodName + " completata.");
			return res;
		} catch(WebApplicationException e){
			throw e;
		}catch(Exception e){
			throw new ConsoleException(e);
		}
		
	}
}
