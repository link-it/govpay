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
package it.govpay.ejb.core.builder;

import it.govpay.ejb.core.model.GatewayPagamentoModel;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumCanalePagamento;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumDocumentoPagamento;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumFornitoreGateway;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumModalitaPagamento;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumModelloVersamento;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumStato;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumStrumentoPagamento;
import it.govpay.ejb.core.utils.EnumUtils;
import it.govpay.ejb.core.utils.GovPayConstants;
import it.govpay.orm.configurazione.CfgCanalePagamento;
import it.govpay.orm.configurazione.CfgCommissionePagamento;
import it.govpay.orm.configurazione.CfgDocumentoPagamento;
import it.govpay.orm.configurazione.CfgFornitoreGateway;
import it.govpay.orm.configurazione.CfgGatewayPagamento;
import it.govpay.orm.configurazione.CfgModalitaPagamento;
import it.govpay.orm.configurazione.CfgStrumentoPagamento;
import it.govpay.orm.configurazione.CfgTipoCommissione;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

// TODO: eliminare le mappe di conversione e utilizzare gli enum con attibuti chiave descrizione (vedi EnumModelloVersamento)
public class GatewayPagamentoBuilder {

	// ---------------------------------------------------------------------------------	
	// Mappe statiche bidirezionali per associazione EnumCanalePagamento e relativo ID
	// ---------------------------------------------------------------------------------
	static final Map<EnumCanalePagamento, Long> mapCanalePagamento2Id = new HashMap<EnumCanalePagamento, Long>() {
		private static final long serialVersionUID = 1L;
		{
			put(EnumCanalePagamento.BANCA, 3L);
			put(EnumCanalePagamento.POSTE, 6L);
			put(EnumCanalePagamento.WEB, 1L);
			put(EnumCanalePagamento.PSP, 14L);

		}
	};

	static final Map<Long,EnumCanalePagamento> mapId2CanalePagamento = new HashMap<Long, EnumCanalePagamento >() {
		private static final long serialVersionUID = 1L;
		{
			put(3L, EnumCanalePagamento.BANCA);
			put(6L, EnumCanalePagamento.POSTE);
			put(1L, EnumCanalePagamento.WEB);
			put(14L,EnumCanalePagamento.PSP);

		}
	};

	// ---------------------------------------------------------------------------------	
	// Mappe statiche bidirezionali per associazione EnumDocumentoPagamento e relativo ID
	// ---------------------------------------------------------------------------------	
	static final Map<EnumDocumentoPagamento, Long> mapDocumentoPagamento2Id = new HashMap<EnumDocumentoPagamento, Long>() {
		private static final long serialVersionUID = 1L;
		{
			put(EnumDocumentoPagamento.BOLLETTINO_NDP, 5L);
		}
	};

	static final Map<Long, EnumDocumentoPagamento> mapId2DocumentoPagamento = new HashMap<Long, EnumDocumentoPagamento>() {
		private static final long serialVersionUID = 1L;
		{
			put(5L, EnumDocumentoPagamento.BOLLETTINO_NDP);
		}
	};

	// ---------------------------------------------------------------------------------	
	// Mappe statiche bidirezionali per associazione EnumFornitoreGateway e relativo ID	
	// ---------------------------------------------------------------------------------	
	static final Map<EnumFornitoreGateway, Long> mapFornitoreGateway2Id = new HashMap<EnumFornitoreGateway, Long>() {
		private static final long serialVersionUID = 1L;

		{
			put(EnumFornitoreGateway.NODO_PAGAMENTI_SPC, 4L);
		}
	};

	static final Map<Long,EnumFornitoreGateway > mapId2FornitoreGateway = new HashMap<Long, EnumFornitoreGateway>() {
		private static final long serialVersionUID = 1L;
		{
			put(4L,EnumFornitoreGateway.NODO_PAGAMENTI_SPC);
		}
	};

	// ---------------------------------------------------------------------------------
	// Mappe statiche bidirezionali per associazione EnumModalitaPagamento e relativo ID
	// ---------------------------------------------------------------------------------	

