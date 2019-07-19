package it.govpay.core.business.reportistica;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.viste.model.EntrataPrevista;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Anagrafica;
import it.govpay.stampe.model.ProspettoRiscossioneDominioInput;
import it.govpay.stampe.model.ProspettoRiscossioneDominioInput.ElencoFlussiRiscossioni;
import it.govpay.stampe.model.ProspettoRiscossioneDominioInput.ElencoRiscossioni;
import it.govpay.stampe.model.ProspettoRiscossioniInput;
import it.govpay.stampe.model.ProspettoRiscossioniInput.ElencoProspettiDominio;
import it.govpay.stampe.model.RiscossioneConFlussoInput;
import it.govpay.stampe.model.VoceRiscossioneInput;
import it.govpay.stampe.pdf.avvisoPagamento.AvvisoPagamentoCostanti;
import it.govpay.stampe.pdf.prospettoRiscossioni.ProspettoRiscossioniPdf;
import it.govpay.stampe.pdf.prospettoRiscossioni.utils.ProspettoRiscossioniProperties;

public class EntratePreviste extends BasicBD{
	
	public static final String COD_FLUSSO_NULL = "_gp_cod_flusso_null";
	private SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");

	public EntratePreviste(BasicBD basicBD) {
		super(basicBD);
	}

	public byte [] getReportPdfEntratePreviste(List<EntrataPrevista> listaEntrate, Date dataDa, Date dataA) throws ServiceException{
		try {
			// 1. aggregazione dei risultati per codDominio
			Map<String, List<EntrataPrevista>> mapDomini = new HashMap<>();
			List<String> codDomini = new ArrayList<>();
			
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
			
			ProspettoRiscossioniInput input = new ProspettoRiscossioniInput();
			DominiBD dominiBD = new DominiBD(this);
			Collections.sort(codDomini);
			
			for (String codDominio :codDomini) {
				ProspettoRiscossioneDominioInput prospRiscDominio = new ProspettoRiscossioneDominioInput();
				if(dataA != null)
					prospRiscDominio.setDataA(this.sdfData.format(dataA));
				if(dataDa != null)
				prospRiscDominio.setDataDa(this.sdfData.format(dataDa));
				
				Dominio dominio = this.impostaAnagraficaEnteCreditore(dominiBD, codDominio, prospRiscDominio);
				
				List<EntrataPrevista> listPerDomini = mapDomini.get(codDominio);
				
				this.popolaProspettoDominio(dominio, listPerDomini, prospRiscDominio);				
				
				ElencoProspettiDominio elencoProspettiDominio = new ElencoProspettiDominio();
				elencoProspettiDominio.getProspettoDominio().add(prospRiscDominio);
				
				input.setElencoProspettiDominio(elencoProspettiDominio);
			}
			
			ProspettoRiscossioniProperties prospettoRiscossioniProperties = ProspettoRiscossioniProperties.getInstance();
			
			InputStream isTemplate = null; 
			
			if(GovpayConfig.getInstance().getTemplateProspettoRiscossioni() != null) {
				File resourceDirFile = new File(GovpayConfig.escape(GovpayConfig.getInstance().getTemplateProspettoRiscossioni()));
				if(resourceDirFile.exists()) {
					isTemplate = new FileInputStream(resourceDirFile);
				}
			}
			
			return ProspettoRiscossioniPdf.getInstance().creaProspettoRiscossioni(log, input, prospettoRiscossioniProperties, isTemplate);
		}catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	private void popolaProspettoDominio (Dominio dominio, List<EntrataPrevista> listPerDomini, ProspettoRiscossioneDominioInput prospRiscDominio) {
		// 1. aggregazione dei risultati per codFlusso
		Map<String, List<EntrataPrevista>> mapFlussi = new HashMap<>();
		List<String> codFlussi = new ArrayList<>();
		
		for (EntrataPrevista entrataPrevista : listPerDomini) {
			
			List<EntrataPrevista> listPerFlusso = null;
			String keyFlusso = entrataPrevista.getCodFlusso() != null ? entrataPrevista.getCodFlusso() : COD_FLUSSO_NULL; 
			
			if(mapFlussi.containsKey(keyFlusso)) {
				listPerFlusso = mapFlussi.remove(keyFlusso);
			} else {
				codFlussi.add(keyFlusso);
				listPerFlusso = new ArrayList<>();
			}
			
			listPerFlusso.add(entrataPrevista);
			mapFlussi.put(keyFlusso, listPerFlusso);
		}
		
		EntrataPrevista.IUVComparator iuvComparator = new EntrataPrevista(). new IUVComparator();
		// lista delle riscossioni fuori flusso
		if(mapFlussi.containsKey(COD_FLUSSO_NULL)) {
			List<EntrataPrevista> listPerFlusso = mapFlussi.remove(COD_FLUSSO_NULL);
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
		}
		
		Collections.sort(codFlussi);
		
		for (String codFlusso : codFlussi) {
			List<EntrataPrevista> listPerFlusso = mapFlussi.get(codFlusso);
			
			if(listPerFlusso != null) {
				Collections.sort(listPerFlusso, iuvComparator); 
				
				RiscossioneConFlussoInput riscossioneConFlusso = new RiscossioneConFlussoInput();
				ElencoFlussiRiscossioni elencoFlussiRiscossioni = new ElencoFlussiRiscossioni();
				
				prospRiscDominio.setElencoFlussiRiscossioni(elencoFlussiRiscossioni);
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
			}
		}
	}
	
	private it.govpay.bd.model.Dominio impostaAnagraficaEnteCreditore(DominiBD dominiBD, String codDominio, ProspettoRiscossioneDominioInput input) throws Exception {
		it.govpay.bd.model.Dominio dominio = dominiBD.getDominio(codDominio); 

		input.setEnteDenominazione(dominio.getRagioneSociale());
		input.setCfEnte(codDominio);
		// se e' presente un logo lo inserisco altrimemti verra' caricato il logo di default.
		if(dominio.getLogo() != null && dominio.getLogo().length > 0)
			input.setLogoEnte(new String(dominio.getLogo()));

		this.impostaIndirizzoEnteCreditore(dominio, input);
		return dominio;
	}
	
	private void impostaIndirizzoEnteCreditore(it.govpay.bd.model.Dominio dominio, ProspettoRiscossioneDominioInput input) throws ServiceException {
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

			if(indirizzoEnte.length() > AvvisoPagamentoCostanti.AVVISO_LUNGHEZZA_CAMPO_INDIRIZZO_DESTINATARIO) {
				input.setIndirizzoEnte(indirizzoEnte);
			}else {
				input.setIndirizzoEnte(indirizzoEnte);
			}

			if(capCitta.length() > AvvisoPagamentoCostanti.AVVISO_LUNGHEZZA_CAMPO_INDIRIZZO_DESTINATARIO) {
				input.setLuogoEnte(capCitta);
			}else {
				input.setLuogoEnte(capCitta);
			}
		}
	}
}
