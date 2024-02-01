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
package it.govpay.pendenze.v2.beans.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openspcoop2.utils.serialization.SerializationConfig;

import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.pendenze.v2.beans.Contabilita;
import it.govpay.pendenze.v2.beans.QuotaContabilita;

public class ContabilitaConverter {

	public static Contabilita toRsModel(String contabilitaJson) throws IOException {
		if(contabilitaJson == null)
			return null;
		
		it.govpay.model.Contabilita dto = ConverterUtils.parse(contabilitaJson, it.govpay.model.Contabilita.class);
		
		return toRsModel(dto);
	}
	
	
	public static List<QuotaContabilita> toRsModel(List<it.govpay.model.QuotaContabilita> dto) {
		if(dto != null) {
			List<QuotaContabilita> rsModel = new ArrayList<QuotaContabilita>();
			for (it.govpay.model.QuotaContabilita contabilita : dto) {
				rsModel.add(toRsModel(contabilita));
			}
			
			return rsModel;
		}
		
		return null;
	}
	
	public static QuotaContabilita toRsModel(it.govpay.model.QuotaContabilita dto) {
		QuotaContabilita rsModel = new QuotaContabilita();
		
		rsModel.setAccertamento(dto.getAccertamento());
		rsModel.setAnnoEsercizio(new BigDecimal(dto.getAnnoEsercizio()));
		rsModel.setCapitolo(dto.getCapitolo());
		rsModel.setImporto(dto.getImporto());
		rsModel.setProprietaCustom(dto.getProprietaCustom());
		rsModel.setTitolo(dto.getTitolo());
		rsModel.setTipologia(dto.getTipologia());
		rsModel.setCategoria(dto.getCategoria());
		rsModel.setArticolo(dto.getArticolo());
		
		return rsModel;
	}

	public static Contabilita toRsModel(it.govpay.model.Contabilita dto) {
		Contabilita rsModel = new Contabilita();
		
		rsModel.setQuote(toRsModel(dto.getQuote()));
		rsModel.setProprietaCustom(dto.getProprietaCustom());
		
		return rsModel;
	}
	
	
	public static String toStringDTO(Contabilita contabilita) throws IOException {
		if(contabilita == null)
			return null;
		
		it.govpay.model.Contabilita dto = toDTO(contabilita);
		
		return getDettaglioAsString(dto);
	}
	
	
	public static List<it.govpay.model.QuotaContabilita> toDTO(List<QuotaContabilita> dto) {
		if(dto != null) {
			List<it.govpay.model.QuotaContabilita> rsModel = new ArrayList<it.govpay.model.QuotaContabilita>();
			for (QuotaContabilita contabilita : dto) {
				rsModel.add(toDTO(contabilita));
			}
			
			return rsModel;
		}
		
		return null;
	}

	public static it.govpay.model.Contabilita toDTO(Contabilita dto) {
		it.govpay.model.Contabilita rsModel = new it.govpay.model.Contabilita();
		
		rsModel.setQuote(toDTO(dto.getQuote()));
		rsModel.setProprietaCustom(dto.getProprietaCustom());
		
		
		return rsModel;
	}
	
	public static it.govpay.model.QuotaContabilita toDTO(QuotaContabilita dto) {
		it.govpay.model.QuotaContabilita rsModel = new it.govpay.model.QuotaContabilita();
		
		rsModel.setAccertamento(dto.getAccertamento());
		rsModel.setAnnoEsercizio(dto.getAnnoEsercizio().intValue());
		rsModel.setCapitolo(dto.getCapitolo());
		rsModel.setImporto(dto.getImporto());
		rsModel.setProprietaCustom(dto.getProprietaCustom());
		rsModel.setTitolo(dto.getTitolo());
		rsModel.setTipologia(dto.getTipologia());
		rsModel.setCategoria(dto.getCategoria());
		rsModel.setArticolo(dto.getArticolo());
		
		return rsModel;
	}
	
	private static String getDettaglioAsString(Object obj) throws IOException {
		if(obj != null) {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
			serializationConfig.setIgnoreNullValues(true);
			return ConverterUtils.toJSON(obj, null, serializationConfig);
		}
		return null;
	}
}
