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
package it.govpay.bd.model.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.model.TipoVersamento;
import it.govpay.model.TipoVersamento.Tipo;

public class TipoVersamentoConverter {

	public static List<TipoVersamento> toDTOList(List<it.govpay.orm.TipoVersamento> lstVO) throws ServiceException {
		List<TipoVersamento> lst = new ArrayList<>();
		if(lstVO != null && !lstVO.isEmpty()) {
			for(it.govpay.orm.TipoVersamento vo: lstVO) {
				lst.add(toDTO(vo));
			}
		}
		return lst;
	}

	public static TipoVersamento toDTO(it.govpay.orm.TipoVersamento vo) throws ServiceException {
		TipoVersamento dto = new TipoVersamento();
		dto.setId(vo.getId());
		dto.setCodTipoVersamento(vo.getCodTipoVersamento());
		dto.setDescrizione(vo.getDescrizione());
		dto.setCodificaIuvDefault(vo.getCodificaIuv());
		if(vo.getTipo() != null)
			dto.setTipoDefault(Tipo.toEnum(vo.getTipo()));
		dto.setPagaTerziDefault(vo.isPagaTerzi());
		dto.setAbilitatoDefault(vo.getAbilitato());
		dto.setCaricamentoPendenzePortaleBackofficeAbilitatoDefault(vo.isBoAbilitato());
		dto.setCaricamentoPendenzePortaleBackofficeCodApplicazioneDefault(vo.getBoCodApplicazione());
		dto.setCaricamentoPendenzePortaleBackofficeFormDefinizioneDefault(vo.getBoFormDefinizione());
		dto.setCaricamentoPendenzePortaleBackofficeFormTipoDefault(vo.getBoFormTipo());
		dto.setCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault(vo.getBoTrasformazioneDef());
		dto.setCaricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault(vo.getBoTrasformazioneTipo());
		dto.setCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault(vo.getBoValidazioneDef());
		dto.setCaricamentoPendenzePortalePagamentoAbilitatoDefault(vo.isPagAbilitato());
		dto.setCaricamentoPendenzePortalePagamentoCodApplicazioneDefault(vo.getPagCodApplicazione());
		dto.setCaricamentoPendenzePortalePagamentoFormDefinizioneDefault(vo.getPagFormDefinizione());
		dto.setCaricamentoPendenzePortalePagamentoFormTipoDefault(vo.getPagFormTipo());
		dto.setCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneDefault(vo.getPagTrasformazioneDef());
		dto.setCaricamentoPendenzePortalePagamentoTrasformazioneTipoDefault(vo.getPagTrasformazioneTipo());
		dto.setCaricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault(vo.getPagValidazioneDef()); 
		dto.setAvvisaturaMailPromemoriaAvvisoAbilitatoDefault(vo.isAvvMailPromAvvAbilitato());
		dto.setAvvisaturaMailPromemoriaAvvisoMessaggioDefault(vo.getAvvMailPromAvvMessaggio());
		dto.setAvvisaturaMailPromemoriaAvvisoOggettoDefault(vo.getAvvMailPromAvvOggetto());
		dto.setAvvisaturaMailPromemoriaAvvisoPdfDefault(vo.getAvvMailPromAvvPdf());
		dto.setAvvisaturaMailPromemoriaAvvisoTipoDefault(vo.getAvvMailPromAvvTipo());
		dto.setAvvisaturaMailPromemoriaRicevutaAbilitatoDefault(vo.isAvvMailPromRicAbilitato());
		dto.setAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiDefault(vo.getAvvMailPromRicEseguiti());
		dto.setAvvisaturaMailPromemoriaRicevutaMessaggioDefault(vo.getAvvMailPromRicOggetto());
		dto.setAvvisaturaMailPromemoriaRicevutaOggettoDefault(vo.getAvvMailPromRicOggetto());
		dto.setAvvisaturaMailPromemoriaRicevutaPdfDefault(vo.getAvvMailPromRicPdf());
		dto.setAvvisaturaMailPromemoriaRicevutaTipoDefault(vo.getAvvMailPromRicTipo());
		dto.setAvvisaturaMailPromemoriaScadenzaAbilitatoDefault(vo.isAvvMailPromScadAbilitato());
		dto.setAvvisaturaMailPromemoriaScadenzaMessaggioDefault(vo.getAvvMailPromScadMessaggio());
		dto.setAvvisaturaMailPromemoriaScadenzaOggettoDefault(vo.getAvvMailPromScadOggetto());
		if(vo.getAvvMailPromScadPreavviso() != null)
			dto.setAvvisaturaMailPromemoriaScadenzaPreavvisoDefault(new BigDecimal(vo.getAvvMailPromScadPreavviso()));
		dto.setAvvisaturaMailPromemoriaScadenzaTipoDefault(vo.getAvvMailPromScadTipo());
		dto.setVisualizzazioneDefinizioneDefault(vo.getVisualizzazioneDefinizione());
		dto.setTracciatoCsvTipoDefault(vo.getTracCsvTipo());
		dto.setTracciatoCsvIntestazioneDefault(vo.getTracCsvHeaderRisposta());
		dto.setTracciatoCsvRichiestaDefault(vo.getTracCsvTemplateRichiesta());
		dto.setTracciatoCsvRispostaDefault(vo.getTracCsvTemplateRisposta());
		dto.setAvvisaturaAppIoPromemoriaAvvisoAbilitatoDefault(vo.isAvvAppIoPromAvvAbilitato());
		dto.setAvvisaturaAppIoPromemoriaAvvisoMessaggioDefault(vo.getAvvAppIoPromAvvMessaggio());
		dto.setAvvisaturaAppIoPromemoriaAvvisoOggettoDefault(vo.getAvvAppIoPromAvvOggetto());
		dto.setAvvisaturaAppIoPromemoriaAvvisoTipoDefault(vo.getAvvAppIoPromAvvTipo());
		dto.setAvvisaturaAppIoPromemoriaRicevutaAbilitatoDefault(vo.isAvvAppIoPromRicAbilitato());
		dto.setAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiDefault(vo.getAvvAppIoPromRicEseguiti());
		dto.setAvvisaturaAppIoPromemoriaRicevutaMessaggioDefault(vo.getAvvAppIoPromRicOggetto());
		dto.setAvvisaturaAppIoPromemoriaRicevutaOggettoDefault(vo.getAvvAppIoPromRicOggetto());
		dto.setAvvisaturaAppIoPromemoriaRicevutaTipoDefault(vo.getAvvAppIoPromRicTipo());
		dto.setAvvisaturaAppIoPromemoriaScadenzaAbilitatoDefault(vo.isAvvAppIoPromScadAbilitato());
		dto.setAvvisaturaAppIoPromemoriaScadenzaMessaggioDefault(vo.getAvvAppIoPromScadMessaggio());
		dto.setAvvisaturaAppIoPromemoriaScadenzaOggettoDefault(vo.getAvvAppIoPromScadOggetto());
		if(vo.getAvvAppIoPromScadPreavviso() != null)
			dto.setAvvisaturaAppIoPromemoriaScadenzaPreavvisoDefault(new BigDecimal(vo.getAvvAppIoPromScadPreavviso()));
		dto.setAvvisaturaAppIoPromemoriaScadenzaTipoDefault(vo.getAvvAppIoPromScadTipo());
		
		return dto;
	}

