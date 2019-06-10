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
package it.govpay.core.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.Versamento;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.business.Iuv;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoNonValidoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.client.VerificaClient;
import it.govpay.model.Anagrafica.TIPO;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.SingoloVersamento.TipoBollo;
import it.govpay.model.TipoVersamento;
import it.govpay.model.Tributo;
import it.govpay.model.Tributo.TipoContabilita;
import it.govpay.model.Versamento.AvvisaturaOperazione;
import it.govpay.model.Versamento.CausaleSemplice;
import it.govpay.model.Versamento.ModoAvvisatura;
import it.govpay.model.Versamento.StatoVersamento;

public class VersamentoUtils {

	public final static QName _VersamentoKeyCodApplicazione_QNAME = new QName("", "codApplicazione");
	public final static QName _VersamentoKeyCodVersamentoEnte_QNAME = new QName("", "codVersamentoEnte");
	public final static QName _VersamentoKeyCodDominio_QNAME = new QName("", "codDominio");
	public final static QName _VersamentoKeyCodUnivocoDebitore_QNAME = new QName("", "codUnivocoDebitore");
	public final static QName _VersamentoKeyBundlekey_QNAME = new QName("", "bundlekey");
	public final static QName _VersamentoKeyIuv_QNAME = new QName("", "iuv");

	public static void validazioneSemantica(Versamento versamento, boolean generaIuv, BasicBD bd) throws GovPayException, ServiceException {
		if(generaIuv && versamento.getSingoliVersamenti(bd).size() != 1) {
			throw new GovPayException(EsitoOperazione.VER_000, versamento.getApplicazione(bd).getCodApplicazione(), versamento.getCodVersamentoEnte());
		}

		BigDecimal somma = BigDecimal.ZERO;
		List<String> codSingoliVersamenti = new ArrayList<>();
		for(SingoloVersamento sv : versamento.getSingoliVersamenti(bd)) {
			if(codSingoliVersamenti.contains(sv.getCodSingoloVersamentoEnte()))
				throw new GovPayException(EsitoOperazione.VER_001, versamento.getApplicazione(bd).getCodApplicazione(), versamento.getCodVersamentoEnte(), sv.getCodSingoloVersamentoEnte());

			codSingoliVersamenti.add(sv.getCodSingoloVersamentoEnte());
			somma = somma.add(sv.getImportoSingoloVersamento());
		}

		if(somma.compareTo(versamento.getImportoTotale()) != 0) {
			throw new GovPayException(EsitoOperazione.VER_002, versamento.getApplicazione(bd).getCodApplicazione(), versamento.getCodVersamentoEnte(), Double.toString(somma.doubleValue()), Double.toString(versamento.getImportoTotale().doubleValue()));
		}
	}

	public static void validazioneSemanticaAggiornamento(Versamento versamentoLetto, Versamento versamentoNuovo, BasicBD bd) throws GovPayException, ServiceException {

		if(!versamentoLetto.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO) && !versamentoLetto.getStatoVersamento().equals(StatoVersamento.ANNULLATO)) {
			throw new GovPayException(EsitoOperazione.VER_003, versamentoNuovo.getApplicazione(bd).getCodApplicazione(), versamentoLetto.getCodVersamentoEnte(), versamentoLetto.getStatoVersamento().toString());
		}
		if(versamentoLetto.getIdUo() != versamentoNuovo.getIdUo()) {
			throw new GovPayException(EsitoOperazione.VER_004, versamentoNuovo.getApplicazione(bd).getCodApplicazione(), versamentoLetto.getCodVersamentoEnte(), versamentoLetto.getUo(bd).getCodUo(), versamentoNuovo.getUo(bd).getCodUo());
		}
		versamentoNuovo.setId(versamentoLetto.getId());
		versamentoNuovo.setDataCreazione(versamentoLetto.getDataCreazione());

