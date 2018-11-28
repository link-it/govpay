package it.govpay.rs.v1.converter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonJsonProvider extends com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider {

    public static ObjectMapper getObjectMapper() {
            ObjectMapper mapper = new ObjectMapper();
           // mapper.registerModule(new DateTimeModule());
            mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.
                        WRITE_DATES_AS_TIMESTAMPS , false);
            return mapper;
    }

    public JacksonJsonProvider() {
            super(getObjectMapper());
    }
}
