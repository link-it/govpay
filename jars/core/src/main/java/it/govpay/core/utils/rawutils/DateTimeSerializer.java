package it.govpay.core.utils.rawutils;

import java.io.IOException;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class DateTimeSerializer extends StdScalarSerializer<DateTime> {

	private static final long serialVersionUID = 1L;

	public DateTimeSerializer() {
        super(DateTime.class);
    }

    @Override
    public void serialize(DateTime dateTime,
                          JsonGenerator jsonGenerator,
                          SerializerProvider provider) throws IOException, JsonGenerationException {
        String dateTimeAsString = DateFormatUtils.newSimpleDateFormatNoMillis().format(dateTime.toDate());
        jsonGenerator.writeString(dateTimeAsString);
    }
}
