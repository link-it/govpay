package it.govpay.core.utils.rawutils;

import java.io.IOException;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class LocalDateSerializer extends StdScalarSerializer<LocalDate> {

	private static final long serialVersionUID = 1L;

	public LocalDateSerializer() {
        super(LocalDate.class);
    }

    @Override
    public void serialize(LocalDate dateTime,
                          JsonGenerator jsonGenerator,
                          SerializerProvider provider) throws IOException, JsonGenerationException {
        String dateTimeAsString = DateFormatUtils.newSimpleDateFormatSoloData().format(dateTime.toDate());
        jsonGenerator.writeString(dateTimeAsString);
    }
}
