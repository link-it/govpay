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
package it.govpay.core.utils.rawutils;

import java.io.IOException;
import java.text.ParseException;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

import it.govpay.core.utils.SimpleDateFormatUtils;

public class LocalDateDeserializer extends StdScalarDeserializer<LocalDate> {

	private static final long serialVersionUID = 1L;
	
	public LocalDateDeserializer() {
        super(DateTime.class);
    }

    @Override
    public LocalDate deserialize(JsonParser jsonParser,
                                DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            JsonToken currentToken = jsonParser.getCurrentToken();
            if (currentToken == JsonToken.VALUE_STRING) {
                String dateTimeAsString = jsonParser.getText().trim();
                return new LocalDate(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi().parse(dateTimeAsString));
            } else {
            	return null;
            }
        } catch (ParseException e) {
        	throw new IOException(e);
        }
    }
}
