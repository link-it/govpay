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
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.VersamentoAnnullatoException;
import it.govpay.core.exceptions.VersamentoDuplicatoException;
import it.govpay.core.exceptions.VersamentoScadutoException;
import it.govpay.core.exceptions.VersamentoSconosciutoException;
import it.govpay.core.rs.v1.beans.base.PendenzaPost;
import it.govpay.core.rs.v1.beans.base.Soggetto;
import it.govpay.core.rs.v1.beans.base.VocePendenza;
import it.govpay.core.rs.v1.beans.client.PendenzaVerificata;
import it.govpay.core.rs.v1.costanti.EsitoOperazione;
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
import it.govpay.servizi.commons.Anagrafica;
import it.govpay.servizi.commons.Versamento.SpezzoneCausaleStrutturata;

public class VersamentoUtils {
	
    public final static QName _VersamentoKeyCodApplicazione_QNAME = new QName("", "codApplicazione");
    public final static QName _VersamentoKeyCodVersamentoEnte_QNAME = new QName("", "codVersamentoEnte");
    public final static QName _VersamentoKeyCodDominio_QNAME = new QName("", "codDominio");
    public final static QName _VersamentoKeyCodUnivocoDebitore_QNAME = new QName("", "codUnivocoDebitore");
    public final static QName _VersamentoKeyBundlekey_QNAME = new QName("", "bundlekey");
    public final static QName _VersamentoKeyIuv_QNAME = new QName("", "iuv");

    public static void validazioneSemantica(Versamento versamento, boolean generaIuv, BasicBD bd) throws GovPayException, ServiceException {
		
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

	public static Versamento toVersamentoModel(it.govpay.servizi.commons.Versamento versamento, BasicBD bd) throws ServiceException, GovPayException {
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
		for(it.govpay.servizi.commons.Versamento.SingoloVersamento singoloVersamento : versamento.getSingoloVersamento()) {
			model.addSingoloVersamento(toSingoloVersamentoModel(model, singoloVersamento, index ++, bd));
		}
		
		return model;
	}
	
	public static SingoloVersamento toSingoloVersamentoModel(Versamento versamento, it.govpay.servizi.commons.Versamento.SingoloVersamento singoloVersamento, int index , BasicBD bd) throws ServiceException, GovPayException {
		SingoloVersamento model = new SingoloVersamento();
		model.setVersamento(versamento);
		model.setCodSingoloVersamentoEnte(singoloVersamento.getCodSingoloVersamentoEnte());
		model.setIndiceDati(index);
		model.setId(null);
		model.setIdVersamento(0);
		model.setImportoSingoloVersamento(singoloVersamento.getImporto());
		model.setStatoSingoloVersamento(StatoSingoloVersamento.NON_ESEGUITO);
		model.setDatiAllegati(singoloVersamento.getNote()); 
		if(singoloVersamento.getBolloTelematico() != null) {
			try {
				model.setTributo(Tributo.BOLLOT, bd);
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.TRB_000, versamento.getUo(bd).getDominio(bd).getCodDominio(), Tributo.BOLLOT);
			}
			model.setHashDocumento(singoloVersamento.getBolloTelematico().getHash());
			model.setProvinciaResidenza(singoloVersamento.getBolloTelematico().getProvincia());
			model.setTipoBollo(TipoBollo.toEnum(singoloVersamento.getBolloTelematico().getTipo()));
		} 
		
		List<Diritti> diritti = new ArrayList<>(); // TODO controllare quale diritto serve in questa fase
		diritti.add(Diritti.SCRITTURA);
		diritti.add(Diritti.ESECUZIONE);
		if(singoloVersamento.getCodTributo() != null) {
			try {
				model.setTributo(singoloVersamento.getCodTributo(), bd);
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.TRB_000, versamento.getUo(bd).getDominio(bd).getCodDominio(), singoloVersamento.getCodTributo());
			}
			
			if(!versamento.getApplicazione(bd).isTrusted() && !AclEngine.isAuthorized(versamento.getApplicazione(bd).getUtenza(), Servizio.PAGAMENTI_E_PENDENZE, versamento.getUo(bd).getDominio(bd).getCodDominio(), singoloVersamento.getCodTributo(),diritti)) {
				throw new GovPayException(EsitoOperazione.VER_022, versamento.getUo(bd).getDominio(bd).getCodDominio(), singoloVersamento.getCodTributo());
			}
		}
		
