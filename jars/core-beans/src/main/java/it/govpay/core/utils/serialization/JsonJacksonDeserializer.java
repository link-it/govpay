/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils.serialization;


import java.io.InputStream;
import java.io.Reader;

import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/** 
 * Contiene utility per effettuare la de-serializzazione di un oggetto
 *
 * @author Poli Andrea (apoli@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */

public class JsonJacksonDeserializer implements IDeserializer{

	private ObjectMapper mapper;

	public JsonJacksonDeserializer() {
		this(new GovPaySerializationConfig());
	}
	
	public JsonJacksonDeserializer(GovPaySerializationConfig config) {
		BeanDeserializerModifier modifier = new BeanDeserializerModifierForIgnorables(config.getExcludes());
		DeserializerFactory dFactory = BeanDeserializerFactory.instance.withDeserializerModifier(modifier);

		this.mapper = new ObjectMapper(null, null, new DefaultDeserializationContext.Impl(dFactory));
		this.mapper.setDateFormat(config.getDf());
		if(config.isSerializeEnumAsString())
			this.mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
		if(config.isFailOnNumbersForEnums())
			this.mapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
		if(config.isEnableJSR310()) {
			this.mapper.registerModule(new JavaTimeModule());
		}

	}
	
	public JsonJacksonDeserializer(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public Object getObject(String s, Class<?> classType) throws IOException{
		try {
			return this.mapper.readValue(s, classType);
		} catch (java.io.IOException e) {
			throw new IOException(e);
		}
	}

	@Override
	public Object readObject(InputStream is, Class<?> classType) throws IOException {
		try {
			return this.mapper.readValue(is, classType);
		} catch (java.io.IOException e) {
			throw new IOException(e);
		}
	}

	@Override
	public Object readObject(Reader reader, Class<?> classType) throws IOException {
		try {
			return this.mapper.readValue(reader, classType);
		} catch (java.io.IOException e) {
			throw new IOException(e);
		}
	}
}
