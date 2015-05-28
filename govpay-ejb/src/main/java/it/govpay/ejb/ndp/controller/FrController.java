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
package it.govpay.ejb.ndp.controller;

import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediElencoFlussiRendicontazione;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediElencoFlussiRendicontazioneRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediFlussoRendicontazione;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediFlussoRendicontazioneRisposta;
import it.gov.digitpa.schemas._2011.ws.paa.TipoIdRendicontazione;
import it.govpay.ejb.core.controller.AnagraficaEJB;
import it.govpay.ejb.core.controller.DistintaEJB;
import it.govpay.ejb.core.exception.GovPayException;
import it.govpay.ejb.core.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.core.model.GatewayPagamentoModel;
import it.govpay.ejb.core.model.ScadenzarioModel;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumModalitaPagamento;
import it.govpay.ejb.core.utils.DataTypeUtils;
import it.govpay.ejb.ndp.ejb.AnagraficaDominioEJB;
import it.govpay.ejb.ndp.ejb.RendicontazioneEJB;
import it.govpay.ejb.ndp.model.DominioEnteModel;
import it.govpay.ejb.ndp.model.impl.FRModel;
import it.govpay.ejb.ndp.util.wsclient.NodoPerPa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;


@Stateless
public class FrController {

	@Inject
	AnagraficaEJB anagraficaEjb;

	@Inject
	AnagraficaDominioEJB anagraficaDominioEjb;

	@Inject
	RendicontazioneEJB rendicontazioneEjb;

	@Inject
	DistintaEJB distintaEjb;

