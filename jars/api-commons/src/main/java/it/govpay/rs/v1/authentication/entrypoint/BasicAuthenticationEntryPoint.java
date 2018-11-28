package it.govpay.rs.v1.authentication.entrypoint;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.govpay.core.springsec.authentication.entrypoint.AbstractBasicAuthenticationEntryPoint;
import it.govpay.rs.v1.converter.JacksonJsonProvider;
import it.govpay.rs.v1.exception.CodiceEccezione;

public class BasicAuthenticationEntryPoint extends AbstractBasicAuthenticationEntryPoint {

	@Override
	protected void fillResponse(AuthenticationException authException, HttpServletResponse httpResponse) {
		ByteArrayInputStream bais = null;
		ServletOutputStream outputStream = null;
		try{
			Response response = CodiceEccezione.AUTORIZZAZIONE.toFaultResponse(authException);
			httpResponse.setStatus(response.getStatus());

			
//			MultivaluedMap<String, Object> headers = response.getHeaders();
//			if(!headers.isEmpty()) {
//				Set<String> keySet = headers.keySet();
//
//				for (String headerKey : keySet) {
//					List<Object> list = headers.get(headerKey);
//					if(!list.isEmpty()) {
//						StringBuilder sb = new StringBuilder();
//						for (Object object : list) {
//							if(sb.length() > 0)
//								sb.append(", ");
//
//							sb.append(object);
//						}
//						httpResponse.setHeader(headerKey, sb.toString());
//					}
//				}
//			}

			ObjectMapper mapper = JacksonJsonProvider.getObjectMapper();
			String fault = mapper.writeValueAsString(response.getEntity());
			bais = new ByteArrayInputStream(fault.getBytes());

			outputStream = httpResponse.getOutputStream();

			IOUtils.copy(bais, outputStream);

			outputStream.flush();
		}catch(Exception e) {

		} finally {
			if(bais!= null) {
				try {
					bais.close();
				} catch (IOException e) {
				}
			}
		}
	}

	@Override
	protected void addCustomHeaders(HttpServletResponse httpResponse) {
		httpResponse.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
	}

}