	public static it.govpay.orm.TipoVersamento toVO(TipoVersamento dto) {
		it.govpay.orm.TipoVersamento vo = new it.govpay.orm.TipoVersamento();
		vo.setId(dto.getId());
		vo.setCodTipoVersamento(dto.getCodTipoVersamento());
		vo.setDescrizione(dto.getDescrizione());
		vo.setCodificaIuv(dto.getCodificaIuvDefault());
		if(dto.getTipoDefault() != null)
			vo.setTipo(dto.getTipoDefault().getCodifica());
		vo.setPagaTerzi(dto.getPagaTerziDefault());
		vo.setAbilitato(dto.isAbilitatoDefault());
		vo.setBoAbilitato(dto.isCaricamentoPendenzePortaleBackofficeAbilitatoDefault());
		vo.setBoCodApplicazione(dto.getCaricamentoPendenzePortaleBackofficeCodApplicazioneDefault());
		vo.setBoFormDefinizione(dto.getCaricamentoPendenzePortaleBackofficeFormDefinizioneDefault());
		vo.setBoFormTipo(dto.getCaricamentoPendenzePortaleBackofficeFormTipoDefault());
		vo.setBoTrasformazioneDef(dto.getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault());
		vo.setBoTrasformazioneTipo(dto.getCaricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault());
		vo.setBoValidazioneDef(dto.getCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault());
		vo.setPagAbilitato(dto.isCaricamentoPendenzePortalePagamentoAbilitatoDefault());
		vo.setPagCodApplicazione(dto.getCaricamentoPendenzePortalePagamentoCodApplicazioneDefault());
		vo.setPagFormDefinizione(dto.getCaricamentoPendenzePortalePagamentoFormDefinizioneDefault());
		vo.setPagFormTipo(dto.getCaricamentoPendenzePortalePagamentoFormTipoDefault());
		vo.setPagTrasformazioneDef(dto.getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneDefault());
		vo.setPagTrasformazioneTipo(dto.getCaricamentoPendenzePortalePagamentoTrasformazioneTipoDefault());
		vo.setPagValidazioneDef(dto.getCaricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault()); 
		vo.setAvvMailPromAvvAbilitato(dto.isAvvisaturaMailPromemoriaAvvisoAbilitatoDefault());
		vo.setAvvMailPromAvvMessaggio(dto.getAvvisaturaMailPromemoriaAvvisoMessaggioDefault());
		vo.setAvvMailPromAvvOggetto(dto.getAvvisaturaMailPromemoriaAvvisoOggettoDefault());
		vo.setAvvMailPromAvvPdf(dto.getAvvisaturaMailPromemoriaAvvisoPdfDefault());
		vo.setAvvMailPromAvvTipo(dto.getAvvisaturaMailPromemoriaAvvisoTipoDefault());
		vo.setAvvMailPromRicAbilitato(dto.isAvvisaturaMailPromemoriaRicevutaAbilitatoDefault());
		vo.setAvvMailPromRicEseguiti(dto.getAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiDefault());
		vo.setAvvMailPromRicOggetto(dto.getAvvisaturaMailPromemoriaRicevutaMessaggioDefault());
		vo.setAvvMailPromRicOggetto(dto.getAvvisaturaMailPromemoriaRicevutaOggettoDefault());
		vo.setAvvMailPromRicPdf(dto.getAvvisaturaMailPromemoriaRicevutaPdfDefault());
		vo.setAvvMailPromRicTipo(dto.getAvvisaturaMailPromemoriaRicevutaTipoDefault());
		vo.setAvvMailPromScadAbilitato(dto.isAvvisaturaMailPromemoriaScadenzaAbilitatoDefault());
		vo.setAvvMailPromScadMessaggio(dto.getAvvisaturaMailPromemoriaScadenzaMessaggioDefault());
		vo.setAvvMailPromScadOggetto(dto.getAvvisaturaMailPromemoriaScadenzaOggettoDefault());
		if(dto.getAvvisaturaMailPromemoriaScadenzaPreavvisoDefault() != null)
			vo.setAvvMailPromScadPreavviso(dto.getAvvisaturaMailPromemoriaScadenzaPreavvisoDefault().intValue());
		vo.setAvvMailPromScadTipo(dto.getAvvisaturaMailPromemoriaScadenzaTipoDefault());
		vo.setVisualizzazioneDefinizione(dto.getVisualizzazioneDefinizioneDefault());
		vo.setTracCsvTipo(dto.getTracciatoCsvTipoDefault());
		vo.setTracCsvHeaderRisposta(dto.getTracciatoCsvIntestazioneDefault());
		vo.setTracCsvTemplateRichiesta(dto.getTracciatoCsvRichiestaDefault());
		vo.setTracCsvTemplateRisposta(dto.getTracciatoCsvRispostaDefault());
		vo.setAvvAppIoPromAvvAbilitato(dto.isAvvisaturaAppIoPromemoriaAvvisoAbilitatoDefault());
		vo.setAvvAppIoPromAvvMessaggio(dto.getAvvisaturaAppIoPromemoriaAvvisoMessaggioDefault());
		vo.setAvvAppIoPromAvvOggetto(dto.getAvvisaturaAppIoPromemoriaAvvisoOggettoDefault());
		vo.setAvvAppIoPromAvvTipo(dto.getAvvisaturaAppIoPromemoriaAvvisoTipoDefault());
		vo.setAvvAppIoPromRicAbilitato(dto.isAvvisaturaAppIoPromemoriaRicevutaAbilitatoDefault());
		vo.setAvvAppIoPromRicEseguiti(dto.getAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiDefault());
		vo.setAvvAppIoPromRicOggetto(dto.getAvvisaturaAppIoPromemoriaRicevutaMessaggioDefault());
		vo.setAvvAppIoPromRicOggetto(dto.getAvvisaturaAppIoPromemoriaRicevutaOggettoDefault());
		vo.setAvvAppIoPromRicTipo(dto.getAvvisaturaAppIoPromemoriaRicevutaTipoDefault());
		vo.setAvvAppIoPromScadAbilitato(dto.isAvvisaturaAppIoPromemoriaScadenzaAbilitatoDefault());
		vo.setAvvAppIoPromScadMessaggio(dto.getAvvisaturaAppIoPromemoriaScadenzaMessaggioDefault());
		vo.setAvvAppIoPromScadOggetto(dto.getAvvisaturaAppIoPromemoriaScadenzaOggettoDefault());
		if(dto.getAvvisaturaAppIoPromemoriaScadenzaPreavvisoDefault() != null)
			vo.setAvvAppIoPromScadPreavviso(dto.getAvvisaturaAppIoPromemoriaScadenzaPreavvisoDefault().intValue());
		vo.setAvvAppIoPromScadTipo(dto.getAvvisaturaAppIoPromemoriaScadenzaTipoDefault());
		
		return vo;
	}


}
