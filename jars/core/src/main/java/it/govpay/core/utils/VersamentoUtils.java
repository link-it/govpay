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

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.beans.Anagrafica;
import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.beans.Versamento.SpezzoneCausaleStrutturata;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;

import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.service.context.IContext;
import it.govpay.core.utils.client.BasicClient.ClientException;
import it.govpay.core.utils.client.VerificaClient;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Anagrafica.TIPO;
import it.govpay.model.Dominio;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.SingoloVersamento.TipoBollo;
import it.govpay.model.Tributo;
import it.govpay.model.Tributo.TipoContabilita;
import it.govpay.model.Versamento.CausaleSemplice;
import it.govpay.model.Versamento.CausaleSpezzoni;
import it.govpay.model.Versamento.CausaleSpezzoniStrutturati;
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

	public static Versamento toVersamentoModel(it.govpay.core.beans.Versamento versamento, BasicBD bd) throws ServiceException, GovPayException {
		Versamento model = new Versamento();
		model.setAggiornabile(versamento.isAggiornabile() == null ? true : versamento.isAggiornabile());
		model.setAnagraficaDebitore(toAnagraficaModel(versamento.getDebitore()));
		
		if(versamento.getCausale() != null) {
			CausaleSemplice causale = model.new CausaleSemplice();
			causale.setCausale(versamento.getCausale());
			model.setCausaleVersamento(causale);
		}
		
		if(versamento.getSpezzoneCausale() != null && versamento.getSpezzoneCausale().size() > 0) {
			CausaleSpezzoni causale = model.new CausaleSpezzoni();
			causale.setSpezzoni(versamento.getSpezzoneCausale());
			model.setCausaleVersamento(causale);
		}
		
		if(versamento.getSpezzoneCausaleStrutturata() != null && versamento.getSpezzoneCausaleStrutturata().size() > 0) {
			CausaleSpezzoniStrutturati causale = model.new CausaleSpezzoniStrutturati();
			for(SpezzoneCausaleStrutturata s : versamento.getSpezzoneCausaleStrutturata())
				causale.addSpezzoneStrutturato(s.getCausale(), s.getImporto());
			model.setCausaleVersamento(causale);
		}
		
		model.setCodAnnoTributario(versamento.getAnnoTributario());
		model.setCodBundlekey(versamento.getBundlekey());
		model.setCodLotto(versamento.getCodDebito()); 
		model.setCodVersamentoEnte(versamento.getCodVersamentoEnte());
		model.setDataCreazione(new Date());
		model.setDataScadenza(versamento.getDataScadenza());
		model.setDataUltimoAggiornamento(new Date());
		model.setDescrizioneStato(null);
		model.setId(null);
		try {
			model.setApplicazione(versamento.getCodApplicazione(), bd);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.APP_000, versamento.getCodApplicazione());
		}
		
		Dominio dominio = null;
		try {
			dominio = AnagraficaManager.getDominio(bd, versamento.getCodDominio());
			model.setIdDominio(dominio.getId()); 
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.DOM_000, versamento.getCodDominio());
		}
		
		try {
			String codUnitaOperativa = (versamento.getCodUnitaOperativa() == null) ? Dominio.EC : versamento.getCodUnitaOperativa();
			model.setUo(dominio.getId(), codUnitaOperativa, bd);
		} catch (NotFoundException e) {
			throw new GovPayException(EsitoOperazione.UOP_000, versamento.getCodUnitaOperativa(), versamento.getCodDominio());
		}
		
		model.setImportoTotale(versamento.getImportoTotale());
		model.setStatoVersamento(StatoVersamento.NON_ESEGUITO);
		
		int index = 1;
		for(it.govpay.core.beans.Versamento.SingoloVersamento singoloVersamento : versamento.getSingoloVersamento()) {
			model.addSingoloVersamento(toSingoloVersamentoModel(model, singoloVersamento, index ++, bd));
		}
		
		return model;
	}
	
	public static SingoloVersamento toSingoloVersamentoModel(Versamento versamento, it.govpay.core.beans.Versamento.SingoloVersamento singoloVersamento, int index, BasicBD bd) throws ServiceException, GovPayException {
		SingoloVersamento model = new SingoloVersamento();
		model.setVersamento(versamento);
		model.setCodSingoloVersamentoEnte(singoloVersamento.getCodSingoloVersamentoEnte());
		model.setIndiceDati(index);
		model.setId(null);
		model.setIdVersamento(0);
		model.setImportoSingoloVersamento(singoloVersamento.getImporto());
		model.setStatoSingoloVersamento(StatoSingoloVersamento.NON_ESEGUITO);
		model.setDatiAllegati(singoloVersamento.getNote()); 
		it.govpay.bd.model.Dominio dominio = versamento.getUo(bd).getDominio(bd);
		if(singoloVersamento.getBolloTelematico() != null) {
			try {
				model.setTributo(Tributo.BOLLOT, bd);
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.TRB_000, dominio.getCodDominio(), Tributo.BOLLOT);
			}
			model.setHashDocumento(singoloVersamento.getBolloTelematico().getHash());
			model.setProvinciaResidenza(singoloVersamento.getBolloTelematico().getProvincia());
			model.setTipoBollo(TipoBollo.toEnum(singoloVersamento.getBolloTelematico().getTipo()));
		} 
		
		List<Diritti> diritti = new ArrayList<>(); // TODO controllare quale diritto serve in questa fase
		diritti.add(Diritti.SCRITTURA);
		diritti.add(Diritti.ESECUZIONE);
		Applicazione applicazione = versamento.getApplicazione(bd);
		if(singoloVersamento.getCodTributo() != null) {
			try {
				model.setTributo(singoloVersamento.getCodTributo(), bd);
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.TRB_000, dominio.getCodDominio(), singoloVersamento.getCodTributo());
			}
			
			if(!applicazione.isTrusted() && !AuthorizationManager.isAuthorized(applicazione.getUtenza(), applicazione.getUtenza().getTipoUtenza(), Servizio.PAGAMENTI_E_PENDENZE, dominio.getCodDominio(), singoloVersamento.getCodTributo(),diritti)) {
				throw new GovPayException(EsitoOperazione.VER_022, dominio.getCodDominio(), singoloVersamento.getCodTributo());
			}
		}
		
		if(singoloVersamento.getTributo() != null) {
			
			if(!applicazione.isTrusted())
				throw new GovPayException(EsitoOperazione.VER_019);
			
			if(!AuthorizationManager.isAuthorized(applicazione.getUtenza(), applicazione.getUtenza().getTipoUtenza(), Servizio.PAGAMENTI_E_PENDENZE, dominio.getCodDominio(), null, diritti))
				throw new GovPayException(EsitoOperazione.VER_021);
			
			try {
				model.setIbanAccredito(AnagraficaManager.getIbanAccredito(bd, dominio.getId(), singoloVersamento.getTributo().getIbanAccredito()));
				if(singoloVersamento.getTributo().getIbanAppoggio() != null)
					model.setIbanAppoggio(AnagraficaManager.getIbanAccredito(bd, dominio.getId(), singoloVersamento.getTributo().getIbanAppoggio()));
				model.setTipoContabilita(TipoContabilita.valueOf(singoloVersamento.getTributo().getTipoContabilita().toString()));
				model.setCodContabilita(singoloVersamento.getTributo().getCodContabilita());
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.VER_020, dominio.getCodDominio(), singoloVersamento.getTributo().getIbanAccredito());
			}
		}
		
		return model;
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
	
	public static it.govpay.model.Anagrafica toAnagraficaModel(Anagrafica anagrafica) {
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
	
	
	public static Versamento aggiornaVersamento(Versamento versamento, BasicBD bd) throws VersamentoScadutoException, VersamentoAnnullatoException, VersamentoDuplicatoException, VersamentoSconosciutoException, ServiceException, ClientException, GovPayException, UtilsException {
		// Se il versamento non e' in attesa, non aggiorno un bel niente
		if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO))
			return versamento;
		
		// Controllo se la data di scadenza e' indicata ed e' decorsa
		if(versamento.getDataScadenza() != null && DateUtils.isDataDecorsa(versamento.getDataScadenza())) {
			throw new VersamentoScadutoException(versamento.getDataScadenza());
		}else {
			if(versamento.getDataValidita() != null && DateUtils.isDataDecorsa(versamento.getDataValidita())) {
				IContext ctx = GpThreadLocal.get();
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
				
				if(versamento.getApplicazione(bd).getConnettoreVerifica() != null) {
					versamento = acquisisciVersamento(versamento.getApplicazione(bd), codVersamentoEnte, bundlekey, debitore, codDominio, iuv, bd);
				} else // connettore verifica non definito, versamento non aggiornabile
					throw new VersamentoScadutoException(versamento.getDataScadenza());
			} else {
				// versamento valido
			} 
		}
		return versamento;
	}
	
	
	public static Versamento acquisisciVersamento(Applicazione applicazione, String codVersamentoEnte, String bundlekey, String debitore, String dominio, String iuv, BasicBD bd) throws VersamentoScadutoException, VersamentoAnnullatoException, VersamentoDuplicatoException, VersamentoSconosciutoException, ServiceException, ClientException, GovPayException, UtilsException {
		
		String codVersamentoEnteD = codVersamentoEnte != null ? codVersamentoEnte : "-";
		String bundlekeyD = bundlekey != null ? bundlekey : "-";
		String debitoreD = debitore != null ? debitore : "-";
		String dominioD = dominio != null ? dominio : "-";
		String iuvD = iuv != null ? iuv : "-";
		
		IContext ctx = GpThreadLocal.get();
		ctx.getApplicationLogger().log("verifica.avvio", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
		if(applicazione.getConnettoreVerifica() == null) {
			ctx.getApplicationLogger().log("verifica.nonConfigurata");
			throw new VersamentoSconosciutoException();
		}
		VerificaClient verificaClient = new VerificaClient(applicazione);
		Versamento versamento = null;
		try {
			versamento = verificaClient.invoke(codVersamentoEnte, bundlekey, debitore, dominio, iuv, bd);
			ctx.getApplicationLogger().log("verifica.Ok", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
		} catch (ClientException e){
			ctx.getApplicationLogger().log("verifica.Fail", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD, e.getMessage());
			throw e;
		} catch (VersamentoScadutoException e) {
			ctx.getApplicationLogger().log("verifica.Scaduto", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
			throw e;
		} catch (VersamentoAnnullatoException e) {
			ctx.getApplicationLogger().log("verifica.Annullato", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
			throw e;
		} catch (VersamentoDuplicatoException e) {
			ctx.getApplicationLogger().log("verifica.Duplicato", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
			throw e;
		} catch (VersamentoSconosciutoException e) {
			ctx.getApplicationLogger().log("verifica.Sconosciuto", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
			throw e;
		} 
		
		it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
		versamentoBusiness.caricaVersamento(versamento, versamento.getNumeroAvviso() == null, true);
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
}
