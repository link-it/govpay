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

import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.model.TipoVersamento.Tipo;
import it.govpay.orm.IdDominio;
import it.govpay.orm.TipoVersamento;

public class TipoVersamentoDominioConverter {

	public static List<TipoVersamentoDominio> toDTOList(List<it.govpay.orm.TipoVersamentoDominio> lstVO) throws ServiceException {
		List<TipoVersamentoDominio> lst = new ArrayList<>();
		if(lstVO != null && !lstVO.isEmpty()) {
			for(it.govpay.orm.TipoVersamentoDominio vo: lstVO) {
				lst.add(toDTO(vo));
			}
		}
		return lst;
	}

	public static TipoVersamentoDominio toDTO(it.govpay.orm.TipoVersamentoDominio vo) throws ServiceException {
		TipoVersamentoDominio dto = new TipoVersamentoDominio();
		dto.setId(vo.getId());
		dto.setIdDominio(vo.getIdDominio().getId());
		
		dto.setAbilitatoCustom(vo.getAbilitato());
		dto.setCodificaIuvCustom(vo.getCodificaIuv());
		if(vo.getTipo() != null)
			dto.setTipoCustom(Tipo.toEnum(vo.getTipo()));
		dto.setPagaTerziCustom(vo.getPagaTerzi());
		dto.setCaricamentoPendenzePortaleBackofficeAbilitatoCustom(vo.getBoAbilitato());
		dto.setCaricamentoPendenzePortaleBackofficeCodApplicazioneCustom(vo.getBoCodApplicazione());
		dto.setCaricamentoPendenzePortaleBackofficeFormDefinizioneCustom(vo.getBoFormDefinizione());
		dto.setCaricamentoPendenzePortaleBackofficeFormTipoCustom(vo.getBoFormTipo());
		dto.setCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneCustom(vo.getBoTrasformazioneDef());
		dto.setCaricamentoPendenzePortaleBackofficeTrasformazioneTipoCustom(vo.getBoTrasformazioneTipo());
		dto.setCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneCustom(vo.getBoValidazioneDef());
		dto.setCaricamentoPendenzePortalePagamentoAbilitatoCustom(vo.getPagAbilitato());
		dto.setCaricamentoPendenzePortalePagamentoCodApplicazioneCustom(vo.getPagCodApplicazione());
		dto.setCaricamentoPendenzePortalePagamentoFormDefinizioneCustom(vo.getPagFormDefinizione());
		dto.setCaricamentoPendenzePortalePagamentoFormImpaginazioneCustom(vo.getPagFormImpaginazione());
		dto.setCaricamentoPendenzePortalePagamentoFormTipoCustom(vo.getPagFormTipo());
		dto.setCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneCustom(vo.getPagTrasformazioneDef());
		dto.setCaricamentoPendenzePortalePagamentoTrasformazioneTipoCustom(vo.getPagTrasformazioneTipo());
		dto.setCaricamentoPendenzePortalePagamentoValidazioneDefinizioneCustom(vo.getPagValidazioneDef()); 
		dto.setAvvisaturaMailPromemoriaAvvisoAbilitatoCustom(vo.getAvvMailPromAvvAbilitato());
		dto.setAvvisaturaMailPromemoriaAvvisoMessaggioCustom(vo.getAvvMailPromAvvMessaggio());
		dto.setAvvisaturaMailPromemoriaAvvisoOggettoCustom(vo.getAvvMailPromAvvOggetto());
		dto.setAvvisaturaMailPromemoriaAvvisoPdfCustom(vo.getAvvMailPromAvvPdf());
		dto.setAvvisaturaMailPromemoriaAvvisoTipoCustom(vo.getAvvMailPromAvvTipo());
		dto.setAvvisaturaMailPromemoriaRicevutaAbilitatoCustom(vo.getAvvMailPromRicAbilitato());
		dto.setAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiCustom(vo.getAvvMailPromRicEseguiti());
		dto.setAvvisaturaMailPromemoriaRicevutaMessaggioCustom(vo.getAvvMailPromRicMessaggio());
		dto.setAvvisaturaMailPromemoriaRicevutaOggettoCustom(vo.getAvvMailPromRicOggetto());
		dto.setAvvisaturaMailPromemoriaRicevutaPdfCustom(vo.getAvvMailPromRicPdf());
		dto.setAvvisaturaMailPromemoriaRicevutaTipoCustom(vo.getAvvMailPromRicTipo());
		dto.setAvvisaturaMailPromemoriaScadenzaAbilitatoCustom(vo.getAvvMailPromScadAbilitato());
		dto.setAvvisaturaMailPromemoriaScadenzaMessaggioCustom(vo.getAvvMailPromScadMessaggio());
		dto.setAvvisaturaMailPromemoriaScadenzaOggettoCustom(vo.getAvvMailPromScadOggetto());
		if(vo.getAvvMailPromScadPreavviso() != null)
			dto.setAvvisaturaMailPromemoriaScadenzaPreavvisoCustom(new BigDecimal(vo.getAvvMailPromScadPreavviso()));
		dto.setAvvisaturaMailPromemoriaScadenzaTipoCustom(vo.getAvvMailPromScadTipo());
		dto.setVisualizzazioneDefinizioneCustom(vo.getVisualizzazioneDefinizione());
		dto.setTracciatoCsvTipoCustom(vo.getTracCsvTipo());
		dto.setTracciatoCsvIntestazioneCustom(vo.getTracCsvHeaderRisposta());
		dto.setTracciatoCsvRichiestaCustom(vo.getTracCsvTemplateRichiesta());
		dto.setTracciatoCsvRispostaCustom(vo.getTracCsvTemplateRisposta());
		dto.setAppIOAPIKey(vo.getAppIoApiKey());
		dto.setAvvisaturaAppIoPromemoriaAvvisoAbilitatoCustom(vo.getAvvAppIoPromAvvAbilitato());
		dto.setAvvisaturaAppIoPromemoriaAvvisoMessaggioCustom(vo.getAvvAppIoPromAvvMessaggio());
		dto.setAvvisaturaAppIoPromemoriaAvvisoOggettoCustom(vo.getAvvAppIoPromAvvOggetto());
		dto.setAvvisaturaAppIoPromemoriaAvvisoTipoCustom(vo.getAvvAppIoPromAvvTipo());
		dto.setAvvisaturaAppIoPromemoriaRicevutaAbilitatoCustom(vo.getAvvAppIoPromRicAbilitato());
		dto.setAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiCustom(vo.getAvvAppIoPromRicEseguiti());
		dto.setAvvisaturaAppIoPromemoriaRicevutaMessaggioCustom(vo.getAvvAppIoPromRicMessaggio());
		dto.setAvvisaturaAppIoPromemoriaRicevutaOggettoCustom(vo.getAvvAppIoPromRicOggetto());
		dto.setAvvisaturaAppIoPromemoriaRicevutaTipoCustom(vo.getAvvAppIoPromRicTipo());
		dto.setAvvisaturaAppIoPromemoriaScadenzaAbilitatoCustom(vo.getAvvAppIoPromScadAbilitato());
		dto.setAvvisaturaAppIoPromemoriaScadenzaMessaggioCustom(vo.getAvvAppIoPromScadMessaggio());
		dto.setAvvisaturaAppIoPromemoriaScadenzaOggettoCustom(vo.getAvvAppIoPromScadOggetto());
		if(vo.getAvvAppIoPromScadPreavviso() != null)
		dto.setAvvisaturaAppIoPromemoriaScadenzaPreavvisoCustom(new BigDecimal(vo.getAvvAppIoPromScadPreavviso()));
		dto.setAvvisaturaAppIoPromemoriaScadenzaTipoCustom(vo.getAvvAppIoPromScadTipo());
		
		dto.setIdTipoVersamento(vo.getTipoVersamento().getId());
		dto.setCodTipoVersamento(vo.getTipoVersamento().getCodTipoVersamento());
		dto.setDescrizione(vo.getTipoVersamento().getDescrizione());
		dto.setCodificaIuvDefault(vo.getTipoVersamento().getCodificaIuv());
		if(vo.getTipoVersamento().getTipo() != null)
			dto.setTipoDefault(Tipo.toEnum(vo.getTipoVersamento().getTipo()));
		dto.setPagaTerziDefault(vo.getTipoVersamento().isPagaTerzi());
		dto.setAbilitatoDefault(vo.getTipoVersamento().isAbilitato());
		dto.setCaricamentoPendenzePortaleBackofficeAbilitatoDefault(vo.getTipoVersamento().isBoAbilitato());
		dto.setCaricamentoPendenzePortaleBackofficeCodApplicazioneDefault(vo.getTipoVersamento().getBoCodApplicazione());
		dto.setCaricamentoPendenzePortaleBackofficeFormDefinizioneDefault(vo.getTipoVersamento().getBoFormDefinizione());
		dto.setCaricamentoPendenzePortaleBackofficeFormTipoDefault(vo.getTipoVersamento().getBoFormTipo());
		dto.setCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault(vo.getTipoVersamento().getBoTrasformazioneDef());
		dto.setCaricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault(vo.getTipoVersamento().getBoTrasformazioneTipo());
		dto.setCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault(vo.getTipoVersamento().getBoValidazioneDef());
		dto.setCaricamentoPendenzePortalePagamentoAbilitatoDefault(vo.getTipoVersamento().isPagAbilitato());
		dto.setCaricamentoPendenzePortalePagamentoCodApplicazioneDefault(vo.getTipoVersamento().getPagCodApplicazione());
		dto.setCaricamentoPendenzePortalePagamentoFormDefinizioneDefault(vo.getTipoVersamento().getPagFormDefinizione());
		dto.setCaricamentoPendenzePortalePagamentoFormImpaginazioneDefault(vo.getTipoVersamento().getPagFormImpaginazione());
		dto.setCaricamentoPendenzePortalePagamentoFormTipoDefault(vo.getTipoVersamento().getPagFormTipo());
		dto.setCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneDefault(vo.getTipoVersamento().getPagTrasformazioneDef());
		dto.setCaricamentoPendenzePortalePagamentoTrasformazioneTipoDefault(vo.getTipoVersamento().getPagTrasformazioneTipo());
		dto.setCaricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault(vo.getTipoVersamento().getPagValidazioneDef()); 
		dto.setAvvisaturaMailPromemoriaAvvisoAbilitatoDefault(vo.getTipoVersamento().isAvvMailPromAvvAbilitato());
		dto.setAvvisaturaMailPromemoriaAvvisoMessaggioDefault(vo.getTipoVersamento().getAvvMailPromAvvMessaggio());
		dto.setAvvisaturaMailPromemoriaAvvisoOggettoDefault(vo.getTipoVersamento().getAvvMailPromAvvOggetto());
		dto.setAvvisaturaMailPromemoriaAvvisoPdfDefault(vo.getTipoVersamento().getAvvMailPromAvvPdf());
		dto.setAvvisaturaMailPromemoriaAvvisoTipoDefault(vo.getTipoVersamento().getAvvMailPromAvvTipo());
		dto.setAvvisaturaMailPromemoriaRicevutaAbilitatoDefault(vo.getTipoVersamento().isAvvMailPromRicAbilitato());
		dto.setAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiDefault(vo.getTipoVersamento().getAvvMailPromRicEseguiti());
		dto.setAvvisaturaMailPromemoriaRicevutaMessaggioDefault(vo.getTipoVersamento().getAvvMailPromRicMessaggio());
		dto.setAvvisaturaMailPromemoriaRicevutaOggettoDefault(vo.getTipoVersamento().getAvvMailPromRicOggetto());
		dto.setAvvisaturaMailPromemoriaRicevutaPdfDefault(vo.getTipoVersamento().getAvvMailPromRicPdf());
		dto.setAvvisaturaMailPromemoriaRicevutaTipoDefault(vo.getTipoVersamento().getAvvMailPromRicTipo());
		dto.setAvvisaturaMailPromemoriaScadenzaAbilitatoDefault(vo.getTipoVersamento().isAvvMailPromScadAbilitato());
		dto.setAvvisaturaMailPromemoriaScadenzaMessaggioDefault(vo.getTipoVersamento().getAvvMailPromScadMessaggio());
		dto.setAvvisaturaMailPromemoriaScadenzaOggettoDefault(vo.getTipoVersamento().getAvvMailPromScadOggetto());
		if(vo.getTipoVersamento().getAvvMailPromScadPreavviso() != null)
			dto.setAvvisaturaMailPromemoriaScadenzaPreavvisoDefault(new BigDecimal(vo.getTipoVersamento().getAvvMailPromScadPreavviso()));
		dto.setAvvisaturaMailPromemoriaScadenzaTipoDefault(vo.getTipoVersamento().getAvvMailPromScadTipo());
		dto.setVisualizzazioneDefinizioneDefault(vo.getTipoVersamento().getVisualizzazioneDefinizione());
		dto.setTracciatoCsvTipoDefault(vo.getTipoVersamento().getTracCsvTipo());
		dto.setTracciatoCsvIntestazioneDefault(vo.getTipoVersamento().getTracCsvHeaderRisposta());
		dto.setTracciatoCsvRichiestaDefault(vo.getTipoVersamento().getTracCsvTemplateRichiesta());
		dto.setTracciatoCsvRispostaDefault(vo.getTipoVersamento().getTracCsvTemplateRisposta());
		dto.setAvvisaturaAppIoPromemoriaAvvisoAbilitatoDefault(vo.getTipoVersamento().isAvvAppIoPromAvvAbilitato());
		dto.setAvvisaturaAppIoPromemoriaAvvisoMessaggioDefault(vo.getTipoVersamento().getAvvAppIoPromAvvMessaggio());
		dto.setAvvisaturaAppIoPromemoriaAvvisoOggettoDefault(vo.getTipoVersamento().getAvvAppIoPromAvvOggetto());
		dto.setAvvisaturaAppIoPromemoriaAvvisoTipoDefault(vo.getTipoVersamento().getAvvAppIoPromAvvTipo());
		dto.setAvvisaturaAppIoPromemoriaRicevutaAbilitatoDefault(vo.getTipoVersamento().isAvvAppIoPromRicAbilitato());
		dto.setAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiDefault(vo.getTipoVersamento().getAvvAppIoPromRicEseguiti());
		dto.setAvvisaturaAppIoPromemoriaRicevutaMessaggioDefault(vo.getTipoVersamento().getAvvAppIoPromRicMessaggio());
		dto.setAvvisaturaAppIoPromemoriaRicevutaOggettoDefault(vo.getTipoVersamento().getAvvAppIoPromRicOggetto());
		dto.setAvvisaturaAppIoPromemoriaRicevutaTipoDefault(vo.getTipoVersamento().getAvvAppIoPromRicTipo());
		dto.setAvvisaturaAppIoPromemoriaScadenzaAbilitatoDefault(vo.getTipoVersamento().isAvvAppIoPromScadAbilitato());
		dto.setAvvisaturaAppIoPromemoriaScadenzaMessaggioDefault(vo.getTipoVersamento().getAvvAppIoPromScadMessaggio());
		dto.setAvvisaturaAppIoPromemoriaScadenzaOggettoDefault(vo.getTipoVersamento().getAvvAppIoPromScadOggetto());
		if(vo.getTipoVersamento().getAvvAppIoPromScadPreavviso() != null)
		dto.setAvvisaturaAppIoPromemoriaScadenzaPreavvisoDefault(new BigDecimal(vo.getTipoVersamento().getAvvAppIoPromScadPreavviso()));
		dto.setAvvisaturaAppIoPromemoriaScadenzaTipoDefault(vo.getTipoVersamento().getAvvAppIoPromScadTipo());
		
		return dto;
	}

