/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.bd.viste.model.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.model.Anagrafica;
import it.govpay.model.Anagrafica.TIPO;
import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Canale.TipoVersamento;
import it.govpay.model.Rpt.EsitoPagamento;
import it.govpay.model.Rpt.StatoRpt;
import it.govpay.model.Rpt.TipoIdentificativoAttestante;
import it.govpay.model.Rpt.VersioneRPT;
import it.govpay.model.Versamento.StatoPagamento;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.model.Versamento.TipoSogliaVersamento;
import it.govpay.model.Versamento.TipologiaTipoVersamento;
import it.govpay.model.exception.CodificaInesistenteException;

public class RptConverter {
	
	private RptConverter() {}

	public static Rpt toDTO(it.govpay.orm.VistaRptVersamento vo) throws CodificaInesistenteException, UnsupportedEncodingException {
		Rpt dto = new Rpt();
		dto.setId(vo.getId());
		
		dto.setCallbackURL(vo.getCallbackURL());
		dto.setCcp(vo.getCcp());
		dto.setCodCarrello(vo.getCodCarrello());
		dto.setCodDominio(vo.getCodDominio());
		dto.setCodMsgRichiesta(vo.getCodMsgRichiesta());
		dto.setCodMsgRicevuta(vo.getCodMsgRicevuta());
		dto.setCodSessione(vo.getCodSessione());
		dto.setCodSessionePortale(vo.getCodSessionePortale());
		dto.setCodStazione(vo.getCodStazione());
		dto.setDataAggiornamento(vo.getDataAggiornamentoStato());
		dto.setDataMsgRichiesta(vo.getDataMsgRichiesta());
		dto.setDataMsgRicevuta(vo.getDataMsgRicevuta());
		dto.setDescrizioneStato(vo.getDescrizioneStato());
		if(vo.getCodEsitoPagamento() != null)
			dto.setEsitoPagamento(EsitoPagamento.toEnum(vo.getCodEsitoPagamento()));
		dto.setId(vo.getId());
		dto.setIdTransazioneRpt(vo.getCodTransazioneRPT());
		dto.setIdTransazioneRt(vo.getCodTransazioneRT());
		dto.setImportoTotalePagato(vo.getImportoTotalePagato());
		dto.setIuv(vo.getIuv());
		dto.setModelloPagamento(ModelloPagamento.toEnum(Integer.parseInt(vo.getModelloPagamento())));
		dto.setPspRedirectURL(vo.getPspRedirectURL());
		dto.setStato(StatoRpt.toEnum(vo.getStato()));
		dto.setXmlRpt(vo.getXmlRPT());
		dto.setXmlRt(vo.getXmlRT());
		
		dto.setDataConservazione(vo.getDataConservazione());
		
		if(vo.getStatoConservazione() != null)
			dto.setStatoConservazione(it.govpay.model.Rpt.StatoConservazione.valueOf(vo.getStatoConservazione()));
		
		dto.setDescrizioneStatoConservazione(vo.getDescrizioneStatoCons());
		
		if(vo.getIdPagamentoPortale() != null)
			dto.setIdPagamentoPortale(vo.getIdPagamentoPortale().getId());

		dto.setCodCanale(vo.getCodCanale());
		dto.setCodIntermediarioPsp(vo.getCodIntermediarioPsp());
		dto.setCodPsp(vo.getCodPsp());
		if(vo.getTipoVersamento() != null)
			dto.setTipoVersamento(TipoVersamento.toEnum(vo.getTipoVersamento()));
		if(vo.getTipoIdentificativoAttestante() != null)
			dto.setTipoIdentificativoAttestante(TipoIdentificativoAttestante.valueOf(vo.getTipoIdentificativoAttestante()));
		dto.setIdentificativoAttestante(vo.getIdentificativoAttestante());
		dto.setDenominazioneAttestante(vo.getDenominazioneAttestante());
		dto.setBloccante(vo.isBloccante()); 
		dto.setVersione(VersioneRPT.toEnum(vo.getVersione()));
		
		Versamento versamento = new Versamento();

		versamento.setId(vo.getId());
		versamento.setIdApplicazione(vo.getVrsIdApplicazione().getId());

		if(vo.getVrsIdUo() != null)
			versamento.setIdUo(vo.getVrsIdUo().getId());

		if(vo.getVrsIdDominio() != null)
			versamento.setIdDominio(vo.getVrsIdDominio().getId());

		if(vo.getVrsIdTipoVersamento() != null)
			versamento.setIdTipoVersamento(vo.getVrsIdTipoVersamento().getId());

		if(vo.getVrsIdTipoVersamentoDominio() != null)
			versamento.setIdTipoVersamentoDominio(vo.getVrsIdTipoVersamentoDominio().getId());
		versamento.setNome(vo.getVrsNome());
		versamento.setCodVersamentoEnte(vo.getVrsCodVersamentoEnte());
		versamento.setStatoVersamento(StatoVersamento.valueOf(vo.getVrsStatoVersamento()));
		versamento.setDescrizioneStato(vo.getVrsDescrizioneStato());
		versamento.setImportoTotale(BigDecimal.valueOf(vo.getVrsImportoTotale()));
		versamento.setAggiornabile(vo.isVrsAggiornabile());
		versamento.setDataCreazione(vo.getVrsDataCreazione());
		versamento.setDataValidita(vo.getVrsDataValidita());
		versamento.setDataScadenza(vo.getVrsDataScadenza());
		versamento.setDataUltimoAggiornamento(vo.getVrsDataOraUltimoAgg());
			versamento.setCausaleVersamento(vo.getVrsCausaleVersamento());
		Anagrafica debitore = new Anagrafica();
		if(vo.getVrsDebitoreTipo()!=null)
			debitore.setTipo(TIPO.valueOf(vo.getVrsDebitoreTipo()));
		debitore.setRagioneSociale(vo.getVrsDebitoreAnagrafica());
		debitore.setCap(vo.getVrsDebitoreCap());
		debitore.setCellulare(vo.getVrsDebitoreCellulare());
		debitore.setCivico(vo.getVrsDebitoreCivico());
		debitore.setCodUnivoco(vo.getVrsDebitoreIdentificativo());
		debitore.setEmail(vo.getVrsDebitoreEmail());
		debitore.setFax(vo.getVrsDebitoreFax());
		debitore.setIndirizzo(vo.getVrsDebitoreIndirizzo());
		debitore.setLocalita(vo.getVrsDebitoreLocalita());
		debitore.setNazione(vo.getVrsDebitoreNazione());
		debitore.setProvincia(vo.getVrsDebitoreProvincia());
		debitore.setTelefono(vo.getVrsDebitoreTelefono());
		versamento.setAnagraficaDebitore(debitore);

		if(vo.getVrsCodAnnoTributario() != null && !vo.getVrsCodAnnoTributario().isEmpty())
			versamento.setCodAnnoTributario(Integer.parseInt(vo.getVrsCodAnnoTributario()));

		versamento.setCodLotto(vo.getVrsCodLotto());

		versamento.setTassonomiaAvviso(vo.getVrsTassonomiaAvviso()); 
		versamento.setTassonomia(vo.getVrsTassonomia());

		versamento.setCodVersamentoLotto(vo.getVrsCodVersamentoLotto()); 
		versamento.setCodBundlekey(vo.getVrsCodBundlekey()); 
		versamento.setDatiAllegati(vo.getVrsDatiAllegati());
		if(vo.getVrsIncasso() != null) {
			versamento.setIncasso(vo.getVrsIncasso().equals(it.govpay.model.Versamento.INCASSO_TRUE));
		}
		versamento.setAnomalie(vo.getVrsAnomalie());

		versamento.setIuvVersamento(vo.getVrsIuvVersamento());
		versamento.setNumeroAvviso(vo.getVrsNumeroAvviso());

		// se il numero avviso e' impostato lo iuv proposto deve coincidere con quello inserito a partire dall'avviso
		if(versamento.getNumeroAvviso() !=  null) {
			versamento.setIuvProposto(versamento.getIuvVersamento());
		}

		versamento.setAck(vo.isVrsAck());
		versamento.setAnomalo(vo.isVrsAnomalo());

		versamento.setDirezione(vo.getVrsDirezione());
		versamento.setDivisione(vo.getVrsDivisione());
		versamento.setIdSessione(vo.getVrsIdSessione());

		versamento.setDataPagamento(vo.getVrsDataPagamento());
		versamento.setImportoPagato(vo.getVrsImportoPagato()); 
		versamento.setImportoIncassato(vo.getVrsImportoIncassato());
		if(vo.getVrsStatoPagamento() != null)
			versamento.setStatoPagamento(StatoPagamento.valueOf(vo.getVrsStatoPagamento())); 
		versamento.setIuvPagamento(vo.getVrsIuvPagamento());
		
		if(vo.getVrsIdDocumento() != null)
			versamento.setIdDocumento(vo.getVrsIdDocumento().getId());
		if(vo.getVrsCodRata() != null) {
			if(vo.getVrsCodRata().startsWith(TipoSogliaVersamento.ENTRO.toString())) {
				versamento.setTipoSoglia(TipoSogliaVersamento.ENTRO);
				String gg = vo.getVrsCodRata().substring(vo.getVrsCodRata().indexOf(TipoSogliaVersamento.ENTRO.toString())+ TipoSogliaVersamento.ENTRO.toString().length());
				versamento.setGiorniSoglia(Integer.parseInt(gg));
			} else if(vo.getVrsCodRata().startsWith(TipoSogliaVersamento.OLTRE.toString())) {
				versamento.setTipoSoglia(TipoSogliaVersamento.OLTRE);
				String gg = vo.getVrsCodRata().substring(vo.getVrsCodRata().indexOf(TipoSogliaVersamento.OLTRE.toString())+ TipoSogliaVersamento.OLTRE.toString().length());
				versamento.setGiorniSoglia(Integer.parseInt(gg));
			} else if(vo.getVrsCodRata().startsWith(TipoSogliaVersamento.RIDOTTO.toString())) {
				versamento.setTipoSoglia(TipoSogliaVersamento.RIDOTTO);
			} else if(vo.getVrsCodRata().startsWith(TipoSogliaVersamento.SCONTATO.toString())) {
				versamento.setTipoSoglia(TipoSogliaVersamento.SCONTATO);
			} else {
				versamento.setNumeroRata(Integer.parseInt(vo.getVrsCodRata()));
			}
		}
		
		if(vo.getVrsTipo() != null)
			versamento.setTipo(TipologiaTipoVersamento.toEnum(vo.getVrsTipo()));
		
		versamento.setProprieta(vo.getVrsProprieta());

		dto.setVersamento(versamento );

		return dto;
	}
		
	
	public static List<Rpt> toDTO(
			List<it.govpay.orm.VistaRptVersamento> rptVOLst) throws UnsupportedEncodingException, CodificaInesistenteException {
		List<Rpt> dto = new ArrayList<>();
		for(it.govpay.orm.VistaRptVersamento vo : rptVOLst) {
			dto.add(toDTO(vo));
		}
		return dto;
	}
}
