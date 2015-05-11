/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.ndp.ejb;

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoliPagamenti;
import it.gov.digitpa.schemas._2011.pagamenti.CtFlussoRiversamento;
import it.govpay.ejb.controller.DistintaEJB;
import it.govpay.ejb.controller.PendenzaEJB;
import it.govpay.ejb.exception.GovPayException;
import it.govpay.ejb.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.model.DistintaModel;
import it.govpay.ejb.model.PagamentoModel;
import it.govpay.ejb.utils.DataTypeUtils;
import it.govpay.ndp.model.RendicontazioneModel;
import it.govpay.ndp.model.RendicontazionePagamentoModel;
import it.govpay.ndp.model.impl.FRModel;
import it.govpay.ndp.util.NdpUtils;
import it.govpay.orm.flussi.CasellarioInfo;
import it.govpay.orm.flussi.EsitiNdp;
import it.govpay.orm.flussi.Rendicontazioni;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.logging.log4j.Logger;

@Stateless
public class RendicontazioneEJB {

	@PersistenceContext(unitName = "GovPayJta")
	private EntityManager entityManager;
	
	@Inject
	DistintaEJB distintaEjb;
	
	
	@Inject
	PendenzaEJB pendenzaEJB;
	
	@Inject 
	private transient Logger log;

	
	public void addFlusso(FRModel flussoRendicontazone) throws GovPayException {

		CtFlussoRiversamento flussoRiversamento;
		try {
			flussoRiversamento = NdpUtils.toFR(flussoRendicontazone.getFlusso());
		} catch (Exception e) {
			log.error("Flusso di rendicontazione malformato: " + e);
			throw new GovPayException(GovPayExceptionEnum.ERRORE_VALIDAZIONE_NDP);
		}

		//  Inserimento del flusso nel casellario
		CasellarioInfo casell = new CasellarioInfo();
		casell.setFlussoCbi(flussoRendicontazone.getFlusso());
		casell.setTipoFlusso("FR");
		casell.setDataCreazione(flussoRendicontazone.getDataFlusso());
		casell.setMittente(flussoRiversamento.getIstitutoMittente().getIdentificativoUnivocoMittente().getCodiceIdentificativoUnivoco());
		casell.setRicevente(flussoRiversamento.getIstitutoRicevente().getIdentificativoUnivocoRicevente().getCodiceIdentificativoUnivoco());
		casell.setNumeroRecord(flussoRiversamento.getNumeroTotalePagamenti().intValue());
		casell.setDimensione(flussoRendicontazone.getBytes().length);
		casell.setNomeSupporto(flussoRiversamento.getIdentificativoFlusso());
		casell.setFlagElaborazione((short)1);					
		casell.setTsInserimento(new Timestamp(System.currentTimeMillis()));
		casell.setOpInserimento("RichiestaDettaglioNDP");
		
		// Setting di IdSupporto, ovvero della chiave univoca:
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
		StringBuilder sb = new StringBuilder();
		sb.append(casell.getTipoFlusso()).
			append(casell.getMittente()).
			append(sdf.format(casell.getDataCreazione())).
			append(casell.getNomeSupporto());
		casell.setIdSupporto(sb.toString());
		entityManager.persist(casell);
		// ---------------------------------------------------------
		// Inserimento del record nella tabella rendicontazioni
		// ---------------------------------------------------------
		
		XMLGregorianCalendar dataReg = flussoRiversamento.getDataRegolamento();

//		String codiceIdentificativoUnivocoMittente = flussoRiversamento.getIstitutoMittente().getIdentificativoUnivocoMittente().getCodiceIdentificativoUnivoco();									
			
		Rendicontazioni rct = new Rendicontazioni();
		rct.setCasellarioInfo(casell);
		rct.setCodRendicontazione("FR");
		rct.setDataRegolamento(DataTypeUtils.xmlGregorianCalendartoDate(dataReg));
		rct.setDivisa("EUR");
		rct.setDataRicezione(casell.getTsInserimento());
		rct.setUtenteCreatore("NDP");
		rct.setImporto(flussoRiversamento.getImportoTotalePagamenti());			
		if (flussoRiversamento.getDataOraFlusso()!=null) {		
			rct.setDataCreazione(new Timestamp(flussoRiversamento.getDataOraFlusso().toGregorianCalendar().getTimeInMillis()));
		} else {
			rct.setDataCreazione(new Timestamp(System.currentTimeMillis()));
		}				
		rct.setStato("DA_RICONCILIARE");
		rct.setNumeroEsiti(flussoRiversamento.getNumeroTotalePagamenti().intValue());
		rct.setTsInserimento(new Timestamp(System.currentTimeMillis()));
		rct.setOpInserimento("Esiti NDP Manager");
		rct.setIdFlusso(flussoRiversamento.getIdentificativoFlusso());
		rct.setIdRegolamento(flussoRiversamento.getIdentificativoUnivocoRegolamento());
		rct.setDataRegolamento(flussoRiversamento.getDataRegolamento().toGregorianCalendar().getTime());
		entityManager.persist(rct);
		
		BigDecimal totaleImportiPagati = BigDecimal.ZERO;
		// Acquisizione dati di rendicontazione
		for(CtDatiSingoliPagamenti dsp : flussoRiversamento.getDatiSingoliPagamenti()) {
			String iur = dsp.getIdentificativoUnivocoRiscossione();
			String iuv = dsp.getIdentificativoUnivocoVersamento();
			BigDecimal importoPagato = dsp.getSingoloImportoPagato();

			totaleImportiPagati = totaleImportiPagati.add(importoPagato);
			
			EsitiNdp esitoNdp = buildEsitoNdp(rct, dsp, iuv, iur);  												
			esitoNdp.setRendicontazioni(rct);
			esitoNdp.setFlagRiconciliazione((short) 0);
			esitoNdp.setImporto(importoPagato);
			
			String newErrore = null;
			String codAnomalia = null;
			
			DistintaModel distinta = distintaEjb.getDistinta(flussoRendicontazone.getIdDominio(), iuv); 
			if(distinta == null) {
				newErrore = "Impossibile recuperare la distinta EnteCreditore: [" + flussoRendicontazone.getIdDominio() + "] IUV ["+ iuv + "]";
				codAnomalia = EsitiNdp.COD_ANOMALIA_IUV_NON_TROVATO;
			} else {
				boolean found = false;
				
				for(PagamentoModel pagamento : distinta.getPagamenti()) {
					if(iur.equals(pagamento.getIdRiscossionePSP())) {
						
						found = true;
						//scrivo nella table esiti_ndp
						
						if(pagamento.getImportoPagato().compareTo(importoPagato) > 0) {
							newErrore = "L'importo rendicontato ["+importoPagato+"] non corrisponde con quello versato: [" + flussoRendicontazone.getIdDominio() + "] IUV ["+ iuv + "] IUR ["+iur+"]";
							codAnomalia = EsitiNdp.COD_ANOMALIA_IMPORTO_MINORE;
						}
						
						if(pagamento.getImportoPagato().compareTo(importoPagato) < 0) {
							newErrore = "L'importo rendicontato ["+importoPagato+"] non corrisponde con quello versato: [" + flussoRendicontazone.getIdDominio() + "] IUV ["+ iuv + "] IUR ["+iur+"]";
							codAnomalia = EsitiNdp.COD_ANOMALIA_IMPORTO_MAGGIORE;
						}
					}
				}
				
				if(!found) {
					newErrore = "Non e' stato trovato nessun pagamento nella distinta con lo IUR indicato: [" + flussoRendicontazone.getIdDominio() + "] IUV ["+ iuv + "] IUR ["+iur+"]";
					codAnomalia = EsitiNdp.COD_ANOMALIA_IUR_NON_TROVATO;
				}
			}
			
			if(newErrore!=null) {
				String errore = casell.getDescErrore()==null ? "" : casell.getDescErrore() + "\n";
				casell.setDescErrore(errore + newErrore);
				casell.setFlagElaborazione((short) 2);
				rct.setStato("ERRORE");
				rct.setFlagElaborazione((short) 2);
				esitoNdp.setCodAnomalia(codAnomalia);
				esitoNdp.setFlagRiconciliazione((short) 2);
			}
			
			entityManager.persist(esitoNdp);
			
			
		}
		
		if(totaleImportiPagati.compareTo(flussoRiversamento.getImportoTotalePagamenti()) != 0){
			String errore = casell.getDescErrore()==null ? "" : casell.getDescErrore() + "\n";
			casell.setDescErrore(errore + "La somma degli importi rendicontati nei singoli pagamenti ["+totaleImportiPagati+"] non corrisponde al totale indicato nel flusso");
			casell.setFlagElaborazione((short) 2);
			rct.setStato("ERRORE");
			rct.setFlagElaborazione((short) 2);
			log.error("La somma degli importi rendicontati nei singoli pagamenti ["+totaleImportiPagati+"] non corrisponde al totale indicato nel flusso");
		}
	}
	
