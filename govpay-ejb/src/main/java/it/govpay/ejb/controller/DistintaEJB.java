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
package it.govpay.ejb.controller;

import it.govpay.ejb.builder.DistintaBuilder;
import it.govpay.ejb.exception.GovPayException;
import it.govpay.ejb.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.filter.DistintaFilter;
import it.govpay.ejb.model.DistintaModel;
import it.govpay.ejb.model.DistintaModel.EnumStatoDistinta;
import it.govpay.ejb.model.EnteCreditoreModel;
import it.govpay.ejb.model.EsitoPagamentoDistinta;
import it.govpay.ejb.model.EsitoPagamentoDistinta.EsitoSingoloPagamento;
import it.govpay.ejb.model.EsitoRevocaDistinta;
import it.govpay.ejb.model.PagamentoModel;
import it.govpay.ejb.model.PagamentoModel.EnumStatoPagamento;
import it.govpay.ejb.model.PagamentoModel.EnumTipoPagamento;
import it.govpay.ejb.model.PendenzaModel;
import it.govpay.ejb.model.TrackingOperazioniPagamentoModel;
import it.govpay.ejb.model.TrackingOperazioniPagamentoModel.EnumTipoTrackingOperazione;
import it.govpay.ejb.model.TributoModel;
import it.govpay.ejb.model.TributoModel.EnumStatoTributo;
import it.govpay.ejb.model.VersanteModel;
import it.govpay.ejb.utils.EnumUtils;
import it.govpay.ejb.utils.GovPayConstants;
import it.govpay.ejb.utils.IdUtils;
import it.govpay.ejb.utils.IuvUtils;
import it.govpay.ejb.utils.QueryUtils;
import it.govpay.ejb.utils.rs.EjbUtils;
import it.govpay.orm.configurazione.CfgGatewayPagamento;
import it.govpay.orm.pagamenti.DatiAnagraficiVersante;
import it.govpay.orm.pagamenti.DistintaPagamento;
import it.govpay.orm.pagamenti.PagamentiOnline;
import it.govpay.orm.pagamenti.Pagamento;
import it.govpay.orm.posizionedebitoria.Condizione;
import it.govpay.orm.posizionedebitoria.DestinatarioPendenza;
import it.govpay.orm.posizionedebitoria.Pendenza;
import it.govpay.orm.utils.Iuv;
import it.govpay.rs.DatiSingoloVersamento;
import it.govpay.rs.DatiVersamento;
import it.govpay.rs.RichiestaPagamento;
import it.govpay.rs.Soggetto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.Logger;

@Stateless
public class DistintaEJB {

	@PersistenceContext(unitName = "GovPayJta")
	private EntityManager entityManager;

	@Inject
	PendenzaEJB pendenzaEjb;
	
	@Inject
	AnagraficaEJB anagraficaEjb;
	
