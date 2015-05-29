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

import it.gov.digitpa.schemas._2011.psp.CtInformativaDetail;
import it.gov.digitpa.schemas._2011.psp.InformativaPSP;
import it.gov.digitpa.schemas._2011.psp.ListaInformativePSP;
import it.gov.digitpa.schemas._2011.psp.ObjectFactory;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediInformativaPSP;
import it.gov.digitpa.schemas._2011.ws.paa.NodoChiediInformativaPSPRisposta;
import it.govpay.ejb.core.exception.GovPayException;
import it.govpay.ejb.core.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.ejb.core.model.GatewayPagamentoModel;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumCanalePagamento;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumFornitoreGateway;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumModalitaPagamento;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumModelloVersamento;
import it.govpay.ejb.core.model.GatewayPagamentoModel.EnumStrumentoPagamento;
import it.govpay.ejb.ndp.ejb.AnagraficaDominioEJB;
import it.govpay.ejb.ndp.model.DominioEnteModel;
import it.govpay.ejb.ndp.util.wsclient.NodoPerPa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.activation.DataHandler;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.Logger;

@Stateless
public class PspController {
				
	/**
	 * Richiede la lista aggiornata al Nodo dei Pagamenti tramite
	 * il servizio nodoChiediInformativaPSP()
	 * 
	 * @throws GovPayException 
	 */

	@Inject
	AnagraficaDominioEJB anagraficaDominioEjb;
	
    @Inject  
    private transient Logger log;

	public List<GatewayPagamentoModel> chiediListaPsp(DominioEnteModel dominioEnte) throws GovPayException {
		
		log.info("Richiedo la lista psp per il dominio " + dominioEnte.getIdDominio());
		NodoChiediInformativaPSP richiesta = new NodoChiediInformativaPSP();
		richiesta.setIdentificativoDominio(dominioEnte.getIdDominio());
		richiesta.setIdentificativoIntermediarioPA(dominioEnte.getIntermediario().getIdIntermediarioPA());
		richiesta.setIdentificativoStazioneIntermediarioPA(dominioEnte.getStazione().getIdStazioneIntermediarioPA());
		richiesta.setPassword(dominioEnte.getStazione().getPassword());
		
		try {	
			NodoPerPa client = new NodoPerPa(dominioEnte.getIntermediario(), log);
			NodoChiediInformativaPSPRisposta risposta = client.nodoChiediInformativaPSP(richiesta);

			if(risposta.getFault() != null) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_NDP, risposta.getFault().getFaultCode() + ": " + risposta.getFault().getFaultString());
			}

			DataHandler dh= risposta.getXmlInformativa();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			dh.writeTo(output);

			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			ListaInformativePSP informativePsp = (ListaInformativePSP) jaxbUnmarshaller.unmarshal(risposta.getXmlInformativa().getDataSource().getInputStream());

			List<GatewayPagamentoModel> listaPsp = new ArrayList<GatewayPagamentoModel>();

