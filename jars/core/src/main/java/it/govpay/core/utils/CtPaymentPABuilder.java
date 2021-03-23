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

import gov.telematici.pagamenti.ws.ccp.PaaTipoDatiPagamentoPSP;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiMarcaBolloDigitale;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtEnteBeneficiario;
import it.gov.digitpa.schemas._2011.pagamenti.CtIdentificativoUnivocoPersonaFG;
import it.gov.digitpa.schemas._2011.pagamenti.CtIdentificativoUnivocoPersonaG;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoVersante;
import it.gov.digitpa.schemas._2011.pagamenti.StAutenticazioneSoggetto;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivocoPersFG;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivocoPersG;
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
import it.govpay.model.SingoloVersamento.TipoBollo;
import it.govpay.model.Versamento.CausaleSemplice;

public class CtPaymentPABuilder {
	
	public Rpt buildRptAttivata (String codIntermediarioPsp, 
			String codPsp, 
			String codCanale, 
			Versamento versamento, 
			String iuv, 
			String ccp, 
			PaaTipoDatiPagamentoPSP datiPsp) throws ServiceException {
		
		
		
		
		CtSoggettoVersante soggettoVersante = datiPsp != null ? datiPsp.getSoggettoVersante() : null;
		String ibanAddebito = datiPsp.getIbanAddebito() != null ?  datiPsp.getIbanAddebito() : null;
		String bicAddebito = datiPsp.getBicAddebito() != null ? datiPsp.getBicAddebito() : null;
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
				this.toOrm(soggettoVersante),
				StAutenticazioneSoggetto.N_A.value(),
				ibanAddebito,
				bicAddebito,
				null
				);
	}
	
