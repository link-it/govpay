/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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

import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.gov.digitpa.schemas._2011.pagamenti.StAutenticazioneSoggetto;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtPaymentPAV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtSubject;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferListPAV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtTransferPAV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentV2Request;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentV2Response;
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
import it.govpay.core.beans.tracciati.MapEntry;
import it.govpay.core.exceptions.IOException;
import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Canale.TipoVersamento;
import it.govpay.model.IbanAccredito;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.model.Rpt.VersioneRPT;
import it.govpay.model.SingoloVersamento.TipoBollo;
import it.govpay.model.Versamento.CausaleSemplice;
import it.govpay.pagopa.beans.utils.JaxbUtils;

public class CtPaymentPAV2Builder {

	public Rpt buildCtPaymentPAV2 (PaGetPaymentV2Request requestBody, Versamento versamento, String iuv, String ccp, String numeroavviso) throws ServiceException, IOException {

		return this.buildCtPaymentPAV2(requestBody,
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

	private Rpt buildCtPaymentPAV2(PaGetPaymentV2Request requestBody,
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
		rpt.setCodMsgRichiesta(CtPaymentPABuilder.buildUUID35());
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
		ctRpt.setDueDate(CtPaymentPABuilder.calcolaDueDate(versamento));

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

		CtSubject debtor = CtPaymentPABuilder.buildSoggettoPagatore(versamento.getAnagraficaDebitore());
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
					transferEl.setCompanyName(dominio2.getRagioneSociale());
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
					transferEl.setCompanyName(dominio2.getRagioneSociale());
				} else {
					// se non e' definito il dominio utilizzo quello del versamento.
					transferEl.setFiscalCodePA(dominio.getCodDominio());
					transferEl.setCompanyName(dominio.getRagioneSociale());
				}
			}

			transferEl.setRemittanceInformation(RptBuilder.buildCausaleSingoloVersamento(rpt.getIuv(), singoloVersamento.getImportoSingoloVersamento(), singoloVersamento.getDescrizione(), singoloVersamento.getDescrizioneCausaleRPT()));

			transferEl.setTransferCategory(singoloVersamento.getTipoContabilita(configWrapper).getCodifica() + "/" + singoloVersamento.getCodContabilita(configWrapper));

			impostaMetadata(singoloVersamento, transferEl);

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

	private void impostaMetadata(SingoloVersamento singoloVersamento, CtTransferPAV2 transferEl) throws IOException {
		// se la pendenza ha dei metadati definiti vengono impostati nell'apposito campo
		if(singoloVersamento.getMetadataPagoPA() != null && singoloVersamento.getMetadataPagoPA().getMapEntries() != null && !singoloVersamento.getMetadataPagoPA().getMapEntries().isEmpty()) {
			CtMetadata metadata = new CtMetadata();
			
			for (MapEntry mapEntry : singoloVersamento.getMetadataPagoPA().getMapEntries()) {
				CtMapEntry ctMapEntry = new CtMapEntry();
				ctMapEntry.setKey(mapEntry.getKey());
				ctMapEntry.setValue(mapEntry.getValue());
				metadata.getMapEntry().add(ctMapEntry );
			}
			
			transferEl.setMetadata(metadata);				
		} else {
			transferEl.setMetadata(CtPaymentPABuilder.impostaValoriContabilita(singoloVersamento));	
		}
	}
}
