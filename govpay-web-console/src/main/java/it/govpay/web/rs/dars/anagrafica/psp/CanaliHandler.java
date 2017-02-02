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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Canale;
import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.web.rs.BaseRsService;
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

public class CanaliHandler extends BaseDarsHandler<it.govpay.bd.model.Canale> implements IDarsHandler<it.govpay.bd.model.Canale>{

	public CanaliHandler(Logger log,BaseDarsService darsService) {
		super(log,darsService);
	}

	@Override
	public Elenco getElenco(UriInfo uriInfo,BasicBD bd) throws WebApplicationException,ConsoleException {
		String methodName = "getElenco " + this.titoloServizio;

		try{	
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);			

			String codPspId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("psp.codPsp.id");
			String codPsp = this.getParameter(uriInfo, codPspId, String.class);

			URI esportazione = null;
			URI cancellazione = null;



			it.govpay.bd.anagrafica.PspBD pspBD = new it.govpay.bd.anagrafica.PspBD(bd);
			it.govpay.bd.model.Psp psp = pspBD.getPsp(codPsp);

			long count = psp.getCanalis().size();

			Map<String, String> params = new HashMap<String, String>();
			params.put(codPspId, codPsp);

			Elenco elenco = new Elenco(this.titoloServizio, this.getInfoRicerca(uriInfo, bd,params),
					this.getInfoCreazione(uriInfo, bd),
					count, esportazione, cancellazione); 

			UriBuilder uriDettaglioBuilder = BaseRsService.checkDarsURI(uriInfo).path(this.pathServizio).path("{id}");

			List<it.govpay.bd.model.Canale> findAll = psp.getCanalis();

			if(findAll != null && findAll.size() > 0){
				for (it.govpay.bd.model.Canale entry : findAll) {
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
	public InfoForm getInfoRicerca(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String, String> parameters) throws ConsoleException {
		URI ricerca =  this.getUriRicerca(uriInfo, bd, parameters);
		InfoForm infoRicerca = new InfoForm(ricerca);
		return infoRicerca;
	}

	@Override
	public InfoForm getInfoCreazione(UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		InfoForm infoCreazione = null; 
		return infoCreazione;
	}

	@Override
	public InfoForm getInfoModifica(UriInfo uriInfo, BasicBD bd, it.govpay.bd.model.Canale entry) throws ConsoleException {
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
			this.log.info("Esecuzione " + methodName + " in corso...");
			// Operazione consentita solo all'amministratore
			this.darsService.checkOperatoreAdmin(bd);

			// recupero oggetto
			it.govpay.bd.anagrafica.PspBD pspBD = new it.govpay.bd.anagrafica.PspBD(bd);
			Canale canale = pspBD.getCanale(id);

			InfoForm infoModifica = this.getInfoModifica(uriInfo, bd,canale);
			URI cancellazione = null;
			URI esportazione = null;

			Dettaglio dettaglio = new Dettaglio(this.getTitolo(canale,bd), esportazione, cancellazione, infoModifica);

			it.govpay.web.rs.dars.model.Sezione root = dettaglio.getSezioneRoot();

			// dati del canale
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".codCanale.label"), canale.getCodCanale());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipoVersamento.label"), canale.getTipoVersamento().toString());
			ModelloPagamento modelloPagamento = canale.getModelloPagamento();
			if(modelloPagamento != null){
				String modelloPagamentoString = null;
				switch (modelloPagamento) {
				case ATTIVATO_PRESSO_PSP:
					modelloPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modelloPagamento.ATTIVATO_PRESSO_PSP");
					break;
				case DIFFERITO:
					modelloPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modelloPagamento.DIFFERITO");
					break;
				case IMMEDIATO:
					modelloPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modelloPagamento.IMMEDIATO");
					break;
				case IMMEDIATO_MULTIBENEFICIARIO:
				default:
					modelloPagamentoString = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modelloPagamento.IMMEDIATO_MULTIBENEFICIARIO");
					break;
				}

				root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modelloPagamento.label"),modelloPagamentoString);
			}

			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".disponibilita.label"), canale.getDisponibilita());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".descrizione.label"), canale.getDescrizione());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".condizioni.label"), canale.getCondizioni());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".urlInfo.label"), canale.getUrlInfo());
			root.addVoce(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".abilitato.label"), Utils.getAbilitatoAsLabel(canale.isAbilitato()));

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
		return null;
	}

	@Override
	public Canale creaEntry(InputStream is, UriInfo uriInfo, BasicBD bd)
			throws WebApplicationException, ConsoleException {
		return null;
	}

	@Override
	public void checkEntry(Canale entry, Canale oldEntry) throws ValidationException {
	}

	@Override
	public Dettaglio update(InputStream is, UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException, ValidationException {
		return null;
	}

	@Override
	public void delete(List<Long> idsToDelete, UriInfo uriInfo, BasicBD bd) throws ConsoleException {
		// operazione non prevista
	}

	@Override
	public String getTitolo(it.govpay.bd.model.Canale entry, BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		sb.append("Codice: ").
		append(entry.getCodCanale());
		return sb.toString();
	}

	@Override
	public String getSottotitolo(it.govpay.bd.model.Canale entry, BasicBD bd) {
		StringBuilder sb = new StringBuilder();

		sb.append("Tipo Versamento: ")
		.append(Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".tipoVersamento."+	entry.getTipoVersamento().name()))
		.append(", Modello Pagamento: ").append(
				Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle(this.nomeServizio + ".modelloPagamento."+	entry.getModelloPagamento().name())
				);

		return sb.toString();
	}

	@Override
	public List<String> getValori(Canale entry, BasicBD bd) throws ConsoleException {
		return null;
	}
	
	@Override
	public Map<String, String> getVoci(Canale entry, BasicBD bd) throws ConsoleException { return null; }

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
	public Object uplaod(MultipartFormDataInput input, UriInfo uriInfo, BasicBD bd)	throws WebApplicationException, ConsoleException, ValidationException { return null;}
}