	static final Map<EnumModalitaPagamento, Long> mapModalitaPagamento2Id = new HashMap<EnumModalitaPagamento, Long>() {
		private static final long serialVersionUID = 1L;
		{
			put(EnumModalitaPagamento.ADDEBITO_DIRETTO, 3L); //TODO: [SR] inserire record corretti su tabella modalita vorrei togliere i "RID_ONLINE" che non c'entrano più niente.
			put(EnumModalitaPagamento.ATTIVATO_PRESSO_PSP, 16L);
			put(EnumModalitaPagamento.BOLLETTINO_POSTALE,15L);
			put(EnumModalitaPagamento.BONIFICO_BANCARIO_TESORERIA,14L);
			put(EnumModalitaPagamento.CARTA_PAGAMENTO,4L);
			put(EnumModalitaPagamento.MYBANK,17L);
		}
	};

	static final Map<Long, EnumModalitaPagamento > mapId2ModalitaPagamento = new HashMap<Long, EnumModalitaPagamento>() {
		private static final long serialVersionUID = 1L;
		{
			put(3L,EnumModalitaPagamento.ADDEBITO_DIRETTO); 
			put(16L,EnumModalitaPagamento.ATTIVATO_PRESSO_PSP);
			put(15L,EnumModalitaPagamento.BOLLETTINO_POSTALE);
			put(14L,EnumModalitaPagamento.BONIFICO_BANCARIO_TESORERIA);
			put(4L,EnumModalitaPagamento.CARTA_PAGAMENTO);
			put(17L,EnumModalitaPagamento.MYBANK);
		}
	};	

	// ---------------------------------------------------------------------------------	
	// Mappe statiche bidirezionali per associazione EnumStrumentoPagamento e relativo ID	
	// ---------------------------------------------------------------------------------	
	static final Map<EnumStrumentoPagamento, Long> mapStrumentoPagamento2Id = new HashMap<EnumStrumentoPagamento, Long>() {
		private static final long serialVersionUID = 1L;
		{
			put(EnumStrumentoPagamento.ADDEBITO_DIRETTO,3L);
			put(EnumStrumentoPagamento.BONIFICO,2L);
			put(EnumStrumentoPagamento.CARTA_PAGAMENTO,6L);
			put(EnumStrumentoPagamento.DOCUMENTO_PAGAMENTO,5L);
		}
	};

	static final Map<Long,EnumStrumentoPagamento> mapId2StrumentoPagamento = new HashMap<Long,EnumStrumentoPagamento>() {
		private static final long serialVersionUID = 1L;
		{
			put(3L,EnumStrumentoPagamento.ADDEBITO_DIRETTO);
			put(2L,EnumStrumentoPagamento.BONIFICO);
			put(6L,EnumStrumentoPagamento.CARTA_PAGAMENTO);
			put(5L,EnumStrumentoPagamento.DOCUMENTO_PAGAMENTO);
		}
	};	


