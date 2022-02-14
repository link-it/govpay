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

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.json.IJsonSchemaValidator;
import org.openspcoop2.utils.json.JsonSchemaValidatorConfig;
import org.openspcoop2.utils.json.JsonValidatorAPI.ApiName;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.json.ValidationResponse;
import org.openspcoop2.utils.json.ValidationResponse.ESITO;
import org.openspcoop2.utils.json.ValidatorFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.TipiVersamentoBD;
import it.govpay.bd.anagrafica.TipiVersamentoDominiBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Documento;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.EventiBD;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.tracciati.PendenzaPost;
import it.govpay.core.business.Iuv;
import it.govpay.core.exceptions.EcException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoNonValidoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.utils.EventoContext.Esito;
import it.govpay.core.utils.client.VerificaClient;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.core.utils.tracciati.validator.PendenzaPostValidator;
import it.govpay.core.utils.trasformazioni.TrasformazioniUtils;
import it.govpay.core.utils.trasformazioni.exception.TrasformazioneException;
import it.govpay.model.Anagrafica.TIPO;
import it.govpay.model.IbanAccredito;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.SingoloVersamento.TipoBollo;
import it.govpay.model.TipoVersamento;
import it.govpay.model.Tributo;
import it.govpay.model.Tributo.TipoContabilita;
import it.govpay.model.Versamento.CausaleSemplice;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.model.Versamento.TipoSogliaVersamento;
import it.govpay.model.Versamento.TipologiaTipoVersamento;

public class VersamentoUtils {
	
	public static VersamentoUtils getInstance() {
		return new VersamentoUtils();
	}

	public final static QName _VersamentoKeyCodApplicazione_QNAME = new QName("", "codApplicazione");
	public final static QName _VersamentoKeyCodVersamentoEnte_QNAME = new QName("", "codVersamentoEnte");
	public final static QName _VersamentoKeyCodDominio_QNAME = new QName("", "codDominio");
	public final static QName _VersamentoKeyCodUnivocoDebitore_QNAME = new QName("", "codUnivocoDebitore");
	public final static QName _VersamentoKeyBundlekey_QNAME = new QName("", "bundlekey");
	public final static QName _VersamentoKeyIuv_QNAME = new QName("", "iuv");

	public static void validazioneSemantica(Versamento versamento, boolean generaIuv) throws GovPayException, ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		
		// solo se non e' multibeneficiario
		if(generaIuv && !VersamentoUtils.isPendenzaMultibeneficiario(versamento, configWrapper) && versamento.getSingoliVersamenti().size() != 1) {
			throw new GovPayException(EsitoOperazione.VER_000, versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte());
		}

		BigDecimal somma = BigDecimal.ZERO;
		List<String> codSingoliVersamenti = new ArrayList<>();
		for(SingoloVersamento sv : versamento.getSingoliVersamenti()) {
			if(codSingoliVersamenti.contains(sv.getCodSingoloVersamentoEnte()))
				throw new GovPayException(EsitoOperazione.VER_001, versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), sv.getCodSingoloVersamentoEnte());

