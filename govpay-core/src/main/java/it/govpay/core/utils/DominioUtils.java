/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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

package it.govpay.core.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.SAXException;

import it.gov.digitpa.schemas._2011.psp.CtContiDiAccredito;
import it.gov.digitpa.schemas._2011.psp.CtErogazioneServizio;
import it.gov.digitpa.schemas._2011.psp.CtInfoContoDiAccreditoPair;
import it.gov.digitpa.schemas._2011.psp.InformativaContoAccredito;
import it.gov.digitpa.schemas._2011.psp.InformativaControparte;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IbanAccredito;

public class DominioUtils {
	
	public static byte[] buildInformativaControparte(Dominio dominio, boolean pagamentiPsp) throws JAXBException, SAXException, XMLStreamException, IOException {
		Date adesso = new Date();
		SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm:ss.sss");
		SimpleDateFormat idFormat = new SimpleDateFormat("yyyy-MM-dd");
		InformativaControparte informativaControparte = new InformativaControparte();
		informativaControparte.setDataInizioValidita(adesso);
		informativaControparte.setDataPubblicazione(adesso);
		informativaControparte.setIdentificativoDominio(dominio.getCodDominio());
		informativaControparte.setIdentificativoFlusso(idFormat.format(adesso) + dominio.getCodDominio() + "-" + hourFormat.format(adesso));
		informativaControparte.setRagioneSociale(dominio.getRagioneSociale());
		CtErogazioneServizio erogazioneServizio = new CtErogazioneServizio();
		informativaControparte.setErogazioneServizio(erogazioneServizio);
		informativaControparte.setPagamentiPressoPSP(pagamentiPsp ? 1 : 0);
		return JaxbUtils.toByte(informativaControparte);
	}
	
	public static byte[] buildInformativaContoAccredito(Dominio dominio, List<IbanAccredito> lstIban) throws JAXBException, SAXException, XMLStreamException, IOException {
		Date adesso = new Date();
		SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm:ss.sss");
		SimpleDateFormat idFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		InformativaContoAccredito informativaContoAccredito = new InformativaContoAccredito();
		
		if(lstIban != null && !lstIban.isEmpty()) {
			CtContiDiAccredito contiAccredito = new CtContiDiAccredito();
			for(IbanAccredito iban: lstIban) {
				if(!iban.isAbilitato()) continue;
				
				if(iban.getIdSellerBank() != null) {
					CtInfoContoDiAccreditoPair info = new CtInfoContoDiAccreditoPair();
					info.setIbanAccredito(iban.getCodIban());
					info.setIdBancaSeller(iban.getIdSellerBank());
					contiAccredito.getIbanAccreditosAndInfoContoDiAccreditoPairs().add(info);
				} else {
					contiAccredito.getIbanAccreditosAndInfoContoDiAccreditoPairs().add(iban.getCodIban());
				}
			}
			informativaContoAccredito.setContiDiAccredito(contiAccredito);
		}
		informativaContoAccredito.setDataInizioValidita(adesso);
		informativaContoAccredito.setDataPubblicazione(adesso);
		informativaContoAccredito.setIdentificativoDominio(dominio.getCodDominio());
		informativaContoAccredito.setIdentificativoFlusso(idFormat.format(adesso) + dominio.getCodDominio() + "-" + hourFormat.format(adesso));
		informativaContoAccredito.setRagioneSociale(dominio.getRagioneSociale());
		return JaxbUtils.toByte(informativaContoAccredito);
	}
}
