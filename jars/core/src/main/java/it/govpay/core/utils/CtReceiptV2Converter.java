/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import java.util.List;

import it.gov.pagopa.bizeventsservice.model.MapEntry;
import it.gov.pagopa.bizeventsservice.model.CtReceiptModelResponse;
import it.gov.pagopa.bizeventsservice.model.Debtor;
import it.gov.pagopa.bizeventsservice.model.Debtor.EntityUniqueIdentifierTypeEnum;
import it.gov.pagopa.bizeventsservice.model.Payer;
import it.gov.pagopa.bizeventsservice.model.TransferPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtEntityUniqueIdentifier;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtReceiptV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtSubject;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferListPAReceiptV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferPAReceiptV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTV2Request;
import it.gov.pagopa.pagopa_api.pa.pafornode.StEntityUniqueIdentifierType;
import it.gov.pagopa.pagopa_api.xsd.common_types.v1_0.CtMapEntry;
import it.gov.pagopa.pagopa_api.xsd.common_types.v1_0.CtMetadata;
import it.gov.pagopa.pagopa_api.xsd.common_types.v1_0.StOutcome;

/**
 * Utilities di conversione ricevuta dal formato ricevuto via API-REST al formato "nativo" via API-SOAP di cui sono gia' disponibili le funzioni di acquisizione
 * 
 * @author Pintori Giuliano (giuliano.pintori@link.it)
 * 
 */
public class CtReceiptV2Converter {
	
	private CtReceiptV2Converter() {}
	
	public static PaSendRTV2Request toPaSendRTV2Request(String codIntermediario, String codStazione, String codDominio, CtReceiptModelResponse response) {
		PaSendRTV2Request paSendRTV2Request = new PaSendRTV2Request();
		
		paSendRTV2Request.setIdBrokerPA(codIntermediario);
		paSendRTV2Request.setIdStation(codStazione);
		paSendRTV2Request.setIdPA(codDominio);
		paSendRTV2Request.setStandin(false);
		paSendRTV2Request.setReceipt(toCtReceiptV2(response));
		
		return paSendRTV2Request;
	}
	
	public static CtReceiptV2 toCtReceiptV2(CtReceiptModelResponse response) {
		CtReceiptV2 ctReceiptV2 = new CtReceiptV2();
		
		ctReceiptV2.setApplicationDate(SimpleDateFormatUtils.toDate(response.getApplicationDate()));
		ctReceiptV2.setChannelDescription(response.getChannelDescription());
		ctReceiptV2.setCompanyName(response.getCompanyName());
		ctReceiptV2.setCreditorReferenceId(response.getCreditorReferenceId());
		ctReceiptV2.setDebtor(toCtSubjectDebtor(response.getDebtor()));
		ctReceiptV2.setDescription(response.getDescription());
		ctReceiptV2.setFee(response.getFee());
		ctReceiptV2.setFiscalCode(response.getFiscalCode());
		ctReceiptV2.setIdBundle(response.getIdBundle());
		ctReceiptV2.setIdChannel(response.getIdChannel());
		ctReceiptV2.setIdCiBundle(response.getIdCiBundle());
		ctReceiptV2.setIdPSP(response.getIdPSP());
		ctReceiptV2.setMetadata(toCtReceiptV2Metadata(response.getMetadata()));
		ctReceiptV2.setNoticeNumber(response.getNoticeNumber());
		ctReceiptV2.setOfficeName(response.getOfficeName());
		if(response.getOutcome() != null) {
			ctReceiptV2.setOutcome(StOutcome.fromValue(response.getOutcome()));
		} else {
			ctReceiptV2.setOutcome(StOutcome.KO);
		}
		ctReceiptV2.setPayer(toCtSubjectPayer(response.getPayer()));
		ctReceiptV2.setPaymentAmount(response.getPaymentAmount());
		ctReceiptV2.setPaymentDateTime(SimpleDateFormatUtils.toDate(response.getPaymentDateTime()));
		ctReceiptV2.setPaymentMethod(response.getPaymentMethod());
		// ctReceiptV2.setPaymentNote(response.getpaymentNote()) NON PRESENTE
		ctReceiptV2.setPrimaryCiIncurredFee(response.getPrimaryCiIncurredFee());
		ctReceiptV2.setPSPCompanyName(response.getPspCompanyName());
		ctReceiptV2.setPspFiscalCode(response.getPspFiscalCode());
		ctReceiptV2.setPspPartitaIVA(response.getPspPartitaIVA());
		ctReceiptV2.setReceiptId(response.getReceiptId());
		ctReceiptV2.setTransferDate(SimpleDateFormatUtils.toDate(response.getTransferDate()));
		ctReceiptV2.setTransferList(toCtTransferListPAReceiptV2(response.getTransferList()));
		
		return ctReceiptV2;
	}

