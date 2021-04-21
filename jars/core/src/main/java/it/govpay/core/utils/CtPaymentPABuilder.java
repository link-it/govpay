/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoVersante;
import it.gov.digitpa.schemas._2011.pagamenti.StAutenticazioneSoggetto;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtEntityUniqueIdentifier;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtPaymentPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtSubject;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferListPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.StEntityUniqueIdentifierType;
import it.gov.pagopa.pagopa_api.pa.pafornode.StOutcome;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.util.IuvUtils;
import it.govpay.model.Anagrafica;
import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Canale.TipoVersamento;
import it.govpay.model.IbanAccredito;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.model.Rpt.Versione;
import it.govpay.model.Versamento.CausaleSemplice;

public class CtPaymentPABuilder {
	
	public Rpt buildRptAttivata (String codIntermediarioPsp, 
			String codPsp, 
			String codCanale, 
			Versamento versamento, 
			String iuv, 
			String ccp) throws ServiceException {
		
		return this.buildRpt(
				null,
				versamento,
				iuv,
				ccp,
				codIntermediarioPsp,
				codPsp,
				codCanale,
				TipoVersamento.ATTIVATO_PRESSO_PSP,
				ModelloPagamento.ATTIVATO_PRESSO_PSP,
				StAutenticazioneSoggetto.N_A.value(),
				null
				);
	}
	
