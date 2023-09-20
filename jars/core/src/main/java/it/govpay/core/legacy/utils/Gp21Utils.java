package it.govpay.core.legacy.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentV2Response;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTV2Request;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.filters.VersamentoFilter.SortFields;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.MessaggiPagoPAUtils;
import it.govpay.ec.v1.beans.TipoSoggetto;
import it.govpay.servizi.commons.Anagrafica;
import it.govpay.servizi.commons.Canale;
import it.govpay.servizi.commons.EsitoTransazione;
import it.govpay.servizi.commons.FlussoRendicontazione;
import it.govpay.servizi.commons.IuvGenerato;
import it.govpay.servizi.commons.ModelloPagamento;
import it.govpay.servizi.commons.Pagamento.Allegato;
import it.govpay.servizi.commons.StatoTransazione;
import it.govpay.servizi.commons.StatoVersamento;
import it.govpay.servizi.commons.TipoAllegato;
import it.govpay.servizi.commons.TipoRendicontazione;
import it.govpay.servizi.commons.TipoVersamento;
import it.govpay.servizi.commons.Transazione;
import it.govpay.servizi.commons.Versamento.SingoloVersamento;

public class Gp21Utils {

	public static Transazione toTransazione(Rpt rpt, BDConfigWrapper configWrapper, boolean convertiMessaggioPagoPAV2InPagoPAV1) throws ServiceException {
		Transazione t = new Transazione();
		Canale canale = new Canale();
		canale.setCodCanale(rpt.getCodCanale());
		canale.setCodPsp(rpt.getCodPsp());
		canale.setCodIntermediarioPsp(rpt.getCodIntermediarioPsp());
		if(rpt.getTipoVersamento() != null) {
			switch (rpt.getTipoVersamento()) {
			case ADDEBITO_DIRETTO:
				canale.setTipoVersamento(TipoVersamento.AD);
				break;
			case ATTIVATO_PRESSO_PSP:
				canale.setTipoVersamento(TipoVersamento.PO);
				break;
			case BOLLETTINO_POSTALE:
				canale.setTipoVersamento(TipoVersamento.BP);
				break;
			case BONIFICO_BANCARIO_TESORERIA:
				canale.setTipoVersamento(TipoVersamento.BBT);
				break;
			case CARTA_PAGAMENTO:
				canale.setTipoVersamento(TipoVersamento.CP);
				break;
			case MYBANK:
				canale.setTipoVersamento(TipoVersamento.OBEP);
				break;
			case OTHER:
				canale.setTipoVersamento(TipoVersamento.OTH);
				break;
			}
		}
		t.setCanale(canale);
		t.setCcp(rpt.getCcp());
		t.setCodDominio(rpt.getCodDominio());
		t.setDescrizioneStato(rpt.getDescrizioneStato());
		if(rpt.getEsitoPagamento() != null) {
			switch (rpt.getEsitoPagamento()) {
			case DECORRENZA_TERMINI:
				t.setEsito(EsitoTransazione.DECORRENZA_TERMINI);
				break;
			case DECORRENZA_TERMINI_PARZIALE:
				t.setEsito(EsitoTransazione.DECORRENZA_TERMINI_PARZIALE);
				break;
			case PAGAMENTO_ESEGUITO:
				t.setEsito(EsitoTransazione.PAGAMENTO_ESEGUITO);
				break;
			case PAGAMENTO_NON_ESEGUITO:
				t.setEsito(EsitoTransazione.PAGAMENTO_NON_ESEGUITO);
				break;
			case PAGAMENTO_PARZIALMENTE_ESEGUITO:
				t.setEsito(EsitoTransazione.PAGAMENTO_PARZIALMENTE_ESEGUITO);
				break;
			case IN_CORSO:
			case RIFIUTATO:
				// azioni non mappate nelle vecchie api
				break;
			}
		}
		t.setIuv(rpt.getIuv());
		if(rpt.getModelloPagamento() != null) {
			switch(rpt.getModelloPagamento()) {
			case ATTIVATO_PRESSO_PSP:
				t.setModello(ModelloPagamento.ATTIVATO_PRESSO_PSP);
				break;
			case DIFFERITO:
				t.setModello(ModelloPagamento.DIFFERITO);
				break;
			case IMMEDIATO:
				t.setModello(ModelloPagamento.IMMEDIATO);
				break;
			case IMMEDIATO_MULTIBENEFICIARIO:
				t.setModello(ModelloPagamento.IMMEDIATO_MULTIBENEFICIARIO);
				break;
			}
		}

		// conversione del messaggio nel formato tradizionale se previsto
		try {
			switch (rpt.getVersione()) {
			case SANP_230:
				t.setRpt(rpt.getXmlRpt());
				break;
			case SANP_240:
				PaGetPaymentRes paGetPaymentRes_RPT = JaxbUtils.toPaGetPaymentRes_RPT(rpt.getXmlRpt(), false);
				
				if(convertiMessaggioPagoPAV2InPagoPAV1) {
					CtRichiestaPagamentoTelematico ctRpt2 = MessaggiPagoPAUtils.toCtRichiestaPagamentoTelematico(paGetPaymentRes_RPT, rpt);
					t.setRpt(JaxbUtils.toByte(ctRpt2));
				} else {				
					t.setRpt(rpt.getXmlRpt());
				}
				break;
			case SANP_321_V2:
				PaGetPaymentV2Response paGetPaymentV2Response = JaxbUtils.toPaGetPaymentV2Response_RPT(rpt.getXmlRpt(), false);
				
				if(convertiMessaggioPagoPAV2InPagoPAV1) {
					CtRichiestaPagamentoTelematico ctRpt2 = MessaggiPagoPAUtils.toCtRichiestaPagamentoTelematico(paGetPaymentV2Response, rpt);
					t.setRpt(JaxbUtils.toByte(ctRpt2));
				} else {				
					t.setRpt(rpt.getXmlRpt());
				}
				break;
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		try {
			switch (rpt.getVersione()) {
			case SANP_230:
				t.setRt(rpt.getXmlRt());
				break;
			case SANP_240:
				PaSendRTReq paSendRTReq_RT = JaxbUtils.toPaSendRTReq_RT(rpt.getXmlRt(), false);
				
				if(convertiMessaggioPagoPAV2InPagoPAV1) {
					CtRicevutaTelematica ctRt2 = MessaggiPagoPAUtils.toCtRicevutaTelematica(paSendRTReq_RT, rpt);
					t.setRt(JaxbUtils.toByte(ctRt2));
				} else {				
					t.setRt(rpt.getXmlRt());
				}
				
				break;
			case SANP_321_V2:
				PaSendRTV2Request paSendRTRtv2Request = JaxbUtils.toPaSendRTV2Request_RT(rpt.getXmlRt(), false);
				
				if(convertiMessaggioPagoPAV2InPagoPAV1) {
					CtRicevutaTelematica ctRt2 = MessaggiPagoPAUtils.toCtRicevutaTelematica(paSendRTRtv2Request, rpt);
					t.setRt(JaxbUtils.toByte(ctRt2));
				} else {				
					t.setRt(rpt.getXmlRt());
				}
				
				break;
			}
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		
		

//		if(versione.compareTo(Versione.GP_02_02_00) >=0) { // Versione 2.2
			t.setData(rpt.getDataMsgRichiesta());
//		}

		try {
			t.setStato(StatoTransazione.valueOf(rpt.getStato().toString()));
		} catch (Exception e) {
			t.setStato(StatoTransazione.RPT_ACCETTATA_NODO);
		}
		List<Pagamento> pagamenti = configWrapper != null ? rpt.getPagamenti(configWrapper) : rpt.getPagamenti();
		for(Pagamento pagamento : pagamenti) {
			t.getPagamento().add(toPagamento(pagamento));
		}
		return t;
	}

	public static it.govpay.servizi.commons.Pagamento toPagamento(Pagamento pagamento) throws ServiceException {
		it.govpay.servizi.commons.Pagamento p = new it.govpay.servizi.commons.Pagamento();

		if(pagamento.getAllegato() != null) {
			Allegato allegato = new Allegato();
			allegato.setTesto(pagamento.getAllegato());
			allegato.setTipo(TipoAllegato.valueOf(pagamento.getTipoAllegato().toString()));
			p.setAllegato(allegato);
		}
		p.setCodSingoloVersamentoEnte(pagamento.getSingoloVersamento(null).getCodSingoloVersamentoEnte());
		p.setCommissioniPsp(pagamento.getCommissioniPsp());
		p.setDataPagamento(pagamento.getDataPagamento());
		p.setImportoPagato(pagamento.getImportoPagato());
		p.setIur(pagamento.getIur());
		p.setCausaleRevoca(pagamento.getCausaleRevoca());
		p.setDatiEsitoRevoca(pagamento.getDatiEsitoRevoca());
		p.setDatiRevoca(pagamento.getDatiRevoca());
		p.setEsitoRevoca(pagamento.getEsitoRevoca());
		p.setImportoRevocato(pagamento.getImportoRevocato());
//		if(versione.compareTo(Versione.GP_02_02_00) >= 0) { // Versione 2.2
			p.setDataAcquisizione(pagamento.getDataAcquisizione());
			p.setDataAcquisizioneRevoca(pagamento.getDataAcquisizioneRevoca());
//		}
//		if(versione.compareTo(Versione.GP_02_05_00) >= 0) { // Versione 2.5
//			if(pagamento.getSingoloVersamento(bd) != null && pagamento.getSingoloVersamento(bd).getIbanAccredito(bd) != null)
//				p.setIbanAccredito(pagamento.getSingoloVersamento(bd).getIbanAccredito(bd).getCodIban());
//			if(pagamento.getSingoloVersamento(bd) != null && pagamento.getSingoloVersamento(bd).getIbanAppoggio(bd) != null)	
//				p.setIbanAppoggio(pagamento.getSingoloVersamento(bd).getIbanAppoggio(bd).getCodIban());
//		}
		return p;
	}

	public static it.govpay.servizi.commons.FlussoRendicontazione.Pagamento toRendicontazionePagamento(Rendicontazione rend, Pagamento pagamento, it.govpay.bd.model.SingoloVersamento singoloVersamento, Versamento versamento,  Fr frModel, BDConfigWrapper configWrapper) throws ServiceException {

		FlussoRendicontazione.Pagamento p = new FlussoRendicontazione.Pagamento();
		
		if(singoloVersamento != null) {
			p.setCodSingoloVersamentoEnte(singoloVersamento.getCodSingoloVersamentoEnte());
		} else if(pagamento != null) {
			p.setCodSingoloVersamentoEnte(pagamento.getSingoloVersamento(null).getCodSingoloVersamentoEnte());
		} else if(versamento!= null) {
			p.setCodSingoloVersamentoEnte(versamento.getSingoliVersamenti().get(0).getCodSingoloVersamentoEnte());
		} else { // pagamento o singoloVersamento non presenti
			p.setCodSingoloVersamentoEnte("UNKNOWN");
		}
		p.setImportoRendicontato(rend.getImporto().abs());
		p.setIur(rend.getIur());
		p.setEsitoRendicontazione(TipoRendicontazione.valueOf(rend.getEsito().toString()));
		p.setDataRendicontazione(rend.getData());
		if(versamento != null) {
			p.setCodApplicazione(versamento.getApplicazione(configWrapper).getCodApplicazione());
		}
		p.setIuv(rend.getIuv());
		p.setCodDominio(frModel.getCodDominio());

		return p;
	}

	public static List<it.govpay.bd.model.Versamento.StatoVersamento> toStatiVersamento(List<StatoVersamento> stati) {
		if(stati == null || stati.size() == 0) return null;

		List<it.govpay.bd.model.Versamento.StatoVersamento> statiVersamento = new ArrayList<it.govpay.bd.model.Versamento.StatoVersamento>();
		for(StatoVersamento stato : stati) {
			statiVersamento.add(it.govpay.bd.model.Versamento.StatoVersamento.valueOf(stato.toString()));
		}
		return statiVersamento;
	}

	public static SortFields toFilterSort(String ordinamento) {
		if(ordinamento == null) return null;

		if(ordinamento.equals("DATA_SCADENZA_ASC"))
			return SortFields.SCADENZA_ASC;

		if(ordinamento.equals("DATA_SCADENZA_DES"))
			return SortFields.SCADENZA_DESC;

		if(ordinamento.equals("DATA_CARICAMENTO_ASC"))
			return SortFields.CARICAMENTO_ASC;

		if(ordinamento.equals("DATA_CARICAMENTO_DES"))
			return SortFields.CARICAMENTO_DESC;

		if(ordinamento.equals("DATA_AGGIORNAMENTO_ASC"))
			return SortFields.AGGIORNAMENTO_ASC;

		if(ordinamento.equals("DATA_AGGIORNAMENTO_DES"))
			return SortFields.AGGIORNAMENTO_DESC;

		return null;
	}

	public static List<IuvGenerato> toIuvGenerato(List<it.govpay.core.business.model.Iuv> iuvGeneratiModel, Applicazione applicazione) {
		List<IuvGenerato> iuvGenerati = new ArrayList<IuvGenerato>();
		for (it.govpay.core.business.model.Iuv iuvGeneratoModel : iuvGeneratiModel) {
			iuvGenerati.add(toIuvGenerato(iuvGeneratoModel, applicazione));
		}
		return iuvGenerati;
	}

	public static IuvGenerato toIuvGenerato(it.govpay.core.business.model.Iuv iuvGeneratoModel, Applicazione applicazione) {
		IuvGenerato iuvGenerato = new IuvGenerato();
		iuvGenerato.setBarCode(iuvGeneratoModel.getBarCode());
		iuvGenerato.setCodApplicazione(iuvGeneratoModel.getCodApplicazione());
		iuvGenerato.setCodDominio(iuvGeneratoModel.getCodDominio());
		iuvGenerato.setCodVersamentoEnte(iuvGeneratoModel.getCodVersamentoEnte());
		iuvGenerato.setIuv(iuvGeneratoModel.getIuv());
		iuvGenerato.setQrCode(iuvGeneratoModel.getQrCode());
		//		if(applicazione.getVersione().compareTo(Versione.GP_02_03_00) >= 0) { // Versione 2.3
		//			iuvGenerato.setNumeroAvviso(iuvGeneratoModel.getNumeroAvviso());
		//		}
		return iuvGenerato;
	}

	public static it.govpay.core.dao.commons.Versamento toVersamentoCommons(it.govpay.servizi.commons.Versamento pendenza) throws ServiceException, GovPayException {
		if(pendenza == null) return null;
		
		it.govpay.core.dao.commons.Versamento versamento = new it.govpay.core.dao.commons.Versamento();

		if(pendenza.getAnnoTributario() != null)
			versamento.setAnnoTributario(pendenza.getAnnoTributario().intValue());

		versamento.setCausale(pendenza.getCausale());
		versamento.setCodApplicazione(pendenza.getCodApplicazione());

		versamento.setCodDominio(pendenza.getCodDominio());
		versamento.setCodUnitaOperativa(pendenza.getCodUnitaOperativa());
		versamento.setCodVersamentoEnte(pendenza.getCodVersamentoEnte());
		versamento.setDataValidita(pendenza.getDataScadenza());
		versamento.setDataCaricamento(new Date());
		versamento.setDebitore(toAnagraficaCommons(pendenza.getDebitore()));
		
		versamento.setIuv(pendenza.getIuv());

		// voci pagamento
		BigDecimal importoVociPendenza = fillSingoliVersamentiFromVociPendenza(versamento, pendenza.getSingoloVersamento());

		// importo pendenza puo' essere null
		versamento.setImportoTotale(pendenza.getImportoTotale() != null ? pendenza.getImportoTotale() : importoVociPendenza); 

		versamento.setCodLotto(pendenza.getCodDebito());

		return versamento;
	}

	public static BigDecimal fillSingoliVersamentiFromVociPendenza(it.govpay.core.dao.commons.Versamento versamento, List<SingoloVersamento> voci) throws ServiceException, GovPayException {

		BigDecimal importoTotale = BigDecimal.ZERO;

		if(voci != null && voci.size() > 0) {
			for (SingoloVersamento vocePendenza : voci) {
				it.govpay.core.dao.commons.Versamento.SingoloVersamento sv = new it.govpay.core.dao.commons.Versamento.SingoloVersamento();

				sv.setCodSingoloVersamentoEnte(vocePendenza.getCodSingoloVersamentoEnte());
//				if(vocePendenza.getDatiAllegati() != null)
//					sv.setDatiAllegati(ConverterUtils.toJSON(vocePendenza.getDatiAllegati(),null));
//				sv.setDescrizione(vocePendenza.getDescrizione());
//				sv.setDescrizioneCausaleRPT(vocePendenza.getDescrizioneCausaleRPT());
				sv.setImporto(vocePendenza.getImporto());
//				sv.setCodDominio(versamento.getCodDominio());

				importoTotale = importoTotale.add(vocePendenza.getImporto());

				// Definisce i dati di un bollo telematico
				if(vocePendenza.getBolloTelematico() != null) {
					it.govpay.core.dao.commons.Versamento.SingoloVersamento.BolloTelematico bollo = new it.govpay.core.dao.commons.Versamento.SingoloVersamento.BolloTelematico();
					bollo.setHash(vocePendenza.getBolloTelematico().getHash());
					bollo.setProvincia(vocePendenza.getBolloTelematico().getProvincia());
					bollo.setTipo(vocePendenza.getBolloTelematico().getTipo());
					sv.setBolloTelematico(bollo);
				} else if(vocePendenza.getCodTributo() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
					sv.setCodTributo(vocePendenza.getCodTributo());

				} else { // Definisce i dettagli di incasso della singola entrata.
					if(vocePendenza.getTributo() != null) {
						it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo tributo = new it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo();
						tributo.setCodContabilita(vocePendenza.getTributo().getCodContabilita());
						tributo.setIbanAccredito(vocePendenza.getTributo().getIbanAccredito());
						tributo.setIbanAppoggio(vocePendenza.getTributo().getIbanAppoggio());
						tributo.setTipoContabilita(it.govpay.core.dao.commons.Versamento.SingoloVersamento.TipoContabilita.valueOf(vocePendenza.getTributo().getTipoContabilita().name()));
						sv.setTributo(tributo);
					}
				}
				
				versamento.getSingoloVersamento().add(sv);
			}
		}

		return importoTotale;
	}

	public static it.govpay.core.dao.commons.Anagrafica toAnagraficaCommons(Anagrafica anagrafica) {
		if(anagrafica == null) return null;
		it.govpay.core.dao.commons.Anagrafica anagraficaModel = new it.govpay.core.dao.commons.Anagrafica();
		anagraficaModel.setCap(anagrafica.getCap());
		anagraficaModel.setCellulare(anagrafica.getCellulare());
		anagraficaModel.setCivico(anagrafica.getCivico());
		anagraficaModel.setCodUnivoco(anagrafica.getCodUnivoco());
		anagraficaModel.setEmail(anagrafica.getEmail());
		anagraficaModel.setFax(anagrafica.getFax());
		anagraficaModel.setIndirizzo(anagrafica.getIndirizzo());
		anagraficaModel.setLocalita(anagrafica.getLocalita());
		anagraficaModel.setNazione(anagrafica.getNazione());
		anagraficaModel.setProvincia(anagrafica.getProvincia());
		anagraficaModel.setRagioneSociale(anagrafica.getRagioneSociale());
		anagraficaModel.setTelefono(anagrafica.getTelefono());
		anagraficaModel.setTipo(TipoSoggetto.F.toString());
		return anagraficaModel;
	}

}
