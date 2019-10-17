package it.govpay.core.business.reportistica;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

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

	private static Logger log = LoggerWrapperFactory.getLogger(EntratePreviste.class);

	public byte [] getReportPdfEntratePreviste(List<EntrataPrevista> listaEntrate, Date dataDa, Date dataA) throws ServiceException{
		try {
			// 1. aggregazione dei risultati per codDominio
			Map<String, List<EntrataPrevista>> mapDomini = new HashMap<>();
			List<String> codDomini = new ArrayList<>();
			
			log.debug("Trovate " + listaEntrate.size() + " entrate previste");
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
			
			log.debug("Trovati " + mapDomini.size() + " raggruppamenti per dominio");
			
			ProspettoRiscossioniInput input = new ProspettoRiscossioniInput();
			DominiBD dominiBD = new DominiBD(this);
			Collections.sort(codDomini);
			
			ElencoProspettiDominio elencoProspettiDominio = new ElencoProspettiDominio();
			for (String codDominio :codDomini) {
				
				log.debug("Elaboro prospetto per il dominio " + codDominio);
				
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
		log.debug("Trovate " + listPerDomini.size() + " entrate previste per il dominio");
		
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
		
		log.debug("Trovati " + mapFlussi.size() + " raggruppamenti di entrate previste per il dominio");
		
		EntrataPrevista.IUVComparator iuvComparator = new EntrataPrevista(). new IUVComparator();
		List<String> codFlussi = new ArrayList<String>(mapFlussi.keySet());
		if(mapFlussi.containsKey(COD_FLUSSO_NULL)) {
			List<EntrataPrevista> listPerFlusso = mapFlussi.remove(COD_FLUSSO_NULL);
			log.debug("Elaborazione " + listPerFlusso.size() + " entrate singole per il dominio");
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
			log.debug("Nessuna entrata singola trovata per il dominio");
		}
		
		log.debug("Elaborazione " + codFlussi.size() + " entrate cumulative per il dominio");
		Collections.sort(codFlussi);
		
		ElencoFlussiRiscossioni elencoFlussiRiscossioni = new ElencoFlussiRiscossioni();
		prospRiscDominio.setElencoFlussiRiscossioni(elencoFlussiRiscossioni);
		for (String codFlusso : codFlussi) {
			log.debug("Elaborazione entrata cumulativa " + codFlusso);
			
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
				log.error("Entrata prevista mancante " + codFlusso);
			}
		}
		log.debug("Inserite " + elencoFlussiRiscossioni.getVoceFlussoRiscossioni().size() + " entrate cumulative per il dominio nell'elenco");
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
