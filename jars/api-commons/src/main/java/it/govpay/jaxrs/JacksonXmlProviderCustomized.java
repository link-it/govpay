package it.govpay.jaxrs;

import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonXmlProviderCustomized extends com.fasterxml.jackson.jakarta.rs.xml.JacksonXMLProvider {

	private static boolean failOnMissingExternalTypeIdProperty = false;
	public static boolean isFailOnMissingExternalTypeIdProperty() {
		return JacksonXmlProviderCustomized.failOnMissingExternalTypeIdProperty;
	}
	public static void setFailOnMissingExternalTypeIdProperty(boolean failOnMissingExternalTypeIdProperty) {
		JacksonXmlProviderCustomized.failOnMissingExternalTypeIdProperty = failOnMissingExternalTypeIdProperty;
	}

	public static XmlMapper getObjectMapper(boolean prettyPrint, TimeZone timeZone) {
		XmlMapper mapper = new XmlMapper();
		mapper.setTimeZone(timeZone);
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
		if (prettyPrint) {
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		}
		mapper.configure(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY, JacksonXmlProviderCustomized.failOnMissingExternalTypeIdProperty);
		return mapper;
	}
	
	public JacksonXmlProviderCustomized() {
		super(getObjectMapper(false, TimeZone.getDefault()));
	}
	public JacksonXmlProviderCustomized(boolean prettyPrint) {
		super(getObjectMapper(prettyPrint, TimeZone.getDefault()));
	}

	public JacksonXmlProviderCustomized(String timeZoneId) {
		super(getObjectMapper(false, TimeZone.getTimeZone(timeZoneId)));
	}
	public JacksonXmlProviderCustomized(String timeZoneId, boolean prettyPrint) {
		super(getObjectMapper(prettyPrint, TimeZone.getTimeZone(timeZoneId)));
	}

}