	@Inject
	private Logger log;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Map<DistintaModel, List<PendenzaModel>> creaDistinta(RichiestaPagamento richiestaPagamento, String ccp) throws GovPayException {
		
		// 1. Verifica che gli IUV utilizzati siano disponibili
		// Lo devo fare qua e non in validazione perche deve essere transazionale.
		log.debug("Verifica dell'univocita' degli IUV");
		List<String> iuvs = new ArrayList<String>();
		for(it.govpay.rs.Pagamento pagamento : richiestaPagamento.getPagamentis()){
			DatiVersamento datiVersamento = pagamento.getDatiVersamento();
			if(iuvs.contains(datiVersamento.getIuv())) {
				throw new GovPayException(GovPayExceptionEnum.IUV_DUPLICATO, "La richiesta contiene due versamenti con lo stesso IUV: " + datiVersamento.getIuv() + ".");
			}
			
			if (existIuv(pagamento.getIdentificativoBeneficiario(), datiVersamento.getIuv(), ccp)) {
				throw new GovPayException(GovPayExceptionEnum.IUV_DUPLICATO, "L'IUV " + datiVersamento.getIuv() + " risulta gia' utilizzato in una precedente richiesta.");
			}
			
			iuvs.add(datiVersamento.getIuv());
		}

		// Per ciascun pagamento
		Map<DistintaModel, List<PendenzaModel>> distintePendenzeMap = new HashMap<DistintaModel, List<PendenzaModel>>();

		String idGruppo = richiestaPagamento.getPagamentis().size() > 1 ? IdUtils.generaCodTransazione() : null;
		if(idGruppo != null) {
			log.info("Pagamento multiplo [idGruppo:" + idGruppo + "].");
		}
		
		for(it.govpay.rs.Pagamento pagamento : richiestaPagamento.getPagamentis()){
			log.debug("Gestione pagamento [" + pagamento.getIdentificativoBeneficiario() + "][" + pagamento.getDatiVersamento().getIuv() + "]");
			List<PendenzaModel> pendenze = new ArrayList<PendenzaModel>();
			
			// Recupero dati creditore
			log.debug("Recupero dati del creditore [" + pagamento.getIdentificativoBeneficiario() + "].");
			EnteCreditoreModel creditore = null;
			try{
				creditore = anagraficaEjb.getCreditoreByIdLogico(pagamento.getIdentificativoBeneficiario());
			} catch (Exception e) {
				log.error("Errore durante il recupero del creditore [" + pagamento.getIdentificativoBeneficiario() + "]", e);
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			if(creditore == null) {
				log.error("Creditore [" + pagamento.getIdentificativoBeneficiario() + "] non censito.");
				throw new GovPayException(GovPayExceptionEnum.BENEFICIARIO_NON_TROVATO);
			}
			
			TributoModel tributo = null;
			try{
				tributo = anagraficaEjb.getTributoById(creditore.getIdEnteCreditore(), pagamento.getDatiVersamento().getTipoDebito());
			} catch (Exception e) {
				log.error("Errore durante il recupero del tributo [" + pagamento.getDatiVersamento().getTipoDebito() + "]", e);
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			
			if(tributo == null) {
				log.error("Tipo debito [" + pagamento.getDatiVersamento().getTipoDebito() + "] non gestito per l'ente  [" + pagamento.getIdentificativoBeneficiario() + "].");
				throw new GovPayException(GovPayExceptionEnum.BENEFICIARIO_NON_TROVATO);
			} else if(EnumStatoTributo.D.equals(tributo.getStato())) {
				log.error("Tipo debito [" + pagamento.getDatiVersamento().getTipoDebito() + "] DISATTIVO per l'ente  [" + pagamento.getIdentificativoBeneficiario() + "].");
				throw new GovPayException(GovPayExceptionEnum.ERRORE_CONFIGURAZIONE, "Il tributo [IdEnteCreditore: "
						+ pagamento.getIdentificativoBeneficiario() + "][TipoTributo: " + pagamento.getDatiVersamento().getTipoDebito() + "] e' DISATTIVO");
			}
			
			 
			// Creazione delle pendenze
			Soggetto pagatore = pagamento.getSoggettoPagatore();
			for(DatiSingoloVersamento datiSingoloVersamento : pagamento.getDatiVersamento().getDatiSingoloVersamentos()){
				PendenzaModel pendenza = DistintaBuilder.buildPendenza(creditore, tributo, pagatore, datiSingoloVersamento);
				pendenze.add(pendenza);
			}
			
			log.debug("Creazione delle pendenze hidden");
			try {
				pendenzaEjb.inserisciPendenze(pendenze, true);
				for(PendenzaModel p : pendenze) {
					log.debug("Creata pendenza [IdPendenza:" + p.getIdPendenza() + "][Iusv:"+p.getIdDebitoEnte()+"]");
				}
			} catch (Exception e) {
				log.error("Errore durante la creazione delle pendenze.", e);
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			List<String> listaIdCondizione = new ArrayList<String>();
			for(PendenzaModel pendenza : pendenze) {
				// Per costruzione ho un solo idCondizione per pendenza.
				listaIdCondizione.add(pendenza.getCondizioniPagamento().get(0).getIdCondizione());
			}

			// 3. Creare le Distinte di pagamento
			DistintaModel distinta = new DistintaModel();
			distinta.setIuv(pagamento.getDatiVersamento().getIuv());
			distinta.setCodTransazione(IdUtils.generaCodTransazione());
			distinta.setCodTransazionePsp(ccp);
			distinta.setStato(EnumStatoDistinta.IN_CORSO);
			distinta.setIdGatewayPagamento(richiestaPagamento.getIdentificativoPsp());
			distinta.setImportoTotale(pagamento.getDatiVersamento().getImportoTotaleDaVersare());
			distinta.setAutenticazione(EjbUtils.toEjbAutorizzazioneSoggetto(richiestaPagamento.getAutenticazioneSoggetto()));
			distinta.setFirma(EjbUtils.toEjbFirma(pagamento.getFirma()));
			distinta.setIbanAddebito(pagamento.getDatiVersamento().getIbanAddebito());
			distinta.setEmailNotifiche(richiestaPagamento.getEmail());
			distinta.setIdentificativoFiscaleCreditore(pagamento.getIdentificativoBeneficiario());
			distinta.setIdGruppo(idGruppo);
			distinta.setDataOraRichiesta(new Date());
			if(richiestaPagamento.getSoggettoVersante() != null) {
				distinta.setSoggettoVersante(EjbUtils.toEjbSoggettoVersante(richiestaPagamento.getSoggettoVersante()));
			} else {
				distinta.setSoggettoVersante(EjbUtils.toEjbSoggettoVersante(pagatore));
			}
			
			List<PagamentoModel> pagamentiDistinta = new  ArrayList<PagamentoModel>();

			// Aggiungo i pagamenti alla distinta
			for (String idCondizione : listaIdCondizione) {
				PagamentoModel pmodel = new PagamentoModel();
				pmodel.setDataPagamento(new Date());
				pmodel.setIdCondizionePagamento(idCondizione);
				pmodel.setStato(EnumStatoPagamento.IC);
				pmodel.setIdRiscossionePSP(null);
				pagamentiDistinta.add(pmodel);
			}
			
			distinta.setPagamenti(pagamentiDistinta);
		
			log.debug("Creazione della distinta.");
			try {
				addDistinta(distinta);
			} catch (Exception e) {
				log.error("Errore durante la creazione della distinta.", e);
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			log.debug("Distinta creata [IdDistinta: " + distinta.getIdDistinta() + "][CodTransazione:"+distinta.getCodTransazione()+"].");
			distintePendenzeMap.put(distinta, pendenze);
		}

		return distintePendenzeMap;

	}
	
	/**
	 * Crea una nuova distinta (EnumStatoOperazione IN_CORSO). Una volta creata imposta nell'oggetto Distinta l'idDistinta assegnato.
	 * 
	 * @param distinta
	 */
	private void addDistinta(DistintaModel distintaModel) {

		Timestamp currentTimestamp = new Timestamp(new Date().getTime());

		//
		// distinta
		//
		DistintaPagamento distinta = new DistintaPagamento();
		distinta.setCodTransazione(distintaModel.getCodTransazione());
		distinta.setCodTransazionePSP(distintaModel.getCodTransazionePsp());
		distinta.setDivisa(GovPayConstants.DIVISA);
		distinta.setImportoCommissioni(BigDecimal.ZERO);
		distinta.setOpAggiornamento(null);
		distinta.setOpInserimento(distintaModel.getSoggettoVersante().getIdFiscale());
		if (distintaModel.getStato() != null) {
			distinta.setStato(distintaModel.getStato().getChiave());
		} else {
			// DEFAULT: IN_CORSO
			distinta.setStato(DistintaModel.EnumStatoDistinta.IN_CORSO.getChiave());
		}
		distinta.setTmbcreazione(currentTimestamp);
		distinta.setImporto(distintaModel.getImportoTotale());
		distinta.setTsInserimento(currentTimestamp);
		distinta.setUtentecreatore(distintaModel.getSoggettoVersante().getIdFiscale());
		distinta.setDataSpedizione(currentTimestamp);
		distinta.setCodPagamento(distintaModel.getCodTransazione());
		distinta.setIuv(distintaModel.getIuv());
		distinta.setEmailVersante(distintaModel.getEmailNotifiche() == null ? "" : distintaModel.getEmailNotifiche());
		distinta.setIdentificativoFiscaleCreditore(distintaModel.getIdentificativoFiscaleCreditore());
		distinta.setTipoFirma(distintaModel.getFirma().name());
		distinta.setAutenticazioneSoggetto(distintaModel.getAutenticazione().name());
		distinta.setIbanAddebito(distintaModel.getIbanAddebito());
		distinta.setIdGruppo(distintaModel.getIdGruppo());

		//
		// distinta.cfgGatewayPagamento
		//
		CfgGatewayPagamento gateway = entityManager.find(CfgGatewayPagamento.class, distintaModel.getIdGatewayPagamento());
		distinta.setCfgGatewayPagamento(gateway);

		//
		// distinta.pagamenti
		//
		Set<Pagamento> pagamenti = new LinkedHashSet<Pagamento>();

		for (PagamentoModel pagamentoPojo : distintaModel.getPagamenti()) {

			String idCondizione = pagamentoPojo.getIdCondizionePagamento();

			Condizione condizione = entityManager.find(Condizione.class, idCondizione);

			Pagamento pagamento = new Pagamento();

			pagamento.setCdTrbEnte(condizione.getCdTrbEnte());
			pagamento.setCondPagamento(condizione);
			pagamento.setCoPagante(distintaModel.getSoggettoVersante().getIdFiscale());
			pagamento.setDtScadenza(condizione.getDtScadenza());
			pagamento.setDistintaPagamento(distinta);
			pagamento.setIdEnte(condizione.getEnte().getIdEnte());
			pagamento.setIdPendenza(condizione.getPendenza().getIdPendenza());
			pagamento.setIdPendenzaente(condizione.getPendenza().getIdPendenzaente());
			pagamento.setIdTributo(condizione.getPendenza().getTributoEnte().getIdTributo());
			pagamento.setImPagato(condizione.getImTotale());
			pagamento.setOpInserimento(distintaModel.getSoggettoVersante().getIdFiscale());
			pagamento.setStPagamento(PagamentoModel.EnumStatoPagamento.IC.name());
			pagamento.setStRiga(GovPayConstants.ST_RIGA_VISIBILE);

			pagamento.setTiDebito(null);
			pagamento.setTiPagamento(EnumTipoPagamento.S.name());
			pagamento.setTipospontaneo(null); // null, PEND

			pagamento.setTsDecorrenza(condizione.getTsDecorrenza());
			pagamento.setTsInserimento(currentTimestamp);
			pagamento.setTsOrdine(currentTimestamp);
			pagamento.setFlagIncasso("0");

			pagamenti.add(pagamento);
		}

		distinta.setPagamenti(pagamenti);
		distinta.setNumeroDisposizioni(pagamenti.size());

	
		entityManager.persist(distinta);
		//
		// distinta.datiAnagraficiVersante
		//
		VersanteModel versante = distintaModel.getSoggettoVersante();
		DatiAnagraficiVersante datiAnagraficiVersante = new DatiAnagraficiVersante();
		datiAnagraficiVersante.setAnagrafica(versante.getAnagrafica());
		datiAnagraficiVersante.setCap(versante.getCap());
		datiAnagraficiVersante.setEmail(versante.geteMail());
		datiAnagraficiVersante.setIndirizzo(versante.getIndirizzo());
		datiAnagraficiVersante.setLocalita(versante.getLocalita());
		datiAnagraficiVersante.setNazione(versante.getNazione());
		datiAnagraficiVersante.setNumeroCivico(versante.getCivico());
		datiAnagraficiVersante.setProvincia(versante.getProvincia());
		// TODO:MINO aggiungere il tipoSoggetto nel model?
		datiAnagraficiVersante.setTipoSoggetto(versante.getIdFiscale().length() == 16 ? 'F' : 'G'); 
		
		datiAnagraficiVersante.setDistintaPagamento(distinta);
		entityManager.persist(datiAnagraficiVersante);
		
		distintaModel.setIdDistinta(distinta.getId());
	}

	/**
	 * Recupera la distinta a partire dalla chiave logica (Creditore / iuv)
	 * Se non esiste ritorna null;
	 * 
	 * @param idFiscaleCreditore (distinte_pagamento.IDENTIFICATIVO_FISCALE_CREDITORE)
	 * @param iuv - identificativoUnivocoVersamento
	 * @return
	 */
	public DistintaModel getDistinta(String idFiscaleCreditore, String iuv) {
		// TODO: [SR] verificare ts_annullamento?
		TypedQuery<DistintaPagamento> query = entityManager
				.createQuery("select d from DistintaPagamento d where d.identificativoFiscaleCreditore = :idFiscaleCreditore and d.iuv = :iuv order by d.tsInserimento desc",
				DistintaPagamento.class);
		
		query.setParameter("idFiscaleCreditore", idFiscaleCreditore);
		query.setParameter("iuv", iuv);
		
		List<DistintaPagamento> distinte = query.getResultList();
		if(distinte.isEmpty()) {
			return null;
		} else {
			// TODO: [SR] viene presa l'ultima distinta in ordine di inserimento perchè potrebbe esserci più di una distinta con lo stesso iuv
			// (nel caso di pagamento tipo 3 annullato dal PSP e poi ritentato)
			return DistintaBuilder.fromDistinta(distinte.get(0));
		}
	}

	public DistintaModel getDistinta(String idFiscaleCreditore, String iuv, String ccp) {
		TypedQuery<DistintaPagamento> query = entityManager.createNamedQuery("getDistintaByIdFiscaleCreditoreIuvAndCcp", DistintaPagamento.class);
		query.setParameter("idFiscaleCreditore", idFiscaleCreditore);
		query.setParameter("iuv", iuv);
		query.setParameter("ccp", ccp);
		try {
			DistintaPagamento distinta = query.getSingleResult();
			return DistintaBuilder.fromDistinta(distinta);
		} catch (NoResultException e) {
			return null;
		}
	}

	public EsitoPagamentoDistinta getEsitoPagamentoDistinta(Long idDistinta) {
		DistintaPagamento distinta = entityManager.find(DistintaPagamento.class, idDistinta);

		EsitoPagamentoDistinta esitoDistinta = new EsitoPagamentoDistinta();
		esitoDistinta.setIdentificativoFiscaleCreditore(distinta.getIdentificativoFiscaleCreditore());
		esitoDistinta.setIdTransazionePSP(distinta.getCodTransazionePSP());
		esitoDistinta.setIuv(distinta.getIuv());
		esitoDistinta.setStato(EnumUtils.findByChiave(distinta.getStato(), EnumStatoDistinta.class));

		BigDecimal importoTotalePagato = new BigDecimal(0);

		esitoDistinta.setDatiSingoliPagamenti(new ArrayList<EsitoPagamentoDistinta.EsitoSingoloPagamento>());
		for (Pagamento pagamento : distinta.getPagamenti()) {
			EsitoSingoloPagamento esitoPagamento = esitoDistinta.new EsitoSingoloPagamento();
			if(EnumStatoPagamento.ES.name().equals(pagamento.getStPagamento())) {
				esitoPagamento.setDataPagamento(pagamento.getTsDecorrenza());
				esitoPagamento.setImportoPagato(pagamento.getImPagato());
			} else {
				esitoPagamento.setDataPagamento(null);
				esitoPagamento.setImportoPagato(BigDecimal.ZERO);
			}
			esitoPagamento.setDescrizioneEsito(pagamento.getNotePagamento());
			esitoPagamento.setIdRiscossionePSP(pagamento.getIdRiscossionePSP());
			esitoPagamento.setIdPagamentoEnte(pagamento.getCondPagamento().getIdPagamento());
			esitoPagamento.setIdPendenza(pagamento.getIdPendenza());
			esitoDistinta.getDatiSingoliPagamenti().add(esitoPagamento);
			importoTotalePagato = importoTotalePagato.add(esitoPagamento.getImportoPagato());
		}
		esitoDistinta.setImportoTotalePagato(importoTotalePagato);
		return esitoDistinta;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void updateStatoDistinta(Long idDistinta, EnumStatoDistinta nuovoStato, String causaleUpdate) {
		updateStatoDistinta(idDistinta, nuovoStato, null, causaleUpdate);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void updateStatoDistinta(Long idDistinta, EnumStatoDistinta nuovoStato, GovPayExceptionEnum exception, String causaleUpdate) {
		String exc = null;
		if(exception != null)  exc = exception.getErrorCode();
		DistintaPagamento distintaPagamento = entityManager.find(DistintaPagamento.class, idDistinta);
		distintaPagamento.setStato(nuovoStato.getChiave());

		for (Pagamento p:distintaPagamento.getPagamenti()) {
			p.setStPagamento(EnumUtils.mapStatoDistintaToStatoPagamento(nuovoStato).toString());
		}
		addNewPagamentoOnline(distintaPagamento, new Timestamp(new Date().getTime()), GovPayConstants.OP_INSERIMENTO, EnumTipoTrackingOperazione.AUTORIZZAZIONE, null, exc, causaleUpdate);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void updateDistinta(EsitoPagamentoDistinta esitoDistinta) {

		Timestamp tsAggiornamento = new Timestamp(new Date().getTime());
		String opAggiornamento = "RT_NODOPAGAMENTI";

		//
		// recupero distinta
		//
		// TODO: [SR] verificare ts_annullamento?
		TypedQuery<DistintaPagamento> query = entityManager.createNamedQuery("getDistintaByIdFiscaleCreditoreIuvAndCcp", DistintaPagamento.class);
		query.setParameter("idFiscaleCreditore", esitoDistinta.getIdentificativoFiscaleCreditore());
		query.setParameter("iuv", esitoDistinta.getIuv());
		query.setParameter("ccp", esitoDistinta.getIdTransazionePSP());
		
		DistintaPagamento distinta = null;
		try {
			distinta = query.getSingleResult();
		} catch (NoResultException e) {
			log.error("Nessuna distinta per idFiscaleCreditore: " + esitoDistinta.getIdentificativoFiscaleCreditore() + " e iuv: " + esitoDistinta.getIuv(), e);
			throw e;
		}		

		//
		// aggiornamento distinta
		//
		distinta.setStato(esitoDistinta.getStato().getChiave());
		distinta.setTsAggiornamento(tsAggiornamento);
		distinta.setOpAggiornamento(opAggiornamento);
		distinta.setCodTransazionePSP(esitoDistinta.getIdTransazionePSP());
		log.debug("Aggiornamento distinta id: " + distinta.getId() + " allo stato " + esitoDistinta.getStato().getDescrizione() + ": " + esitoDistinta.getStato().getChiave());

		//
		// aggiornamento pagamenti
		//
		updatePagamenti(distinta, esitoDistinta, opAggiornamento ,tsAggiornamento);

		if (log.isDebugEnabled()) {
			log.debug("Aggiornamento pagamenti nr: " + esitoDistinta.getDatiSingoliPagamenti().size());
			log.debug("Codice Esito del Pagamento: " + esitoDistinta.getStato().getDescrizione());
			for (Pagamento pagamento : distinta.getPagamenti()) {
				log.debug("Aggiornamento pagamento id: " + pagamento.getId() + " allo stato " + pagamento.getStPagamento());
			}
		}
		addNewPagamentoOnline(distinta, tsAggiornamento, opAggiornamento, TrackingOperazioniPagamentoModel.EnumTipoTrackingOperazione.NOTIFICA, null, null, null);

	}
	

	private void updatePagamenti(DistintaPagamento distinta, EsitoPagamentoDistinta esitoDistinta, String opAggiornamento, Timestamp tsAggiornamento) {

		int pagamentiMod = 0;
		Set<Pagamento> pagamenti = distinta.getPagamenti();
		for (Pagamento pagamento : pagamenti) {
			
			Condizione condizione = pagamento.getCondPagamento();
			Pendenza pendenza = condizione.getPendenza();
			if(EnumStatoDistinta.ESEGUITO.equals(esitoDistinta.getStato())) {
				condizione.setStRiga(GovPayConstants.ST_RIGA_VISIBILE);
				pendenza.setStRiga(GovPayConstants.ST_RIGA_VISIBILE);
				for(DestinatarioPendenza destinatario : pendenza.getDestinatari()) {
					destinatario.setStRiga(GovPayConstants.ST_RIGA_VISIBILE);
				}
			} else if(EnumStatoDistinta.NON_ESEGUITO.equals(esitoDistinta.getStato())) {
				// annullamento pendenza e condizione
				condizione.setTsAnnullamento(tsAggiornamento);
				condizione.setOpAnnullamento(opAggiornamento);
				pendenza.setTsAnnullamento(tsAggiornamento);
				pendenza.setOpAnnullamento(opAggiornamento);
			}


			String idPagamento = pagamento.getCondPagamento().getIdPagamento();
			for (EsitoPagamentoDistinta.EsitoSingoloPagamento datiPagamento : esitoDistinta.getDatiSingoliPagamenti()) {
				if (idPagamento.equals(datiPagamento.getIdPagamentoEnte())) {
					pagamento.setStPagamento(datiPagamento.getStato().name());
					pagamento.setTsDecorrenza(datiPagamento.getDataPagamento() != null ? new Timestamp(datiPagamento.getDataPagamento().getTime()) : null);
					pagamento.setImPagato(datiPagamento.getImportoPagato());
					pagamento.setIdRiscossionePSP(datiPagamento.getIdRiscossionePSP());
					pagamento.setNotePagamento(datiPagamento.getDescrizioneEsito());
					pagamento.setTsAggiornamento(tsAggiornamento);
					pagamento.setOpAggiornamento(opAggiornamento);

					pagamentiMod++;
					break;
				}
			}
		}

		if (pagamentiMod != pagamenti.size()) {
			log.error("non c'è corrispondenza tra i pagamenti della distinta e quelli contenuti nella ricevuta");
			throw new RuntimeException("Aggiornati " + pagamentiMod + " su " + pagamenti.size());
		}

	}


	private void addNewPagamentoOnline(DistintaPagamento distinta, Timestamp tsInserimento, String opInserimento,
			TrackingOperazioniPagamentoModel.EnumTipoTrackingOperazione tipoOperazione, String idSession, String codErrore, String de_operazione) {
		PagamentiOnline pol = new PagamentiOnline();
		pol.setCodAutorizzazione(distinta.getIuv());
		if (de_operazione==null) { 
			pol.setDeOperazione("N/A");
		} else if (de_operazione.length()>255){ 
			pol.setDeOperazione(de_operazione.substring(0,252)+"..");
		} else {
			pol.setDeOperazione(de_operazione);
		}
		pol.setDistintaPagamento(distinta);
		pol.setIdOperazione(distinta.getCodTransazione()); 
		pol.setNumOperazione("1");
		pol.setOpAggiornamento(null);
		pol.setOpInserimento(opInserimento);
		pol.setSessionIdSistema("-");
		pol.setSessionIdTerminale("-");
		pol.setSessionIdTimbro("-");
		pol.setSessionIdToken(idSession!=null ? idSession : "-");
		pol.setApplicationId(distinta.getCfgGatewayPagamento().getApplicationId());
		pol.setSystemId(distinta.getCfgGatewayPagamento().getSystemId());
		pol.setTiOperazione(tipoOperazione.getChiave());
		pol.setTsAggiornamento(null);
		pol.setTsInserimento(tsInserimento);
		pol.setTsOperazione(tsInserimento);
		if (codErrore!=null) {
			pol.setCodErrore(codErrore);
			pol.setEsito("KO");
		} else {
			pol.setCodErrore("OK000000");
			pol.setEsito("OK");			
		}


		if (distinta.getPagamentiOnline() == null) {
			distinta.setPagamentiOnline(new HashSet<PagamentiOnline>());
		}
		distinta.getPagamentiOnline().add(pol);
		entityManager.persist(pol);
	}



	/**
	 * Ritorna la lista delle distinte il non pendenti (ovvero che non sono ne IN_CORSO ne ESEGUITE_SBF) per cui non e' ancora stato notificato l'esito
	 */
	public List<DistintaModel> getDistinteNonNotificate(String idEnteCreditore) {

		TypedQuery<DistintaPagamento> query = entityManager.createQuery("select distinct p.distintaPagamento from Pagamento p where p.statoNotifica is null", DistintaPagamento.class);
		List<DistintaPagamento> distintePagamento = query.getResultList();
		
		List<DistintaModel> distinteNonNotificate = new ArrayList<DistintaModel>();
		for (DistintaPagamento distintaPagamento : distintePagamento) {
			distinteNonNotificate.add(DistintaBuilder.fromDistinta(distintaPagamento));
		}
		return distinteNonNotificate;
	}

	/**
	 * Imposta la distinta individuata come notificata.
	 */
	public void setDistintaNotificata(long idDistinta) {
		
		TypedQuery<Pagamento> query = entityManager.createQuery("select p from Pagamento p where p.distintaPagamento.id = :idDistinta", Pagamento.class);
		query.setParameter("idDistinta", idDistinta);
		
		Timestamp adesso = new Timestamp(new Date().getTime()); 
		List<Pagamento> pagamentiDellaDistinta = query.getResultList();
		for (Pagamento pagamento : pagamentiDellaDistinta) {
			pagamento.setStatoNotifica(PagamentoModel.StatoNotifica.NOTIFICATO.name());
			pagamento.setNotificaEseguito(adesso);
		}
	}

	/**
	 * Ritorna la lista delle distinte pendenti (quindi IN_CORSO e ESEGUITO_SBF)
	 * e con tipo di pagamento non "attivato da PSP"
	 */
	public List<DistintaModel> getDistintePendenti(String idFiscaleCreditore) {
		
		TypedQuery<DistintaPagamento> query = entityManager.createNamedQuery("getDistintePendenti", DistintaPagamento.class);
		query.setParameter("idFiscaleCreditore", idFiscaleCreditore);
		
		List<DistintaPagamento> distintePagamento = query.getResultList();
		
		List<DistintaModel> distintePendenti = new ArrayList<DistintaModel>();
		for (DistintaPagamento distinta : distintePagamento) {
			distintePendenti.add(DistintaBuilder.fromDistinta(distinta));
		}
		return distintePendenti;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String getNextIuv(String idFiscaleCreditore, boolean isISO11694) throws GovPayException {
		while(true) {
			String iuv = null;
			long nextProgressivoIUV = nextProgressivoIUV();
			if(nextProgressivoIUV > 999999999999999l) 
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Superato il limite di IUV generabili.");
			String reference = String.format("%015d", nextProgressivoIUV);
			
			if(isISO11694) {
				String check = IuvUtils.getCheckDigit(reference);
				iuv = "RF" + check + reference;
			} else {
				iuv= reference;
			}
			
			if(!existIuv(idFiscaleCreditore, iuv, null)) {
				return iuv;
			}
		}
	}

	public long nextProgressivoIUV() {
		Iuv nuovoIuv = new Iuv();
		entityManager.persist(nuovoIuv);
		return nuovoIuv.getId();
	}

	/**
	 * Controlla se per il creditore indicato, l'IUV/CCP e' gia' utilizzato.
	 */
	public boolean existIuv(String idFiscaleCreditore, String iuv, String ccp) {
		
		String qlString = "select d from DistintaPagamento d where d.identificativoFiscaleCreditore = :idFiscaleCreditore and d.iuv in :iuv";
		if(ccp != null) 
			qlString = qlString + " and d.codTransazionePSP = :ccp";
		
		TypedQuery<DistintaPagamento> query = entityManager.createQuery(qlString, DistintaPagamento.class);
		query.setParameter("idFiscaleCreditore", idFiscaleCreditore);
		query.setParameter("iuv", iuv);
		if(ccp != null) 
			query.setParameter("ccp", ccp);
		
		List<DistintaPagamento> distinte = query.getResultList();
		return !distinte.isEmpty();
		
	}


	private void appendConstraintDistinta(StringBuilder qlStringBuilder, Map<String, Object> parmetersMap, DistintaFilter filtro) {
		if(filtro != null) {
			if (filtro.getIuv() != null) {
				qlStringBuilder.append(" and d.iuv = :iuv");
				parmetersMap.put("iuv", filtro.getIuv());
			}
			if (filtro.getImportoDa() != null) {
				qlStringBuilder.append(" and d.importo >= :importoDa");
				parmetersMap.put("importoDa", filtro.getImportoDa());
			}
			if (filtro.getImportoA() != null) {
				qlStringBuilder.append(" and d.importo <= :importoA");
				parmetersMap.put("importoA", filtro.getImportoA());
			}
			if (filtro.getDataInizio() != null) {
				qlStringBuilder.append(" and d.tmbcreazione >= :dataInizio");
				parmetersMap.put("dataInizio", new Timestamp(filtro.getDataInizio().getTime()));
			}
			if (filtro.getDataFine() != null) {
				qlStringBuilder.append(" and d.tmbcreazione <= :dataFine");
				parmetersMap.put("dataFine", new Timestamp(filtro.getDataFine().getTime()));
			}
			if (filtro.getStato() != null) {
				qlStringBuilder.append(" and d.stato = :stato");
				parmetersMap.put("stato", filtro.getStato().name());
			}
			if (filtro.getCfEnteCreditore() != null) {
				qlStringBuilder.append(" and d.identificativoFiscaleCreditore = :idFiscaleCreditore");
				parmetersMap.put("idFiscaleCreditore", filtro.getCfEnteCreditore());
			}
			// TODO:MINO verificare - mi sapetto una lista di idFiscali e non id
			if (filtro.getIdentificativiEnteCreditore() != null && !filtro.getIdentificativiEnteCreditore().isEmpty()) {
				qlStringBuilder.append(" and d.identificativoFiscaleCreditore in :listaIdFiscaleEnteCreditore");
				parmetersMap.put("listaIdFiscaleEnteCreditore", filtro.getIdentificativiEnteCreditore());
			}
			if (filtro.getCfVersanteODebitore() != null) {
				qlStringBuilder.append(" and d.utentecreatore = :idFiscaleVersante");
				parmetersMap.put("idFiscaleVersante", filtro.getCfVersanteODebitore());
				// TODO:MINO manca ricerca da debitore
				// parmetersMap.put("idFiscaleDebitore", filtro.getCfVersanteODebitore());
			}
			qlStringBuilder.append(" order by d.tmbcreazione desc");
		}		
	}
	
	
	
	public List<DistintaModel> findAllDistinte(DistintaFilter filtro) {

		StringBuilder qlStringBuilder = new StringBuilder("select d from DistintaPagamento d where 1 = 1");
		Map<String, Object> parmetersMap = new HashMap<String, Object>();
		appendConstraintDistinta(qlStringBuilder, parmetersMap, filtro);
		
		TypedQuery<DistintaPagamento> query = entityManager.createQuery(qlStringBuilder.toString(), DistintaPagamento.class);
		QueryUtils.setParameters(query, parmetersMap);
		QueryUtils.setPagingParameters(query, filtro);

		List<DistintaPagamento> distinte = query.getResultList();

		List<DistintaModel> lst = new ArrayList<DistintaModel>();
		for (DistintaPagamento distintaPagamento : distinte) {
			lst.add(DistintaBuilder.fromDistinta(distintaPagamento));
		}
		return lst;
	}

	
	public int countAllDistinte(DistintaFilter filtro) {

		StringBuilder qlStringBuilder = new StringBuilder("select count(*) from DistintaPagamento d where 1 = 1");
		Map<String, Object> parmetersMap = new HashMap<String, Object>();
		appendConstraintDistinta(qlStringBuilder, parmetersMap, filtro);
		
		Query query = entityManager.createQuery(qlStringBuilder.toString());
		QueryUtils.setParameters(query, parmetersMap);

		Long count = (Long)query.getSingleResult();
		return count.intValue();
	}

	
	public DistintaModel findDistintaById(Long idDistinta) {
		try {
			DistintaPagamento distinta = entityManager.find(DistintaPagamento.class, idDistinta);
			return DistintaBuilder.fromDistinta(distinta);
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * Ritorna la lista delle distinte attivate e ancora da spedire, quindi in stato ESEGUITA_SBF e con tipo di pagamento attivato da PSP
	 * @param idEnteCreditore
	 * @return
	 */
	public List<DistintaModel> getDistinteDaSpedire(String idFiscaleCreditore) {
		
		TypedQuery<DistintaPagamento> query = entityManager.createNamedQuery("getDistinteDaSpedire", DistintaPagamento.class);
		query.setParameter("idFiscaleCreditore", idFiscaleCreditore);
		
		List<DistintaPagamento> distintePagamento = query.getResultList();
		
		List<DistintaModel> distintePendenti = new ArrayList<DistintaModel>();
		for (DistintaPagamento distinta : distintePagamento) {
			distintePendenti.add(DistintaBuilder.fromDistinta(distinta));
		}
		return distintePendenti;
	}
	
	public void updateDistinta(Long idDistinta, EsitoRevocaDistinta esitoRevoca) {
		// TODO: implementare [REVOCA]
		return;
	}

	public EsitoRevocaDistinta getEsitoRevocaDistinta(Long idDistinta) {
		// TODO: implementare [REVOCA]
		return null;
	}
}