	private Rpt buildRpt(
			String codCarrello, 
			Versamento versamento, 
			String iuv, 
			String ccp, 
			String codIntermediarioPsp, 
			String codPsp, 
			String codCanale, 
			TipoVersamento tipoVersamento,
			ModelloPagamento modelloPagamento,
			String autenticazione, 
			String redirect) throws ServiceException {
		
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		
		Dominio dominio = versamento.getDominio(configWrapper); 
		UnitaOperativa uo = versamento.getUo(configWrapper);
		
		Rpt rpt = new Rpt();
		rpt.setVersione(Versione.SANP_240);
		rpt.setCallbackURL(redirect);
		rpt.setCcp(ccp);
		rpt.setCodCarrello(codCarrello);
		rpt.setCodDominio(dominio.getCodDominio());
		rpt.setCodMsgRichiesta(this.buildUUID35());
		rpt.setCodSessione(null);
		rpt.setCodStazione(dominio.getStazione().getCodStazione());
		rpt.setDataAggiornamento(new Date());
		rpt.setDataMsgRichiesta(new Date());
		rpt.setDescrizioneStato(null);
		rpt.setId(null);
		rpt.setCodPsp(codPsp);
		rpt.setCodIntermediarioPsp(codIntermediarioPsp);
		rpt.setCodCanale(codCanale);
		rpt.setTipoVersamento(tipoVersamento);
		rpt.setIdVersamento(versamento.getId());
		rpt.setVersamento(versamento);
		rpt.setIuv(iuv);
		rpt.setModelloPagamento(modelloPagamento);
		rpt.setPspRedirectURL(null);
		rpt.setStato(StatoRpt.RPT_ATTIVATA);
		rpt.setIdTransazioneRpt(ContextThreadLocal.get().getTransactionId());
		rpt.setBloccante(true);
		rpt.setEsitoPagamento(EsitoPagamento.IN_CORSO);
		
		PaGetPaymentRes paGetPaymentRes = new PaGetPaymentRes();
		CtPaymentPA ctRpt = new CtPaymentPA();
		
		/*
			Its contains all payment information :
		
			- `creditorReferenceId` : its equal to **IUV** _Identificativo Univoco Versamento_ 
			- `paymentAmount` : amount, it must be equal to the sums of `transferAmount` present in the `transferList`
			- `dueDate` : indicates the expiration payment date according to the ISO 8601 format `[YYYY]-[MM]-[DD]`.
			- `retentionDate` : indicates the retention payment date according to the ISO 8601 format `[YYYY]-[MM]-[DD]`.
			- `lastPayment` : boolean flag used for in installment payments 
			- `description` : free text available to describe the payment reasons
			- `companyName` : Public Administration full name
			- `officeName` : Public Admninistration Department Name
			- `debtor` : identifies the debtor to whom the debt position refers
			- `transferList` : the list of all available transfer information (_see below to details_)
			- `metadata` : (_see below to details_)
		*/
		ctRpt.setCompanyName(dominio.getRagioneSociale());
		ctRpt.setCreditorReferenceId(iuv);
		CtSubject debtor = this.buildSoggettoPagatore(versamento.getAnagraficaDebitore());
		ctRpt.setDebtor(debtor);
		ctRpt.setDueDate(versamento.getDataValidita()); // TODO  <!-- REQUIRED : entro quando è valido il pagamento - expiration data -->
		ctRpt.setLastPayment(true); // true se si tratta di rata unica o ultima rata
		ctRpt.setMetadata(null);
		if(uo != null && !uo.getCodUo().equals(it.govpay.model.Dominio.EC) && uo.getAnagrafica() != null) {
			ctRpt.setOfficeName(versamento.getUo(configWrapper).getAnagrafica().getRagioneSociale());
		}
		if(versamento.getCausaleVersamento() != null) {
			if(versamento.getCausaleVersamento() instanceof CausaleSemplice) {
				ctRpt.setDescription(((CausaleSemplice) versamento.getCausaleVersamento()).getCausale());
			}

//			if(versamento.getCausaleVersamento() instanceof CausaleSpezzoni) {
//				datiPagamento.setSpezzoniCausaleVersamento(new CtSpezzoniCausaleVersamento());
//				datiPagamento.getSpezzoniCausaleVersamento().getSpezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento().addAll(((CausaleSpezzoni) versamento.getCausaleVersamento()).getSpezzoni());
//			}
//
//			if(versamento.getCausaleVersamento() instanceof CausaleSpezzoniStrutturati) {
//				datiPagamento.setSpezzoniCausaleVersamento(new CtSpezzoniCausaleVersamento());
//				CausaleSpezzoniStrutturati causale = (CausaleSpezzoniStrutturati) versamento.getCausaleVersamento();
//				for(int i=0; i < causale.getSpezzoni().size(); i++) {
//					CtSpezzoneStrutturatoCausaleVersamento spezzone = new CtSpezzoneStrutturatoCausaleVersamento();
//					spezzone.setCausaleSpezzone(causale.getSpezzoni().get(i));
//					spezzone.setImportoSpezzone(causale.getImporti().get(i));
//					datiPagamento.getSpezzoniCausaleVersamento().getSpezzoneCausaleVersamentoOrSpezzoneStrutturatoCausaleVersamento().add(spezzone);
//				}
//			}
		} else {
			ctRpt.setDescription(" ");
		}
		ctRpt.setPaymentAmount(versamento.getImportoTotale());
		ctRpt.setRetentionDate(null); // <!-- fino a questa data non ci rigereremo verso la PA --> TODO ????
		
		CtTransferListPA transferList = new CtTransferListPA();
		ctRpt.setTransferList(transferList );
		
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
		int i=1;
		for (SingoloVersamento singoloVersamento : singoliVersamenti) {
			CtTransferPA transferEl = new CtTransferPA();
			
			/*
			Structure containing the details of possible tranfer payments.
		
			Currently set at 5 eligible payments per single position.
			
			Where each `transfer` items contains :
			
			- `idTransfer` : index of the list (from `1` to `5`) 
			- `transferAmount` : amount 
			- `fiscalCodePA` : Tax code of the public administration
			- `IBAN` : contains the IBAN of the account to be credited
			- `remittanceInformation` : reason for payment (_alias_ `causaleVersamento`)
			- `transferCategory` : contains the union of **CODICE TIPOLOGIA SERVIZIO** following by ***TIPO SERVIZIO* as define in [Tassonomia dei servizi](https://drive.google.com/file/d/13xOd__Qd4pwKHr3wjE-73NAB2O7UKmIt/view)
			
			 */
			
			transferEl.setFiscalCodePA(dominio.getCodDominio());
			
			if(singoloVersamento.getIbanAccredito(configWrapper) != null) {
				IbanAccredito ibanAccredito = singoloVersamento.getIbanAccredito(configWrapper);
				transferEl.setIBAN(this.getNotEmpty(ibanAccredito.getCodIban()));
			}
			
			transferEl.setIdTransfer(i);
			transferEl.setTransferAmount(singoloVersamento.getImportoSingoloVersamento());
			transferEl.setTransferCategory(singoloVersamento.getTipoContabilita(configWrapper).getCodifica() + "/" + singoloVersamento.getCodContabilita(configWrapper));
			transferEl.setRemittanceInformation(this.buildCausaleSingoloVersamento(rpt.getIuv(), singoloVersamento.getImportoSingoloVersamento(), singoloVersamento.getDescrizione(), singoloVersamento.getDescrizioneCausaleRPT()));
			
			transferList.getTransfer().add(transferEl );
			i++;
		}

		byte[] rptXml;
		paGetPaymentRes.setOutcome(StOutcome.OK);
		paGetPaymentRes.setData(ctRpt);
		try {
			rptXml = JaxbUtils.toByte(paGetPaymentRes);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		rpt.setXmlRpt(rptXml);
		return rpt;
	}

	private String buildUUID35() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	private CtSubject buildSoggettoPagatore(Anagrafica debitore) {
		CtSubject soggettoDebitore = new CtSubject();
		CtEntityUniqueIdentifier idUnivocoDebitore = new CtEntityUniqueIdentifier();
		String cFiscale = debitore.getCodUnivoco();
		idUnivocoDebitore.setEntityUniqueIdentifierValue(cFiscale);
		idUnivocoDebitore.setEntityUniqueIdentifierType((cFiscale.length() == 16) ? StEntityUniqueIdentifierType.F : StEntityUniqueIdentifierType.G);
		soggettoDebitore.setFullName(debitore.getRagioneSociale());
		soggettoDebitore.setPostalCode(this.getNotEmpty(debitore.getCap()));
		soggettoDebitore.setCivicNumber(this.getNotEmpty(debitore.getCivico()));
		soggettoDebitore.setEMail(this.getNotEmpty(debitore.getEmail()));
		soggettoDebitore.setUniqueIdentifier(idUnivocoDebitore);
		soggettoDebitore.setStreetName(this.getNotEmpty(debitore.getIndirizzo()));
		soggettoDebitore.setCity(this.getNotEmpty(debitore.getLocalita()));
		soggettoDebitore.setCountry(this.getNotEmpty(debitore.getNazione()));
		soggettoDebitore.setStateProvinceRegion(this.getNotEmpty(debitore.getProvincia()));
		return soggettoDebitore;
	}

	private String getNotEmpty(String text) {
		if(text == null || text.trim().isEmpty())
			return null;
		else
			return text;
	}

	private static final DecimalFormat nFormatter = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));

	private String buildCausaleSingoloVersamento(String iuv, BigDecimal importoTotale, String descrizione, String descrizioneCausaleRPT) {
		StringBuilder sb = new StringBuilder();
		//Controllo se lo IUV che mi e' stato passato e' ISO11640:2011
		if(IuvUtils.checkISO11640(iuv)) {
			sb.append("/RFS/");
		}else { 
			sb.append("/RFB/");
		}
		
		sb.append(iuv);
		sb.append("/");
		sb.append(nFormatter.format(importoTotale));
		if(StringUtils.isNotEmpty(descrizioneCausaleRPT)) {
			sb.append("/TXT/").append(descrizioneCausaleRPT);
		} else {
			if(StringUtils.isNotEmpty(descrizione)) {
				sb.append("/TXT/").append(descrizione);
			}
		}
		
		if(sb.toString().length() > 140)
			return sb.toString().substring(0, 140);
		
		return sb.toString();
	}

	private Anagrafica toOrm(CtSoggettoVersante soggettoVersante) {
		if(soggettoVersante == null) return null;
		Anagrafica anagrafica = new Anagrafica();
		anagrafica.setCap(soggettoVersante.getCapVersante());
		anagrafica.setCivico(soggettoVersante.getCivicoVersante());
		anagrafica.setCodUnivoco(soggettoVersante.getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco());
		anagrafica.setEmail(soggettoVersante.getEMailVersante());
		anagrafica.setIndirizzo(soggettoVersante.getIndirizzoVersante());
		anagrafica.setLocalita(soggettoVersante.getLocalitaVersante());
		anagrafica.setNazione(soggettoVersante.getNazioneVersante());
		anagrafica.setProvincia(soggettoVersante.getProvinciaVersante());
		anagrafica.setRagioneSociale(soggettoVersante.getAnagraficaVersante());
		return anagrafica;
	}
}