			codSingoliVersamenti.add(sv.getCodSingoloVersamentoEnte());
			somma = somma.add(sv.getImportoSingoloVersamento());
		}

		if(somma.compareTo(versamento.getImportoTotale()) != 0) {
			throw new GovPayException(EsitoOperazione.VER_002, versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), Double.toString(somma.doubleValue()), Double.toString(versamento.getImportoTotale().doubleValue()));
		}
	}

	public static void validazioneSemanticaAggiornamento(Versamento versamentoLetto, Versamento versamentoNuovo) throws GovPayException, ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		
		if(!versamentoLetto.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO) && !versamentoLetto.getStatoVersamento().equals(StatoVersamento.ANNULLATO)) {
			throw new GovPayException(EsitoOperazione.VER_003, versamentoNuovo.getApplicazione(configWrapper).getCodApplicazione(), versamentoLetto.getCodVersamentoEnte(), versamentoLetto.getStatoVersamento().toString());
		}
		if(versamentoLetto.getIdUo().compareTo(versamentoNuovo.getIdUo()) != 0) {
			// Pendenza (idA2A,idPendenza) presenta un beneficiario (vNuovo.idDominio/vNuovo.idUo) diverso da quello originale (vLetto.idDominio/vLetto.idUo)
			throw new GovPayException(EsitoOperazione.VER_004, versamentoNuovo.getApplicazione(configWrapper).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), 
					versamentoNuovo.getUo(configWrapper).getDominio(configWrapper).getCodDominio() , versamentoNuovo.getUo(configWrapper).getCodUo(),
					versamentoLetto.getUo(configWrapper).getDominio(configWrapper).getCodDominio(), versamentoLetto.getUo(configWrapper).getCodUo() );
		}
		versamentoNuovo.setId(versamentoLetto.getId());
		versamentoNuovo.setDataCreazione(versamentoLetto.getDataCreazione());

		// aggiornamento del numero avviso se il numero avviso non era stato salvato viene aggiornato automaticamente
		// se il numero avviso e' diverso da quello che e' stato salvato in origine devo segnalare eccezione
		if(versamentoLetto.getNumeroAvviso() != null && versamentoNuovo.getNumeroAvviso() !=null
				&& !versamentoLetto.getNumeroAvviso().equals(versamentoNuovo.getNumeroAvviso())) {
			throw new GovPayException(EsitoOperazione.VER_024, versamentoNuovo.getApplicazione(configWrapper).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), versamentoNuovo.getNumeroAvviso(), versamentoLetto.getNumeroAvviso());
		}

		List<SingoloVersamento> singoliversamentiLetti = versamentoLetto.getSingoliVersamenti();
		List<SingoloVersamento> singoliVersamentiNuovi = versamentoNuovo.getSingoliVersamenti();

		if(singoliversamentiLetti.size() != singoliVersamentiNuovi.size()) {
			throw new GovPayException(EsitoOperazione.VER_005, versamentoNuovo.getApplicazione(configWrapper).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), Integer.toString(singoliversamentiLetti.size()), Integer.toString(singoliVersamentiNuovi.size()));
		}
		Collections.sort(singoliversamentiLetti);
		Collections.sort(singoliVersamentiNuovi);

		for(int i=0; i<singoliversamentiLetti.size(); i++) {
			SingoloVersamento letto = singoliversamentiLetti.get(i);
			SingoloVersamento nuovo = singoliVersamentiNuovi.get(i);

			if(!letto.getCodSingoloVersamentoEnte().equals(nuovo.getCodSingoloVersamentoEnte())) {
				throw new GovPayException(EsitoOperazione.VER_006, versamentoNuovo.getApplicazione(configWrapper).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), letto.getCodSingoloVersamentoEnte());
			}

			if(letto.getIdTributo() != null || nuovo.getIdTributo() != null) { // se sono entrambi null OK
				if(letto.getIdTributo() == null && nuovo.getIdTributo() != null) { // se uno e' null e uno no Eccezione
					throw new GovPayException(EsitoOperazione.VER_007, versamentoNuovo.getApplicazione(configWrapper).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), letto.getCodSingoloVersamentoEnte(), "null", Long.toString(nuovo.getIdTributo()));
				}

				if(letto.getIdTributo() != null && nuovo.getIdTributo() == null) { // se uno e' null e uno no Eccezione
					throw new GovPayException(EsitoOperazione.VER_007, versamentoNuovo.getApplicazione(configWrapper).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), letto.getCodSingoloVersamentoEnte(), Long.toString(letto.getIdTributo()), "null");
				}

				if(letto.getIdTributo().longValue() != nuovo.getIdTributo().longValue()) { // se sono tutti e due non null controllo l'uguaglianza dei long value
					throw new GovPayException(EsitoOperazione.VER_007, versamentoNuovo.getApplicazione(configWrapper).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), letto.getCodSingoloVersamentoEnte(), Long.toString(letto.getIdTributo()), Long.toString(nuovo.getIdTributo()));
				}
			}

			if(!(letto.getIbanAccredito(configWrapper) == null && nuovo.getIbanAccredito(configWrapper) == null)) {
				if(letto.getIbanAccredito(configWrapper) == null || nuovo.getIbanAccredito(configWrapper).getId() == null || !letto.getIbanAccredito(configWrapper).getId().equals(nuovo.getIbanAccredito(configWrapper).getId())) {
					throw new GovPayException(EsitoOperazione.VER_023, versamentoNuovo.getApplicazione(configWrapper).getCodApplicazione(), versamentoNuovo.getCodVersamentoEnte(), letto.getCodSingoloVersamentoEnte());
				}
			}
			nuovo.setId(letto.getId());
			nuovo.setIdVersamento(letto.getIdVersamento());
		}
	}

	public static Versamento aggiornaVersamento(Versamento versamento) throws VersamentoScadutoException, VersamentoAnnullatoException, VersamentoDuplicatoException, 
	VersamentoSconosciutoException, ServiceException, ClientException, GovPayException, UtilsException, VersamentoNonValidoException {
		// Se il versamento non e' in attesa, non aggiorno un bel niente
		if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO))
			return versamento;
		
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

		// Controllo se la data di scadenza e' indicata ed e' decorsa
		if(versamento.getDataScadenza() != null && DateUtils.isDataDecorsa(versamento.getDataScadenza())) {
			throw new VersamentoScadutoException(versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte(), versamento.getDataScadenza());
		}else {
			if(versamento.getDataValidita() != null && DateUtils.isDataDecorsa(versamento.getDataValidita())) {
				IContext ctx = ContextThreadLocal.get();
				String codVersamentoEnte = versamento.getCodVersamentoEnte();
				String bundlekey = versamento.getCodBundlekey();
				String debitore = versamento.getAnagraficaDebitore().getCodUnivoco();
				String codDominio = versamento.getUo(configWrapper).getDominio(configWrapper).getCodDominio(); 
				String iuv = null;

				String codVersamentoEnteD = codVersamentoEnte != null ? codVersamentoEnte : "-";
				String bundlekeyD = bundlekey != null ? bundlekey : "-";
				String debitoreD = debitore != null ? debitore : "-";
				String dominioD = codDominio != null ? codDominio : "-";
				String iuvD = iuv != null ? iuv : "-";
				ctx.getApplicationLogger().log("verifica.validita", versamento.getApplicazione(configWrapper).getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);

				if(versamento.getApplicazione(configWrapper).getConnettoreIntegrazione() != null) {
					TipologiaTipoVersamento tipo = versamento.getTipo();
					try {
						versamento = acquisisciVersamento(versamento.getApplicazione(configWrapper), codVersamentoEnte, bundlekey, debitore, codDominio, iuv, tipo);
					} catch (ClientException e) {
						// Errore nella chiamata all'ente. Controllo se e' mandatoria o uso quel che ho
						if(GovpayConfig.getInstance().isAggiornamentoValiditaMandatorio()) 
							throw new VersamentoScadutoException(versamento.getApplicazione(configWrapper).getCodApplicazione(), codVersamentoEnte, versamento.getDataScadenza());
					} catch (VersamentoSconosciutoException e) {
						// Versamento sconosciuto all'ente (bug dell'ente?). Controllo se e' mandatoria o uso quel che ho
						if(GovpayConfig.getInstance().isAggiornamentoValiditaMandatorio()) 
							throw new VersamentoScadutoException(versamento.getApplicazione(configWrapper).getCodApplicazione(), codVersamentoEnte, versamento.getDataScadenza());
					} catch (VersamentoNonValidoException e) {
						// Versamento non valido per errori di validazione, se e' mandatorio l'aggiornamento rilancio l'eccezione altrimenti uso quello che ho
						if(GovpayConfig.getInstance().isAggiornamentoValiditaMandatorio())
							throw e;
					}
				} else if(GovpayConfig.getInstance().isAggiornamentoValiditaMandatorio()) 
					// connettore verifica non definito, versamento non aggiornabile
					throw new VersamentoScadutoException(versamento.getApplicazione(configWrapper).getCodApplicazione(), codVersamentoEnte, versamento.getDataScadenza());
			} else {
				// versamento valido
			} 
		}
		return versamento;
	}


	public static Versamento acquisisciVersamento(Applicazione applicazione, String codVersamentoEnte, String bundlekey, String debitore, String dominio, String iuv, TipologiaTipoVersamento tipo) 
			throws VersamentoScadutoException, VersamentoAnnullatoException, VersamentoDuplicatoException, VersamentoSconosciutoException, ServiceException, ClientException, GovPayException, UtilsException, VersamentoNonValidoException {

		String codVersamentoEnteD = codVersamentoEnte != null ? codVersamentoEnte : "-";
		String bundlekeyD = bundlekey != null ? bundlekey : "-";
		String debitoreD = debitore != null ? debitore : "-";
		String dominioD = dominio != null ? dominio : "-";
		String iuvD = iuv != null ? iuv : "-";

		IContext ctx = ContextThreadLocal.get();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		ctx.getApplicationLogger().log("verifica.avvio", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
		if(applicazione.getConnettoreIntegrazione() == null) {
			ctx.getApplicationLogger().log("verifica.nonConfigurata");
			throw new VersamentoSconosciutoException();
		}
		VerificaClient verificaClient = new VerificaClient(applicazione);
		// salvataggio id Rpt/ versamento/ pagamento
		verificaClient.getEventoCtx().setCodDominio(dominio); 
		verificaClient.getEventoCtx().setIuv(iuv);
		verificaClient.getEventoCtx().setIdA2A(applicazione.getCodApplicazione());
		verificaClient.getEventoCtx().setIdPendenza(codVersamentoEnte);
		Versamento versamento = null;
		try {
			try {
				versamento = verificaClient.invoke(codVersamentoEnte, bundlekey, debitore, dominio, iuv);
				ctx.getApplicationLogger().log("verifica.Ok", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);

				verificaClient.getEventoCtx().setEsito(Esito.OK);
			} catch (ClientException e){
				ctx.getApplicationLogger().log("verifica.Fail", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD, e.getMessage());
				verificaClient.getEventoCtx().setSottotipoEsito(e.getResponseCode() + "");
				verificaClient.getEventoCtx().setEsito(Esito.FAIL);
				verificaClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				verificaClient.getEventoCtx().setException(e);
				throw e;
			} catch (VersamentoScadutoException e) {
				ctx.getApplicationLogger().log("verifica.Scaduto", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
				verificaClient.getEventoCtx().setSottotipoEsito("Pendenza Scaduta");
				verificaClient.getEventoCtx().setEsito(Esito.KO);
				verificaClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				verificaClient.getEventoCtx().setException(e);
				throw e;
			} catch (VersamentoAnnullatoException e) {
				ctx.getApplicationLogger().log("verifica.Annullato", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
				verificaClient.getEventoCtx().setSottotipoEsito("Pendenza Annullata");
				verificaClient.getEventoCtx().setEsito(Esito.KO);
				verificaClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				verificaClient.getEventoCtx().setException(e);
				throw e;
			} catch (VersamentoDuplicatoException e) {
				ctx.getApplicationLogger().log("verifica.Duplicato", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
				verificaClient.getEventoCtx().setSottotipoEsito("Pendenza Duplicata");
				verificaClient.getEventoCtx().setEsito(Esito.KO);
				verificaClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				verificaClient.getEventoCtx().setException(e);
				throw e;
			} catch (VersamentoSconosciutoException e) {
				ctx.getApplicationLogger().log("verifica.Sconosciuto", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
				verificaClient.getEventoCtx().setSottotipoEsito("Pendenza Sconosciuta");
				verificaClient.getEventoCtx().setEsito(Esito.KO);
				verificaClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				verificaClient.getEventoCtx().setException(e);
				throw e;
			} catch (VersamentoNonValidoException e) {
				ctx.getApplicationLogger().log("verifica.Fail", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD, e.getMessage());
				verificaClient.getEventoCtx().setSottotipoEsito("Pendenza non valida");
				verificaClient.getEventoCtx().setEsito(Esito.KO);
				verificaClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				verificaClient.getEventoCtx().setException(e);
				throw e;
			} 

			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento();
			boolean generaIuv = VersamentoUtils.generaIUV(versamento, configWrapper);
			versamento.setTipo(tipo);
			versamentoBusiness.caricaVersamento(versamento, generaIuv, true, false, null, null);
		}finally {
			EventoContext eventoCtx = verificaClient.getEventoCtx();

			if(eventoCtx.isRegistraEvento()) {
				// log evento
				EventiBD eventiBD = new EventiBD(configWrapper);
				eventiBD.insertEvento(eventoCtx.toEventoDTO());
			}
		}
		return versamento;
	}


	public static Versamento inoltroPendenza(Applicazione applicazione, String codDominio, String codTipoVersamento, String codUnitaOperativa, String jsonBody) 
			throws VersamentoScadutoException, VersamentoAnnullatoException, VersamentoDuplicatoException, VersamentoSconosciutoException, ServiceException, ClientException, GovPayException, UtilsException, VersamentoNonValidoException {

		IContext ctx = ContextThreadLocal.get();
		BDConfigWrapper configWrapper = new BDConfigWrapper(ctx.getTransactionId(), true);
		ctx.getApplicationLogger().log("verifica.modello4Avvio", applicazione.getCodApplicazione(), codDominio, codTipoVersamento);
		if(applicazione.getConnettoreIntegrazione() == null) {
			ctx.getApplicationLogger().log("verifica.nonConfigurata");
			throw new VersamentoSconosciutoException();
		}
		VerificaClient verificaClient = new VerificaClient(applicazione);
		// salvataggio id Rpt/ versamento/ pagamento
		verificaClient.getEventoCtx().setCodDominio(codDominio); 
		verificaClient.getEventoCtx().setIdA2A(applicazione.getCodApplicazione());
		Versamento versamento = null;
		try {
			try {
				versamento = verificaClient.invokeInoltro(codDominio, codTipoVersamento, codUnitaOperativa, jsonBody);
				String codVersamentoEnte = "-"; 
				if(versamento != null) {
					codVersamentoEnte = versamento.getCodVersamentoEnte(); 
					verificaClient.getEventoCtx().setIuv(versamento.getIuvVersamento());
					verificaClient.getEventoCtx().setIdPendenza(codVersamentoEnte);
				}
				ctx.getApplicationLogger().log("verifica.modello4Ok", applicazione.getCodApplicazione(), codDominio, codTipoVersamento, codVersamentoEnte);
				verificaClient.getEventoCtx().setEsito(Esito.OK);
			} catch (ClientException e){
				ctx.getApplicationLogger().log("verifica.modello4Fail", applicazione.getCodApplicazione(), codDominio, codTipoVersamento, e.getMessage());
				verificaClient.getEventoCtx().setSottotipoEsito(e.getResponseCode() + "");
				verificaClient.getEventoCtx().setEsito(Esito.FAIL);
				verificaClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				verificaClient.getEventoCtx().setException(e);
				throw e;
			} catch (VersamentoScadutoException e) {
				ctx.getApplicationLogger().log("verifica.modello4Scaduto", applicazione.getCodApplicazione(),  codDominio, codTipoVersamento);
				verificaClient.getEventoCtx().setSottotipoEsito("Pendenza Scaduta");
				verificaClient.getEventoCtx().setEsito(Esito.KO);
				verificaClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				verificaClient.getEventoCtx().setException(e);
				throw e;
			} catch (VersamentoAnnullatoException e) {
				ctx.getApplicationLogger().log("verifica.modello4Annullato", applicazione.getCodApplicazione(), codDominio, codTipoVersamento);
				verificaClient.getEventoCtx().setSottotipoEsito("Pendenza Annullata");
				verificaClient.getEventoCtx().setEsito(Esito.KO);
				verificaClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				verificaClient.getEventoCtx().setException(e);
				throw e;
			} catch (VersamentoDuplicatoException e) {
				ctx.getApplicationLogger().log("verifica.modello4Duplicato", applicazione.getCodApplicazione(), codDominio, codTipoVersamento);
				verificaClient.getEventoCtx().setSottotipoEsito("Pendenza Duplicata");
				verificaClient.getEventoCtx().setEsito(Esito.KO);
				verificaClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				verificaClient.getEventoCtx().setException(e);
				throw e;
			} catch (VersamentoSconosciutoException e) {
				ctx.getApplicationLogger().log("verifica.modello4Sconosciuto", applicazione.getCodApplicazione(), codDominio, codTipoVersamento);
				verificaClient.getEventoCtx().setSottotipoEsito("Pendenza Sconosciuta");
				verificaClient.getEventoCtx().setEsito(Esito.KO);
				verificaClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				verificaClient.getEventoCtx().setException(e);
				throw e;
			} catch (VersamentoNonValidoException e) {
				ctx.getApplicationLogger().log("verifica.modello4Fail", applicazione.getCodApplicazione(), codDominio, codTipoVersamento, e.getMessage());
				verificaClient.getEventoCtx().setSottotipoEsito("Pendenza non valida");
				verificaClient.getEventoCtx().setEsito(Esito.KO);
				verificaClient.getEventoCtx().setDescrizioneEsito(e.getMessage());
				verificaClient.getEventoCtx().setException(e);
				throw e;
			} 

			//			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			//			boolean generaIuv = versamento.getNumeroAvviso() == null && versamento.getSingoliVersamenti(bd).size() == 1;
			//			versamentoBusiness.caricaVersamento(versamento, generaIuv, true);
		}finally {
			EventoContext eventoCtx = verificaClient.getEventoCtx();

			if(eventoCtx.isRegistraEvento()) {
				// log evento
				EventiBD eventiBD = new EventiBD(configWrapper);
				eventiBD.insertEvento(eventoCtx.toEventoDTO());
			}
		}
		return versamento;
	}
	
	public static String getIuvFromNumeroAvviso(String numeroAvviso) throws GovPayException {
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
			return numeroAvviso.substring(3);
		}else if(numeroAvviso.startsWith("1")) // '1' + reference(17)
			return numeroAvviso.substring(1);
		else if(numeroAvviso.startsWith("2")) // '2' + ref(15) + check(2)
			return numeroAvviso.substring(1);
		else if(numeroAvviso.startsWith("3")) { // '3' + segregationCode(2) +  ref(13) + check(2)
			return numeroAvviso.substring(1);
		}else 
			throw new GovPayException(EsitoOperazione.VER_017, numeroAvviso);
	}


	public static void verifyNumeroAvviso(String numeroAvviso,String codDominio, String codStazione, Integer applicationCode, Integer segregationCode) throws GovPayException {
		
		getIuvFromNumeroAvviso(numeroAvviso);

		if(numeroAvviso.startsWith("0")) { // '0' + applicationCode(2) + ref(13) + check(2)
			// controllo che l'application code coincida con quello previsto
			String appCodeS = numeroAvviso.substring(1, 3);
			Integer appCode = Integer.parseInt(appCodeS);

			if(applicationCode != null && applicationCode.intValue() != appCode.intValue())
				throw new GovPayException(EsitoOperazione.VER_026, numeroAvviso, appCodeS, codStazione);
		} else if(numeroAvviso.startsWith("3")) { // '3' + segregationCode(2) +  ref(13) + check(2)
			// controllo che segregationCode coincida con quello previsto
			String segCodeS = numeroAvviso.substring(1, 3);
			Integer segCode = Integer.parseInt(segCodeS);

			if(segregationCode != null && segregationCode.intValue() != segCode.intValue())
				throw new GovPayException(EsitoOperazione.VER_027, numeroAvviso, segCodeS, codDominio);
		}
	}

	public static Versamento toVersamentoModel(it.govpay.core.dao.commons.Versamento versamento) throws ServiceException, GovPayException, ValidationException { 
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		Versamento model = new Versamento();
		model.setAggiornabile(versamento.isAggiornabile() == null ? true : versamento.isAggiornabile());
		model.setAnagraficaDebitore(toAnagraficaModel(versamento.getDebitore()));

		CausaleSemplice causale = model.new CausaleSemplice();
		causale.setCausale(versamento.getCausale());
		model.setCausaleVersamento(causale);
		model.setDatiAllegati(versamento.getDatiAllegati()); 
		model.setCodAnnoTributario(versamento.getAnnoTributario());
		model.setCodBundlekey(versamento.getBundlekey());
		model.setCodLotto(versamento.getCodLotto()); 
		model.setCodVersamentoEnte(versamento.getCodVersamentoEnte());
		model.setDataCreazione(versamento.getDataCaricamento());
		model.setDataScadenza(versamento.getDataScadenza());
		model.setDataValidita(versamento.getDataValidita());
		model.setDataUltimoAggiornamento(new Date());
		model.setDescrizioneStato(null);
		model.setNome(versamento.getNome());

		model.setId(null);
		try {
			model.setApplicazione(versamento.getCodApplicazione(), configWrapper);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.APP_000, versamento.getCodApplicazione());
		}

		if(!model.getApplicazione(configWrapper).getUtenza().isAbilitato())
			throw new GovPayException(EsitoOperazione.APP_001, versamento.getCodApplicazione());

		Applicazione applicazione = model.getApplicazione(configWrapper);

		Dominio dominio = null;
		try {
			dominio = AnagraficaManager.getDominio(configWrapper, versamento.getCodDominio());
			model.setIdDominio(dominio.getId()); 
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.DOM_000, versamento.getCodDominio());
		}

		if(!dominio.isAbilitato())
			throw new GovPayException(EsitoOperazione.DOM_001, dominio.getCodDominio());

		try {
			String codUnitaOperativa = (versamento.getCodUnitaOperativa() == null) ? it.govpay.model.Dominio.EC : versamento.getCodUnitaOperativa();
			model.setUo(dominio.getId(), codUnitaOperativa, configWrapper);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.UOP_000, versamento.getCodUnitaOperativa(), versamento.getCodDominio());
		}

		if(!model.getUo(configWrapper).isAbilitato())
			throw new GovPayException(EsitoOperazione.UOP_001, versamento.getCodUnitaOperativa(), versamento.getCodDominio());

		model.setImportoTotale(versamento.getImportoTotale());
		model.setStatoVersamento(StatoVersamento.NON_ESEGUITO);

		// in un versamento multivoce non si puo' passare il numero avviso
		if(versamento.getSingoloVersamento().size() > 1 && StringUtils.isNotEmpty(versamento.getNumeroAvviso())) {
			throw new GovPayException(EsitoOperazione.VER_031);
		}

		int index = 1;
		for(it.govpay.core.dao.commons.Versamento.SingoloVersamento singoloVersamento : versamento.getSingoloVersamento()) {
			model.addSingoloVersamento(toSingoloVersamentoModel(model, singoloVersamento, index++ , configWrapper));
		}

		model.setTassonomia(versamento.getTassonomia());
		model.setTassonomiaAvviso(versamento.getTassonomiaAvviso());

		model.setIncasso(versamento.getIncasso());
		model.setAnomalie(versamento.getAnomalie()); 
		model.setNumeroAvviso(versamento.getNumeroAvviso());	

		if(versamento.getNumeroAvviso() != null) {
			String iuvFromNumeroAvviso = it.govpay.core.utils.VersamentoUtils.getIuvFromNumeroAvviso(versamento.getNumeroAvviso());
			it.govpay.core.utils.VersamentoUtils.verifyNumeroAvviso(versamento.getNumeroAvviso(),dominio.getCodDominio(),dominio.getStazione().getCodStazione(),
					dominio.getStazione().getApplicationCode(), dominio.getSegregationCode());

			// check sulla validita' dello iuv
			Iuv iuvBD  = new Iuv();
			TipoIUV tipo = iuvBD.getTipoIUV(iuvFromNumeroAvviso);
			try {
				iuvBD.checkIUV(dominio, iuvFromNumeroAvviso, tipo );
			}catch(UtilsException e) {
				throw new GovPayException(e);
			}

			model.setIuvVersamento(iuvFromNumeroAvviso);
			model.setIuvProposto(iuvFromNumeroAvviso); 
		}

		String codTipoVersamento = versamento.getCodTipoVersamento();
		boolean utilizzaTipoPendenzaNonCensito = false;
		if(codTipoVersamento == null) {
			utilizzaTipoPendenzaNonCensito = true;
			if(versamento.getSingoloVersamento() != null && versamento.getSingoloVersamento().size() > 0) {
				it.govpay.core.dao.commons.Versamento.SingoloVersamento sv = versamento.getSingoloVersamento().get(0);
				if(sv.getBolloTelematico() != null) {
					codTipoVersamento = Tributo.BOLLOT;
				} else if(sv.getCodTributo() != null) {
					codTipoVersamento = sv.getCodTributo();
				} else {
					codTipoVersamento = GovpayConfig.getInstance().getCodTipoVersamentoPendenzeLibere();
				}
			} 
		}  

		// tipo Pendenza
		TipoVersamento tipoVersamento = null;
		try {
			tipoVersamento = AnagraficaManager.getTipoVersamento(configWrapper, codTipoVersamento);
		} catch (NotFoundException e) {
			if(!GovpayConfig.getInstance().isCensimentoTipiVersamentoSconosciutiEnabled()) {
				if(utilizzaTipoPendenzaNonCensito) {
					try {
						tipoVersamento = AnagraficaManager.getTipoVersamento(configWrapper, GovpayConfig.getInstance().getCodTipoVersamentoPendenzeNonCensite());
					} catch (NotFoundException e1) {
						throw new ServiceException("Non e' stato censito un tipo pendenza di default valido");
					}
				} else {
					throw new GovPayException(EsitoOperazione.TVR_000, codTipoVersamento);
				}
			} else {
				// censimento del tipo pendenza
				TipiVersamentoBD tipiVersamentoBD = new TipiVersamentoBD(configWrapper);
				tipoVersamento = tipiVersamentoBD.autoCensimentoTipoVersamento(codTipoVersamento);
				try {
					AnagraficaManager.cleanCache();
				} catch (UtilsException e1) {
					throw new ServiceException(e1);
				}
			}
		}
		
		if(!tipoVersamento.isAbilitatoDefault())
			throw new GovPayException(EsitoOperazione.TVR_001, codTipoVersamento);
		
		model.setIdTipoVersamento(tipoVersamento.getId()); 

		if(!applicazione.isTrusted() && !AuthorizationManager.isTipoVersamentoAuthorized(applicazione.getUtenza(), tipoVersamento.getCodTipoVersamento())) {
			throw new GovPayException(EsitoOperazione.VER_022, tipoVersamento.getCodTipoVersamento());
		}

		// tipo pendenza dominio
		TipoVersamentoDominio tipoVersamentoDominio= null;

		try {
			tipoVersamentoDominio = AnagraficaManager.getTipoVersamentoDominio(configWrapper, dominio.getId(), tipoVersamento.getCodTipoVersamento());
		} catch (NotFoundException e) {
			if(!GovpayConfig.getInstance().isCensimentoTipiVersamentoSconosciutiEnabled()) {
				throw new GovPayException(EsitoOperazione.TVD_000, codTipoVersamento, versamento.getCodDominio());
			} else {
				TipiVersamentoDominiBD tipiVersamentoDominiBD = new TipiVersamentoDominiBD(configWrapper);
				tipoVersamentoDominio = tipiVersamentoDominiBD.autoCensimentoTipoVersamentoDominio(tipoVersamento, dominio);
				try {
					AnagraficaManager.cleanCache();
				} catch (UtilsException e1) {
					throw new ServiceException(e1);
				}
			}
//			try {
//				tipoVersamentoDominio = AnagraficaManager.getTipoVersamentoDominio(bd, dominio.getId(), GovpayConfig.getInstance().getCodTipoVersamentoPendenzeNonCensite());
//			} catch (NotFoundException e1) {
//				throw new ServiceException("Non e' stato censito un tipo pendenza di default valido");
//			}
		}
		
		if(tipoVersamentoDominio.getAbilitatoCustom() != null && !tipoVersamentoDominio.getAbilitatoCustom())
			throw new GovPayException(EsitoOperazione.TVD_001, codTipoVersamento, versamento.getCodDominio());
		
		model.setIdTipoVersamentoDominio(tipoVersamentoDominio.getId());
		
		model.setDirezione(versamento.getDirezione());
		model.setDivisione(versamento.getDivisione()); 
		
		// Documento
		if(versamento.getDocumento() != null) {
			Documento documentoModel = new Documento();
		
			documentoModel.setCodDocumento(versamento.getDocumento().getCodDocumento());
			documentoModel.setDescrizione(versamento.getDocumento().getDescrizione());
			documentoModel.setIdApplicazione(applicazione.getId());
			documentoModel.setIdDominio(dominio.getId());
			
			model.setNumeroRata(versamento.getDocumento().getCodRata());
			
			if(versamento.getDocumento().getTipoSoglia() != null) {
				model.setTipoSoglia(TipoSogliaVersamento.valueOf(versamento.getDocumento().getTipoSoglia()));
			}
			model.setGiorniSoglia(versamento.getDocumento().getGiorniSoglia());
			
			model.setDocumento(documentoModel);
		}
		
		model.setDataNotificaAvviso(versamento.getDataNotificaAvviso());
		model.setAvvAppIODataPromemoriaScadenza(versamento.getDataPromemoriaScadenza());
		model.setAvvMailDataPromemoriaScadenza(versamento.getDataPromemoriaScadenza());
		
		model.setProprietaPendenza(versamento.getProprieta()); 

		return model;
	}


	public static SingoloVersamento toSingoloVersamentoModel(Versamento versamento, it.govpay.core.dao.commons.Versamento.SingoloVersamento singoloVersamento, int index, BDConfigWrapper configWrapper) throws ServiceException, GovPayException, ValidationException {
		SingoloVersamento model = new SingoloVersamento();
		model.setVersamento(versamento);
		model.setCodSingoloVersamentoEnte(singoloVersamento.getCodSingoloVersamentoEnte());
		model.setIndiceDati(index);
		model.setId(null);
		model.setIdVersamento(0);
		model.setImportoSingoloVersamento(singoloVersamento.getImporto());
		model.setStatoSingoloVersamento(StatoSingoloVersamento.NON_ESEGUITO);
		model.setDescrizione(singoloVersamento.getDescrizione()); 
		model.setDescrizioneCausaleRPT(singoloVersamento.getDescrizioneCausaleRPT()); 
		model.setDatiAllegati(singoloVersamento.getDatiAllegati()); 
		model.setContabilita(singoloVersamento.getContabilita());
		
		Dominio dominioSingoloVersamento = null;
		if(singoloVersamento.getCodDominio() != null) {
			try {
				dominioSingoloVersamento = AnagraficaManager.getDominio(configWrapper, singoloVersamento.getCodDominio());
				model.setIdDominio(dominioSingoloVersamento.getId()); 
				
				
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.DOM_000, singoloVersamento.getCodDominio());
			}
	
			if(!dominioSingoloVersamento.isAbilitato())
				throw new GovPayException(EsitoOperazione.DOM_001, dominioSingoloVersamento.getCodDominio());
		} else {
			// se il dominio non e' esplicitamente indicato nel singolo versamento utilizzo quello del versamento
			dominioSingoloVersamento = versamento.getDominio(configWrapper);
		}
		
		if(singoloVersamento.getBolloTelematico() != null) {
			try {
				model.setTributo(Tributo.BOLLOT, configWrapper);
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.TRB_000, dominioSingoloVersamento.getCodDominio(), Tributo.BOLLOT);
			}

			if(model.getTributo(configWrapper)!= null) {
				if(!model.getTributo(configWrapper).isAbilitato())
					throw new GovPayException(EsitoOperazione.TRB_001, dominioSingoloVersamento.getCodDominio(), Tributo.BOLLOT);
			}

			model.setHashDocumento(singoloVersamento.getBolloTelematico().getHash());
			model.setProvinciaResidenza(singoloVersamento.getBolloTelematico().getProvincia());
			try {
				model.setTipoBollo(TipoBollo.toEnum(singoloVersamento.getBolloTelematico().getTipo()));
			} catch (ServiceException e) {
				throw new ValidationException(e.getMessage());
			}
			
			// campi tipocompatibilita e codcompatibilita dalle nuove API
			if(singoloVersamento.getBolloTelematico().getTipoContabilita() != null)
				model.setTipoContabilita(TipoContabilita.valueOf(singoloVersamento.getBolloTelematico().getTipoContabilita().toString()));
			if(singoloVersamento.getBolloTelematico().getCodContabilita() != null)
				model.setCodContabilita(singoloVersamento.getBolloTelematico().getCodContabilita());
			
		} 

		if(singoloVersamento.getCodTributo() != null) {
			try {
				model.setTributo(singoloVersamento.getCodTributo(), configWrapper);
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.TRB_000, dominioSingoloVersamento.getCodDominio(), singoloVersamento.getCodTributo());
			}

			if(model.getTributo(configWrapper)!= null) {
				if(!model.getTributo(configWrapper).isAbilitato())
					throw new GovPayException(EsitoOperazione.TRB_001, dominioSingoloVersamento.getCodDominio(), singoloVersamento.getCodTributo());
			}

		}

		if(singoloVersamento.getTributo() != null) {

			//			if(!applicazione.isTrusted())
			//				throw new GovPayException(EsitoOperazione.VER_019);
			//			
			//			if(!AuthorizationManager.isAuthorized(applicazione.getUtenza(), applicazione.getUtenza().getTipoUtenza(), Servizio.PAGAMENTI_E_PENDENZE, dominio.getCodDominio(), null, diritti))
			//				throw new GovPayException(EsitoOperazione.VER_021);
			// TODO test sull'autodeterminazione

			model.setTipoContabilita(TipoContabilita.valueOf(singoloVersamento.getTributo().getTipoContabilita().toString()));
			model.setCodContabilita(singoloVersamento.getTributo().getCodContabilita());
			
			try {
				model.setIbanAccredito(AnagraficaManager.getIbanAccredito(configWrapper, dominioSingoloVersamento.getId(), singoloVersamento.getTributo().getIbanAccredito()));
				if(!model.getIbanAccredito(configWrapper).isAbilitato())
					throw new GovPayException(EsitoOperazione.VER_032, dominioSingoloVersamento.getCodDominio(), singoloVersamento.getTributo().getIbanAccredito());
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.VER_020, dominioSingoloVersamento.getCodDominio(), singoloVersamento.getTributo().getIbanAccredito());
			}
			
			try {
				if(singoloVersamento.getTributo().getIbanAppoggio() != null) {
					model.setIbanAppoggio(AnagraficaManager.getIbanAccredito(configWrapper, dominioSingoloVersamento.getId(), singoloVersamento.getTributo().getIbanAppoggio()));

					if(!model.getIbanAppoggio(configWrapper).isAbilitato())
						throw new GovPayException(EsitoOperazione.VER_034, dominioSingoloVersamento.getCodDominio(), singoloVersamento.getTributo().getIbanAppoggio());
				}
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.VER_033, dominioSingoloVersamento.getCodDominio(), singoloVersamento.getTributo().getIbanAppoggio());
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
	
	public static void validazioneInputVersamentoModello4(Logger log, String inputModello4, String validazioneDefinizione) throws GovPayException, ValidationException {
		if(validazioneDefinizione != null) {
			log.debug("Validazione tramite JSON Schema...");

			IJsonSchemaValidator validator = null;

			try{
				validator = ValidatorFactory.newJsonSchemaValidator(ApiName.NETWORK_NT);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new GovPayException(EsitoOperazione.VAL_000, e, e.getMessage());
			}
			JsonSchemaValidatorConfig config = new JsonSchemaValidatorConfig();

			try {
				// TODO eliminare dopo demo
				if(validazioneDefinizione.startsWith("\""))
					validazioneDefinizione = validazioneDefinizione.substring(1);

				if(validazioneDefinizione.endsWith("\""))
					validazioneDefinizione = validazioneDefinizione.substring(0, validazioneDefinizione.length() - 1);
				
				byte[] validazioneBytes = Base64.getDecoder().decode(validazioneDefinizione.getBytes());
				
				validator.setSchema(validazioneBytes, config, log);
			} catch (IllegalArgumentException e) {
				log.error("JSON schema non codificato correttamente: " + e.getMessage(), e);
				throw new GovPayException(EsitoOperazione.VAL_001, e, "non e' stato possibile decodificare il jsonschema di validazione ("+e.getMessage()+").");
			} catch (ValidationException e) {
				log.error("Validazione tramite JSON Schema completata con errore: " + e.getMessage(), e);
				throw new GovPayException(EsitoOperazione.VAL_001, e , e.getMessage());
			} 	
			ValidationResponse validate = null;
			try {
				validate = validator.validate(inputModello4.getBytes());
			} catch (ValidationException e) {
				log.debug("Validazione tramite JSON Schema completata con errore: " + e.getMessage(), e);
				throw new GovPayException(EsitoOperazione.VAL_002, e, e.getMessage());
			} 

			ESITO esito = validate.getEsito();

			switch (esito) {
			case KO:
				log.debug("Validazione tramite JSON Schema completata con esito KO: " + validate.getErrors());
				throw new ValidationException(String.join(",", validate.getErrors()));
			case OK:
				log.debug("Validazione tramite JSON Schema completata con esito OK.");
				break;
			}
		}
	}
	
	public static String trasformazioneInputVersamentoModello4(Logger log, Dominio dominio, String codTipoVersamento, String tipoTemplateTrasformazione, UnitaOperativa uo,
			String inputModello4, MultivaluedMap<String, String> queryParameters,
			MultivaluedMap<String, String> pathParameters, Map<String, String> headers,
			String trasformazioneDefinizione) throws GovPayException, UnprocessableEntityException {
		log.debug("Trasformazione tramite template "+tipoTemplateTrasformazione+"...");
		String name = "TrasformazionePendenzaModello4";
		try {
			// TODO eliminare dopo demo
			if(trasformazioneDefinizione.startsWith("\""))
				trasformazioneDefinizione = trasformazioneDefinizione.substring(1);

			if(trasformazioneDefinizione.endsWith("\""))
				trasformazioneDefinizione = trasformazioneDefinizione.substring(0, trasformazioneDefinizione.length() - 1);

			byte[] template = Base64.getDecoder().decode(trasformazioneDefinizione.getBytes());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Map<String, Object> dynamicMap = new HashMap<String, Object>();
			String idUnitaOperativa = uo != null ? uo.getCodUo() : null;
			TrasformazioniUtils.fillDynamicMap(log, dynamicMap, ContextThreadLocal.get(), queryParameters, 
					pathParameters, headers, inputModello4, dominio.getCodDominio(), codTipoVersamento, idUnitaOperativa); 
			TrasformazioniUtils.convertFreeMarkerTemplate(name, template , dynamicMap , baos );
			// assegno il json trasformato
			inputModello4 = baos.toString();
			log.debug("Trasformazione tramite template "+tipoTemplateTrasformazione+" completata con successo.");
		} catch (TrasformazioneException e) {
			log.error("Trasformazione tramite template "+tipoTemplateTrasformazione+" completata con errore: " + e.getMessage(), e);
			throw new GovPayException(e.getMessage(), EsitoOperazione.TRASFORMAZIONE, e, e.getMessage());
		}
		return inputModello4;
	}
	
	public static Versamento inoltroInputVersamentoModello4(Logger log, String codDominio, String codTipoVersamento, String codUnitaOperativa, String codApplicazioneInoltro, 
			 String inputModello4) throws ServiceException, GovPayException, EcException, ValidationException {
		
		if(codApplicazioneInoltro != null) {
			return _inoltroInputVersamentoModello4(log, codDominio, codTipoVersamento, codUnitaOperativa, inputModello4, codApplicazioneInoltro);
		} else {
			PendenzaPost pendenzaPost = PendenzaPost.parse(inputModello4);
			new PendenzaPostValidator(pendenzaPost).validate();
			
			it.govpay.core.dao.commons.Versamento versamentoCommons = TracciatiConverter.getVersamentoFromPendenza(pendenzaPost);
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPendenza(versamentoCommons.getCodVersamentoEnte());
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdA2A(versamentoCommons.getCodApplicazione());
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento();
			return versamentoBusiness.chiediVersamento(versamentoCommons);
		}
	}

	private static Versamento _inoltroInputVersamentoModello4(Logger log, String codDominio, String codTipoVersamento, String codUnitaOperativa, String inputModello4, String codApplicazione)
			throws ServiceException, GovPayException, EcException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		Versamento chiediVersamento = null;
		log.debug("Inoltro verso l'applicazione "+codApplicazione+"...");
		
		it.govpay.bd.model.Applicazione applicazione = null;
		try {
			applicazione = AnagraficaManager.getApplicazione(configWrapper, codApplicazione);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.APP_000, codApplicazione);
		}
		
		if(!applicazione.getUtenza().isAbilitato())
			throw new GovPayException(EsitoOperazione.APP_001, applicazione.getCodApplicazione());
		
		try {
			chiediVersamento = VersamentoUtils.inoltroPendenza(applicazione, codDominio, codTipoVersamento, codUnitaOperativa, inputModello4);
			VersamentoUtils.validazioneSemantica(chiediVersamento, chiediVersamento.getNumeroAvviso() == null && chiediVersamento.getSingoliVersamenti().size() == 1);
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPendenza(chiediVersamento.getCodVersamentoEnte());
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdA2A(chiediVersamento.getApplicazione(configWrapper).getCodApplicazione());
		} catch (ClientException e){
			throw new EcException("L'inoltro del versamento [Dominio: " + codDominio + " TipoVersamento:" + codTipoVersamento + "] all'applicazione competente [Applicazione:" + codApplicazione + "] e' fallito con errore: " + e.getMessage());
		} catch (UtilsException e){
			throw new EcException("L'inoltro del versamento [Dominio: " + codDominio + " TipoVersamento:" + codTipoVersamento + "] all'applicazione competente [Applicazione:" + codApplicazione + "] e' fallito con errore: " + e.getMessage());
		} catch (VersamentoScadutoException e) {
			throw new EcException("L'inoltro del versamento [Dominio: " + codDominio + " TipoVersamento:" + codTipoVersamento + "] all'applicazione competente [Applicazione:" + codApplicazione + "] ha dato esito PAA_PAGAMENTO_SCADUTO");
		} catch (VersamentoAnnullatoException e) {
			throw new EcException("L'inoltro del versamento [Dominio: " + codDominio + " TipoVersamento:" + codTipoVersamento + "] all'applicazione competente [Applicazione:" + codApplicazione + "] ha dato esito PAA_PAGAMENTO_ANNULLATO");
		} catch (VersamentoDuplicatoException e) {
			throw new EcException("L'inoltro del versamento [Dominio: " + codDominio + " TipoVersamento:" + codTipoVersamento + "] all'applicazione competente [Applicazione:" + codApplicazione + "] ha dato esito PAA_PAGAMENTO_DUPLICATO");
		} catch (VersamentoSconosciutoException e) {
			throw new EcException("L'inoltro del versamento [Dominio: " + codDominio + " TipoVersamento:" + codTipoVersamento + "] all'applicazione competente [Applicazione:" + codApplicazione + "] ha dato esito PAA_PAGAMENTO_SCONOSCIUTO");
		} catch (VersamentoNonValidoException e) { 
			throw new EcException("L'inoltro del versamento [Dominio: " + codDominio + " TipoVersamento:" + codTipoVersamento + "] all'applicazione competente [Applicazione:" + codApplicazione + "] e' fallito con errore: " + e.getMessage());
		} catch (GovPayException e){
			throw new EcException("L'inoltro del versamento [Dominio: " + codDominio + " TipoVersamento:" + codTipoVersamento + "] all'applicazione competente [Applicazione:" + codApplicazione + "] e' fallito con errore: " + e.getMessage());
		} 
		
		log.debug("Inoltro verso l'applicazione "+codApplicazione+" completato con successo.");
		return chiediVersamento;
	}
	
	public boolean isNumeroRata(String stringValue) {
		try {
			if(StringUtils.isEmpty(stringValue)) 
				return false;
			
			Integer.parseInt(stringValue.trim());
			return true;
		} catch (Throwable t) {
			return false;
		}
	}
	
	public String getTipoSogliaPagamento(String stringValue) {
		String tmp = stringValue.toUpperCase().trim();
		if(tmp.startsWith(TipoSogliaVersamento.ENTRO.toString())) {
			return TipoSogliaVersamento.ENTRO.toString();
		} else if(tmp.startsWith(TipoSogliaVersamento.OLTRE.toString())) {
			return TipoSogliaVersamento.OLTRE.toString();
		} else {
			return null;
		}
	}
	
	public Integer getGiorniSogliaPagamento(String stringValue) {
		try {
			String val = stringValue;
			String tmp = stringValue.toUpperCase().trim();
			if(tmp.startsWith(TipoSogliaVersamento.ENTRO.toString())) {
				val = tmp.substring(tmp.indexOf(TipoSogliaVersamento.ENTRO.toString())+ TipoSogliaVersamento.ENTRO.toString().length());
			} else if(tmp.startsWith(TipoSogliaVersamento.OLTRE.toString())) {
				val = tmp.substring(tmp.indexOf(TipoSogliaVersamento.OLTRE.toString())+ TipoSogliaVersamento.OLTRE.toString().length());
			} else {
				val = stringValue.trim();
			}
			
			return Integer.parseInt(val);
		} catch (Throwable t) {
			return null;
		}
	}
	
	public static boolean isPendenzaMultibeneficiario(Versamento versamento, BDConfigWrapper configWrapper) throws ServiceException {
		for(SingoloVersamento singoloVersamento : versamento.getSingoliVersamenti(configWrapper)) {
			// appena trovo un singolo versamento con un id dominio definito sono in modalita' multibeneficiario
			if(singoloVersamento.getIdDominio() != null) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean generaIUV(Versamento versamento, BDConfigWrapper configWrapper) throws ServiceException {
		if(isPendenzaMultibeneficiario(versamento, configWrapper)) {
			return versamento.getNumeroAvviso() == null;
		}
		
		boolean generaIuv = versamento.getNumeroAvviso() == null && versamento.getSingoliVersamenti(configWrapper).size() == 1;
				
		return generaIuv;
	}
	
	public static boolean isAllIBANPostali(Versamento versamento, BDConfigWrapper configWrapper) throws ServiceException {
		for(SingoloVersamento singoloVersamento : versamento.getSingoliVersamenti(configWrapper)) {
			// sv con tributo definito
			it.govpay.bd.model.Tributo tributo = singoloVersamento.getTributo(configWrapper);
			IbanAccredito ibanAccredito = singoloVersamento.getIbanAccredito(configWrapper);
			IbanAccredito ibanAppoggio = singoloVersamento.getIbanAppoggio(configWrapper);
			
			if(tributo != null) {
				IbanAccredito ibanAccreditoTributo = tributo.getIbanAccredito();
				IbanAccredito ibanAppoggioTributo = tributo.getIbanAppoggio();
				if(ibanAccreditoTributo != null) {
					if(!ibanAccreditoTributo.isPostale())
						return false;
				} else if(ibanAppoggioTributo != null) {
					if(!ibanAppoggioTributo.isPostale())
						return false;
				} else {
					// MBT
					return false;
				} 
			} else if(ibanAccredito != null) {
				if(!ibanAccredito.isPostale())
					return false;
			} else if(ibanAppoggio != null) {
				if(!ibanAppoggio.isPostale())
						return false;
			} else { // iban non definito per la voce 

			}
		}
		return true;
	}
	
	public static Dominio getDominioSingoloVersamento(SingoloVersamento singoloVersamento,Dominio dominio, BDConfigWrapper configWrapper) throws ServiceException {
		
		// appena trovo un singolo versamento con un id dominio definito sono in modalita' multibeneficiario
		if(singoloVersamento.getIdDominio() != null) {
			try {
				return AnagraficaManager.getDominio(configWrapper, singoloVersamento.getIdDominio());
			} catch (NotFoundException e) {
				// se passo qui ho fallito la validazione della pendenza !
				throw new ServiceException("Dominio ["+singoloVersamento.getIdDominio()+"] non censito in base dati.");
			}
		}
		return dominio;
	}
}
