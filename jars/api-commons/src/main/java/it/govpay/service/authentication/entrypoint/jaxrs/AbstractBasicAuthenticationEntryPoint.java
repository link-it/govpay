package it.govpay.service.authentication.entrypoint.jaxrs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.openspcoop2.utils.Costanti;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.govpay.core.jaxrs.JacksonJsonProviderCustomized;

public abstract class AbstractBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
	
	private String realname = Costanti.OPENSPCOOP2;
	
	public String getRealname() {
		return this.realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	
	private TimeZone timeZone = TimeZone.getDefault();
	private String timeZoneId = null;
	public String getTimeZoneId() {
		return this.timeZoneId;
	}
	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
		this.timeZone = TimeZone.getTimeZone(timeZoneId);
	}
	
	public void fillResponse(AuthenticationException authException, HttpServletResponse httpResponse) {
		AbstractBasicAuthenticationEntryPoint.fillResponse(httpResponse, getPayload(authException, httpResponse), this.timeZone);
	}
	
	public static void fillResponse(HttpServletResponse httpResponse, Response response, TimeZone timeZone) {
		ByteArrayInputStream bais = null;
		ServletOutputStream outputStream = null;
		try{
			httpResponse.setStatus(response.getStatus());

			MultivaluedMap<String, Object> headers = response.getHeaders();
			if(!headers.isEmpty()) {
				Set<String> keySet = headers.keySet();

				for (String headerKey : keySet) {
					List<Object> list = headers.get(headerKey);
					if(!list.isEmpty()) {
						StringBuilder sb = new StringBuilder();
						for (Object object : list) {
							if(sb.length() > 0)
								sb.append(", ");

							sb.append(object);
						}
						httpResponse.setHeader(headerKey, sb.toString());
					}
				}
			}

			ObjectMapper mapper = JacksonJsonProviderCustomized.getObjectMapper(false, timeZone);
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
	
	protected abstract Response getPayload(AuthenticationException authException, HttpServletResponse httpResponse);

	protected abstract void addCustomHeaders(jakarta.servlet.http.HttpServletResponse httpResponse);
	
	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException)  {
		this.addCustomHeaders(response);
		this.fillResponse(authException, response);
	}

	@Override
	public void afterPropertiesSet() {
		setRealmName(this.realname);
		super.afterPropertiesSet();
	}
}