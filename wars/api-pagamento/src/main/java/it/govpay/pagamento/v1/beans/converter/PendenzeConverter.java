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
package it.govpay.pagamento.v1.beans.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.springframework.security.core.Authentication;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.DateUtils;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pagamento.v1.beans.Avviso;
import it.govpay.pagamento.v1.beans.Avviso.StatoEnum;
import it.govpay.pagamento.v1.beans.Pagamento;
import it.govpay.pagamento.v1.beans.Pendenza;
import it.govpay.pagamento.v1.beans.PendenzaIndex;
import it.govpay.pagamento.v1.beans.Rpp;
import it.govpay.pagamento.v1.beans.Segnalazione;
import it.govpay.pagamento.v1.beans.Soggetto;
import it.govpay.pagamento.v1.beans.StatoPendenza;
import it.govpay.pagamento.v1.beans.TassonomiaAvviso;
import it.govpay.pagamento.v1.beans.VocePendenza;
import it.govpay.pagamento.v1.beans.VocePendenza.TipoBolloEnum;
import it.govpay.pagamento.v1.beans.VocePendenza.TipoContabilitaEnum;

public class PendenzeConverter {

	public static Pendenza toRsModel(LeggiPendenzaDTOResponse dto, Authentication user) throws ServiceException, IOException {
		return toRsModel(dto.getVersamento(), dto.getPagamenti(), dto.getRpts(),user);
	}

	public static Pendenza toRsModel(it.govpay.bd.model.Versamento versamento,List<PagamentoPortale> pagamenti, List<Rpt> rpts, Authentication user) throws ServiceException, IOException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		Pendenza rsModel = new Pendenza();

		if(versamento.getCodAnnoTributario()!= null)
			rsModel.setAnnoRiferimento(new BigDecimal(versamento.getCodAnnoTributario()));

		rsModel.setCartellaPagamento(versamento.getCodLotto());

		if(versamento.getCausaleVersamento()!= null)
			try {
				rsModel.setCausale(versamento.getCausaleVersamento().getSimple());
			} catch (UnsupportedEncodingException e) {
				throw new IOException(e);
			}

		rsModel.setDataCaricamento(versamento.getDataCreazione());
		rsModel.setDataPagamento(versamento.getDataPagamento());
		rsModel.setDataScadenza(versamento.getDataScadenza());
		rsModel.setDataValidita(versamento.getDataValidita());
		Dominio dominio = versamento.getDominio(configWrapper);
		rsModel.setDominio(DominiConverter.toRsModelIndex(dominio));
		rsModel.setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNome(versamento.getNome());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setSoggettoPagatore(controlloUtenzaPagatore(AnagraficaConverter.toSoggettoRsModel(versamento.getAnagraficaDebitore()),user));
		if(versamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(versamento.getDatiAllegati()));

