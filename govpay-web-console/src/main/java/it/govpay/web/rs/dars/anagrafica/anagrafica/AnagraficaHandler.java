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
package it.govpay.web.rs.dars.anagrafica.anagrafica;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Anagrafica;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.BaseDarsService;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.input.base.InputText;
import it.govpay.web.utils.Utils;

public class AnagraficaHandler {

	private static Map<String, Map<String, ParamField<?>>> infoCreazioneMap = null; 
	private String pathServizio =null;
	private String nomeServizio = null;
	private String nomeAnagrafica = null;

	public AnagraficaHandler(String nomeAnagrafica,String nomeServizio,String pathServizio){
		this.pathServizio = pathServizio;
		this.nomeServizio = nomeServizio;
		this.nomeAnagrafica = nomeAnagrafica;

		if(infoCreazioneMap == null)
			infoCreazioneMap = new HashMap<String, Map<String,ParamField<?>>>();
	}

	public List<ParamField<?>> getInfoCreazioneAnagraficaDominio(UriInfo uriInfo, BasicBD bd) throws ConsoleException {

		String ragioneSocialeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".ragioneSociale.id");
		String codUnivocoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".codUnivoco.id");
		String indirizzoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".indirizzo.id");
		String civicoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".civico.id");
		String capId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".cap.id");
		String localitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".localita.id");
		String provinciaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".provincia.id");
		String nazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".nazione.id");
		String emailId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".email.id");
		String telefonoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".telefono.id");
		String cellulareId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".cellulare.id");
		String faxId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".fax.id");

		Map<String, ParamField<?>> mappaCreazione = infoCreazioneMap.get(this.nomeServizio + "." + this.nomeAnagrafica);

		if(mappaCreazione == null){
			mappaCreazione = new HashMap<String, ParamField<?>>();


			// ragionesociale
			String ragioneSocialeLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".ragioneSociale.label");
			InputText ragineSociale = new InputText(ragioneSocialeId, ragioneSocialeLabel, null, true, false, true, 1, 70);
			//			ragineSociale.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".ragioneSociale.errorMessage"));
			mappaCreazione.put(ragioneSocialeId, ragineSociale);

			// codunivoco (hidden!)
			String codUnivocoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".codUnivoco.label");
			InputText codUnivoco = new InputText(codUnivocoId, codUnivocoLabel, null, false, true,false, 1, 35);
			//			codUnivoco.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".codUnivoco.errorMessage"));
			mappaCreazione.put(codUnivocoId, codUnivoco);

			// indirizzo
			String indirizzoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".indirizzo.label");
			InputText indirizzo = new InputText(indirizzoId, indirizzoLabel, null, false, false, true, 1, 70);
			//			indirizzo.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".indirizzo.errorMessage"));
			mappaCreazione.put(indirizzoId, indirizzo);

			// civico
			String civicoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica +  ".civico.label");
			InputText civico = new InputText(civicoId, civicoLabel, null, false, false, true, 1, 16);
			//			civico.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio  + "." + this.nomeAnagrafica + ".civico.errorMessage"));
			mappaCreazione.put(civicoId, civico);

			// cap
			String capLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica +  ".cap.label");
			InputText cap = new InputText(capId, capLabel, null, false, false, true, 1, 16);
			//			cap.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio  + "." + this.nomeAnagrafica + ".cap.errorMessage"));
			mappaCreazione.put(capId, cap);

			// localita
			String localitaLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".localita.label");
			InputText localita = new InputText(localitaId, localitaLabel, null, false, false, true, 1, 35);
			//			localita.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".localita.errorMessage"));
			mappaCreazione.put(localitaId, localita);

			// provincia
			String provinciaLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".provincia.label");
			InputText provincia = new InputText(provinciaId, provinciaLabel, null, false, false, true, 1, 35);
			//			provincia.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".provincia.errorMessage"));
			mappaCreazione.put(provinciaId, provincia);

			// nazione
			String nazioneLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".nazione.label");
			InputText nazione = new InputText(nazioneId, nazioneLabel, null, false, false, true, 1, 2);
			//			nazione.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".nazione.errorMessage"));
			mappaCreazione.put(nazioneId, nazione);

			// email
			String emailLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".email.label");
			InputText email = new InputText(emailId, emailLabel, null, false, true, false, 1, 255);
			//			email.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".email.errorMessage"));
			mappaCreazione.put(emailId, email);

			// telefono
			String telefonoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica +  ".telefono.label");
			InputText telefono = new InputText(telefonoId, telefonoLabel, null, false, true, false, 1, 255);
			//			telefono.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio  + "." + this.nomeAnagrafica + ".telefono.errorMessage"));
			mappaCreazione.put(telefonoId, telefono);

			// cellulare
			String cellulareLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica +  ".cellulare.label");
			InputText cellulare = new InputText(cellulareId, cellulareLabel, null, false, true, false, 1, 255);
			//	cellulare.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio  + "." + this.nomeAnagrafica + ".cellulare.errorMessage"));
			mappaCreazione.put(cellulareId, cellulare);

			// fax
			String faxLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica +  ".fax.label");
			InputText fax = new InputText(faxId, faxLabel, null, false, true, false, 1, 255);
			//	fax.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio  + "." + this.nomeAnagrafica + ".fax.errorMessage"));
			mappaCreazione.put(faxId, fax);

			infoCreazioneMap.put(this.nomeServizio + "." + this.nomeAnagrafica, mappaCreazione);

		}

		List<ParamField<?>> listaParametri = new ArrayList<ParamField<?>>();

		// lista field
		InputText ragioneSociale = (InputText) mappaCreazione.get(ragioneSocialeId);
		ragioneSociale.setDefaultValue(null);
		listaParametri.add(ragioneSociale);

		// Codunivoco
		InputText codUnivoco = (InputText) mappaCreazione.get(codUnivocoId);
		codUnivoco.setDefaultValue(null); 
		listaParametri.add(codUnivoco);

		//indirizzo
		InputText indirizzo = (InputText) mappaCreazione.get(indirizzoId);
		indirizzo.setDefaultValue(null);
		listaParametri.add(indirizzo);

		//civico
		InputText civico = (InputText) mappaCreazione.get(civicoId);
		civico.setDefaultValue(null);
		listaParametri.add(civico);

		// cap
		InputText cap = (InputText) mappaCreazione.get(capId);
		cap.setDefaultValue(null);
		listaParametri.add(cap);

		// localita
		InputText localita = (InputText) mappaCreazione.get(localitaId);
		localita.setDefaultValue(null);
		listaParametri.add(localita);

		// provincia
		InputText provincia = (InputText) mappaCreazione.get(provinciaId);
		provincia.setDefaultValue(null);
		listaParametri.add(provincia);

		// nazione 
		InputText nazione = (InputText) mappaCreazione.get(nazioneId);
		nazione.setDefaultValue(null);
		listaParametri.add(nazione);

		// email
		InputText email = (InputText) mappaCreazione.get(emailId);
		email.setDefaultValue(null);
		listaParametri.add(email);

		// telefono
		InputText telefono = (InputText) mappaCreazione.get(telefonoId);
		telefono.setDefaultValue(null);
		listaParametri.add(telefono);

		// cellulare
		InputText cellulare = (InputText) mappaCreazione.get(cellulareId);
		cellulare.setDefaultValue(null);
		listaParametri.add(cellulare);

		// fax
		InputText fax = (InputText) mappaCreazione.get(faxId);
		fax.setDefaultValue(null);
		listaParametri.add(fax);

		return listaParametri;
	}

	public List<ParamField<?>> getInfoCreazioneAnagraficaUO(UriInfo uriInfo, BasicBD bd) throws ConsoleException {

		String ragioneSocialeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".ragioneSociale.id");
		String codUnivocoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".codUnivoco.id");
		String indirizzoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".indirizzo.id");
		String civicoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".civico.id");
		String capId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".cap.id");
		String localitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".localita.id");
		String provinciaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".provincia.id");
		String nazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".nazione.id");
		String emailId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".email.id");
		String telefonoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".telefono.id");
		String cellulareId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".cellulare.id");
		String faxId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".fax.id");

		Map<String, ParamField<?>> mappaCreazione = infoCreazioneMap.get(this.nomeServizio + "." + this.nomeAnagrafica);

		if(mappaCreazione == null){
			mappaCreazione = new HashMap<String, ParamField<?>>();


			// ragionesociale
			String ragioneSocialeLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".ragioneSociale.label");
			InputText ragineSociale = new InputText(ragioneSocialeId, ragioneSocialeLabel, null, false, false, true, 1, 70);
			//			ragineSociale.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".ragioneSociale.errorMessage"));
			mappaCreazione.put(ragioneSocialeId, ragineSociale);

			// codunivoco (hidden!)
			String codUnivocoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".codUnivoco.label");
			InputText codUnivoco = new InputText(codUnivocoId, codUnivocoLabel, null, true, true,true, 1, 35);
			//			codUnivoco.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".codUnivoco.errorMessage"));
			mappaCreazione.put(codUnivocoId, codUnivoco);

			// indirizzo
			String indirizzoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".indirizzo.label");
			InputText indirizzo = new InputText(indirizzoId, indirizzoLabel, null, false, false, true, 1, 70);
			//			indirizzo.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".indirizzo.errorMessage"));
			mappaCreazione.put(indirizzoId, indirizzo);

			// civico
			String civicoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica +  ".civico.label");
			InputText civico = new InputText(civicoId, civicoLabel, null, false, false, true, 1, 16);
			//			civico.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio  + "." + this.nomeAnagrafica + ".civico.errorMessage"));
			mappaCreazione.put(civicoId, civico);

			// cap
			String capLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica +  ".cap.label");
			InputText cap = new InputText(capId, capLabel, null, false, false, true, 1, 16);
			//			cap.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio  + "." + this.nomeAnagrafica + ".cap.errorMessage"));
			mappaCreazione.put(capId, cap);

			// localita
			String localitaLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".localita.label");
			InputText localita = new InputText(localitaId, localitaLabel, null, false, false, true, 1, 35);
			//			localita.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".localita.errorMessage"));
			mappaCreazione.put(localitaId, localita);

			// provincia
			String provinciaLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".provincia.label");
			InputText provincia = new InputText(provinciaId, provinciaLabel, null, false, false, true, 1, 35);
			//			provincia.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".provincia.errorMessage"));
			mappaCreazione.put(provinciaId, provincia);

			// nazione
			String nazioneLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".nazione.label");
			InputText nazione = new InputText(nazioneId, nazioneLabel, null, false, false, true, 1, 2);
			//			nazione.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".nazione.errorMessage"));
			mappaCreazione.put(nazioneId, nazione);

			// email
			String emailLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".email.label");
			InputText email = new InputText(emailId, emailLabel, null, false, true, false, 1, 255);
			//			email.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".email.errorMessage"));
			mappaCreazione.put(emailId, email);

			// telefono
			String telefonoLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica +  ".telefono.label");
			InputText telefono = new InputText(telefonoId, telefonoLabel, null, false, true, false, 1, 255);
			//			telefono.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio  + "." + this.nomeAnagrafica + ".telefono.errorMessage"));
			mappaCreazione.put(telefonoId, telefono);

			// cellulare
			String cellulareLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica +  ".cellulare.label");
			InputText cellulare = new InputText(cellulareId, cellulareLabel, null, false, true, false, 1, 255);
			//	cellulare.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio  + "." + this.nomeAnagrafica + ".cellulare.errorMessage"));
			mappaCreazione.put(cellulareId, cellulare);

			// fax
			String faxLabel = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica +  ".fax.label");
			InputText fax = new InputText(faxId, faxLabel, null, false, true, false, 1, 255);
			//	fax.setValidation(null, Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio  + "." + this.nomeAnagrafica + ".fax.errorMessage"));
			mappaCreazione.put(faxId, fax);

			infoCreazioneMap.put(this.nomeServizio + "." + this.nomeAnagrafica, mappaCreazione);

		}

		List<ParamField<?>> listaParametri = new ArrayList<ParamField<?>>();

		// lista field
		InputText ragioneSociale = (InputText) mappaCreazione.get(ragioneSocialeId);
		ragioneSociale.setDefaultValue(null);
		listaParametri.add(ragioneSociale);

		// Codunivoco
		InputText codUnivoco = (InputText) mappaCreazione.get(codUnivocoId);
		codUnivoco.setDefaultValue(null);
		listaParametri.add(codUnivoco);

		//indirizzo
		InputText indirizzo = (InputText) mappaCreazione.get(indirizzoId);
		indirizzo.setDefaultValue(null);
		listaParametri.add(indirizzo);

		//civico
		InputText civico = (InputText) mappaCreazione.get(civicoId);
		civico.setDefaultValue(null);
		listaParametri.add(civico);

		// cap
		InputText cap = (InputText) mappaCreazione.get(capId);
		cap.setDefaultValue(null);
		listaParametri.add(cap);

		// localita
		InputText localita = (InputText) mappaCreazione.get(localitaId);
		localita.setDefaultValue(null);
		listaParametri.add(localita);

		// provincia
		InputText provincia = (InputText) mappaCreazione.get(provinciaId);
		provincia.setDefaultValue(null);
		listaParametri.add(provincia);

		// nazione 
		InputText nazione = (InputText) mappaCreazione.get(nazioneId);
		nazione.setDefaultValue(null);
		listaParametri.add(nazione);

		// email
		InputText email = (InputText) mappaCreazione.get(emailId);
		email.setDefaultValue(null);
		listaParametri.add(email);

		// telefono
		InputText telefono = (InputText) mappaCreazione.get(telefonoId);
		telefono.setDefaultValue(null);
		listaParametri.add(telefono);

		// cellulare
		InputText cellulare = (InputText) mappaCreazione.get(cellulareId);
		cellulare.setDefaultValue(null);
		listaParametri.add(cellulare);

		// fax
		InputText fax = (InputText) mappaCreazione.get(faxId);
		fax.setDefaultValue(null);
		listaParametri.add(fax);



		return listaParametri;
	}

	public List<ParamField<?>> getInfoModificaAnagraficaUO(UriInfo uriInfo, BasicBD bd, Anagrafica anagrafica) throws ConsoleException {

		List<ParamField<?>> listaParametri = new ArrayList<ParamField<?>>();

		String ragioneSocialeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".ragioneSociale.id");
		String codUnivocoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".codUnivoco.id");
		String indirizzoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".indirizzo.id");
		String civicoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".civico.id");
		String capId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".cap.id");
		String localitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".localita.id");
		String provinciaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".provincia.id");
		String nazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".nazione.id");
		String emailId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".email.id");
		String telefonoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".telefono.id");
		String cellulareId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".cellulare.id");
		String faxId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".fax.id");

		Map<String, ParamField<?>> mappaCreazione = infoCreazioneMap.get(this.nomeServizio + "." + this.nomeAnagrafica);

		if(mappaCreazione == null)
			this.getInfoCreazioneAnagraficaUO(uriInfo, bd);

		mappaCreazione = infoCreazioneMap.get(this.nomeServizio + "." + this.nomeAnagrafica);

		// prelevo i componenti e gli setto i valori correnti
		//Ragione Sociale
		InputText ragioneSociale = (InputText) mappaCreazione.get(ragioneSocialeId);
		ragioneSociale.setDefaultValue(anagrafica.getRagioneSociale());
		listaParametri.add(ragioneSociale);

		// Codunivoco
		InputText codUnivoco = (InputText) mappaCreazione.get(codUnivocoId);
		codUnivoco.setDefaultValue(anagrafica.getCodUnivoco());
		listaParametri.add(codUnivoco);

		//indirizzo
		InputText indirizzo = (InputText) mappaCreazione.get(indirizzoId);
		indirizzo.setDefaultValue(anagrafica.getIndirizzo());
		listaParametri.add(indirizzo);

		//civico
		InputText civico = (InputText) mappaCreazione.get(civicoId);
		civico.setDefaultValue(anagrafica.getCivico());
		listaParametri.add(civico);

		// cap
		InputText cap = (InputText) mappaCreazione.get(capId);
		cap.setDefaultValue(anagrafica.getCap());
		listaParametri.add(cap);

		// localita
		InputText localita = (InputText) mappaCreazione.get(localitaId);
		localita.setDefaultValue(anagrafica.getLocalita());
		listaParametri.add(localita);

		// provincia
		InputText provincia = (InputText) mappaCreazione.get(provinciaId);
		provincia.setDefaultValue(anagrafica.getProvincia());
		listaParametri.add(provincia);

		// nazione 
		InputText nazione = (InputText) mappaCreazione.get(nazioneId);
		nazione.setDefaultValue(anagrafica.getNazione());
		listaParametri.add(nazione);

		// email
		InputText email = (InputText) mappaCreazione.get(emailId);
		email.setDefaultValue(anagrafica.getEmail());
		listaParametri.add(email);

		// telefono
		InputText telefono = (InputText) mappaCreazione.get(telefonoId);
		telefono.setDefaultValue(anagrafica.getTelefono());
		listaParametri.add(telefono);

		// cellulare
		InputText cellulare = (InputText) mappaCreazione.get(cellulareId);
		cellulare.setDefaultValue(anagrafica.getCellulare());
		listaParametri.add(cellulare);

		// fax
		InputText fax = (InputText) mappaCreazione.get(faxId);
		fax.setDefaultValue(anagrafica.getFax());
		listaParametri.add(fax);


		return listaParametri;
	}

	public List<ParamField<?>> getInfoModificaAnagraficaDominio(UriInfo uriInfo, BasicBD bd, Anagrafica anagrafica, String ragioneSocialeValue) throws ConsoleException {

		List<ParamField<?>> listaParametri = new ArrayList<ParamField<?>>();

		String ragioneSocialeId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".ragioneSociale.id");
		String codUnivocoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".codUnivoco.id");
		String indirizzoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".indirizzo.id");
		String civicoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".civico.id");
		String capId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".cap.id");
		String localitaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".localita.id");
		String provinciaId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".provincia.id");
		String nazioneId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".nazione.id");
		String emailId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".email.id");
		String telefonoId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".telefono.id");
		String cellulareId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".cellulare.id");
		String faxId = Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".fax.id");


		Map<String, ParamField<?>> mappaCreazione = infoCreazioneMap.get(this.nomeServizio + "." + this.nomeAnagrafica);

		if(mappaCreazione == null)
			this.getInfoCreazioneAnagraficaDominio(uriInfo, bd);

		mappaCreazione = infoCreazioneMap.get(this.nomeServizio + "." + this.nomeAnagrafica);

		// prelevo i componenti e gli setto i valori correnti
		//Ragione Sociale
		InputText ragioneSociale = (InputText) mappaCreazione.get(ragioneSocialeId);
		if(ragioneSocialeValue == null)
			ragioneSociale.setDefaultValue(anagrafica.getRagioneSociale());
		else 
			ragioneSociale.setDefaultValue(ragioneSocialeValue);   
		
		listaParametri.add(ragioneSociale);

		// Codunivoco
		InputText codUnivoco = (InputText) mappaCreazione.get(codUnivocoId);
		codUnivoco.setDefaultValue(anagrafica.getCodUnivoco());
		listaParametri.add(codUnivoco);

		//indirizzo
		InputText indirizzo = (InputText) mappaCreazione.get(indirizzoId);
		indirizzo.setDefaultValue(anagrafica.getIndirizzo());
		listaParametri.add(indirizzo);

		//civico
		InputText civico = (InputText) mappaCreazione.get(civicoId);
		civico.setDefaultValue(anagrafica.getCivico());
		listaParametri.add(civico);

		// cap
		InputText cap = (InputText) mappaCreazione.get(capId);
		cap.setDefaultValue(anagrafica.getCap());
		listaParametri.add(cap);

		// localita
		InputText localita = (InputText) mappaCreazione.get(localitaId);
		localita.setDefaultValue(anagrafica.getLocalita());
		listaParametri.add(localita);

		// provincia
		InputText provincia = (InputText) mappaCreazione.get(provinciaId);
		provincia.setDefaultValue(anagrafica.getProvincia());
		listaParametri.add(provincia);

		// nazione 
		InputText nazione = (InputText) mappaCreazione.get(nazioneId);
		nazione.setDefaultValue(anagrafica.getNazione());
		listaParametri.add(nazione);

		// email
		InputText email = (InputText) mappaCreazione.get(emailId);
		email.setDefaultValue(anagrafica.getEmail());
		listaParametri.add(email);

		// telefono
		InputText telefono = (InputText) mappaCreazione.get(telefonoId);
		telefono.setDefaultValue(anagrafica.getTelefono());
		listaParametri.add(telefono);

		// cellulare
		InputText cellulare = (InputText) mappaCreazione.get(cellulareId);
		cellulare.setDefaultValue(anagrafica.getCellulare());
		listaParametri.add(cellulare);

		// fax
		InputText fax = (InputText) mappaCreazione.get(faxId);
		fax.setDefaultValue(anagrafica.getFax());
		listaParametri.add(fax);

		return listaParametri;
	}

	public URI getUriField(UriInfo uriInfo, BasicBD bd, String fieldName) throws ConsoleException {
		try{
			URI uri = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path(BaseDarsService.PATH_FIELD).path(fieldName).build(); 
			return uri;
		}catch(Exception e){
			throw new ConsoleException(e);
		} 
	}

	public void fillSezioneAnagraficaUO(it.govpay.web.rs.dars.model.Sezione sezioneAnagrafica, Anagrafica anagrafica) {
		if(StringUtils.isNotEmpty(anagrafica.getCodUnivoco()))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".codUnivoco.label"), anagrafica.getCodUnivoco());
		if(StringUtils.isNotEmpty(anagrafica.getRagioneSociale()))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".ragioneSociale.label"), anagrafica.getRagioneSociale());
		if(StringUtils.isNotEmpty(anagrafica.getIndirizzo()))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".indirizzo.label"), anagrafica.getIndirizzo());
		if(StringUtils.isNotEmpty(anagrafica.getCivico()))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".civico.label"), anagrafica.getCivico());
		if(StringUtils.isNotEmpty(anagrafica.getCap()))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".cap.label"), anagrafica.getCap());
		if(StringUtils.isNotEmpty(anagrafica.getLocalita()))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".localita.label"), anagrafica.getLocalita());
		if(StringUtils.isNotEmpty(anagrafica.getProvincia()))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".provincia.label"), anagrafica.getProvincia());
		if(StringUtils.isNotEmpty(anagrafica.getNazione()))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".nazione.label"), anagrafica.getNazione());
		if(StringUtils.isNotEmpty(anagrafica.getEmail()))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".email.label"), anagrafica.getEmail());
		if(StringUtils.isNotEmpty(anagrafica.getTelefono()))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".telefono.label"), anagrafica.getTelefono());
		if(StringUtils.isNotEmpty(anagrafica.getCellulare()))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".cellulare.label"), anagrafica.getCellulare());
		if(StringUtils.isNotEmpty(anagrafica.getFax()))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".fax.label"), anagrafica.getFax());
	}

	public void fillSezioneAnagraficaDominio(it.govpay.web.rs.dars.model.Sezione sezioneAnagrafica, Anagrafica anagrafica, String ragioneSocialeValue) {
		if(StringUtils.isNotEmpty(ragioneSocialeValue))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".ragioneSociale.label"), ragioneSocialeValue);
		if(StringUtils.isNotEmpty(anagrafica.getIndirizzo()))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".indirizzo.label"), anagrafica.getIndirizzo());
		if(StringUtils.isNotEmpty(anagrafica.getCivico()))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".civico.label"), anagrafica.getCivico());
		if(StringUtils.isNotEmpty(anagrafica.getCap()))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".cap.label"), anagrafica.getCap());
		if(StringUtils.isNotEmpty(anagrafica.getLocalita()))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".localita.label"), anagrafica.getLocalita());
		if(StringUtils.isNotEmpty(anagrafica.getProvincia()))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".provincia.label"), anagrafica.getProvincia());
		if(StringUtils.isNotEmpty(anagrafica.getNazione()))
			sezioneAnagrafica.addVoce(Utils.getInstance().getMessageFromResourceBundle(this.nomeServizio + "." + this.nomeAnagrafica + ".nazione.label"), anagrafica.getNazione());
	}
}