	private static CtTransferListPAReceiptV2 toCtTransferListPAReceiptV2(List<TransferPA> transferList) {
		if(transferList == null) {
			return null;
		}
		
		CtTransferListPAReceiptV2 ctTransferListPAReceiptV2 = new CtTransferListPAReceiptV2();
		
		for (TransferPA TransferPA : transferList) {
			
			CtTransferPAReceiptV2 ctTransferPAReceiptV2 = new CtTransferPAReceiptV2();
			
			//ctTransferPAReceiptV2.setCompanyName(TransferPA.getCompanyName()) NON PRESENTE
			ctTransferPAReceiptV2.setFiscalCodePA(TransferPA.getFiscalCodePA());
			ctTransferPAReceiptV2.setIBAN(TransferPA.getIban());
			ctTransferPAReceiptV2.setIdTransfer(TransferPA.getIdTransfer());
			if(TransferPA.getMbdAttachment() != null) {
				ctTransferPAReceiptV2.setMBDAttachment(TransferPA.getMbdAttachment().getBytes());
			}
			ctTransferPAReceiptV2.setMetadata(toCtReceiptV2Metadata(TransferPA.getMetadata()));
			ctTransferPAReceiptV2.setRemittanceInformation(TransferPA.getRemittanceInformation());
			ctTransferPAReceiptV2.setTransferAmount(TransferPA.getTransferAmount());
			ctTransferPAReceiptV2.setTransferCategory(TransferPA.getTransferCategory());
			
			
			ctTransferListPAReceiptV2.getTransfer().add(ctTransferPAReceiptV2 );
		}
		
		
		return ctTransferListPAReceiptV2;
	}

	private static CtMetadata toCtReceiptV2Metadata(List<MapEntry> metadata) {
		if(metadata == null) {
			return null;
		}
		
		CtMetadata ctMetadata = new CtMetadata();
		
		for (MapEntry MapEntry : metadata) {
			CtMapEntry ctMapEntry = new CtMapEntry();
			
			ctMapEntry.setKey(MapEntry.getKey());
			ctMapEntry.setValue(MapEntry.getValue());
			
			ctMetadata.getMapEntry().add(ctMapEntry);
		}
		
		return ctMetadata;
	}

	private static CtSubject toCtSubjectDebtor(Debtor debtor) {
		if(debtor == null) {
			return null;
		}
		
		CtSubject ctSubject = new CtSubject();
		
		ctSubject.setCity(debtor.getCity());
		ctSubject.setCivicNumber(debtor.getCivicNumber());
		ctSubject.setCountry(debtor.getCountry());
		ctSubject.setEMail(debtor.getEmail());
		ctSubject.setFullName(debtor.getFullName());
		ctSubject.setPostalCode(debtor.getPostalCode());
		ctSubject.setStateProvinceRegion(debtor.getStateProvinceRegion());
		ctSubject.setStreetName(debtor.getStreetName());
		CtEntityUniqueIdentifier uniqueIdentifier = new CtEntityUniqueIdentifier();
		if(debtor.getEntityUniqueIdentifierType() != null) {
			if(debtor.getEntityUniqueIdentifierType().compareTo(EntityUniqueIdentifierTypeEnum.F) == 0) {
				uniqueIdentifier.setEntityUniqueIdentifierType(StEntityUniqueIdentifierType.F);
			} else {
				uniqueIdentifier.setEntityUniqueIdentifierType(StEntityUniqueIdentifierType.G);
			}
		}
		uniqueIdentifier.setEntityUniqueIdentifierValue(debtor.getEntityUniqueIdentifierValue());
		ctSubject.setUniqueIdentifier(uniqueIdentifier );
		
		return ctSubject;
	}
	
	private static CtSubject toCtSubjectPayer(Payer payer) {
		if(payer == null) {
			return null;
		}
		
		CtSubject ctSubject = new CtSubject();
		
		ctSubject.setCity(payer.getCity());
		ctSubject.setCivicNumber(payer.getCivicNumber());
		ctSubject.setCountry(payer.getCountry());
		ctSubject.setEMail(payer.getEmail());
		ctSubject.setFullName(payer.getFullName());
		ctSubject.setPostalCode(payer.getPostalCode());
		ctSubject.setStateProvinceRegion(payer.getStateProvinceRegion());
		ctSubject.setStreetName(payer.getStreetName());
		CtEntityUniqueIdentifier uniqueIdentifier = new CtEntityUniqueIdentifier();
		if(payer.getEntityUniqueIdentifierType() != null) {
			if(payer.getEntityUniqueIdentifierType().compareTo(it.gov.pagopa.bizeventsservice.model.Payer.EntityUniqueIdentifierTypeEnum.F) == 0) {
				uniqueIdentifier.setEntityUniqueIdentifierType(StEntityUniqueIdentifierType.F);
			} else {
				uniqueIdentifier.setEntityUniqueIdentifierType(StEntityUniqueIdentifierType.G);
			}
		}
		uniqueIdentifier.setEntityUniqueIdentifierValue(payer.getEntityUniqueIdentifierValue());
		ctSubject.setUniqueIdentifier(uniqueIdentifier );
		
		return ctSubject;
	}
}