	public boolean existFlusso(String idDominio, String idPsp, String idFlusso) {
		TypedQuery<CasellarioInfo> queryDominio = entityManager.createQuery(
		  "select c from CasellarioInfo c "
		+ "where c.nomeSupporto = :nomeSupporto "
		+ "and c.tipoFlusso = 'FR' "
		+ "and c.mittente = :mittente "
		+ "and c.ricevente = :ricevente ", CasellarioInfo.class);
		queryDominio.setParameter("nomeSupporto", idFlusso);
		queryDominio.setParameter("mittente", idPsp);
		queryDominio.setParameter("ricevente", idDominio);

		CasellarioInfo casellario;
		try {
			casellario = queryDominio.getSingleResult();
		} catch (NoResultException e) {
			return false;
		}
		return casellario != null;
	}
	
	public RendicontazioneModel getRendicontazione(String idDominio, String idPsp, String idFlusso) {
		
		RendicontazioneModel rendicontazione = new RendicontazioneModel();
		
		TypedQuery<CasellarioInfo> queryDominio = entityManager.createQuery(
		  "select c from CasellarioInfo c "
		+ "where c.nomeSupporto = :nomeSupporto "
		+ "and c.tipoFlusso = 'FR' "
		+ "and c.mittente = :mittente "
		+ "and c.ricevente = :ricevente ", CasellarioInfo.class);
		queryDominio.setParameter("nomeSupporto", idFlusso);
		queryDominio.setParameter("mittente", idPsp);
		queryDominio.setParameter("ricevente", idDominio);

		CasellarioInfo casellario;
		try {
			casellario = queryDominio.getSingleResult();
			
			// Recupero le informazioni dal casellario e le inserisco 
			rendicontazione.setDataRendicontazione(casellario.getDataCreazione());
			rendicontazione.setDescErrore(casellario.getDescErrore());
			rendicontazione.setValido(casellario.getFlagElaborazione()==1);
			rendicontazione.setFlusso(casellario.getFlussoCbi());
			rendicontazione.setMittente(casellario.getMittente());
			rendicontazione.setRicevente(casellario.getRicevente());
			
			// Recupero la rendicontazione associata  
			
			TypedQuery<Rendicontazioni> queryRendicontazione = entityManager.createQuery(
					  "select r from Rendicontazioni r "
					+ "where r.casellarioInfo = :casellario ", Rendicontazioni.class);
			queryRendicontazione.setParameter("casellario", casellario);
			
			Rendicontazioni rct = queryRendicontazione.getSingleResult();
			rendicontazione.setDataRegolamento(rct.getDataRegolamento());
			rendicontazione.setStato(rct.getStato());
			rendicontazione.setIdRegolamento(rct.getIdRegolamento());
			rendicontazione.setImportoRendicontato(rct.getImporto());
			
			List<RendicontazionePagamentoModel> rendicontazioniPagamento = new ArrayList<RendicontazionePagamentoModel>();
			rendicontazione.setRendicontazioniPagamento(rendicontazioniPagamento);
			
			// Recupero tutti gli esiti associati alla rendicontazione.
			Set<EsitiNdp> esitiNdp = rct.getEsitiNdps();
			for(EsitiNdp esitoNdp : esitiNdp) {
				RendicontazionePagamentoModel rendicontazionePagamento = new RendicontazionePagamentoModel();
				
				rendicontazionePagamento.setImportoPagato(esitoNdp.getImporto());			
				rendicontazionePagamento.setIdentificativoUnivocoVersamento(esitoNdp.getIdRiconciliazione());
				rendicontazionePagamento.setIdentificativoUnivocoRiscossione(esitoNdp.getIdRiscossione());
				rendicontazionePagamento.setDataEsitoPagamento(esitoNdp.getDataPagamento());
				rendicontazionePagamento.setEsitoPagamento(esitoNdp.getEsitoPagamento());
				rendicontazionePagamento.setRiconciliato(esitoNdp.getFlagRiconciliazione() == 2);
				rendicontazionePagamento.setCodAnomalia(esitoNdp.getCodAnomalia());
				
				if(esitoNdp.getCodAnomalia()==null) {
					DistintaModel distinta = distintaEjb.getDistinta(idDominio, esitoNdp.getIdRiconciliazione()); 
					for(PagamentoModel pagamento : distinta.getPagamenti()) {
						if(esitoNdp.getIdRiscossione().equals(pagamento.getIdRiscossionePSP())) {
							rendicontazionePagamento.setPagamento(pagamento);
						}
					}
				}
				
				rendicontazioniPagamento.add(rendicontazionePagamento);
			}
		} catch (NoResultException e) {
			return null;
		}
		return rendicontazione;
	}
	
