package it.govpay.core.jaxrs;

import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonJsonProviderCustomized extends com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider {

	private static boolean failOnMissingExternalTypeIdProperty = false;
	public static boolean isFailOnMissingExternalTypeIdProperty() {
		return JacksonJsonProviderCustomized.failOnMissingExternalTypeIdProperty;
	}
	public static void setFailOnMissingExternalTypeIdProperty(boolean failOnMissingExternalTypeIdProperty) {
		JacksonJsonProviderCustomized.failOnMissingExternalTypeIdProperty = failOnMissingExternalTypeIdProperty;
	}

	public static ObjectMapper getObjectMapper(boolean prettyPrint, TimeZone timeZone) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setTimeZone(timeZone);
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS , false);
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		if( prettyPrint) {
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		}
		mapper.configure(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY, JacksonJsonProviderCustomized.failOnMissingExternalTypeIdProperty);
		return mapper;
	}
	
	public JacksonJsonProviderCustomized() {
		super(getObjectMapper(false, TimeZone.getDefault()));
	}
	public JacksonJsonProviderCustomized(boolean prettyPrint) {
		super(getObjectMapper(prettyPrint, TimeZone.getDefault()));
	}
	
	public JacksonJsonProviderCustomized(String timeZoneId) {
		super(getObjectMapper(false, TimeZone.getTimeZone(timeZoneId)));
	}
	public JacksonJsonProviderCustomized(String timeZoneId, boolean prettyPrint) {
		super(getObjectMapper(prettyPrint, TimeZone.getTimeZone(timeZoneId)));
	}

}