	/**
	 * Mappa un Gateway pagamento sul relativo oggetto ORM
	 * 
	 * @param g
	 * @param result
	 * @param em
	 */
	public static void toCfgGatewayPagamento(GatewayPagamentoModel g, CfgGatewayPagamento result, EntityManager em) {

		if (g==null) {
			throw new IllegalArgumentException("Gateway pagamento è null");
		}	

		result.setApplicationId(g.getApplicationId());
		result.setApplicationIp(null);
		result.setBundleKey(g.getBundleKey());
		if (g.getDataFineValidita()!=null) result.setDataFineValidita(new Timestamp(g.getDataFineValidita().getTime()));
		if (g.getDataInizioValidita()!=null) result.setDataInizioValidita(new Timestamp(g.getDataInizioValidita().getTime()));
		if (g.getDataPubblicazione()!=null) result.setDataPubblicazione(new Timestamp(g.getDataPubblicazione().getTime()));
		result.setDescGateway(g.getDescrizioneGateway());
		result.setDescrizione(g.getDescrizione());
		result.setDisponibilitaServizio(g.getDisponibilitaServizio());
		//result.setId(Long);
		result.setMaxImporto(null);
		result.setMolteplicita("9");
		result.setPriorita(g.getPriorita());
		if (g.getStato()!=null) result.setStato(g.getStato().toString());
		result.setStRiga(GovPayConstants.ST_RIGA_VISIBILE);
		result.setSubsystemId(g.getSubSystemId());
		result.setSystemId(g.getSystemId());
		result.setSystemName(g.getSystemName());
		result.setTimeout(null);
		result.setTimeoutAup(null);
		result.setTimeoutNp(null);
		result.setUrlInfoCanale(g.getUrlInformazioniCanale());
		result.setUrlInfoPsp(g.getUrlInformazioniPsp());
		result.setFlStornoGestito(g.isStornoGestito()?"Y":"N");

		if (g.getModelloVersamento()!=null) {
			result.setModelloVersamento(g.getModelloVersamento().getChiave());
			switch (g.getModelloVersamento()) {
			case IMMEDIATO:
				result.setFlPagabileIris("1");				
				break;
			case IMMEDIATO_MULTIBENEFICIARIO:
				result.setFlPagabileIris("1");
				break;
			case ATTIVATO_PRESSO_PSP:
				result.setFlPagabileIris("0");
				break;
			case DIFFERITO:
				result.setFlPagabileIris("1");
				break;
			default:
				result.setFlPagabileIris("1");
				break;
			}
		} else {
			result.setFlPagabileIris("0");
		}
		if (g.getCanalePagamento()!=null) {
			Long idCanalePagamento=mapCanalePagamento2Id.get(g.getCanalePagamento());
			CfgCanalePagamento canalePagamento=em.find(CfgCanalePagamento.class, idCanalePagamento);
			result.setCfgCanalePagamento(canalePagamento);
		}	else {
			result.setCfgCanalePagamento(null);
		}
		if (g.getDocumentoPagamento()!=null) {
			Long idDocumentoPagamento=mapDocumentoPagamento2Id.get(g.getDocumentoPagamento());
			CfgDocumentoPagamento documentoPagamento=em.find(CfgDocumentoPagamento.class, idDocumentoPagamento);
			result.setCfgDocumentoPagamento(documentoPagamento);
		}	else {
			result.setCfgDocumentoPagamento(null);
		}
		if (g.getFornitoreGateway()!=null) {
			Long idFornitoreGateway=mapFornitoreGateway2Id.get(g.getFornitoreGateway());
			CfgFornitoreGateway fornitoreGateway=em.find(CfgFornitoreGateway.class, idFornitoreGateway);
			result.setCfgFornitoreGateway(fornitoreGateway);
		}	else {
			result.setCfgFornitoreGateway(null);
		}
		if (g.getModalitaPagamento()!=null) {
			Long idModalitaPagamento=mapModalitaPagamento2Id.get(g.getModalitaPagamento());
			CfgModalitaPagamento modalitaPagamento=em.find(CfgModalitaPagamento.class, idModalitaPagamento);
			result.setCfgModalitaPagamento(modalitaPagamento);
		}	else {
			result.setCfgModalitaPagamento(null);
		}
		if (g.getStrumentoPagamento()!=null) {
			Long idStrumentoPagamento=mapStrumentoPagamento2Id.get(g.getStrumentoPagamento());
			CfgStrumentoPagamento strumentoPagamento=em.find(CfgStrumentoPagamento.class, idStrumentoPagamento);
			result.setCfgStrumentoPagamento(strumentoPagamento);
		}	else {
			result.setCfgStrumentoPagamento(null);
		}

		// Implementazione parziale. Le commissioni pagamento sono espresse in modo molto semplice, con una 
		// descrizione testuale. Quindi assumiamo  per ora  un legame 1 ad 1 tra CfgGatewayPagamento e CfgCommissioniPagamento

		CfgCommissionePagamento  comm = null;
		Set<CfgCommissionePagamento> comms = result.getCfgCommissionePagamenti();
		if (comms!=null && comms.size()>0) {
			comm = comms.iterator().next();  // Assert: Ci deve essere sempre uno ed un solo record nella tabella commissioni
			comm.setOpAggiornamento(result.getOpAggiornamento());
			comm.completeForUpdate();
		} else {
			comm = new CfgCommissionePagamento();
			comm.setCfgGatewayPagamento(result);
			comm.setOpInserimento(result.getOpInserimento());
			comm.completeForInsert();
			Set<CfgCommissionePagamento> newComms = new LinkedHashSet<CfgCommissionePagamento>();
			newComms.add(comm);
			result.setCfgCommissionePagamenti(newComms);
		}
		comm.setCfgTipoCommissione(em.find(CfgTipoCommissione.class, 3L));
		comm.setDescrizione(g.getImportoCommissioneMassima());
		comm.setDivisa(GovPayConstants.DIVISA);
		comm.setStRiga(GovPayConstants.ST_RIGA_VISIBILE);
		comm.setValore(new BigDecimal(0));
	}

