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

import it.gov.digitpa.schemas._2011.pagamenti.CtDatiMarcaBolloDigitale;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiSingoloVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDatiVersamentoRPT;
import it.gov.digitpa.schemas._2011.pagamenti.CtDominio;
import it.gov.digitpa.schemas._2011.pagamenti.CtEnteBeneficiario;
import it.gov.digitpa.schemas._2011.pagamenti.CtIdentificativoUnivocoPersonaFG;
import it.gov.digitpa.schemas._2011.pagamenti.CtIdentificativoUnivocoPersonaG;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoPagatore;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoVersante;
import it.gov.digitpa.schemas._2011.pagamenti.StAutenticazioneSoggetto;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivocoPersFG;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivocoPersG;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoVersamento;
import it.gov.digitpa.schemas._2011.ws.psp.PaaTipoDatiPagamentoPSP;
import it.govpay.bd.BasicBD;
import it.govpay.bd.pagamento.util.IuvUtils;
import it.govpay.model.Anagrafica;
import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Canale.TipoVersamento;
import it.govpay.bd.model.Canale;
import it.govpay.bd.model.Dominio;
import it.govpay.model.IbanAccredito;
import it.govpay.model.Portale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.model.Rpt.FirmaRichiesta;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.model.SingoloVersamento.TipoBollo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

public class RptBuilder {
	
	public Rpt buildRptAttivata (String codIntermediarioPsp, 
			String codPsp, 
			String codCanale, 
			Versamento versamento, 
			String iuv, 
			String ccp, 
			PaaTipoDatiPagamentoPSP datiPsp, BasicBD bd) throws ServiceException {
		
		return buildRpt(
				null,
				versamento,
				iuv,
				ccp,
				null,
				codIntermediarioPsp,
				codPsp,
				codCanale,
				TipoVersamento.ATTIVATO_PRESSO_PSP,
				ModelloPagamento.ATTIVATO_PRESSO_PSP,
				toOrm(datiPsp.getSoggettoVersante()),
				StAutenticazioneSoggetto.N_A.value(),
				datiPsp.getIbanAddebito(),
				datiPsp.getBicAddebito(),
				null,
				bd
				);
	}
	
	public Rpt buildRpt(
			String codCarrello, 
			Versamento versamento, 
			Canale canale,
			String iuv, 
			String ccp, 
			Portale portale, 
			Anagrafica versante, 
			String autenticazione, 
			String ibanAddebito, 
			String redirect, 
			BasicBD bd) throws ServiceException {
		
		if(canale == null)
			return buildRpt(
					codCarrello,
					versamento,
					iuv,
					ccp,
					portale,
					Rpt.codIntermediarioPspWISP20,
					Rpt.codPspWISP20,
					Rpt.codCanaleWISP20,
					Rpt.tipoVersamentoWISP20,
					Rpt.modelloPagamentoWISP20,
					versante,
					autenticazione,
					ibanAddebito,
					null,
					redirect,
					bd
					);
		else 
			return buildRpt(
					codCarrello,
					versamento,
					iuv,
					ccp,
					portale,
					canale.getPsp().getCodIntermediarioPsp(),
					canale.getPsp().getCodPsp(),
					canale.getCodCanale(),
					canale.getTipoVersamento(),
					ModelloPagamento.IMMEDIATO_MULTIBENEFICIARIO,
					versante,
					autenticazione,
					ibanAddebito,
					null,
					redirect,
					bd
					);
		
	}

