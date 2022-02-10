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
package it.govpay.web.legacy.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.viste.model.Rendicontazione;
import it.govpay.core.beans.GpAvviaTransazionePagamentoResponse.RifTransazione;
import it.govpay.core.beans.VersamentoKey;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTOResponse;
import it.govpay.core.exceptions.RequestValidationException;
import it.govpay.core.legacy.utils.Gp21Utils;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.model.Iuv;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.model.Versionabile.Versione;
import it.govpay.servizi.commons.Canale;
import it.govpay.servizi.commons.FlussoRendicontazione;
import it.govpay.servizi.commons.IuvGenerato;
import it.govpay.servizi.commons.MetaInfo;
import it.govpay.servizi.commons.StatoVersamento;
import it.govpay.servizi.commons.TipoAutenticazione;
import it.govpay.servizi.gpapp.GpCaricaIuv;
import it.govpay.servizi.gpprt.GpAvviaTransazionePagamento;
import it.govpay.servizi.gpprt.GpAvviaTransazionePagamentoResponse;
import it.govpay.servizi.gpprt.GpChiediListaVersamentiResponse.Versamento.SpezzoneCausaleStrutturata;
import it.govpay.servizi.gprnd.GpChiediListaFlussiRendicontazioneResponse;

public class ConverterUtils extends Gp21Utils {

	public static it.govpay.servizi.gpprt.GpChiediListaVersamentiResponse.Versamento toVersamento(Versione versione, Versamento versamento, BDConfigWrapper configWrapper) throws ServiceException {
		it.govpay.servizi.gpprt.GpChiediListaVersamentiResponse.Versamento v = new it.govpay.servizi.gpprt.GpChiediListaVersamentiResponse.Versamento();
		v.setCodApplicazione(versamento.getApplicazione(configWrapper).getCodApplicazione());
		v.setCodVersamentoEnte(versamento.getCodVersamentoEnte());
		v.setDataScadenza(versamento.getDataScadenza());
		v.setImportoTotale(versamento.getImportoTotale());
		v.setStato(StatoVersamento.valueOf(versamento.getStatoVersamento().toString()));
		if(versamento.getCausaleVersamento() instanceof Versamento.CausaleSemplice)
			v.setCausale(((Versamento.CausaleSemplice) versamento.getCausaleVersamento()).getCausale());
		if(versamento.getCausaleVersamento() instanceof Versamento.CausaleSpezzoni)
			v.getSpezzoneCausale().addAll(((Versamento.CausaleSpezzoni) versamento.getCausaleVersamento()).getSpezzoni());
		if(versamento.getCausaleVersamento() instanceof Versamento.CausaleSpezzoniStrutturati) {
			Versamento.CausaleSpezzoniStrutturati c = (Versamento.CausaleSpezzoniStrutturati) versamento.getCausaleVersamento();
			for(int i = 0; i<c.getImporti().size(); i++) {
				SpezzoneCausaleStrutturata s = new SpezzoneCausaleStrutturata();
				s.setCausale(c.getSpezzoni().get(i));
				s.setImporto(c.getImporti().get(i));
				v.getSpezzoneCausaleStrutturata().add(s);
			}
		}
//		if(versione.compareTo(Versione.GP_02_02_00) >=0) { // Versione 2.2
			v.setCodDominio(versamento.getUo(configWrapper).getDominio(configWrapper).getCodDominio());
			Iuv iuv = versamento.getIuv(configWrapper);
			if(iuv != null) {
				it.govpay.core.business.model.Iuv iuvGenerato = IuvUtils.toIuv(versamento.getApplicazione(configWrapper), versamento.getUo(configWrapper).getDominio(configWrapper), iuv, versamento.getImportoTotale());
				v.setIuv(iuv.getIuv());
				v.setBarCode(iuvGenerato.getBarCode());
				v.setQrCode(iuvGenerato.getQrCode());
//				if(versione.compareTo(Versione.GP_02_03_00) >=0) { // Versione 2.3
//					v.setNumeroAvviso(iuvGenerato.getNumeroAvviso());
//				}
			}
//		}
		return v;
	}

