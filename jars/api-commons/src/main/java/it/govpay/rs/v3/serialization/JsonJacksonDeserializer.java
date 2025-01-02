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
package it.govpay.rs.v3.serialization;

import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.SerializationConfig;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.rawutils.DateModule;
import it.govpay.core.utils.serialization.BeanDeserializerModifierForIgnorables;

public class JsonJacksonDeserializer {
	
	private JsonJacksonDeserializer () {}
	
	public static <T> T parse(String jsonString, Class<T> t) throws ValidationException  {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		serializationConfig.setIgnoreNullValues(true);
		return parse(jsonString, t, serializationConfig);
	}
	
	public static <T> T parse(String jsonString, Class<T> t, SerializationConfig serializationConfig) throws ValidationException  {
		try {
			BeanDeserializerModifier modifier = new BeanDeserializerModifierForIgnorables(serializationConfig.getExcludes());
			DeserializerFactory dFactory = BeanDeserializerFactory.instance.withDeserializerModifier(modifier);

			ObjectMapper mapper = new ObjectMapper(null, null, new DefaultDeserializationContext.Impl(dFactory));
			mapper.setDateFormat(serializationConfig.getDf());
			if(serializationConfig.isSerializeEnumAsString())
				mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);

			mapper = new ObjectMapper();
			mapper.registerModule(new JaxbAnnotationModule());
			mapper.registerModule(new DateModule());
			mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
			mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS,true);
						
			IDeserializer deserializer = new it.govpay.core.utils.serialization.JsonJacksonDeserializer(mapper);
			
			@SuppressWarnings("unchecked")
			T object = (T) deserializer.getObject(jsonString, t);
			return object;
		} catch(org.openspcoop2.utils.serialization.IOException e) {
			throw new ValidationException(e.getMessage(), e);
		}
	}
}