	private Rpt buildRpt(
			String codCarrello, 
			Versamento versamento, 
			String iuv, 
			String ccp, 
			Portale portale, 
			String codIntermediarioPsp, 
			String codPsp, 
			String codCanale, 
			TipoVersamento tipoVersamento,
			ModelloPagamento modelloPagamento,
			Anagrafica versante, 
			String autenticazione, 
			String ibanAddebito, 
			String bicAddebito,
			String redirect, 
			BasicBD bd) throws ServiceException {
		Rpt rpt = new Rpt();
		rpt.setCallbackURL(redirect);
		rpt.setCcp(ccp);
		rpt.setCodCarrello(codCarrello);
		rpt.setCodDominio(versamento.getUo(bd).getDominio(bd).getCodDominio());
		rpt.setCodMsgRichiesta(buildUUID35());
		rpt.setCodSessione(null);
		rpt.setCodStazione(versamento.getUo(bd).getDominio(bd).getStazione(bd).getCodStazione());
		rpt.setDataAggiornamento(new Date());
		rpt.setDataMsgRichiesta(new Date());
		rpt.setDescrizioneStato(null);
		rpt.setId(null);
		rpt.setCodPsp(codPsp);
		rpt.setCodIntermediarioPsp(codIntermediarioPsp);
		rpt.setCodCanale(codCanale);
		rpt.setTipoVersamento(tipoVersamento);
		if(portale != null)
			rpt.setIdPortale(portale.getId());
		rpt.setIdVersamento(versamento.getId());
		rpt.setIuv(iuv);
		rpt.setModelloPagamento(modelloPagamento);
		rpt.setPspRedirectURL(null);
		rpt.setStato(StatoRpt.RPT_ATTIVATA);
		rpt.setIdTransazioneRpt(GpThreadLocal.get().getTransactionId());
		rpt.setVersamento(versamento);

		CtRichiestaPagamentoTelematico ctRpt = new CtRichiestaPagamentoTelematico();
		ctRpt.setVersioneOggetto(Rpt.VERSIONE);
		CtDominio ctDominio = new CtDominio();
		ctDominio.setIdentificativoDominio(rpt.getCodDominio());
		ctDominio.setIdentificativoStazioneRichiedente(versamento.getUo(bd).getDominio(bd).getStazione(bd).getCodStazione());
		ctRpt.setDominio(ctDominio);
		ctRpt.setIdentificativoMessaggioRichiesta(rpt.getCodMsgRichiesta());
		ctRpt.setDataOraMessaggioRichiesta(rpt.getDataMsgRichiesta());
		ctRpt.setAutenticazioneSoggetto(StAutenticazioneSoggetto.fromValue(autenticazione.toString()));
		ctRpt.setSoggettoVersante(buildSoggettoVersante(versante));
		ctRpt.setSoggettoPagatore(buildSoggettoPagatore(versamento.getAnagraficaDebitore()));
		ctRpt.setEnteBeneficiario(buildEnteBeneficiario(versamento.getUo(bd).getDominio(bd), versamento.getUo(bd), bd));
		
		CtDatiVersamentoRPT datiVersamento = new CtDatiVersamentoRPT();
		datiVersamento.setDataEsecuzionePagamento(rpt.getDataMsgRichiesta());
		datiVersamento.setImportoTotaleDaVersare(versamento.getImportoTotale());
		datiVersamento.setTipoVersamento(StTipoVersamento.fromValue(tipoVersamento.getCodifica()));
		datiVersamento.setIdentificativoUnivocoVersamento(rpt.getIuv());
		datiVersamento.setCodiceContestoPagamento(rpt.getCcp());
		datiVersamento.setFirmaRicevuta(FirmaRichiesta.NESSUNA.getCodifica());
		datiVersamento.setIbanAddebito(ibanAddebito != null ? ibanAddebito : null);
		datiVersamento.setBicAddebito(bicAddebito != null ? bicAddebito : null);
		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(bd);
		for (SingoloVersamento singoloVersamento : singoliVersamenti) {
			datiVersamento.getDatiSingoloVersamento().add(buildDatiSingoloVersamento(rpt, singoloVersamento, bd));
		}
		ctRpt.setDatiVersamento(datiVersamento);
		
		byte[] rptXml;
		try {
			rptXml = JaxbUtils.toByte(ctRpt);
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
		soggettoVersante.setAnagraficaVersante(getNotEmpty(versante.getRagioneSociale()));
		soggettoVersante.setCapVersante(getNotEmpty(versante.getCap()));
		soggettoVersante.setCivicoVersante(getNotEmpty(versante.getCivico()));
		soggettoVersante.setEMailVersante(getNotEmpty(versante.getEmail()));
		soggettoVersante.setIdentificativoUnivocoVersante(idUnivocoVersante);
		soggettoVersante.setIndirizzoVersante(getNotEmpty(versante.getIndirizzo()));
		soggettoVersante.setLocalitaVersante(getNotEmpty(versante.getLocalita()));
		soggettoVersante.setNazioneVersante(getNotEmpty(versante.getNazione()));
		soggettoVersante.setProvinciaVersante(getNotEmpty(versante.getProvincia()));
		return soggettoVersante;
	}

	private CtSoggettoPagatore buildSoggettoPagatore(Anagrafica debitore) {
		CtSoggettoPagatore soggettoDebitore = new CtSoggettoPagatore();
		CtIdentificativoUnivocoPersonaFG idUnivocoDebitore = new CtIdentificativoUnivocoPersonaFG();
		String cFiscale = debitore.getCodUnivoco();
		idUnivocoDebitore.setCodiceIdentificativoUnivoco(cFiscale);
		idUnivocoDebitore.setTipoIdentificativoUnivoco((cFiscale.length() == 16) ? StTipoIdentificativoUnivocoPersFG.F : StTipoIdentificativoUnivocoPersFG.G);
		soggettoDebitore.setAnagraficaPagatore(debitore.getRagioneSociale());
		soggettoDebitore.setCapPagatore(getNotEmpty(debitore.getCap()));
		soggettoDebitore.setCivicoPagatore(getNotEmpty(debitore.getCivico()));
		soggettoDebitore.setEMailPagatore(getNotEmpty(debitore.getEmail()));
		soggettoDebitore.setIdentificativoUnivocoPagatore(idUnivocoDebitore);
		soggettoDebitore.setIndirizzoPagatore(getNotEmpty(debitore.getIndirizzo()));
		soggettoDebitore.setLocalitaPagatore(getNotEmpty(debitore.getLocalita()));
		soggettoDebitore.setNazionePagatore(getNotEmpty(debitore.getNazione()));
		soggettoDebitore.setProvinciaPagatore(getNotEmpty(debitore.getProvincia()));
		return soggettoDebitore;
	}

	private CtEnteBeneficiario buildEnteBeneficiario(Dominio dominio, UnitaOperativa uo, BasicBD bd) throws ServiceException {

		CtEnteBeneficiario enteBeneficiario = new CtEnteBeneficiario();
		CtIdentificativoUnivocoPersonaG idUnivocoBeneficiario = new CtIdentificativoUnivocoPersonaG();
		idUnivocoBeneficiario.setCodiceIdentificativoUnivoco(dominio.getCodDominio());
		idUnivocoBeneficiario.setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersG.G);
		enteBeneficiario.setIdentificativoUnivocoBeneficiario(idUnivocoBeneficiario);
		enteBeneficiario.setDenominazioneBeneficiario(dominio.getRagioneSociale());

		try {
			Anagrafica anagrafica = dominio.getAnagrafica(bd);
			enteBeneficiario.setCapBeneficiario(getNotEmpty(anagrafica.getCap()));
			enteBeneficiario.setCivicoBeneficiario(getNotEmpty(anagrafica.getCivico()));
			enteBeneficiario.setIndirizzoBeneficiario(getNotEmpty(anagrafica.getIndirizzo()));
			enteBeneficiario.setLocalitaBeneficiario(getNotEmpty(anagrafica.getLocalita()));
			enteBeneficiario.setNazioneBeneficiario(getNotEmpty(anagrafica.getNazione()));
			enteBeneficiario.setProvinciaBeneficiario(getNotEmpty(anagrafica.getProvincia()));
		} catch (NotFoundException e) {
		}

		if(!uo.getCodUo().equals(Dominio.EC) && uo.getAnagrafica() != null) {
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

	private CtDatiSingoloVersamentoRPT buildDatiSingoloVersamento(Rpt rpt, SingoloVersamento singoloVersamento, BasicBD bd) throws ServiceException  {
		CtDatiSingoloVersamentoRPT datiSingoloVersamento = new CtDatiSingoloVersamentoRPT();
		datiSingoloVersamento.setImportoSingoloVersamento(singoloVersamento.getImportoSingoloVersamento());
		
		if(singoloVersamento.getIbanAccredito(bd) != null) {
			IbanAccredito ibanAccredito = singoloVersamento.getIbanAccredito(bd);
			datiSingoloVersamento.setIbanAccredito(getNotEmpty(ibanAccredito.getCodIban()));
			datiSingoloVersamento.setBicAccredito(getNotEmpty(ibanAccredito.getCodBic()));
			
			if(singoloVersamento.getIbanAppoggio(bd) != null) {
				datiSingoloVersamento.setIbanAppoggio(getNotEmpty(singoloVersamento.getIbanAccredito(bd).getCodIban()));
				datiSingoloVersamento.setBicAppoggio(getNotEmpty(singoloVersamento.getIbanAppoggio(bd).getCodBic()));
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
			
		}
		datiSingoloVersamento.setDatiSpecificiRiscossione(singoloVersamento.getTipoContabilita(bd).getCodifica() + "/" + singoloVersamento.getCodContabilita(bd));
		datiSingoloVersamento.setCausaleVersamento(buildCausaleSingoloVersamento(rpt.getIuv(), singoloVersamento.getImportoSingoloVersamento()));
		return datiSingoloVersamento;
	}

	private static final DecimalFormat nFormatter = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));

	private String buildCausaleSingoloVersamento(String iuv, BigDecimal importoTotale) {
		//Controllo se lo IUV che mi e' stato passato e' ISO11640:2011
		if(IuvUtils.checkISO11640(iuv))
			return "/RFS/" + iuv + "/" + nFormatter.format(importoTotale);
		else 
			return "/RFB/" + iuv + "/" + nFormatter.format(importoTotale);
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