	public static FlussoRendicontazione toFr(Fr frModel, List<Rendicontazione> rends, BDConfigWrapper configWrapper) throws ServiceException {

		FlussoRendicontazione fr = new FlussoRendicontazione();
		int annoFlusso = Integer.parseInt(simpleDateFormatAnno.format(frModel.getDataFlusso()));
		fr.setAnnoRiferimento(annoFlusso);
		fr.setCodBicRiversamento(frModel.getCodBicRiversamento());
		fr.setCodFlusso(frModel.getCodFlusso());
		fr.setCodPsp(frModel.getCodPsp());
		fr.setDataFlusso(frModel.getDataFlusso());
		fr.setDataRegolamento(frModel.getDataRegolamento());
		fr.setImportoTotale(BigDecimal.ZERO);
		fr.setNumeroPagamenti(0l);
		fr.setIur(frModel.getIur());

		if(rends != null) {
			for(Rendicontazione rend : rends) {
				Pagamento pagamento = rend.getPagamento();
				it.govpay.bd.model.Rendicontazione rendicontazione = rend.getRendicontazione();
				it.govpay.servizi.commons.FlussoRendicontazione.Pagamento rendicontazionePagamento = ConverterUtils.toRendicontazionePagamento(rendicontazione, pagamento, frModel, configWrapper);
				if(rendicontazionePagamento != null) {
					fr.setImportoTotale(rendicontazione.getImporto().add(fr.getImportoTotale()));
					fr.setNumeroPagamenti(fr.getNumeroPagamenti() + 1);
					fr.getPagamento().add(rendicontazionePagamento);
				}
			}
		}
		
		return fr;
	}

	public static SimpleDateFormat simpleDateFormatAnno = new SimpleDateFormat("yyyy");
	public static it.govpay.servizi.gprnd.GpChiediListaFlussiRendicontazioneResponse.FlussoRendicontazione toFr(Fr frModel) {
		GpChiediListaFlussiRendicontazioneResponse.FlussoRendicontazione efr = new GpChiediListaFlussiRendicontazioneResponse.FlussoRendicontazione();
		int annoFlusso = Integer.parseInt(simpleDateFormatAnno.format(frModel.getDataFlusso()));
		efr.setAnnoRiferimento(annoFlusso);
		efr.setCodBicRiversamento(frModel.getCodBicRiversamento());
		efr.setCodDominio(frModel.getCodDominio());
		efr.setCodFlusso(frModel.getCodFlusso());
		efr.setCodPsp(frModel.getCodPsp());
		efr.setDataFlusso(frModel.getDataFlusso());
		efr.setDataRegolamento(frModel.getDataRegolamento());
		efr.setIur(frModel.getIur());
		efr.setImportoTotale(frModel.getImportoTotalePagamenti());
		efr.setNumeroPagamenti(frModel.getNumeroPagamenti());
		return efr;
	}


	public static List<GpAvviaTransazionePagamentoResponse.RifTransazione> toRifTransazione(PagamentiPortaleDTOResponse pagamentiPortaleDTOResponse){
		List<GpAvviaTransazionePagamentoResponse.RifTransazione> rifTransazioni = new ArrayList<GpAvviaTransazionePagamentoResponse.RifTransazione>();

		it.govpay.core.beans.GpAvviaTransazionePagamentoResponse transazioneResponse = pagamentiPortaleDTOResponse.getTransazioneResponse();
		
		for(RifTransazione rifTransazioneModel : transazioneResponse.getRifTransazione()) {
			GpAvviaTransazionePagamentoResponse.RifTransazione rifTransazione = new GpAvviaTransazionePagamentoResponse.RifTransazione();
			rifTransazione.setCcp(rifTransazioneModel.getCcp());
			rifTransazione.setCodApplicazione(rifTransazioneModel.getCodApplicazione());
			rifTransazione.setCodDominio(rifTransazioneModel.getCodDominio());
			rifTransazione.setCodVersamentoEnte(rifTransazioneModel.getCodVersamentoEnte());
			rifTransazione.setIuv(rifTransazioneModel.getIuv());
			rifTransazioni.add(rifTransazione);
		}

		return rifTransazioni;
	}

