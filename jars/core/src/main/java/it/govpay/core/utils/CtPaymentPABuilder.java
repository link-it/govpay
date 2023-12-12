/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.gov.digitpa.schemas._2011.pagamenti.StAutenticazioneSoggetto;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtEntityUniqueIdentifier;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtPaymentPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtPaymentPAV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtSubject;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferListPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferListPAV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferPAV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentV2Request;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentV2Response;
import it.gov.pagopa.pagopa_api.pa.pafornode.StEntityUniqueIdentifierType;
import it.gov.pagopa.pagopa_api.pa.pafornode.StTransferType;
import it.gov.pagopa.pagopa_api.xsd.common_types.v1_0.CtMapEntry;
import it.gov.pagopa.pagopa_api.xsd.common_types.v1_0.CtMetadata;
import it.gov.pagopa.pagopa_api.xsd.common_types.v1_0.CtRichiestaMarcaDaBollo;
import it.gov.pagopa.pagopa_api.xsd.common_types.v1_0.StOutcome;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.core.utils.tracciati.TracciatiNotificaPagamentiUtils;
import it.govpay.model.Anagrafica;
import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Canale.TipoVersamento;
import it.govpay.model.IbanAccredito;
import it.govpay.model.QuotaContabilita;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.model.Rpt.VersioneRPT;
import it.govpay.model.SingoloVersamento.TipoBollo;
import it.govpay.model.Versamento.CausaleSemplice;
import it.govpay.pagopa.beans.utils.JaxbUtils;

public class CtPaymentPABuilder {

	public static final String CONTABILITA_QUOTA_TIPO_CONTABILIZZAZIONE_PARAM_KEY = "RC_TIP_{0}";
	public static final String CONTABILITA_QUOTA_TIPO_CONTABILIZZAZIONE_PARAM_VALUE_DL118 = "CORRISPETTIVO_DL118";
	public static final String CONTABILITA_QUOTA_TIPO_CONTABILIZZAZIONE_PARAM_VALUE_TIPICO = "INCASSO_TIPICO";
	public static final String CONTABILITA_QUOTA_TIPO_CONTABILIZZAZIONE_PARAM_CIVILISTICO = "CIVILISTICO";
	// Corrispettivo DL 118
	public static final String CONTABILITA_QUOTA_ANNO_COMPETENZA_PARAM_KEY = "RC_AC_{0}";
	public static final String CONTABILITA_QUOTA_CODICE_UFFICIO_PARAM_KEY = "RC_CU_{0}";
	public static final String CONTABILITA_QUOTA_CAPITOLO_PARAM_KEY = "RC_CAP_{0}";
	public static final String CONTABILITA_QUOTA_ACCERTAMENTO_PARAM_KEY = "RC_ACC_{0}";
	public static final String CONTABILITA_QUOTA_ARTICOLO_PARAM_KEY = "RC_ART_{0}";
	public static final String CONTABILITA_QUOTA_FP5LIVELLO_PARAM_KEY = "RC_PF5_{0}";
	public static final String CONTABILITA_QUOTA_IMPORTO_PARAM_KEY = "RC_IMP_{0}";
	// Incasso Tipico
	public static final String CONTABILITA_QUOTA_TIPO_INCASSO_PARAM_KEY = "RC_TI_{0}";
	public static final String CONTABILITA_QUOTA_EMISSIONE_FATTURA_PARAM_KEY = "RC_EF_{0}";
	public static final String CONTABILITA_QUOTA_NUMERO_DOCUMENTO_PARAM_KEY = "RC_ND_{0}";
	// Civilistico
	public static final String CONTABILITA_QUOTA_CONTO_PARAM_KEY = "RC_CON_{0}";
	public static final String CONTABILITA_QUOTA_COMMESSA_PARAM_KEY = "RC_COM_{0}";

	// vecchie chiavi
	public static final String CONTABILITA_QUOTA_CAPITOLO_BILANCIO_KEY = "CAPITOLOBILANCIO";
	public static final String CONTABILITA_QUOTA_ARTICOLO_BILANCIO_KEY = "ARTICOLOBILANCIO";
	public static final String CONTABILITA_QUOTA_CODICE_ACCERTAMENTO_KEY = "CODICEACCERTAMENTO";
	public static final String CONTABILITA_QUOTA_ANNO_RIFERIMENTO_KEY = "ANNORIFERIMENTO";
	public static final String CONTABILITA_QUOTA_TITOLO_BILANCIO_KEY = "TITOLOBILANCIO";
	public static final String CONTABILITA_QUOTA_CATEGORIA_BILANCIO_KEY = "CATEGORIABILANCIO";
	public static final String CONTABILITA_QUOTA_TIPOLOGIA_BILANCIO_KEY = "TIPOLOGIABILANCIO";
	public static final String CONTABILITA_QUOTA_ENTRY_KEY = "CAPITOLOBILANCIO,ARTICOLOBILANCIO,CODICEACCERTAMENTO,ANNORIFERIMENTO,TITOLOBILANCIO,CATEGORIABILANCIO,TIPOLOGIABILANCIO,IMPORTOEUROCENT";

