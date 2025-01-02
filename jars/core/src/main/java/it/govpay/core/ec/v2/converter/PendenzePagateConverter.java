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
package it.govpay.core.ec.v2.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoVersante;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.core.exceptions.IOException;
import it.govpay.ec.v2.beans.Documento;
import it.govpay.ec.v2.beans.PendenzaPagata;
import it.govpay.ec.v2.beans.RiscossioneVocePagata;
import it.govpay.ec.v2.beans.Soggetto;
import it.govpay.ec.v2.beans.TipoRiferimentoVocePendenza.TipoBolloEnum;
import it.govpay.ec.v2.beans.TipoRiscossione;
import it.govpay.ec.v2.beans.TipoSoggetto;
import it.govpay.ec.v2.beans.TipoSogliaVincoloPagamento;
import it.govpay.ec.v2.beans.VincoloPagamento;
import it.govpay.ec.v2.beans.VocePendenzaPagata;

public class PendenzePagateConverter {

	public static PendenzaPagata toRsModel(it.govpay.bd.model.Rpt rpt) throws ServiceException, IOException, UnsupportedEncodingException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		PendenzaPagata rsModel = new PendenzaPagata();
		
		Versamento versamento = rpt.getVersamento(configWrapper);

		if(versamento.getCodAnnoTributario()!= null)
			rsModel.setAnnoRiferimento(new BigDecimal(versamento.getCodAnnoTributario()));

		rsModel.setCartellaPagamento(versamento.getCodLotto());

		if(versamento.getCausaleVersamento()!= null)
			rsModel.setCausale(versamento.getCausaleVersamento().getSimple());

		rsModel.setDataScadenza(versamento.getDataScadenza());

		rsModel.setDominio(DominiConverter.toRsModelIndex(versamento.getDominio(configWrapper)));
		rsModel.setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		if(versamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(versamento.getDatiAllegati()));

		UnitaOperativa uo = versamento.getUo(configWrapper);
		if(uo != null && !uo.getCodUo().equals(it.govpay.model.Dominio.EC)) {
			rsModel.setUnitaOperativa(DominiConverter.toRsModelIndex(uo));
		}

		rsModel.setIdTipoPendenza(versamento.getTipoVersamento(configWrapper).getCodTipoVersamento());
		rsModel.setDirezione(versamento.getDirezione());
		rsModel.setDivisione(versamento.getDivisione());
		rsModel.setTassonomia(versamento.getTassonomia()); 
		rsModel.setUUID(versamento.getIdSessione());
		rsModel.setSoggettoPagatore(toSoggettoRsModel(versamento.getAnagraficaDebitore()));

		rsModel.setDocumento(toDocumentoRsModel(versamento));
		
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setDataValidita(versamento.getDataValidita());
		rsModel.setProprieta(PendenzeConverter.toProprietaPendenzaRsModel(versamento.getProprietaPendenza()));
		