	public static List<it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto> toIuvRichiesto(List<it.govpay.servizi.gpapp.GpGeneraIuv.IuvRichiesto> iuvRichiesti) {
		List<it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto> iuvRichiestiModel = new ArrayList<it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto>();
		for (it.govpay.servizi.gpapp.GpGeneraIuv.IuvRichiesto iuvRichiesto : iuvRichiesti) {
			it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto iuvRichiestoModel = new it.govpay.core.business.model.GeneraIuvDTO.IuvRichiesto();
			iuvRichiestoModel.setCodVersamentoEnte(iuvRichiesto.getCodVersamentoEnte());
			iuvRichiestoModel.setImportoTotale(iuvRichiesto.getImportoTotale());
			iuvRichiestiModel.add(iuvRichiestoModel);
		}
		return iuvRichiestiModel;
	}

	public static List<it.govpay.core.business.model.CaricaIuvDTO.Iuv> toIuvDaCaricare(List<it.govpay.servizi.gpapp.GpCaricaIuv.IuvGenerato> iuvRichiesti) {
		List<it.govpay.core.business.model.CaricaIuvDTO.Iuv> iuvRichiestiModel = new ArrayList<it.govpay.core.business.model.CaricaIuvDTO.Iuv>();
		for (it.govpay.servizi.gpapp.GpCaricaIuv.IuvGenerato iuvRichiesto : iuvRichiesti) {
			it.govpay.core.business.model.CaricaIuvDTO.Iuv iuvRichiestoModel = new it.govpay.core.business.model.CaricaIuvDTO.Iuv();
			iuvRichiestoModel.setCodVersamentoEnte(iuvRichiesto.getCodVersamentoEnte());
			iuvRichiestoModel.setImportoTotale(iuvRichiesto.getImportoTotale());
			iuvRichiestoModel.setIuv(iuvRichiesto.getIuv());
			iuvRichiestiModel.add(iuvRichiestoModel);
		}
		return iuvRichiestiModel;
	}

	public static Collection<? extends IuvGenerato> toIuvCaricato(BDConfigWrapper configWrapper, GpCaricaIuv bodyrichiesta, Applicazione applicazione) throws ServiceException, NotFoundException {
		List<IuvGenerato> iuvCaricati = new ArrayList<IuvGenerato>();
		for (it.govpay.servizi.gpapp.GpCaricaIuv.IuvGenerato iuvDaCaricareModel : bodyrichiesta.getIuvGenerato()) {
			Dominio dominio = AnagraficaManager.getDominio(configWrapper, bodyrichiesta.getCodDominio());

			it.govpay.model.Iuv iuv = new it.govpay.model.Iuv();
			iuv.setIdDominio(dominio.getId());
			iuv.setPrg(0);
			iuv.setIuv(iuvDaCaricareModel.getIuv());
			iuv.setDataGenerazione(new Date());
			iuv.setIdApplicazione(applicazione.getId());
			iuv.setTipo(TipoIUV.NUMERICO);
			iuv.setCodVersamentoEnte(iuvDaCaricareModel.getCodVersamentoEnte());
			iuv.setApplicationCode(dominio.getStazione().getApplicationCode());

			it.govpay.core.business.model.Iuv iuvCaricatoModel = IuvUtils.toIuv(applicazione, dominio, iuv, iuvDaCaricareModel.getImportoTotale());

			iuvCaricati.add(toIuvGenerato(iuvCaricatoModel, applicazione)); 
		}
		return iuvCaricati;
	}