	/**
	 * 
	 * @param rct
	 * @param ctDatiSingoliPagamenti
	 * @param IUV
	 * @param IUR
	 * @return
	 */
	private EsitiNdp buildEsitoNdp(Rendicontazioni rct,
			CtDatiSingoliPagamenti ctDatiSingoliPagamenti, String IUV,
			String IUR) {
		EsitiNdp esitoNdp = new EsitiNdp();
		esitoNdp.setImporto(ctDatiSingoliPagamenti.getSingoloImportoPagato());			
		esitoNdp.setIdRiconciliazione(IUV);
		esitoNdp.setIdRiscossione(IUR);
		esitoNdp.setDataPagamento(ctDatiSingoliPagamenti.getDataEsitoSingoloPagamento().toGregorianCalendar().getTime());
		rct.setOpInserimento("Esiti Ndp Manager");
		rct.setTsInserimento(new Timestamp(System.currentTimeMillis()));					
		
		String esito = ctDatiSingoliPagamenti.getCodiceEsitoSingoloPagamento();
		if ("0".equals(esito)) {
			esitoNdp.setEsitoPagamento("ESEGUITO");
			esitoNdp.setSegno("C");
		} else if ("3".equals(esito)) {
			esitoNdp.setEsitoPagamento("REVOCATO");
			esitoNdp.setSegno("D");
		}
		return esitoNdp;
	}
}