	@Inject  
	private transient Logger log;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void downloadRendicontazioni(String idEnteCreditore, ScadenzarioModel scadenzario) throws GovPayException {

		DominioEnteModel dominioEnte = anagraficaDominioEjb.getDominioEnte(idEnteCreditore, scadenzario.getIdStazione());
		if(dominioEnte == null) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_CONFIGURAZIONE, "Non e' stata trovata la configurazione di dominio [IdEnteCreditore: " + idEnteCreditore + "]");
		}

		ThreadContext.put("dom", dominioEnte.getIdDominio());

		NodoPerPa client = new NodoPerPa(dominioEnte.getIntermediario(), log);

		// Richiedo la lista delle rendicontazioni per ciascun PSP
		List<GatewayPagamentoModel> gws = anagraficaEjb.getListaGatewayPagamento(EnumModalitaPagamento.ADDEBITO_DIRETTO, EnumModalitaPagamento.ATTIVATO_PRESSO_PSP, EnumModalitaPagamento.BOLLETTINO_POSTALE, EnumModalitaPagamento.BONIFICO_BANCARIO_TESORERIA, EnumModalitaPagamento.CARTA_PAGAMENTO);

		List<String> sids = new ArrayList<String>();
		for(GatewayPagamentoModel gw : gws) {
			if(sids.contains(gw.getSystemId())) continue;
			sids.add(gw.getSystemId());
			log.info("Download flussi rendicontazione dal psp [" + gw.getSystemId() + "] in corso.");
			try {
				NodoChiediElencoFlussiRendicontazione richiesta = new NodoChiediElencoFlussiRendicontazione();
				richiesta.setIdentificativoDominio(dominioEnte.getIdDominio());
				richiesta.setIdentificativoIntermediarioPA(dominioEnte.getIntermediario().getIdIntermediarioPA());
				richiesta.setIdentificativoStazioneIntermediarioPA(dominioEnte.getStazione().getIdStazioneIntermediarioPA()); 
				richiesta.setPassword(dominioEnte.getStazione().getPassword());
				richiesta.setIdentificativoPSP(gw.getSystemId());

				NodoChiediElencoFlussiRendicontazioneRisposta risposta;
				try {
					risposta = client.nodoChiediElencoFlussiRendicontazione(richiesta);
				} catch (Exception e) {
					// Errore nella richiesta. Loggo e continuo con il prossimo psp
					log.error("Richiesta elenco flussi rendicontazione per il psp [" + gw.getSystemId() + "] fallita: " + e);
					continue;
				}

				if(risposta.getFault() != null) {
					// Errore nella richiesta. Loggo e continuo con il prossimo psp
					log.error("Richiesta elenco flussi rendicontazione per il psp [" + gw.getSystemId() + "] fallita: " + risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString());
					continue;
				} else {
					log.debug("Ritornati " + risposta.getElencoFlussiRendicontazione().getTotRestituiti() + " flussi rendicontazione per il psp [" + gw.getSystemId() + "]");
					// Per ogni flusso della lista, vedo se ce l'ho gia' in DB ed in caso lo archivio
					for(TipoIdRendicontazione idRendicontazione : risposta.getElencoFlussiRendicontazione().getIdRendicontazione()) {
						String idFlusso = idRendicontazione.getIdentificativoFlusso();
						Date dataFlusso = DataTypeUtils.xmlGregorianCalendartoDate(idRendicontazione.getDataOraFlusso());
						if(!rendicontazioneEjb.existFlusso(dominioEnte.getIdDominio(), gw.getSystemId(), idFlusso)){
							log.debug("Individuato flusso non presente negli archivi: " + risposta.getElencoFlussiRendicontazione().getTotRestituiti() + " flussi rendicontazione per il psp [" + gw.getSystemId() + "]");
							// Il documento non e' stato archiviato. Lo richiedo e lo memorizzo
							NodoChiediFlussoRendicontazione richiestaFlusso = new NodoChiediFlussoRendicontazione();
							richiestaFlusso.setIdentificativoDominio(dominioEnte.getIdDominio());
							richiestaFlusso.setIdentificativoIntermediarioPA(dominioEnte.getIntermediario().getIdIntermediarioPA());
							richiestaFlusso.setIdentificativoStazioneIntermediarioPA(dominioEnte.getIntermediario().getIdIntermediarioPA()+"_01");
							richiestaFlusso.setPassword(dominioEnte.getStazione().getPassword());
							richiestaFlusso.setIdentificativoPSP(gw.getSystemId());
							richiestaFlusso.setIdentificativoFlusso(idFlusso);

							NodoChiediFlussoRendicontazioneRisposta rispostaFlusso;
							try {
								rispostaFlusso = client.nodoChiediFlussoRendicontazione(richiestaFlusso);
							} catch (Exception e) {
								// Errore nella richiesta. Loggo e continuo con il prossimo flusso
								log.error("Richiesta flusso  rendicontazione [" + idFlusso + "] per il psp [" + gw.getSystemId() + "] fallita: " + e);
								continue;
							}

							if(risposta.getFault() != null) {
								// Errore nella richiesta. Loggo e continuo con il prossimo flusso
								log.error("Richiesta flusso  rendicontazione [" + idFlusso + "] per il psp [" + gw.getSystemId() + "] fallita: " + risposta.getFault().getFaultCode() + " " + risposta.getFault().getFaultString());
							} else {
								ByteArrayOutputStream output = new ByteArrayOutputStream();
								try {
									DataHandler dh = rispostaFlusso.getXmlRendicontazione();
									dh.writeTo(output);
								} catch (IOException e) {
									log.error("Errore durante la lettura del flusso di rendicontazione: " + e);
									continue;
								}
								FRModel flussoModel = new FRModel(dominioEnte.getIdDominio(), gw.getSystemId(), idFlusso, dataFlusso, output.toByteArray());
								try {
									rendicontazioneEjb.addFlusso(flussoModel);
								} catch (Exception e) {
									log.error("Errore durante l'acquisizione del flusso: " + e);
									continue;
								}
							}
						}
					}
				}
			} catch (Exception e) {
				log.error("Errore durante la sincronizzazione dei flussi di rendicontazione per il PSP [" + gw.getSystemId() + "]", e);
				continue;
			}
		}
	}
}