	public static it.govpay.orm.TipoVersamentoDominio toVO(TipoVersamentoDominio dto) {
		it.govpay.orm.TipoVersamentoDominio vo = new it.govpay.orm.TipoVersamentoDominio();
		vo.setId(dto.getId());
		
		TipoVersamento tipoVersamento = new TipoVersamento();
		tipoVersamento.setId(dto.getIdTipoVersamento());
		tipoVersamento.setCodTipoVersamento(dto.getCodTipoVersamento());
		tipoVersamento.setDescrizione(dto.getDescrizione());
		tipoVersamento.setCodificaIuv(dto.getCodificaIuvDefault());
		if(dto.getTipoDefault() != null)
			tipoVersamento.setTipo(dto.getTipoDefault().getCodifica());
		tipoVersamento.setPagaTerzi(dto.getPagaTerziDefault());
		tipoVersamento.setAbilitato(dto.isAbilitatoDefault());
		tipoVersamento.setBoAbilitato(dto.isCaricamentoPendenzePortaleBackofficeAbilitatoDefault());
		tipoVersamento.setBoCodApplicazione(dto.getCaricamentoPendenzePortaleBackofficeCodApplicazioneDefault());
		tipoVersamento.setBoFormDefinizione(dto.getCaricamentoPendenzePortaleBackofficeFormDefinizioneDefault());
		tipoVersamento.setBoFormTipo(dto.getCaricamentoPendenzePortaleBackofficeFormTipoDefault());
		tipoVersamento.setBoTrasformazioneDef(dto.getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneDefault());
		tipoVersamento.setBoTrasformazioneTipo(dto.getCaricamentoPendenzePortaleBackofficeTrasformazioneTipoDefault());
		tipoVersamento.setBoValidazioneDef(dto.getCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneDefault());
		tipoVersamento.setPagAbilitato(dto.isCaricamentoPendenzePortalePagamentoAbilitatoDefault());
		tipoVersamento.setPagCodApplicazione(dto.getCaricamentoPendenzePortalePagamentoCodApplicazioneDefault());
		tipoVersamento.setPagFormDefinizione(dto.getCaricamentoPendenzePortalePagamentoFormDefinizioneDefault());
		tipoVersamento.setPagFormImpaginazione(dto.getCaricamentoPendenzePortalePagamentoFormImpaginazioneDefault());
		tipoVersamento.setPagFormTipo(dto.getCaricamentoPendenzePortalePagamentoFormTipoDefault());
		tipoVersamento.setPagTrasformazioneDef(dto.getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneDefault());
		tipoVersamento.setPagTrasformazioneTipo(dto.getCaricamentoPendenzePortalePagamentoTrasformazioneTipoDefault());
		tipoVersamento.setPagValidazioneDef(dto.getCaricamentoPendenzePortalePagamentoValidazioneDefinizioneDefault()); 
		tipoVersamento.setAvvMailPromAvvAbilitato(dto.isAvvisaturaMailPromemoriaAvvisoAbilitatoDefault());
		tipoVersamento.setAvvMailPromAvvMessaggio(dto.getAvvisaturaMailPromemoriaAvvisoMessaggioDefault());
		tipoVersamento.setAvvMailPromAvvOggetto(dto.getAvvisaturaMailPromemoriaAvvisoOggettoDefault());
		tipoVersamento.setAvvMailPromAvvPdf(dto.getAvvisaturaMailPromemoriaAvvisoPdfDefault());
		tipoVersamento.setAvvMailPromAvvTipo(dto.getAvvisaturaMailPromemoriaAvvisoTipoDefault());
		tipoVersamento.setAvvMailPromRicAbilitato(dto.isAvvisaturaMailPromemoriaRicevutaAbilitatoDefault());
		tipoVersamento.setAvvMailPromRicEseguiti(dto.getAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiDefault());
		tipoVersamento.setAvvMailPromRicMessaggio(dto.getAvvisaturaMailPromemoriaRicevutaMessaggioDefault());
		tipoVersamento.setAvvMailPromRicOggetto(dto.getAvvisaturaMailPromemoriaRicevutaOggettoDefault());
		tipoVersamento.setAvvMailPromRicPdf(dto.getAvvisaturaMailPromemoriaRicevutaPdfDefault());
		tipoVersamento.setAvvMailPromRicTipo(dto.getAvvisaturaMailPromemoriaRicevutaTipoDefault());
		tipoVersamento.setAvvMailPromScadAbilitato(dto.isAvvisaturaMailPromemoriaScadenzaAbilitatoDefault());
		tipoVersamento.setAvvMailPromScadMessaggio(dto.getAvvisaturaMailPromemoriaScadenzaMessaggioDefault());
		tipoVersamento.setAvvMailPromScadOggetto(dto.getAvvisaturaMailPromemoriaScadenzaOggettoDefault());
		if(dto.getAvvisaturaMailPromemoriaScadenzaPreavvisoDefault() !=null)
			tipoVersamento.setAvvMailPromScadPreavviso(dto.getAvvisaturaMailPromemoriaScadenzaPreavvisoDefault().intValue());
		tipoVersamento.setAvvMailPromScadTipo(dto.getAvvisaturaMailPromemoriaScadenzaTipoDefault());
		tipoVersamento.setVisualizzazioneDefinizione(dto.getVisualizzazioneDefinizioneDefault());
		tipoVersamento.setTracCsvTipo(dto.getTracciatoCsvTipoDefault());
		tipoVersamento.setTracCsvHeaderRisposta(dto.getTracciatoCsvIntestazioneDefault());
		tipoVersamento.setTracCsvTemplateRichiesta(dto.getTracciatoCsvRichiestaDefault());
		tipoVersamento.setTracCsvTemplateRisposta(dto.getTracciatoCsvRispostaDefault());
		tipoVersamento.setAvvAppIoPromAvvAbilitato(dto.isAvvisaturaAppIoPromemoriaAvvisoAbilitatoDefault());
		tipoVersamento.setAvvAppIoPromAvvMessaggio(dto.getAvvisaturaAppIoPromemoriaAvvisoMessaggioDefault());
		tipoVersamento.setAvvAppIoPromAvvOggetto(dto.getAvvisaturaAppIoPromemoriaAvvisoOggettoDefault());
		tipoVersamento.setAvvAppIoPromAvvTipo(dto.getAvvisaturaAppIoPromemoriaAvvisoTipoDefault());
		tipoVersamento.setAvvAppIoPromRicAbilitato(dto.isAvvisaturaAppIoPromemoriaRicevutaAbilitatoDefault());
		tipoVersamento.setAvvAppIoPromRicEseguiti(dto.getAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiDefault());
		tipoVersamento.setAvvAppIoPromRicMessaggio(dto.getAvvisaturaAppIoPromemoriaRicevutaMessaggioDefault());
		tipoVersamento.setAvvAppIoPromRicOggetto(dto.getAvvisaturaAppIoPromemoriaRicevutaOggettoDefault());
		tipoVersamento.setAvvAppIoPromRicTipo(dto.getAvvisaturaAppIoPromemoriaRicevutaTipoDefault());
		tipoVersamento.setAvvAppIoPromScadAbilitato(dto.isAvvisaturaAppIoPromemoriaScadenzaAbilitatoDefault());
		tipoVersamento.setAvvAppIoPromScadMessaggio(dto.getAvvisaturaAppIoPromemoriaScadenzaMessaggioDefault());
		tipoVersamento.setAvvAppIoPromScadOggetto(dto.getAvvisaturaAppIoPromemoriaScadenzaOggettoDefault());
		if(dto.getAvvisaturaAppIoPromemoriaScadenzaPreavvisoDefault() != null)
			tipoVersamento.setAvvAppIoPromScadPreavviso(dto.getAvvisaturaAppIoPromemoriaScadenzaPreavvisoDefault().intValue());
		tipoVersamento.setAvvAppIoPromScadTipo(dto.getAvvisaturaAppIoPromemoriaScadenzaTipoDefault());
		
		vo.setCodificaIuv(dto.getCodificaIuvCustom());
		if(dto.getTipoCustom() != null)
			vo.setTipo(dto.getTipoCustom().getCodifica());
		vo.setPagaTerzi(dto.getPagaTerziCustom());
		vo.setTipoVersamento(tipoVersamento);
		vo.setAbilitato(dto.getAbilitatoCustom());
		
		vo.setBoAbilitato(dto.getCaricamentoPendenzePortaleBackofficeAbilitatoCustom());
		vo.setBoCodApplicazione(dto.getCaricamentoPendenzePortaleBackofficeCodApplicazioneCustom());
		vo.setBoFormDefinizione(dto.getCaricamentoPendenzePortaleBackofficeFormDefinizioneCustom());
		vo.setBoFormTipo(dto.getCaricamentoPendenzePortaleBackofficeFormTipoCustom());
		vo.setBoTrasformazioneDef(dto.getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizioneCustom());
		vo.setBoTrasformazioneTipo(dto.getCaricamentoPendenzePortaleBackofficeTrasformazioneTipoCustom());
		vo.setBoValidazioneDef(dto.getCaricamentoPendenzePortaleBackofficeValidazioneDefinizioneCustom());
		vo.setPagAbilitato(dto.getCaricamentoPendenzePortalePagamentoAbilitatoCustom());
		vo.setPagCodApplicazione(dto.getCaricamentoPendenzePortalePagamentoCodApplicazioneCustom());
		vo.setPagFormDefinizione(dto.getCaricamentoPendenzePortalePagamentoFormDefinizioneCustom());
		vo.setPagFormImpaginazione(dto.getCaricamentoPendenzePortalePagamentoFormImpaginazioneCustom());
		vo.setPagFormTipo(dto.getCaricamentoPendenzePortalePagamentoFormTipoCustom());
		vo.setPagTrasformazioneDef(dto.getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizioneCustom());
		vo.setPagTrasformazioneTipo(dto.getCaricamentoPendenzePortalePagamentoTrasformazioneTipoCustom());
		vo.setPagValidazioneDef(dto.getCaricamentoPendenzePortalePagamentoValidazioneDefinizioneCustom()); 
		vo.setAvvMailPromAvvAbilitato(dto.getAvvisaturaMailPromemoriaAvvisoAbilitatoCustom());
		vo.setAvvMailPromAvvMessaggio(dto.getAvvisaturaMailPromemoriaAvvisoMessaggioCustom());
		vo.setAvvMailPromAvvOggetto(dto.getAvvisaturaMailPromemoriaAvvisoOggettoCustom());
		vo.setAvvMailPromAvvPdf(dto.getAvvisaturaMailPromemoriaAvvisoPdfCustom());
		vo.setAvvMailPromAvvTipo(dto.getAvvisaturaMailPromemoriaAvvisoTipoCustom());
		vo.setAvvMailPromRicAbilitato(dto.getAvvisaturaMailPromemoriaRicevutaAbilitatoCustom());
		vo.setAvvMailPromRicEseguiti(dto.getAvvisaturaMailPromemoriaRicevutaInviaSoloEseguitiCustom());
		vo.setAvvMailPromRicMessaggio(dto.getAvvisaturaMailPromemoriaRicevutaMessaggioCustom());
		vo.setAvvMailPromRicOggetto(dto.getAvvisaturaMailPromemoriaRicevutaOggettoCustom());
		vo.setAvvMailPromRicPdf(dto.getAvvisaturaMailPromemoriaRicevutaPdfCustom());
		vo.setAvvMailPromRicTipo(dto.getAvvisaturaMailPromemoriaRicevutaTipoCustom());
		vo.setAvvMailPromScadAbilitato(dto.getAvvisaturaMailPromemoriaScadenzaAbilitatoCustom());
		vo.setAvvMailPromScadMessaggio(dto.getAvvisaturaMailPromemoriaScadenzaMessaggioCustom());
		vo.setAvvMailPromScadOggetto(dto.getAvvisaturaMailPromemoriaScadenzaOggettoCustom());
		if(dto.getAvvisaturaMailPromemoriaScadenzaPreavvisoCustom() != null)
			vo.setAvvMailPromScadPreavviso(dto.getAvvisaturaMailPromemoriaScadenzaPreavvisoCustom().intValue());
		vo.setAvvMailPromScadTipo(dto.getAvvisaturaMailPromemoriaScadenzaTipoCustom());
		vo.setVisualizzazioneDefinizione(dto.getVisualizzazioneDefinizioneCustom());
		vo.setTracCsvTipo(dto.getTracciatoCsvTipoCustom());
		vo.setTracCsvHeaderRisposta(dto.getTracciatoCsvIntestazioneCustom());
		vo.setTracCsvTemplateRichiesta(dto.getTracciatoCsvRichiestaCustom());
		vo.setTracCsvTemplateRisposta(dto.getTracciatoCsvRispostaCustom());
		vo.setAppIoApiKey(dto.getAppIOAPIKey());
		vo.setAvvAppIoPromAvvAbilitato(dto.getAvvisaturaAppIoPromemoriaAvvisoAbilitatoCustom());
		vo.setAvvAppIoPromAvvMessaggio(dto.getAvvisaturaAppIoPromemoriaAvvisoMessaggioCustom());
		vo.setAvvAppIoPromAvvOggetto(dto.getAvvisaturaAppIoPromemoriaAvvisoOggettoCustom());
		vo.setAvvAppIoPromAvvTipo(dto.getAvvisaturaAppIoPromemoriaAvvisoTipoCustom());
		vo.setAvvAppIoPromRicAbilitato(dto.getAvvisaturaAppIoPromemoriaRicevutaAbilitatoCustom());
		vo.setAvvAppIoPromRicEseguiti(dto.getAvvisaturaAppIoPromemoriaRicevutaInviaSoloEseguitiCustom());
		vo.setAvvAppIoPromRicMessaggio(dto.getAvvisaturaAppIoPromemoriaRicevutaMessaggioCustom());
		vo.setAvvAppIoPromRicOggetto(dto.getAvvisaturaAppIoPromemoriaRicevutaOggettoCustom());
		vo.setAvvAppIoPromRicTipo(dto.getAvvisaturaAppIoPromemoriaRicevutaTipoCustom());
		vo.setAvvAppIoPromScadAbilitato(dto.getAvvisaturaAppIoPromemoriaScadenzaAbilitatoCustom());
		vo.setAvvAppIoPromScadMessaggio(dto.getAvvisaturaAppIoPromemoriaScadenzaMessaggioCustom());
		vo.setAvvAppIoPromScadOggetto(dto.getAvvisaturaAppIoPromemoriaScadenzaOggettoCustom());
		if(dto.getAvvisaturaAppIoPromemoriaScadenzaPreavvisoCustom() != null)
			vo.setAvvAppIoPromScadPreavviso(dto.getAvvisaturaAppIoPromemoriaScadenzaPreavvisoCustom().intValue());
		vo.setAvvAppIoPromScadTipo(dto.getAvvisaturaAppIoPromemoriaScadenzaTipoCustom());
		
		
		IdDominio idDominio = new IdDominio();
		idDominio.setId(dto.getIdDominio());
		vo.setIdDominio(idDominio);
		
		return vo;
	}
}