	public static GatewayPagamentoModel fromCfgGatewayPagamento(CfgGatewayPagamento cfgGtw) {
		if (cfgGtw == null) {
			return null;
		}	
		GatewayPagamentoModel gtwPojo = new GatewayPagamentoModel();
		gtwPojo.setIdGateway(cfgGtw.getId());
		gtwPojo.setApplicationId(cfgGtw.getApplicationId());
		gtwPojo.setBundleKey(cfgGtw.getBundleKey());
		gtwPojo.setDataFineValidita(cfgGtw.getDataFineValidita());
		gtwPojo.setDataInizioValidita(cfgGtw.getDataInizioValidita());
		gtwPojo.setDataPubblicazione(cfgGtw.getDataPubblicazione());
		gtwPojo.setDescrizione(cfgGtw.getDescrizione());
		gtwPojo.setDescrizioneGateway(cfgGtw.getDescGateway());
		gtwPojo.setDisponibilitaServizio(cfgGtw.getDisponibilitaServizio());
		gtwPojo.setIdGateway(cfgGtw.getId());
		gtwPojo.setPriorita(cfgGtw.getPriorita());
		gtwPojo.setStato(EnumStato.valueOf(cfgGtw.getStato()));
		gtwPojo.setSubSystemId(cfgGtw.getSubsystemId());
		gtwPojo.setSystemId(cfgGtw.getSystemId());
		gtwPojo.setSystemName(cfgGtw.getSystemName());
		gtwPojo.setUrlInformazioniCanale(cfgGtw.getUrlInfoPsp());
		gtwPojo.setStornoGestito("Y".equals(cfgGtw.getFlStornoGestito()));

		if (cfgGtw.getCfgCanalePagamento()!=null && cfgGtw.getCfgCanalePagamento().getId()!=null) {
			gtwPojo.setCanalePagamento(mapId2CanalePagamento.get(cfgGtw.getCfgCanalePagamento().getId()));
		}
		if (cfgGtw.getCfgDocumentoPagamento()!=null && cfgGtw.getCfgDocumentoPagamento().getId()!=null) {
			gtwPojo.setDocumentoPagamento(mapId2DocumentoPagamento.get(cfgGtw.getCfgDocumentoPagamento().getId()));
		}
		if (cfgGtw.getCfgModalitaPagamento()!=null && cfgGtw.getCfgModalitaPagamento().getId()!=null) {
			gtwPojo.setModalitaPagamento(mapId2ModalitaPagamento.get(cfgGtw.getCfgModalitaPagamento().getId()));
		}
		if (cfgGtw.getCfgStrumentoPagamento()!=null && cfgGtw.getCfgStrumentoPagamento().getId()!=null) {
			gtwPojo.setStrumentoPagamento(mapId2StrumentoPagamento.get(cfgGtw.getCfgStrumentoPagamento().getId()));
		}
		if (cfgGtw.getCfgFornitoreGateway()!=null && cfgGtw.getCfgFornitoreGateway().getId()!=null) {
			gtwPojo.setFornitoreGateway(mapId2FornitoreGateway.get(cfgGtw.getCfgFornitoreGateway().getId()));
		}
		if (cfgGtw.getModelloVersamento() != null) {
			gtwPojo.setModelloVersamento(EnumUtils.findByChiave(cfgGtw.getModelloVersamento(), EnumModelloVersamento.class));
		}
		if (cfgGtw.getCfgCommissionePagamenti()!=null &&  cfgGtw.getCfgCommissionePagamenti().size()>0) {
			gtwPojo.setImportoCommissioneMassima(cfgGtw.getCfgCommissionePagamenti().iterator().next().getDescrizione());
		}
		return gtwPojo;
	}

	public static Long modalitaPagamentoToIdModalita(EnumModalitaPagamento modalitaPagamento) {
		if (modalitaPagamento == null)
			return null;
		return   mapModalitaPagamento2Id.get(modalitaPagamento);
	}

	public static Long fornitoreGatewayToIdFornitoreGateway(EnumFornitoreGateway fornitoreGateway) {
		if (fornitoreGateway == null)
			return null;
		return  mapFornitoreGateway2Id.get(fornitoreGateway);
	}
}
