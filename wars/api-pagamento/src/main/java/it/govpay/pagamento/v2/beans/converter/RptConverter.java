package it.govpay.pagamento.v2.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.springframework.security.core.Authentication;

import it.gov.digitpa.schemas._2011.pagamenti.CtIdentificativoUnivocoPersonaFG;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoVersante;
import it.gov.digitpa.schemas._2011.pagamenti.StTipoIdentificativoUnivocoPersFG;
import it.gov.pagopa.pagopa_api.pa.pafornode.CtPaymentPA;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.govpay.bd.model.UtenzaCittadino;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pagamento.v2.beans.Rpp;
import it.govpay.pagamento.v2.beans.RppIndex;
import it.govpay.rs.v1.ConverterUtils;
import it.govpay.rs.v1.authentication.SPIDAuthenticationDetailsSource;

public class RptConverter {


	public static Rpp toRsModel(it.govpay.bd.model.Rpt rpt, it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Applicazione applicazione, Authentication user) throws ServiceException {
		Rpp rsModel = new Rpp();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(PendenzeConverter.toRsModelIndex(versamento,user));

		GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(user);
		try {
			if(rpt.getXmlRpt() != null) {
				switch (rpt.getVersione()) {
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

					rsModel.setRpt(new RawObject(ConverterUtils.getRptJson(ctRpt)));
					break;
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
					
					rsModel.setRpt(ConverterUtils.getRptJson(paGetPaymentRes_RPT));
					break;
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}


		try {
			if(rpt.getXmlRt() != null) {
				rsModel.setRt(new RawObject(ConverterUtils.getRtJson(rpt)));
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}

		return rsModel;
	}

	public static RppIndex toRsModelIndex(it.govpay.bd.model.Rpt rpt, it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Applicazione applicazione, Authentication user) throws ServiceException {
		RppIndex rsModel = new RppIndex();

		rsModel.setStato(rpt.getStato().toString());
		rsModel.setDettaglioStato(rpt.getDescrizioneStato());
		rsModel.setPendenza(PendenzeConverter.toRsModelIndex(versamento,user));

		GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(user);
		try {
			if(rpt.getXmlRpt() != null) {
				switch (rpt.getVersione()) {
				case SANP_230:
					CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);

					CtSoggettoVersante soggettoVersante = ctRpt.getSoggettoVersante();

					if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
						if(soggettoVersante == null) {
							soggettoVersante = new CtSoggettoVersante();
							ctRpt.setSoggettoVersante(soggettoVersante);
						}

						UtenzaCittadino cittadino = (UtenzaCittadino) userDetails.getUtenza();

						if(soggettoVersante.getIdentificativoUnivocoVersante() == null)
							soggettoVersante.setIdentificativoUnivocoVersante(new CtIdentificativoUnivocoPersonaFG());

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

					rsModel.setRpt(new RawObject(ConverterUtils.getRptJson(ctRpt)));
					break;
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
					
					rsModel.setRpt(ConverterUtils.getRptJson(paGetPaymentRes_RPT));
					break;
				}
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}


		try {
			if(rpt.getXmlRt() != null) {
				rsModel.setRt(new RawObject(ConverterUtils.getRtJson(rpt)));
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}

		return rsModel;
	}
}