		StatoPendenza statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoPendenza.ANNULLATA;
			break;
		case ESEGUITO: statoPendenza = StatoPendenza.ESEGUITA;
			break;
		case ESEGUITO_ALTRO_CANALE:  statoPendenza = StatoPendenza.ESEGUITA;
			break;
		case NON_ESEGUITO: if(versamento.getDataScadenza() != null && DateUtils.isDataDecorsa(versamento.getDataScadenza(), DateUtils.CONTROLLO_SCADENZA)) {statoPendenza = StatoPendenza.SCADUTA;} else { statoPendenza = StatoPendenza.NON_ESEGUITA;}
			break;
		case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoPendenza.ESEGUITA_PARZIALE;
			break;
		default:
			break;

		}

		rsModel.setStato(statoPendenza);
		rsModel.setTassonomia(versamento.getTassonomia());
		rsModel.setTassonomiaAvviso(TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso()));
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());

		rsModel.setSegnalazioni(unmarshall(versamento.getAnomalie()));

		UnitaOperativa uo = versamento.getUo(configWrapper);
		if(uo != null && !uo.getCodUo().equals(it.govpay.model.Dominio.EC))
			rsModel.setUnitaOperativa(DominiConverter.toUnitaOperativaRsModel(uo));

		List<VocePendenza> v = new ArrayList<>();
		int indice = 1;
		for(SingoloVersamento s: versamento.getSingoliVersamenti()) {
			v.add(toVocePendenzaRsModel(s, indice++, configWrapper));
		}
		rsModel.setVoci(v);

		List<Pagamento> listaPagamentoIndex = new ArrayList<>();

		if(pagamenti != null && pagamenti.size() > 0) {
			for (PagamentoPortale pagamento : pagamenti) {
				listaPagamentoIndex.add(PagamentiPortaleConverter.toRsModel(pagamento,user));
			}
		}
		rsModel.setPagamenti(listaPagamentoIndex);

		List<Rpp> rpps = new ArrayList<>();
		if(rpts != null && rpts.size() > 0) {
			for (Rpt rpt : rpts) {
				rpps.add(RptConverter.toRsModel(rpt, rpt.getVersamento(), rpt.getVersamento().getApplicazione(configWrapper), user));
			}
		}
		rsModel.setRpp(rpps);

		return rsModel;
	}

	private static List<Segnalazione> unmarshall(String anomalie) {
		List<Segnalazione> list = new ArrayList<>();

		if(anomalie == null || anomalie.isEmpty()) return list;

		String[] split = anomalie.split("\\|");
		for(String s : split){
			String[] split2 = s.split("#");
			Segnalazione a = new Segnalazione();
			a.setCodice(split2[0]);
			a.setDescrizione(split2[1]);
			list.add(a);
		}
		return list;
	}

	public static PendenzaIndex toRsModelIndex(it.govpay.bd.model.Versamento versamento, Authentication user) throws ServiceException, IOException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		PendenzaIndex rsModel = new PendenzaIndex();

		if(versamento.getCodAnnoTributario()!= null)
			rsModel.setAnnoRiferimento(new BigDecimal(versamento.getCodAnnoTributario()));

		rsModel.setCartellaPagamento(versamento.getCodLotto());

		if(versamento.getCausaleVersamento()!= null)
			try {
				rsModel.setCausale(versamento.getCausaleVersamento().getSimple());
			} catch (UnsupportedEncodingException e) {
				throw new IOException(e);
			}

		rsModel.setDataCaricamento(versamento.getDataCreazione());
		rsModel.setDataPagamento(versamento.getDataPagamento());
		rsModel.setDataScadenza(versamento.getDataScadenza());
		rsModel.setDataValidita(versamento.getDataValidita());

		rsModel.setDominio(DominiConverter.toRsModelIndex(versamento.getDominio(configWrapper)));
		rsModel.setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());

		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNome(versamento.getNome());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setSoggettoPagatore(controlloUtenzaPagatore(AnagraficaConverter.toSoggettoRsModel(versamento.getAnagraficaDebitore()),user));
		if(versamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(versamento.getDatiAllegati());

		StatoPendenza statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoPendenza.ANNULLATA;
			break;
		case ESEGUITO: statoPendenza = StatoPendenza.ESEGUITA;
			break;
		case ESEGUITO_ALTRO_CANALE:  statoPendenza = StatoPendenza.ESEGUITA;
			break;
		case NON_ESEGUITO: if(versamento.getDataScadenza() != null && DateUtils.isDataDecorsa(versamento.getDataScadenza(), DateUtils.CONTROLLO_SCADENZA)) {statoPendenza = StatoPendenza.SCADUTA;} else { statoPendenza = StatoPendenza.NON_ESEGUITA;}
			break;
		case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoPendenza.ESEGUITA_PARZIALE;
			break;
		default:
			break;

		}

		rsModel.setStato(statoPendenza);
		rsModel.setTassonomia(versamento.getTassonomia());
		rsModel.setTassonomiaAvviso(TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso()));
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());

		UnitaOperativa uo = versamento.getUo(configWrapper);
		if(uo != null && !uo.getCodUo().equals(it.govpay.model.Dominio.EC))
			rsModel.setUnitaOperativa(DominiConverter.toUnitaOperativaRsModel(uo));

		rsModel.setPagamenti(UriBuilderUtils.getPagamentiByIdA2AIdPendenza(versamento.getApplicazione(configWrapper).getCodApplicazione(),versamento.getCodVersamentoEnte()));
		rsModel.setRpp(UriBuilderUtils.getRppsByIdA2AIdPendenza(versamento.getApplicazione(configWrapper).getCodApplicazione(),versamento.getCodVersamentoEnte()));

		return rsModel;
	}

	public static VocePendenza toVocePendenzaRsModel(it.govpay.bd.model.SingoloVersamento singoloVersamento, int indice, BDConfigWrapper configWrapper) throws ServiceException {
		VocePendenza rsModel = new VocePendenza();
		if(singoloVersamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(singoloVersamento.getDatiAllegati()));
		rsModel.setDescrizione(singoloVersamento.getDescrizione());
		rsModel.setDescrizioneCausaleRPT(singoloVersamento.getDescrizioneCausaleRPT());

		rsModel.setIdVocePendenza(singoloVersamento.getCodSingoloVersamentoEnte());
		rsModel.setImporto(singoloVersamento.getImportoSingoloVersamento());
		rsModel.setIndice(new BigDecimal(indice));

		switch(singoloVersamento.getStatoSingoloVersamento()) {
		case ESEGUITO:rsModel.setStato(VocePendenza.StatoEnum.ESEGUITO);
			break;
		case NON_ESEGUITO:rsModel.setStato(VocePendenza.StatoEnum.NON_ESEGUITO);
			break;
		default:
			break;}

		// Definisce i dati di un bollo telematico
		if(singoloVersamento.getHashDocumento() != null && singoloVersamento.getTipoBollo() != null && singoloVersamento.getProvinciaResidenza() != null) {
			rsModel.setHashDocumento(singoloVersamento.getHashDocumento());
			rsModel.setTipoBollo(TipoBolloEnum.fromCodificaPagoPA(singoloVersamento.getTipoBollo().getCodifica()));
			rsModel.setProvinciaResidenza(singoloVersamento.getProvinciaResidenza());
		} else if(singoloVersamento.getTributo(configWrapper) != null && singoloVersamento.getTributo(configWrapper).getCodTributo() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
			rsModel.setCodEntrata(singoloVersamento.getTributo(configWrapper).getCodTributo());
		} else { // Definisce i dettagli di incasso della singola entrata.
			rsModel.setCodiceContabilita(singoloVersamento.getCodContabilita());
			rsModel.setIbanAccredito(singoloVersamento.getIbanAccredito(configWrapper).getCodIban());
			if(singoloVersamento.getIbanAppoggio(configWrapper) != null)
				rsModel.setIbanAppoggio(singoloVersamento.getIbanAppoggio(configWrapper).getCodIban());
			rsModel.setTipoContabilita(TipoContabilitaEnum.fromValue(singoloVersamento.getTipoContabilita().name()));
		}


		return rsModel;
	}


	public static Avviso toAvvisoRsModel(it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Dominio dominio, String barCode, String qrCode) throws ServiceException, IOException {
		Avviso rsModel = new Avviso();

		if(versamento.getCausaleVersamento()!= null)
			try {
				rsModel.setDescrizione(versamento.getCausaleVersamento().getSimple());
			} catch (UnsupportedEncodingException e) {
				throw new IOException(e);
			}

		rsModel.setDataScadenza(versamento.getDataScadenza());
		rsModel.setDataPagamento(versamento.getDataPagamento());
		rsModel.setDataValidita(versamento.getDataValidita());
		rsModel.setIdDominio(dominio.getCodDominio());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setTassonomiaAvviso(TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso()));
		rsModel.setBarcode(barCode);
		rsModel.setQrcode(qrCode);

		StatoEnum statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoEnum.ANNULLATO;
			break;
		case ESEGUITO: statoPendenza = StatoEnum.PAGATO;
			break;
		case ESEGUITO_ALTRO_CANALE:  statoPendenza = StatoEnum.PAGATO;
			break;
		case NON_ESEGUITO: if(versamento.getDataScadenza() != null && DateUtils.isDataDecorsa(versamento.getDataScadenza(), DateUtils.CONTROLLO_SCADENZA)) {statoPendenza = StatoEnum.SCADUTO;} else { statoPendenza = StatoEnum.NON_PAGATO;}
			break;
		case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoEnum.PAGATO;
			break;
		default:
			break;

		}

		rsModel.setStato(statoPendenza);

		return rsModel;
	}

	public static Soggetto controlloUtenzaPagatore(Soggetto soggetto, Authentication user) {

		GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(user);

		if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
//			if(soggetto == null) {
//				soggetto = new Soggetto();
//			}
//
//			UtenzaCittadino cittadino = (UtenzaCittadino) userDetails.getUtenza();
//			soggetto.setIdentificativo(cittadino.getCodIdentificativo());
//			String nomeCognome = cittadino.getProprieta(SPIDAuthenticationDetailsSource.SPID_HEADER_NAME) + " "
//					+ cittadino.getProprieta(SPIDAuthenticationDetailsSource.SPID_HEADER_FAMILY_NAME);
//			soggetto.setAnagrafica(nomeCognome);
//			soggetto.setEmail(cittadino.getProprieta(SPIDAuthenticationDetailsSource.SPID_HEADER_EMAIL));
//			soggetto.setTipo(TipoEnum.F);
//			soggetto.setCap(null);
//			soggetto.setCellulare(null);
//			soggetto.setCivico(null);
//			soggetto.setIndirizzo(null);
//			soggetto.setLocalita(null);
//			soggetto.setNazione(null);
//			soggetto.setProvincia(null);
		}

		if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO)) {
			return null;
		}

		return soggetto;
	}
}
