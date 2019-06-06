package it.govpay.core.utils.rawutils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class DateModule extends SimpleModule {

	private static final long serialVersionUID = 1L;

	public DateModule() {
        super();
        addSerializer(DateTime.class, new DateTimeSerializer());
        addSerializer(LocalDate.class, new LocalDateSerializer());
        addDeserializer(DateTime.class, new DateTimeDeserializer());
        addDeserializer(LocalDate.class, new LocalDateDeserializer());
    }
}

