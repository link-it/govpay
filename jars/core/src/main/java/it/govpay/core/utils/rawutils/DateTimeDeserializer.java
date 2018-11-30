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

public class DateTimeDeserializer extends StdScalarDeserializer<DateTime> {

	private static final long serialVersionUID = 1L;
	
	public DateTimeDeserializer() {
        super(LocalDate.class);
    }

    @Override
    public DateTime deserialize(JsonParser jsonParser,
                                DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            JsonToken currentToken = jsonParser.getCurrentToken();
            if (currentToken == JsonToken.VALUE_STRING) {
                String dateTimeAsString = jsonParser.getText().trim();
                return new DateTime(DateFormatUtils.newSimpleDateFormatNoMillis().parse(dateTimeAsString));
            } else {
            	return null;
            }
        } catch (ParseException e) {
        	throw new IOException(e);
        }
    }
}
