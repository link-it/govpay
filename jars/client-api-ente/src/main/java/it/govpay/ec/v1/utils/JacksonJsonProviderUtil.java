package it.govpay.ec.v1.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.govpay.ec.v1.beans.Notifica;
import it.govpay.ec.v1.beans.PendenzaVerificata;

public class JacksonJsonProviderUtil {
	
	private static ObjectMapper mapper;
	
	static {
		mapper = new ObjectMapper();
	}

	public static String write(Notifica notifica) throws JsonProcessingException {
		return mapper.writeValueAsString(notifica);
	}
	
	public static PendenzaVerificata readPendenzaVerificata(String pendenzaVerificata) throws JsonParseException, JsonMappingException, IOException  {
		return mapper.readValue(pendenzaVerificata, PendenzaVerificata.class);
	}
}