//	public Rpt buildRpt(
//			String codCarrello, 
//			Versamento versamento, 
//			Canale canale,
//			String iuv, 
//			String ccp, 
//			Anagrafica versante, 
//			String autenticazione, 
//			String ibanAddebito, 
//			String redirect) throws ServiceException {
//		
//		return this.buildRpt(
//				codCarrello,
//				versamento,
//				iuv,
//				ccp,
//				it.govpay.model.Rpt.codIntermediarioPspWISP20,
//				it.govpay.model.Rpt.codPspWISP20,
//				it.govpay.model.Rpt.codCanaleWISP20,
//				it.govpay.model.Rpt.tipoVersamentoWISP20,
//				it.govpay.model.Rpt.modelloPagamentoWISP20,
//				versante,
//				autenticazione,
//				ibanAddebito,
//				null,
//				redirect
//				);
//		
//	}

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
			Anagrafica versante, 
			String autenticazione, 
			String ibanAddebito, 
			String bicAddebito,
			String redirect) throws ServiceException {
		
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		
		Dominio dominio = versamento.getDominio(configWrapper); 
		UnitaOperativa uo = versamento.getUo(configWrapper);
		
		Rpt rpt = new Rpt();
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
		
		ctRpt.setCompanyName(dominio.getRagioneSociale());
		ctRpt.setCreditorReferenceId(iuv);
		CtSubject debtor = this.buildSoggettoPagatore(versamento.getAnagraficaDebitore());
		ctRpt.setDebtor(debtor);
		ctRpt.setDueDate(versamento.getDataValidita()); // TODO
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

	private CtSoggettoVersante buildSoggettoVersante(Anagrafica versante) {
		if(versante == null) return null;
		CtSoggettoVersante soggettoVersante = new CtSoggettoVersante();
		CtIdentificativoUnivocoPersonaFG idUnivocoVersante = new CtIdentificativoUnivocoPersonaFG();
		String cFiscale = versante.getCodUnivoco();
		idUnivocoVersante.setCodiceIdentificativoUnivoco(cFiscale);
		idUnivocoVersante.setTipoIdentificativoUnivoco((cFiscale.length() == 16) ? StTipoIdentificativoUnivocoPersFG.F : StTipoIdentificativoUnivocoPersFG.G);
		soggettoVersante.setAnagraficaVersante(this.getNotEmpty(versante.getRagioneSociale()));
		soggettoVersante.setCapVersante(this.getNotEmpty(versante.getCap()));
		soggettoVersante.setCivicoVersante(this.getNotEmpty(versante.getCivico()));
		soggettoVersante.setEMailVersante(this.getNotEmpty(versante.getEmail()));
		soggettoVersante.setIdentificativoUnivocoVersante(idUnivocoVersante);
		soggettoVersante.setIndirizzoVersante(this.getNotEmpty(versante.getIndirizzo()));
		soggettoVersante.setLocalitaVersante(this.getNotEmpty(versante.getLocalita()));
		soggettoVersante.setNazioneVersante(this.getNotEmpty(versante.getNazione()));
		soggettoVersante.setProvinciaVersante(this.getNotEmpty(versante.getProvincia()));
		return soggettoVersante;
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

	private CtEnteBeneficiario buildEnteBeneficiario(Dominio dominio, UnitaOperativa uo) throws ServiceException {

		CtEnteBeneficiario enteBeneficiario = new CtEnteBeneficiario();
		CtIdentificativoUnivocoPersonaG idUnivocoBeneficiario = new CtIdentificativoUnivocoPersonaG();
		idUnivocoBeneficiario.setCodiceIdentificativoUnivoco(dominio.getCodDominio());
		idUnivocoBeneficiario.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersG.G);
		enteBeneficiario.setIdentificativoUnivocoBeneficiario(idUnivocoBeneficiario);
		enteBeneficiario.setDenominazioneBeneficiario(dominio.getRagioneSociale());

		Anagrafica anagrafica = dominio.getAnagrafica();
		enteBeneficiario.setCapBeneficiario(this.getNotEmpty(anagrafica.getCap()));
		enteBeneficiario.setCivicoBeneficiario(this.getNotEmpty(anagrafica.getCivico()));
		enteBeneficiario.setIndirizzoBeneficiario(this.getNotEmpty(anagrafica.getIndirizzo()));
		enteBeneficiario.setLocalitaBeneficiario(this.getNotEmpty(anagrafica.getLocalita()));
		enteBeneficiario.setNazioneBeneficiario(this.getNotEmpty(anagrafica.getNazione()));
		enteBeneficiario.setProvinciaBeneficiario(this.getNotEmpty(anagrafica.getProvincia()));

		if(!uo.getCodUo().equals(it.govpay.model.Dominio.EC) && uo.getAnagrafica() != null) {
			if(uo.getAnagrafica().getCodUnivoco() != null && uo.getAnagrafica().getCodUnivoco().trim().length()>0)
				enteBeneficiario.setCodiceUnitOperBeneficiario(uo.getAnagrafica().getCodUnivoco());
			if(uo.getAnagrafica().getRagioneSociale() != null && uo.getAnagrafica().getRagioneSociale().trim().length()>0)
				enteBeneficiario.setDenomUnitOperBeneficiario(uo.getAnagrafica().getRagioneSociale());
			if(uo.getAnagrafica().getIndirizzo() != null && uo.getAnagrafica().getIndirizzo().trim().length()>0)
				enteBeneficiario.setIndirizzoBeneficiario(uo.getAnagrafica().getIndirizzo());
			if(uo.getAnagrafica().getCivico() != null && uo.getAnagrafica().getCivico().trim().length()>0)
				enteBeneficiario.setCivicoBeneficiario(uo.getAnagrafica().getCivico());
			if(uo.getAnagrafica().getCap() != null && uo.getAnagrafica().getCap().trim().length()>0)
				enteBeneficiario.setCapBeneficiario(uo.getAnagrafica().getCap());
			if(uo.getAnagrafica().getLocalita() != null && uo.getAnagrafica().getLocalita().trim().length()>0)
				enteBeneficiario.setLocalitaBeneficiario(uo.getAnagrafica().getLocalita());
			if(uo.getAnagrafica().getProvincia() != null && uo.getAnagrafica().getProvincia().trim().length()>0)
				enteBeneficiario.setProvinciaBeneficiario(uo.getAnagrafica().getProvincia());
			if(uo.getAnagrafica().getNazione() != null && uo.getAnagrafica().getNazione().trim().length()>0)
				enteBeneficiario.setNazioneBeneficiario(uo.getAnagrafica().getNazione());
		}
		return enteBeneficiario;
	}

	private String getNotEmpty(String text) {
		if(text == null || text.trim().isEmpty())
			return null;
		else
			return text;
	}

	private CtDatiSingoloVersamentoRPT buildDatiSingoloVersamento(Rpt rpt, SingoloVersamento singoloVersamento, BDConfigWrapper configWrapper) throws ServiceException  {
		CtDatiSingoloVersamentoRPT datiSingoloVersamento = new CtDatiSingoloVersamentoRPT();
		datiSingoloVersamento.setImportoSingoloVersamento(singoloVersamento.getImportoSingoloVersamento());
		
		if(singoloVersamento.getIbanAccredito(configWrapper) != null) {
			IbanAccredito ibanAccredito = singoloVersamento.getIbanAccredito(configWrapper);
			datiSingoloVersamento.setBicAccredito(this.getNotEmpty(ibanAccredito.getCodBic()));
			datiSingoloVersamento.setIbanAccredito(this.getNotEmpty(ibanAccredito.getCodIban()));
			
			if(singoloVersamento.getIbanAppoggio(configWrapper) != null) {
				IbanAccredito ibanAppoggio = singoloVersamento.getIbanAppoggio(configWrapper);
				datiSingoloVersamento.setBicAppoggio(this.getNotEmpty(ibanAppoggio.getCodBic()));
				datiSingoloVersamento.setIbanAppoggio(this.getNotEmpty(ibanAppoggio.getCodIban()));
			}
		} else {
			CtDatiMarcaBolloDigitale marcaBollo = new CtDatiMarcaBolloDigitale();
			marcaBollo.setHashDocumento(singoloVersamento.getHashDocumento());
			marcaBollo.setProvinciaResidenza(singoloVersamento.getProvinciaResidenza());
			if(singoloVersamento.getTipoBollo() != null)
				marcaBollo.setTipoBollo(singoloVersamento.getTipoBollo().getCodifica());
			else
				marcaBollo.setTipoBollo(TipoBollo.IMPOSTA_BOLLO.getCodifica());
			datiSingoloVersamento.setDatiMarcaBolloDigitale(marcaBollo);
			datiSingoloVersamento.setDatiSpecificiRiscossione(singoloVersamento.getTipoContabilita(configWrapper).getCodifica() + "/" + singoloVersamento.getCodContabilita(configWrapper));
		}
		datiSingoloVersamento.setDatiSpecificiRiscossione(singoloVersamento.getTipoContabilita(configWrapper).getCodifica() + "/" + singoloVersamento.getCodContabilita(configWrapper));
		datiSingoloVersamento.setCausaleVersamento(this.buildCausaleSingoloVersamento(rpt.getIuv(), singoloVersamento.getImportoSingoloVersamento(), singoloVersamento.getDescrizione(), singoloVersamento.getDescrizioneCausaleRPT()));
		return datiSingoloVersamento;
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