	public static PagamentiPortaleDTO getPagamentiPortaleDTO(GpAvviaTransazionePagamento bodyrichiesta,
			MetaInfo metaInfo, Authentication user, String idSessione, Logger log) throws Exception {

		String idSessionePortale = bodyrichiesta.getCodSessionePortale();
		
		PagamentiPortaleDTO pagamentiPortaleDTO = new PagamentiPortaleDTO(user);
//		GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(user);

		pagamentiPortaleDTO.setIdSessione(idSessione);
		pagamentiPortaleDTO.setIdSessionePortale(idSessionePortale);
		if(bodyrichiesta.getAutenticazione() != null)
			pagamentiPortaleDTO.setAutenticazioneSoggetto(bodyrichiesta.getAutenticazione().toString());
		else 
			pagamentiPortaleDTO.setAutenticazioneSoggetto(TipoAutenticazione.N_A.toString());

//		pagamentiPortaleDTO.setCredenzialiPagatore(pagamentiPortaleRequest.getCredenzialiPagatore());
//		pagamentiPortaleDTO.setDataEsecuzionePagamento(pagamentiPortaleRequest.getDataEsecuzionePagamento());

		if(bodyrichiesta.getIbanAddebito() != null) {
//			pagamentiPortaleDTO.setBicAddebito(pagamentiPortaleRequest.getContoAddebito().getBic());
			pagamentiPortaleDTO.setIbanAddebito(bodyrichiesta.getIbanAddebito());
		}

		pagamentiPortaleDTO.setUrlRitorno(bodyrichiesta.getUrlRitorno());


//		PagamentiPortaleConverter.controlloUtenzaVersante(pagamentiPortaleRequest, user);
		pagamentiPortaleDTO.setVersante(toAnagraficaCommons(bodyrichiesta.getVersante()));

		if(bodyrichiesta.getVersamentoOrVersamentoRef() != null && bodyrichiesta.getVersamentoOrVersamentoRef().size() > 0 ) {
			List<Object> listRefs = new ArrayList<>();

			int i =0;
			for (Object pendenzaObj: bodyrichiesta.getVersamentoOrVersamentoRef()) {
				if(pendenzaObj instanceof it.govpay.servizi.commons.Versamento) {
					it.govpay.core.dao.commons.Versamento versamento = toVersamentoCommons((it.govpay.servizi.commons.Versamento) pendenzaObj);

					listRefs.add(versamento);
				} else if(pendenzaObj instanceof it.govpay.servizi.commons.VersamentoKey) {
					it.govpay.servizi.commons.VersamentoKey pendenzaKey = (it.govpay.servizi.commons.VersamentoKey) pendenzaObj;
					
					VersamentoKey versamentoKey = new VersamentoKey();
					
					Iterator<JAXBElement<String>> iterator = pendenzaKey.getContent().iterator();
                    while(iterator.hasNext()){
                            JAXBElement<String> element = iterator.next();

                            if(element.getName().equals(VersamentoUtils._VersamentoKeyBundlekey_QNAME)) {
                            	versamentoKey.setBundlekey(element.getValue());
                            }
                            if(element.getName().equals(VersamentoUtils._VersamentoKeyCodUnivocoDebitore_QNAME)) {
                            	versamentoKey.setCodUnivocoDebitore(element.getValue());
                            }
                            if(element.getName().equals(VersamentoUtils._VersamentoKeyCodApplicazione_QNAME)) {
                            	versamentoKey.setCodApplicazione(element.getValue());
                            }
                            if(element.getName().equals(VersamentoUtils._VersamentoKeyCodDominio_QNAME)) {
                            	versamentoKey.setCodDominio(element.getValue());
                            }
                            if(element.getName().equals(VersamentoUtils._VersamentoKeyCodVersamentoEnte_QNAME)) {
                            	versamentoKey.setCodVersamentoEnte(element.getValue());
                            }
                            if(element.getName().equals(VersamentoUtils._VersamentoKeyIuv_QNAME)) {
                            	versamentoKey.setIuv(element.getValue());
                            }
                    }

                    listRefs.add(versamentoKey);
				} else {
					throw new RequestValidationException("La pendenza "+(i+1)+" e' di un tipo non riconosciuto.");
				}
				i++;
			}

			pagamentiPortaleDTO.setPendenzeOrPendenzeRef(listRefs);
		}
		
		if(bodyrichiesta.getCanale() != null) {
			Canale canale = bodyrichiesta.getCanale();
			
			pagamentiPortaleDTO.setIdentificativoCanale(canale.getCodCanale());
			pagamentiPortaleDTO.setIdentificativoIntermediarioPSP(canale.getCodIntermediarioPsp());
			pagamentiPortaleDTO.setIdentificativoPSP(canale.getCodPsp());
			if(canale.getTipoVersamento() != null) {
				pagamentiPortaleDTO.setTipoVersamento(canale.getTipoVersamento().toString());
			}
		}

		// Salvataggio del messaggio di richiesta sul db
		//		pagamentiPortaleDTO.setJsonRichiesta(jsonRichiesta);
//		pagamentiPortaleDTO.setJsonRichiesta(pagamentiPortaleRequest.toJSON(null));
		
		pagamentiPortaleDTO.setCodiceConvenzione(bodyrichiesta.getCodConvenzione());
		

		return pagamentiPortaleDTO;
	}

}
