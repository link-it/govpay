package it.govpay.pagamento.v3.beans.converter;

import java.io.UnsupportedEncodingException;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.springframework.security.core.Authentication;

import it.gov.digitpa.schemas._2011.pagamenti.CtIdentificativoUnivocoPersonaFG;
import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoVersante;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivocoPersFG;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtPaymentPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtPaymentPAV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtReceipt;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtReceiptV2;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentV2Response;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTV2Request;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.UtenzaCittadino;
import it.govpay.bd.model.Versamento;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pagamento.v3.beans.EsitoRpp;
import it.govpay.pagamento.v3.beans.ModelloPagamento;
import it.govpay.pagamento.v3.beans.Ricevuta;
import it.govpay.pagamento.v3.beans.RicevutaIstitutoAttestante;
import it.govpay.pagamento.v3.beans.RicevutaRpt;
import it.govpay.pagamento.v3.beans.RicevutaRt;
import it.govpay.pagamento.v3.beans.RicevutaRt.TipoEnum;
import it.govpay.pagamento.v3.beans.RicevuteRisultati;
import it.govpay.pagopa.beans.utils.JaxbUtils;
import it.govpay.rs.v1.authentication.SPIDAuthenticationDetailsSource;

public class RicevuteConverter {

	public static RicevuteRisultati toRsModelIndex(Rpt dto) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		RicevuteRisultati rsModel = new RicevuteRisultati();
		rsModel.setData(dto.getDataMsgRicevuta());
		rsModel.setDominio(DominiConverter.toRsModelIndex(dto.getDominio(configWrapper)));
		rsModel.setIdRicevuta(dto.getCcp());
		rsModel.setIuv(dto.getIuv());
		if(dto.getEsitoPagamento() != null)
			rsModel.setEsito(EsitoRpp.fromRptEsitoPagamento(dto.getEsitoPagamento().name()));