		if(singoloVersamento.getTributo() != null) {
			
			if(!versamento.getApplicazione(bd).isTrusted())
				throw new GovPayException(EsitoOperazione.VER_019);
			
			if(!AclEngine.isAuthorized(versamento.getApplicazione(bd).getUtenza(), Servizio.PAGAMENTI_E_PENDENZE, versamento.getUo(bd).getDominio(bd).getCodDominio(), null, diritti))
				throw new GovPayException(EsitoOperazione.VER_021);
			
			try {
				model.setIbanAccredito(AnagraficaManager.getIbanAccredito(bd, versamento.getUo(bd).getDominio(bd).getId(), singoloVersamento.getTributo().getIbanAccredito()));
				model.setTipoContabilita(TipoContabilita.valueOf(singoloVersamento.getTributo().getTipoContabilita().toString()));
				model.setCodContabilita(singoloVersamento.getTributo().getCodContabilita());
			} catch (NotFoundException e) {
				throw new GovPayException(EsitoOperazione.VER_020, versamento.getUo(bd).getDominio(bd).getCodDominio(), singoloVersamento.getTributo().getIbanAccredito());
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
	
	
	public static Versamento aggiornaVersamento(Versamento versamento, BasicBD bd) throws VersamentoScadutoException, VersamentoAnnullatoException, VersamentoDuplicatoException, VersamentoSconosciutoException, ServiceException, ClientException, GovPayException {
		// Se il versamento non e' in attesa, non aggiorno un bel niente
		if(!versamento.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO))
			return versamento;
		
		// Controllo se la data di scadenza e' indicata ed e' decorsa
		if(versamento.getDataScadenza() != null && versamento.getDataScadenza().before(new Date())) {
			throw new VersamentoScadutoException(versamento.getDataScadenza());
		}else {
			if(versamento.getDataValidita() != null && DateUtils.isDataScaduta(versamento.getDataValidita())) {
				GpContext ctx = GpThreadLocal.get();
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
				ctx.log("verifica.validita", versamento.getApplicazione(bd).getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
				
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
	
	
	public static Versamento acquisisciVersamento(Applicazione applicazione, String codVersamentoEnte, String bundlekey, String debitore, String dominio, String iuv, BasicBD bd) throws VersamentoScadutoException, VersamentoAnnullatoException, VersamentoDuplicatoException, VersamentoSconosciutoException, ServiceException, ClientException, GovPayException {
		
		String codVersamentoEnteD = codVersamentoEnte != null ? codVersamentoEnte : "-";
		String bundlekeyD = bundlekey != null ? bundlekey : "-";
		String debitoreD = debitore != null ? debitore : "-";
		String dominioD = dominio != null ? dominio : "-";
		String iuvD = iuv != null ? iuv : "-";
		
		GpContext ctx = GpThreadLocal.get();
		ctx.log("verifica.avvio", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
		if(applicazione.getConnettoreVerifica() == null) {
			ctx.log("verifica.nonConfigurata");
			throw new VersamentoSconosciutoException();
		}
		VerificaClient verificaClient = new VerificaClient(applicazione);
		Versamento versamento = null;
		try {
			versamento = verificaClient.invoke(codVersamentoEnte, bundlekey, debitore, dominio, iuv, bd);
			ctx.log("verifica.Ok", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
		} catch (ClientException e){
			ctx.log("verifica.Fail", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD, e.getMessage());
			throw e;
		} catch (VersamentoScadutoException e) {
			ctx.log("verifica.Scaduto", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
			throw e;
		} catch (VersamentoAnnullatoException e) {
			ctx.log("verifica.Annullato", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
			throw e;
		} catch (VersamentoDuplicatoException e) {
			ctx.log("verifica.Duplicato", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
			throw e;
		} catch (VersamentoSconosciutoException e) {
			ctx.log("verifica.Sconosciuto", applicazione.getCodApplicazione(), codVersamentoEnteD, bundlekeyD, debitoreD, dominioD, iuvD);
			throw e;
		} 
		
		it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
		versamentoBusiness.caricaVersamento(applicazione, versamento, versamento.getNumeroAvviso() == null, true);
		return versamento;
	}
	
	public static it.govpay.core.dao.commons.Versamento getVersamentoFromPendenza(PendenzaPost pendenza, String ida2a, String idPendenza) {
		it.govpay.core.dao.commons.Versamento versamento = new it.govpay.core.dao.commons.Versamento();

		if(pendenza.getAnnoRiferimento() != null)
			versamento.setAnnoTributario(pendenza.getAnnoRiferimento().intValue());

		versamento.setCausale(pendenza.getCausale());
		versamento.setCodApplicazione(ida2a);

		versamento.setCodDominio(pendenza.getIdDominio());
		versamento.setCodUnitaOperativa(pendenza.getIdUnitaOperativa());
		versamento.setCodVersamentoEnte(idPendenza);
		versamento.setDataScadenza(pendenza.getDataScadenza());
		versamento.setDataValidita(pendenza.getDataValidita());
		versamento.setDataCaricamento(pendenza.getDataCaricamento() != null ? pendenza.getDataCaricamento() : new Date());
		versamento.setDebitore(toAnagraficaCommons(pendenza.getSoggettoPagatore()));
		versamento.setImportoTotale(pendenza.getImporto());
		versamento.setTassonomia(pendenza.getTassonomia());
		if(pendenza.getTassonomiaAvviso() != null)
			versamento.setTassonomiaAvviso(pendenza.getTassonomiaAvviso().toString());
		versamento.setNumeroAvviso(pendenza.getNumeroAvviso());
		
//		versamento.setIncasso(pendenza.getIncasso()); //TODO
//		versamento.setAnomalie(pendenza.getAnomalie()); 

		// voci pagamento
		fillSingoliVersamentiFromVociPendenza(versamento, pendenza.getVoci());

		return versamento;
	}
	
	public static it.govpay.core.dao.commons.Versamento getVersamentoFromPendenzaVerificata(PendenzaVerificata pendenzaVerificata) {
		it.govpay.core.dao.commons.Versamento versamento = new it.govpay.core.dao.commons.Versamento();
		
		if(pendenzaVerificata.getAnnoRiferimento() != null)
			versamento.setAnnoTributario(pendenzaVerificata.getAnnoRiferimento().intValue());

		versamento.setCausale(pendenzaVerificata.getCausale());
		versamento.setCodApplicazione(pendenzaVerificata.getIdA2A());

		versamento.setCodDominio(pendenzaVerificata.getIdDominio());
		versamento.setCodUnitaOperativa(pendenzaVerificata.getIdUnitaOperativa());
		versamento.setCodVersamentoEnte(pendenzaVerificata.getIdPendenza());
		versamento.setDataScadenza(pendenzaVerificata.getDataScadenza());
		versamento.setDataValidita(pendenzaVerificata.getDataValidita());
		versamento.setDataCaricamento(pendenzaVerificata.getDataCaricamento() != null ? pendenzaVerificata.getDataCaricamento() : new Date());
		versamento.setDebitore(toAnagraficaCommons(pendenzaVerificata.getSoggettoPagatore()));
		versamento.setImportoTotale(pendenzaVerificata.getImporto());
	
		versamento.setDataCaricamento(pendenzaVerificata.getDataCaricamento()); 
		versamento.setCodVersamentoLotto(pendenzaVerificata.getCartellaPagamento());
		versamento.setDatiAllegati(pendenzaVerificata.getDatiAllegati());
		
		versamento.setTassonomia(pendenzaVerificata.getTassonomia());
		if(pendenzaVerificata.getTassonomiaAvviso() != null)
			versamento.setTassonomiaAvviso(pendenzaVerificata.getTassonomiaAvviso().toString());
		versamento.setNome(pendenzaVerificata.getNome());
		
		versamento.setStatoVersamento(StatoVersamento.NON_ESEGUITO);
		versamento.setNumeroAvviso(pendenzaVerificata.getNumeroAvviso());
//		versamento.setIuv(pendenzaVerificata.getNumeroAvviso());
		
//		versamento.setIncasso(pendenza.getIncasso()); //TODO
//		versamento.setAnomalie(pendenzaVerificata.getAnomalie()); 

		// voci pagamento
		fillSingoliVersamentiFromVociPendenza(versamento, pendenzaVerificata.getVoci());
		
		return versamento;
	}
	
	public static it.govpay.core.dao.commons.Versamento getVersamentoFromPendenza(PendenzaPost pendenza) {
		it.govpay.core.dao.commons.Versamento versamento = new it.govpay.core.dao.commons.Versamento();

		if(pendenza.getAnnoRiferimento() != null)
			versamento.setAnnoTributario(pendenza.getAnnoRiferimento().intValue());

		versamento.setCausale(pendenza.getCausale());
		versamento.setCodApplicazione(pendenza.getIdA2A());

		versamento.setCodDominio(pendenza.getIdDominio());
		versamento.setCodUnitaOperativa(pendenza.getIdUnitaOperativa());
		versamento.setCodVersamentoEnte(pendenza.getIdPendenza());
		versamento.setDataScadenza(pendenza.getDataScadenza());
		versamento.setDataValidita(pendenza.getDataValidita());
		versamento.setDataCaricamento(pendenza.getDataCaricamento() != null ? pendenza.getDataCaricamento() : new Date());
		versamento.setDebitore(toAnagraficaCommons(pendenza.getSoggettoPagatore()));
		versamento.setImportoTotale(pendenza.getImporto());
	
		versamento.setNome(pendenza.getNome());
		versamento.setTassonomia(pendenza.getTassonomia());
		if(pendenza.getTassonomiaAvviso() != null)
			versamento.setTassonomiaAvviso(pendenza.getTassonomiaAvviso().toString());
		
		versamento.setNumeroAvviso(pendenza.getNumeroAvviso());
//		if(pendenza.getNumeroAvviso()!=null)
//			versamento.setIuv(pendenza.getNumeroAvviso());
		
//		versamento.setIncasso(pendenza.getIncasso()); //TODO
//		versamento.setAnomalie(pendenza.getAnomalie()); 

		// voci pagamento
		fillSingoliVersamentiFromVociPendenza(versamento, pendenza.getVoci());

		return versamento;
		
		
	}

	public static void fillSingoliVersamentiFromVociPendenza(it.govpay.core.dao.commons.Versamento versamento, List<VocePendenza> voci) {

		if(voci != null && voci.size() > 0) {
			for (VocePendenza vocePendenza : voci) {
				it.govpay.core.dao.commons.Versamento.SingoloVersamento sv = new it.govpay.core.dao.commons.Versamento.SingoloVersamento();

				//sv.setCodTributo(value); ??

				sv.setCodSingoloVersamentoEnte(vocePendenza.getIdVocePendenza());
				sv.setDatiAllegati(vocePendenza.getDatiAllegati());
				sv.setDescrizione(vocePendenza.getDescrizione());
				sv.setImporto(vocePendenza.getImporto());

				// Definisce i dati di un bollo telematico
				if(vocePendenza.getHashDocumento() != null && vocePendenza.getTipoBollo() != null && vocePendenza.getProvinciaResidenza() != null) {
					it.govpay.core.dao.commons.Versamento.SingoloVersamento.BolloTelematico bollo = new it.govpay.core.dao.commons.Versamento.SingoloVersamento.BolloTelematico();
					bollo.setHash(vocePendenza.getHashDocumento());
					bollo.setProvincia(vocePendenza.getProvinciaResidenza());
					bollo.setTipo(vocePendenza.getTipoBollo());
					sv.setBolloTelematico(bollo);
				} else if(vocePendenza.getCodEntrata() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
					sv.setCodTributo(vocePendenza.getCodEntrata());

				} else { // Definisce i dettagli di incasso della singola entrata.
					it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo tributo = new it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo();
					tributo.setCodContabilita(vocePendenza.getCodiceContabilita());
					tributo.setIbanAccredito(vocePendenza.getIbanAccredito());
					tributo.setTipoContabilita(it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo.TipoContabilita.valueOf(vocePendenza.getTipoContabilita().name()));
					sv.setTributo(tributo);
				}

				versamento.getSingoloVersamento().add(sv);
			}
		}
	}
	
	public static it.govpay.core.dao.commons.Anagrafica toAnagraficaCommons(Soggetto anagraficaRest) {
		it.govpay.core.dao.commons.Anagrafica anagraficaCommons = null;
		if(anagraficaRest != null) {
			anagraficaCommons = new it.govpay.core.dao.commons.Anagrafica();
			anagraficaCommons.setCap(anagraficaRest.getCap());
			anagraficaCommons.setCellulare(anagraficaRest.getCellulare());
			anagraficaCommons.setCivico(anagraficaRest.getCivico());
			anagraficaCommons.setCodUnivoco(anagraficaRest.getIdentificativo());
			anagraficaCommons.setEmail(anagraficaRest.getEmail());
			anagraficaCommons.setIndirizzo(anagraficaRest.getIndirizzo());
			anagraficaCommons.setLocalita(anagraficaRest.getLocalita());
			anagraficaCommons.setNazione(anagraficaRest.getNazione());
			anagraficaCommons.setProvincia(anagraficaRest.getProvincia());
			anagraficaCommons.setRagioneSociale(anagraficaRest.getAnagrafica());
			anagraficaCommons.setTipo(anagraficaRest.getTipo().name());
		}

		return anagraficaCommons;
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
