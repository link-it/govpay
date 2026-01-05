/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.core.business.reportistica;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.viste.model.EntrataPrevista;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.LogUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Anagrafica;
import it.govpay.stampe.model.ProspettoRiscossioneDominioInput;
import it.govpay.stampe.model.ProspettoRiscossioneDominioInput.ElencoFlussiRiscossioni;
import it.govpay.stampe.model.ProspettoRiscossioneDominioInput.ElencoRiscossioni;
import it.govpay.stampe.model.ProspettoRiscossioniInput;
import it.govpay.stampe.model.ProspettoRiscossioniInput.ElencoProspettiDominio;
import it.govpay.stampe.model.RiscossioneConFlussoInput;
import it.govpay.stampe.model.VoceRiscossioneInput;
import it.govpay.stampe.pdf.prospettoRiscossioni.ProspettoRiscossioniPdf;
import it.govpay.stampe.pdf.prospettoRiscossioni.utils.ProspettoRiscossioniProperties;

public class EntratePreviste {
	
	public static final String COD_FLUSSO_NULL = "_gp_cod_flusso_null";
	private SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");

	public EntratePreviste() {
		//donothing
	}

	private static Logger log = LoggerWrapperFactory.getLogger(EntratePreviste.class);

	public byte [] getReportPdfEntratePreviste(List<EntrataPrevista> listaEntrate, Date dataDa, Date dataA) throws ServiceException{
		try {
			// 1. aggregazione dei risultati per codDominio
			Map<String, List<EntrataPrevista>> mapDomini = new HashMap<>();
			List<String> codDomini = new ArrayList<>();
			
			LogUtils.logDebug(log, "Trovate " + listaEntrate.size() + " entrate previste");
			for (EntrataPrevista entrataPrevista : listaEntrate) {
				List<EntrataPrevista> listPerDomini = null;
				
				if(mapDomini.containsKey(entrataPrevista.getCodDominio())) {
					listPerDomini = mapDomini.remove(entrataPrevista.getCodDominio());
				} else {
					codDomini.add(entrataPrevista.getCodDominio());
					listPerDomini = new ArrayList<>();
				}
				
				listPerDomini.add(entrataPrevista);
				mapDomini.put(entrataPrevista.getCodDominio(), listPerDomini);
			}
			
			LogUtils.logDebug(log, "Trovati " + mapDomini.size() + " raggruppamenti per dominio");
			
			ProspettoRiscossioniInput input = new ProspettoRiscossioniInput();
			BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
			DominiBD dominiBD = new DominiBD(configWrapper);
			Collections.sort(codDomini);
			
			ElencoProspettiDominio elencoProspettiDominio = new ElencoProspettiDominio();
			for (String codDominio :codDomini) {
				
				LogUtils.logDebug(log, "Elaboro prospetto per il dominio " + codDominio);
				
				ProspettoRiscossioneDominioInput prospRiscDominio = new ProspettoRiscossioneDominioInput();
				if(dataA != null)
					prospRiscDominio.setDataA(this.sdfData.format(dataA));
				else
					prospRiscDominio.setDataA(this.sdfData.format(new Date()));
				if(dataDa != null)
					prospRiscDominio.setDataDa(this.sdfData.format(dataDa));
				else
					prospRiscDominio.setDataDa("01/01/2015");
				Dominio dominio = this.impostaAnagraficaEnteCreditore(dominiBD, codDominio, prospRiscDominio);
				List<EntrataPrevista> listPerDomini = mapDomini.get(codDominio);
				this.popolaProspettoDominio(dominio, listPerDomini, prospRiscDominio);				
				elencoProspettiDominio.getProspettoDominio().add(prospRiscDominio);
			}
			input.setElencoProspettiDominio(elencoProspettiDominio);
			ProspettoRiscossioniProperties prospettoRiscossioniProperties = ProspettoRiscossioniProperties.getInstance();
			
			File jasperFile = null; 
			if(GovpayConfig.getInstance().getTemplateProspettoRiscossioni() != null) {
				jasperFile = new File(GovpayConfig.escape(GovpayConfig.getInstance().getTemplateProspettoRiscossioni()));
			}
			
			return ProspettoRiscossioniPdf.getInstance().creaProspettoRiscossioni(log, input, prospettoRiscossioniProperties, jasperFile);
		}catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	private void popolaProspettoDominio (Dominio dominio, List<EntrataPrevista> listPerDomini, ProspettoRiscossioneDominioInput prospRiscDominio) {
		LogUtils.logDebug(log, "Trovate " + listPerDomini.size() + " entrate previste per il dominio "+ dominio.getCodDominio());
		
		Map<String, List<EntrataPrevista>> mapFlussi = new HashMap<>();
		for (EntrataPrevista entrataPrevista : listPerDomini) {
			List<EntrataPrevista> listPerFlusso = null;
			String keyFlusso = entrataPrevista.getCodFlusso() != null ? entrataPrevista.getCodFlusso() : COD_FLUSSO_NULL; 
			if(mapFlussi.containsKey(keyFlusso)) {
				listPerFlusso = mapFlussi.get(keyFlusso);
			} else {
				mapFlussi.put(keyFlusso, new ArrayList<>());
				listPerFlusso = mapFlussi.get(keyFlusso);
			}
			listPerFlusso.add(entrataPrevista);
		}
		
		LogUtils.logDebug(log, "Trovati " + mapFlussi.size() + " raggruppamenti di entrate previste per il dominio");
		
		EntrataPrevista.IUVComparator iuvComparator = new EntrataPrevista(). new IUVComparator();
		List<String> codFlussi = new ArrayList<>(mapFlussi.keySet());
		if(mapFlussi.containsKey(COD_FLUSSO_NULL)) {
			List<EntrataPrevista> listPerFlusso = mapFlussi.remove(COD_FLUSSO_NULL);
			LogUtils.logDebug(log, "Elaborazione " + listPerFlusso.size() + " entrate singole per il dominio");
			codFlussi.remove(COD_FLUSSO_NULL);
			Collections.sort(listPerFlusso, iuvComparator); 
			ElencoRiscossioni elencoRiscossioni = new ElencoRiscossioni();
			for (EntrataPrevista entrataPrevista : listPerFlusso) {
				VoceRiscossioneInput voceRiscossione = new VoceRiscossioneInput();
				voceRiscossione.setData(entrataPrevista.getDataPagamento() != null ? SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(entrataPrevista.getDataPagamento()) : "");
				voceRiscossione.setIdPendenza(entrataPrevista.getCodVersamentoEnte());
				voceRiscossione.setImporto(entrataPrevista.getImportoPagato() != null ? entrataPrevista.getImportoPagato().doubleValue() : 0.0);
				voceRiscossione.setIuv(entrataPrevista.getIuv());
				voceRiscossione.setAnno(entrataPrevista.getAnno());
				voceRiscossione.setDataPagamento(entrataPrevista.getDataPagamento() != null ? SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(entrataPrevista.getDataPagamento()) : "");
				voceRiscossione.setIdentificativoDebitore(entrataPrevista.getIdentificativoDebitore());
				voceRiscossione.setIdEntrata(entrataPrevista.getCodEntrata());
				voceRiscossione.setIdTipoPendenza(entrataPrevista.getCodTipoVersamento());
				elencoRiscossioni.getVoceRiscossione().add(voceRiscossione );
			}
			prospRiscDominio.setElencoRiscossioni(elencoRiscossioni);
		} else {
			LogUtils.logDebug(log, "Nessuna entrata singola trovata per il dominio");
		}
		
		LogUtils.logDebug(log, "Elaborazione " + codFlussi.size() + " entrate cumulative per il dominio");
		Collections.sort(codFlussi);
		
		ElencoFlussiRiscossioni elencoFlussiRiscossioni = new ElencoFlussiRiscossioni();
		prospRiscDominio.setElencoFlussiRiscossioni(elencoFlussiRiscossioni);
		for (String codFlusso : codFlussi) {
			LogUtils.logDebug(log, "Elaborazione entrata cumulativa " + codFlusso);
			
			List<EntrataPrevista> listPerFlusso = mapFlussi.get(codFlusso);
			
			if(listPerFlusso != null) {
				Collections.sort(listPerFlusso, iuvComparator); 
				
				RiscossioneConFlussoInput riscossioneConFlusso = new RiscossioneConFlussoInput();
				
				elencoFlussiRiscossioni.getVoceFlussoRiscossioni().add(riscossioneConFlusso);
				
				EntrataPrevista eMetadata = null;
				it.govpay.stampe.model.RiscossioneConFlussoInput.ElencoRiscossioni elencoRiscossioni = new it.govpay.stampe.model.RiscossioneConFlussoInput.ElencoRiscossioni();
				
				for (EntrataPrevista entrataPrevista : listPerFlusso) {
					if(eMetadata == null)
						eMetadata = entrataPrevista;
					
					VoceRiscossioneInput voceRiscossione = new VoceRiscossioneInput();
					
					voceRiscossione.setData(entrataPrevista.getDataPagamento() != null ? SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(entrataPrevista.getDataPagamento()) : "");
					voceRiscossione.setIdPendenza(entrataPrevista.getCodVersamentoEnte());
					voceRiscossione.setImporto(entrataPrevista.getImportoPagato() != null ? entrataPrevista.getImportoPagato().doubleValue() : 0.0);
					voceRiscossione.setIuv(entrataPrevista.getIuv());
					voceRiscossione.setAnno(entrataPrevista.getAnno());
					voceRiscossione.setDataPagamento(entrataPrevista.getDataPagamento() != null ? SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(entrataPrevista.getDataPagamento()) : "");
					voceRiscossione.setIdentificativoDebitore(entrataPrevista.getIdentificativoDebitore());
					voceRiscossione.setIdEntrata(entrataPrevista.getCodEntrata());
					voceRiscossione.setIdTipoPendenza(entrataPrevista.getCodTipoVersamento());
					
					elencoRiscossioni.getVoceRiscossione().add(voceRiscossione);
				}
				
				riscossioneConFlusso.setIdFlusso(codFlusso);
				if(eMetadata != null) {
					riscossioneConFlusso.setData(SimpleDateFormatUtils.newSimpleDateFormatSoloData().format(eMetadata.getDataRegolamento()));
					riscossioneConFlusso.setImporto(eMetadata.getImportoTotalePagamenti() != null ? eMetadata.getImportoTotalePagamenti().doubleValue() : 0.0);
					riscossioneConFlusso.setIur(eMetadata.getFrIur());
				}
				
				riscossioneConFlusso.setElencoRiscossioni(elencoRiscossioni);
			} else {
				LogUtils.logWarn(log, "Entrata prevista mancante " + codFlusso);
			}
		}
		LogUtils.logDebug(log, "Inserite " + elencoFlussiRiscossioni.getVoceFlussoRiscossioni().size() + " entrate cumulative per il dominio nell'elenco");
	}
	
	private it.govpay.bd.model.Dominio impostaAnagraficaEnteCreditore(DominiBD dominiBD, String codDominio, ProspettoRiscossioneDominioInput input) throws NotFoundException, ServiceException {
		it.govpay.bd.model.Dominio dominio = dominiBD.getDominio(codDominio); 

		input.setEnteDenominazione(dominio.getRagioneSociale());
		input.setCfEnte(codDominio);
		// se e' presente un logo lo inserisco altrimemti verra' caricato il logo di default.
		if(dominio.getLogo() != null && dominio.getLogo().length > 0)
			input.setLogoEnte(new String(dominio.getLogo()));

		this.impostaIndirizzoEnteCreditore(dominio, input);
		return dominio;
	}
	
	private void impostaIndirizzoEnteCreditore(it.govpay.bd.model.Dominio dominio, ProspettoRiscossioneDominioInput input) {
		Anagrafica anagraficaEnteCreditore = dominio.getAnagrafica();
		if(anagraficaEnteCreditore != null) {
			String indirizzo = StringUtils.isNotEmpty(anagraficaEnteCreditore.getIndirizzo()) ? anagraficaEnteCreditore.getIndirizzo() : "";
			String civico = StringUtils.isNotEmpty(anagraficaEnteCreditore.getCivico()) ? anagraficaEnteCreditore.getCivico() : "";
			String cap = StringUtils.isNotEmpty(anagraficaEnteCreditore.getCap()) ? anagraficaEnteCreditore.getCap() : "";
			String localita = StringUtils.isNotEmpty(anagraficaEnteCreditore.getLocalita()) ? anagraficaEnteCreditore.getLocalita() : "";
			String provincia = StringUtils.isNotEmpty(anagraficaEnteCreditore.getProvincia()) ? (" (" +anagraficaEnteCreditore.getProvincia() +")" ) : "";
			String indirizzoCivico = indirizzo + " " + civico;
			String capCitta = cap + " " + localita + provincia;

			String indirizzoEnte = indirizzoCivico + ",";

			input.setIndirizzoEnte(indirizzoEnte);

			input.setLuogoEnte(capCitta);
		}
	}
}