			for(InformativaPSP informativaPsp : informativePsp.getInformativaPSPs()) {
				for(CtInformativaDetail informativaPspDetail : informativaPsp.getListaInformativaDetail().getInformativaDetails()) {
				
					GatewayPagamentoModel psp = new GatewayPagamentoModel();

					boolean isAttivo = informativaPsp.getInformativaMaster().getDataInizioValidita().toGregorianCalendar().before(new GregorianCalendar());

					psp.setBundleKey(informativaPsp.getIdentificativoPSP()+"-"+informativaPspDetail.getIdentificativoCanale()+"-"+informativaPspDetail.getTipoVersamento());
					log.debug("Recuperato il psp " + informativaPsp.getIdentificativoPSP()+"-"+informativaPspDetail.getIdentificativoCanale()+"-"+informativaPspDetail.getTipoVersamento());
					
					
					psp.setDescrizione(informativaPspDetail.getDescrizioneServizio());
					psp.setStato(isAttivo?GatewayPagamentoModel.EnumStato.ATTIVO:GatewayPagamentoModel.EnumStato.DISATTIVO);
					psp.setDataInizioValidita(informativaPsp.getInformativaMaster().getDataInizioValidita().toGregorianCalendar().getTime());
				
					psp.setDataFineValidita(new GregorianCalendar(2500, 01, 01).getTime());					
					
					psp.setSystemId(informativaPsp.getIdentificativoPSP());
					psp.setApplicationId(informativaPspDetail.getIdentificativoCanale()+"-"+informativaPspDetail.getTipoVersamento());
					psp.setDataPubblicazione(new Timestamp(informativaPsp.getInformativaMaster().getDataPubblicazione().toGregorianCalendar().getTimeInMillis()));
					psp.setSystemName(informativaPsp.getRagioneSociale());
					psp.setSubSystemId(informativaPspDetail.getIdentificativoIntermediario());
					psp.setPriorita(Integer.toString(informativaPspDetail.getPriorita()));
					psp.setDisponibilitaServizio(informativaPspDetail.getDisponibilitaServizio());
					psp.setFornitoreGateway(EnumFornitoreGateway.NODO_PAGAMENTI_SPC);
					psp.setDescrizioneGateway(informativaPsp.getRagioneSociale()); 
					psp.setImportoCommissioneMassima(informativaPspDetail.getCondizioniEconomicheMassime());
					psp.setStornoGestito(informativaPsp.getInformativaMaster().getStornoPagamento()!=0);		
															
					try {
						new URL(informativaPspDetail.getUrlInformazioniCanale());
						psp.setUrlInformazioniPsp(informativaPspDetail.getUrlInformazioniCanale());
					} catch (MalformedURLException e) {
						// Non e' una URL. non ci metto niente.
					}
					switch (informativaPspDetail.getModelloPagamento()) {
					case 0:
						psp.setModelloVersamento(EnumModelloVersamento.IMMEDIATO);
						break;
					case 1:
						psp.setModelloVersamento(EnumModelloVersamento.IMMEDIATO_MULTIBENEFICIARIO);
						break;
					case 2:
						psp.setModelloVersamento(EnumModelloVersamento.DIFFERITO);
						break;
					case 4:
						psp.setModelloVersamento(EnumModelloVersamento.ATTIVATO_PRESSO_PSP);
						break;
					}

					switch (informativaPspDetail.getTipoVersamento()) {
					case AD:
						psp.setModalitaPagamento(EnumModalitaPagamento.ADDEBITO_DIRETTO);
						psp.setStrumentoPagamento(EnumStrumentoPagamento.ADDEBITO_DIRETTO);
						psp.setCanalePagamento(EnumCanalePagamento.BANCA);
						psp.setDocumentoPagamento(null);				
						break;
					case BBT:
						psp.setModalitaPagamento(EnumModalitaPagamento.BONIFICO_BANCARIO_TESORERIA);
						psp.setStrumentoPagamento(EnumStrumentoPagamento.BONIFICO);
						psp.setCanalePagamento(EnumCanalePagamento.BANCA);
						psp.setDocumentoPagamento(null);				
						break;
					case BP:
						psp.setModalitaPagamento(EnumModalitaPagamento.BOLLETTINO_POSTALE);
						psp.setStrumentoPagamento(EnumStrumentoPagamento.BONIFICO);
						psp.setCanalePagamento(EnumCanalePagamento.POSTE);
						psp.setDocumentoPagamento(null);										
						break;
					case CP:
						psp.setModalitaPagamento(EnumModalitaPagamento.CARTA_PAGAMENTO);
						psp.setStrumentoPagamento(EnumStrumentoPagamento.CARTA_PAGAMENTO);
						psp.setCanalePagamento(EnumCanalePagamento.BANCA);
						psp.setDocumentoPagamento(null);										
						break;
					case PO:
						psp.setModalitaPagamento(EnumModalitaPagamento.ATTIVATO_PRESSO_PSP);
						psp.setStrumentoPagamento(EnumStrumentoPagamento.DOCUMENTO_PAGAMENTO);
						psp.setCanalePagamento(EnumCanalePagamento.PSP);
						psp.setDocumentoPagamento(null);										
						break;
					case OBEP:
						psp.setModalitaPagamento(EnumModalitaPagamento.MYBANK);
						psp.setStrumentoPagamento(EnumStrumentoPagamento.DOCUMENTO_PAGAMENTO);
						psp.setCanalePagamento(EnumCanalePagamento.PSP);
						psp.setDocumentoPagamento(null);										
						break;
					}
					

					listaPsp.add(psp);
				}
			}
			return listaPsp;
		} catch (IOException ioe) {
			// Impossibile serializzare la risposta
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, ioe);
		} catch (JAXBException jaxbe) {
			// Impossibile fare l'unmarshalling dei dati
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, jaxbe);
		}


	}

	
}
