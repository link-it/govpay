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
		dto.setFormDefinizioneCustom(vo.getFormDefinizione());
		dto.setFormTipoCustom(vo.getFormTipo());
		dto.setValidazioneDefinizioneCustom(vo.getValidazioneDefinizione());
		dto.setTrasformazioneDefinizioneCustom(vo.getTrasformazioneDefinizione());
		dto.setTrasformazioneTipoCustom(vo.getTrasformazioneTipo());
		dto.setCodApplicazioneCustom(vo.getCodApplicazione());
		dto.setPromemoriaAvvisoAbilitatoCustom(vo.getPromemoriaAvvisoAbilitato());
		dto.setPromemoriaAvvisoTipoCustom(vo.getPromemoriaAvvisoTipo());
		dto.setPromemoriaAvvisoPdfCustom(vo.getPromemoriaAvvisoPdf());
		dto.setPromemoriaAvvisoMessaggioCustom(vo.getPromemoriaAvvisoMessaggio());
		dto.setPromemoriaRicevutaAbilitatoCustom(vo.getPromemoriaRicevutaAbilitato());
		dto.setPromemoriaAvvisoOggettoCustom(vo.getPromemoriaAvvisoOggetto());
		dto.setPromemoriaRicevutaTipoCustom(vo.getPromemoriaRicevutaTipo());
		dto.setPromemoriaRicevutaPdfCustom(vo.getPromemoriaRicevutaPdf());
		dto.setPromemoriaRicevutaMessaggioCustom(vo.getPromemoriaRicevutaMessaggio());
		dto.setPromemoriaRicevutaOggettoCustom(vo.getPromemoriaRicevutaOggetto());
		dto.setVisualizzazioneDefinizioneCustom(vo.getVisualizzazioneDefinizione());
		dto.setTracciatoCsvTipoCustom(vo.getTracCsvTipo());
		dto.setTracciatoCsvIntestazioneCustom(vo.getTracCsvHeaderRisposta());
		dto.setTracciatoCsvRichiestaCustom(vo.getTracCsvTemplateRichiesta());
		dto.setTracciatoCsvRispostaCustom(vo.getTracCsvTemplateRisposta());
		
		dto.setCodTipoVersamento(vo.getTipoVersamento().getCodTipoVersamento());
		dto.setDescrizione(vo.getTipoVersamento().getDescrizione());
		dto.setCodificaIuvDefault(vo.getTipoVersamento().getCodificaIuv());
		if(vo.getTipoVersamento().getTipo() != null)
			dto.setTipoDefault(Tipo.toEnum(vo.getTipoVersamento().getTipo()));
		dto.setPagaTerziDefault(vo.getTipoVersamento().isPagaTerzi());
		dto.setAbilitatoDefault(vo.getTipoVersamento().isAbilitato());
		dto.setFormDefinizioneDefault(vo.getTipoVersamento().getFormDefinizione());
		dto.setFormTipoDefault(vo.getTipoVersamento().getFormTipo());
		dto.setValidazioneDefinizioneDefault(vo.getTipoVersamento().getValidazioneDefinizione());
		dto.setTrasformazioneDefinizioneDefault(vo.getTipoVersamento().getTrasformazioneDefinizione());
		dto.setTrasformazioneTipoDefault(vo.getTipoVersamento().getTrasformazioneTipo());
		dto.setCodApplicazioneDefault(vo.getTipoVersamento().getCodApplicazione());
		dto.setPromemoriaAvvisoAbilitatoDefault(vo.getTipoVersamento().getPromemoriaAvvisoAbilitato());
		dto.setPromemoriaAvvisoTipoDefault(vo.getTipoVersamento().getPromemoriaAvvisoTipo());
		dto.setPromemoriaAvvisoPdfDefault(vo.getTipoVersamento().getPromemoriaAvvisoPdf());
		dto.setPromemoriaAvvisoMessaggioDefault(vo.getTipoVersamento().getPromemoriaAvvisoMessaggio());
		dto.setPromemoriaAvvisoOggettoDefault(vo.getTipoVersamento().getPromemoriaAvvisoOggetto());
		dto.setPromemoriaRicevutaAbilitatoDefault(vo.getTipoVersamento().getPromemoriaRicevutaAbilitato());
		dto.setPromemoriaRicevutaTipoDefault(vo.getTipoVersamento().getPromemoriaRicevutaTipo());
		dto.setPromemoriaRicevutaPdfDefault(vo.getTipoVersamento().getPromemoriaRicevutaPdf());
		dto.setPromemoriaRicevutaMessaggioDefault(vo.getTipoVersamento().getPromemoriaRicevutaMessaggio());
		dto.setPromemoriaRicevutaOggettoDefault(vo.getTipoVersamento().getPromemoriaRicevutaOggetto());
		dto.setVisualizzazioneDefinizioneDefault(vo.getTipoVersamento().getVisualizzazioneDefinizione());
		dto.setTracciatoCsvTipoDefault(vo.getTipoVersamento().getTracCsvTipo());
		dto.setTracciatoCsvIntestazioneDefault(vo.getTipoVersamento().getTracCsvHeaderRisposta());
		dto.setTracciatoCsvRichiestaDefault(vo.getTipoVersamento().getTracCsvTemplateRichiesta());
		dto.setTracciatoCsvRispostaDefault(vo.getTipoVersamento().getTracCsvTemplateRisposta());
		
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
		tipoVersamento.setFormDefinizione(dto.getFormDefinizioneDefault());
		tipoVersamento.setFormTipo(dto.getFormTipoDefault());
		tipoVersamento.setValidazioneDefinizione(dto.getValidazioneDefinizioneDefault());
		tipoVersamento.setTrasformazioneDefinizione(dto.getTrasformazioneDefinizioneDefault());
		tipoVersamento.setTrasformazioneTipo(dto.getTrasformazioneTipoDefault());
		tipoVersamento.setCodApplicazione(dto.getCodApplicazioneDefault());
		tipoVersamento.setPromemoriaAvvisoAbilitato(dto.isPromemoriaAvvisoAbilitatoDefault());
		tipoVersamento.setPromemoriaAvvisoTipo(dto.getPromemoriaAvvisoTipoDefault());
		tipoVersamento.setPromemoriaAvvisoPdf(dto.getPromemoriaAvvisoPdfDefault());
		tipoVersamento.setPromemoriaAvvisoMessaggio(dto.getPromemoriaAvvisoMessaggioDefault());
		tipoVersamento.setPromemoriaAvvisoOggetto(dto.getPromemoriaAvvisoOggettoDefault());
		tipoVersamento.setPromemoriaRicevutaAbilitato(dto.isPromemoriaRicevutaAbilitatoDefault());
		tipoVersamento.setPromemoriaRicevutaTipo(dto.getPromemoriaRicevutaTipoDefault());
		tipoVersamento.setPromemoriaRicevutaPdf(dto.getPromemoriaRicevutaPdfDefault());
		tipoVersamento.setPromemoriaRicevutaMessaggio(dto.getPromemoriaRicevutaMessaggioDefault());
		tipoVersamento.setPromemoriaRicevutaOggetto(dto.getPromemoriaRicevutaOggettoDefault());
		tipoVersamento.setVisualizzazioneDefinizione(dto.getVisualizzazioneDefinizioneDefault());
		tipoVersamento.setTracCsvTipo(dto.getTracciatoCsvTipoDefault());
		tipoVersamento.setTracCsvHeaderRisposta(dto.getTracciatoCsvIntestazioneDefault());
		tipoVersamento.setTracCsvTemplateRichiesta(dto.getTracciatoCsvRichiestaDefault());
		tipoVersamento.setTracCsvTemplateRisposta(dto.getTracciatoCsvRispostaDefault());
		
		vo.setCodificaIuv(dto.getCodificaIuvCustom());
		if(dto.getTipoCustom() != null)
			vo.setTipo(dto.getTipoCustom().getCodifica());
		vo.setPagaTerzi(dto.getPagaTerziCustom());
		vo.setTipoVersamento(tipoVersamento);
		vo.setAbilitato(dto.getAbilitatoCustom());
		vo.setFormDefinizione(dto.getFormDefinizioneCustom());
		vo.setFormTipo(dto.getFormTipoCustom());
		vo.setValidazioneDefinizione(dto.getValidazioneDefinizioneCustom());
		vo.setTrasformazioneDefinizione(dto.getTrasformazioneDefinizioneCustom());
		vo.setTrasformazioneTipo(dto.getTrasformazioneTipoCustom());
		vo.setCodApplicazione(dto.getCodApplicazioneCustom());
		vo.setPromemoriaAvvisoAbilitato(dto.getPromemoriaAvvisoAbilitatoCustom());
		vo.setPromemoriaAvvisoTipo(dto.getPromemoriaAvvisoTipoCustom());
		vo.setPromemoriaAvvisoPdf(dto.getPromemoriaAvvisoPdfCustom());
		vo.setPromemoriaAvvisoMessaggio(dto.getPromemoriaAvvisoMessaggioCustom());
		vo.setPromemoriaAvvisoOggetto(dto.getPromemoriaAvvisoOggettoCustom());
		vo.setPromemoriaRicevutaAbilitato(dto.getPromemoriaRicevutaAbilitatoCustom());
		vo.setPromemoriaRicevutaTipo(dto.getPromemoriaRicevutaTipoCustom());
		vo.setPromemoriaRicevutaPdf(dto.getPromemoriaRicevutaPdfCustom());
		vo.setPromemoriaRicevutaMessaggio(dto.getPromemoriaRicevutaMessaggioCustom());
		vo.setPromemoriaRicevutaOggetto(dto.getPromemoriaRicevutaOggettoCustom());
		vo.setVisualizzazioneDefinizione(dto.getVisualizzazioneDefinizioneCustom());
		vo.setTracCsvTipo(dto.getTracciatoCsvTipoCustom());
		vo.setTracCsvHeaderRisposta(dto.getTracciatoCsvIntestazioneCustom());
		vo.setTracCsvTemplateRichiesta(dto.getTracciatoCsvRichiestaCustom());
		vo.setTracCsvTemplateRisposta(dto.getTracciatoCsvRispostaCustom());
		
		IdDominio idDominio = new IdDominio();
		idDominio.setId(dto.getIdDominio());
		vo.setIdDominio(idDominio);
		
		return vo;
	}
}