		// aggiornamento del numero avviso se il numero avviso non era stato salvato viene aggiornato automaticamente
		// se il numero avviso e' diverso da quello che e' stato salvato in origine devo segnalare eccezione
		if(versamentoLetto.getNumeroAvviso() != null && versamentoNuovo.getNumeroAvviso() !=null
				&& !versamentoLetto.getNumeroAvviso().equals(versamentoNuovo.getNumeroAvviso())) {
			throw new GovPayException(EsitoOperazione.VER_024, versamentoNuovo.getApplicazione(bd).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), versamentoNuovo.getNumeroAvviso(), versamentoLetto.getNumeroAvviso());
		}

		List<SingoloVersamento> singoliversamentiLetti = versamentoLetto.getSingoliVersamenti(bd);
		List<SingoloVersamento> singoliVersamentiNuovi = versamentoNuovo.getSingoliVersamenti(bd);

		if(singoliversamentiLetti.size() != singoliVersamentiNuovi.size()) {
			throw new GovPayException(EsitoOperazione.VER_005, versamentoNuovo.getApplicazione(bd).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), Integer.toString(singoliversamentiLetti.size()), Integer.toString(singoliVersamentiNuovi.size()));
		}
		Collections.sort(singoliversamentiLetti);
		Collections.sort(singoliVersamentiNuovi);

		for(int i=0; i<singoliversamentiLetti.size(); i++) {
			SingoloVersamento letto = singoliversamentiLetti.get(i);
			SingoloVersamento nuovo = singoliVersamentiNuovi.get(i);

			if(!letto.getCodSingoloVersamentoEnte().equals(nuovo.getCodSingoloVersamentoEnte())) {
				throw new GovPayException(EsitoOperazione.VER_006, versamentoNuovo.getApplicazione(bd).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), letto.getCodSingoloVersamentoEnte());
			}

			if(letto.getIdTributo() != null || nuovo.getIdTributo() != null) { // se sono entrambi null OK
				if(letto.getIdTributo() == null && nuovo.getIdTributo() != null) { // se uno e' null e uno no Eccezione
					throw new GovPayException(EsitoOperazione.VER_007, versamentoNuovo.getApplicazione(bd).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), letto.getCodSingoloVersamentoEnte(), "null", Long.toString(nuovo.getIdTributo()));
				}

				if(letto.getIdTributo() != null && nuovo.getIdTributo() == null) { // se uno e' null e uno no Eccezione
					throw new GovPayException(EsitoOperazione.VER_007, versamentoNuovo.getApplicazione(bd).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), letto.getCodSingoloVersamentoEnte(), Long.toString(letto.getIdTributo()), "null");
				}

				if(letto.getIdTributo().longValue() != nuovo.getIdTributo().longValue()) { // se sono tutti e due non null controllo l'uguaglianza dei long value
					throw new GovPayException(EsitoOperazione.VER_007, versamentoNuovo.getApplicazione(bd).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), letto.getCodSingoloVersamentoEnte(), Long.toString(letto.getIdTributo()), Long.toString(nuovo.getIdTributo()));
				}
			}

			if(!(letto.getIbanAccredito(bd) == null && nuovo.getIbanAccredito(bd) == null)) {
				if(letto.getIbanAccredito(bd) == null || nuovo.getIbanAccredito(bd).getId() == null || !letto.getIbanAccredito(bd).getId().equals(nuovo.getIbanAccredito(bd).getId())) {
					throw new GovPayException(EsitoOperazione.VER_023, versamentoNuovo.getApplicazione(bd).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), letto.getCodSingoloVersamentoEnte());
				}
			}
			nuovo.setId(letto.getId());
			nuovo.setIdVersamento(letto.getIdVersamento());
		}
	}

	public static Versamento aggiornaVersamento(Versamento versamento, BasicBD bd) throws VersamentoScadutoException, VersamentoAnnullatoException, VersamentoDuplicatoException, 
			VersamentoSconosciutoException, ServiceException, ClientException, GovPayException, UtilsException, VersamentoNonValidoException {
		// Se il versamento non e' in attesa, non aggiorno un bel niente
		if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO))
			return versamento;

		// Controllo se la data di scadenza e' indicata ed e' decorsa
		if(versamento.getDataScadenza() != null && DateUtils.isDataDecorsa(versamento.getDataScadenza())) {
			throw new VersamentoScadutoException(versamento.getApplicazione(bd).getCodApplicazione(), versamento.getCodVersamentoEnte(), versamento.getDataScadenza());
		}else {
			if(versamento.getDataValidita() != null && DateUtils.isDataDecorsa(versamento.getDataValidita())) {
				IContext ctx = ContextThreadLocal.get();
				String codVersamentoEnte = versamento.getCodVersamentoEnte();
				String bundlekey = versamento.getCodBundlekey();
				String debitore = versamento.getAnagraficaDebitore().getCodUnivoco();
				String codDominio = versamento.getUo(bd).getDominio(bd).getCodDominio(); 
				String iuv = null;

				String codVersamentoEnteD = codVersamentoEnte != null ? codVersamentoEnte : "-";
				String bundlekeyD = bundlekey != null ? bundlekey : "-";
				String debitoreD = debitore != null ? debitore : "-";
				String dominioD = codDominio != null ? codDominio : "-";
				String iuvD = iuv != null ? iuv : "-";
				ctx.getApplicationLogger().log("verifica.validita", versamento.getApplicazione(bd).getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);

				if(versamento.getApplicazione(bd).getConnettoreIntegrazione() != null) {
					versamento = acquisisciVersamento(versamento.getApplicazione(bd), codVersamentoEnte, bundlekey, debitore, codDominio, iuv, bd);
				} else // connettore verifica non definito, versamento non aggiornabile
					throw new VersamentoScadutoException(versamento.getApplicazione(bd).getCodApplicazione(), codVersamentoEnte, versamento.getDataScadenza());
			} else {
				// versamento valido
			} 
		}
		return versamento;
	}


	public static Versamento acquisisciVersamento(Applicazione applicazione, String codVersamentoEnte, String bundlekey, String debitore, String dominio, String iuv, BasicBD bd) 
			throws VersamentoScadutoException, VersamentoAnnullatoException, VersamentoDuplicatoException, VersamentoSconosciutoException, ServiceException, ClientException, GovPayException, UtilsException, VersamentoNonValidoException {

		String codVersamentoEnteD = codVersamentoEnte != null ? codVersamentoEnte : "-";
		String bundlekeyD = bundlekey != null ? bundlekey : "-";
		String debitoreD = debitore != null ? debitore : "-";
		String dominioD = dominio != null ? dominio : "-";
		String iuvD = iuv != null ? iuv : "-";

		IContext ctx = ContextThreadLocal.get();
		ctx.getApplicationLogger().log("verifica.avvio", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
		if(applicazione.getConnettoreIntegrazione() == null) {
			ctx.getApplicationLogger().log("verifica.nonConfigurata");
			throw new VersamentoSconosciutoException();
		}
		VerificaClient verificaClient = new VerificaClient(applicazione,bd);
		// salvataggio id Rpt/ versamento/ pagamento
		verificaClient.getEventoCtx().setCodDominio(dominio); 
		verificaClient.getEventoCtx().setIuv(iuv);
		verificaClient.getEventoCtx().setIdA2A(applicazione.getCodApplicazione());
		verificaClient.getEventoCtx().setIdPendenza(codVersamentoEnte);
		Versamento versamento = null;
		try {
			try {
				versamento = verificaClient.invoke(codVersamentoEnte, bundlekey, debitore, dominio, iuv, bd);
				ctx.getApplicationLogger().log("verifica.Ok", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
				
				verificaClient.getEventoCtx().setEsito(Esito.OK);
			} catch (ClientException e){
				ctx.getApplicationLogger().log("verifica.Fail", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD, e.getMessage());
				verificaClient.getEventoCtx().setSottotipoEsito(((ClientException)e).getResponseCode() + "");
				verificaClient.getEventoCtx().setEsito(Esito.FAIL);
				verificaClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				throw e;
			} catch (VersamentoScadutoException e) {
				ctx.getApplicationLogger().log("verifica.Scaduto", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
				verificaClient.getEventoCtx().setSottotipoEsito("Pendenza Scaduta");
				verificaClient.getEventoCtx().setEsito(Esito.KO);
				verificaClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				throw e;
			} catch (VersamentoAnnullatoException e) {
				ctx.getApplicationLogger().log("verifica.Annullato", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
				verificaClient.getEventoCtx().setSottotipoEsito("Pendenza Annullata");
				verificaClient.getEventoCtx().setEsito(Esito.KO);
				verificaClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				throw e;
			} catch (VersamentoDuplicatoException e) {
				ctx.getApplicationLogger().log("verifica.Duplicato", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
				verificaClient.getEventoCtx().setSottotipoEsito("Pendenza Duplicata");
				verificaClient.getEventoCtx().setEsito(Esito.KO);
				verificaClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				throw e;
			} catch (VersamentoSconosciutoException e) {
				ctx.getApplicationLogger().log("verifica.Sconosciuto", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
				verificaClient.getEventoCtx().setSottotipoEsito("Pendenza Sconosciuta");
				verificaClient.getEventoCtx().setEsito(Esito.KO);
				verificaClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				throw e;
			} catch (VersamentoNonValidoException e) {
				ctx.getApplicationLogger().log("verifica.Fail", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD, e.getMessage());
				verificaClient.getEventoCtx().setSottotipoEsito("Pendenza non Valida");
				verificaClient.getEventoCtx().setEsito(Esito.KO);
				verificaClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				throw e;
			} 

			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			boolean generaIuv = versamento.getNumeroAvviso() == null && versamento.getSingoliVersamenti(bd).size() == 1;
			versamentoBusiness.caricaVersamento(versamento, generaIuv, true);
		}finally {
			EventoContext eventoCtx = verificaClient.getEventoCtx();
			
			if(eventoCtx.isRegistraEvento()) {
				// log evento
				GiornaleEventi giornaleEventi = new GiornaleEventi(bd);
				giornaleEventi.registraEvento(eventoCtx.toEventoDTO());
			}
		}
		return versamento;
	}


	public static String getIuvFromNumeroAvviso(String numeroAvviso,String codDominio, String codStazione, Integer applicationCode, Integer segregationCode) throws GovPayException {
		if(numeroAvviso == null)
			return null;

		if(numeroAvviso.length() != 18)
			throw new GovPayException(EsitoOperazione.VER_017, numeroAvviso);

		try {
			Long.parseLong(numeroAvviso);
		}catch(Exception e) {
			throw new GovPayException(EsitoOperazione.VER_017, numeroAvviso);
		}


		if(numeroAvviso.startsWith("0")) { // '0' + applicationCode(2) + ref(13) + check(2)
			// controllo che l'application code coincida con quello previsto
			String appCodeS = numeroAvviso.substring(1, 3);
			Integer appCode = Integer.parseInt(appCodeS);

			if(applicationCode != null && applicationCode.intValue() != appCode.intValue())
				throw new GovPayException(EsitoOperazione.VER_026, numeroAvviso, appCodeS, codStazione);

			return numeroAvviso.substring(3);
		}else if(numeroAvviso.startsWith("1")) // '1' + reference(17)
			return numeroAvviso.substring(1);
		else if(numeroAvviso.startsWith("2")) // '2' + ref(15) + check(2)
			return numeroAvviso.substring(1);
		else if(numeroAvviso.startsWith("3")) { // '3' + segregationCode(2) +  ref(13) + check(2)
			// controllo che segregationCode coincida con quello previsto
			String segCodeS = numeroAvviso.substring(1, 3);
			Integer segCode = Integer.parseInt(segCodeS);

			if(segregationCode != null && segregationCode.intValue() != segCode.intValue())
				throw new GovPayException(EsitoOperazione.VER_027, numeroAvviso, segCodeS, codDominio);

			return numeroAvviso.substring(1);
		}else 
			throw new GovPayException(EsitoOperazione.VER_017, numeroAvviso);
		//		return numeroAvviso;
	}

	public static Versamento toVersamentoModel(it.govpay.core.dao.commons.Versamento versamento, BasicBD bd) throws ServiceException, GovPayException, ValidationException { 
		Versamento model = new Versamento();
		model.setAggiornabile(versamento.isAggiornabile() == null ? true : versamento.isAggiornabile());
		model.setAnagraficaDebitore(toAnagraficaModel(versamento.getDebitore()));

		CausaleSemplice causale = model.new CausaleSemplice();
		causale.setCausale(versamento.getCausale());
		model.setCausaleVersamento(causale);
		model.setDatiAllegati(versamento.getDatiAllegati()); 
		model.setCodAnnoTributario(versamento.getAnnoTributario());
		model.setCodBundlekey(versamento.getBundlekey());
		model.setCodLotto(versamento.getCodDebito()); 
		model.setCodVersamentoEnte(versamento.getCodVersamentoEnte());
		model.setDataCreazione(versamento.getDataCaricamento());
		model.setDataScadenza(versamento.getDataScadenza());
		model.setDataValidita(versamento.getDataValidita());
		model.setDataUltimoAggiornamento(new Date());
		model.setDescrizioneStato(null);
		model.setNome(versamento.getNome());

		if(versamento.getAvvisaturaAbilitata() != null) {
			model.setAvvisaturaAbilitata(versamento.getAvvisaturaAbilitata().booleanValue());
		} else {
			// imposto il default di sistema
			model.setAvvisaturaAbilitata(GovpayConfig.getInstance().isAvvisaturaDigitaleEnabled());
		}

		if(versamento.getModoAvvisatura() != null) {
			model.setAvvisaturaModalita(versamento.getModoAvvisatura());
		} else {
			// imposto il default
			model.setAvvisaturaModalita(ModoAvvisatura.ASICNRONA.getValue()); 
		}

		model.setId(null);
		try {
			model.setApplicazione(versamento.getCodApplicazione(), bd);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.APP_000, versamento.getCodApplicazione());
		}

		if(!model.getApplicazione(bd).getUtenza().isAbilitato())
			throw new GovPayException(EsitoOperazione.APP_001, versamento.getCodApplicazione());

		Applicazione applicazione = model.getApplicazione(bd);

		Dominio dominio = null;
		try {
			dominio = AnagraficaManager.getDominio(bd, versamento.getCodDominio());
			model.setIdDominio(dominio.getId()); 
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.DOM_000, versamento.getCodDominio());
		}

		if(!dominio.isAbilitato())
			throw new GovPayException(EsitoOperazione.DOM_001, dominio.getCodDominio());

		try {
			String codUnitaOperativa = (versamento.getCodUnitaOperativa() == null) ? it.govpay.model.Dominio.EC : versamento.getCodUnitaOperativa();
			model.setUo(dominio.getId(), codUnitaOperativa, bd);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.UOP_000, versamento.getCodUnitaOperativa(), versamento.getCodDominio());
		}

		if(!model.getUo(bd).isAbilitato())
			throw new GovPayException(EsitoOperazione.UOP_001, versamento.getCodUnitaOperativa(), versamento.getCodDominio());

		model.setImportoTotale(versamento.getImportoTotale());
		model.setStatoVersamento(StatoVersamento.NON_ESEGUITO);

		// in un versamento multivoce non si puo' passare il numero avviso
		if(versamento.getSingoloVersamento().size() > 1 && StringUtils.isNotEmpty(versamento.getNumeroAvviso())) {
			throw new GovPayException(EsitoOperazione.VER_031);
		}

		int index = 1;
		for(it.govpay.core.dao.commons.Versamento.SingoloVersamento singoloVersamento : versamento.getSingoloVersamento()) {
			model.addSingoloVersamento(toSingoloVersamentoModel(model, singoloVersamento, index++ ,bd));
		}

		model.setTassonomia(versamento.getTassonomia());
		model.setTassonomiaAvviso(versamento.getTassonomiaAvviso());

		model.setIncasso(versamento.getIncasso());
		model.setAnomalie(versamento.getAnomalie()); 
		model.setNumeroAvviso(versamento.getNumeroAvviso());	

		if(versamento.getNumeroAvviso() != null) {
			String iuvFromNumeroAvviso = it.govpay.core.utils.VersamentoUtils.getIuvFromNumeroAvviso(versamento.getNumeroAvviso(),dominio.getCodDominio(),dominio.getStazione().getCodStazione(),
					dominio.getStazione().getApplicationCode(), dominio.getSegregationCode());

			// check sulla validita' dello iuv
			Iuv iuvBD  = new Iuv(bd);
			TipoIUV tipo = iuvBD.getTipoIUV(iuvFromNumeroAvviso);
			try {
				iuvBD.checkIUV(dominio, iuvFromNumeroAvviso, tipo );
			}catch(UtilsException e) {
				throw new GovPayException(e);
			}

			model.setIuvVersamento(iuvFromNumeroAvviso);
			model.setIuvProposto(iuvFromNumeroAvviso); 

			//			if(versamento.getIuv().startsWith("0")) {
			//				model.setIuvVersamento(versamento.getIuv().substring(1));
			//			} else {
			//				model.setIuvVersamento(versamento.getIuv().substring(3));
			//			}
			//			model.setNumeroAvviso(versamento.getIuv());
			model.setAvvisaturaOperazione(AvvisaturaOperazione.CREATE.getValue());
			model.setAvvisaturaDaInviare(true);
		}

		// tipo Pendenza
		TipoVersamento tipoVersamento = null;
		try {
			tipoVersamento = AnagraficaManager.getTipoVersamento(bd, versamento.getCodTipoVersamento());
		} catch (NotFoundException e) {
			try {
				tipoVersamento = AnagraficaManager.getTipoVersamento(bd, GovpayConfig.getInstance().getCodTipoVersamentoPendenzeNonCensite());
			} catch (NotFoundException e1) {
				throw new ServiceException("Non e' stato censito un tipo pendenza di default valido");
			}
		}
		model.setIdTipoVersamento(tipoVersamento.getId()); 

		if(!applicazione.isTrusted() && !AuthorizationManager.isTipoVersamentoAuthorized(applicazione.getUtenza(), tipoVersamento.getCodTipoVersamento())) {
			throw new GovPayException(EsitoOperazione.VER_022, tipoVersamento.getCodTipoVersamento());
		}

		// tipo pendenza dominio
		TipoVersamentoDominio tipoVersamentoDominio= null;

		try {
			tipoVersamentoDominio = AnagraficaManager.getTipoVersamentoDominio(bd, dominio.getId(), tipoVersamento.getCodTipoVersamento());
		} catch (NotFoundException e) {
			try {
				tipoVersamentoDominio = AnagraficaManager.getTipoVersamentoDominio(bd, dominio.getId(), GovpayConfig.getInstance().getCodTipoVersamentoPendenzeNonCensite());
			} catch (NotFoundException e1) {
				throw new ServiceException("Non e' stato censito un tipo pendenza di default valido");
			}
		}
		model.setIdTipoVersamentoDominio(tipoVersamentoDominio.getId());

		return model;
	}


	public static SingoloVersamento toSingoloVersamentoModel(Versamento versamento, it.govpay.core.dao.commons.Versamento.SingoloVersamento singoloVersamento, int index, BasicBD bd) throws ServiceException, GovPayException, ValidationException {
		SingoloVersamento model = new SingoloVersamento();
		model.setVersamento(versamento);
		model.setCodSingoloVersamentoEnte(singoloVersamento.getCodSingoloVersamentoEnte());
		model.setIndiceDati(index);
		model.setId(null);
		model.setIdVersamento(0);
		model.setImportoSingoloVersamento(singoloVersamento.getImporto());
		model.setStatoSingoloVersamento(StatoSingoloVersamento.NON_ESEGUITO);
		model.setDescrizione(singoloVersamento.getDescrizione()); 
		model.setDatiAllegati(singoloVersamento.getDatiAllegati()); 
		Dominio dominio = versamento.getUo(bd).getDominio(bd);
		if(singoloVersamento.getBolloTelematico() != null) {
			try {
				model.setTributo(Tributo.BOLLOT, bd);
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.TRB_000, dominio.getCodDominio(), Tributo.BOLLOT);
			}

			if(model.getTributo(bd)!= null) {
				if(!model.getTributo(bd).isAbilitato())
					throw new GovPayException(EsitoOperazione.TRB_001, dominio.getCodDominio(), Tributo.BOLLOT);
			}

			model.setHashDocumento(singoloVersamento.getBolloTelematico().getHash());
			model.setProvinciaResidenza(singoloVersamento.getBolloTelematico().getProvincia());
			try {
				model.setTipoBollo(TipoBollo.toEnum(singoloVersamento.getBolloTelematico().getTipo()));
			} catch (ServiceException e) {
				throw new ValidationException(e.getMessage());
			}
		} 

		if(singoloVersamento.getCodTributo() != null) {
			try {
				model.setTributo(singoloVersamento.getCodTributo(), bd);
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.TRB_000, dominio.getCodDominio(), singoloVersamento.getCodTributo());
			}

			if(model.getTributo(bd)!= null) {
				if(!model.getTributo(bd).isAbilitato())
					throw new GovPayException(EsitoOperazione.TRB_001, dominio.getCodDominio(), singoloVersamento.getCodTributo());
			}

		}

		if(singoloVersamento.getTributo() != null) {

			//			if(!applicazione.isTrusted())
			//				throw new GovPayException(EsitoOperazione.VER_019);
			//			
			//			if(!AuthorizationManager.isAuthorized(applicazione.getUtenza(), applicazione.getUtenza().getTipoUtenza(), Servizio.PAGAMENTI_E_PENDENZE, dominio.getCodDominio(), null, diritti))
			//				throw new GovPayException(EsitoOperazione.VER_021);
			// TODO test sull'autodeterminazione

			try {
				model.setIbanAccredito(AnagraficaManager.getIbanAccredito(bd, dominio.getId(), singoloVersamento.getTributo().getIbanAccredito()));
				if(singoloVersamento.getTributo().getIbanAppoggio() != null)
					model.setIbanAppoggio(AnagraficaManager.getIbanAccredito(bd, dominio.getId(), singoloVersamento.getTributo().getIbanAppoggio()));
				model.setTipoContabilita(TipoContabilita.valueOf(singoloVersamento.getTributo().getTipoContabilita().toString()));
				model.setCodContabilita(singoloVersamento.getTributo().getCodContabilita());

				if(!model.getIbanAccredito(bd).isAbilitato())
					throw new GovPayException(EsitoOperazione.VER_032, dominio.getCodDominio(), singoloVersamento.getTributo().getIbanAccredito());
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.VER_020, dominio.getCodDominio(), singoloVersamento.getTributo().getIbanAccredito());
			}
		}

		return model;
	}

	public static it.govpay.model.Anagrafica toAnagraficaModel(it.govpay.core.dao.commons.Anagrafica anagrafica) {
		if(anagrafica == null) return null;
		it.govpay.model.Anagrafica anagraficaModel = new it.govpay.model.Anagrafica();

		anagraficaModel.setCap(anagrafica.getCap());
		anagraficaModel.setCellulare(anagrafica.getCellulare());
		anagraficaModel.setCivico(anagrafica.getCivico());
		anagraficaModel.setCodUnivoco(anagrafica.getCodUnivoco());
		anagraficaModel.setEmail(anagrafica.getEmail());
		anagraficaModel.setFax(anagrafica.getFax());
		anagraficaModel.setIndirizzo(anagrafica.getIndirizzo());
		anagraficaModel.setLocalita(anagrafica.getLocalita());
		anagraficaModel.setNazione(anagrafica.getNazione());
		anagraficaModel.setProvincia(anagrafica.getProvincia());
		anagraficaModel.setRagioneSociale(anagrafica.getRagioneSociale());
		anagraficaModel.setTelefono(anagrafica.getTelefono());
		anagraficaModel.setTipo(TIPO.valueOf(anagrafica.getTipo()));
		return anagraficaModel;
	}
}