		return rsModel;
	}
	
	public static Ricevuta toRsModel(Rpt rpt, Authentication user) throws ServiceException, IOException, UnsupportedEncodingException {
		return toRsModel(rpt, rpt.getVersamento(), user);
	}


	public static Ricevuta toRsModel(Rpt rpt, Versamento versamento, Authentication user) throws ServiceException, UnsupportedEncodingException, IOException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		Ricevuta rsModel = new Ricevuta();
		
		GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(user);

		if(rpt.getIdentificativoAttestante() != null) {
			RicevutaIstitutoAttestante istitutoAttestante = new RicevutaIstitutoAttestante();
			istitutoAttestante.setDenominazione(rpt.getDenominazioneAttestante());
			istitutoAttestante.setIdPSP(rpt.getIdentificativoAttestante());
			istitutoAttestante.setIdCanale(rpt.getCodCanale());
			rsModel.setIstitutoAttestante(istitutoAttestante);
		}

		rsModel.setData(rpt.getDataMsgRicevuta());
		rsModel.setDominio(DominiConverter.toRsModelIndex(rpt.getDominio(configWrapper)));
		rsModel.setIdRicevuta(rpt.getCcp());
		rsModel.setIuv(rpt.getIuv());
		if(rpt.getEsitoPagamento() != null)
			rsModel.setEsito(EsitoRpp.fromRptEsitoPagamento(rpt.getEsitoPagamento().name()));

		if(rpt.getIdPagamentoPortale() != null) {
			PagamentoPortale pagamentoPortale = rpt.getPagamentoPortale(configWrapper);
			rsModel.setIdPagamento(pagamentoPortale.getIdSessione());
			rsModel.setIdSessionePsp(pagamentoPortale.getIdSessionePsp());
		}

		rsModel.setPendenza(PendenzeConverter.toPendenzaPagataRsModel(rpt, user));

		rsModel.setDataPagamento(rpt.getDataMsgRicevuta());

		RicevutaRpt ricevutaRpt = new RicevutaRpt();

		try {
			ricevutaRpt.setXml(rpt.getXmlRpt());
			switch (rpt.getVersione()) {
			case SANP_240:
				PaGetPaymentRes paGetPaymentRes_RPT = JaxbUtils.toPaGetPaymentRes_RPT(rpt.getXmlRpt(), false);
				
				CtPaymentPA data = paGetPaymentRes_RPT.getData();
				
				if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
					// in questa versione non sono presenti informazioni sul versante
				}
				
				if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO)) {
					// in questa versione non sono presenti informazioni sul versante
					
					// imposto il soggetto pagatore a null
					data.setDebtor(null);
				}
				
				ricevutaRpt.setTipo(it.govpay.pagamento.v3.beans.RicevutaRpt.TipoEnum.CTPAYMENTPA);
				ricevutaRpt.setJson(new RawObject(ConverterUtils.getRptJson(rpt)));

				rsModel.setImporto(paGetPaymentRes_RPT.getData().getPaymentAmount());
				break;
			case SANP_230:
				CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
				
				CtSoggettoVersante soggettoVersante = ctRpt.getSoggettoVersante();

				if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
					if(soggettoVersante == null) {
						soggettoVersante = new CtSoggettoVersante();
						ctRpt.setSoggettoVersante(soggettoVersante);
					}

					if(soggettoVersante.getIdentificativoUnivocoVersante() == null)
						soggettoVersante.setIdentificativoUnivocoVersante(new CtIdentificativoUnivocoPersonaFG());

					UtenzaCittadino cittadino = (UtenzaCittadino) userDetails.getUtenza();
					soggettoVersante.getIdentificativoUnivocoVersante().setCodiceIdentificativoUnivoco(cittadino.getCodIdentificativo());
					soggettoVersante.getIdentificativoUnivocoVersante().setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersFG.F);
					String nomeCognome = cittadino.getProprieta(SPIDAuthenticationDetailsSource.SPID_HEADER_NAME) + " "
							+ cittadino.getProprieta(SPIDAuthenticationDetailsSource.SPID_HEADER_FAMILY_NAME);
					soggettoVersante.setAnagraficaVersante(nomeCognome);
					soggettoVersante.setCapVersante(null);
					soggettoVersante.setCivicoVersante(null);
					//					soggettoVersante.setEMailVersante(cittadino.getProprieta(SPIDAuthenticationDetailsSource.SPID_HEADER_EMAIL)); eMail deve tornare indietro
					soggettoVersante.setIndirizzoVersante(null);
					soggettoVersante.setLocalitaVersante(null);
					soggettoVersante.setNazioneVersante(null);
					soggettoVersante.setProvinciaVersante(null);
				}

				if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO)) {
					if(soggettoVersante == null) {
						soggettoVersante = new CtSoggettoVersante();
						ctRpt.setSoggettoVersante(soggettoVersante);
					}

					if(soggettoVersante.getIdentificativoUnivocoVersante() == null)
						soggettoVersante.setIdentificativoUnivocoVersante(new CtIdentificativoUnivocoPersonaFG());

					soggettoVersante.getIdentificativoUnivocoVersante().setCodiceIdentificativoUnivoco(TIPO_UTENZA.ANONIMO.toString());
					soggettoVersante.getIdentificativoUnivocoVersante().setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersFG.F);
					soggettoVersante.setAnagraficaVersante(TIPO_UTENZA.ANONIMO.toString());
					soggettoVersante.setCapVersante(null);
					soggettoVersante.setCivicoVersante(null);
					//					soggettoVersante.setEMailVersante(value); eMail deve tornare indietro
					soggettoVersante.setIndirizzoVersante(null);
					soggettoVersante.setLocalitaVersante(null);
					soggettoVersante.setNazioneVersante(null);
					soggettoVersante.setProvinciaVersante(null);

					// imposto il soggetto pagatore a null
					ctRpt.setSoggettoPagatore(null);
				}
				
				ricevutaRpt.setTipo(it.govpay.pagamento.v3.beans.RicevutaRpt.TipoEnum.CTRICHIESTAPAGAMENTOTELEMATICO);
				ricevutaRpt.setJson(new RawObject(ConverterUtils.getRptJson(ctRpt)));

				rsModel.setVersante(PendenzeConverter.toSoggettoRsModel(ctRpt.getSoggettoVersante()));

				rsModel.setImporto(ctRpt.getDatiVersamento().getImportoTotaleDaVersare());
				break;
			case SANP_321_V2:
				PaGetPaymentV2Response paGetPaymentV2Response = JaxbUtils.toPaGetPaymentV2Response_RPT(rpt.getXmlRpt(), false);
				
				CtPaymentPAV2 dataV2 = paGetPaymentV2Response.getData();
				
				if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
					// in questa versione non sono presenti informazioni sul versante
				}
				
				if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO)) {
					// in questa versione non sono presenti informazioni sul versante
					
					// imposto il soggetto pagatore a null
					dataV2.setDebtor(null);
				}
				
				ricevutaRpt.setTipo(it.govpay.pagamento.v3.beans.RicevutaRpt.TipoEnum.CTPAYMENTPA);
				ricevutaRpt.setJson(new RawObject(ConverterUtils.getRptJson(rpt)));
				
				rsModel.setImporto(paGetPaymentV2Response.getData().getPaymentAmount());
				break;
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}

		rsModel.setRpt(ricevutaRpt);

		if(rpt.getXmlRt() != null) {
			RicevutaRt ricevutaRt = new RicevutaRt();
			ricevutaRt.setXml(rpt.getXmlRt());

			try {
				switch (rpt.getVersione()) {
				case SANP_240:
					PaSendRTReq paSendRTReq_RT = JaxbUtils.toPaSendRTReq_RT(rpt.getXmlRt(), false);
					
					CtReceipt data = paSendRTReq_RT.getReceipt();
					
					if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
						// in questa versione non sono presenti informazioni sul versante
					}
					
					if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO)) {
						// in questa versione non sono presenti informazioni sul versante
						
						// imposto il soggetto pagatore a null
						data.setDebtor(null);
					}
					
					ricevutaRt.setTipo(TipoEnum.CTRECEIPT);
					ricevutaRt.setJson(new RawObject(ConverterUtils.getRtJson(rpt)));
					rsModel.setImporto(paSendRTReq_RT.getReceipt().getPaymentAmount());
					break;
				case SANP_230:
					CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
					
					CtSoggettoVersante soggettoVersante = ctRt.getSoggettoVersante();

					if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
						if(soggettoVersante == null) {
							soggettoVersante = new CtSoggettoVersante();
							ctRt.setSoggettoVersante(soggettoVersante);
						}

						if(soggettoVersante.getIdentificativoUnivocoVersante() == null)
							soggettoVersante.setIdentificativoUnivocoVersante(new CtIdentificativoUnivocoPersonaFG());

						UtenzaCittadino cittadino = (UtenzaCittadino) userDetails.getUtenza();
						soggettoVersante.getIdentificativoUnivocoVersante().setCodiceIdentificativoUnivoco(cittadino.getCodIdentificativo());
						soggettoVersante.getIdentificativoUnivocoVersante().setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersFG.F);
						String nomeCognome = cittadino.getProprieta(SPIDAuthenticationDetailsSource.SPID_HEADER_NAME) + " "
								+ cittadino.getProprieta(SPIDAuthenticationDetailsSource.SPID_HEADER_FAMILY_NAME);
						soggettoVersante.setAnagraficaVersante(nomeCognome);
						soggettoVersante.setCapVersante(null);
						soggettoVersante.setCivicoVersante(null);
						//					soggettoVersante.setEMailVersante(cittadino.getProprieta(SPIDAuthenticationDetailsSource.SPID_HEADER_EMAIL)); eMail deve tornare indietro
						soggettoVersante.setIndirizzoVersante(null);
						soggettoVersante.setLocalitaVersante(null);
						soggettoVersante.setNazioneVersante(null);
						soggettoVersante.setProvinciaVersante(null);
					}

					if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO)) {
						if(soggettoVersante == null) {
							soggettoVersante = new CtSoggettoVersante();
							ctRt.setSoggettoVersante(soggettoVersante);
						}

						if(soggettoVersante.getIdentificativoUnivocoVersante() == null)
							soggettoVersante.setIdentificativoUnivocoVersante(new CtIdentificativoUnivocoPersonaFG());

						soggettoVersante.getIdentificativoUnivocoVersante().setCodiceIdentificativoUnivoco(TIPO_UTENZA.ANONIMO.toString());
						soggettoVersante.getIdentificativoUnivocoVersante().setTipoIdentificativoUnivoco(StTipoIdentificativoUnivocoPersFG.F);
						soggettoVersante.setAnagraficaVersante(TIPO_UTENZA.ANONIMO.toString());
						soggettoVersante.setCapVersante(null);
						soggettoVersante.setCivicoVersante(null);
						//					soggettoVersante.setEMailVersante(value); eMail deve tornare indietro
						soggettoVersante.setIndirizzoVersante(null);
						soggettoVersante.setLocalitaVersante(null);
						soggettoVersante.setNazioneVersante(null);
						soggettoVersante.setProvinciaVersante(null);

						// imposto il soggetto pagatore a null
						ctRt.setSoggettoPagatore(null);
					}
					
					ricevutaRt.setTipo(TipoEnum.CTRICEVUTATELEMATICA);
					ricevutaRt.setJson(new RawObject(ConverterUtils.getRtJson(ctRt)));
					rsModel.setImporto(ctRt.getDatiPagamento().getImportoTotalePagato());
					break;
				case SANP_321_V2:
					PaSendRTV2Request paSendRTV2Request = JaxbUtils.toPaSendRTV2Request_RT(rpt.getXmlRt(), false);
					
					CtReceiptV2 dataV2 = paSendRTV2Request.getReceipt();
					
					if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
						// in questa versione non sono presenti informazioni sul versante
					}
					
					if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO)) {
						// in questa versione non sono presenti informazioni sul versante
						
						// imposto il soggetto pagatore a null
						dataV2.setDebtor(null);
					}
					
					ricevutaRt.setTipo(TipoEnum.CTRECEIPT);
					ricevutaRt.setJson(new RawObject(ConverterUtils.getRtJson(rpt)));
					rsModel.setImporto(paSendRTV2Request.getReceipt().getPaymentAmount());
					break;
				}
			} catch (Exception e) {
				throw new ServiceException(e);
			}
			rsModel.setRt(ricevutaRt);

		}

		if(rpt.getPagamentoPortale() != null) {
			if(rpt.getPagamentoPortale().getTipo() == 1) {
				rsModel.setModello(ModelloPagamento.ENTE);
			} else if(rpt.getPagamentoPortale().getTipo() == 3) {
				rsModel.setModello(ModelloPagamento.PSP);
			}
		}

		return rsModel;
	}
}