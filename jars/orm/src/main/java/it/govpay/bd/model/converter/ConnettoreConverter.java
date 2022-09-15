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
import java.util.Arrays;
import java.util.List;

import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Connettore;
import it.govpay.model.Versionabile.Versione;
import it.govpay.model.connettori.Header;
import it.govpay.model.exception.CodificaInesistenteException;

public class ConnettoreConverter {

	private static ObjectMapper mapper;
	static {
		mapper = new ObjectMapper();
		mapper.registerModule(new JaxbAnnotationModule());
		mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
	}

	public static Connettore toDTO(List<it.govpay.orm.Connettore> connettoreLst) throws CodificaInesistenteException, IOException {
		Connettore dto = new Connettore();
		if(connettoreLst != null && !connettoreLst.isEmpty()) {
			for(it.govpay.orm.Connettore connettore: connettoreLst){

				dto.setIdConnettore(connettore.getCodConnettore());
				if(Connettore.P_HTTPUSER_NAME.equals(connettore.getCodProprieta())) {
					dto.setHttpUser(connettore.getValore());
				}

				if(Connettore.P_HTTPPASSW_NAME.equals(connettore.getCodProprieta())) {
					dto.setHttpPassw(connettore.getValore());
				}

				if(Connettore.P_URL_NAME.equals(connettore.getCodProprieta())) {
					dto.setUrl(connettore.getValore());
				}

				if(Connettore.P_URL_SERVIZI_AVVISATURA_NAME.equals(connettore.getCodProprieta())) {
					dto.setUrlServiziAvvisatura(connettore.getValore());
				}

				if(Connettore.P_TIPOAUTENTICAZIONE_NAME.equals(connettore.getCodProprieta())) {
					dto.setTipoAutenticazione(Connettore.EnumAuthType.valueOf(connettore.getValore()));
				}

				if(Connettore.P_TIPOSSL_NAME.equals(connettore.getCodProprieta())) {
					dto.setTipoSsl(Connettore.EnumSslType.valueOf(connettore.getValore()));
				}

				if(Connettore.P_SSLKSLOCATION_NAME.equals(connettore.getCodProprieta())) {
					dto.setSslKsLocation(connettore.getValore());
				}

				if(Connettore.P_SSLKSPASS_WORD_NAME.equals(connettore.getCodProprieta())) {
					dto.setSslKsPasswd(connettore.getValore());
				}

				if(Connettore.P_SSLKSTYPE_NAME.equals(connettore.getCodProprieta())) {
					dto.setSslKsType(connettore.getValore());
				}

				if(Connettore.P_SSLTSLOCATION_NAME.equals(connettore.getCodProprieta())) {
					dto.setSslTsLocation(connettore.getValore());
				}

				if(Connettore.P_SSLTSPASS_WORD_NAME.equals(connettore.getCodProprieta())) {
					dto.setSslTsPasswd(connettore.getValore());
				}

				if(Connettore.P_SSLTSTYPE_NAME.equals(connettore.getCodProprieta())) {
					dto.setSslTsType(connettore.getValore());
				}

				if(Connettore.P_SSLPKEYPASS_WORD_NAME.equals(connettore.getCodProprieta())) {
					dto.setSslPKeyPasswd(connettore.getValore());
				}

				if(Connettore.P_SSLTYPE_NAME.equals(connettore.getCodProprieta())) {
					dto.setSslType(connettore.getValore());
				}

				if(Connettore.P_AZIONEINURL_NAME.equals(connettore.getCodProprieta())) {
					dto.setAzioneInUrl(Boolean.parseBoolean(connettore.getValore()));
				}

				if(Connettore.P_VERSIONE.equals(connettore.getCodProprieta())) {
					dto.setVersione(Versione.toEnum(connettore.getValore()));
				}

				if(Connettore.P_HEADERS.equals(connettore.getCodProprieta())) {
					try {
						SerializationConfig serializationConfig = new SerializationConfig();
						serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi());
						serializationConfig.setIgnoreNullValues(true);

						mapper.setDateFormat(serializationConfig.getDf());
						if(serializationConfig.isSerializeEnumAsString())
							mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);

						CollectionType var = mapper.getTypeFactory().constructCollectionType(List.class, Header.class);

						dto.setHeaders(mapper.readerFor(var).readValue(connettore.getValore()));
						dto.setVersione(Versione.toEnum(connettore.getValore()));
					} catch (JsonProcessingException e) {
						throw new IOException(e.getMessage(), e);
					}
				}
			}
		}
		return dto;
	}

	public static List<it.govpay.orm.Connettore> toVOList(Connettore connettore) throws IOException {
		List<it.govpay.orm.Connettore> voList = new ArrayList<>();

		if(connettore.getHttpUser() != null && !connettore.getHttpUser().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_HTTPUSER_NAME);
			vo.setValore(connettore.getHttpUser());
			voList.add(vo);
		}

		if(connettore.getHttpPassw() != null && !connettore.getHttpPassw().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_HTTPPASSW_NAME);
			vo.setValore(connettore.getHttpPassw());
			voList.add(vo);
		}

		if(connettore.getUrl() != null && !connettore.getUrl().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_URL_NAME);
			vo.setValore(connettore.getUrl());
			voList.add(vo);
		}

		if(connettore.getUrlServiziAvvisatura() != null && !connettore.getUrlServiziAvvisatura().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_URL_SERVIZI_AVVISATURA_NAME);
			vo.setValore(connettore.getUrlServiziAvvisatura());
			voList.add(vo);
		}

		if(connettore.getTipoAutenticazione() != null) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_TIPOAUTENTICAZIONE_NAME);
			vo.setValore(connettore.getTipoAutenticazione().toString());
			voList.add(vo);
		}

		if(connettore.getTipoSsl() != null) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_TIPOSSL_NAME);
			vo.setValore(connettore.getTipoSsl().toString());
			voList.add(vo);
		}

		if(connettore.getSslKsLocation() != null && !connettore.getSslKsLocation().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_SSLKSLOCATION_NAME);
			vo.setValore(connettore.getSslKsLocation());
			voList.add(vo);
		}

		if(connettore.getSslKsPasswd() != null && !connettore.getSslKsPasswd().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_SSLKSPASS_WORD_NAME);
			vo.setValore(connettore.getSslKsPasswd());
			voList.add(vo);
		}

		if(connettore.getSslKsType() != null && !connettore.getSslKsType().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_SSLKSTYPE_NAME);
			vo.setValore(connettore.getSslKsType());
			voList.add(vo);
		}

		if(connettore.getSslTsLocation() != null && !connettore.getSslTsLocation().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_SSLTSLOCATION_NAME);
			vo.setValore(connettore.getSslTsLocation());
			voList.add(vo);
		}

		if(connettore.getSslTsPasswd() != null && !connettore.getSslTsPasswd().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_SSLTSPASS_WORD_NAME);
			vo.setValore(connettore.getSslTsPasswd());
			voList.add(vo);
		}

		if(connettore.getSslTsType() != null && !connettore.getSslTsType().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_SSLTSTYPE_NAME);
			vo.setValore(connettore.getSslTsType());
			voList.add(vo);
		}

		if(connettore.getSslPKeyPasswd() != null && !connettore.getSslPKeyPasswd().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_SSLPKEYPASS_WORD_NAME);
			vo.setValore(connettore.getSslPKeyPasswd());
			voList.add(vo);
		}

		if(connettore.getSslType() != null && !connettore.getSslType().trim().isEmpty()) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_SSLTYPE_NAME);
			vo.setValore(connettore.getSslType());
			voList.add(vo);
		}

		if(connettore.getVersione() != null) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_VERSIONE);
			vo.setValore(connettore.getVersione().getApiLabel());
			voList.add(vo);
		}

		if(connettore.getHeaders() != null && connettore.getHeaders().size() > 0) {
			it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
			vo.setCodConnettore(connettore.getIdConnettore());
			vo.setCodProprieta(Connettore.P_HEADERS);

			try {
				SerializationConfig serializationConfig = new SerializationConfig();
				serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
				serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi());
				ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
				vo.setValore(serializer.getObject(connettore.getHeaders()));
			} catch(org.openspcoop2.utils.serialization.IOException e) {
				throw new IOException(e.getMessage(), e);
			}
			voList.add(vo);
		}

		it.govpay.orm.Connettore vo = new it.govpay.orm.Connettore();
		vo.setCodConnettore(connettore.getIdConnettore());
		vo.setCodProprieta(Connettore.P_AZIONEINURL_NAME);
		vo.setValore(Boolean.toString(connettore.isAzioneInUrl()));
		voList.add(vo);

		return voList;
	}

}