		// Ciclo i singoli versamenti per inserire le voci
		for(SingoloVersamento sv : versamento.getSingoliVersamenti()) {
			
			// Di ogni voce cerco, se esiste, la riscossione associata
			int indiceDati = sv.getIndiceDati() == null ? 0 : sv.getIndiceDati().intValue();
			Pagamento pagamento = null;
			for(Pagamento p : rpt.getPagamenti()) {
				if(p.getIndiceDati() == indiceDati) {
					pagamento = p;
					break;
				}
			}
			rsModel.addVociItem(toRsModelVocePendenzaPagata(sv, pagamento));
		}
		return rsModel;
	}

	private static Documento toDocumentoRsModel(Versamento versamento) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		if(versamento.getDocumento(configWrapper) != null) {
			it.govpay.bd.model.Documento documento = versamento.getDocumento(configWrapper);

			Documento rsModel = new Documento();

			rsModel.setDescrizione(documento.getDescrizione());
			rsModel.setIdentificativo(documento.getCodDocumento());
			if(versamento.getNumeroRata() != null)
				rsModel.setRata(new BigDecimal(versamento.getNumeroRata()));
			if(versamento.getTipoSoglia() != null) {
				VincoloPagamento soglia = new VincoloPagamento();
				
				if(versamento.getGiorniSoglia() != null)
					soglia.setGiorni(new BigDecimal(versamento.getGiorniSoglia()));

				switch(versamento.getTipoSoglia()) {
				case ENTRO:
					soglia.setTipo(TipoSogliaVincoloPagamento.ENTRO.toString());
					break;
				case OLTRE:
					soglia.setTipo(TipoSogliaVincoloPagamento.OLTRE.toString());
					break;
				case RIDOTTO:
					soglia.setTipo(TipoSogliaVincoloPagamento.RIDOTTO.toString());
					break;
				case SCONTATO:
					soglia.setTipo(TipoSogliaVincoloPagamento.SCONTATO.toString());
					break;
				}
				rsModel.setSoglia(soglia );
			}
			return rsModel;
		}
		return null;
	}

	public static VocePendenzaPagata toRsModelVocePendenzaPagata(SingoloVersamento singoloVersamento, Pagamento pagamento) throws ServiceException, IOException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		VocePendenzaPagata rsModel = new VocePendenzaPagata();

		if(singoloVersamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(singoloVersamento.getDatiAllegati()));
		rsModel.setDescrizione(singoloVersamento.getDescrizione());
		rsModel.setDescrizioneCausaleRPT(singoloVersamento.getDescrizioneCausaleRPT());

		if(singoloVersamento.getDominio(configWrapper) != null) {
			rsModel.setDominio(DominiConverter.toRsModelIndex(singoloVersamento.getDominio(configWrapper)));
		}

		rsModel.setIdVocePendenza(singoloVersamento.getCodSingoloVersamentoEnte());
		//		rsModel.setImporto(singoloVersamento.getImportoSingoloVersamento());
		//		rsModel.setIndice(new BigDecimal(indice));
		rsModel.setContabilita(ContabilitaConverter.toRsModel(singoloVersamento.getContabilita()));
		
		
		// Definisce i dati di un bollo telematico
		if(singoloVersamento.getHashDocumento() != null && singoloVersamento.getTipoBollo() != null && singoloVersamento.getProvinciaResidenza() != null) {
			rsModel.setHashDocumento(singoloVersamento.getHashDocumento());
			
			switch(singoloVersamento.getTipoBollo()) {
			case IMPOSTA_BOLLO:
				rsModel.setTipoBollo(TipoBolloEnum._01);
				break;
			}
			rsModel.setProvinciaResidenza(singoloVersamento.getProvinciaResidenza());
			if(singoloVersamento.getTipoContabilita() != null && singoloVersamento.getCodContabilita() != null)
				rsModel.setCodiceTassonomicoPagoPA(singoloVersamento.getTipoContabilita().getCodifica() + "/"+ singoloVersamento.getCodContabilita());
		} else if(singoloVersamento.getTributo(configWrapper) != null && singoloVersamento.getTributo(configWrapper).getCodTributo() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
			rsModel.setCodEntrata(singoloVersamento.getTributo(configWrapper).getCodTributo());
		} else { // Definisce i dettagli di incasso della singola entrata.
			rsModel.setCodiceTassonomicoPagoPA(singoloVersamento.getCodContabilita());
			rsModel.setIbanAccredito(singoloVersamento.getIbanAccredito(configWrapper).getCodIban());
			if(singoloVersamento.getIbanAppoggio(configWrapper) !=null)
				rsModel.setIbanAccredito(singoloVersamento.getIbanAppoggio(configWrapper).getCodIban());
			if(singoloVersamento.getTipoContabilita() != null && singoloVersamento.getCodContabilita() != null)
				rsModel.setCodiceTassonomicoPagoPA(singoloVersamento.getTipoContabilita().getCodifica() + "/"+ singoloVersamento.getCodContabilita());
		}
		
		if(pagamento != null) {
			RiscossioneVocePagata riscossione = toRsModel(pagamento);
			rsModel.setRiscossione(riscossione );
		}
		
		return rsModel;
	}
	
	public static RiscossioneVocePagata toRsModel(it.govpay.bd.model.Pagamento input) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		RiscossioneVocePagata rsModel = new RiscossioneVocePagata();
		rsModel.setIdDominio(input.getDominio(configWrapper).getCodDominio());
		rsModel.setIuv(input.getIuv());
		rsModel.setIur(input.getIur());
		rsModel.setIndice(new BigDecimal(input.getIndiceDati()));
		rsModel.setImporto(input.getImportoPagato());
		rsModel.setData(input.getDataPagamento());
		switch (input.getTipo()) {
		case ALTRO_INTERMEDIARIO:
			rsModel.setTipo(TipoRiscossione.ALTRO_INTERMEDIARIO);
			break;
		case ENTRATA:
			rsModel.setTipo(TipoRiscossione.ENTRATA);
			break;
		case MBT:
			rsModel.setTipo(TipoRiscossione.MBT);
			break;
		case ENTRATA_PA_NON_INTERMEDIATA:
			rsModel.setTipo(TipoRiscossione.ENTRATA_PA_NON_INTERMEDIATA);
			break;
		} 

		return rsModel;
	}

	public static Soggetto toSoggettoRsModel(it.govpay.model.Anagrafica anagrafica) {
		if(anagrafica == null) return null;
		Soggetto rsModel = new Soggetto();

		if(anagrafica.getTipo() != null)
			rsModel.setTipo(TipoSoggetto.fromValue(anagrafica.getTipo().toString()));

		rsModel.setIdentificativo(anagrafica.getCodUnivoco());
		rsModel.setAnagrafica(anagrafica.getRagioneSociale());
		rsModel.setIndirizzo(anagrafica.getIndirizzo());
		rsModel.setCivico(anagrafica.getCivico());
		rsModel.setCap(anagrafica.getCap());
		rsModel.setLocalita(anagrafica.getLocalita());
		rsModel.setProvincia(anagrafica.getProvincia());
		rsModel.setNazione(anagrafica.getNazione());
		rsModel.setEmail(anagrafica.getEmail());
		rsModel.setCellulare(anagrafica.getCellulare());

		return rsModel;
	}

	public static Soggetto toSoggettoRsModel(CtSoggettoVersante soggettoVersante) {
		if(soggettoVersante == null) return null;
		Soggetto rsModel = new Soggetto();

		if(soggettoVersante.getIdentificativoUnivocoVersante().getTipoIdentificativoUnivoco() != null)
			rsModel.setTipo(TipoSoggetto.fromValue(soggettoVersante.getIdentificativoUnivocoVersante().getTipoIdentificativoUnivoco().toString()));

		rsModel.setIdentificativo(soggettoVersante.getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco());
		rsModel.setAnagrafica(soggettoVersante.getAnagraficaVersante());
		rsModel.setIndirizzo(soggettoVersante.getIndirizzoVersante());
		rsModel.setCivico(soggettoVersante.getCivicoVersante());
		rsModel.setCap(soggettoVersante.getCapVersante());
		rsModel.setLocalita(soggettoVersante.getLocalitaVersante());
		rsModel.setProvincia(soggettoVersante.getProvinciaVersante());
		rsModel.setNazione(soggettoVersante.getNazioneVersante());
		rsModel.setEmail(soggettoVersante.getEMailVersante());
		//		rsModel.setCellulare(soggettoVersante.getCellulare());

		return rsModel;
	}
}