	public Rpt buildRptAttivata (PaGetPaymentReq requestBody, Versamento versamento, String iuv, String ccp, String numeroavviso) throws ServiceException, IOException {

		return this.buildRpt(requestBody,
				null,
				versamento,
				iuv,
				ccp,
				numeroavviso,
				TipoVersamento.ATTIVATO_PRESSO_PSP,
				ModelloPagamento.ATTIVATO_PRESSO_PSP,
				StAutenticazioneSoggetto.N_A.value(),
				null
				);
	}

	private Rpt buildRpt(PaGetPaymentReq requestBody,
			String codCarrello, 
			Versamento versamento, 
			String iuv, 
			String ccp, 
			String numeroavviso, 
			TipoVersamento tipoVersamento,
			ModelloPagamento modelloPagamento,
			String autenticazione, 
			String redirect) throws ServiceException, IOException {

		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

		Dominio dominio = versamento.getDominio(configWrapper); 
		UnitaOperativa uo = versamento.getUo(configWrapper);

		Rpt rpt = new Rpt();
		rpt.setVersione(VersioneRPT.SANP_240);
		rpt.setCallbackURL(redirect);
		rpt.setCodCarrello(codCarrello);
		rpt.setCodDominio(dominio.getCodDominio());
		rpt.setCodMsgRichiesta(this.buildUUID35());
		rpt.setCodSessione(null);
		rpt.setCodStazione(dominio.getStazione().getCodStazione());
		rpt.setDataAggiornamento(new Date());
		rpt.setDataMsgRichiesta(new Date());
		rpt.setDescrizioneStato(null);
		rpt.setId(null);
		rpt.setTipoVersamento(tipoVersamento);
		rpt.setIdVersamento(versamento.getId());
		rpt.setVersamento(versamento);
		rpt.setIuv(iuv);
		rpt.setCcp(ccp);
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

		ctRpt.setCreditorReferenceId(iuv);

		// L'amount con il dueDate mi deve far scegliere tra le opzioni di pagamento
		// da attivare. Al momento viene gestita una sola opzione di pagamento
		// pertanto ignoro l'importo comunicato e rispondo sempre con il dovuto.
		// https://github.com/pagopa/pagopa-api/issues/194

		ctRpt.setPaymentAmount(versamento.getImportoTotale());
		ctRpt.setDueDate(calcolaDueDate(versamento));

		// Capire se il numero avviso utilizzato e' relativo alla rata di un documento, 
		// nel caso sia l'ultima valorizzare true altrimenti e' sempre false
		// se non e' una rata o rata unica e' sempre true. 
		ctRpt.setLastPayment(true); 

		if(versamento.getCausaleVersamento() != null) {
			if(versamento.getCausaleVersamento() instanceof CausaleSemplice) {
				ctRpt.setDescription(((CausaleSemplice) versamento.getCausaleVersamento()).getCausale());
			}
		} else {
			ctRpt.setDescription(" ");
		}

		ctRpt.setCompanyName(dominio.getRagioneSociale());

		if(uo != null && !uo.getCodUo().equals(it.govpay.model.Dominio.EC) && uo.getAnagrafica() != null) {
			ctRpt.setOfficeName(versamento.getUo(configWrapper).getAnagrafica().getRagioneSociale());
		}

		CtSubject debtor = this.buildSoggettoPagatore(versamento.getAnagraficaDebitore());
		ctRpt.setDebtor(debtor);
		ctRpt.setMetadata(null);

		CtTransferListPA transferList = new CtTransferListPA();
		ctRpt.setTransferList(transferList );

		boolean utilizzaIBANPostali = false;
		StTransferType transferType = requestBody.getTransferType();
		if(transferType != null && transferType.equals(StTransferType.POSTAL)) {
			utilizzaIBANPostali = true;
		}

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
			- `transferCategory` : contains taxonomic code, composed by `Codice tipo Ente Creditore`+`Progressivo macro area`+`Codice tipologia servizio`+`Motivo Giuridico` ( ex. `0101002IM` ) 
			| Segment                     | Regex                       |Example |
			|-----------------------------|-----------------------------|--------|
			|Codice tipo Ente Creditore   | `\d{2}`                     | 01     |
			|Progressivo macro area       | `\d{2}`                     | 01     |
			|Codice tipologia servizio    | `\d{2}`                     | 002    |
			|Motivo Giuridico             | `\w{2}`                     | IM     |

			 */

			transferEl.setIdTransfer(i);
			transferEl.setTransferAmount(singoloVersamento.getImportoSingoloVersamento());


			// Imposto IBAN e codice fiscale ente proprietario dell'iban

			// sv con tributo definito
			it.govpay.bd.model.Tributo tributo = singoloVersamento.getTributo(configWrapper);
			IbanAccredito ibanScelto = null;
			IbanAccredito ibanAccredito = null;
			IbanAccredito ibanAppoggio = null;
			if(tributo != null) {
				ibanAccredito = tributo.getIbanAccredito();
				ibanAppoggio = tributo.getIbanAppoggio();
			} else { // sv con le informazioni tributo direttamente nei dati 
				ibanAccredito = singoloVersamento.getIbanAccredito(configWrapper);
				ibanAppoggio = singoloVersamento.getIbanAppoggio(configWrapper);
			}

			// solo il transfer 1 e' vincolato ad essere postale se richiesto
			if(i == 1) {
				if(ibanAccredito != null) {
					ibanScelto = ibanAccredito;

					// devo utilizzare iban postale
					if(utilizzaIBANPostali) {
						if(!ibanAccredito.isPostale()) {
							// provo a verificare se l'iban appoggio e' postale
							if(ibanAppoggio != null && ibanAppoggio.isPostale()) {
								ibanScelto = ibanAppoggio;
							}
						} 
					}
				}  else if(ibanAppoggio != null) {
					// se capito qua dentro ho solo l'iban di appoggio, quindi devo utilizzarlo sia che sia postale o meno 
					ibanScelto = ibanAppoggio;
				}
			} else {
				// altri transfer
				if(ibanAccredito != null) {
					ibanScelto = ibanAccredito;
				} else if(ibanAppoggio != null) {
					ibanScelto = ibanAppoggio;
				}
			}

			if(ibanScelto != null) {
				transferEl.setIBAN(ibanScelto.getCodIban());
				try {
					Dominio dominio2 = AnagraficaManager.getDominio(configWrapper, ibanScelto.getIdDominio());
					transferEl.setFiscalCodePA(dominio2.getCodDominio());
				} catch (NotFoundException e) {
					// se passo qui ho fallito la validazione della pendenza !
					throw new ServiceException("Dominio ["+ibanScelto.getIdDominio()+"] non censito in base dati.");
				}
			}

			transferEl.setRemittanceInformation(RptBuilder.buildCausaleSingoloVersamento(rpt.getIuv(), singoloVersamento.getImportoSingoloVersamento(), singoloVersamento.getDescrizione(), singoloVersamento.getDescrizioneCausaleRPT()));

			// TODO come generare la stringa indicata nell'xsd

			transferEl.setTransferCategory(singoloVersamento.getTipoContabilita(configWrapper).getCodifica() + "/" + singoloVersamento.getCodContabilita(configWrapper));

			transferEl.setMetadata(impostaValoriContabilita(singoloVersamento));

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

	public Rpt buildRptAttivata_SANP_321_V2 (PaGetPaymentV2Request requestBody, Versamento versamento, String iuv, String ccp, String numeroavviso) throws ServiceException, IOException {

		return this.buildRpt_V2(requestBody,
				null,
				versamento,
				iuv,
				ccp,
				numeroavviso,
				TipoVersamento.ATTIVATO_PRESSO_PSP,
				ModelloPagamento.ATTIVATO_PRESSO_PSP,
				StAutenticazioneSoggetto.N_A.value(),
				null
				);
	}

	private Rpt buildRpt_V2(PaGetPaymentV2Request requestBody,
			String codCarrello, 
			Versamento versamento, 
			String iuv, 
			String ccp, 
			String numeroavviso, 
			TipoVersamento tipoVersamento,
			ModelloPagamento modelloPagamento,
			String autenticazione, 
			String redirect) throws ServiceException, IOException {

		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);

		Dominio dominio = versamento.getDominio(configWrapper); 
		UnitaOperativa uo = versamento.getUo(configWrapper);

		Rpt rpt = new Rpt();
		rpt.setVersione(VersioneRPT.SANP_321_V2);
		rpt.setCallbackURL(redirect);
		rpt.setCodCarrello(codCarrello);
		rpt.setCodDominio(dominio.getCodDominio());
		rpt.setCodMsgRichiesta(this.buildUUID35());
		rpt.setCodSessione(null);
		rpt.setCodStazione(dominio.getStazione().getCodStazione());
		rpt.setDataAggiornamento(new Date());
		rpt.setDataMsgRichiesta(new Date());
		rpt.setDescrizioneStato(null);
		rpt.setId(null);
		rpt.setTipoVersamento(tipoVersamento);
		rpt.setIdVersamento(versamento.getId());
		rpt.setVersamento(versamento);
		rpt.setIuv(iuv);
		rpt.setCcp(ccp);
		rpt.setModelloPagamento(modelloPagamento);
		rpt.setPspRedirectURL(null);
		rpt.setStato(StatoRpt.RPT_ATTIVATA);
		rpt.setIdTransazioneRpt(ContextThreadLocal.get().getTransactionId());
		rpt.setBloccante(true);
		rpt.setEsitoPagamento(EsitoPagamento.IN_CORSO);

		PaGetPaymentV2Response paGetPaymentRes = new PaGetPaymentV2Response();
		CtPaymentPAV2 ctRpt = new CtPaymentPAV2();

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

		ctRpt.setCreditorReferenceId(iuv);

		// L'amount con il dueDate mi deve far scegliere tra le opzioni di pagamento
		// da attivare. Al momento viene gestita una sola opzione di pagamento
		// pertanto ignoro l'importo comunicato e rispondo sempre con il dovuto.
		// https://github.com/pagopa/pagopa-api/issues/194

		ctRpt.setPaymentAmount(versamento.getImportoTotale());
		ctRpt.setDueDate(calcolaDueDate(versamento));

		// Capire se il numero avviso utilizzato e' relativo alla rata di un documento, 
		// nel caso sia l'ultima valorizzare true altrimenti e' sempre false
		// se non e' una rata o rata unica e' sempre true. 
		ctRpt.setLastPayment(true); 

		if(versamento.getCausaleVersamento() != null) {
			if(versamento.getCausaleVersamento() instanceof CausaleSemplice) {
				ctRpt.setDescription(((CausaleSemplice) versamento.getCausaleVersamento()).getCausale());
			}
		} else {
			ctRpt.setDescription(" ");
		}

		ctRpt.setCompanyName(dominio.getRagioneSociale());

		if(uo != null && !uo.getCodUo().equals(it.govpay.model.Dominio.EC) && uo.getAnagrafica() != null) {
			ctRpt.setOfficeName(versamento.getUo(configWrapper).getAnagrafica().getRagioneSociale());
		}

		CtSubject debtor = this.buildSoggettoPagatore(versamento.getAnagraficaDebitore());
		ctRpt.setDebtor(debtor);
		ctRpt.setMetadata(null);

		CtTransferListPAV2 transferList = new CtTransferListPAV2();
		ctRpt.setTransferList(transferList );

		boolean utilizzaIBANPostali = false;
		StTransferType transferType = requestBody.getTransferType();
		if(transferType != null && transferType.equals(StTransferType.POSTAL)) {
			utilizzaIBANPostali = true;
		}

		List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(configWrapper);
		int i=1;
		for (SingoloVersamento singoloVersamento : singoliVersamenti) {
			CtTransferPAV2 transferEl = new CtTransferPAV2();

			/*
			Structure containing the details of possible tranfer payments.

			Currently set at 5 eligible payments per single position.

			Where each `transfer` items contains :

			- `idTransfer` : index of the list (from `1` to `5`) 
			- `transferAmount` : amount 
			- `fiscalCodePA` : Tax code of the public administration
			- `IBAN` : contains the IBAN of the account to be credited
			- `remittanceInformation` : reason for payment (_alias_ `causaleVersamento`)
			- `transferCategory` : contains taxonomic code, composed by `Codice tipo Ente Creditore`+`Progressivo macro area`+`Codice tipologia servizio`+`Motivo Giuridico` ( ex. `0101002IM` ) 
			| Segment                     | Regex                       |Example |
			|-----------------------------|-----------------------------|--------|
			|Codice tipo Ente Creditore   | `\d{2}`                     | 01     |
			|Progressivo macro area       | `\d{2}`                     | 01     |
			|Codice tipologia servizio    | `\d{2}`                     | 002    |
			|Motivo Giuridico             | `\w{2}`                     | IM     |

			 */

			transferEl.setIdTransfer(i);
			transferEl.setTransferAmount(singoloVersamento.getImportoSingoloVersamento());


			// Imposto IBAN e codice fiscale ente proprietario dell'iban

			// sv con tributo definito
			it.govpay.bd.model.Tributo tributo = singoloVersamento.getTributo(configWrapper);
			IbanAccredito ibanScelto = null;
			IbanAccredito ibanAccredito = null;
			IbanAccredito ibanAppoggio = null;
			if(tributo != null) {
				ibanAccredito = tributo.getIbanAccredito();
				ibanAppoggio = tributo.getIbanAppoggio();
			} else { // sv con le informazioni tributo direttamente nei dati 
				ibanAccredito = singoloVersamento.getIbanAccredito(configWrapper);
				ibanAppoggio = singoloVersamento.getIbanAppoggio(configWrapper);
			}

			// solo il transfer 1 e' vincolato ad essere postale se richiesto
			if(i == 1) {
				if(ibanAccredito != null) {
					ibanScelto = ibanAccredito;

					// devo utilizzare iban postale
					if(utilizzaIBANPostali) {
						if(!ibanAccredito.isPostale()) {
							// provo a verificare se l'iban appoggio e' postale
							if(ibanAppoggio != null && ibanAppoggio.isPostale()) {
								ibanScelto = ibanAppoggio;
							}
						} 
					}
				}  else if(ibanAppoggio != null) {
					// se capito qua dentro ho solo l'iban di appoggio, quindi devo utilizzarlo sia che sia postale o meno 
					ibanScelto = ibanAppoggio;
				}
			} else {
				// altri transfer
				if(ibanAccredito != null) {
					ibanScelto = ibanAccredito;
				} else if(ibanAppoggio != null) {
					ibanScelto = ibanAppoggio;
				}
			}

			if(ibanScelto != null) {
				transferEl.setIBAN(ibanScelto.getCodIban());
				try {
					Dominio dominio2 = AnagraficaManager.getDominio(configWrapper, ibanScelto.getIdDominio());
					transferEl.setFiscalCodePA(dominio2.getCodDominio());
				} catch (NotFoundException e) {
					// se passo qui ho fallito la validazione della pendenza !
					throw new ServiceException("Dominio ["+ibanScelto.getIdDominio()+"] non censito in base dati.");
				}
			} else { // MBT
				CtRichiestaMarcaDaBollo marcaBollo = new CtRichiestaMarcaDaBollo();
				if(singoloVersamento.getHashDocumento() != null)
					marcaBollo.setHashDocumento(singoloVersamento.getHashDocumento().getBytes());
				marcaBollo.setProvinciaResidenza(singoloVersamento.getProvinciaResidenza());
				if(singoloVersamento.getTipoBollo() != null)
					marcaBollo.setTipoBollo(singoloVersamento.getTipoBollo().getCodifica());
				else
					marcaBollo.setTipoBollo(TipoBollo.IMPOSTA_BOLLO.getCodifica());
				transferEl.setRichiestaMarcaDaBollo(marcaBollo);

				// Dominio
				Dominio dominio2 = singoloVersamento.getDominio(configWrapper);
				if(dominio2 != null) {
					transferEl.setFiscalCodePA(dominio2.getCodDominio());
				} else {
					// se non e' definito il dominio utilizzo quello del versamento.
					transferEl.setFiscalCodePA(dominio.getCodDominio());
				}
			}

			transferEl.setRemittanceInformation(RptBuilder.buildCausaleSingoloVersamento(rpt.getIuv(), singoloVersamento.getImportoSingoloVersamento(), singoloVersamento.getDescrizione(), singoloVersamento.getDescrizioneCausaleRPT()));

			// TODO come generare la stringa indicata nell'xsd

			transferEl.setTransferCategory(singoloVersamento.getTipoContabilita(configWrapper).getCodifica() + "/" + singoloVersamento.getCodContabilita(configWrapper));

			transferEl.setMetadata(impostaValoriContabilita(singoloVersamento));

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

	@SuppressWarnings("unchecked")
	private static CtMetadata impostaValoriContabilita(SingoloVersamento singoloVersamento) throws IOException {
		CtMetadata ctMetadata = null;
		// Gestione della Contabilita' inserendo le informazioni nel campo metadati
		String contabilitaString = singoloVersamento.getContabilita();

		if(contabilitaString != null && contabilitaString.length() > 0) {
			it.govpay.model.Contabilita dto = ConverterUtils.parse(singoloVersamento.getContabilita(), it.govpay.model.Contabilita.class);

			List<QuotaContabilita> quote = dto.getQuote();

			if(quote != null && quote.size() > 0) {
				ctMetadata = new CtMetadata();

				if(quote.size() == 1) {
					QuotaContabilita quotaContabilita = quote.get(0);

					// CAPITOLOBILANCIO: valorizzato con capitolo
					if(quotaContabilita.getCapitolo() != null) {
						CtMapEntry ctMapEntry = new CtMapEntry();

						ctMapEntry.setKey(CONTABILITA_QUOTA_CAPITOLO_BILANCIO_KEY);
						ctMapEntry.setValue(quotaContabilita.getCapitolo());

						ctMetadata.getMapEntry().add(ctMapEntry );
					}

					// ARTICOLOBILANCIO: valorizzato con articolo
					if(quotaContabilita.getArticolo() != null) {
						CtMapEntry ctMapEntry = new CtMapEntry();

						ctMapEntry.setKey(CONTABILITA_QUOTA_ARTICOLO_BILANCIO_KEY);
						ctMapEntry.setValue(quotaContabilita.getArticolo());

						ctMetadata.getMapEntry().add(ctMapEntry );
					}

					// CODICEACCERTAMENTO: valorizzato con accertamento
					if(quotaContabilita.getAccertamento() != null) {
						CtMapEntry ctMapEntry = new CtMapEntry();

						ctMapEntry.setKey(CONTABILITA_QUOTA_CODICE_ACCERTAMENTO_KEY);
						ctMapEntry.setValue(quotaContabilita.getAccertamento());

						ctMetadata.getMapEntry().add(ctMapEntry );
					}

					// ANNORIFERIMENTO: valorizzato con annoEsercizio
					{
						CtMapEntry ctMapEntry = new CtMapEntry();

						ctMapEntry.setKey(CONTABILITA_QUOTA_ANNO_RIFERIMENTO_KEY);
						ctMapEntry.setValue(quotaContabilita.getAnnoEsercizio() + "");

						ctMetadata.getMapEntry().add(ctMapEntry );
					}

					// TITOLOBILANCIO: valorizzato con titolo
					if(quotaContabilita.getTitolo() != null) {
						CtMapEntry ctMapEntry = new CtMapEntry();

						ctMapEntry.setKey(CONTABILITA_QUOTA_TITOLO_BILANCIO_KEY);
						ctMapEntry.setValue(quotaContabilita.getTitolo());

						ctMetadata.getMapEntry().add(ctMapEntry );
					}

					// CATEGORIABILANCIO: valorizzato con categoria
					if(quotaContabilita.getCategoria() != null) {
						CtMapEntry ctMapEntry = new CtMapEntry();

						ctMapEntry.setKey(CONTABILITA_QUOTA_CATEGORIA_BILANCIO_KEY);
						ctMapEntry.setValue(quotaContabilita.getCategoria());

						ctMetadata.getMapEntry().add(ctMapEntry );
					}

					// TIPOLOGIABILANCIO: valorizzato con tipologia
					if(quotaContabilita.getTipologia() != null) {
						CtMapEntry ctMapEntry = new CtMapEntry();

						ctMapEntry.setKey(CONTABILITA_QUOTA_TIPOLOGIA_BILANCIO_KEY);
						ctMapEntry.setValue(quotaContabilita.getTipologia());

						ctMetadata.getMapEntry().add(ctMapEntry );
					}

					//	eventuali parametri custom fino ad un massimo complessivo di 10 entry
					Object proprietaCustomObj = quotaContabilita.getProprietaCustom();

					if(proprietaCustomObj != null) {
						if(proprietaCustomObj instanceof String) {
							String proprietaCustom = (String) proprietaCustomObj;

							if(proprietaCustom != null && proprietaCustom.length() > 0) {
								Map<String, Object> parse = JSONSerializable.parse(proprietaCustom, Map.class);

								for (String key : parse.keySet()) {
									if(ctMetadata.getMapEntry().size() < 10) {
										CtMapEntry ctMapEntry = new CtMapEntry();

										ctMapEntry.setKey(key);
										ctMapEntry.setValue(parse.get(key).toString());

										ctMetadata.getMapEntry().add(ctMapEntry );
									}
								}
							}
						}  else if(proprietaCustomObj instanceof java.util.LinkedHashMap) {
							java.util.LinkedHashMap<?,?> parse = (LinkedHashMap<?,?>) proprietaCustomObj;

							for (Object key : parse.keySet()) {
								if(ctMetadata.getMapEntry().size() < 10) {
									CtMapEntry ctMapEntry = new CtMapEntry();

									ctMapEntry.setKey(key.toString());
									ctMapEntry.setValue(parse.get(key).toString());

									ctMetadata.getMapEntry().add(ctMapEntry );
								}
							}
						}
					}

				} else {
					for (QuotaContabilita quotaContabilita : quote) {
						CtMapEntry ctMapEntry = new CtMapEntry();

						List<Object> values = new ArrayList<>();
						// CAPITOLOBILANCIO: valorizzato con capitolo
						if(quotaContabilita.getCapitolo() != null) {
							values.add(quotaContabilita.getCapitolo());
						} else {
							values.add("");
						}

						// ARTICOLOBILANCIO: valorizzato con articolo
						if(quotaContabilita.getArticolo() != null) {
							values.add(quotaContabilita.getArticolo());
						} else {
							values.add("");
						}

						// CODICEACCERTAMENTO: valorizzato con accertamento
						if(quotaContabilita.getAccertamento() != null) {
							values.add(quotaContabilita.getAccertamento());
						}  else {
							values.add("");
						}

						// ANNORIFERIMENTO: valorizzato con annoEsercizio
						values.add(quotaContabilita.getAnnoEsercizio());

						// TITOLOBILANCIO: valorizzato con titolo
						if(quotaContabilita.getTitolo() != null) {
							values.add(quotaContabilita.getTitolo());
						} else {
							values.add("");
						}

						// CATEGORIABILANCIO: valorizzato con categoria
						if(quotaContabilita.getCategoria() != null) {
							values.add(quotaContabilita.getCategoria());
						} else {
							values.add("");
						}

						// TIPOLOGIABILANCIO: valorizzato con tipologia
						if(quotaContabilita.getTipologia() != null) {
							values.add(quotaContabilita.getTipologia());
						} else {
							values.add("");
						}

						// IMPORTOEUROCENT
						values.add(TracciatiNotificaPagamentiUtils.printImporto(quotaContabilita.getImporto(), true, true));

						ctMapEntry.setKey(CONTABILITA_QUOTA_ENTRY_KEY);
						ctMapEntry.setValue(StringUtils.join(values, ","));

						ctMetadata.getMapEntry().add(ctMapEntry );
					}
				}
			}
		}

		return ctMetadata;
	}

//	@SuppressWarnings("unchecked")
//	private static CtMetadata impostaValoriContabilita_SANP34(SingoloVersamento singoloVersamento) throws IOException {
//		CtMetadata ctMetadata = null;
//		// Gestione della Contabilita' inserendo le informazioni nel campo metadati
//		String contabilitaString = singoloVersamento.getContabilita();
//
//		if(contabilitaString != null && contabilitaString.length() > 0) {
//			it.govpay.model.Contabilita dto = ConverterUtils.parse(singoloVersamento.getContabilita(), it.govpay.model.Contabilita.class);
//
//			List<QuotaContabilita> quote = dto.getQuote();
//
//			if(quote != null && quote.size() > 0) {
//				ctMetadata = new CtMetadata();
//
//				List<CtMapEntry> listEntriesProvvisoria = new ArrayList<>();
//				int nQuota = 1;
//				for (QuotaContabilita quotaContabilita : quote) {
//
//					String tipologia = null;
//					// RC_TIP_N: valorizzato con tipologia
//					if(quotaContabilita.getTipologia() != null) {
//						tipologia = quotaContabilita.getTipologia();
//						CtMapEntry ctMapEntry = new CtMapEntry();
//
//						ctMapEntry.setKey(MessageFormat.format(CONTABILITA_QUOTA_TIPO_CONTABILIZZAZIONE_PARAM_KEY, nQuota));
//						ctMapEntry.setValue(tipologia);
//
//						listEntriesProvvisoria.add(ctMapEntry );
//					} 
//
//					// lettura parametri custom
//					Object proprietaCustomObj = quotaContabilita.getProprietaCustom();
//					Map<String, String> proprietaCustomMap = new HashMap<>();
//					if(proprietaCustomObj != null) {
//						if(proprietaCustomObj instanceof String) {
//							String proprietaCustom = (String) proprietaCustomObj;
//
//							if(proprietaCustom != null && proprietaCustom.length() > 0) {
//								Map<String, Object> parse = JSONSerializable.parse(proprietaCustom, Map.class);
//
//								for (String key : parse.keySet()) {
//									proprietaCustomMap.put(key, parse.get(key).toString());
//								}
//							}
//						}  else if(proprietaCustomObj instanceof java.util.LinkedHashMap) {
//							java.util.LinkedHashMap<?,?> parse = (LinkedHashMap<?,?>) proprietaCustomObj;
//
//							for (Object key : parse.keySet()) {
//								proprietaCustomMap.put(key.toString(), parse.get(key).toString());
//							}
//						}
//					}
//
//					if(tipologia != null) {
//						switch(tipologia) {
//						case CONTABILITA_QUOTA_TIPO_CONTABILIZZAZIONE_PARAM_VALUE_DL118:
//							/*
//							 RC_TIP_N: valorizzato con tipologia
//							 RC_CAP_N: valorizzato con capitolo
//							RC_ART_N: valorizzato con articolo
//							RC_ACC_N: valorizzato con accertamento
//							RC_AC_N: valorizzato con annoEsercizio CAMPO OBBLIGATORIO
//							RC_IMP_N: valorizzato con importo CAMPO OBBLIGATORIO
//							RC_CU_N: valorizzato da proprieta' custom codiceUfficio
//							RC_PF5_N: valorizzato da proprieta' custom pf5livello
//							Capitolo e Accertamento sono fra loro alternativi, uno dei due deve essere inserito. Accertamento e pf_5_livello sono fra loro alternativi. 
//							 * */
//							// RC_AC_N: valorizzato con annoEsercizio CAMPO OBBLIGATORIO
//							{
//								CtMapEntry ctMapEntry = new CtMapEntry();
//	
//								ctMapEntry.setKey(MessageFormat.format(CONTABILITA_QUOTA_ANNO_COMPETENZA_PARAM_KEY, nQuota));
//								ctMapEntry.setValue(quotaContabilita.getAnnoEsercizio() + "");
//	
//								ctMetadata.getMapEntry().add(ctMapEntry );
//							}
//
//							
//							
//							break;
//						case CONTABILITA_QUOTA_TIPO_CONTABILIZZAZIONE_PARAM_VALUE_TIPICO:
//							/*
//							 RC_TIP_N: valorizzato con tipologia
//								RC_AC_N: valorizzato con annoEsercizio Campo obbligatorio
//								RC_IMP_N: valorizzato con importo Campo obbligatorio
//								RC_CU_N: valorizzato da proprieta' custom codiceUfficio
//								RC_TI_N: valorizzato da proprieta' custom tipoIncasso Campo obbligatorio
//								RC_EF_N: valorizzato da proprieta' custom emissioneFattura
//								RC_ND_N: valorizzato da proprieta' custom numeroDocumento
//								Emissione fattura e nr_documento sono fra loro alternativi. 
//							 */
//
//							break;
//						case CONTABILITA_QUOTA_TIPO_CONTABILIZZAZIONE_PARAM_CIVILISTICO:
//							/*
//							 RC_TIP_N: valorizzato con tipologia
//							RC_AC_N: valorizzato con annoEsercizio Campo obbligatorio
//							RC_IMP_N: valorizzato con importo Campo obbligatorio
//							RC_CU_N: valorizzato da proprieta' custom codiceUfficio
//							RC_CON_N: valorizzato da proprieta' custom conto
//							RC_COM_N: valorizzato da proprieta' custom commessa
//							RC_ND_N: valorizzato da proprieta' custom numeroDocumento
//							Conto, Commessa e Nr Documento sono fra loro alternativi, uno dei tre deve essere inserito.
//							 */
//							break;
//						default: 
//							break;
//						}
//					}
//				}
//
//				// massimo 10 entries
//				if(listEntriesProvvisoria.size() <= 10) {
//					ctMetadata.getMapEntry().addAll(listEntriesProvvisoria);
//				} else {
//					ctMetadata.getMapEntry().addAll(listEntriesProvvisoria.subList(0, 9));
//				}
//			}
//		}
//		return ctMetadata;
//	}

	private static Date calcolaDueDate(Versamento versamento) {
		if(versamento.getDataValidita() != null) {
			return versamento.getDataValidita(); // indicates the expiration payment date
		} else if(versamento.getDataScadenza() != null) {
			return versamento.getDataScadenza(); // indicates the expiration payment date
		} else {
			Integer numeroGiorniValiditaPendenza = GovpayConfig.getInstance().getNumeroGiorniValiditaPendenza();

			if(numeroGiorniValiditaPendenza != null) {
				Calendar instance = Calendar.getInstance();
				instance.setTime(versamento.getDataCreazione()); 
				instance.add(Calendar.DATE, numeroGiorniValiditaPendenza);
				return instance.getTime();
			} else {
				return new Date(32503590000000l); //31.12.2999
			}
		}   
	}
}
